package com.github.louism33.chesscore;

import org.junit.Assert;

class MoveMakingUtilities {

    static void removePieces(long[][] pieces, int[] pieceSquareTable, long sourceSquare, long victimPieceSquare, int move){
        int movingPiece = MoveParser.getMovingPieceInt(move);

        Assert.assertTrue(movingPiece > 0);
        Assert.assertTrue(movingPiece < 13);

        togglePiecesFrom(pieces, pieceSquareTable, sourceSquare, movingPiece);

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

        togglePiecesFrom(pieces, pieceSquareTable, victimPieceSquare, victimPiece);
    }


    static void togglePiecesFrom (long[][] pieces, int[] pieceSquareTable, long removeThis, int thesePieces){
        // remove
        if ((pieces[thesePieces / 7][thesePieces < 7 ? thesePieces : thesePieces - 6] & removeThis) != 0) {
            pieceSquareTable[BitOperations.getIndexOfFirstPiece(removeThis)] = 0;
            pieces[thesePieces / 7][thesePieces < 7 ? thesePieces : thesePieces - 6] ^= removeThis;
        }
        else {
            pieceSquareTable[BitOperations.getIndexOfFirstPiece(removeThis)] = thesePieces;
            pieces[thesePieces / 7][thesePieces < 7 ? thesePieces : thesePieces - 6] |= removeThis;
        }
    }

}
