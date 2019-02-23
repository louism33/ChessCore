package com.github.louism33.chesscore;

import static com.github.louism33.chesscore.BitOperations.getFirstPiece;
import static com.github.louism33.chesscore.BitOperations.getIndexOfFirstPiece;
import static com.github.louism33.chesscore.MoveConstants.*;
import static com.github.louism33.chesscore.MoveParser.*;

class MoveAdder {

    public static void addMovesFromAttackTableMasterBetter(int[] moves, long attackBoard, int source, 
                                                           int sourcePiece, Chessboard board) {
        
        int index = numberOfRealMoves(moves);

        long enemyPieces = board.isWhiteTurn() ? board.blackPieces() : board.whitePieces();

        while (attackBoard != 0){
            final long destination = getFirstPiece(attackBoard);

            moves[index] = moveFromSourceDestinationCaptureBetter(board, source, sourcePiece, getIndexOfFirstPiece(destination),
                    ((destination & enemyPieces) != 0));

            index++;

            attackBoard &= attackBoard - 1;
        }
    }

    public static void addMovesFromAttackTableMasterPromotion(Chessboard board, int[] moves, long attackBoard,
                                                              int source, int movingPiece, long enemyPieces) {
        while (attackBoard != 0){
            int index = numberOfRealMoves(moves);
            
            final long destination = getFirstPiece(attackBoard);
            final boolean capture = (destination & enemyPieces) != 0;
            final int move = moveFromSourceDestinationCaptureBetter(board, source, movingPiece, 
                    getIndexOfFirstPiece(destination), capture) | PROMOTION_MASK;
            
            moves[index] = move | KNIGHT_PROMOTION_MASK;
            moves[index+1] = move | BISHOP_PROMOTION_MASK;
            moves[index+2] = move | ROOK_PROMOTION_MASK;
            moves[index+3] = move | QUEEN_PROMOTION_MASK;

            attackBoard &= attackBoard - 1;
        }
    }
    
}