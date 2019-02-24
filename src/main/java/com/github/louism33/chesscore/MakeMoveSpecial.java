package com.github.louism33.chesscore;

import static com.github.louism33.chesscore.BitOperations.newPieceOnSquare;
import static com.github.louism33.chesscore.BoardConstants.*;
import static com.github.louism33.chesscore.MoveMakingUtilities.removePieces;
import static com.github.louism33.chesscore.MoveMakingUtilities.togglePiecesFrom;
import static com.github.louism33.chesscore.MoveParser.getDestinationIndex;
import static com.github.louism33.chesscore.MoveParser.getSourceIndex;

class MakeMoveSpecial {

    static int makeCastlingMove(int castlingRights, long[][] pieces, int[] pieceSquareTable, int move){
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

                pieceSquareTable[newRookIndex - 2] = WHITE_ROOK;
                pieceSquareTable[MoveParser.getDestinationIndex(move)] = WHITE_KING;

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

                pieceSquareTable[newRookIndex - 2] = BLACK_ROOK;
                pieceSquareTable[MoveParser.getDestinationIndex(move)] = BLACK_KING;
                        
                castlingRights &= castlingRightsMask[BLACK][K];
                castlingRights &= castlingRightsMask[BLACK][Q];

                KING = BLACK_KING;
                ROOK = BLACK_ROOK;
                break;
        }
        
        togglePiecesFrom(pieces, pieceSquareTable, sourcePiece, KING);
        togglePiecesFrom(pieces, pieceSquareTable, newPieceOnSquare(originalRookIndex), ROOK);
        
        return castlingRights;
    }



    static void makePromotingMove(long[][] pieces, int[] pieceSquareTable, int turn, int move){
        long sourcePiece = newPieceOnSquare(getSourceIndex(move));
        long destinationPiece = newPieceOnSquare(getDestinationIndex(move));

        removePieces(pieces, pieceSquareTable, sourcePiece, destinationPiece, move);

        pieces[turn][MoveParser.whichPromotion(move) + 2] |= destinationPiece;
        pieceSquareTable[getDestinationIndex(move)] = MoveParser.whichPromotion(move) + 2 + turn * 6;
    }

    static void makeEnPassantMove(long[][] pieces, int[] pieceSquareTable, int turn, int move){
        long sourcePiece = newPieceOnSquare(MoveParser.getSourceIndex(move));
        long destinationPiece = newPieceOnSquare(MoveParser.getDestinationIndex(move));

        switch (turn) {
            case WHITE:
                togglePiecesFrom(pieces, pieceSquareTable, sourcePiece, WHITE_PAWN);
                togglePiecesFrom(pieces, pieceSquareTable, destinationPiece >>> 8, BLACK_PAWN);
                pieces[WHITE][PAWN] |= destinationPiece;
                pieceSquareTable[MoveParser.getDestinationIndex(move)] = WHITE_PAWN;
                break;

            case BLACK:
                togglePiecesFrom(pieces, pieceSquareTable, sourcePiece, BLACK_PAWN);
                togglePiecesFrom(pieces, pieceSquareTable, destinationPiece << 8, WHITE_PAWN);
                pieces[BLACK][PAWN] |= destinationPiece;
                pieceSquareTable[MoveParser.getDestinationIndex(move)] = BLACK_PAWN;
        }
    }
}
