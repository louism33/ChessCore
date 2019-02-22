package com.github.louism33.chesscore;

import org.junit.Assert;

import static com.github.louism33.chesscore.BoardConstants.*;
import static com.github.louism33.chesscore.MoveConstants.*;

class MoveMakingUtilities {

    static void removePieces(Chessboard board, long sourceSquare, long victimPieceSquare, int move){
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


    static void removePiecesFrom (Chessboard board, long removeThis, int thesePieces){
//        board.pieces[thesePieces / 7][(thesePieces % 7) + 1] ^= removeThis;
        
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
    
    static void addPieceTo(Chessboard board, long addThis, int toThese){

//        board.pieces[toThese / 7][(toThese % 7) + 1] ^= addThis;
        
        switch (toThese) {
            case WHITE_PAWN:
                board.setWhitePawns(board.getWhitePawns() | addThis);
                break;
            case WHITE_KNIGHT:
                board.setWhiteKnights(board.getWhiteKnights() | addThis);
                break;
            case WHITE_BISHOP:
                board.setWhiteBishops(board.getWhiteBishops() | addThis);
                break;
            case WHITE_ROOK:
                board.setWhiteRooks(board.getWhiteRooks() | addThis);
                break;
            case WHITE_QUEEN:
                board.setWhiteQueen(board.getWhiteQueen() | addThis);
                break;
            case WHITE_KING:
                board.setWhiteKing(board.getWhiteKing() | addThis);
                break;

            case BLACK_PAWN:
                board.setBlackPawns(board.getBlackPawns() | addThis);
                break;
            case BLACK_KNIGHT:
                board.setBlackKnights(board.getBlackKnights() | addThis);
                break;
            case BLACK_BISHOP:
                board.setBlackBishops(board.getBlackBishops() | addThis);
                break;
            case BLACK_ROOK:
                board.setBlackRooks(board.getBlackRooks() | addThis);
                break;
            case BLACK_QUEEN:
                board.setBlackQueen(board.getBlackQueen() | addThis);
                break;
            case BLACK_KING:
                board.setBlackKing(board.getBlackKing() | addThis);
                break;
        }
    }

}
