package chessprogram.god;

import java.util.List;

import static chessprogram.god.BitOperations.*;
import static chessprogram.god.BitOperations.getAllPieces;
import static chessprogram.god.MoveConstantsPawnCapture.*;

class PieceMovePawns {

    static long multiPawnPushes(Chessboard board, long pawns, boolean white, long legalPushes) {
        long allPieces = board.whitePieces() | board.blackPieces();
        final long possiblePawnSquares = white ? pawns << 8 : pawns >>> 8;
        final long promotingRank = white ? BitboardResources.RANK_SEVEN : BitboardResources.RANK_TWO;
        return possiblePawnSquares & legalPushes & ~allPieces & ~promotingRank;
    }

    static long multiDoublePawnPushes(Chessboard board, long pawns, boolean white, long legalPushes) {
        long allPieces = board.whitePieces() | board.blackPieces();
        final long possiblePawnSquares = white ? pawns << 16 : pawns >>> 16;
        final long startingRank = white ? BitboardResources.RANK_TWO : BitboardResources.RANK_SEVEN;
//        return possiblePawnSquares & legalPushes & ~allPieces & startingRank;
    
        // todo, is it possible to bulk these ?
        throw new RuntimeException();
    }

    // todo
    static long singlePawnPushes(Chessboard board, long piece, boolean white, long legalPushes) {
        long allPieces = board.whitePieces() | board.blackPieces();
        long answer = 0;
        long temp = piece;

        // promotion moves are handled elsewhere
        if (white) {
            do {
                if ((temp & BitboardResources.RANK_EIGHT) != 0) break;
                temp <<= 8;
                if ((temp & allPieces) != 0) break;
                answer |= temp;
            } while (((temp & BitboardResources.RANK_THREE) != 0));
        }
        else {
            do {
                if ((temp & BitboardResources.RANK_ONE) != 0) break;
                temp >>>= 8;
                if ((temp & allPieces) != 0) break;
                answer |= temp;
            } while (((temp & BitboardResources.RANK_SIX) != 0));
        }
        return answer & legalPushes;
    }

    static long singlePawnCaptures(long piece, boolean white, long legalCaptures) {
        long answer = 0;

        if (piece == 0) {
            return 0;
        }
        if (white){
            int index = getIndexOfFirstPiece(piece);
            long l = PAWN_CAPTURE_TABLE_WHITE[index];
            answer |= l;
        }
        else{
            int index = getIndexOfFirstPiece(piece);
            long l = PAWN_CAPTURE_TABLE_BLACK[index];
            answer |= l;
        }

        return answer & legalCaptures;
    }

    static long masterPawnCapturesTable(Chessboard board, boolean white,
                                        long ignoreThesePieces, long legalCaptures){
        long ans = 0, pawns;
        if (white){
            pawns = board.getWhitePawns();
        }
        else {
            pawns = board.getBlackPawns();
        }
        List<Long> allPawns = getAllPieces(pawns, ignoreThesePieces);
        for (Long piece : allPawns){
            ans |= singlePawnCaptures(piece, white, legalCaptures);
        }
        return ans;
    }

}
