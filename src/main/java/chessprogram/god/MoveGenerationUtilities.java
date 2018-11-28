package chessprogram.god;

import java.util.ArrayList;
import java.util.List;

class MoveGenerationUtilities {

    public static void addMovesFromAttackTableMaster(List<Move> moves, long attackBoard, int source, Chessboard board) {
        addMovesFromAttackTableMaster(moves, attackBoard, source, board.isWhiteTurn() ? board.blackPieces() : board.whitePieces());
    }

    public static void addMovesFromAttackTableMaster(List<Move> moves, long attackBoard, int source, long enemyPieces) {
        while (attackBoard != 0){
            final long destination = BitOperations.getFirstPiece(attackBoard);
            moves.add(new Move(source, BitOperations.getIndexOfFirstPiece(destination), 
                    ((destination & enemyPieces) != 0)));
            attackBoard &= attackBoard - 1;
        }
    }

    @Deprecated
    static void addMovesFromAttackBoard(List<Move> moves, long attackBoard, int source) {
        List<Integer> indexOfAllPieces = BitOperations.getIndexOfAllPieces(attackBoard);
        for (int i : indexOfAllPieces) {
            moves.add(new Move(source, i));
        }
    }

    @Deprecated
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

    @Deprecated
    static void addMovesFromAttackBoardLong(List<Move> moves, long attackBoard, long longSource) {
        int source = BitOperations.getIndexOfFirstPiece(longSource);
        List<Integer> indexOfAllPieces = BitOperations.getIndexOfAllPieces(attackBoard);
        for (int i : indexOfAllPieces) {
            moves.add(new Move(source, i));
        }
    }

}