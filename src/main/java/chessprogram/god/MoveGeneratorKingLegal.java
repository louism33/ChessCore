package chessprogram.god;

import org.junit.Assert;

import java.util.List;

import static chessprogram.god.BitOperations.UNIVERSE;
import static chessprogram.god.MoveGenerationUtilities.*;

class MoveGeneratorKingLegal {

    static void addKingLegalMovesOnly(List<Move> moves, Chessboard board, boolean white){
        long enemies = (white) ? board.blackPieces() : board.whitePieces();
        long myKing = (white) ? board.getWhiteKing() : board.getBlackKing();
        int indexOfKing = BitOperations.getIndexOfFirstPiece(myKing);
        
        addMovesFromAttackTableMaster(moves, kingLegalPushAndCaptureTable(board, white), indexOfKing, enemies);
    }

    private static long kingLegalPushAndCaptureTable(Chessboard board, boolean white){
        long ans = 0;
        long myKing = (white) ? board.getWhiteKing() : board.getBlackKing();
        long kingSafeSquares = ~kingDangerSquares(board, white);
        long enemyPieces = (!white) ? board.whitePieces() : board.blackPieces();
        long kingSafeCaptures = enemyPieces & kingSafeSquares;
        long kingSafePushes = (~board.allPieces() & kingSafeSquares);
        
        ans |= PieceMoveKing.singleKingTable(myKing, kingSafePushes | kingSafeCaptures);

        Assert.assertTrue(((kingSafeCaptures & kingSafePushes) == 0));

        return ans;
    }

    private static long kingDangerSquares(Chessboard board, boolean white){
        Chessboard boardWithoutMyKing = CopierToBeDeleted.copyBoard(board, white, true);
        return MoveGeneratorPseudo.generatePseudoCaptureTable(boardWithoutMyKing, !white, 0, UNIVERSE, UNIVERSE);
    }

}
