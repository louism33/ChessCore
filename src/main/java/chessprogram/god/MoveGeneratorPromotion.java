package chessprogram.god;

import java.util.ArrayList;
import java.util.List;

import static chessprogram.god.BitOperations.getAllPieces;
import static chessprogram.god.MoveConstants.*;

class MoveGeneratorPromotion {

    static void addPromotionMoves(List<Move> moves, Chessboard board, boolean white,
                                  long ignoreThesePieces, long legalPushes, long legalCaptures){
        generatePromotionPushes(moves, board, white, ignoreThesePieces, legalPushes);
        generatePromotionCaptures(moves, board, white, ignoreThesePieces, legalCaptures);
    }

    private static void generatePromotionPushes(List<Move> moves, Chessboard board, boolean white,
                                                      long ignoreThesePieces, long legalPushes){
        long legalPieces = ~ignoreThesePieces;

        if (white){
            long PENULTIMATE_RANK = BitboardResources.RANK_SEVEN;
            long promotablePawns = board.getWhitePawns() & PENULTIMATE_RANK & legalPieces;
            if ((promotablePawns) != 0) {
                List<Long> allPromotablePawns = getAllPieces(promotablePawns, 0);
                for (long piece : allPromotablePawns) {
                    long pawnMoves = PieceMovePawns.singlePawnPushes(board, piece, true, (BitboardResources.RANK_EIGHT & legalPushes));
                    if (pawnMoves != 0) {
                        int indexOfPiece = BitOperations.getIndexOfFirstPiece(piece);
                        List<Move> todoMoves = new ArrayList<>();
                        MoveGenerationUtilities.addMovesFromAttackBoard(todoMoves, pawnMoves, indexOfPiece);
                        Move move = todoMoves.get(0);
                        move.move |= PROMOTION_MASK;
                        moves.addAll(promotingMovesByPiece(move));
                    }
                }
            }
        }

        else {
            long PENULTIMATE_RANK = BitboardResources.RANK_TWO;
            long promotablePawns = board.getBlackPawns() & PENULTIMATE_RANK & legalPieces;
            if ((promotablePawns) != 0) {
                List<Long> allPromotablePawns = getAllPieces(promotablePawns, 0);
                for (long piece : allPromotablePawns) {
                    long pawnMoves = PieceMovePawns.singlePawnPushes(board, piece, false, (BitboardResources.RANK_ONE & legalPushes));
                    if (pawnMoves != 0) {
                        int indexOfPiece = BitOperations.getIndexOfFirstPiece(piece);

                        List<Move> todoMoves = new ArrayList<>();
                        MoveGenerationUtilities.addMovesFromAttackBoard(todoMoves, pawnMoves, indexOfPiece);
                        Move move = todoMoves.get(0);
                        
                        move.move |= PROMOTION_MASK;
                        moves.addAll(promotingMovesByPiece(move));
                    }
                }
            }
        }

    }

    private static void generatePromotionCaptures(List<Move> moves, Chessboard board, boolean white,
                                                        long ignoreThesePieces, long legalCaptures){
        long legalPieces = ~ignoreThesePieces;

        if (white){
            long PENULTIMATE_RANK = BitboardResources.RANK_SEVEN;
            long promotablePawns = board.getWhitePawns() & PENULTIMATE_RANK & legalPieces;
            long promotionCaptureSquares = BitboardResources.RANK_EIGHT & board.blackPieces();
            if ((promotablePawns) != 0) {
                List<Long> allPromotablePawns = getAllPieces(promotablePawns, 0);
                for (long piece : allPromotablePawns) {
                    long pawnMoves = PieceMovePawns.singlePawnCaptures(piece, true, (promotionCaptureSquares & legalCaptures));
                    if (pawnMoves != 0) {
                        int indexOfPiece = BitOperations.getIndexOfFirstPiece(piece);
                        List<Move> unflaggedCaptures = new ArrayList<>();
                        
                        MoveGenerationUtilities.movesFromAttackBoardCapture(unflaggedCaptures, pawnMoves, indexOfPiece, true);

                        for (Move move : unflaggedCaptures) {
                            move.move |= PROMOTION_MASK;
                            moves.addAll(promotingMovesByPiece(move));
                        }
                    }
                }
            }
        }

        else {
            long PENULTIMATE_RANK = BitboardResources.RANK_TWO;
            long promotablePawns = board.getBlackPawns() & PENULTIMATE_RANK & legalPieces;
            long promotionCaptureSquares = BitboardResources.RANK_ONE & board.whitePieces();
            if ((promotablePawns) != 0) {
                List<Long> allPromotablePawns = getAllPieces(promotablePawns, 0);
                for (long piece : allPromotablePawns) {
                    long pawnMoves = PieceMovePawns.singlePawnCaptures(piece, false, (promotionCaptureSquares & legalCaptures));
                    if (pawnMoves != 0) {
                        int indexOfPiece = BitOperations.getIndexOfFirstPiece(piece);
                        List<Move> unflaggedCaptures = new ArrayList<>();
                        MoveGenerationUtilities.movesFromAttackBoardCapture(unflaggedCaptures, pawnMoves, indexOfPiece, true);

                        for (Move move : unflaggedCaptures) {
                            move.move |= PROMOTION_MASK;
                            moves.addAll(promotingMovesByPiece(move));
                        }
                    }
                }
            }
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
