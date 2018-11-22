package chessprogram.god;

import java.util.List;
import java.util.stream.Collectors;

import static chessprogram.god.BitOperations.*;
import static chessprogram.god.BitboardResources.*;

public enum Square {

    A8,A7,A6,A5,A4,A3,A2,A1,
    B8,B7,B6,B5,B4,B3,B2,B1,
    C8,C7,C6,C5,C4,C3,C2,C1,
    D8,D7,D6,D5,D4,D3,D2,D1,
    E8,E7,E6,E5,E4,E3,E2,E1,
    F8,F7,F6,F5,F4,F3,F2,F1,
    G8,G7,G6,G5,G4,G3,G2,G1,
    H8,H7,H6,H5,H4,H3,H2,H1;
    
    public static Square getPieceOnSquare(long piece){
        return values()[getIndexOfFirstPiece(piece)];
    }
    
    public static List<Square> getPiecesOnSquare(long pieces){
        return getAllPieces(pieces, 0)
                .stream()
                .map(Square::getPieceOnSquare)
                .collect(Collectors.toList());
    }
    
    public static long longFromSquare (Square square){
        final int ordinal = square.ordinal();
        return newPieceOnSquare(ordinal);
    }

    public long toBitboard (){
        final int ordinal = this.ordinal();
        return newPieceOnSquare(ordinal);
    }

    public int getRowNumber(){
        return this.ordinal() / 8;
    }

    /**
     * file A : 7, file H: 0
     * @return 0 - 7
     */
    public int getFileNumber(){
        return this.ordinal() % 8;
    }
    
    public long getRow(){ 
        return ROWS[this.ordinal() / 8];
    }
    
    public long getFile(){
        return FILES[this.ordinal() % 8];
    }
    
}
