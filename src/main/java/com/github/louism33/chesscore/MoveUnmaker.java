package com.github.louism33.chesscore;

import org.junit.Assert;

import static com.github.louism33.chesscore.BitOperations.newPieceOnSquare;
import static com.github.louism33.chesscore.MakeMoveRegular.makeRegularMove;
import static com.github.louism33.chesscore.MoveMakingUtilities.removePieces;
import static com.github.louism33.chesscore.MoveParser.*;
import static com.github.louism33.chesscore.StackDataUtil.SpecialMove;
import static com.github.louism33.chesscore.StackDataUtil.SpecialMove.*;

class MoveUnmaker {

    static void unMakeMoveMaster(Chessboard board) throws IllegalUnmakeException {
        
        if (!board.hasPreviousMove()){
            throw new IllegalUnmakeException("No moves to unmake.");
        }

        Long realPop = board.getZobristStack().pop();
        board.setBoardHash(realPop);

        long zobbyPop = board.zobristStackArrayPop();
        board.setZOBBYHash(zobbyPop);

        Assert.assertEquals(realPop.longValue(), zobbyPop);

        board.moveStackArrayPop();
        
        long pop = board.moveStackData;
        
        if (StackDataUtil.getMove(pop) == 0){
            board.setWhiteTurn(StackDataUtil.getTurn(pop) == 1);
            return;
        }

        int pieceToMoveBack = getDestinationIndex(StackDataUtil.getMove(pop));
        int squareToMoveBackTo = getSourceIndex(StackDataUtil.getMove(pop));

        if (SpecialMove.values()[StackDataUtil.getSpecialMove(pop)] == SpecialMove.BASICQUIETPUSH){
            int basicReversedMove = moveFromSourceDestination(board, pieceToMoveBack, squareToMoveBackTo);
            makeRegularMove(board, basicReversedMove);
        }

        else if (SpecialMove.values()[StackDataUtil.getSpecialMove(pop)] == BASICLOUDPUSH){
            int basicReversedMove = moveFromSourceDestination(board, pieceToMoveBack, squareToMoveBackTo);
            makeRegularMove(board, basicReversedMove);
        }

        else if (SpecialMove.values()[StackDataUtil.getSpecialMove(pop)] == BASICCAPTURE){
            int basicReversedMove = moveFromSourceDestination(board, pieceToMoveBack, squareToMoveBackTo);
            makeRegularMove(board, basicReversedMove);
            int takenPiece = MoveParser.getVictimPiece(StackDataUtil.getMove(pop)).ordinal();
            if (takenPiece != 0){
                addRelevantPieceToSquare(board, takenPiece, pieceToMoveBack);
            }
        }

        //double pawn push
        else if (SpecialMove.values()[StackDataUtil.getSpecialMove(pop)] == ENPASSANTVICTIM){
            int basicReversedMove = moveFromSourceDestination(board, pieceToMoveBack, squareToMoveBackTo);
            makeRegularMove(board, basicReversedMove);
        }

        else if (SpecialMove.values()[StackDataUtil.getSpecialMove(pop)] == ENPASSANTCAPTURE){
            int basicReversedMove = moveFromSourceDestination(board, pieceToMoveBack, squareToMoveBackTo);
            makeRegularMove(board, basicReversedMove);
            int takenPiece = MoveParser.getVictimPiece(StackDataUtil.getMove(pop)).ordinal();

            if (StackDataUtil.getTurn(pop) == 1) {
                addRelevantPieceToSquare(board, 7, pieceToMoveBack - 8);
            }
            else {
                addRelevantPieceToSquare(board, 1, pieceToMoveBack + 8);
            }
        }

        else if (SpecialMove.values()[StackDataUtil.getSpecialMove(pop)] == CASTLING){
            int basicReversedMove = moveFromSourceDestination(board, pieceToMoveBack, squareToMoveBackTo);

            if (pieceToMoveBack == 1){
                long originalKing = newPieceOnSquare(squareToMoveBackTo);
                long originalRook = newPieceOnSquare(0);
                long newRook = newPieceOnSquare(pieceToMoveBack + 1);
                long newKing = newPieceOnSquare(pieceToMoveBack);

                removePieces(board, newKing, newRook);
                board.setWhiteKing(board.getWhiteKing() | originalKing);
                board.setWhiteRooks(board.getWhiteRooks() | originalRook);
            }

            else if (pieceToMoveBack == 5){
                long originalKing = newPieceOnSquare(squareToMoveBackTo);
                long originalRook = newPieceOnSquare(7);
                long newRook = newPieceOnSquare(pieceToMoveBack - 1);
                long newKing = newPieceOnSquare(pieceToMoveBack);

                removePieces(board, newKing, newRook);
                board.setWhiteKing(board.getWhiteKing() | originalKing);
                board.setWhiteRooks(board.getWhiteRooks() | originalRook);
            }

            else if (pieceToMoveBack == 57){
                long originalKing = newPieceOnSquare(squareToMoveBackTo);
                long originalRook = newPieceOnSquare(56);
                long newRook = newPieceOnSquare(pieceToMoveBack + 1);
                long newKing = newPieceOnSquare(pieceToMoveBack);

                removePieces(board, newKing, newRook);
                board.setBlackKing(board.getBlackKing() | originalKing);
                board.setBlackRooks(board.getBlackRooks() | originalRook);
            }

            else if (pieceToMoveBack == 61){
                long originalKing = newPieceOnSquare(squareToMoveBackTo);
                long originalRook = newPieceOnSquare(63);
                long newRook = newPieceOnSquare(pieceToMoveBack - 1);
                long newKing = newPieceOnSquare(pieceToMoveBack);

                removePieces(board, newKing, newRook);
                board.setBlackKing(board.getBlackKing() | originalKing);
                board.setBlackRooks(board.getBlackRooks() | originalRook);
            }

        }

        else if (SpecialMove.values()[StackDataUtil.getSpecialMove(pop)] == PROMOTION){
            long sourceSquare = newPieceOnSquare(pieceToMoveBack);
            long destinationSquare = newPieceOnSquare(squareToMoveBackTo);
            removePieces(board, sourceSquare, destinationSquare);
            if (StackDataUtil.getTurn(pop) == 1) {
                addRelevantPieceToSquare(board, 1, squareToMoveBackTo);
            }
            else {
                addRelevantPieceToSquare(board, 7, squareToMoveBackTo);
            }
            int takenPiece = MoveParser.getVictimPiece(StackDataUtil.getMove(pop)).ordinal();
            if (takenPiece > 0){
                addRelevantPieceToSquare(board, takenPiece, pieceToMoveBack);
            }
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
            castlingRights -= 1;
            board.setWhiteCanCastleK(true);
        }
      
        board.setWhiteTurn(StackDataUtil.getTurn(pop) == 1);
    }

    

