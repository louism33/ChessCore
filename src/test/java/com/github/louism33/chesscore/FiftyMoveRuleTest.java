package com.github.louism33.chesscore;

import com.github.louism33.utils.MoveParserFromAN;
import com.github.louism33.utils.PGNParser;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.github.louism33.chesscore.Chessboard.MAX_DEPTH_AND_ARRAY_LENGTH;

@SuppressWarnings("ALL")
public class FiftyMoveRuleTest {

    @Test
    void repetitionFromRealGamesTestTest() {

        String pgn = "" +
                "1. e4 {book} e5 {book} 2. Nf3 {book} Nc6 {book} 3. Bc4 {book} Bc5 {book}\n" +
                "4. c3 {0.70s} Nf6 {0.95s} 5. d4 {0.69s} exd4 {0.93s} 6. cxd4 {0.67s}\n" +
                "Bb4+ {0.92s} 7. Kf1 {0.66s} Nxe4 {0.90s} 8. d5 {0.65s} Na5 {0.88s}\n" +
                "9. Bd3 {0.63s} Nf6 {0.86s} 10. a3 {0.62s} Be7 {0.85s} 11. b4 {0.61s} c6 {0.83s}\n" +
                "12. d6 {0.60s} Bxd6 {0.81s} 13. bxa5 {0.58s} Qxa5 {0.80s} 14. Bxh7 {0.57s}\n" +
                "Rxh7 {0.78s} 15. Qxd6 {0.56s} Rh5 {0.77s} 16. Qg3 {0.55s} Kf8 {0.75s}\n" +
                "17. Bd2 {0.54s} Qb5+ {0.74s} 18. Kg1 {0.53s} Qb2 {0.72s} 19. Bc3 {0.52s}\n" +
                "Qc1+ {0.71s} 20. Ne1 {0.51s} Qh6 {0.70s} 21. Qd6+ Kg8 {0.68s} 22. Qe7\n" +
                "Qc1 {0.67s} 23. Bxf6 gxf6 {0.66s} 24. Qe8+ Kg7 {0.65s} 25. Qe3 Qd1 {0.63s}\n" +
                "26. Qd2 Rd5 {0.62s} 27. Qxd1 Rxd1 {0.61s} 28. Kf1 d5 {0.60s} 29. Ke2 Rc1 {0.59s}\n" +
                "30. Kd2 Rc4 {0.58s} 31. Nf3 Bf5 {0.57s} 32. Nc3 Rg8 {0.56s} 33. h3 Rd8 {0.54s}\n" +
                "34. Rac1 Rf4 {0.53s} 35. Ne2 Ra4 {0.52s} 36. Nfd4 Be4 {0.51s} 37. f3 Bg6 {0.51s}\n" +
                "38. Rc3 Rc4 39. g4 Rxc3 40. Nxc3 Kf8 {1.5s} 41. Re1 {1.1s} c5 {1.5s}\n" +
                "42. Nf5 {1.1s} Bxf5 {1.5s} 43. gxf5 {1.1s} d4 {1.5s} 44. Ne4 {1.1s} Rd5 {1.4s}\n" +
                "45. Nxf6 {1.0s} Rxf5 {1.4s} 46. Nd7+ {1.0s} Kg7 {1.4s} 47. Rg1+ {1.0s}\n" +
                "Kh7 {1.3s} 48. Rg4 {0.98s} Kh6 {1.3s} 49. f4 {0.96s} c4 {1.3s} 50. Ne5 {0.94s}\n" +
                "b5 {1.3s} 51. Ke2 {0.92s} f6 {1.2s} 52. Nf3 {0.91s} Rd5 {1.2s} 53. Rg8 {0.89s}\n" +
                "Kh7 {1.2s} 54. Rc8 {0.87s} d3+ {1.2s} 55. Ke3 {0.85s} Rd7 {1.2s} 56. f5 {0.84s}\n" +
                "Re7+ {1.1s} 57. Kf4 {0.82s} Rd7 {1.1s} 58. Nd2 {0.80s} Rd4+ {1.1s}\n" +
                "59. Kf3 {0.79s} Kg7 {1.1s} 60. a4 {0.77s} a6 {1.0s} 61. Rc7+ {0.76s} Kh6 {1.0s}\n" +
                "62. Rc6 {0.74s} bxa4 {1.0s} 63. Ke3 {0.72s} Rh4 {0.98s} 64. Nxc4 {0.71s}\n" +
                "Rxh3+ {0.97s} 65. Ke4 {0.70s} Kg5 {0.95s} 66. Rxa6 {0.68s} Rh8 {0.93s}\n" +
                "67. Kxd3 {0.67s} Kxf5 {0.91s} 68. Ne3+ {0.66s} Ke5 {0.89s} 69. Ra5+ {0.64s}\n" +
                "Kd6 {0.88s} 70. Rxa4 {0.63s} Ke5 {0.86s} 71. Ra5+ {0.62s} Ke6 {0.84s}\n" +
                "72. Ra6+ {0.60s} Ke5 {0.83s} 73. Nc4+ {0.59s} Kf5 {0.81s} 74. Nd6+ {0.58s}\n" +
                "Ke6 {0.79s} 75. Ne4+ {0.57s} Kf5 {0.78s} 76. Rxf6+ {0.56s} Ke5 {0.77s}\n" +
                "77. Rg6 {0.55s} Rh3+ {0.75s} 78. Rg3 {0.54s} Rh7 {0.74s} 79. Rg5+ {0.53s}\n" +
                "Kf4 {0.72s} 80. Rd5 {0.51s} Rh3+ {1.8s} 81. Kd4 {1.3s} Rb3 {1.7s} 82. Nc5 {1.3s}\n" +
                "Rb4+ {1.7s} 83. Kd3 {1.3s} Rb6 {1.7s} 84. Kc4 {1.2s} Rb8 {1.6s} 85. Nd3+ {1.2s}\n" +
                "Ke4 {1.6s} 86. Re5+ {1.2s} Kf3 87. Nc5 {1.2s} Rh8 {1.5s} 88. Ne6 {1.1s}\n" +
                "Rb8 {1.5s} 89. Kc5 {1.1s} Ra8 {1.5s} 90. Kd5 {1.1s} Ra5+ {1.5s} 91. Nc5 {1.1s}\n" +
                "Ra8 {1.4s} 92. Re7 {1.0s} Rd8+ {1.4s} 93. Ke5 {1.0s} Rc8 {1.4s} 94. Kd4 {1.0s}\n" +
                "Ra8 {1.3s} 95. Re3+ {0.98s} Kf4 {1.3s} 96. Nd3+ {0.96s} Kg4 {1.3s}\n" +
                "97. Re5 {0.94s} Ra4+ {1.3s} 98. Ke3 {0.93s} Ra3 {1.2s} 99. Rc5 {0.91s}\n" +
                "Rb3 {1.2s} 100. Ke4 {0.89s} Rb7 {1.2s} 101. Rf5 {0.87s} Kh4 {1.2s}\n" +
                "102. Rf4+ {0.85s} Kg5 {1.2s} 103. Rf3 {0.84s} Rb1 {1.1s} 104. Rf5+ {0.82s}\n" +
                "Kh6 {1.1s} 105. Rf6+ {0.80s} Kg5 {1.1s} 106. Rf7 {0.79s} Ra1 {1.1s}\n" +
                "107. Rf5+ {0.77s} Kh6 {1.0s} 108. Rf2 {0.76s} Kg5 {1.0s} 109. Nc5 {0.74s}\n" +
                "Re1+ {1.0s} 110. Kd4 {0.73s} Re8 {0.98s} 111. Ne4+ {0.71s} Kg4 {0.96s}\n" +
                "112. Ke3 {0.70s} Rd8 {0.95s} 113. Rf7 {0.68s} Ra8 {0.93s} 114. Rf4+ {0.67s}\n" +
                "Kh5 {0.91s} 115. Kd4 {0.66s} Ra4+ {0.89s} 116. Kd5 {0.64s} Ra5+ {0.88s}\n" +
                "117. Ke6 {0.63s} Ra6+ {0.86s} 118. Nd6 {0.62s} Kg5 {0.84s} 119. Rf1 {0.61s}\n" +
                "Ra2 {0.83s} 120. Nc4 {0.59s} Re2+ {1.9s} 121. Ne5 {1.4s} Re4 {1.8s}\n" +
                "122. Rf5+ {1.4s} Kh4 {1.8s} 123. Kd5 {1.3s} Ra4 {1.8s} 124. Rf3 {1.3s}\n" +
                "Kg5 {1.7s} 125. Nc4 {1.3s} Ra1 {1.7s} 126. Rf7" +
                "";

        

        List<String> pgns = new ArrayList<>();
        pgns.add(pgn);

        for (int p = 0; p < pgns.size(); p++) {
            List<String> s = PGNParser.parsePGNSimple(pgns.get(p));

            Chessboard board = new Chessboard();
            for (int i = 0; i < s.size(); i++) {
                String move = s.get(i);

                move = move.trim();

                int move1 = 0;
                try {
                    move1 = MoveParserFromAN.buildMoveFromANWithOO(board, move);
                } catch (Throwable e) {
                    System.out.println(s);
                    System.out.println(board);
                    System.out.println(board.zobristHash);
                    System.out.println(Arrays.toString(board.zobristHashStack));
                    System.out.println(move);
                    System.out.println(MoveParser.toString(move1));
                    e.printStackTrace();
                }
                board.makeMoveAndFlipTurn(move1);

                boolean dbr = board.isDrawByFiftyMoveRule();


                if (i != s.size() - 1) {
                    if (dbr) {
                        System.out.println(board);
                        System.out.println("master index is: " + board.masterIndex);
                        System.out.println("qhmc is: " + board.quietHalfMoveCounter);
                    }
                    Assert.assertFalse(dbr);
                }
            }

            boolean dbr = board.isDrawByFiftyMoveRule();
            Assert.assertTrue(dbr);
        }
    }


