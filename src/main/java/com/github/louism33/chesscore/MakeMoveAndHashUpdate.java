package com.github.louism33.chesscore;

import org.junit.Assert;

import static com.github.louism33.chesscore.MakeMoveRegular.makeMoveMaster;
import static com.github.louism33.chesscore.MoveUnmaker.unMakeMoveMaster;

class MakeMoveAndHashUpdate {

    static void makeMoveAndHashUpdate(Chessboard board, int move){
        Assert.assertNotEquals(move, 0);

        board.zobristStackArrayPush(board.getBoardHash());
        
        board.getZobristStack().push(board.getBoardHash());
        board.setBoardHash(ZobristHashUtil.updateHashPreMove(board, board.getBoardHash(), move));
        board.setZOBBYHash(ZobristHashUtil.updateHashPreMove(board, board.zobbyHash, move));
        
        
        makeMoveMaster(board, move);
        
        board.setBoardHash(ZobristHashUtil.updateHashPostMove(board, board.getBoardHash(), move));
        board.setZOBBYHash((ZobristHashUtil.updateHashPostMove(board, board.zobbyHash, move)));
    }

    static void UnMakeMoveAndHashUpdate(Chessboard board) throws IllegalUnmakeException {
        
       
       
        unMakeMoveMaster(board);
    }

    static void makeNullMoveAndHashUpdate(Chessboard board){

        board.zobristStackArrayPush(board.getBoardHash());
        board.getZobristStack().push(board.getBoardHash());
        
        if (board.hasPreviousMove()){
            board.setBoardHash(ZobristHashUtil.updateWithEPFlags(board, board.getBoardHash()));
            board.setZOBBYHash(ZobristHashUtil.updateWithEPFlags(board, board.zobbyHash));
        }

        makeMoveMaster(board, 0);
        
        board.setBoardHash(ZobristHashUtil.zobristFlipTurn(board.getBoardHash()));
        board.setZOBBYHash(ZobristHashUtil.zobristFlipTurn(board.zobbyHash));
    }

    static void unMakeNullMove(Chessboard board) throws IllegalUnmakeException {

        // todo: replace pop with delta calc
//        board.setBoardHash(board.getZobristStack().pop());
        unMakeMoveMaster(board);
    }
}

