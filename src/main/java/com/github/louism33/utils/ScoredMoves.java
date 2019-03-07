package com.github.louism33.utils;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class ScoredMoves {

    private static final Pattern splitter = Pattern.compile(", ");
    private List<ScoredMove> scoredMoves;

    public ScoredMoves(List<ScoredMove> scoredMoves) {
        this.scoredMoves = scoredMoves;
    }

    static ScoredMoves parseComment(String comment){
        System.out.println("*************************************");
        System.out.println(comment);

        final String[] s = splitter.split(comment);

        System.out.println(Arrays.toString(s));


        for (int i = 0; i < s.length; i++) {
            final String[] split = s[i].split("=");
            if (split.length < 2) {
                return null;
            }
            final String move = split[0];
            System.out.println("move is " + move);
            final String score = split[1];
            System.out.println("score is "+ score);
        }
        
        System.out.println("*************************************");
        return null;
    }
    
    
    public List<ScoredMove> getScoredMoves() {
        return scoredMoves;
    }

    public void setScoredMoves(List<ScoredMove> scoredMoves) {
        this.scoredMoves = scoredMoves;
    }

    public class ScoredMove {
        private int move;
        private int score;

        public ScoredMove(int move, int score) {
            this.move = move;
            this.score = score;
        }

        public int getMove() {
            return move;
        }

        public void setMove(int move) {
            this.move = move;
        }

        public int getScore() {
            return score;
        }

        public void setScore(int score) {
            this.score = score;
        }
        
        
    }

}
