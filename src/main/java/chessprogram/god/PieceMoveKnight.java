package chessprogram.god;

import static chessprogram.god.BitOperations.getIndexOfFirstPiece;
import static chessprogram.god.MoveConstantsKnight.KNIGHT_MOVE_TABLE;

class PieceMoveKnight {

    static long singleKnightTable(long piece, long mask){
        return KNIGHT_MOVE_TABLE[getIndexOfFirstPiece(piece)] & mask;
    }

    static long masterAttackTableKnights(Chessboard board, boolean white,
                                         long ignoreThesePieces, long legalPushes, long legalCaptures, 
                                         long knights){
        long ans = 0;
        while (knights != 0) {
            final long knight = BitOperations.getFirstPiece(knights);
            if ((knight & ignoreThesePieces) == 0) {
                ans |= singleKnightTable(knight, legalPushes | legalCaptures);
            }
            knights &= knights - 1;
        }
        return ans;
    }

}
