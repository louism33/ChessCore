package chessprogram.god;

import java.util.ArrayList;
import java.util.List;

import static chessprogram.god.BitOperations.getFirstPiece;
import static chessprogram.god.BitOperations.getIndexOfFirstPiece;
import static chessprogram.god.BitboardResources.*;
import static chessprogram.god.MoveConstants.*;
import static chessprogram.god.MoveGenerationUtilitiesIntMove.addMovesFromAttackTableMaster;
import static chessprogram.god.PieceMovePawnsIntMove.singlePawnCaptures;
import static chessprogram.god.PieceMovePawnsIntMove.singlePawnPushes;

class MoveGeneratorPromotionIntMove {

    static void addPromotionMoves(List<Integer> moves, ChessboardIntMove board, boolean white,
                                               long ignoreThesePieces, long legalPushes, long legalCaptures){
        long legalPieces = ~ignoreThesePieces;
        long PENULTIMATE_RANK;
        long FINAL_RANK;
        long promotablePawns;
        long promotionCaptureSquares;

        if (white) {
            PENULTIMATE_RANK = RANK_SEVEN;
            promotablePawns = board.getWhitePawns() & PENULTIMATE_RANK & legalPieces;
            promotionCaptureSquares = RANK_EIGHT & board.blackPieces();
            FINAL_RANK = RANK_EIGHT;
        }
        else {
            PENULTIMATE_RANK = RANK_TWO;
            promotablePawns = board.getBlackPawns() & PENULTIMATE_RANK & legalPieces;
            promotionCaptureSquares = RANK_ONE & board.whitePieces();
            FINAL_RANK = RANK_ONE;
        }

        while (promotablePawns != 0){
            final long pawn = getFirstPiece(promotablePawns);
            long pawnMoves = singlePawnPushes(board, pawn, board.isWhiteTurn(), (FINAL_RANK & legalPushes))
                    | singlePawnCaptures(pawn, board.isWhiteTurn(), (promotionCaptureSquares & legalCaptures));
            
            if (pawnMoves != 0) {
                List<Integer> todoMoves = new ArrayList<>();
                addMovesFromAttackTableMaster(todoMoves, pawnMoves, getIndexOfFirstPiece(pawn), board);
                
                // todo
                for (int move : todoMoves){
                    move |= PROMOTION_MASK;
                    moves.addAll(promotingMovesByPiece(move));
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
