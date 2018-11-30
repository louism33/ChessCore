package chessprogram.god;

import java.util.List;

import static chessprogram.god.BitOperations.*;
import static chessprogram.god.CheckHelper.boardInCheck;
import static chessprogram.god.MoveConstants.ENPASSANT_MASK;
import static chessprogram.god.MoveGenerationUtilities.addMovesFromAttackTableMaster;
import static chessprogram.god.PieceMovePawns.singlePawnCaptures;
import static chessprogram.god.StackMoveData.SpecialMove.ENPASSANTVICTIM;

class MoveGeneratorEnPassant {

    static void addEnPassantMoves2(int[] moves, Chessboard board, boolean white,
                                  long ignoreThesePieces, long legalPushes, long legalCaptures,
                                  long myPawns, long myKnights, long myBishops, long myRooks, long myQueens, long myKing,
                                  long enemyPawns, long enemyKnights, long enemyBishops, long enemyRooks, long enemyQueens, long enemyKing,
                                  long enemies, long friends, long allPieces) {
        
    }
    
    static void addEnPassantMoves(int[] moves, Chessboard board, boolean white,
                                  long ignoreThesePieces, long legalPushes, long legalCaptures,
                                  long myPawns, long myKnights, long myBishops, long myRooks, long myQueens, long myKing,
                                  long enemyPawns, long enemyKnights, long enemyBishops, long enemyRooks, long enemyQueens, long enemyKing,
                                  long enemies, long friends, long allPieces) {
        
        int[] temp = new int[8];

        long enPassantTakingRank = white ? BitboardResources.RANK_FIVE : BitboardResources.RANK_FOUR;

        long myPawnsInPosition = myPawns & enPassantTakingRank;
        if (myPawnsInPosition == 0) {
            return;
        }

        long enemyPawnsInPosition = enemyPawns & enPassantTakingRank;
        if (enemyPawnsInPosition == 0) {
            return;
        }

        if (board.moveStack.size() < 1){
            return;
        }

        StackMoveData previousMove = board.moveStack.peek();
        if (previousMove.typeOfSpecialMove != ENPASSANTVICTIM){
            return;
        }

        long FILE = extractFileFromInt(previousMove.enPassantFile);

        List<Long> allEnemyPawnsInPosition = getAllPieces(enemyPawnsInPosition, ignoreThesePieces);

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
            return;
        }

        while (myPawnsInPosition != 0){
            final long pawn = getFirstPiece(myPawnsInPosition);
            if ((pawn & ignoreThesePieces) == 0) {
                long pawnEnPassantCapture = singlePawnCaptures(pawn, white, enemyTakingSpots);
                int[] epMoves = new int[8];
                addMovesFromAttackTableMaster(temp, pawnEnPassantCapture, getIndexOfFirstPiece(pawn), board);
            }
            myPawnsInPosition &= myPawnsInPosition - 1;
        }


        int[] safeEPMoves = new int[8];
        // remove moves that would leave us in check
        for (int i = 0; i < temp.length; i++) {
            int move = temp[i];
            
            if (move == 0){
                break;
            }
            move |= ENPASSANT_MASK;

            board.makeMoveAndFlipTurn(move);

            boolean enPassantWouldLeadToCheck = boardInCheck(board, white,
                    myPawns, myKnights, myBishops, myRooks, myQueens, myKing,
                    enemyPawns, enemyKnights, enemyBishops, enemyRooks, enemyQueens, enemyKing,
                    enemies, friends, allPieces);

            board.unMakeMoveAndFlipTurn();

            if (enPassantWouldLeadToCheck) {
                continue;
            }
            safeEPMoves[i] = move;
        }
        
        int index = 0;
        while (moves[index] != 0){
            index++;
        }
        
        for (int i = 0; i < safeEPMoves.length; i++){
            moves[index + i] = safeEPMoves[i];
        }
        
    }


    private static long extractFileFromInt(int file){
        if (file == 1){
            return BitboardResources.FILE_A;
        }
        else if (file == 2){
            return BitboardResources.FILE_B;
        }
        else if (file == 3){
            return BitboardResources.FILE_C;
        }
        else if (file == 4){
            return BitboardResources.FILE_D;
        }
        else if (file == 5){
            return BitboardResources.FILE_E;
        }
        else if (file == 6){
            return BitboardResources.FILE_F;
        }
        else if (file == 7){
            return BitboardResources.FILE_G;
        }
        else if (file == 8){
            return BitboardResources.FILE_H;
        }
        throw new RuntimeException("Incorrect File gotten from Stack.");
    }

}