    private static void addRelevantPieceToSquare(Chessboard board, int pieceToAdd, int placeToAddIt){
        long placeToAddPiece = newPieceOnSquare(placeToAddIt);

        if (pieceToAdd == 1){
            board.setWhitePawns(board.getWhitePawns() | placeToAddPiece);
        }
        else if (pieceToAdd == 2){
            board.setWhiteKnights(board.getWhiteKnights() | placeToAddPiece);
        }
        else if (pieceToAdd == 3){
            board.setWhiteBishops(board.getWhiteBishops() | placeToAddPiece);
        }
        else if (pieceToAdd == 4){
            board.setWhiteRooks(board.getWhiteRooks() | placeToAddPiece);
        }
        else if (pieceToAdd == 5){
            board.setWhiteQueen(board.getWhiteQueen() | placeToAddPiece);
        }
        else if (pieceToAdd == 6){
            board.setWhiteKing(board.getWhiteKing() | placeToAddPiece);
        }

        else if (pieceToAdd == 7){
            board.setBlackPawns(board.getBlackPawns() | placeToAddPiece);
        }
        else if (pieceToAdd == 8){
            board.setBlackKnights(board.getBlackKnights() | placeToAddPiece);
        }
        else if (pieceToAdd == 9){
            board.setBlackBishops(board.getBlackBishops() | placeToAddPiece);
        }
        else if (pieceToAdd == 10){
            board.setBlackRooks(board.getBlackRooks() | placeToAddPiece);
        }
        else if (pieceToAdd == 11){
            board.setBlackQueen(board.getBlackQueen() | placeToAddPiece);
        }
        else if (pieceToAdd == 12){
            board.setBlackKing(board.getBlackKing() | placeToAddPiece);
        }
        else {
            throw new RuntimeException("problem with putting back a captured piece");
        }
    }

}
