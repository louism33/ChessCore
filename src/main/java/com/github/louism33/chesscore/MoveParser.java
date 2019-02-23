package com.github.louism33.chesscore;

import org.junit.Assert;

import java.util.Arrays;
import java.util.List;

import static com.github.louism33.chesscore.BitOperations.newPieceOnSquare;
import static com.github.louism33.chesscore.BoardConstants.*;
import static com.github.louism33.chesscore.MoveConstants.*;
import static com.github.louism33.chesscore.MovePrettifier.prettyMove;
import static com.github.louism33.chesscore.Piece.pieceOnSquareInt;
import static com.github.louism33.chesscore.Piece.values;

public class MoveParser {

    /*
    00000001
    11111111
    00000000
    00000000
     */


    public static int numberOfRealMoves(int[] moves){
        int index = 0;
        while (moves[index] != 0){
            index++;
        }

        return index;
    }
    
    public static void printMoves(int[] moves){
        System.out.println(Arrays.toString(MoveParser.toString(moves)));
    }
    
    static int buildBetterMove(int s, int whichSourcePiece, int d, int victimPiece) {
        Assert.assertTrue(s >= 0 && s < 64 && d >= 0 && d < 64);

        int move = 0;
        move |= ((s << SOURCE_OFFSET) & SOURCE_MASK);
        move |= (d & DESTINATION_MASK);

        move |= (MoveConstants.SOURCE_PIECE_MASK
                & (whichSourcePiece << MoveConstants.SOURCE_PIECE_OFFSET));

        if (victimPiece != NO_PIECE) {
            move |= (CAPTURE_MOVE_MASK | (victimPiece << MoveConstants.VICTIM_PIECE_OFFSET));
        }

        return move;
    }


    static int moveFromSourceDestinationCaptureBetter(int source, int sourcePiece,
                                                      int destinationIndex, int victimPiece) {
        
        return buildBetterMove(source, sourcePiece, destinationIndex, NO_PIECE)
                | (victimPiece != NO_PIECE ? (CAPTURE_MOVE_MASK | (victimPiece << VICTIM_PIECE_OFFSET)) : 0);
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
    
    public static int whichPromotion(int move){
        return (move & WHICH_PROMOTION) >>> WHICH_PROMOTION_OFFSET;
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
        return values()[indexOfSourcePiece];
    }

    public static int getMovingPieceInt(int move){
        return (move & SOURCE_PIECE_MASK) >>> SOURCE_PIECE_OFFSET;
    }

    public static int getVictimPieceInt(int move){
        return (move & VICTIM_PIECE_MASK) >>> VICTIM_PIECE_OFFSET;
    }

    public static boolean moveIsPawnPushSeven(int move){
        return getMovingPieceInt(move) == WHITE_PAWN
                & getMovingPieceInt(move) == BLACK_PAWN
                & (getDestinationLong(move) & BoardConstants.RANK_SEVEN) != 0
                & (getDestinationLong(move) & BoardConstants.RANK_TWO) != 0;
    }

    public static boolean moveIsPawnPushSix(int move){
        return getMovingPieceInt(move) == WHITE_PAWN
                & getMovingPieceInt(move) == BLACK_PAWN
                & (getDestinationLong(move) & BoardConstants.RANK_SIX) != 0
                & (getDestinationLong(move) & BoardConstants.RANK_THREE) != 0;
    }
    
    public static boolean verifyMoveCheap(Chessboard board, int move){
        long sourceLong = getSourceLong(move);
        if ((sourceLong & board.allPieces()) == 0){
            return false;
        }
        long destinationLong = getDestinationLong(move);
        if (isCaptureMove(move)){
            if ((destinationLong & board.allPieces()) == 0){
                return false;
            }
        }
        return true;
    }

    public static boolean verifyMoveCertain(Chessboard board, int move){
        int[] legalMoves = board.generateLegalMoves();
        for (int j = 0; j < legalMoves.length; j++) {
            int possibleMove = legalMoves[j];
            if (possibleMove == 0) {
                break;
            }
            if (move == possibleMove) {
                return true;
            }
        }
        return false;
    }


    public static String[] toString(List<Integer> moves){
        final int number = moves.size();
        String[] realMoves = new String[number];
        for (int i = 0; i < number; i ++){
            realMoves[i] = prettyMove(moves.get(i));
        }
        return realMoves;
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
        return move == 0 ? "NULL_MOVE" : prettyMove(move);
    }
}