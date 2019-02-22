package com.github.louism33.chesscore;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

public class A {

    @Test
    void hi(){
        Chessboard board = new Chessboard();
//        int[] moves = board.generateCleanLegalMoves();
//
//        for (int m : moves) {
//            System.out.print(MoveParser.toString(m) + " " + MoveParser.getMovingPieceInt(m) + " " + MoveParser.getMovingPiece(m));
//            System.out.println();
//            
//            Chessboard chessboard = new Chessboard();
//
//            board.makeMoveAndFlipTurn(moves[2]);
//
//            System.out.println(board);
//            try {
//                board.unMakeMoveAndFlipTurn();
//            } catch (IllegalUnmakeException e) {
//                e.printStackTrace();
//            }
//
//            Assert.assertEquals(board, chessboard);
//        }

        for (int i = 0; i < 64; i++) {
//            Art.printLong(BoardConstants.fileForwardBlack[i] ^ BoardConstants.fileForwardBlack[i+16]);
            int mask = 0;
            long l = BoardConstants.fileForwardWhite[i];
            if (i < 8) {
                mask = 0;
            }
            else if (i < 16) {
                mask = 16;
            }
            else if (i <= 63 - 8) {
                mask = 8;
            }
//            if (i >= 48 && i <= 55) {
//                mask = -16;
//            }
            l ^= BoardConstants.fileForwardWhite[i +  mask];
            
//            System.out.println(i);
            
            if (i >=  64 - 8) {
                l = 0;
            }
            
//            Art.printLong(l);
            
            System.out.print(l+"L, ");
            if (i % 8 == 0 && i > 1) {
                System.out.println();
            }
//
//            Art.printLong(BoardConstants.fileForwardBlack[i]);
//            Art.printLong(BoardConstants.PAWN_PUSH_MASK_WHITE[i]);
//            System.out.println();
        }

//        Art.printLong(BoardConstants.fileForwardBlack[63]);
    }
}
