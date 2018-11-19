package chessprogram.god;

import java.util.ArrayList;
import java.util.List;

import static chessprogram.god.BitOperations.getAllPieces;

class MoveGeneratorPromotion {

    public static List<Move> generatePromotionMoves(Chessboard board, boolean white,
                                                    long ignoreThesePieces, long legalPushes, long legalCaptures){
        List<Move> moves = new ArrayList<>();
        moves.addAll(generatePromotionPushes(board, white, ignoreThesePieces, legalPushes));
        moves.addAll(generatePromotionCaptures(board, white, ignoreThesePieces, legalCaptures));
        return moves;
    }

    private static List<Move> generatePromotionPushes(Chessboard board, boolean white, 
                                                     long ignoreThesePieces, long legalPushes){
        List<Move> moves = new ArrayList<>();
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
                        Move move = MoveGenerationUtilities.movesFromAttackBoard(pawnMoves, indexOfPiece).get(0);
                        move.move |= Move.PROMOTION_MASK;
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

                        Move move = MoveGenerationUtilities.movesFromAttackBoard(pawnMoves, indexOfPiece).get(0);
                        move.move |= Move.PROMOTION_MASK;
                        moves.addAll(promotingMovesByPiece(move));
                    }
                }
            }
        }

        return moves;
    }

    private static List<Move> generatePromotionCaptures(Chessboard board, boolean white,
                                                       long ignoreThesePieces, long legalCaptures){
        List<Move> moves = new ArrayList<>();
        long legalPieces = ~ignoreThesePieces;

        if (white){
            long PENULTIMATE_RANK = BitboardResources.RANK_SEVEN;
            long promotablePawns = board.getWhitePawns() & PENULTIMATE_RANK & legalPieces;
            long promotionCaptureSquares = BitboardResources.RANK_EIGHT & board.blackPieces();
            if ((promotablePawns) != 0) {
                List<Long> allPromotablePawns = getAllPieces(promotablePawns, 0);
                for (long piece : allPromotablePawns) {
                    long pawnMoves = PieceMovePawns.singlePawnCaptures(board, piece, true, (promotionCaptureSquares & legalCaptures));
                    if (pawnMoves != 0) {
                        int indexOfPiece = BitOperations.getIndexOfFirstPiece(piece);
                        List<Move> unflaggedCaptures = MoveGenerationUtilities.movesFromAttackBoardCapture(pawnMoves, indexOfPiece, true);

                        for (Move move : unflaggedCaptures) {
                            move.move |= Move.PROMOTION_MASK;
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
                    long pawnMoves = PieceMovePawns.singlePawnCaptures(board, piece, false, (promotionCaptureSquares & legalCaptures));
                    if (pawnMoves != 0) {
                        int indexOfPiece = BitOperations.getIndexOfFirstPiece(piece);
                        List<Move> unflaggedCaptures = MoveGenerationUtilities.movesFromAttackBoardCapture(pawnMoves, indexOfPiece, true);

                        for (Move move : unflaggedCaptures) {
                            move.move |= Move.PROMOTION_MASK;
                            moves.addAll(promotingMovesByPiece(move));
                        }
                    }
                }
            }
        }

        return moves;
    }




    private static List<Move> promotingMovesByPiece(Move move){
        List<Move> moves = new ArrayList<>();

        Move moveK = new Move(move);
        moveK.move |= Move.KNIGHT_PROMOTION_MASK;
        moves.add(moveK);

        Move moveB = new Move(move);
        moveB.move |= Move.BISHOP_PROMOTION_MASK;
        moves.add(moveB);

        Move moveR = new Move(move);
        moveR.move |= Move.ROOK_PROMOTION_MASK;
        moves.add(moveR);

        Move moveQ = new Move(move);
        moveQ.move |= Move.QUEEN_PROMOTION_MASK;
        moves.add(moveQ);

        return moves;
    }
}
