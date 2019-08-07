package com.github.louism33.chesscore;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import static com.github.louism33.chesscore.BitOperations.populationCount;
import static com.github.louism33.chesscore.BoardConstants.BLACK;
import static com.github.louism33.chesscore.BoardConstants.ROOK;
import static com.github.louism33.chesscore.Utils.squareDirectlyAttackedBy;

public class UtilTest {

    
    @Test
    public void squareAttackedBy() {
        Chessboard board = new Chessboard();

        Assert.assertEquals(populationCount(squareDirectlyAttackedBy(board, 0)), 0);
        Assert.assertEquals(populationCount(squareDirectlyAttackedBy(board, 1)), 1);
        Assert.assertEquals(populationCount(squareDirectlyAttackedBy(board, 2)), 1);
        Assert.assertEquals(populationCount(squareDirectlyAttackedBy(board, 3)), 1);
        Assert.assertEquals(populationCount(squareDirectlyAttackedBy(board, 4)), 1);
        Assert.assertEquals(populationCount(squareDirectlyAttackedBy(board, 5)), 1);
        Assert.assertEquals(populationCount(squareDirectlyAttackedBy(board, 6)), 1);
        Assert.assertEquals(populationCount(squareDirectlyAttackedBy(board, 7)), 0);
        
        Assert.assertEquals(populationCount(squareDirectlyAttackedBy(board, 8)), 1);
        Assert.assertEquals(populationCount(squareDirectlyAttackedBy(board, 9)), 1);
        Assert.assertEquals(populationCount(squareDirectlyAttackedBy(board, 10)), 1);
        Assert.assertEquals(populationCount(squareDirectlyAttackedBy(board, 11)), 4);
        Assert.assertEquals(populationCount(squareDirectlyAttackedBy(board, 12)), 4);
        Assert.assertEquals(populationCount(squareDirectlyAttackedBy(board, 13)), 1);
        Assert.assertEquals(populationCount(squareDirectlyAttackedBy(board, 14)), 1);
        Assert.assertEquals(populationCount(squareDirectlyAttackedBy(board, 15)), 1);
        
        Assert.assertEquals(populationCount(squareDirectlyAttackedBy(board, 16)), 2);
        Assert.assertEquals(populationCount(squareDirectlyAttackedBy(board, 17)), 2);
        Assert.assertEquals(populationCount(squareDirectlyAttackedBy(board, 18)), 3);
        Assert.assertEquals(populationCount(squareDirectlyAttackedBy(board, 19)), 2);
        Assert.assertEquals(populationCount(squareDirectlyAttackedBy(board, 20)), 2);
        Assert.assertEquals(populationCount(squareDirectlyAttackedBy(board, 21)), 3);
        Assert.assertEquals(populationCount(squareDirectlyAttackedBy(board, 22)), 2);
        Assert.assertEquals(populationCount(squareDirectlyAttackedBy(board, 23)), 2);


        Assert.assertEquals(populationCount(squareDirectlyAttackedBy(board, 63 - 0)), 0);
        Assert.assertEquals(populationCount(squareDirectlyAttackedBy(board, 63 - 1)), 1);
        Assert.assertEquals(populationCount(squareDirectlyAttackedBy(board, 63 - 2)), 1);
        Assert.assertEquals(populationCount(squareDirectlyAttackedBy(board, 63 - 3)), 1);
        Assert.assertEquals(populationCount(squareDirectlyAttackedBy(board, 63 - 4)), 1);
        Assert.assertEquals(populationCount(squareDirectlyAttackedBy(board, 63 - 5)), 1);
        Assert.assertEquals(populationCount(squareDirectlyAttackedBy(board, 63 - 6)), 1);
        Assert.assertEquals(populationCount(squareDirectlyAttackedBy(board, 63 - 7)), 0);

        Assert.assertEquals(populationCount(squareDirectlyAttackedBy(board, 63 - 8)), 1);
        Assert.assertEquals(populationCount(squareDirectlyAttackedBy(board, 63 - 9)), 1);
        Assert.assertEquals(populationCount(squareDirectlyAttackedBy(board, 63 - 10)), 1);
        Assert.assertEquals(populationCount(squareDirectlyAttackedBy(board, 63 - 11)), 4);
        Assert.assertEquals(populationCount(squareDirectlyAttackedBy(board, 63 - 12)), 4);
        Assert.assertEquals(populationCount(squareDirectlyAttackedBy(board, 63 - 13)), 1);
        Assert.assertEquals(populationCount(squareDirectlyAttackedBy(board, 63 - 14)), 1);
        Assert.assertEquals(populationCount(squareDirectlyAttackedBy(board, 63 - 15)), 1);

        Assert.assertEquals(populationCount(squareDirectlyAttackedBy(board, 63 - 16)), 2);
        Assert.assertEquals(populationCount(squareDirectlyAttackedBy(board, 63 - 17)), 2);
        Assert.assertEquals(populationCount(squareDirectlyAttackedBy(board, 63 - 18)), 3);
        Assert.assertEquals(populationCount(squareDirectlyAttackedBy(board, 63 - 19)), 2);
        Assert.assertEquals(populationCount(squareDirectlyAttackedBy(board, 63 - 20)), 2);
        Assert.assertEquals(populationCount(squareDirectlyAttackedBy(board, 63 - 21)), 3);
        Assert.assertEquals(populationCount(squareDirectlyAttackedBy(board, 63 - 22)), 2);
        Assert.assertEquals(populationCount(squareDirectlyAttackedBy(board, 63 - 23)), 2);
    }
    
    
    @Test
    public void xraysToSquareTest() {
        Chessboard board = new Chessboard();
        int x = 16;
//        Assert.assertEquals(populationCount(xRayToSquare(board.pieces[WHITE][ALL_COLOUR_PIECES] | board.pieces[BLACK][ALL_COLOUR_PIECES], 16, newPieceOnSquare(8) | newPieceOnSquare(9))), 2);
        
    }

    @Test
    public void maxLowTest() {
        Chessboard board = new Chessboard("k7/8/8/3q4/4P3/5P2/8/K7 b KQkq -");
        board.generateLegalMoves();
        int sqIndex = 35-8;
        final long l = squareDirectlyAttackedBy(board, sqIndex);
        Assert.assertEquals(populationCount(l), 2);
    }
    
}
