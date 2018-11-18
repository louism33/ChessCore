package chessprogram.check;

import chessprogram.chessboard.BitIndexing;
import chessprogram.chessboard.Chessboard;
import chessprogram.move.Move;
import chessprogram.moveGeneration.MoveGenerationUtilities;
import chessprogram.moveGeneration.PieceMoveKing;
import org.junit.Assert;

import java.util.List;

public class KingLegalMoves {

    public static List<Move> kingLegalMovesOnly(Chessboard board, boolean white){
        long myKing = (white) ? board.getWhiteKing() : board.getBlackKing();
        int indexOfKing = BitIndexing.getIndexOfFirstPiece(myKing);
        return MoveGenerationUtilities.movesFromAttackBoard(kingLegalPushAndCaptureTable(board, white), indexOfKing);
    }

    private static long kingLegalPushAndCaptureTable(Chessboard board, boolean white){
        long ans = 0;
        long myKing = (white) ? board.getWhiteKing() : board.getBlackKing();
        long kingSafeSquares = ~CheckUtilities.kingDangerSquares(board, white);
        long enemyPieces = (!white) ? board.whitePieces() : board.blackPieces();
        long kingSafeCaptures = enemyPieces & kingSafeSquares;
        long kingSafePushes = (~board.allPieces() & kingSafeSquares);
        
        ans |= PieceMoveKing.singleKingPushes(board, myKing, white, kingSafePushes);
        ans |= PieceMoveKing.singleKingCaptures(board, myKing, white, kingSafeCaptures);

        Assert.assertTrue(((kingSafeCaptures & kingSafePushes) == 0));

        return ans;
    }

}
