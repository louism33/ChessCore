package chessprogram.god;

import java.util.ArrayList;
import java.util.List;

import static chessprogram.god.BitOperations.UNIVERSE;
import static chessprogram.god.BitOperations.getAllPieces;
import static chessprogram.god.CheckHelper.numberOfPiecesThatLegalThreatenSquare;
import static chessprogram.god.MoveGenerationUtilities.addMovesFromAttackTableMaster;
import static chessprogram.god.MoveGeneratorCheck.addCheckEvasionMoves;
import static chessprogram.god.MoveGeneratorEnPassant.addEnPassantMoves;
import static chessprogram.god.MoveGeneratorKingLegal.addKingLegalMovesOnly;
import static chessprogram.god.MoveGeneratorPromotionIntMove.addPromotionMoves;
import static chessprogram.god.MoveGeneratorPseudoIntMove.addAllMovesWithoutKing;
import static chessprogram.god.PieceMovePawns.singlePawnCaptures;
import static chessprogram.god.PieceMovePawns.singlePawnPushes;
import static chessprogram.god.PieceMoveSliding.*;
import static chessprogram.god.PinnedManager.whichPiecesArePinned;

class MoveGeneratorMaster {

    static List<Integer> generateLegalMoves(Chessboard board, boolean white) {
        List<Integer> moves = new ArrayList<>();

        long myPawns, myKnights, myBishops, myRooks, myQueens, myKing;
        long enemyPawns, enemyKnights, enemyBishops, enemyRooks, enemyQueens, enemyKing;
        long friends, enemies;

        if (white){
            myPawns = board.getWhitePawns();
            myKnights = board.getWhiteKnights();
            myBishops = board.getWhiteBishops();
            myRooks = board.getWhiteRooks();
            myQueens = board.getWhiteQueen();
            myKing = board.getWhiteKing();

            enemyPawns = board.getBlackPawns();
            enemyKnights = board.getBlackKnights();
            enemyBishops = board.getBlackBishops();
            enemyRooks = board.getBlackRooks();
            enemyQueens = board.getBlackQueen();
            enemyKing = board.getBlackKing();

            friends = board.whitePieces();
            enemies = board.blackPieces();
        }
        else {
            myPawns = board.getBlackPawns();
            myKnights = board.getBlackKnights();
            myBishops = board.getBlackBishops();
            myRooks = board.getBlackRooks();
            myQueens = board.getBlackQueen();
            myKing = board.getBlackKing();

            enemyPawns = board.getWhitePawns();
            enemyKnights = board.getWhiteKnights();
            enemyBishops = board.getWhiteBishops();
            enemyRooks = board.getWhiteRooks();
            enemyQueens = board.getWhiteQueen();
            enemyKing = board.getWhiteKing();

            friends = board.blackPieces();
            enemies = board.whitePieces();
        }
        
        long allPieces = friends | enemies;

        int numberOfCheckers = numberOfPiecesThatLegalThreatenSquare(board, white, myKing,
                myPawns, myKnights, myBishops, myRooks, myQueens, myKing,
                enemyPawns, enemyKnights, enemyBishops, enemyRooks, enemyQueens, enemyKing,
                enemies);

        if (numberOfCheckers > 1){
            addKingLegalMovesOnly(moves, board, white,
                    myPawns, myKnights, myBishops, myRooks, myQueens, myKing,
                    enemyPawns, enemyKnights, enemyBishops, enemyRooks, enemyQueens, enemyKing,
                    enemies);
            return moves;
        }

        long pinnedPieces = whichPiecesArePinned(board, white, myKing);
        
        if (numberOfCheckers == 1){
            addCheckEvasionMoves(moves, board, white, pinnedPieces, 
                    myPawns, myKnights, myBishops, myRooks, myQueens, myKing,
                    enemyPawns, enemyKnights, enemyBishops, enemyRooks, enemyQueens, enemyKing,
                    enemies);
           
            return moves;
        }

        addNotInCheckMoves(moves, board, white, pinnedPieces,
                myPawns, myKnights, myBishops, myRooks, myQueens, myKing,
                enemyPawns, enemyKnights, enemyBishops, enemyRooks, enemyQueens, enemyKing,
                enemies);
        
        return moves;
    }

