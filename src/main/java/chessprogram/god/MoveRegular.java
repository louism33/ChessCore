package chessprogram.god;

import static chessprogram.god.BitOperations.newPieceOnSquare;

class MoveRegular {

    static void makeRegularMove(Chessboard board, Move move){
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
        else if  ((sourcePiece & board.getWhiteRooks()) != 0){
            MoveMakingUtilities.removePieces(board, sourcePiece, destinationPiece);
            board.setWhiteRooks(board.getWhiteRooks() | destinationPiece);
        }
        else if  ((sourcePiece & board.getWhiteQueen()) != 0){
            MoveMakingUtilities.removePieces(board, sourcePiece, destinationPiece);
            board.setWhiteQueen(board.getWhiteQueen() | destinationPiece);
        }
        else if  ((sourcePiece & board.getWhiteKing()) != 0){
            MoveMakingUtilities.removePieces(board, sourcePiece, destinationPiece);
            board.setWhiteKing(board.getWhiteKing() | destinationPiece);
        }

        else if  ((sourcePiece & board.getBlackPawns()) != 0){
            MoveMakingUtilities.removePieces(board, sourcePiece, destinationPiece);
            board.setBlackPawns(board.getBlackPawns() | destinationPiece);
        }
        else if  ((sourcePiece & board.getBlackKnights()) != 0){
            MoveMakingUtilities.removePieces(board, sourcePiece, destinationPiece);
            board.setBlackKnights(board.getBlackKnights() | destinationPiece);
        }
        else if  ((sourcePiece & board.getBlackBishops()) != 0){
            MoveMakingUtilities.removePieces(board, sourcePiece, destinationPiece);
            board.setBlackBishops(board.getBlackBishops() | destinationPiece);
        }
        else if  ((sourcePiece & board.getBlackRooks()) != 0){
            MoveMakingUtilities.removePieces(board, sourcePiece, destinationPiece);
            board.setBlackRooks(board.getBlackRooks() | destinationPiece);
        }
        else if  ((sourcePiece & board.getBlackQueen()) != 0){
            MoveMakingUtilities.removePieces(board, sourcePiece, destinationPiece);
            board.setBlackQueen(board.getBlackQueen() | destinationPiece);
        }
        else if  ((sourcePiece & board.getBlackKing()) != 0) {
            MoveMakingUtilities.removePieces(board, sourcePiece, destinationPiece);
            board.setBlackKing(board.getBlackKing() | destinationPiece);
        }
        else {
            throw new RuntimeException("MoveRegular: false move "+ move);
        }
    }



}
