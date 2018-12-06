package com.github.louism33.chesscore;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MoveParserFromAN {

    public static int destinationIndex(Chessboard board, String algebraicNotation){
        return rankAndFile(board, algebraicNotation);
    }
    
    private static int makeMoveBasedOnAlgNotation(Chessboard board, String algebraicNotation){
        System.out.println(algebraicNotation);
        System.out.println();

        long whichPieceCouldBeMoving = whichPieceIsMoving(board, algebraicNotation);
        int x = rankAndFile(board, algebraicNotation);
        
        long destinationSquare = BitOperations.newPieceOnSquare(x);

        Piece piece = extractRealPieceFromLong(board, whichPieceCouldBeMoving, destinationSquare);

//        findOriginalPiece(piece, destinationSquare);

        Art.printLong(whichPieceCouldBeMoving);
        Art.printLong(destinationSquare);

        return 0;
    }
    
    
    private static Piece extractRealPieceFromLong(Chessboard board, long whichPieceCouldBeMoving, long destinationSquare){
        if ((whichPieceCouldBeMoving & board.getWhitePawns()) != 0){
            return Piece.WHITE_PAWN;
        }
        else if ((whichPieceCouldBeMoving & board.getWhiteKnights()) != 0){
            return Piece.WHITE_KNIGHT;
        }
        else if ((whichPieceCouldBeMoving & board.getWhiteBishops()) != 0){
            return Piece.WHITE_BISHOP;
        }
        else if ((whichPieceCouldBeMoving & board.getWhiteRooks()) != 0){
            return Piece.WHITE_ROOK;
        }
        else if ((whichPieceCouldBeMoving & board.getWhiteQueen()) != 0){
            return Piece.WHITE_QUEEN;
        }
        else if ((whichPieceCouldBeMoving & board.getWhiteKing()) != 0){
            return Piece.WHITE_KING;
        }

        else if ((whichPieceCouldBeMoving & board.getBlackPawns()) != 0){
            return Piece.BLACK_PAWN;
        }
        else if ((whichPieceCouldBeMoving & board.getBlackKnights()) != 0){
            return Piece.BLACK_KNIGHT;
        }
        else if ((whichPieceCouldBeMoving & board.getBlackBishops()) != 0){
            return Piece.BLACK_BISHOP;
        }
        else if ((whichPieceCouldBeMoving & board.getBlackRooks()) != 0){
            return Piece.BLACK_ROOK;
        }
        else if ((whichPieceCouldBeMoving & board.getBlackQueen()) != 0){
            return Piece.BLACK_QUEEN;
        }
        else if ((whichPieceCouldBeMoving & board.getBlackKing()) != 0){
            return Piece.BLACK_KING;
        }
        else {
            throw new RuntimeException("Could not retrieve Piece");
        }
    }

    private static long whichPieceIsMoving(Chessboard board, String algebraicNotation){
        String boardPattern = "[p|n|b|r|q|k|P|N|B|R|Q|K]?";
        Pattern r = Pattern.compile(boardPattern);
        Matcher m = r.matcher(algebraicNotation);

        String pieceFromAN = "";

        if (m.find()){
            pieceFromAN = m.group();
        }
        if (pieceFromAN.length() == 0){
            throw new RuntimeException("Could not parse Piece");
        }

        System.out.println(pieceFromAN);
        
        switch (pieceFromAN) {
            case "p": {
                return board.getBlackPawns();
            }
            case "n": {
                return board.getBlackKnights();
            }
            case "b": {
                return board.getBlackBishops();
            }
            case "r": {
                return board.getBlackRooks();
            }
            case "q": {
                return board.getBlackQueen();
            }
            case "k": {
                return board.getBlackKing();
            }

            case "P": {
                return board.getWhitePawns();
            }
            case "N": {
                return board.getWhiteKnights();
            }
            case "B": {
                return board.getWhiteBishops();
            }
            case "R": {
                return board.getWhiteRooks();
            }
            case "Q": {
                return board.getWhiteQueen();
            }
            case "K": {
                return board.getWhiteKing();
            }
            default:
                System.out.println("problem with Piece identifier in which piece in moving()");
                return 0;
        }
    }
    
    private static int rankAndFile(Chessboard board, String algebraicNotation){
        int f = whichDestinationFile(board, algebraicNotation);
        int r = whichDestinationRank(board, algebraicNotation);
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
                System.out.println("problem with Getting destinationIndex file");
                return 0;
        }
    }
    
    
}