package chessprogram.god;

import static chessprogram.god.BitboardResources.UNIVERSE;
import static chessprogram.god.MoveGeneratorEnPassant.addEnPassantMoves;
import static chessprogram.god.MoveGeneratorKingLegal.addKingLegalMovesOnly;
import static chessprogram.god.MoveGeneratorPromotion.addPromotionMoves;
import static chessprogram.god.MoveGeneratorPseudo.addAllMovesWithoutKing;
import static chessprogram.god.PieceMoveKnight.singleKnightTable;
import static chessprogram.god.PieceMovePawns.singlePawnCaptures;
import static chessprogram.god.PieceMoveSliding.*;

class MoveGeneratorCheck {

    static void addCheckEvasionMoves(int[] moves, Chessboard board, boolean white, long pinnedPieces,
                                     long myPawns, long myKnights, long myBishops, long myRooks, long myQueens, long myKing,
                                     long enemyPawns, long enemyKnights, long enemyBishops, long enemyRooks, long enemyQueens, long enemyKing, 
                                     long enemies, long friends, long allPieces){
        // if a piece in pinned to the king, it can never be used to block / capture a different checker
        long blockingSquaresMask, checkingPieceMask;
        long jumper = inCheckByAJumper(board, white, myKing, enemyPawns, enemyKnights);
        if (jumper != 0){
            blockingSquaresMask = 0;
            checkingPieceMask = jumper;
        }
        else {
            long slider = inCheckByASlider(board, white, myKing, enemyBishops, enemyRooks, enemyQueens, allPieces);
            blockingSquaresMask = Magic.extractRayFromTwoPiecesBitboard(myKing, slider) & (~slider);
            checkingPieceMask = slider;
        }
        long PENULTIMATE_RANK = white ? BitboardResources.RANK_SEVEN : BitboardResources.RANK_TWO;
        long promotablePawns = myPawns & PENULTIMATE_RANK;
        long piecesToIgnoreAndPromotingPawns = pinnedPieces | promotablePawns;

        addPromotionMoves(moves, board, white, pinnedPieces, blockingSquaresMask, checkingPieceMask,
                myPawns, myKnights, myBishops, myRooks, myQueens, myKing,
                enemyPawns, enemyKnights, enemyBishops, enemyRooks, enemyQueens, enemyKing,
                enemies, friends, allPieces);

        addAllMovesWithoutKing (moves, board, white, piecesToIgnoreAndPromotingPawns, blockingSquaresMask, checkingPieceMask,
                myPawns, myKnights, myBishops, myRooks, myQueens, myKing,
                enemyPawns, enemyKnights, enemyBishops, enemyRooks, enemyQueens, enemyKing,
                enemies, friends, allPieces);

        addKingLegalMovesOnly(moves, board, white,
                myPawns, myKnights, myBishops, myRooks, myQueens, myKing,
                enemyPawns, enemyKnights, enemyBishops, enemyRooks, enemyQueens, enemyKing,
                enemies, friends, allPieces);

        addEnPassantMoves(moves, board, white, piecesToIgnoreAndPromotingPawns, blockingSquaresMask, checkingPieceMask,
                myPawns, myKnights, myBishops, myRooks, myQueens, myKing,
                enemyPawns, enemyKnights, enemyBishops, enemyRooks, enemyQueens, enemyKing,
                enemies, friends, allPieces);
    }

    private static long inCheckByAJumper(Chessboard board, boolean white,
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

    // todo, square threatened by?
    private static long inCheckByASlider(Chessboard board, boolean white,
                                         long myKing,
                                         long enemyBishops, long enemyRooks, long enemyQueens,
                                         long allPieces){
        long possibleBishop = singleBishopTable(allPieces, white, myKing, enemyBishops);
        if (possibleBishop != 0) {
            return possibleBishop;
        }
        long possibleRook = singleRookTable(allPieces, white, myKing, enemyRooks);
        if (possibleRook != 0){
            return possibleRook;
        }
        long possibleQueen = singleQueenTable(allPieces, white, myKing, enemyQueens);
        if (possibleQueen != 0){
            return possibleQueen;
        }
        return 0;
    }

}
