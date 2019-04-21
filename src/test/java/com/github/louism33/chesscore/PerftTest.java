package com.github.louism33.chesscore;

import com.github.louism33.utils.NPSTest;
import com.github.louism33.utils.Perft;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

public class PerftTest {

    /*
    Many thanks to:
    
    - Martin Sedlak
    http://www.talkchess.com/forum3/viewtopic.php?t=47318
    
    - JVMerlino
    http://www.talkchess.com/forum3/viewtopic.php?f=7&t=47318&sid=346f11bd9bec1af8783af3009d320d94&start=20
    
    - Peter Ellis Jones
    https://gist.github.com/peterellisjones/8c46c28141c162d1d8a0f0badbc9cff9
     */

    @Test
    void npsTest(){
        NPSTest.npsTest(new Chessboard(), 6);
//        NPSTest.npsTest(new Chessboard(), 7);
//        NPSTest.npsTest(new Chessboard(), 8);
    }


    @Test
    void regularBoard() {
        
        Assert.assertEquals(4865609, Perft.perftTest(new Chessboard(), 5));
        System.out.println("regular board depth " + 5 + " correct.");
//        Assert.assertEquals(119060324, Perft.perftTest(new Chessboard(), 6));
//        System.out.println("regular board depth " + 6 + " correct.");
//        Assert.assertEquals(3195901860L, Perft.perftTest(new Chessboard(), 7));
//        System.out.println("regular board depth " + 7 + " correct.");
//        Assert.assertEquals(84998978956L, Perft.perftTest(new Chessboard(), 8));
//        System.out.println("regular board depth " + 8 + " correct.");
    }


    @Test
    void test1() {
        String fen = "3k4/3p4/8/K1P4r/8/8/8/8 b - - 0 1";
        Assert.assertEquals(1134888, Perft.perftTest(new Chessboard(fen), 6));
        System.out.println(fen+"\ndepth " + 6 + " correct.");
    }

    @Test
    void test2() {
        String fen = "8/8/4k3/8/2p5/8/B2P2K1/8 w - - 0 1";
        Assert.assertEquals(1015133, Perft.perftTest(new Chessboard(fen), 6));
        System.out.println(fen+"\ndepth " + 6 + " correct.");
    }

    @Test
    void test3() {
        String fen = "8/8/1k6/2b5/2pP4/8/5K2/8 b - d3 0 1";
        Assert.assertEquals(1440467, Perft.perftTest(new Chessboard(fen), 6));
        System.out.println(fen+"\ndepth " + 6 + " correct.");
    }

    @Test
    void test4() {
        String fen = "5k2/8/8/8/8/8/8/4K2R w K - 0 1";
        Assert.assertEquals(661072, Perft.perftTest(new Chessboard(fen), 6));
        System.out.println(fen+"\ndepth " + 6 + " correct.");
    }

    @Test
    void test5() {
        String fen = "3k4/8/8/8/8/8/8/R3K3 w Q - 0 1";
        Assert.assertEquals(803711, Perft.perftTest(new Chessboard(fen), 6));
        System.out.println(fen+"\ndepth " + 6 + " correct.");

    }


    @Test
    void test6() {
        String fen = "r3k2r/1b4bq/8/8/8/8/7B/R3K2R w KQkq - 0 1";
        Assert.assertEquals(1274206, Perft.perftTest(new Chessboard(fen), 4));
        System.out.println(fen+"\ndepth " + 4 + " correct.");
    }


    @Test
    void test7() {
        String fen = "r3k2r/8/3Q4/8/8/5q2/8/R3K2R b KQkq - 0 1";
        Assert.assertEquals(1720476, Perft.perftTest(new Chessboard(fen), 4));
        System.out.println(fen+"\ndepth " + 4 + " correct.");
    }

    @Test
    void test8() {
        String fen = "2K2r2/4P3/8/8/8/8/8/3k4 w - - 0 1";
        Assert.assertEquals(3821001, Perft.perftTest(new Chessboard(fen), 6));
        System.out.println(fen+"\ndepth " + 6 + " correct.");
    }

