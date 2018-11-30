package chessprogram.god;

import org.junit.Assert;

import static chessprogram.god.MoveMaker.makeMoveMaster;
import static chessprogram.god.MoveUnmakerIntMove.unMakeMoveMaster;

class MakeMoveAndHashUpdate {

    static void makeMoveAndHashUpdate(Chessboard board, int move, ZobristHashIntMove zobristHash){

        Assert.assertNotEquals(move, 0);
        
        zobristHash.zobristStack.push(zobristHash.getBoardHash());
        zobristHash.updateHashPreMove(board, move);
        makeMoveMaster(board, move);
        zobristHash.updateHashPostMove(board, move);
    }

    static void UnMakeMoveAndHashUpdate(Chessboard board, ZobristHashIntMove zobristHash){
        zobristHash.setBoardHash(zobristHash.zobristStack.pop());
        unMakeMoveMaster(board);
    }

    static void makeNullMoveAndHashUpdate(Chessboard board, ZobristHashIntMove zobristHash){
        zobristHash.zobristStack.push(zobristHash.getBoardHash());
        
        if (board.moveStack.size() > 0) {
            zobristHash.updateWithEPFlags(board);
        }

        makeMoveMaster(board, 0);
        zobristHash.setBoardHash(zobristHash.getBoardHash() ^ ZobristHashIntMove.zobristHashColourBlack);
    }

    static void unMakeNullMove(Chessboard board, ZobristHashIntMove zobristHash){
        zobristHash.setBoardHash(zobristHash.zobristStack.pop());
        unMakeMoveMaster(board);
    }
}

