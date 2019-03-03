package com.github.louism33.utils;

import com.github.louism33.chesscore.Chessboard;

public final class NPSTest {

    public static void npsTest(Chessboard board, int d) {
        long t1 = System.currentTimeMillis();
        long nodes = Perft.perftTest(board, d);
        long t2 = System.currentTimeMillis();
        long t = t2 - t1;
        if (t > 0) {
            System.out.println("NPS: " + ((nodes / t) * 1000) );
        }
    }
}
