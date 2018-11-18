package chessprogram.moveMaking;


import chessprogram.chessboard.Chessboard;
import chessprogram.move.Move;

import static chessprogram.bitboards.BitManipulations.newPieceOnSquare;

class MoveEnPassant {
    
    static void makeEnPassantMove(Chessboard board, Move move){
        long sourcePiece = newPieceOnSquare(move.getSourceIndex());
        long destinationPiece = newPieceOnSquare(move.getDestinationIndex());
        
        if ((destinationPiece & board.allPieces()) != 0) {
            throw new RuntimeException("EP move Problem");
        }
        
        if ((sourcePiece & board.getWhitePawns()) != 0){
            MoveMakingUtilities.removePieces(board, sourcePiece, destinationPiece >>> 8);
            board.setWhitePawns(board.getWhitePawns() | destinationPiece);
        }
        
        else if  ((sourcePiece & board.getBlackPawns()) != 0){
            MoveMakingUtilities.removePieces(board, sourcePiece, destinationPiece << 8);
            board.setBlackPawns(board.getBlackPawns() | destinationPiece);
        }
        else {
            throw new RuntimeException("false EP move");
        }
    }


}
