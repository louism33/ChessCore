package chessprogram.god;

import java.util.List;

class MoveGenerationUtilitiesIntMove {

    public static void addMovesFromAttackTableMaster(List<Integer> moves, long attackBoard, int source, ChessboardIntMove board) {
        addMovesFromAttackTableMaster(moves, attackBoard, source, board.isWhiteTurn() ? board.blackPieces() : board.whitePieces());
    }

    public static void addMovesFromAttackTableMaster(List<Integer> moves, long attackBoard, int source, long enemyPieces) {
        while (attackBoard != 0){
            final long destination = BitOperations.getFirstPiece(attackBoard);
            
            moves.add(MoveParserIntMove.moveFromSourceDestinationCapture(source, BitOperations.getIndexOfFirstPiece(destination),
                    ((destination & enemyPieces) != 0)));
            
            attackBoard &= attackBoard - 1;
        }
    }

    @Deprecated
    static void addMovesFromAttackBoard(List<Integer> moves, long attackBoard, int source) {
        List<Integer> indexOfAllPieces = BitOperations.getIndexOfAllPieces(attackBoard);
        for (int i : indexOfAllPieces) {
            moves.add(MoveParserIntMove.moveFromSourceDestination(source, i));
        }
    }

    @Deprecated
    static void movesFromAttackBoardCapture(List<Integer> moves, long attackBoard, int source, boolean capture) {
        List<Integer> indexOfAllPieces = BitOperations.getIndexOfAllPieces(attackBoard);
        for (int i : indexOfAllPieces) {
            if (capture) {
                moves.add(MoveParserIntMove.moveFromSourceDestinationCapture(source, i, true));
            }
            else {
                moves.add(MoveParserIntMove.moveFromSourceDestination(source, i));
            }

        }
    }

    @Deprecated
    static void addMovesFromAttackBoardLong(List<Integer> moves, long attackBoard, long longSource) {
        int source = BitOperations.getIndexOfFirstPiece(longSource);
        List<Integer> indexOfAllPieces = BitOperations.getIndexOfAllPieces(attackBoard);
        for (int i : indexOfAllPieces) {
            moves.add(MoveParserIntMove.moveFromSourceDestination(source, i));
        }
    }

}