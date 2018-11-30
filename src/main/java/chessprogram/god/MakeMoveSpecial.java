package chessprogram.god;

import static chessprogram.god.BitOperations.newPieceOnSquare;
import static chessprogram.god.MoveMakingUtilities.removePieces;
import static chessprogram.god.MoveParser.*;

class MakeMoveSpecial {

    static void makeCastlingMove(Chessboard board, int move){
        long sourcePiece = newPieceOnSquare(MoveParser.getSourceIndex(move));
        if ((sourcePiece & BitboardResources.INITIAL_WHITE_KING) != 0){
            if (MoveParser.getDestinationIndex(move) == 1){
                long originalRook = newPieceOnSquare(0);
                long newRook = newPieceOnSquare(MoveParser.getDestinationIndex(move) + 1);
                long newKing = newPieceOnSquare(MoveParser.getDestinationIndex(move));
                removePieces(board, sourcePiece, originalRook);
                board.setWhiteKing(board.getWhiteKing() | newKing);
                board.setWhiteRooks(board.getWhiteRooks() | newRook);
                board.setWhiteCanCastleK(false);
                board.setWhiteCanCastleQ(false);
            }
            else if (MoveParser.getDestinationIndex(move) == 5){
                long originalRook = newPieceOnSquare(7);
                long newRook = newPieceOnSquare(MoveParser.getDestinationIndex(move) - 1);
                long newKing = newPieceOnSquare(MoveParser.getDestinationIndex(move));
                removePieces(board, sourcePiece, originalRook);
                board.setWhiteKing(board.getWhiteKing() | newKing);
                board.setWhiteRooks(board.getWhiteRooks() | newRook);
                board.setWhiteCanCastleK(false);
                board.setWhiteCanCastleQ(false);
            }
        }

        else if ((sourcePiece & BitboardResources.INITIAL_BLACK_KING) != 0){
            if (MoveParser.getDestinationIndex(move) == 57){
                long originalRook = newPieceOnSquare(56);
                long newRook = newPieceOnSquare(MoveParser.getDestinationIndex(move) + 1);
                long newKing = newPieceOnSquare(MoveParser.getDestinationIndex(move));
                removePieces(board, sourcePiece, originalRook);
                board.setBlackKing(board.getBlackKing() | newKing);
                board.setBlackRooks(board.getBlackRooks() | newRook);
                board.setBlackCanCastleK(false);
                board.setBlackCanCastleQ(false);
            }
            else if (MoveParser.getDestinationIndex(move) == 61){
                long originalRook = newPieceOnSquare(63);
                long newRook = newPieceOnSquare(MoveParser.getDestinationIndex(move) - 1);
                long newKing = newPieceOnSquare(MoveParser.getDestinationIndex(move));
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
        if (MoveParser.getSourceIndex(move) == 0 || MoveParser.getDestinationIndex(move) == 0){
            board.setWhiteCanCastleK(false);
        }
        if (MoveParser.getSourceIndex(move) == 3 || MoveParser.getDestinationIndex(move) == 3){
            board.setWhiteCanCastleK(false);
            board.setWhiteCanCastleQ(false);
        }
        if (MoveParser.getSourceIndex(move) == 7 || MoveParser.getDestinationIndex(move) == 7){
            board.setWhiteCanCastleQ(false);
        }

        if (MoveParser.getSourceIndex(move) == 56 || MoveParser.getDestinationIndex(move) == 56){
            board.setBlackCanCastleK(false);
        }
        if (MoveParser.getSourceIndex(move) == 59 || MoveParser.getDestinationIndex(move) == 59){
            board.setBlackCanCastleK(false);
            board.setBlackCanCastleQ(false);
        }
        if (MoveParser.getSourceIndex(move) == 63 || MoveParser.getDestinationIndex(move) == 63){
            board.setBlackCanCastleQ(false);
        }
    }


    static void makePromotingMove(Chessboard board, int move){
        long sourcePiece = newPieceOnSquare(getSourceIndex(move));
        long destinationPiece = newPieceOnSquare(getDestinationIndex(move));

        if ((sourcePiece & board.getWhitePawns()) != 0){
            removePieces(board, sourcePiece, destinationPiece);
            if (isPromotionToKnight(move)){
                board.setWhiteKnights(board.getWhiteKnights() | destinationPiece);
            }
            else if (isPromotionToBishop(move)){
                board.setWhiteBishops(board.getWhiteBishops() | destinationPiece);
            }
            else if (isPromotionToRook(move)){
                board.setWhiteRooks(board.getWhiteRooks() | destinationPiece);
            }
            else if (isPromotionToQueen(move)){
                board.setWhiteQueen(board.getWhiteQueen() | destinationPiece);
            }
        }

        else if ((sourcePiece & board.getBlackPawns()) != 0){
            removePieces(board, sourcePiece, destinationPiece);
            if (isPromotionToKnight(move)){
                board.setBlackKnights(board.getBlackKnights() | destinationPiece);
            }
            else if (isPromotionToBishop(move)){
                board.setBlackBishops(board.getBlackBishops() | destinationPiece);
            }
            else if (isPromotionToRook(move)){
                board.setBlackRooks(board.getBlackRooks() | destinationPiece);
            }
            else if (isPromotionToQueen(move)){
                board.setBlackQueen(board.getBlackQueen() | destinationPiece);
            }
        }
    }

    static void makeEnPassantMove(Chessboard board, int move){
        long sourcePiece = newPieceOnSquare(MoveParser.getSourceIndex(move));
        long destinationPiece = newPieceOnSquare(MoveParser.getDestinationIndex(move));

        if ((destinationPiece & board.allPieces()) != 0) {
            throw new RuntimeException("EP move Problem");
        }

        if ((sourcePiece & board.getWhitePawns()) != 0){
            removePieces(board, sourcePiece, destinationPiece >>> 8);
            board.setWhitePawns(board.getWhitePawns() | destinationPiece);
        }

        else if  ((sourcePiece & board.getBlackPawns()) != 0){
            removePieces(board, sourcePiece, destinationPiece << 8);
            board.setBlackPawns(board.getBlackPawns() | destinationPiece);
        }
        else {
            throw new RuntimeException("false EP move");
        }
    }


}
