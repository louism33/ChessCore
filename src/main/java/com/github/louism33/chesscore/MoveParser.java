package com.github.louism33.chesscore;

import static com.github.louism33.chesscore.MoveConstants.*;
import static com.github.louism33.chesscore.MovePrettifier.prettyMove;

public class MoveParser {

    public static int copyMove(int move){
        return move;
    }

    private static int buildMove(int s, int d) {
        if (s >= 64 | s < 0 | d >= 64 | d < 0) {
            throw new RuntimeException("Move: False Move " + s + " " + d);
        }
        int move = 0;
        move |= ((s << SOURCE_OFFSET) & SOURCE_MASK);
        move |= (d & DESTINATION_MASK);
        return move;
    }

    public static String toString(int move){
        return prettyMove(move);
    }

    public static int moveFromSourceDestination(int source, int destinationIndex) {
        return buildMove(source, destinationIndex);
    }

    public static int moveFromSourceDestinationCapture(int source, int destinationIndex, boolean capture) {
        return buildMove(source, destinationIndex) | (capture ? CAPTURE_MOVE_MASK : 0);
    }

    public static int makeSpecialMove(int source, int destinationIndex, boolean castling, boolean enPassant, boolean promotion,
                                      boolean promoteToKnight, boolean promoteToBishop, boolean promoteToRook, boolean promoteToQueen) {

        int move = buildMove(source, destinationIndex);

        if (castling) move |= CASTLING_MASK;
        if (enPassant) move |= ENPASSANT_MASK;
        if (promotion) {
            if (promoteToKnight) move |= KNIGHT_PROMOTION_MASK;
            else if (promoteToBishop) move |= BISHOP_PROMOTION_MASK;
            else if (promoteToRook) move |= ROOK_PROMOTION_MASK;
            else if (promoteToQueen) move |= QUEEN_PROMOTION_MASK;
        }
        return move;
    }

    public static int getSourceIndex(int move) {
        return ((move & SOURCE_MASK) >>> SOURCE_OFFSET);
    }

    public static int getDestinationIndex(int move) {
        return move & DESTINATION_MASK;
    }

    public static boolean isCaptureMove(int move){
        return (move & CAPTURE_MOVE_MASK) != 0;
    }

    public static boolean isSpecialMove (int move){
        return (move & SPECIAL_MOVE_MASK) != 0;
    }

    public static boolean isCastlingMove (int move){
        return (move & SPECIAL_MOVE_MASK) == CASTLING_MASK;
    }

    public static boolean isEnPassantMove (int move){
        return (move & SPECIAL_MOVE_MASK) == ENPASSANT_MASK;
    }

    public static boolean isPromotionMove (int move){
        return (move & SPECIAL_MOVE_MASK) == PROMOTION_MASK;
    }

    public static boolean isPromotionToKnight (int move){
        if (!((move & SPECIAL_MOVE_MASK) == PROMOTION_MASK)) return false;
        return (move & WHICH_PROMOTION) == KNIGHT_PROMOTION_MASK;
    }

    public static boolean isPromotionToBishop(int move){
        if (!((move & SPECIAL_MOVE_MASK) == PROMOTION_MASK)) return false;
        return (move & WHICH_PROMOTION) == BISHOP_PROMOTION_MASK;
    }

    public static boolean isPromotionToRook (int move){
        if (!((move & SPECIAL_MOVE_MASK) == PROMOTION_MASK)) return false;
        return (move & WHICH_PROMOTION) == ROOK_PROMOTION_MASK;
    }

    public static boolean isPromotionToQueen (int move){
        if (!((move & SPECIAL_MOVE_MASK) == PROMOTION_MASK)) return false;
        return (move & WHICH_PROMOTION) == QUEEN_PROMOTION_MASK;
    }

    public static Piece getMovingPiece(int move){
        final int indexOfSourcePiece = (move & SOURCE_PIECE_MASK) >>> SOURCE_PIECE_OFFSET;
        final Piece value = Piece.values()[indexOfSourcePiece];
        return value;
    }

    public static Piece getVictimPiece(int move){
        if (!isCaptureMove(move)) {
            return null;
        }
        final int indexOfVictimPiece = (move & VICTIM_PIECE_MASK) >>> VICTIM_PIECE_OFFSET;
        final Piece value = Piece.values()[indexOfVictimPiece];
        return value;
    }

}