package com.github.louism33.chesscore;

import static com.github.louism33.chesscore.BitOperations.extractRayFromTwoPieces;
import static com.github.louism33.chesscore.BitOperations.getIndexOfFirstPiece;
import static com.github.louism33.chesscore.PieceMove.xrayBishopAttacks;
import static com.github.louism33.chesscore.PieceMove.xrayRookAttacks;

public class PinnedManager {

    public static long whichPiecesArePinned(Chessboard board, boolean white, long squareOfInterest,
                                     long myPawns, long myKnights, long myBishops, long myRooks, long myQueens, long myKing,
                                     long enemyPawns, long enemyKnights, long enemyBishops, long enemyRooks, long enemyQueens, long enemyKing,
                                     long enemies, long friends, long allPieces){
        if (squareOfInterest == 0) {
            return 0;
        }
        return pinsToSquare(board, white, squareOfInterest,
                myPawns, myKnights, myBishops, myRooks, myQueens, myKing,
                enemyPawns, enemyKnights, enemyBishops, enemyRooks, enemyQueens, enemyKing,
                enemies, friends, allPieces);
    }
    
    public static long whichPiecesArePinned(Chessboard board, boolean white, Square squareOfInterest,
                                     long myPawns, long myKnights, long myBishops, long myRooks, long myQueens, long myKing,
                                     long enemyPawns, long enemyKnights, long enemyBishops, long enemyRooks, long enemyQueens, long enemyKing,
                                     long enemies, long friends, long allPieces){
        return pinsToSquare(board, white, squareOfInterest.toBitboard(),
                myPawns, myKnights, myBishops, myRooks, myQueens, myKing,
                enemyPawns, enemyKnights, enemyBishops, enemyRooks, enemyQueens, enemyKing,
                enemies, friends, allPieces);
    }
    
    private static long pinsToSquare(Chessboard board, boolean white, long squareOfInterest,
                                     long myPawns, long myKnights, long myBishops, long myRooks, long myQueens, long myKing,
                                     long enemyPawns, long enemyKnights, long enemyBishops, long enemyRooks, long enemyQueens, long enemyKing,
                                     long enemies, long friends, long allPieces) {

        long pinnedPieces = 0;

        long pinners = xrayBishopAttacks(allPieces, friends, squareOfInterest);
        long pinningPieces = pinners & (enemyBishops | enemyQueens);

        while (pinningPieces != 0){
            final int indexOfPinningPiece = getIndexOfFirstPiece(pinningPieces);
            final long ray = extractRayFromTwoPieces(indexOfPinningPiece, getIndexOfFirstPiece(squareOfInterest));
            pinnedPieces |= (ray & friends);
            pinningPieces &= pinningPieces - 1;
        }

        pinners = xrayRookAttacks(allPieces, friends, squareOfInterest);
        pinningPieces = pinners & (enemyRooks | enemyQueens);

        while (pinningPieces != 0){
            final int indexOfPinningPiece = getIndexOfFirstPiece(pinningPieces);
            final long ray = extractRayFromTwoPieces(indexOfPinningPiece, getIndexOfFirstPiece(squareOfInterest));
            pinnedPieces |= (ray & friends);
            pinningPieces &= pinningPieces - 1;
        }

        return pinnedPieces;
    }

}
