package com.github.louism33.chesscore;

public class StackDataRes {
    
    // 13 free bits
    /*
    00000000
    00000111
    11111111
    11111111
    11111111
    11111111
    11111111
    11111111
     */
     
    public static void main (String[] args){
        System.out.println(BitOperations.populationCount(SMD_MOVE_MASK
        | SMD_FIFTY_MOVES | SMD_TURN | SMD_SPECIAL_MOVE | SMD_EP_FILE | SMD_CASTLE_FLAGS));
        Art.printLong(SMD_MOVE_MASK | SMD_FIFTY_MOVES | SMD_SPECIAL_MOVE | SMD_EP_FILE | SMD_CASTLE_FLAGS  | SMD_TURN);
        
        Art.printLong(SMD_EP_FILE);
        Art.printLong(SMD_EP_FILE >>> smdEPOffset);
        
    }
    
    public static final long SMD_MOVE_MASK = 0xffffffffL;
    
    public static final long SMD_FIFTY_MOVES = 0x3f00000000L;
    public static final int smdFiftyPieceOffset = 32;

    public static final long SMD_SPECIAL_MOVE = 0x3c000000000L;
    public static final int smdSpecialOffset = 38;

    // 0 should mean no ep
    public static final long SMD_EP_FILE = 0x3c0000000000L;
    public static final int smdEPOffset = 42;

    public static final long SMD_CASTLE_FLAGS = 0x3c00000000000L;
    public static final int smdCastleOffset = 46;

    public static final long SMD_TURN = 0x4000000000000L;
    public static final int smdTurnOffset = 50;
    
    
}
