package chessprogram.god;

import java.util.ArrayList;
import java.util.List;

import static chessprogram.god.StackMoveData.SpecialMove;

class MoveGeneratorEnPassant {

    public static List<Move> generateEnPassantMoves(Chessboard board, boolean white,
                                                    long ignoreThesePieces, long legalPushes, long legalCaptures) {
        List<Move> moves = new ArrayList<>();

        long myPawns = white ? board.getWhitePawns() : board.getBlackPawns();
        long enemyPawns = white ? board.getBlackPawns() : board.getWhitePawns();
        long enPassantTakingRank = white ? bBitBoardUtils.RANK_FIVE : bBitBoardUtils.RANK_FOUR;

        long myPawnsInPosition = myPawns & enPassantTakingRank;
        if (myPawnsInPosition == 0) {
            return new ArrayList<>();
        }

        long enemyPawnsInPosition = enemyPawns & enPassantTakingRank;
        if (enemyPawnsInPosition == 0) {
            return new ArrayList<>();
        }

        if (board.moveStack.size() < 1){
            return new ArrayList<>();
        }

        StackMoveData previousMove = board.moveStack.peek();
        if (previousMove.typeOfSpecialMove != SpecialMove.ENPASSANTVICTIM){
            return new ArrayList<>();
        }



        long FILE = extractFileFromInt(previousMove.enPassantFile);

        List<Long> allEnemyPawnsInPosition = dBitExtractor.getAllPieces(enemyPawnsInPosition, ignoreThesePieces);

        long enemyTakingSpots = 0;
        for (Long enemyPawn : allEnemyPawnsInPosition){
            long takingSpot = white ? enemyPawn << 8 : enemyPawn >>> 8;
            long potentialTakingSpot = takingSpot & FILE;

            if ((potentialTakingSpot & board.allPieces()) != 0){
                continue;
            }
            
            if (((enemyPawn & legalCaptures) == 0) && ((potentialTakingSpot & legalPushes) == 0)) {
                continue;
            }
            enemyTakingSpots |= potentialTakingSpot;
        }


        if (enemyTakingSpots == 0){
            return new ArrayList<>();
        }

        List<Long> allMyPawnsInPosition = dBitExtractor.getAllPieces(myPawnsInPosition, ignoreThesePieces);

        for (Long myPawn : allMyPawnsInPosition){
            int indexOfFirstPiece = dBitIndexing.getIndexOfFirstPiece(myPawn);
            long pawnEnPassantCapture = PieceMovePawns.singlePawnCaptures(board, myPawn, white, enemyTakingSpots);
            List<Move> epMoves = MoveGenerationUtilities.movesFromAttackBoard(pawnEnPassantCapture, indexOfFirstPiece);
            moves.addAll(epMoves);
        }

        List<Move> safeEPMoves = new ArrayList<>();
        // remove moves that would leave us in check
        for (Move move : moves){
            move.move |= Move.ENPASSANT_MASK;

            MoveOrganiser.makeMoveMaster(board, move);
            boolean enPassantWouldLeadToCheck = cCheckChecker.boardInCheck(board, white);
//            MoveUnmaker.unMakeMoveMaster(board);
            board.unMakeMoveAndFlipTurn();
            
            if (enPassantWouldLeadToCheck){
                continue;
            }
            safeEPMoves.add(move);
        }
        return safeEPMoves;
    }


    private static long extractFileFromInt(int file){
        if (file == 1){
            return bBitBoardUtils.FILE_A;
        }
        else if (file == 2){
            return bBitBoardUtils.FILE_B;
        }
        else if (file == 3){
            return bBitBoardUtils.FILE_C;
        }
        else if (file == 4){
            return bBitBoardUtils.FILE_D;
        }
        else if (file == 5){
            return bBitBoardUtils.FILE_E;
        }
        else if (file == 6){
            return bBitBoardUtils.FILE_F;
        }
        else if (file == 7){
            return bBitBoardUtils.FILE_G;
        }
        else if (file == 8){
            return bBitBoardUtils.FILE_H;
        }
        throw new RuntimeException("Incorrect File gotten from Stack.");
    }

}
