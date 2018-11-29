package chessprogram.god;

import static chessprogram.god.BitOperations.getIndexOfFirstPiece;
import static chessprogram.god.MoveConstantsPawnCapture.PAWN_CAPTURE_TABLE_BLACK;
import static chessprogram.god.MoveConstantsPawnCapture.PAWN_CAPTURE_TABLE_WHITE;

class PieceMovePawns {

    static long singlePawnPushes(Chessboard board, long pawns, boolean white, long legalPushes) {
        long allPieces = board.allPieces();
        final long possiblePawnSinglePushes = white ? pawns << 8 : pawns >>> 8;
        final long intermediateRank = white ? BitboardResources.RANK_THREE : BitboardResources.RANK_SIX;
        long possibleDoubles = (((possiblePawnSinglePushes & intermediateRank & ~allPieces) ));
        return (possiblePawnSinglePushes | (white ? possibleDoubles << 8 : possibleDoubles >>> 8))
                & legalPushes & ~allPieces;
    }

    static long singlePawnCaptures(long piece, boolean white, long legalCaptures) {
        return legalCaptures & (white
                ? PAWN_CAPTURE_TABLE_WHITE[getIndexOfFirstPiece(piece)]
                : PAWN_CAPTURE_TABLE_BLACK[getIndexOfFirstPiece(piece)]);
    }

    static long masterPawnCapturesTable(Chessboard board, boolean white,
                                        long ignoreThesePieces, long legalCaptures, long pawns){
        long ans = 0;
        while (pawns != 0){
            final long pawn = BitOperations.getFirstPiece(pawns);
            if ((pawn & ignoreThesePieces) == 0) {
                ans |= singlePawnCaptures(pawn, white, legalCaptures);
            }
            pawns &= pawns - 1;
        }
        return ans;
    }

}
