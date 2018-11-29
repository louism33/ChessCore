package chessprogram.god;

import org.junit.Assert;

import java.util.List;

import static chessprogram.god.BitOperations.UNIVERSE;
import static chessprogram.god.CopierToBeDeletedIntMove.copyBoard;
import static chessprogram.god.MoveGeneratorPseudoIntMove.generatePseudoCaptureTable;

class MoveGeneratorKingLegalIntMove {

    static void addKingLegalMovesOnly(List<Integer> moves, ChessboardIntMove board, boolean white){
        long enemies = (white) ? board.blackPieces() : board.whitePieces();
        long myKing = (white) ? board.getWhiteKing() : board.getBlackKing();
        int indexOfKing = BitOperations.getIndexOfFirstPiece(myKing);
        
        MoveGenerationUtilitiesIntMove.addMovesFromAttackTableMaster(moves, kingLegalPushAndCaptureTable(board, white), indexOfKing, enemies);
    }

    private static long kingLegalPushAndCaptureTable(ChessboardIntMove board, boolean white){
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

    private static long kingDangerSquares(ChessboardIntMove board, boolean white){
        ChessboardIntMove boardWithoutMyKing = copyBoard(board, white, true);
        return generatePseudoCaptureTable(boardWithoutMyKing, !white, 0, UNIVERSE, UNIVERSE);
    }

}
