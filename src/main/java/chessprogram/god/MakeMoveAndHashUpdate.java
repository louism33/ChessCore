package chessprogram.god;

import static chessprogram.god.MoveUnmaker.unMakeMoveMaster;

public class MakeMoveAndHashUpdate {

    static void makeMoveAndHashUpdate(Chessboard board, Move move, ZobristHash zobristHash){
        zobristHash.zobristStack.push(zobristHash.getBoardHash());
        zobristHash.updateHashPreMove(board, move);
        MoveOrganiser.makeMoveMaster(board, move);
        zobristHash.updateHashPostMove(board, move);
    }

    static void UnMakeMoveAndHashUpdate(Chessboard board, ZobristHash zobristHash){
        zobristHash.setBoardHash(zobristHash.zobristStack.pop());
        unMakeMoveMaster(board);
    }

    public static void makeNullMoveAndHashUpdate(Chessboard board, ZobristHash zobristHash){
        zobristHash.zobristStack.push(zobristHash.getBoardHash());
        if (board.moveStack.size() > 0) {
            zobristHash.updateWithEPFlags(board);
        }
        zobristHash.setBoardHash(zobristHash.getBoardHash() ^ ZobristHash.zobristHashColourBlack);
    }

    public static void unMakeNullMove(Chessboard board, ZobristHash zobristHash){
        zobristHash.setBoardHash(zobristHash.zobristStack.pop());
    }
}

