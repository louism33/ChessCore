package com.github.louism33.chesscore;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

public class A {
    
    @Test
    void hi(){
        Chessboard board = new Chessboard();
        int[] moves = board.generateCleanLegalMoves();

        for (int m : moves) {
            System.out.print(MoveParser.toString(m) + " " + MoveParser.getMovingPieceInt(m) + " " + MoveParser.getMovingPiece(m));
            System.out.println();
            
            Chessboard chessboard = new Chessboard();

            board.makeMoveAndFlipTurn(moves[2]);

            System.out.println(board);
            try {
                board.unMakeMoveAndFlipTurn();
            } catch (IllegalUnmakeException e) {
                e.printStackTrace();
            }

            Assert.assertEquals(board, chessboard);
        }


        
    }
}
