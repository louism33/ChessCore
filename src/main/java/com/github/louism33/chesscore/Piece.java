package com.github.louism33.chesscore;

public enum Piece {
    NO_PIECE,
    
    WHITE_PAWN,
    WHITE_KNIGHT,
    WHITE_BISHOP,
    WHITE_ROOK,
    WHITE_QUEEN,
    WHITE_KING,

    BLACK_PAWN,
    BLACK_KNIGHT,
    BLACK_BISHOP,
    BLACK_ROOK,
    BLACK_QUEEN,
    BLACK_KING;

    public static int pieceOnSquareInt(Chessboard board, long square){
        if (square == 0){
            return BoardConstants.NO_PIECE;
        }
        if ((square & board.getWhitePawns()) != 0) return BoardConstants.WHITE_PAWN;
        if ((square & board.getWhiteKnights()) != 0) return BoardConstants.WHITE_KNIGHT;
        if ((square & board.getWhiteBishops()) != 0) return BoardConstants.WHITE_BISHOP;
        if ((square & board.getWhiteRooks()) != 0) return BoardConstants.WHITE_ROOK;
        if ((square & board.getWhiteQueen()) != 0) return BoardConstants.WHITE_QUEEN;
        if ((square & board.getWhiteKing()) != 0) return BoardConstants.WHITE_KING;

        if ((square & board.getBlackPawns()) != 0) return BoardConstants.BLACK_PAWN;
        if ((square & board.getBlackKnights()) != 0) return BoardConstants.BLACK_KNIGHT;
        if ((square & board.getBlackBishops()) != 0)  return BoardConstants.BLACK_BISHOP;
        if ((square & board.getBlackRooks()) != 0) return BoardConstants.BLACK_ROOK;
        if ((square & board.getBlackQueen()) != 0) return BoardConstants.BLACK_QUEEN;
        if ((square & board.getBlackKing()) != 0) return BoardConstants.BLACK_KING;

        return BoardConstants.NO_PIECE;
    }
}
