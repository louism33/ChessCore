package com.github.louism33.chesscore;

import static com.github.louism33.chesscore.BitOperations.populationCount;
import static com.github.louism33.chesscore.BoardConstants.*;
import static com.github.louism33.chesscore.PieceMove.*;

class CheckHelper {

    static boolean boardInCheck(boolean white, long myKing,
                                long pawns, long knights, long bishops, long rooks, long queens, long king,
                                long allPiece){
        int numberOfCheckers = numberOfPiecesThatLegalThreatenSquare(white, myKing,
                pawns, knights, bishops, rooks, queens, king,
                allPiece, 2);
        
        return numberOfCheckers > 0;
    }

    //todo add intermediate function to return bitboard of checkers, as we only need king moves if checked by pawn of knight
    
    static int numberOfPiecesThatLegalThreatenSquare(boolean myColour, long square,
                                                     long pawns, long knights, long bishops, long rooks, long queens, long king,
                                                     long allPieces, int stopAt){
        
        int numberOfThreats = 0;

        if (pawns != 0) {
            numberOfThreats += populationCount(singlePawnCaptures(square, myColour, pawns));
        }
        if (numberOfThreats >= stopAt){
            return numberOfThreats;
        }
        if (knights != 0) {
            numberOfThreats += populationCount(singleKnightTable(square, knights));
        }
        if (numberOfThreats >= stopAt){
            return numberOfThreats;
        }
        if (bishops != 0) {
            numberOfThreats += populationCount(singleBishopTable(allPieces, square, bishops));
        }
        if (numberOfThreats >= stopAt){
            return numberOfThreats;
        }
        if (rooks != 0) {
            numberOfThreats += populationCount(singleRookTable(allPieces, square, rooks));
        }
        if (numberOfThreats >= stopAt){
            return numberOfThreats;
        }
        if (queens != 0) {
            numberOfThreats += populationCount(singleQueenTable(allPieces, square, queens));
        }
        if (numberOfThreats >= stopAt){
            return numberOfThreats;
        }
        if (king != 0) {
            numberOfThreats += populationCount(singleKingTable(square, king));
        }

        return numberOfThreats;
    }

    static boolean isDrawByRepetition(Chessboard board){
        int limit = 25;
        long currentZob = board.zobristHash;
        for (int i = 0; i < limit; i++) {
            if (board.zobristHashStack[i] == currentZob){
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
                if (populationCount(board.pieces[BLACK][BISHOP])
                        + populationCount(board.pieces[WHITE][BISHOP])
                        + populationCount(board.pieces[BLACK][KNIGHT])
                        +populationCount(board.pieces[WHITE][KNIGHT]) != 0) {
                    
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
                if (populationCount(board.pieces[BLACK][BISHOP])
                        + populationCount(board.pieces[WHITE][BISHOP])
                        + populationCount(board.pieces[BLACK][KNIGHT])
                        +populationCount(board.pieces[WHITE][KNIGHT]) != 0) {

                    drawByMaterial = true;
                }
                break;
            case 4:
                break;
        }

        return drawByMaterial;
    }
}
