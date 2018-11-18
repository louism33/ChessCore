package chessprogram.god;

import java.util.ArrayList;
import java.util.List;

import static chessprogram.god.dBitExtractor.getAllPieces;

class MoveGeneratorPawns {

    static List<Move> masterPawnPushes(Chessboard board, boolean white,
                                              long ignoreThesePieces, long legalPushes){
        return masterMovePawns(board, white, ignoreThesePieces, legalPushes, 0);
    }


    public static List<Move> masterPawnCaptures(Chessboard board, boolean white,
                                                long ignoreThesePieces, long legalCaptures){
        return masterMovePawns(board, white, ignoreThesePieces, 0, legalCaptures);
    }

    private static List<Move> masterMovePawns(Chessboard board, boolean white,
                                              long ignoreThesePieces, long legalPushes, long legalCaptures){
        long ans = 0, pawns;
        List<Move> moves = new ArrayList<>();
        if (white){
            pawns = board.getWhitePawns();
        }
        else {
            pawns = board.getBlackPawns();
        }

        List<Long> allPawns = getAllPieces(pawns, ignoreThesePieces);
        for (Long piece : allPawns){
            long pawnMoves = PieceMovePawns.singlePawnPushes(board, piece, white, legalPushes);
            int indexOfPiece = dBitIndexing.getIndexOfFirstPiece(piece);
            moves.addAll(MoveGenerationUtilities.movesFromAttackBoard(pawnMoves, indexOfPiece));
        }

        for (Long piece : allPawns){
            long pawnMoves = PieceMovePawns.singlePawnCaptures(board, piece, white, legalCaptures);
            int indexOfPiece = dBitIndexing.getIndexOfFirstPiece(piece);
            moves.addAll(MoveGenerationUtilities.movesFromAttackBoard(pawnMoves, indexOfPiece));
        }
        return moves;
    }

}
