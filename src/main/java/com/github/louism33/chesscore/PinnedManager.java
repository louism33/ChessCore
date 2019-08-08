package com.github.louism33.chesscore;

import static com.github.louism33.chesscore.BitOperations.extractRayFromTwoPieces;
import static com.github.louism33.chesscore.BoardConstants.UNIVERSE;
import static com.github.louism33.chesscore.PieceMove.*;
import static java.lang.Long.lowestOneBit;
import static java.lang.Long.numberOfTrailingZeros;

public final class PinnedManager {

    public static long whichPiecesArePinned(Chessboard board, int turn, long squareOfInterest,
                                            long enemyBishops, long enemyRooks, long enemyQueens,
                                            long friends, long allPieces){
        if (squareOfInterest == 0) {
            return 0;
        }
        return pinsToSquare(board, turn,
                squareOfInterest, enemyBishops, enemyRooks,
                enemyQueens, friends, allPieces);
    }


    
    // todo, if possible combine with inCheck()

    /**
     * be very careful of calling this with anything other than the King square, as it sets fields
     * @param board
     * @param turn
     * @param squareOfInterest
     * @param enemyBishops
     * @param enemyRooks
     * @param enemyQueens
     * @param friends
     * @param allPieces
     * @return
     */
    private static long pinsToSquare(Chessboard board, int turn, long squareOfInterest,
                                     long enemyBishops, long enemyRooks, long enemyQueens,
                                     long friends, long allPieces) {

        long pinnedPieces = 0;
        long friendsTemp = friends;
//        long pinners = xrayBishopAttacks(allPieces, friends, squareOfInterest);


        final long bishopMoves = singleBishopTable(allPieces, squareOfInterest, UNIVERSE);
        board.kingVision[turn * 2 + Chessboard.KING_VISION_BISHOP] = bishopMoves;
        friendsTemp &= bishopMoves;
        long pinners = bishopMoves ^ singleBishopTable(allPieces ^ friendsTemp, squareOfInterest, UNIVERSE);

        
                
                
                
        long pinningPieces = pinners & (enemyBishops | enemyQueens);

        while (pinningPieces != 0){ // todo, can this be slimmed down
            final int indexOfPinningPiece = numberOfTrailingZeros(pinningPieces);
            final long ray = extractRayFromTwoPieces(indexOfPinningPiece, numberOfTrailingZeros(squareOfInterest));
            final long p = ray & friends;
            if (p != 0) {
                pinnedPieces |= p;
                board.pinningPieces[turn] |= lowestOneBit(pinningPieces);
            }
            pinningPieces &= pinningPieces - 1;
        }

//        pinners = xrayRookAttacks(allPieces, friends, squareOfInterest);

        friendsTemp = friends;
        final long rookMoves = singleRookTable(allPieces, squareOfInterest, UNIVERSE);
        board.kingVision[turn * 2 + Chessboard.KING_VISION_ROOK] = rookMoves;
        friendsTemp &= rookMoves;
        pinners = rookMoves ^ singleRookTable(allPieces ^ friendsTemp, squareOfInterest, UNIVERSE);




        pinningPieces = pinners & (enemyRooks | enemyQueens);

        while (pinningPieces != 0){
            final int indexOfPinningPiece = numberOfTrailingZeros(pinningPieces);
            final long ray = extractRayFromTwoPieces(indexOfPinningPiece, numberOfTrailingZeros(squareOfInterest));
            final long p = ray & friends;
            if (p != 0) {
                pinnedPieces |= p;
                board.pinningPieces[turn] |= lowestOneBit(pinningPieces);
            }
            pinningPieces &= pinningPieces - 1;
        }

        board.pinnedPieces[turn] = pinnedPieces;
        
        return pinnedPieces;
    }

}
