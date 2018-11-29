package chessprogram.god;

import java.util.List;

class MoveGeneratorSliding {

    static void addSlidingMoves (List<Move> moves, Chessboard board, boolean white,
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
            long bishop = BitOperations.getFirstPiece(bishops);
            if ((bishop & ignoreThesePieces) == 0) {
                long slides = PieceMoveSliding.singleBishopTable(allPieces, white, bishop, mask);
                MoveGenerationUtilities.addMovesFromAttackTableMaster(moves, slides, BitOperations.getIndexOfFirstPiece(bishop), board);
            }
            bishops &= (bishops - 1);
        }
        while (rooks != 0){
            long rook = BitOperations.getFirstPiece(rooks);
            if ((rook & ignoreThesePieces) == 0) {
                long slides = PieceMoveSliding.singleRookTable(allPieces, white, rook, mask);
                MoveGenerationUtilities.addMovesFromAttackTableMaster(moves, slides, BitOperations.getIndexOfFirstPiece(rook), board);
            }
            rooks &= (rooks - 1);
        }
        while (queens != 0){
            long queen = BitOperations.getFirstPiece(queens);
            if ((queen & ignoreThesePieces) == 0) {
                long slides = PieceMoveSliding.singleQueenTable(allPieces, white, queen, mask);
                MoveGenerationUtilities.addMovesFromAttackTableMaster(moves, slides, BitOperations.getIndexOfFirstPiece(queen), board);
            }
            queens &= (queens - 1);
        }
    }

}
