package com.github.louism33.chesscore;

import org.junit.Assert;

import static com.github.louism33.chesscore.BitOperations.populationCount;
import static com.github.louism33.chesscore.BoardConstants.*;
import static com.github.louism33.chesscore.Setup.ready;
import static java.lang.Long.numberOfTrailingZeros;

final class PieceMove {

    static long singlePawnPushes(long pawns, int turn, long legalPushes, long allPieces) {
        Assert.assertEquals(1, populationCount(pawns));
        final long possiblePawnSinglePushes = turn == WHITE ? pawns << 8 : pawns >>> 8;
        final long intermediateRank = INTERMEDIATE_RANKS[turn];
        final long possibleDoubles = (((possiblePawnSinglePushes & intermediateRank & ~allPieces) ));
        return (possiblePawnSinglePushes | (turn == WHITE ? possibleDoubles << 8 : possibleDoubles >>> 8))
                & legalPushes & ~allPieces;
    }
    
    static long singlePawnCaptures(long piece, int turn, long legalCaptures) {
        Assert.assertTrue(numberOfTrailingZeros(piece) > 0 || numberOfTrailingZeros(piece) <= 63);
        return legalCaptures & (PAWN_CAPTURE_TABLE[turn][numberOfTrailingZeros(piece)]);
    }
    
    static long singleQueenTable(long occupancy, long piece, long mask){
        return singleBishopTable(occupancy, piece, mask) | singleRookTable(occupancy, piece, mask);
    }

    static long singleQueenTable(long occupancy, int pieceIndex, long mask){
        return singleBishopTable(occupancy, pieceIndex, mask) | singleRookTable(occupancy, pieceIndex, mask);
    }

    static long xrayQueenAttacks(long allPieces, long blockers, long queen){
        return xrayRookAttacks(allPieces, blockers, queen) | xrayBishopAttacks(allPieces, blockers, queen);
    }

    static long xrayRookAttacks(long allPieces, long blockers, long rook){
        final long rookMoves = singleRookTable(allPieces, rook, UNIVERSE);
        blockers &= rookMoves;
        return rookMoves ^ singleRookTable(allPieces ^ blockers, rook, UNIVERSE);
    }

    static long xrayBishopAttacks(long allPieces, long blockers, long bishop){
        final long bishopMoves = singleBishopTable(allPieces, bishop, UNIVERSE);
        blockers &= bishopMoves;
        return bishopMoves ^ singleBishopTable(allPieces ^ blockers, bishop, UNIVERSE);
    }


    static long singleRookTable(long occupancy, long rook, long legalMovesMask){
        Assert.assertTrue(ready);
        Assert.assertEquals(populationCount(rook), 1);

        final int rookIndex = numberOfTrailingZeros(rook);
        final long rookMagicNumber = rookMagicNumbers[rookIndex];

        final int index = (int) (((occupancy & rookBlankBoardAttackMasks[rookIndex]) * rookMagicNumber)
                >>> (64 - (rookShiftAmounts[rookIndex])));

        final long legalMoves = rookDatabase[rookIndex][index];

        return legalMoves & legalMovesMask;
    }

    static long singleRookTable(long occupancy, int rookIndex, long legalMovesMask){
        Assert.assertTrue(ready);
        final long rookMagicNumber = rookMagicNumbers[rookIndex];

        final int index = (int) (((occupancy & rookBlankBoardAttackMasks[rookIndex]) * rookMagicNumber)
                >>> (64 - (rookShiftAmounts[rookIndex])));

        final long legalMoves = rookDatabase[rookIndex][index];

        return legalMoves & legalMovesMask;
    }

    static long singleBishopTable(long allPieces, long bishop, long legalMovesMask){
        Assert.assertTrue(ready);
        Assert.assertEquals(populationCount(bishop), 1);

        final int bishopIndex = numberOfTrailingZeros(bishop);
        final long bishopMagicNumber = bishopMagicNumbers[bishopIndex];

        final int index = (int) (((allPieces & bishopBlankBoardAttackMasks[bishopIndex]) * bishopMagicNumber)
                >>> (64 - (bishopShiftAmounts[bishopIndex])));

        final long legalMoves = bishopDatabase[bishopIndex][index];

        return legalMoves & legalMovesMask;
    }

    static long singleBishopTable(long allPieces, int bishopIndex, long legalMovesMask){
        Assert.assertTrue(ready);

        final long bishopMagicNumber = bishopMagicNumbers[bishopIndex];

        final int index = (int) (((allPieces & bishopBlankBoardAttackMasks[bishopIndex]) * bishopMagicNumber)
                >>> (64 - (bishopShiftAmounts[bishopIndex])));

        final long legalMoves = bishopDatabase[bishopIndex][index];

        return legalMoves & legalMovesMask;
    }

}
