package chessprogram.god;

import java.util.List;

import static chessprogram.god.BitOperations.*;
import static chessprogram.god.BitOperations.getAllPieces;
import static chessprogram.god.MoveConstantsKing.*;

class PieceMoveKing {

    static long singleKingTable(long piece, long mask){
        return KING_MOVE_TABLE[getIndexOfFirstPiece(piece)] & mask;
    }

    static long masterAttackTableKing(Chessboard board, boolean white,
                                             long ignoreThesePieces, long legalPushes, long legalCaptures){
        long ans = 0, king;
        if (white){
            king = board.getWhiteKing();
        }
        else {
            king = board.getBlackKing();
        }

        List<Long> allKings = getAllPieces(king, ignoreThesePieces);
        for (Long piece : allKings){
            ans |= singleKingTable(piece, legalPushes | legalCaptures);
        }
        return ans;
    }


}
