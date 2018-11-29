package chessprogram.god;

import static chessprogram.god.PieceMoveSliding.xrayBishopAttacks;
import static chessprogram.god.PieceMoveSliding.xrayRookAttacks;

class PinnedManager {

    static long whichPiecesArePinned(Chessboard board, boolean white, long squareOfInterest){
        if (squareOfInterest == 0) {
            return 0;
        }
        return pinsToSquare(board, white, squareOfInterest);
    }
    
    static long whichPiecesArePinned(Chessboard board, boolean white, Square squareOfInterest){
        return pinsToSquare(board, white, squareOfInterest.toBitboard());
    }
    
    private static long pinsToSquare(Chessboard board, boolean white, long squareOfInterest) {
        long myPieces, enemyBishops, enemyRooks, enemyQueens, allPieces = board.allPieces();

        if (white){
            myPieces = board.whitePieces();
            enemyBishops = board.getBlackBishops();
            enemyRooks = board.getBlackRooks();
            enemyQueens = board.getBlackQueen();
        } else {
            myPieces = board.blackPieces();
            enemyBishops = board.getWhiteBishops();
            enemyRooks = board.getWhiteRooks();
            enemyQueens = board.getWhiteQueen();
        }

        long pinnedPieces = 0;

        long pinners = xrayBishopAttacks(allPieces, myPieces, squareOfInterest);
        long pinningPieces = pinners & (enemyBishops | enemyQueens);

        while (pinningPieces != 0){
            final int indexOfPinningPiece = BitOperations.getIndexOfFirstPiece(pinningPieces);
            final long ray = Magic.extractRayFromTwoPieces(indexOfPinningPiece, BitOperations.getIndexOfFirstPiece(squareOfInterest));
            pinnedPieces |= (ray & myPieces);
            pinningPieces &= pinningPieces - 1;
        }

        pinners = xrayRookAttacks(allPieces, myPieces, squareOfInterest);
        pinningPieces = pinners & (enemyRooks | enemyQueens);

        while (pinningPieces != 0){
            final int indexOfPinningPiece = BitOperations.getIndexOfFirstPiece(pinningPieces);
            final long ray = Magic.extractRayFromTwoPieces(indexOfPinningPiece, BitOperations.getIndexOfFirstPiece(squareOfInterest));
            pinnedPieces |= (ray & myPieces);
            pinningPieces &= pinningPieces - 1;
        }

        return pinnedPieces;
    }

}
