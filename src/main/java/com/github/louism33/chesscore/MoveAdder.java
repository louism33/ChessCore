package com.github.louism33.chesscore;

import static com.github.louism33.chesscore.BitOperations.getFirstPiece;
import static com.github.louism33.chesscore.BitOperations.getIndexOfFirstPiece;
import static com.github.louism33.chesscore.MoveConstants.*;
import static com.github.louism33.chesscore.MoveParser.moveFromSourceDestinationCapture;
import static com.github.louism33.chesscore.MoveParser.numberOfRealMoves;

class MoveAdder {
    
    public static void addMovesFromAttackTableMaster(int[] moves, long attackBoard, int source, Chessboard board) {
        int index = numberOfRealMoves(moves);

        long enemyPieces = board.isWhiteTurn() ? board.blackPieces() : board.whitePieces();
        
        while (attackBoard != 0){
            final long destination = getFirstPiece(attackBoard);

            moves[index] = moveFromSourceDestinationCapture(board, source, getIndexOfFirstPiece(destination),
                    ((destination & enemyPieces) != 0));

            index++;

            attackBoard &= attackBoard - 1;
        }
    }

    public static void addMovesFromAttackTableMasterCastling(Chessboard board, int[] moves, int source, int destination) {

        int index = numberOfRealMoves(moves);

        moves[index] = MoveParser.makeSpecialMove(board, source, destination, true, false, false, false, false, false, false);
    }

    public static void addMovesFromAttackTableMasterPromotion(Chessboard board, int[] moves, long attackBoard, int source, long enemyPieces) {
        while (attackBoard != 0){
            int index = numberOfRealMoves(moves);
            
            final long destination = getFirstPiece(attackBoard);
            final boolean capture = (destination & enemyPieces) != 0;
            final int move = moveFromSourceDestinationCapture(board, source, getIndexOfFirstPiece(destination), capture) | PROMOTION_MASK;
            
            moves[index] = move | KNIGHT_PROMOTION_MASK;
            moves[index+1] = move | BISHOP_PROMOTION_MASK;
            moves[index+2] = move | ROOK_PROMOTION_MASK;
            moves[index+3] = move | QUEEN_PROMOTION_MASK;

            attackBoard &= attackBoard - 1;
        }
    }
    
}