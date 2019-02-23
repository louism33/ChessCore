package com.github.louism33.chesscore;

import org.junit.Assert;

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
    }
    
    static void addPieceTo(Chessboard board, long addThis, int toThese){
        board.pieces[toThese / 7][toThese < 7 ? toThese : toThese - 6] ^= addThis;
    }

}
