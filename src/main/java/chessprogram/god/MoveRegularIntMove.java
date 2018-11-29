package chessprogram.god;

import static chessprogram.god.BitOperations.newPieceOnSquare;
import static chessprogram.god.MoveMakingUtilitiesIntMove.removePieces;
import static chessprogram.god.MoveParserIntMove.getDestinationIndex;
import static chessprogram.god.MoveParserIntMove.getSourceIndex;

class MoveRegularIntMove {

    static void makeRegularMove(Chessboard board, int move){
        long sourcePiece = newPieceOnSquare(getSourceIndex(move));
        long destinationPiece = newPieceOnSquare(getDestinationIndex(move));

        if ((sourcePiece & board.getWhitePawns()) != 0){
            removePieces(board, sourcePiece, destinationPiece);
            board.setWhitePawns(board.getWhitePawns() | destinationPiece);
        }
        else if ((sourcePiece & board.getWhiteKnights()) != 0){
            removePieces(board, sourcePiece, destinationPiece);
            board.setWhiteKnights(board.getWhiteKnights() | destinationPiece);
        }
        else if ((sourcePiece & board.getWhiteBishops()) != 0){
            removePieces(board, sourcePiece, destinationPiece);
            board.setWhiteBishops(board.getWhiteBishops() | destinationPiece);
        }
        else if  ((sourcePiece & board.getWhiteRooks()) != 0){
            removePieces(board, sourcePiece, destinationPiece);
            board.setWhiteRooks(board.getWhiteRooks() | destinationPiece);
        }
        else if  ((sourcePiece & board.getWhiteQueen()) != 0){
            removePieces(board, sourcePiece, destinationPiece);
            board.setWhiteQueen(board.getWhiteQueen() | destinationPiece);
        }
        else if  ((sourcePiece & board.getWhiteKing()) != 0){
            removePieces(board, sourcePiece, destinationPiece);
            board.setWhiteKing(board.getWhiteKing() | destinationPiece);
        }

        else if  ((sourcePiece & board.getBlackPawns()) != 0){
            removePieces(board, sourcePiece, destinationPiece);
            board.setBlackPawns(board.getBlackPawns() | destinationPiece);
        }
        else if  ((sourcePiece & board.getBlackKnights()) != 0){
            removePieces(board, sourcePiece, destinationPiece);
            board.setBlackKnights(board.getBlackKnights() | destinationPiece);
        }
        else if  ((sourcePiece & board.getBlackBishops()) != 0){
            removePieces(board, sourcePiece, destinationPiece);
            board.setBlackBishops(board.getBlackBishops() | destinationPiece);
        }
        else if  ((sourcePiece & board.getBlackRooks()) != 0){
            removePieces(board, sourcePiece, destinationPiece);
            board.setBlackRooks(board.getBlackRooks() | destinationPiece);
        }
        else if  ((sourcePiece & board.getBlackQueen()) != 0){
            removePieces(board, sourcePiece, destinationPiece);
            board.setBlackQueen(board.getBlackQueen() | destinationPiece);
        }
        else if  ((sourcePiece & board.getBlackKing()) != 0) {
            removePieces(board, sourcePiece, destinationPiece);
            board.setBlackKing(board.getBlackKing() | destinationPiece);
        }
        else {
            throw new RuntimeException("MoveRegular: false move "+ move);
        }
    }



}
