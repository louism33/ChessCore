package chessprogram.god;

import java.util.ArrayList;
import java.util.List;

class MoveGenerationUtilities {

    public static List<Move> movesFromAttackBoard(long attackBoard, int source) {
        List<Move> moves = new ArrayList<>();
        List<Integer> indexOfAllPieces = dBitIndexing.getIndexOfAllPieces(attackBoard);
        for (int i : indexOfAllPieces) {
            moves.add(new Move(source, i));
        }
        return moves;
    }

    static List<Move> movesFromAttackBoardLong(long attackBoard, long longSource) {
        List<Move> moves = new ArrayList<>();
        int source = dBitIndexing.getIndexOfFirstPiece(longSource);
        List<Integer> indexOfAllPieces = dBitIndexing.getIndexOfAllPieces(attackBoard);
        for (int i : indexOfAllPieces) {
            moves.add(new Move(source, i));
        }
        return moves;
    }

}