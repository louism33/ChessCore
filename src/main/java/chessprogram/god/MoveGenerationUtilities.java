package chessprogram.god;

import java.util.List;

class MoveGenerationUtilities {

    public static void addMovesFromAttackTableMaster(List<Integer> moves, long attackBoard, int source, Chessboard board) {
        addMovesFromAttackTableMaster(moves, attackBoard, source, board.isWhiteTurn() ? board.blackPieces() : board.whitePieces());
    }

    public static void addMovesFromAttackTableMaster(List<Integer> moves, long attackBoard, int source, long enemyPieces) {
        while (attackBoard != 0){
            final long destination = BitOperations.getFirstPiece(attackBoard);
            
            moves.add(MoveParser.moveFromSourceDestinationCapture(source, BitOperations.getIndexOfFirstPiece(destination),
                    ((destination & enemyPieces) != 0)));
            
            attackBoard &= attackBoard - 1;
        }
    }

    @Deprecated
    static void addMovesFromAttackBoardLong(List<Integer> moves, long attackBoard, long longSource) {
        int source = BitOperations.getIndexOfFirstPiece(longSource);
        List<Integer> indexOfAllPieces = BitOperations.getIndexOfAllPieces(attackBoard);
        for (int i : indexOfAllPieces) {
            moves.add(MoveParser.moveFromSourceDestination(source, i));
        }
    }

}