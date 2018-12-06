package com.github.louism33.chesscore;

import org.junit.Assert;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.github.louism33.chesscore.BitboardResources.FILES;
import static com.github.louism33.chesscore.BitboardResources.UNIVERSE;
import static com.github.louism33.chesscore.ConstantsMove.*;
import static com.github.louism33.chesscore.ConstantsMove.CAPTURE_MOVE_MASK;
import static com.github.louism33.chesscore.ConstantsMove.VICTIM_PIECE_MASK;
import static com.github.louism33.chesscore.MoveParser.*;

public class MoveParserFromAN {

    private static final String boardPattern =
            "([(PNBRQK)|a-h]?)" +
//            "[a-h]?"+
            "([a-h])?" +
            "(x?)" +
//            "([a-h])?" +
            "([a-h][1-8])?" +
            "(x?)" +
            "([a-zA-Z][1-8])" +
            "(\\+?)" +
            "";
    
    public static int buildMoveFromAN(Chessboard board, String an){

//        System.out.println();
//        System.out.println("Algebraic notation: "+an);
        
        Pattern r = Pattern.compile(boardPattern);
        Matcher m = r.matcher(an);

        String all = "";
        String sourcePiece = "";
        String capture1 = "";
        String possibleSourceFile = "";
        String sourceSquareString = "";
        String capture2 = "";
        String destinationString = "";

        String checkMove = "";

        if (m.find()){
            all = m.group();
            sourcePiece = m.group(1);
            capture1 = m.group(3);
            possibleSourceFile = m.group(2);
            sourceSquareString = m.group(4);
            capture2 = m.group(5);
            destinationString = m.group(6);
            checkMove = m.group(7);
        }

//        System.out.println("__________________");
//        System.out.println("all: "+  all);
//        System.out.println();
//        
//        System.out.println("sourcePiece:            "+ sourcePiece);
//
//        System.out.println("capture1:               "+ capture1);
//        System.out.println("possibleSourceFile:     "+ possibleSourceFile);
//        System.out.println("sourceSquareString:     "+ sourceSquareString);
//        System.out.println("capture2:               "+ capture2);
//        System.out.println("destinationString:      "+ destinationString);
//        System.out.println("checking move:          "+ checkMove);
//        System.out.println();
//        System.out.println("__________________");

        Square sourceSquare = null;
        if (sourceSquareString != null){
            sourceSquare = Square.valueOf(sourceSquareString.toUpperCase());
        }
        Square destinationSquare = Square.valueOf(destinationString.toUpperCase());

        // ep ?
        boolean isCapture = (destinationSquare.toBitboard() & board.allPieces()) != 0;
        
        Piece movingPiece = null;
        if (sourcePiece != null && !sourcePiece.equals("")) {
//            System.out.println(translateFromLetter(sourcePiece));
            movingPiece = translateFromLetter(board.isWhiteTurn(), sourcePiece);
        }
        
        long optionalSourceFile = UNIVERSE;
        
        if (possibleSourceFile != null && !possibleSourceFile.equals("")){
            optionalSourceFile = FILES[7 - (possibleSourceFile.charAt(0) - 'a')];
        }
        
        int move = moveFromSourceDestinationSquareCaptureSecure(board, movingPiece, optionalSourceFile, sourceSquare, destinationSquare,
                isCapture);

        
        if ((capture1 != null && capture1.equals("x")) || capture2 != null && capture2.equals("x")){
            Assert.assertTrue(isCapture);
            Assert.assertTrue(MoveParser.isCaptureMove(move));
        }
        
        if (sourcePiece != null && !sourcePiece.equals("")) {
            Assert.assertEquals(translateFromLetter(board.isWhiteTurn(), sourcePiece), MoveParser.getMovingPiece(move));
        }

        if (checkMove.equals("+")) {
            return makeCheckingMove(move);
        }

        return move;
    }


    private static Piece translateFromLetter (boolean white, String piece){
        if (white) {
            switch (piece) {
                case "a":
                case "b":
                case "c":
                case "d":
                case "e":
                case "f":
                case "g":
                case "P":
                    return Piece.WHITE_PAWN;
                case "N":
                    return Piece.WHITE_KNIGHT;
                case "R":
                    return Piece.WHITE_ROOK;
                case "B":
                    return Piece.WHITE_BISHOP;
                case "Q":
                    return Piece.WHITE_QUEEN;
                case "K":
                    return Piece.WHITE_KING;
                default:
                    return Piece.NO_PIECE;
            }
        }
        else {
            switch (piece){
                case "a":
                case "b":
                case "c":
                case "d":
                case "e":
                case "f":
                case "g":
                case "P":
                    return Piece.BLACK_PAWN;
                case "N":
                    return Piece.BLACK_KNIGHT;
                case "R":
                    return Piece.BLACK_ROOK;
                case "B":
                    return Piece.BLACK_BISHOP;
                case "Q":
                    return Piece.BLACK_QUEEN;
                case "K":
                    return Piece.BLACK_KING;
                default:
                    return Piece.NO_PIECE;
            }
        }
    }


    public static int destinationIndex(Chessboard board, String algebraicNotation){
        return rankAndFile(board, algebraicNotation);
    }

    private static int rankAndFile(Chessboard board, String square){
        int f = whichDestinationFile(board, square);
        int r = whichDestinationRank(board, square);
        return (r-1) * 8 + f;
    }
    private static int whichDestinationRank(Chessboard board, String algebraicNotation){
        String boardPattern = ".?x?.x?(\\d)";
        Pattern r = Pattern.compile(boardPattern);
        Matcher m = r.matcher(algebraicNotation);

        String pieceFromAN = "";

        if (m.find()){
            pieceFromAN = m.group(1);
        }
        if (pieceFromAN.length() == 0){
            throw new RuntimeException("Could not parse file");
        }

        return Integer.parseInt(pieceFromAN);
    }

    private static int whichDestinationFile(Chessboard board, String algebraicNotation){
        String boardPattern = "([a-h])(\\d)";
        Pattern r = Pattern.compile(boardPattern);
        Matcher m = r.matcher(algebraicNotation);

        String pieceFromAN = "";

        if (m.find()){
            pieceFromAN = m.group(1);
        }
        if (pieceFromAN.length() == 0){
            throw new RuntimeException("Could not parse file");
        }

        switch (pieceFromAN) {
            case "a": {
                return 7;
            }
            case "b": {
                return 6;
            }
            case "c": {
                return 5;
            }
            case "d": {
                return 4;
            }
            case "e": {
                return 3;
            }
            case "f": {
                return 2;
            }
            case "g": {
                return 1;
            }
            case "h": {
                return 0;
            }
            default:
                throw new RuntimeException("problem with Getting destinationIndex file");
        }
    }


}
