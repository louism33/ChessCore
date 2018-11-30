package chessprogram.god;

import static chessprogram.god.BitOperations.getFirstPiece;
import static chessprogram.god.BitOperations.getIndexOfFirstPiece;
import static chessprogram.god.MoveGenerationUtilities.addMovesFromAttackTableMaster;
import static chessprogram.god.PieceMoveKnight.singleKnightTable;

class MoveGeneratorKnight {

    static void addKnightMoves(int[] moves, Chessboard board, boolean white,
                               long ignoreThesePieces, long mask, long myKnights){

        while (myKnights != 0){
            final long knight = getFirstPiece(myKnights);
            if ((knight & ignoreThesePieces) == 0) {
                long jumps = singleKnightTable(knight, mask);
                addMovesFromAttackTableMaster(moves, jumps, getIndexOfFirstPiece(knight), board);
            }
            myKnights &= (myKnights - 1);
        }
    }
}
