package chessprogram.god;

import java.util.List;

import static chessprogram.god.BitOperations.*;
import static chessprogram.god.Magic.singleBishopMagicMoves;
import static chessprogram.god.Magic.singleRookMagicMoves;

class PieceMoveSliding {

    static long singleBishopTable(Chessboard board, boolean white, long piece, long legalCaptures){
        return singleBishopMagicMoves(board, white, piece, legalCaptures);
    }

    static long singleRookTable(Chessboard board, boolean white, long piece, long legalPushes){
        return singleRookMagicMoves(board, white, piece, legalPushes);
    }

    static long singleQueenTable(Chessboard board, boolean white, long piece, long mask){
        return singleBishopMagicMoves(board, white, piece, mask) | singleRookMagicMoves(board, white, piece, mask);
    }

    static long masterAttackTableSliding(Chessboard board, boolean white,
                                                long ignoreThesePieces, long legalPushes, long legalCaptures){
        long mask = legalPushes | legalCaptures;
        long ans = 0, bishops, rooks, queens;
        
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
            ans |= singleBishopTable(board, white, piece, mask);
        }

        List<Long> allRooks = getAllPieces(rooks, ignoreThesePieces);
        for (Long piece : allRooks){
            ans |= singleRookTable(board, white, piece, mask);
        }

        List<Long> allQueens = getAllPieces(queens, ignoreThesePieces);
        for (Long piece : allQueens){
            ans |= singleQueenTable(board, white, piece, mask);
        }
        return ans;
    }
}
