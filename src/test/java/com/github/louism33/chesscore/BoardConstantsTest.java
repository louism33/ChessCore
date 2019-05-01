package com.github.louism33.chesscore;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import static com.github.louism33.chesscore.BitOperations.populationCount;
import static com.github.louism33.chesscore.BoardConstants.*;

class BoardConstantsTest {

    @Test
    void intialBoard(){

        for (int i = 0; i < 8; i++) {
            Assert.assertEquals(INITIAL_PIECE_SQUARES[i+8], WHITE_PAWN);
            Assert.assertEquals(INITIAL_PIECE_SQUARES[64-9-i], BLACK_PAWN);
        }

        Assert.assertEquals(INITIAL_PIECE_SQUARES[0], WHITE_ROOK);
        Assert.assertEquals(INITIAL_PIECE_SQUARES[1], WHITE_KNIGHT);
        Assert.assertEquals(INITIAL_PIECE_SQUARES[2], WHITE_BISHOP);
        Assert.assertEquals(INITIAL_PIECE_SQUARES[3], WHITE_KING);
        Assert.assertEquals(INITIAL_PIECE_SQUARES[4], WHITE_QUEEN);
        Assert.assertEquals(INITIAL_PIECE_SQUARES[5], WHITE_BISHOP);
        Assert.assertEquals(INITIAL_PIECE_SQUARES[6], WHITE_KNIGHT);
        Assert.assertEquals(INITIAL_PIECE_SQUARES[7], WHITE_ROOK);

        Assert.assertEquals(INITIAL_PIECE_SQUARES[56], BLACK_ROOK);
        Assert.assertEquals(INITIAL_PIECE_SQUARES[57], BLACK_KNIGHT);
        Assert.assertEquals(INITIAL_PIECE_SQUARES[58], BLACK_BISHOP);
        Assert.assertEquals(INITIAL_PIECE_SQUARES[59], BLACK_KING);
        Assert.assertEquals(INITIAL_PIECE_SQUARES[60], BLACK_QUEEN);
        Assert.assertEquals(INITIAL_PIECE_SQUARES[61], BLACK_BISHOP);
        Assert.assertEquals(INITIAL_PIECE_SQUARES[62], BLACK_KNIGHT);
        Assert.assertEquals(INITIAL_PIECE_SQUARES[63], BLACK_ROOK);
    }
    
    @Test
    void colourTest() {
        Assert.assertEquals(0, (WHITE_COLOURED_SQUARES & BLACK_COLOURED_SQUARES));
        Assert.assertEquals(32, populationCount(WHITE_COLOURED_SQUARES));
        Assert.assertEquals(32, populationCount(BLACK_COLOURED_SQUARES));
    }
    
    @Test
    void rotateTest() {
        Assert.assertEquals(0, distanceFromPivotBLTR(7));
        Assert.assertEquals(1, distanceFromPivotBLTR(6));
        Assert.assertEquals(2, distanceFromPivotBLTR(5));
        Assert.assertEquals(3, distanceFromPivotBLTR(4));
        Assert.assertEquals(4, distanceFromPivotBLTR(3));
        Assert.assertEquals(5, distanceFromPivotBLTR(2));
        Assert.assertEquals(6, distanceFromPivotBLTR(1));
        Assert.assertEquals(7, distanceFromPivotBLTR(0));

        Assert.assertEquals(6, distanceFromPivotBLTR(8));
        Assert.assertEquals(5, distanceFromPivotBLTR(9));
        Assert.assertEquals(4, distanceFromPivotBLTR(10));
        Assert.assertEquals(3, distanceFromPivotBLTR(11));
        Assert.assertEquals(2, distanceFromPivotBLTR(12));
        Assert.assertEquals(1, distanceFromPivotBLTR(13));
        Assert.assertEquals(0, distanceFromPivotBLTR(14));
        Assert.assertEquals(-1, distanceFromPivotBLTR(15));


        Assert.assertEquals(5, distanceFromPivotBLTR(16));
        Assert.assertEquals(4, distanceFromPivotBLTR(17));
        Assert.assertEquals(3, distanceFromPivotBLTR(18));
        Assert.assertEquals(2, distanceFromPivotBLTR(19));
        Assert.assertEquals(1, distanceFromPivotBLTR(20));
        Assert.assertEquals(0, distanceFromPivotBLTR(21));
        Assert.assertEquals(-1, distanceFromPivotBLTR(22));
        Assert.assertEquals(-2, distanceFromPivotBLTR(23));

        Assert.assertEquals(7, indexOfRotateBLTR(7));
        Assert.assertEquals(14, indexOfRotateBLTR(14));
        Assert.assertEquals(21, indexOfRotateBLTR(21));
        Assert.assertEquals(28, indexOfRotateBLTR(28));
        Assert.assertEquals(35, indexOfRotateBLTR(35));
        Assert.assertEquals(42, indexOfRotateBLTR(42));
        Assert.assertEquals(49, indexOfRotateBLTR(49));
        Assert.assertEquals(56, indexOfRotateBLTR(56));
        
        
        Assert.assertEquals(63, indexOfRotateBLTR(0));
        Assert.assertEquals(55, indexOfRotateBLTR(1));
        Assert.assertEquals(47, indexOfRotateBLTR(2));
        Assert.assertEquals(39, indexOfRotateBLTR(3));
        Assert.assertEquals(31, indexOfRotateBLTR(4));
        Assert.assertEquals(23, indexOfRotateBLTR(5));
        Assert.assertEquals(15, indexOfRotateBLTR(6));
        Assert.assertEquals(7, indexOfRotateBLTR(7));

        Assert.assertEquals(0, indexOfRotateBLTR(63));
        Assert.assertEquals(1, indexOfRotateBLTR(55));
        Assert.assertEquals(2, indexOfRotateBLTR(47));
        Assert.assertEquals(3, indexOfRotateBLTR(39));
        Assert.assertEquals(4, indexOfRotateBLTR(31));
        Assert.assertEquals(5, indexOfRotateBLTR(23));
        Assert.assertEquals(6, indexOfRotateBLTR(15));
        Assert.assertEquals(7, indexOfRotateBLTR(7));

        Assert.assertEquals(0, indexOfRotateTLBR(0));
        Assert.assertEquals(9, indexOfRotateTLBR(9));
        Assert.assertEquals(18, indexOfRotateTLBR(18));
        Assert.assertEquals(27, indexOfRotateTLBR(27));
        Assert.assertEquals(36, indexOfRotateTLBR(36));
        Assert.assertEquals(45, indexOfRotateTLBR(45));
        Assert.assertEquals(54, indexOfRotateTLBR(54));
        Assert.assertEquals(63, indexOfRotateTLBR(63));

        Assert.assertEquals(0, indexOfRotateTLBR(0));
        Assert.assertEquals(1, indexOfRotateTLBR(8));
        Assert.assertEquals(2, indexOfRotateTLBR(16));
        Assert.assertEquals(3, indexOfRotateTLBR(24));
        Assert.assertEquals(4, indexOfRotateTLBR(32));
        Assert.assertEquals(5, indexOfRotateTLBR(40));
        Assert.assertEquals(6, indexOfRotateTLBR(48));
        Assert.assertEquals(7, indexOfRotateTLBR(56));
    }
    
    
}
