package com.github.louism33.chesscore;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static com.github.louism33.chesscore.BitOperations.newPieceOnSquare;
import static com.github.louism33.chesscore.Square.*;

class SquareTest {

    @Test
    void test(){
        for (int s = 63; s >= 0; s--){
            Assert.assertEquals(values()[s].toBitboard(), newPieceOnSquare(s));
        }
    }

    @Test
    void getPieceOnSquare() {
        Chessboard board = new Chessboard();
        final Square pieceOnSquare = Square.getSquareOfBitboard(board.getWhiteKing());
        Assert.assertEquals(pieceOnSquare, E1);
    }

    @Test
    void getPiecesOnSquare() {
        Chessboard board = new Chessboard();
        final List<Square> piecesOnSquare = Square.getPiecesOnSquare(board.getBlackPawns());
        List<Square> blackPawns = new ArrayList<>();
        blackPawns.add(A7);
        blackPawns.add(B7);
        blackPawns.add(C7);
        blackPawns.add(D7);
        blackPawns.add(E7);
        blackPawns.add(F7);
        blackPawns.add(G7);
        blackPawns.add(H7);
        Assert.assertTrue(piecesOnSquare.containsAll(blackPawns));
    }

    @Test
    void longFromSquare() {
        final long l = Square.longFromSquare(H1);
        final long l1 = newPieceOnSquare(0);
        Assert.assertEquals(l, l1);
    }

    @Test
    void squaresFromBitboard() {
        Chessboard newBoard = new Chessboard();
        final List<Square> squares = Square.squaresFromBitboard(newBoard.getWhiteRooks());
        List<Square> initialWhiteRooks = new ArrayList<>();
        initialWhiteRooks.add(A1);
        initialWhiteRooks.add(H1);

        Assert.assertTrue(squares.containsAll(initialWhiteRooks));

        final List<Square> squares2 = Square.squaresFromBitboard(newBoard.getBlackRooks());
        List<Square> initialWhiteRooks2 = new ArrayList<>();
        initialWhiteRooks2.add(A8);
        initialWhiteRooks2.add(H8);

        Assert.assertTrue(squares2.containsAll(initialWhiteRooks2));
    }

    @Test
    void squareFromSingleBitboard() {
        final long l = newPieceOnSquare(0);
        final Square square = Square.squareFromSingleBitboard(l);
        Assert.assertEquals(square, H1);
    }

}
