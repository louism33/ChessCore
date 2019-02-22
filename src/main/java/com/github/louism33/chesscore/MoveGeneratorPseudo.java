package com.github.louism33.chesscore;

import static com.github.louism33.chesscore.MoveGeneratorRegular.*;
import static com.github.louism33.chesscore.PieceMove.*;

class MoveGeneratorPseudo {

    public static void addAllMovesWithoutKing(int[] moves, Chessboard board, boolean whiteTurn,
                                              long ignoreThesePieces, long legalPushes, long legalCaptures,
                                              long myKnights, long myBishops, long myRooks, long myQueens,
                                              long allPieces){

        addKnightMoves(moves, board, ignoreThesePieces, (legalPushes | legalCaptures), 
                myKnights);
        addSlidingMoves(moves, board, whiteTurn, ignoreThesePieces, (legalPushes | legalCaptures),
                myBishops, myRooks, myQueens, allPieces);
        addPawnPushes(moves, board, whiteTurn, ignoreThesePieces, legalCaptures, legalPushes, 
                allPieces);
    }

    public static long generatePseudoCaptureTable(boolean whiteTurn,
                                                  long ignoreThesePieces, long legalPushes, long legalCaptures,
                                                  long enemyPawns, long enemyKnights, long enemyBishops, long enemyRooks, long enemyQueens, long enemyKing,
                                                  long allPieces){
        long ans = 0;

        ans |= masterAttackTableKing(ignoreThesePieces, legalPushes, legalCaptures,
                enemyKing);

        ans |= masterAttackTableKnights(ignoreThesePieces, legalPushes, legalCaptures, 
                enemyKnights);

        ans |= masterAttackTableSliding(ignoreThesePieces, legalPushes, legalCaptures,
                enemyBishops, enemyRooks, enemyQueens, allPieces);

        ans |= masterPawnCapturesTable(whiteTurn, ignoreThesePieces, legalCaptures, enemyPawns);

        return ans;
    }

}
