package com.github.louism33.chesscore;

import static com.github.louism33.chesscore.BitOperations.*;
import static com.github.louism33.chesscore.BitboardResources.UNIVERSE;
import static com.github.louism33.chesscore.CheckHelper.numberOfPiecesThatLegalThreatenSquare;
import static com.github.louism33.chesscore.MoveAdder.addMovesFromAttackTableMaster;
import static com.github.louism33.chesscore.MoveGeneratorCheck.addCheckEvasionMoves;
import static com.github.louism33.chesscore.MoveGeneratorPseudo.addAllMovesWithoutKing;
import static com.github.louism33.chesscore.MoveGeneratorRegular.addKingLegalMovesOnly;
import static com.github.louism33.chesscore.MoveGeneratorSpecial.*;
import static com.github.louism33.chesscore.PieceMove.*;
import static com.github.louism33.chesscore.PinnedManager.whichPiecesArePinned;

class MoveGeneratorMaster {

    static int[] generateLegalMoves(Chessboard board, boolean white) {
        int[] moves = new int[128];

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
                enemyPawns, enemyKnights, enemyBishops, enemyRooks, enemyQueens, enemyKing,
                enemies, friends, allPieces);

        if (numberOfCheckers > 1){
            addKingLegalMovesOnly(moves, board, white,
                    myPawns, myKnights, myBishops, myRooks, myQueens, myKing,
                    enemyPawns, enemyKnights, enemyBishops, enemyRooks, enemyQueens, enemyKing,
                    enemies, friends, allPieces);
            return moves;
        }

        long pinnedPieces = whichPiecesArePinned(board, white, myKing,
                myPawns, myKnights, myBishops, myRooks, myQueens, myKing,
                enemyPawns, enemyKnights, enemyBishops, enemyRooks, enemyQueens, enemyKing,
                enemies, friends, allPieces);

        if (numberOfCheckers == 1){
            addCheckEvasionMoves(moves, board, white, pinnedPieces,
                    myPawns, myKnights, myBishops, myRooks, myQueens, myKing,
                    enemyPawns, enemyKnights, enemyBishops, enemyRooks, enemyQueens, enemyKing,
                    enemies, friends, allPieces);

            return moves;
        }

        addNotInCheckMoves(moves, board, white, pinnedPieces,
                myPawns, myKnights, myBishops, myRooks, myQueens, myKing,
                enemyPawns, enemyKnights, enemyBishops, enemyRooks, enemyQueens, enemyKing,
                enemies, friends, allPieces);

