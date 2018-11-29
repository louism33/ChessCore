package chessprogram.god;

import java.util.ArrayList;
import java.util.List;

import static chessprogram.god.BitOperations.*;
import static chessprogram.god.BitboardResources.*;
import static chessprogram.god.MoveConstants.*;
import static chessprogram.god.PieceMovePawns.*;

class MoveGeneratorPromotion {

    static void addPromotionMoves(List<Move> moves, Chessboard board, boolean white,
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
                List<Move> todoMoves = new ArrayList<>();
                MoveGenerationUtilities.addMovesFromAttackTableMaster(todoMoves, pawnMoves, getIndexOfFirstPiece(pawn), board);
                
                for (Move move : todoMoves){
                    move.move |= PROMOTION_MASK;
                    moves.addAll(promotingMovesByPiece(move));
                }
            }
            promotablePawns &= promotablePawns - 1;
        }
    }

    private static List<Move> promotingMovesByPiece(Move move){
        List<Move> moves = new ArrayList<>();

        Move moveK = new Move(move);
        moveK.move |= KNIGHT_PROMOTION_MASK;
        moves.add(moveK);

        Move moveB = new Move(move);
        moveB.move |= BISHOP_PROMOTION_MASK;
        moves.add(moveB);

        Move moveR = new Move(move);
        moveR.move |= ROOK_PROMOTION_MASK;
        moves.add(moveR);

        Move moveQ = new Move(move);
        moveQ.move |= QUEEN_PROMOTION_MASK;
        moves.add(moveQ);

        return moves;
    }
}
