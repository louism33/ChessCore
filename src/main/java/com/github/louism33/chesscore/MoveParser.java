package com.github.louism33.chesscore;

import static com.github.louism33.chesscore.BitOperations.newPieceOnSquare;
import static com.github.louism33.chesscore.MoveConstants.*;
import static com.github.louism33.chesscore.MovePrettifier.prettyMove;
import static com.github.louism33.chesscore.Piece.*;

public class MoveParser {

    public static int numberOfRealMoves(int[] moves){
        int index = 0;
        while (moves[index] != 0){
            index++;
        }

        return index;
    }
    
    public static int copyMove(int move){
        return move;
    }

    private static int buildMove(Chessboard board, int s, int d) {
        if (s >= 64 | s < 0 | d >= 64 | d < 0) {
            throw new RuntimeException("Move: False Move " + s + " " + d);
        }
        int move = 0;
        move |= ((s << SOURCE_OFFSET) & SOURCE_MASK);
        move |= (d & DESTINATION_MASK);
        
        move |= (MoveConstants.SOURCE_PIECE_MASK | whichPieceMask(pieceOnSquare(board, newPieceOnSquare(s)))) << MoveConstants.SOURCE_PIECE_OFFSET;
        
        return move;
    }

    public static String[] toString(int[] moves){
        final int number = numberOfRealMoves(moves);
        String[] realMoves = new String[number];
        for (int i = 0; i < number; i ++){
            realMoves[i] = prettyMove(moves[i]);
        }
        return realMoves;
    }
    
    public static String toString(int move){
        return move == 0 ? "" : prettyMove(move);
    }

    public static int moveFromSourceDestination(Chessboard board, int source, int destinationIndex) {
        return buildMove(board, source, destinationIndex);
    }

    public static int moveFromSourceDestinationCapture(Chessboard board, int source, int destinationIndex, boolean capture) {
        return buildMove(board, source, destinationIndex) 
                | (capture ? (CAPTURE_MOVE_MASK | capturePieceMask(board, destinationIndex)) : 0);
    }

    private static int capturePieceMask(Chessboard board, int destinationIndex) {
        return whichPieceMask(pieceOnSquare(board, newPieceOnSquare(destinationIndex))) << MoveConstants.VICTIM_PIECE_OFFSET;
    }

    private static int whichPieceMask(Piece piece) {
        switch (piece){

            case WHITE_PAWN:
                return MoveConstants.WHITE_PAWN_MASK;
            case WHITE_KNIGHT:
                return MoveConstants.WHITE_KNIGHT_MASK;
            case WHITE_BISHOP:
                return MoveConstants.WHITE_BISHOP_MASK;
            case WHITE_ROOK:
                return MoveConstants.WHITE_ROOK_MASK;
            case WHITE_QUEEN:
                return MoveConstants.WHITE_QUEEN_MASK;
            case WHITE_KING:
                return MoveConstants.WHITE_KING_MASK;

            case BLACK_PAWN:
                return MoveConstants.BLACK_PAWN_MASK;
            case BLACK_KNIGHT:
                return MoveConstants.BLACK_KNIGHT_MASK;
            case BLACK_BISHOP:
                return MoveConstants.BLACK_BISHOP_MASK;
            case BLACK_ROOK:
                return MoveConstants.BLACK_ROOK_MASK;
            case BLACK_QUEEN:
                return MoveConstants.BLACK_QUEEN_MASK;
            case BLACK_KING:
                return MoveConstants.BLACK_KING_MASK;
                
            case NO_PIECE:
                return 0;
        }
        return 0;
    }

    private static int whichMover(int move){
        return 0;
    }
    
    
    public static int makeSpecialMove(Chessboard board, int source, int destinationIndex, boolean castling, boolean enPassant, boolean promotion,
                                      boolean promoteToKnight, boolean promoteToBishop, boolean promoteToRook, boolean promoteToQueen) {

        int move = buildMove(board, source, destinationIndex);

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
    
    public static long getSourceLong(int move) {
        return BitOperations.newPieceOnSquare((move & SOURCE_MASK) >>> SOURCE_OFFSET);
    }

    public static int getDestinationIndex(int move) {
        return move & DESTINATION_MASK;
    }

    public static long getDestinationLong(int move) {
        return BitOperations.newPieceOnSquare(move & DESTINATION_MASK);
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
        final Piece value = values()[indexOfSourcePiece];
        return value;
    }

    public static Piece getVictimPiece(int move){
        if (!isCaptureMove(move)) {
            return NO_PIECE;
        }
        final int indexOfVictimPiece = (move & VICTIM_PIECE_MASK) >>> VICTIM_PIECE_OFFSET;
        final Piece value = values()[indexOfVictimPiece];
        return value;
    }

    public static boolean moveIsPawnPushSeven(int move){
        return getMovingPiece(move) == WHITE_PAWN
                & getMovingPiece(move) == BLACK_PAWN
                & (getDestinationLong(move) & BitboardResources.RANK_SEVEN) != 0
                & (getDestinationLong(move) & BitboardResources.RANK_TWO) != 0;
    }

    public static boolean moveIsPawnPushSix(int move){
        return getMovingPiece(move) == WHITE_PAWN
                & getMovingPiece(move) == BLACK_PAWN
                & (getDestinationLong(move) & BitboardResources.RANK_SIX) != 0
                & (getDestinationLong(move) & BitboardResources.RANK_THREE) != 0;
    }

}