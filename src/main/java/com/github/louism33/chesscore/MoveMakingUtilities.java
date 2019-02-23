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
        board.pieces[thesePieces / 7][thesePieces < 7 ? thesePieces : thesePieces - 6] ^= removeThis;
//        whichPiece < 7 ? whichPiece : whichPiece - 6
        
        switch (thesePieces) {
            case WHITE_PAWN_MASK :
                board.setWhitePawns(board.pieces[WHITE][PAWN] ^ removeThis);
                break;
            case WHITE_KNIGHT_MASK :
                board.setWhiteKnights(board.pieces[WHITE][KNIGHT] ^ removeThis);
                break;
            case WHITE_BISHOP_MASK :
                board.setWhiteBishops(board.pieces[WHITE][BISHOP] ^ removeThis);
                break;
            case WHITE_ROOK_MASK :
                board.setWhiteRooks(board.pieces[WHITE][ROOK] ^ removeThis);
                break;
            case WHITE_QUEEN_MASK :
                board.setWhiteQueen(board.pieces[WHITE][QUEEN] ^ removeThis);
                break;
            case WHITE_KING_MASK :
                board.setWhiteKing(board.pieces[WHITE][KING] ^ removeThis);
                break;

            case BLACK_PAWN_MASK :
                board.setBlackPawns(board.pieces[BLACK][PAWN] ^ removeThis);
                break;
            case BLACK_KNIGHT_MASK :
                board.setBlackKnights(board.pieces[BLACK][KNIGHT] ^ removeThis);
                break;
            case BLACK_BISHOP_MASK :
                board.setBlackBishops(board.pieces[BLACK][BISHOP] ^ removeThis);
                break;
            case BLACK_ROOK_MASK :
                board.setBlackRooks(board.pieces[BLACK][ROOK] ^ removeThis);
                break;
            case BLACK_QUEEN_MASK :
                board.setBlackQueen(board.pieces[BLACK][QUEEN] ^ removeThis);
                break;
            case BLACK_KING_MASK :
                board.setBlackKing(board.pieces[BLACK][KING] ^ removeThis);
                break;
        }
    }
    
    static void addPieceTo(Chessboard board, long addThis, int toThese){

        board.pieces[toThese / 7][toThese < 7 ? toThese : toThese - 6] ^= addThis;
        
        switch (toThese) {
            case WHITE_PAWN:
                board.setWhitePawns(board.pieces[WHITE][PAWN] | addThis);
                break;
            case WHITE_KNIGHT:
                board.setWhiteKnights(board.pieces[WHITE][KNIGHT] | addThis);
                break;
            case WHITE_BISHOP:
                board.setWhiteBishops(board.pieces[WHITE][BISHOP] | addThis);
                break;
            case WHITE_ROOK:
                board.setWhiteRooks(board.pieces[WHITE][ROOK] | addThis);
                break;
            case WHITE_QUEEN:
                board.setWhiteQueen(board.pieces[WHITE][QUEEN] | addThis);
                break;
            case WHITE_KING:
                board.setWhiteKing(board.pieces[WHITE][KING] | addThis);
                break;

            case BLACK_PAWN:
                board.setBlackPawns(board.pieces[BLACK][PAWN] | addThis);
                break;
            case BLACK_KNIGHT:
                board.setBlackKnights(board.pieces[BLACK][KNIGHT] | addThis);
                break;
            case BLACK_BISHOP:
                board.setBlackBishops(board.pieces[BLACK][BISHOP] | addThis);
                break;
            case BLACK_ROOK:
                board.setBlackRooks(board.pieces[BLACK][ROOK] | addThis);
                break;
            case BLACK_QUEEN:
                board.setBlackQueen(board.pieces[BLACK][QUEEN] | addThis);
                break;
            case BLACK_KING:
                board.setBlackKing(board.pieces[BLACK][KING] | addThis);
                break;
        }
    }

}
