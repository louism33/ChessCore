package com.github.louism33.chesscore;

import static com.github.louism33.chesscore.BitOperations.newPieceOnSquare;
import static com.github.louism33.chesscore.MakeMoveSpecial.*;
import static com.github.louism33.chesscore.MoveMakingUtilities.removePieces;
import static com.github.louism33.chesscore.MoveParser.getDestinationIndex;
import static com.github.louism33.chesscore.MoveParser.getSourceIndex;
import static com.github.louism33.chesscore.Piece.NO_PIECE;
import static com.github.louism33.chesscore.Piece.pieceOnSquare;
import static com.github.louism33.chesscore.StackDataParser.SpecialMove;
import static com.github.louism33.chesscore.StackDataParser.SpecialMove.*;

class MakeMoveRegular {

    static void makeMoveMaster(Chessboard board, int move) {
        if(move == 0){
            StackDataParser stackDataParser = new StackDataParser(0, board, 50, SpecialMove.NULL_MOVE);
            board.moveStack.push(stackDataParser);
            return;
        }
        
        if (MoveParser.isSpecialMove(move)){
            if (MoveParser.isCastlingMove(move)) {
                StackDataParser stackDataParser = new StackDataParser(move, board, 50, CASTLING);
                board.moveStack.push(stackDataParser);
                makeCastlingMove(board, move);
                castleFlagManager(board, move);
            }

            else if (MoveParser.isEnPassantMove(move)){
                StackDataParser stackDataParser = new StackDataParser
                        (move, board, 50, ENPASSANTCAPTURE);
                board.moveStack.push(stackDataParser);
                makeEnPassantMove(board, move);
                castleFlagManager(board, move);
            }

            else if (MoveParser.isPromotionMove(move)){
                int destination = MoveParser.getDestinationIndex(move);
                long destSquare = newPieceOnSquare(destination);
                boolean capturePromotion = (destSquare & board.allPieces()) != 0;
                if (capturePromotion) {
                    long destinationPiece = newPieceOnSquare(MoveParser.getDestinationIndex(move));
                    int takenPiece = whichIntPieceOnSquare(board, destinationPiece);

                    StackDataParser stackDataParser = new StackDataParser(move, board, 50, PROMOTION, takenPiece);
                    board.moveStack.push(stackDataParser);
                    makePromotingMove(board, move);
                    castleFlagManager(board, move);
                }
                else {
                    StackDataParser stackDataParser = new StackDataParser(move, board, 50, PROMOTION);
                    board.moveStack.push(stackDataParser);
                    makePromotingMove(board, move);
                    castleFlagManager(board, move);
                }

            }
        }


        else {
            int destination = MoveParser.getDestinationIndex(move);
            long destSquare = newPieceOnSquare(destination);
            boolean captureMove = (destSquare & board.allPieces()) != 0;
            if (captureMove) {
                long destinationPiece = newPieceOnSquare(MoveParser.getDestinationIndex(move));
                int takenPiece = whichIntPieceOnSquare(board, destinationPiece);
                StackDataParser stackDataParser = new StackDataParser
                        (move, board, 50, BASICCAPTURE, takenPiece);
                board.moveStack.push(stackDataParser);
                makeRegularMove(board, move);
                castleFlagManager(board, move);
            }
            
            else if (enPassantPossibility(board, move)){
                int sourceAsPiece = MoveParser.getSourceIndex(move);
                int whichFile = 8 - sourceAsPiece % 8;
                StackDataParser stackDataParser = new StackDataParser
                        (move, board, 50, whichFile, ENPASSANTVICTIM);
                board.moveStack.push(stackDataParser);
                makeRegularMove(board, move);
                castleFlagManager(board, move);
            }

            else {
                long destinationPiece = newPieceOnSquare(MoveParser.getSourceIndex(move));
                int movingPiece = whichIntPieceOnSquare(board, destinationPiece);
                if (movingPiece == 1 || movingPiece == 7){
                    StackDataParser stackDataParser = new StackDataParser
                            (move, board, 50, BASICLOUDPUSH);
                    board.moveStack.push(stackDataParser);
                    makeRegularMove(board, move);
                    castleFlagManager(board, move);
                }
                else {
                    // increment 50 move rule
                    StackDataParser stackDataParser = new StackDataParser
                            (move, board, 50, BASICQUIETPUSH);
                    board.moveStack.push(stackDataParser);
                    makeRegularMove(board, move);
                    castleFlagManager(board, move);
                }
            }
        }
    }

    private static boolean enPassantPossibility(Chessboard board, int move){
        // determine if flag should be added to enable EP on next turn
        long sourceSquare = newPieceOnSquare(MoveParser.getSourceIndex(move));
        long destinationSquare = newPieceOnSquare(MoveParser.getDestinationIndex(move));
        long HOME_RANK = (board.isWhiteTurn()) ? BitboardResources.RANK_TWO : BitboardResources.RANK_SEVEN;
        long MY_PAWNS = (board.isWhiteTurn()) ? board.getWhitePawns() : board.getBlackPawns();
        long enPassantPossibilityRank = (board.isWhiteTurn()) ? BitboardResources.RANK_FOUR : BitboardResources.RANK_FIVE;

        if ((sourceSquare & HOME_RANK) == 0){
            return false;
        }

        if ((sourceSquare & MY_PAWNS) == 0){
            return false;
        }
        return (destinationSquare & enPassantPossibilityRank) != 0;
    }

    public static Piece whichPieceOnSquare(Chessboard board, long destinationPiece){
        return Piece.pieceOnSquare(board, destinationPiece);
    }

    static int whichIntPieceOnSquare(Chessboard board, long destinationPiece){
        return Piece.pieceOnSquare(board, destinationPiece).ordinal();
    }

    static void makeRegularMove(Chessboard board, int move) {
        final long sourcePiece = newPieceOnSquare(getSourceIndex(move));
        final long destinationPiece = newPieceOnSquare(getDestinationIndex(move));

        final Piece piece = pieceOnSquare(board, sourcePiece);

        if (piece == NO_PIECE){
            return;
        }

        removePieces(board, sourcePiece, destinationPiece);

        switch (piece) {
            case WHITE_PAWN:
                board.setWhitePawns(board.getWhitePawns() | destinationPiece);
                break;
            case WHITE_KNIGHT:
                board.setWhiteKnights(board.getWhiteKnights() | destinationPiece);
                break;
            case WHITE_BISHOP:
                board.setWhiteBishops(board.getWhiteBishops() | destinationPiece);
                break;
            case WHITE_ROOK:
                board.setWhiteRooks(board.getWhiteRooks() | destinationPiece);
                break;
            case WHITE_QUEEN:
                board.setWhiteQueen(board.getWhiteQueen() | destinationPiece);
                break;
            case WHITE_KING:
                board.setWhiteKing(board.getWhiteKing() | destinationPiece);
                break;

            case BLACK_PAWN:
                board.setBlackPawns(board.getBlackPawns() | destinationPiece);
                break;
            case BLACK_KNIGHT:
                board.setBlackKnights(board.getBlackKnights() | destinationPiece);
                break;
            case BLACK_BISHOP:
                board.setBlackBishops(board.getBlackBishops() | destinationPiece);
                break;
            case BLACK_ROOK:
                board.setBlackRooks(board.getBlackRooks() | destinationPiece);
                break;
            case BLACK_QUEEN:
                board.setBlackQueen(board.getBlackQueen() | destinationPiece);
                break;
            case BLACK_KING:
                board.setBlackKing(board.getBlackKing() | destinationPiece);
                break;
        }
    }

}
