package chessprogram.god;

import java.util.List;

import static chessprogram.god.BitOperations.*;
import static chessprogram.god.Magic.singleBishopMagicMoves;
import static chessprogram.god.Magic.singleRookMagicMoves;

class PieceMoveSliding {

    static long singleBishopTable(long occupancy, boolean white, long piece, long legalCaptures){
        return singleBishopMagicMoves(occupancy, piece, legalCaptures);
    }

    static long singleRookTable(long occupancy, boolean white, long piece, long legalPushes){
        return singleRookMagicMoves(occupancy, piece, legalPushes);
    }

    static long singleQueenTable(long occupancy, boolean white, long piece, long mask){
        return singleBishopMagicMoves(occupancy, piece, mask) | singleRookMagicMoves(occupancy, piece, mask);
    }

    static long masterAttackTableSliding(Chessboard board, boolean white,
                                                long ignoreThesePieces, long legalPushes, long legalCaptures){
        long mask = legalPushes | legalCaptures;
        long ans = 0, bishops, rooks, queens, allPieces = board.allPieces();
        if (white){
            bishops = board.getWhiteBishops();
            rooks = board.getWhiteRooks();
            queens = board.getWhiteQueen();
        }
        else {
            bishops = board.getBlackBishops();
            rooks = board.getBlackRooks();
            queens = board.getBlackQueen();
        }

        List<Long> allBishops = getAllPieces(bishops, ignoreThesePieces);
        for (Long piece : allBishops){
            ans |= singleBishopTable(allPieces, white, piece, mask);
        }

        List<Long> allRooks = getAllPieces(rooks, ignoreThesePieces);
        for (Long piece : allRooks){
            ans |= singleRookTable(allPieces, white, piece, mask);
        }

        List<Long> allQueens = getAllPieces(queens, ignoreThesePieces);
        for (Long piece : allQueens){
            ans |= singleQueenTable(allPieces, white, piece, mask);
        }
        return ans;
    }

    static long xrayQueenAttacks(long allPieces, long blockers, long queen){
        return xrayRookAttacks(allPieces, blockers, queen) | xrayBishopAttacks(allPieces, blockers, queen);
    }
    
    static long xrayRookAttacks(long allPieces, long blockers, long rook){
        final long rookMoves = singleRookTable(allPieces, true, rook, UNIVERSE);
        blockers &= rookMoves;
        return rookMoves ^ singleRookTable(allPieces ^ blockers, true, rook, UNIVERSE);
    }

    static long xrayBishopAttacks(long allPieces, long blockers, long bishop){
        final long bishopMoves = singleBishopTable(allPieces, true, bishop, UNIVERSE);
        blockers &= bishopMoves;
        return bishopMoves ^ singleBishopTable(allPieces ^ blockers, true, bishop, UNIVERSE);
    }
}
