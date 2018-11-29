package chessprogram.god;

import java.util.List;

import static chessprogram.god.BitOperations.*;
import static chessprogram.god.MoveParser.*;

class MoveGenerationUtilities {

    public static void addMovesFromAttackTableMaster(List<Integer> moves, long attackBoard, int source, Chessboard board) {
        addMovesFromAttackTableMaster(moves, attackBoard, source, board.isWhiteTurn() ? board.blackPieces() : board.whitePieces());
    }

    public static void addMovesFromAttackTableMaster(List<Integer> moves, long attackBoard, int source, long enemyPieces) {
        while (attackBoard != 0){
            final long destination = getFirstPiece(attackBoard);
            
            moves.add(moveFromSourceDestinationCapture(source, getIndexOfFirstPiece(destination),
                    ((destination & enemyPieces) != 0)));
            
            attackBoard &= attackBoard - 1;
        }
    }

    @Deprecated
    static void addMovesFromAttackBoardLong(List<Integer> moves, long attackBoard, long longSource) {
        int source = getIndexOfFirstPiece(longSource);
        List<Integer> indexOfAllPieces = getIndexOfAllPieces(attackBoard);
        for (int i : indexOfAllPieces) {
            moves.add(moveFromSourceDestination(source, i));
        }
    }

}