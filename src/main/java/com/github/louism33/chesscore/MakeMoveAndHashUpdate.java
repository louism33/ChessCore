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
        board.getZobristStack().push(board.getBoardHash());
        
        if (board.moveStackCool.size() > 0) {
            ZobristHashUtil.updateWithEPFlags(board, board.getBoardHash());
        }

        makeMoveMaster(board, 0);
        
        board.setBoardHash(ZobristHashUtil.zobristFlipTurn(board.getBoardHash()));
    }

    static void unMakeNullMove(Chessboard board) throws IllegalUnmakeException {
        board.setBoardHash(board.getZobristStack().pop());
        unMakeMoveMaster(board);
    }
}

