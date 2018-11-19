package chessprogram.god;

import java.util.ArrayList;
import java.util.List;

public class eDetailedPerftSearching {
    
    private static long nodesForNps = 0;
    
    public static long runPerftTestWithBoard(int d, Chessboard board, long correctAnswer){
        String s = Art.boardArt(board);
        System.out.println(s);
        System.out.println("-----------------------------------");
        System.out.println("Correct Number of nodes at depth " + d + ": "+ correctAnswer);
        System.out.println("-----------------------------------");
        return runPerftTestWithBoard(d, board);
    }

    private static long runPerftTestWithBoard(int d, Chessboard board){
        int maxD = d > 0 ? d : 6;
        reset();
        return countFinalNodesAtDepth(board, maxD);
    }

    private static long countFinalNodesAtDepth(Chessboard board, int depth) {
        long t1 = System.currentTimeMillis();
        long ii = eDetailedPerftSearching.countFinalNodesAtDepthHelper(board, depth);
        System.out.println("Final Nodes at Depth " + depth + ": " + ii);
        long t2 = System.currentTimeMillis();
        long t = t2 - t1;
        long seconds = t / 1000;
        System.out.println("Depth " + depth + " took " + seconds + " seconds (" + t+" milliseconds).");
        if (t > 0) {
            System.out.println("NPS: " + ((nodesForNps / t) * 1000) );
        }
        return ii;
    }

    private static long countFinalNodesAtDepthHelper(Chessboard board, int depth){
        long temp = 0;
        if (depth == 0){
            return 1;
        }
        List<Move> moves = board.generateLegalMoves();
        if (depth == 1){
            final int size = moves.size();
            nodesForNps += size;
            return size;
        }
        for (Move move : moves) {
            MoveOrganiser.makeMoveMaster(board, move);
            MoveOrganiser.flipTurn(board);
            nodesForNps++;
            long movesAtDepth = countFinalNodesAtDepthHelper(board, depth - 1);
            temp += movesAtDepth;
            board.unMakeMoveAndFlipTurn();
        }
        return temp;
    }

    private static void reset(){
        nodesForNps = 0;
    }
}
