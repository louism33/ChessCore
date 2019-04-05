package com.github.louism33.chesscore;

import static com.github.louism33.chesscore.BitOperations.populationCount;
import static com.github.louism33.chesscore.BoardConstants.*;
import static com.github.louism33.chesscore.PieceMove.*;
import static java.lang.Long.numberOfTrailingZeros;

final class CheckHelper {

    static boolean boardInCheck(int turn, long myKing,
                                long pawns, long knights, long bishops, long rooks, long queens, long king,
                                long allPiece){
        
        int numberOfCheckers = populationCount(bitboardOfPiecesThatLegalThreatenSquare(turn, myKing,
                pawns, knights, bishops, rooks, queens, king,
                allPiece, 1));

        return numberOfCheckers > 0;
    }

    static long bitboardOfPiecesThatLegalThreatenSquare(int turn, long square,
                                                        long pawns, long knights, long bishops, long rooks, long queens, long king,
                                                        long allPieces, int stopAt){

        long threats = 0;

        if (pawns != 0) {
            threats |= singlePawnCaptures(square, turn, pawns);
        }
        if (stopAt != 0){
            if (populationCount(threats) >= stopAt){
                return threats;
            }
        }
        if (knights != 0) {
            threats |= KNIGHT_MOVE_TABLE[numberOfTrailingZeros(square)] & knights;
        }
        if (stopAt != 0){
            if (populationCount(threats) >= stopAt){
                return threats;
            }
        }
        if (bishops != 0) {
            threats |= singleBishopTable(allPieces, square, bishops);
        }
        if (stopAt != 0){
            if (populationCount(threats) >= stopAt){
                return threats;
            }
        }
        if (rooks != 0) {
            threats |= singleRookTable(allPieces, square, rooks);
        }
        if (stopAt != 0){
            if (populationCount(threats) >= stopAt){
                return threats;
            }
        }
        if (queens != 0) {
            threats |= singleQueenTable(allPieces, square, queens);
        }
        if (stopAt != 0){
            if (populationCount(threats) >= stopAt){
                return threats;
            }
        }
        if (king != 0) {
            threats |= KING_MOVE_TABLE[numberOfTrailingZeros(square)] & king;
        }

        return threats;
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
