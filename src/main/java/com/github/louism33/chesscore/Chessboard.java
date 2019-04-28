package com.github.louism33.chesscore;

import org.junit.Assert;

import java.util.Arrays;

import static com.github.louism33.chesscore.BitOperations.*;
import static com.github.louism33.chesscore.BoardConstants.*;
import static com.github.louism33.chesscore.CheckHelper.bitboardOfPiecesThatLegalThreatenSquare;
import static com.github.louism33.chesscore.CheckHelper.boardInCheck;
import static com.github.louism33.chesscore.MakeMoveSpecial.*;
import static com.github.louism33.chesscore.MaterialHashUtil.*;
import static com.github.louism33.chesscore.MaterialHashUtil.startingMaterialHash;
import static com.github.louism33.chesscore.MoveAdder.addMovesFromAttackTableMaster;
import static com.github.louism33.chesscore.MoveConstants.*;
import static com.github.louism33.chesscore.MoveGeneratorCheck.addCheckEvasionMoves;
import static com.github.louism33.chesscore.MoveGeneratorPseudo.addAllMovesWithoutKing;
import static com.github.louism33.chesscore.MoveGeneratorRegular.addKingLegalMovesOnly;
import static com.github.louism33.chesscore.MoveGeneratorSpecial.*;
import static com.github.louism33.chesscore.MoveMakingUtilities.removePieces;
import static com.github.louism33.chesscore.MoveMakingUtilities.togglePiecesFrom;
import static com.github.louism33.chesscore.MoveParser.*;
import static com.github.louism33.chesscore.PieceMove.*;
import static com.github.louism33.chesscore.PinnedManager.whichPiecesArePinned;
import static com.github.louism33.chesscore.StackDataUtil.*;
import static com.github.louism33.chesscore.ZobristHashUtil.*;
import static java.lang.Long.numberOfTrailingZeros;

public final class Chessboard {

    public static final int MAX_DEPTH_AND_ARRAY_LENGTH = 128;

    public final long[][] pieces = new long[2][7];

    public final int[] pieceSquareTable = new int[64];
    public int turn;
    /*
    castling rights bits:
    BK BA WK WQ
     */
    public int castlingRights = 0xf;

    public int quietHalfMoveCounter = 0, fullMoveCounter = 0;

    public int materialHash;
    public long zobristHash;
    public long zobristPawnHash;

    public long moveStackData;

    private static final int maxNumberOfMovesInAnyPosition = 128;
    final int[] moves = new int[maxNumberOfMovesInAnyPosition];

    private final int[][] legalMoveStack = new int[MAX_DEPTH_AND_ARRAY_LENGTH][maxNumberOfMovesInAnyPosition];

    public final int[] materialHashStack = new int[MAX_DEPTH_AND_ARRAY_LENGTH];
    public final long[] zobristHashStack = new long[MAX_DEPTH_AND_ARRAY_LENGTH];
    public final long[] zobristPawnHashStack = new long[MAX_DEPTH_AND_ARRAY_LENGTH];

    private final long[] pastMoveStackArray = new long[MAX_DEPTH_AND_ARRAY_LENGTH];
    public boolean inCheckRecorder;

    public long checkingPieces;

    public long pinnedPieces;
    public long pinningPieces;
    private final long[] pinnedPiecesArray = new long[MAX_DEPTH_AND_ARRAY_LENGTH];
    private final boolean[] checkStack = new boolean[MAX_DEPTH_AND_ARRAY_LENGTH];


    private long boardToHash() {
        long hash = 0;
        for (int sq = 0; sq < 64; sq++) {
            long pieceOnSquare = newPieceOnSquare(sq);
            int pieceIndex = pieceSquareTable[numberOfTrailingZeros(pieceOnSquare)] - 1;
            if (pieceIndex != -1) {
                hash ^= zobristHashPieces[sq][pieceIndex];
            }

        }

        hash ^= zobristHashCastlingRights[castlingRights];

        if (!isWhiteTurn()) {
            hash = zobristFlipTurn(hash);
        }

        if (hasPreviousMove()) {
            hash = updateWithEPFlags(moveStackArrayPeek(), hash);
        }

        return hash;
    }

    private long makePawnHash() {
        long hash = 0;
        for (int sq = 0; sq < 64; sq++) {
            long pieceOnSquare = newPieceOnSquare(sq);
            int pieceIndex = pieceSquareTable[numberOfTrailingZeros(pieceOnSquare)] - 1;
            // adding 1 because zobrist pieces indexed from 0 but pawn is == 1
            if (pieceIndex + 1 == WHITE_PAWN || pieceIndex + 1 == BLACK_PAWN) {
                hash ^= zobristHashPieces[sq][pieceIndex];
            }
        }

        return hash;
    }

    public Chessboard() {
        Setup.init(false);

        System.arraycopy(INITIAL_PIECES[BLACK], 0, this.pieces[BLACK], 0, INITIAL_PIECES[BLACK].length);
        System.arraycopy(INITIAL_PIECES[WHITE], 0, this.pieces[WHITE], 0, INITIAL_PIECES[WHITE].length);

        System.arraycopy(INITIAL_PIECE_SQUARES, 0, pieceSquareTable, 0, pieceSquareTable.length);

        materialHash = startingMaterialHash;
        
        turn = WHITE;

        zobristHash = boardToHash();
        zobristPawnHash = makePawnHash();
    }

    public Chessboard(Chessboard board) {
        this.turn = board.turn;
        this.castlingRights = board.castlingRights;
        this.quietHalfMoveCounter = board.quietHalfMoveCounter;
        this.fullMoveCounter = board.fullMoveCounter;
        this.zobristHash = board.zobristHash;
        this.zobristPawnHash = board.zobristPawnHash;
        this.moveStackData = board.moveStackData;
        this.inCheckRecorder = board.inCheckRecorder;
        this.pinnedPieces = board.pinnedPieces;
        this.pinningPieces = board.pinningPieces;
        this.legalMoveStackIndex = board.legalMoveStackIndex;
        this.masterIndex = board.masterIndex;
        this.moveStackIndex = board.moveStackIndex;
        this.materialHash = board.materialHash;

        System.arraycopy(board.pieces[WHITE], 0, this.pieces[WHITE], 0, 7);
        System.arraycopy(board.pieces[BLACK], 0, this.pieces[BLACK], 0, 7);
        System.arraycopy(board.moves, 0, this.moves, 0, board.moves.length);

        for (int i = 0; i < board.legalMoveStack.length; i++) {
            System.arraycopy(board.legalMoveStack[i], 0, this.legalMoveStack[i], 0, board.legalMoveStack[i].length);
        }

        System.arraycopy(board.materialHashStack, 0, this.materialHashStack, 0, board.materialHashStack.length);
        System.arraycopy(board.zobristHashStack, 0, this.zobristHashStack, 0, board.zobristHashStack.length);
        System.arraycopy(board.zobristPawnHashStack, 0, this.zobristPawnHashStack, 0, board.zobristPawnHashStack.length);
        System.arraycopy(board.pastMoveStackArray, 0, this.pastMoveStackArray, 0, board.pastMoveStackArray.length);
        System.arraycopy(board.pinnedPiecesArray, 0, this.pinnedPiecesArray, 0, board.pinnedPiecesArray.length);
        System.arraycopy(board.checkStack, 0, this.checkStack, 0, board.checkStack.length);

        System.arraycopy(board.pieceSquareTable, 0, pieceSquareTable, 0, board.pieceSquareTable.length);

        Setup.init(false);
    }


