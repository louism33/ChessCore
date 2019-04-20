package com.github.louism33.chesscore;

import com.github.louism33.utils.MoveParserFromAN;
import org.junit.Test;

import static com.github.louism33.chesscore.BoardConstants.*;
import static com.github.louism33.chesscore.PieceMove.*;
import static java.lang.Long.lowestOneBit;

public final class Utils {

    // todo consider keeping this autoincremented in generate
    public static final long squareDirectlyAttackedBy(Chessboard board, int squareIndex) {
        int turn = board.turn;
        long friends = board.pieces[turn][ALL_COLOUR_PIECES];
        long enemies = board.pieces[1 - turn][ALL_COLOUR_PIECES];
        final long allPieces = friends | enemies;

        long allAttackers = 0;

        allAttackers |= PAWN_CAPTURE_TABLE[turn][squareIndex] & board.pieces[1 - turn][PAWN];
        allAttackers |= PAWN_CAPTURE_TABLE[1 - turn][squareIndex] & board.pieces[turn][PAWN];
        allAttackers |= BoardConstants.KNIGHT_MOVE_TABLE[squareIndex]
                & (board.pieces[turn][KNIGHT] | board.pieces[1 - turn][KNIGHT]);
        allAttackers |= singleBishopTable(allPieces, squareIndex, UNIVERSE)
                & (board.pieces[turn][BISHOP] | board.pieces[1 - turn][BISHOP]
                | board.pieces[turn][QUEEN] | board.pieces[1 - turn][QUEEN]);
        allAttackers |= singleRookTable(allPieces, squareIndex, UNIVERSE)
                & (board.pieces[turn][ROOK] | board.pieces[1 - turn][ROOK]
                | board.pieces[turn][QUEEN] | board.pieces[1 - turn][QUEEN]);
        allAttackers |= KING_MOVE_TABLE[squareIndex]
                & (board.pieces[turn][KING] | board.pieces[1 - turn][KING]);

        return allAttackers;
    }

    public static final long xRayToSquare(long[][] pieces, long occupancy, long removePiece, int destinationIndex) {
        final long source = BitOperations.newPieceOnSquare(destinationIndex);
        final long queens = pieces[WHITE][QUEEN] | pieces[BLACK][QUEEN];
        long answer = xrayBishopAttacks(occupancy, removePiece, source)
                & (pieces[BLACK][BISHOP] | pieces[WHITE][BISHOP] | queens);
        answer |= xrayRookAttacks(occupancy, removePiece, source)
                & (pieces[BLACK][ROOK] | pieces[WHITE][ROOK] | queens);
        return answer;
    }

}
