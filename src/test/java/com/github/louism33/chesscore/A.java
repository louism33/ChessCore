package com.github.louism33.chesscore;

import org.junit.jupiter.api.Test;

import static com.github.louism33.chesscore.BoardConstants.bigAdjacentSquares;

public class A {



    @Test
    void a(){
        Chessboard board = new Chessboard();

        int[] moves = board.generateLegalMoves();

        for (int i = 0; i < 64; i++) {
//            System.out.println(i + ":");
//            Art.printLong(BitOperations.newPieceOnSquare(i));
//            Art.printLong(bigAdjacentSquares[i]);
        }

    }
}
