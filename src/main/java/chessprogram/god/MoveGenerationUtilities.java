package chessprogram.god;

import java.util.ArrayList;
import java.util.List;

class MoveGenerationUtilities {

    public static List<Move> movesFromAttackBoard(long attackBoard, int source) {
        List<Move> moves = new ArrayList<>();
        List<Integer> indexOfAllPieces = BitOperations.getIndexOfAllPieces(attackBoard);
        for (int i : indexOfAllPieces) {
            moves.add(new Move(source, i));
        }
        return moves;
    }

    public static List<Move> movesFromAttackBoardCapture(long attackBoard, int source, boolean capture) {
        List<Move> moves = new ArrayList<>();
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
        return moves;
    }

    static List<Move> movesFromAttackBoardLong(long attackBoard, long longSource) {
        List<Move> moves = new ArrayList<>();
        int source = BitOperations.getIndexOfFirstPiece(longSource);
        List<Integer> indexOfAllPieces = BitOperations.getIndexOfAllPieces(attackBoard);
        for (int i : indexOfAllPieces) {
            moves.add(new Move(source, i));
        }
        return moves;
    }

}