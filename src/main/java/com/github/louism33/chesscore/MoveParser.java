package com.github.louism33.chesscore;

import org.junit.Assert;

import java.util.Arrays;
import java.util.List;

import static com.github.louism33.chesscore.BoardConstants.*;
import static com.github.louism33.chesscore.MoveConstants.*;

public class MoveParser {

    /*
    00000001
    11111111
    00000000
    00000000
     */


    public static int numberOfRealMoves(int[] moves){
        return moves[moves.length - 1];
    }
    
    public static void printMoves(int[] moves){
        System.out.println(Arrays.toString(MoveParser.toString(moves)));
    }
    
    public static int buildMove(int source, int whichSourcePiece, int destinationIndex, int victimPiece) {
        Assert.assertTrue(source >= 0 && source < 64 && destinationIndex >= 0 && destinationIndex < 64);

        int move = 0;
        move |= ((source << SOURCE_OFFSET) & SOURCE_MASK);
        move |= (destinationIndex & DESTINATION_MASK);

        move |= (MoveConstants.SOURCE_PIECE_MASK
                & (whichSourcePiece << MoveConstants.SOURCE_PIECE_OFFSET));

        if (victimPiece != NO_PIECE) {
            move |= (CAPTURE_MOVE_MASK | (victimPiece << MoveConstants.VICTIM_PIECE_OFFSET));
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
        int number = moves[moves.length - 1];
        String[] realMoves = new String[number];
        for (int i = 0; i < number; i ++){
            realMoves[i] = prettyMove(moves[i]);
        }
        return realMoves;
    }

    public static String toString(int move){
        return move == 0 ? "0000" : prettyMove(move);
    }

    static String prettyMove(int move){
        if (move == 0) {
            return ".";
        }
        int sourceAsPiece = getSourceIndex(move);
        String file = Character.toString('h' - (sourceAsPiece % (8)));
        String rank = (sourceAsPiece / 8 + 1) + "";
        int destination = getDestinationIndex(move);
        String destinationFile = Character.toString('h' - (destination % (8)));
        String destinationRank = (destination / 8 + 1) + "";
        String m = ""+file+ rank+destinationFile+ destinationRank;

        if (isPromotionMove(move)){
            if (isPromotionToKnight(move)){
                m += "N";
            }
            else if (isPromotionToBishop(move)){
                m += "B";
            }
            else if (isPromotionToRook(move)){
                m += "R";
            }
            else if (isPromotionToQueen(move)){
                m += "Q";
            }
        }
        return m;
    }
}