package chessprogram.god;

import static chessprogram.god.BitOperations.getFirstPiece;
import static chessprogram.god.BitOperations.getIndexOfFirstPiece;
import static chessprogram.god.MoveGenerationUtilities.addMovesFromAttackTableMaster;
import static chessprogram.god.PieceMoveSliding.*;

class MoveGeneratorSlidingIntMove {

    static void addSlidingMoves (int[] moves, Chessboard board, boolean white,
                                 long ignoreThesePieces, long mask,
                                 long myBishops, long myRooks, long myQueens,
                                 long allPieces){
        while (myBishops != 0){
            long bishop = getFirstPiece(myBishops);
            if ((bishop & ignoreThesePieces) == 0) {
                long slides = singleBishopTable(allPieces, white, bishop, mask);
                addMovesFromAttackTableMaster(moves, slides, getIndexOfFirstPiece(bishop), board);
            }
            myBishops &= (myBishops - 1);
        }
        while (myRooks != 0){
            long rook = getFirstPiece(myRooks);
            if ((rook & ignoreThesePieces) == 0) {
                long slides = singleRookTable(allPieces, white, rook, mask);
                addMovesFromAttackTableMaster(moves, slides, getIndexOfFirstPiece(rook), board);
            }
            myRooks &= (myRooks - 1);
        }
        while (myQueens != 0){
            long queen = getFirstPiece(myQueens);
            if ((queen & ignoreThesePieces) == 0) {
                long slides = singleQueenTable(allPieces, white, queen, mask);
                addMovesFromAttackTableMaster(moves, slides, getIndexOfFirstPiece(queen), board);
            }
            myQueens &= (myQueens - 1);
        }
    }

}
