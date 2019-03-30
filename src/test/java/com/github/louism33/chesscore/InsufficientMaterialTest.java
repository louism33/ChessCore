package com.github.louism33.chesscore;

import com.github.louism33.utils.MoveParserFromAN;
import com.github.louism33.utils.PGNParser;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InsufficientMaterialTest {


    @Test
    void repetitionFromRealGamesTest() {

        String pgn = "" +
                "1. c4 {book} e5 {book} 2. Nc3 {book} Nf6 {book} 3. Nf3 {book} Nc6 {book}\n" +
                "4. e4 {book} Bb4 {book} 5. d3 {book} d6 {book} 6. Be2 {book} h6 {book}\n" +
                "7. O-O {book} Bxc3 {book} 8. bxc3 {book} a5 {book} 9. Be3 {book} Ke7 {book}\n" +
                "10. Qc2 {book} Bg4 {book} 11. Rab1 {book} Qc8 {book} 12. d4 {book} exd4 {book}\n" +
                "13. Nxd4 {book} Bxe2 {book} 14. Qxe2 {book} Kd7 {book} 15. f3 {book} a4 {book}\n" +
                "16. Nf5 {book} Rg8 {book} 17. Qd3 {book} Ne5 {book} 18. Qe2 {book} Ke6 {book}\n" +
                "19. a3 {book} c5 {book} 20. Rb6 {book} Nc6 {book} 21. Rd1 {book} Ne8 {book}\n" +
                "22. f4 {book} Qc7 {book} 23. Rdb1 {book} Rb8 {book} 24. Qf3 {book} Rf8 {book}\n" +
                "25. Ng3 {book} Nf6 {book} 26. f5+ {book} Kd7 {book} 27. Qe2 {book} Rfe8 {book}\n" +
                "28. Bf4 {book} h5 {book} 29. Qd1 {book} Ne5 {book} 30. Bg5 {book} Rh8 {book}\n" +
                "31. Bxf6 {book} gxf6 {book} 32. Nxh5 {book} Kc8 {book} 33. Qe2 {book} Nd7 {book}\n" +
                "34. R6b2 {book} Qa5 {book} 35. Rc2 {book} Kc7 {book} 36. Nf4 {book} Kc6 {book}\n" +
                "37. Qd3 {book} Ne5 {book} 38. Qd5+ {book} Kc7 {book} 39. Nd3 {book} Rhf8 {book}\n" +
                "40. Nxe5 {book} fxe5 {book} 41. f6 {book} Qa6 {book} 42. Rf2 {book} Qc6 {book}\n" +
                "43. Qd3 {book} Rh8 {book} 44. h3 {book} Qe8 {book} 45. Qd5 {book} Qd7 {book}\n" +
                "46. Rff1 {book} Rhg8 {book} 47. Kh2 {book} Rh8 {book} 48. Kg1 {book} Rhg8 {book}\n" +
                "49. Kh2 {book} Rh8 {book} 50. Rb2 {book} b6 {book} 51. Rbf2 {book} b5 {book}\n" +
                "52. Rd2 {book} bxc4 {book} 53. Qxc4 {book} Rb6 {book} 54. Rd3 {book} Rhb8 {book}\n" +
                "55. Rfd1 {book} Rd8 {book} 56. R3d2 {book} Qe6 {book} 57. Qxa4 {book}\n" +
                "Qxf6 {book} 58. Kg1 {book} Qe6 {book} 59. c4 {book} Qg6 {book} 60. Qa7+ {book}\n" +
                "Kc6 {book} 61. Re1 {book} Rdb8 {book} 62. Qa4+ {book} Kc7 {book} 63. Qa7+ {book}\n" +
                "R8b7 {book} 64. Qa5 {book} Qe6 {book} 65. Rc2 {book} Kd7 {book} 66. Qd2 {book}\n" +
                "f5 {book} 67. Qd5 {book} Qxd5 {book} 68. exd5 {book} Rb3 {book} 69. Ra1 {book}\n" +
                "Rb8 {book} 70. Kh2 {book} f4 {book} 71. a4 {book} Rb1 {book} 72. Rxb1 {book}\n" +
                "Rxb1 {book} 73. Ra2 {book} Rb4 {book} 74. a5 {book} Kc8 {book} 75. a6 {book}\n" +
                "Kb8 {book} 76. h4 {book} Ka7 {book} 77. Kh3 {book} Rxc4 {book} 78. h5 {book}\n" +
                "Rc3+ {book} 79. Kg4 {book} Rc1 {book} 80. Kf5 {book} Rh1 {book} 81. Ke6 {book}\n" +
                "Rh2 {book} 82. Ra1 {book} Rxh5 {book} 83. Kxd6 {book} Rg5 {book} 84. Kc6 {book}\n" +
                "Rxg2 {book} 85. d6 {book} Rd2 {book} 86. d7 {book} e4 {book} 87. Ra4 {book}\n" +
                "c4 {book} 88. Rxc4 {book} f3 {book} 89. Rxe4 {book} Rc2+ {book} 90. Kd6 {book}\n" +
                "Rd2+ {book} 91. Ke7 {book} f2 {book} 92. Rf4 {book} Re2+ {book} 93. Kd6 {book}\n" +
                "Rd2+ {book} 94. Ke6 {book} Re2+ {book} 95. Kd5 {book} Rd2+ {book} 96. Ke6 {book}\n" +
                "Re2+ {book} 97. Kd5 {book} Rd2+ {book} 98. Kc6 {book} Rc2+ {book} 99. Kb5 {book}\n" +
                "Rd2 {book} 100. d8=Q {book} Rxd8 {book} 101. Rf7+ {book} Ka8 {book}\n" +
                "102. Rxf2 {book} Rb8+ {book} 103. Ka5 {book} Re8 {book} 104. Rf6 {book}\n" +
                "Kb8 {book} 105. Kb6 {book} Ka8 {book} 106. Rc6 {book} Rb8+ {book}\n" +
                "107. Ka5 {book} Rd8 {book} 108. Kb5 {book} Rb8+ {book} 109. Rb6 {book}\n" +
                "Ka7 {book} 110. Rxb8 {book} Kxb8 {book} 111. Kb6 {book} Ka8 {book}\n" +
                "112. Kc6 {book} Kb8 {book} 113. Kc5 {book} Ka7 {book} 114. Kd5 {3.1s}\n" +
                "Kxa6" +
                "";
               
        String pgn2 = "" +
                "1. c4 {book} e5 {book} 2. Nc3 {book} Nf6 {book} 3. Nf3 {book} Nc6 {book}\n" +
                "4. e4 {book} Bb4 {book} 5. d3 {book} d6 {book} 6. Be2 {book} h6 {book}\n" +
                "7. O-O {book} Bxc3 {book} 8. bxc3 {book} a5 {book} 9. Be3 {book} Ke7 {book}\n" +
                "10. Qc2 {book} Bg4 {book} 11. Rab1 {book} Qc8 {book} 12. d4 {book} exd4 {book}\n" +
                "13. Nxd4 {book} Bxe2 {book} 14. Qxe2 {book} Kd7 {book} 15. f3 {book} a4 {book}\n" +
                "16. Nf5 {book} Rg8 {book} 17. Qd3 {book} Ne5 {book} 18. Qe2 {book} Ke6 {book}\n" +
                "19. a3 {book} c5 {book} 20. Rb6 {book} Nc6 {book} 21. Rd1 {book} Ne8 {book}\n" +
                "22. f4 {book} Qc7 {book} 23. Rdb1 {book} Rb8 {book} 24. Qf3 {book} Rf8 {book}\n" +
                "25. Ng3 {book} Nf6 {book} 26. f5+ {book} Kd7 {book} 27. Qe2 {book} Rfe8 {book}\n" +
                "28. Bf4 {book} h5 {book} 29. Qd1 {book} Ne5 {book} 30. Bg5 {book} Rh8 {book}\n" +
                "31. Bxf6 {book} gxf6 {book} 32. Nxh5 {book} Kc8 {book} 33. Qe2 {book} Nd7 {book}\n" +
                "34. R6b2 {book} Qa5 {book} 35. Rc2 {book} Kc7 {book} 36. Nf4 {book} Kc6 {book}\n" +
                "37. Qd3 {book} Ne5 {book} 38. Qd5+ {book} Kc7 {book} 39. Nd3 {book} Rhf8 {book}\n" +
                "40. Nxe5 {book} fxe5 {book} 41. f6 {book} Qa6 {book} 42. Rf2 {book} Qc6 {book}\n" +
                "43. Qd3 {book} Rh8 {book} 44. h3 {book} Qe8 {book} 45. Qd5 {book} Qd7 {book}\n" +
                "46. Rff1 {book} Rhg8 {book} 47. Kh2 {book} Rh8 {book} 48. Kg1 {book} Rhg8 {book}\n" +
                "49. Kh2 {book} Rh8 {book} 50. Rb2 {book} b6 {book} 51. Rbf2 {book} b5 {book}\n" +
                "52. Rd2 {book} bxc4 {book} 53. Qxc4 {book} Rb6 {book} 54. Rd3 {book} Rhb8 {book}\n" +
                "55. Rfd1 {book} Rd8 {book} 56. R3d2 {book} Qe6 {book} 57. Qxa4 {book}\n" +
                "Qxf6 {book} 58. Kg1 {book} Qe6 {book} 59. c4 {book} Qg6 {book} 60. Qa7+ {book}\n" +
                "Kc6 {book} 61. Re1 {book} Rdb8 {book} 62. Qa4+ {book} Kc7 {book} 63. Qa7+ {book}\n" +
                "R8b7 {book} 64. Qa5 {book} Qe6 {book} 65. Rc2 {book} Kd7 {book} 66. Qd2 {book}\n" +
                "f5 {book} 67. Qd5 {book} Qxd5 {book} 68. exd5 {book} Rb3 {book} 69. Ra1 {book}\n" +
                "Rb8 {book} 70. Kh2 {book} f4 {book} 71. a4 {book} Rb1 {book} 72. Rxb1 {book}\n" +
                "Rxb1 {book} 73. Ra2 {book} Rb4 {book} 74. a5 {book} Kc8 {book} 75. a6 {book}\n" +
                "Kb8 {book} 76. h4 {book} Ka7 {book} 77. Kh3 {book} Rxc4 {book} 78. h5 {book}\n" +
                "Rc3+ {book} 79. Kg4 {book} Rc1 {book} 80. Kf5 {book} Rh1 {book} 81. Ke6 {book}\n" +
                "Rh2 {book} 82. Ra1 {book} Rxh5 {book} 83. Kxd6 {book} Rg5 {book} 84. Kc6 {book}\n" +
                "Rxg2 {book} 85. d6 {book} Rd2 {book} 86. d7 {book} e4 {book} 87. Ra4 {book}\n" +
                "c4 {book} 88. Rxc4 {book} f3 {book} 89. Rxe4 {book} Rc2+ {book} 90. Kd6 {book}\n" +
                "Rd2+ {book} 91. Ke7 {book} f2 {book} 92. Rf4 {book} Re2+ {book} 93. Kd6 {book}\n" +
                "Rd2+ {book} 94. Ke6 {book} Re2+ {book} 95. Kd5 {book} Rd2+ {book} 96. Ke6 {book}\n" +
                "Re2+ {book} 97. Kd5 {book} Rd2+ {book} 98. Kc6 {book} Rc2+ {book} 99. Kb5 {book}\n" +
                "Rd2 {book} 100. d8=Q {book} Rxd8 {book} 101. Rf7+ {book} Ka8 {book}\n" +
                "102. Rxf2 {book} Rb8+ {book} 103. Ka5 {book} Re8 {book} 104. Rf6 {book}\n" +
                "Kb8 {book} 105. Kb6 {book} Ka8 {book} 106. Rc6 {book} Rb8+ {book}\n" +
                "107. Ka5 {book} Rd8 {book} 108. Kb5 {book} Rb8+ {book} 109. Rb6 {book}\n" +
                "Ka7 {book} 110. Rxb8 {book} Kxb8 {book} 111. Kb6 {book} Ka8 {book}\n" +
                "112. Kc6 {book} Kb8 {book} 113. Kc5 {book} Ka7 {book} 114. Kd5 {3.2s}\n" +
                "Kxa6" +
                "";
        
        List<String> pgns = new ArrayList<>();
        pgns.add(pgn);
        pgns.add(pgn2);


        for (int p = 0; p < pgns.size(); p++) {
            List<String> s = PGNParser.parsePGN(pgns.get(p));

            Chessboard board = new Chessboard();
            for (int i = 0; i < s.size(); i++) {
                String move = s.get(i);

                move = move.trim();

                int move1 = 0;
                try {
                    move1 = MoveParserFromAN.buildMoveFromANWithOO(board, move);
                } catch (Exception | Error e) {
                    System.out.println(s);
                    System.out.println(board);
                    System.out.println(board.zobristHash);
                    System.out.println(Arrays.toString(board.zobristHashStack));
                    System.out.println(move);
                    System.out.println(MoveParser.toString(move1));
                    e.printStackTrace();
                }
                board.makeMoveAndFlipTurn(move1);

                boolean dbr = board.isDrawByInsufficientMaterial();

                if (i != s.size() - 1) {
                    if (dbr) {
                        System.out.println(s);
                        System.out.println(board);
                        System.out.println("master index is: " + board.masterIndex);
                        System.out.println("qhmc is: " + board.quietHalfMoveCounter);
                        System.out.println(board.zobristHash);
                        System.out.println(Arrays.toString(board.zobristHashStack));
                    }
                    Assert.assertFalse(dbr);
                }
            }

            boolean dbr = board.isDrawByInsufficientMaterial();
            if (!dbr) {
                System.out.println(s);
                System.out.println(board);
                System.out.println("master index is: " + board.masterIndex);
                System.out.println("qhmc is: " + board.quietHalfMoveCounter);
                System.out.println(board.zobristHash);
                System.out.println(Arrays.toString(board.zobristHashStack));
            }
            Assert.assertTrue(dbr);
        }
    }
}
