package com.github.louism33.chesscore;

import com.github.louism33.utils.MoveParserFromAN;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

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


    @Test
    void repetitionTest5() {
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

        board.makeMoveAndFlipTurn(MoveParserFromAN.buildMoveFromLAN(board, "e2e3"));
        
        Assert.assertFalse(board.isDrawByRepetition());

        Assert.assertFalse(board.isDrawByRepetition());
        board.makeMoveAndFlipTurn(MoveParserFromAN.buildMoveFromLAN(board, "b1c3"));

        Assert.assertFalse(board.isDrawByRepetition());
        board.makeMoveAndFlipTurn(MoveParserFromAN.buildMoveFromLAN(board, "g8f6"));

        Assert.assertFalse(board.isDrawByRepetition());
        board.makeMoveAndFlipTurn(MoveParserFromAN.buildMoveFromLAN(board, "c3b1"));

        Assert.assertFalse(board.isDrawByRepetition());
        board.makeMoveAndFlipTurn(MoveParserFromAN.buildMoveFromLAN(board, "f6g8"));

        Assert.assertTrue(board.isDrawByRepetition());
    }

    @Test
    void repetitionTest6() {
        Chessboard board = new Chessboard();

        for (int i = 0; i < Chessboard.MAX_DEPTH_AND_ARRAY_LENGTH; i++) {
            board.zobristHashStack[i] = i * 2;
        }
        
        board.zobristHashStack[0] = 1; // 0 is treated speacially, hack for the test only

        board.quietHalfMoveCounter = 10;
        board.masterIndex = 2;
        board.zobristHash = 1;
        Assert.assertTrue(board.isDrawByRepetition());
        
        board.quietHalfMoveCounter = 10;
        board.masterIndex = 5;
        board.zobristHash = 6;
        Assert.assertTrue(board.isDrawByRepetition());

        board.quietHalfMoveCounter = 128;
        board.masterIndex = 5;
        board.zobristHash = 6;
        Assert.assertTrue(board.isDrawByRepetition());

        board.quietHalfMoveCounter = 10; // other turn
        board.masterIndex = 5;
        board.zobristHash = 8;
        Assert.assertFalse(board.isDrawByRepetition());

        board.quietHalfMoveCounter = 10; // loop to back of stack
        board.masterIndex = 5;
        board.zobristHash = 254;
        Assert.assertTrue(board.isDrawByRepetition());

        board.quietHalfMoveCounter = 10; // loop to back of stack
        board.masterIndex = 5;
        board.zobristHash = 252;
        Assert.assertFalse(board.isDrawByRepetition());

        board.quietHalfMoveCounter = 128; // loop to back of stack
        board.masterIndex = 5;
        board.zobristHash = 252;
        Assert.assertFalse(board.isDrawByRepetition());

        board.quietHalfMoveCounter = 10_000; // loop to back of stack
        board.masterIndex = 5;
        board.zobristHash = 252;
        Assert.assertFalse(board.isDrawByRepetition());

        board.quietHalfMoveCounter = 123_123; // loop to back of stack
        board.masterIndex = 5;
        board.zobristHash = 252;
        Assert.assertFalse(board.isDrawByRepetition());

        board.quietHalfMoveCounter = 128;
        board.masterIndex = 1337; // absurd master index
        board.zobristHash = 6;
        Assert.assertTrue(board.isDrawByRepetition());
    }
}
