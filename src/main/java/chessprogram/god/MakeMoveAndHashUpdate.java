package chessprogram.god;

import org.junit.Assert;

import static chessprogram.god.MakeMoveRegular.makeMoveMaster;
import static chessprogram.god.MoveUnmaker.unMakeMoveMaster;

class MakeMoveAndHashUpdate {

    static void makeMoveAndHashUpdate(Chessboard board, int move, ZobristHash zobristHash){

        Assert.assertNotEquals(move, 0);
        
        zobristHash.zobristStack.push(zobristHash.getBoardHash());
        zobristHash.updateHashPreMove(board, move);
        makeMoveMaster(board, move);
        zobristHash.updateHashPostMove(board, move);
    }

    static void UnMakeMoveAndHashUpdate(Chessboard board, ZobristHash zobristHash){
        zobristHash.setBoardHash(zobristHash.zobristStack.pop());
        unMakeMoveMaster(board);
    }

    static void makeNullMoveAndHashUpdate(Chessboard board, ZobristHash zobristHash){
        zobristHash.zobristStack.push(zobristHash.getBoardHash());
        
        if (board.moveStack.size() > 0) {
            zobristHash.updateWithEPFlags(board);
        }

        makeMoveMaster(board, 0);
        zobristHash.setBoardHash(zobristHash.getBoardHash() ^ ZobristHash.zobristHashColourBlack);
    }

    static void unMakeNullMove(Chessboard board, ZobristHash zobristHash){
        zobristHash.setBoardHash(zobristHash.zobristStack.pop());
        unMakeMoveMaster(board);
    }
}

