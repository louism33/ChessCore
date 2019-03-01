package com.github.louism33.chesscore;

import com.github.louism33.utils.ExtendedPositionDescriptionParser;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RunWith(Parameterized.class)
public class CloneTest {

    @Parameterized.Parameters(name = "{index} Test: {1}")
    public static Collection<Object[]> data() {
        List<Object[]> answers = new ArrayList<>();

        for (int i = 0; i < splitUpWACs.length; i++) {
            String splitUpWAC = splitUpWACs[i];
            Object[] objectAndName = new Object[2];
            System.out.println(splitUpWAC);
            ExtendedPositionDescriptionParser.EPDObject EPDObject = ExtendedPositionDescriptionParser.parseEDPPosition(splitUpWAC);
            objectAndName[0] = EPDObject;
            objectAndName[1] = EPDObject.getId();
            answers.add(objectAndName);
        }
        return answers;
    }

    private static ExtendedPositionDescriptionParser.EPDObject EPDObject;

    public CloneTest(Object edp, Object name) {
        EPDObject = (ExtendedPositionDescriptionParser.EPDObject) edp;
    }

    @org.junit.Test
    public void test() {
        final Chessboard board = EPDObject.getBoard();
        
//        MoveParser.printMoves(EPDObject.getBestMoves());
        
        Chessboard board2 = new Chessboard(board);

        Assert.assertEquals(board, board2);
        
    }

