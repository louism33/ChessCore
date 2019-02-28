package com.github.louism33.chesscore;

import org.junit.Assert;

import static com.github.louism33.chesscore.BitOperations.*;
import static com.github.louism33.chesscore.BoardConstants.*;
import static com.github.louism33.chesscore.CheckHelper.numberOfPiecesThatLegalThreatenSquare;
import static com.github.louism33.chesscore.MoveAdder.addMovesFromAttackTableMasterBetter;
import static com.github.louism33.chesscore.MoveGeneratorCheck.addCheckEvasionMoves;
import static com.github.louism33.chesscore.MoveGeneratorPseudo.addAllMovesWithoutKing;
import static com.github.louism33.chesscore.MoveGeneratorRegular.addKingLegalMovesOnly;
import static com.github.louism33.chesscore.MoveGeneratorSpecial.*;
import static com.github.louism33.chesscore.PieceMove.*;
import static com.github.louism33.chesscore.PinnedManager.whichPiecesArePinned;

class MoveGeneratorMaster {

    static void generateLegalMoves(Chessboard board, long pinnedPieces, boolean inCheckRecorder, int turn, long[][] pieces, int castlingRights,
                                   int[] pieceSquareTable, boolean hasPreviousMove, long peek, int[] moves) {
        Assert.assertNotNull(moves);

        long myPawns, myKnights, myBishops, myRooks, myQueens, myKing;
        long enemyPawns, enemyKnights, enemyBishops, enemyRooks, enemyQueens, enemyKing;
        long friends, enemies;

        myPawns = pieces[turn][PAWN];
        myKnights = pieces[turn][KNIGHT];
        myBishops = pieces[turn][BISHOP];
        myRooks = pieces[turn][ROOK];
        myQueens = pieces[turn][QUEEN];
        myKing = pieces[turn][KING];

        enemyPawns = pieces[1 - turn][PAWN];
        enemyKnights = pieces[1 - turn][KNIGHT];
        enemyBishops = pieces[1 - turn][BISHOP];
        enemyRooks = pieces[1 - turn][ROOK];
        enemyQueens = pieces[1 - turn][QUEEN];
        enemyKing = pieces[1 - turn][KING];

        friends = board.getPieces(turn);
        enemies = board.getPieces(1 - turn);

        long allPieces = friends | enemies;


        int numberOfCheckers = numberOfPiecesThatLegalThreatenSquare(turn == WHITE, myKing,
                enemyPawns, enemyKnights, enemyBishops, enemyRooks, enemyQueens, enemyKing,
                allPieces);

        if (numberOfCheckers > 1){
            board.inCheckRecorder = true;

            addKingLegalMovesOnly(moves, turn, pieces, pieceSquareTable,
                    myKing,
                    enemyPawns, enemyKnights, enemyBishops, enemyRooks, enemyQueens, enemyKing,
                    enemies, allPieces);
            return;
        }

        Assert.assertEquals(allPieces, board.allPieces());


        long currentPinnedPieces = whichPiecesArePinned(myKing,
                enemyBishops, enemyRooks, enemyQueens,
                friends, allPieces);

        board.pinnedPieces = currentPinnedPieces;

        if (numberOfCheckers == 1){
            board.inCheckRecorder = true;

            addCheckEvasionMoves(moves, board, turn == WHITE, currentPinnedPieces,
                    myPawns, myKnights, myBishops, myRooks, myQueens, myKing,
                    enemyPawns, enemyKnights, enemyBishops, enemyRooks, enemyQueens, enemyKing,
                    enemies, friends, allPieces);

            return;
        }

        board.inCheckRecorder = false;

        Assert.assertEquals(allPieces, board.allPieces());


        addNotInCheckMoves(moves, turn, pieces, castlingRights, pieceSquareTable, 
                hasPreviousMove, peek, currentPinnedPieces,
                myPawns, myKnights, myBishops, myRooks, myQueens, myKing,
                enemyPawns, enemyKnights, enemyBishops, enemyRooks, enemyQueens, enemyKing,
                enemies, friends, allPieces);

    }

