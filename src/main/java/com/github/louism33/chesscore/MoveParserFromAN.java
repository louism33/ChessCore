package com.github.louism33.chesscore;

import org.junit.Assert;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.github.louism33.chesscore.BoardConstants.*;
import static com.github.louism33.chesscore.MoveConstants.*;
import static com.github.louism33.chesscore.MoveGeneratorSpecial.extractFileFromStack;
import static com.github.louism33.utils.Parser.moveFromSourceDestinationSquareCaptureSecure;
import static com.github.louism33.chesscore.StackDataUtil.ENPASSANTVICTIM;

public class MoveParserFromAN {

//    public static void main (String[] args){
//        Chessboard board = new Chessboard("r3k2r/8/8/8/2pP4/8/B7/R3K2R b KQkq d3 5 3");
//        System.out.println(board);
//
//        int[] moves = board.generateLegalMoves();
//        MoveParser.printMoves(moves);
//
//        String an = "e8g8";
//        int move = buildMoveFromAN(board, an);
//        String s = MoveParser.toString(move);
//        System.out.println("-----> "+ s);
//
//        System.out.println("MP : " + MoveParser.isCastlingMove(move));
//        board.makeMoveAndFlipTurn(move);
//        System.out.println(board);
//
//    }

    private static final String boardPattern =
            "([(PNBRQKpnrqk)|a-h]?)" +
                    "([a-h])?" +
                    "(x?)" +
                    "([a-h][1-8])?" +
                    "(x?)" +
                    "([a-zA-Z][1-8])" +
                    "(\\+)?" +
                    "([nbrqNBRQ])?" +
                    "";

    public static int buildMoveFromAN(Chessboard board, String an){
        Pattern r = Pattern.compile(boardPattern);
        Matcher m = r.matcher(an);

        String sourcePiece = "";
        String capture1 = "";
        String possibleSourceFile = "";
        String sourceSquareString = "";
        String capture2 = "";
        String destinationString = "";

        String checkMove = "";
        String promotionPiece = "";

        if (m.find()){
            sourcePiece = m.group(1);
            capture1 = m.group(3);
            possibleSourceFile = m.group(2);
            sourceSquareString = m.group(4);
            capture2 = m.group(5);
            destinationString = m.group(6);
            checkMove = m.group(7);
            promotionPiece = m.group(8);
        }

//        System.out.println(sourcePiece);
//        System.out.println(possibleSourceFile);
//        System.out.println(capture1);
//        System.out.println(sourceSquareString);
//        System.out.println(capture2);
//        System.out.println(destinationString);
//        System.out.println(checkMove);
//        System.out.println("prom piece: " + promotionPiece);

        Square sourceSquare = null;
        if (sourceSquareString != null){
            sourceSquare = Square.valueOf(sourceSquareString.toUpperCase());
        }

        Square destinationSquare = Square.valueOf(destinationString.toUpperCase());

//        System.out.println(destinationSquare.ordinal());
        // ep ?
        boolean isCapture = (destinationSquare.toBitboard() & board.allPieces()) != 0;

        Piece movingPiece = null;
        if (sourcePiece != null && !sourcePiece.equals("")) {
            movingPiece = translateFromLetter(board.isWhiteTurn(), sourcePiece);
        }

        long optionalSourceFile = UNIVERSE;

        if (possibleSourceFile != null && !possibleSourceFile.equals("")){
            optionalSourceFile = FILES[7 - (possibleSourceFile.charAt(0) - 'a')];
        }

        int move = moveFromSourceDestinationSquareCaptureSecure(board, movingPiece, optionalSourceFile, sourceSquare, destinationSquare,
                isCapture);

        if (isEP(board, sourceSquare, destinationSquare, movingPiece, isCapture)){
            move |= ENPASSANT_MASK;
        }

        if (sourceSquare != null){
            if (isCastle(board, sourceSquare, destinationSquare, movingPiece)){
                move |= CASTLING_MASK;
            }
        }

        if (promotionPiece != null){
            Assert.assertTrue(destinationSquare.ordinal() < 8 || destinationSquare.ordinal() > 55);
            move |= PROMOTION_MASK;
            switch (promotionPiece){
                case "n":
                case "N":
                    move |= KNIGHT_PROMOTION_MASK;
                    break;
                case "b":
                case "B":
                    move |= BISHOP_PROMOTION_MASK;
                    break;
                case "r":
                case "R":
                    move |= ROOK_PROMOTION_MASK;
                    break;
                case "q":
                case "Q":
                    move |= QUEEN_PROMOTION_MASK;
                    break;
            }
        }

        if ((capture1 != null && capture1.equals("x")) || capture2 != null && capture2.equals("x")){
            Assert.assertTrue(isCapture);
            Assert.assertTrue(MoveParser.isCaptureMove(move));
        }

        if (sourcePiece != null && !sourcePiece.equals("")) {
            Assert.assertEquals(translateFromLetter(board.isWhiteTurn(), sourcePiece), MoveParser.getMovingPiece(move));
        }

        if (checkMove != null && checkMove.equals("+")) {
//            return makeCheckingMove(move);
        }

        return move;
    }

