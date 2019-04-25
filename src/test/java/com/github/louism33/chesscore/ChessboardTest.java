package com.github.louism33.chesscore;

import com.github.louism33.utils.MoveParserFromAN;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import static com.github.louism33.chesscore.BoardConstants.BLACK_PAWN;
import static com.github.louism33.chesscore.BoardConstants.WHITE_PAWN;
import static com.github.louism33.chesscore.MoveParser.getMovingPieceInt;

public class ChessboardTest {

    @Test
    void wonkyFenTest() {
        Chessboard board = new Chessboard("rnbqkbnr/pp1ppp1p/6p1/2p5/1P6/1QP5/P2PPPPP/RNB1KBNR b KQkq - 0 1;");
        System.out.println(board);
    }
    
    @Test
    void previousMoveWasPawnPushToSixTest(){
        String fen = "8/7p/5k2/5p2/p1p2P2/Pr1pPK2/1P1R3P/8 b - -";
        Chessboard board = new Chessboard(fen);
        final int move = MoveParserFromAN.buildMoveFromLAN(board, "c4c3");
        final int turnBefore = board.turn;
        board.makeMoveAndFlipTurn(move);

        Assert.assertTrue(board.previousMoveWasPawnPushToSix());
        Assert.assertTrue(MoveParser.moveIsPawnPushSix(turnBefore, move));
    }

    @Test
    void previousMoveWasPawnPushToSevenTest(){
        String fen = "8/7p/5k2/5p2/p4P2/PrppPK2/1P1R3P/8 b - -";
        Chessboard board = new Chessboard(fen);

        final int move = MoveParserFromAN.buildMoveFromLAN(board, "c3c2");
        final int turnBefore = board.turn;
        board.makeMoveAndFlipTurn(move);

        Assert.assertTrue(board.previousMoveWasPawnPushToSeven());
        Assert.assertTrue(MoveParser.moveIsPawnPushSeven(turnBefore, move));
    }

    @Test
    void previousMoveWasPawnPushToSeven2Test(){
        String fen = "8/8/PPPPPPPP/8/k6K/pppppppp/8/8 b - -";
        Chessboard board = new Chessboard(fen);

        final int[] moves = board.generateLegalMoves();
        final int len = moves[moves.length - 1];
        for (int i = 0; i < len; i++) {
            final int move = moves[i];
            if (move == 0) {
                continue;
            }
            final int movingPieceInt = getMovingPieceInt(move);
            if (movingPieceInt != BLACK_PAWN) {
                continue;
            }

            Assert.assertTrue(MoveParser.moveIsPawnPushSeven(board.turn, move));
            board.makeMoveAndFlipTurn(move);
            Assert.assertTrue(board.previousMoveWasPawnPushToSeven());
            board.unMakeMoveAndFlipTurn();
        }

        board.makeNullMoveAndFlipTurn();

        for (int i = 0; i < len; i++) {
            final int move = moves[i];
            if (move == 0) {
                continue;
            }
            final int movingPieceInt = getMovingPieceInt(move);
            if (movingPieceInt != WHITE_PAWN) {
                continue;
            }

            Assert.assertTrue(MoveParser.moveIsPawnPushSeven(board.turn, move));
            board.makeMoveAndFlipTurn(move);
            Assert.assertTrue(board.previousMoveWasPawnPushToSeven());
            board.unMakeMoveAndFlipTurn();
        }
    }

    @Test
    void previousMoveWasPawnPushToSix2Test(){
        String fen = "k6K/8/8/PPPPPPPP/pppppppp/8/8/8 b - -";
        Chessboard board = new Chessboard(fen);

        final int[] moves = board.generateLegalMoves();
        final int len = moves[moves.length - 1];
        for (int i = 0; i < len; i++) {
            final int move = moves[i];
            if (move == 0) {
                continue;
            }
            final int movingPieceInt = getMovingPieceInt(move);
            if (movingPieceInt != BLACK_PAWN) {
                continue;
            }

            Assert.assertTrue(MoveParser.moveIsPawnPushSix(board.turn, move));
            board.makeMoveAndFlipTurn(move);
            Assert.assertTrue(board.previousMoveWasPawnPushToSix());
            board.unMakeMoveAndFlipTurn();
        }

        board.makeNullMoveAndFlipTurn();

        for (int i = 0; i < len; i++) {
            final int move = moves[i];
            if (move == 0) {
                continue;
            }
            final int movingPieceInt = getMovingPieceInt(move);
            if (movingPieceInt != WHITE_PAWN) {
                continue;
            }

            Assert.assertTrue(MoveParser.moveIsPawnPushSix(board.turn, move));
            board.makeMoveAndFlipTurn(move);
            Assert.assertTrue(board.previousMoveWasPawnPushToSix());
            board.unMakeMoveAndFlipTurn();
        }
    }

    @Test
    public void moveIsCaptureOfLastMovePieceTest() {
        String fen = "8/7p/5k2/5p2/p1p2P2/Pr1pPK2/1P1R3P/8 b - -";
        Chessboard board = new Chessboard(fen);

        final int move = MoveParserFromAN.buildMoveFromLAN(board, "b3b2");
        board.makeMoveAndFlipTurn(move);
        final int moveC = MoveParserFromAN.buildMoveFromLAN(board, "d2b2");
        Assert.assertTrue(board.moveIsCaptureOfLastMovePiece(moveC));
    }
}

