package chessprogram.god;

import org.junit.Assert;

import static chessprogram.god.BitOperations.newPieceOnSquare;
import static chessprogram.god.MakeMoveRegular.makeRegularMove;
import static chessprogram.god.MoveMakingUtilities.removePieces;
import static chessprogram.god.MoveParser.*;
import static chessprogram.god.StackMoveData.SpecialMove;
import static chessprogram.god.StackMoveData.SpecialMove.*;

class MoveUnmaker {

    static void unMakeMoveMaster(Chessboard board) {
        StackMoveData popSMD = board.moveStack.pop();

        if (popSMD.move == 0){
            Assert.assertSame(popSMD.typeOfSpecialMove, NULL_MOVE);
            board.setWhiteTurn(popSMD.whiteTurn);
            return;
        }

        int pieceToMoveBack = getDestinationIndex(popSMD.move);
        int squareToMoveBackTo = getSourceIndex(popSMD.move);

        if (popSMD.typeOfSpecialMove == SpecialMove.BASICQUIETPUSH){
            int basicReversedMove = moveFromSourceDestination(pieceToMoveBack, squareToMoveBackTo);
            makeRegularMove(board, basicReversedMove);
        }

        else if (popSMD.typeOfSpecialMove == BASICLOUDPUSH){
            int basicReversedMove = moveFromSourceDestination(pieceToMoveBack, squareToMoveBackTo);
            makeRegularMove(board, basicReversedMove);
        }

        else if (popSMD.typeOfSpecialMove == BASICCAPTURE){
            int basicReversedMove = moveFromSourceDestination(pieceToMoveBack, squareToMoveBackTo);
            makeRegularMove(board, basicReversedMove);
            int takenPiece = popSMD.takenPiece;
            if (takenPiece != 0){
                addRelevantPieceToSquare(board, takenPiece, pieceToMoveBack);
            }
        }

        //double pawn push
        else if (popSMD.typeOfSpecialMove == ENPASSANTVICTIM){
            int basicReversedMove = moveFromSourceDestination(pieceToMoveBack, squareToMoveBackTo);
            makeRegularMove(board, basicReversedMove);
        }

        else if (popSMD.typeOfSpecialMove == ENPASSANTCAPTURE){
            int basicReversedMove = moveFromSourceDestination(pieceToMoveBack, squareToMoveBackTo);
            makeRegularMove(board, basicReversedMove);
            int takenPiece = popSMD.takenPiece;

            if (popSMD.whiteTurn) {
                addRelevantPieceToSquare(board, 7, pieceToMoveBack - 8);
            }
            else {
                addRelevantPieceToSquare(board, 1, pieceToMoveBack + 8);
            }
        }

        else if (popSMD.typeOfSpecialMove == CASTLING){
            int basicReversedMove = moveFromSourceDestination(pieceToMoveBack, squareToMoveBackTo);

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

        else if (popSMD.typeOfSpecialMove == PROMOTION){
            long sourceSquare = newPieceOnSquare(pieceToMoveBack);
            long destinationSquare = newPieceOnSquare(squareToMoveBackTo);
            removePieces(board, sourceSquare, destinationSquare);
            if (popSMD.whiteTurn) {
                addRelevantPieceToSquare(board, 1, squareToMoveBackTo);
            }
            else {
                addRelevantPieceToSquare(board, 7, squareToMoveBackTo);
            }
            int takenPiece = popSMD.takenPiece;
            if (takenPiece > 0){
                addRelevantPieceToSquare(board, takenPiece, pieceToMoveBack);
            }
        }

        board.setWhiteCanCastleK(popSMD.whiteCanCastleK);
        board.setWhiteCanCastleQ(popSMD.whiteCanCastleQ);
        board.setBlackCanCastleK(popSMD.blackCanCastleK);
        board.setBlackCanCastleQ(popSMD.blackCanCastleQ);

        board.setWhiteTurn(popSMD.whiteTurn);
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
