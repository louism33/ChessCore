package com.github.louism33.chesscore;

import static com.github.louism33.chesscore.BitOperations.getFirstPiece;
import static com.github.louism33.chesscore.BitOperations.getIndexOfFirstPiece;
import static com.github.louism33.chesscore.MoveConstants.*;
import static com.github.louism33.chesscore.MoveParser.moveFromSourceDestinationCaptureBetter;
import static com.github.louism33.chesscore.MoveParser.numberOfRealMoves;

class MoveAdder {

    public static void addMovesFromAttackTableMasterBetter(int[] moves, long attackBoard, int source, 
                                                           int sourcePiece, int[] pieceSquareTable) {
        
        int index = numberOfRealMoves(moves);

        while (attackBoard != 0){
            final long destination = getFirstPiece(attackBoard);
            int destinationIndex = getIndexOfFirstPiece(destination);

            moves[index] = moveFromSourceDestinationCaptureBetter(source, sourcePiece, 
                    getIndexOfFirstPiece(destination), pieceSquareTable[destinationIndex]);

            index++;

            attackBoard &= attackBoard - 1;
        }
    }

    public static void addMovesFromAttackTableMasterPromotion(int[] pieceSquareTable, int[] moves, long attackBoard,
                                                              int source, int movingPiece) {
        while (attackBoard != 0){
            int index = numberOfRealMoves(moves);
            
            final long destination = getFirstPiece(attackBoard);

            int destinationIndex = getIndexOfFirstPiece(destination);

            final int move = moveFromSourceDestinationCaptureBetter(source, movingPiece, 
                    getIndexOfFirstPiece(destination),
                    pieceSquareTable[destinationIndex]) | PROMOTION_MASK;
            
            moves[index] = move | KNIGHT_PROMOTION_MASK;
            moves[index+1] = move | BISHOP_PROMOTION_MASK;
            moves[index+2] = move | ROOK_PROMOTION_MASK;
            moves[index+3] = move | QUEEN_PROMOTION_MASK;

            attackBoard &= attackBoard - 1;
        }
    }

}