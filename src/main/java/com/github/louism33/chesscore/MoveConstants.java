package com.github.louism33.chesscore;

public class MoveConstants {

    /*
    00000001
    11111111
    00000000
    00000000
     */
    final static int
            WHITE_PAWN_MASK = 0x00000001;
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


    public final static int SOURCE_OFFSET = 6;
    public final static int SOURCE_MASK = 0x00000fc0;
    public final static int DESTINATION_MASK = 0x0000003f;

    public final static int ENPASSANT_MASK = 0x00002000;
    public final static int PROMOTION_MASK = 0x00003000;

    final static int SPECIAL_MOVE_MASK = 0x00003000;
    public final static int CASTLING_MASK = 0x00001000;


    public final static int KNIGHT_PROMOTION_MASK = 0x00000000;
    public final static int BISHOP_PROMOTION_MASK = 0x00004000;
    public final static int ROOK_PROMOTION_MASK = 0x00008000;
    public final static int QUEEN_PROMOTION_MASK = 0x0000c000;
    final static int WHICH_PROMOTION_OFFSET = 14;

    final static int WHICH_PROMOTION = 0x0000c000;

    public final static int SOURCE_PIECE_MASK = 0x000f0000;
    public final static int SOURCE_PIECE_OFFSET = 16;

    final static int VICTIM_PIECE_MASK = 0x00f00000;
    public final static int VICTIM_PIECE_OFFSET = 20;


    public final static int CAPTURE_MOVE_MASK = 0x01000000
            ;
}
