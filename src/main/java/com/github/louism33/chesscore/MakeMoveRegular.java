package com.github.louism33.chesscore;

class MakeMoveRegular {

    static int whichIntPieceOnSquare(Chessboard board, long destinationPiece){
        return Piece.pieceOnSquareInt(board, destinationPiece);
    }

}
