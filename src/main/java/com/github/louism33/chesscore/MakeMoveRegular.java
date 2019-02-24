package com.github.louism33.chesscore;

import static com.github.louism33.chesscore.BitOperations.newPieceOnSquare;
import static com.github.louism33.chesscore.MoveMakingUtilities.removePieces;
import static com.github.louism33.chesscore.MoveMakingUtilities.togglePiecesFrom;
import static com.github.louism33.chesscore.MoveParser.getDestinationIndex;
import static com.github.louism33.chesscore.MoveParser.getSourceIndex;

class MakeMoveRegular {

    static int whichIntPieceOnSquare(Chessboard board, long destinationPiece){
        return Piece.pieceOnSquareInt(board, destinationPiece);
    }

    static void makeRegularMove(long[][] pieces, int move) {
        final long destinationPiece = newPieceOnSquare(getDestinationIndex(move));

        removePieces(pieces, newPieceOnSquare(getSourceIndex(move)), destinationPiece, move);

        togglePiecesFrom(pieces, destinationPiece, MoveParser.getMovingPieceInt(move));
    }
    
    static void makeRegularMove(Chessboard board, int move) {
        final long destinationPiece = newPieceOnSquare(getDestinationIndex(move));

        removePieces(board.pieces, newPieceOnSquare(getSourceIndex(move)), destinationPiece, move);

        togglePiecesFrom(board.pieces, destinationPiece, MoveParser.getMovingPieceInt(move));
    }

}
