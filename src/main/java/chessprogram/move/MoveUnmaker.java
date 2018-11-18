package chessprogram.move;

import chessprogram.chessboard.Chessboard;
import chessprogram.moveMaking.MoveMakingUtilities;
import chessprogram.moveMaking.StackMoveData;

import static chessprogram.bitboards.BitManipulations.newPieceOnSquare;
import static chessprogram.moveMaking.StackMoveData.SpecialMove.*;

public class MoveUnmaker {

    public static void unMakeMoveMaster(Chessboard board) {
        StackMoveData popSMD = board.moveStack.pop();
        int pieceToMoveBack = popSMD.move.getDestinationIndex();
        int squareToMoveBackTo = popSMD.move.getSourceIndex();

        if (popSMD.typeOfSpecialMove == BASICQUIETPUSH){
            Move basicReversedMove = new Move(pieceToMoveBack, squareToMoveBackTo);
            makeRegularMove(board, basicReversedMove);
        }
        
        else if (popSMD.typeOfSpecialMove == BASICLOUDPUSH){
            Move basicReversedMove = new Move(pieceToMoveBack, squareToMoveBackTo);
            makeRegularMove(board, basicReversedMove);
        }

        else if (popSMD.typeOfSpecialMove == BASICCAPTURE){
            Move basicReversedMove = new Move(pieceToMoveBack, squareToMoveBackTo);
            makeRegularMove(board, basicReversedMove);
            int takenPiece = popSMD.takenPiece;
            if (takenPiece != 0){
                addRelevantPieceToSquare(board, takenPiece, pieceToMoveBack);
            }
        }

        //double pawn push
        else if (popSMD.typeOfSpecialMove == ENPASSANTVICTIM){
            Move basicReversedMove = new Move(pieceToMoveBack, squareToMoveBackTo);
            makeRegularMove(board, basicReversedMove);
        }

        else if (popSMD.typeOfSpecialMove == ENPASSANTCAPTURE){
            Move basicReversedMove = new Move(pieceToMoveBack, squareToMoveBackTo);
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
            Move basicReversedMove = new Move(pieceToMoveBack, squareToMoveBackTo);

            if (pieceToMoveBack == 1){
                long originalKing = newPieceOnSquare(squareToMoveBackTo);
                long originalRook = newPieceOnSquare(0);
                long newRook = newPieceOnSquare(pieceToMoveBack + 1);
                long newKing = newPieceOnSquare(pieceToMoveBack);

                MoveMakingUtilities.removePieces(board, newKing, newRook);
                board.setWhiteKing(board.getWhiteKing() | originalKing);
                board.setWhiteRooks(board.getWhiteRooks() | originalRook);
            }

            else if (pieceToMoveBack == 5){
                long originalKing = newPieceOnSquare(squareToMoveBackTo);
                long originalRook = newPieceOnSquare(7);
                long newRook = newPieceOnSquare(pieceToMoveBack - 1);
                long newKing = newPieceOnSquare(pieceToMoveBack);

                MoveMakingUtilities.removePieces(board, newKing, newRook);
                board.setWhiteKing(board.getWhiteKing() | originalKing);
                board.setWhiteRooks(board.getWhiteRooks() | originalRook);
            }

            else if (pieceToMoveBack == 57){
                long originalKing = newPieceOnSquare(squareToMoveBackTo);
                long originalRook = newPieceOnSquare(56);
                long newRook = newPieceOnSquare(pieceToMoveBack + 1);
                long newKing = newPieceOnSquare(pieceToMoveBack);

                MoveMakingUtilities.removePieces(board, newKing, newRook);
                board.setBlackKing(board.getBlackKing() | originalKing);
                board.setBlackRooks(board.getBlackRooks() | originalRook);
            }

            else if (pieceToMoveBack == 61){
                long originalKing = newPieceOnSquare(squareToMoveBackTo);
                long originalRook = newPieceOnSquare(63);
                long newRook = newPieceOnSquare(pieceToMoveBack - 1);
                long newKing = newPieceOnSquare(pieceToMoveBack);

                MoveMakingUtilities.removePieces(board, newKing, newRook);
                board.setBlackKing(board.getBlackKing() | originalKing);
                board.setBlackRooks(board.getBlackRooks() | originalRook);
            }
            
        }
        
        else if (popSMD.typeOfSpecialMove == PROMOTION){
            long sourceSquare = newPieceOnSquare(pieceToMoveBack);
            long destinationSquare = newPieceOnSquare(squareToMoveBackTo);
            MoveMakingUtilities.removePieces(board, sourceSquare, destinationSquare);
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


    private static void makeRegularMove(Chessboard board, Move move){
        long sourcePiece = newPieceOnSquare(move.getSourceIndex());
        long destinationPiece = newPieceOnSquare(move.getDestinationIndex());

        if ((sourcePiece & board.getWhitePawns()) != 0){
            MoveMakingUtilities.removePieces(board, sourcePiece, destinationPiece);
            board.setWhitePawns(board.getWhitePawns() | destinationPiece);
        }
        else if ((sourcePiece & board.getWhiteKnights()) != 0){
            MoveMakingUtilities.removePieces(board, sourcePiece, destinationPiece);
            board.setWhiteKnights(board.getWhiteKnights() | destinationPiece);
        }
        else if ((sourcePiece & board.getWhiteBishops()) != 0){
            MoveMakingUtilities.removePieces(board, sourcePiece, destinationPiece);
            board.setWhiteBishops(board.getWhiteBishops() | destinationPiece);
        }
        else if ((sourcePiece & board.getWhiteRooks()) != 0){
            MoveMakingUtilities.removePieces(board, sourcePiece, destinationPiece);
            board.setWhiteRooks(board.getWhiteRooks() | destinationPiece);
        }
        else if ((sourcePiece & board.getWhiteQueen()) != 0){
            MoveMakingUtilities.removePieces(board, sourcePiece, destinationPiece);
            board.setWhiteQueen(board.getWhiteQueen() | destinationPiece);
        }
        else if ((sourcePiece & board.getWhiteKing()) != 0){
            MoveMakingUtilities.removePieces(board, sourcePiece, destinationPiece);
            board.setWhiteKing(board.getWhiteKing() | destinationPiece);
        }

        else if ((sourcePiece & board.getBlackPawns()) != 0){
            MoveMakingUtilities.removePieces(board, sourcePiece, destinationPiece);
            board.setBlackPawns(board.getBlackPawns() | destinationPiece);
        }
        else if ((sourcePiece & board.getBlackKnights()) != 0){
            MoveMakingUtilities.removePieces(board, sourcePiece, destinationPiece);
            board.setBlackKnights(board.getBlackKnights() | destinationPiece);
        }
        else if ((sourcePiece & board.getBlackBishops()) != 0){
            MoveMakingUtilities.removePieces(board, sourcePiece, destinationPiece);
            board.setBlackBishops(board.getBlackBishops() | destinationPiece);
        }
        else if ((sourcePiece & board.getBlackRooks()) != 0){
            MoveMakingUtilities.removePieces(board, sourcePiece, destinationPiece);
            board.setBlackRooks(board.getBlackRooks() | destinationPiece);
        }
        else if ((sourcePiece & board.getBlackQueen()) != 0){
            MoveMakingUtilities.removePieces(board, sourcePiece, destinationPiece);
            board.setBlackQueen(board.getBlackQueen() | destinationPiece);
        }
        else if ((sourcePiece & board.getBlackKing()) != 0) {
            MoveMakingUtilities.removePieces(board, sourcePiece, destinationPiece);
            board.setBlackKing(board.getBlackKing() | destinationPiece);
        }
        else {
            throw new RuntimeException("unMakeMoveMaster: false move " + move);
        }
    }

}