    private static void addNotInCheckMoves(List<Integer> moves, Chessboard board, boolean whiteTurn, long pinnedPieces,
                                           long myPawns, long myKnights, long myBishops, long myRooks, long myQueens, long myKing,
                                           long enemyPawns, long enemyKnights, long enemyBishops, long enemyRooks, long enemyQueens, long enemyKing,
                                           long enemies){
        
        long ENEMY_PIECES = (whiteTurn) ? board.blackPieces() : board.whitePieces();
        long ALL_EMPTY_SQUARES = ~board.allPieces();

        long PENULTIMATE_RANK = whiteTurn ? BitboardResources.RANK_SEVEN : BitboardResources.RANK_TWO;
        long promotablePawns = myPawns & PENULTIMATE_RANK;
        long pinnedPiecesAndPromotingPawns = pinnedPieces | promotablePawns;

        MoveGeneratorCastling.addCastlingMoves(moves, board, whiteTurn,
                myPawns, myKnights, myBishops, myRooks, myQueens, myKing,
                enemyPawns, enemyKnights, enemyBishops, enemyRooks, enemyQueens, enemyKing,
                enemies);

        addKingLegalMovesOnly(moves, board, whiteTurn,
                myPawns, myKnights, myBishops, myRooks, myQueens, myKing,
                enemyPawns, enemyKnights, enemyBishops, enemyRooks, enemyQueens, enemyKing,
                enemies);

        if (pinnedPieces == 0){
            addPromotionMoves
                    (moves, board, whiteTurn, 0, ALL_EMPTY_SQUARES, ENEMY_PIECES,
                            myPawns, myKnights, myBishops, myRooks, myQueens, myKing,
                            enemyPawns, enemyKnights, enemyBishops, enemyRooks, enemyQueens, enemyKing,
                            enemies);

            addAllMovesWithoutKing
                    (moves, board, whiteTurn, promotablePawns, ALL_EMPTY_SQUARES, ENEMY_PIECES,
                            myPawns, myKnights, myBishops, myRooks, myQueens, myKing,
                            enemyPawns, enemyKnights, enemyBishops, enemyRooks, enemyQueens, enemyKing,
                            enemies);

            addEnPassantMoves
                    (moves, board, whiteTurn, promotablePawns, ALL_EMPTY_SQUARES, ENEMY_PIECES,
                            myPawns, myKnights, myBishops, myRooks, myQueens, myKing,
                            enemyPawns, enemyKnights, enemyBishops, enemyRooks, enemyQueens, enemyKing,
                            enemies);
        }
        else {
            addPromotionMoves
                    (moves, board, whiteTurn, pinnedPieces, ALL_EMPTY_SQUARES, ENEMY_PIECES,
                            myPawns, myKnights, myBishops, myRooks, myQueens, myKing,
                            enemyPawns, enemyKnights, enemyBishops, enemyRooks, enemyQueens, enemyKing,
                            enemies);

            addAllMovesWithoutKing
                    (moves, board, whiteTurn, pinnedPiecesAndPromotingPawns, ~board.allPieces(), ENEMY_PIECES,
                            myPawns, myKnights, myBishops, myRooks, myQueens, myKing,
                            enemyPawns, enemyKnights, enemyBishops, enemyRooks, enemyQueens, enemyKing,
                            enemies);

            addEnPassantMoves
                    (moves, board, whiteTurn, pinnedPiecesAndPromotingPawns, ALL_EMPTY_SQUARES, ENEMY_PIECES,
                            myPawns, myKnights, myBishops, myRooks, myQueens, myKing,
                            enemyPawns, enemyKnights, enemyBishops, enemyRooks, enemyQueens, enemyKing,
                            enemies);

            addPinnedPiecesMoves(moves, board, whiteTurn, pinnedPieces, myKing,
                    myPawns, myKnights, myBishops, myRooks, myQueens, myKing,
                    enemyPawns, enemyKnights, enemyBishops, enemyRooks, enemyQueens, enemyKing,
                    enemies);
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

    private static void addPinnedPiecesMoves(List<Integer> moves, Chessboard board, boolean whiteTurn,
                                             long pinnedPieces, long squareWeArePinnedTo,
                                             long myPawns, long myKnights, long myBishops, long myRooks, long myQueens, long myKing,
                                             long enemyPawns, long enemyKnights, long enemyBishops, long enemyRooks, long enemyQueens, long enemyKing,
                                             long enemies){
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

        //todo
        for (long pinnedPiece : allPinnedPieces){
            long xray = xrayQueenAttacks(allPieces, pinnedPiece, squareWeArePinnedTo);
            long pinningPiece = xray & ENEMY_PIECES;
            long infiniteRay = Magic.extractRayFromTwoPiecesBitboardInclusive(squareWeArePinnedTo, pinningPiece);
            long pushMask = infiniteRay ^ (pinningPiece | squareWeArePinnedTo);

            final int pinnedPieceIndex = BitOperations.getIndexOfFirstPiece(pinnedPiece);
            final long mask = (pushMask | pinningPiece);

            if ((pinnedPiece & knights) != 0) {
                // knights cannot move cardinally or diagonally, and so cannot move while pinned
                continue;
            }
            if ((pinnedPiece & pawns) != 0) {
                long PENULTIMATE_RANK = whiteTurn ? BitboardResources.RANK_SEVEN : BitboardResources.RANK_TWO;
                long allButPinnedFriends = FRIENLDY_PIECES & ~pinnedPiece;

                if ((pinnedPiece & PENULTIMATE_RANK) == 0) {
                    addMovesFromAttackTableMaster(moves,
                            singlePawnPushes(board, pinnedPiece, whiteTurn, pushMask)
                                    | singlePawnCaptures(pinnedPiece, whiteTurn, pinningPiece),
                            pinnedPieceIndex, board);

                    // a pinned pawn may still EP
                    addEnPassantMoves(moves, board, whiteTurn, allButPinnedFriends, pushMask, pinningPiece,
                            myPawns, myKnights, myBishops, myRooks, myQueens, myKing,
                            enemyPawns, enemyKnights, enemyBishops, enemyRooks, enemyQueens, enemyKing,
                            enemies);
                }
                else {
                    // a pinned pawn may still promote, through a capture of the pinner
                    addPromotionMoves(moves, board, whiteTurn, allButPinnedFriends, pushMask, pinningPiece,
                            myPawns, myKnights, myBishops, myRooks, myQueens, myKing,
                            enemyPawns, enemyKnights, enemyBishops, enemyRooks, enemyQueens, enemyKing,
                            enemies);
                }
                continue;
            }
            if ((pinnedPiece & bishops) != 0) {
                addMovesFromAttackTableMaster(moves,
                        singleBishopTable(allPieces, whiteTurn, pinnedPiece, UNIVERSE) & mask,
                        pinnedPieceIndex, board);
                continue;
            }
            if ((pinnedPiece & rooks) != 0) {
                addMovesFromAttackTableMaster(moves,
                        singleRookTable(allPieces, whiteTurn, pinnedPiece, UNIVERSE) & mask,
                        pinnedPieceIndex, board);
                continue;
            }
            if ((pinnedPiece & queens) != 0) {
                addMovesFromAttackTableMaster(moves,
                        (singleQueenTable(allPieces, whiteTurn, pinnedPiece, UNIVERSE) & mask),
                        pinnedPieceIndex, board);
            }
        }
    }


}
