package com.github.louism33.chesscore;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

class PerftTestDetail {
    @Test
    void detailTest1(){
        Assert.assertEquals(
                Perft.perftTest(1, new Chessboard("r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - 0 1"), 48),
                48);

        System.out.println("--------- ");

        Assert.assertEquals(
                Perft.perftTest(2, new Chessboard("r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - 0 1"), 2039),
                2039);

        System.out.println("--------- ");

        Assert.assertEquals(
                Perft.perftTest(3, new Chessboard("r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - 0 1"), 97862),
                97862);

        System.out.println("--------- ");

        Assert.assertEquals(
                Perft.perftTest(4, new Chessboard("r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - 0 1"), 4085603),
                4085603);

        System.out.println("--------- ");

        Assert.assertEquals(
                Perft.perftTest(5, new Chessboard("r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - 0 1"), 193690690),
                193690690);

        System.out.println("--------- ");
//
//        Assert.assertEquals(
//                Perft.runPerftTestWithBoardLong(6, new Chessboard("r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - 0 1"), 8031647685L),
//                8031647685L);
//
//        System.out.println("--------- ");
    }

    @Test
    void detailTest2(){
        Assert.assertEquals(
                Perft.perftTest(1, new Chessboard("8/PPP4k/8/8/8/8/4Kppp/8 w - - 0 1"), 18),
                18);

        System.out.println("--------- ");

        Assert.assertEquals(
                Perft.perftTest(2, new Chessboard("8/PPP4k/8/8/8/8/4Kppp/8 w - - 0 1"), 290),
                290);

        System.out.println("--------- ");

        Assert.assertEquals(
                Perft.perftTest(3, new Chessboard("8/PPP4k/8/8/8/8/4Kppp/8 w - - 0 1"), 5044),
                5044);

        System.out.println("--------- ");

        Assert.assertEquals(
                Perft.perftTest(4, new Chessboard("8/PPP4k/8/8/8/8/4Kppp/8 w - - 0 1"), 89363),
                89363);

        System.out.println("--------- ");

        Assert.assertEquals(
                Perft.perftTest(5, new Chessboard("8/PPP4k/8/8/8/8/4Kppp/8 w - - 0 1"), 1745545),
                1745545);

        System.out.println("--------- ");

//        Assert.assertEquals(
//                Perft.runPerftTestWithBoardLong(6, new Chessboard("8/PPP4k/8/8/8/8/4Kppp/8 w - - 0 1"), 34336777),
//                34336777);
//
//        System.out.println("--------- ");
    }


    @Test
    void detailTest3() {
        Assert.assertEquals(
                Perft.perftTest(1, new Chessboard("8/3K4/2p5/p2b2r1/5k2/8/8/1q6 b - 1 67"), 50),
                50);

        System.out.println("--------- ");

        Assert.assertEquals(
                Perft.perftTest(2, new Chessboard("8/3K4/2p5/p2b2r1/5k2/8/8/1q6 b - 1 67"), 279),
                279);

        System.out.println("--------- ");


        Assert.assertEquals(
                Perft.perftTest(3, new Chessboard("8/3K4/2p5/p2b2r1/5k2/8/8/1q6 b - 1 67"), 13310),
                13310);

        System.out.println("--------- ");

        Assert.assertEquals(
                Perft.perftTest(4, new Chessboard("8/3K4/2p5/p2b2r1/5k2/8/8/1q6 b - 1 67"), 54703),
                54703);

        System.out.println("--------- ");

        Assert.assertEquals(
                Perft.perftTest(5, new Chessboard("8/3K4/2p5/p2b2r1/5k2/8/8/1q6 b - 1 67"), 2538084),
                2538084);

        System.out.println("--------- ");
    }

    @Test
    void detailTest4() {
        Assert.assertEquals(
                Perft.perftTest(1, new Chessboard("8/2p5/3p4/KP5r/1R3p1k/8/4P1P1/8 w - - 0 1"), 14),
                14);

        System.out.println("--------- ");

        Assert.assertEquals(
                Perft.perftTest(2, new Chessboard("8/2p5/3p4/KP5r/1R3p1k/8/4P1P1/8 w - - 0 1"), 191),
                191);

        System.out.println("--------- ");

        Assert.assertEquals(
                Perft.perftTest(3, new Chessboard("8/2p5/3p4/KP5r/1R3p1k/8/4P1P1/8 w - - 0 1"), 2812),
                2812);

        System.out.println("--------- ");

        Assert.assertEquals(
                Perft.perftTest(4, new Chessboard("8/2p5/3p4/KP5r/1R3p1k/8/4P1P1/8 w - - 0 1"), 43238),
                43238);

        System.out.println("--------- ");

        Assert.assertEquals(
                Perft.perftTest(5, new Chessboard("8/2p5/3p4/KP5r/1R3p1k/8/4P1P1/8 w - - 0 1"), 674624),
                674624);

        System.out.println("--------- ");
    }


    @Test
    void detailTest5() {
        Assert.assertEquals(
                Perft.perftTest(5, new Chessboard("r3k2r/pb3p2/5npp/n2p4/1p1PPB2/6P1/P2N1PBP/R3K2R b KQkq -"), 26957954),
                26957954);

    }
}
