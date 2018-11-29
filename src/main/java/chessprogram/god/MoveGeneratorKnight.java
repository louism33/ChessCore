package chessprogram.god;

import java.util.List;

import static chessprogram.god.BitOperations.*;
import static chessprogram.god.MoveGenerationUtilities.addMovesFromAttackTableMaster;
import static chessprogram.god.PieceMoveKnight.singleKnightTable;

class MoveGeneratorKnight {

    static void addKnightMoves(List<Integer> moves, Chessboard board, boolean white,
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
