package com.github.louism33.chesscore;

import org.junit.Assert;

import static com.github.louism33.chesscore.BitOperations.newPieceOnSquare;
import static com.github.louism33.chesscore.MoveConstants.*;
import static com.github.louism33.chesscore.MoveMakingUtilities.removePieces;
import static com.github.louism33.chesscore.MoveMakingUtilities.removePiecesFrom;
import static com.github.louism33.chesscore.MoveParser.*;

class MakeMoveSpecial {

    static void makeCastlingMove(Chessboard board, int move){
        long sourcePiece = newPieceOnSquare(MoveParser.getSourceIndex(move));
        int originalRookIndex = 0, newRookIndex = MoveParser.getDestinationIndex(move) + 1,
                KING = WHITE_KING, ROOK = WHITE_ROOK;
        long newRook, newKing;

        switch (MoveParser.getDestinationIndex(move)) {
            case 1:
                originalRookIndex = -7;
                newRookIndex += 2;
            case 5:
                originalRookIndex += 7;
                newRook = newPieceOnSquare(newRookIndex - 2);
                newKing = newPieceOnSquare(MoveParser.getDestinationIndex(move));

                board.setWhiteKing(board.getWhiteKing() | newKing);
                board.setWhiteRooks(board.getWhiteRooks() | newRook);
                board.setWhiteCanCastleK(false);
                board.setWhiteCanCastleQ(false);
                break;

            case 57:
                originalRookIndex = -7;
                newRookIndex += 2;
            case 61:
                originalRookIndex += 63;
                newRook = newPieceOnSquare(newRookIndex - 2);
                newKing = newPieceOnSquare(MoveParser.getDestinationIndex(move));

                board.setBlackKing(board.getBlackKing() | newKing);
                board.setBlackRooks(board.getBlackRooks() | newRook);
                board.setBlackCanCastleK(false);
                board.setBlackCanCastleQ(false);
                KING = BLACK_KING;
                ROOK = BLACK_ROOK;
                break;
        }
        removePiecesFrom(board, sourcePiece, KING);
        removePiecesFrom(board, newPieceOnSquare(originalRookIndex), ROOK);
    }


    static void castleFlagManager (Chessboard board, int move){
        // disable relevant castle flag whenever a piece moves into the relevant square.
        switch (MoveParser.getSourceIndex(move)) {
            case 0:
                board.setWhiteCanCastleK(false);
                break;
            case 3:
                board.setWhiteCanCastleK(false);
            case 7:
                board.setWhiteCanCastleQ(false);
                break;
            case 56:
                board.setBlackCanCastleK(false);
                break;
            case 59:
                board.setBlackCanCastleK(false);
            case 63:
                board.setBlackCanCastleQ(false);
                break;
        }
        switch (MoveParser.getDestinationIndex(move)) {
            case 0:
                board.setWhiteCanCastleK(false);
                break;
            case 3:
                board.setWhiteCanCastleK(false);
            case 7:
                board.setWhiteCanCastleQ(false);
                break;
            case 56:
                board.setBlackCanCastleK(false);
                break;
            case 59:
                board.setBlackCanCastleK(false);
            case 63:
                board.setBlackCanCastleQ(false);
                break;
        }
    }

    static void makePromotingMove(Chessboard board, int move){
        long sourcePiece = newPieceOnSquare(getSourceIndex(move));
        long destinationPiece = newPieceOnSquare(getDestinationIndex(move));

        removePieces(board, sourcePiece, destinationPiece, move);
        
        if (board.isWhiteTurn()){
            switch (move & WHICH_PROMOTION) {
                case KNIGHT_PROMOTION_MASK:
                    board.setWhiteKnights(board.getWhiteKnights() | destinationPiece);
                    break;
                case BISHOP_PROMOTION_MASK:
                    board.setWhiteBishops(board.getWhiteBishops() | destinationPiece);
                    break;
                case ROOK_PROMOTION_MASK:
                    board.setWhiteRooks(board.getWhiteRooks() | destinationPiece);
                    break;
                case QUEEN_PROMOTION_MASK:
                    board.setWhiteQueen(board.getWhiteQueen() | destinationPiece);
                    break;
            }
        }

        else{
            switch (move & WHICH_PROMOTION) {
                case KNIGHT_PROMOTION_MASK:
                    board.setBlackKnights(board.getBlackKnights() | destinationPiece);
                    break;
                case BISHOP_PROMOTION_MASK:
                    board.setBlackBishops(board.getBlackBishops() | destinationPiece);
                    break;
                case ROOK_PROMOTION_MASK:
                    board.setBlackRooks(board.getBlackRooks() | destinationPiece);
                    break;
                case QUEEN_PROMOTION_MASK:
                    board.setBlackQueen(board.getBlackQueen() | destinationPiece);
                    break;
            }
        }
    }

    static void makeEnPassantMove(Chessboard board, int move){
        long sourcePiece = newPieceOnSquare(MoveParser.getSourceIndex(move));
        long destinationPiece = newPieceOnSquare(MoveParser.getDestinationIndex(move));

        Assert.assertEquals(0, (destinationPiece & board.allPieces()));
        Assert.assertTrue( ((sourcePiece & board.getWhitePawns()) != 0) 
                || ((sourcePiece & board.getBlackPawns()) != 0) );
        
        if (board.isWhiteTurn()) {
            removePiecesFrom(board, sourcePiece, WHITE_PAWN);
            removePiecesFrom(board, destinationPiece >>> 8, BLACK_PAWN);
            board.setWhitePawns(board.getWhitePawns() | destinationPiece);
        }
        else {
            removePiecesFrom(board, sourcePiece, BLACK_PAWN);
            removePiecesFrom(board, destinationPiece << 8, WHITE_PAWN);
            board.setBlackPawns(board.getBlackPawns() | destinationPiece);
        }
    }
}
