package com.github.louism33.chesscore;

import static com.github.louism33.chesscore.BitOperations.*;
import static com.github.louism33.chesscore.BoardConstants.ALL_COLOUR_PIECES;
import static com.github.louism33.chesscore.BoardConstants.NO_PIECE;
import static com.github.louism33.chesscore.MoveConstants.*;
import static com.github.louism33.chesscore.MoveParser.moveFromSourceDestinationCaptureBetter;
import static com.github.louism33.chesscore.MoveParser.numberOfRealMoves;
import static com.github.louism33.chesscore.Piece.pieceOnSquareInt;

class MoveAdder {

    public static void addMovesFromAttackTableMasterBetter(int[] moves, long attackBoard, int source, 
                                                           int sourcePiece, Chessboard board) {
        
        int index = numberOfRealMoves(moves);

        long enemyPieces = board.pieces[1 - board.turn][ALL_COLOUR_PIECES];

        while (attackBoard != 0){
            final long destination = getFirstPiece(attackBoard);
            int destinationIndex = getIndexOfFirstPiece(destination);

            int victimPiece = ((destination & enemyPieces) != 0) ? whichPieceOnSquare(board, destinationIndex) : NO_PIECE;
            
            moves[index] = moveFromSourceDestinationCaptureBetter(source, sourcePiece, 
                    getIndexOfFirstPiece(destination), victimPiece);

            index++;

            attackBoard &= attackBoard - 1;
        }
    }

    public static void addMovesFromAttackTableMasterPromotion(Chessboard board, int[] moves, long attackBoard,
                                                              int source, int movingPiece, long enemyPieces) {
        while (attackBoard != 0){
            int index = numberOfRealMoves(moves);
            
            final long destination = getFirstPiece(attackBoard);

            int destinationIndex = getIndexOfFirstPiece(destination);

            int victimPiece = ((destination & enemyPieces) != 0) ? whichPieceOnSquare(board, destinationIndex) : NO_PIECE;
            
            final int move = moveFromSourceDestinationCaptureBetter(source, movingPiece, 
                    getIndexOfFirstPiece(destination), victimPiece) | PROMOTION_MASK;
            
            moves[index] = move | KNIGHT_PROMOTION_MASK;
            moves[index+1] = move | BISHOP_PROMOTION_MASK;
            moves[index+2] = move | ROOK_PROMOTION_MASK;
            moves[index+3] = move | QUEEN_PROMOTION_MASK;

            attackBoard &= attackBoard - 1;
        }
    }

    static int whichPieceOnSquare(Chessboard board, int destinationIndex) {
        return pieceOnSquareInt(board, newPieceOnSquare(destinationIndex));
    }
}