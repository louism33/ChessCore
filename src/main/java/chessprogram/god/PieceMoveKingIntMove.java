package chessprogram.god;

import static chessprogram.god.BitOperations.getIndexOfFirstPiece;
import static chessprogram.god.MoveConstantsKing.KING_MOVE_TABLE;

class PieceMoveKingIntMove {

    static long singleKingTable(long piece, long mask){
        return KING_MOVE_TABLE[getIndexOfFirstPiece(piece)] & mask;
    }

    static long masterAttackTableKing(Chessboard board, boolean white,
                                      long ignoreThesePieces, long legalPushes, long legalCaptures, 
                                      long kings){

        long ans = 0;
        while (kings != 0) {
            final long king = BitOperations.getFirstPiece(kings);
            if ((king & ignoreThesePieces) == 0) {
                ans |= singleKingTable(king, legalPushes | legalCaptures);
            }
            kings &= kings - 1;
        }
        return ans;
    }

}
