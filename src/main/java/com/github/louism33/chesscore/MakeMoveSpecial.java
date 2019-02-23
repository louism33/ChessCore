package com.github.louism33.chesscore;

import org.junit.Assert;

import static com.github.louism33.chesscore.BitOperations.newPieceOnSquare;
import static com.github.louism33.chesscore.BoardConstants.*;
import static com.github.louism33.chesscore.MoveMakingUtilities.removePieces;
import static com.github.louism33.chesscore.MoveMakingUtilities.removePiecesFrom;
import static com.github.louism33.chesscore.MoveParser.getDestinationIndex;
import static com.github.louism33.chesscore.MoveParser.getSourceIndex;

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

                board.pieces[WHITE][ROOK] |= newRook;
                board.pieces[WHITE][KING] |= newKing;

                board.castlingRights &= castlingRightsMask[WHITE][K];
                board.castlingRights &= castlingRightsMask[WHITE][Q];
                
                break;

            case 57:
                originalRookIndex = -7;
                newRookIndex += 2;
            case 61:
                originalRookIndex += 63;
                newRook = newPieceOnSquare(newRookIndex - 2);
                newKing = newPieceOnSquare(MoveParser.getDestinationIndex(move));

                board.pieces[BLACK][ROOK] |= newRook;
                board.pieces[BLACK][KING] |= newKing;

                board.castlingRights &= castlingRightsMask[BLACK][K];
                board.castlingRights &= castlingRightsMask[BLACK][Q];
                
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
                board.castlingRights &= castlingRightsMask[WHITE][K];
                break;
            case 3:
                board.castlingRights &= castlingRightsMask[WHITE][K];
            case 7:
                board.castlingRights &= castlingRightsMask[WHITE][Q];
                break;
            case 56:
                board.castlingRights &= castlingRightsMask[BLACK][K];
                break;
            case 59:
                board.castlingRights &= castlingRightsMask[BLACK][K];
            case 63:
                board.castlingRights &= castlingRightsMask[BLACK][Q];
                break;
        }
        switch (MoveParser.getDestinationIndex(move)) {
            case 0:
                board.castlingRights &= castlingRightsMask[WHITE][K];
                break;
            case 3:
                board.castlingRights &= castlingRightsMask[WHITE][K];
            case 7:
                board.castlingRights &= castlingRightsMask[WHITE][Q];
                break;
            case 56:
                board.castlingRights &= castlingRightsMask[BLACK][K];
                break;
            case 59:
                board.castlingRights &= castlingRightsMask[BLACK][K];
            case 63:
                board.castlingRights &= castlingRightsMask[BLACK][Q];
                break;
        }
    }

    static void makePromotingMove(long[][] pieces, int turn, int move){
        long sourcePiece = newPieceOnSquare(getSourceIndex(move));
        long destinationPiece = newPieceOnSquare(getDestinationIndex(move));

        removePieces(pieces, sourcePiece, destinationPiece, move);

        pieces[turn][MoveParser.whichPromotion(move) + 2] |= destinationPiece;

    }

    static void makeEnPassantMove(Chessboard board, int move){
        long sourcePiece = newPieceOnSquare(MoveParser.getSourceIndex(move));
        long destinationPiece = newPieceOnSquare(MoveParser.getDestinationIndex(move));

        Assert.assertEquals(0, (destinationPiece & board.allPieces()));
        Assert.assertTrue( ((sourcePiece & board.pieces[WHITE][PAWN]) != 0)
                || ((sourcePiece & board.pieces[BLACK][PAWN]) != 0) );

        if (board.isWhiteTurn()) {
            removePiecesFrom(board, sourcePiece, WHITE_PAWN);
            removePiecesFrom(board, destinationPiece >>> 8, BLACK_PAWN);

            long p = board.pieces[WHITE][PAWN];
            board.pieces[WHITE][PAWN] |= destinationPiece;
        }
        else {
            removePiecesFrom(board, sourcePiece, BLACK_PAWN);
            removePiecesFrom(board, destinationPiece << 8, WHITE_PAWN);
            board.pieces[BLACK][PAWN] |= destinationPiece;
        }
    }
}
