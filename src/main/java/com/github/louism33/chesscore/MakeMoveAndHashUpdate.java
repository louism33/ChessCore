package com.github.louism33.chesscore;

import org.junit.Assert;

import java.util.Arrays;

import static com.github.louism33.chesscore.MakeMoveRegular.makeMoveMaster;
import static com.github.louism33.chesscore.MoveUnmaker.unMakeMoveMaster;

class MakeMoveAndHashUpdate {

    static void makeMoveAndHashUpdate(Chessboard board, int move){
        Assert.assertNotEquals(move, 0);

        board.getZobristStack().push(board.getBoardHash());
        board.zobristStackArrayPush(board.getBoardHash());


        long[] longs = board.filterZerosAndFlip(board.getZobristStackArray());
        
        if (!Arrays.equals(board.getZobristStackAsArray(), longs)){
            System.out.println("zobristStackArray length: " + longs.length);
            System.out.println("zobristStack size : " + board.getZobristStack().size());

            System.out.println("zobristStackArray:  " + Arrays.toString(longs));
            System.out.println("zobristStack:       " + Arrays.toString(board.getZobristStackAsArray()));

            System.out.println("index: "+board.getIndex());

//            System.out.println(Arrays.toString(board.getZobristStackArray()));
//            System.out.println(Arrays.toString(board.filterZerosAndFlip(board.getZobristStackArray())));
        }

        Assert.assertTrue(Arrays.equals(board.getZobristStackAsArray(), longs));
        
        board.setBoardHash(ZobristHashUtil.updateHashPreMove(board, board.getBoardHash(), move));
        
        makeMoveMaster(board, move);
        
        board.setBoardHash(ZobristHashUtil.updateHashPostMove(board, board.getBoardHash(), move));
    }

    static void UnMakeMoveAndHashUpdate(Chessboard board) throws IllegalUnmakeException {
        unMakeMoveMaster(board);
    }

    static void makeNullMoveAndHashUpdate(Chessboard board){
        board.getZobristStack().push(board.getBoardHash());
        board.zobristStackArrayPush(board.getBoardHash());
        
        if (board.hasPreviousMove()){
//            ZobristHashUtil.updateWithEPFlags(board, board.getBoardHash());
            board.setBoardHash(ZobristHashUtil.updateWithEPFlags(board, board.getBoardHash()));
        }

        makeMoveMaster(board, 0);
        
        board.setBoardHash(ZobristHashUtil.zobristFlipTurn(board.getBoardHash()));
    }

    static void unMakeNullMove(Chessboard board) throws IllegalUnmakeException {
        unMakeMoveMaster(board);
    }
}

