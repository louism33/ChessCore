package com.github.louism33.chesscore;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import static com.github.louism33.chesscore.BoardConstants.*;
import static com.github.louism33.chesscore.PinnedManager.whichPiecesArePinned;

class ChessboardCheckStateTest {

    @Test
    void testTest() {
        verifyStateToDepth(4, new Chessboard("r1bq1rk1/p4ppp/1pnp1n2/2p5/2PPpP2/1NP1P3/P3B1PP/R1BQ1RK1 b"));
        verifyStateToDepth(4, new Chessboard("2r2rk1/1p1R1pp1/p3p2p/8/4B3/3QB1P1/q1P3KP/8 w"));
        verifyStateToDepth(4, new Chessboard("2R1r3/5k2/pBP1n2p/6p1/8/5P1P/2P3P1/7K w"));
    }

    @Test
    void testMoreTest() {
        verifyStateToDepth(2, new Chessboard("1R1K4/7k/8/8/8/8/8/8 w - - 1 1"));
        verifyStateToDepth(4, new Chessboard("1R1K2k1/8/8/8/8/8/8/8 b"));
        verifyStateToDepth(3, new Chessboard("R2K2k1/8/8/8/8/8/8/8 b"));
        verifyStateToDepth(3, new Chessboard("Q2K2k1/8/8/8/8/8/8/8 b"));
        verifyStateToDepth(3, new Chessboard("8/Q2K2k1/8/8/8/8/8/8 b"));
        verifyStateToDepth(3, new Chessboard("8/1Q1K2k1/8/8/8/8/8/8 b"));
        verifyStateToDepth(2, new Chessboard("7k/8/8/8/8/8/1P6/B6K w"));
        verifyStateToDepth(2, new Chessboard("8/8/8/4k3/8/6P1/1P5B/B6K w"));
        verifyStateToDepth(4, new Chessboard("8/8/8/4k3/8/6P1/1P5R/B6K w"));
        verifyStateToDepth(4, new Chessboard("8/2p5/1P1p4/K5kr/1R3p2/6P1/4P3/8 b - - 0 3"));
        verifyStateToDepth(4, new Chessboard("2Rnr3/2P2k2/pB5p/6p1/8/5P1P/2P3P1/7K w - - 1 2"));
    }


    @Test
    void test1Test() {
        verifyStateToDepth(6, new Chessboard("3k4/3p4/8/K1P4r/8/8/8/8 b - - 0 1"));
        verifyStateToDepth(4, new Chessboard("3K4/PPPPPPP1/8/8/8/8/ppppppp1/3k4 b - - 0 1"));
        verifyStateToDepth(5, new Chessboard("8/pppppppp/8/R1P3k1/8/8/8/3K4 b - - 0 1"));
    }

    @Test
    void tinyDepthsTest() {
        verifyStateToDepth(3, new Chessboard("r6r/1b2k1bq/8/8/7B/8/8/R3K2R b QK - 3 2"));

        verifyStateToDepth(3, new Chessboard("8/8/8/2k5/2pP4/8/B7/4K3 b - d3 5 3"));

        verifyStateToDepth(3, new Chessboard("r1bqkbnr/pppppppp/n7/8/8/P7/1PPPPPPP/RNBQKBNR w QqKk - 2 2"));

        verifyStateToDepth(3, new Chessboard("r3k2r/p1pp1pb1/bn2Qnp1/2qPN3/1p2P3/2N5/PPPBBPPP/R3K2R b QqKk - 3 2"));

        verifyStateToDepth(3, new Chessboard("2kr3r/p1ppqpb1/bn2Qnp1/3PN3/1p2P3/2N5/PPPBBPPP/R3K2R b QK - 3 2"));

        verifyStateToDepth(3, new Chessboard("rnb2k1r/pp1Pbppp/2p5/q7/2B5/8/PPPQNnPP/RNB1K2R w QK - 3 9"));

        verifyStateToDepth(3, new Chessboard("2r5/3pk3/8/2P5/8/2K5/8/8 w - - 5 4"));

        verifyStateToDepth(3, new Chessboard("rnbq1k1r/pp1Pbppp/2p5/8/2B5/8/PPP1NnPP/RNBQK2R w KQ - 1 8"));

        verifyStateToDepth(3, new Chessboard("r4rk1/1pp1qppp/p1np1n2/2b1p1B1/2B1P1b1/P1NP1N2/1PP1QPPP/R4RK1 w - - 0 10"));

    }

