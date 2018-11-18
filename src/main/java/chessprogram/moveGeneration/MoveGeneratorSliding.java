package chessprogram.moveGeneration;

import chessprogram.chessboard.BitIndexing;
import chessprogram.chessboard.Chessboard;
import chessprogram.move.Move;

import java.util.ArrayList;
import java.util.List;

import static chessprogram.chessboard.BitExtractor.getAllPieces;

class MoveGeneratorSliding {

    static List<Move> masterSlidingPushes (Chessboard board, boolean white,
                                                  long ignoreThesePieces, long legalPushes){
        long ans = 0, bishops, rooks, queens;
        List<Move> moves = new ArrayList<>();
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

        List<Long> allBishops = getAllPieces(bishops, ignoreThesePieces);
        for (Long piece : allBishops){
            long slidingMoves = PieceMoveSliding.singleBishopPushes(board, piece, white, legalPushes);
            int indexOfPiece = BitIndexing.getIndexOfFirstPiece(piece);
            moves.addAll(MoveGenerationUtilities.movesFromAttackBoard(slidingMoves, indexOfPiece));
        }
        List<Long> allRooks = getAllPieces(rooks, ignoreThesePieces);
        for (Long piece : allRooks){
            long slidingMoves = PieceMoveSliding.singleRookPushes(board, piece, white, legalPushes);
            int indexOfPiece = BitIndexing.getIndexOfFirstPiece(piece);
            moves.addAll(MoveGenerationUtilities.movesFromAttackBoard(slidingMoves, indexOfPiece));
        }
        List<Long> allQueens = getAllPieces(queens, ignoreThesePieces);
        for (Long piece : allQueens){
            long slidingMoves = PieceMoveSliding.singleQueenPushes(board, piece, white, legalPushes);
            int indexOfPiece = BitIndexing.getIndexOfFirstPiece(piece);
            moves.addAll(MoveGenerationUtilities.movesFromAttackBoard(slidingMoves, indexOfPiece));
        }
        return moves;
    }

    static List<Move> masterSlidingCaptures (Chessboard board, boolean white,
                                                    long ignoreThesePieces, long legalCaptures){
        long ans = 0, bishops, rooks, queens;
        List<Move> moves = new ArrayList<>();
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

        List<Long> allBishops = getAllPieces(bishops, ignoreThesePieces);
        for (Long piece : allBishops){
            long slidingMoves = PieceMoveSliding.singleBishopCaptures(board, piece, white, legalCaptures);
            int indexOfPiece = BitIndexing.getIndexOfFirstPiece(piece);
            moves.addAll(MoveGenerationUtilities.movesFromAttackBoard(slidingMoves, indexOfPiece));
        }
        List<Long> allRooks = getAllPieces(rooks, ignoreThesePieces);
        for (Long piece : allRooks){
            long slidingMoves = PieceMoveSliding.singleRookCaptures(board, piece, white, legalCaptures);
            int indexOfPiece = BitIndexing.getIndexOfFirstPiece(piece);
            moves.addAll(MoveGenerationUtilities.movesFromAttackBoard(slidingMoves, indexOfPiece));
        }
        List<Long> allQueens = getAllPieces(queens, ignoreThesePieces);
        for (Long piece : allQueens){
            long slidingMoves = PieceMoveSliding.singleQueenCaptures(board, piece, white, legalCaptures);
            int indexOfPiece = BitIndexing.getIndexOfFirstPiece(piece);
            moves.addAll(MoveGenerationUtilities.movesFromAttackBoard(slidingMoves, indexOfPiece));
        }
        return moves;
    }

    public static List<Move> masterMoveSliding (Chessboard board, boolean white,
                                                long ignoreThesePieces, long legalPushes, long legalCaptures){
        long ans = 0, bishops, rooks, queens;
        List<Move> moves = new ArrayList<>();
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

        List<Long> allBishops = getAllPieces(bishops, ignoreThesePieces);
        for (Long piece : allBishops){
            int indexOfPiece = BitIndexing.getIndexOfFirstPiece(piece);

            long slidingPushes = PieceMoveSliding.singleBishopPushes(board, piece, white, legalPushes);
            moves.addAll(MoveGenerationUtilities.movesFromAttackBoard(slidingPushes, indexOfPiece));

            long slidingCaptures = PieceMoveSliding.singleBishopCaptures(board, piece, white, legalCaptures);
            moves.addAll(MoveGenerationUtilities.movesFromAttackBoard(slidingCaptures, indexOfPiece));
        }

        List<Long> allRooks = getAllPieces(rooks, ignoreThesePieces);
        for (Long piece : allRooks){
            int indexOfPiece = BitIndexing.getIndexOfFirstPiece(piece);

            long rookPushes = PieceMoveSliding.singleRookPushes(board, piece, white, legalPushes);
            moves.addAll(MoveGenerationUtilities.movesFromAttackBoard(rookPushes, indexOfPiece));

            long rookCaptures = PieceMoveSliding.singleRookCaptures(board, piece, white, legalCaptures);
            moves.addAll(MoveGenerationUtilities.movesFromAttackBoard(rookCaptures, indexOfPiece));
        }

        List<Long> allQueens = getAllPieces(queens, ignoreThesePieces);
        for (Long piece : allQueens){
            int indexOfPiece = BitIndexing.getIndexOfFirstPiece(piece);

            long queenPushes = PieceMoveSliding.singleQueenPushes(board, piece, white, legalPushes);
            moves.addAll(MoveGenerationUtilities.movesFromAttackBoard(queenPushes, indexOfPiece));

            long queenCaptures = PieceMoveSliding.singleQueenCaptures(board, piece, white, legalCaptures);
            moves.addAll(MoveGenerationUtilities.movesFromAttackBoard(queenCaptures, indexOfPiece));
        }
        return moves;
    }

}
