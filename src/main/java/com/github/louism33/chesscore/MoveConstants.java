package com.github.louism33.chesscore;

public final class MoveConstants {

    /*
    free: 
    11111110
    00000000
    00000000
    00000000
    
    0000000c
    vvvvmmmm
    ppSSssss
    ssdddddd
    
    c = capture flag
    v = victim piece
    m = source piece
    p = promotions
    S = Special
    s = source
    d = destination
     */
    final static int WHITE_PAWN_MASK = 0x00000001;
    final static int WHITE_KNIGHT_MASK = 0x00000002;
    final static int WHITE_BISHOP_MASK = 0x00000003;
    final static int WHITE_ROOK_MASK = 0x00000004;
    final static int WHITE_QUEEN_MASK = 0x00000005;
    final static int WHITE_KING_MASK = 0x00000006;

    final static int BLACK_PAWN_MASK = 0x00000007;
    final static int BLACK_KNIGHT_MASK = 0x00000008;
    final static int BLACK_BISHOP_MASK = 0x00000009;
    final static int BLACK_ROOK_MASK = 0x0000000a;
    final static int BLACK_QUEEN_MASK = 0x0000000b;
    final static int BLACK_KING_MASK = 0x0000000c;

    public static void main(String[] args) {
        int i = DESTINATION_MASK;
        i |= SOURCE_MASK;
        i |= SPECIAL_MOVE_MASK;
        i |= QUEEN_PROMOTION_MASK;
        i |= SOURCE_PIECE_MASK;
        i |= VICTIM_PIECE_MASK;

        i |= CAPTURE_MOVE_MASK;
        i |= CHECKING_MOVE_MASK;
        
        Art.printLong(i);
        
        Art.printLong(MOVE_SCORE_MASK);
        Art.printLong(MOVE_MASK_WITH_CHECK);
        Art.printLong(MOVE_MASK_WITHOUT_CHECK);
    }

    public final static int DESTINATION_MASK = 0x0000003f;
    
    public final static int SOURCE_OFFSET = 6;
    public final static int SOURCE_MASK = 0x00000fc0;

    final static int SPECIAL_MOVE_MASK = 0x00003000;
    public final static int CASTLING_MASK = 0x00001000;
    public final static int ENPASSANT_MASK = 0x00002000;
    public final static int PROMOTION_MASK = 0x00003000;

    final static int WHICH_PROMOTION = 0x0000c000;
    public final static int KNIGHT_PROMOTION_MASK = 0x00000000;
    public final static int BISHOP_PROMOTION_MASK = 0x00004000;
    public final static int ROOK_PROMOTION_MASK = 0x00008000;
    public final static int QUEEN_PROMOTION_MASK = 0x0000c000;
    final static int WHICH_PROMOTION_OFFSET = 14;

    public final static int SOURCE_PIECE_MASK = 0x000f0000;
    public final static int SOURCE_PIECE_OFFSET = 16;

    final static int VICTIM_PIECE_MASK = 0x00f00000;
    public final static int VICTIM_PIECE_OFFSET = 20;

    public final static int CAPTURE_MOVE_MASK = 0x01000000;

    public final static int CHECKING_MOVE_MASK = 0x02000000;
    public final static int FIRST_FREE_BIT = CHECKING_MOVE_MASK << 1;

    public static final int moveScoreOffset = 26;
    public static final int MOVE_SCORE_MASK = 0xfc000000;
    
    public static final int MOVE_MASK_WITH_CHECK = 0x3ffffff;
    public static final int MOVE_MASK_WITHOUT_CHECK = 0x1ffffff;
    
}
