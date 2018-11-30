package chessprogram.god;

import static chessprogram.god.BitOperations.getFirstPiece;
import static chessprogram.god.BitOperations.getIndexOfFirstPiece;
import static chessprogram.god.MoveConstants.*;
import static chessprogram.god.MoveParser.moveFromSourceDestinationCapture;

class MoveAdder {

    private static int moveIndex(int[] moves){
        int index = 0;
        while (moves[index] != 0){
            index++;
        }
        
        return index;
    }
    
    public static void addMovesFromAttackTableMasterCastling(int[] moves, int source, int destination) {

        int index = moveIndex(moves);

        moves[index] = MoveParser.makeSpecialMove(source, destination, true, false, false, false, false, false, false);
    }

    public static void addMovesFromAttackTableMasterPromotion(int[] moves, long attackBoard, int source, long enemyPieces) {
        while (attackBoard != 0){
            int index = moveIndex(moves);
            
            final long destination = getFirstPiece(attackBoard);
            final boolean capture = (destination & enemyPieces) != 0;
            final int move = moveFromSourceDestinationCapture(source, getIndexOfFirstPiece(destination), capture) | PROMOTION_MASK;
            
            moves[index] = move | KNIGHT_PROMOTION_MASK;
            moves[index+1] = move | BISHOP_PROMOTION_MASK;
            moves[index+2] = move | ROOK_PROMOTION_MASK;
            moves[index+3] = move | QUEEN_PROMOTION_MASK;

            attackBoard &= attackBoard - 1;
        }
    }

    public static void addMovesFromAttackTableMaster(int[] moves, long attackBoard, int source, Chessboard board) {
        addMovesFromAttackTableMaster(moves, attackBoard, source, board.isWhiteTurn() ? board.blackPieces() : board.whitePieces());
    }

    public static void addMovesFromAttackTableMaster(int[] moves, long attackBoard, int source, long enemyPieces) {

        int index = moveIndex(moves);
        
        while (attackBoard != 0){
            final long destination = getFirstPiece(attackBoard);

            moves[index] = moveFromSourceDestinationCapture(source, getIndexOfFirstPiece(destination),
                    ((destination & enemyPieces) != 0));

            index++;

            attackBoard &= attackBoard - 1;
        }
    }
    
}