    public final int[] generateLegalMoves() {
        Assert.assertNotNull(this.legalMoveStack[legalMoveStackIndex]);
        // only clean array of moves if it has something in it
        if (this.legalMoveStack[legalMoveStackIndex][0] != 0) {
            Arrays.fill(this.legalMoveStack[legalMoveStackIndex], 0);
        }

        int[] moves = this.legalMoveStack[legalMoveStackIndex];

        final long myPawns, myKnights, myBishops, myRooks, myQueens, myKing;
        final long enemyPawns, enemyKnights, enemyBishops, enemyRooks, enemyQueens, enemyKing;
        final long friends, enemies;

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

        getPieces();
        friends = this.pieces[turn][ALL_COLOUR_PIECES];
        enemies = this.pieces[1 - turn][ALL_COLOUR_PIECES];


        final long allPieces = friends | enemies;

        final long checkingPieces = bitboardOfPiecesThatLegalThreatenSquare(turn, myKing,
                enemyPawns, enemyKnights, enemyBishops, enemyRooks, enemyQueens, 0,
                allPieces, 2);

        this.checkingPieces = checkingPieces;

        final int numberOfCheckers = populationCount(checkingPieces);

        if (numberOfCheckers > 1) {
            inCheckRecorder = true;

            addKingLegalMovesOnly(this.legalMoveStack[legalMoveStackIndex], turn, this.pieces, pieceSquareTable,
                    myKing,
                    enemyPawns, enemyKnights, enemyBishops, enemyRooks, enemyQueens, enemyKing,
                    friends, allPieces);
            return this.legalMoveStack[legalMoveStackIndex];
        }

        final long currentPinnedPieces = whichPiecesArePinned(this, myKing,
                enemyBishops, enemyRooks, enemyQueens,
                friends, allPieces);

        pinnedPieces = currentPinnedPieces;


        final boolean hasPreviousMove = hasPreviousMove();
        if (numberOfCheckers == 1) {
            inCheckRecorder = true;

            addCheckEvasionMoves(this.checkingPieces, this.legalMoveStack[legalMoveStackIndex], turn, pieceSquareTable,
                    this.pieces, hasPreviousMove, moveStackArrayPeek(), currentPinnedPieces,
                    myPawns, myKnights, myBishops, myRooks, myQueens, myKing,
                    enemyPawns, enemyKnights, enemyBishops, enemyRooks, enemyQueens, enemyKing,
                    enemies, friends, allPieces);

            return this.legalMoveStack[legalMoveStackIndex];
        }

        inCheckRecorder = false;

        long pinnedPieces = currentPinnedPieces;

        // not in check moves
        final long emptySquares = ~allPieces;
        final long promotablePawns = myPawns & PENULTIMATE_RANKS[turn];
        final long pinnedPiecesAndPromotingPawns = pinnedPieces | promotablePawns;

        addCastlingMoves(this.legalMoveStack[legalMoveStackIndex], turn, castlingRights,
                enemyPawns, enemyKnights, enemyBishops, enemyRooks, enemyQueens, enemyKing,
                allPieces);

        addKingLegalMovesOnly(this.legalMoveStack[legalMoveStackIndex], turn, this.pieces, pieceSquareTable,
                myKing,
                enemyPawns, enemyKnights, enemyBishops, enemyRooks, enemyQueens, enemyKing,
                friends, allPieces);

        if (pinnedPieces == 0) {
            addPromotionMoves
                    (this.legalMoveStack[legalMoveStackIndex], turn, pieceSquareTable, 0, emptySquares, enemies,
                            myPawns,
                            enemies, allPieces);

            addAllMovesWithoutKing
                    (this.legalMoveStack[legalMoveStackIndex], this.pieces, turn, pieceSquareTable, promotablePawns, emptySquares, enemies,
                            myKnights, myBishops, myRooks, myQueens,
                            allPieces);

            if (hasPreviousMove) {
                addEnPassantMoves
                        (this.legalMoveStack[legalMoveStackIndex], moveStackArrayPeek(), turn, promotablePawns, emptySquares, enemies,
                                myPawns, myKing,
                                enemyPawns, enemyKnights, enemyBishops, enemyRooks, enemyQueens, enemyKing, allPieces
                        );
            }
        } else {
            addPromotionMoves
                    (this.legalMoveStack[legalMoveStackIndex], turn, pieceSquareTable, pinnedPieces, emptySquares, enemies,
                            myPawns,
                            enemies, allPieces);

            addAllMovesWithoutKing
                    (this.legalMoveStack[legalMoveStackIndex], this.pieces, turn, pieceSquareTable, pinnedPiecesAndPromotingPawns, ~allPieces, enemies,
                            myKnights, myBishops, myRooks, myQueens,
                            allPieces);

            if (hasPreviousMove) {
                addEnPassantMoves
                        (this.legalMoveStack[legalMoveStackIndex], moveStackArrayPeek(), turn, pinnedPiecesAndPromotingPawns, emptySquares, enemies,
                                myPawns, myKing,
                                enemyPawns, enemyKnights, enemyBishops, enemyRooks, enemyQueens, enemyKing, allPieces
                        );
            }

            // pinned pieces moves
            while (pinnedPieces != 0) {
                long pinnedPiece = getFirstPiece(pinnedPieces);
                long pinningPiece = xrayQueenAttacks(allPieces, pinnedPiece, myKing) & enemies;
                long pushMask = extractRayFromTwoPiecesBitboardInclusive(myKing, pinningPiece)
                        ^ (pinningPiece | myKing);

                final int pinnedPieceIndex = numberOfTrailingZeros(pinnedPiece);
                final long mask = (pushMask | pinningPiece);

                if ((pinnedPiece & myKnights) != 0) {
                    // knights cannot move cardinally or diagonally, and so cannot move while pinned
                    pinnedPieces &= pinnedPieces - 1;
                    continue;
                }
                if ((pinnedPiece & myPawns) != 0) {
                    final long allButPinnedFriends = friends & ~pinnedPiece;

                    if ((pinnedPiece & PENULTIMATE_RANKS[turn]) == 0) {
                        final long captureTable = singlePawnCaptures(pinnedPiece, turn, pinningPiece);
                        if (captureTable != 0) {
                            final int destinationIndex = numberOfTrailingZeros(captureTable);
                            moves[moves[moves.length - 1]++] =
                                    buildMove(numberOfTrailingZeros(pinnedPiece), PIECE[turn][PAWN],
                                            destinationIndex, pieceSquareTable[destinationIndex]);
                        }

                        final long quietMask = singlePawnPushes(pinnedPiece, turn, pushMask, allPieces);
                        if (quietMask != 0) {
                            addMovesFromAttackTableMaster(this.legalMoveStack[legalMoveStackIndex],
                                    quietMask,
                                    pinnedPieceIndex, PIECE[turn][PAWN]);
                        }

                        // a pinned pawn may still EP
                        if (hasPreviousMove) {
                            addEnPassantMoves(this.legalMoveStack[legalMoveStackIndex],
                                    moveStackArrayPeek(), turn, allButPinnedFriends, pushMask, pinningPiece,
                                    myPawns, myKing,
                                    enemyPawns, enemyKnights, enemyBishops, enemyRooks, enemyQueens, enemyKing, allPieces
                            );
                        }
                    } else {
                        // a pinned pawn may still promote, through a capture of the pinner
                        addPromotionMoves(this.legalMoveStack[legalMoveStackIndex], turn,
                                pieceSquareTable, allButPinnedFriends, pushMask, pinningPiece,
                                myPawns,
                                enemies, allPieces);
                    }
                    pinnedPieces &= pinnedPieces - 1;
                    continue;
                }
                if ((pinnedPiece & myBishops) != 0) {
                    final long table = singleBishopTable(allPieces, pinnedPiece, UNIVERSE) & mask;
                    final long captureTable = table & allPieces;
                    if (captureTable != 0) {
                        final int destinationIndex = numberOfTrailingZeros(captureTable);
                        moves[moves[moves.length - 1]++] =
                                buildMove(numberOfTrailingZeros(pinnedPiece), PIECE[turn][BISHOP],
                                        destinationIndex, pieceSquareTable[destinationIndex]);
                    }
                    final long quietMask = table & ~allPieces;
                    if (quietMask != 0) {
                        addMovesFromAttackTableMaster(this.legalMoveStack[legalMoveStackIndex],
                                quietMask,
                                pinnedPieceIndex, PIECE[turn][BISHOP]);
                    }
                    pinnedPieces &= pinnedPieces - 1;
                    continue;
                }
                if ((pinnedPiece & myRooks) != 0) {
                    final long table = singleRookTable(allPieces, pinnedPiece, UNIVERSE) & mask;
                    final long captureTable = table & allPieces;
                    if (captureTable != 0) {
                        final int destinationIndex = numberOfTrailingZeros(captureTable);
                        moves[moves[moves.length - 1]++] =
                                buildMove(numberOfTrailingZeros(pinnedPiece), PIECE[turn][ROOK],
                                        destinationIndex, pieceSquareTable[destinationIndex]);
                    }
                    final long quietMask = table & ~allPieces;
                    if (quietMask != 0) {
                        addMovesFromAttackTableMaster(this.legalMoveStack[legalMoveStackIndex],
                                quietMask,
                                pinnedPieceIndex, PIECE[turn][ROOK]);
                    }
                    pinnedPieces &= pinnedPieces - 1;
                    continue;
                }
                if ((pinnedPiece & myQueens) != 0) {
                    final long table = singleQueenTable(allPieces, pinnedPiece, UNIVERSE) & mask;
                    final long captureTable = table & allPieces;
                    if (captureTable != 0) {
                        final int destinationIndex = numberOfTrailingZeros(captureTable);
                        moves[moves[moves.length - 1]++] =
                                buildMove(numberOfTrailingZeros(pinnedPiece), PIECE[turn][QUEEN],
                                        destinationIndex, pieceSquareTable[destinationIndex]);
                    }

                    final long quietTable = table & ~allPieces;
                    if (quietTable != 0) {
                        addMovesFromAttackTableMaster(this.legalMoveStack[legalMoveStackIndex],
                                quietTable,
                                pinnedPieceIndex, PIECE[turn][QUEEN]);
                    }
                }

                pinnedPieces &= pinnedPieces - 1;
            }
        }

        return this.legalMoveStack[legalMoveStackIndex];
    }

