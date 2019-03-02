package com.github.louism33.chesscore;

import static com.github.louism33.chesscore.BitOperations.getFirstPiece;
import static com.github.louism33.chesscore.BoardConstants.*;
import static com.github.louism33.chesscore.MoveAdder.addMovesFromAttackTableMasterBetter;
import static com.github.louism33.chesscore.PieceMove.*;
import static java.lang.Long.numberOfTrailingZeros;

class MoveGeneratorPseudo {

    static void addAllMovesWithoutKing(int[] moves, long[][] pieces, int turn, int[] pieceSquareTable,
                                       long ignoreThesePieces, long legalPushes, long legalCaptures,
                                       long myKnights, long myBishops, long myRooks, long myQueens,
                                       long allPieces){

        //knight moves
        long mask = (legalPushes | legalCaptures);
        while (myKnights != 0){
            final long knight = getFirstPiece(myKnights);
            if ((knight & ignoreThesePieces) == 0) {
                long jumps = KNIGHT_MOVE_TABLE[numberOfTrailingZeros(knight)] & mask;
                if (jumps != 0) {
                    addMovesFromAttackTableMasterBetter(moves, jumps, numberOfTrailingZeros(knight),
                            PIECE[turn][KNIGHT], pieceSquareTable);
                }
            }
            myKnights &= (myKnights - 1);
        }

        //sliding moves
        while (myBishops != 0){
            long bishop = getFirstPiece(myBishops);
            if ((bishop & ignoreThesePieces) == 0) {
                long slides = singleBishopTable(allPieces, bishop, mask);
                if (slides != 0) {
                    addMovesFromAttackTableMasterBetter(moves, slides, numberOfTrailingZeros(bishop), PIECE[turn][BISHOP], pieceSquareTable);
                }
            }
            myBishops &= (myBishops - 1);
        }
        while (myRooks != 0){
            long rook = getFirstPiece(myRooks);
            if ((rook & ignoreThesePieces) == 0) {
                long slides = singleRookTable(allPieces, rook, mask);
                if (slides != 0) {
                    addMovesFromAttackTableMasterBetter(moves, slides, numberOfTrailingZeros(rook), PIECE[turn][ROOK], pieceSquareTable);
                }
            }
            myRooks &= (myRooks - 1);
        }
        while (myQueens != 0){
            long queen = getFirstPiece(myQueens);
            if ((queen & ignoreThesePieces) == 0) {
                long slides = singleQueenTable(allPieces, queen, mask);
                if (slides != 0) {
                    addMovesFromAttackTableMasterBetter(moves, slides, numberOfTrailingZeros(queen), PIECE[turn][QUEEN], pieceSquareTable);
                }
            }
            myQueens &= (myQueens - 1);
        }

        //pawn moves
        long myPawns = pieces[turn][PAWN];

        long allPawnPushes = (turn == WHITE ? myPawns << 8 : myPawns >>> 8) & ~allPieces & legalPushes;

        while (myPawns != 0){
            long pawn = getFirstPiece(myPawns);
            if ((pawn & ignoreThesePieces) == 0){
                final int pawnIndex = numberOfTrailingZeros(pawn);
                long mySquares;
                if ((pawn & PENULTIMATE_RANKS[1 - turn]) != 0) {
                    mySquares = singlePawnPushes(pawn, turn, legalPushes, allPieces);
                }
                else {
                    mySquares = (allPawnPushes & (PAWN_PUSH_MASK[turn][pawnIndex]));
                }

                final long pawnCaptures = singlePawnCaptures(pawn, turn, legalCaptures);

                final long table = mySquares | pawnCaptures;
                if (table != 0) {
                    addMovesFromAttackTableMasterBetter(moves, table,
                            pawnIndex, PIECE[turn][PAWN], pieceSquareTable);
                }
            }
            myPawns &= myPawns - 1;
        }
    }

}
