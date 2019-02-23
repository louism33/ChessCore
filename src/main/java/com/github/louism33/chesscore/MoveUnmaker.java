package com.github.louism33.chesscore;

import org.junit.Assert;

import static com.github.louism33.chesscore.BitOperations.newPieceOnSquare;
import static com.github.louism33.chesscore.BoardConstants.*;
import static com.github.louism33.chesscore.MakeMoveRegular.makeRegularMove;
import static com.github.louism33.chesscore.MakeMoveRegular.whichIntPieceOnSquare;
import static com.github.louism33.chesscore.MoveMakingUtilities.removePiecesFrom;
import static com.github.louism33.chesscore.MoveParser.*;
import static com.github.louism33.chesscore.StackDataUtil.*;

class MoveUnmaker {

    static void unMakeMoveMaster(Chessboard board){

        if (!board.hasPreviousMove()){
            throw new RuntimeException("no move to unmake");
        }

        Assert.assertTrue(board.hasPreviousMove());

        board.masterStackPop();

        long pop = board.moveStackData;

        if (StackDataUtil.getMove(pop) == 0){
            board.turn = StackDataUtil.getTurn(pop);
            return;
        }

        int pieceToMoveBackIndex = getDestinationIndex(StackDataUtil.getMove(pop));
        int squareToMoveBackTo = getSourceIndex(StackDataUtil.getMove(pop));
        int basicReversedMove = buildBetterMove(pieceToMoveBackIndex, whichIntPieceOnSquare(board, newPieceOnSquare(pieceToMoveBackIndex)), squareToMoveBackTo, NO_PIECE);

        switch (StackDataUtil.getSpecialMove(pop)) {
            //double pawn push
            case ENPASSANTVICTIM:
            case BASICQUIETPUSH:
            case BASICLOUDPUSH:
                makeRegularMove(board, basicReversedMove);
                break;

            case BASICCAPTURE:
                makeRegularMove(board, basicReversedMove);
                int takenPiece = MoveParser.getVictimPieceInt(StackDataUtil.getMove(pop));
                if (MoveParser.getVictimPieceInt(StackDataUtil.getMove(pop)) != 0){
                    addRelevantPieceToSquare(board, takenPiece, pieceToMoveBackIndex);
                }
                break;

            case ENPASSANTCAPTURE:
                makeRegularMove(board, basicReversedMove);
                if (StackDataUtil.getTurn(pop) == BLACK) {
                    addRelevantPieceToSquare(board, BLACK_PAWN, pieceToMoveBackIndex - 8);
                }
                else {
                    addRelevantPieceToSquare(board, WHITE_PAWN, pieceToMoveBackIndex + 8);
                }
                break;

            case CASTLING:
                // king moved to:
                long originalRook, newRook,
                        originalKing = newPieceOnSquare(squareToMoveBackTo),
                        newKing = newPieceOnSquare(pieceToMoveBackIndex);

                switch (StackDataUtil.getTurn(pop)) {
                    case BLACK:
                        originalRook = newPieceOnSquare(pieceToMoveBackIndex == 1 ? 0 : 7);
                        newRook = newPieceOnSquare(pieceToMoveBackIndex == 1 ? pieceToMoveBackIndex + 1 : pieceToMoveBackIndex - 1);
                        removePiecesFrom(board, newKing, WHITE_KING);
                        removePiecesFrom(board, newRook, WHITE_ROOK);
                        break;

                    default:
                        originalRook = newPieceOnSquare(pieceToMoveBackIndex == 57 ? 56 : 63);
                        newRook = newPieceOnSquare(pieceToMoveBackIndex == 57 ? pieceToMoveBackIndex + 1 : pieceToMoveBackIndex - 1);
                        removePiecesFrom(board, newKing, BLACK_KING);
                        removePiecesFrom(board, newRook, BLACK_ROOK);
                        break;
                }

                board.pieces[1 - StackDataUtil.getTurn(pop)][KING] |= originalKing;
                board.pieces[1 - StackDataUtil.getTurn(pop)][ROOK] |= originalRook;
                break;

            case PROMOTION:
                long sourceSquare = newPieceOnSquare(pieceToMoveBackIndex);
                long destinationSquare = newPieceOnSquare(squareToMoveBackTo);
                long mask = ~(sourceSquare | destinationSquare);
                
                board.pieces[WHITE][PAWN] &= mask;
                board.pieces[WHITE][KNIGHT] &= mask;
                board.pieces[WHITE][BISHOP] &= mask;
                board.pieces[WHITE][ROOK] &= mask;
                board.pieces[WHITE][QUEEN] &= mask;
                board.pieces[WHITE][KING] &= mask;
                
                board.pieces[BLACK][PAWN] &= mask;
                board.pieces[BLACK][KNIGHT] &= mask;
                board.pieces[BLACK][BISHOP] &= mask;
                board.pieces[BLACK][ROOK] &= mask;
                board.pieces[BLACK][QUEEN] &= mask;
                board.pieces[BLACK][KING] &= mask;
                
                
                addRelevantPieceToSquare(board,
                        StackDataUtil.getTurn(pop) == 1 ? 1 : 7, squareToMoveBackTo);
                int takenPiecePromotion = MoveParser.getVictimPieceInt(StackDataUtil.getMove(pop));
                if (takenPiecePromotion > 0){
                    addRelevantPieceToSquare(board, takenPiecePromotion, pieceToMoveBackIndex);
                }
                break;
        }

        board.castlingRights = StackDataUtil.getCastlingRights(pop);
        
        
        board.setWhiteTurn(StackDataUtil.getTurn(pop) == 1);
    }


    private static void addRelevantPieceToSquare(Chessboard board, int pieceToAdd, int placeToAddIt){
        board.pieces[pieceToAdd / 7][pieceToAdd < 7 ? pieceToAdd : pieceToAdd - 6] |= newPieceOnSquare(placeToAddIt);
    }

}
