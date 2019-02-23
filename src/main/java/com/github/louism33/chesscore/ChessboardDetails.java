package com.github.louism33.chesscore;

class ChessboardDetails {

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


}
