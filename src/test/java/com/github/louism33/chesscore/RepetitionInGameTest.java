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
public class RepetitionInGameTest {


    @Test
    void repetitionFromRealGamesTest() {

        String pgn = "1. e4 {book} c5 {book} 2. Nf3 {book} d6 {book} 3. d4 {book} cxd4 {book}\n" +
                "4. Nxd4 {book} Nf6 {book} 5. Nc3 {book} g6 {book} 6. Bg5 {0.97s} Bg7 {0.68s}\n" +
                "7. Bc4 {0.94s} Bd7 {0.66s} 8. O-O {0.92s} O-O {0.64s} 9. Qd3 {0.89s} Ng4 {0.62s}\n" +
                "10. Nd5 {0.87s} Ne5 {0.61s} 11. Qb3 {0.85s} Nbc6 {0.59s} 12. Nxc6 {0.82s}\n" +
                "Nxc6 {0.57s} 13. Rfe1 {0.80s} h6 {0.56s} 14. Bd2 {0.78s} e6 {0.54s}\n" +
                "15. Ne3 {0.76s} Qb6 {0.53s} 16. Bc3 {0.74s} Qxb3 {0.51s} 17. axb3 {0.72s} Bxc3\n" +
                "18. bxc3 {0.70s} Ne5 19. Red1 {0.68s} Bc6 20. Rd4 {0.66s} Nxc4 21. Nxc4 {0.65s}\n" +
                "Bxe4 22. Rxe4 {0.63s} d5 23. Rd4 {0.61s} dxc4 24. Rxc4 {0.59s} e5\n" +
                "25. Kf1 {0.58s} a6 26. Re1 {0.56s} Rfe8 27. Rce4 {0.55s} f6 28. f4 {0.53s} Rac8\n" +
                "29. c4 {0.52s} Rc6 30. Ke2 {0.51s} Rd6 31. fxe5 fxe5 32. Kf3 Rf6+ 33. Kg3 Rf5\n" +
                "34. c5 h5 35. Rb4 Rg5+ 36. Kh3 e4 37. Rxb7 Rxc5 38. Rb6 Kg7 39. c4 a5 40. Kg3\n" +
                "Rf5 {1.1s} 41. Rb5 {1.4s} Kf6 {1.0s} 42. Ra1 {1.4s} Rxb5 {1.00s} 43. cxb5 {1.4s}\n" +
                "e3 {0.97s} 44. Kf3 {1.3s} e2 {0.94s} 45. Kf2 {1.3s} e1=Q+ {0.92s}\n" +
                "46. Rxe1 {1.3s} Rb8 {0.89s} 47. Ke2 {1.2s} Rxb5 {0.87s} 48. Rb1 {1.2s}\n" +
                "Rg5 {0.84s} 49. Kf3 {1.2s} Rf5+ {0.82s} 50. Ke3 {1.1s} Re5+ {0.80s}\n" +
                "51. Kd3 {1.1s} Rd5+ {0.78s} 52. Kc3 {1.1s} Rc5+ {0.76s} 53. Kd4 {1.0s}\n" +
                "Rc2 {0.73s} 54. Rg1 {1.0s} Rd2+ {0.71s} 55. Ke4 {0.99s} Rb2 {0.69s}\n" +
                "56. Kd5 {0.96s} Rxb3 {0.68s} 57. Rf1+ {0.94s} Ke7 {0.66s} 58. Rf2 {0.91s}\n" +
                "Rb5+ {0.64s} 59. Kc4 {0.89s} Rg5 {0.62s} 60. Rb2 {0.86s} h4 {0.60s}\n" +
                "61. Rb7+ {0.84s} Ke6 {0.59s} 62. Rb6+ {0.82s} Kd7 {0.57s} 63. Rb2 {0.80s}\n" +
                "Ke7 {0.55s} 64. Rb7+ {0.77s} Ke6 {0.54s} 65. Rb6+ {0.76s} Kd7 {0.52s}\n" +
                "66. Rb2 {0.73s} Ke7";

        String pgn2 = "" +
                "1. e4 {book} d6 {book} 2. d4 {0.70s} e5 {0.95s} 3. Nc3 {0.69s} exd4 {0.93s}\n" +
                "4. Qxd4 {0.67s} Bd7 {0.92s} 5. Be3 {0.66s} Nc6 {0.90s} 6. Qd2 {0.65s}\n" +
                "Nf6 {0.88s} 7. Bd3 {0.63s} Be7 {0.86s} 8. Nf3 {0.62s} Be6 {0.85s} 9. O-O {0.61s}\n" +
                "O-O {0.83s} 10. Ng5 {0.59s} Ng4 {0.81s} 11. f4 {0.58s} Bxg5 {0.80s}\n" +
                "12. fxg5 {0.57s} Qd7 {0.78s} 13. a4 {0.56s} Nxe3 {0.77s} 14. Qxe3 {0.55s}\n" +
                "Nb4 {0.75s} 15. Nb5 {0.54s} a5 {0.74s} 16. Nc3 {0.53s} Qc6 {0.72s}\n" +
                "17. Rab1 {0.52s} Rfd8 {0.71s} 18. Rbc1 {0.51s} d5 {0.70s} 19. Bb5 d4 {0.69s}\n" +
                "20. Bxc6 dxe3 {0.67s} 21. Bxb7 Rab8 {0.66s} 22. Bd5 Bxd5 {0.65s} 23. exd5\n" +
                "Nxd5 {0.64s} 24. Rce1 Rxb2 {0.62s} 25. Nxd5 Rxd5 {0.61s} 26. Rxe3 Kf8 {0.60s}\n" +
                "27. Rc3 c5 {0.59s} 28. Rcf3 Rxg5 {0.58s} 29. Rxf7+ Ke8 {0.56s} 30. R7f2\n" +
                "Rg4 {0.56s} 31. Re2+ Kd7 {0.54s} 32. Rf7+ Kd6 {0.54s} 33. Kf2 Ra2 {0.53s}\n" +
                "34. Ra7 Raxa4 {0.51s} 35. Ree7 Ra2 {0.51s} 36. Red7+ Ke5 37. Re7+ Kd5 38. Rxg7\n" +
                "Rxc2+ 39. Kf3 h5 40. Rxg4 hxg4+ {1.5s} 41. Kg3 {1.1s} Ra2 {1.5s} 42. Rg7 {1.1s}\n" +
                "c4 {1.5s} 43. Rg5+ {1.1s} Ke6 {1.4s} 44. Rc5 {1.1s} Ra4 {1.4s} 45. Kxg4 {1.0s}\n" +
                "Kd6 {1.4s} 46. Rc8 {1.0s} Kd7 {1.4s} 47. Rc5 {0.99s} Kd6 {1.3s} 48. Rc8 {0.97s}\n" +
                "Kd7 {1.3s} 49. Rc5 {0.95s} Kd6" +
                "";

        String pgn3 = "" +
                "1. e4 {book} c6 {book} 2. d4 {0.70s} d5 {0.95s} 3. e5 {0.69s} e6 {0.94s}\n" +
                "4. Nf3 {0.67s} c5 {0.92s} 5. Nc3 {0.66s} Bd7 {0.90s} 6. Be2 {0.65s} cxd4 {0.88s}\n" +
                "7. Nxd4 {0.63s} Bb4 {0.86s} 8. Qd3 {0.62s} Ne7 {0.85s} 9. O-O {0.61s}\n" +
                "O-O {0.83s} 10. Be3 {0.60s} Nbc6 {0.81s} 11. Nxc6 {0.58s} Nxc6 {0.80s}\n" +
                "12. f4 {0.57s} Qa5 {0.78s} 13. a4 {0.56s} Bc5 {0.77s} 14. Bxc5 {0.55s}\n" +
                "Qxc5+ {0.75s} 15. Kh1 {0.54s} a6 {0.74s} 16. Qh3 {0.53s} Nb4 {0.72s}\n" +
                "17. Bd3 {0.52s} Nxd3 {0.71s} 18. Qxd3 {0.51s} a5 {0.70s} 19. Nb5 Bxb5 {0.68s}\n" +
                "20. axb5 Rfc8 {0.67s} 21. c3 Qb6 {0.66s} 22. b3 Rc5 {0.65s} 23. c4 Rac8 {0.64s}\n" +
                "24. Qd4 Rxb5 {0.62s} 25. Qxb6 Rxb6 {0.61s} 26. cxd5 exd5 {0.60s} 27. Rxa5\n" +
                "Rd8 {0.59s} 28. Rf3 Kf8 {0.58s} 29. Rd3 Ke7 {0.57s} 30. Raxd5 Rxd5 {0.56s}\n" +
                "31. Rxd5 Rxb3 {0.54s} 32. Kg1 Rb2 {0.54s} 33. f5 g6 {0.53s} 34. Rd6 gxf5 {0.52s}\n" +
                "35. Rf6 Rc2 {0.51s} 36. Rxf5 Re2 37. Kf1 Re4 38. Kg1 Rh4 39. Rf6 Rb4 40. Kf2\n" +
                "Rb5 {1.5s} 41. Rf5 {1.1s} h6 {1.5s} 42. Ke3 {1.1s} Ke6 {1.5s} 43. Rf6+ {1.1s}\n" +
                "Kxe5 {1.4s} 44. Rxf7 {1.1s} Rb3+ {1.4s} 45. Ke2 {1.0s} Rb6 {1.4s}\n" +
                "46. Re7+ {1.0s} Kd4 {1.4s} 47. Rc7 {0.99s} Ke4 {1.3s} 48. Rc4+ {0.97s}\n" +
                "Ke5 {1.3s} 49. Kf3 {0.95s} Rb3+ {1.3s} 50. Ke2 {0.93s} Rb2+ {1.3s}\n" +
                "51. Kf3 {0.91s} Rb3+ {1.2s} 52. Ke2 {0.90s} Rb2+ {1.2s} 53. Kf3 {0.88s}\n" +
                "Rb3+" +
                "";

        String pgn4 = "" +
                "1. e4 {book} e6 {book} 2. d4 {0.97s} d5 {0.68s} 3. Nc3 {0.94s} Bb4 {0.66s}\n" +
                "4. exd5 {0.92s} exd5 {0.64s} 5. Bd3 {0.89s} Nf6 {0.62s} 6. g1e2 {0.87s}\n" +
                "O-O {0.61s} 7. O-O {0.85s} Nc6 {0.59s} 8. Bg5 {0.82s} Be6 {0.57s} 9. a3 {0.80s}\n" +
                "Bxc3 {0.56s} 10. Nxc3 {0.78s} h6 {0.54s} 11. Be3 {0.76s} a5 {0.53s}\n" +
                "12. Qf3 {0.74s} Re8 {0.51s} 13. Nb5 {0.72s} Na7 {0.50s} 14. Nxa7 {0.70s} Rxa7\n" +
                "15. Bf4 {0.68s} a4 16. Rfe1 {0.66s} c6 17. Be5 {0.65s} Nd7 18. Bf5 {0.63s} Qb6\n" +
                "19. Bxe6 {0.61s} Rxe6 20. Qc3 {0.59s} Nxe5 21. Rxe5 {0.58s} Rxe5\n" +
                "22. dxe5 {0.56s} c5 23. Rb1 {0.55s} d4 24. Qc4 {0.54s} Ra8 25. b4 {0.52s} axb3\n" +
                "26. Qxb3 {0.51s} Qxb3 27. Rxb3 c4 28. Rxb7 Rxa3 29. g4 Rc3 30. Rb8+ Kh7 31. Rd8\n" +
                "d3 32. cxd3 Rxd3 33. Rc8 Rd4 34. h3 Rd5 35. f4 Rd4 36. f5 Re4 37. Kf2 Rxe5\n" +
                "38. Rc7 Rd5 39. Ke2 Re5+ 40. Kd2 Rd5+ {1.0s} 41. Ke2 {1.4s} Re5+ {1.00s}\n" +
                "42. Kf3 {1.4s} Ra5 {0.97s} 43. Rxc4 {1.3s} Ra3+ {0.94s} 44. Kg2 {1.3s}\n" +
                "Ra2+ {0.92s} 45. Kf3 {1.3s} Ra3+ {0.90s} 46. Kg2 {1.2s} Ra2+ {0.87s}\n" +
                "47. Kf3 {1.2s} Ra3+" +
                "";

        String pgn5 = "" +
                "1. e4 {book} e5 {book} 2. Nf3 {book} Nc6 {book} 3. Bc4 {book} Nf6 {0.97s}\n" +
                "4. O-O {0.70s} Bc5 {0.95s} 5. Nc3 {0.69s} O-O {0.93s} 6. d3 {0.67s} d6 {0.92s}\n" +
                "7. Bd5 {0.66s} Qe7 {0.90s} 8. Bg5 {0.65s} Bg4 {0.88s} 9. Bxc6 {0.63s}\n" +
                "bxc6 {0.86s} 10. h3 {0.62s} Bd7 {0.85s} 11. Qd2 {0.61s} Rab8 {0.83s}\n" +
                "12. Na4 {0.60s} Bb4 {0.81s} 13. c3 {0.58s} c5 {0.80s} 14. cxb4 {0.57s}\n" +
                "Bxa4 {0.78s} 15. bxc5 {0.56s} dxc5 {0.77s} 16. Rfc1 {0.55s} Qd6 {0.75s}\n" +
                "17. Qc3 {0.54s} Nd7 {0.74s} 18. Be3 {0.53s} Rfe8 {0.72s} 19. Bxc5 {0.52s}\n" +
                "Nxc5 {0.71s} 20. Qxc5 {0.51s} Rxb2 {0.70s} 21. Qxc7 Qb6 {0.69s} 22. Rc5\n" +
                "f6 {0.67s} 23. Nh4 g6 {0.66s} 24. Nf3 Rb1+ {0.65s} 25. Rxb1 Qxb1+ {0.64s}\n" +
                "26. Kh2 Qxd3 {0.62s} 27. Ra5 Qc2 {0.61s} 28. Qd6 Qxe4 {0.60s} 29. Qxf6\n" +
                "Bc6 {0.59s} 30. Kg3 Bd5 {0.58s} 31. Rxa7 Bb7 {0.57s} 32. h4 Qd5 {0.56s} 33. Qb6\n" +
                "Re7 {0.55s} 34. Ra5 Qd3 {0.54s} 35. Qe3 Qxe3 {0.53s} 36. fxe3 e4 {0.51s} 37. Nd4\n" +
                "Rf7 {0.51s} 38. Ne6 Rf6 39. Nc5 Bc6 40. Ra6 Kg7 {1.5s} 41. Ra7+ {1.1s}\n" +
                "Kg8 {1.5s} 42. Re7 {1.1s} Bd5 {1.5s} 43. Nxe4 {1.1s} Ra6 {1.5s} 44. a4 {1.1s}\n" +
                "Kf8 {1.4s} 45. Re5 {1.0s} Bc6 {1.4s} 46. Ng5 {1.0s} Bxa4 {1.4s} 47. Nxh7+ {1.0s}\n" +
                "Kf7 {1.3s} 48. Ng5+ {0.98s} Kg7 {1.3s} 49. e4 {0.96s} Kf6 {1.3s} 50. Kf4 {0.94s}\n" +
                "Rb6 {1.3s} 51. Rd5 {0.92s} Be8 {1.2s} 52. Rd2 {0.91s} Ke7 {1.2s} 53. Nf3 {0.89s}\n" +
                "Rf6+ {1.2s} 54. Ke3 {0.87s} Rb6 {1.2s} 55. Ne5 {0.85s} Rb1 {1.1s}\n" +
                "56. Rd5 {0.84s} Rg1 {1.1s} 57. g4 {0.82s} Ra1 {1.1s} 58. Rd2 {0.80s} Ke6 {1.1s}\n" +
                "59. Nf3 {0.79s} Bd7 {1.1s} 60. g5 {0.77s} Rc1 {1.0s} 61. Rd5 {0.76s} Ra1 {1.0s}\n" +
                "62. Ne5 {0.74s} Be8 {1.0s} 63. Nc4 {0.73s} Re1+ {0.98s} 64. Kf3 {0.71s}\n" +
                "Rf1+ {0.96s} 65. Kg2 {0.70s} Rc1 {0.95s} 66. Ne5 {0.68s} Re1 {0.93s}\n" +
                "67. Kf3 {0.67s} Rh1 {0.91s} 68. Kg4 {0.66s} Ra1 {0.89s} 69. Kf4 {0.64s}\n" +
                "Ke7 {0.88s} 70. Rd4 {0.63s} Ke6 {0.86s} 71. Ng4 {0.62s} Rf1+ {0.84s}\n" +
                "72. Kg3 {0.60s} Rg1+ {0.83s} 73. Kf3 {0.59s} Rh1 {0.81s} 74. Nf6 {0.58s}\n" +
                "Bf7 {0.80s} 75. Rd7 {0.57s} Rf1+ {0.78s} 76. Ke3 {0.56s} Re1+ {0.76s}\n" +
                "77. Kf2 {0.55s} Rh1 {0.75s} 78. Rc7 {0.54s} Rxh4 {0.74s} 79. Ke3 {0.53s}\n" +
                "Rh1 {0.72s} 80. Nd5 {0.51s} Re1+ {1.8s} 81. Kd4 {1.3s} Rd1+ {1.7s}\n" +
                "82. Kc4 {1.3s} Rc1+ {1.7s} 83. Nc3 {1.3s} Rg1 {1.7s} 84. Rc5 {1.2s} Rg2 {1.6s}\n" +
                "85. Kd4 {1.2s} Rd2+ {1.6s} 86. Ke3 {1.2s} Rc2 {1.6s} 87. Rc7 {1.2s} Bg8 {1.5s}\n" +
                "88. Kd3 {1.1s} Rg2 {1.5s} 89. Rc5 {1.1s} Kd6 {1.5s} 90. Ra5 {1.1s} Be6 {1.5s}\n" +
                "91. Ne2 {1.1s} Bc4+ {1.4s} 92. Kxc4 {1.0s} Rxe2 {1.4s} 93. Rd5+ {1.0s}\n" +
                "Ke6 {1.4s} 94. Kd3 {1.0s} Ra2 {1.3s} 95. Ke3 {0.98s} Ra1 {1.3s} 96. Rc5 {0.96s}\n" +
                "Re1+ {1.3s} 97. Kd3 {0.94s} Rd1+ {1.3s} 98. Ke2 {0.93s} Ra1 {1.2s}\n" +
                "99. Kf2 {0.91s} Ra3 {1.2s} 100. Rb5 {0.89s} Kd6 {1.2s} 101. Ke2 {0.87s}\n" +
                "Ke6 {1.2s} 102. Rd5 {0.85s} Ra1 {1.2s} 103. Ke3 {0.84s} Re1+ {1.1s}\n" +
                "104. Kf4 {0.82s} Rf1+ {1.1s} 105. Kg4 {0.80s} Rg1+ {1.1s} 106. Kf3 {0.79s}\n" +
                "Rf1+ {1.1s} 107. Kg3 {0.77s} Re1 {1.0s} 108. Kf4 {0.76s} Rf1+ {1.0s} 109. Kg4\n" +
                "Rg1+ {1.0s} 110. Kf3 {0.74s} Rf1+ {1.0s} 111. Kg3 {0.73s} Re1 {0.98s}\n" +
                "112. Kf4" +
                "";

        String pgn6 = "" +
                "1. e4 {book} e5 {book} 2. Nf3 {book} Nc6 {book} 3. Bc4 {book} Nf6 {0.70s}\n" +
                "4. Qe2 {0.97s} d6 {0.68s} 5. d3 {0.94s} Bg4 {0.66s} 6. Be3 {0.92s} d5 {0.64s}\n" +
                "7. exd5 {0.89s} Nxd5 {0.63s} 8. O-O {0.87s} Be7 {0.61s} 9. Nc3 {0.85s}\n" +
                "Nxc3 {0.59s} 10. bxc3 {0.82s} O-O {0.57s} 11. Rab1 {0.80s} Rb8 {0.56s}\n" +
                "12. h3 {0.78s} Bxf3 {0.54s} 13. Qxf3 {0.76s} Qd6 {0.53s} 14. Bd5 {0.74s}\n" +
                "Qa3 {0.51s} 15. Bxc6 {0.72s} bxc6 16. Rxb8 {0.70s} Rxb8 17. Qxc6 {0.68s} Bd6\n" +
                "18. Qc4 {0.66s} Qa5 19. f4 {0.65s} exf4 20. Bxf4 {0.63s} Qb5 21. Bxd6 {0.61s}\n" +
                "Qxc4 22. dxc4 {0.59s} cxd6 23. Rd1 {0.58s} Kf8 24. Rxd6 {0.56s} Rb2\n" +
                "25. Rd2 {0.55s} Rxa2 26. Kf2 {0.54s} Ra3 27. Rd8+ {0.52s} Ke7 28. Rd3 {0.51s}\n" +
                "Ra4 29. Rd4 Ra3 30. Rd3 Ra4 31. Re3+ Kf6 32. Rf3+ Ke7 33. Re3+ Kf6 34. Rf3+ Ke7\n" +
                "35. Re3+" +
                "";

        String pgn7 = "" +
                "1. e4 {book} e5 {book} 2. Nf3 {0.97s} Nc6 {0.68s} 3. Nc3 {0.94s} Bb4 {0.66s}\n" +
                "4. Bb5 {0.92s} Nf6 {0.64s} 5. O-O {0.89s} O-O {0.63s} 6. d3 {0.87s} Nd4 {0.61s}\n" +
                "7. Nxd4 {0.85s} exd4 {0.59s} 8. Nd5 {0.82s} Nxd5 {0.57s} 9. exd5 {0.80s}\n" +
                "c6 {0.56s} 10. Bc4 {0.78s} b5 {0.54s} 11. Bb3 {0.76s} Qc7 {0.53s}\n" +
                "12. Bd2 {0.74s} Bd6 {0.51s} 13. Qh5 {0.72s} Bb7 {0.50s} 14. a4 {0.70s} b4\n" +
                "15. dxc6 {0.68s} dxc6 16. a5 {0.66s} c5 17. a6 {0.65s} Bc6 18. f4 {0.63s} Be7\n" +
                "19. f5 {0.61s} Bf6 20. Qg4 {0.59s} Be5 21. Bh6 {0.58s} Rfe8 22. Kh1 {0.56s} Qd7\n" +
                "23. Bg5 {0.55s} Bc7 24. Kg1 {0.54s} Rab8 25. Bf6 {0.52s} g6 26. Qg5 {0.51s} Re2\n" +
                "27. fxg6 Rxg2+ 28. Qxg2 Bxg2 29. Bxf7+ Kf8 30. Bb3 Bxf1 31. g7+ Qxg7+ 32. Bxg7+\n" +
                "Kxg7 33. Rxf1 Rf8 34. Rxf8 Kxf8 35. Bd5 Bf4 36. h4 Ke7 37. Be4 h6 38. Kf2 Bc1\n" +
                "39. b3 Kd6 40. Kf3 Be3 {1.0s} 41. Bf5 {1.4s} Kd5 {1.00s} 42. Be4+ {1.4s}\n" +
                "Ke6 {0.97s} 43. Bc6 {1.3s} Ke5 {0.94s} 44. Be4 {1.3s} Bf4 {0.92s} 45. Ba8 {1.3s}\n" +
                "Bc1 {0.89s} 46. Bb7 {1.2s} Bd2 {0.87s} 47. Ke2 {1.2s} Bf4 {0.84s} 48. Kf3 {1.2s}\n" +
                "Bc1 {0.82s} 49. Be4 {1.1s} Bd2 {0.80s} 50. Ke2 {1.1s} Bf4 {0.78s} 51. Kf3 {1.1s}\n" +
                "Be3 {0.76s} 52. Bb7 {1.0s} Bg1 {0.73s} 53. Bc8 {1.0s} Bh2 {0.71s}\n" +
                "54. Bb7 {0.99s} Kf5 {0.69s} 55. Bc8+ {0.96s} Ke5 {0.68s} 56. Bb7 {0.94s}\n" +
                "Kf5 {0.66s} 57. Bc8+ {0.91s} Ke5" +
                "";

        String pgn8 = "" +
                "1. e4 {0.97s} e5 {0.68s} 2. Nf3 {0.94s} d5 {0.66s} 3. Nxe5 {0.92s} Bd6 {0.64s}\n" +
                "4. d4 {0.89s} dxe4 {0.63s} 5. Bc4 {0.87s} Bxe5 {0.61s} 6. Qh5 {0.85s}\n" +
                "Qe7 {0.59s} 7. dxe5 {0.82s} Nc6 {0.57s} 8. Bb5 {0.80s} Bd7 {0.56s}\n" +
                "9. Bxc6 {0.78s} Bxc6 {0.54s} 10. O-O {0.76s} g6 {0.53s} 11. Qg5 {0.74s}\n" +
                "f6 {0.51s} 12. exf6 {0.72s} Nxf6 13. Nc3 {0.70s} O-O 14. Bf4 {0.68s} a5\n" +
                "15. Nb5 {0.66s} Bxb5 16. Qxb5 {0.65s} c6 17. Qg5 {0.63s} Qe6 18. Qc5 {0.61s} Nd5\n" +
                "19. Bh6 {0.60s} Rf7 20. Bd2 {0.58s} b6 21. Qc4 {0.56s} b5 22. Qd4 {0.55s} Rd7\n" +
                "23. Be3 {0.54s} Nf4 24. Qc5 {0.52s} Rd5 25. Qb6 {0.51s} g5 26. f3 Rd7 27. Rae1\n" +
                "Nd5 28. Qc5 Nxe3 29. Qxg5+ Rg7 30. Qxe3 Qxa2 31. Qxe4 Qxb2 32. Qxc6 Rf8 33. Re4\n" +
                "Rg5 34. f4 Rgf5 35. Rf3 Qc1+ 36. Kf2 Qd2+ 37. Kf1 Qd1+ 38. Re1 Qd8 39. Qe6+ Kh8\n" +
                "40. Qe3 Qf6 {1.0s} 41. Rd1 {1.4s} Rxf4 {0.99s} 42. Rxf4 {1.4s} Qxf4+ {0.96s}\n" +
                "43. Qxf4 {1.3s} Rxf4+ {0.94s} 44. Ke2 {1.3s} Re4+ {0.91s} 45. Kd3 {1.3s}\n" +
                "Re5 {0.89s} 46. Ra1 {1.2s} b4 {0.86s} 47. c4 {1.2s} bxc3 {0.84s} 48. Kxc3 {1.2s}\n" +
                "Kg7 {0.82s} 49. Rb1 {1.1s} Re2 {0.79s} 50. Rb2 {1.1s} Re5 {0.77s} 51. Rf2 {1.1s}\n" +
                "Rh5 {0.75s} 52. h3 {1.0s} Kg6 {0.73s} 53. Kd4 {1.0s} Rb5 {0.71s} 54. Ke4 {0.98s}\n" +
                "a4 {0.69s} 55. Kd4 {0.96s} a3 {0.67s} 56. Kc3 {0.94s} Re5 {0.65s}\n" +
                "57. Ra2 {0.90s} Re3+ {0.64s} 58. Kd4 {0.89s} Rg3 {0.62s} 59. Kd5 {0.86s}\n" +
                "Re3 {0.60s} 60. Rc2 {0.84s} Rb3 {0.58s} 61. Ra2 {0.81s} Kf6 {0.57s}\n" +
                "62. Kc4 {0.79s} Re3 {0.55s} 63. Kd4 {0.77s} Rb3 {0.54s} 64. Kd5 {0.75s}\n" +
                "Rd3+ {0.52s} 65. Ke4 {0.73s} Rb3 {0.51s} 66. Kd5 {0.71s} Rd3+ 67. Ke4 {0.69s}\n" +
                "Rb3 68. Kd5" +
                "";

        String pgn9 = "" +
                "1. e4 {0.97s} e5 {0.68s} 2. Nf3 {0.94s} d5 {0.66s} 3. Nxe5 {0.92s} Bd6 {0.64s}\n" +
                "4. d4 {0.89s} dxe4 {0.63s} 5. Bc4 {0.87s} Bxe5 {0.61s} 6. Qh5 {0.85s}\n" +
                "Qe7 {0.59s} 7. dxe5 {0.83s} Nc6 {0.57s} 8. Bb5 {0.80s} g6 {0.56s} 9. Qg5 {0.78s}\n" +
                "f6 {0.54s} 10. exf6 {0.76s} Nxf6 {0.53s} 11. O-O {0.74s} Bf5 {0.51s}\n" +
                "12. Bxc6+ {0.72s} bxc6 13. Nc3 {0.70s} O-O 14. Be3 {0.68s} Nd5 15. Qxe7 {0.66s}\n" +
                "Nxe7 16. Rfd1 {0.65s} Rab8 17. b3 {0.63s} a5 18. Bg5 {0.61s} Rbe8 19. a4 {0.60s}\n" +
                "Kg7 20. Bf4 {0.58s} Bg4 21. Be5+ {0.57s} Kf7 22. Bxc7 {0.55s} Bxd1\n" +
                "23. Rxd1 {0.53s} Nf5 24. Bxa5 {0.52s} e3 25. Bb4 {0.51s} Rh8 26. Kf1 Rd8 27. Bc5\n" +
                "Rxd1+ 28. Nxd1 exf2 29. Ne3 Rd8 30. Nxf5 gxf5 31. Be3 Ke6 32. Kxf2 c5 33. Kf3\n" +
                "Kd5 34. Bf2 Kc6 35. Bg3 Rd2 36. c4 Rd3+ 37. Kf4 Rxb3 38. Kxf5 Rb2 39. Kf6 Rxg2\n" +
                "40. a5 Ra2 {1.0s} 41. Kg7 {1.4s} Rxa5 {0.99s} 42. Kxh7 {1.4s} Ra4 {0.96s}\n" +
                "43. Kg6 {1.3s} Rxc4 {0.94s} 44. Kf5 {1.3s} Kd5 {0.91s} 45. h4 {1.3s} Rc1 {0.89s}\n" +
                "46. Bf4 {1.2s} Rh1 {0.86s} 47. Bd2 {1.2s} c4 {0.84s} 48. Bc3 {1.2s} Rxh4 {0.82s}\n" +
                "49. Be1 {1.1s} Rh3 {0.80s} 50. Kg4 {1.1s} Rd3 {0.77s} 51. Bb4 {1.1s} c3 {0.75s}\n" +
                "52. Be7 {1.0s} c2 {0.73s} 53. Bg5 {1.0s} Ke4 {0.71s} 54. Bh6 {0.98s} Re3 {0.69s}\n" +
                "55. Bxe3 {0.95s} Kxe3 {0.67s} 56. Kf5 {0.93s} c1=Q {0.64s} 57. Ke6 {0.91s} Qc6+\n" +
                "58. Ke7 {0.88s} Ke4 59. Kf7 {0.86s} Qd7+ 60. Kg6 {0.83s} Qe7 61. Kh5 Qf7+\n" +
                "62. Kh6 {0.58s} Qf6+ 63. Kh7 {0.80s} Kd4 64. Kg8 Ke5 65. Kh7 Kd4 66. Kg8 Ke5\n" +
                "67. Kh7 Kd4" +
                "";
        
        String pgn10 = "" +
                "1. e4 {0.97s} e5 {0.68s} 2. Nf3 {0.94s} d5 {0.66s} 3. Nxe5 {0.92s} Bd6 {0.64s}\n" +
                "4. d4 {0.89s} dxe4 {0.63s} 5. Bc4 {0.87s} Bxe5 {0.61s} 6. Qh5 {0.85s}\n" +
                "Qe7 {0.59s} 7. dxe5 {0.83s} Nc6 {0.57s} 8. Bb5 {0.80s} Bd7 {0.56s}\n" +
                "9. Bxc6 {0.78s} Bxc6 {0.54s} 10. O-O {0.76s} g6 {0.53s} 11. Qg5 {0.74s}\n" +
                "f6 {0.51s} 12. exf6 {0.72s} Nxf6 {0.50s} 13. Nc3 {0.70s} O-O 14. Bf4 {0.68s} a6\n" +
                "15. Qe5 {0.66s} Qxe5 16. Bxe5 {0.65s} Rf7 17. a4 {0.63s} Re8 18. Bd4 {0.61s} Ng4\n" +
                "19. Rfd1 {0.60s} Rf5 20. h3 {0.58s} Ne5 21. Re1 {0.56s} Nc4 22. b4 {0.55s} Nd6\n" +
                "23. Kh2 {0.53s} g5 24. Rf1 {0.52s} Ref8 25. Kg3 {0.51s} Re8 26. Kh2 Ref8 27. Kg3\n" +
                "Re8 28. Kh2" +
                "";
        
        String pgn11 = "" +
                "1. b4 {book} e5 {0.70s} 2. b5 {0.97s} d5 {0.68s} 3. d4 {0.94s} Nd7 {0.66s}\n" +
                "4. dxe5 {0.92s} Nxe5 {0.64s} 5. Nc3 {0.89s} d4 {0.62s} 6. Na4 {0.87s}\n" +
                "Bb4+ {0.61s} 7. Bd2 {0.85s} Ba3 {0.59s} 8. Nf3 {0.82s} Nxf3+ {0.57s}\n" +
                "9. exf3 {0.80s} Nf6 {0.56s} 10. Bd3 {0.78s} Be6 {0.54s} 11. O-O {0.76s}\n" +
                "Be7 {0.53s} 12. c3 {0.74s} O-O {0.51s} 13. Qc2 {0.72s} a6 14. bxa6 {0.70s} bxa6\n" +
                "15. cxd4 {0.68s} Qxd4 16. Bc3 {0.66s} Qh4 17. Be5 {0.65s} Rfd8 18. Be4 {0.63s}\n" +
                "Nxe4 19. fxe4 {0.61s} Bd6 20. Bxd6 {0.60s} cxd6 21. Nb6 {0.58s} Rab8\n" +
                "22. Rab1 {0.56s} Qg5 23. a4 {0.55s} Qc5 24. Qb2 {0.54s} Qe5 25. Qc2 {0.52s} Qc5\n" +
                "26. Qb2 {0.51s} Qe5 27. Qc2 Qc5" +
                "";
        
        String pgn12 = "" +
                "1. g3 {book} e5 {0.70s} 2. e4 {0.97s} d5 {0.68s} 3. d4 {0.94s} dxe4 {0.66s}\n" +
                "4. dxe5 {0.92s} Qxd1+ {0.64s} 5. Kxd1 Nc6 {0.64s} 6. Bf4 {0.89s} f6 {0.63s}\n" +
                "7. Bb5 {0.87s} Bf5 {0.61s} 8. Nd2 {0.85s} Bc5 {0.59s} 9. Bxc6+ {0.82s}\n" +
                "bxc6 {0.57s} 10. Ne2 {0.80s} Bxf2 {0.56s} 11. g4 {0.78s} Bxg4 {0.54s}\n" +
                "12. Nxe4 {0.76s} O-O-O+ {0.53s} 13. Kc1 {0.74s} Bd4 {0.51s} 14. N2c3 {0.72s}\n" +
                "fxe5 15. Bg5 {0.70s} Re8 16. Kd2 {0.68s} Ne7 17. Rhf1 {0.66s} Nf5\n" +
                "18. Kd3 {0.65s} h6 19. Bd2 {0.63s} Rd8 20. Kc4 {0.61s} Bb6 21. Rae1 {0.59s} Nd4\n" +
                "22. Rc1 {0.58s} Be2+ 23. Nxe2 {0.56s} Nxe2 24. Bc3 {0.55s} Nxc1 25. Rxc1 {0.54s}\n" +
                "Rhf8 26. Re1 {0.52s} Rd5 27. Re2 {0.51s} Kd7 28. Rg2 Rf4 29. Re2 Bg1 30. h3 Rh4\n" +
                "31. Re1 Bb6 32. b4 Rxh3 33. a3 Ke6 34. Re2 Kf5 35. Nd2 Be3 36. Nf1 Bd4 37. Be1\n" +
                "Rxa3 38. c3 Bb6 39. Bg3 a5 40. Bf2 Ke6 {1.0s} 41. Ne3 {1.4s} Bxe3 {1.0s}\n" +
                "42. Bxe3 {1.4s} axb4 {0.98s} 43. Kxb4 {1.3s} Rda5 {0.95s} 44. Kc4 {1.3s}\n" +
                "R3a4+ {0.93s} 45. Kb3 {1.3s} Ra3+ {0.90s} 46. Kc4 {1.2s} R3a4+ {0.88s}\n" +
                "47. Kb3 {1.2s} Ra3+ {0.85s} 48. Kc4" +
                "";
        
        String pgn13 = "" +
                "1. f4 {book} d5 {0.97s} 2. d4 {0.70s} Bf5 {0.95s} 3. Nf3 {0.69s} e6 {0.93s}\n" +
                "4. e3 {0.67s} Nf6 {0.91s} 5. Bd2 {0.66s} Be7 {0.90s} 6. Be2 {0.65s} O-O {0.88s}\n" +
                "7. O-O {0.63s} c5 {0.86s} 8. dxc5 {0.62s} Bxc5 {0.85s} 9. b4 {0.61s} Bb6 {0.83s}\n" +
                "10. Nd4 {0.60s} Bxd4 {0.81s} 11. exd4 {0.58s} Qc7 {0.80s} 12. Nc3 {0.57s}\n" +
                "Qb6 {0.78s} 13. g4 {0.56s} Qxd4+ {0.77s} 14. Rf2 {0.55s} Ne4 {0.75s}\n" +
                "15. Nxe4 {0.54s} Bxe4 {0.74s} 16. Rc1 {0.53s} Qb2 {0.72s} 17. c4 {0.52s}\n" +
                "Qxa2 {0.71s} 18. b5 {0.51s} Qb2 {0.70s} 19. cxd5 exd5 {0.69s} 20. Rc7 a6 {0.67s}\n" +
                "21. Bc3 Qb1 {0.66s} 22. Qxb1 Bxb1 {0.65s} 23. Bf3 Be4 {0.64s} 24. Bxe4\n" +
                "dxe4 {0.62s} 25. Ra2 b6 {0.61s} 26. Rb7 Rd8 {0.60s} 27. Rxb6 Nd7 {0.59s} 28. Rd6\n" +
                "Kf8 {0.58s} 29. bxa6 Ra7 {0.57s} 30. Re2 Rb8 {0.56s} 31. Bd4 Ke7 {0.55s}\n" +
                "32. Bxa7 Rb1+ {0.54s} 33. Kg2 Kxd6 {0.53s} 34. Bd4 Rb3 {0.52s} 35. Ra2\n" +
                "Rb8 {0.51s} 36. a7 Ra8 37. Bxg7 Nb6 38. Be5+ Kc5 39. Ra5+ Kc6 40. Bd4 Nd5 {1.5s}\n" +
                "41. f5 {1.1s} Nb4 {1.5s} 42. Re5 {1.1s} Kd6 {1.5s} 43. Rxe4 {1.1s} Kd5 {1.4s}\n" +
                "44. Kf3 {1.1s} Nc6 {1.4s} 45. Bb6 {1.0s} Ne5+ {1.4s} 46. Kf4 {1.0s} Nd3+ {1.4s}\n" +
                "47. Kf3 {0.99s} Ne5+ {1.3s} 48. Kf4 {0.97s} Nd3+ {1.3s} 49. Kf3 {0.95s}\n" +
                "Ne5+" +
                "";
        
        String pgn14 = "" +
                "1. f4 {book} d5 {0.70s} 2. d4 {0.97s} Nf6 {0.68s} 3. e3 {0.94s} Bf5 {0.66s}\n" +
                "4. Bb5+ {0.92s} Nbd7 {0.64s} 5. Nf3 {0.89s} e6 {0.63s} 6. O-O {0.87s}\n" +
                "Bd6 {0.61s} 7. Bd2 {0.85s} c6 {0.59s} 8. Bd3 {0.82s} O-O {0.57s} 9. Nc3 {0.80s}\n" +
                "Qb6 {0.56s} 10. Rb1 {0.78s} c5 {0.54s} 11. Qe2 {0.76s} Be4 {0.53s}\n" +
                "12. Ne5 {0.74s} cxd4 {0.51s} 13. Nxd7 {0.72s} Nxd7 {0.50s} 14. Bxe4 {0.70s} dxc3\n" +
                "15. bxc3 {0.68s} Qc6 16. Bd3 {0.66s} Nb6 17. e4 {0.65s} f5 18. exf5 {0.63s} Bc5+\n" +
                "19. Kh1 {0.61s} exf5 20. Rfe1 {0.60s} Rae8 21. Qf3 {0.58s} Rxe1+\n" +
                "22. Rxe1 {0.56s} Qd6 23. Be3 {0.55s} Qc6 24. Bd2 {0.54s} Qd6 25. Be3 {0.52s} Qc6\n" +
                "26. Bd2" +
                "";
        
        String pgn15 = "" +
                "1. g4 {book} d5 {0.70s} 2. d4 {0.97s} Bxg4 {0.68s} 3. h3 {0.94s} Bh5 {0.66s}\n" +
                "4. Nf3 {0.92s} e6 {0.64s} 5. Bg2 {0.89s} Nf6 {0.63s} 6. Nc3 {0.87s} Bb4 {0.61s}\n" +
                "7. O-O {0.85s} O-O {0.59s} 8. Bf4 {0.82s} Qe7 {0.57s} 9. Nb5 {0.80s} Ba5 {0.56s}\n" +
                "10. c4 {0.78s} a6 {0.54s} 11. Nc3 {0.76s} dxc4 {0.53s} 12. Qa4 {0.74s}\n" +
                "Bb4 {0.51s} 13. Ne5 {0.72s} Bxc3 14. bxc3 {0.70s} Nd5 15. e3 {0.68s} g5\n" +
                "16. Bg3 {0.66s} Be2 17. Rfc1 {0.65s} Bd3 18. e4 {0.63s} Nf4 19. Re1 {0.61s} Ne2+\n" +
                "20. Rxe2 {0.60s} Bxe2 21. Qc2 {0.58s} Bd3 22. Nxd3 {0.56s} cxd3 23. Qxd3 {0.55s}\n" +
                "Nc6 24. Rb1 {0.54s} Rab8 25. e5 {0.52s} Qd7 26. Qf3 {0.51s} Qe7 27. Qd3 h5\n" +
                "28. Bf3 h4 29. Bh2 Kg7 30. Kh1 Rh8 31. Kg1 a5 32. Qc4 Qd7 33. Qd3 Qe7 34. Qc4\n" +
                "Qd7 35. Qd3 Qe7" +
                "";
        
        String pgn16 = "" +
                "1. Nf3 {book} d5 {0.97s} 2. d4 {0.70s} Nf6 {0.95s} 3. Bf4 {0.69s} Bf5 {0.93s}\n" +
                "4. e3 {0.67s} e6 {0.92s} 5. Bb5+ {0.66s} Nbd7 {0.90s} 6. O-O {0.65s} Bd6 {0.88s}\n" +
                "7. Nc3 {0.63s} O-O {0.86s} 8. Qd2 {0.62s} Qe7 {0.85s} 9. a4 {0.61s} a5 {0.83s}\n" +
                "10. Ne5 {0.60s} Nb6 {0.81s} 11. Bg5 {0.58s} c5 {0.80s} 12. Be2 {0.57s}\n" +
                "h6 {0.78s} 13. Bxf6 {0.56s} gxf6 {0.77s} 14. dxc5 {0.55s} Bxc5 {0.75s}\n" +
                "15. Nf3 {0.54s} Rfd8 {0.74s} 16. Nd4 {0.53s} Bg6 {0.72s} 17. f4 {0.52s}\n" +
                "e5 {0.71s} 18. f5 {0.51s} Nc4 {0.70s} 19. Bxc4 dxc4 {0.69s} 20. fxg6\n" +
                "exd4 {0.67s} 21. gxf7+ Kxf7 {0.66s} 22. Nd1 c3 {0.65s} 23. Qe2 cxb2 {0.64s}\n" +
                "24. Nxb2 Qxe3+ {0.62s} 25. Qf2 Qxf2+ {0.61s} 26. Kxf2 Rac8 {0.60s} 27. Nd3\n" +
                "Bd6 {0.59s} 28. Ra2 Bxh2 {0.58s} 29. Rh1 Bd6 {0.56s} 30. Rxh6 Ke6 {0.56s}\n" +
                "31. Rh5 b6 {0.54s} 32. Rh4 Kd5 {0.54s} 33. Rb2 Rc4 {0.52s} 34. Rxb6\n" +
                "Rxc2+ {0.52s} 35. Kf1 f5 {0.51s} 36. Rb5+ Bc5 37. Rxa5 Rb8 38. Rxc5+ Rxc5\n" +
                "39. Nxc5 Kxc5 40. Rh5 Rf8 {1.5s} 41. g4 {1.1s} Kc4 {1.5s} 42. Rxf5 {1.1s}\n" +
                "Rg8 {1.5s} 43. g5 {1.1s} d3 {1.4s} 44. Ke1 {1.1s} Kc3 {1.4s} 45. Rd5 {1.0s}\n" +
                "Kc4 {1.4s} 46. Rf5 {1.0s} Kc3 {1.4s} 47. Rd5 {0.99s} Kc4 {1.3s}\n" +
                "48. Rf5" +
                "";
        
        String pgn17 = "" +
                "1. e4 {book} e6 {book} 2. d4 {0.70s} d5 {0.95s} 3. e5 {0.69s} c5 {0.93s}\n" +
                "4. Nf3 {0.67s} cxd4 {0.91s} 5. Nxd4 {0.66s} Bc5 {0.90s} 6. Nb3 {0.65s}\n" +
                "Bb6 {0.88s} 7. Bb5+ {0.63s} Nd7 {0.86s} 8. O-O {0.62s} Ne7 {0.85s}\n" +
                "9. Bxd7+ {0.61s} Bxd7 {0.83s} 10. a4 {0.59s} Ng6 {0.81s} 11. a5 {0.58s}\n" +
                "Bc7 {0.80s} 12. f4 {0.57s} f6 {0.78s} 13. exf6 {0.56s} Qxf6 {0.77s}\n" +
                "14. g3 {0.55s} O-O {0.75s} 15. Nc3 {0.54s} b6 {0.74s} 16. Be3 {0.53s}\n" +
                "bxa5 {0.72s} 17. Nxa5 {0.52s} Bb6 {0.71s} 18. Bxb6 {0.51s} axb6 {0.70s} 19. Nb3\n" +
                "Ne7 {0.68s} 20. Qd3 Nc6 {0.67s} 21. Qd2 Qe7 {0.66s} 22. Rxa8 Rxa8 {0.65s} 23. f5\n" +
                "d4 {0.64s} 24. Nb5 e5 {0.62s} 25. Nc7 Ra2 {0.61s} 26. Nc1 Rxb2 {0.60s} 27. Nd3\n" +
                "Ra2 {0.59s} 28. Nd5 Qd8 {0.58s} 29. f6 Bh3 {0.57s} 30. f7+ Kf8 {0.56s} 31. N5b4\n" +
                "Bxf1 {0.55s} 32. Nxc6 Qd6 {0.54s} 33. Kxf1 Ra1+ {0.53s} 34. Ke2 Rh1 {0.52s}\n" +
                "35. Qb4 Rxh2+ {0.51s} 36. Kf3 Qxb4 37. Ncxb4 Kxf7 38. Nxe5+ Ke6 39. Nec6 Kd6\n" +
                "40. Nxd4 Kc5 {1.5s} 41. c3 {1.1s} Kc4 {1.5s} 42. Ne2 {1.1s} Rh6 {1.5s}\n" +
                "43. Nc2 {1.1s} Rf6+ {1.4s} 44. Ke3 {1.1s} Rf8 {1.4s} 45. Kd2 {1.0s} Kd5 {1.4s}\n" +
                "46. Kd3 {1.0s} Rf3+ {1.4s} 47. Ne3+ {0.99s} Ke5 {1.3s} 48. g4 {0.97s} Rf6 {1.3s}\n" +
                "49. Nc4+ {0.95s} Kd5 {1.3s} 50. Nd4 {0.93s} h6 {1.3s} 51. Ne3+ {0.91s}\n" +
                "Ke5 {1.2s} 52. c4 {0.90s} Rf7 {1.2s} 53. Nc6+ {0.88s} Kd6 {1.2s} 54. Nb4 {0.86s}\n" +
                "Ke5 {1.2s} 55. Nbd5 {0.84s} Rd7 {1.1s} 56. Kc3 {0.83s} Rd6 {1.1s}\n" +
                "57. Kb4 {0.81s} Ke4 {1.1s} 58. Kb5 {0.79s} Rg6 {1.1s} 59. Ka4 {0.78s} Rc6 {1.1s}\n" +
                "60. Kb3 {0.76s} Re6 {1.0s} 61. Kc3 {0.75s} Rg6 {1.0s} 62. Kd2 {0.73s}\n" +
                "Rc6 {0.99s} 63. Nc3+ {0.72s} Ke5 {0.97s} 64. Kd3 {0.70s} Rf6 {0.95s}\n" +
                "65. Ncd5 {0.69s} Rg6 {0.94s} 66. Nc7 {0.68s} Rd6+ {0.92s} 67. Ke2 {0.66s}\n" +
                "Ke4 {0.90s} 68. Ncd5 {0.65s} Re6 {0.88s} 69. Nb4 {0.64s} g6 {0.87s}\n" +
                "70. Nbd5 {0.62s} Kd4 {0.85s} 71. Kd2 {0.61s} Re4 {0.83s} 72. Nb4 {0.60s}\n" +
                "Rf4 {0.82s} 73. Ke2 {0.59s} Rf8 {0.80s} 74. Nc6+ {0.57s} Kc5 {0.79s}\n" +
                "75. Ne5 {0.56s} Kd4 {0.77s} 76. Nf3+ {0.55s} Ke4 {0.76s} 77. Nd2+ {0.54s}\n" +
                "Kd4 {0.74s} 78. Nd5 {0.53s} Re8+ {0.73s} 79. Kd1 {0.52s} Re6 {0.71s}\n" +
                "80. Kc2 {0.51s} Rd6 {1.8s} 81. Nc3 {1.3s} Rc6 {1.7s} 82. Nf3+ {1.3s} Ke3 {1.7s}\n" +
                "83. Nd2 {1.2s} Kd4 {1.7s} 84. Nf3+ {1.2s} Ke3 {1.6s} 85. Nd2 {1.2s}\n" +
                "Kd4" +
                "";
        
        String pgn18 = "" +
                "1. e4 {book} e5 {book} 2. Nf3 {book} Nc6 {book} 3. Bb5 {book} Bb4 {book}\n" +
                "4. a3 {0.97s} Bc5 {0.68s} 5. O-O {0.94s} Nf6 {0.66s} 6. Nc3 {0.92s} O-O {0.64s}\n" +
                "7. Nxe5 {0.89s} Bd4 {0.63s} 8. Nf3 {0.87s} Bxc3 {0.61s} 9. dxc3 {0.85s}\n" +
                "Nxe4 {0.59s} 10. c4 {0.82s} d6 {0.57s} 11. Qe2 {0.80s} Re8 {0.56s}\n" +
                "12. Be3 {0.78s} Bd7 {0.54s} 13. Rfd1 {0.76s} a6 {0.53s} 14. Bxc6 {0.74s}\n" +
                "bxc6 {0.51s} 15. Nd2 {0.72s} Qf6 16. Qf3 {0.70s} Qxf3 17. Nxf3 {0.68s} c5\n" +
                "18. b4 {0.66s} cxb4 19. axb4 {0.65s} Rab8 20. Rdb1 {0.63s} Be6 21. Rxa6 {0.61s}\n" +
                "Nc3 22. Re1 {0.59s} Rxb4 23. c5 {0.58s} Ne2+ 24. Kh1 {0.56s} dxc5\n" +
                "25. Rxe6 {0.55s} Rxe6 26. Rxe2 {0.54s} Rb1+ 27. Re1 {0.52s} Rxe1+\n" +
                "28. Nxe1 {0.51s} c4 29. Kg1 Ra6 30. Kf1 Kf8 31. Bc5+ Kg8 32. Nf3 Ra2 33. c3 f6\n" +
                "34. Be3 Kf7 35. Nd4 Rb2 36. Ke1 Ke7 37. Nf5+ Kf7 38. Bf4 Rb5 39. Nd4 Rb1+\n" +
                "40. Kd2 c5 {1.0s} 41. Nf5 {1.4s} g5 {1.0s} 42. Bg3 {1.4s} Ke6 {0.98s}\n" +
                "43. Ne3 {1.4s} f5 {0.96s} 44. Bc7 {1.3s} Rb7 {0.93s} 45. Ba5 {1.3s} Rb2+ {0.91s}\n" +
                "46. Ke1 {1.2s} Rb1+ {0.88s} 47. Ke2 {1.2s} Rb2+ {0.86s} 48. Kf3 {1.2s}\n" +
                "Ra2 {0.83s} 49. Bd8 {1.1s} f4 {0.81s} 50. Nxc4 {1.1s} g4+ {0.79s} 51. Ke4 {1.1s}\n" +
                "Rxf2 {0.77s} 52. Bc7 {1.1s} Re2+ {0.74s} 53. Kxf4 {1.0s} Rxg2 {0.72s}\n" +
                "54. Ke4 {1.0s} Rc2 {0.70s} 55. Kd3 {0.97s} Ra2 {0.69s} 56. Ne3 {0.95s}\n" +
                "h5 {0.67s} 57. Kc4 {0.92s} h4 {0.65s} 58. Kb3 {0.90s} Ra7 {0.63s}\n" +
                "59. Bd8 {0.88s} g3 {0.61s} 60. h3 {0.85s} Rb7+ {0.59s} 61. Ka3 {0.83s}\n" +
                "Rf7 {0.58s} 62. Bxh4 {0.81s} Rf2 {0.56s} 63. Kb3 {0.79s} Re2 {0.55s}\n" +
                "64. Bg5 {0.77s} g2 {0.53s} 65. Nxg2 {0.74s} Rxg2 {0.52s} 66. Be3 {0.72s}\n" +
                "Kd5 {0.50s} 67. c4+ {0.70s} Kc6 68. Kc3 {0.69s} Rg3 69. Kd3 {0.67s} Rxh3\n" +
                "70. Ke4 {0.65s} Rh2 71. Bg5 {0.63s} Rg2 72. Bf4 {0.62s} Rb2 73. Bh6 {0.60s} Rb4\n" +
                "74. Kd3 {0.58s} Rb3+ 75. Kd2 {0.57s} Rb7 76. Kc3 {0.55s} Rh7 77. Be3 {0.54s} Rh3\n" +
                "78. Kd3 {0.52s} Rh4 79. Kc3 {0.51s} Rh7 80. Kd3 Rf7 {1.1s} 81. Ke4 {1.5s}\n" +
                "Re7+ {1.1s} 82. Kf3 {1.5s} Rd7 {1.1s} 83. Ke2 {1.5s} Rd6 {1.0s} 84. Bf2 {1.4s}\n" +
                "Rd8 {1.0s} 85. Be3 {1.4s} Rd7 {0.99s} 86. Bf2 {1.4s} Re7+ {0.96s} 87. Be3 {1.3s}\n" +
                "Ra7 {0.94s} 88. Kd3 {1.3s} Ra3+ {0.91s} 89. Kd2 {1.2s} Ra2+ {0.89s}\n" +
                "90. Kd3 {1.2s} Ra8 {0.86s} 91. Ke4 {1.2s} Rd8 {0.84s} 92. Bg1 {1.2s}\n" +
                "Re8+ {0.81s} 93. Kd3 {1.1s} Rf8 {0.79s} 94. Be3 {1.1s} Rf6 {0.77s}\n" +
                "95. Ke4 {1.1s} Rd6 {0.75s} 96. Bg1 {1.0s} Rd7 {0.73s} 97. Be3 {1.0s} Rd8 {0.71s}\n" +
                "98. Bg1 {0.98s} Rd2 {0.69s} 99. Be3 {0.95s} Rd1 {0.67s} 100. Bf2 {0.93s}\n" +
                "Rd7 {0.65s} 101. Be3 {0.90s} Rd8" +
                "";
        
        String pgn19 = "" +
                "1. e4 {book} e5 {book} 2. Nf3 {book} Nc6 {book} 3. Bc4 {book} Bc5 {book}\n" +
                "4. O-O {0.97s} Nf6 {0.68s} 5. Nc3 {0.94s} O-O {0.66s} 6. d3 {0.92s} d6 {0.64s}\n" +
                "7. Bg5 {0.89s} Be6 {0.63s} 8. Nd5 {0.87s} Bxd5 {0.61s} 9. exd5 {0.85s}\n" +
                "Na5 {0.59s} 10. Bb3 {0.82s} Nxb3 {0.57s} 11. axb3 {0.80s} a6 {0.56s}\n" +
                "12. Qd2 {0.78s} h6 {0.54s} 13. Bxf6 {0.76s} Qxf6 {0.53s} 14. b4 {0.74s}\n" +
                "Bb6 {0.51s} 15. Rfe1 {0.72s} g5 16. Qe2 {0.70s} a5 17. bxa5 {0.68s} Bxa5\n" +
                "18. Nd2 {0.66s} c6 19. dxc6 {0.65s} bxc6 20. Red1 {0.63s} d5 21. Qe3 {0.61s}\n" +
                "Bxd2 22. Qxd2 {0.60s} d4 23. c4 {0.58s} c5 24. Qe2 {0.56s} Rfe8 25. Rxa8 {0.55s}\n" +
                "Rxa8 26. Re1 {0.53s} Re8 27. Qe4 {0.52s} Qb6 28. Qd5 {0.51s} Qxb2 29. g3 Qc3\n" +
                "30. Rxe5 Rxe5 31. Qxe5 Qxd3 32. Qe8+ Kg7 33. Qe5+ Kg6 34. Qd6+ f6 35. Qxc5 Qb1+\n" +
                "36. Kg2 Qe4+ 37. Kf1 g4 38. Qc8 d3 39. Qg8+ Kf5 40. Qh7+ Ke5 {1.0s}\n" +
                "41. Qe7+ {1.4s} Kd4 {1.0s} 42. Qa7+ {1.4s} Kxc4 {0.99s} 43. Qa4+ {1.4s}\n" +
                "Kd5 {0.96s} 44. Qa8+ {1.3s} Ke5 {0.94s} 45. Qe8+ {1.3s} Kf5 {0.91s}\n" +
                "46. Qh5+ {1.3s} Ke6 47. Qe8+ {1.2s} Kf5 {0.86s} 48. Qh5+ {1.2s} Ke6\n" +
                "49. Qe8+ {1.2s} Kf5" +
                "";
        
        List<String> pgns = new ArrayList<>();
        pgns.add(pgn);
        pgns.add(pgn2);
        pgns.add(pgn3);
        pgns.add(pgn4);
        pgns.add(pgn5);
        pgns.add(pgn6);
        pgns.add(pgn7);
        pgns.add(pgn8);
        pgns.add(pgn9);
        pgns.add(pgn10);
        pgns.add(pgn11);
        pgns.add(pgn12);
        pgns.add(pgn13);
        pgns.add(pgn14);
        pgns.add(pgn15);
        pgns.add(pgn16);
        pgns.add(pgn17);
        pgns.add(pgn18);
        pgns.add(pgn19);


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

                boolean dbr = board.isDrawByRepetition(2);


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

            boolean dbr = board.isDrawByRepetition(2);
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
