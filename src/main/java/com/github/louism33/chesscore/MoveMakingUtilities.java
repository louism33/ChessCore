package com.github.louism33.chesscore;

import org.junit.Assert;

import static com.github.louism33.chesscore.MoveConstants.*;

class MoveMakingUtilities {

    static void removePiecesPrecise (Chessboard board, long sourceSquare, long victimPieceSquare, int move){
        int movingPiece = MoveParser.getMovingPieceInt(move);

        Assert.assertTrue(movingPiece > 0);
        Assert.assertTrue(movingPiece < 13);
        
        removePiecesFrom(board, sourceSquare, movingPiece);

        if (victimPieceSquare == 0) {
            return;
        }

        if (!MoveParser.isCaptureMove(move)) {
            return;
        }
        int victimPiece = MoveParser.getVictimPieceInt(move);
        if (victimPiece == 0) {
            return;
        }
        
        removePiecesFrom(board, victimPieceSquare, victimPiece);
    }


    public static void removePiecesFrom (Chessboard board, long removeThis, int thesePieces){
        switch (thesePieces) {
            case WHITE_PAWN_MASK :
                board.setWhitePawns(board.getWhitePawns() ^ removeThis);
                break;
            case WHITE_KNIGHT_MASK :
                board.setWhiteKnights(board.getWhiteKnights() ^ removeThis);
                break;
            case WHITE_BISHOP_MASK :
                board.setWhiteBishops(board.getWhiteBishops() ^ removeThis);
                break;
            case WHITE_ROOK_MASK :
                board.setWhiteRooks(board.getWhiteRooks() ^ removeThis);
                break;
            case WHITE_QUEEN_MASK :
                board.setWhiteQueen(board.getWhiteQueen() ^ removeThis);
                break;
            case WHITE_KING_MASK :
                board.setWhiteKing(board.getWhiteKing() ^ removeThis);
                break;

            case BLACK_PAWN_MASK :
                board.setBlackPawns(board.getBlackPawns() ^ removeThis);
                break;
            case BLACK_KNIGHT_MASK :
                board.setBlackKnights(board.getBlackKnights() ^ removeThis);
                break;
            case BLACK_BISHOP_MASK :
                board.setBlackBishops(board.getBlackBishops() ^ removeThis);
                break;
            case BLACK_ROOK_MASK :
                board.setBlackRooks(board.getBlackRooks() ^ removeThis);
                break;
            case BLACK_QUEEN_MASK :
                board.setBlackQueen(board.getBlackQueen() ^ removeThis);
                break;
            case BLACK_KING_MASK :
                board.setBlackKing(board.getBlackKing() ^ removeThis);
                break;
        }
    }

}
