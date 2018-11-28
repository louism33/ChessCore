package chessprogram.god;

import java.util.List;

import static chessprogram.god.MoveGeneratorKnight.addKnightMoves;
import static chessprogram.god.MoveGeneratorPawns.addPawnPushes;
import static chessprogram.god.MoveGeneratorSliding.addSlidingMoves;

class MoveGeneratorPseudo {

    public static void addAllMovesWithoutKing(List<Move> moves, Chessboard board, boolean whiteTurn,
                                              long ignoreThesePieces, long legalPushes, long legalCaptures){
        
        addKnightMoves(moves, board, whiteTurn, ignoreThesePieces, (legalPushes | legalCaptures));
        addSlidingMoves(moves, board, whiteTurn, ignoreThesePieces, (legalPushes | legalCaptures));
        addPawnPushes(moves, board, whiteTurn, ignoreThesePieces, legalCaptures, legalPushes);
    }

    public static long generatePseudoCaptureTable(Chessboard board, boolean whiteTurn,
                                                  long ignoreThesePieces, long legalPushes, long legalCaptures){
        long ans = 0;

        ans |= PieceMoveKing.masterAttackTableKing(board, whiteTurn, ignoreThesePieces, legalPushes, legalCaptures);
        
        ans |= PieceMoveKnight.masterAttackTableKnights(board, whiteTurn, ignoreThesePieces, legalPushes, legalCaptures);

        ans |= PieceMoveSliding.masterAttackTableSliding(board, whiteTurn, ignoreThesePieces, legalPushes, legalCaptures);

        ans |= PieceMovePawns.masterPawnCapturesTable(board, whiteTurn, ignoreThesePieces, legalCaptures);

        return ans;
    }

}
