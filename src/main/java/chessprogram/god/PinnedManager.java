package chessprogram.god;

import static chessprogram.god.PieceMoveSliding.xrayBishopAttacks;
import static chessprogram.god.PieceMoveSliding.xrayRookAttacks;

class PinnedManager {

    static long whichPiecesArePinned(Chessboard board, boolean white, long squareOfInterest,
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
    
    static long whichPiecesArePinned(Chessboard board, boolean white, Square squareOfInterest,
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
            final int indexOfPinningPiece = BitOperations.getIndexOfFirstPiece(pinningPieces);
            final long ray = Magic.extractRayFromTwoPieces(indexOfPinningPiece, BitOperations.getIndexOfFirstPiece(squareOfInterest));
            pinnedPieces |= (ray & friends);
            pinningPieces &= pinningPieces - 1;
        }

        pinners = xrayRookAttacks(allPieces, friends, squareOfInterest);
        pinningPieces = pinners & (enemyRooks | enemyQueens);

        while (pinningPieces != 0){
            final int indexOfPinningPiece = BitOperations.getIndexOfFirstPiece(pinningPieces);
            final long ray = Magic.extractRayFromTwoPieces(indexOfPinningPiece, BitOperations.getIndexOfFirstPiece(squareOfInterest));
            pinnedPieces |= (ray & friends);
            pinningPieces &= pinningPieces - 1;
        }

        return pinnedPieces;
    }

}
