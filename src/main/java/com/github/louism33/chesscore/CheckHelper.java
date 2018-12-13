package com.github.louism33.chesscore;

import static com.github.louism33.chesscore.BitOperations.populationCount;
import static com.github.louism33.chesscore.PieceMove.*;

class CheckHelper {

    static boolean boardInCheck(Chessboard board, boolean white, long myKing,
                                long pawns, long knights, long bishops, long rooks, long queens, long king,
                                long enemies, long friends, long allPiece){
        int numberOfCheckers = numberOfPiecesThatLegalThreatenSquare(board, white, myKing,
                pawns, knights, bishops, rooks, queens, king,
                enemies, friends, allPiece);
        
        return numberOfCheckers > 0;
    }

    static int numberOfPiecesThatLegalThreatenSquare(Chessboard board, boolean myColour, long square,
                                                     long pawns, long knights, long bishops, long rooks, long queens, long king,
                                                     long enemies, long friends, long allPieces){
        
        int numberOfThreats = 0;

        if (pawns != 0) {
            numberOfThreats += populationCount(singlePawnCaptures(square, myColour, pawns));
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

    static boolean isDrawByRepetition(Chessboard board){
        int limit = 25;
        long currentZob = board.getBoardHash();
        for (int i = 0; i < limit; i++) {
            if (board.getZobristHashStack()[i] == currentZob){
                return true;
            }
        }
        return false;
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
