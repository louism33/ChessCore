package chessprogram.god;

import static chessprogram.god.MoveGeneratorRegular.addKnightMoves;
import static chessprogram.god.MoveGeneratorRegular.addPawnPushes;
import static chessprogram.god.MoveGeneratorRegular.addSlidingMoves;
import static chessprogram.god.PieceMove.masterAttackTableKing;
import static chessprogram.god.PieceMove.masterAttackTableKnights;
import static chessprogram.god.PieceMove.masterAttackTableSliding;
import static chessprogram.god.PieceMove.masterPawnCapturesTable;

class MoveGeneratorPseudo {

    public static void addAllMovesWithoutKing(int[] moves, Chessboard board, boolean whiteTurn,
                                              long ignoreThesePieces, long legalPushes, long legalCaptures,
                                              long myPawns, long myKnights, long myBishops, long myRooks, long myQueens, long myKing,
                                              long enemyPawns, long enemyKnights, long enemyBishops, long enemyRooks, long enemyQueens, long enemyKing,
                                              long enemies, long friends, long allPieces){

        addKnightMoves(moves, board, whiteTurn, ignoreThesePieces, (legalPushes | legalCaptures), 
                myKnights);
        addSlidingMoves(moves, board, whiteTurn, ignoreThesePieces, (legalPushes | legalCaptures),
                myBishops, myRooks, myQueens, allPieces);
        addPawnPushes(moves, board, whiteTurn, ignoreThesePieces, legalCaptures, legalPushes, 
                myPawns, allPieces);
    }

    public static long generatePseudoCaptureTable(Chessboard board, boolean whiteTurn,
                                                  long ignoreThesePieces, long legalPushes, long legalCaptures,
                                                  long myPawns, long myKnights, long myBishops, long myRooks, long myQueens, long myKing,
                                                  long enemyPawns, long enemyKnights, long enemyBishops, long enemyRooks, long enemyQueens, long enemyKing,
                                                  long enemies, long friends, long allPieces){
        long ans = 0;

        ans |= masterAttackTableKing(board, whiteTurn, ignoreThesePieces, legalPushes, legalCaptures,
                enemyKing);

        ans |= masterAttackTableKnights(board, whiteTurn, ignoreThesePieces, legalPushes, legalCaptures, 
                enemyKnights);

        ans |= masterAttackTableSliding(board, whiteTurn, ignoreThesePieces, legalPushes, legalCaptures,
                enemyBishops, enemyRooks, enemyQueens, allPieces);

        ans |= masterPawnCapturesTable(board, whiteTurn, ignoreThesePieces, legalCaptures, enemyPawns);

        return ans;
    }

}
