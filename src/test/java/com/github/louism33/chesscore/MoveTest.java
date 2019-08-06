package com.github.louism33.chesscore;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import static com.github.louism33.chesscore.MoveConstants.FIRST_FREE_BIT;

public class MoveTest {

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
        verifyHashToDepth(5, new Chessboard("8/8/1k6/2b5/2pP4/8/5K2/8 b - d3 0 1"));

        verifyHashToDepth(5, new Chessboard("8/5k2/8/2Pp4/2B5/1K6/8/8 w - d6 0 1"));
    }



    private static void verifyHashToDepth(int depth, Chessboard board) {
        final Chessboard initial = new Chessboard(board);
        Assert.assertEquals(board, initial);

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
        if (depth == 1){
            return moves[moves.length - 1];
        }
        for (int i = 0; i < moves.length; i++) {
            int move = moves[i];
            if (move == 0) {
                break;
            }

            int[] copies = new int[moves.length];
            System.arraycopy(moves, 0, copies, 0, moves.length);
            
            Assert.assertTrue(move < FIRST_FREE_BIT);

            board.makeMoveAndFlipTurn(move);

            long movesAtDepth = countFinalNodesAtDepthHelper(board, depth - 1);
            temp += movesAtDepth;

            board.unMakeMoveAndFlipTurn();
            Assert.assertEquals(board, new Chessboard(board));

            Assert.assertArrayEquals(moves, copies);
            
        }
        return temp;
    }
    
}
