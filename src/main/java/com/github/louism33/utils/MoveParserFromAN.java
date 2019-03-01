package com.github.louism33.utils;

import com.github.louism33.chesscore.Chessboard;
import com.github.louism33.chesscore.MoveParser;
import org.junit.Assert;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.github.louism33.chesscore.BitOperations.getIndexOfFirstPiece;
import static com.github.louism33.chesscore.BitOperations.newPieceOnSquare;
import static com.github.louism33.chesscore.BoardConstants.*;
import static com.github.louism33.chesscore.MoveConstants.*;
import static com.github.louism33.chesscore.MoveParser.buildMove;

public class MoveParserFromAN {

    private static Pattern pattern = Pattern.compile(".?([abcdefgh][12345678])[-x]?([abcdefgh][12345678])(\\w)?");
    private static Matcher matcher = pattern.matcher("");

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

    private static Matcher anMatcher = Pattern.compile("([RBQKPN])?([a-h])?([1-8])?([x])?([a-h])([1-8])([=]?)([QNRB]?)([+#]?)").matcher("");
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

        int sourceIndex = getIndexOfFirstPiece(movingPieceLong);
        
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

        int destinationIndex = getIndexOfFirstPiece(destinationSquare);

        if (movingPiece == 0) {
            movingPiece = board.pieceSquareTable[sourceIndex];
        }
        basicMove = buildMove(sourceIndex, movingPiece, destinationIndex, board.pieceSquareTable[destinationIndex]);

        Assert.assertEquals(chars[3] != 0, MoveParser.isCaptureMove(basicMove));

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
