package com.github.louism33.chesscore;

public enum Piece {
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
    BLACK_KING,
    
    NO_PIECE;

    public static Piece pieceOnSquare(Chessboard board, Square square){
        return pieceOnSquare(board, square.toBitboard());
    }
    
    public static Piece pieceOnSquare(Chessboard board, long square){
        if (square == 0){
            return NO_PIECE;
        }
        if ((square & board.getWhitePawns()) != 0) return WHITE_PAWN;
        if ((square & board.getWhiteKnights()) != 0) return WHITE_KNIGHT;
        if ((square & board.getWhiteBishops()) != 0) return WHITE_BISHOP;
        if ((square & board.getWhiteRooks()) != 0) return WHITE_ROOK;
        if ((square & board.getWhiteQueen()) != 0) return WHITE_QUEEN;
        if ((square & board.getWhiteKing()) != 0) return WHITE_KING;

        if ((square & board.getBlackPawns()) != 0) return BLACK_PAWN;
        if ((square & board.getBlackKnights()) != 0) return BLACK_KNIGHT;
        if ((square & board.getBlackBishops()) != 0)  return BLACK_BISHOP;
        if ((square & board.getBlackRooks()) != 0) return BLACK_ROOK;
        if ((square & board.getBlackQueen()) != 0) return BLACK_QUEEN;
        if ((square & board.getBlackKing()) != 0) return BLACK_KING;
        
        return NO_PIECE;
    }
}
