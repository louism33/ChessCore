package chessprogram.god;

import static chessprogram.god.BitOperations.newPieceOnSquare;
import static chessprogram.god.MoveMakingUtilitiesIntMove.removePieces;
import static chessprogram.god.MoveParserIntMove.*;

class MovePromotionIntMove {

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
}
