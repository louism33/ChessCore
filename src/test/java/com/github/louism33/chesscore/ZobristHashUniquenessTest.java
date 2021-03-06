package com.github.louism33.chesscore;

import org.junit.Assert;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

public class ZobristHashUniquenessTest {

    @Test
    void test1() {
        verifyHashToDepth(6, new Chessboard("3k4/3p4/8/K1P4r/8/8/8/8 b - - 0 1"));
    }

    @Test
    void tinyDepths() {
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
    void regularBoard() {
        verifyHashToDepth(5, new Chessboard());

        verifyHashToDepth(6, new Chessboard());
    }

    @Test
    void AvoidIllegalEPCapture() {
        verifyHashToDepth(6, new Chessboard("8/5bk1/8/2Pp4/8/1K6/8/8 w - d6 0 1"));

        verifyHashToDepth(6, new Chessboard("8/8/1k6/8/2pP4/8/5BK1/8 b - d3 0 1"));
    }


    @Test
    void EPCaptureChecksOpponent() {
        verifyHashToDepth(6, new Chessboard("8/8/1k6/2b5/2pP4/8/5K2/8 b - d3 0 1"));

        verifyHashToDepth(6, new Chessboard("8/5k2/8/2Pp4/2B5/1K6/8/8 w - d6 0 1"));
    }


    @Test
    void shortCastlingGivesCheck() {
        verifyHashToDepth(6, new Chessboard("5k2/8/8/8/8/8/8/4K2R w K - 0 1"));

        verifyHashToDepth(6, new Chessboard("4k2r/8/8/8/8/8/8/5K2 b k - 0 1"));
    }

    @Test
    void longCastlingGivesCheck() {
        verifyHashToDepth(6, new Chessboard("3k4/8/8/8/8/8/8/R3K3 w Q - 0 1"));

        verifyHashToDepth(6, new Chessboard("r3k3/8/8/8/8/8/8/3K4 b q - 0 1"));
    }

    @Test
    void bigDepth3() {
        verifyHashToDepth(5, new Chessboard("8/7p/p5pb/4k3/P1pPn3/8/P5PP/1rB2RK1 b - d3 0 28"));
    }

    @Test
    void bigDepth4() {
        verifyHashToDepth(6, new Chessboard("8/3K4/2p5/p2b2r1/5k2/8/8/1q6 b - - 1 67"));
    }
    @Test
    void bigDepth5() {
        verifyHashToDepth(6, new Chessboard("rnbqkb1r/ppppp1pp/7n/4Pp2/8/8/PPPP1PPP/RNBQKBNR w KQkq f6 0 3"));
    }

    @Test
    void bigDepth10() {
        verifyHashToDepth(6, new Chessboard("8/2p5/3p4/KP5r/1R3p1k/8/4P1P1/8 w - -"));
    }


    @Test
    void bigDepth11() {
        verifyHashToDepth(6, new Chessboard("8/5p2/8/2k3P1/p3K3/8/1P6/8 b - -"));
    }

    @Test
    void bigDepth12() {
        verifyHashToDepth(4, new Chessboard("r3k2r/pb3p2/5npp/n2p4/1p1PPB2/6P1/P2N1PBP/R3K2R w KQkq -"));
    }
    
    @AfterAll
    public static void recorder(){
        System.out.println("Max array size:                 " + ARRAY_SIZE );
        System.out.println("Total number of hash checks:    " + numberOfHashChecks);
        System.out.println("Same entries:                   " + sameEntry);
        System.out.println("Different Entry:                " + differentEntry);
        System.out.println("Successes:                      " + checkSuccess);
        System.out.println("Failures:                       " + checkFail);
        System.out.println("Total size:                     " + size);
        System.out.println();
        System.out.println("For size "+ size+", there was a ratio of success to failure of "+(100*(double) checkSuccess/((double) numberOfHashChecks)));
    }
    private static int shift = 26; // 26
    
    private static int ARRAY_SIZE = 1 << shift;
    
    private static long numberOfHashChecks = 0;
    private static long checkSuccess = 0;
    private static long sameEntry = 0;
    private static long differentEntry = 0;
    private static long checkFail = 0;
    private static long size = 0;
    private static long[] hashesSeen = new long[ARRAY_SIZE];
    
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
        for (int move : moves) {
            if (move == 0){
                break;
            }
            board.makeMoveAndFlipTurn(move);

            int index = (int) (board.zobristHash >>> (64-shift));
            long entry = hashesSeen[index];
            numberOfHashChecks++;
            if (entry == 0){
                hashesSeen[index] = board.zobristHash;
                size++;
            } else{
                
                if (entry == board.zobristHash){
                    checkSuccess++;
                }
                else {
                    checkFail++;
                }
                
                if (hashesSeen[index] == board.zobristHash){
                    sameEntry++;
                } else {
                    differentEntry++;
                }
            }

            long movesAtDepth = countFinalNodesAtDepthHelper(board, depth - 1);
            temp += movesAtDepth;
            board.unMakeMoveAndFlipTurn();
        }
        return temp;
    }
}

    
