package chessprogram.god;

import java.util.Objects;

class ChessboardDetails {

    boolean whiteTurn = true;
    boolean whiteCanCastleK = true;
    boolean whiteCanCastleQ = true;
    boolean blackCanCastleK = true;
    boolean blackCanCastleQ = true;

    long whitePawns;
    long whiteKnights;
    long whiteBishops;
    long whiteRooks;
    long whiteQueen;
    long whiteKing;

    long blackPawns;
    long blackKnights;
    long blackBishops;
    long blackRooks;
    long blackQueen;
    long blackKing;
    
    ChessboardDetails(){
        
    }
    
    ChessboardDetails(boolean normalBoard){
        init();
    }
    
    void init(){
        whitePawns = 0x000000000000FF00L;
        whiteKnights = 0x0000000000000042L;
        whiteBishops = 0x0000000000000024L;
        whiteRooks = 0x0000000000000081L;
        whiteQueen = 0x0000000000000010L;
        whiteKing = 0x0000000000000008L;

        blackPawns = 0x00FF000000000000L;
        blackKnights = 0x4200000000000000L;
        blackBishops = 0x2400000000000000L;
        blackRooks = 0x8100000000000000L;
        blackQueen = 0x1000000000000000L;
        blackKing = 0x0800000000000000L;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessboardDetails that = (ChessboardDetails) o;
        return whiteTurn == that.whiteTurn &&
                whiteCanCastleK == that.whiteCanCastleK &&
                whiteCanCastleQ == that.whiteCanCastleQ &&
                blackCanCastleK == that.blackCanCastleK &&
                blackCanCastleQ == that.blackCanCastleQ &&
                whitePawns == that.whitePawns &&
                whiteKnights == that.whiteKnights &&
                whiteBishops == that.whiteBishops &&
                whiteRooks == that.whiteRooks &&
                whiteQueen == that.whiteQueen &&
                whiteKing == that.whiteKing &&
                blackPawns == that.blackPawns &&
                blackKnights == that.blackKnights &&
                blackBishops == that.blackBishops &&
                blackRooks == that.blackRooks &&
                blackQueen == that.blackQueen &&
                blackKing == that.blackKing;
    }

    @Override
    public int hashCode() {
        return Objects.hash(whiteTurn, whiteCanCastleK, whiteCanCastleQ, blackCanCastleK, blackCanCastleQ, whitePawns, whiteKnights, whiteBishops, whiteRooks, whiteQueen, whiteKing, blackPawns, blackKnights, blackBishops, blackRooks, blackQueen, blackKing);
    }
}
