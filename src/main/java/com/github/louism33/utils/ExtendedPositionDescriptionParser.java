package com.github.louism33.utils;

import com.github.louism33.chesscore.Chessboard;
import com.github.louism33.chesscore.MoveParser;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class ExtendedPositionDescriptionParser {

    private static final Pattern boardPattern = Pattern.compile("([/|\\w]* [wb] [-|\\w]* [-|\\w])");
    private static final Pattern bestMovePattern = Pattern.compile("bm ([\\w\\s+]+)");
    private static final Pattern avoidMovePattern = Pattern.compile("am ([\\w|+]+)");
    private static final Pattern idPattern = Pattern.compile("id \"(\\S*)\"");
    private static final Pattern commentPattern = Pattern.compile(" c0 \"(.*)\";");
    private static final Matcher boardMatcher = boardPattern.matcher("");
    private static final Matcher bmMatcher = bestMovePattern.matcher("");
    private static final Matcher amMatcher = avoidMovePattern.matcher("");
    private static final Matcher idMatcher = idPattern.matcher("");
    private static final Matcher commentMatcher = commentPattern.matcher("");

    public static void main(String[] args) {
        String e = "1kr5/3n4/q3p2p/p2n2p1/PppB1P2/5BP1/1P2Q2P/3R2K1 w - - bm f5; id \"Undermine.001\"; c0 \"f5=10, Be5+=2, Bf2=3, Bg4=2\";";

        final EPDObject epdObject = parseEDPPosition(e);
        System.out.println(epdObject);
    }
    
    public static EPDObject parseEDPPosition(String edpPosition){
        System.out.println(edpPosition);
        
        boardMatcher.reset(edpPosition);
        bmMatcher.reset(edpPosition);
        amMatcher.reset(edpPosition);
        idMatcher.reset(edpPosition);
        commentMatcher.reset(edpPosition);

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
                goodMoves[i] = (MoveParserFromAN.buildMoveFromAN(board, bms[i]));
            }
            goodMoves[goodMoves.length - 1] += length;
        }

        int[] badMoves = new int[8];
        if (amMatcher.find()) {
            String[] ams = amMatcher.group(1).split(" ");
            int length = ams.length;
            for (int i = 0; i < length; i++) {
                badMoves[i] = (MoveParserFromAN.buildMoveFromAN(board, ams[i]));
            }
            badMoves[badMoves.length - 1] += length;
        }

        String id = "";
        if (idMatcher.find()) {
            id = idMatcher.group(1);
        }
        
        ScoredMoves scoredMoves = null;
        if (commentMatcher.find()) {
            System.out.println(commentMatcher.group());
            scoredMoves = ScoredMoves.parseComment(commentMatcher.group(1));
        }

        return new EPDObject(board, goodMoves, id, fen, badMoves, edpPosition, scoredMoves);
    }

    public static class EPDObject {
        private final Chessboard board;
        private final int[] bestMoves;
        private final int[] avoidMoves;
        private final String id;
        private final String boardFen;
        private final String fullString;
        private final ScoredMoves scoredMoves;

        EPDObject(Chessboard board, int[] bestMoves, String id,
                  String boardFen, int[] avoidMoves, String fullString, ScoredMoves scoredMoves) {
            this.board = board;
            this.bestMoves = bestMoves;
            this.id = id;
            this.boardFen = boardFen;
            this.avoidMoves = avoidMoves;
            this.fullString = fullString;
            this.scoredMoves = scoredMoves;
        }

        public Chessboard getBoard() {
            return board;
        }

        public String getBestPrettyMoves() {
            return Arrays.toString(MoveParser.toString(bestMoves));
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

        public String getFullString() {
            return fullString;
        }

        @Override
        public String toString() {
            return "EPDObject{" +
                    '\n' +fullString+
                    (id.length() > 0 ? "\n     id='" + id + '\'' : "") +
                    (this.scoredMoves == null ? "" : "\n" + this.scoredMoves) +
                    "\n     boardFen='" + boardFen + '\'' +
                    (bestMoves[bestMoves.length - 1] > 0 ? "\n     bestMoves=" + Arrays.toString(MoveParser.toString(bestMoves)) : "") +
                    (avoidMoves[avoidMoves.length - 1] > 0 ? "\n     avoidMoves =" + Arrays.toString(MoveParser.toString(avoidMoves)) : "") +
                    '}';
        }
    }
    
}
