package com.github.louism33.chesscore;

import static com.github.louism33.chesscore.BitOperations.getFirstPiece;
import static com.github.louism33.chesscore.BitOperations.getIndexOfFirstPiece;
import static com.github.louism33.chesscore.BoardConstants.*;
import static com.github.louism33.chesscore.MoveAdder.addMovesFromAttackTableMasterBetter;
import static com.github.louism33.chesscore.PieceMove.*;
import static java.lang.Long.numberOfTrailingZeros;

class MoveGeneratorRegular {

    static void addKingLegalMovesOnly(int[] moves, int turn, long[][] pieces, int[] pieceSquareTable, long myKing,
                                      long enemyPawns, long enemyKnights, long enemyBishops, long enemyRooks, long enemyQueens, long enemyKing,
                                      long enemies, long allPieces){

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

        while (enemyBishops != 0){
            kingDangerSquares |= singleBishopTable(allPieces, getFirstPiece(enemyBishops), UNIVERSE);
            enemyBishops &= enemyBishops - 1;
        }

        while (enemyRooks != 0){
            kingDangerSquares |= singleRookTable(allPieces, getFirstPiece(enemyRooks), UNIVERSE);
            enemyRooks &= enemyRooks - 1;
        }

        while (enemyQueens != 0){
            kingDangerSquares |= singleQueenTable(allPieces, getFirstPiece(enemyQueens), UNIVERSE);
            enemyQueens &= enemyQueens - 1;
        }

        while (enemyPawns != 0){
            kingDangerSquares |= PAWN_CAPTURE_TABLE[1-turn][numberOfTrailingZeros(enemyPawns)];
            enemyPawns &= enemyPawns - 1;
        }

        pieces[turn][KING] = myKing;

        long kingSafeSquares = ~kingDangerSquares;

        long kingSafeCaptures = enemies & kingSafeSquares;
        long kingSafePushes = (~allPieces & kingSafeSquares);
        long table = KING_MOVE_TABLE[numberOfTrailingZeros(myKing)] & (kingSafePushes | kingSafeCaptures);
        
        
        addMovesFromAttackTableMasterBetter(moves, table,
                getIndexOfFirstPiece(myKing),
                PIECE[turn][KING],
                pieceSquareTable);
    }

}
