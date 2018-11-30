package chessprogram.god;

import java.util.Stack;

import static chessprogram.god.BitOperations.populationCount;
import static chessprogram.god.PieceMoveKing.singleKingTable;
import static chessprogram.god.PieceMoveKnight.singleKnightTable;
import static chessprogram.god.PieceMovePawns.singlePawnCaptures;
import static chessprogram.god.PieceMoveSliding.*;

class CheckHelper {

    static boolean boardInCheck(Chessboard board, boolean white,
                                long myPawns, long myKnights, long myBishops, long myRooks, long myQueens, long myKing,
                                long enemyPawns, long enemyKnights, long enemyBishops, long enemyRooks, long enemyQueens, long enemyKing,
                                long enemies, long friends, long allPiece){
        int numberOfCheckers = numberOfPiecesThatLegalThreatenSquare(board, white, myKing,
                myPawns, myKnights, myBishops, myRooks, myQueens, myKing,
                enemyPawns, enemyKnights, enemyBishops, enemyRooks, enemyQueens, enemyKing,
                enemies, friends, allPiece);
        
        return numberOfCheckers > 0;
    }

    static int numberOfPiecesThatLegalThreatenSquare(Chessboard board, boolean myColour, long square,
                                                     long myPawns, long myKnights, long myBishops, long myRooks, long myQueens, long myKing,
                                                     long enemyPawns, long enemyKnights, long enemyBishops, long enemyRooks, long enemyQueens, long enemyKing,
                                                     long enemies, long friends, long allPiece){
        long knights, bishops, rooks, queens, king, allPieces = board.allPieces();
        if (!myColour){
            myPawns = board.getWhitePawns();
            knights = board.getWhiteKnights();
            bishops = board.getWhiteBishops();
            rooks = board.getWhiteRooks();
            queens = board.getWhiteQueen();
            king = board.getWhiteKing();
        }
        else {
            myPawns = board.getBlackPawns();
            knights = board.getBlackKnights();
            bishops = board.getBlackBishops();
            rooks = board.getBlackRooks();
            queens = board.getBlackQueen();
            king = board.getBlackKing();
        }

        int numberOfThreats = 0;

        if (myPawns != 0) {
            numberOfThreats += populationCount(singlePawnCaptures(square, myColour, myPawns));
        }
        if (numberOfThreats > 1){
            return numberOfThreats;
        }
        if (knights != 0) {
            numberOfThreats += populationCount(singleKnightTable(square, knights));
        }
        if (numberOfThreats > 1){
            return numberOfThreats;
        }
        if (bishops != 0) {
            numberOfThreats += populationCount(singleBishopTable(allPieces, board.isWhiteTurn(), square, bishops));
        }
        if (numberOfThreats > 1){
            return numberOfThreats;
        }
        if (rooks != 0) {
            numberOfThreats += populationCount(singleRookTable(allPieces, myColour, square, rooks));
        }
        if (numberOfThreats > 1){
            return numberOfThreats;
        }
        if (queens != 0) {
            numberOfThreats += populationCount(singleQueenTable(allPieces, myColour, square, queens));
        }
        if (numberOfThreats > 1){
            return numberOfThreats;
        }
        if (king != 0) {
            numberOfThreats += populationCount(singleKingTable(square, king));
        }

        return numberOfThreats;
    }

    // faster to create new stack and pop things to it ?
    static boolean isDrawByRepetition(Chessboard board, ZobristHashIntMove zobristHash){
        Stack<Long> zobristStack = (Stack<Long>) zobristHash.getZobristStack().clone();
        long zobristHashToMatch = zobristHash.getBoardHash();
        int howManyMovesToSearchToMax = 50;
        int limit = Math.min(howManyMovesToSearchToMax, zobristStack.size());

        int counter = 0;
        for (int previousBoardHashIndex = limit - 1; previousBoardHashIndex >= 0; previousBoardHashIndex--){
            Long pop = zobristStack.pop();
            if (pop == zobristHashToMatch){
                counter++;
            }
            if (counter > 0){
                return true;
            }
        }
        return counter > 0;
    }

    static boolean isDrawByInsufficientMaterial(Chessboard board){
        boolean drawByMaterial = false;
        int totalPieces = populationCount(board.allPieces());

        switch (totalPieces){
            case 2:
                drawByMaterial = true;
                break;
            case 3:
                if (populationCount(board.getBlackBishops())
                        + populationCount(board.getWhiteBishops())
                        + populationCount(board.getBlackKnights())
                        +populationCount(board.getWhiteKnights()) != 0) {
                    
                    drawByMaterial = true;
                }
                break;
            case 4:
                break;
        }

        return drawByMaterial;
    }

    // todo
    static boolean colourHasInsufficientMaterialToMate(Chessboard board, boolean white){
        boolean drawByMaterial = false;
        int totalPieces = populationCount(board.allPieces());

        switch (totalPieces){
            case 2:
                drawByMaterial = true;
                break;
            case 3:
                if (populationCount(board.getBlackBishops())
                        + populationCount(board.getWhiteBishops())
                        + populationCount(board.getBlackKnights())
                        +populationCount(board.getWhiteKnights()) != 0) {

                    drawByMaterial = true;
                }
                break;
            case 4:
                break;
        }

        return drawByMaterial;
    }
}
