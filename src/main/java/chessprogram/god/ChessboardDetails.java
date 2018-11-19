package chessprogram.god;

class ChessboardDetails {

    boolean whiteTurn = true;
    boolean whiteCanCastleK = true;
    boolean whiteCanCastleQ = true;
    boolean blackCanCastleK = true;
    boolean blackCanCastleQ = true;

    long whitePawns = 0x000000000000FF00L;
    long whiteKnights = 0x0000000000000042L;
    long whiteBishops = 0x0000000000000024L;
    long whiteRooks = 0x0000000000000081L;
    long whiteQueen = 0x0000000000000010L;
    long whiteKing = 0x0000000000000008L;

    long blackPawns = 0x00FF000000000000L;
    long blackKnights = 0x4200000000000000L;
    long blackBishops = 0x2400000000000000L;
    long blackRooks = 0x8100000000000000L;
    long blackQueen = 0x1000000000000000L;
    long blackKing = 0x0800000000000000L;
}
