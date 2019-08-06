package com.github.louism33.chesscore;

import org.junit.Assert;

import static com.github.louism33.chesscore.BitOperations.extractRayFromTwoPiecesBitboard;
import static com.github.louism33.chesscore.BitOperations.populationCount;
import static com.github.louism33.chesscore.BoardConstants.*;
import static com.github.louism33.chesscore.MoveGeneratorPseudo.addAllMovesWithoutKing;
import static com.github.louism33.chesscore.MoveGeneratorRegular.addKingLegalMovesOnly;
import static com.github.louism33.chesscore.MoveGeneratorSpecial.addEnPassantMoves;
import static com.github.louism33.chesscore.MoveGeneratorSpecial.addPromotionMoves;

final class MoveGeneratorCheck {

    static void addCheckEvasionMoves(long checkingPiece, int[] moves, int turn, int[] pieceSquareTable, long[][] pieces,
                                     boolean hasPreviousMove, long peek, long pinnedPieces,
                                     long myPawns, long myKnights, long myBishops, long myRooks, long myQueens, long myKing,
                                     long enemyPawns, long enemyKnights, long enemyBishops, long enemyRooks, long enemyQueens, long enemyKing,
                                     long enemies, long friends, long allPieces){

        Assert.assertEquals(1, populationCount(checkingPiece));

        // if a piece in pinned to the king, it can never be used to block / capture a different checker
        long blockingSquaresMask;
        switch (pieceSquareTable[Long.numberOfTrailingZeros(checkingPiece)]) {
            case WHITE_BISHOP:
            case BLACK_BISHOP:
            case WHITE_ROOK:
            case BLACK_ROOK:
            case WHITE_QUEEN:
            case BLACK_QUEEN:
                blockingSquaresMask = extractRayFromTwoPiecesBitboard(myKing, checkingPiece) & (~checkingPiece);
                break;
            default:
                blockingSquaresMask = 0;
        }
        
        long piecesToIgnoreAndPromotingPawns = pinnedPieces | (myPawns & PENULTIMATE_RANKS[turn]);

        addPromotionMoves(moves, turn, pieceSquareTable, pinnedPieces, blockingSquaresMask, checkingPiece,
                myPawns,
                enemies,
                allPieces);
        
        if (hasPreviousMove) {
            addEnPassantMoves(moves, peek, turn, piecesToIgnoreAndPromotingPawns, blockingSquaresMask, checkingPiece,
                    myPawns, myKing,
                    enemyPawns, enemyKnights, enemyBishops, enemyRooks, enemyQueens, enemyKing, allPieces
            );
        }
        
        addAllMovesWithoutKing (moves, pieces, turn, pieceSquareTable, piecesToIgnoreAndPromotingPawns, blockingSquaresMask, checkingPiece,
                myKnights, myBishops, myRooks, myQueens,
                allPieces);

        addKingLegalMovesOnly(moves, turn, pieces, pieceSquareTable,
                myKing,
                enemyPawns, enemyKnights, enemyBishops, enemyRooks, enemyQueens, enemyKing,
                friends,
                allPieces);
    }

}
