package com.github.louism33.chesscore;

import org.junit.Assert;

import static com.github.louism33.chesscore.BitOperations.getFirstPiece;
import static com.github.louism33.chesscore.BitOperations.populationCount;
import static com.github.louism33.chesscore.MoveConstants.*;
import static com.github.louism33.chesscore.MoveParser.buildMove;
import static java.lang.Long.numberOfTrailingZeros;

public final class MoveAdder {

    static void addMovesFromAttackTableMaster(final int[] moves, long attackBoard, final int source,
                                              final int sourcePiece) {

        final int numberOfMoves = populationCount(attackBoard);
        
        Assert.assertTrue(numberOfMoves > 0);
        final int startIndex = moves[moves.length - 1];
        int i = 0;
        while (attackBoard != 0){
            final int destinationIndex = numberOfTrailingZeros(attackBoard);

            moves[startIndex + i] = buildMove(source, sourcePiece,
                    destinationIndex);
            i++;
            attackBoard &= attackBoard - 1;
        }
        moves[moves.length - 1] += numberOfMoves;
    }

    static void addMovesFromAttackTableMasterPromotion(final int[] pieceSquareTable, final int[] moves, long attackBoard,
                                                       final int source, final int movingPiece) {


        final int numberOfMoves = populationCount(attackBoard);
        Assert.assertTrue(numberOfMoves > 0);
        int startIndex = moves[moves.length - 1];
        int i = 0;
        while (attackBoard != 0){
            final int index = startIndex + i;

            final long destination = getFirstPiece(attackBoard);

            final int destinationIndex = numberOfTrailingZeros(destination);

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

    static void addMovesFromAttackTableMasterPromotion(final int[] moves, long attackBoard,
                                                       final int source, final int movingPiece) {


        final int numberOfMoves = populationCount(attackBoard);
        Assert.assertTrue(numberOfMoves > 0);
        int startIndex = moves[moves.length - 1];
        int i = 0;
        while (attackBoard != 0){
            final int index = startIndex + i;

            final long destination = getFirstPiece(attackBoard);

            final int move = buildMove(source, movingPiece,
                    numberOfTrailingZeros(destination)) | PROMOTION_MASK;

            moves[index] = move | KNIGHT_PROMOTION_MASK; // todo, change order, because queen is always more interesting. Q N R B
            moves[index+1] = move | BISHOP_PROMOTION_MASK;
            moves[index+2] = move | ROOK_PROMOTION_MASK;
            moves[index+3] = move | QUEEN_PROMOTION_MASK;

            attackBoard &= attackBoard - 1;

            i += 4;
        }

        moves[moves.length - 1] += 4*numberOfMoves;
    }

}