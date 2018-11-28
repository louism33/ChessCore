package chessprogram.god;

import java.util.ArrayList;
import java.util.List;

import static chessprogram.god.BitOperations.getAllPieces;

class MoveGeneratorKnight {

    static List<Move> masterKnightCaptures(Chessboard board, boolean white,
                                                  long ignoreThesePieces, long legalCaptures){
        long ans = 0, knights;
        List<Move> moves = new ArrayList<>();
        if (white){
            knights = board.getWhiteKnights();
        }
        else {
            knights = board.getBlackKnights();
        }

        List<Long> allKnights = getAllPieces(knights, ignoreThesePieces);
        for (Long piece : allKnights){
            long jumpingMoves = PieceMoveKnight.singleKnightTable(piece, legalCaptures);
            int indexOfPiece = BitOperations.getIndexOfFirstPiece(piece);
            MoveGenerationUtilities.movesFromAttackBoardCapture(moves, jumpingMoves, indexOfPiece, true);
        }

        return moves;
    }

    static List<Move> masterKnightPushes(Chessboard board, boolean white,
                                                long ignoreThesePieces, long legalPushes){
        long ans = 0, knights;
        List<Move> moves = new ArrayList<>();
        if (white){
            knights = board.getWhiteKnights();
        }
        else {
            knights = board.getBlackKnights();
        }

        List<Long> allUnpinnedKnights = getAllPieces(knights, ignoreThesePieces);
        for (Long piece : allUnpinnedKnights){
            long jumpingMoves = PieceMoveKnight.singleKnightTable(piece, legalPushes);
            int indexOfPiece = BitOperations.getIndexOfFirstPiece(piece);
            MoveGenerationUtilities.addMovesFromAttackBoard(moves, jumpingMoves, indexOfPiece);
        }
        return moves;
    }

}
