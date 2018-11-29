package chessprogram.god;

import java.util.List;

import static chessprogram.god.BitOperations.*;
import static chessprogram.god.BitOperations.getAllPieces;
import static chessprogram.god.MoveConstantsKnight.*;

class PieceMoveKnight {

    static long singleKnightTable(long piece, long mask){
        return KNIGHT_MOVE_TABLE[getIndexOfFirstPiece(piece)] & mask;
    }

    static long masterAttackTableKnights(Chessboard board, boolean white,
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
