package com.github.louism33.chesscore;

import org.junit.Assert;

class MoveMakingUtilities {

    static void removePieces(long[][] pieces, long sourceSquare, long victimPieceSquare, int move){
        int movingPiece = MoveParser.getMovingPieceInt(move);

        Assert.assertTrue(movingPiece > 0);
        Assert.assertTrue(movingPiece < 13);
        
        togglePiecesFrom(pieces, sourceSquare, movingPiece);

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
        
        togglePiecesFrom(pieces, victimPieceSquare, victimPiece);
    }


    static void togglePiecesFrom (long[][] pieces, long removeThis, int thesePieces){
        pieces[thesePieces / 7][thesePieces < 7 ? thesePieces : thesePieces - 6] ^= removeThis;
    }
    
    static void removePiecesFrom (Chessboard board, long removeThis, int thesePieces){
        board.pieces[thesePieces / 7][thesePieces < 7 ? thesePieces : thesePieces - 6] ^= removeThis;
    }
    
    static void addPieceTo(Chessboard board, long addThis, int toThese){
        board.pieces[toThese / 7][toThese < 7 ? toThese : toThese - 6] ^= addThis;
    }

}
