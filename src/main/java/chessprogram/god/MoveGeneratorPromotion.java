package chessprogram.god;

import java.util.ArrayList;
import java.util.List;

import static chessprogram.god.BitOperations.getFirstPiece;
import static chessprogram.god.BitOperations.getIndexOfFirstPiece;
import static chessprogram.god.BitboardResources.*;
import static chessprogram.god.MoveConstants.*;
import static chessprogram.god.MoveGenerationUtilities.addMovesFromAttackTableMaster;
import static chessprogram.god.PieceMovePawns.singlePawnCaptures;
import static chessprogram.god.PieceMovePawns.singlePawnPushes;

class MoveGeneratorPromotion {

    static void addPromotionMoves(List<Integer> moves, Chessboard board, boolean white,
                                  long ignoreThesePieces, long legalPushes, long legalCaptures,
                                  long myPawns, long myKnights, long myBishops, long myRooks, long myQueens, long myKing,
                                  long enemyPawns, long enemyKnights, long enemyBishops, long enemyRooks, long enemyQueens, long enemyKing,
                                  long enemies, long friends, long allPieces){
        long legalPieces = ~ignoreThesePieces;
        long penultimateRank;
        long finalRank;
        if (white) {
            penultimateRank = RANK_SEVEN;
            finalRank = RANK_EIGHT;
        }
        else {
            penultimateRank = RANK_TWO;
            finalRank = RANK_ONE;
        }
        
        long promotablePawns = myPawns & penultimateRank & legalPieces;
        
        while (promotablePawns != 0){
            final long pawn = getFirstPiece(promotablePawns);
            long pawnMoves = singlePawnPushes(board, pawn, board.isWhiteTurn(), (finalRank & legalPushes))
                    | singlePawnCaptures(pawn, board.isWhiteTurn(), ((finalRank & enemies) & legalCaptures));
            
            if (pawnMoves != 0) {
                List<Integer> todoMoves = new ArrayList<>();
                addMovesFromAttackTableMaster(todoMoves, pawnMoves, getIndexOfFirstPiece(pawn), board);
                
                for (int i = 0; i < todoMoves.size(); i++) {
                    moves.addAll(promotingMovesByPiece(todoMoves.get(i) | PROMOTION_MASK));
                }
            }
            promotablePawns &= promotablePawns - 1;
        }
    }

    private static List<Integer> promotingMovesByPiece(int move){
        List<Integer> moves = new ArrayList<>();
        moves.add(move | KNIGHT_PROMOTION_MASK);
        moves.add(move | BISHOP_PROMOTION_MASK);
        moves.add(move | ROOK_PROMOTION_MASK);
        moves.add(move | QUEEN_PROMOTION_MASK);
        return moves;
    }
}
