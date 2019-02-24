package com.github.louism33.chesscore;

import static com.github.louism33.chesscore.BitOperations.newPieceOnSquare;
import static com.github.louism33.chesscore.BoardConstants.*;
import static com.github.louism33.chesscore.MoveMakingUtilities.removePieces;
import static com.github.louism33.chesscore.MoveMakingUtilities.togglePiecesFrom;
import static com.github.louism33.chesscore.MoveParser.getDestinationIndex;
import static com.github.louism33.chesscore.MoveParser.getSourceIndex;

class MakeMoveSpecial {

    static int makeCastlingMove(int castlingRights, long[][] pieces, int move){
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

                pieces[WHITE][ROOK] |= newRook;
                pieces[WHITE][KING] |= newKing;

                castlingRights &= castlingRightsMask[WHITE][K];
                castlingRights &= castlingRightsMask[WHITE][Q];

                break;

            case 57:
                originalRookIndex = -7;
                newRookIndex += 2;
            case 61:
                originalRookIndex += 63;
                newRook = newPieceOnSquare(newRookIndex - 2);
                newKing = newPieceOnSquare(MoveParser.getDestinationIndex(move));

                pieces[BLACK][ROOK] |= newRook;
                pieces[BLACK][KING] |= newKing;

                castlingRights &= castlingRightsMask[BLACK][K];
                castlingRights &= castlingRightsMask[BLACK][Q];

                KING = BLACK_KING;
                ROOK = BLACK_ROOK;
                break;
        }
        togglePiecesFrom(pieces, sourcePiece, KING);
        togglePiecesFrom(pieces, newPieceOnSquare(originalRookIndex), ROOK);
        
        return castlingRights;
    }


//    static void castleFlagManager (Chessboard board, int move){
//        // disable relevant castle flag whenever a piece moves into the relevant square.
//        switch (MoveParser.getSourceIndex(move)) {
//            case 0:
//                board.castlingRights &= castlingRightsMask[WHITE][K];
//                break;
//            case 3:
//                board.castlingRights &= castlingRightsMask[WHITE][K];
//            case 7:
//                board.castlingRights &= castlingRightsMask[WHITE][Q];
//                break;
//            case 56:
//                board.castlingRights &= castlingRightsMask[BLACK][K];
//                break;
//            case 59:
//                board.castlingRights &= castlingRightsMask[BLACK][K];
//            case 63:
//                board.castlingRights &= castlingRightsMask[BLACK][Q];
//                break;
//        }
//        switch (MoveParser.getDestinationIndex(move)) {
//            case 0:
//                board.castlingRights &= castlingRightsMask[WHITE][K];
//                break;
//            case 3:
//                board.castlingRights &= castlingRightsMask[WHITE][K];
//            case 7:
//                board.castlingRights &= castlingRightsMask[WHITE][Q];
//                break;
//            case 56:
//                board.castlingRights &= castlingRightsMask[BLACK][K];
//                break;
//            case 59:
//                board.castlingRights &= castlingRightsMask[BLACK][K];
//            case 63:
//                board.castlingRights &= castlingRightsMask[BLACK][Q];
//                break;
//        }
//    }

    static void makePromotingMove(long[][] pieces, int turn, int move){
        long sourcePiece = newPieceOnSquare(getSourceIndex(move));
        long destinationPiece = newPieceOnSquare(getDestinationIndex(move));

        removePieces(pieces, sourcePiece, destinationPiece, move);

        pieces[turn][MoveParser.whichPromotion(move) + 2] |= destinationPiece;

    }

    static void makeEnPassantMove(long[][] pieces, int turn, int move){
        long sourcePiece = newPieceOnSquare(MoveParser.getSourceIndex(move));
        long destinationPiece = newPieceOnSquare(MoveParser.getDestinationIndex(move));

        switch (turn) {
            case WHITE:
                togglePiecesFrom(pieces, sourcePiece, WHITE_PAWN);
                togglePiecesFrom(pieces, destinationPiece >>> 8, BLACK_PAWN);
                pieces[WHITE][PAWN] |= destinationPiece;
                break;

            case BLACK:
                togglePiecesFrom(pieces, sourcePiece, BLACK_PAWN);
                togglePiecesFrom(pieces, destinationPiece << 8, WHITE_PAWN);
                pieces[BLACK][PAWN] |= destinationPiece;
        }
    }
}
