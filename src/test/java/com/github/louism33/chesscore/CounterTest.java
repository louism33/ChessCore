package com.github.louism33.chesscore;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import static com.github.louism33.chesscore.BoardConstants.BLACK_PAWN;
import static com.github.louism33.chesscore.BoardConstants.WHITE_PAWN;

public class CounterTest {


    @Test
    void fenOneTest(){
        String fen = "8/7p/5k2/5p2/p1p2P2/Pr1pPK2/1P1R3P/8 b kqKQ - 123 321";
        Chessboard board = new Chessboard(fen);
        Assert.assertEquals( 123, board.quietHalfMoveCounter);
        Assert.assertEquals(321, board.fullMoveCounter);
    }

    @Test
    void fenTwoTest(){
        String fen = "3k4/8/8/8/8/8/8/R3K3 w Q - 0 1";
        Chessboard board = new Chessboard(fen);
        Assert.assertEquals( 0, board.quietHalfMoveCounter);
        Assert.assertEquals(1, board.fullMoveCounter);
    }

    @Test
    void fenThreeTest(){
        String fen = "8/7p/5k2/5p2/p4P2/PrppPK2/1P1R3P/8 b - -";
        Chessboard board = new Chessboard(fen);
        Assert.assertEquals( 0, board.quietHalfMoveCounter);
        Assert.assertEquals(0, board.fullMoveCounter);
    }

    @Test
    void fenFourTest(){
        String fen = "8/3K4/2p5/p2b2r1/5k2/8/8/1q6 b - - 1 67";
        Chessboard board = new Chessboard(fen);
        Assert.assertEquals( 1, board.quietHalfMoveCounter);
        Assert.assertEquals(67, board.fullMoveCounter);
    }

    @Test
    void fenFiveTest(){
        String fen = "8/7p/p5pb/4k3/P1pPn3/8/P5PP/1rB2RK1 b - d3 0 28";
        Chessboard board = new Chessboard(fen);
        Assert.assertEquals( 0, board.quietHalfMoveCounter);
        Assert.assertEquals(28, board.fullMoveCounter);
    }

    @Test
    void fenSixTest(){
        String fen = "rnbqkb1r/ppppp1pp/7n/4Pp2/8/8/PPPP1PPP/RNBQKBNR w KQkq f6 0 3";
        Chessboard board = new Chessboard(fen);
        Assert.assertEquals( 0, board.quietHalfMoveCounter);
        Assert.assertEquals(3, board.fullMoveCounter);
    }

    @Test
    void fenSevenTest(){
        String fen = "rnbqkb1r/ppppp1pp/7n/4Pp2/8/8/PPPP1PPP/RNBQKBNR w KQkq f 0 3";
        Chessboard board = new Chessboard(fen);
        Assert.assertEquals( 0, board.quietHalfMoveCounter);
        Assert.assertEquals(3, board.fullMoveCounter);
    }

    @Test
    void fenEightTest(){
        Chessboard board = new Chessboard();

        for (int i = 0; i < 100; i++) {


            int[] moves = board.generateLegalMoves();

            int move = moves[0];

            int preCounter = board.quietHalfMoveCounter;

            int movingPieceInt = MoveParser.getMovingPieceInt(move);
            boolean captureMove = MoveParser.isCaptureMove(move);

            board.makeMoveAndFlipTurn(move);
            int postCounter = board.quietHalfMoveCounter;

            if (movingPieceInt == WHITE_PAWN || movingPieceInt == BLACK_PAWN) {
                Assert.assertEquals(0, postCounter);
            }

            else if (captureMove) {
                Assert.assertEquals(0, postCounter);
            }

            else {
                Assert.assertEquals(postCounter, preCounter + 1);
            }
        }
    }


    @Test
    void fenNineTest(){
        Chessboard board = new Chessboard();

        for (int i = 0; i < 100; i++) {

            int[] moves = board.generateLegalMoves();
            int pseudoRandomMove = (i * 100) % moves[moves.length - 1];
            int move = moves[pseudoRandomMove];

            int preCounter = board.quietHalfMoveCounter;

            int movingPieceInt = MoveParser.getMovingPieceInt(move);
            boolean captureMove = MoveParser.isCaptureMove(move);

            board.makeMoveAndFlipTurn(move);
            int postCounter = board.quietHalfMoveCounter;

            if (movingPieceInt == WHITE_PAWN || movingPieceInt == BLACK_PAWN) {
                Assert.assertEquals(0, postCounter);
            }

            else if (captureMove) {
                Assert.assertEquals(0, postCounter);
            }

            else {
                Assert.assertEquals(postCounter, preCounter + 1);
            }
        }
    }


    @Test
    void fenTenTest(){
        Chessboard board = new Chessboard();

        for (int i = 0; i < 200; i++) {

            int[] moves = board.generateLegalMoves();
            int pseudoRandomMove = (i * 98) % moves[moves.length - 1];
            int move = moves[pseudoRandomMove];

            int preCounter = board.quietHalfMoveCounter;

            int movingPieceInt = MoveParser.getMovingPieceInt(move);
            boolean captureMove = MoveParser.isCaptureMove(move);

            board.makeMoveAndFlipTurn(move);
            int postCounter = board.quietHalfMoveCounter;

            if (movingPieceInt == WHITE_PAWN || movingPieceInt == BLACK_PAWN) {
                Assert.assertEquals(0, postCounter);
            }

            else if (captureMove) {
                Assert.assertEquals(0, postCounter);
            }

            else {
                Assert.assertEquals(postCounter, preCounter + 1);
            }
        }
    }
}