    @Test
    void test9() {
        String fen = "8/8/1P2K3/8/2n5/1q6/8/5k2 b - - 0 1";
        Assert.assertEquals(1004658, Perft.perftTest(new Chessboard(fen), 5));
        System.out.println(fen+"\ndepth " + 5 + " correct.");
    }

    @Test
    void test10() {
        String fen = "4k3/1P6/8/8/8/8/K7/8 w - - 0 1";
        Assert.assertEquals(217342, Perft.perftTest(new Chessboard(fen), 6));
        System.out.println(fen+"\ndepth " + 6 + " correct.");
    }

    @Test
    void test11() {
        String fen = "8/P1k5/K7/8/8/8/8/8 w - - 0 1";
        Assert.assertEquals(92683, Perft.perftTest(new Chessboard(fen), 6));
        System.out.println(fen+"\ndepth " + 6 + " correct.");

    }

    @Test
    void test12() {
        Assert.assertEquals(
                Perft.perftTest(6, new Chessboard("K1k5/8/P7/8/8/8/8/8 w - - 0 1"), 2217),
                2217);
    }

    @Test
    void test13() {
        Assert.assertEquals(
                Perft.perftTest(7, new Chessboard("8/k1P5/8/1K6/8/8/8/8 w - - 0 1"), 567584),
                567584);
    }


    @Test
    void test14() {
        Assert.assertEquals(
                Perft.perftTest(4, new Chessboard("8/8/2k5/5q2/5n2/8/5K2/8 b - - 0 1"), 23527),
                23527);

    }


    @Test
    void test15() {
        Assert.assertEquals(
                Perft.perftTest(5, new Chessboard("r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq -"), 193690690),
                193690690);

    }

    @Test
    void AvoidIllegalEPCapture() {
        Assert.assertEquals(
                Perft.perftTest(6, new Chessboard("8/5bk1/8/2Pp4/8/1K6/8/8 w - d6 0 1"), 824064),
                824064);

        Assert.assertEquals(
                Perft.perftTest(6, new Chessboard("8/8/1k6/8/2pP4/8/5BK1/8 b - d3 0 1"), 824064),
                824064);
    }


    @Test
    void EPCaptureChecksOpponent() {
        Assert.assertEquals(
                Perft.perftTest(6, new Chessboard("8/8/1k6/2b5/2pP4/8/5K2/8 b - d3 0 1"), 1440467),
                1440467);

        Assert.assertEquals(
                Perft.perftTest(6, new Chessboard("8/5k2/8/2Pp4/2B5/1K6/8/8 w - d6 0 1"), 1440467),
                1440467);
    }


    @Test
    void shortCastlingGivesCheck() {
        Assert.assertEquals(
                Perft.perftTest(6, new Chessboard("5k2/8/8/8/8/8/8/4K2R w K - 0 1"), 661072),
                661072);

        Assert.assertEquals(
                Perft.perftTest(6, new Chessboard("4k2r/8/8/8/8/8/8/5K2 b k - 0 1"), 661072),
                661072);
    }

    @Test
    void longCastlingGivesCheck() {
        Assert.assertEquals(
                Perft.perftTest(6, new Chessboard("3k4/8/8/8/8/8/8/R3K3 w Q - 0 1"), 803711),
                803711);

        Assert.assertEquals(
                Perft.perftTest(6, new Chessboard("r3k3/8/8/8/8/8/8/3K4 b q - 0 1"), 803711),
                803711);
    }


    @Test
    void CastlingCRAndDoublePins() {
        Assert.assertEquals(
                Perft.perftTest(4, new Chessboard("r3k2r/1b4bq/8/8/8/8/7B/R3K2R w KQkq - 0 1"), 1274206),
                1274206);

        Assert.assertEquals(
                Perft.perftTest(4, new Chessboard("r3k2r/7b/8/8/8/8/1B4BQ/R3K2R b KQkq - 0 1"), 1274206),
                1274206);
    }


