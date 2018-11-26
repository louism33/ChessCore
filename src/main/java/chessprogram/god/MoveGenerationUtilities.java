package chessprogram.god;

import java.util.ArrayList;
import java.util.List;

class MoveGenerationUtilities {

    static void movesFromAttackBoard(List<Move> moves, long attackBoard, int source) {
        List<Integer> indexOfAllPieces = BitOperations.getIndexOfAllPieces(attackBoard);
        for (int i : indexOfAllPieces) {
            moves.add(new Move(source, i));
        }
    }

    static void movesFromAttackBoardCapture(List<Move> moves, long attackBoard, int source, boolean capture) {
        List<Integer> indexOfAllPieces = BitOperations.getIndexOfAllPieces(attackBoard);
        for (int i : indexOfAllPieces) {
            if (capture) {
                final Move m = new Move(source, i, true);
                moves.add(m);
            }
            else {
                moves.add(new Move(source, i));
            }

        }
    }
    
    

    static void addMovesFromAttackBoardLong(List<Move> moves, long attackBoard, long longSource) {
        int source = BitOperations.getIndexOfFirstPiece(longSource);
        List<Integer> indexOfAllPieces = BitOperations.getIndexOfAllPieces(attackBoard);
        for (int i : indexOfAllPieces) {
            moves.add(new Move(source, i));
        }
    }

}