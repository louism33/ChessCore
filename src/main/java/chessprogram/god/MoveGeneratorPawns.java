package chessprogram.god;

import java.util.ArrayList;
import java.util.List;

import static chessprogram.god.BitOperations.getAllPieces;

class MoveGeneratorPawns {

    static void addPawnPushes(List<Move> moves, Chessboard board, boolean white,
                              long ignoreThesePieces, long legalCaptures, long legalPushes){

        long pawns;
        if (white){
            pawns = board.getWhitePawns();
        }
        else {
            pawns = board.getBlackPawns();
        }

        while (pawns != 0){
            long pawn = BitOperations.getFirstPiece(pawns);
            if ((pawn & ignoreThesePieces) == 0){
                final long pawnPushes = PieceMovePawns.singlePawnPushes(board, pawn, white, legalPushes);
                final int pawnIndex = BitOperations.getIndexOfFirstPiece(pawn);
                MoveGenerationUtilities.addMovesFromAttackTableMaster(moves, pawnPushes, pawnIndex, board);
                final long pawnCaptures = PieceMovePawns.singlePawnCaptures(pawn, white, legalCaptures);
                MoveGenerationUtilities.addMovesFromAttackTableMaster(moves, pawnCaptures, pawnIndex, board);
            }
            pawns &= pawns - 1;
        }
    }

}