    @Test
    void regularBoard() {
        verifyStateToDepth(4, new Chessboard());

        verifyStateToDepth(5, new Chessboard());
    }

    @Test
    void AvoidIllegalEPCapture() {
        verifyStateToDepth(5, new Chessboard("8/5bk1/8/2Pp4/8/1K6/8/8 w - d6 0 1"));

        verifyStateToDepth(5, new Chessboard("8/8/1k6/8/2pP4/8/5BK1/8 b - d3 0 1"));

        verifyStateToDepth(5, new Chessboard("8/RP4k1/8/8/K7/8/8/8 w - d3 0 1"));
    }


    @Test
    void EPCaptureChecksOpponent() {
        verifyStateToDepth(6, new Chessboard("8/8/1k6/2b5/2pP4/8/5K2/8 b - d3 0 1"));

        verifyStateToDepth(6, new Chessboard("8/5k2/8/2Pp4/2B5/1K6/8/8 w - d6 0 1"));
    }


    @Test
    void shortCastlingGivesCheck() {
        verifyStateToDepth(6, new Chessboard("5k2/8/8/8/8/8/8/4K2R w K - 0 1"));

        verifyStateToDepth(6, new Chessboard("4k2r/8/8/8/8/8/8/5K2 b k - 0 1"));
    }

    @Test
    void longCastlingGivesCheck() {
        verifyStateToDepth(4, new Chessboard("3k4/8/8/8/8/8/8/R3K3 w Q - 0 1"));

        verifyStateToDepth(5, new Chessboard("r3k3/8/8/8/8/8/8/3K4 b q - 0 1"));
    }

    @Test
    void bigDepth3() {
        verifyStateToDepth(5, new Chessboard("8/7p/p5pb/4k3/P1pPn3/8/P5PP/1rB2RK1 b - d3 0 28"));
        verifyStateToDepth(2, new Chessboard("8/7p/p5p1/8/P1pkn3/4b3/P2B2PP/1r3RK1 w - - 2 31"));
        final Chessboard board = new Chessboard("8/7K/QQ6/6k1/7r/7r/8/8 w - - 2 31");
        verifyStateToDepth(5, board);
    }

    @Test
    void bigDepth4() {
        verifyStateToDepth(5, new Chessboard("8/3K4/2p5/p2b2r1/5k2/8/8/1q6 b - - 1 67"));
    }

    @Test
    void bigDepth5() {
        verifyStateToDepth(4, new Chessboard("rnbqkb1r/ppppp1pp/7n/4Pp2/8/8/PPPP1PPP/RNBQKBNR w KQkq f6 0 3"));
        verifyStateToDepth(2, new Chessboard("rnbqkb1r/ppppp1pp/5P2/8/6n1/8/PPPP1PPP/RNBQKBNR w KQkq - 1 5"));
    }

    @Test
    void bigDepth10() {
        verifyStateToDepth(5, new Chessboard("8/2p5/3p4/KP5r/1R3p1k/8/4P1P1/8 w - -"));
    }


    @Test
    void bigDepth11() {
        verifyStateToDepth(5, new Chessboard("8/5p2/8/2k3P1/p3K3/8/1P6/8 b - -"));
    }

    @Test
    void bigDepth12() {
        verifyStateToDepth(4, new Chessboard("r3k2r/pb3p2/5npp/n2p4/1p1PPB2/6P1/P2N1PBP/R3K2R w KQkq -"));
    }

