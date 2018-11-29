package chessprogram.god;

import java.util.List;

import static chessprogram.god.BitOperations.*;
import static chessprogram.god.BitOperations.UNIVERSE;
import static chessprogram.god.MoveGenerationUtilities.addMovesFromAttackTableMaster;
import static chessprogram.god.MoveGeneratorPseudoIntMove.generatePseudoCaptureTable;
import static chessprogram.god.PieceMoveKingIntMove.singleKingTable;

class MoveGeneratorKingLegal {

    static void addKingLegalMovesOnly(List<Integer> moves, Chessboard board, boolean white,
                                      long myPawns, long myKnights, long myBishops, long myRooks, long myQueens, long myKing,
                                      long enemyPawns, long enemyKnights, long enemyBishops, long enemyRooks, long enemyQueens, long enemyKing,
                                      long enemies, long friends, long allPieces){
        addMovesFromAttackTableMaster(moves, 
                kingLegalPushAndCaptureTable(board, white, myKing, enemies),
                getIndexOfFirstPiece(myKing),
                enemies);
    }

    private static long kingLegalPushAndCaptureTable(Chessboard board, boolean white, long myKing, long enemies){
        long kingSafeSquares = ~kingDangerSquares(board, white);
        long kingSafeCaptures = enemies & kingSafeSquares;
        long kingSafePushes = (~board.allPieces() & kingSafeSquares);
        return singleKingTable(myKing, kingSafePushes | kingSafeCaptures);
    }

    private static long kingDangerSquares(Chessboard board, boolean white){
        long myKing;
        
        if (white){
            myKing = board.getWhiteKing();
            board.setWhiteKing(0);
        }
        else {
            myKing = board.getBlackKing();
            board.setBlackKing(0);
        }
        
        long kingDangerSquares = generatePseudoCaptureTable(board, !white, 0, UNIVERSE, UNIVERSE);

        if (white){
            board.setWhiteKing(myKing);
        }
        else {
            board.setBlackKing(myKing);
        }
        
        return kingDangerSquares;
    }

}
