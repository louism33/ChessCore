package chessprogram.god;


import static chessprogram.god.BitOperations.newPieceOnSquare;
import static chessprogram.god.MoveMakingUtilitiesIntMove.removePieces;

class MoveMakingEnPassantIntMove {
    
    static void makeEnPassantMove(ChessboardIntMove board, int move){
        long sourcePiece = newPieceOnSquare(MoveParserIntMove.getSourceIndex(move));
        long destinationPiece = newPieceOnSquare(MoveParserIntMove.getDestinationIndex(move));
        
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
