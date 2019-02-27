package com.github.louism33.utils;

import com.github.louism33.chesscore.Art;
import com.github.louism33.chesscore.Chessboard;

public class Perft {

    static Chessboard board;
    private static long nodesForNps = 0;

    public static long perftTest(Chessboard b, int d){
        board = b;
        if (d < 1) {
            d = 1;
        }
        return countFinalNodesAtDepth(d);
    }

    private static long countFinalNodesAtDepth(int depth){
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

            board.makeMoveAndFlipTurnBetter(move);

            long movesAtDepth = countFinalNodesAtDepth(depth - 1);
            temp += movesAtDepth;
            board.unMakeMoveAndFlipTurn();
        }
        return temp;
    }

    public static long perftTest(int d, Chessboard board){
        String s = Art.boardArt(board);
        System.out.println(s);
        return runPerftTestWithBoard(d, board);
    }

    public static long perftTest(int d, Chessboard board, long correctAnswer){
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
        long ii = 0;
        ii = countFinalNodesAtDepthHelper(board, depth);
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
        int[] moves = board.generateLegalMoves();

        if (depth == 1){
            final int size = moves[moves.length - 1];
            nodesForNps += size;
            return size;
        }
        for (int i = 0; i < moves.length; i++) {
            int move = moves[i];
            if (move == 0) {
                break;
            }

            board.makeMoveAndFlipTurnBetter(move);

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
