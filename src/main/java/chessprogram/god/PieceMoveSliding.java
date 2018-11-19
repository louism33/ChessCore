package chessprogram.god;

import java.util.List;

class PieceMoveSliding {

    public static long singleBishopPushes(Chessboard board, long piece, boolean white, long legalPushes){
        return singleBishopAllMoves(board, piece, white, legalPushes, 0);
    }

    public static long singleBishopCaptures(Chessboard board, long piece, boolean white, long legalCaptures){
        return singleBishopAllMoves(board, piece, white, 0, legalCaptures);
    }

    private static long singleBishopAllMoves(Chessboard board, long piece, boolean white, long legalPushes, long legalCaptures){
        long ALL_PIECES = board.whitePieces() | board.blackPieces(),
                NORTH_WEST = BitboardResources.FILE_A | BitboardResources.RANK_EIGHT,
                NORTH_EAST = BitboardResources.FILE_H | BitboardResources.RANK_EIGHT,
                SOUTH_WEST = BitboardResources.FILE_A | BitboardResources.RANK_ONE,
                SOUTH_EAST = BitboardResources.FILE_H | BitboardResources.RANK_ONE;

        long answer = 0;
        long temp = piece;

        while (true) {
            if ((temp & NORTH_WEST) != 0) break;
            temp <<= 9;
            answer |= temp;
            if ((temp & ALL_PIECES) != 0) break;
        }
        temp = piece;
        while (true) {
            if ((temp & NORTH_EAST) != 0) break;
            temp <<= 7;
            answer |= temp;
            if ((temp & ALL_PIECES) != 0) break;
        }
        temp = piece;
        while (true) {
            if ((temp & SOUTH_WEST) != 0) break;
            temp >>>= 7;
            answer |= temp;
            if ((temp & ALL_PIECES) != 0) break;
        }
        temp = piece;
        while (true) {
            if ((temp & SOUTH_EAST) != 0) break;
            temp >>>= 9;
            answer |= temp;
            if ((temp & ALL_PIECES) != 0) break;
        }
        return answer & (legalPushes | legalCaptures);
    }


    public static long singleRookPushes(Chessboard board, long piece, boolean white, long legalPushes){
        return singleRookAllMoves(board, piece, white, legalPushes, 0);
    }

    public static long singleRookCaptures(Chessboard board, long piece, boolean white, long legalCaptures){
        return singleRookAllMoves(board, piece, white, 0, legalCaptures);
    }
    
    private static long singleRookAllMoves(Chessboard board, long piece, boolean white, long legalPushes, long legalCaptures){
        long allPieces = board.whitePieces() | board.blackPieces();
        long answer = 0;
        long temp = piece;
        while (true) {
            if ((temp & BitboardResources.FILE_A) != 0) break;
            temp <<= 1;
            answer |= temp;
            if ((temp & allPieces) != 0) break;
        }
        temp = piece;
        while (true) {
            if ((temp & BitboardResources.FILE_H) != 0) break;
            temp >>>= 1;
            answer |= temp;
            if ((temp & allPieces) != 0) break;
        }
        temp = piece;
        while (true) {
            if ((temp & BitboardResources.RANK_EIGHT) != 0) break;
            temp <<= 8;
            answer |= temp;
            if ((temp & allPieces) != 0) break;
        }
        temp = piece;
        while (true) {
            if ((temp & BitboardResources.RANK_ONE) != 0) break;
            temp >>>= 8;
            answer |= temp;
            if ((temp & allPieces) != 0) break;
        }
        return answer & (legalPushes | legalCaptures);
    }

    public static long singleQueenPushes(Chessboard board, long piece, boolean white, long legalPushes){
        return singleQueenAllMoves(board, piece, white, legalPushes, 0);
    }

    public static long singleQueenCaptures(Chessboard board, long piece, boolean white, long legalCaptures){
        return singleQueenAllMoves(board, piece, white, 0, legalCaptures);
    }

    private static long singleQueenAllMoves(Chessboard board, long piece, boolean white, long legalPushes, long legalCaptures){
        return singleBishopAllMoves(board, piece, white, legalPushes, legalCaptures) | singleRookAllMoves(board, piece, white, legalPushes, legalCaptures);
    }

    static long masterAttackTableSliding(Chessboard board, boolean white,
                                                long ignoreThesePieces, long legalPushes, long legalCaptures){
        long ans = 0, bishops, rooks, queens;
        if (white){
            bishops = board.getWhiteBishops();
            rooks = board.getWhiteRooks();
            queens = board.getWhiteQueen();
        }
        else {
            bishops = board.getBlackBishops();
            rooks = board.getBlackRooks();
            queens = board.getBlackQueen();
        }

        List<Long> allBishops = BitOperations.getAllPieces(bishops, ignoreThesePieces);
        for (Long piece : allBishops){
            ans |= singleBishopPushes(board, piece, white, legalPushes);
            ans |= singleBishopCaptures(board, piece, white, legalCaptures);
        }

        List<Long> allRooks = BitOperations.getAllPieces(rooks, ignoreThesePieces);
        for (Long piece : allRooks){
            ans |= singleRookPushes(board, piece, white, legalPushes);
            ans |= singleRookCaptures(board, piece, white, legalCaptures);
        }

        List<Long> allQueens = BitOperations.getAllPieces(queens, ignoreThesePieces);
        for (Long piece : allQueens){
            ans |= singleQueenPushes(board, piece, white, legalPushes);
            ans |= singleQueenCaptures(board, piece, white, legalCaptures);
        }
        return ans;
    }
}
