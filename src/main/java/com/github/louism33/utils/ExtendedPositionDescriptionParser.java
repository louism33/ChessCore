package com.github.louism33.utils;

import com.github.louism33.chesscore.Chessboard;
import com.github.louism33.chesscore.MoveParser;
import com.github.louism33.chesscore.MoveParserFromAN;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExtendedPositionDescriptionParser {

    private static Pattern boardPattern = Pattern.compile("([/|\\w]* [wb] [-|\\w]* [-|\\w])");
    private static Pattern bestMovePattern = Pattern.compile("bm ([\\w\\s+]+)");
    private static Pattern avoidMovePattern = Pattern.compile("am ([\\w|+]+)");
    private static Pattern idPattern = Pattern.compile("id \"(\\S*)\"");
    private static Matcher boardMatcher = boardPattern.matcher("");
    private static Matcher bmMatcher = bestMovePattern.matcher("");
    private static Matcher amMatcher = avoidMovePattern.matcher("");
    private static Matcher idMatcher = idPattern.matcher("");

    public static EPDObject parseEDPPosition(String edpPosition){
        boardMatcher.reset(edpPosition);
        bmMatcher.reset(edpPosition);
        amMatcher.reset(edpPosition);
        idMatcher.reset(edpPosition);

        Chessboard board = new Chessboard();
        String fen = "";
        if (boardMatcher.find()) {
            fen = boardMatcher.group(1);
            board = new Chessboard(fen);
        }

        int[] goodMoves = new int[8];
        if (bmMatcher.find()) {
            String[] bms = bmMatcher.group(1).split(" ");
            int length = bms.length;
            for (int i = 0; i < length; i++) {
                goodMoves[i] = (MoveParserFromAN.buildMoveFromLAN(board, bms[i]));
            }
            goodMoves[goodMoves.length - 1] += length;
        }

        int[] badMoves = new int[8];
        if (amMatcher.find()) {
            String[] ams = amMatcher.group(1).split(" ");
            int length = ams.length;
            for (int i = 0; i < length; i++) {
                badMoves[i] = (MoveParserFromAN.buildMoveFromLAN(board, ams[i]));
            }
            badMoves[badMoves.length - 1] += length;
        }

        String id = "";
        if (idMatcher.find()) {
            id = idMatcher.group(1);
        }

        return new EPDObject(board, goodMoves, id, fen, badMoves);
    }

    public static class EPDObject {
        private final Chessboard board;
        private final int[] bestMoves;
        private final int[] avoidMoves;
        private final String id;
        private final String boardFen;

        EPDObject(Chessboard board, int[] bestMoves, String id,
                  String boardFen, int[] avoidMoves) {
            this.board = board;
            this.bestMoves = bestMoves;
            this.id = id;
            this.boardFen = boardFen;
            this.avoidMoves = avoidMoves;
        }

        public Chessboard getBoard() {
            return board;
        }

        public int[] getBestMoves() {
            return bestMoves;
        }

        public int[] getAvoidMoves() {
            return avoidMoves;
        }

        public String getId() {
            return id;
        }

        public String getBoardFen() {
            return boardFen;
        }

        @Override
        public String toString() {
            return "EPDObject{" +
                    (id.length() > 0 ? "\n     id='" + id + '\'' : "") +
                    "\n     boardFen='" + boardFen + '\'' +
                    (bestMoves[bestMoves.length - 1] > 0 ? "\n     bestMoves=" + Arrays.toString(MoveParser.toString(bestMoves)) : "") +
                    (avoidMoves[avoidMoves.length - 1] > 0 ? "\n     avoidMoves =" + Arrays.toString(MoveParser.toString(avoidMoves)) : "") +
                    '}';
        }
    }

}
