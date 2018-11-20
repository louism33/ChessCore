package chessprogram.god;

import java.util.Objects;

import static chessprogram.god.MoveConstants.*;

public class Move {
    
    int move;

    // todo get rid of eventually
    public int getMove() {
        return move;
    }

    
            

    
  

    // copy constructor
    public Move (Move move){
        this.move = move.move;
    }

    public Move(int source, int destinationIndex) {
        buildMove(source, destinationIndex);
    }

    public Move(int source, int destinationIndex, boolean capture) {
        buildMove(source, destinationIndex);
        if (capture){
            MoveUtils.makeCapture(this);
            this.move |= CAPTURE_MOVE_MASK;
        }
    }

    public Move(int source, int destinationIndex, boolean castling, boolean enPassant, boolean promotion,
                boolean promoteToKnight, boolean promoteToBishop, boolean promoteToRook, boolean promoteToQueen) {

        buildMove(source, destinationIndex);

        if (castling) this.move |= CASTLING_MASK;
        if (enPassant) this.move |= ENPASSANT_MASK;
        if (promotion) {
            if (promoteToKnight) this.move |= KNIGHT_PROMOTION_MASK;
            else if (promoteToBishop) this.move |= BISHOP_PROMOTION_MASK;
            else if (promoteToRook) this.move |= ROOK_PROMOTION_MASK;
            else if (promoteToQueen) this.move |= QUEEN_PROMOTION_MASK;
        }
    }

    private void buildMove(int s, int d) {
        if (s >= 64 | s < 0 | d >= 64 | d < 0) {
            throw new RuntimeException("Move: False Move " + s + " " + d);
        }
        this.move |= ((s << SOURCE_OFFSET) & SOURCE_MASK);
        this.move |= (d & DESTINATION_MASK);
        MoveUtils.buildSource(this);
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

    public boolean isCaptureMove(){
        return (this.move & CAPTURE_MOVE_MASK) != 0;
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
    
    public Piece getMovingPiece(){
        final int indexOfSourcePiece = (this.move & SOURCE_PIECE_MASK) >>> SOURCE_PIECE_OFFSET;
        final Piece value = Piece.values()[indexOfSourcePiece];
        System.out.println(value);
        return value;
    }

    public Piece getVictimPiece(){
        if (!isCaptureMove()) {
            return null;
        }
        final int indexOfVictimPiece = (this.move & VICTIM_PIECE_MASK) >>> VICTIM_PIECE_OFFSET;
        final Piece value = Piece.values()[indexOfVictimPiece];
        System.out.println(value);
        return value;
    }

}