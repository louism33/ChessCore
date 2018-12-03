package com.github.louism33.chesscore;

import org.junit.Assert;

import java.util.Arrays;

public class Perft {
    
    private static long nodesForNps = 0;

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
        try {
            ii = Perft.countFinalNodesAtDepthHelper(board, depth);
        } catch (IllegalUnmakeException e) {
            e.printStackTrace();
        }
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

    private static long countFinalNodesAtDepthHelper(Chessboard board, int depth) throws IllegalUnmakeException {
        long temp = 0;
        if (depth == 0){
            return 1;
        }
        int[] moves = board.generateLegalMoves();

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

//
//            if (board.filterZerosAndFlip().length != board.moveStack.size()){
//                System.out.println("moveStackArray: " + board.filterZerosAndFlip().length);
//                System.out.println("moveStack: " + board.moveStack.size());
//
//                System.out.println("moveStackArray:  " + Arrays.toString(board.filterZerosAndFlip()));
//                System.out.println("moveStack:       " + Arrays.toString(board.getMoveStackAsArray()));
//            }
//            Assert.assertEquals(board.filterZerosAndFlip().length, board.moveStack.size());
//            
//            if (!Arrays.equals(board.getMoveStackAsArray(), board.filterZerosAndFlip())){
//                System.out.println(Arrays.toString(board.filterZerosAndFlip()));
//                System.out.println(Arrays.toString(board.getMoveStackAsArray()));
//            }
//            
//            Assert.assertTrue(Arrays.equals(board.getMoveStackAsArray(), board.filterZerosAndFlip()));
            
            
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
