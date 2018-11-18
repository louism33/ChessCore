package chessprogram.move;

import java.util.Objects;

public class Move {

    public int move;
    final public static int
            ENPASSANT_MASK = 0x00002000,
            PROMOTION_MASK = 0x00003000,
            
            KNIGHT_PROMOTION_MASK = 0x00000000,
            BISHOP_PROMOTION_MASK = 0x00004000,
            ROOK_PROMOTION_MASK = 0x00008000,
            QUEEN_PROMOTION_MASK = 0x0000c000;

    final private static int
            WHICH_PROMOTION = 0x0000c000,
            SOURCE_OFFSET = 6,
            SOURCE_MASK = 0x00000fc0,
            DESTINATION_MASK = 0x0000003f,

            SPECIAL_MOVE_MASK = 0x00003000,
            CASTLING_MASK = 0x00001000;



    // copy constructor
    public Move (Move move){
        this.move = move.move;
    }

    public Move(int source, int destinationIndex) {
        makeSourceAndDest(source, destinationIndex);
    }

    public Move(int source, int destinationIndex, boolean castling, boolean enPassant, boolean promotion,
                boolean promoteToKnight, boolean promoteToBishop, boolean promoteToRook, boolean promoteToQueen) {

        makeSourceAndDest(source, destinationIndex);

        if (castling) this.move |= CASTLING_MASK;
        if (enPassant) this.move |= ENPASSANT_MASK;
        if (promotion) {
            if (promoteToKnight) this.move |= KNIGHT_PROMOTION_MASK;
            else if (promoteToBishop) this.move |= BISHOP_PROMOTION_MASK;
            else if (promoteToRook) this.move |= ROOK_PROMOTION_MASK;
            else if (promoteToQueen) this.move |= QUEEN_PROMOTION_MASK;
        }
    }

    public Move(int source, int destinationIndex, boolean castling, boolean enPassant, boolean promotion,
                boolean promoteToKnight, boolean promoteToBishop, boolean promoteToRook, boolean promoteToQueen, int hack) {

        makeSourceAndDest(source, destinationIndex);

        if (castling) this.move |= CASTLING_MASK;
        if (enPassant) this.move |= ENPASSANT_MASK;
        if (promotion) {
            this.move |= PROMOTION_MASK;
            if (promoteToKnight) this.move |= KNIGHT_PROMOTION_MASK;
            else if (promoteToBishop) this.move |= BISHOP_PROMOTION_MASK;
            else if (promoteToRook) this.move |= ROOK_PROMOTION_MASK;
            else if (promoteToQueen) this.move |= QUEEN_PROMOTION_MASK;
        }
    }


    private void makeSourceAndDest(int s, int d) {
        if (s >= 64 | s < 0 | d >= 64 | d < 0) {
            throw new RuntimeException("Move: False Move " + s + " " + d);
        }
        this.move |= ((s << SOURCE_OFFSET) & SOURCE_MASK);
        this.move |= (d & DESTINATION_MASK);
    }

    @Override
    public String toString() {
        return MovePrettifier.prettyMove(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Move move1 = (Move) o;
        return move == move1.move;
    }

    @Override
    public int hashCode() {
        return Objects.hash(move);
    }

    public int getSourceIndex() {
        return ((this.move & SOURCE_MASK) >>> SOURCE_OFFSET);
    }

    public int getDestinationIndex() {
        return this.move & DESTINATION_MASK;
    }

    public boolean isSpecialMove (){
        return (this.move & SPECIAL_MOVE_MASK) != 0;
    }

    public boolean isCastlingMove (){
        return (this.move & SPECIAL_MOVE_MASK) == CASTLING_MASK;
    }

    public boolean isEnPassantMove (){
        return (this.move & SPECIAL_MOVE_MASK) == ENPASSANT_MASK;
    }

    public boolean isPromotionMove (){
        return (this.move & SPECIAL_MOVE_MASK) == PROMOTION_MASK;
    }

    public boolean isPromotionToKnight (){
        if (!((this.move & SPECIAL_MOVE_MASK) == PROMOTION_MASK)) return false;
        return (this.move & WHICH_PROMOTION) == KNIGHT_PROMOTION_MASK;
    }

    public boolean isPromotionToBishop(){
        if (!((this.move & SPECIAL_MOVE_MASK) == PROMOTION_MASK)) return false;
        return (this.move & WHICH_PROMOTION) == BISHOP_PROMOTION_MASK;
    }

    public boolean isPromotionToRook (){
        if (!((this.move & SPECIAL_MOVE_MASK) == PROMOTION_MASK)) return false;
        return (this.move & WHICH_PROMOTION) == ROOK_PROMOTION_MASK;
    }

    public boolean isPromotionToQueen (){
        if (!((this.move & SPECIAL_MOVE_MASK) == PROMOTION_MASK)) return false;
        return (this.move & WHICH_PROMOTION) == QUEEN_PROMOTION_MASK;
    }

}