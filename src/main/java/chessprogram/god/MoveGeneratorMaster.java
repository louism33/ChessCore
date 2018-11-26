package chessprogram.god;

import java.util.ArrayList;
import java.util.List;

import static chessprogram.god.BitOperations.*;
import static chessprogram.god.MoveGenerationUtilities.*;
import static chessprogram.god.MoveGeneratorCastling.*;
import static chessprogram.god.MoveGeneratorCheck.*;
import static chessprogram.god.MoveGeneratorEnPassant.*;
import static chessprogram.god.MoveGeneratorKingLegal.*;
import static chessprogram.god.MoveGeneratorPromotion.*;
import static chessprogram.god.MoveGeneratorPseudo.*;
import static chessprogram.god.PieceMovePawns.*;
import static chessprogram.god.PieceMoveSliding.*;
import static chessprogram.god.PinnedManager.*;

class MoveGeneratorMaster {

    static List<Move> generateLegalMoves(Chessboard board, boolean white) {
        List<Move> moves = new ArrayList<>();
        long myKing = (white) ? board.getWhiteKing() : board.getBlackKing();
        int numberOfCheckers = CheckHelper.numberOfPiecesThatLegalThreatenSquare(board, white, myKing);

        if (numberOfCheckers > 1){
            addKingLegalMovesOnly(moves, board, white);
            return moves;
        }
        else if (numberOfCheckers == 1){
            addCheckEvasionMoves(moves, board, white);
            return moves;
        }
        else {
            addNotInCheckMoves(moves, board, white);
            return moves;
        }
    }


    private static void addNotInCheckMoves(List<Move> moves, Chessboard board, boolean whiteTurn){
        long ENEMY_PIECES = (whiteTurn) ? board.blackPieces() : board.whitePieces();
        long ALL_EMPTY_SQUARES = ~board.allPieces();
        long myKing = (whiteTurn) ? board.getWhiteKing() : board.getBlackKing();
        
        //todo
        long pinnedPieces = whichPiecesArePinned(board, whiteTurn, myKing);
        
        long PENULTIMATE_RANK = whiteTurn ? BitboardResources.RANK_SEVEN : BitboardResources.RANK_TWO;
        long myPawns = whiteTurn ? board.getWhitePawns() : board.getBlackPawns();
        long promotablePawns = myPawns & PENULTIMATE_RANK;
        long pinnedPiecesAndPromotingPawns = pinnedPieces | promotablePawns;

        addCastlingMoves(moves, board, whiteTurn);

        addKingLegalMovesOnly(moves, board, whiteTurn);

        if (pinnedPieces == 0){
            addPromotionMoves
                    (moves, board, whiteTurn, 0, ALL_EMPTY_SQUARES, ENEMY_PIECES);

            addAllMovesWithoutKing
                    (moves, board, whiteTurn, promotablePawns, ALL_EMPTY_SQUARES, ENEMY_PIECES);

            addEnPassantMoves
                    (moves, board, whiteTurn, promotablePawns, ALL_EMPTY_SQUARES, ENEMY_PIECES);
        }
        else {
            addPromotionMoves
                    (moves, board, whiteTurn, pinnedPieces, ALL_EMPTY_SQUARES, ENEMY_PIECES);

            addAllMovesWithoutKing
                    (moves, board, whiteTurn, pinnedPiecesAndPromotingPawns, ~board.allPieces(), ENEMY_PIECES);

            addEnPassantMoves
                    (moves, board, whiteTurn, pinnedPiecesAndPromotingPawns, ALL_EMPTY_SQUARES, ENEMY_PIECES);

            addPinnedPiecesMoves(moves, board, whiteTurn, pinnedPieces, myKing);
        }
    }
    
    private static void addPinnedPiecesMoves(List<Move> moves, Chessboard board, boolean whiteTurn,
                                             long pinnedPieces, long squareWeArePinnedTo){
        List<Long> allPinnedPieces = getAllPieces(pinnedPieces, 0);

        long pawns, knights, bishops, rooks, queens;
        if (whiteTurn){
            pawns = board.getWhitePawns();
            knights = board.getWhiteKnights();
            bishops = board.getWhiteBishops();
            rooks = board.getWhiteRooks();
            queens = board.getWhiteQueen();
        }
        else {
            pawns = board.getBlackPawns();
            knights = board.getBlackKnights();
            bishops = board.getBlackBishops();
            rooks = board.getBlackRooks();
            queens = board.getBlackQueen();
        }

        long FRIENLDY_PIECES = (whiteTurn) ? board.whitePieces() : board.blackPieces();
        long ENEMY_PIECES = (whiteTurn) ? board.blackPieces() : board.whitePieces();

        for (long pinnedPiece : allPinnedPieces){
            long infiniteRay = extractInfiniteRayFromTwoPieces(board, squareWeArePinnedTo, pinnedPiece);
            long pushMask = infiniteRay & ~(board.blackPieces() | board.whitePieces());
            long captureMask = infiniteRay & ENEMY_PIECES;

            if ((pinnedPiece & knights) != 0) {
                // knights cannot move cardinally or diagonally, and so cannot move while pinned
                continue;
            }
            if ((pinnedPiece & pawns) != 0) {

                long PENULTIMATE_RANK = whiteTurn ? BitboardResources.RANK_SEVEN : BitboardResources.RANK_TWO;
                long allButPinnedFriends = FRIENLDY_PIECES & ~pinnedPiece;

                if ((pinnedPiece & PENULTIMATE_RANK) == 0) {

                    long singlePawnAllPushes = singlePawnPushes(board, pinnedPiece, whiteTurn, pushMask);
                    addMovesFromAttackBoardLong(moves, singlePawnAllPushes, pinnedPiece);

                    long singlePawnAllCaptures = singlePawnCaptures(pinnedPiece, whiteTurn, captureMask);
                    addMovesFromAttackBoardLong(moves, singlePawnAllCaptures, pinnedPiece);

                    // a pinned pawn may still EP
                    addEnPassantMoves(moves, board, whiteTurn, allButPinnedFriends, pushMask, captureMask);
                }
                else {
                    // a pinned pawn may still promote, through a capture of the pinner
                    addPromotionMoves(moves, board, whiteTurn, allButPinnedFriends, pushMask, captureMask);
                }
                continue;
            }
            if ((pinnedPiece & bishops) != 0) {
                long pinnedBishopAllMoves = singleBishopTable(board, whiteTurn, pinnedPiece, UNIVERSE);
                addMovesFromAttackBoardLong(moves, pinnedBishopAllMoves & pushMask, pinnedPiece);
                addMovesFromAttackBoardLong(moves, pinnedBishopAllMoves & captureMask, pinnedPiece);
                continue;
            }
            if ((pinnedPiece & rooks) != 0) {
                long singleRookAllMoves = singleRookTable(board, whiteTurn, pinnedPiece, UNIVERSE);
                addMovesFromAttackBoardLong(moves, singleRookAllMoves & pushMask, pinnedPiece);
                addMovesFromAttackBoardLong(moves, singleRookAllMoves & captureMask, pinnedPiece);
                continue;
            }
            if ((pinnedPiece & queens) != 0) {
                long singleQueenAllMoves = singleQueenTable(board, whiteTurn, pinnedPiece, UNIVERSE);
                addMovesFromAttackBoardLong(moves, singleQueenAllMoves & pushMask, pinnedPiece);
                addMovesFromAttackBoardLong(moves, singleQueenAllMoves & captureMask, pinnedPiece);
            }
        }
    }


}
