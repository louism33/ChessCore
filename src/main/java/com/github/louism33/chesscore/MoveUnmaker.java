package com.github.louism33.chesscore;

import org.junit.Assert;

import static com.github.louism33.chesscore.BitOperations.newPieceOnSquare;
import static com.github.louism33.chesscore.BoardConstants.*;
import static com.github.louism33.chesscore.MakeMoveRegular.makeRegularMove;
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
            board.setWhiteTurn(StackDataUtil.getTurn(pop) == 1);
            return;
        }

        int pieceToMoveBackIndex = getDestinationIndex(StackDataUtil.getMove(pop));
        int squareToMoveBackTo = getSourceIndex(StackDataUtil.getMove(pop));
        int basicReversedMove = moveFromSourceDestination(board, pieceToMoveBackIndex, squareToMoveBackTo);

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
                if (StackDataUtil.getTurn(pop) == 1) {
                    addRelevantPieceToSquare(board, 7, pieceToMoveBackIndex - 8);
                }
                else {
                    addRelevantPieceToSquare(board, 1, pieceToMoveBackIndex + 8);
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
                        board.setWhiteKing(board.getWhiteKing() | originalKing);
                        board.setWhiteRooks(board.getWhiteRooks() | originalRook);
                        break;

                    default:
                        originalRook = newPieceOnSquare(pieceToMoveBackIndex == 57 ? 56 : 63);
                        newRook = newPieceOnSquare(pieceToMoveBackIndex == 57 ? pieceToMoveBackIndex + 1 : pieceToMoveBackIndex - 1);
                        removePiecesFrom(board, newKing, BLACK_KING);
                        removePiecesFrom(board, newRook, BLACK_ROOK);
                        board.setBlackKing(board.getBlackKing() | originalKing);
                        board.setBlackRooks(board.getBlackRooks() | originalRook);
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
                
                
                board.setWhitePawns(board.getWhitePawns() & mask);
                board.setWhiteKnights(board.getWhiteKnights() & mask);
                board.setWhiteBishops(board.getWhiteBishops() & mask);
                board.setWhiteRooks(board.getWhiteRooks() & mask);
                board.setWhiteQueen(board.getWhiteQueen() & mask);
                board.setWhiteKing(board.getWhiteKing() & mask);
                board.setBlackPawns(board.getBlackPawns() & mask);
                board.setBlackKnights(board.getBlackKnights() & mask);
                board.setBlackBishops(board.getBlackBishops() & mask);
                board.setBlackRooks(board.getBlackRooks() & mask);
                board.setBlackQueen(board.getBlackQueen() & mask);
                board.setBlackKing(board.getBlackKing() & mask);

                addRelevantPieceToSquare(board,
                        StackDataUtil.getTurn(pop) == 1 ? 1 : 7, squareToMoveBackTo);
                int takenPiecePromotion = MoveParser.getVictimPieceInt(StackDataUtil.getMove(pop));
                if (takenPiecePromotion > 0){
                    addRelevantPieceToSquare(board, takenPiecePromotion, pieceToMoveBackIndex);
                }
                break;
        }

        int castlingRights = StackDataUtil.getCastlingRights(pop);

        if (castlingRights >= 8){
            castlingRights -= 8;
            board.setBlackCanCastleQ(true);
        }
        if (castlingRights >= 4){
            castlingRights -= 4;
            board.setBlackCanCastleK(true);
        }
        if (castlingRights >= 2){
            castlingRights -= 2;
            board.setWhiteCanCastleQ(true);
        }
        if (castlingRights >= 1){
            board.setWhiteCanCastleK(true);
        }

        board.setWhiteTurn(StackDataUtil.getTurn(pop) == 1);
    }


    private static void addRelevantPieceToSquare(Chessboard board, int pieceToAdd, int placeToAddIt){
        long placeToAddPiece = newPieceOnSquare(placeToAddIt);

        board.pieces[pieceToAdd / 7][pieceToAdd < 7 ? pieceToAdd : pieceToAdd - 6] |= placeToAddPiece;

        switch (pieceToAdd) {
            case 1:
                board.setWhitePawns(board.getWhitePawns() | placeToAddPiece);
                break;
            case 2:
                board.setWhiteKnights(board.getWhiteKnights() | placeToAddPiece);
                break;
            case 3:
                board.setWhiteBishops(board.getWhiteBishops() | placeToAddPiece);
                break;
            case 4:
                board.setWhiteRooks(board.getWhiteRooks() | placeToAddPiece);
                break;
            case 5:
                board.setWhiteQueen(board.getWhiteQueen() | placeToAddPiece);
                break;
            case 6:
                board.setWhiteKing(board.getWhiteKing() | placeToAddPiece);
                break;
            case 7:
                board.setBlackPawns(board.getBlackPawns() | placeToAddPiece);
                break;
            case 8:
                board.setBlackKnights(board.getBlackKnights() | placeToAddPiece);
                break;
            case 9:
                board.setBlackBishops(board.getBlackBishops() | placeToAddPiece);
                break;
            case 10:
                board.setBlackRooks(board.getBlackRooks() | placeToAddPiece);
                break;
            case 11:
                board.setBlackQueen(board.getBlackQueen() | placeToAddPiece);
                break;
            case 12:
                board.setBlackKing(board.getBlackKing() | placeToAddPiece);
                break;
            default:
                throw new RuntimeException("problem with putting back a captured piece");
        }
    }

}
