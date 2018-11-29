package chessprogram.god;

import java.util.Stack;

public class CopierToBeDeletedIntMove {

    public static ChessboardIntMove copyBoard(ChessboardIntMove board, boolean white, boolean ignoreMyKing){
        ChessboardIntMove newBoard = new ChessboardIntMove();
        
        newBoard.moveStack = (Stack< StackMoveDataIntMove >) board.moveStack.clone();
        
        newBoard.setWhitePawns(board.getWhitePawns());
        newBoard.setWhiteKnights(board.getWhiteKnights());
        newBoard.setWhiteBishops(board.getWhiteBishops());
        newBoard.setWhiteRooks(board.getWhiteRooks());
        newBoard.setWhiteQueen(board.getWhiteQueen());
        newBoard.setWhiteKing(board.getWhiteKing());

        newBoard.setBlackPawns(board.getBlackPawns());
        newBoard.setBlackKnights(board.getBlackKnights());
        newBoard.setBlackBishops(board.getBlackBishops());
        newBoard.setBlackRooks(board.getBlackRooks());
        newBoard.setBlackQueen(board.getBlackQueen());
        newBoard.setBlackKing(board.getBlackKing());

        if (ignoreMyKing && white) {
            newBoard.setWhiteKing(0);
        }
        if (ignoreMyKing && !white) {
            newBoard.setBlackKing(0);
        }

        newBoard.setWhiteCanCastleK(board.isWhiteCanCastleK());
        newBoard.setBlackCanCastleK(board.isBlackCanCastleK());
        newBoard.setWhiteCanCastleQ(board.isWhiteCanCastleQ());
        newBoard.setBlackCanCastleQ(board.isBlackCanCastleQ());

        newBoard.setWhiteTurn(board.isWhiteTurn());
        
        newBoard.makeZobrist();
        newBoard.cloneZobristStack(board.getZobristHash());
        
        return newBoard;
    }


}
