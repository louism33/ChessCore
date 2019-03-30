package com.github.louism33.chesscore;

public final class StackConstants {
    
    // 11 free bits
    /*
    00000000
    00011111
    11111111
    11111111
    11111111
    11111111
    11111111
    11111111
    
    00000000
    000tcccc
    eeeessss
    ffffffff
    mmmmmmmm
    mmmmmmmm
    mmmmmmmm
    mmmmmmmm
    
    t: turn
    c: castle rights
    s: ep file
    s: special move
    f: quiet move counter
    m: move
     */
     
    public static final long SMD_MOVE_MASK = 0xffffffffL;

    public static final long SMD_QUIET_MOVES = 0xff00000000L;
    public static final int smdQuietPieceOffset = 32;

    public static final long SMD_SPECIAL_MOVE = 0xf0000000000L;
    public static final int smdSpecialOffset = 40;

    // 0 should mean no ep
    public static final long SMD_EP_FILE = 0xf00000000000L;
    public static final int smdEPOffset = 44;

    public static final long SMD_CASTLE_FLAGS = 0xf000000000000L;
    public static final int smdCastleOffset = 48;

    public static final long SMD_TURN = 0x10000000000000L;
    public static final int smdTurnOffset = 52;

}
