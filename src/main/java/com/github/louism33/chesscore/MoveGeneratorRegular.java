package com.github.louism33.chesscore;

import static com.github.louism33.chesscore.BitOperations.getFirstPiece;
import static com.github.louism33.chesscore.BitOperations.getIndexOfFirstPiece;
import static com.github.louism33.chesscore.BoardConstants.*;
import static com.github.louism33.chesscore.MoveAdder.addMovesFromAttackTableMaster;
import static com.github.louism33.chesscore.MoveGeneratorPseudo.generatePseudoCaptureTable;
import static com.github.louism33.chesscore.PieceMove.*;

class MoveGeneratorRegular {

    static void addPawnPushes(int[] moves, Chessboard board, boolean white,
                              long ignoreThesePieces, long legalCaptures, long legalPushes,
                              long allPieces){

        long myPawns = board.pieces[board.turn][PAWN];
        
        final long homeRank = (white ? RANK_TWO : RANK_SEVEN);
        long allPawnPushes = (white ? myPawns << 8 : myPawns >>> 8) & ~allPieces & legalPushes;
        
        while (myPawns != 0){
            long pawn = getFirstPiece(myPawns);
            if ((pawn & ignoreThesePieces) == 0){
                final int pawnIndex = getIndexOfFirstPiece(pawn);
                long mySquares;
                if ((pawn & homeRank) != 0) {
                    mySquares = singlePawnPushes(pawn, white, legalPushes, allPieces);
                }
                else {
                    mySquares = (allPawnPushes & (white ? PAWN_PUSH_MASK_WHITE[pawnIndex] : PAWN_PUSH_MASK_BLACK[pawnIndex]));
                }
                
                final long pawnCaptures = singlePawnCaptures(pawn, white, legalCaptures);
                
                addMovesFromAttackTableMaster(moves, mySquares | pawnCaptures, pawnIndex, board);
            }
            myPawns &= myPawns - 1;
        }

        
    }

    static void addKnightMoves(int[] moves, Chessboard board,
                               long ignoreThesePieces, long mask, long myKnights){

        while (myKnights != 0){
            final long knight = getFirstPiece(myKnights);
            if ((knight & ignoreThesePieces) == 0) {
                long jumps = singleKnightTable(knight, mask);
                addMovesFromAttackTableMaster(moves, jumps, getIndexOfFirstPiece(knight), board);
            }
            myKnights &= (myKnights - 1);
        }
    }

    static void addSlidingMoves (int[] moves, Chessboard board, boolean white,
                                 long ignoreThesePieces, long mask,
                                 long myBishops, long myRooks, long myQueens,
                                 long allPieces){
        while (myBishops != 0){
            long bishop = getFirstPiece(myBishops);
            if ((bishop & ignoreThesePieces) == 0) {
                long slides = singleBishopTable(allPieces, bishop, mask);
                addMovesFromAttackTableMaster(moves, slides, getIndexOfFirstPiece(bishop), board);
            }
            myBishops &= (myBishops - 1);
        }
        while (myRooks != 0){
            long rook = getFirstPiece(myRooks);
            if ((rook & ignoreThesePieces) == 0) {
                long slides = singleRookTable(allPieces, rook, mask);
                addMovesFromAttackTableMaster(moves, slides, getIndexOfFirstPiece(rook), board);
            }
            myRooks &= (myRooks - 1);
        }
        while (myQueens != 0){
            long queen = getFirstPiece(myQueens);
            if ((queen & ignoreThesePieces) == 0) {
                long slides = singleQueenTable(allPieces, queen, mask);
                addMovesFromAttackTableMaster(moves, slides, getIndexOfFirstPiece(queen), board);
            }
            myQueens &= (myQueens - 1);
        }
    }

    static void addKingLegalMovesOnly(int[] moves, Chessboard board, boolean white,
                                      long myBishops, long myQueens, long myKing,
                                      long enemyPawns, long enemyKnights, long enemyBishops, long enemyRooks, long enemyQueens, long enemyKing,
                                      long enemies, long friends){

        addMovesFromAttackTableMaster(moves,
                kingLegalPushAndCaptureTable(board, white,
                        myBishops, myQueens, myKing,
                        enemyPawns, enemyKnights, enemyBishops, enemyRooks, enemyQueens, enemyKing,
                        enemies, friends),
                getIndexOfFirstPiece(myKing),
                board);
    }

    private static long kingLegalPushAndCaptureTable(Chessboard board, boolean white,
                                                     long myBishops, long myQueens, long myKing,
                                                     long enemyPawns, long enemyKnights, long enemyBishops, long enemyRooks, long enemyQueens, long enemyKing,
                                                     long enemies, long friends){

        long kingSafeSquares = ~kingDangerSquares(board, white,
                myKing,
                enemyPawns, enemyKnights, enemyBishops, enemyRooks, enemyQueens, enemyKing
        );

        long kingSafeCaptures = enemies & kingSafeSquares;
        long kingSafePushes = (~board.allPieces() & kingSafeSquares);
        return singleKingTable(myKing, kingSafePushes | kingSafeCaptures);
    }

    private static long kingDangerSquares(Chessboard board, boolean white,
                                          long myKing,
                                          long enemyPawns, long enemyKnights, long enemyBishops, long enemyRooks, long enemyQueens, long enemyKing){

        board.pieces[white ? WHITE : BLACK][KING] = 0;
        
        if (white){
            board.setWhiteKing(0);
        }
        else {
            board.setBlackKing(0);
        }

        long kingDangerSquares = generatePseudoCaptureTable(!white, 0, UNIVERSE, UNIVERSE,
                enemyPawns, enemyKnights, enemyBishops, enemyRooks, enemyQueens, enemyKing,
                board.allPieces());

        board.pieces[white ? WHITE : BLACK][KING] = myKing;

        if (white){
            board.setWhiteKing(myKing);
        }
        else {
            board.setBlackKing(myKing);
        }

        return kingDangerSquares;
    }

}
