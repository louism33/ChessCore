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
        long ans = 0, knights;
        if (white){
            knights = board.getWhiteKnights();
        }
        else {
            knights = board.getBlackKnights();
        }

        List<Long> allKnights = getAllPieces(knights, ignoreThesePieces);
        for (Long piece : allKnights){
            ans |= singleKnightTable(piece, legalPushes | legalCaptures);
        }

        return ans;
    }

}
