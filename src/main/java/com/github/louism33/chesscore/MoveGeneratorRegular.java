package com.github.louism33.chesscore;

import static com.github.louism33.chesscore.BitOperations.getFirstPiece;
import static com.github.louism33.chesscore.BitOperations.populationCount;
import static com.github.louism33.chesscore.BoardConstants.*;
import static com.github.louism33.chesscore.CheckHelper.bitboardOfPiecesThatLegalThreatenSquare;
import static com.github.louism33.chesscore.MoveAdder.addMovesFromAttackTableMaster;
import static com.github.louism33.chesscore.MoveParser.buildMove;
import static com.github.louism33.chesscore.PieceMove.*;
import static java.lang.Long.numberOfTrailingZeros;

class MoveGeneratorRegular {

    static void addKingLegalMovesOnly(int[] moves, int turn, long[][] pieces, int[] pieceSquareTable, long myKing,
                                      long enemyPawns, long enemyKnights, long enemyBishops, long enemyRooks, long enemyQueens, long enemyKing,
                                      long friends, long allPieces){

        final int myKingIndex = numberOfTrailingZeros(myKing);
        final long kingPseudoMoves = KING_MOVE_TABLE[myKingIndex] & ~friends;
        long table;
        if (kingPseudoMoves == 0) {
            return;
        }
        if (populationCount(kingPseudoMoves) == 1) {
            if (populationCount(bitboardOfPiecesThatLegalThreatenSquare(turn, kingPseudoMoves,
                    enemyPawns, enemyKnights, enemyBishops, enemyRooks, enemyQueens, enemyKing, allPieces, 1)) != 0){
                return;
            }            
            
            table = kingPseudoMoves;
        }
        else {
            pieces[turn][KING] = 0;

            allPieces ^= myKing;

            long kingDangerSquares = 0;

            while (enemyKing != 0) {
                kingDangerSquares |= KING_MOVE_TABLE[numberOfTrailingZeros(enemyKing)];
                enemyKing &= enemyKing - 1;
            }

            while (enemyKnights != 0) {
                kingDangerSquares |= KNIGHT_MOVE_TABLE[numberOfTrailingZeros(enemyKnights)];
                enemyKnights &= enemyKnights - 1;
            }

            while (enemyBishops != 0) {
                kingDangerSquares |= singleBishopTable(allPieces, getFirstPiece(enemyBishops), UNIVERSE);
                enemyBishops &= enemyBishops - 1;
            }

            while (enemyRooks != 0) {
                kingDangerSquares |= singleRookTable(allPieces, getFirstPiece(enemyRooks), UNIVERSE);
                enemyRooks &= enemyRooks - 1;
            }

            while (enemyQueens != 0) {
                kingDangerSquares |= singleQueenTable(allPieces, getFirstPiece(enemyQueens), UNIVERSE);
                enemyQueens &= enemyQueens - 1;
            }

            enemyPawns &= bigAdjacentSquares[myKingIndex];
            while (enemyPawns != 0) {
                kingDangerSquares |= PAWN_CAPTURE_TABLE[1 - turn][numberOfTrailingZeros(enemyPawns)];
                enemyPawns &= enemyPawns - 1;
            }

            pieces[turn][KING] = myKing;

            table = kingPseudoMoves & ~kingDangerSquares;
        }
        
        if (table != 0) {
            long captureTable = table & allPieces;
            if (captureTable != 0) {

                final int i1 = populationCount(captureTable);
                final int startIndex = moves[moves.length - 1];
                int i = 0;
                while (captureTable != 0){
                    final int destinationIndex = numberOfTrailingZeros(captureTable);
                    moves[startIndex + i] = buildMove(numberOfTrailingZeros(myKing), PIECE[turn][KING],
                            destinationIndex, pieceSquareTable[destinationIndex]);
                    i++;
                    captureTable &= captureTable - 1;
                }   
                moves[moves.length - 1] += i1;
            }
            final long quietTable = table & ~allPieces;
            if (quietTable != 0) {
                addMovesFromAttackTableMaster(moves,
                        quietTable,
                        numberOfTrailingZeros(myKing),
                        PIECE[turn][KING]);
            }
        }
    }

}
