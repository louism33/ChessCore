package com.github.louism33.chesscore;

import static com.github.louism33.chesscore.BitOperations.newPieceOnSquare;
import static com.github.louism33.chesscore.BoardConstants.*;
import static com.github.louism33.chesscore.MakeMoveSpecial.*;
import static com.github.louism33.chesscore.MoveConstants.*;
import static com.github.louism33.chesscore.MoveMakingUtilities.addPieceTo;
import static com.github.louism33.chesscore.MoveMakingUtilities.removePieces;
import static com.github.louism33.chesscore.MoveParser.getDestinationIndex;
import static com.github.louism33.chesscore.MoveParser.getSourceIndex;
import static com.github.louism33.chesscore.StackDataUtil.*;

class MakeMoveRegular {

    static void makeMoveMaster(Chessboard board, int move) {
        if (move == 0){
            board.moveStackArrayPush(buildStackDataBetter(0, board.turn, board.getFiftyMoveCounter(), board.castlingRights, NULL_MOVE));
            return;
        }

        boolean resetFifty = true;

        if (MoveParser.isSpecialMove(move)) {
            switch (move & SPECIAL_MOVE_MASK) {
                case CASTLING_MASK:
                    board.moveStackArrayPush(buildStackDataBetter(move, board.turn, board.getFiftyMoveCounter(), board.castlingRights, CASTLING));
                    makeCastlingMove(board, move);
                    break;

                case ENPASSANT_MASK:
                    board.moveStackArrayPush(buildStackDataBetter(move, board.turn, board.getFiftyMoveCounter(), board.castlingRights, ENPASSANTCAPTURE));
                    makeEnPassantMove(board, move);
                    break;

                case PROMOTION_MASK:
                    board.moveStackArrayPush(buildStackDataBetter(move, board.turn, board.getFiftyMoveCounter(), board.castlingRights, PROMOTION));
                    makePromotingMove(board.pieces, board.turn, move);
                    break;
            }
        }

        else {
            if (MoveParser.isCaptureMove(move)) {
                board.moveStackArrayPush(buildStackDataBetter(move, board.turn, board.getFiftyMoveCounter(), board.castlingRights, BASICCAPTURE));
            }

            else if (enPassantPossibility(board.turn, board.pieces[board.turn][PAWN], move)){
                int whichFile = 8 - MoveParser.getSourceIndex(move) % 8;
                board.moveStackArrayPush(buildStackDataBetter(move, board.turn, board.getFiftyMoveCounter(), board.castlingRights, ENPASSANTVICTIM, whichFile));
            }

            else {
                switch (Piece.pieceOnSquareInt(board, newPieceOnSquare(MoveParser.getSourceIndex(move)))){
                    case WHITE_PAWN: // white pawn
                    case BLACK_PAWN: // black pawn
                        board.moveStackArrayPush(buildStackDataBetter(move, board.turn, board.getFiftyMoveCounter(), board.castlingRights, BASICLOUDPUSH));
                        break;
                    default:
                        // increment 50 move rule
                        resetFifty = false;
                        board.moveStackArrayPush(buildStackDataBetter(move, board.turn, board.getFiftyMoveCounter(), board.castlingRights, BASICQUIETPUSH));
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
    
    private static boolean enPassantPossibility(int turn, long myPawns, int move){
        // determine if flag should be added to enable EP on next turn
        long sourceSquare = newPieceOnSquare(MoveParser.getSourceIndex(move));
        long destinationSquare = newPieceOnSquare(MoveParser.getDestinationIndex(move));
        long HOME_RANK = PENULTIMATE_RANKS[1 - turn];
        long enPassantPossibilityRank = ENPASSANT_RANK[turn];

        if ((sourceSquare & HOME_RANK) == 0){
            return false;
        }

        if ((sourceSquare & myPawns) == 0){
            return false;
        }
        return (destinationSquare & enPassantPossibilityRank) != 0;
    }

    static int whichIntPieceOnSquare(Chessboard board, long destinationPiece){
        return Piece.pieceOnSquareInt(board, destinationPiece);
    }

    static void makeRegularMove(Chessboard board, int move) {
        final long sourcePiece = newPieceOnSquare(getSourceIndex(move));
        final long destinationPiece = newPieceOnSquare(getDestinationIndex(move));

        removePieces(board.pieces, sourcePiece, destinationPiece, move);

        addPieceTo(board, destinationPiece, MoveParser.getMovingPieceInt(move));
    }

}
