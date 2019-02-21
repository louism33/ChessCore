package com.github.louism33.chesscore;

import static com.github.louism33.chesscore.BitOperations.newPieceOnSquare;
import static com.github.louism33.chesscore.MoveConstants.*;
import static com.github.louism33.chesscore.MakeMoveSpecial.*;
import static com.github.louism33.chesscore.MoveMakingUtilities.removePieces;
import static com.github.louism33.chesscore.MoveParser.*;
import static com.github.louism33.chesscore.Piece.pieceOnSquareInt;
import static com.github.louism33.chesscore.StackDataUtil.*;

class MakeMoveRegular {

    static void makeMoveMaster(Chessboard board, int move) {
        if (move == 0){
            board.moveStackArrayPush(buildStackData(0, board, NULL_MOVE));
            return;
        }

        boolean resetFifty = true;

        if (MoveParser.isSpecialMove(move)) {
            switch (move & SPECIAL_MOVE_MASK) {
                case CASTLING_MASK:
                    board.moveStackArrayPush(buildStackData(move, board, CASTLING));
                    makeCastlingMove(board, move);
                    break;

                case ENPASSANT_MASK:
                    board.moveStackArrayPush(buildStackData(move, board, ENPASSANTCAPTURE));
                    makeEnPassantMove(board, move);
                    break;

                case PROMOTION_MASK:
                    board.moveStackArrayPush(buildStackData(move, board, PROMOTION));
                    makePromotingMove(board, move);
                    break;
            }
        }

        else {
            if (MoveParser.isCaptureMove(move)) {
                board.moveStackArrayPush(buildStackData(move, board, BASICCAPTURE));
            }

            else if (enPassantPossibility(board, move)){
                int whichFile = 8 - MoveParser.getSourceIndex(move) % 8;
                board.moveStackArrayPush(buildStackData(move, board, ENPASSANTVICTIM, whichFile));
            }

            else {
                switch (whichIntPieceOnSquare(board, newPieceOnSquare(MoveParser.getSourceIndex(move)))){
                    case 1: // white pawn
                    case 7: // black pawn
                        board.moveStackArrayPush(buildStackData(move, board, BASICLOUDPUSH));
                        break;
                    default:
                        // increment 50 move rule
                        resetFifty = false;
                        board.moveStackArrayPush(buildStackData(move, board, BASICQUIETPUSH));
                }   

            } 
            makeRegularMove(board, move);
        }

        // todo update unmake move to compensate
//        if (resetFifty) {
//            board.setFiftyMoveCounter(0);
//        }
//        else {
//            board.setFiftyMoveCounter(board.getFiftyMoveCounter() + 1);
//        }
        
        
        castleFlagManager(board, move);
    }
    
    private static boolean enPassantPossibility(Chessboard board, int move){
        // determine if flag should be added to enable EP on next turn
        long sourceSquare = newPieceOnSquare(MoveParser.getSourceIndex(move));
        long destinationSquare = newPieceOnSquare(MoveParser.getDestinationIndex(move));
        long HOME_RANK = (board.isWhiteTurn()) ? BoardConstants.RANK_TWO : BoardConstants.RANK_SEVEN;
        long MY_PAWNS = (board.isWhiteTurn()) ? board.getWhitePawns() : board.getBlackPawns();
        long enPassantPossibilityRank = (board.isWhiteTurn()) ? BoardConstants.RANK_FOUR : BoardConstants.RANK_FIVE;

        if ((sourceSquare & HOME_RANK) == 0){
            return false;
        }

        if ((sourceSquare & MY_PAWNS) == 0){
            return false;
        }
        return (destinationSquare & enPassantPossibilityRank) != 0;
    }

    static int whichIntPieceOnSquare(Chessboard board, long destinationPiece){
//        return Piece.pieceOnSquare(board, destinationPiece).ordinal();
        return Piece.pieceOnSquareInt(board, destinationPiece);
    }

    static void makeRegularMove(Chessboard board, int move) {
        final long sourcePiece = newPieceOnSquare(getSourceIndex(move));
        final long destinationPiece = newPieceOnSquare(getDestinationIndex(move));

        final int piece = pieceOnSquareInt(board, sourcePiece);

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