    private static void verifyStateToDepth(int depth, Chessboard board) {
        final Chessboard initial = new Chessboard(board);

        Assert.assertEquals(board, initial);

        long ii = countFinalNodesAtDepthHelper(board, depth);
        Assert.assertEquals(board, new Chessboard(board));
        Assert.assertEquals(board, initial);
    }

    private static long countFinalNodesAtDepthHelper(Chessboard board, int depth) {
        long temp = 0;
        if (depth == 0) {
            return 1;
        }
        
        int[] moves = board.generateLegalMoves(board.getCheckers());

        final int turn = board.turn;
        final long myPawns, myKnights, myBishops, myRooks, myQueens, myKing;
        final long enemyPawns, enemyKnights, enemyBishops, enemyRooks, enemyQueens, enemyKing;
        final long friends, enemies;

        myPawns = board.pieces[turn][PAWN];
        myKnights = board.pieces[turn][KNIGHT];
        myBishops = board.pieces[turn][BISHOP];
        myRooks = board.pieces[turn][ROOK];
        myQueens = board.pieces[turn][QUEEN];
        myKing = board.pieces[turn][KING];

        enemyPawns = board.pieces[1 - turn][PAWN];
        enemyKnights = board.pieces[1 - turn][KNIGHT];
        enemyBishops = board.pieces[1 - turn][BISHOP];
        enemyRooks = board.pieces[1 - turn][ROOK];
        enemyQueens = board.pieces[1 - turn][QUEEN];
        enemyKing = board.pieces[1 - turn][KING];

        board.getPieces();
        friends = board.pieces[turn][ALL_COLOUR_PIECES];
        enemies = board.pieces[1 - turn][ALL_COLOUR_PIECES];

        final long allPieces = friends | enemies;

        final long currentPinnedPieces = whichPiecesArePinned(board, board.turn,
                myKing, enemyBishops, enemyRooks,
                enemyQueens, friends, allPieces);

        Assert.assertEquals(board.pinnedPieces[turn], currentPinnedPieces);

        if (board.pinnedPieces[board.turn] != 0) {
            Assert.assertTrue(board.pinningPieces[board.turn] != 0);
        }else {
            Assert.assertEquals(0, board.pinningPieces[turn]);
        }
        
        Assert.assertEquals(board.inCheckRecorder, board.inCheck());
        Assert.assertEquals(board.inCheckRecorder, new Chessboard(board).inCheck());

        if (depth == 1) {
            return moves[moves.length - 1];
        }
        for (int move : moves) {
            if (move == 0) {
                break;
            }
            
            long biggestMoveBit = 0x02000000L;

            Assert.assertTrue(move < biggestMoveBit);

            final boolean givesCheckMove = board.moveGivesCheck(move, false);

            boolean pwintMEEEEE = false;
            board.makeMoveAndFlipTurn(move);
            final boolean incheck = board.inCheck();
            
            if (givesCheckMove != incheck) {
                System.out.println(board);
                MoveParser.printMove(move);
                System.out.println("givesCheckMove: " + givesCheckMove);
                System.out.println("incheck       : " + incheck);
                pwintMEEEEE = true;
            }
            Assert.assertEquals(givesCheckMove, board.inCheck());
            if (givesCheckMove) {
                Assert.assertTrue(board.getCheckers() != 0);
            } else {
                Assert.assertTrue(board.getCheckers() == 0);
            }

            Assert.assertTrue(board.currentCheckStateKnown);
            
            board.unMakeMoveAndFlipTurn();

            if (pwintMEEEEE) {
                System.out.println("board before:");
                System.out.println(board);
                System.out.println(board.toFenString());
                Assert.fail();
            }
            
            board.makeMoveAndFlipTurn(move);

            long movesAtDepth = countFinalNodesAtDepthHelper(board, depth - 1);
            temp += movesAtDepth;
            board.unMakeMoveAndFlipTurn();
        }
        return temp;
    }
}

