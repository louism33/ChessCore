package com.github.louism33.chesscore;

import org.junit.Assert;

import static com.github.louism33.chesscore.BitOperations.*;
import static com.github.louism33.chesscore.BoardConstants.*;
import static com.github.louism33.chesscore.CheckHelper.numberOfPiecesThatLegalThreatenSquare;
import static com.github.louism33.chesscore.MoveAdder.addMovesFromAttackTableMaster;
import static com.github.louism33.chesscore.MoveGeneratorCheck.addCheckEvasionMoves;
import static com.github.louism33.chesscore.MoveGeneratorPseudo.addAllMovesWithoutKing;
import static com.github.louism33.chesscore.MoveGeneratorRegular.addKingLegalMovesOnly;
import static com.github.louism33.chesscore.MoveGeneratorSpecial.*;
import static com.github.louism33.chesscore.PieceMove.*;
import static com.github.louism33.chesscore.PinnedManager.whichPiecesArePinned;

class MoveGeneratorMaster {

    static void generateLegalMoves(Chessboard board, int[] moves, int turn) {
        Assert.assertNotNull(moves);

        boolean white = turn == WHITE;
        
        long myPawns, myKnights, myBishops, myRooks, myQueens, myKing;
        long enemyPawns, enemyKnights, enemyBishops, enemyRooks, enemyQueens, enemyKing;
        long friends, enemies;

        if (white){
//            myPawns = board.getWhitePawns();
//            myKnights = board.getWhiteKnights();
//            myBishops = board.getWhiteBishops();
//            myRooks = board.getWhiteRooks();
//            myQueens = board.getWhiteQueen();
//            myKing = board.getWhiteKing();
//
//            enemyPawns = board.getBlackPawns();
//            enemyKnights = board.getBlackKnights();
//            enemyBishops = board.getBlackBishops();
//            enemyRooks = board.getBlackRooks();
//            enemyQueens = board.getBlackQueen();
//            enemyKing = board.getBlackKing();

            myPawns = board.pieces[WHITE][PAWN];
            myKnights = board.pieces[WHITE][KNIGHT];
            myBishops = board.pieces[WHITE][BISHOP];
            myRooks = board.pieces[WHITE][ROOK];
            myQueens = board.pieces[WHITE][QUEEN];
            myKing = board.pieces[WHITE][KING];

            enemyPawns = board.pieces[BLACK][PAWN];
            enemyKnights = board.pieces[BLACK][KNIGHT];
            enemyBishops = board.pieces[BLACK][BISHOP];
            enemyRooks = board.pieces[BLACK][ROOK];
            enemyQueens = board.pieces[BLACK][QUEEN];
            enemyKing = board.pieces[BLACK][KING];

            
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

            myPawns = board.pieces[BLACK][PAWN];
            myKnights = board.pieces[BLACK][KNIGHT];
            myBishops = board.pieces[BLACK][BISHOP];
            myRooks = board.pieces[BLACK][ROOK];
            myQueens = board.pieces[BLACK][QUEEN];
            myKing = board.pieces[BLACK][KING];

            enemyPawns = board.pieces[WHITE][PAWN];
            enemyKnights = board.pieces[WHITE][KNIGHT];
            enemyBishops = board.pieces[WHITE][BISHOP];
            enemyRooks = board.pieces[WHITE][ROOK];
            enemyQueens = board.pieces[WHITE][QUEEN];
            enemyKing = board.pieces[WHITE][KING];

            friends = board.blackPieces();
            enemies = board.whitePieces();
        }

        /**
         * myPawns = board.pieces[turn][PAWN];
         */

//        if (myRooks != board.pieces[turn][ROOK]) {
//            System.out.println(board);
//            System.out.println("turn");
//            Art.printLong(board.pieces[turn][ROOK]);
//            System.out.println("anti turn");
//            Art.printLong(board.pieces[1-turn][ROOK]);
//            System.out.println("correct: ");
//            Art.printLong(myRooks);
//
//            Assert.assertEquals(myRooks, board.pieces[turn][ROOK]);
//        }

//        Assert.assertEquals(myPawns, board.pieces[turn][PAWN]);
//        Assert.assertEquals(myKnights, board.pieces[turn][KNIGHT]);
//        Assert.assertEquals(myBishops, board.pieces[turn][BISHOP]);
//        Assert.assertEquals(myRooks, board.pieces[turn][ROOK]);
//        Assert.assertEquals(myQueens, board.pieces[turn][QUEEN]);
//        Assert.assertEquals(myKing, board.pieces[turn][KING]);
//
//        Assert.assertEquals(enemyPawns, board.pieces[1 - turn][PAWN]);
//        Assert.assertEquals(enemyKnights, board.pieces[1 - turn][KNIGHT]);
//        Assert.assertEquals(enemyBishops, board.pieces[1 - turn][BISHOP]);
//        Assert.assertEquals(enemyRooks, board.pieces[1 - turn][ROOK]);
//        Assert.assertEquals(enemyQueens, board.pieces[1 - turn][QUEEN]);
//        Assert.assertEquals(enemyKing, board.pieces[1 - turn][KING]);

        long allPieces = friends | enemies;

        int numberOfCheckers = numberOfPiecesThatLegalThreatenSquare(white, myKing,
                enemyPawns, enemyKnights, enemyBishops, enemyRooks, enemyQueens, enemyKing,
                allPieces);

        if (numberOfCheckers > 1){
            board.inCheckRecorder = true;
            
            addKingLegalMovesOnly(moves, board, white,
                    myBishops, myQueens, myKing,
                    enemyPawns, enemyKnights, enemyBishops, enemyRooks, enemyQueens, enemyKing,
                    enemies, friends);
            return;
        }

        long pinnedPieces = whichPiecesArePinned(myKing,
                enemyBishops, enemyRooks, enemyQueens,
                friends, allPieces);

        board.pinnedPieces = pinnedPieces;
        
        if (numberOfCheckers == 1){
            board.inCheckRecorder = true;

            addCheckEvasionMoves(moves, board, white, pinnedPieces,
                    myPawns, myKnights, myBishops, myRooks, myQueens, myKing,
                    enemyPawns, enemyKnights, enemyBishops, enemyRooks, enemyQueens, enemyKing,
                    enemies, friends, allPieces);

            return;
        }
        
        board.inCheckRecorder = false;

        addNotInCheckMoves(moves, board, turn, pinnedPieces,
                myPawns, myKnights, myBishops, myRooks, myQueens, myKing,
                enemyPawns, enemyKnights, enemyBishops, enemyRooks, enemyQueens, enemyKing,
                enemies, friends, allPieces);

    }

