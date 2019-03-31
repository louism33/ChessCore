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
}
