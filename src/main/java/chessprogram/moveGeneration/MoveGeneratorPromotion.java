package chessprogram.moveGeneration;

import chessprogram.bitboards.BitBoardUtils;
import chessprogram.chessboard.BitIndexing;
import chessprogram.chessboard.Chessboard;
import chessprogram.move.Move;

import java.util.ArrayList;
import java.util.List;

import static chessprogram.chessboard.BitExtractor.getAllPieces;

public class MoveGeneratorPromotion {

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
            long PENULTIMATE_RANK = BitBoardUtils.RANK_SEVEN;
            long promotablePawns = board.getWhitePawns() & PENULTIMATE_RANK & legalPieces;
            if ((promotablePawns) != 0) {
                List<Long> allPromotablePawns = getAllPieces(promotablePawns, 0);
                for (long piece : allPromotablePawns) {
                    long pawnMoves = PieceMovePawns.singlePawnPushes(board, piece, true, (BitBoardUtils.RANK_EIGHT & legalPushes));
                    if (pawnMoves != 0) {
                        int indexOfPiece = BitIndexing.getIndexOfFirstPiece(piece);
                        Move move = MoveGenerationUtilities.movesFromAttackBoard(pawnMoves, indexOfPiece).get(0);
                        move.move |= Move.PROMOTION_MASK;
                        moves.addAll(promotingMovesByPiece(move));
                    }
                }
            }
        }

        else {
            long PENULTIMATE_RANK = BitBoardUtils.RANK_TWO;
            long promotablePawns = board.getBlackPawns() & PENULTIMATE_RANK & legalPieces;
            if ((promotablePawns) != 0) {
                List<Long> allPromotablePawns = getAllPieces(promotablePawns, 0);
                for (long piece : allPromotablePawns) {
                    long pawnMoves = PieceMovePawns.singlePawnPushes(board, piece, false, (BitBoardUtils.RANK_ONE & legalPushes));
                    if (pawnMoves != 0) {
                        int indexOfPiece = BitIndexing.getIndexOfFirstPiece(piece);

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
            long PENULTIMATE_RANK = BitBoardUtils.RANK_SEVEN;
            long promotablePawns = board.getWhitePawns() & PENULTIMATE_RANK & legalPieces;
            long promotionCaptureSquares = BitBoardUtils.RANK_EIGHT & board.blackPieces();
            if ((promotablePawns) != 0) {
                List<Long> allPromotablePawns = getAllPieces(promotablePawns, 0);
                for (long piece : allPromotablePawns) {
                    long pawnMoves = PieceMovePawns.singlePawnCaptures(board, piece, true, (promotionCaptureSquares & legalCaptures));
                    if (pawnMoves != 0) {
                        int indexOfPiece = BitIndexing.getIndexOfFirstPiece(piece);
                        List<Move> unflaggedCaptures = MoveGenerationUtilities.movesFromAttackBoard(pawnMoves, indexOfPiece);

                        for (Move move : unflaggedCaptures) {
                            move.move |= Move.PROMOTION_MASK;
                            moves.addAll(promotingMovesByPiece(move));
                        }
                    }
                }
            }
        }

        else {
            long PENULTIMATE_RANK = BitBoardUtils.RANK_TWO;
            long promotablePawns = board.getBlackPawns() & PENULTIMATE_RANK & legalPieces;
            long promotionCaptureSquares = BitBoardUtils.RANK_ONE & board.whitePieces();
            if ((promotablePawns) != 0) {
                List<Long> allPromotablePawns = getAllPieces(promotablePawns, 0);
                for (long piece : allPromotablePawns) {
                    long pawnMoves = PieceMovePawns.singlePawnCaptures(board, piece, false, (promotionCaptureSquares & legalCaptures));
                    if (pawnMoves != 0) {
                        int indexOfPiece = BitIndexing.getIndexOfFirstPiece(piece);
                        List<Move> unflaggedCaptures = MoveGenerationUtilities.movesFromAttackBoard(pawnMoves, indexOfPiece);

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
