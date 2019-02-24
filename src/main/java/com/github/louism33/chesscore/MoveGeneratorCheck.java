package com.github.louism33.chesscore;

import static com.github.louism33.chesscore.BitOperations.extractRayFromTwoPiecesBitboard;
import static com.github.louism33.chesscore.BoardConstants.UNIVERSE;
import static com.github.louism33.chesscore.MoveGeneratorPseudo.addAllMovesWithoutKing;
import static com.github.louism33.chesscore.MoveGeneratorRegular.addKingLegalMovesOnly;
import static com.github.louism33.chesscore.MoveGeneratorSpecial.addEnPassantMoves;
import static com.github.louism33.chesscore.MoveGeneratorSpecial.addPromotionMoves;
import static com.github.louism33.chesscore.PieceMove.*;

class MoveGeneratorCheck {

    static void addCheckEvasionMoves(int[] moves, Chessboard board, boolean white, long pinnedPieces,
                                     long myPawns, long myKnights, long myBishops, long myRooks, long myQueens, long myKing,
                                     long enemyPawns, long enemyKnights, long enemyBishops, long enemyRooks, long enemyQueens, long enemyKing, 
                                     long enemies, long friends, long allPieces){
        // if a piece in pinned to the king, it can never be used to block / capture a different checker
        long blockingSquaresMask, checkingPieceMask;
        long jumper = inCheckByAJumper(white, myKing, enemyPawns, enemyKnights);
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

        addPromotionMoves(moves, board.turn, board.pieceSquareTable, pinnedPieces, blockingSquaresMask, checkingPieceMask,
                myPawns,
                enemies, allPieces);

        addAllMovesWithoutKing (moves, board.pieces, board.turn, board.pieceSquareTable, piecesToIgnoreAndPromotingPawns, blockingSquaresMask, checkingPieceMask,
                myKnights, myBishops, myRooks, myQueens,
                allPieces);

        addKingLegalMovesOnly(moves, board.turn, board.pieces, board.pieceSquareTable,
                myKing,
                enemyPawns, enemyKnights, enemyBishops, enemyRooks, enemyQueens, enemyKing,
                enemies, allPieces);

        addEnPassantMoves(moves, board, white, piecesToIgnoreAndPromotingPawns, blockingSquaresMask, checkingPieceMask,
                myPawns, myKing,
                enemyPawns, enemyKnights, enemyBishops, enemyRooks, enemyQueens, enemyKing
        );
    }

    private static long inCheckByAJumper(boolean white,
                                         long myKing, long enemyPawns, long enemyKnights){
        long possiblePawn = singlePawnCaptures(myKing, white, enemyPawns);
        if (possiblePawn != 0) {
            return possiblePawn;
        }
        long possibleKnight = singleKnightTable(myKing, UNIVERSE) & enemyKnights;
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