    private static void addNotInCheckMoves(int[] moves, int turn, long[][] pieces, int castlingRights,
                                           int[] pieceSquareTable, boolean hasPreviousMove, long peek, long pinnedPieces,
                                           long myPawns, long myKnights, long myBishops, long myRooks, long myQueens, long myKing,
                                           long enemyPawns, long enemyKnights, long enemyBishops, long enemyRooks, long enemyQueens, long enemyKing,
                                           long enemies, long friends, long allPieces){

        long emptySquares = ~allPieces;

        long promotablePawns = myPawns & PENULTIMATE_RANKS[turn];
        long pinnedPiecesAndPromotingPawns = pinnedPieces | promotablePawns;

        addCastlingMoves(moves, turn, castlingRights,
                enemyPawns, enemyKnights, enemyBishops, enemyRooks, enemyQueens, enemyKing,
                allPieces);

        addKingLegalMovesOnly(moves, turn, pieces, pieceSquareTable,
                myKing,
                enemyPawns, enemyKnights, enemyBishops, enemyRooks, enemyQueens, enemyKing,
                enemies, allPieces);

        if (pinnedPieces == 0){
            addPromotionMoves
                    (moves, turn, pieceSquareTable, 0, emptySquares, enemies,
                            myPawns,
                            enemies, allPieces);

            addAllMovesWithoutKing
                    (moves, pieces, turn, pieceSquareTable, promotablePawns, emptySquares, enemies,
                            myKnights, myBishops, myRooks, myQueens,
                            allPieces);

            if (hasPreviousMove) {
                addEnPassantMoves
                        (moves, peek, turn, promotablePawns, emptySquares, enemies,
                                myPawns, myKing,
                                enemyPawns, enemyKnights, enemyBishops, enemyRooks, enemyQueens, enemyKing, allPieces
                        );
            }
        }
        else {
            addPromotionMoves
                    (moves, turn, pieceSquareTable, pinnedPieces, emptySquares, enemies,
                            myPawns,
                            enemies, allPieces);

            addAllMovesWithoutKing
                    (moves, pieces, turn, pieceSquareTable, pinnedPiecesAndPromotingPawns, ~allPieces, enemies,
                            myKnights, myBishops, myRooks, myQueens,
                            allPieces);

            if (hasPreviousMove) {
                addEnPassantMoves
                        (moves, peek, turn, pinnedPiecesAndPromotingPawns, emptySquares, enemies,
                                myPawns, myKing,
                                enemyPawns, enemyKnights, enemyBishops, enemyRooks, enemyQueens, enemyKing, allPieces
                        );
            }

            addPinnedPiecesMoves(moves, turn, pieceSquareTable,
                    hasPreviousMove, peek, pinnedPieces, myKing,
                    myPawns, myKnights, myBishops, myRooks, myQueens, myKing,
                    enemyPawns, enemyKnights, enemyBishops, enemyRooks, enemyQueens, enemyKing,
                    enemies, friends, allPieces);
        }
    }

    private static void addPinnedPiecesMoves(int[] moves, int turn,
                                             int[] pieceSquareTable, boolean hasPreviousMove, long peek,
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
                long allButPinnedFriends = friends & ~pinnedPiece;

                if ((pinnedPiece & PENULTIMATE_RANKS[turn]) == 0) {

                    addMovesFromAttackTableMasterBetter(moves,
                            singlePawnPushes(pinnedPiece, turn, pushMask, allPieces)
                                    | singlePawnCaptures(pinnedPiece, turn, pinningPiece),
                            pinnedPieceIndex, PIECE[turn][PAWN], pieceSquareTable);

                    // a pinned pawn may still EP
                    if (hasPreviousMove) {
                        addEnPassantMoves(moves, peek, turn, allButPinnedFriends, pushMask, pinningPiece,
                                myPawns, myKing,
                                enemyPawns, enemyKnights, enemyBishops, enemyRooks, enemyQueens, enemyKing, allPieces
                        );
                    }
                } else {
                    // a pinned pawn may still promote, through a capture of the pinner
                    addPromotionMoves(moves, turn, pieceSquareTable, allButPinnedFriends, pushMask, pinningPiece,
                            myPawns,
                            enemies, allPieces);
                }
                pinnedPieces &= pinnedPieces - 1;
                continue;
            }
            if ((pinnedPiece & myBishops) != 0) {
                addMovesFromAttackTableMasterBetter(moves,
                        singleBishopTable(allPieces, pinnedPiece, UNIVERSE) & mask,
                        pinnedPieceIndex, PIECE[turn][BISHOP], pieceSquareTable);
                pinnedPieces &= pinnedPieces - 1;
                continue;
            }
            if ((pinnedPiece & myRooks) != 0) {
                addMovesFromAttackTableMasterBetter(moves,
                        singleRookTable(allPieces, pinnedPiece, UNIVERSE) & mask,
                        pinnedPieceIndex, PIECE[turn][ROOK], pieceSquareTable);
                pinnedPieces &= pinnedPieces - 1;
                continue;
            }
            if ((pinnedPiece & myQueens) != 0) {
                addMovesFromAttackTableMasterBetter(moves,
                        (singleQueenTable(allPieces, pinnedPiece, UNIVERSE) & mask),
                        pinnedPieceIndex, PIECE[turn][QUEEN], pieceSquareTable);
            }

            pinnedPieces &= pinnedPieces - 1;
        }
    }

}
