package chessprogram.god;

import java.util.Stack;

import static chessprogram.god.BitOperations.populationCount;
import static chessprogram.god.PieceMoveKnight.singleKnightTable;
import static chessprogram.god.PieceMovePawns.singlePawnCaptures;
import static chessprogram.god.PieceMoveSliding.*;

class CheckHelperIntMove {

    static boolean boardInCheck(ChessboardIntMove board, boolean white){
        long myKing = (white) ? board.getWhiteKing() : board.getBlackKing();
        int numberOfCheckers = numberOfPiecesThatLegalThreatenSquare(board, white, myKing);
        return numberOfCheckers > 0;
    }

    static int numberOfPiecesThatLegalThreatenSquare(ChessboardIntMove board, boolean myColour, long square){
        long pawns, knights, bishops, rooks, queens, king, allPieces = board.allPieces();
        if (!myColour){
            pawns = board.getWhitePawns();
            knights = board.getWhiteKnights();
            bishops = board.getWhiteBishops();
            rooks = board.getWhiteRooks();
            queens = board.getWhiteQueen();
            king = board.getWhiteKing();
        }
        else {
            pawns = board.getBlackPawns();
            knights = board.getBlackKnights();
            bishops = board.getBlackBishops();
            rooks = board.getBlackRooks();
            queens = board.getBlackQueen();
            king = board.getBlackKing();
        }

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
            numberOfThreats += populationCount(PieceMoveKing.singleKingTable(square, king));
        }

        return numberOfThreats;
    }

    // faster to create new stack and pop things to it ?
    static boolean isDrawByRepetition(ChessboardIntMove board, ZobristHashIntMove zobristHash){
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

    static boolean isDrawByInsufficientMaterial(ChessboardIntMove board){
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
    static boolean colourHasInsufficientMaterialToMate(ChessboardIntMove board, boolean white){
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
