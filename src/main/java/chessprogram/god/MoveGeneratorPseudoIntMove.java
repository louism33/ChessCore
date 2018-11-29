package chessprogram.god;

import java.util.List;

import static chessprogram.god.MoveGeneratorKnightIntMove.addKnightMoves;
import static chessprogram.god.MoveGeneratorPawnsIntMove.addPawnPushes;
import static chessprogram.god.MoveGeneratorSlidingIntMove.addSlidingMoves;
import static chessprogram.god.PieceMoveKingIntMove.masterAttackTableKing;
import static chessprogram.god.PieceMoveKnightIntMove.masterAttackTableKnights;
import static chessprogram.god.PieceMovePawnsIntMove.masterPawnCapturesTable;
import static chessprogram.god.PieceMoveSlidingIntMove.masterAttackTableSliding;

class MoveGeneratorPseudoIntMove {

    public static void addAllMovesWithoutKing(List<Integer> moves, Chessboard board, boolean whiteTurn,
                                              long ignoreThesePieces, long legalPushes, long legalCaptures){
        
        addKnightMoves(moves, board, whiteTurn, ignoreThesePieces, (legalPushes | legalCaptures));
        addSlidingMoves(moves, board, whiteTurn, ignoreThesePieces, (legalPushes | legalCaptures));
        addPawnPushes(moves, board, whiteTurn, ignoreThesePieces, legalCaptures, legalPushes);
    }

    public static long generatePseudoCaptureTable(Chessboard board, boolean whiteTurn,
                                                  long ignoreThesePieces, long legalPushes, long legalCaptures){
        long ans = 0;

        ans |= masterAttackTableKing(board, whiteTurn, ignoreThesePieces, legalPushes, legalCaptures);
        
        ans |= masterAttackTableKnights(board, whiteTurn, ignoreThesePieces, legalPushes, legalCaptures);

        ans |= masterAttackTableSliding(board, whiteTurn, ignoreThesePieces, legalPushes, legalCaptures);

        ans |= masterPawnCapturesTable(board, whiteTurn, ignoreThesePieces, legalCaptures);

        return ans;
    }

}
