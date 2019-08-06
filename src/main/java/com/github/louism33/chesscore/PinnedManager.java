package com.github.louism33.chesscore;

import static com.github.louism33.chesscore.BitOperations.extractRayFromTwoPieces;
import static com.github.louism33.chesscore.PieceMove.xrayBishopAttacks;
import static com.github.louism33.chesscore.PieceMove.xrayRookAttacks;
import static java.lang.Long.lowestOneBit;
import static java.lang.Long.numberOfTrailingZeros;

public final class PinnedManager {

    public static long whichPiecesArePinned(Chessboard board, long squareOfInterest,
                                            long enemyBishops, long enemyRooks, long enemyQueens,
                                            long friends, long allPieces){
        if (squareOfInterest == 0) {
            return 0;
        }
        return pinsToSquare(board, squareOfInterest,
                enemyBishops, enemyRooks, enemyQueens,
                friends, allPieces);
    }
    
    // todo, if possible combine with inCheck()
    private static long pinsToSquare(Chessboard board, long squareOfInterest,
                                     long enemyBishops, long enemyRooks, long enemyQueens,
                                     long friends, long allPieces) {

        long pinnedPieces = 0;

        long pinners = xrayBishopAttacks(allPieces, friends, squareOfInterest);
        long pinningPieces = pinners & (enemyBishops | enemyQueens);

        while (pinningPieces != 0){
            final int indexOfPinningPiece = numberOfTrailingZeros(pinningPieces);
            final long ray = extractRayFromTwoPieces(indexOfPinningPiece, numberOfTrailingZeros(squareOfInterest));
            final long p = ray & friends;
            if (p != 0) {
                pinnedPieces |= p;
                board.pinningPieces |= lowestOneBit(pinningPieces);
            }
            pinningPieces &= pinningPieces - 1;
        }

        pinners = xrayRookAttacks(allPieces, friends, squareOfInterest);
        pinningPieces = pinners & (enemyRooks | enemyQueens);

        while (pinningPieces != 0){
            final int indexOfPinningPiece = numberOfTrailingZeros(pinningPieces);
            final long ray = extractRayFromTwoPieces(indexOfPinningPiece, numberOfTrailingZeros(squareOfInterest));
            final long p = ray & friends;
            if (p != 0) {
                pinnedPieces |= p;
                board.pinningPieces |= lowestOneBit(pinningPieces);
            }
            pinningPieces &= pinningPieces - 1;
        }

        return pinnedPieces;
    }

}
