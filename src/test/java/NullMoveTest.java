import chessprogram.god.Chessboard;
import chessprogram.god.CopierToBeDeleted;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.List;

public class NullMoveTest {

    @Test
    void tinyDepths() {
        verifyHashToDepth(1, new Chessboard("r6r/1b2k1bq/8/8/7B/8/8/R3K2R b QK - 3 2"));

        System.out.println("-----------------------------");

        verifyHashToDepth(1, new Chessboard("8/8/8/2k5/2pP4/8/B7/4K3 b - d3 5 3"));

        System.out.println("-----------------------------");

        verifyHashToDepth(1, new Chessboard("r1bqkbnr/pppppppp/n7/8/8/P7/1PPPPPPP/RNBQKBNR w QqKk - 2 2"));

        System.out.println("-----------------------------");

        verifyHashToDepth(1, new Chessboard("r3k2r/p1pp1pb1/bn2Qnp1/2qPN3/1p2P3/2N5/PPPBBPPP/R3K2R b QqKk - 3 2"));

        System.out.println("-----------------------------");

        verifyHashToDepth(1, new Chessboard("2kr3r/p1ppqpb1/bn2Qnp1/3PN3/1p2P3/2N5/PPPBBPPP/R3K2R b QK - 3 2"));

        System.out.println("-----------------------------");

        verifyHashToDepth(1, new Chessboard("rnb2k1r/pp1Pbppp/2p5/q7/2B5/8/PPPQNnPP/RNB1K2R w QK - 3 9"));

        System.out.println("-----------------------------");

        verifyHashToDepth(1, new Chessboard("2r5/3pk3/8/2P5/8/2K5/8/8 w - - 5 4"));

        System.out.println("-----------------------------");

        verifyHashToDepth(3, new Chessboard("rnbq1k1r/pp1Pbppp/2p5/8/2B5/8/PPP1NnPP/RNBQK2R w KQ - 1 8"));

        System.out.println("-----------------------------");

        verifyHashToDepth(3, new Chessboard("r4rk1/1pp1qppp/p1np1n2/2b1p1B1/2B1P1b1/P1NP1N2/1PP1QPPP/R4RK1 w - - 0 10"));

    }
    
    @Test
    void bigDepth11() {
        verifyHashToDepth(8, new Chessboard("8/5p2/8/2k3P1/p3K3/8/1P6/8 b - -"));
    }

    @Test
    void bigDepth12() {
        verifyHashToDepth(5, new Chessboard("r3k2r/pb3p2/5npp/n2p4/1p1PPB2/6P1/P2N1PBP/R3K2R w KQkq -"));
    }

    @Test
    void regularBoard() {
        verifyHashToDepth(5, new Chessboard());

        System.out.println("-----------------------------");

        verifyHashToDepth(6, new Chessboard());
    }

    @Test
    void AvoidIllegalEPCapture() {
        verifyHashToDepth(6, new Chessboard("8/5bk1/8/2Pp4/8/1K6/8/8 w - d6 0 1"));

        System.out.println("-----------------------------");

        verifyHashToDepth(6, new Chessboard("8/8/1k6/8/2pP4/8/5BK1/8 b - d3 0 1"));
    }


    @Test
    void EPCaptureChecksOpponent() {
        verifyHashToDepth(6, new Chessboard("8/8/1k6/2b5/2pP4/8/5K2/8 b - d3 0 1"));

        System.out.println("-----------------------------");

        verifyHashToDepth(6, new Chessboard("8/5k2/8/2Pp4/2B5/1K6/8/8 w - d6 0 1"));
    }

    
    
    private static long verifyHashToDepth(int depth, Chessboard board) {
        final Chessboard initial = CopierToBeDeleted.copyBoard(board, board.isWhiteTurn(), false);
        Assert.assertEquals(board, initial);
        System.out.println(board);
        
        board.makeNullMoveAndFlipTurn();
        Assert.assertEquals(board, CopierToBeDeleted.copyBoard(board, board.isWhiteTurn(), false));
        board.unMakeNullMoveAndFlipTurn();
        Assert.assertEquals(board, CopierToBeDeleted.copyBoard(board, board.isWhiteTurn(), false));
        
        
        long ii = countFinalNodesAtDepthHelper(board, depth);
        Assert.assertEquals(board, CopierToBeDeleted.copyBoard(board, board.isWhiteTurn(), false));
        Assert.assertEquals(board, initial);

        return ii;
    }

    private static long countFinalNodesAtDepthHelper(Chessboard board, int depth){
        long temp = 0;
        if (depth == 0){
            return 1;
        }
        List<Integer> moves = board.generateLegalMoves();
        if (depth == 1){
            final int size = moves.size();
            return size;
        }
        for (int move : moves) {
            board.makeMoveAndFlipTurn(move);
            Assert.assertEquals(board, CopierToBeDeleted.copyBoard(board, board.isWhiteTurn(), false));

            board.makeNullMoveAndFlipTurn();

            Assert.assertEquals(board, CopierToBeDeleted.copyBoard(board, board.isWhiteTurn(), false));

            long movesAtDepth = countFinalNodesAtDepthHelper(board, depth - 1);
            temp += movesAtDepth;
            
            board.unMakeNullMoveAndFlipTurn();
            Assert.assertEquals(board, CopierToBeDeleted.copyBoard(board, board.isWhiteTurn(), false));

            board.unMakeMoveAndFlipTurn();
            Assert.assertEquals(board, CopierToBeDeleted.copyBoard(board, board.isWhiteTurn(), false));

        }
        return temp;
    }
}