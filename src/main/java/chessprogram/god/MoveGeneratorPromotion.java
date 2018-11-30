package chessprogram.god;

import static chessprogram.god.BitOperations.getFirstPiece;
import static chessprogram.god.BitboardResources.*;
import static chessprogram.god.MoveGenerationUtilities.addMovesFromAttackTableMasterPromotion;
import static chessprogram.god.PieceMovePawns.singlePawnCaptures;
import static chessprogram.god.PieceMovePawns.singlePawnPushes;

class MoveGeneratorPromotion {

    static void addPromotionMoves(int[] moves, Chessboard board, boolean white,
                                  long ignoreThesePieces, long legalPushes, long legalCaptures,
                                  long myPawns, long myKnights, long myBishops, long myRooks, long myQueens, long myKing,
                                  long enemyPawns, long enemyKnights, long enemyBishops, long enemyRooks, long enemyQueens, long enemyKing,
                                  long enemies, long friends, long allPieces){
        long penultimateRank;
        long finalRank;
        if (white) {
            penultimateRank = RANK_SEVEN;
            finalRank = RANK_EIGHT;
        }
        else {
            penultimateRank = RANK_TWO;
            finalRank = RANK_ONE;
        }

        long promotablePawns = myPawns & penultimateRank & ~ignoreThesePieces;

        while (promotablePawns != 0){
            final long pawn = getFirstPiece(promotablePawns);
            long pawnMoves = singlePawnPushes(board, pawn, board.isWhiteTurn(), (finalRank & legalPushes), allPieces)
                    | singlePawnCaptures(pawn, board.isWhiteTurn(), ((finalRank & enemies) & legalCaptures));

            if (pawnMoves != 0) {
                addMovesFromAttackTableMasterPromotion(moves, pawnMoves, BitOperations.getIndexOfFirstPiece(pawn), enemies);

            }
            promotablePawns &= promotablePawns - 1;
        }
    }

}
