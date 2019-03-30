package com.github.louism33.utils;

import org.junit.Assert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PGNParser {


    public static List<String> parsePGN(String pgn){

        pgn = pgn.replaceAll(" ?\\d+\\. ", " ");

        pgn = pgn.replaceAll("\\{[\\w\\.]*} ?", "");

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
