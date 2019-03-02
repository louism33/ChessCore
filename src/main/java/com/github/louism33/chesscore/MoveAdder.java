package com.github.louism33.chesscore;

import org.junit.Assert;

import static com.github.louism33.chesscore.BitOperations.getFirstPiece;
import static com.github.louism33.chesscore.BitOperations.populationCount;
import static com.github.louism33.chesscore.MoveConstants.*;
import static com.github.louism33.chesscore.MoveParser.buildMove;
import static java.lang.Long.numberOfTrailingZeros;

class MoveAdder {

    static void addMovesFromAttackTableMasterBetter(int[] moves, long attackBoard, int source,
                                                    int sourcePiece, int[] pieceSquareTable) {

        final int numberOfMoves = populationCount(attackBoard);
        Assert.assertTrue(numberOfMoves > 0);
        final int startIndex = moves[moves.length - 1];
        int i = 0;
        while (attackBoard != 0){

            final int destinationIndex = numberOfTrailingZeros(attackBoard);
            
            moves[startIndex + i] = buildMove(source, sourcePiece,
                    destinationIndex, pieceSquareTable[destinationIndex]);
            i++;
            attackBoard &= attackBoard - 1;
        }
        moves[moves.length - 1] += numberOfMoves;
    }

    static void addMovesFromAttackTableMasterPromotion(int[] pieceSquareTable, int[] moves, long attackBoard,
                                                       int source, int movingPiece) {


        final int numberOfMoves = populationCount(attackBoard);
        Assert.assertTrue(numberOfMoves > 0);
        int startIndex = moves[moves.length - 1];
        int i = 0;
        while (attackBoard != 0){
            int index = startIndex + i;

            final long destination = getFirstPiece(attackBoard);

            int destinationIndex = numberOfTrailingZeros(destination);

            final int move = buildMove(source, movingPiece,
                    numberOfTrailingZeros(destination),
                    pieceSquareTable[destinationIndex]) | PROMOTION_MASK;

            moves[index] = move | KNIGHT_PROMOTION_MASK;
            moves[index+1] = move | BISHOP_PROMOTION_MASK;
            moves[index+2] = move | ROOK_PROMOTION_MASK;
            moves[index+3] = move | QUEEN_PROMOTION_MASK;

            attackBoard &= attackBoard - 1;
            
            i += 4;
        } 
        
        moves[moves.length - 1] += 4*numberOfMoves;
    }

}