package com.github.louism33.chesscore;

import static com.github.louism33.chesscore.BitOperations.extractRayFromTwoPiecesBitboard;
import static com.github.louism33.chesscore.BitOperations.getIndexOfFirstPiece;
import static com.github.louism33.chesscore.BoardConstants.*;
import static com.github.louism33.chesscore.MoveGeneratorPseudo.addAllMovesWithoutKing;
import static com.github.louism33.chesscore.MoveGeneratorRegular.addKingLegalMovesOnly;
import static com.github.louism33.chesscore.MoveGeneratorSpecial.addEnPassantMoves;
import static com.github.louism33.chesscore.MoveGeneratorSpecial.addPromotionMoves;
import static com.github.louism33.chesscore.PieceMove.*;

class MoveGeneratorCheck {

    static void addCheckEvasionMoves(int[] moves, int turn, int[] pieceSquareTable, long[][] pieces,
                                     boolean hasPreviousMove, long peek, boolean white, long pinnedPieces,
                                     long myPawns, long myKnights, long myBishops, long myRooks, long myQueens, long myKing,
                                     long enemyPawns, long enemyKnights, long enemyBishops, long enemyRooks, long enemyQueens, long enemyKing,
                                     long enemies, long friends, long allPieces){
        // if a piece in pinned to the king, it can never be used to block / capture a different checker
        long blockingSquaresMask, checkingPieceMask;
        long jumper = inCheckByAJumper(turn, myKing, enemyPawns, enemyKnights);
        if (jumper != 0){
            blockingSquaresMask = 0;
            checkingPieceMask = jumper;
        }
        else {
            long slider = inCheckByASlider(myKing, enemyBishops, enemyRooks, enemyQueens, allPieces);
            blockingSquaresMask = extractRayFromTwoPiecesBitboard(myKing, slider) & (~slider);
            checkingPieceMask = slider;
        }
        long PENULTIMATE_RANK = white ? BoardConstants.RANK_SEVEN : BoardConstants.RANK_TWO;
        long promotablePawns = myPawns & PENULTIMATE_RANK;
        long piecesToIgnoreAndPromotingPawns = pinnedPieces | promotablePawns;

        addPromotionMoves(moves, turn, pieceSquareTable, pinnedPieces, blockingSquaresMask, checkingPieceMask,
                myPawns,
                enemies, allPieces);

        addAllMovesWithoutKing (moves, pieces, turn, pieceSquareTable, piecesToIgnoreAndPromotingPawns, blockingSquaresMask, checkingPieceMask,
                myKnights, myBishops, myRooks, myQueens,
                allPieces);

        addKingLegalMovesOnly(moves, turn, pieces, pieceSquareTable,
                myKing,
                enemyPawns, enemyKnights, enemyBishops, enemyRooks, enemyQueens, enemyKing,
                friends, enemies, allPieces);

        if (hasPreviousMove) {
            addEnPassantMoves(moves, peek, turn, piecesToIgnoreAndPromotingPawns, blockingSquaresMask, checkingPieceMask,
                    myPawns, myKing,
                    enemyPawns, enemyKnights, enemyBishops, enemyRooks, enemyQueens, enemyKing, allPieces
            );

        }
    }

    private static long inCheckByAJumper(int turn, long myKing, long enemyPawns, long enemyKnights){
        long possiblePawn = singlePawnCaptures(myKing, turn, enemyPawns);
        if (possiblePawn != 0) {
            return possiblePawn;
        }
        long possibleKnight = KNIGHT_MOVE_TABLE[getIndexOfFirstPiece(myKing)] & enemyKnights;
        if (possibleKnight != 0) {
            return possibleKnight;
        }

        return 0;
    }


    private static long inCheckByASlider(long myKing,
                                         long enemyBishops, long enemyRooks, long enemyQueens,
                                         long allPieces){
        long possibleBishop = singleBishopTable(allPieces, myKing, enemyBishops);
        if (possibleBishop != 0) {
            return possibleBishop;
        }
        long possibleRook = singleRookTable(allPieces, myKing, enemyRooks);
        if (possibleRook != 0){
            return possibleRook;
        }
        long possibleQueen = singleQueenTable(allPieces, myKing, enemyQueens);
        if (possibleQueen != 0){
            return possibleQueen;
        }
        return 0;
    }

}
