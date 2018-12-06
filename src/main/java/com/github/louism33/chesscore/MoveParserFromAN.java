package com.github.louism33.chesscore;

import org.junit.Assert;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.github.louism33.chesscore.ConstantsMove.*;
import static com.github.louism33.chesscore.ConstantsMove.CAPTURE_MOVE_MASK;
import static com.github.louism33.chesscore.ConstantsMove.VICTIM_PIECE_MASK;
import static com.github.louism33.chesscore.MoveParser.*;

public class MoveParserFromAN {

    private static final String boardPattern = "([abcdefgh]?)" +
            "([pPnNbBrRqQkK]?)" +
            "([a-h][1|2|3|4|5|6|7|8])?" +
            "(x?)" +
            "([a-zA-Z][1|2|3|4|5|6|7|8]?)" +
            "(\\+?)" +
            "";
    
    public static void main(String[] args){

//        Chessboard board = new Chessboard("r1b1kb1r/pppp1ppp/5q2/4n3/3KP3/2N3PN/PPP4P/R1BQ1B1R b kq - 0 1");
//        Chessboard board = new Chessboard("r3k2r/ppp2Npp/1b5n/4p2b/2B1P2q/BQP2P2/P5PP/RN5K w kq - 1 0");
//        Chessboard board = new Chessboard("r1b3kr/ppp1Bp1p/1b6/n2P4/2p3q1/2Q2N2/P4PPP/RN2R1K1 w - - 1 0");
//        Chessboard board = new Chessboard("3q1r1k/2p4p/1p1pBrp1/p2Pp3/2PnP3/5PP1/PP1Q2K1/5R1R w - - 1 0");
//        Chessboard board = new Chessboard("8/6pk/pb5p/8/1P2qP2/P3p3/2r2PNP/1QR3K1 b - - 0 1");
        Chessboard board = new Chessboard("8/7p/5k2/5p2/p1p2P2/Pr1pPK2/1P1R3P/8 b - - 0 1");


        System.out.println(board);
//        String temp = "f8c5+";
//        String temp = "Bb5+";
//        String temp = "Qxh8+";
//        String temp = "Rxh7+";
//        String temp = "exf2+";
        String temp = "Rxb2";

        System.out.println(temp);
        int x = buildMoveFromAN(board, temp);

        System.out.println(MoveParser.toString(x));

    }


    public static int buildMoveFromAN(Chessboard board, String an){

        System.out.println("Algebraic notation: "+an);
        
        Pattern r = Pattern.compile(boardPattern);
        Matcher m = r.matcher(an);

        String all = "";
        String sourcePawn = "";
        String sourcePiece = "";
        String sourceSquareString = "";
        String capture = "";
        String destinationString = "";

        String checkMove = "";

        if (m.find()){
            all = m.group();
            sourcePawn = m.group(1);
            sourcePiece = m.group(2);
            sourceSquareString = m.group(3);
            capture = m.group(4);
            destinationString = m.group(5);
            checkMove = m.group(6);
        }

        System.out.println("__________________");
        System.out.println("all: "+  all);
        System.out.println();
        System.out.println("sourcePawn:             "+ sourcePawn);
        System.out.println("sourcePiece:            "+ sourcePiece);
        System.out.println("sourceSquareString:     "+ sourceSquareString);
        System.out.println("capture:                " + capture);
        System.out.println("destinationString:      "+ destinationString);
        System.out.println("checking move:          " + checkMove);
        System.out.println();
        System.out.println("__________________");

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
        
        if (movingPiece == null && sourcePawn != null && !sourcePawn.equals("")){
            movingPiece = board.isWhiteTurn() ? Piece.WHITE_PAWN : Piece.BLACK_PAWN;
        }
        
        int move = moveFromSourceDestinationSquareCaptureSecure(board, movingPiece, sourceSquare, destinationSquare,
                isCapture);

        
        if (capture != null && capture.equals("x")){
            Assert.assertTrue(isCapture);
            Assert.assertTrue(MoveParser.isCaptureMove(move));
        }
        
        if (sourcePiece != null && !sourcePiece.equals("")) {
            Assert.assertEquals(translateFromLetter(board.isWhiteTurn(), sourcePiece), MoveParser.getMovingPiece(move));
        }

        System.out.println("move will be: "+ MoveParser.toString(move));
        
        if (checkMove.equals("+")) {
            return makeCheckingMove(move);
        }

        return move;
    }


    private static Piece translateFromLetter (boolean white, String piece){
        if (white) {
            switch (piece) {
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
