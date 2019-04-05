package com.github.louism33.chesscore;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static com.github.louism33.chesscore.StackDataUtil.buildStackDataBetter;

public class StackTest {

    @Test
    void stackClashTest() {
        int total = 10_000_000;

        for (int i = 0; i < total; i++) {

            Random r = new Random();

            int move = r.nextInt(MoveConstants.CHECKING_MOVE_MASK);
            int quiets = r.nextInt(256);
            int special = r.nextInt(8);
            int epfile = r.nextInt(9);
            int castleflag = r.nextInt(16);
            int turn = i % 2;

            long sd = buildStackDataBetter(move, turn, quiets, castleflag, special, epfile);
            
            Assert.assertEquals(StackDataUtil.getTurn(sd), 1 - turn); // other player
            Assert.assertEquals(StackDataUtil.getQuietHalfmoveCounter(sd), quiets);
            Assert.assertEquals(StackDataUtil.getCastlingRights(sd), castleflag);
            Assert.assertEquals(StackDataUtil.getSpecialMove(sd), special);
            Assert.assertEquals(StackDataUtil.getEPMove(sd), epfile);
        }
    }

    @Test
    void stackClash2Test() {
        int total = 10_000_000;

        for (int i = 0; i < total; i++) {

            Random r = new Random();

            int move = r.nextInt(MoveConstants.CHECKING_MOVE_MASK);
            int quiets = r.nextInt(256);
            int special = r.nextInt(8);
            int castleflag = r.nextInt(16);
            int turn = i % 2;

            long sd = buildStackDataBetter(move, turn, quiets, castleflag, special);

            Assert.assertEquals(StackDataUtil.getTurn(sd), 1 - turn); // other player
            Assert.assertEquals(StackDataUtil.getQuietHalfmoveCounter(sd), quiets);
            Assert.assertEquals(StackDataUtil.getCastlingRights(sd), castleflag);
            Assert.assertEquals(StackDataUtil.getSpecialMove(sd), special);
        }
    }
}