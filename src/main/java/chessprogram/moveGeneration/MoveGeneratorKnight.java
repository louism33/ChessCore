package chessprogram.moveGeneration;

import chessprogram.chessboard.BitIndexing;
import chessprogram.chessboard.Chessboard;
import chessprogram.move.Move;

import java.util.ArrayList;
import java.util.List;

import static chessprogram.chessboard.BitExtractor.getAllPieces;

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
            long jumpingMoves = PieceMoveKnight.singleKnightCaptures(board, piece, white, legalCaptures);
            int indexOfPiece = BitIndexing.getIndexOfFirstPiece(piece);
            moves.addAll(MoveGenerationUtilities.movesFromAttackBoard(jumpingMoves, indexOfPiece));
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
            long jumpingMoves = PieceMoveKnight.singleKnightPushes(board, piece, white, legalPushes);
            int indexOfPiece = BitIndexing.getIndexOfFirstPiece(piece);
            moves.addAll(MoveGenerationUtilities.movesFromAttackBoard(jumpingMoves, indexOfPiece));
        }
        return moves;
    }

}
