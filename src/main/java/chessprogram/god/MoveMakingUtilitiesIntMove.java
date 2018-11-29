package chessprogram.god;

class MoveMakingUtilitiesIntMove {

    public static void removePieces (Chessboard board, long sourceSquare, long destinationSquare){
        long mask = ~(sourceSquare | destinationSquare);
        board.setWhitePawns(board.getWhitePawns() & mask);
        board.setWhiteKnights(board.getWhiteKnights() & mask);
        board.setWhiteBishops(board.getWhiteBishops() & mask);
        board.setWhiteRooks(board.getWhiteRooks() & mask);
        board.setWhiteQueen(board.getWhiteQueen() & mask);
        board.setWhiteKing(board.getWhiteKing() & mask);
        board.setBlackPawns(board.getBlackPawns() & mask);
        board.setBlackKnights(board.getBlackKnights() & mask);
        board.setBlackBishops(board.getBlackBishops() & mask);
        board.setBlackRooks(board.getBlackRooks() & mask);
        board.setBlackQueen(board.getBlackQueen() & mask);
        board.setBlackKing(board.getBlackKing() & mask);
    }

}
