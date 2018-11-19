package chessprogram.god;

import org.junit.Assert;

import java.util.List;

class cKingLegalMoves {

    public static List<Move> kingLegalMovesOnly(Chessboard board, boolean white){
        long myKing = (white) ? board.getWhiteKing() : board.getBlackKing();
        int indexOfKing = dBitIndexing.getIndexOfFirstPiece(myKing);
        return MoveGenerationUtilities.movesFromAttackBoard(kingLegalPushAndCaptureTable(board, white), indexOfKing);
    }

    private static long kingLegalPushAndCaptureTable(Chessboard board, boolean white){
        long ans = 0;
        long myKing = (white) ? board.getWhiteKing() : board.getBlackKing();
        long kingSafeSquares = ~cCheckUtilities.kingDangerSquares(board, white);
        long enemyPieces = (!white) ? board.whitePieces() : board.blackPieces();
        long kingSafeCaptures = enemyPieces & kingSafeSquares;
        long kingSafePushes = (~board.allPieces() & kingSafeSquares);
        
        ans |= PieceMoveKing.singleKingPushes(board, myKing, white, kingSafePushes);
        ans |= PieceMoveKing.singleKingCaptures(board, myKing, white, kingSafeCaptures);

        Assert.assertTrue(((kingSafeCaptures & kingSafePushes) == 0));

        return ans;
    }

}
