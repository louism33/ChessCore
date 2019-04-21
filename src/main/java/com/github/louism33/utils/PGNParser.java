package com.github.louism33.utils;

import org.junit.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("ALL")
public final class PGNParser {

    public static class TexelObject {
        public List<String> allMoves;
        public float winner;
        public int numberOfBookMoves = 0;

        public TexelObject(List<String> allMoves, int numberOfBookMoves, float winner) {
            this.allMoves = allMoves;
            this.numberOfBookMoves = numberOfBookMoves;
            this.winner = winner;
        }
    }

    static Matcher whiteMates = Pattern.compile("White mates").matcher("");
    static Matcher blackMates = Pattern.compile("Black mates").matcher("");
    static Matcher draw = Pattern.compile("1/2-1/2").matcher("");


    public static TexelObject parsePGNForTexel(String pgn){
        // count book moves
        int cnt = pgn.split("\\{book\\}").length - 1;
        float winner = 0;
        whiteMates.reset(pgn);
        blackMates.reset(pgn);
        draw.reset(pgn);
        final String mateRemover = "\\{[\\+\\-]M.*";
        if (whiteMates.find()) {
            pgn = pgn.replaceAll(mateRemover, "");
            winner = 1;
        } else if (blackMates.find()) {
            pgn = pgn.replaceAll(mateRemover, "");
            winner = 0;
        } else {
            Assert.assertTrue(draw.find());
            winner = 0.5f;
            pgn = pgn.replaceAll("\\{[^\\{]*Draw.*", "");
        }

        pgn = pgn.replaceAll(" ?\\d+\\. ", " ");

        pgn = pgn.replaceAll("\\{[\\+\\-M\\d\\/\\w\\.]*\\s?[\\w\\.]*} ?", "");

        pgn = pgn.replaceAll("#.*", "");

        pgn = pgn.replaceAll("\n", " ");
        pgn = pgn.replaceAll("  ", " ");
        pgn = pgn.replaceAll("  ", " ");

        String[] s = pgn.split(" ");

        List<String> ss = new ArrayList<>();
        // only grab moves that did not lead finding a mate
        for (int i = 0; i < s.length - 1; i++) {
            if (s[i] == null || s[i].equals("") || s[i].equals(" ")) {
                continue;
            }
            ss.add(s[i]);
        }

        TexelObject tex = new TexelObject(ss, cnt, winner);
        return tex;
    }


    public static List<String> parsePGNSimple(String pgn){

        pgn = pgn.replaceAll(" ?\\d+\\. ", " ");

//        pgn = pgn.replaceAll("\\{[\\w\\.]*} ?", "");

        pgn = pgn.replaceAll("\\{[\\+\\-M\\d\\/\\w\\.]*\\s?[\\w\\.]*} ?", "");

        pgn = pgn.replaceAll("#.*", "");

        pgn = pgn.replaceAll("\n", " ");
        pgn = pgn.replaceAll("  ", " ");
        pgn = pgn.replaceAll("  ", " ");

        String[] s = pgn.split(" ");

        List<String> ss = new ArrayList<>();
        for (int i = 0; i < s.length; i++) {
            if (s[i] == null || s[i].equals("") || s[i].equals(" ")) {
                continue;
            }
            ss.add(s[i]);
        }

        return ss;
    }
}