    public final void makeMoveAndFlipTurn(final int move) {
        this.rotateMoveIndexUp();
        Assert.assertNotEquals(move, 0);
        masterStackPush();

        final int sourceIndex = getSourceIndex(move);
        final int destinationIndex = getDestinationIndex(move);
        final int sourcePieceIdentifier = pieceSquareTable[sourceIndex] - 1;
        final boolean captureMove = isCaptureMove(move);
        final long destinationPiece = newPieceOnSquare(destinationIndex);
        final long destinationZH = zobristHashPieces[destinationIndex][sourcePieceIdentifier];

        zobristHash ^= zobristHashPieces[sourceIndex][sourcePieceIdentifier];
        zobristHash ^= destinationZH;

        // +1 because zobrist has offset array index
        if (sourcePieceIdentifier + 1 == WHITE_PAWN || sourcePieceIdentifier + 1 == BLACK_PAWN) {
            zobristPawnHash ^= zobristHashPieces[sourceIndex][sourcePieceIdentifier];
            zobristPawnHash ^= destinationZH;
        }

        if (captureMove) {
            final int victim = pieceSquareTable[destinationIndex];
            materialHash = removePieceFromMaterialHash(materialHash, victim, destinationPiece);
            final long victimHash = zobristHashPieces[destinationIndex][victim - 1];
            this.zobristHash ^= victimHash;
            if (victim == WHITE_PAWN || victim == BLACK_PAWN) {
                zobristPawnHash ^= victimHash;
            }
        }

        /* 
        "positive" EP flag is set in updateHashPostMove, in updateHashPreMove we cancel a previous EP flag
        */
        if (hasPreviousMove()) {
            zobristHash = updateWithEPFlags(moveStackArrayPeek(), zobristHash);
        }

        fullMoveCounter++;

        if (move == 0) {
            moveStackArrayPush(buildStackDataBetter(0, turn, quietHalfMoveCounter, castlingRights, NULL_MOVE));
            quietHalfMoveCounter++;
            return;
        }

        boolean resetFifty = false;

        if (isSpecialMove(move)) {
            switch (move & SPECIAL_MOVE_MASK) {
                case CASTLING_MASK:
                    int originalRookIndex = 0;
                    int newRookIndex = 0;
                    switch (destinationIndex) {
                        case 1:
                            originalRookIndex = 0;
                            newRookIndex = destinationIndex + 1;
                            break;
                        case 5:
                            originalRookIndex = 7;
                            newRookIndex = destinationIndex - 1;
                            break;
                        case 57:
                            originalRookIndex = 56;
                            newRookIndex = destinationIndex + 1;
                            break;
                        case 61:
                            originalRookIndex = 63;
                            newRookIndex = destinationIndex - 1;
                            break;
                    }

                    final int myRook = pieceSquareTable[originalRookIndex] - 1;
                    zobristHash ^= zobristHashPieces[originalRookIndex][myRook];
                    zobristHash ^= zobristHashPieces[newRookIndex][myRook];

                    moveStackArrayPush(buildStackDataBetter(move, turn, quietHalfMoveCounter, castlingRights, CASTLING));
                    castlingRights = makeCastlingMove(castlingRights, pieces, pieceSquareTable, move);
                    break;

                case ENPASSANT_MASK:

                    resetFifty = true;

                    final long victimPawn = turn == WHITE ? destinationPiece >>> 8 : destinationPiece << 8;
                    final int victimPawnIndex = numberOfTrailingZeros(victimPawn);
                    final long zobristHashVictim = zobristHashPieces
                            [victimPawnIndex]
                            [pieceSquareTable[victimPawnIndex] - 1];
                    this.zobristHash ^= zobristHashVictim;
                    this.zobristPawnHash ^= zobristHashVictim;

                    moveStackArrayPush(buildStackDataBetter(move, turn, quietHalfMoveCounter, castlingRights, ENPASSANTCAPTURE));
                    makeEnPassantMove(pieces, pieceSquareTable, turn, move);
                    materialHash = removePieceFromMaterialHash(materialHash, turn == BLACK ? WHITE_PAWN : BLACK_PAWN, destinationPiece);
                    break;

                case PROMOTION_MASK:

                    resetFifty = true;

                    int whichPromotingPiece = 0;

                    switch (move & WHICH_PROMOTION) {
                        case KNIGHT_PROMOTION_MASK:
                            whichPromotingPiece = 2 + turn * 6;
                            break;
                        case BISHOP_PROMOTION_MASK:
                            whichPromotingPiece = 3 + turn * 6;
                            break;
                        case ROOK_PROMOTION_MASK:
                            whichPromotingPiece = 4 + turn * 6;
                            break;
                        case QUEEN_PROMOTION_MASK:
                            whichPromotingPiece = 5 + turn * 6;
                            break;
                    }

                    /*
                    remove my pawn from zh
                     */
                    this.zobristHash ^= destinationZH;
                    this.zobristPawnHash ^= destinationZH;

                    Assert.assertTrue(whichPromotingPiece != 0);
                    long promotionZH = zobristHashPieces[destinationIndex][whichPromotingPiece - 1];
                    this.zobristHash ^= promotionZH;

                    moveStackArrayPush(buildStackDataBetter(move, turn, quietHalfMoveCounter, castlingRights, PROMOTION));

                    makePromotingMove(pieces, pieceSquareTable, turn, move);
                    
                    materialHash = removePieceFromMaterialHash(materialHash, turn == WHITE ? WHITE_PAWN : BLACK_PAWN, destinationPiece);
                    materialHash = addPieceToMaterialHash(materialHash, whichPromotingPiece, destinationPiece);
                    
                    break;
            }
        } else {
            if (captureMove) {
                resetFifty = true;
                moveStackArrayPush(buildStackDataBetter(move, turn, quietHalfMoveCounter, castlingRights, BASICCAPTURE));
            } else if (enPassantPossibility(turn, pieces[turn][PAWN], newPieceOnSquare(sourceIndex), destinationPiece)) {
                resetFifty = true;
                final int whichFile = 8 - sourceIndex % 8;
                moveStackArrayPush(buildStackDataBetter(move, turn, quietHalfMoveCounter, castlingRights, ENPASSANTVICTIM, whichFile));
            } else {
                switch (pieceSquareTable[sourceIndex]) {
                    case WHITE_PAWN:
                    case BLACK_PAWN:
                        resetFifty = true;
                        moveStackArrayPush(buildStackDataBetter(move, turn, quietHalfMoveCounter, castlingRights, BASICLOUDPUSH));
                        break;
                    default:
                        resetFifty = false;
                        moveStackArrayPush(buildStackDataBetter(move, turn, quietHalfMoveCounter, castlingRights, BASICQUIETPUSH));
                }

            }

            makeRegularMove(pieces, pieceSquareTable, move);
        }

        if (resetFifty) {
            quietHalfMoveCounter = 0;
        } else {
            quietHalfMoveCounter++;
        }


        if (castlingRights != 0) {
            castleFlagManager(sourceIndex, destinationIndex);
        }

        Assert.assertTrue(hasPreviousMove());
        zobristHash = (updateHashPostMove(moveStackArrayPeek(), castlingRights, zobristHash));

        this.turn = 1 - this.turn;
    }

