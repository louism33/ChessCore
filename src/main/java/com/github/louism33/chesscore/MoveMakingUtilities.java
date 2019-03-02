package com.github.louism33.chesscore;

import org.junit.Assert;

import static com.github.louism33.chesscore.BoardConstants.ALL_COLOUR_PIECES;
import static java.lang.Long.numberOfTrailingZeros;

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
        final int colour = thesePieces / 7;
        final int colourBlindPiece = thesePieces < 7 ? thesePieces : thesePieces - 6;
        if ((pieces[colour][colourBlindPiece] & removeThis) != 0) {
            pieceSquareTable[numberOfTrailingZeros(removeThis)] = 0;
            pieces[colour][colourBlindPiece] ^= removeThis;
            pieces[colour][ALL_COLOUR_PIECES] ^= removeThis;
        }
        else {
            pieceSquareTable[numberOfTrailingZeros(removeThis)] = thesePieces;
            pieces[colour][colourBlindPiece] |= removeThis;
            pieces[colour][ALL_COLOUR_PIECES] |= removeThis;
        }
    }

}
