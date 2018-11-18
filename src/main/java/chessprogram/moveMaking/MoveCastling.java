package chessprogram.moveMaking;

import chessprogram.bitboards.BitBoardUtils;
import chessprogram.chessboard.Chessboard;
import chessprogram.move.Move;

import static chessprogram.bitboards.BitManipulations.newPieceOnSquare;

class MoveCastling {

    static void makeCastlingMove(Chessboard board, Move move){
        long sourcePiece = newPieceOnSquare(move.getSourceIndex());
        if ((sourcePiece & BitBoardUtils.WHITE_KING) != 0){
            if (move.getDestinationIndex() == 1){
                long originalRook = newPieceOnSquare(0);
                long newRook = newPieceOnSquare(move.getDestinationIndex() + 1);
                long newKing = newPieceOnSquare(move.getDestinationIndex());
                MoveMakingUtilities.removePieces(board, sourcePiece, originalRook);
                board.setWhiteKing(board.getWhiteKing() | newKing);
                board.setWhiteRooks(board.getWhiteRooks() | newRook);
                board.setWhiteCanCastleK(false);
                board.setWhiteCanCastleQ(false);
            }
            else if (move.getDestinationIndex() == 5){
                long originalRook = newPieceOnSquare(7);
                long newRook = newPieceOnSquare(move.getDestinationIndex() - 1);
                long newKing = newPieceOnSquare(move.getDestinationIndex());
                MoveMakingUtilities.removePieces(board, sourcePiece, originalRook);
                board.setWhiteKing(board.getWhiteKing() | newKing);
                board.setWhiteRooks(board.getWhiteRooks() | newRook);
                board.setWhiteCanCastleK(false);
                board.setWhiteCanCastleQ(false);
            }
        }

        else if ((sourcePiece & BitBoardUtils.BLACK_KING) != 0){
            if (move.getDestinationIndex() == 57){
                long originalRook = newPieceOnSquare(56);
                long newRook = newPieceOnSquare(move.getDestinationIndex() + 1);
                long newKing = newPieceOnSquare(move.getDestinationIndex());
                MoveMakingUtilities.removePieces(board, sourcePiece, originalRook);
                board.setBlackKing(board.getBlackKing() | newKing);
                board.setBlackRooks(board.getBlackRooks() | newRook);
                board.setBlackCanCastleK(false);
                board.setBlackCanCastleQ(false);
            }
            else if (move.getDestinationIndex() == 61){
                long originalRook = newPieceOnSquare(63);
                long newRook = newPieceOnSquare(move.getDestinationIndex() - 1);
                long newKing = newPieceOnSquare(move.getDestinationIndex());
                MoveMakingUtilities.removePieces(board, sourcePiece, originalRook);
                board.setBlackKing(board.getBlackKing() | newKing);
                board.setBlackRooks(board.getBlackRooks() | newRook);
                board.setBlackCanCastleK(false);
                board.setBlackCanCastleQ(false);
            }
        }
        else {
            throw new RuntimeException("Incorrect call to castling move");
        }
    }

    static void castleFlagManager (Chessboard board, Move move){

        // disable relevant castle flag whenever a piece moves into the relevant square.
        if (move.getSourceIndex() == 0 || move.getDestinationIndex() == 0){
            board.setWhiteCanCastleK(false);
        }
        if (move.getSourceIndex() == 3 || move.getDestinationIndex() == 3){
            board.setWhiteCanCastleK(false);
            board.setWhiteCanCastleQ(false);
        }
        if (move.getSourceIndex() == 7 || move.getDestinationIndex() == 7){
            board.setWhiteCanCastleQ(false);
        }

        if (move.getSourceIndex() == 56 || move.getDestinationIndex() == 56){
            board.setBlackCanCastleK(false);
        }
        if (move.getSourceIndex() == 59 || move.getDestinationIndex() == 59){
            board.setBlackCanCastleK(false);
            board.setBlackCanCastleQ(false);
        }
        if (move.getSourceIndex() == 63 || move.getDestinationIndex() == 63){
            board.setBlackCanCastleQ(false);
        }
    }
}
