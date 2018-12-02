package com.github.louism33.chesscore;

import org.junit.Assert;

import static com.github.louism33.chesscore.MakeMoveRegular.makeMoveMaster;
import static com.github.louism33.chesscore.MoveUnmaker.unMakeMoveMaster;

class MakeMoveAndHashUpdate {

    static void makeMoveAndHashUpdate(Chessboard board, int move){

        Assert.assertNotEquals(move, 0);

        board.getZobristStack().push(board.getBoardHash());
        board.setBoardHash(ZobristHashUtil.updateHashPreMove(board, board.getBoardHash(), move));
        
        makeMoveMaster(board, move);
        board.setBoardHash(ZobristHashUtil.updateHashPostMove(board, board.getBoardHash(), move));
    }

    static void UnMakeMoveAndHashUpdate(Chessboard board) throws IllegalUnmakeException {
        board.setBoardHash(board.getZobristStack().pop());
        unMakeMoveMaster(board);
    }

    static void makeNullMoveAndHashUpdate(Chessboard board){
        final long boardHash = board.getBoardHash();
        board.getZobristStack().push(board.getBoardHash());
        
        if (board.moveStack.size() > 0) {
            
//            System.out.println(board.moveStack.peek());
            
//            board.setBoardHash(ZobristHashUtil.updateWithEPFlags(board, board.getBoardHash()));
            ZobristHashUtil.updateWithEPFlags(board, board.getBoardHash());
        }

        makeMoveMaster(board, 0);
        final long boardHash2 = board.getBoardHash();

//        System.out.println(boardHash);
//        System.out.println(boardHash2);
        
        board.setBoardHash(ZobristHashUtil.zobristFlipTurn(board.getBoardHash()));
        
        final long boardHash3 = board.getBoardHash();
//        System.out.println(boardHash3);

    }

    static void unMakeNullMove(Chessboard board) throws IllegalUnmakeException {
        board.setBoardHash(board.getZobristStack().pop());
        unMakeMoveMaster(board);
    }
}

