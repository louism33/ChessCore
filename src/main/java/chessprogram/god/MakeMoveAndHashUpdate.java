package chessprogram.god;

import static chessprogram.god.MoveUnmaker.unMakeMoveMaster;

class MakeMoveAndHashUpdate {

    static void makeMoveAndHashUpdate(Chessboard board, Move move, ZobristHash zobristHash){
        zobristHash.zobristStack.push(zobristHash.getBoardHash());
        zobristHash.updateHashPreMove(board, move);
        MoveMaker.makeMoveMaster(board, move);
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

        MoveMaker.makeMoveMaster(board, null);
        zobristHash.setBoardHash(zobristHash.getBoardHash() ^ ZobristHash.zobristHashColourBlack);
    }

    static void unMakeNullMove(Chessboard board, ZobristHash zobristHash){
        zobristHash.setBoardHash(zobristHash.zobristStack.pop());
        unMakeMoveMaster(board);
    }
}