    private static boolean isCastle(Chessboard board, Square sourceSquare, Square destinationSquare, Piece movingPiece){
        if (sourceSquare == Square.E1
                && ((sourceSquare.toBitboard() & board.pieces[WHITE][KING]) != 0)){
            if (destinationSquare == Square.G1 || destinationSquare == Square.C1){
                return true;
            }
        }
        if (sourceSquare == Square.E8
                && ((sourceSquare.toBitboard() & board.pieces[BLACK][KING]) != 0)){
            if (destinationSquare == Square.G8 || destinationSquare == Square.C8){
                return true;
            }
        }
        return false;
    }

    private static boolean isEP (Chessboard board, Square sourceSquare, Square destinationSquare, Piece movingPiece, boolean isCapture){
        if ((destinationSquare.ordinal() >= 16 && destinationSquare.ordinal() <= 23)
                || (destinationSquare.ordinal() >= 40 && destinationSquare.ordinal() <= 47)){

            if (isCapture){
                return false;
            }

            if (!board.hasPreviousMove()){
                return false;
            }

            long previousMove = board.moveStackArrayPeek();

            if (StackDataUtil.getSpecialMove(previousMove) != ENPASSANTVICTIM){
                return false;
            }

            long FILE = extractFileFromStack(StackDataUtil.getEPMove(previousMove));

            if (FILES[destinationSquare.getFileNumber()] != FILE){
                return false;
            }

            if ((sourceSquare.toBitboard() & (board.pieces[WHITE][PAWN] | board.pieces[BLACK][PAWN])) == 0){
                return false;
            }

            int ordinalS = sourceSquare.ordinal();
            int ordinalD = destinationSquare.ordinal();
            if ((ordinalS != ordinalD + 7)
                    && (ordinalS != ordinalD + 9)
                    && (ordinalS != ordinalD - 7)
                    && (ordinalS != ordinalD - 9)){
                return false;
            }

            return true;
        }

        return false;
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
                case "h":
                case "P":
                    return Piece.WHITE_PAWN;
                case "N":
                    return Piece.WHITE_KNIGHT;
                case "B":
                    return Piece.WHITE_BISHOP;
                case "R":
                    return Piece.WHITE_ROOK;
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
                case "h":
                case "P":
                case "p":
                    return Piece.BLACK_PAWN;
                case "N":
                case "n":
                    return Piece.BLACK_KNIGHT;
                case "B":
                    return Piece.BLACK_BISHOP;
                case "R":
                case "r":
                    return Piece.BLACK_ROOK;
                case "Q":
                case "q":
                    return Piece.BLACK_QUEEN;
                case "K":
                case "k":
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
