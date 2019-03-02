package com.github.louism33.chesscore;

import static com.github.louism33.chesscore.BitOperations.getFirstPiece;
import static com.github.louism33.chesscore.BoardConstants.*;
import static com.github.louism33.chesscore.MoveAdder.addMovesFromAttackTableMaster;
import static com.github.louism33.chesscore.PieceMove.*;
import static java.lang.Long.numberOfTrailingZeros;

class MoveGeneratorPseudo {

    static void addAllMovesWithoutKing(final int[] moves, final long[][] pieces, final int turn, final int[] pieceSquareTable,
                                       final long ignoreThesePieces, final long legalPushes, final long legalCaptures,
                                       long myKnights, long myBishops, long myRooks, long myQueens,
                                       final long allPieces){

        //knight moves
        long mask = (legalPushes | legalCaptures);
        while (myKnights != 0){
            final long knight = getFirstPiece(myKnights);
            if ((knight & ignoreThesePieces) == 0) {
                final long jumps = KNIGHT_MOVE_TABLE[numberOfTrailingZeros(knight)] & mask;
                if (jumps != 0) {
                    if ((jumps & allPieces) != 0) {
                        addMovesFromAttackTableMaster(moves, jumps, numberOfTrailingZeros(knight),
                                PIECE[turn][KNIGHT], pieceSquareTable);
                    }
                    else {
                        addMovesFromAttackTableMaster(moves, jumps, numberOfTrailingZeros(knight),
                                PIECE[turn][KNIGHT]);
                    }
                }
            }
            myKnights &= (myKnights - 1);
        }

        //sliding moves
        while (myBishops != 0){
            final long bishop = getFirstPiece(myBishops);
            if ((bishop & ignoreThesePieces) == 0) {
                final long slides = singleBishopTable(allPieces, bishop, mask);
                if (slides != 0) {
                    if ((slides & allPieces) != 0) {
                        addMovesFromAttackTableMaster(moves, slides, numberOfTrailingZeros(bishop), PIECE[turn][BISHOP], pieceSquareTable);
                    }
                    else{
                        addMovesFromAttackTableMaster(moves, slides, numberOfTrailingZeros(bishop), PIECE[turn][BISHOP]);
                    }
                }
            }
            myBishops &= (myBishops - 1);
        }
        while (myRooks != 0){
            final long rook = getFirstPiece(myRooks);
            if ((rook & ignoreThesePieces) == 0) {
                final long slides = singleRookTable(allPieces, rook, mask);
                if (slides != 0) {
                    if ((slides & allPieces) != 0) {
                        addMovesFromAttackTableMaster(moves, slides, numberOfTrailingZeros(rook), PIECE[turn][ROOK], pieceSquareTable);
                    }
                    else {
                        addMovesFromAttackTableMaster(moves, slides, numberOfTrailingZeros(rook), PIECE[turn][ROOK]);
                    }
                }
            }
            myRooks &= (myRooks - 1);
        }
        while (myQueens != 0){
            final long queen = getFirstPiece(myQueens);
            if ((queen & ignoreThesePieces) == 0) {
                final long slides = singleQueenTable(allPieces, queen, mask);
                if (slides != 0) {
                    if ((slides & allPieces) != 0) {
                        addMovesFromAttackTableMaster(moves, slides, numberOfTrailingZeros(queen), PIECE[turn][QUEEN], pieceSquareTable);
                    }
                    else {
                        addMovesFromAttackTableMaster(moves, slides, numberOfTrailingZeros(queen), PIECE[turn][QUEEN]);
                    }
                }
            }
            myQueens &= (myQueens - 1);
        }

        //pawn moves
        long myPawns = pieces[turn][PAWN] & ~ignoreThesePieces;
        while (myPawns != 0){
            final long pawn = getFirstPiece(myPawns);
            final int pawnIndex = numberOfTrailingZeros(pawn);
            final long quietTable;
            // doubles
            if ((pawn & PENULTIMATE_RANKS[1 - turn]) != 0) {
                quietTable = singlePawnPushes(pawn, turn, legalPushes, allPieces);
            }
            else {
                quietTable = (turn == WHITE ? pawn << 8 : pawn >>> 8) & ~allPieces & legalPushes;
            }

            final long captureTable = singlePawnCaptures(pawn, turn, legalCaptures);

            if (quietTable != 0) {
                addMovesFromAttackTableMaster(moves, quietTable,
                        pawnIndex, PIECE[turn][PAWN]);
            }
            if (captureTable != 0) {
                addMovesFromAttackTableMaster(moves, captureTable,
                        pawnIndex, PIECE[turn][PAWN], pieceSquareTable);
            }
            myPawns &= myPawns - 1;
        }
    }

}
