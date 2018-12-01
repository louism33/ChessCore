package com.github.louism33.chesscore;

import org.junit.Assert;

import static com.github.louism33.chesscore.BitOperations.*;
import static com.github.louism33.chesscore.BitboardResources.*;
import static com.github.louism33.chesscore.Setup.ready;
import static com.github.louism33.chesscore.Setup.setup;

public class PieceMove {

    public static long singlePawnPushes(Chessboard board, long pawns, boolean white, long legalPushes, long allPieces) {
        final long possiblePawnSinglePushes = white ? pawns << 8 : pawns >>> 8;
        final long intermediateRank = white ? BitboardResources.RANK_THREE : BitboardResources.RANK_SIX;
        long possibleDoubles = (((possiblePawnSinglePushes & intermediateRank & ~allPieces) ));
        return (possiblePawnSinglePushes | (white ? possibleDoubles << 8 : possibleDoubles >>> 8))
                & legalPushes & ~allPieces;
    }

    public static long singlePawnCaptures(long piece, boolean white, long legalCaptures) {
        return legalCaptures & (white
                ? PAWN_CAPTURE_TABLE_WHITE[getIndexOfFirstPiece(piece)]
                : PAWN_CAPTURE_TABLE_BLACK[getIndexOfFirstPiece(piece)]);
    }

    public static long masterPawnCapturesTable(Chessboard board, boolean white,
                                        long ignoreThesePieces, long legalCaptures, long pawns){
        long ans = 0;
        while (pawns != 0){
            final long pawn = BitOperations.getFirstPiece(pawns);
            if ((pawn & ignoreThesePieces) == 0) {
                ans |= singlePawnCaptures(pawn, white, legalCaptures);
            }
            pawns &= pawns - 1;
        }
        return ans;
    }


    public static long singleKnightTable(long piece, long mask){
        return KNIGHT_MOVE_TABLE[getIndexOfFirstPiece(piece)] & mask;
    }

    public static long masterAttackTableKnights(Chessboard board, boolean white,
                                         long ignoreThesePieces, long legalPushes, long legalCaptures,
                                         long knights){
        long ans = 0;
        while (knights != 0) {
            final long knight = getFirstPiece(knights);
            if ((knight & ignoreThesePieces) == 0) {
                ans |= singleKnightTable(knight, legalPushes | legalCaptures);
            }
            knights &= knights - 1;
        }
        return ans;
    }

    public static long singleBishopTable(long occupancy, boolean white, long piece, long legalCaptures){
        return singleBishopMagicMoves(occupancy, piece, legalCaptures);
    }

    public static long singleRookTable(long occupancy, boolean white, long piece, long legalPushes){
        return singleRookMagicMoves(occupancy, piece, legalPushes);
    }

    public static long singleQueenTable(long occupancy, boolean white, long piece, long mask){
        return singleBishopMagicMoves(occupancy, piece, mask) | singleRookMagicMoves(occupancy, piece, mask);
    }

    public static long masterAttackTableSliding(Chessboard board, boolean white,
                                         long ignoreThesePieces, long legalPushes, long legalCaptures,
                                         long bishops, long rooks, long queens, long allPieces){
        long mask = legalPushes | legalCaptures;
        long ans = 0;
        // board without king
        
        while (bishops != 0){
            final long bishop = getFirstPiece(bishops);
            if ((bishop & ignoreThesePieces) == 0) {
                ans |= singleBishopTable(allPieces, white, getFirstPiece(bishops), mask);
            }
            bishops &= bishops - 1;
        }

        while (rooks != 0){
            final long rook = getFirstPiece(rooks);
            if ((rook & ignoreThesePieces) == 0) {
                ans |= singleRookTable(allPieces, white, getFirstPiece(rooks), mask);
            }
            rooks &= rooks - 1;
        }

        while (queens != 0){
            final long queen = getFirstPiece(queens);
            if ((queen & ignoreThesePieces) == 0) {
                ans |= singleQueenTable(allPieces, white, getFirstPiece(queens), mask);
            }
            queens &= queens - 1;
        }

        return ans;
    }

    public static long xrayQueenAttacks(long allPieces, long blockers, long queen){
        return xrayRookAttacks(allPieces, blockers, queen) | xrayBishopAttacks(allPieces, blockers, queen);
    }

    public static long xrayRookAttacks(long allPieces, long blockers, long rook){
        final long rookMoves = singleRookTable(allPieces, true, rook, UNIVERSE);
        blockers &= rookMoves;
        return rookMoves ^ singleRookTable(allPieces ^ blockers, true, rook, UNIVERSE);
    }

    public static long xrayBishopAttacks(long allPieces, long blockers, long bishop){
        final long bishopMoves = singleBishopTable(allPieces, true, bishop, UNIVERSE);
        blockers &= bishopMoves;
        return bishopMoves ^ singleBishopTable(allPieces ^ blockers, true, bishop, UNIVERSE);
    }


    public static long singleRookMagicMoves(long occupancy, long rook, long legalMovesMask){
        if (!ready){
            setup();
        }
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
        if (!ready){
            setup();
        }
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

    public static long masterAttackTableKing(Chessboard board, boolean white,
                                      long ignoreThesePieces, long legalPushes, long legalCaptures,
                                      long kings){

        long ans = 0;
        while (kings != 0) {
            final long king = BitOperations.getFirstPiece(kings);
            if ((king & ignoreThesePieces) == 0) {
                ans |= singleKingTable(king, legalPushes | legalCaptures);
            }
            kings &= kings - 1;
        }
        return ans;
    }
    
}
