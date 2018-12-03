package com.github.louism33.chesscore;

public class PerftDivide {
    
    private static long nodesForNps = 0;
    
    public static void main (String[] args){
        Chessboard boardAdvanced = new Chessboard("r3k2r/p1ppqpb1/bn2pnp1/3PN3/Pp2P3/2N2Q1p/1PPBBPPP/R3K2R b KQkq a3 0 1");
        Chessboard board = new Chessboard("r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - 0 1");
        final int[] legalMoves = board.generateCleanLegalMoves();

//        perftTest(1, boardAdvanced, 0);

        // a2a4

        for (int i = legalMoves.length - 3; i < legalMoves.length-2; i++) {
            final int legalMove = legalMoves[i];
            System.out.print(MoveParser.toString(legalMove) +": ");
            board.makeMoveAndFlipTurn(legalMove);
            perftTest(1, board, 0);
            try {
                board.unMakeMoveAndFlipTurn();
            } catch (IllegalUnmakeException e) {
                e.printStackTrace();
            }
            System.out.println();
        }
    }

    public static long perftTest(int d, Chessboard board){
        System.out.println(board);
        return runPerftTestWithBoard(d, board);
    }
    
    public static long perftTest(int d, Chessboard board, long correctAnswer){
//        System.out.println(board);
//        System.out.println("-----------------------------------");
//        System.out.println("Correct Number of nodes at depth " + d + ": "+ correctAnswer);
//        System.out.println("-----------------------------------");
        return runPerftTestWithBoard(d, board);
    }

    private static long runPerftTestWithBoard(int d, Chessboard board){
        int maxD = d > 0 ? d : 6;
        reset();
        return countFinalNodesAtDepth(board, maxD);
    }

    private static long countFinalNodesAtDepth(Chessboard board, int depth) {
        long t1 = System.currentTimeMillis();
        long ii = 0;
        try {
            ii = PerftDivide.countFinalNodesAtDepthHelper(board, depth);
        } catch (IllegalUnmakeException e) {
            e.printStackTrace();
        }
        System.out.println(ii);
        long t2 = System.currentTimeMillis();
        long t = t2 - t1;
        long seconds = t / 1000;
//        System.out.println("Depth " + depth + " took " + seconds + " seconds (" + t+" milliseconds).");
        if (t > 0) {
//            System.out.println("NPS: " + ((nodesForNps / t) * 1000) );
        }
        return ii;
    }

    private static long countFinalNodesAtDepthHelper(Chessboard board, int depth) throws IllegalUnmakeException {
        long temp = 0;
        if (depth == 0){
            return 1;
        }
        int[] moves = board.generateLegalMoves();

        MoveParser.printMoves(moves);
        
        if (depth == 1){
            final int size = realMoves(moves);
            nodesForNps += size;
            return size;
        }
        for (int i = 0; i < moves.length; i++) {
            int move = moves[i];
            if (move == 0) {
                continue;
            }

            board.makeMoveAndFlipTurn(move);
            
            nodesForNps++;
            long movesAtDepth = countFinalNodesAtDepthHelper(board, depth - 1);
            temp += movesAtDepth;
            board.unMakeMoveAndFlipTurn();
        }
        return temp;
    }
    
    private static int realMoves(int[] moves){
        int index = 0;
        while (moves[index] != 0){
            index++;
        }
        return index;
    }

    private static void reset(){
        nodesForNps = 0;
    }
}
