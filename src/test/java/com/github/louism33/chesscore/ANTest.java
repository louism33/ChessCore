package com.github.louism33.chesscore;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import static com.github.louism33.utils.MoveParserFromAN.buildMoveFromLAN;

public class ANTest {


    @Test
    void anTest(){

//        String ms = "g1f3, d7d5, d2d4, b8c6, c1f4, g7g5, f4g5, g8f6, g5f6, e7f6, e2e3, f8d6, c2c4, d5c4, f1c4, h8g8, e1f1, c8e6, d4d5, c6e5, d5e6, e5c4, e6f7";
        String ms = "b1c3, d7d5, g1f3, g8f6, d2d4, b8d7, c1f4, e7e6, e2e3, a7a6, f1d3, c7c5, e1g1, c5c4, d3e2, f8b4, f3e5, b4c3, b2c3, f6e4, e2f3, d7f6, d1e1, d8a5, f3e4, f6e4, f2f3, e4f6, a1b1, a5a2, e1c1, a2a5, f4g3, f6h5, c1e1, f7f6, e5g4, h5g3, h2g3, e8g8, e3e4, d5e4, f3e4, b7b5, e4e5, f6f5, g4h2, h7h5, h2f3, a5d8, e1e2, f8f7, d4d5, e6d5, f3d4, g7g6, e5e6, f7c7, f1e1, d8e7, b1a1, c7c5, g1h1, a6a5, e2d2, g8h7, d4f3, a8a6, d2g5, c5c7, g5e3, a5a4, f3g5, h7g8, h1h2, c7c6, g5f3, c6e6, e3g5, e7g5, f3g5, e6e1, a1e1, a4a3, e1e8, g8g7, e8c8, a3a2, g5e6, g7f7, e6c7, a6a5, c7d5, a2a1q, c8c7, f7f8, d5f6, a1e1, f6h7, f8e8, h7f6, e8d8, c7d7, d8c8, d7d1, e1d1";
        String[] moves = ms.split(" ");
        
        Chessboard board1 = new Chessboard();
        Chessboard board2 = new Chessboard();
        int length = moves.length;
        
        
        for (int i = 0; i < length; i++) {
            int move1 = buildMoveFromLAN(board1, moves[i]);
            board1.makeMoveAndFlipTurn(move1);

            String s1 = MoveParser.toString(move1);
            int move2 = buildMoveFromLAN((board2), s1);

            board2.makeMoveAndFlipTurn(move2);

            Assert.assertEquals(move1, move2);
            Assert.assertEquals(board1, board2);
        }


    }
    
}
