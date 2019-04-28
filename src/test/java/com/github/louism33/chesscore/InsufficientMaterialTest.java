package com.github.louism33.chesscore;

import com.github.louism33.utils.MoveParserFromAN;
import com.github.louism33.utils.PGNParser;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.github.louism33.chesscore.MaterialHashUtil.*;

public class InsufficientMaterialTest {

    @Test
    void insufficientMatFromRealGamesTest() {

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
        
        String pgn3 = "" +
                "1. e4 {book} e5 {book} 2. Nf3 {book} Nc6 {book} 3. Bb5 {book} Nf6 {0.97s}\n" +
                "4. O-O {0.70s} Bc5 {0.95s} 5. Nc3 {0.69s} Qe7 {0.93s} 6. d3 {0.67s} a6 {0.92s}\n" +
                "7. Bxc6 {0.66s} dxc6 {0.90s} 8. Be3 {0.65s} Bxe3 {0.88s} 9. fxe3 {0.63s}\n" +
                "Bg4 {0.86s} 10. a3 {0.62s} c5 {0.85s} 11. Nd5 {0.61s} Nxd5 {0.83s}\n" +
                "12. exd5 {0.59s} O-O {0.81s} 13. e4 {0.58s} a5 {0.80s} 14. c4 {0.57s} a4 {0.78s}\n" +
                "15. b3 {0.56s} axb3 {0.77s} 16. Qxb3 {0.55s} b6 {0.75s} 17. Qc3 {0.54s}\n" +
                "Rfe8 {0.74s} 18. Nd2 {0.53s} Qg5 {0.72s} 19. Rae1 {0.52s} Qh6 {0.71s}\n" +
                "20. h3 {0.51s} Bh5 {0.70s} 21. Rf2 Bg6 {0.68s} 22. Nf1 Qg5 {0.67s} 23. Ne3\n" +
                "Rf8 {0.66s} 24. Kh2 h5 {0.65s} 25. g3 h4 {0.63s} 26. g4 Qe7 {0.62s} 27. Kg1\n" +
                "Qd6 {0.61s} 28. Rb1 Rfe8 {0.60s} 29. Qb3 Rf8 {0.59s} 30. Ra1 Qe7 {0.58s}\n" +
                "31. Raf1 Qd6 {0.57s} 32. a4 Kh7 {0.56s} 33. Ng2 Qe7 {0.55s} 34. Re2 Kh6 {0.53s}\n" +
                "35. Ne3 Kg5 {0.53s} 36. Ra2 Rh8 {0.52s} 37. Rg2 Qe8 {0.51s} 38. Ra1 Qe7 39. Rc2\n" +
                "Rhe8 40. Qc3 Kh6 {1.5s} 41. Qe1 {1.1s} Qf6 {1.5s} 42. Ng2 {1.1s} c6 {1.5s}\n" +
                "43. dxc6 {1.1s} Red8 {1.5s} 44. Qe3+ {1.1s} Kh7 {1.4s} 45. c7 {1.0s} Rd7 {1.4s}\n" +
                "46. a5 {1.0s} Qc6 {1.4s} 47. Nxh4 {1.0s} Rxa5 {1.3s} 48. Rxa5 {0.98s}\n" +
                "bxa5 {1.3s} 49. Rb2 {0.96s} Qxc7 {1.3s} 50. Rb5 {0.94s} Qd8 {1.3s}\n" +
                "51. Nf3 {0.92s} Rxd3 {1.2s} 52. Ng5+ {0.91s} Kh6 {1.2s} 53. Nxf7+ {0.89s} Kh7\n" +
                "54. Nxd8 {0.87s} Rxe3 {1.2s} 55. Rxc5 {0.85s} Rxe4 {1.2s} 56. Ne6 {0.84s}\n" +
                "Kh6 {1.1s} 57. Rc6 {0.82s} Be8 {1.1s} 58. Rc7 {0.80s} Ba4 {1.1s}\n" +
                "59. Rxg7 {0.79s} Bc2 {1.1s} 60. g5+ {0.77s} Kh5 61. Rh7+ {0.76s} Kg6\n" +
                "62. Rh6+ {0.74s} Kf7 {1.0s} 63. Rf6+ {0.73s} Ke7 {0.98s} 64. Nc5 {0.71s}\n" +
                "Rxc4 {0.96s} 65. Re6+ {0.70s} Kd8 {0.95s} 66. Rxe5 {0.68s} Kc7 {0.93s}\n" +
                "67. Na6+ {0.67s} Kb6 {0.91s} 68. Nb8 {0.66s} a4 {0.89s} 69. Re7 {0.64s}\n" +
                "Rc3 {0.88s} 70. g6 {0.63s} Bxg6 {0.86s} 71. Re6+ {0.62s} Kb7 {0.84s}\n" +
                "72. Rxg6 {0.60s} a3 {0.83s} 73. Rg2 {0.59s} Kxb8 {0.81s} 74. h4 {0.58s}\n" +
                "Kc7 {0.80s} 75. Kh2 {0.57s} Rb3 {0.78s} 76. Rg7+ {0.56s} Kb6 {0.77s}\n" +
                "77. Rg6+ {0.55s} Kb5 {0.75s} 78. Rg8 {0.54s} Kc5 {0.74s} 79. Ra8 {0.53s}\n" +
                "Kd5 {0.72s} 80. h5 {0.51s} Ke5 {1.8s} 81. Ra6 {1.3s} Rb2+ {1.7s} 82. Kh3 {1.3s}\n" +
                "Ra2 {1.7s} 83. h6 {1.3s} Ra1 {1.7s} 84. Kg2 {1.2s} Ra2+ {1.6s} 85. Kg3 {1.2s}\n" +
                "Ra1 {1.6s} 86. Ra5+ {1.2s} Kf6 {1.6s} 87. Ra7 {1.2s} Rh1 {1.5s} 88. Rxa3 {1.1s}\n" +
                "Rxh6 {1.5s} 89. Ra6+ {1.1s} Kg5 {1.5s} 90. Rxh6 {1.1s}\n" +
                "Kxh6" +
                "";
        
        String pgn4 = "" +
                "1. e4 {book} e5 {book} 2. Nf3 {book} Nc6 {book} 3. Bb5 {book} f5 {book}\n" +
                "4. d3 {0.70s} fxe4 {0.95s} 5. dxe4 {0.69s} Nf6 {0.93s} 6. Bxc6 {0.67s}\n" +
                "dxc6 {0.91s} 7. Qxd8+ {0.66s} Kxd8 {0.90s} 8. Nxe5 {0.65s} Ke8 {0.88s}\n" +
                "9. Nc3 {0.63s} Bb4 {0.86s} 10. f3 {0.62s} Be6 {0.85s} 11. Be3 {0.61s}\n" +
                "Ke7 {0.83s} 12. O-O {0.60s} Nd7 {0.81s} 13. f4 {0.58s} Nxe5 {0.80s}\n" +
                "14. fxe5 {0.57s} Bc4 {0.78s} 15. Rf5 {0.56s} Ke6 {0.77s} 16. Rg5 {0.55s}\n" +
                "Rhg8 {0.75s} 17. a4 {0.54s} h6 {0.74s} 18. Rg4 {0.53s} g5 {0.72s}\n" +
                "19. Bd4 {0.52s} c5 {0.71s} 20. Bf2 {0.51s} Bxc3 {0.70s} 21. bxc3 h5 {0.69s}\n" +
                "22. Rg3 b6 {0.67s} 23. Rf3 Raf8 {0.66s} 24. Rxf8 Rxf8 {0.65s} 25. Re1 h4 {0.63s}\n" +
                "26. Be3 Rg8 {0.62s} 27. Bd2 Kxe5 {0.61s} 28. Kf2 Be6 {0.60s} 29. Ke3 g4 {0.59s}\n" +
                "30. Rf1 g3 {0.58s} 31. h3 Bd7 {0.57s} 32. a5 Be6 {0.56s} 33. c4 Bxc4 {0.55s}\n" +
                "34. Bc3+ Kd6 {0.54s} 35. Rf6+ Be6 {0.53s} 36. Rh6 Rf8 {0.52s} 37. axb6\n" +
                "cxb6 {0.51s} 38. Bf6 Rg8 39. Bxh4 Rg7 40. Kf3 Rf7+ {1.5s} 41. Rf6 {1.1s}\n" +
                "Rxf6+ {1.5s} 42. Bxf6 {1.1s} Ba2 {1.5s} 43. Ke3 {1.1s} Bb1 {1.5s} 44. Kd3 {1.1s}\n" +
                "a5 {1.4s} 45. Bh4 {1.0s} a4 {1.4s} 46. Bf6 {1.0s} a3 {1.4s} 47. h4 {1.0s}\n" +
                "Ba2 {1.3s} 48. h5 {0.98s} Bf7 {1.3s} 49. h6 {0.96s} c4+ {1.3s} 50. Ke3 {0.94s}\n" +
                "Bg6 {1.3s} 51. Bc3 {0.92s} a2 {1.2s} 52. Kf4 {0.91s} b5 {1.2s} 53. Be5+ {0.89s}\n" +
                "Ke6 {1.2s} 54. Bb2 {0.87s} b4 {1.2s} 55. c3 {0.85s} b3 {1.2s} 56. Ba1 {0.84s}\n" +
                "Kf7 {1.1s} 57. Bb2 {0.82s} Kf6 {1.1s} 58. Ba1 {0.80s} Bh7 {1.1s} 59. Bb2 {0.79s}\n" +
                "Ke6 {1.1s} 60. Ba1 {0.77s} Kf7 {1.0s} 61. Bb2 {0.76s} Kf6 {1.0s} 62. Ba1 {0.74s}\n" +
                "Ke6 {1.0s} 63. Bb2 {0.72s} Kd6 {0.98s} 64. Ba3+ {0.71s} Kc7 {0.96s}\n" +
                "65. Bb2 {0.70s} Kd6 {0.95s} 66. Ba3+ {0.68s} Kd7 {0.93s} 67. Bb2 {0.67s}\n" +
                "Ke7 {0.91s} 68. Ke5 {0.66s} Ke8 {0.89s} 69. Kd4 {0.64s} Bg8 {0.88s}\n" +
                "70. e5 {0.63s} Ke7 {0.86s} 71. e6 {0.62s} Kf6 {0.84s} 72. Kxc4 {0.60s}\n" +
                "Bxe6+ {0.83s} 73. Kb4 {0.59s} Kg6 {0.81s} 74. c4 {0.58s} Kxh6 {0.80s}\n" +
                "75. Kxb3 {0.57s} Kg6 {0.78s} 76. Bc3 {0.56s} Kf5 {0.76s} 77. Kxa2 {0.55s}\n" +
                "Bxc4+ {0.75s} 78. Ka3 {0.54s} Bf1 {0.74s} 79. Kb4 {0.53s} Bxg2 {0.72s}\n" +
                "80. Be1 {0.52s} Kf4 {1.8s} 81. Bxg3+ {1.3s}\n" +
                "Kxg3" +
                "";
        
        List<String> pgns = new ArrayList<>();
        pgns.add(pgn);
        pgns.add(pgn2);
        pgns.add(pgn3);
        pgns.add(pgn4);


        for (int p = 0; p < pgns.size(); p++) {
            List<String> s = PGNParser.parsePGNSimple(pgns.get(p));

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

                boolean drawMat = board.isDrawByInsufficientMaterial();

                if (i != s.size() - 1) {
                    if (drawMat) {
                        System.out.println(s);
                        System.out.println(board);
                        System.out.println("master index is: " + board.masterIndex);
                        System.out.println("qhmc is: " + board.quietHalfMoveCounter);
                        System.out.println(board.zobristHash);
                        System.out.println(Arrays.toString(board.zobristHashStack));
                        board.isDrawByInsufficientMaterial();
                    }
                    Assert.assertFalse(drawMat);
                }
            }

            boolean dbr = board.isDrawByInsufficientMaterial();
            Assert.assertTrue(dbr);

            Assert.assertTrue(isBasicallyDrawn(board));
        }
    }


    @Test
    void isKBBKTestSameSqBlack() {
        Chessboard board = new Chessboard("5b1b/8/8/8/8/8/7k/K7");
        Assert.assertTrue(board.isDrawByInsufficientMaterial());
        Assert.assertTrue(isBasicallyDrawn(board));
    }

    @Test
    void isKBBKTestSameSqWhite() {
        Chessboard board = new Chessboard("5B1B/8/8/8/8/8/7k/K7");
        Assert.assertTrue(board.isDrawByInsufficientMaterial());
        Assert.assertTrue(isBasicallyDrawn(board));
    }


    @Test
    void isKBBKTestDiffSqBlack() {
        Chessboard board = new Chessboard("6bb/8/8/8/8/8/7k/K7");
        Assert.assertFalse(board.isDrawByInsufficientMaterial());
        Assert.assertFalse(isBasicallyDrawn(board));
    }

    @Test
    void isKBBKTestDiffSqWhite() {
        Chessboard board = new Chessboard("6BB/8/8/8/8/8/7k/K7");
        Assert.assertFalse(board.isDrawByInsufficientMaterial());
        Assert.assertFalse(isBasicallyDrawn(board));
    }

    @Test
    void isKNKNTest() {
        Chessboard board = new Chessboard("8/8/8/8/8/8/6nk/3NK3");
        Assert.assertTrue(isBasicallyDrawn(board));
    }

    @Test
    void isKBKBTestDifferentSq() {
        Chessboard board = new Chessboard("6bB/8/8/8/8/8/7k/K7");
        Assert.assertTrue(isBasicallyDrawn(board));
    }

    @Test
    void isKBKBTestSameSqB() {
        Chessboard board = new Chessboard("5b1B/8/8/8/8/8/7k/K7");
        Assert.assertTrue(isBasicallyDrawn(board));
    }

    @Test
    void isKBKBTestSameSqW() {
        Chessboard board = new Chessboard("8/8/8/8/8/8/7k/K4b1B");
        Assert.assertTrue(isBasicallyDrawn(board));
    }
}
