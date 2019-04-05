package com.github.louism33.chesscore;

import com.github.louism33.utils.MoveParserFromAN;
import com.github.louism33.utils.PGNParser;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StressTest {

    @Test
    void crazyLongGamesTest() {

        try {
            String pgn = "" +
                    "1. e4 e5 2. Nf3 Nc6 3. d4 exd4 4. Nxd4 Bc5 5. Nxc6 Qf6 6. Qd2 dxc6 7. Nc3 Bd4 8. Bd3 Ne7 9. O-O Ng6 10. Ne2 Bb6 11. Nf4 Ne5 12. Be2 Ng4 13. Nd3 Be6 14. h3 Ne5 15. Nxe5 Qxe5 16. Qf4 Qc5 17. c3 O-O-O 18. Be3 Qd6 19. Rfd1 Qxf4 20. Bxf4 Rxd1+ 21. Bxd1 Rd8 22. Bg4 Re8 23. Re1 a5 24. Kf1 Bc5 25. Be3 Bf8 26. f3 b5 27. Kf2 a4 28. Rd1 Bd6 29. Bd4 g6 30. Bxe6+ fxe6 31. e5 Be7 32. Be3 c5 33. f4 c4 34. a3 h5 35. g3 c5 36. Kf3 Rh8 37. Ke4 Kc7 38. Rg1 Kd7 39. Rg2 Rh7 40. Rd2+ Kc6 41. Rg2 Kd7 42. Bf2 Rh8 43. g4 hxg4 44. hxg4 Rh3 45. f5 gxf5+ 46. gxf5 exf5+ 47. Kxf5 b4 48. Bg3 Rh5+ 49. Ke4 Ke6 50. Bf4 Rh3 51. Rg6+ Kf7 52. Ra6 bxc3 53. bxc3 Rxc3 54. Kf5 Rf3 55. e6+ Kg7 56. Ra7 Kf8 57. Ke4 Rd3 58. Be5 Bh4 59. Rh7 Bd8 60. Kf5 Rf3+ 61. Kg6 Re3 62. e7+ Ke8 63. exd8=Q+ Kxd8 64. Kf6 c3 65. Ke6 Kc8 66. Rc7+ Kb8 67. Rxc5+ Kb7 68. Kd5 c2 69. Bb2 Re2 70. Rc4 Rh2 71. Kc5 Rh6 72. Kd5 Rh5+ 73. Be5 Rh3 74. Bd6 Rd3+ 75. Ke6 Rd2 76. Bf4 Rf2 77. Bc1 Rg2 78. Ke5 Rh2 79. Rc3 Kb6 80. Kd4 Kb5 81. Rc8 Rg2 82. Kc3 Rg6 83. Kb2 Rg2 84. Rb8+ Ka6 85. Rd8 Kb5 86. Rd4 Rh2 87. Bd2 Kc5 88. Rd8 Rh3 89. Bb4+ Kb5 90. Rd5+ Kb6 91. Rd2 Kb5 92. Kxc2 Rh5 93. Rf2 Rg5 94. Bd2 Rc5+ 95. Kb2 Rd5 96. Rg2 Rc5 97. Rg4 Rc4 98. Rg8 Re4 99. Ra8 Rg4 100. Ra5+ Kb6 101. Bb4 Rg2+ 102. Kc3 Rg3+ 103. Kb2 Rg2+ 104. Kc1 Rg1+ 105. Kd2 Rg2+ 106. Ke1 Kc6 107. Rxa4 Kb5 108. Ra8 Ra2 109. Rc8 Rg2 110. Kd1 Ka4 111. Ra8+ Kb3 112. Be7 Rg7 113. Ra7 Rg2 114. Rb7+ Ka4 115. Rb4+ Ka5 116. Bd8+ Ka6 117. Bf6 Rf2 118. Bc3 Rg2 119. Bd2 Rh2 120. Kc2 Rg2 121. a4 Rh2 122. Rd4 Rg2 123. Kb3 Rg6 124. Bb4 Rh6 125. Kc4 Rg6 126. Re4 Rh6 127. Bc5 Kb7 128. Kb5 Rg6 129. Re7+ Kb8 130. Bb6 Rg8 131. Ba7+ Ka8 132. Bc5 Rg6 133. Rd7 Rh6 134. Rg7 Re6 135. a5 Re8 136. Ka6 Rd8 137. Rf7 Kb8 138. Ba7+ Ka8 139. Bb6 Rc8 140. Be3 Rc6+ 141. Kb5 Rc8 142. Ra7+ Kb8 143. Rg7 Ka8 144. Bc5 Rb8+ 145. Kc4 Rc8 146. Rd7 Re8 147. Bb6 Rc8+ 148. Kd5 Rg8 149. Kd6 Rg6+ 150. Kc5 Rg5+ 151. Kc6 Rg6+ 152. Kb5 Rg8 153. Rd1 Re8 154. Rh1 Rg8 155. Rh2 Re8 156. Rh3 Rg8 157. Rh5 Re8 158. Rh2 Rg8 159. Bc5 Rb8+ 160. Kc4 Rg8 161. Bd6 Rc8+ 162. Kd5 Rg8 163. Kc6 Rc8+ 164. Bc7 Rg8 165. a6 Ka7 166. Rh3 Rf8 167. Rh7 Ka8 168. Rh1 Ka7 169. Rb1 Rg8 170. Rf1 Re8 171. Bb6+ Ka8 172. Kb5 Rg8 173. Bc5 Rb8+ 174. Kc4 Rg8 175. Rf4 Rh8 176. Rg4 Re8 177. Rg3 Rh8 178. Rg2 Re8 179. Rg6 Rh8 180. Rf6 Re8 181. Rf5 Rg8 182. Rf4 Rh8 183. Rf3 Rg8 184. Rf2 Rh8 185. Rf1 Rg8 186. Re1 Rh8 187. Re2 Rg8 188. Re3 Rh8 189. Re4 Rg8 190. Re5 Rh8 191. Re6 Rg8 192. Re1 Rh8 193. Rd1 Rg8 194. Rd2 Rh8 195. Rd3 Rg8 196. Rd4 Rh8 197. Rd1 Rg8 198. a7 Rh8 199. Rg1 Re8 200. Kb5 Rh8 201. Rg2 Re8 202. Bd6 Rc8 203. Kb6 Rh8 204. Kc6 Rc8+ 205. Bc7 Rh8 206. Rf2 Rg8 207. Rh2 Rf8 208. Rh5 Rg8 209. Rh1 Rf8 210. Rb1 Rf2 211. Bd6 Rc2+ 212. Kd7 Kxa7 213. Bc7 Rg2 214. Kc8 Rg8+ 215. Bd8 Rg6 216. Rb5 Rh6 217. Bc7 Rc6 218. Ra5+ Ra6 219. Rc5 Rg6 220. Rc1 Ka6 221. Rb1 Rh6 222. Kd7 Rh3 223. Rb6+ Ka7 224. Rb1 Ka6 225. Kc8 Rh5 226. Rb8 Rc5 227. Rb2 Rh5 228. Rb8 Rf5 229. Rb6+ Ka7 230. Rc6 Rf8+ 231. Bd8 Rg8 232. Rc7+ Ka8 233. Rc1 Rg7 234. Bc7 Rg8+ 235. Kd7 Rg7+ 236. Kc6 Rg6+ 237. Bd6 Rg7 238. Rh1 Rh7 239. Re1";

            String pgn2 = "" +
                    "1. e4 {book} e6 {book} 2. d4 {0.70s} d5 {0.95s} 3. e5 {0.69s} c5 {0.93s}\n" +
                    "4. Nf3 {0.67s} cxd4 {0.91s} 5. Nxd4 {0.66s} Bc5 {0.90s} 6. Nb3 {0.65s}\n" +
                    "Bb6 {0.88s} 7. Nc3 {0.63s} Nc6 {0.86s} 8. Bb5 {0.62s} Ne7 {0.85s}\n" +
                    "9. Bxc6+ {0.61s} Nxc6 {0.83s} 10. O-O {0.59s} Nxe5 {0.81s} 11. Bf4 {0.58s}\n" +
                    "Ng6 {0.80s} 12. Be3 {0.57s} O-O {0.78s} 13. a4 {0.56s} Bd7 {0.77s}\n" +
                    "14. Qd3 {0.55s} Qc7 {0.76s} 15. Bxb6 {0.54s} axb6 {0.74s} 16. Qe3 {0.53s}\n" +
                    "Qd6 {0.72s} 17. f4 {0.52s} Rfc8 {0.71s} 18. Nd4 {0.51s} Qb4 {0.70s} 19. f5\n" +
                    "exf5 {0.69s} 20. Nxd5 Qd6 {0.67s} 21. Nc3 Re8 {0.66s} 22. Qf2 f4 {0.65s}\n" +
                    "23. Nde2 Bf5 {0.64s} 24. Nd4 Bg4 {0.62s} 25. Ndb5 Qc6 {0.61s} 26. Rfe1\n" +
                    "Red8 {0.60s} 27. Ne4 Kh8 {0.59s} 28. Nd4 Qc4 {0.58s} 29. Nb5 Rd7 {0.56s}\n" +
                    "30. Qxb6 Qxc2 {0.56s} 31. Nf2 Bh5 {0.54s} 32. Nd6 Qc7 {0.54s} 33. Qxc7\n" +
                    "Rxc7 {0.52s} 34. Re8+ Rxe8 {0.51s} 35. Nxe8 Rc6 {0.51s} 36. b4 Ne5 37. b5 Re6\n" +
                    "38. Nc7 Rd6 39. a5 Rg6 40. Nd5 f3 {1.5s} 41. g3 {1.1s} Rd6 {1.5s} 42. a6 {1.1s}\n" +
                    "bxa6 {1.5s} 43. bxa6 {1.1s} Nc6 {1.4s} 44. Nf4 {1.1s} g6 {1.4s} 45. a7 {1.0s}\n" +
                    "Nxa7 {1.4s} 46. Rxa7 {1.0s} Kg8 {1.4s} 47. Ne4 {0.99s} Rb6 {1.3s}\n" +
                    "48. Nxh5 {0.97s} gxh5 {1.3s} 49. Ng5 {0.95s} Kg7 {1.3s} 50. Nxf7 {0.93s}\n" +
                    "Kf6 {1.3s} 51. h4 {0.91s} Rb2 {1.2s} 52. Ng5 {0.90s} Kf5 {1.2s} 53. Nxf3 {0.88s}\n" +
                    "Kg4 {1.2s} 54. Nd4 {0.86s} Kxg3 {1.2s} 55. Rg7+ {0.84s} Kf4 {1.1s}\n" +
                    "56. Rxh7 {0.83s} Ke4 {1.1s} 57. Nc6 {0.81s} Rb6 {1.1s} 58. Rh6 {0.79s}\n" +
                    "Ke3 {1.1s} 59. Kf1 {0.78s} Rb1+ {1.1s} 60. Kg2 Rb5 {1.1s} 61. Re6+ {0.76s}\n" +
                    "Kf4 {1.0s} 62. Nd4 {0.75s} Rb4 {1.0s} 63. Rf6+ {0.73s} Ke4 {0.99s}\n" +
                    "64. Nf3 {0.72s} Ke3 {0.97s} 65. Re6+ {0.70s} Kf4 {0.95s} 66. Re2 {0.69s}\n" +
                    "Rc4 {0.94s} 67. Kf2 {0.68s} Rc3 {0.92s} 68. Ng5 {0.66s} Kg4 {0.90s}\n" +
                    "69. Re4+ {0.65s} Kf5 70. Rb4 {0.64s} Rd3 {0.87s} 71. Ne4 {0.62s} Rd5 {0.85s}\n" +
                    "72. Ng3+ {0.61s} Ke6 {0.83s} 73. Rb6+ {0.60s} Ke5 {0.82s} 74. Nxh5 {0.59s}\n" +
                    "Rd4 {0.80s} 75. Rb5+ {0.57s} Kd6 {0.79s} 76. Kg3 {0.56s} Kc6 {0.77s}\n" +
                    "77. Rb2 {0.55s} Kd5 {0.76s} 78. Nf4+ {0.54s} Ke4 {0.74s} 79. h5 {0.53s}\n" +
                    "Ra4 {0.73s} 80. h6 {0.52s} Ra7 {1.8s} 81. Rb4+ {1.3s} Kf5 {1.7s} 82. Rb5+ {1.3s}\n" +
                    "Ke4 {1.7s} 83. Rh5 {1.3s} Rh7 {1.7s} 84. Ne6 {1.2s} Ke3 {1.6s} 85. Ng5 {1.2s}\n" +
                    "Rh8 {1.6s} 86. h7 {1.2s} Kd3 {1.6s} 87. Nf7 {1.2s} Rxh7 {1.6s} 88. Rxh7 {1.1s}\n" +
                    "Ke4 {1.5s} 89. Rh5 {1.1s} Ke3 {1.5s} 90. Re5+ {1.1s} Kd3 {1.5s} 91. Kf4 {1.1s}\n" +
                    "Kd4 {1.4s} 92. Rh5 {1.0s} Kc3 {1.4s} 93. Rd5 {1.0s} Kc4 {1.4s} 94. Ke4 {1.0s}\n" +
                    "Kb3 {1.4s} 95. Kd3 Kb4 {1.4s} 96. Ne5 Kb3 {0.68s} 97. Rd4 Kb2 {1.4s} 98. Rd5\n" +
                    "Kb3 {1.4s} 99. Rd4" +
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
                }
            }
        } catch (Exception | Error e) {
            throw new AssertionError("failed on stress test");
        }

        System.out.println("stress test ok");
    } 
    
    
    @Test
    void dontCrashTest() {

        try {
            String pgn = "" +
                    "1. d4 {book} Nf6 {book} 2. c4 {book} g6 {book} 3. Nc3 {0.70s} d6 {0.95s}\n" +
                    "4. e4 {0.69s} Bg7 {0.93s} 5. Nf3 {0.67s} O-O {0.92s} 6. Bd3 {0.66s} e5 {0.90s}\n" +
                    "7. O-O {0.65s} Bg4 {0.88s} 8. d5 {0.63s} Na6 {0.86s} 9. Bg5 {0.62s} Nc5 {0.85s}\n" +
                    "10. b4 {0.61s} Nxd3 {0.83s} 11. Qxd3 {0.60s} h6 {0.81s} 12. Bxf6 {0.58s}\n" +
                    "Qxf6 {0.80s} 13. Nd2 {0.57s} g5 {0.78s} 14. a4 {0.56s} Qe7 {0.77s}\n" +
                    "15. f3 {0.55s} Bd7 {0.75s} 16. b5 {0.54s} b6 {0.74s} 17. Qc2 {0.53s} Qf6 {0.72s}\n" +
                    "18. Nb3 {0.52s} Qf4 {0.71s} 19. Qe2 {0.51s} h5 {0.70s} 20. Nc1 Qf6 {0.69s}\n" +
                    "21. Nd3 h4 {0.67s} 22. Qb2 h3 {0.66s} 23. g4 a5 {0.65s} 24. Qe2 Rfe8 {0.64s}\n" +
                    "25. Nf2 Qh6 {0.62s} 26. Ncd1 Rab8 {0.61s} 27. Ne3 Bf6 {0.60s} 28. Qd3\n" +
                    "Rf8 {0.59s} 29. Rfd1 Kg7 {0.58s} 30. Ra2 Rh8 {0.57s} 31. Qb3 Kg8 {0.56s} 32. Rc2\n" +
                    "Be7 {0.55s} 33. Rf1 Qf6 {0.54s} 34. Nh1 Re8 {0.53s} 35. Ng3 Qg6 {0.52s} 36. Qc3\n" +
                    "Kh7 {0.51s} 37. Qd3 Kg8 38. Rd1 Qf6 39. Qc3 Kh7 40. Rf2 Ra8 {1.5s}\n" +
                    "41. Qd3 {1.1s} Qh6 {1.5s} 42. Qf1 {1.1s} Rab8 {1.5s} 43. Nh5 {1.1s} Rbd8 {1.4s}\n" +
                    "44. Qxh3 {1.1s} Kg8 {1.4s} 45. Qg2 {1.0s} Qg6 {1.4s} 46. Rc2 {1.0s} Re8 {1.4s}\n" +
                    "47. Qf1 {1.00s} Ra8 {1.3s} 48. Qe2 {0.98s} Bf6 {1.3s} 49. Qd3 {0.96s} Kh7 {1.3s}\n" +
                    "50. Qb3 {0.94s} Be7 {1.3s} 51. Rcd2 {0.92s} Kg8 {1.2s} 52. Ng3 {0.90s}\n" +
                    "Rd8 {1.2s} 53. Re1 {0.88s} Bf6 {1.2s} 54. Rc2 {0.86s} Ra8 {1.2s} 55. Qd3 {0.85s}\n" +
                    "Kh7 {1.1s} 56. Rce2 {0.83s} Kg7 {1.1s} 57. Kh1 {0.81s} Kg8 {1.1s}\n" +
                    "58. Rb2 {0.80s} Rd8 {1.1s} 59. Rc1 {0.78s} Bg7 {1.1s} 60. Kg1 {0.77s} Bf6 {1.0s}\n" +
                    "61. Ra2 {0.75s} Bg7 {1.0s} 62. Rf1 {0.74s} Bf6 {1.00s} 63. Qc3 {0.72s}\n" +
                    "Rb8 {0.98s} 64. Re2 {0.71s} Rd8 {0.96s} 65. Rc1 {0.69s} Re8 {0.94s}\n" +
                    "66. Qb3 {0.68s} Rd8 {0.92s} 67. Rd2 {0.67s} Kh7 {0.91s} 68. Qd3 {0.65s}\n" +
                    "Kg8 {0.89s} 69. Nh5 {0.64s} Ra8 {0.87s} 70. Re2 {0.63s} Bg7 {0.85s}\n" +
                    "71. Rec2" +
                    "";

            String pgn2 = "" +
                    "";

            List<String> pgns = new ArrayList<>();
            pgns.add(pgn);


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
                }
            }
        } catch (Exception | Error e) {
            throw new AssertionError("failed on crash test");
        }

        System.out.println("crash test ok");
    }
}
