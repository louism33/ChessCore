import com.github.louism33.chesscore.Chessboard;
import com.github.louism33.chesscore.Perft;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

public class PerftTestsBigDepths {

    @Test
    void bigDepth1() {
        Assert.assertEquals(
                Perft.perftTest(6, new Chessboard("r3k2r/8/8/8/3pPp2/8/8/R3K1RR b KQkq e3 0 1"), 485647607),
                485647607);

    }

    @Test
    void bigDepth2() {
        Assert.assertEquals(
                Perft.perftTest(6, new Chessboard("r3k2r/Pppp1ppp/1b3nbN/nP6/BBP1P3/q4N2/Pp1P2PP/R2Q1RK1 w kq - 0 1"), 706045033),
                706045033);
    }

    @Test
    void bigDepth3() {
        Assert.assertEquals(
                Perft.perftTest(6, new Chessboard("8/7p/p5pb/4k3/P1pPn3/8/P5PP/1rB2RK1 b - d3 0 28"), 38633283),
                38633283);
    }

    @Test
    void bigDepth4() {
        Assert.assertEquals(
                Perft.perftTest(7, new Chessboard("8/3K4/2p5/p2b2r1/5k2/8/8/1q6 b - - 1 67"), 493407574),
                493407574);
    }
    @Test
    void bigDepth5() {
        Assert.assertEquals(
                Perft.perftTest(6, new Chessboard("rnbqkb1r/ppppp1pp/7n/4Pp2/8/8/PPPP1PPP/RNBQKBNR w KQkq f6 0 3"), 244063299),
                244063299);
    }

    @Test
    void bigDepth6() {
        Assert.assertEquals(
                Perft.perftTest(5, new Chessboard("r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq -"), 193690690),
                193690690);
    }



    @Test
    void bigDepth7() {
        Assert.assertEquals(
                Perft.perftTest(8, new Chessboard("8/p7/8/1P6/K1k3p1/6P1/7P/8 w - -"), 8103790),
                8103790);
    }

    @Test
    void bigDepth8() {
        Assert.assertEquals(
                Perft.perftTest(1, new Chessboard("n1n5/PPPk4/8/8/8/8/4Kppp/5N1N b - -"), 24),
                24);

        System.out.println("---------");

        Assert.assertEquals(
                Perft.perftTest(2, new Chessboard("n1n5/PPPk4/8/8/8/8/4Kppp/5N1N b - -"), 496),
                496);

        System.out.println("---------");

        Assert.assertEquals(
                Perft.perftTest(3, new Chessboard("n1n5/PPPk4/8/8/8/8/4Kppp/5N1N b - -"), 9483),
                9483);

        System.out.println("---------");

        Assert.assertEquals(
                Perft.perftTest(4, new Chessboard("n1n5/PPPk4/8/8/8/8/4Kppp/5N1N b - -"), 182838),
                182838);

        System.out.println("---------");

        Assert.assertEquals(
                Perft.perftTest(5, new Chessboard("n1n5/PPPk4/8/8/8/8/4Kppp/5N1N b - -"), 3605103),
                3605103);

        System.out.println("---------");

        Assert.assertEquals(
                Perft.perftTest(6, new Chessboard("n1n5/PPPk4/8/8/8/8/4Kppp/5N1N b - -"), 71179139),
                71179139);

        System.out.println("---------");
    }


    @Test
    void bigDepth9() {
        Assert.assertEquals(
                Perft.perftTest(6, new Chessboard("r3k2r/p6p/8/B7/1pp1p3/3b4/P6P/R3K2R w KQkq -"), 77054993),
                77054993);
    }

    @Test
    void bigDepth10() {
        Assert.assertEquals(
                Perft.perftTest(7, new Chessboard("8/2p5/3p4/KP5r/1R3p1k/8/4P1P1/8 w - -"), 178633661),
                178633661);


    }


    @Test
    void bigDepth11() {
        Assert.assertEquals(
                Perft.perftTest(8, new Chessboard("8/5p2/8/2k3P1/p3K3/8/1P6/8 b - -"), 64451405),
                64451405);
    }

    @Test
    void bigDepth12() {
        Assert.assertEquals(
                Perft.perftTest(5, new Chessboard("r3k2r/pb3p2/5npp/n2p4/1p1PPB2/6P1/P2N1PBP/R3K2R w KQkq -"), 29179893),
                29179893);
    }
}
