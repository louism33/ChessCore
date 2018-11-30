package chessprogram.god;

import java.util.List;

import static chessprogram.god.BitOperations.*;
import static chessprogram.god.BitboardResources.FILES;
import static chessprogram.god.CheckHelper.boardInCheck;
import static chessprogram.god.MoveConstants.ENPASSANT_MASK;
import static chessprogram.god.MoveGenerationUtilities.addMovesFromAttackTableMaster;
import static chessprogram.god.PieceMovePawns.singlePawnCaptures;
import static chessprogram.god.StackMoveData.SpecialMove.ENPASSANTVICTIM;

class MoveGeneratorEnPassant {

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
        
        while (enemyPawnsInPosition != 0){
            long enemyPawn = BitOperations.getFirstPiece(enemyPawnsInPosition);
            
            if ((enemyPawn & ignoreThesePieces) == 0){
                long takingSpot = white ? enemyPawn << 8 : enemyPawn >>> 8;
                long potentialTakingSpot = takingSpot & FILE;

                if ((potentialTakingSpot & board.allPieces()) != 0){
                    enemyPawnsInPosition &= enemyPawnsInPosition - 1;
                    continue;
                }

                if (((enemyPawn & legalCaptures) == 0) && ((potentialTakingSpot & legalPushes) == 0)) {
                    enemyPawnsInPosition &= enemyPawnsInPosition - 1;
                    continue;
                }
                enemyTakingSpots |= potentialTakingSpot;
            }
            enemyPawnsInPosition &= enemyPawnsInPosition - 1;
        }
        
        if (enemyTakingSpots == 0){
            return;
        }

        while (myPawnsInPosition != 0){
            final long pawn = getFirstPiece(myPawnsInPosition);
            if ((pawn & ignoreThesePieces) == 0) {
                long pawnEnPassantCapture = singlePawnCaptures(pawn, white, enemyTakingSpots);
                addMovesFromAttackTableMaster(temp, pawnEnPassantCapture, getIndexOfFirstPiece(pawn), board);
            }
            myPawnsInPosition &= myPawnsInPosition - 1;
        }

        int index = 0;
        while (moves[index] != 0){
            index++;
        }

        // remove moves that would leave us in check
        for (int i = 0; i < temp.length; i++) {
            int move = temp[i];

            if (move == 0){
                break;
            }
            move |= ENPASSANT_MASK;

            board.makeMoveAndFlipTurn(move);

            enemyPawns = board.isWhiteTurn() ? board.getWhitePawns() : board.getBlackPawns();
            
            boolean enPassantWouldLeadToCheck = boardInCheck(board, white, myKing,
                    enemyPawns, enemyKnights, enemyBishops, enemyRooks, enemyQueens, enemyKing,
                    enemies, friends, board.allPieces());

            board.unMakeMoveAndFlipTurn();

            if (enPassantWouldLeadToCheck) {
                continue;
            }
            moves[index + i] = move;
        }
    }

    private static long extractFileFromInt(int file){
        return FILES[8 - file];
    }

}