    @Test
    void CastlingPrevented() {
        Assert.assertEquals(
                Perft.perftTest(4, new Chessboard("r3k2r/8/3Q4/8/8/5q2/8/R3K2R b KQkq - 0 1"), 1720476),
                1720476);

        Assert.assertEquals(
                Perft.perftTest(4, new Chessboard("r3k2r/8/5Q2/8/8/3q4/8/R3K2R w KQkq - 0 1"), 1720476),
                1720476);
    }

    @Test
    void PromoteOutOfCheck() {
        Assert.assertEquals(
                Perft.perftTest(6, new Chessboard("2K2r2/4P3/8/8/8/8/8/3k4 w - - 0 1"), 3821001),
                3821001);

        Assert.assertEquals(
                Perft.perftTest(6, new Chessboard("3K4/8/8/8/8/8/4p3/2k2R2 b - - 0 1"), 3821001),
                3821001);
    }

    @Test
    void discoveredCheck() {
        Assert.assertEquals(
                Perft.perftTest(5, new Chessboard("8/8/1P2K3/8/2n5/1q6/8/5k2 b - - 0 1"), 1004658),
                1004658);

        Assert.assertEquals(
                Perft.perftTest(5, new Chessboard("5K2/8/1Q6/2N5/8/1p2k3/8/8 w - - 0 1"), 1004658),
                1004658);
    }

    @Test
    void promoteToCheck() {
        Assert.assertEquals(
                Perft.perftTest(6, new Chessboard("4k3/1P6/8/8/8/8/K7/8 w - - 0 1"), 217342),
                217342);

        Assert.assertEquals(
                Perft.perftTest(6, new Chessboard("8/k7/8/8/8/8/1p6/4K3 b - - 0 1"), 217342),
                217342);
    }

    @Test
    void underPromoteToCheck() {
        Assert.assertEquals(
                Perft.perftTest(6, new Chessboard("8/P1k5/K7/8/8/8/8/8 w - - 0 1"), 92683),
                92683);

        Assert.assertEquals(
                Perft.perftTest(6, new Chessboard("8/8/8/8/8/k7/p1K5/8 b - - 0 1"), 92683),
                92683);
    }

    @Test
    void selfStaleMate() {
        Assert.assertEquals(
                Perft.perftTest(6, new Chessboard("K1k5/8/P7/8/8/8/8/8 w - - 0 1"), 2217),
                2217);

        Assert.assertEquals(
                Perft.perftTest(6, new Chessboard("8/8/8/8/8/p7/8/k1K5 b - - 0 1"), 2217),
                2217);
    }

    @Test
    void checkStalemate() {
        Assert.assertEquals(
                Perft.perftTest(7, new Chessboard("8/k1P5/8/1K6/8/8/8/8 w - - 0 1"), 567584),
                567584);

        Assert.assertEquals(
                Perft.perftTest(7, new Chessboard("8/8/8/8/1k6/8/K1p5/8 b - - 0 1"), 567584),
                567584);
    }


    @Test
    void doubleCheck() {
        Assert.assertEquals(
                Perft.perftTest(4, new Chessboard("8/8/2k5/5q2/5n2/8/5K2/8 b - - 0 1"), 23527),
                23527);

        Assert.assertEquals(
                Perft.perftTest(4, new Chessboard("8/5k2/8/5N2/5Q2/2K5/8/8 w - - 0 1"), 23527),
                23527);
    }

    @Test
    void misc() {

        Assert.assertEquals(
                Perft.perftTest(4, new Chessboard("8/7p/p5pb/4k3/P1pPn3/8/P5PP/1rB2RK1 b - d3 0 28"), 67197),
                67197);

        Assert.assertEquals(
                Perft.perftTest(6, new Chessboard("8/7p/p5pb/4k3/P1pPn3/8/P5PP/1rB2RK1 b - d3 0 28"), 38633283),
                38633283);
    }
    
}