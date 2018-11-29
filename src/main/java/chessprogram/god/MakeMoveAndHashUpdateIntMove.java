package chessprogram.god;

import static chessprogram.god.MoveMakerIntMove.makeMoveMaster;
import static chessprogram.god.MoveUnmakerIntMove.unMakeMoveMaster;

class MakeMoveAndHashUpdateIntMove {

    static void makeMoveAndHashUpdate(ChessboardIntMove board, int move, ZobristHashIntMove zobristHash){
        zobristHash.zobristStack.push(zobristHash.getBoardHash());
        zobristHash.updateHashPreMove(board, move);
        makeMoveMaster(board, move);
        zobristHash.updateHashPostMove(board, move);
    }

    static void UnMakeMoveAndHashUpdate(ChessboardIntMove board, ZobristHashIntMove zobristHash){
        zobristHash.setBoardHash(zobristHash.zobristStack.pop());
        unMakeMoveMaster(board);
    }

    static void makeNullMoveAndHashUpdate(ChessboardIntMove board, ZobristHashIntMove zobristHash){
        zobristHash.zobristStack.push(zobristHash.getBoardHash());
        
        if (board.moveStack.size() > 0) {
            zobristHash.updateWithEPFlags(board);
        }

        makeMoveMaster(board, 0);
        zobristHash.setBoardHash(zobristHash.getBoardHash() ^ ZobristHashIntMove.zobristHashColourBlack);
    }

    static void unMakeNullMove(ChessboardIntMove board, ZobristHashIntMove zobristHash){
        zobristHash.setBoardHash(zobristHash.zobristStack.pop());
        unMakeMoveMaster(board);
    }
}

