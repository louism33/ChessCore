package com.github.louism33.chesscore;

import com.github.louism33.utils.ExtendedPositionDescriptionParser;
import com.github.louism33.utils.Square;
import org.junit.Assert;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.github.louism33.chesscore.BitOperations.*;
import static com.github.louism33.chesscore.BoardConstants.*;
import static com.github.louism33.chesscore.MoveConstants.*;
import static com.github.louism33.chesscore.MoveGeneratorSpecial.extractFileFromStack;
import static com.github.louism33.chesscore.MoveParser.buildMove;
import static com.github.louism33.chesscore.StackDataUtil.ENPASSANTVICTIM;
import static com.github.louism33.utils.Parser.moveFromSourceDestinationSquareCaptureSecure;

public class MoveParserFromAN {

    private static Pattern pattern = Pattern.compile(".?([abcdefgh][12345678])[-x]([abcdefgh][12345678])(\\w)?");
    private static Matcher matcher = pattern.matcher("");

    public static int buildMoveFromLAN(Chessboard board, String an){
        matcher.reset(an);

        if (matcher.find() && false) {
            String movingPieceStr = matcher.group(1);
            String promotionPiece = matcher.group(3);

            int sourceIndex = 'h' - movingPieceStr.charAt(0) + (movingPieceStr.charAt(1) - '1')*8;
            int destinationIndex = 'h' - matcher.group(2).charAt(0) + (matcher.group(2).charAt(1) - '1')*8;

            int basicMove = buildMove(sourceIndex, board.pieceSquareTable[sourceIndex], destinationIndex, board.pieceSquareTable[destinationIndex]);

            long movingPiece = newPieceOnSquare(sourceIndex);
            long destinationSquare = newPieceOnSquare(destinationIndex);

            int turn = board.turn;
            if (movingPiece == INITIAL_PIECES[turn][KING] && board.pieceSquareTable[sourceIndex] == PIECE[turn][KING]){
                if ((destinationSquare & INITIAL_PIECES[turn][ROOK]) != 0){
                    basicMove |= CASTLING_MASK;
                }
            }

            // if it is a diagonal non-capture by a pawn, it must be EP
            if (matcher.group(1).charAt(0) != matcher.group(2).charAt(0)) {
                if (board.pieceSquareTable[sourceIndex] == PIECE[turn][PAWN]
                        && board.pieceSquareTable[destinationIndex] == NO_PIECE) {
                    basicMove |= ENPASSANT_MASK;
                }
            }

            if (promotionPiece != null) {
                basicMove |= PROMOTION_MASK;
                switch (promotionPiece){
                    case "n":
                    case "N":
                        basicMove |= KNIGHT_PROMOTION_MASK;
                        break;
                    case "b":
                    case "B":
                        basicMove |= BISHOP_PROMOTION_MASK;
                        break;
                    case "r":
                    case "R":
                        basicMove |= ROOK_PROMOTION_MASK;
                        break;
                    case "q":
                    case "Q":
                        basicMove |= QUEEN_PROMOTION_MASK;
                        break;
                }
            }

            return basicMove;
        }

        return buildMoveFromAN(board, an);
    }


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

    public static void main(String[] args){
//        String e = "2rr3k/pp3pp1/1nnqbN1p/3pN3/2pP4/2P3Q1/PPB4P/R4RK1 w - - bm Qg6; id \"WAC.001\";\n";
        String e = "2rr3k/pp3pp1/1nnqbN1p/3pN3/2pP4/2P3Q1/PPB4P/R4RK1 w - - bm Qh8; id \"WAC.001\";\n";



        ExtendedPositionDescriptionParser.EPDObject EPDObject = ExtendedPositionDescriptionParser.parseEDPPosition(e);

    }

    private static Matcher anMatcher = Pattern.compile("([RBQKPN])?([a-h])?([1-8])?([x])?([a-h])([1-8])([=]?)([QNRB]?)([+#]?)").matcher("");