    private void castleFlagManager(int sourceIndex, int destinationIndex) {
        // disable relevant castle flag whenever a piece moves into the relevant square.
        switch (sourceIndex) {
            case 0:
                castlingRights &= castlingRightsMask[WHITE][K];
                break;
            case 3:
                castlingRights &= castlingRightsMask[WHITE][K];
            case 7:
                castlingRights &= castlingRightsMask[WHITE][Q];
                break;
            case 56:
                castlingRights &= castlingRightsMask[BLACK][K];
                break;
            case 59:
                castlingRights &= castlingRightsMask[BLACK][K];
            case 63:
                castlingRights &= castlingRightsMask[BLACK][Q];
                break;
        }
        switch (destinationIndex) {
            case 0:
                castlingRights &= castlingRightsMask[WHITE][K];
                break;
            case 3:
                castlingRights &= castlingRightsMask[WHITE][K];
            case 7:
                castlingRights &= castlingRightsMask[WHITE][Q];
                break;
            case 56:
                castlingRights &= castlingRightsMask[BLACK][K];
                break;
            case 59:
                castlingRights &= castlingRightsMask[BLACK][K];
            case 63:
                castlingRights &= castlingRightsMask[BLACK][Q];
                break;
        }
    }

    private static boolean enPassantPossibility(int turn, long myPawns, long sourceSquare, long destinationSquare) {
        // determine if flag should be added to enable EP on next turn
        long homeRank = PENULTIMATE_RANKS[1 - turn];

        if ((sourceSquare & homeRank) == 0) {
            return false;
        }

        if ((sourceSquare & myPawns) == 0) {
            return false;
        }
        long enPassantPossibilityRank = ENPASSANT_RANK[turn];
        return (destinationSquare & enPassantPossibilityRank) != 0;
    }

