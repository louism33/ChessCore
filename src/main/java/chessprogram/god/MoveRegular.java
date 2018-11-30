package chessprogram.god;

import static chessprogram.god.BitOperations.newPieceOnSquare;
import static chessprogram.god.MoveMakingUtilities.removePieces;
import static chessprogram.god.MoveParser.getDestinationIndex;
import static chessprogram.god.MoveParser.getSourceIndex;
import static chessprogram.god.Piece.NO_PIECE;
import static chessprogram.god.Piece.pieceOnSquare;

class MoveRegular {

    static void makeRegularMove(Chessboard board, int move) {
        final long sourcePiece = newPieceOnSquare(getSourceIndex(move));
        final long destinationPiece = newPieceOnSquare(getDestinationIndex(move));

        final Piece piece = pieceOnSquare(board, sourcePiece);
        
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
