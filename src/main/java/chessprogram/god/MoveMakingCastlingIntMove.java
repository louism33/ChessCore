package chessprogram.god;

import static chessprogram.god.BitOperations.newPieceOnSquare;
import static chessprogram.god.MoveMakingUtilitiesIntMove.removePieces;

class MoveMakingCastlingIntMove {

    static void makeCastlingMove(Chessboard board, int move){
        long sourcePiece = newPieceOnSquare(MoveParserIntMove.getSourceIndex(move));
        if ((sourcePiece & BitboardResources.INITIAL_WHITE_KING) != 0){
            if (MoveParserIntMove.getDestinationIndex(move) == 1){
                long originalRook = newPieceOnSquare(0);
                long newRook = newPieceOnSquare(MoveParserIntMove.getDestinationIndex(move) + 1);
                long newKing = newPieceOnSquare(MoveParserIntMove.getDestinationIndex(move));
                removePieces(board, sourcePiece, originalRook);
                board.setWhiteKing(board.getWhiteKing() | newKing);
                board.setWhiteRooks(board.getWhiteRooks() | newRook);
                board.setWhiteCanCastleK(false);
                board.setWhiteCanCastleQ(false);
            }
            else if (MoveParserIntMove.getDestinationIndex(move) == 5){
                long originalRook = newPieceOnSquare(7);
                long newRook = newPieceOnSquare(MoveParserIntMove.getDestinationIndex(move) - 1);
                long newKing = newPieceOnSquare(MoveParserIntMove.getDestinationIndex(move));
                removePieces(board, sourcePiece, originalRook);
                board.setWhiteKing(board.getWhiteKing() | newKing);
                board.setWhiteRooks(board.getWhiteRooks() | newRook);
                board.setWhiteCanCastleK(false);
                board.setWhiteCanCastleQ(false);
            }
        }

        else if ((sourcePiece & BitboardResources.INITIAL_BLACK_KING) != 0){
            if (MoveParserIntMove.getDestinationIndex(move) == 57){
                long originalRook = newPieceOnSquare(56);
                long newRook = newPieceOnSquare(MoveParserIntMove.getDestinationIndex(move) + 1);
                long newKing = newPieceOnSquare(MoveParserIntMove.getDestinationIndex(move));
                removePieces(board, sourcePiece, originalRook);
                board.setBlackKing(board.getBlackKing() | newKing);
                board.setBlackRooks(board.getBlackRooks() | newRook);
                board.setBlackCanCastleK(false);
                board.setBlackCanCastleQ(false);
            }
            else if (MoveParserIntMove.getDestinationIndex(move) == 61){
                long originalRook = newPieceOnSquare(63);
                long newRook = newPieceOnSquare(MoveParserIntMove.getDestinationIndex(move) - 1);
                long newKing = newPieceOnSquare(MoveParserIntMove.getDestinationIndex(move));
                removePieces(board, sourcePiece, originalRook);
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

    static void castleFlagManager (Chessboard board, int move){

        // disable relevant castle flag whenever a piece moves into the relevant square.
        if (MoveParserIntMove.getSourceIndex(move) == 0 || MoveParserIntMove.getDestinationIndex(move) == 0){
            board.setWhiteCanCastleK(false);
        }
        if (MoveParserIntMove.getSourceIndex(move) == 3 || MoveParserIntMove.getDestinationIndex(move) == 3){
            board.setWhiteCanCastleK(false);
            board.setWhiteCanCastleQ(false);
        }
        if (MoveParserIntMove.getSourceIndex(move) == 7 || MoveParserIntMove.getDestinationIndex(move) == 7){
            board.setWhiteCanCastleQ(false);
        }

        if (MoveParserIntMove.getSourceIndex(move) == 56 || MoveParserIntMove.getDestinationIndex(move) == 56){
            board.setBlackCanCastleK(false);
        }
        if (MoveParserIntMove.getSourceIndex(move) == 59 || MoveParserIntMove.getDestinationIndex(move) == 59){
            board.setBlackCanCastleK(false);
            board.setBlackCanCastleQ(false);
        }
        if (MoveParserIntMove.getSourceIndex(move) == 63 || MoveParserIntMove.getDestinationIndex(move) == 63){
            board.setBlackCanCastleQ(false);
        }
    }
}
