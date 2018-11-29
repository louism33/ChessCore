package chessprogram.god;

import java.util.List;

import static chessprogram.god.BitOperations.getFirstPiece;
import static chessprogram.god.BitOperations.getIndexOfFirstPiece;
import static chessprogram.god.MoveGenerationUtilitiesIntMove.addMovesFromAttackTableMaster;
import static chessprogram.god.PieceMovePawnsIntMove.singlePawnCaptures;
import static chessprogram.god.PieceMovePawnsIntMove.singlePawnPushes;

class MoveGeneratorPawnsIntMove {

    static void addPawnPushes(List<Integer> moves, ChessboardIntMove board, boolean white,
                              long ignoreThesePieces, long legalCaptures, long legalPushes){

        long pawns;
        if (white){
            pawns = board.getWhitePawns();
        }
        else {
            pawns = board.getBlackPawns();
        }

        while (pawns != 0){
            long pawn = getFirstPiece(pawns);
            if ((pawn & ignoreThesePieces) == 0){
                final long multi = singlePawnPushes(board, pawn, white, legalPushes);
                final long pawnCaptures = singlePawnCaptures(pawn, white, legalCaptures);
                final int pawnIndex = getIndexOfFirstPiece(pawn);
                addMovesFromAttackTableMaster(moves, multi | pawnCaptures, pawnIndex, board);
            }
            pawns &= pawns - 1;
        }

    }

}
