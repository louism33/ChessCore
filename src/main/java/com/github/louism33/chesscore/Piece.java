package com.github.louism33.chesscore;

import static com.github.louism33.chesscore.BoardConstants.*;

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
        if ((square & board.pieces[WHITE][PAWN]) != 0) return BoardConstants.WHITE_PAWN;
        if ((square & board.pieces[WHITE][KNIGHT]) != 0) return BoardConstants.WHITE_KNIGHT;
        if ((square & board.pieces[WHITE][BISHOP]) != 0) return BoardConstants.WHITE_BISHOP;
        if ((square & board.pieces[WHITE][ROOK]) != 0) return BoardConstants.WHITE_ROOK;
        if ((square & board.pieces[WHITE][QUEEN]) != 0) return BoardConstants.WHITE_QUEEN;
        if ((square & board.pieces[WHITE][KING]) != 0) return BoardConstants.WHITE_KING;

        if ((square & board.pieces[BLACK][PAWN]) != 0) return BoardConstants.BLACK_PAWN;
        if ((square & board.pieces[BLACK][KNIGHT]) != 0) return BoardConstants.BLACK_KNIGHT;
        if ((square & board.pieces[BLACK][BISHOP]) != 0)  return BoardConstants.BLACK_BISHOP;
        if ((square & board.pieces[BLACK][ROOK]) != 0) return BoardConstants.BLACK_ROOK;
        if ((square & board.pieces[BLACK][QUEEN]) != 0) return BoardConstants.BLACK_QUEEN;
        if ((square & board.pieces[BLACK][KING]) != 0) return BoardConstants.BLACK_KING;

        return BoardConstants.NO_PIECE;
    }
}