    private void rotateMoveIndexUp() {
        this.legalMoveStackIndex = (this.legalMoveStackIndex + 1 + MAX_DEPTH_AND_ARRAY_LENGTH) % MAX_DEPTH_AND_ARRAY_LENGTH;
    }


    private void rotateMoveIndexDown() {
        this.legalMoveStackIndex = (this.legalMoveStackIndex - 1 + MAX_DEPTH_AND_ARRAY_LENGTH) % MAX_DEPTH_AND_ARRAY_LENGTH;
    }

    public void unMakeMoveAndFlipTurn() {
        this.rotateMoveIndexDown();

        Assert.assertTrue(hasPreviousMove());

        masterStackPop();

        long pop = moveStackData;

        quietHalfMoveCounter = StackDataUtil.getQuietHalfmoveCounter(pop);

        // todo, unmake null move here
        if (StackDataUtil.getMove(pop) == 0) {
            turn = StackDataUtil.getTurn(pop);
            return;
        }

        fullMoveCounter--;

        int pieceToMoveBackIndex = getDestinationIndex(StackDataUtil.getMove(pop));
        int squareToMoveBackTo = getSourceIndex(StackDataUtil.getMove(pop));
        int basicReversedMove = buildMove(pieceToMoveBackIndex, pieceSquareTable[pieceToMoveBackIndex],
                squareToMoveBackTo, NO_PIECE);

        switch (StackDataUtil.getSpecialMove(pop)) {
            //double pawn push
            case ENPASSANTVICTIM:
            case BASICQUIETPUSH:
            case BASICLOUDPUSH:
                makeRegularMove(pieces, pieceSquareTable, basicReversedMove);
                break;

            case BASICCAPTURE:
                makeRegularMove(pieces, pieceSquareTable, basicReversedMove);
                int takenPiece = getVictimPieceInt(StackDataUtil.getMove(pop));
                if (getVictimPieceInt(StackDataUtil.getMove(pop)) != 0) {
                    togglePiecesFrom(pieces, pieceSquareTable, newPieceOnSquare(pieceToMoveBackIndex), takenPiece);
                }
                break;

            case ENPASSANTCAPTURE:
                makeRegularMove(pieces, pieceSquareTable, basicReversedMove);
                if (StackDataUtil.getTurn(pop) == BLACK) {
                    togglePiecesFrom(pieces, pieceSquareTable, newPieceOnSquare(pieceToMoveBackIndex - 8), BLACK_PAWN);
                } else {
                    togglePiecesFrom(pieces, pieceSquareTable, newPieceOnSquare(pieceToMoveBackIndex + 8), WHITE_PAWN);
                }
                break;

            case CASTLING:
                // king moved to:
                long originalRook, newRook,
                        originalKing = newPieceOnSquare(squareToMoveBackTo),
                        newKing = newPieceOnSquare(pieceToMoveBackIndex);

                if (getTurn(pop) == BLACK) {
                    originalRook = newPieceOnSquare(pieceToMoveBackIndex == 1 ? 0 : 7);
                    newRook = newPieceOnSquare(pieceToMoveBackIndex == 1 ? pieceToMoveBackIndex + 1 : pieceToMoveBackIndex - 1);
                    togglePiecesFrom(pieces, pieceSquareTable, newKing, WHITE_KING);
                    togglePiecesFrom(pieces, pieceSquareTable, newRook, WHITE_ROOK);
                } else {
                    originalRook = newPieceOnSquare(pieceToMoveBackIndex == 57 ? 56 : 63);
                    newRook = newPieceOnSquare(pieceToMoveBackIndex == 57 ? pieceToMoveBackIndex + 1 : pieceToMoveBackIndex - 1);
                    togglePiecesFrom(pieces, pieceSquareTable, newKing, BLACK_KING);
                    togglePiecesFrom(pieces, pieceSquareTable, newRook, BLACK_ROOK);
                }

                pieces[1 - StackDataUtil.getTurn(pop)][KING] |= originalKing;
                pieces[1 - StackDataUtil.getTurn(pop)][ROOK] |= originalRook;

                pieces[1 - StackDataUtil.getTurn(pop)][ALL_COLOUR_PIECES] |= originalKing | originalRook;

                pieceSquareTable[squareToMoveBackTo] = WHITE_KING + (1 - StackDataUtil.getTurn(pop)) * 6;
                pieceSquareTable[numberOfTrailingZeros(originalRook)] = WHITE_ROOK + (1 - StackDataUtil.getTurn(pop)) * 6;
                break;

            case PROMOTION:
                long sourceSquare = newPieceOnSquare(pieceToMoveBackIndex);
                long destinationSquare = newPieceOnSquare(squareToMoveBackTo);
                long mask = ~(sourceSquare | destinationSquare);

                pieces[WHITE][PAWN] &= mask;
                pieces[WHITE][KNIGHT] &= mask;
                pieces[WHITE][BISHOP] &= mask;
                pieces[WHITE][ROOK] &= mask;
                pieces[WHITE][QUEEN] &= mask;
                pieces[WHITE][KING] &= mask;

                pieces[BLACK][PAWN] &= mask;
                pieces[BLACK][KNIGHT] &= mask;
                pieces[BLACK][BISHOP] &= mask;
                pieces[BLACK][ROOK] &= mask;
                pieces[BLACK][QUEEN] &= mask;
                pieces[BLACK][KING] &= mask;

                pieces[WHITE][ALL_COLOUR_PIECES] &= mask;
                pieces[BLACK][ALL_COLOUR_PIECES] &= mask;

                pieceSquareTable[pieceToMoveBackIndex] = 0;
                pieceSquareTable[squareToMoveBackTo] = 0;

                togglePiecesFrom(pieces, pieceSquareTable, destinationSquare,
                        StackDataUtil.getTurn(pop) == 1 ? WHITE_PAWN : BLACK_PAWN);
                int takenPiecePromotion = getVictimPieceInt(StackDataUtil.getMove(pop));
                if (takenPiecePromotion > 0) {
                    togglePiecesFrom(pieces, pieceSquareTable, sourceSquare, takenPiecePromotion);
                }
                break;
        }

        castlingRights = StackDataUtil.getCastlingRights(pop);
        turn = 1 - turn;
    }

    private static void makeRegularMove(long[][] pieces, int[] pieceSquareTable, int move) {
        final long destinationPiece = newPieceOnSquare(getDestinationIndex(move));
        removePieces(pieces, pieceSquareTable, newPieceOnSquare(getSourceIndex(move)), destinationPiece, move);
        togglePiecesFrom(pieces, pieceSquareTable, destinationPiece, getMovingPieceInt(move));
    }

