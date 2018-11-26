package chessprogram.god;

import java.util.ArrayList;
import java.util.List;

import static chessprogram.god.BitOperations.getAllPieces;
import static chessprogram.god.Magic.*;

class MoveGeneratorSliding {

    static List<Move> masterSlidingPushes (Chessboard board, boolean white,
                                                  long ignoreThesePieces, long legalPushes){
        long bishops, rooks, queens;
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
            long slidingMoves = PieceMoveSliding.singleBishopTable(board, white, piece, legalPushes);
            int indexOfPiece = BitOperations.getIndexOfFirstPiece(piece);
            MoveGenerationUtilities.movesFromAttackBoard(moves, slidingMoves, indexOfPiece);
        }
        List<Long> allRooks = getAllPieces(rooks, ignoreThesePieces);
        for (Long piece : allRooks){
            long slidingMoves = PieceMoveSliding.singleRookTable(board, white, piece, legalPushes);
            PieceMoveSliding.singleRookTable(board, white, piece, legalPushes);
            int indexOfPiece = BitOperations.getIndexOfFirstPiece(piece);
            MoveGenerationUtilities.movesFromAttackBoard(moves, slidingMoves, indexOfPiece);
        }
        List<Long> allQueens = getAllPieces(queens, ignoreThesePieces);
        for (Long piece : allQueens){
            long slidingMoves = PieceMoveSliding.singleRookTable(board, white, piece, legalPushes)
                    |  PieceMoveSliding.singleBishopTable(board, white, piece, legalPushes);
            int indexOfPiece = BitOperations.getIndexOfFirstPiece(piece);
            MoveGenerationUtilities.movesFromAttackBoard(moves, slidingMoves, indexOfPiece);
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
            long slidingMoves = PieceMoveSliding.singleBishopTable(board, white, piece, legalCaptures);
            int indexOfPiece = BitOperations.getIndexOfFirstPiece(piece);
            MoveGenerationUtilities.movesFromAttackBoardCapture(moves, slidingMoves, indexOfPiece, true);
        }
        List<Long> allRooks = getAllPieces(rooks, ignoreThesePieces);
        for (Long piece : allRooks){
            long slidingMoves = PieceMoveSliding.singleRookTable(board, white, piece, legalCaptures);
            int indexOfPiece = BitOperations.getIndexOfFirstPiece(piece);
            MoveGenerationUtilities.movesFromAttackBoardCapture(moves, slidingMoves, indexOfPiece, true);
        }
        List<Long> allQueens = getAllPieces(queens, ignoreThesePieces);
        for (Long piece : allQueens){
            long slidingMoves = PieceMoveSliding.singleRookTable(board, white, piece, legalCaptures)
                    |  PieceMoveSliding.singleBishopTable(board, white, piece, legalCaptures);
            int indexOfPiece = BitOperations.getIndexOfFirstPiece(piece);
            MoveGenerationUtilities.movesFromAttackBoardCapture(moves, slidingMoves, indexOfPiece, true);
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
            int indexOfPiece = BitOperations.getIndexOfFirstPiece(piece);

            long slidingPushes = PieceMoveSliding.singleBishopTable(board, white, piece, legalPushes);
            MoveGenerationUtilities.movesFromAttackBoard(moves, slidingPushes, indexOfPiece);

//            long slidingCaptures = PieceMoveSliding.singleBishopTable(board, piece, white, legalCaptures);
            long slidingCaptures = PieceMoveSliding.singleBishopTable(board, white, piece, legalCaptures);
            MoveGenerationUtilities.movesFromAttackBoardCapture(moves, slidingCaptures, indexOfPiece, true);
        }

        List<Long> allRooks = getAllPieces(rooks, ignoreThesePieces);
        for (Long piece : allRooks){
            int indexOfPiece = BitOperations.getIndexOfFirstPiece(piece);

            long rookPushes = PieceMoveSliding.singleRookTable(board, white, piece, legalPushes);
            MoveGenerationUtilities.movesFromAttackBoard(moves, rookPushes, indexOfPiece);

            long rookCaptures = PieceMoveSliding.singleRookTable(board, white, piece, legalCaptures);
            MoveGenerationUtilities.movesFromAttackBoardCapture(moves, rookCaptures, indexOfPiece, true);
        }

        List<Long> allQueens = getAllPieces(queens, ignoreThesePieces);
        for (Long piece : allQueens){
            int indexOfPiece = BitOperations.getIndexOfFirstPiece(piece);

            long queenPushes = PieceMoveSliding.singleQueenTable(board, white, piece, legalPushes);
            MoveGenerationUtilities.movesFromAttackBoard(moves, queenPushes, indexOfPiece);

            long queenCaptures = PieceMoveSliding.singleQueenTable(board, white, piece, legalCaptures);
            MoveGenerationUtilities.movesFromAttackBoardCapture(moves, queenCaptures, indexOfPiece, true);
        }
        return moves;
    }

}
