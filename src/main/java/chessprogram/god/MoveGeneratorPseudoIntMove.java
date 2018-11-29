package chessprogram.god;

import java.util.List;

import static chessprogram.god.MoveGeneratorKnight.addKnightMoves;
import static chessprogram.god.MoveGeneratorPawnsIntMove.addPawnPushes;
import static chessprogram.god.MoveGeneratorSlidingIntMove.addSlidingMoves;
import static chessprogram.god.PieceMoveKingIntMove.masterAttackTableKing;
import static chessprogram.god.PieceMoveKnight.masterAttackTableKnights;
import static chessprogram.god.PieceMovePawns.masterPawnCapturesTable;
import static chessprogram.god.PieceMoveSliding.masterAttackTableSliding;

class MoveGeneratorPseudoIntMove {

    public static void addAllMovesWithoutKing(List<Integer> moves, Chessboard board, boolean whiteTurn,
                                              long ignoreThesePieces, long legalPushes, long legalCaptures,
                                              long myPawns, long myKnights, long myBishops, long myRooks, long myQueens, long myKing,
                                              long enemyPawns, long enemyKnights, long enemyBishops, long enemyRooks, long enemyQueens, long enemyKing,
                                              long enemies){
        
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
