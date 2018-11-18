package chessprogram.miscAdmin;

import chessprogram.chessboard.Chessboard;
import chessprogram.moveMaking.StackMoveData;

import java.util.Stack;

class BlankBoard {
    
    static Chessboard blankBoard(){
        Chessboard board = new Chessboard();

        board.setWhitePawns(0);
        board.setWhiteKnights(0);
        board.setWhiteBishops(0);
        board.setWhiteRooks(0);
        board.setWhiteQueen(0);
        board.setWhiteKing(0);

        board.setBlackPawns(0);
        board.setBlackKnights(0);
        board.setBlackBishops(0);
        board.setBlackRooks(0);
        board.setBlackQueen(0);
        board.setBlackKing(0);
        
        return board;
    }

    static void printMoveStack(Chessboard board){
        int size = board.moveStack.size();
        Stack<StackMoveData> copyStack = (Stack<StackMoveData>) board.moveStack.clone();
        for (int m = 0; m < size; m++){
            System.out.println(copyStack.pop());
        }
    }
}
