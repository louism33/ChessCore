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

    static void generateLegalMoves(Chessboard board, int[] moves, int turn) {
        Assert.assertNotNull(moves);
        
        Assert.assertTrue(board.isWhiteTurn() ? turn == WHITE : turn == BLACK);

        boolean white = turn == WHITE;
        
        long myPawns, myKnights, myBishops, myRooks, myQueens, myKing;
        long enemyPawns, enemyKnights, enemyBishops, enemyRooks, enemyQueens, enemyKing;
        long friends, enemies;

        myPawns = board.pieces[turn][PAWN];
        myKnights = board.pieces[turn][KNIGHT];
        myBishops = board.pieces[turn][BISHOP];
        myRooks = board.pieces[turn][ROOK];
        myQueens = board.pieces[turn][QUEEN];
        myKing = board.pieces[turn][KING];

        enemyPawns = board.pieces[1 - turn][PAWN];
        enemyKnights = board.pieces[1 - turn][KNIGHT];
        enemyBishops = board.pieces[1 - turn][BISHOP];
        enemyRooks = board.pieces[1 - turn][ROOK];
        enemyQueens = board.pieces[1 - turn][QUEEN];
        enemyKing = board.pieces[1 - turn][KING];

        friends = board.getPieces(turn);
        enemies = board.getPieces(1 - turn);

        long allPieces = friends | enemies;

        int numberOfCheckers = numberOfPiecesThatLegalThreatenSquare(white, myKing,
                enemyPawns, enemyKnights, enemyBishops, enemyRooks, enemyQueens, enemyKing,
                allPieces);

        if (numberOfCheckers > 1){
            board.inCheckRecorder = true;
            
            addKingLegalMovesOnly(moves, board, white,
                    myKing,
                    enemyPawns, enemyKnights, enemyBishops, enemyRooks, enemyQueens, enemyKing,
                    enemies);
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

        addNotInCheckMoves(moves, board, pinnedPieces,
                myPawns, myKnights, myBishops, myRooks, myQueens, myKing,
                enemyPawns, enemyKnights, enemyBishops, enemyRooks, enemyQueens, enemyKing,
                enemies, friends, allPieces);

    }

    private static void addNotInCheckMoves(int[] moves, Chessboard board, long pinnedPieces,
                                           long myPawns, long myKnights, long myBishops, long myRooks, long myQueens, long myKing,
                                           long enemyPawns, long enemyKnights, long enemyBishops, long enemyRooks, long enemyQueens, long enemyKing,
                                           long enemies, long friends, long allPieces){

        long emptySquares = ~allPieces;

        long promotablePawns = myPawns & PENULTIMATE_RANKS[board.turn];
        long pinnedPiecesAndPromotingPawns = pinnedPieces | promotablePawns;

        
        boolean whiteTurn = board.turn == WHITE;
        addCastlingMoves(moves, board.turn, board.castlingRights, 
                enemyPawns, enemyKnights, enemyBishops, enemyRooks, enemyQueens, enemyKing,
                allPieces);

        addKingLegalMovesOnly(moves, board, whiteTurn,
                myKing,
                enemyPawns, enemyKnights, enemyBishops, enemyRooks, enemyQueens, enemyKing,
                enemies);

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

            addPinnedPiecesMoves(moves, board, board.turn, pinnedPieces, myKing,
                    myPawns, myKnights, myBishops, myRooks, myQueens, myKing,
                    enemyPawns, enemyKnights, enemyBishops, enemyRooks, enemyQueens, enemyKing,
                    enemies, friends, allPieces);
        }
    }

    private static void addPinnedPiecesMoves(int[] moves, Chessboard board, int turn,
                                             long pinnedPieces, long squareWeArePinnedTo,
                                             long myPawns, long myKnights, long myBishops, long myRooks, long myQueens, long myKing,
                                             long enemyPawns, long enemyKnights, long enemyBishops, long enemyRooks, long enemyQueens, long enemyKing,
                                             long enemies, long friends, long allPieces){

        boolean whiteTurn = turn == WHITE;
        
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
                            singlePawnPushes(pinnedPiece, whiteTurn, pushMask, allPieces)
                                    | singlePawnCaptures(pinnedPiece, whiteTurn, pinningPiece),
                            pinnedPieceIndex, board.turn == WHITE ? WHITE_PAWN : BLACK_PAWN, board);

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
                addMovesFromAttackTableMasterBetter(moves,
                        singleBishopTable(allPieces, pinnedPiece, UNIVERSE) & mask,
                        pinnedPieceIndex, board.turn == WHITE ? WHITE_BISHOP : BLACK_BISHOP, board);
                pinnedPieces &= pinnedPieces - 1;
                continue;
            }
            if ((pinnedPiece & myRooks) != 0) {
                addMovesFromAttackTableMasterBetter(moves,
                        singleRookTable(allPieces, pinnedPiece, UNIVERSE) & mask,
                        pinnedPieceIndex,  board.turn == WHITE ? WHITE_ROOK : BLACK_ROOK, board);
                pinnedPieces &= pinnedPieces - 1;
                continue;
            }
            if ((pinnedPiece & myQueens) != 0) {
                addMovesFromAttackTableMasterBetter(moves,
                        (singleQueenTable(allPieces, pinnedPiece, UNIVERSE) & mask),
                        pinnedPieceIndex, board.turn == WHITE ? WHITE_QUEEN : BLACK_QUEEN, board);
            }

            pinnedPieces &= pinnedPieces - 1;
        }
    }

}
