package chessprogram.god;

import static chessprogram.god.BitOperations.getIndexOfFirstPiece;
import static chessprogram.god.MoveConstantsKnight.KNIGHT_MOVE_TABLE;

class PieceMoveKnightIntMove {

    static long singleKnightTable(long piece, long mask){
        return KNIGHT_MOVE_TABLE[getIndexOfFirstPiece(piece)] & mask;
    }

    static long masterAttackTableKnights(ChessboardIntMove board, boolean white,
                                         long ignoreThesePieces, long legalPushes, long legalCaptures){
        long ans = 0, knights = white ? board.getWhiteKnights() : board.getBlackKnights();
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
