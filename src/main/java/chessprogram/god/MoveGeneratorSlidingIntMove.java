package chessprogram.god;

import java.util.List;

import static chessprogram.god.BitOperations.getFirstPiece;
import static chessprogram.god.BitOperations.getIndexOfFirstPiece;
import static chessprogram.god.MoveGenerationUtilitiesIntMove.addMovesFromAttackTableMaster;

class MoveGeneratorSlidingIntMove {

    static void addSlidingMoves (List<Integer> moves, ChessboardIntMove board, boolean white,
                                           long ignoreThesePieces, long mask){
        long bishops, rooks, queens, allPieces = board.allPieces();
        if (white){
            bishops = board.getWhiteBishops();
            rooks = board.getWhiteRooks();
            queens = board.getWhiteQueen();
        }
        else {
            bishops = board.getBlackBishops();
            rooks = board.getBlackRooks();
            queens = board.getBlackQueen();
        }
        
        while (bishops != 0){
            long bishop = getFirstPiece(bishops);
            if ((bishop & ignoreThesePieces) == 0) {
                long slides = PieceMoveSliding.singleBishopTable(allPieces, white, bishop, mask);
                addMovesFromAttackTableMaster(moves, slides, getIndexOfFirstPiece(bishop), board);
            }
            bishops &= (bishops - 1);
        }
        while (rooks != 0){
            long rook = getFirstPiece(rooks);
            if ((rook & ignoreThesePieces) == 0) {
                long slides = PieceMoveSliding.singleRookTable(allPieces, white, rook, mask);
                addMovesFromAttackTableMaster(moves, slides, getIndexOfFirstPiece(rook), board);
            }
            rooks &= (rooks - 1);
        }
        while (queens != 0){
            long queen = getFirstPiece(queens);
            if ((queen & ignoreThesePieces) == 0) {
                long slides = PieceMoveSliding.singleQueenTable(allPieces, white, queen, mask);
                addMovesFromAttackTableMaster(moves, slides, getIndexOfFirstPiece(queen), board);
            }
            queens &= (queens - 1);
        }
    }

}
