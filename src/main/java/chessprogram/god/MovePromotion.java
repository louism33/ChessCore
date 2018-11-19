package chessprogram.god;

import static chessprogram.god.bBitManipulations.newPieceOnSquare;

class MovePromotion {

    static void makePromotingMove(Chessboard board, Move move){
        long sourcePiece = newPieceOnSquare(move.getSourceIndex());
        long destinationPiece = newPieceOnSquare(move.getDestinationIndex());

        if ((sourcePiece & board.getWhitePawns()) != 0){
            MoveMakingUtilities.removePieces(board, sourcePiece, destinationPiece);
            if (move.isPromotionToKnight()){
                board.setWhiteKnights(board.getWhiteKnights() | destinationPiece);
            }
            else if (move.isPromotionToBishop()){
                board.setWhiteBishops(board.getWhiteBishops() | destinationPiece);
            }
            else if (move.isPromotionToRook()){
                board.setWhiteRooks(board.getWhiteRooks() | destinationPiece);
            }
            else if (move.isPromotionToQueen()){
                board.setWhiteQueen(board.getWhiteQueen() | destinationPiece);
            }
        }

        else if ((sourcePiece & board.getBlackPawns()) != 0){
            MoveMakingUtilities.removePieces(board, sourcePiece, destinationPiece);
            if (move.isPromotionToKnight()){
                board.setBlackKnights(board.getBlackKnights() | destinationPiece);
            }
            else if (move.isPromotionToBishop()){
                board.setBlackBishops(board.getBlackBishops() | destinationPiece);
            }
            else if (move.isPromotionToRook()){
                board.setBlackRooks(board.getBlackRooks() | destinationPiece);
            }
            else if (move.isPromotionToQueen()){
                board.setBlackQueen(board.getBlackQueen() | destinationPiece);
            }
        }

    }
}