    public boolean isDrawByRepetition(Chessboard board) {
        int l = board.quietHalfMoveCounter < MAX_DEPTH_AND_ARRAY_LENGTH ? board.quietHalfMoveCounter : MAX_DEPTH_AND_ARRAY_LENGTH;
        int numberOfReps = 0;
        if (board.quietHalfMoveCounter < 2) {
            return false;
        }

        int c = 0;

        for (int i = simulateMasterIndexDown2(board.masterIndex); i >= -1; i = simulateMasterIndexDown2(i)) {
            long h = board.zobristHashStack[i];
            if (board.zobristHash == h) {
                numberOfReps++;
//                return true;
            }

            if (numberOfReps == 2) {
                return true;
            }

            c += 2;
            if (c >= l) {
                break;
            }
        }
        return false;
    }

    private int simulateMasterIndexUp2(int masterIndex) {
        return (masterIndex + 2 + MAX_DEPTH_AND_ARRAY_LENGTH) % MAX_DEPTH_AND_ARRAY_LENGTH;
    }

    private int simulateMasterIndexDown2(int masterIndex) {
        return (masterIndex - 2 + MAX_DEPTH_AND_ARRAY_LENGTH) % MAX_DEPTH_AND_ARRAY_LENGTH;
    }


    public boolean isDrawByRepetitionOld(Chessboard board) {
        int limit = 25;

        long currentZob = board.zobristHash;
        for (int i = 0; i < limit; i++) {
            if (board.zobristHashStack[i] == currentZob) {
                return true;
            }
        }
        return false;
    }
}