    //arasan
    private static final String wacTests = "" +
            "r1bq1r1k/p1pnbpp1/1p2p3/6p1/3PB3/5N2/PPPQ1PPP/2KR3R w - - bm g4; id \"arasan20.1\"; \n" +
            "r1b2rk1/1p1nbppp/pq1p4/3B4/P2NP3/2N1p3/1PP3PP/R2Q1R1K w - - bm Rxf7; id \"arasan20.2\";\n" +
            "r1q1k2r/1p1nbpp1/2p2np1/p1Pp4/3Pp3/P1N1P1P1/1P1B1P1P/R2QRBK1 b kq - bm Bxc5; id \"arasan20.3\";\n" +
            "2rr3k/2qnbppp/p1n1p3/1p1pP3/3P1N2/1Q1BBP2/PP3P1P/1KR3R1 w - - bm Bxh7; id \"arasan20.4\";\n" +
            "3q1r1k/1b3ppp/p1n5/1p1pPB2/2rP4/P6N/1P2Q1PP/R4RK1 w - - bm Qh5; id \"arasan20.5\"; \n" +
            "r1b1k2r/1p1pppb1/p5pp/3P4/q2p1B2/3P1Q2/PPP2PPP/R3R1K1 w kq - bm Rxe7+; id \"arasan20.6\"; \n" +
            "R4bk1/2Bbp2p/2p2pp1/1rPp4/3P4/4P2P/4BPPK/1q1Q4 w - - bm Qa4; id \"arasan20.7\"; \n" +
            "r1r3k1/p3bppp/2bp3Q/q2pP1P1/1p1BP3/8/PPP1B2P/2KR2R1 w - - bm e6; id \"arasan20.8\"; \n" +
            "b2rk1r1/p3q3/2p5/3nPR2/3P2pp/1R1B2P1/P1Q2P2/6K1 w - - bm Bc4; id \"arasan20.9\"; \n" +
            "r2q3r/1p1bbQ2/4p1Bk/3pP3/1n1P1P1p/pP6/Pn4PP/R1B1R1K1 w - - bm g4; id \"arasan20.10\"; \n" +
            "1r2brk1/4n1p1/4p2p/p2pP1qP/2pP1NP1/P1Q1BK2/2P4R/6R1 b - - bm Bg6; id \"arasan20.11\";\n" +
            "1rb2k1r/2q2pp1/p2b3p/2n3B1/2QN4/3B4/PpP3PP/1K2R2R w - - bm Bd8; id \"arasan20.12\";\n" +
            "5rk1/1pp3p1/3ppr1p/pP2p2n/4P2q/P2PQ2P/2P1NPP1/R4RK1 b - - bm Rf3; id \"arasan20.13\"; \n" +
            "r4rk1/1b1n1pb1/3p2p1/1p1Pq1Bp/2p1P3/2P2RNP/1PBQ2P1/5R1K w - - bm Nf5; id \"arasan20.14\";\n" +
            "2kr2r1/ppq1bp1p/4pn2/2p1n1pb/4P1P1/2P2N1P/PPBNQP2/R1B1R1K1 b - - bm Nfxg4; id \"arasan20.15\";\n" +
            "8/3r4/pr1Pk1p1/8/7P/6P1/3R3K/5R2 w - - bm Re2+; id \"arasan20.16\"; \n" +
            "3r1rk1/q4pp1/n1bNp2p/p7/pn2P1N1/6P1/1P1Q1PBP/2RR2K1 w - - bm Nxh6+ e5; id \"arasan20.17\";\n" +
            "r1q2rk1/ppnbbpp1/n4P1p/4P3/3p4/2N1B1PP/PP4BK/R2Q1R2 w - - bm Bxh6; id \"arasan20.18\"; \n" +
            "1R6/5p1k/4bPpp/3pN3/2pP1P1P/2r5/6PK/8 w - - bm h5; id \"arasan20.19\"; \n" +
            "3q1rk1/pr1b1p1p/1bp2p2/2ppP3/8/2P1BN2/PPQ3PP/R4RK1 w - - bm Bh6; id \"arasan20.20\"; \n" +
            "8/5pk1/p4npp/1pPN4/1P2p3/1P4PP/5P2/5K2 w - - bm Nxf6; id \"arasan20.21\"; \n" +
            "8/6p1/P1b1pp2/2p1p3/1k4P1/3PP3/1PK5/5B2 w - - bm Bg2; id \"arasan20.22\"; \n" +
            "r5n1/p1p1q2k/4b2p/3pB3/3PP1pP/8/PPPQ2P1/5RK1 w - - bm Qf4; id \"arasan20.23\"; \n" +
            "2b2rk1/r3q1pp/1nn1p3/3pP1NP/p1pP2Q1/2P1N3/1P1KBP2/R5R1 w - - bm Nxh7; id \"arasan20.24\";\n" +
            "rnb3k1/p3qpr1/2p1p3/2NP3p/1pP3p1/3BQ3/P4PP1/4RRK1 w - - bm Qd4; id \"arasan20.25\"; \n" +
            "r3r1k1/p3bppp/q1b2n2/5Q2/1p1B4/1BNR4/PPP3PP/2K2R2 w - - bm Rg3; id \"arasan20.26\"; ;\n" +
            "2bq1rk1/rpb2p2/2p1p1p1/p1N3Np/P2P1P1P/1Q2R1P1/1P3P2/3R2K1 w - - bm f5; id \"arasan20.27\"; \n" +
            "3q1r1k/2r2pp1/p6p/1pbppP1N/3pP1PP/3P1Q2/PPP4R/5RK1 w - - bm g5; id \"arasan20.28\";\n" +
            "1q6/6k1/5Np1/1r4Pp/2p4P/2Nrb3/PP6/KR5Q b - - bm Bd4; id \"arasan20.29\";\n" +
            "b2rk3/r4p2/p3p3/P3Q1Np/2Pp3P/8/6P1/6K1 w - - bm Qh8+; id \"arasan20.30\";\n" +
            "2kr1b1r/1pp1ppp1/p7/q2P3n/2BB1pb1/2NQ4/P1P1N3/1R3RK1 w - - bm Rxb7; id \"arasan20.31\"; \n" +
            "r4rkq/1ppb4/3P2n1/1N1Pp3/P3Pbn1/3B1NP1/1P2QB2/R4RK1 b - - bm Rf7; id \"arasan20.32\"; \n" +
            "br4k1/1qrnbppp/pp1ppn2/8/NPPBP3/PN3P2/5QPP/2RR1B1K w - - bm Nxb6; id \"arasan20.33\"; \n" +
            "r2q1rk1/ppp2p2/3p1np1/4pNQ1/4P1pP/1PPP4/1P3P2/R3K1R1 w Q - bm Qh6; id \"arasan20.34\";\n" +
            "1qb2rk1/3p1pp1/1p6/1pbBp3/r5p1/3QB3/PPP2P1P/2KR2R1 w - - bm b3; id \"arasan20.35\";\n" +
            "r1b2q1k/2Qn1p1p/1p1Rpp2/p6B/4P2P/6N1/P4PP1/6K1 w - - bm e5; id \"arasan20.36\"; \n" +
            "r2q1rk1/p2pn3/bpp2p1p/3Nb1pQ/7B/8/PPB2PPP/R3R1K1 w - - bm Bxg5; id \"arasan20.37\"; \n" +
            "r4rk1/p4ppp/qp2p3/b5B1/n1R5/5N2/PP2QPPP/1R4K1 w - - bm Bf6; id \"arasan20.38\"; \n" +
            "r2q1rk1/4bppp/3pb3/2n1pP2/1p2P1PP/1P3Q2/1BP1N1B1/2KR3R b - - bm Ra2; id \"arasan20.39\";\n" +
            "2r2rkb/1Q1b3p/p2p3q/2PPnp2/1P2p1p1/2N5/P3BPPB/4RRK1 b - - bm e3; id \"arasan20.40\"; \n" +
            "2b1rk2/5p2/p1P5/2p2P2/2p5/7B/P7/2KR4 w - - bm f6; id \"arasan20.41\";\n" +
            "rn1qr1k1/1p2bppp/p3p3/3pP3/P2P1B2/2RB1Q1P/1P3PP1/R5K1 w - - bm Bxh7+; id \"arasan20.42\"; \n" +
            "1k4rr/p1pq2b1/1p6/1P1pp1p1/6n1/2PP2QN/PN1BP1B1/5RK1 b - - bm e4; id \"arasan20.43\";\n" +
            "1n3rk1/3rbppp/p2p4/4pP2/Ppq1P3/1N2B3/1PP3PP/R2Q1R1K w - - bm f6; id \"arasan20.44\"; \n" +
            "8/2p1k3/3p3p/2PP1pp1/1P1K1P2/6P1/8/8 w - - bm g4; id \"arasan20.45\"; \n" +
            "r1b2rk1/pp2bppp/3p4/q7/3BN1n1/1B3Q2/PPP3PP/R4RK1 w - - bm Qxf7+; id \"arasan20.46\"; \n" +
            "r1b1rk2/p1pq2p1/1p1b1p1p/n2P4/2P1NP2/P2B1R2/1BQ3PP/R6K w - - bm Nxf6; id \"arasan20.47\"; \n" +
            "r2qr3/2p1b1pk/p5pp/1p2p3/nP2P1P1/1BP2RP1/P3QPK1/R1B5 w - - bm Bxh6; id \"arasan20.48\"; \n" +
            "1rbq1rk1/p5bp/3p2p1/2pP4/1p1n1BP1/3P3P/PP2N1B1/1R1Q1RK1 b - - bm Bxg4; id \"arasan20.49\"; \n" +
            "k1b4r/1p3p2/pq2pNp1/5n1p/P3QP2/1P1R1BPP/2P5/1K6 b - - am Nxg3; id \"arasan20.50\";  \n" +
            "7b/8/kq6/8/8/1N2R3/K2P4/8 w - - bm Nd4; id \"arasan20.51\";  \n" +
            "q3nrk1/4bppp/3p4/r3nPP1/4P2P/NpQ1B3/1P4B1/1K1R3R b - - bm Nc7; id \"arasan20.52\";  \n" +
            "2r5/8/6k1/P1p3p1/2R5/1P1q4/1K4Q1/8 w - - bm a6; id \"arasan20.53\";  \n" +
            "8/3R1P2/1ppP1p2/3r4/8/K7/p4k2/8 w - - bm Kb2; id \"arasan20.54\";  \n" +
            "2qrrbk1/1b3ppp/pn1Pp3/6P1/1Pp2B2/1nN2NQB/1P3P1P/3RR1K1 w - - bm g6; id \"arasan20.55\";  \n" +
            "r1q1nrk1/3nb2p/3p2p1/p5P1/1pbNPB2/1N6/PPPQ4/2KR1B1R w - - bm Nf5; id \"arasan20.56\";  \n" +
            "5rk1/pp3ppp/3q4/8/2Pp2b1/P5Pn/PBQPr1BP/4RR1K b - - bm Rxg2; id \"arasan20.57\";  \n" +
            "3b3r/1q3pk1/4b2p/3pPppQ/R1pP1P1P/1rP1N1P1/6N1/2R3K1 w - - bm g4; id \"arasan20.58\";  \n" +
            "r1b2rk1/pp1p2pR/8/1pb2p2/5N2/7Q/qPPB1PPP/6K1 w - - bm g3; id \"arasan20.59\";  \n" +
            "7q/3k2p1/n1p1p1Pr/1pPpPpQ1/3P1N1p/1P2KP2/6P1/7R w - - bm Nxd5; id \"arasan20.60\";  \n" +
            "5rk1/8/pqPp1r1p/1p1Pp1bR/4B3/5PP1/PP2Q1K1/R7 w - - bm Rxg5+; id \"arasan20.61\";  \n" +
            "3r2k1/pb3Np1/4pq1p/2pp1n2/3P4/1PQ5/P4PPP/R2R2K1 b - - bm Nxd4; id \"arasan20.62\";  \n" +
            "2kr1r2/ppq1b1p1/2n5/2PpPb1N/QP1B1pp1/2P5/P2N1P1P/R4RK1 b - - bm Rh8; id \"arasan20.63\";  \n" +
            "r1r2k2/pp2bpp1/2bppn1p/6B1/2qNPPPP/2N5/PPPQ4/1K1RR3 w - - bm f5; id \"arasan20.64\";  \n" +
            "3r2k1/6p1/B1R2p1p/1pPr1P2/3P4/8/1P3nP1/2KR4 w - - bm Rc8; id \"arasan20.65\";  \n" +
            "3qb1k1/5rb1/r3p1Np/1n1pP2P/p1pB1PQ1/2P5/R1B4K/6R1 w - - bm Bc5; id \"arasan20.66\";  \n" +
            "3q1k2/p4pb1/3Pp3/p3P3/r6p/2QB3P/3B1P2/6K1 w - - bm Bb5; id \"arasan20.67\";  \n" +
            "r4r1k/ppqbn1pp/3b1p2/2pP3B/2P4N/7P/P2B1PP1/1R1QR1K1 w - - bm Rxe7; id \"arasan20.68\";  \n" +
            "1r4k1/1q3pp1/r3b2p/p2N4/3R4/QP3P2/2P3PP/1K1R4 w - - bm Nf6+; id \"arasan20.69\";  \n" +
            "r2q1r2/1bp1npk1/3p1p1p/p3p2Q/P3P2N/1BpPP3/1P1N2PP/5RK1 w - - bm Rf3; id \"arasan20.70\";  \n" +
            "2r3k1/1bp3pp/pp1pNn1r/3P1p1q/1PP1pP2/P3P1P1/3Q3P/2RR1BK1 w - - bm c5; id \"arasan20.71\";  \n" +
            "2r3r1/1p1qb2k/p5pp/2n1Bp2/2RP3P/1P2PNQ1/5P1K/3R4 w - - bm Ng5+; id \"arasan20.72\";  \n" +
            "rn3rk1/pp1q3p/4p1B1/2p5/3N1b2/4B3/PPQ2PPP/3R2K1 w - - bm Nf5; id \"arasan20.73\";  \n" +
            "rr5k/1q2pPbp/3p2p1/PbpP4/1nB1nP1Q/1NB5/1P4PP/R4R1K w - - bm f5; id \"arasan20.74\";  \n" +
            "r4rk1/pp1qbppp/1n6/6R1/P1pP4/5Q1P/2B2PP1/2B2RK1 w - - bm Rxg7+; id \"arasan20.75\";  \n" +
            "1r4k1/p7/2P1n1pp/5p2/2QPqP2/PN2p3/5P1P/4RK2 b - - bm Rc8; id \"arasan20.76\";  \n" +
            "1qrrbbk1/1p1nnppp/p3p3/4P3/2P5/1PN1N3/PB2Q1PP/1B2RR1K w - - bm Bxh7+; id \"arasan20.77\";  \n" +
            "r1b2rk1/qp5p/p1n1ppp1/7N/4P1P1/2N1pP2/PPP5/2KR1QR1 w - - bm e5; id \"arasan20.78\";  \n" +
            "3r4/2q5/5pk1/p3n1p1/N3Pp1p/1PPr1P1P/2Q1R1P1/5R1K b - - bm g4; id \"arasan20.79\";  \n" +
            "1q2r1k1/3R1pb1/3R2p1/7p/p3N3/2P1BP1P/1P3PK1/8 b - - bm Rxe4; id \"arasan20.80\";  \n" +
            "r1b1k2r/2q2pp1/p1p1pn2/2b4p/Pp2P3/3B3P/1PP1QPP1/RNB2RK1 b kq - bm Ng4; id \"arasan20.81\";  \n" +
            "2r1rb1k/ppq2pp1/4b2p/3pP2Q/5B2/2PB2R1/P4PPP/1R4K1 w - - bm Rxg7; id \"arasan20.82\";  \n" +
            "6k1/p4qp1/1p3r1p/2pPp1p1/1PP1PnP1/2P1KR1P/1B6/7Q b - - bm h5; id \"arasan20.83\";  \n" +
            "rnb1kb1r/pp1p1ppp/1q2p3/8/3NP1n1/2N1B3/PPP2PPP/R2QKB1R w KQkq - bm Qxg4; id \"arasan20.84\";\n" +
            "r3kb1r/1b1n2p1/p3Nn1p/3Pp3/1p4PP/3QBP2/qPP5/2KR1B1R w kq - bm Qg6+; id \"arasan20.85\";  \n" +
            "1r1qrbk1/pb3p2/2p1pPpp/1p4B1/2pP2PQ/2P5/P4PBP/R3R1K1 w - - bm Bxh6; id \"arasan20.86\";  \n" +
            "2r1r2k/1b1n1p1p/p3pPp1/1p1pP2q/3N4/P3Q1P1/1PP4P/2KRRB2 w - - bm g4; id \"arasan20.87\";  \n" +
            "2r1b1k1/5p2/1R2nB2/1p2P2p/2p5/2Q1P2K/3R1PB1/r3q3 w - - bm Rxe6; id \"arasan20.88\";  \n" +
            "rn2r1k1/ppq1pp1p/2b2bp1/8/2BNPP1B/2P4P/P1Q3P1/1R3RK1 w - - bm Bxf7+; id \"arasan20.89\";  \n" +
            "1kr5/1p3p2/q3p3/pRbpPp2/P1rNnP2/2P1B1Pp/1P2Q2P/R5K1 b - - bm Bxd4; id \"arasan20.90\";  \n" +
            "r3r2k/1pq2pp1/4b2p/3pP3/p1nB3P/P2B1RQ1/1PP3P1/3R3K w - - bm Rf6; id \"arasan20.91\";  \n" +
            "r3brk1/2q1bp1p/pnn3p1/1p1pP1N1/3P4/3B2P1/PP1QNR1P/R1B3K1 w - - bm Nxh7; id \"arasan20.92\";  \n" +
            "1r3r2/q5k1/4p1n1/1bPpPp1p/pPpR1Pp1/P1B1Q3/2B3PP/3R2K1 w - - bm Rxd5; id \"arasan20.93\";  \n" +
            "rq3rk1/1b1n1ppp/ppn1p3/3pP3/5B2/2NBP2P/PP2QPP1/2RR2K1 w - - bm Nxd5; id \"arasan20.94\";  \n" +
            "7r/k4pp1/pn2p1pr/2ppP3/1q3P2/1PN2R1P/P1P2QP1/3R3K w - - bm a3; id \"arasan20.95\";  \n" +
            "1r3rk1/3bbppp/1qn2P2/p2pP1P1/3P4/2PB1N2/6K1/qNBQ1R2 w - - bm Bxh7+; id \"arasan20.96\";  \n" +
            "1r1qrbk1/5ppp/2b1p2B/2npP3/1p4QP/pP1B1N2/P1P2PP1/1K1R3R w - - bm Bxh7+; id \"arasan20.97\";  \n" +
            "r5k1/pbpq1pp1/3b2rp/N3n3/1N6/2P3B1/PP1Q1PPP/R4RK1 b - - bm Rxg3; id \"arasan20.98\";  \n" +
            "1r2r1k1/2R2p2/1N1Rp2p/p2b3P/4pPP1/8/P4K2/8 w - - bm g5; id \"arasan20.99\";  \n" +
            "r4r2/pp1b1ppk/2n1p3/3pPnB1/q1pP2QP/P1P4R/2PKNPP1/R7 w - - bm Qh5+; id \"arasan20.100\";  \n" +
            "8/2k2Bp1/2n5/p1P4p/4pPn1/P3PqPb/1r1BQ2P/2R1K1R1 b - - bm Nce5; id \"arasan20.101\";  \n" +
            "8/5kpp/8/8/8/5P2/1RPK2PP/6r1 w - - bm c4; id \"arasan20.102\";  \n" +
            "r3rnk1/pp2ppb1/1np3p1/3qP2p/3P1B2/4Q1N1/PP2BPP1/1K1R3R w - - bm Bh6; id \"arasan20.103\";  \n" +
            "1r1q2k1/2r3bp/B2p1np1/3P1p2/R1P1pP2/4B2P/P5PK/3Q1R2 b - - bm Ng4+; id \"arasan20.104\";  \n" +
            "2r1rnk1/1p2pp1p/p1np2p1/q4PP1/3NP2Q/4B2R/PPP4P/3R3K w - - bm b4; id \"arasan20.105\";  \n" +
            "2b2qk1/1r4pp/2p1p3/p2n1PPB/2p4P/2p5/P4Q2/4RRK1 w - - bm Qg3; id \"arasan20.106\";  \n" +
            "1r1rkb2/2q2p2/p2p1P1B/P1pPp2Q/2P3b1/1P6/2B3PP/5R1K w - - bm Qxg4; id \"arasan20.107\";  \n" +
            "r4rk1/3b3p/p1pb4/1p1n2p1/2P2p2/1B1P2Pq/PP1NRP1P/R1BQ2K1 w - - bm Qf1; id \"arasan20.108\";  \n" +
            "1r3rk1/4bpp1/p3p2p/q1PpPn2/bn3Q1P/1PN1BN2/2P1BPP1/1KR2R2 b - - bm Bxb3; id \"arasan20.109\";  \n" +
            "2nb2k1/1rqb1pp1/p2p1n1p/2pPp3/P1P1P3/2B1NN1P/2B2PP1/Q3R2K w - - bm Nxe5; id \"arasan20.110\";  \n" +
            "3r2k1/p1qn1p1p/4p1p1/2p1N3/8/2P3P1/PP2QP1P/4R1K1 w - - bm Nxf7; id \"arasan20.111\";  \n" +
            "r2q1rk1/pb1nbp1p/1pp1pp2/8/2BPN2P/5N2/PPP1QPP1/2KR3R w - - bm Nfg5; id \"arasan20.112\";  \n" +
            "4rr2/3bp1bk/p2q1np1/2pPp2p/2P4P/1R4N1/1P1BB1P1/1Q3RK1 w - - bm Bxh5; id \"arasan20.113\";  \n" +
            "8/8/4b1p1/2Bp3p/5P1P/1pK1Pk2/8/8 b - - bm g5 d4+; id \"arasan20.114\";  \n" +
            "8/5p2/3p2p1/1bk4p/p2pBNnP/P5P1/1P3P2/4K3 b - - bm d3; id \"arasan20.115\";  \n" +
            "8/4nk2/1p3p2/1r1p2pp/1P1R1N1P/6P1/3KPP2/8 w - - bm Nd3; id \"arasan20.116\";  \n" +
            "6k1/1bq1bpp1/p6p/2p1pP2/1rP1P1P1/2NQ4/2P4P/K2RR3 b - - bm Bd5; id \"arasan20.117\";  \n" +
            "r3r1k1/1bqnbp1p/pp1pp1p1/6P1/Pn2PP1Q/1NN1BR2/1PPR2BP/6K1 w - - bm Rh3; id \"arasan20.118\";  \n" +
            "4rrk1/1pp1n1pp/1bp1P2q/p4p2/P4P2/3R2N1/1PP2P1P/2BQRK2 w - - bm Nh5; id \"arasan20.119\";  \n" +
            "3q4/4k3/1p1b1p1r/p2Q4/3B1p1p/7P/1P4P1/3R3K w - - bm b4; id \"arasan20.120\";  \n" +
            "8/5p1k/6p1/1p1Q3p/3P4/1R2P1KP/6P1/r4q2 b - - bm h4+; id \"arasan20.121\";  \n" +
            "7k/3q1pp1/1p3r2/p1bP4/P1P2p2/1P2rNpP/2Q3P1/4RR1K b - - bm Rxf3; id \"arasan20.122\";  \n" +
            "3r3r/k1p2pb1/B1b2q2/2RN3p/3P2p1/1Q2B1Pn/PP3PKP/5R2 w - - bm Rfc1; id \"arasan20.123\";  \n" +
            "r1b3kr/pp1n2Bp/2pb2q1/3p3N/3P4/2P2Q2/P1P3PP/4RRK1 w - - bm Re5; id \"arasan20.124\";  \n" +
            "2r3k1/1q3pp1/2n1b2p/4P3/3p1BP1/Q6P/1p3PB1/1R4K1 b - - bm Rb8; id \"arasan20.125\";  \n" +
            "rn2kb1r/1b1n1p1p/p3p1p1/1p2q1B1/3N3Q/2N5/PPP3PP/2KR1B1R w kq - bm Nxe6; id \"arasan20.126\";  \n" +
            "r7/ppp3kp/2bn4/4qp2/2B1pR2/2P1Q2P/P5P1/5RK1 w - - bm Rxf5; id \"arasan20.127\";  \n" +
            "1r6/r6k/2np2p1/2pNp1qp/1pP1Pp1b/1P1P1P2/1B3P2/1Q1R1K1R b - - bm Bxf2; id \"arasan20.128\";  \n" +
            "1nr3k1/q4rpp/1p1p1n2/3Pp3/1PQ1P1b1/4B1P1/2R2NBP/2R3K1 w - - bm Qxc8+; id \"arasan20.129\";  \n" +
            "8/5rk1/p2p4/1p1P1b1p/1P1K2pP/2P3P1/4R3/5B2 w - - bm Rf2; id \"arasan20.130\";  \n" +
            "5rk1/2p1R2p/r5q1/2pPR2p/5p2/1p5P/P4PbK/2BQ4 w - - bm d6; id \"arasan20.131\";  \n" +
            "r2q1r2/1b2bpkp/p3p1p1/2ppP1P1/7R/1PN1BQR1/1PP2P1P/4K3 w - - bm Qf6+; id \"arasan20.132\";  \n" +
            "r1r3k1/1ppn2bp/p1q1p1p1/3pP3/3PB1P1/PQ3NP1/3N4/2BK3R w - - bm Ng5; id \"arasan20.133\";  \n" +
            "1rr1b1k1/1pq1bp2/p2p1np1/4p3/P2BP3/2NB2Q1/1PP3PP/4RR1K w - - bm Rxf6; id \"arasan20.134\";  \n" +
            "r1r3k1/1q3p1p/4p1pP/1bnpP1P1/pp1Q1P2/1P6/P1PR1N2/1K3B1R b - - bm axb3; id \"arasan20.135\";  \n" +
            "r1b2rk1/pppnq3/4ppp1/6N1/3P3Q/2PB4/P1PK2PP/3R3R w - - bm Nxe6; id \"arasan20.136\";  \n" +
            "3r1r1k/pp5p/4b1pb/6q1/3P4/4p1BP/PP2Q1PK/3RRB2 b - - bm Qxg3+; id \"arasan20.137\";  \n" +
            "r2r2k1/3bb1Pp/3pp1p1/p1q5/1p2PP2/P1N5/1PPQ4/1K1R1B1R w - - bm Nd5; id \"arasan20.138\";  \n" +
            "8/2R5/3p4/3P4/3k3P/2p3K1/1r4P1/8 w - - bm Kf3; id \"arasan20.139\";  \n" +
            "r1bq2k1/1pp2ppp/3prn2/p3n3/2P5/PQN1PP2/1P1PB2P/R1B2R1K b - - bm Nfg4; id \"arasan20.140\";  \n" +
            "2kr3r/pp4pp/4pp2/2pq4/P1Nn4/4Q3/KP2B1PP/2RR4 b - - am Qxg2; id \"arasan20.141\";  \n" +
            "5r2/1p4k1/pP1pP1pp/2rP2q1/4Qp2/3Bb3/P5PP/4RR1K w - - bm Rf3; id \"arasan20.142\";  \n" +
            "r2qr1k1/1b1pppbp/1p4p1/pP2P1B1/3N4/R7/1PP2PPP/3QR1K1 w - a6 bm Nf5; id \"arasan20.143\";  \n" +
            "4k3/1R6/Pb3p2/1P1n4/5p2/8/4K3/8 w - - bm Kd3; id \"arasan20.144\";  \n" +
            "r4nk1/2pq1ppp/3p4/p3pNPQ/4P3/2PP1RP1/Pr3PK1/7R w - - bm Ne3; id \"arasan20.145\";  \n" +
            "r1q2rk1/1b2bppp/p1p1p3/4B3/PP6/3B3P/2P1QPP1/R2R2K1 w - - bm Bxh7+; id \"arasan20.146\";  \n" +
            "r2qrb1k/1p1b2p1/p2ppn1p/8/3NP3/1BN5/PPP3QP/1K3RR1 w - - bm e5; id \"arasan20.147\";  \n" +
            "r2q1k1r/pp2n1pp/2nb1p2/1B1p3Q/N2P4/2P1B3/PP4PP/R4RK1 w - - bm Rxf6+; id \"arasan20.148\";  \n" +
            "4r1k1/6p1/bp2r2p/3QNp2/P2BnP2/4P2P/5qPK/3RR3 b - - bm Kh7; id \"arasan20.149\";  \n" +
            "8/5rk1/p3Q1pp/1p1P1p1b/2p1Pq1P/P4P2/1PKN4/5R2 w - - bm d6; id \"arasan20.150\";  \n" +
            "r1bqkb1r/4pppp/p1p5/2ppP3/8/2P2N2/PPP2PPP/R1BQR1K1 w kq - bm e6; id \"arasan20.151\";  \n" +
            "3r1rk1/1b2bpp1/2n1p2p/qp1n2N1/4N3/P3P3/1BB1QPPP/2R2RK1 w - - bm Qh5; id \"arasan20.152\";  \n" +
            "3R4/pp2r1pk/q1p3bp/2P2r2/PP6/2Q3P1/6BP/5RK1 w - - bm Rxf5; id \"arasan20.153\";  \n" +
            "r3k3/1p4p1/1Bb1Bp1p/P1p1bP1P/2Pp2P1/3P4/5K2/4R3 w - - bm g5; id \"arasan20.154\";  \n" +
            "1r1rb1k1/5ppp/4p3/1p1p3P/1q2P2Q/pN3P2/PPP4P/1K1R2R1 w - - bm Rxg7+; id \"arasan20.155\";  \n" +
            "1r1q1rk1/4bp1p/n3p3/pbNpP1PB/5P2/1P2B1K1/1P1Q4/2RR4 w - - bm Ne4; id \"arasan20.156\";  \n" +
            "r1bq1rk1/pp2bppp/1n2p3/3pP3/8/2RBBN2/PP2QPPP/2R3K1 w - - bm Bxh7+; id \"arasan20.157\";  \n" +
            "r6k/N1Rb2bp/p2p1nr1/3Pp2q/1P2Pp1P/5N2/P3QBP1/4R1K1 b - - bm Bh3; id \"arasan20.158\";  \n" +
            "r1b2rk1/1pq1nppp/pbn1p3/8/3N4/3BBN2/PPP1QPPP/3R1RK1 w - - bm Bxh7+; id \"arasan20.159\";  \n" +
            "3r1rk1/1b2qp1p/1p3np1/1N1p4/6n1/2NBP1K1/PBQ2PP1/3RR3 b - - bm d4; id \"arasan20.160\";  \n" +
            "br3bk1/3r1p2/3q2p1/3P2Np/2B4P/3QR1P1/3R1P1K/8 w - - bm Nxf7; id \"arasan20.161\";  \n" +
            "r3r2k/ppq3np/2p3p1/NPPp1bb1/P2Pnp2/3B1P2/2Q3PP/1RN1BRK1 b - - bm Ng3; id \"arasan20.162\";  \n" +
            "7k/5rp1/3q1p1p/2bNpQ1P/4P1P1/8/1R3PK1/8 w - - bm g5; id \"arasan20.163\";  \n" +
            "4r3/4r3/1ppqpnk1/p3Rp1p/P2P1R1Q/2PB2P1/1P3P2/6K1 w - - bm Bxf5+; id \"arasan20.164\";  \n" +
            "r3nrk1/1pqbbppp/p2pp3/2n1P3/5P2/2NBBNQ1/PPP3PP/R4RK1 w - - bm Bxh7; id \"arasan20.165\";  \n" +
            "rnbq3r/ppp2kpp/4pp2/3n4/2BP4/BQ3N2/P4PPP/4RRK1 w - - bm Ng5+; id \"arasan20.166\";  \n" +
            "8/2N5/1P2p3/5bPk/1q3b2/3Bp2P/2P5/6QK b - - bm Kh4; id \"arasan20.167\";  \n" +
            "1k1r1b1r/1p6/p4pp1/P1p1p3/2NpP1p1/1PPP2Pq/1B3P1P/2RQR1K1 b - - bm f5; id \"arasan20.168\";  \n" +
            "5r2/3rkp2/2R2p2/p2Bb2Q/1p2P2P/4q1P1/Pp6/1K1R4 b - - bm b3; id \"arasan20.169\";  \n" +
            "5rk1/qp1b1rnp/4p1p1/p2pB3/8/1R1B4/PP1QRPPP/6K1 w - - bm Bxg6; id \"arasan20.170\";  \n" +
            "6k1/5r1p/1p2Q1p1/p7/P1P2P2/2K1R1P1/2N2qb1/8 w - - bm Qd6 Qe8+; id \"arasan20.171\";  \n" +
            "4r1k1/1p4p1/p1qBp1Qp/b1pnP3/8/5NP1/1P3PKP/3R4 w - - bm Rxd5; id \"arasan20.172\";  \n" +
            "2r1k2r/pp1bb1pp/6n1/3Q1p2/1B1N4/P7/1q4PP/4RRK1 w k - bm Bxe7; id \"arasan20.173\";  \n" +
            "3b2k1/4qp2/2P4Q/3B3p/1P6/1K6/8/8 w - - bm Bc4; id \"arasan20.174\";  \n" +
            "1r2brk1/6p1/1q2p1Pp/pN1pPPb1/np1N4/5Q2/1PP1B3/1K1R3R w - - bm f6; id \"arasan20.175\";  \n" +
            "2rq1Nk1/pb3pp1/4p3/1p6/3b1Pn1/P1N5/1PQ3PP/R1B2R1K b - - bm f5; id \"arasan20.176\";  \n" +
            "r1b2rk1/1p4p1/p1n1p3/3p1pB1/NqP3n1/b2BP3/1PQN1P1P/1K4RR w - - bm Rxg4; id \"arasan20.177\";  \n" +
            "q2rn1k1/1b3p1p/1p4p1/2n1B1P1/r1PN3P/P4P2/4Q1B1/3RR1K1 w - - bm Bf6; id \"arasan20.178\";  \n" +
            "r1b3r1/5p1k/p1n2P1p/P1qpp1P1/1p1p4/3P2Q1/BPPB2P1/R4RK1 w - - bm Kf2; id \"arasan20.179\";  \n" +
            "r2q1rk1/2p2ppp/pb1p1n2/n3p3/P2PP3/2P2NN1/R4PPP/2BQ1RK1 w - - bm Bg5; id \"arasan20.180\";  \n" +
            "1r2rbk1/1p1n1p2/p3b1p1/q2NpNPp/4P2Q/1P5R/6BP/5R1K w - h6 bm Ng3; id \"arasan20.181\";  \n" +
            "r4rk1/1bqnppBp/pp1p1np1/8/P2pP3/2N1QN1P/1PP1BPP1/R4RK1 w - - bm Qh6; id \"arasan20.182\";  \n" +
            "5b2/1b2qp1k/2pp1npp/1p6/1P2PP2/r1PQ2NP/2B3P1/3RB1K1 w - - bm e5; id \"arasan20.183\";  \n" +
            "r1qr1bk1/2p2pp1/ppn1p2p/8/1PPPN1nP/P4NP1/2Q2PK1/2BRR3 w - - bm Neg5; id \"arasan20.184\";  \n" +
            "r1b2r1k/4qp1p/p2ppb1Q/4nP2/1p1NP3/2N5/PPP4P/2KR1BR1 w - - bm Nc6; id \"arasan20.185\";  \n" +
            "5rk1/1p3n2/1q2pB2/1P1p1b1p/5Q1P/3p1NP1/5P2/2R3K1 w - - bm Ne5; id \"arasan20.186\";  \n" +
            "8/2k5/2PrR1p1/7p/5p1P/5P1K/6P1/8 w - - bm Rxd6; id \"arasan20.187\";  \n" +
            "8/4bBpp/3p4/P6P/2PN2p1/3k1b2/P7/6K1 w - - bm h6; id \"arasan20.188\";  \n" +
            "4K1k1/8/1p5p/1Pp3b1/8/1P3P2/P1B2P2/8 w - - bm f4; id \"arasan20.189\";  \n" +
            "5rn1/1p3p1k/r5pp/p1ppPPq1/6N1/1PPP3Q/1P5P/R4R1K w - - bm e6; id \"arasan20.190\";  \n" +
            "8/k3qrpR/1p1p4/p2QpPp1/P1P1P1K1/1P6/8/8 w - - bm b4; id \"arasan20.191\";  \n" +
            "3r1rk1/pbq1bp1p/1n1Rp1p1/2p1P1N1/4N2P/1P3Q2/PB3PP1/K6R w - - bm h5; id \"arasan20.192\";  \n" +
            "r2qk2r/2p1bpp1/p5B1/1p1pP3/3P2p1/5PnP/PP3R2/RNBQ2K1 b kq - bm Rxh3; id \"arasan20.193\";  \n" +
            "rn2r1k1/p4pn1/1p2p3/qPppP1Q1/3P4/2P2N2/2P2PPP/1R3RK1 w - - bm Nh4; id \"arasan20.194\";  \n" +
            "k6r/ppqb4/2n5/4p2r/P2p1P1P/B1pQ2P1/2P3B1/RR4K1 w - - bm a5; id \"arasan20.195\";  \n" +
            "1r1q2k1/p4p1p/2Pp2p1/2p1P3/1r1n4/1P4P1/3R1PBP/3QR1K1 w - - bm e6; id \"arasan20.196\";  \n" +
            "1rr5/5R2/6k1/3B2P1/1p2P1n1/1PpK4/8/R7 w - - bm Ra6+; id \"arasan20.197\";  \n" +
            "b1r1r1k1/p2n1p2/1p5p/2qp1Rn1/2P3pN/6P1/P2N1P1P/Q3RBK1 b - - bm Qb4; id \"arasan20.198\";  \n" +
            "1q4rk/R1nbp3/1n1p3p/QP1P4/3pPp2/2N2P1P/1P1N3K/5B2 w - - bm Nb3; id \"arasan20.199\";  \n" +
            "4rrk1/1bq1pp2/p2p1n1Q/1pn2p1p/4P3/P1N2P2/BPP3PP/2KRR3 w - - bm g4; id \"arasan20.200\";  " +
            "";
    
