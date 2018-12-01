import com.github.louism33.chesscore.Chessboard;
import com.github.louism33.chesscore.Perft;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

public class PerftTestTinyDepths {

    @Test
    void tinyDepths() {
        Assert.assertEquals(
                Perft.perftTest(1, new Chessboard("r6r/1b2k1bq/8/8/7B/8/8/R3K2R b QK - 3 2"), 8),
                8);

        System.out.println("-----------------------------");

        Assert.assertEquals(
                Perft.perftTest(1, new Chessboard("8/8/8/2k5/2pP4/8/B7/4K3 b - d3 5 3"), 8),
                8);

        System.out.println("-----------------------------");

        Assert.assertEquals(
                Perft.perftTest(1, new Chessboard("r1bqkbnr/pppppppp/n7/8/8/P7/1PPPPPPP/RNBQKBNR w QqKk - 2 2"), 19),
                19);

        System.out.println("-----------------------------");

        Assert.assertEquals(
                Perft.perftTest(1, new Chessboard("r3k2r/p1pp1pb1/bn2Qnp1/2qPN3/1p2P3/2N5/PPPBBPPP/R3K2R b QqKk - 3 2"), 5),
                5);

        System.out.println("-----------------------------");

        Assert.assertEquals(
                Perft.perftTest(1, new Chessboard("2kr3r/p1ppqpb1/bn2Qnp1/3PN3/1p2P3/2N5/PPPBBPPP/R3K2R b QK - 3 2"), 44),
                44);

        System.out.println("-----------------------------");

        Assert.assertEquals(
                Perft.perftTest(1, new Chessboard("rnb2k1r/pp1Pbppp/2p5/q7/2B5/8/PPPQNnPP/RNB1K2R w QK - 3 9"), 39),
                39);

        System.out.println("-----------------------------");

        Assert.assertEquals(
                Perft.perftTest(1, new Chessboard("2r5/3pk3/8/2P5/8/2K5/8/8 w - - 5 4"), 9),
                9);

        System.out.println("-----------------------------");

        Assert.assertEquals(
                Perft.perftTest(3, new Chessboard("rnbq1k1r/pp1Pbppp/2p5/8/2B5/8/PPP1NnPP/RNBQK2R w KQ - 1 8"), 62379),
                62379);

        System.out.println("-----------------------------");

        Assert.assertEquals(
                Perft.perftTest(3, new Chessboard("r4rk1/1pp1qppp/p1np1n2/2b1p1B1/2B1P1b1/P1NP1N2/1PP1QPPP/R4RK1 w - - 0 10"), 89890),
                89890);

    }
    
}
