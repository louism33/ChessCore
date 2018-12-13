package com.github.louism33.chesscore;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExtendedPositionDescriptionParser {
    
    private static final String pattern = "" +
            "([/|\\w]* )" +
            "(bm ([\\w| |+]*);)" +
            "(am ([\\w| |+]*);)?" +
            "(id \\\"(\\w*[\\.+| +\\w]*)\\\")" +
            "";
    
    public static EPDObject parseEDPPosition(String edpPosition){
        
        String id = extractIDString(edpPosition);
        
        String[] bms = extractBestMoves(edpPosition);

        Chessboard chessboard = new Chessboard(edpPosition);
        
        String boardFen = extractBoardFen(edpPosition);

        List<Integer> goodDestinations = new ArrayList<>();
        for (int i = 0; i < bms.length; i++) {
            String bm = bms[i];
            goodDestinations.add(MoveParserFromAN.buildMoveFromAN(chessboard, bm));
        }

        String[] ams = extractAvoidMoves(edpPosition);
        List<Integer> badDestinations = new ArrayList<>();
        for (int i = 0; i < ams.length; i++) {
            String am = ams[i];
            badDestinations.add(MoveParserFromAN.buildMoveFromAN(chessboard, am));
        }

        return new EPDObject(chessboard, goodDestinations, id, boardFen, badDestinations);
    }

    private static String extractBoardFen(String edpPosition){
        String pattern = "[/|\\w]* ";
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(edpPosition);

        String ans = "";

        if (m.find()){
            ans = m.group();
        }
        return ans;
    }

    private static String[] extractBestMoves(String edpPosition){
        String pattern = "bm ([\\w| |+]*);";
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(edpPosition);

        String ans = "";

        if (m.find()){
            ans = m.group(1);
        }

        return ans.split(" ");
    }

    private static String[] extractAvoidMoves(String edpPosition){
        String bool = "am .*";
        Pattern p1 = Pattern.compile(bool);
        Matcher m1 = p1.matcher(edpPosition);
        
        
        if (!m1.find()){
            return new String[0];
        }

        
        String pattern = "am ([\\w| |+]*);";
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(edpPosition);

        String ans = "";

        if (m.find()){
            ans = m.group(1);
        }

        return ans.split(" ");
    }

    private static String extractBestMove(String edpPosition){
        String pattern = "bm (\\w*)";
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(edpPosition);

        String ans = "";
        
        if (m.find()){
            ans = m.group(1);
        }
        return ans;
    }
    
    private static String extractIDString(String edpPosition){
        String pattern = "id \"(\\w*[.+| +\\w]*)\"";
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(edpPosition);

        String ans = "";

        if (m.find()){
            ans = m.group(1);
        }
        return ans;
    }
    
    public static class EPDObject {
        private final Chessboard board;
        private final List<Integer> bestMoves;
        private final List<Integer> avoidMoves;
        private final String id;
        private final String boardFen;

        EPDObject(Chessboard board, List<Integer> bestMoves, String id,
                  String boardFen, List<Integer> avoidMoves) {
            this.board = board;
            this.bestMoves = bestMoves;
            this.id = id;
            this.boardFen = boardFen;
            this.avoidMoves = avoidMoves;
        }

        public Chessboard getBoard() {
            return board;
        }

        public List<Integer> getBestMoves() {
            return bestMoves;
        }

        public String getId() {
            return id;
        }

        public List<Integer> getAvoidMoves() {
            return avoidMoves;
        }

        public String getBoardFen() {
            return boardFen;
        }

        @Override
        public String toString() {
            return "EPDObject{" +
                    "bestMoves=" + bestMoves +
                    ", id='" + id + '\'' +
                    ", boardFen='" + boardFen + '\'' +
                    '}';
        }
    }
    
}
