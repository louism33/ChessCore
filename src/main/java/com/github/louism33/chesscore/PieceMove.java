package com.github.louism33.chesscore;

import org.junit.Assert;

import static com.github.louism33.chesscore.BitOperations.getIndexOfFirstPiece;
import static com.github.louism33.chesscore.BitOperations.populationCount;
import static com.github.louism33.chesscore.BoardConstants.*;
import static com.github.louism33.chesscore.Setup.ready;

class PieceMove {

    static long singlePawnPushes(long pawns, int turn, long legalPushes, long allPieces) {
        final long possiblePawnSinglePushes = turn == WHITE ? pawns << 8 : pawns >>> 8;
        final long intermediateRank = INTERMEDIATE_RANKS[turn];
        long possibleDoubles = (((possiblePawnSinglePushes & intermediateRank & ~allPieces) ));
        return (possiblePawnSinglePushes | (turn == WHITE ? possibleDoubles << 8 : possibleDoubles >>> 8))
                & legalPushes & ~allPieces;
    }
    
    static long singlePawnPushes(long pawns, boolean white, long legalPushes, long allPieces) {
        final long possiblePawnSinglePushes = white ? pawns << 8 : pawns >>> 8;
        final long intermediateRank = white ? BoardConstants.RANK_THREE : BoardConstants.RANK_SIX;
        long possibleDoubles = (((possiblePawnSinglePushes & intermediateRank & ~allPieces) ));
        return (possiblePawnSinglePushes | (white ? possibleDoubles << 8 : possibleDoubles >>> 8))
                & legalPushes & ~allPieces;
    }

    static long singlePawnCaptures(long piece, int turn, long legalCaptures) {
        Assert.assertTrue(getIndexOfFirstPiece(piece) > 0 || getIndexOfFirstPiece(piece) <= 63);
        return legalCaptures & (PAWN_CAPTURE_TABLE[turn][getIndexOfFirstPiece(piece)]);
    }
    
    static long singlePawnCaptures(long piece, boolean white, long legalCaptures) {
        Assert.assertTrue(getIndexOfFirstPiece(piece) > 0 || getIndexOfFirstPiece(piece) <= 63);
        return legalCaptures & (white
                ? PAWN_CAPTURE_TABLE_WHITE[getIndexOfFirstPiece(piece)]
                : PAWN_CAPTURE_TABLE_BLACK[getIndexOfFirstPiece(piece)]);
    }

    static long singleKnightTable(long piece, long mask){
        return KNIGHT_MOVE_TABLE[getIndexOfFirstPiece(piece)] & mask;
    }

    public static long singleBishopTable(long occupancy, long piece, long legalCaptures){
        return singleBishopMagicMoves(occupancy, piece, legalCaptures);
    }

    public static long singleRookTable(long occupancy, long piece, long legalPushes){
        return singleRookMagicMoves(occupancy, piece, legalPushes);
    }

    public static long singleQueenTable(long occupancy, long piece, long mask){
        return singleBishopMagicMoves(occupancy, piece, mask) | singleRookMagicMoves(occupancy, piece, mask);
    }

    public static long xrayQueenAttacks(long allPieces, long blockers, long queen){
        return xrayRookAttacks(allPieces, blockers, queen) | xrayBishopAttacks(allPieces, blockers, queen);
    }

    public static long xrayRookAttacks(long allPieces, long blockers, long rook){
        final long rookMoves = singleRookTable(allPieces, rook, UNIVERSE);
        blockers &= rookMoves;
        return rookMoves ^ singleRookTable(allPieces ^ blockers, rook, UNIVERSE);
    }

    public static long xrayBishopAttacks(long allPieces, long blockers, long bishop){
        final long bishopMoves = singleBishopTable(allPieces, bishop, UNIVERSE);
        blockers &= bishopMoves;
        return bishopMoves ^ singleBishopTable(allPieces ^ blockers, bishop, UNIVERSE);
    }


    public static long singleRookMagicMoves(long occupancy, long rook, long legalMovesMask){
        Assert.assertTrue(ready);
        Assert.assertEquals(populationCount(rook), 1);

        final int rookIndex = getIndexOfFirstPiece(rook);
        final long rookMagicNumber = rookMagicNumbers[rookIndex];

        final int index = (int) (((occupancy & rookBlankBoardAttackMasks[rookIndex]) * rookMagicNumber)
                >>> (64 - (rookShiftAmounts[rookIndex])));

        final long legalMoves = rookDatabase[rookIndex][index];

        return legalMoves & legalMovesMask;
    }

    public static long singleBishopMagicMoves(long allPieces, long bishop, long legalMovesMask){
        Assert.assertTrue(ready);
        Assert.assertEquals(populationCount(bishop), 1);

        final int bishopIndex = getIndexOfFirstPiece(bishop);
        final long bishopMagicNumber = bishopMagicNumbers[bishopIndex];

        final int index = (int) (((allPieces & bishopBlankBoardAttackMasks[bishopIndex]) * bishopMagicNumber)
                >>> (64 - (bishopShiftAmounts[bishopIndex])));

        final long legalMoves = bishopDatabase[bishopIndex][index];

        return legalMoves & legalMovesMask;
    }


    public static long singleKingTable(long piece, long mask){
        return KING_MOVE_TABLE[getIndexOfFirstPiece(piece)] & mask;
    }

}
