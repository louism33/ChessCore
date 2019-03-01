package com.github.louism33.utils;

import com.github.louism33.chesscore.Chessboard;
import com.github.louism33.chesscore.MoveConstants;
import com.github.louism33.chesscore.MoveParser;
import com.github.louism33.chesscore.MoveParserFromAN;
import org.junit.Assert;

import static com.github.louism33.chesscore.BitOperations.newPieceOnSquare;
import static com.github.louism33.chesscore.MoveConstants.*;
import static com.github.louism33.utils.Piece.pieceOnSquareInt;

public class Parser {
    public static int moveFromSourceDestinationSquareCaptureSecure(Chessboard board, Piece movingPiece,
                                                                   long file, Square source, Square destinationIndex, boolean capture) {
        if (source == null){
            int sourceIndex = -1;
            
            int[] moves = board.generateLegalMoves();
            for (int i = 0; i < moves.length; i++){
                int move = moves[i];
                if (move == 0){
                    break;
                }
                if ((MoveParser.getSourceLong(move) & file) == 0){
                    continue;
                }
                
                if (MoveParser.getDestinationIndex(move) == destinationIndex.ordinal()){
                    if (movingPiece != null && movingPiece != Piece.NO_PIECE){
                        if (MoveParser.getMovingPiece(move) != movingPiece){
                            continue;
                        }
                    }
                    sourceIndex = MoveParser.getSourceIndex(move);
                }
            }
            if (sourceIndex == -1){
                throw new RuntimeException("Could not parse Algebraic notation move");
            }
            return buildSimpleMove(board, sourceIndex, destinationIndex.ordinal())
                    | (capture ? (CAPTURE_MOVE_MASK | capturePieceMask(board, destinationIndex.ordinal())) : 0);
        }
        
        return buildSimpleMove(board, source.ordinal(), destinationIndex.ordinal())
                | (capture ? (CAPTURE_MOVE_MASK | capturePieceMask(board, destinationIndex.ordinal())) : 0);
    }

    public static int buildSimpleMove(Chessboard board, int s, int d) {
        Assert.assertTrue(s >= 0 && s < 64 && d >= 0 && d < 64);
        
        int move = 0;
        move |= ((s << SOURCE_OFFSET) & SOURCE_MASK);
        move |= (d & DESTINATION_MASK);
        
        move |= (MoveConstants.SOURCE_PIECE_MASK 
                | (pieceOnSquareInt(board, newPieceOnSquare(s)))) << MoveConstants.SOURCE_PIECE_OFFSET;
        
        return move;
    }

    public static int newMove(Chessboard board, String algebraicNotation){
        return MoveParserFromAN.buildMoveFromLAN(board, algebraicNotation);
    }

    public static int capturePieceMask(Chessboard board, int destinationIndex) {
        return (pieceOnSquareInt(board, newPieceOnSquare(destinationIndex))) << MoveConstants.VICTIM_PIECE_OFFSET;
    }
}