    private static int buildMoveFromAN(Chessboard board, String an){
        anMatcher.reset(an);
        char[] chars = new char[9];
        if (anMatcher.find()) {
            int groupCount = anMatcher.groupCount();

            for (int i = 0; i < groupCount; i++) {
                String entry = anMatcher.group(i + 1);
                if (entry != null && entry.length() != 0) {
                    chars[i] = entry.charAt(0);
                }
            }
        }

        System.out.println("================================");
        System.out.println(an);
        System.out.println(chars);
        
        int basicMove;

        int turn = board.turn;
        int sourcePieceType = chars[0] != 0 ? getSourcePiece(chars[0], turn) : PIECE[turn][PAWN];

        boolean b1 = chars[1] != 0;
        
        long movingPieceLong = board.pieces[turn][sourcePieceType < 7 ? sourcePieceType : sourcePieceType - 6];
         if (b1){
             movingPieceLong &= FILES['h' - chars[1]];
         }

        int indexOfMovingPiece = getIndexOfFirstPiece(movingPieceLong);
         
        boolean b2 = chars[2] != 0;
        if (b2) {
            System.out.println(chars[2]);
            System.out.println(chars[2]);
        }


        boolean b3 = chars[3] != 0;
        boolean capture = b3;



        boolean b4 = chars[4] != 0;
        long destinationFile = 0;
        if (b4) {
            destinationFile = FILES['h' - chars[4]];
        }

        boolean b5 = chars[5] != 0;
        long destinationSquare = 0;
        if (b5) {
            long rank = RANKS[chars[5] - '1']; // * 8;
            destinationSquare = rank & destinationFile;
        }

        boolean b6 = chars[6] != 0;
        if (b6) {
//            System.out.println(chars[6]);
        }

        boolean b7 = chars[7] != 0;
        if (b7) {
//            System.out.println(chars[7]);
        }

        boolean b8 = chars[8] != 0;
        if (b8) {
//            System.out.println(chars[8]);
        }

        int indexOfVictimPiece = getIndexOfFirstPiece(destinationSquare);


        if (sourcePieceType == 0) {
            sourcePieceType = board.pieceSquareTable[indexOfMovingPiece];
        }
//        System.out.println(indexOfMovingPiece + " " + sourcePieceType + " " + indexOfVictimPiece + " " + board.pieceSquareTable[indexOfVictimPiece]);


        basicMove = buildMove(indexOfMovingPiece, sourcePieceType, indexOfVictimPiece, board.pieceSquareTable[indexOfVictimPiece]);
        String s = MoveParser.toString(basicMove);
        System.out.println(s);

//        String sourcePiece = "";
//        String capture1 = "";
//        String possibleSourceFile = "";
//        String sourceSquareString = "";
//        String capture2 = "";
//        String destinationString = "";
//
//        String checkMove = "";
//        String promotionPiece = "";
//
//        if (anMatcher.find()){
//            sourcePiece = anMatcher.group(1);
//            capture1 = anMatcher.group(3);
//            possibleSourceFile = anMatcher.group(2);
//            sourceSquareString = anMatcher.group(4);
//            capture2 = anMatcher.group(5);
//            destinationString = anMatcher.group(6);
//            checkMove = anMatcher.group(7);
//            promotionPiece = anMatcher.group(8);
//        }
//
//
//        Square sourceSquare = null;
//        if (sourceSquareString != null){
//            sourceSquare = Square.valueOf(sourceSquareString.toUpperCase());
//        }
//
//        Square destinationSquare = Square.valueOf(destinationString.toUpperCase());
//
//        boolean isCapture = (destinationSquare.toBitboard() & board.allPieces()) != 0;
//
//        Piece movingPiece = null;
//        if (sourcePiece != null && !sourcePiece.equals("")) {
//            movingPiece = translateFromLetter(board.isWhiteTurn(), sourcePiece);
//        }
//
//        long optionalSourceFile = UNIVERSE;
//
//        if (possibleSourceFile != null && !possibleSourceFile.equals("")){
//            optionalSourceFile = FILES[7 - (possibleSourceFile.charAt(0) - 'a')];
//        }
//
//        int move = moveFromSourceDestinationSquareCaptureSecure(board, movingPiece, optionalSourceFile, sourceSquare, destinationSquare,
//                isCapture);
//
//        if (isEP(board, sourceSquare, destinationSquare, movingPiece, isCapture)){
//            move |= ENPASSANT_MASK;
//        }
//
//        if (sourceSquare != null){
//            if (isCastle(board, sourceSquare, destinationSquare, movingPiece)){
//                move |= CASTLING_MASK;
//            }
//        }
//
//        if (promotionPiece != null){
//            Assert.assertTrue(destinationSquare.ordinal() < 8 || destinationSquare.ordinal() > 55);
//            move |= PROMOTION_MASK;
//            switch (promotionPiece){
//                case "n":
//                case "N":
//                    move |= KNIGHT_PROMOTION_MASK;
//                    break;
//                case "b":
//                case "B":
//                    move |= BISHOP_PROMOTION_MASK;
//                    break;
//                case "r":
//                case "R":
//                    move |= ROOK_PROMOTION_MASK;
//                    break;
//                case "q":
//                case "Q":
//                    move |= QUEEN_PROMOTION_MASK;
//                    break;
//            }
//        }
//
//        if ((capture1 != null && capture1.equals("x")) || capture2 != null && capture2.equals("x")){
//            Assert.assertTrue(isCapture);
//            Assert.assertTrue(MoveParser.isCaptureMove(move));
//        }
//
//        if (sourcePiece != null && !sourcePiece.equals("")) {
//            Assert.assertEquals(translateFromLetter(board.isWhiteTurn(), sourcePiece), MoveParser.getMovingPiece(move));
//        }

        return basicMove;
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

    private static int getSourcePiece(char c, int turn) {
        switch (c) {
            case 'P':
            case 'p':
                return PIECE[turn][PAWN];
            case 'N':
            case 'n':
                return PIECE[turn][KNIGHT];
            case 'B':
            case 'b':
                return PIECE[turn][BISHOP];
            case 'R':
            case 'r':
                return PIECE[turn][ROOK];
            case 'Q':
            case 'q':
                return PIECE[turn][QUEEN];
            case 'K':
            case 'k':
                return PIECE[turn][KING];
            default:
                return NO_PIECE;
        }
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
        int r = whichDestinationRank(square);
        return (r-1) * 8 + f;
    }
    private static int whichDestinationRank(String algebraicNotation){
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


    public enum Piece {
        NO_PIECE,

        WHITE_PAWN,
        WHITE_KNIGHT,
        WHITE_BISHOP,
        WHITE_ROOK,
        WHITE_QUEEN,
        WHITE_KING,

        BLACK_PAWN,
        BLACK_KNIGHT,
        BLACK_BISHOP,
        BLACK_ROOK,
        BLACK_QUEEN,
        BLACK_KING;

    }
}