    private static void addNotInCheckMoves(int[] moves, Chessboard board, int turn, long pinnedPieces,
                                           long myPawns, long myKnights, long myBishops, long myRooks, long myQueens, long myKing,
                                           long enemyPawns, long enemyKnights, long enemyBishops, long enemyRooks, long enemyQueens, long enemyKing,
                                           long enemies, long friends, long allPieces){

        boolean whiteTurn = turn == WHITE;
        long emptySquares = ~allPieces;

        long promotablePawns = myPawns & (whiteTurn ? BoardConstants.RANK_SEVEN : BoardConstants.RANK_TWO);
        long pinnedPiecesAndPromotingPawns = pinnedPieces | promotablePawns;

        addCastlingMoves(moves, board, whiteTurn,
                enemyPawns, enemyKnights, enemyBishops, enemyRooks, enemyQueens, enemyKing,
                allPieces);

        addKingLegalMovesOnly(moves, board, whiteTurn,
                myBishops, myQueens, myKing,
                enemyPawns, enemyKnights, enemyBishops, enemyRooks, enemyQueens, enemyKing,
                enemies, friends);

        if (pinnedPieces == 0){
            addPromotionMoves
                    (moves, board, whiteTurn, 0, emptySquares, enemies,
                            myPawns,
                            enemies, allPieces);

            addAllMovesWithoutKing
                    (moves, board, whiteTurn, promotablePawns, emptySquares, enemies,
                            myKnights, myBishops, myRooks, myQueens,
                            allPieces);

            addEnPassantMoves
                    (moves, board, whiteTurn, promotablePawns, emptySquares, enemies,
                            myPawns, myKing,
                            enemyPawns, enemyKnights, enemyBishops, enemyRooks, enemyQueens, enemyKing
                    );
        }
        else {
            addPromotionMoves
                    (moves, board, whiteTurn, pinnedPieces, emptySquares, enemies,
                            myPawns,
                            enemies, allPieces);

            addAllMovesWithoutKing
                    (moves, board, whiteTurn, pinnedPiecesAndPromotingPawns, ~board.allPieces(), enemies,
                            myKnights, myBishops, myRooks, myQueens,
                            allPieces);

            addEnPassantMoves
                    (moves, board, whiteTurn, pinnedPiecesAndPromotingPawns, emptySquares, enemies,
                            myPawns, myKing,
                            enemyPawns, enemyKnights, enemyBishops, enemyRooks, enemyQueens, enemyKing
                    );

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
                long PENULTIMATE_RANK = whiteTurn ? BoardConstants.RANK_SEVEN : BoardConstants.RANK_TWO;
                long allButPinnedFriends = friends & ~pinnedPiece;

                if ((pinnedPiece & PENULTIMATE_RANK) == 0) {
                    addMovesFromAttackTableMaster(moves,
                            singlePawnPushes(pinnedPiece, whiteTurn, pushMask, allPieces)
                                    | singlePawnCaptures(pinnedPiece, whiteTurn, pinningPiece),
                            pinnedPieceIndex, board);

                    // a pinned pawn may still EP
                    addEnPassantMoves(moves, board, whiteTurn, allButPinnedFriends, pushMask, pinningPiece,
                            myPawns, myKing,
                            enemyPawns, enemyKnights, enemyBishops, enemyRooks, enemyQueens, enemyKing
                    );
                } else {
                    // a pinned pawn may still promote, through a capture of the pinner
                    addPromotionMoves(moves, board, whiteTurn, allButPinnedFriends, pushMask, pinningPiece,
                            myPawns,
                            enemies, allPieces);
                }
                pinnedPieces &= pinnedPieces - 1;
                continue;
            }
            if ((pinnedPiece & myBishops) != 0) {
                addMovesFromAttackTableMaster(moves,
                        singleBishopTable(allPieces, pinnedPiece, UNIVERSE) & mask,
                        pinnedPieceIndex, board);
                pinnedPieces &= pinnedPieces - 1;
                continue;
            }
            if ((pinnedPiece & myRooks) != 0) {
                addMovesFromAttackTableMaster(moves,
                        singleRookTable(allPieces, pinnedPiece, UNIVERSE) & mask,
                        pinnedPieceIndex, board);
                pinnedPieces &= pinnedPieces - 1;
                continue;
            }
            if ((pinnedPiece & myQueens) != 0) {
                addMovesFromAttackTableMaster(moves,
                        (singleQueenTable(allPieces, pinnedPiece, UNIVERSE) & mask),
                        pinnedPieceIndex, board);
            }

            pinnedPieces &= pinnedPieces - 1;
        }
    }

}
