package com.github.louism33.chesscore;

import org.junit.Assert;

import static com.github.louism33.chesscore.BitOperations.*;
import static com.github.louism33.chesscore.BoardConstants.*;
import static com.github.louism33.chesscore.Setup.ready;
import static com.github.louism33.chesscore.Setup.setup;

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

    static long masterPawnCapturesTable(boolean white, long ignoreThesePieces, long legalCaptures, long pawns){
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


    static long singleKnightTable(long piece, long mask){
        return KNIGHT_MOVE_TABLE[getIndexOfFirstPiece(piece)] & mask;
    }

    public static long masterAttackTableKnights(long ignoreThesePieces, long legalPushes, long legalCaptures,
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

    public static long singleBishopTable(long occupancy, long piece, long legalCaptures){
        return singleBishopMagicMoves(occupancy, piece, legalCaptures);
    }

    public static long singleRookTable(long occupancy, long piece, long legalPushes){
        return singleRookMagicMoves(occupancy, piece, legalPushes);
    }

    public static long singleQueenTable(long occupancy, long piece, long mask){
        return singleBishopMagicMoves(occupancy, piece, mask) | singleRookMagicMoves(occupancy, piece, mask);
    }

    public static long masterAttackTableSliding(long ignoreThesePieces, long legalPushes, long legalCaptures,
                                                long bishops, long rooks, long queens, long allPieces){
        long mask = legalPushes | legalCaptures;
        long ans = 0;
        
        while (bishops != 0){
            final long bishop = getFirstPiece(bishops);
            if ((bishop & ignoreThesePieces) == 0) {
                ans |= singleBishopTable(allPieces, getFirstPiece(bishops), mask);
            }
            bishops &= bishops - 1;
        }

        while (rooks != 0){
            final long rook = getFirstPiece(rooks);
            if ((rook & ignoreThesePieces) == 0) {
                ans |= singleRookTable(allPieces, getFirstPiece(rooks), mask);
            }
            rooks &= rooks - 1;
        }

        while (queens != 0){
            final long queen = getFirstPiece(queens);
            if ((queen & ignoreThesePieces) == 0) {
                ans |= singleQueenTable(allPieces, getFirstPiece(queens), mask);
            }
            queens &= queens - 1;
        }

        return ans;
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
        if (!ready){
            setup(false);
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
            setup(false);
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

    public static long masterAttackTableKing(long ignoreThesePieces, long legalPushes, long legalCaptures,
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
