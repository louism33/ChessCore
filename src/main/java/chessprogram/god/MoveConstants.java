package chessprogram.god;

public class MoveConstants {

    // todo, should not be public
    final static int  
            SOURCE_PIECE_MASK = 0x000f0000,
            SOURCE_PIECE_OFFSET = 16,
    
            VICTIM_PIECE_MASK = 0x00f00000,
            VICTIM_PIECE_OFFSET = 20,

    WHITE_PAWN_MASK = 0x00010000,
            WHITE_KNIGHT_MASK = 0x00020000,
            WHITE_BISHOP_MASK = 0x00030000,
            WHITE_ROOK_MASK = 0x00040000,
            WHITE_QUEEN_MASK = 0x00050000,
            WHITE_KING_MASK = 0x00060000,

    BLACK_PAWN_MASK = 0x00070000,
            BLACK_KNIGHT_MASK = 0x00080000,
            BLACK_BISHOP_MASK = 0x00090000,
            BLACK_ROOK_MASK = 0x000a0000,
            BLACK_QUEEN_MASK = 0x000b0000,
            BLACK_KING_MASK = 0x000c0000,
    
            
    CAPTURE_MOVE_MASK = 0x01000000,

            
    ENPASSANT_MASK = 0x00002000,
            PROMOTION_MASK = 0x00003000,

    KNIGHT_PROMOTION_MASK = 0x00000000,
            BISHOP_PROMOTION_MASK = 0x00004000,
            ROOK_PROMOTION_MASK = 0x00008000,
            QUEEN_PROMOTION_MASK = 0x0000c000,

    WHICH_PROMOTION = 0x0000c000,
            SOURCE_OFFSET = 6,
            SOURCE_MASK = 0x00000fc0,
            DESTINATION_MASK = 0x0000003f,

    SPECIAL_MOVE_MASK = 0x00003000,
            CASTLING_MASK = 0x00001000;


}
