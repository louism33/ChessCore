package com.github.louism33.chesscore;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

public class NullMoveTest {

    @Test
    void tinyDepthsTest() {
        verifyHashToDepth(1, new Chessboard("r6r/1b2k1bq/8/8/7B/8/8/R3K2R b QK - 3 2"));

        verifyHashToDepth(1, new Chessboard("8/8/8/2k5/2pP4/8/B7/4K3 b - d3 5 3"));

        verifyHashToDepth(1, new Chessboard("r1bqkbnr/pppppppp/n7/8/8/P7/1PPPPPPP/RNBQKBNR w QqKk - 2 2"));

        verifyHashToDepth(1, new Chessboard("r3k2r/p1pp1pb1/bn2Qnp1/2qPN3/1p2P3/2N5/PPPBBPPP/R3K2R b QqKk - 3 2"));

        verifyHashToDepth(1, new Chessboard("2kr3r/p1ppqpb1/bn2Qnp1/3PN3/1p2P3/2N5/PPPBBPPP/R3K2R b QK - 3 2"));

        verifyHashToDepth(1, new Chessboard("rnb2k1r/pp1Pbppp/2p5/q7/2B5/8/PPPQNnPP/RNB1K2R w QK - 3 9"));

        verifyHashToDepth(1, new Chessboard("2r5/3pk3/8/2P5/8/2K5/8/8 w - - 5 4"));

        verifyHashToDepth(3, new Chessboard("rnbq1k1r/pp1Pbppp/2p5/8/2B5/8/PPP1NnPP/RNBQK2R w KQ - 1 8"));

        verifyHashToDepth(3, new Chessboard("r4rk1/1pp1qppp/p1np1n2/2b1p1B1/2B1P1b1/P1NP1N2/1PP1QPPP/R4RK1 w - - 0 10"));

    }

    @Test
    void bigDepth11Test() {
        verifyHashToDepth(6, new Chessboard("8/5p2/8/2k3P1/p3K3/8/1P6/8 b - -"));
    }

    @Test
    void bigDepth12Test() {
        verifyHashToDepth(4, new Chessboard("r3k2r/pb3p2/5npp/n2p4/1p1PPB2/6P1/P2N1PBP/R3K2R w KQkq -"));
    }

    @Test
    void regularBoardTest() {
        verifyHashToDepth(3, new Chessboard());

        verifyHashToDepth(4, new Chessboard());
    }

    @Test
    void AvoidIllegalEPCaptureTest() {
        verifyHashToDepth(5, new Chessboard("8/5bk1/8/2Pp4/8/1K6/8/8 w - d6 0 1"));

        verifyHashToDepth(5, new Chessboard("8/8/1k6/8/2pP4/8/5BK1/8 b - d3 0 1"));
    }


    @Test
    void EPCaptureChecksOpponentTest() {
        verifyHashToDepth(3, new Chessboard("8/8/1k6/2b5/2pP4/8/5K2/8 b - d3 0 1"));
        
//        verifyHashToDepth(5, new Chessboard("8/8/1k6/2b5/2pP4/8/5K2/8 b - d3 0 1"));

//        verifyHashToDepth(5, new Chessboard("8/5k2/8/2Pp4/2B5/1K6/8/8 w - d6 0 1"));
    }



    private static void verifyHashToDepth(int depth, Chessboard board) {
        final Chessboard initial = new Chessboard(board);
        Assert.assertEquals(board, initial);


        board.makeNullMoveAndFlipTurn();


        Assert.assertEquals(board, new Chessboard(board));

        board.unMakeNullMoveAndFlipTurn();
        Assert.assertEquals(board, new Chessboard(board));

        countFinalNodesAtDepthHelper(board, depth);
        
        Assert.assertEquals(board, new Chessboard(board));
        Assert.assertEquals(board, initial);

    }

    private static long countFinalNodesAtDepthHelper(Chessboard board, int depth){
        long temp = 0;
        if (depth == 0){
            return 1;
        }

        int[] moves = board.generateLegalMoves();

        Assert.assertEquals(board.inCheckRecorder, board.inCheck());
        Assert.assertEquals(board.inCheckRecorder, board.getCheckers() != 0);
//        System.out.println(board);
//        System.out.println(board.inCheckRecorder);
//        MoveParser.printMove(moves);
        
        if (depth == 1){
            return moves[moves.length - 1];
        }
        for (int move : moves) {
            if (move == 0){
                break;
            }
            final boolean checkingMove = board.moveGivesCheck(move);
            boolean makeNullMove = !checkingMove;
            int[] copies = new int[moves.length];
            System.arraycopy(moves, 0, copies, 0, moves.length);

            long beforeHash = board.zobristHash;
            long beforePawnHash = board.zobristPawnHash;
            
            if (makeNullMove) {
                board.makeNullMoveAndFlipTurn();
                
                Assert.assertNotEquals(beforeHash, board.zobristHash);
                Assert.assertEquals(beforePawnHash, board.zobristPawnHash);
                Assert.assertEquals(board, new Chessboard(board));
                
                board.unMakeNullMoveAndFlipTurn();
            }
            

            board.makeMoveAndFlipTurn(move);
            
            Assert.assertEquals(board, new Chessboard(board));
            Assert.assertEquals(checkingMove, board.inCheck());
            Assert.assertEquals(checkingMove, board.getCheckers() != 0);
            
            if (makeNullMove) {
                board.makeNullMoveAndFlipTurn();
                
                Assert.assertNotEquals(beforeHash, board.zobristHash);
                Assert.assertEquals(board, new Chessboard(board));
            }

            long movesAtDepth = countFinalNodesAtDepthHelper(board, depth - 1);
            temp += movesAtDepth;

            if (makeNullMove) {
                board.unMakeNullMoveAndFlipTurn();
            }
            
            
    
            board.unMakeMoveAndFlipTurn();

            Assert.assertEquals(beforeHash, board.zobristHash);
            Assert.assertEquals(beforePawnHash, board.zobristPawnHash);
            Assert.assertEquals(board, new Chessboard(board));


            Assert.assertEquals(board, new Chessboard(board));
            
            Assert.assertArrayEquals(moves, copies);

        }
        return temp;
    }
}
