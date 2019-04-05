package com.github.louism33.utils;

import com.github.louism33.chesscore.Art;
import com.github.louism33.chesscore.Chessboard;
import com.github.louism33.chesscore.MoveParser;
import org.junit.Assert;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.github.louism33.chesscore.BitOperations.*;
import static com.github.louism33.chesscore.BoardConstants.*;
import static com.github.louism33.chesscore.MoveConstants.*;
import static com.github.louism33.chesscore.MoveParser.buildMove;
import static com.github.louism33.chesscore.PieceMove.*;
import static java.lang.Long.numberOfTrailingZeros;

public final class MoveParserFromAN {

    private static final Pattern pattern = Pattern.compile(".?([abcdefgh][12345678])[-x]?([abcdefgh][12345678])(\\w)?");
    private static final Matcher matcher = pattern.matcher("");

    public static int buildMoveFromLAN(Chessboard board, String an){
        matcher.reset(an);

        if (matcher.find()) {
            String movingPieceStr = matcher.group(1);
            String promotionPiece = matcher.group(3);

            int sourceIndex = 'h' - movingPieceStr.charAt(0) + (movingPieceStr.charAt(1) - '1')*8;
            int destinationIndex = 'h' - matcher.group(2).charAt(0) + (matcher.group(2).charAt(1) - '1')*8;

            int basicMove = buildMove(sourceIndex, board.pieceSquareTable[sourceIndex], destinationIndex, board.pieceSquareTable[destinationIndex]);

            long movingPiece = newPieceOnSquare(sourceIndex);
            long destinationSquare = newPieceOnSquare(destinationIndex);

            int turn = board.turn;
            if (movingPiece == INITIAL_PIECES[turn][KING] && board.pieceSquareTable[sourceIndex] == PIECE[turn][KING]){
                if ((destinationSquare & CASTLE_KING_DESTINATIONS) != 0){
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

    // is lower case b in first group necessary or safe?
    private static final Matcher anMatcher = Pattern.compile("([PNBRQKpnrqk])?([a-h])?([1-8])?([x])?([a-h])([1-8])([=]?)([QNRB]?)([+#]?)").matcher("");
    
    public static int buildMoveFromANWithOO(Chessboard board, String an){
        anMatcher.reset(an);

        if (an.equals("O-O") || an.equals("O-O+")) {
            if (board.turn == WHITE) {
                return buildMoveFromLAN(board, "e1g1");
            } else {
                return buildMoveFromLAN(board, "e8g8");
            }
        }

        if (an.equals("O-O-O") || an.equals("O-O-O+")) {
            if (board.turn == WHITE) {
                return buildMoveFromLAN(board, "e1c1");
            } else {
                return buildMoveFromLAN(board, "e8c8");
            }
        }
        
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

        if (chars[1] != 0 && chars[2] != 0 && chars[4] != 0 && chars[5] != 0) {
            return buildMoveFromLAN(board, an);
        }

        int basicMove;

        int turn = board.turn;
        int movingPiece = PIECE[turn][PAWN];

        if (chars[0] != 0) {
            movingPiece = getSourcePiece(chars[0], turn);
        }

        long movingPieceLong = board.pieces[turn][movingPiece < 7 ? movingPiece : movingPiece - 6];

        if (chars[1] != 0){
            movingPieceLong &= FILES['h' - chars[1]];
        }

        if (chars[2] != 0) {
            movingPieceLong &= RANKS[chars[2] - '1'];
        }

        boolean b4 = chars[4] != 0;
        long destinationFile = 0;
        if (b4) {
            destinationFile = FILES['h' - chars[4]];
        }

        boolean b5 = chars[5] != 0;
        long destinationSquare = 0;
        if (b5) {
            long rank = RANKS[chars[5] - '1'];
            destinationSquare = rank & destinationFile;
        }
        
        int destinationIndex = numberOfTrailingZeros(destinationSquare);

        if (populationCount(destinationSquare) != 1) {
            throw new RuntimeException();
        }

        int sourceIndex;

        if (populationCount(movingPieceLong) != 1) {
            sourceIndex = numberOfTrailingZeros(getMovingPiece(board, board.allPieces(), destinationIndex, movingPieceLong, movingPiece));
        }
        else {
            sourceIndex = numberOfTrailingZeros(movingPieceLong);
        }

        if (movingPiece == 0) {
            movingPiece = board.pieceSquareTable[sourceIndex];
        }
        basicMove = buildMove(sourceIndex, movingPiece, destinationIndex, board.pieceSquareTable[destinationIndex]);



        if (movingPiece == INITIAL_PIECES[turn][KING] && board.pieceSquareTable[sourceIndex] == PIECE[turn][KING]){
            if ((destinationSquare & CASTLE_KING_DESTINATIONS) != 0){
                basicMove |= CASTLING_MASK;
            }
        }

        // if it is a diagonal non-capture by a pawn, it must be EP
        if ((sourceIndex != destinationIndex + 16)
                && (sourceIndex != destinationIndex - 16)
                && (sourceIndex != destinationIndex + 8)
                && (sourceIndex != destinationIndex - 8)) {
            if (board.pieceSquareTable[sourceIndex] == PIECE[turn][PAWN]
                    && board.pieceSquareTable[destinationIndex] == NO_PIECE) {
                basicMove |= ENPASSANT_MASK;
            }
        }
        
        if (chars[3] != 0) {
//            Assert.assertTrue(MoveParser.isCaptureMove(basicMove) || MoveParser.isEnPassantMove(basicMove));
        }
        
        if (chars[7] != 0) {
            basicMove |= PROMOTION_MASK;
            switch (chars[7]){
                case 'n':
                case 'N':
                    basicMove |= KNIGHT_PROMOTION_MASK;
                    break;
                case 'b':
                case 'B':
                    basicMove |= BISHOP_PROMOTION_MASK;
                    break;
                case 'r':
                case 'R':
                    basicMove |= ROOK_PROMOTION_MASK;
                    break;
                case 'q':
                case 'Q':
                    basicMove |= QUEEN_PROMOTION_MASK;
                    break;
            }
        }

        return basicMove;
    }


    public static int buildMoveFromAN(Chessboard board, String an){
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

        if (chars[1] != 0 && chars[2] != 0 && chars[4] != 0 && chars[5] != 0) {
            return buildMoveFromLAN(board, an);
        }

        int basicMove;

        int turn = board.turn;
        int movingPiece = PIECE[turn][PAWN];

        if (chars[0] != 0) {
            movingPiece = getSourcePiece(chars[0], turn);
        }

        long movingPieceLong = board.pieces[turn][movingPiece < 7 ? movingPiece : movingPiece - 6];

        if (chars[1] != 0){
            movingPieceLong &= FILES['h' - chars[1]];
        }

        if (chars[2] != 0) {
            movingPieceLong &= RANKS[chars[2] - '1'];
        }

        boolean b4 = chars[4] != 0;
        long destinationFile = 0;
        if (b4) {
            destinationFile = FILES['h' - chars[4]];
        }

        boolean b5 = chars[5] != 0;
        long destinationSquare = 0;
        if (b5) {
            long rank = RANKS[chars[5] - '1'];
            destinationSquare = rank & destinationFile;
        }

        int destinationIndex = numberOfTrailingZeros(destinationSquare);

        if (populationCount(destinationSquare) != 1) {
            throw new RuntimeException();
        }

        int sourceIndex;

        if (populationCount(movingPieceLong) != 1) {
            sourceIndex = numberOfTrailingZeros(getMovingPiece(board, board.allPieces(), destinationIndex, movingPieceLong, movingPiece));
        }
        else {
            sourceIndex = numberOfTrailingZeros(movingPieceLong);
        }

        if (movingPiece == 0) {
            movingPiece = board.pieceSquareTable[sourceIndex];
        }
        basicMove = buildMove(sourceIndex, movingPiece, destinationIndex, board.pieceSquareTable[destinationIndex]);



        if (movingPiece == INITIAL_PIECES[turn][KING] && board.pieceSquareTable[sourceIndex] == PIECE[turn][KING]){
            if ((destinationSquare & CASTLE_KING_DESTINATIONS) != 0){
                basicMove |= CASTLING_MASK;
            }
        }

        // if it is a diagonal non-capture by a pawn, it must be EP
        if ((sourceIndex != destinationIndex + 16)
                && (sourceIndex != destinationIndex - 16)
                && (sourceIndex != destinationIndex + 8)
                && (sourceIndex != destinationIndex - 8)) {
            if (board.pieceSquareTable[sourceIndex] == PIECE[turn][PAWN]
                    && board.pieceSquareTable[destinationIndex] == NO_PIECE) {
                basicMove |= ENPASSANT_MASK;
            }
        }
        if (chars[3] != 0) {
//            Assert.assertTrue(MoveParser.isCaptureMove(basicMove) || MoveParser.isEnPassantMove(basicMove));
        }
        if (chars[7] != 0) {
            basicMove |= PROMOTION_MASK;
            switch (chars[7]){
                case 'n':
                case 'N':
                    basicMove |= KNIGHT_PROMOTION_MASK;
                    break;
                case 'b':
                case 'B':
                    basicMove |= BISHOP_PROMOTION_MASK;
                    break;
                case 'r':
                case 'R':
                    basicMove |= ROOK_PROMOTION_MASK;
                    break;
                case 'q':
                case 'Q':
                    basicMove |= QUEEN_PROMOTION_MASK;
                    break;
            }
        }

        return basicMove;
    }

    private static long getMovingPiece(Chessboard board, long allPieces, int destinationIndex, long candidateMovers, int movingPieceType){
        switch (movingPieceType) {
            case NO_PIECE:
                throw new RuntimeException();
            case WHITE_PAWN:
                candidateMovers = pawnFinder(allPieces, candidateMovers, WHITE, destinationIndex);
                break;
            case BLACK_PAWN:
                candidateMovers = pawnFinder(allPieces, candidateMovers, BLACK, destinationIndex);
                break;
            case WHITE_KNIGHT:
            case BLACK_KNIGHT:
                candidateMovers &= KNIGHT_MOVE_TABLE[destinationIndex];
                break;
            case WHITE_BISHOP:
            case BLACK_BISHOP:
                candidateMovers = singleBishopTable(allPieces, destinationIndex, candidateMovers);
                break;
            case WHITE_ROOK:
            case BLACK_ROOK:
                candidateMovers = singleRookTable(allPieces, destinationIndex, candidateMovers);
                break;
            case WHITE_QUEEN:
            case BLACK_QUEEN:
                candidateMovers = singleQueenTable(allPieces, destinationIndex, candidateMovers);
                break;
            case WHITE_KING:
            case BLACK_KING:
                candidateMovers &= KING_MOVE_TABLE[destinationIndex];
                break;
        }

        if (populationCount(candidateMovers) != 1) {
            board.generateLegalMoves();
            
            long candidateMoversWithoutPins = ~board.pinnedPieces & candidateMovers;
            if (populationCount(candidateMoversWithoutPins) != 1) {
                throw new RuntimeException();
            }
            return candidateMoversWithoutPins;
        }

        return candidateMovers;
    }
    
    
    private static long pawnFinder(long allPieces, long myPawns, int turn, int destinationIndex){
        long destinationSquare = newPieceOnSquare(destinationIndex);

        while (myPawns != 0){
            final long pawn = getFirstPiece(myPawns);
            
            long quietTable;
            // doubles
            if ((pawn & PENULTIMATE_RANKS[1 - turn]) != 0) {
                quietTable = singlePawnPushes(pawn, turn, destinationSquare, allPieces);
            }
            else {
                quietTable = (turn == WHITE ? pawn << 8 : pawn >>> 8) & ~allPieces & destinationSquare;
            }

            quietTable &= ~allPieces;
            quietTable &= destinationSquare;

            if (quietTable != 0) {
                return pawn;
            }

            long captureTable = singlePawnCaptures(pawn, turn, destinationSquare) & allPieces;

            captureTable &= destinationSquare;

            if (captureTable != 0) {
                return pawn;
            }
            
            myPawns &= myPawns - 1;
        }

        throw new RuntimeException("couldn't find moving pawn");
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
}
