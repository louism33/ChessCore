package chessprogram.moveGeneration;

import chessprogram.bitboards.King;
import chessprogram.chessboard.BitIndexing;
import chessprogram.chessboard.Chessboard;

import java.util.List;

import static chessprogram.chessboard.BitExtractor.getAllPieces;

public class PieceMoveKing {

    public static long singleKingPushes(Chessboard board, long piece, boolean white, long legalPushes){
        return singleKingAllMoves(board, piece, white, legalPushes, 0);
    }

    public static long singleKingCaptures(Chessboard board, long piece, boolean white, long legalCaptures){
        return singleKingAllMoves(board, piece, white, 0, legalCaptures);
    }

    private static long singleKingAllMoves(Chessboard board, long piece, boolean white, long legalPushes, long legalCaptures){
        if (piece == 0) return 0;
        long table = 0;
        int index = BitIndexing.getIndexOfFirstPiece(piece);

        long l1 = King.KING_MOVE_TABLE[index];
        table |= l1;
       
        return table & (legalPushes | legalCaptures);
    }

    static long masterAttackTableKing(Chessboard board, boolean white,
                                             long ignoreThesePieces, long legalPushes, long legalCaptures){
        long ans = 0, king;
        if (white){
            king = board.getWhiteKing();
        }
        else {
            king = board.getBlackKing();
        }

        List<Long> allKings = getAllPieces(king, ignoreThesePieces);
        for (Long piece : allKings){
            ans |= singleKingAllMoves(board, piece, white, legalPushes, legalCaptures);
        }
        return ans;
    }


}
