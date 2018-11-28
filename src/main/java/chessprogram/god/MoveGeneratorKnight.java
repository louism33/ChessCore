package chessprogram.god;

import java.util.List;

import static chessprogram.god.MoveGenerationUtilities.addMovesFromAttackTableMaster;

class MoveGeneratorKnight {

    static void addKnightMoves(List<Move> moves, Chessboard board, boolean white,
                               long ignoreThesePieces, long mask){
        long knights;
        if (white){
            knights = board.getWhiteKnights();
        }
        else {
            knights = board.getBlackKnights();
        }

        while (knights != 0){
            final long knight = BitOperations.getFirstPiece(knights);
            if ((knight & ignoreThesePieces) == 0) {
                long jumps = PieceMoveKnight.singleKnightTable(knight, mask);
                addMovesFromAttackTableMaster(moves, jumps, BitOperations.getIndexOfFirstPiece(knight), board);
            }
            knights &= (knights - 1);
        }
    }
}
