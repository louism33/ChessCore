package chessprogram.god;

import static chessprogram.god.BitOperations.*;
import static chessprogram.god.CheckHelper.numberOfPiecesThatLegalThreatenSquare;
import static chessprogram.god.Magic.extractRayFromTwoPiecesBitboardInclusive;
import static chessprogram.god.MoveGenerationUtilities.addMovesFromAttackTableMaster;
import static chessprogram.god.MoveGeneratorCastling.addCastlingMoves;
import static chessprogram.god.MoveGeneratorCheck.addCheckEvasionMoves;
import static chessprogram.god.MoveGeneratorEnPassant.addEnPassantMoves;
import static chessprogram.god.MoveGeneratorKingLegal.addKingLegalMovesOnly;
import static chessprogram.god.MoveGeneratorPromotion.addPromotionMoves;
import static chessprogram.god.MoveGeneratorPseudo.addAllMovesWithoutKing;
import static chessprogram.god.PieceMovePawns.singlePawnCaptures;
import static chessprogram.god.PieceMovePawns.singlePawnPushes;
import static chessprogram.god.PieceMoveSliding.*;
import static chessprogram.god.PinnedManager.whichPiecesArePinned;

class MoveGeneratorMaster {

    static int[] generateLegalMoves(Chessboard board, boolean white) {
        int[] moves = new int[192];

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

            addPinnedPiecesMovesMagic(moves, board, whiteTurn, pinnedPieces, myKing,
                    myPawns, myKnights, myBishops, myRooks, myQueens, myKing,
                    enemyPawns, enemyKnights, enemyBishops, enemyRooks, enemyQueens, enemyKing,
                    enemies, friends, allPieces);
        }
    }

    private static void addPinnedPiecesMovesMagic(int[] moves, Chessboard board, boolean whiteTurn,
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
                            singlePawnPushes(board, pinnedPiece, whiteTurn, pushMask)
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
