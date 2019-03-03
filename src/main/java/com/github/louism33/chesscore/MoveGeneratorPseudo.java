package com.github.louism33.chesscore;

import static com.github.louism33.chesscore.BitOperations.getFirstPiece;
import static com.github.louism33.chesscore.BitOperations.populationCount;
import static com.github.louism33.chesscore.BoardConstants.*;
import static com.github.louism33.chesscore.MoveParser.buildMove;
import static com.github.louism33.chesscore.PieceMove.*;
import static java.lang.Long.numberOfTrailingZeros;

final class MoveGeneratorPseudo {

    final static void addAllMovesWithoutKing(final int[] moves, final long[][] pieces, final int turn, final int[] pieceSquareTable,
                                       final long ignoreThesePieces, final long legalPushes, final long legalCaptures,
                                       long myKnights, long myBishops, long myRooks, long myQueens,
                                       final long allPieces){

        //knight moves
        long mask = (legalPushes | legalCaptures);
        while (myKnights != 0){
            final long knight = getFirstPiece(myKnights);
            if ((knight & ignoreThesePieces) == 0) {
                final int knightIndex = numberOfTrailingZeros(knight);
                final long jumps = KNIGHT_MOVE_TABLE[knightIndex] & mask;
                if (jumps != 0) {
                    
                    long captureTable = jumps & allPieces;
                    if (captureTable != 0) {
                        final int i1 = populationCount(captureTable);
                        final int startIndex = moves[moves.length - 1];
                        int i = 0;
                        while (captureTable != 0){
                            final int destinationIndex = numberOfTrailingZeros(captureTable);
                            moves[startIndex + i] = buildMove(knightIndex, PIECE[turn][KNIGHT],
                                    destinationIndex, pieceSquareTable[destinationIndex]);
                            i++;
                            captureTable &= captureTable - 1;
                        }
                        moves[moves.length - 1] += i1;
                    }
                    
                    long quietTable = jumps & ~allPieces;
                    if (quietTable != 0){
                        final int i1 = populationCount(quietTable);
                        final int startIndex = moves[moves.length - 1];
                        int i = 0;
                        while (quietTable != 0){
                            final int destinationIndex = numberOfTrailingZeros(quietTable);
                            moves[startIndex + i] = buildMove(knightIndex, PIECE[turn][KNIGHT],
                                    destinationIndex);
                            i++;
                            quietTable &= quietTable - 1;
                        }
                        moves[moves.length - 1] += i1;
                    }
                }
            }
            myKnights &= (myKnights - 1);
        }

        //sliding moves
        while (myBishops != 0){
            final long bishop = getFirstPiece(myBishops);
            if ((bishop & ignoreThesePieces) == 0) {
                final int bishopIndex = numberOfTrailingZeros(bishop);
                final long slides = singleBishopTable(allPieces, bishopIndex, mask);
                if (slides != 0) {

                    long captureTable = slides & allPieces;
                    if (captureTable != 0) {
                        final int i1 = populationCount(captureTable);
                        final int startIndex = moves[moves.length - 1];
                        int i = 0;
                        while (captureTable != 0){
                            final int destinationIndex = numberOfTrailingZeros(captureTable);
                            moves[startIndex + i] = buildMove(bishopIndex, PIECE[turn][BISHOP],
                                    destinationIndex, pieceSquareTable[destinationIndex]);
                            i++;
                            captureTable &= captureTable - 1;
                        }
                        moves[moves.length - 1] += i1;     
                    }
                    
                    long quietTable = slides & ~allPieces;
                    if (quietTable != 0){
                        final int i1 = populationCount(quietTable);
                        final int startIndex = moves[moves.length - 1];
                        int i = 0;
                        while (quietTable != 0){
                            final int destinationIndex = numberOfTrailingZeros(quietTable);
                            moves[startIndex + i] = buildMove(bishopIndex, PIECE[turn][BISHOP],
                                    destinationIndex);
                            i++;
                            quietTable &= quietTable - 1;
                        }
                        moves[moves.length - 1] += i1;
                    }
                }
            }
            myBishops &= (myBishops - 1);
        }
        while (myRooks != 0){
            final long rook = getFirstPiece(myRooks);
            if ((rook & ignoreThesePieces) == 0) {
                final int rookIndex = numberOfTrailingZeros(rook);
                final long slides = singleRookTable(allPieces, rookIndex, mask);
                if (slides != 0) {
                    long captureTable = slides & allPieces;
                    if (captureTable != 0) {
                        final int i1 = populationCount(captureTable);
                        final int startIndex = moves[moves.length - 1];
                        int i = 0;
                        while (captureTable != 0){
                            final int destinationIndex = numberOfTrailingZeros(captureTable);
                            moves[startIndex + i] = buildMove(rookIndex, PIECE[turn][ROOK],
                                    destinationIndex, pieceSquareTable[destinationIndex]);
                            i++;
                            captureTable &= captureTable - 1;
                        }
                        moves[moves.length - 1] += i1;
                        
                    }
                    long quietTable = slides & ~allPieces;
                    if (quietTable != 0) {
                        final int i1 = populationCount(quietTable);
                        final int startIndex = moves[moves.length - 1];
                        int i = 0;
                        while (quietTable != 0){
                            final int destinationIndex = numberOfTrailingZeros(quietTable);
                            moves[startIndex + i] = buildMove(rookIndex, PIECE[turn][ROOK],
                                    destinationIndex);
                            i++;
                            quietTable &= quietTable - 1;
                        }
                        moves[moves.length - 1] += i1;
                    }
                }
            }
            myRooks &= (myRooks - 1);
        }
        while (myQueens != 0){
            final long queen = getFirstPiece(myQueens);
            if ((queen & ignoreThesePieces) == 0) {
                final int queenIndex = numberOfTrailingZeros(queen);
                final long slides = singleQueenTable(allPieces, queenIndex, mask);
                if (slides != 0) {
                    long captureTable = slides & allPieces;
                    if (captureTable != 0) {
                        final int i1 = populationCount(captureTable);
                        final int startIndex = moves[moves.length - 1];
                        int i = 0;
                        while (captureTable != 0){
                            final int destinationIndex = numberOfTrailingZeros(captureTable);
                            moves[startIndex + i] = buildMove(queenIndex, PIECE[turn][QUEEN],
                                    destinationIndex, pieceSquareTable[destinationIndex]);
                            i++;
                            captureTable &= captureTable - 1;
                        }
                        moves[moves.length - 1] += i1;
                        
                    }
                    long quietTable = slides & ~allPieces;
                    if (quietTable != 0) {
                        final int i1 = populationCount(quietTable);
                        final int startIndex = moves[moves.length - 1];
                        int i = 0;
                        while (quietTable != 0){
                            final int destinationIndex = numberOfTrailingZeros(quietTable);
                            moves[startIndex + i] = buildMove(queenIndex, PIECE[turn][QUEEN],
                                    destinationIndex);
                            i++;
                            quietTable &= quietTable - 1;
                        }
                        moves[moves.length - 1] += i1;
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
            long quietTable;
            // doubles
            if ((pawn & PENULTIMATE_RANKS[1 - turn]) != 0) {
                quietTable = singlePawnPushes(pawn, turn, legalPushes, allPieces);
            }
            else {
                quietTable = (turn == WHITE ? pawn << 8 : pawn >>> 8) & ~allPieces & legalPushes;
            }

            long captureTable = singlePawnCaptures(pawn, turn, legalCaptures);
            if (captureTable != 0) {
                final int i1 = populationCount(captureTable);
                final int startIndex = moves[moves.length - 1];
                int i = 0;
                while (captureTable != 0){
                    final int destinationIndex = numberOfTrailingZeros(captureTable);
                    moves[startIndex + i] = buildMove(pawnIndex, PIECE[turn][PAWN],
                            destinationIndex, pieceSquareTable[destinationIndex]);
                    i++;
                    captureTable &= captureTable - 1;
                }
                moves[moves.length - 1] += i1;
            }
            
            if (quietTable != 0) {
                final int i1 = populationCount(quietTable);
                final int startIndex = moves[moves.length - 1];
                int i = 0;
                while (quietTable != 0){
                    final int destinationIndex = numberOfTrailingZeros(quietTable);
                    moves[startIndex + i] = buildMove(pawnIndex, PIECE[turn][PAWN],
                            destinationIndex);
                    i++;
                    quietTable &= quietTable - 1;
                }
                moves[moves.length - 1] += i1;
                
            }
            
            myPawns &= myPawns - 1;
        }
    }

}