    // todo, research into whether to reset counter on null move or not
    public void makeNullMoveAndFlipTurn() {
        quietHalfMoveCounter++;
        this.rotateMoveIndexUp();
        masterStackPush();

        if (hasPreviousMove()) {
            zobristHash = (updateWithEPFlags(moveStackArrayPeek(), zobristHash));
        }

        moveStackArrayPush(buildStackDataBetter(0, turn, quietHalfMoveCounter, castlingRights, NULL_MOVE));

        zobristHash = zobristFlipTurn(zobristHash);

        this.turn = 1 - this.turn;
    }

    public void unMakeNullMoveAndFlipTurn() {
        Assert.assertTrue(quietHalfMoveCounter > 0);
        quietHalfMoveCounter--;
        this.rotateMoveIndexDown();
        Assert.assertTrue(hasPreviousMove());
        masterStackPop();
        this.turn = 1 - this.turn;
    }

    public boolean isWhiteTurn() {
        return this.turn == WHITE;
    }


    public boolean inCheck(boolean white) {
        long myKing, enemyPawns, enemyKnights, enemyBishops, enemyRooks, enemyQueen, enemyKing, enemies, friends;
        if (white) {
            myKing = pieces[WHITE][KING];
            enemyPawns = pieces[BLACK][PAWN];
            enemyKnights = pieces[BLACK][KNIGHT];
            enemyBishops = pieces[BLACK][BISHOP];
            enemyRooks = pieces[BLACK][ROOK];
            enemyQueen = pieces[BLACK][QUEEN];
            enemyKing = pieces[BLACK][KING];

            enemies = blackPieces();
            friends = whitePieces();
        } else {
            myKing = pieces[BLACK][KING];
            enemyPawns = pieces[WHITE][PAWN];
            enemyKnights = pieces[WHITE][KNIGHT];
            enemyBishops = pieces[WHITE][BISHOP];
            enemyRooks = pieces[WHITE][ROOK];
            enemyQueen = pieces[WHITE][QUEEN];
            enemyKing = pieces[WHITE][KING];

            enemies = whitePieces();
            friends = blackPieces();
        }

        return boardInCheck(turn, myKing,
                enemyPawns, enemyKnights, enemyBishops, enemyRooks, enemyQueen, enemyKing,
                allPieces());

    }

    // todo
    public boolean inCheck(boolean white, int move) {
        long myKing, enemyPawns, enemyKnights, enemyBishops, enemyRooks, enemyQueen, enemyKing, enemies, friends;
        if (white) {
            myKing = pieces[WHITE][KING];
            enemyPawns = pieces[BLACK][PAWN];
            enemyKnights = pieces[BLACK][KNIGHT];
            enemyBishops = pieces[BLACK][BISHOP];
            enemyRooks = pieces[BLACK][ROOK];
            enemyQueen = pieces[BLACK][QUEEN];
            enemyKing = pieces[BLACK][KING];

            enemies = blackPieces();
            friends = whitePieces();
        } else {
            myKing = pieces[BLACK][KING];
            enemyPawns = pieces[WHITE][PAWN];
            enemyKnights = pieces[WHITE][KNIGHT];
            enemyBishops = pieces[WHITE][BISHOP];
            enemyRooks = pieces[WHITE][ROOK];
            enemyQueen = pieces[WHITE][QUEEN];
            enemyKing = pieces[WHITE][KING];

            enemies = whitePieces();
            friends = blackPieces();
        }

        return boardInCheck(turn, myKing,
                enemyPawns, enemyKnights, enemyBishops, enemyRooks, enemyQueen, enemyKing,
                allPieces());

    }


    public boolean isDrawByFiftyMoveRule() {
        return quietHalfMoveCounter >= 100;
    }

    // todo, consider hashing instead of linear search
    // todo, research into whether to reset counter on null move or not
    public boolean isDrawByRepetition(int stopAt) {
        int l = quietHalfMoveCounter < MAX_DEPTH_AND_ARRAY_LENGTH ? quietHalfMoveCounter : MAX_DEPTH_AND_ARRAY_LENGTH;
        int numberOfReps = 0;
        if (quietHalfMoveCounter < 2) {
            return false;
        }

        int c = 0;

        for (int i = simulateMasterIndexDown2(masterIndex); i >= -1; i = simulateMasterIndexDown2(i)) {
            long h = zobristHashStack[i];
            if (zobristHash == h) {
                numberOfReps++;
            }

            if (numberOfReps >= stopAt) {
                return true;
            }

            c += 2;
            if (c >= l) {
                break;
            }
        }
        return false;
    }

    private int simulateMasterIndexUp2(int masterIndex) {
        return (masterIndex + 2 + MAX_DEPTH_AND_ARRAY_LENGTH) % MAX_DEPTH_AND_ARRAY_LENGTH;
    }

    private int simulateMasterIndexDown2(int masterIndex) {
        return (masterIndex - 2 + MAX_DEPTH_AND_ARRAY_LENGTH) % MAX_DEPTH_AND_ARRAY_LENGTH;
    }

    // todo, add flag so no need to recalc? or only call after captures
    public boolean isDrawByInsufficientMaterial() {
        boolean drawByMaterial = false;
        // todo, necessary?
        getPieces();

        long friends = this.pieces[turn][ALL_COLOUR_PIECES];
        long enemies = this.pieces[1 - turn][ALL_COLOUR_PIECES];

        int totalPieces = populationCount(friends | enemies);
        int nonKingPieces = totalPieces - 2;
        switch (nonKingPieces) {
            case 0:
                drawByMaterial = true;
                break;
            case 1:
                if (populationCount(pieces[BLACK][BISHOP])
                        + populationCount(pieces[WHITE][BISHOP])
                        + populationCount(pieces[BLACK][KNIGHT])
                        + populationCount(pieces[WHITE][KNIGHT]) == nonKingPieces) {

                    drawByMaterial = true;
                }
                break;
            case 2:
                if (populationCount((pieces[BLACK][BISHOP] | pieces[WHITE][BISHOP]) & WHITE_COLOURED_SQUARES) == 2
                        || populationCount((pieces[BLACK][BISHOP] | pieces[WHITE][BISHOP]) & BLACK_COLOURED_SQUARES) == 2
                        || populationCount(pieces[BLACK][KNIGHT]) == 2
                        || populationCount(pieces[WHITE][KNIGHT]) == 2
                ) {
                    drawByMaterial = true;
                }
                break;
        }

        return drawByMaterial;
    }

