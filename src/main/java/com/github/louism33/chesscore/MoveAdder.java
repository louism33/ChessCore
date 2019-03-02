package com.github.louism33.chesscore;

import org.junit.Assert;

import static com.github.louism33.chesscore.BitOperations.*;
import static com.github.louism33.chesscore.MoveConstants.*;
import static com.github.louism33.chesscore.MoveParser.buildMove;
import static java.lang.Long.numberOfTrailingZeros;

class MoveAdder {

    static void addMovesFromAttackTableMasterBetter(int[] moves, long attackBoard, int source, 
                                                           int sourcePiece, int[] pieceSquareTable) {


//        int x = populationCount(attackBoard);
//        System.out.println(x);
//        if (x == 0) {
//            throw new RuntimeException();
//        }else {
//            System.out.println(x);
//        }
        
        while (attackBoard != 0){
            
            final long destination = getFirstPiece(attackBoard);

            moves[moves[moves.length - 1]] = buildMove(source, sourcePiece, 
                    getIndexOfFirstPiece(destination), pieceSquareTable[numberOfTrailingZeros(destination)]);

            moves[moves.length - 1]++;

            attackBoard &= attackBoard - 1;
        }
    }

    static void addMovesFromAttackTableMasterPromotion(int[] pieceSquareTable, int[] moves, long attackBoard,
                                                              int source, int movingPiece) {
        
        while (attackBoard != 0){
            int index = moves[moves.length - 1];

            Assert.assertEquals(index, moves[moves.length - 1]);
            
            final long destination = getFirstPiece(attackBoard);

            int destinationIndex = getIndexOfFirstPiece(destination);

            final int move = buildMove(source, movingPiece, 
                    getIndexOfFirstPiece(destination),
                    pieceSquareTable[destinationIndex]) | PROMOTION_MASK;
            
            moves[index] = move | KNIGHT_PROMOTION_MASK;
            moves[index+1] = move | BISHOP_PROMOTION_MASK;
            moves[index+2] = move | ROOK_PROMOTION_MASK;
            moves[index+3] = move | QUEEN_PROMOTION_MASK;

            moves[moves.length - 1] += 4;

            attackBoard &= attackBoard - 1;
        }
    }

}