    private static final String wacTests1 = "" +
            "2rr3k/pp3pp1/1nnqbN1p/3pN3/2pP4/2P3Q1/PPB4P/R4RK1 w - - bm Qg6; id \"WAC.001\";\n" +
            "8/7p/5k2/5p2/p1p2P2/Pr1pPK2/1P1R3P/8 b - - bm Rxb2; id \"WAC.002\";\n" +
            "5rk1/1ppb3p/p1pb4/6q1/3P1p1r/2P1R2P/PP1BQ1P1/5RKN w - - bm Rg3; id \"WAC.003\";\n" +
            "r1bq2rk/pp3pbp/2p1p1pQ/7P/3P4/2PB1N2/PP3PPR/2KR4 w - - bm Qxh7+; id \"WAC.004\";\n" +
            "5k2/6pp/p1qN4/1p1p4/3P4/2PKP2Q/PP3r2/3R4 b - - bm Qc4+; id \"WAC.005\";\n" +
            "7k/p7/1R5K/6r1/6p1/6P1/8/8 w - - bm Rb7; id \"WAC.006\";\n" +
            "rnbqkb1r/pppp1ppp/8/4P3/6n1/7P/PPPNPPP1/R1BQKBNR b KQkq - bm Ne3; id \"WAC.007\";\n" +
            "r4q1k/p2bR1rp/2p2Q1N/5p2/5p2/2P5/PP3PPP/R5K1 w - - bm Rf7; id \"WAC.008\";\n" +
            "3q1rk1/p4pp1/2pb3p/3p4/6Pr/1PNQ4/P1PB1PP1/4RRK1 b - - bm Bh2+; id \"WAC.009\";\n" +
            "2br2k1/2q3rn/p2NppQ1/2p1P3/Pp5R/4P3/1P3PPP/3R2K1 w - - bm Rxh7; id \"WAC.010\";\n" +
            "r1b1kb1r/3q1ppp/pBp1pn2/8/Np3P2/5B2/PPP3PP/R2Q1RK1 w kq - bm Bxc6; id \"WAC.011\";\n" +
            "4k1r1/2p3r1/1pR1p3/3pP2p/3P2qP/P4N2/1PQ4P/5R1K b - - bm Qxf3+; id \"WAC.012\";\n" +
            "5rk1/pp4p1/2n1p2p/2Npq3/2p5/6P1/P3P1BP/R4Q1K w - - bm Qxf8+; id \"WAC.013\";\n" +
            "r2rb1k1/pp1q1p1p/2n1p1p1/2bp4/5P2/PP1BPR1Q/1BPN2PP/R5K1 w - - bm Qxh7+; id \"WAC.014\";\n" +
            "1R6/1brk2p1/4p2p/p1P1Pp2/P7/6P1/1P4P1/2R3K1 w - - bm Rxb7; id \"WAC.015\";\n" +
            "r4rk1/ppp2ppp/2n5/2bqp3/8/P2PB3/1PP1NPPP/R2Q1RK1 w - - bm Nc3; id \"WAC.016\";\n" +
            "1k5r/pppbn1pp/4q1r1/1P3p2/2NPp3/1QP5/P4PPP/R1B1R1K1 w - - bm Ne5; id \"WAC.017\";\n" +
            "R7/P4k2/8/8/8/8/r7/6K1 w - - bm Rh8; id \"WAC.018\";\n" +
            "r1b2rk1/ppbn1ppp/4p3/1QP4q/3P4/N4N2/5PPP/R1B2RK1 w - - bm c6; id \"WAC.019\";\n" +
            "r2qkb1r/1ppb1ppp/p7/4p3/P1Q1P3/2P5/5PPP/R1B2KNR b kq - bm Bb5; id \"WAC.020\";\n" +
            "5rk1/1b3p1p/pp3p2/3n1N2/1P6/P1qB1PP1/3Q3P/4R1K1 w - - bm Qh6; id \"WAC.021\";\n" +
            "r1bqk2r/ppp1nppp/4p3/n5N1/2BPp3/P1P5/2P2PPP/R1BQK2R w KQkq - bm Ba2 Nxf7; id \"WAC.022\";\n" +
            "r3nrk1/2p2p1p/p1p1b1p1/2NpPq2/3R4/P1N1Q3/1PP2PPP/4R1K1 w - - bm g4; id \"WAC.023\";\n" +
            "6k1/1b1nqpbp/pp4p1/5P2/1PN5/4Q3/P5PP/1B2B1K1 b - - bm Bd4; id \"WAC.024\";\n" +
            "3R1rk1/8/5Qpp/2p5/2P1p1q1/P3P3/1P2PK2/8 b - - bm Qh4+; id \"WAC.025\";\n" +
            "3r2k1/1p1b1pp1/pq5p/8/3NR3/2PQ3P/PP3PP1/6K1 b - - bm Bf5; id \"WAC.026\";\n" +
            "7k/pp4np/2p3p1/3pN1q1/3P4/Q7/1r3rPP/2R2RK1 w - - bm Qf8+; id \"WAC.027\";\n" +
            "1r1r2k1/4pp1p/2p1b1p1/p3R3/RqBP4/4P3/1PQ2PPP/6K1 b - - bm Qe1+; id \"WAC.028\";\n" +
            "r2q2k1/pp1rbppp/4pn2/2P5/1P3B2/6P1/P3QPBP/1R3RK1 w - - bm c6; id \"WAC.029\";\n" +
            "1r3r2/4q1kp/b1pp2p1/5p2/pPn1N3/6P1/P3PPBP/2QRR1K1 w - - bm Nxd6; id \"WAC.030\";\n" +
            "rb3qk1/pQ3ppp/4p3/3P4/8/1P3N2/1P3PPP/3R2K1 w - - bm Qxa8 d6 dxe6 g3; id \"WAC.031\";\n" +
            "6k1/p4p1p/1p3np1/2q5/4p3/4P1N1/PP3PPP/3Q2K1 w - - bm Qd8+; id \"WAC.032\";\n" +
            "8/p1q2pkp/2Pr2p1/8/P3Q3/6P1/5P1P/2R3K1 w - - bm Qe5+ Qf4; id \"WAC.033\";\n" +
            "7k/1b1r2p1/p6p/1p2qN2/3bP3/3Q4/P5PP/1B1R3K b - - bm Bg1; id \"WAC.034\";\n" +
            "r3r2k/2R3pp/pp1q1p2/8/3P3R/7P/PP3PP1/3Q2K1 w - - bm Rxh7+; id \"WAC.035\";\n" +
            "3r4/2p1rk2/1pQq1pp1/7p/1P1P4/P4P2/6PP/R1R3K1 b - - bm Re1+; id \"WAC.036\";\n" +
            "2r5/2rk2pp/1pn1pb2/pN1p4/P2P4/1N2B3/nPR1KPPP/3R4 b - - bm Nxd4+; id \"WAC.037\";\n" +
            "4k3/p4prp/1p6/2b5/8/2Q3P1/P2R1PKP/4q3 w - - bm Qd3 Rd8+; id \"WAC.038\";\n" +
            "r1br2k1/pp2bppp/2nppn2/8/2P1PB2/2N2P2/PqN1B1PP/R2Q1R1K w - - bm Na4; id \"WAC.039\";\n" +
            "3r1r1k/1p4pp/p4p2/8/1PQR4/6Pq/P3PP2/2R3K1 b - - bm Rc8; id \"WAC.040\";\n" +
            "1k6/5RP1/1P6/1K6/6r1/8/8/8 w - - bm Ka5 Kc5 b7; id \"WAC.041\";\n" +
            "r1b1r1k1/pp1n1pbp/1qp3p1/3p4/1B1P4/Q3PN2/PP2BPPP/R4RK1 w - - bm Ba5; id \"WAC.042\";\n" +
            "r2q3k/p2P3p/1p3p2/3QP1r1/8/B7/P5PP/2R3K1 w - - bm Be7 Qxa8; id \"WAC.043\";\n" +
            "3rb1k1/pq3pbp/4n1p1/3p4/2N5/2P2QB1/PP3PPP/1B1R2K1 b - - bm dxc4; id \"WAC.044\";\n" +
            "7k/2p1b1pp/8/1p2P3/1P3r2/2P3Q1/1P5P/R4qBK b - - bm Qxa1; id \"WAC.045\";\n" +
            "r1bqr1k1/pp1nb1p1/4p2p/3p1p2/3P4/P1N1PNP1/1PQ2PP1/3RKB1R w K - bm Nb5; id \"WAC.046\";\n" +
            "r1b2rk1/pp2bppp/2n1pn2/q5B1/2BP4/2N2N2/PP2QPPP/2R2RK1 b - - bm Nxd4; id \"WAC.047\";\n" +
            "1rbq1rk1/p1p1bppp/2p2n2/8/Q1BP4/2N5/PP3PPP/R1B2RK1 b - - bm Rb4; id \"WAC.048\";\n" +
            "2b3k1/4rrpp/p2p4/2pP2RQ/1pP1Pp1N/1P3P1P/1q6/6RK w - - bm Qxh7+; id \"WAC.049\";\n" +
            "k4r2/1R4pb/1pQp1n1p/3P4/5p1P/3P2P1/r1q1R2K/8 w - - bm Rxb6+; id \"WAC.050\";\n" +
            "r1bq1r2/pp4k1/4p2p/3pPp1Q/3N1R1P/2PB4/6P1/6K1 w - - bm Rg4+; id \"WAC.051\";\n" +
            "r1k5/1p3q2/1Qpb4/3N1p2/5Pp1/3P2Pp/PPPK3P/4R3 w - - bm Re7 c4; id \"WAC.052\";\n" +
            "6k1/6p1/p7/3Pn3/5p2/4rBqP/P4RP1/5QK1 b - - bm Re1; id \"WAC.053\";\n" +
            "r3kr2/1pp4p/1p1p4/7q/4P1n1/2PP2Q1/PP4P1/R1BB2K1 b q - bm Qh1+; id \"WAC.054\";\n" +
            "r3r1k1/pp1q1pp1/4b1p1/3p2B1/3Q1R2/8/PPP3PP/4R1K1 w - - bm Qxg7+; id \"WAC.055\";\n" +
            "r1bqk2r/pppp1ppp/5n2/2b1n3/4P3/1BP3Q1/PP3PPP/RNB1K1NR b KQkq - bm Bxf2+; id \"WAC.056\";\n" +
            "r3q1kr/ppp5/3p2pQ/8/3PP1b1/5R2/PPP3P1/5RK1 w - - bm Rf8+; id \"WAC.057\";\n" +
            "8/8/2R5/1p2qp1k/1P2r3/2PQ2P1/5K2/8 w - - bm Qd1+; id \"WAC.058\";\n" +
            "r1b2rk1/2p1qnbp/p1pp2p1/5p2/2PQP3/1PN2N1P/PB3PP1/3R1RK1 w - - bm Nd5; id \"WAC.059\";\n" +
            "rn1qr1k1/1p2np2/2p3p1/8/1pPb4/7Q/PB1P1PP1/2KR1B1R w - - bm Qh8+; id \"WAC.060\";\n" +
            "3qrbk1/ppp1r2n/3pP2p/3P4/2P4P/1P3Q2/PB6/R4R1K w - - bm Qf7+; id \"WAC.061\";\n" +
            "6r1/3Pn1qk/p1p1P1rp/2Q2p2/2P5/1P4P1/P3R2P/5RK1 b - - bm Rxg3+; id \"WAC.062\";\n" +
            "r1brnbk1/ppq2pp1/4p2p/4N3/3P4/P1PB1Q2/3B1PPP/R3R1K1 w - - bm Nxf7; id \"WAC.063\";\n" +
            "8/6pp/3q1p2/3n1k2/1P6/3NQ2P/5PP1/6K1 w - - bm g4+; id \"WAC.064\";\n" +
            "1r1r1qk1/p2n1p1p/bp1Pn1pQ/2pNp3/2P2P1N/1P5B/P6P/3R1RK1 w - - bm Ne7+; id \"WAC.065\";\n" +
            "1k1r2r1/ppq5/1bp4p/3pQ3/8/2P2N2/PP4P1/R4R1K b - - bm Qxe5; id \"WAC.066\";\n" +
            "3r2k1/p2q4/1p4p1/3rRp1p/5P1P/6PK/P3R3/3Q4 w - - bm Rxd5; id \"WAC.067\";\n" +
            "6k1/5ppp/1q6/2b5/8/2R1pPP1/1P2Q2P/7K w - - bm Qxe3; id \"WAC.068\";\n" +
            "2k5/pppr4/4R3/4Q3/2pp2q1/8/PPP2PPP/6K1 w - - bm f3 h3; id \"WAC.069\";\n" +
            "2kr3r/pppq1ppp/3p1n2/bQ2p3/1n1PP3/1PN1BN1P/1PP2PP1/2KR3R b - - bm Na2+; id \"WAC.070\";\n" +
            "2kr3r/pp1q1ppp/5n2/1Nb5/2Pp1B2/7Q/P4PPP/1R3RK1 w - - bm Nxa7+; id \"WAC.071\";\n" +
            "r3r1k1/pp1n1ppp/2p5/4Pb2/2B2P2/B1P5/P5PP/R2R2K1 w - - bm e6; id \"WAC.072\";\n" +
            "r1q3rk/1ppbb1p1/4Np1p/p3pP2/P3P3/2N4R/1PP1Q1PP/3R2K1 w - - bm Qd2; id \"WAC.073\";\n" +
            "5r1k/pp4pp/2p5/2b1P3/4Pq2/1PB1p3/P3Q1PP/3N2K1 b - - bm Qf1+; id \"WAC.074\";\n" +
            "r3r1k1/pppq1ppp/8/8/1Q4n1/7P/PPP2PP1/RNB1R1K1 b - - bm Qd6; id \"WAC.075\";\n" +
            "r1b1qrk1/2p2ppp/pb1pnn2/1p2pNB1/3PP3/1BP5/PP2QPPP/RN1R2K1 w - - bm Bxf6; id \"WAC.076\";\n" +
            "3r2k1/ppp2ppp/6q1/b4n2/3nQB2/2p5/P4PPP/RN3RK1 b - - bm Ng3; id \"WAC.077\";\n" +
            "r2q3r/ppp2k2/4nbp1/5Q1p/2P1NB2/8/PP3P1P/3RR1K1 w - - bm Ng5+; id \"WAC.078\";\n" +
            "r3k2r/pbp2pp1/3b1n2/1p6/3P3p/1B2N1Pq/PP1PQP1P/R1B2RK1 b kq - bm Qxh2+; id \"WAC.079\";\n" +
            "r4rk1/p1B1bpp1/1p2pn1p/8/2PP4/3B1P2/qP2QP1P/3R1RK1 w - - bm Ra1; id \"WAC.080\";\n" +
            "r4rk1/1bR1bppp/4pn2/1p2N3/1P6/P3P3/4BPPP/3R2K1 b - - bm Bd6; id \"WAC.081\";\n" +
            "3rr1k1/pp3pp1/4b3/8/2P1B2R/6QP/P3q1P1/5R1K w - - bm Bh7+; id \"WAC.082\";\n" +
            "3rr1k1/ppqbRppp/2p5/8/3Q1n2/2P3N1/PPB2PPP/3R2K1 w - - bm Qxd7; id \"WAC.083\";\n" +
            "r2q1r1k/2p1b1pp/p1n5/1p1Q1bN1/4n3/1BP1B3/PP3PPP/R4RK1 w - - bm Qg8+; id \"WAC.084\";\n" +
            "kr2R3/p4r2/2pq4/2N2p1p/3P2p1/Q5P1/5P1P/5BK1 w - - bm Na6; id \"WAC.085\";\n" +
            "8/p7/1ppk1n2/5ppp/P1PP4/2P1K1P1/5N1P/8 b - - bm Ng4+; id \"WAC.086\";\n" +
            "8/p3k1p1/4r3/2ppNpp1/PP1P4/2P3KP/5P2/8 b - - bm Rxe5; id \"WAC.087\";\n" +
            "r6k/p1Q4p/2p1b1rq/4p3/B3P3/4P3/PPP3P1/4RRK1 b - - bm Rxg2+; id \"WAC.088\";\n" +
            "1r3b1k/p4rpp/4pp2/3q4/2ppbPPQ/6RK/PP5P/2B1NR2 b - - bm g5; id \"WAC.089\";\n" +
            "3qrrk1/1pp2pp1/1p2bn1p/5N2/2P5/P1P3B1/1P4PP/2Q1RRK1 w - - bm Nxg7; id \"WAC.090\";\n" +
            "2qr2k1/4b1p1/2p2p1p/1pP1p3/p2nP3/PbQNB1PP/1P3PK1/4RB2 b - - bm Be6; id \"WAC.091\";\n" +
            "r4rk1/1p2ppbp/p2pbnp1/q7/3BPPP1/2N2B2/PPP4P/R2Q1RK1 b - - bm Bxg4; id \"WAC.092\";\n" +
            "r1b1k1nr/pp3pQp/4pq2/3pn3/8/P1P5/2P2PPP/R1B1KBNR w KQkq - bm Bh6; id \"WAC.093\";\n" +
            "8/k7/p7/3Qp2P/n1P5/3KP3/1q6/8 b - - bm e4+; id \"WAC.094\";\n" +
            "2r5/1r6/4pNpk/3pP1qp/8/2P1QP2/5PK1/R7 w - - bm Ng4+; id \"WAC.095\";\n" +
            "r1b4k/ppp2Bb1/6Pp/3pP3/1qnP1p1Q/8/PPP3P1/1K1R3R w - - bm Qd8+ b3; id \"WAC.096\";\n" +
            "6k1/5p2/p5np/4B3/3P4/1PP1q3/P3r1QP/6RK w - - bm Qa8+; id \"WAC.097\";\n" +
            "1r3rk1/5pb1/p2p2p1/Q1n1q2p/1NP1P3/3p1P1B/PP1R3P/1K2R3 b - - bm Nxe4; id \"WAC.098\";\n" +
            "r1bq1r1k/1pp1Np1p/p2p2pQ/4R3/n7/8/PPPP1PPP/R1B3K1 w - - bm Rh5; id \"WAC.099\";\n" +
            "8/k1b5/P4p2/1Pp2p1p/K1P2P1P/8/3B4/8 w - - bm Be3 b6+; id \"WAC.100\";\n" +
            "5rk1/p5pp/8/8/2Pbp3/1P4P1/7P/4RN1K b - - bm Bc3; id \"WAC.101\";\n" +
            "2Q2n2/2R4p/1p1qpp1k/8/3P3P/3B2P1/5PK1/r7 w - - bm Qxf8+; id \"WAC.102\";\n" +
            "6k1/2pb1r1p/3p1PpQ/p1nPp3/1q2P3/2N2P2/PrB5/2K3RR w - - bm Qxg6+; id \"WAC.103\";\n" +
            "b4r1k/pq2rp2/1p1bpn1p/3PN2n/2P2P2/P2B3K/1B2Q2N/3R2R1 w - - bm Qxh5; id \"WAC.104\";\n" +
            "r2r2k1/pb3ppp/1p1bp3/7q/3n2nP/PP1B2P1/1B1N1P2/RQ2NRK1 b - - bm Bxg3 Qxh4; id \"WAC.105\";\n" +
            "4rrk1/pppb4/7p/3P2pq/3Qn3/P5P1/1PP4P/R3RNNK b - - bm Nf2+; id \"WAC.106\";\n" +
            "5n2/pRrk2p1/P4p1p/4p3/3N4/5P2/6PP/6K1 w - - bm Nb5; id \"WAC.107\";\n" +
            "r5k1/1q4pp/2p5/p1Q5/2P5/5R2/4RKPP/r7 w - - bm Qe5; id \"WAC.108\";\n" +
            "rn2k1nr/pbp2ppp/3q4/1p2N3/2p5/QP6/PB1PPPPP/R3KB1R b KQkq - bm c3; id \"WAC.109\";\n" +
            "2kr4/bp3p2/p2p2b1/P7/2q5/1N4B1/1PPQ2P1/2KR4 b - - bm Be3; id \"WAC.110\";\n" +
            "6k1/p5p1/5p2/2P2Q2/3pN2p/3PbK1P/7P/6q1 b - - bm Qf1+; id \"WAC.111\";\n" +
            "r4kr1/ppp5/4bq1b/7B/2PR1Q1p/2N3P1/PP3P1P/2K1R3 w - - bm Rxe6; id \"WAC.112\";\n" +
            "rnbqkb1r/1p3ppp/5N2/1p2p1B1/2P5/8/PP2PPPP/R2QKB1R b KQkq - bm Qxf6; id \"WAC.113\";\n" +
            "r1b1rnk1/1p4pp/p1p2p2/3pN2n/3P1PPq/2NBPR1P/PPQ5/2R3K1 w - - bm Bxh7+; id \"WAC.114\";\n" +
            "4N2k/5rpp/1Q6/p3q3/8/P5P1/1P3P1P/5K2 w - - bm Nd6; id \"WAC.115\";\n" +
            "r2r2k1/2p2ppp/p7/1p2P1n1/P6q/5P2/1PB1QP1P/R5RK b - - bm Rd2; id \"WAC.116\";\n" +
            "3r1rk1/q4ppp/p1Rnp3/8/1p6/1N3P2/PP3QPP/3R2K1 b - - bm Ne4; id \"WAC.117\";\n" +
            "r5k1/pb2rpp1/1p6/2p4q/5R2/2PB2Q1/P1P3PP/5R1K w - - bm Rh4; id \"WAC.118\";\n" +
            "r2qr1k1/p1p2ppp/2p5/2b5/4nPQ1/3B4/PPP3PP/R1B2R1K b - - bm Qxd3; id \"WAC.119\";\n" +
            "r4rk1/1bn2qnp/3p1B1Q/p2P1pP1/1pp5/5N1P/PPB2P2/2KR3R w - - bm Rhg1 g6; id \"WAC.120\";\n" +
            "6k1/5p1p/2bP2pb/4p3/2P5/1p1pNPPP/1P1Q1BK1/1q6 b - - bm Bxf3+; id \"WAC.121\";\n" +
            "1k6/ppp4p/1n2pq2/1N2Rb2/2P2Q2/8/P4KPP/3r1B2 b - - bm Rxf1+; id \"WAC.122\";\n" +
            "6k1/1b2rp2/1p4p1/3P4/PQ4P1/2N2q2/5P2/3R2K1 b - - bm Bxd5 Rc7 Re6; id \"WAC.123\";\n" +
            "6k1/3r4/2R5/P5P1/1P4p1/8/4rB2/6K1 b - - bm g3; id \"WAC.124\";\n" +
            "r1bqr1k1/pp3ppp/1bp5/3n4/3B4/2N2P1P/PPP1B1P1/R2Q1RK1 b - - bm Bxd4+; id \"WAC.125\";\n" +
            "r5r1/pQ5p/1qp2R2/2k1p3/4P3/2PP4/P1P3PP/6K1 w - - bm Rxc6+; id \"WAC.126\";\n" +
            "2k4r/1pr1n3/p1p1q2p/5pp1/3P1P2/P1P1P3/1R2Q1PP/1RB3K1 w - - bm Rxb7; id \"WAC.127\";\n" +
            "6rk/1pp2Qrp/3p1B2/1pb1p2R/3n1q2/3P4/PPP3PP/R6K w - - bm Qg6; id \"WAC.128\";\n" +
            "3r1r1k/1b2b1p1/1p5p/2p1Pp2/q1B2P2/4P2P/1BR1Q2K/6R1 b - - bm Bf3; id \"WAC.129\";\n" +
            "6k1/1pp3q1/5r2/1PPp4/3P1pP1/3Qn2P/3B4/4R1K1 b - - bm Qh6 Qh8; id \"WAC.130\";\n" +
            "2rq1bk1/p4p1p/1p4p1/3b4/3B1Q2/8/P4PpP/3RR1K1 w - - bm Re8; id \"WAC.131\";\n" +
            "4r1k1/5bpp/2p5/3pr3/8/1B3pPq/PPR2P2/2R2QK1 b - - bm Re1; id \"WAC.132\";\n" +
            "r1b1k2r/1pp1q2p/p1n3p1/3QPp2/8/1BP3B1/P5PP/3R1RK1 w kq - bm Bh4; id \"WAC.133\";\n" +
            "3r2k1/p6p/2Q3p1/4q3/2P1p3/P3Pb2/1P3P1P/2K2BR1 b - - bm Rd1+; id \"WAC.134\";\n" +
            "3r1r1k/N2qn1pp/1p2np2/2p5/2Q1P2N/3P4/PP4PP/3R1RK1 b - - bm Nd4; id \"WAC.135\";\n" +
            "6kr/1q2r1p1/1p2N1Q1/5p2/1P1p4/6R1/7P/2R3K1 w - - bm Rc8+; id \"WAC.136\";\n" +
            "3b1rk1/1bq3pp/5pn1/1p2rN2/2p1p3/2P1B2Q/1PB2PPP/R2R2K1 w - - bm Rd7; id \"WAC.137\";\n" +
            "r1bq3r/ppppR1p1/5n1k/3P4/6pP/3Q4/PP1N1PP1/5K1R w - - bm h5; id \"WAC.138\";\n" +
            "rnb3kr/ppp2ppp/1b6/3q4/3pN3/Q4N2/PPP2KPP/R1B1R3 w - - bm Nf6+; id \"WAC.139\";\n" +
            "r2b1rk1/pq4p1/4ppQP/3pB1p1/3P4/2R5/PP3PP1/5RK1 w - - bm Bc7 Rc7; id \"WAC.140\";\n" +
            "4r1k1/p1qr1p2/2pb1Bp1/1p5p/3P1n1R/1B3P2/PP3PK1/2Q4R w - - bm Qxf4; id \"WAC.141\";\n" +
            "r2q3n/ppp2pk1/3p4/5Pr1/2NP1Qp1/2P2pP1/PP3K2/4R2R w - - bm Re8 f6+; id \"WAC.142\";\n" +
            "5b2/pp2r1pk/2pp1pRp/4rP1N/2P1P3/1P4QP/P3q1P1/5R1K w - - bm Rxh6+; id \"WAC.143\";\n" +
            "r2q1rk1/pp3ppp/2p2b2/8/B2pPPb1/7P/PPP1N1P1/R2Q1RK1 b - - bm d3; id \"WAC.144\";\n" +
            "r1bq4/1p4kp/3p1n2/p4pB1/2pQ4/8/1P4PP/4RRK1 w - - bm Re8; id \"WAC.145\";\n" +
            "8/8/2Kp4/3P1B2/2P2k2/5p2/8/8 w - - bm Bc8 Bd3 Bh3; id \"WAC.146\";\n" +
            "r2r2k1/ppqbppbp/2n2np1/2pp4/6P1/1P1PPNNP/PBP2PB1/R2QK2R b KQ - bm Nxg4; id \"WAC.147\";\n" +
            "2r1k3/6pr/p1nBP3/1p3p1p/2q5/2P5/P1R4P/K2Q2R1 w - - bm Rxg7; id \"WAC.148\";\n" +
            "6k1/6p1/2p4p/4Pp2/4b1qP/2Br4/1P2RQPK/8 b - - bm Bxg2; id \"WAC.149\";\n" +
            "r3r1k1/5p2/pQ1b2pB/1p6/4p3/6P1/Pq2BP1P/2R3K1 b - - bm Ba3 Be5 Bf8 e3; c0 \"All win but e3 is best.\"; id \"WAC.150\";\n" +
            "8/3b2kp/4p1p1/pr1n4/N1N4P/1P4P1/1K3P2/3R4 w - - bm Nc3; id \"WAC.151\";\n" +
            "1br2rk1/1pqb1ppp/p3pn2/8/1P6/P1N1PN1P/1B3PP1/1QRR2K1 w - - bm Ne4; id \"WAC.152\";\n" +
            "2r3k1/q4ppp/p3p3/pnNp4/2rP4/2P2P2/4R1PP/2R1Q1K1 b - - bm Nxd4; id \"WAC.153\";\n" +
            "r1b2rk1/2p2ppp/p7/1p6/3P3q/1BP3bP/PP3QP1/RNB1R1K1 w - - bm Qxf7+; id \"WAC.154\";\n" +
            "5bk1/1rQ4p/5pp1/2pP4/3n1PP1/7P/1q3BB1/4R1K1 w - - bm d6; id \"WAC.155\";\n" +
            "r1b1qN1k/1pp3p1/p2p3n/4p1B1/8/1BP4Q/PP3KPP/8 w - - bm Qxh6+; id \"WAC.156\";\n" +
            "5rk1/p4ppp/2p1b3/3Nq3/4P1n1/1p1B2QP/1PPr2P1/1K2R2R w - - bm Ne7+; id \"WAC.157\";\n" +
            "5rk1/n1p1R1bp/p2p4/1qpP1QB1/7P/2P3P1/PP3P2/6K1 w - - bm Rxg7+; id \"WAC.158\";\n" +
            "r1b2r2/5P1p/ppn3pk/2p1p1Nq/1bP1PQ2/3P4/PB4BP/1R3RK1 w - - bm Ne6+; id \"WAC.159\";\n" +
            "qn1kr2r/1pRbb3/pP5p/P2pP1pP/3N1pQ1/3B4/3B1PP1/R5K1 w - - bm Qxd7+; id \"WAC.160\";\n" +
            "3r3k/3r1P1p/pp1Nn3/2pp4/7Q/6R1/Pq4PP/5RK1 w - - bm Qxd8+; id \"WAC.161\";\n" +
            "r3kbnr/p4ppp/2p1p3/8/Q1B3b1/2N1B3/PP3PqP/R3K2R w KQkq - bm Bd5; id \"WAC.162\";\n" +
            "5rk1/2p4p/2p4r/3P4/4p1b1/1Q2NqPp/PP3P1K/R4R2 b - - bm Qg2+; id \"WAC.163\";\n" +
            "8/6pp/4p3/1p1n4/1NbkN1P1/P4P1P/1PR3K1/r7 w - - bm Rxc4+; id \"WAC.164\";\n" +
            "1r5k/p1p3pp/8/8/4p3/P1P1R3/1P1Q1qr1/2KR4 w - - bm Re2; id \"WAC.165\";\n" +
            "r3r1k1/5pp1/p1p4p/2Pp4/8/q1NQP1BP/5PP1/4K2R b K - bm d4; id \"WAC.166\";\n" +
            "7Q/ppp2q2/3p2k1/P2Ppr1N/1PP5/7R/5rP1/6K1 b - - bm Rxg2+; id \"WAC.167\";\n" +
            "r3k2r/pb1q1p2/8/2p1pP2/4p1p1/B1P1Q1P1/P1P3K1/R4R2 b kq - bm Qd2+; id \"WAC.168\";\n" +
            "5rk1/1pp3bp/3p2p1/2PPp3/1P2P3/2Q1B3/4q1PP/R5K1 b - - bm Bh6; id \"WAC.169\";\n" +
            "5r1k/6Rp/1p2p3/p2pBp2/1qnP4/4P3/Q4PPP/6K1 w - - bm Qxc4; id \"WAC.170\";\n" +
            "2rq4/1b2b1kp/p3p1p1/1p1nNp2/7P/1B2B1Q1/PP3PP1/3R2K1 w - - bm Bh6+; id \"WAC.171\";\n" +
            "5r1k/p5pp/8/1P1pq3/P1p2nR1/Q7/5BPP/6K1 b - - bm Qe1+; id \"WAC.172\";\n" +
            "2r1b3/1pp1qrk1/p1n1P1p1/7R/2B1p3/4Q1P1/PP3PP1/3R2K1 w - - bm Qh6+; id \"WAC.173\";\n" +
            "2r2rk1/6p1/p3pq1p/1p1b1p2/3P1n2/PP3N2/3N1PPP/1Q2RR1K b - - bm Nxg2; id \"WAC.174\";\n" +
            "r5k1/pppb3p/2np1n2/8/3PqNpP/3Q2P1/PPP5/R4RK1 w - - bm Nh5; id \"WAC.175\";\n" +
            "r1bq3r/ppp2pk1/3p1pp1/8/2BbPQ2/2NP2P1/PPP4P/R4R1K b - - bm Rxh2+; id \"WAC.176\";\n" +
            "r1b3r1/4qk2/1nn1p1p1/3pPp1P/p4P2/1p3BQN/PKPBN3/3R3R b - - bm Qa3+; id \"WAC.177\";\n" +
            "3r2k1/p1rn1p1p/1p2pp2/6q1/3PQNP1/5P2/P1P4R/R5K1 w - - bm Nxe6; id \"WAC.178\";\n" +
            "r1b2r1k/pp4pp/3p4/3B4/8/1QN3Pn/PP3q1P/R3R2K b - - bm Qg1+; id \"WAC.179\";\n" +
            "r1q2rk1/p3bppb/3p1n1p/2nPp3/1p2P1P1/6NP/PP2QPB1/R1BNK2R b KQ - bm Nxd5; id \"WAC.180\";\n" +
            "r3k2r/2p2p2/p2p1n2/1p2p3/4P2p/1PPPPp1q/1P5P/R1N2QRK b kq - bm Ng4; id \"WAC.181\";\n" +
            "r1b2rk1/ppqn1p1p/2n1p1p1/2b3N1/2N5/PP1BP3/1B3PPP/R2QK2R w KQ - bm Qh5; id \"WAC.182\";\n" +
            "1r2k1r1/5p2/b3p3/1p2b1B1/3p3P/3B4/PP2KP2/2R3R1 w - - bm Bf6; id \"WAC.183\";\n" +
            "4kn2/r4p1r/p3bQ2/q1nNP1Np/1p5P/8/PPP3P1/2KR3R w - - bm Qe7+; id \"WAC.184\";\n" +
            "1r1rb1k1/2p3pp/p2q1p2/3PpP1Q/Pp1bP2N/1B5R/1P4PP/2B4K w - - bm Qxh7+; id \"WAC.185\";\n" +
            "r5r1/p1q2p1k/1p1R2pB/3pP3/6bQ/2p5/P1P1NPPP/6K1 w - - bm Bf8+; id \"WAC.186\";\n" +
            "6k1/5p2/p3p3/1p3qp1/2p1Qn2/2P1R3/PP1r1PPP/4R1K1 b - - bm Nh3+; id \"WAC.187\";\n" +
            "3RNbk1/pp3p2/4rQpp/8/1qr5/7P/P4P2/3R2K1 w - - bm Qg7+; id \"WAC.188\";\n" +
            "3r1k2/1ppPR1n1/p2p1rP1/3P3p/4Rp1N/5K2/P1P2P2/8 w - - bm Re8+; id \"WAC.189\";\n" +
            "8/p2b2kp/1q1p2p1/1P1Pp3/4P3/3B2P1/P2Q3P/2Nn3K b - - bm Bh3; id \"WAC.190\";\n" +
            "2r1Rn1k/1p1q2pp/p7/5p2/3P4/1B4P1/P1P1QP1P/6K1 w - - bm Qc4; id \"WAC.191\";\n" +
            "r3k3/ppp2Npp/4Bn2/2b5/1n1pp3/N4P2/PPP3qP/R2QKR2 b Qq - bm Nd3+; id \"WAC.192\";\n" +
            "5bk1/p4ppp/Qp6/4B3/1P6/Pq2P1P1/2rr1P1P/R4RK1 b - - bm Qxe3; id \"WAC.193\";\n" +
            "5rk1/ppq2ppp/2p5/4bN2/4P3/6Q1/PPP2PPP/3R2K1 w - - bm Nh6+; id \"WAC.194\";\n" +
            "3r1rk1/1p3p2/p3pnnp/2p3p1/2P2q2/1P5P/PB2QPPN/3RR1K1 w - - bm g3; id \"WAC.195\";\n" +
            "rr4k1/p1pq2pp/Q1n1pn2/2bpp3/4P3/2PP1NN1/PP3PPP/R1B1K2R b KQ - bm Nb4; id \"WAC.196\";\n" +
            "7k/1p4p1/7p/3P1n2/4Q3/2P2P2/PP3qRP/7K b - - bm Qf1+; id \"WAC.197\";\n" +
            "2br2k1/ppp2p1p/4p1p1/4P2q/2P1Bn2/2Q5/PP3P1P/4R1RK b - - bm Rd3; id \"WAC.198\";\n" +
            "r1br2k1/pp2nppp/2n5/1B1q4/Q7/4BN2/PP3PPP/2R2RK1 w - - bm Bxc6 Rcd1 Rfd1; id \"WAC.199\";\n" +
            "2rqrn1k/pb4pp/1p2pp2/n2P4/2P3N1/P2B2Q1/1B3PPP/2R1R1K1 w - - bm Bxf6; id \"WAC.200\";\n" +
            "2b2r1k/4q2p/3p2pQ/2pBp3/8/6P1/1PP2P1P/R5K1 w - - bm Ra7; id \"WAC.201\";\n" +
            "QR2rq1k/2p3p1/3p1pPp/8/4P3/8/P1r3PP/1R4K1 b - - bm Rxa2; id \"WAC.202\";\n" +
            "r4rk1/5ppp/p3q1n1/2p2NQ1/4n3/P3P3/1B3PPP/1R3RK1 w - - bm Qh6; id \"WAC.203\";\n" +
            "r1b1qrk1/1p3ppp/p1p5/3Nb3/5N2/P7/1P4PQ/K1R1R3 w - - bm Rxe5; id \"WAC.204\";\n" +
            "r3rnk1/1pq2bb1/p4p2/3p1Pp1/3B2P1/1NP4R/P1PQB3/2K4R w - - bm Qxg5; id \"WAC.205\";\n" +
            "1Qq5/2P1p1kp/3r1pp1/8/8/7P/p4PP1/2R3K1 b - - bm Rc6; id \"WAC.206\";\n" +
            "r1bq2kr/p1pp1ppp/1pn1p3/4P3/2Pb2Q1/BR6/P4PPP/3K1BNR w - - bm Qxg7+; id \"WAC.207\";\n" +
            "3r1bk1/ppq3pp/2p5/2P2Q1B/8/1P4P1/P6P/5RK1 w - - bm Bf7+; id \"WAC.208\";\n" +
            "4kb1r/2q2p2/r2p4/pppBn1B1/P6P/6Q1/1PP5/2KRR3 w k - bm Rxe5+; id \"WAC.209\";\n" +
            "3r1rk1/pp1q1ppp/3pn3/2pN4/5PP1/P5PQ/1PP1B3/1K1R4 w - - bm Rh1; id \"WAC.210\";\n" +
            "r1bqrk2/pp1n1n1p/3p1p2/P1pP1P1Q/2PpP1NP/6R1/2PB4/4RBK1 w - - bm Qxf7+; id \"WAC.211\";\n" +
            "rn1qr2Q/pbppk1p1/1p2pb2/4N3/3P4/2N5/PPP3PP/R4RK1 w - - bm Qxg7+; id \"WAC.212\";\n" +
            "3r1r1k/1b4pp/ppn1p3/4Pp1R/Pn5P/3P4/4QP2/1qB1NKR1 w - - bm Rxh7+; id \"WAC.213\";\n" +
            "r2r2k1/1p2qpp1/1np1p1p1/p3N3/2PPN3/bP5R/4QPPP/4R1K1 w - - bm Ng5; id \"WAC.214\";\n" +
            "3r2k1/pb1q1pp1/1p2pb1p/8/3N4/P2QB3/1P3PPP/1Br1R1K1 w - - bm Qh7+; id \"WAC.215\";\n" +
            "r2qr1k1/1b1nbppp/p3pn2/1p1pN3/3P1B2/2PB1N2/PP2QPPP/R4RK1 w - - bm Nxf7 a4; id \"WAC.216\";\n" +
            "r3kb1r/1pp3p1/p3bp1p/5q2/3QN3/1P6/PBP3P1/3RR1K1 w kq - bm Qd7+; id \"WAC.217\";\n" +
            "6k1/pp5p/2p3q1/6BP/2nPr1Q1/8/PP3R1K/8 w - - bm Bh6; id \"WAC.218\";\n" +
            "7k/p4q1p/1pb5/2p5/4B2Q/2P1B3/P6P/7K b - - bm Qf1+; id \"WAC.219\";\n" +
            "3rr1k1/ppp2ppp/8/5Q2/4n3/1B5R/PPP1qPP1/5RK1 b - - bm Qxf1+; id \"WAC.220\";\n" +
            "r3k3/P5bp/2N1bp2/4p3/2p5/6NP/1PP2PP1/3R2K1 w q - bm Rd8+; id \"WAC.221\";\n" +
            "2r1r2k/1q3ppp/p2Rp3/2p1P3/6QB/p3P3/bP3PPP/3R2K1 w - - bm Bf6; id \"WAC.222\";\n" +
            "r1bqk2r/pp3ppp/5n2/8/1b1npB2/2N5/PP1Q2PP/1K2RBNR w kq - bm Nxe4; id \"WAC.223\";\n" +
            "5rk1/p1q3pp/1p1r4/2p1pp1Q/1PPn1P2/3B3P/P2R2P1/3R2K1 b - - bm Rh6 e4; id \"WAC.224\";\n" +
            "4R3/4q1kp/6p1/1Q3b2/1P1b1P2/6KP/8/8 b - - bm Qh4+; id \"WAC.225\";\n" +
            "2b2rk1/p1p4p/2p1p1p1/br2N1Q1/1p2q3/8/PB3PPP/3R1RK1 w - - bm Nf7; id \"WAC.226\";\n" +
            "2k1rb1r/ppp3pp/2np1q2/5b2/2B2P2/2P1BQ2/PP1N1P1P/2KR3R b - - bm d5; id \"WAC.227\";\n" +
            "r4rk1/1bq1bp1p/4p1p1/p2p4/3BnP2/1N1B3R/PPP3PP/R2Q2K1 w - - bm Bxe4; id \"WAC.228\";\n" +
            "8/8/8/1p5r/p1p1k1pN/P2pBpP1/1P1K1P2/8 b - - bm Rxh4 b4; id \"WAC.229\";\n" +
            "2b5/1r6/2kBp1p1/p2pP1P1/2pP4/1pP3K1/1R3P2/8 b - - bm Rb4; id \"WAC.230\";\n" +
            "r4rk1/1b1nqp1p/p5p1/1p2PQ2/2p5/5N2/PP3PPP/R1BR2K1 w - - bm Bg5; id \"WAC.231\";\n" +
            "1R2rq1k/2p3p1/Q2p1pPp/8/4P3/8/P1r3PP/1R4K1 w - - bm Qb5 Rxe8; id \"WAC.232\";\n" +
            "5rk1/p1p2r1p/2pp2p1/4p3/PPPnP3/3Pq1P1/1Q1R1R1P/4NK2 b - - bm Nb3; id \"WAC.233\";\n" +
            "2kr1r2/p6p/5Pp1/2p5/1qp2Q1P/7R/PP6/1KR5 w - - bm Rb3; id \"WAC.234\";\n" +
            "5r2/1p1RRrk1/4Qq1p/1PP3p1/8/4B3/1b3P1P/6K1 w - - bm Qe4 Qxf7+ Rxf7+; id \"WAC.235\";\n" +
            "1R6/p5pk/4p2p/4P3/8/2r3qP/P3R1b1/4Q1K1 b - - bm Rc1; id \"WAC.236\";\n" +
            "r5k1/pQp2qpp/8/4pbN1/3P4/6P1/PPr4P/1K1R3R b - - bm Rc1+; id \"WAC.237\";\n" +
            "1k1r4/pp1r1pp1/4n1p1/2R5/2Pp1qP1/3P2QP/P4PB1/1R4K1 w - - bm Bxb7; id \"WAC.238\";\n" +
            "8/6k1/5pp1/Q6p/5P2/6PK/P4q1P/8 b - - bm Qf1+; id \"WAC.239\";\n" +
            "2b4k/p1b2p2/2p2q2/3p1PNp/3P2R1/3B4/P1Q2PKP/4r3 w - - bm Qxc6; id \"WAC.240\";\n" +
            "2rq1rk1/pp3ppp/2n2b2/4NR2/3P4/PB5Q/1P4PP/3R2K1 w - - bm Qxh7+; id \"WAC.241\";\n" +
            "r1b1r1k1/pp1nqp2/2p1p1pp/8/4N3/P1Q1P3/1P3PPP/1BRR2K1 w - - bm Rxd7; id \"WAC.242\";\n" +
            "1r3r1k/3p4/1p1Nn1R1/4Pp1q/pP3P1p/P7/5Q1P/6RK w - - bm Qe2; id \"WAC.243\";\n" +
            "r6r/pp3ppp/3k1b2/2pb4/B4Pq1/2P1Q3/P5PP/1RBR2K1 w - - bm Qxc5+; id \"WAC.244\";\n" +
            "4rrn1/ppq3bk/3pPnpp/2p5/2PB4/2NQ1RPB/PP5P/5R1K w - - bm Qxg6+; id \"WAC.245\";\n" +
            "6R1/4qp1p/ppr1n1pk/8/1P2P1QP/6N1/P4PP1/6K1 w - - bm Qh5+; id \"WAC.246\";\n" +
            "2k1r3/1p2Bq2/p2Qp3/Pb1p1p1P/2pP1P2/2P5/2P2KP1/1R6 w - - bm Rxb5; id \"WAC.247\";\n" +
            "5r1k/1p4pp/3q4/3Pp1R1/8/8/PP4PP/4Q1K1 b - - bm Qc5+; id \"WAC.248\";\n" +
            "r4rk1/pbq2pp1/1ppbpn1p/8/2PP4/1P1Q1N2/PBB2PPP/R3R1K1 w - - bm c5 d5; id \"WAC.249\";\n" +
            "1b5k/7P/p1p2np1/2P2p2/PP3P2/4RQ1R/q2r3P/6K1 w - - bm Re8+; id \"WAC.250\";\n" +
            "k7/p4p2/P1q1b1p1/3p3p/3Q4/7P/5PP1/1R4K1 w - - bm Qe5 Qf4; id \"WAC.251\";\n" +
            "1rb1r1k1/p1p2ppp/5n2/2pP4/5P2/2QB4/qNP3PP/2KRB2R b - - bm Bg4 Re2; c0 \"Bg4 wins, but Re2 is far better.\"; id \"WAC.252\";\n" +
            "k5r1/p4b2/2P5/5p2/3P1P2/4QBrq/P5P1/4R1K1 w - - bm Qe8+; id \"WAC.253\";\n" +
            "r6k/pp3p1p/2p1bp1q/b3p3/4Pnr1/2PP2NP/PP1Q1PPN/R2B2RK b - - bm Nxh3; id \"WAC.254\";\n" +
            "3r3r/p4pk1/5Rp1/3q4/1p1P2RQ/5N2/P1P4P/2b4K w - - bm Rfxg6+; id \"WAC.255\";\n" +
            "3r1rk1/1pb1qp1p/2p3p1/p7/P2Np2R/1P5P/1BP2PP1/3Q1BK1 w - - bm Nf5; id \"WAC.256\";\n" +
            "4r1k1/pq3p1p/2p1r1p1/2Q1p3/3nN1P1/1P6/P1P2P1P/3RR1K1 w - - bm Rxd4; id \"WAC.257\";\n" +
            "r3brkn/1p5p/2p2Ppq/2Pp3B/3Pp2Q/4P1R1/6PP/5R1K w - - bm Bxg6; id \"WAC.258\";\n" +
            "r1bq1rk1/ppp2ppp/2np4/2bN1PN1/2B1P3/3p4/PPP2nPP/R1BQ1K1R w - - bm Qh5; id \"WAC.259\";\n" +
            "2r2b1r/p1Nk2pp/3p1p2/N2Qn3/4P3/q6P/P4PP1/1R3K1R w - - bm Qe6+; id \"WAC.260\";\n" +
            "r5k1/1bp3pp/p2p4/1p6/5p2/1PBP1nqP/1PP3Q1/R4R1K b - - bm Nd4; id \"WAC.261\";\n" +
            "6k1/p1B1b2p/2b3r1/2p5/4p3/1PP1N1Pq/P2R1P2/3Q2K1 b - - bm Rh6; id \"WAC.262\";\n" +
            "rnbqr2k/pppp1Qpp/8/b2NN3/2B1n3/8/PPPP1PPP/R1B1K2R w KQ - bm Qg8+; id \"WAC.263\";\n" +
            "r2r2k1/1R2qp2/p5pp/2P5/b1PN1b2/P7/1Q3PPP/1B1R2K1 b - - bm Qe5 Rab8; id \"WAC.264\";\n" +
            "2r1k2r/2pn1pp1/1p3n1p/p3PP2/4q2B/P1P5/2Q1N1PP/R4RK1 w k - bm exf6; id \"WAC.265\";\n" +
            "r3q2r/2p1k1p1/p5p1/1p2Nb2/1P2nB2/P7/2PNQbPP/R2R3K b - - bm Rxh2+; id \"WAC.266\";\n" +
            "2r1kb1r/pp3ppp/2n1b3/1q1N2B1/1P2Q3/8/P4PPP/3RK1NR w Kk - bm Nc7+; id \"WAC.267\";\n" +
            "2r3kr/ppp2n1p/7B/5q1N/1bp5/2Pp4/PP2RPPP/R2Q2K1 w - - bm Re8+; id \"WAC.268\";\n" +
            "2kr2nr/pp1n1ppp/2p1p3/q7/1b1P1B2/P1N2Q1P/1PP1BPP1/R3K2R w KQ - bm axb4; id \"WAC.269\";\n" +
            "2r1r1k1/pp1q1ppp/3p1b2/3P4/3Q4/5N2/PP2RPPP/4R1K1 w - - bm Qg4; id \"WAC.270\";\n" +
            "2kr4/ppp3Pp/4RP1B/2r5/5P2/1P6/P2p4/3K4 w - - bm Rd6; id \"WAC.271\";\n" +
            "nrq4r/2k1p3/1p1pPnp1/pRpP1p2/P1P2P2/2P1BB2/1R2Q1P1/6K1 w - - bm Bxc5; id \"WAC.272\";\n" +
            "2k4B/bpp1qp2/p1b5/7p/1PN1n1p1/2Pr4/P5PP/R3QR1K b - - bm Ng3+ g3; id \"WAC.273\";\n" +
            "8/1p6/p5R1/k7/Prpp4/K7/1NP5/8 w - - am Rd6; bm Rb6 Rg5+; id \"WAC.274\";\n" +
            "r1b2rk1/1p1n1ppp/p1p2q2/4p3/P1B1Pn2/1QN2N2/1P3PPP/3R1RK1 b - - bm Nc5 Nxg2 b5; id \"WAC.275\";\n" +
            "r5k1/pp1RR1pp/1b6/6r1/2p5/B6P/P4qPK/3Q4 w - - bm Qd5+; id \"WAC.276\";\n" +
            "1r4r1/p2kb2p/bq2p3/3p1p2/5P2/2BB3Q/PP4PP/3RKR2 b - - bm Rg3 Rxg2; id \"WAC.277\";\n" +
            "r2qkb1r/pppb2pp/2np1n2/5pN1/2BQP3/2N5/PPP2PPP/R1B1K2R w KQkq - bm Bf7+; id \"WAC.278\";\n" +
            "r7/4b3/2p1r1k1/1p1pPp1q/1P1P1P1p/PR2NRpP/2Q3K1/8 w - - bm Nxf5 Rc3; id \"WAC.279\";\n" +
            "r1r2bk1/5p1p/pn4p1/N2b4/3Pp3/B3P3/2q1BPPP/RQ3RK1 b - - bm Bxa3; id \"WAC.280\";\n" +
            "2R5/2R4p/5p1k/6n1/8/1P2QPPq/r7/6K1 w - - bm Rxh7+; id \"WAC.281\";\n" +
            "6k1/2p3p1/1p1p1nN1/1B1P4/4PK2/8/2r3b1/7R w - - bm Rh8+; id \"WAC.282\";\n" +
            "3q1rk1/4bp1p/1n2P2Q/3p1p2/6r1/Pp2R2N/1B4PP/7K w - - bm Ng5; id \"WAC.283\";\n" +
            "3r3k/pp4pp/8/1P6/3N4/Pn2P1qb/1B1Q2B1/2R3K1 w - - bm Nf5; id \"WAC.284\";\n" +
            "2rr3k/1b2bppP/p2p1n2/R7/3P4/1qB2P2/1P4Q1/1K5R w - - bm Qxg7+; id \"WAC.285\";\n" +
            "3r1k2/1p6/p4P2/2pP2Qb/8/1P1KB3/P6r/8 b - - bm Rxd5+; id \"WAC.286\";\n" +
            "rn3k1r/pp2bBpp/2p2n2/q5N1/3P4/1P6/P1P3PP/R1BQ1RK1 w - - bm Qg4 Qh5; id \"WAC.287\";\n" +
            "r1b2rk1/p4ppp/1p1Qp3/4P2N/1P6/8/P3qPPP/3R1RK1 w - - bm Nf6+; id \"WAC.288\";\n" +
            "2r3k1/5p1p/p3q1p1/2n3P1/1p1QP2P/1P4N1/PK6/2R5 b - - bm Qe5; id \"WAC.289\";\n" +
            "2k2r2/2p5/1pq5/p1p1n3/P1P2n1B/1R4Pp/2QR4/6K1 b - - bm Ne2+; id \"WAC.290\";\n" +
            "5r1k/3b2p1/p6p/1pRpR3/1P1P2q1/P4pP1/5QnP/1B4K1 w - - bm h3; id \"WAC.291\";\n" +
            "4r3/1Q1qk2p/p4pp1/3Pb3/P7/6PP/5P2/4R1K1 w - - bm d6+; id \"WAC.292\";\n" +
            "1nbq1r1k/3rbp1p/p1p1pp1Q/1p6/P1pPN3/5NP1/1P2PPBP/R4RK1 w - - bm Nfg5; id \"WAC.293\";\n" +
            "3r3k/1r3p1p/p1pB1p2/8/p1qNP1Q1/P6P/1P4P1/3R3K w - - bm Bf8 Nf5 Qf4; id \"WAC.294\";\n" +
            "4r3/p4r1p/R1p2pp1/1p1bk3/4pNPP/2P1K3/2P2P2/3R4 w - - bm Rxd5+; id \"WAC.295\";\n" +
            "3r4/1p2k2p/p1b1p1p1/4Q1Pn/2B3KP/4pP2/PP2R1N1/6q1 b - - bm Rd4+ Rf8; id \"WAC.296\";\n" +
            "3r1rk1/p3qp1p/2bb2p1/2p5/3P4/1P6/PBQN1PPP/2R2RK1 b - - bm Bxg2 Bxh2+; id \"WAC.297\";\n" +
            "3Q4/p3b1k1/2p2rPp/2q5/4B3/P2P4/7P/6RK w - - bm Qh8+; id \"WAC.298\";\n" +
            "1n2rr2/1pk3pp/pNn2p2/2N1p3/8/6P1/PP2PPKP/2RR4 w - - bm Nca4; id \"WAC.299\";\n" +
            "b2b1r1k/3R1ppp/4qP2/4p1PQ/4P3/5B2/4N1K1/8 w - - bm g6; id \"WAC.300\";" +
            "";

    private static final String[] splitUpWACs = wacTests.split("\n");
    static int totalWACS = splitUpWACs.length;


}
