package com.github.louism33.chesscore;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import static com.github.louism33.chesscore.BoardConstants.*;

public class PinningPiecesTest {

    @Test
    void regularBoardTest() {
        verifyHashToDepth(4, new Chessboard());
    }

    @Test
    void moreTest() {
        verifyHashToDepth(4, new Chessboard("6k1/1ppb3p/p1pb4/6q1/3P1r1r/2P1R2P/PP1BQ1P1/6KN w - - 0 2"));
    }

    @Test
    void AvoidIllegalEPCaptureTest() {
        verifyHashToDepth(5, new Chessboard("8/5bk1/8/2Pp4/8/1K6/8/8 w - d6 0 1"));

        verifyHashToDepth(5, new Chessboard("8/8/1k6/8/2pP4/8/5BK1/8 b - d3 0 1"));
    }


    @Test
    void EPCaptureChecksOpponentTest() {
        verifyHashToDepth(5, new Chessboard("8/8/1k6/2b5/2pP4/8/5K2/8 b - d3 0 1"));

        verifyHashToDepth(5, new Chessboard("8/5k2/8/2Pp4/2B5/1K6/8/8 w - d6 0 1"));
    }
    
    
    @Test
    void bigDepth11Test() {
        verifyHashToDepth(6, new Chessboard("8/5p2/8/2k3P1/p3K3/8/1P6/8 b - -"));
    }

    @Test
    void bigDepth12Test() {
        verifyHashToDepth(4, new Chessboard("r3k2r/pb3p2/5npp/n2p4/1p1PPB2/6P1/P2N1PBP/R3K2R w KQkq -"));
    }



    private static void verifyHashToDepth(int depth, Chessboard board) {
        final Chessboard initial = new Chessboard(board);
        Assert.assertEquals(board, initial);

        countFinalNodesAtDepthHelper(board, depth);

        Assert.assertEquals(board, new Chessboard(board));
        Assert.assertEquals(board, initial);
    }

    private static long countFinalNodesAtDepthHelper(Chessboard board, int depth){
        long temp = 0;
        if (depth == 0){
            return 1;
        }
        int[] moves = board.generateLegalMoves();

        final boolean pinning = board.pinningPieces[board.turn] != 0;
        final boolean pinned = board.pinnedPieces[board.turn] != 0;

        final int turn = board.turn;
        final int et = 1 - turn;
        long pp = board.pinnedPieces[board.turn];
        while (pp != 0) {
            long p = Long.lowestOneBit(pp);
            final long threatenPinnedPiece = CheckHelper.bitboardOfPiecesThatLegalThreatenSquare(turn, p,
                    board.pieces[et][PAWN], board.pieces[et][KNIGHT], board.pieces[et][BISHOP], board.pieces[et][ROOK],
                    board.pieces[et][QUEEN], board.pieces[et][KING], board.allPieces(), 32);

            Assert.assertTrue((threatenPinnedPiece & board.pinningPieces[board.turn]) != 0);

            pp &= pp - 1;
        }

        pp = board.pinnedPieces[board.turn];

        while (pp != 0) {
            long p = Long.lowestOneBit(pp);

            final long threatenKingWithoutPinnedPiece = CheckHelper.bitboardOfPiecesThatLegalThreatenSquare(turn, board.pieces[turn][KING],
                    board.pieces[et][PAWN], board.pieces[et][KNIGHT], board.pieces[et][BISHOP], board.pieces[et][ROOK],
                    board.pieces[et][QUEEN], board.pieces[et][KING], board.allPieces() ^ p, 32);

            Assert.assertTrue((threatenKingWithoutPinnedPiece & board.pinningPieces[board.turn]) != 0);

            pp &= pp - 1;
        }
        
        Assert.assertTrue((board.pinnedPieces[turn] & board.pinnedPieces[1-turn]) == 0);
        Assert.assertTrue((board.pinningPieces[turn] & board.pinningPieces[1-turn]) == 0);

        Assert.assertEquals(pinning, pinned);
        
        if (depth == 1){
            return moves[moves.length - 1];
        }
        for (int i = 0; i < moves.length; i++) {
            int move = moves[i];
            if (move == 0) {
                break;
            }

            board.makeMoveAndFlipTurn(move);

            long movesAtDepth = countFinalNodesAtDepthHelper(board, depth - 1);
            temp += movesAtDepth;

            board.unMakeMoveAndFlipTurn();
            Assert.assertEquals(board, new Chessboard(board));

        }
        return temp;
    }
    
}