    private boolean colourHasInsufficientMaterialToMate(boolean white) {
        return CheckHelper.colourHasInsufficientMaterialToMate(this, white);
    }

    public boolean inCheckmate() {
        if (!this.inCheck(isWhiteTurn())) {
            return false;
        }
        return this.generateLegalMoves().length == 0;
    }

    public boolean inStalemate() {
        if (this.inCheck(isWhiteTurn())) {
            return false;
        }
        return this.generateLegalMoves().length == 0;
    }

    public boolean previousMoveWasPawnPushToSix() {
        if (!hasPreviousMove()) {
            return false;
        }
        long peek = moveStackArrayPeek();
        return moveIsPawnPushSix(1 - StackDataUtil.getTurn(peek), StackDataUtil.getMove(peek));
    }

    public boolean previousMoveWasPawnPushToSeven() {
        if (!hasPreviousMove()) {
            return false;
        }
        long peek = moveStackArrayPeek();
        return moveIsPawnPushSeven(1 - StackDataUtil.getTurn(peek), StackDataUtil.getMove(peek));
    }

    public boolean moveIsCaptureOfLastMovePiece(int move) {
        if (!hasPreviousMove()) {
            return false;
        }

        long peek = moveStackArrayPeek();
        if (StackDataUtil.getMove(peek) == 0) {
            return false;
        }

        int previousMoveDestinationIndex = getDestinationIndex(StackDataUtil.getMove(peek));
        return (getDestinationIndex(move) == previousMoveDestinationIndex);
    }


    private long whitePieces() {
        this.pieces[WHITE][ALL_COLOUR_PIECES] = 0;
        for (int i = PAWN; i <= KING; i++) {
            this.pieces[WHITE][ALL_COLOUR_PIECES] |= this.pieces[WHITE][i];
        }
        return this.pieces[WHITE][ALL_COLOUR_PIECES];
    }

    public void getPieces() {
        long b = 0, w = 0;
        for (int i = PAWN; i <= KING; i++) {
            w |= this.pieces[WHITE][i];
            b |= this.pieces[BLACK][i];
        }
        this.pieces[WHITE][ALL_COLOUR_PIECES] = w;
        this.pieces[BLACK][ALL_COLOUR_PIECES] = b;
    }

    private long blackPieces() {
        this.pieces[BLACK][ALL_COLOUR_PIECES] = 0;
        for (int i = PAWN; i <= KING; i++) {
            this.pieces[BLACK][ALL_COLOUR_PIECES] |= this.pieces[BLACK][i];
        }
        return this.pieces[BLACK][ALL_COLOUR_PIECES];
    }

    public long allPieces() {
        getPieces();
        return this.pieces[WHITE][ALL_COLOUR_PIECES] | this.pieces[BLACK][ALL_COLOUR_PIECES];
    }

    @Override
    public boolean equals(Object o) {
        // we do not check equality for fields that change during move gen
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Chessboard that = (Chessboard) o;
        this.whitePieces();
        this.blackPieces();
        that.whitePieces();
        that.blackPieces();
        return turn == that.turn &&
                materialHash == that.materialHash && 
                castlingRights == that.castlingRights &&
                quietHalfMoveCounter == that.quietHalfMoveCounter &&
                zobristHash == that.zobristHash &&
                zobristPawnHash == that.zobristPawnHash &&
                masterIndex == that.masterIndex &&
                Arrays.deepEquals(pieces, that.pieces) &&
                Arrays.equals(zobristHashStack, that.zobristHashStack) &&
                Arrays.equals(materialHashStack, that.materialHashStack) &&
                Arrays.equals(zobristPawnHashStack, that.zobristPawnHashStack) &&
                Arrays.equals(pinnedPiecesArray, that.pinnedPiecesArray) &&
                Arrays.equals(checkStack, that.checkStack) &&
                Arrays.equals(pieceSquareTable, that.pieceSquareTable)
                ;
    }

    @Override
    public String toString() {
        String turn = isWhiteTurn() ? "It is white's turn." : "It is black's turn.";
        return '\n' + Art.boardArt(this) + '\n' + turn + '\n'
                ;
    }

    private int legalMoveStackIndex = 0;
    public int masterIndex = 0;
    private int moveStackIndex = 0;

    private void rotateMasterIndexUp() {
        this.masterIndex = (this.masterIndex + 1 + MAX_DEPTH_AND_ARRAY_LENGTH) % MAX_DEPTH_AND_ARRAY_LENGTH;
    }

    private void rotateMasterIndexDown() {
        this.masterIndex = (this.masterIndex - 1 + MAX_DEPTH_AND_ARRAY_LENGTH) % MAX_DEPTH_AND_ARRAY_LENGTH;
    }

    private void rotateMoveStackIndexUp() {
        this.moveStackIndex = (this.moveStackIndex + 1 + MAX_DEPTH_AND_ARRAY_LENGTH) % MAX_DEPTH_AND_ARRAY_LENGTH;
    }

    private void rotateMoveStackIndexDown() {
        this.moveStackIndex = (this.moveStackIndex - 1 + MAX_DEPTH_AND_ARRAY_LENGTH) % MAX_DEPTH_AND_ARRAY_LENGTH;
    }

    private boolean hasPreviousMove() {
        return pastMoveStackArray[(this.moveStackIndex - 1 + MAX_DEPTH_AND_ARRAY_LENGTH) % MAX_DEPTH_AND_ARRAY_LENGTH] != 0;
    }

    private void masterStackPush() {
        checkStack[masterIndex] = this.inCheckRecorder;
        inCheckRecorder = false;

        pinnedPiecesArray[masterIndex] = this.pinnedPieces;
        pinnedPieces = 0;
        pinningPieces = 0;

        materialHashStack[masterIndex] = materialHash;
        zobristHashStack[masterIndex] = zobristHash;
        zobristPawnHashStack[masterIndex] = zobristPawnHash;
        rotateMasterIndexUp();
    }

    private void masterStackPop() {
        rotateMasterIndexDown();
        inCheckRecorder = checkStack[masterIndex];
        checkStack[masterIndex] = false;

        pinnedPieces = pinnedPiecesArray[masterIndex];
        pinnedPiecesArray[masterIndex] = 0;

        materialHash = materialHashStack[masterIndex];
        materialHashStack[masterIndex] = 0;
        
        zobristHash = zobristHashStack[masterIndex];
        zobristHashStack[masterIndex] = 0;

        zobristPawnHash = zobristPawnHashStack[masterIndex];
        zobristPawnHashStack[masterIndex] = 0;

        pastMoveStackArray[moveStackIndex] = 0;
        rotateMoveStackIndexDown();
        moveStackData = pastMoveStackArray[moveStackIndex];
    }