        return moves;
    }

    private static void addNotInCheckMoves(int[] moves, Chessboard board, boolean whiteTurn, long pinnedPieces,
                                           long myPawns, long myKnights, long myBishops, long myRooks, long myQueens, long myKing,
                                           long enemyPawns, long enemyKnights, long enemyBishops, long enemyRooks, long enemyQueens, long enemyKing,
                                           long enemies, long friends, long allPieces){

        long emptySquares = ~allPieces;

        long promotablePawns = myPawns & (whiteTurn ? BitboardResources.RANK_SEVEN : BitboardResources.RANK_TWO);
        long pinnedPiecesAndPromotingPawns = pinnedPieces | promotablePawns;

        addCastlingMoves(moves, board, whiteTurn,
                myPawns, myKnights, myBishops, myRooks, myQueens, myKing,
                enemyPawns, enemyKnights, enemyBishops, enemyRooks, enemyQueens, enemyKing,
                enemies, friends, allPieces);

        addKingLegalMovesOnly(moves, board, whiteTurn,
                myPawns, myKnights, myBishops, myRooks, myQueens, myKing,
                enemyPawns, enemyKnights, enemyBishops, enemyRooks, enemyQueens, enemyKing,
                enemies, friends, allPieces);

        if (pinnedPieces == 0){
            addPromotionMoves
                    (moves, board, whiteTurn, 0, emptySquares, enemies,
                            myPawns, myKnights, myBishops, myRooks, myQueens, myKing,
                            enemyPawns, enemyKnights, enemyBishops, enemyRooks, enemyQueens, enemyKing,
                            enemies, friends, allPieces);

            addAllMovesWithoutKing
                    (moves, board, whiteTurn, promotablePawns, emptySquares, enemies,
                            myPawns, myKnights, myBishops, myRooks, myQueens, myKing,
                            enemyPawns, enemyKnights, enemyBishops, enemyRooks, enemyQueens, enemyKing,
                            enemies, friends, allPieces);

            addEnPassantMoves
                    (moves, board, whiteTurn, promotablePawns, emptySquares, enemies,
                            myPawns, myKnights, myBishops, myRooks, myQueens, myKing,
                            enemyPawns, enemyKnights, enemyBishops, enemyRooks, enemyQueens, enemyKing,
                            enemies, friends, allPieces);
        }
        else {
            addPromotionMoves
                    (moves, board, whiteTurn, pinnedPieces, emptySquares, enemies,
                            myPawns, myKnights, myBishops, myRooks, myQueens, myKing,
                            enemyPawns, enemyKnights, enemyBishops, enemyRooks, enemyQueens, enemyKing,
                            enemies, friends, allPieces);

            addAllMovesWithoutKing
                    (moves, board, whiteTurn, pinnedPiecesAndPromotingPawns, ~board.allPieces(), enemies,
                            myPawns, myKnights, myBishops, myRooks, myQueens, myKing,
                            enemyPawns, enemyKnights, enemyBishops, enemyRooks, enemyQueens, enemyKing,
                            enemies, friends, allPieces);

            addEnPassantMoves
                    (moves, board, whiteTurn, pinnedPiecesAndPromotingPawns, emptySquares, enemies,
                            myPawns, myKnights, myBishops, myRooks, myQueens, myKing,
                            enemyPawns, enemyKnights, enemyBishops, enemyRooks, enemyQueens, enemyKing,
                            enemies, friends, allPieces);

            addPinnedPiecesMoves(moves, board, whiteTurn, pinnedPieces, myKing,
                    myPawns, myKnights, myBishops, myRooks, myQueens, myKing,
                    enemyPawns, enemyKnights, enemyBishops, enemyRooks, enemyQueens, enemyKing,
                    enemies, friends, allPieces);
        }
    }

    private static void addPinnedPiecesMoves(int[] moves, Chessboard board, boolean whiteTurn,
                                             long pinnedPieces, long squareWeArePinnedTo,
                                             long myPawns, long myKnights, long myBishops, long myRooks, long myQueens, long myKing,
                                             long enemyPawns, long enemyKnights, long enemyBishops, long enemyRooks, long enemyQueens, long enemyKing,
                                             long enemies, long friends, long allPieces){
        while (pinnedPieces != 0){
            long pinnedPiece = getFirstPiece(pinnedPieces);
            long pinningPiece = xrayQueenAttacks(allPieces, pinnedPiece, squareWeArePinnedTo) & enemies;
            long pushMask = extractRayFromTwoPiecesBitboardInclusive(squareWeArePinnedTo, pinningPiece)
                    ^ (pinningPiece | squareWeArePinnedTo);

            final int pinnedPieceIndex = getIndexOfFirstPiece(pinnedPiece);
            final long mask = (pushMask | pinningPiece);

            if ((pinnedPiece & myKnights) != 0) {
                // knights cannot move cardinally or diagonally, and so cannot move while pinned
                pinnedPieces &= pinnedPieces - 1;
                continue;
            }
            if ((pinnedPiece & myPawns) != 0) {
                long PENULTIMATE_RANK = whiteTurn ? BitboardResources.RANK_SEVEN : BitboardResources.RANK_TWO;
                long allButPinnedFriends = friends & ~pinnedPiece;

                if ((pinnedPiece & PENULTIMATE_RANK) == 0) {
                    addMovesFromAttackTableMaster(moves,
                            singlePawnPushes(board, pinnedPiece, whiteTurn, pushMask, allPieces)
                                    | singlePawnCaptures(pinnedPiece, whiteTurn, pinningPiece),
                            pinnedPieceIndex, board);

                    // a pinned pawn may still EP
                    addEnPassantMoves(moves, board, whiteTurn, allButPinnedFriends, pushMask, pinningPiece,
                            myPawns, myKnights, myBishops, myRooks, myQueens, myKing,
                            enemyPawns, enemyKnights, enemyBishops, enemyRooks, enemyQueens, enemyKing,
                            enemies, friends, allPieces);
                } else {
                    // a pinned pawn may still promote, through a capture of the pinner
                    addPromotionMoves(moves, board, whiteTurn, allButPinnedFriends, pushMask, pinningPiece,
                            myPawns, myKnights, myBishops, myRooks, myQueens, myKing,
                            enemyPawns, enemyKnights, enemyBishops, enemyRooks, enemyQueens, enemyKing,
                            enemies, friends, allPieces);
                }
                pinnedPieces &= pinnedPieces - 1;
                continue;
            }
            if ((pinnedPiece & myBishops) != 0) {
                addMovesFromAttackTableMaster(moves,
                        singleBishopTable(allPieces, whiteTurn, pinnedPiece, UNIVERSE) & mask,
                        pinnedPieceIndex, board);
                pinnedPieces &= pinnedPieces - 1;
                continue;
            }
            if ((pinnedPiece & myRooks) != 0) {
                addMovesFromAttackTableMaster(moves,
                        singleRookTable(allPieces, whiteTurn, pinnedPiece, UNIVERSE) & mask,
                        pinnedPieceIndex, board);
                pinnedPieces &= pinnedPieces - 1;
                continue;
            }
            if ((pinnedPiece & myQueens) != 0) {
                addMovesFromAttackTableMaster(moves,
                        (singleQueenTable(allPieces, whiteTurn, pinnedPiece, UNIVERSE) & mask),
                        pinnedPieceIndex, board);
            }

            pinnedPieces &= pinnedPieces - 1;
        }
    }

}
