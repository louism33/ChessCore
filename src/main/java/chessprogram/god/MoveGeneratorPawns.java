package chessprogram.god;

import static chessprogram.god.BitOperations.getFirstPiece;
import static chessprogram.god.BitOperations.getIndexOfFirstPiece;
import static chessprogram.god.MoveGenerationUtilities.addMovesFromAttackTableMaster;
import static chessprogram.god.PieceMovePawns.singlePawnCaptures;
import static chessprogram.god.PieceMovePawns.singlePawnPushes;

class MoveGeneratorPawns {

    static void addPawnPushes(int[] moves, Chessboard board, boolean white,
                              long ignoreThesePieces, long legalCaptures, long legalPushes,
                              long myPawns, long allPieces){
        while (myPawns != 0){
            long pawn = getFirstPiece(myPawns);
            if ((pawn & ignoreThesePieces) == 0){
                final long multi = singlePawnPushes(board, pawn, white, legalPushes, allPieces);
                final long pawnCaptures = singlePawnCaptures(pawn, white, legalCaptures);
                final int pawnIndex = getIndexOfFirstPiece(pawn);
                addMovesFromAttackTableMaster(moves, multi | pawnCaptures, pawnIndex, board);
            }
            myPawns &= myPawns - 1;
        }

    }

}
