package chessprogram.god;

import java.util.ArrayList;
import java.util.List;

import static chessprogram.god.BitOperations.UNIVERSE;
import static chessprogram.god.BitOperations.getAllPieces;
import static chessprogram.god.CheckHelperIntMove.numberOfPiecesThatLegalThreatenSquare;
import static chessprogram.god.MoveGenerationUtilitiesIntMove.addMovesFromAttackBoardLong;
import static chessprogram.god.MoveGeneratorCheckIntMove.addCheckEvasionMoves;
import static chessprogram.god.MoveGeneratorEnPassantIntMove.addEnPassantMoves;
import static chessprogram.god.MoveGeneratorKingLegalIntMove.addKingLegalMovesOnly;
import static chessprogram.god.MoveGeneratorPromotionIntMove.addPromotionMoves;
import static chessprogram.god.MoveGeneratorPseudoIntMove.addAllMovesWithoutKing;
import static chessprogram.god.PieceMovePawnsIntMove.singlePawnCaptures;
import static chessprogram.god.PieceMovePawnsIntMove.singlePawnPushes;
import static chessprogram.god.PieceMoveSlidingIntMove.singleBishopTable;
import static chessprogram.god.PieceMoveSlidingIntMove.singleQueenTable;
import static chessprogram.god.PieceMoveSlidingIntMove.singleRookTable;
import static chessprogram.god.PinnedManagerIntMove.whichPiecesArePinned;

class MoveGeneratorMasterIntMove {

    static List<Integer> generateLegalMoves(ChessboardIntMove board, boolean white) {
        List<Integer> moves = new ArrayList<>();
        long myKing = (white) ? board.getWhiteKing() : board.getBlackKing();
        int numberOfCheckers = numberOfPiecesThatLegalThreatenSquare(board, white, myKing);

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


    private static void addNotInCheckMoves(List<Integer> moves, ChessboardIntMove board, boolean whiteTurn){
        long ENEMY_PIECES = (whiteTurn) ? board.blackPieces() : board.whitePieces();
        long ALL_EMPTY_SQUARES = ~board.allPieces();
        long myKing = (whiteTurn) ? board.getWhiteKing() : board.getBlackKing();
        
        long pinnedPieces = whichPiecesArePinned(board, whiteTurn, myKing);
        
        long PENULTIMATE_RANK = whiteTurn ? BitboardResources.RANK_SEVEN : BitboardResources.RANK_TWO;
        long myPawns = whiteTurn ? board.getWhitePawns() : board.getBlackPawns();
        long promotablePawns = myPawns & PENULTIMATE_RANK;
        long pinnedPiecesAndPromotingPawns = pinnedPieces | promotablePawns;

        MoveGeneratorCastlingIntMoves.addCastlingMoves(moves, board, whiteTurn);

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
    
            /*
        while (pawns != 0){
            final long pawn = BitOperations.getFirstPiece(pawns);
            if ((pawn & ignoreThesePieces) == 0) {
                ans |= singlePawnCaptures(pawn, white, legalCaptures);
            }
            pawns &= pawns - 1;
        }
         */
    
    private static void addPinnedPiecesMoves(List<Integer> moves, ChessboardIntMove board, boolean whiteTurn,
                                             long pinnedPieces, long squareWeArePinnedTo){
        List<Long> allPinnedPieces = getAllPieces(pinnedPieces, 0);

        long pawns, knights, bishops, rooks, queens, allPieces = board.allPieces();
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
            long xray = PieceMoveSliding.xrayQueenAttacks(allPieces, pinnedPiece, squareWeArePinnedTo);
            long pinningPiece = xray & ENEMY_PIECES;
            long infiniteRay = Magic.extractRayFromTwoPiecesBitboardInclusive(squareWeArePinnedTo, pinningPiece);
            long pushMask = infiniteRay ^ (pinningPiece | squareWeArePinnedTo);

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

                    long singlePawnAllCaptures = singlePawnCaptures(pinnedPiece, whiteTurn, pinningPiece);
                    addMovesFromAttackBoardLong(moves, singlePawnAllCaptures, pinnedPiece);

                    // a pinned pawn may still EP
                    addEnPassantMoves(moves, board, whiteTurn, allButPinnedFriends, pushMask, pinningPiece);
                }
                else {
                    // a pinned pawn may still promote, through a capture of the pinner
                    addPromotionMoves(moves, board, whiteTurn, allButPinnedFriends, pushMask, pinningPiece);
                }
                continue;
            }
            if ((pinnedPiece & bishops) != 0) {
                long pinnedBishopAllMoves = singleBishopTable(allPieces, whiteTurn, pinnedPiece, UNIVERSE);
                addMovesFromAttackBoardLong(moves, pinnedBishopAllMoves & pushMask, pinnedPiece);
                addMovesFromAttackBoardLong(moves, pinnedBishopAllMoves & pinningPiece, pinnedPiece);
                continue;
            }
            if ((pinnedPiece & rooks) != 0) {
                long singleRookAllMoves = singleRookTable(allPieces, whiteTurn, pinnedPiece, UNIVERSE);
                addMovesFromAttackBoardLong(moves, singleRookAllMoves & pushMask, pinnedPiece);
                addMovesFromAttackBoardLong(moves, singleRookAllMoves & pinningPiece, pinnedPiece);
                continue;
            }
            if ((pinnedPiece & queens) != 0) {
                long singleQueenAllMoves = singleQueenTable(allPieces, whiteTurn, pinnedPiece, UNIVERSE);
                addMovesFromAttackBoardLong(moves, singleQueenAllMoves & pushMask, pinnedPiece);
                addMovesFromAttackBoardLong(moves, singleQueenAllMoves & pinningPiece, pinnedPiece);
            }
        }
    }


}