    private void moveStackArrayPush(long l) {
        pastMoveStackArray[moveStackIndex] = l;
        rotateMoveStackIndexUp();
    }

    private long moveStackArrayPeek() {
        return moveStackIndex > 0 ? pastMoveStackArray[moveStackIndex - 1] : 0;
    }

    public Chessboard(String fen) {
        if (fen.endsWith(";")) {
            fen = fen.substring(0, fen.length() - 1);
        }
        char[] c = fen.toCharArray();
        int phase = 1;
        int square = 64, whichPiece;

        Arrays.fill(this.pieceSquareTable, 0);
        Arrays.fill(this.pieces[WHITE], 0);
        Arrays.fill(this.pieces[BLACK], 0);

        castlingRights = 0;

        for (int i = 0; i < c.length; i++) {
            if (c[i] == ' ') {
                phase++;
                continue;
            }
            if (c[i] == '-') {
                continue;
            }

            switch (phase) {
                case 1: //board
                    switch (c[i]) {
                        case 'P':
                            whichPiece = WHITE_PAWN;
                            break;
                        case 'N':
                            whichPiece = WHITE_KNIGHT;
                            break;
                        case 'B':
                            whichPiece = WHITE_BISHOP;
                            break;
                        case 'R':
                            whichPiece = WHITE_ROOK;
                            break;
                        case 'Q':
                            whichPiece = WHITE_QUEEN;
                            break;
                        case 'K':
                            whichPiece = WHITE_KING;
                            break;

                        case 'p':
                            whichPiece = BLACK_PAWN;
                            break;
                        case 'n':
                            whichPiece = BLACK_KNIGHT;
                            break;
                        case 'b':
                            whichPiece = BLACK_BISHOP;
                            break;
                        case 'r':
                            whichPiece = BLACK_ROOK;
                            break;
                        case 'q':
                            whichPiece = BLACK_QUEEN;
                            break;
                        case 'k':
                            whichPiece = BLACK_KING;
                            break;
                        case '/':
                            continue;
                        default:
                            square -= (c[i] - 48);
                            continue;
                    }
                    square--;

                    this.pieces[whichPiece / 7][whichPiece < 7 ? whichPiece : whichPiece - 6] |= newPieceOnSquare(square);
                    pieces[whichPiece / 7][ALL_COLOUR_PIECES] |= newPieceOnSquare(square);
                    pieceSquareTable[square] = whichPiece;

                    break;

                case 2: //player
                    if (c[i] == 'b') {
                        turn = BLACK;
                    } else {
                        turn = WHITE;
                    }
                    break;

                case 3: //castle
                    switch (c[i]) {
                        case 'K':
                            castlingRights |= castlingRightsOn[WHITE][K];
                            break;
                        case 'Q':
                            castlingRights |= castlingRightsOn[WHITE][Q];
                            break;
                        case 'q':
                            castlingRights |= castlingRightsOn[BLACK][Q];
                            break;
                        case 'k':
                            castlingRights |= castlingRightsOn[BLACK][K];
                            break;
                    }
                    break;

                case 4: //ep
                    final long item = buildStackDataBetter(0, turn, quietHalfMoveCounter,
                            castlingRights, ENPASSANTVICTIM, (int) c[i] - 96);
                    moveStackArrayPush(item);

                    // ignore number following epFile
                    while (i < c.length && c[i] != ' ') {
                        i++;
                    }
                    phase++;
                    break;

                case 5:
                    String fifty = "";
                    while (i < c.length && c[i] != ' ') {
                        fifty = fifty + c[i];
                        i++;
                    }
                    quietHalfMoveCounter = Integer.valueOf(fifty);
                    phase++;
                    break;

                case 6:
                    String full = "";
                    while (i < c.length && c[i] != ' ') {
                        full = full + c[i];
                        i++;
                    }
                    fullMoveCounter = Integer.valueOf(full);
                    phase++;
                    break;
            }
        }
        zobristHash = boardToHash();
        zobristPawnHash = makePawnHash();
        materialHash = makeMaterialHash(this);
        Setup.init(false);
    }

    public String toFenString() {
        StringBuilder fen = new StringBuilder();
        int c = 0;
        for (int i = 63; i >= 0; i--) {
            final int p = pieceSquareTable[i];
            if (p == NO_PIECE) {
                c++;
            } else {
                if (c > 0) {
                    fen.append(c);
                    c = 0;
                }
                switch (p) {
                    case WHITE_PAWN:
                        fen.append("P");
                        break;
                    case WHITE_KNIGHT:
                        fen.append("N");
                        break;
                    case WHITE_BISHOP:
                        fen.append("B");
                        break;
                    case WHITE_ROOK:
                        fen.append("R");
                        break;
                    case WHITE_QUEEN:
                        fen.append("Q");
                        break;
                    case WHITE_KING:
                        fen.append("K");
                        break;

                    case BLACK_PAWN:
                        fen.append("p");
                        break;
                    case BLACK_KNIGHT:
                        fen.append("n");
                        break;
                    case BLACK_BISHOP:
                        fen.append("b");
                        break;
                    case BLACK_ROOK:
                        fen.append("r");
                        break;
                    case BLACK_QUEEN:
                        fen.append("q");
                        break;
                    case BLACK_KING:
                        fen.append("k");
                        break;
                }
            }
            if (i % 8 == 0 && i != 0) {
                if (c > 0) {
                    fen.append(c);
                    c = 0;
                }
                fen.append("/");
            }
            if (i == 0 && c > 0) {
                fen.append(c);
                c = 0;
            }
        }

        fen.append(" ");
        if (turn == WHITE) {
            fen.append("w");
        } else {
            fen.append("b");
        }

        fen.append(" ");

        if (castlingRights == 0) {
            fen.append("-");
        } else {
            if ((castlingRights & castlingRightsOn[WHITE][K]) != 0) {
                fen.append("K");
            }

            if ((castlingRights & castlingRightsOn[WHITE][Q]) != 0) {
                fen.append("Q");
            }

            if ((castlingRights & castlingRightsOn[BLACK][K]) != 0) {
                fen.append("k");
            }

            if ((castlingRights & castlingRightsOn[BLACK][Q]) != 0) {
                fen.append("q");
            }
        }

        fen.append(" ");

        if (hasPreviousMove() && StackDataUtil.getSpecialMove(moveStackArrayPeek()) == ENPASSANTVICTIM) {
            long f = StackDataUtil.getEPMove(moveStackArrayPeek());
            fen.append((char) (f + 96));
        } else {
            fen.append("-");
        }

        fen.append(" ");
        fen.append(quietHalfMoveCounter);
        fen.append(" ");
        fen.append(fullMoveCounter);

        return fen.toString();
    }


}
