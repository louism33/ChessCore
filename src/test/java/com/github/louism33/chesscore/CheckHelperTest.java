package com.github.louism33.chesscore;

import com.github.louism33.utils.MoveParserFromAN;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

public class CheckHelperTest {

    @Test
    void repetitionTest1() {
        Chessboard board = new Chessboard();
        
        Assert.assertFalse(board.isDrawByRepetition());
        String m1 = "b1c3";
        board.makeMoveAndFlipTurn(MoveParserFromAN.buildMoveFromLAN(board, m1));

        Assert.assertFalse(board.isDrawByRepetition());
        String m2 = "g8f6";
        board.makeMoveAndFlipTurn(MoveParserFromAN.buildMoveFromLAN(board, m2));

        Assert.assertFalse(board.isDrawByRepetition());
        String m3 = "c3b1";
        board.makeMoveAndFlipTurn(MoveParserFromAN.buildMoveFromLAN(board, m3));

        Assert.assertFalse(board.isDrawByRepetition());
        String m4 = "f6g8";
        board.makeMoveAndFlipTurn(MoveParserFromAN.buildMoveFromLAN(board, m4));

        Assert.assertTrue(board.isDrawByRepetition());

    }

    @Test
    void repetitionTest2() {
        Chessboard board = new Chessboard();
        Chessboard init = new Chessboard();

        Assert.assertFalse(board.isDrawByRepetition());
        String m1 = "b1c3";
        board.makeMoveAndFlipTurn(MoveParserFromAN.buildMoveFromLAN(board, m1));

        Assert.assertFalse(board.isDrawByRepetition());
        String m2 = "g8f6";
        board.makeMoveAndFlipTurn(MoveParserFromAN.buildMoveFromLAN(board, m2));

        Assert.assertFalse(board.isDrawByRepetition());
        board.unMakeMoveAndFlipTurn();

        Assert.assertFalse(board.isDrawByRepetition());
        board.unMakeMoveAndFlipTurn();

        Assert.assertFalse(board.isDrawByRepetition());

        Assert.assertEquals(board, init);
    }


    @Test
    void repetitionTest3() {
        Chessboard board = new Chessboard();
        Chessboard init = new Chessboard();

        Assert.assertFalse(board.isDrawByRepetition());
        String m1 = "b1c3";
        board.makeMoveAndFlipTurn(MoveParserFromAN.buildMoveFromLAN(board, m1));

        Assert.assertFalse(board.isDrawByRepetition());
        String m2 = "g8f6";
        board.makeMoveAndFlipTurn(MoveParserFromAN.buildMoveFromLAN(board, m2));

        Assert.assertFalse(board.isDrawByRepetition());
        String m3 = "c3b1";
        board.makeMoveAndFlipTurn(MoveParserFromAN.buildMoveFromLAN(board, m3));

        Assert.assertFalse(board.isDrawByRepetition());
        String m4 = "f6g8";
        board.makeMoveAndFlipTurn(MoveParserFromAN.buildMoveFromLAN(board, m4));

        Assert.assertTrue(board.isDrawByRepetition());
        board.unMakeMoveAndFlipTurn();

        Assert.assertFalse(board.isDrawByRepetition());
        board.unMakeMoveAndFlipTurn();

        Assert.assertFalse(board.isDrawByRepetition());
        board.unMakeMoveAndFlipTurn();

        Assert.assertFalse(board.isDrawByRepetition());
        board.unMakeMoveAndFlipTurn();
        
        Assert.assertFalse(board.isDrawByRepetition());

        Assert.assertEquals(board, init);
    }

    @Test
    void repetitionTest4() {
        Chessboard board = new Chessboard();

        Assert.assertFalse(board.isDrawByRepetition());
        String m1 = "b1c3";
        board.makeMoveAndFlipTurn(MoveParserFromAN.buildMoveFromLAN(board, m1));

        Assert.assertFalse(board.isDrawByRepetition());
        String m2 = "g8f6";
        board.makeMoveAndFlipTurn(MoveParserFromAN.buildMoveFromLAN(board, m2));

        Assert.assertFalse(board.isDrawByRepetition());
        String m3 = "c3b1";
        board.makeMoveAndFlipTurn(MoveParserFromAN.buildMoveFromLAN(board, m3));

        Assert.assertFalse(board.isDrawByRepetition());
        String m4 = "f6g8";
        board.makeMoveAndFlipTurn(MoveParserFromAN.buildMoveFromLAN(board, m4));

        Assert.assertTrue(board.isDrawByRepetition());
        board.unMakeMoveAndFlipTurn();

        Assert.assertFalse(board.isDrawByRepetition());
        board.unMakeMoveAndFlipTurn();

        Assert.assertFalse(board.isDrawByRepetition());
        String m5 = "c3b1";
        board.makeMoveAndFlipTurn(MoveParserFromAN.buildMoveFromLAN(board, m5));

        Assert.assertFalse(board.isDrawByRepetition());
        String m6 = "f6g8";
        board.makeMoveAndFlipTurn(MoveParserFromAN.buildMoveFromLAN(board, m6));


        Assert.assertTrue(board.isDrawByRepetition());
    }
}
