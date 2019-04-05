package com.github.louism33.utils;

import com.github.louism33.chesscore.Chessboard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public final class ScoredMoves {

    private static final Pattern splitter = Pattern.compile(", ");
    private List<ScoredMove> scoredMoves;

    public ScoredMoves(List<ScoredMove> scoredMoves) {
        this.scoredMoves = scoredMoves;
    }

    static ScoredMoves parseComment(Chessboard board, String comment){
        try {
            final String[] s = splitter.split(comment);

            List<ScoredMove> scoredMoveList = new ArrayList<>();

            for (int i = 0; i < s.length; i++) {
                final String[] split = s[i].split("=");
                if (split.length < 2) {
                    return null;
                }
                final String move = split[0];
                final String score = split[1];

                scoredMoveList.add(new ScoredMove(MoveParserFromAN.buildMoveFromANWithOO(board, move), Integer.parseInt(score)));
            }

            return new ScoredMoves(scoredMoveList);
        } catch (Exception e) {
            return null;
        }
    }
    
    
    public List<ScoredMove> getScoredMoves() {
        return scoredMoves;
    }

    public void setScoredMoves(List<ScoredMove> scoredMoves) {
        this.scoredMoves = scoredMoves;
    }

    public static class ScoredMove {
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

        @Override
        public String toString() {
            return "ScoredMove{" +
                    "move=" + move +
                    ", score=" + score +
                    '}';
        }
    }

}
