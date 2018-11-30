package chessprogram.god;


import static chessprogram.god.BitOperations.newPieceOnSquare;
import static chessprogram.god.MoveMakingUtilities.removePieces;

class MoveMakingEnPassant {
    
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
