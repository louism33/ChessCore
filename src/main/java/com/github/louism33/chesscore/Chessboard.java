package com.github.louism33.chesscore;

import org.junit.Assert;

import java.util.Arrays;

import static com.github.louism33.chesscore.BitOperations.getIndexOfFirstPiece;
import static com.github.louism33.chesscore.BitOperations.newPieceOnSquare;
import static com.github.louism33.chesscore.BoardConstants.*;
import static com.github.louism33.chesscore.CheckHelper.*;
import static com.github.louism33.chesscore.MakeMoveSpecial.*;
import static com.github.louism33.chesscore.MoveConstants.*;
import static com.github.louism33.chesscore.MoveMakingUtilities.removePieces;
import static com.github.louism33.chesscore.MoveMakingUtilities.togglePiecesFrom;
import static com.github.louism33.chesscore.MoveParser.*;
import static com.github.louism33.chesscore.StackDataUtil.*;
import static com.github.louism33.chesscore.ZobristHashUtil.*;

public class Chessboard {

    long[][] pieces = new long[2][7];
    
    int[] pieceSquareTable = new int[64];
    int turn;
    /*
    castling rights bits:
    BK BA WK WQ
     */
    int castlingRights = 0xf;

    private int fiftyMoveCounter = 0, fullMoveCounter = 0;

    private void updateHashPreMove(int move){
        int sourceSquare = getSourceIndex(move);
        int destinationSquareIndex = getDestinationIndex(move);
        int sourcePieceIdentifier = pieceSquareTable[sourceSquare] - 1;

        zobristHash ^= zobristHashPieces[sourceSquare][sourcePieceIdentifier];
        long destinationZH = zobristHashPieces[destinationSquareIndex][sourcePieceIdentifier];

        zobristHash ^= destinationZH;

        /*
        captures
         */
        if (isCaptureMove(move)){
            int destinationPieceIdentifier = pieceSquareTable[destinationSquareIndex] - 1;
            /*
            remove taken piece from hash
            */
            long victimZH = zobristHashPieces[destinationSquareIndex][destinationPieceIdentifier];
            zobristHash ^= victimZH;
        }

        /* 
        "positive" EP flag is set in updateHashPostMove, in updateHashPreMove we cancel a previous EP flag
        */
        if (hasPreviousMove()){
            zobristHash = updateWithEPFlags(moveStackArrayPeek(), zobristHash);
        }

        long destinationPiece = newPieceOnSquare(getDestinationIndex(move));

        if (isSpecialMove(move)){
            if (isCastlingMove(move)) {
                int originalRookIndex = 0;
                int newRookIndex = 0;
                switch (getDestinationIndex(move)) {
                    case 1:
                        originalRookIndex = 0;
                        newRookIndex = getDestinationIndex(move) + 1;
                        break;
                    case 5:
                        originalRookIndex = 7;
                        newRookIndex = getDestinationIndex(move) - 1;
                        break;
                    case 57:
                        originalRookIndex = 56;
                        newRookIndex = getDestinationIndex(move) + 1;
                        break;
                    case 61:
                        originalRookIndex = 63;
                        newRookIndex = getDestinationIndex(move) - 1;
                        break;
                }

                int myRook = pieceSquareTable[originalRookIndex] - 1;
                zobristHash ^= zobristHashPieces[originalRookIndex][myRook];
                zobristHash ^= zobristHashPieces[newRookIndex][myRook];
            }

            else if (isEnPassantMove(move)){
                long victimPawn = turn == WHITE ? destinationPiece >>> 8 : destinationPiece << 8;
                zobristHash ^= zobristHashPieces
                        [BitOperations.getIndexOfFirstPiece(victimPawn)]
                        [pieceSquareTable[getIndexOfFirstPiece(victimPawn)] - 1];
            }

            else if (isPromotionMove(move)){
                int whichPromotingPiece = 0;

                switch (move & WHICH_PROMOTION){
                    case KNIGHT_PROMOTION_MASK:
                        whichPromotingPiece = 2 + (turn) * 6;
                        break;
                    case BISHOP_PROMOTION_MASK:
                        whichPromotingPiece = 3 + (turn) * 6;
                        break;
                    case ROOK_PROMOTION_MASK:
                        whichPromotingPiece = 4 + (turn) * 6;
                        break;
                    case QUEEN_PROMOTION_MASK:
                        whichPromotingPiece = 5 + (turn) * 6;
                        break;
                }

                /*
                remove my pawn from zh
                 */
                zobristHash ^= destinationZH;

                Assert.assertTrue(whichPromotingPiece != 0);
                long promotionZH = zobristHashPieces[destinationSquareIndex][whichPromotingPiece - 1];
                zobristHash ^= promotionZH;
            }
        }

    }

    private long boardToHash(){
        long hash = 0;
        for (int sq = 0; sq < 64; sq++) {
            long pieceOnSquare = newPieceOnSquare(sq);
            int pieceIndex = pieceSquareTable[getIndexOfFirstPiece(pieceOnSquare)] - 1;
            if (pieceIndex != -1) {
                hash ^= zobristHashPieces[sq][pieceIndex];
            }
        }

        hash ^= zobristHashCastlingRights[castlingRights];

        if (!isWhiteTurn()){
            hash = zobristFlipTurn(hash);
        }

        if (hasPreviousMove()){
            hash = updateWithEPFlags(moveStackArrayPeek(), hash);
        }

        return hash;
    }

    public int getFiftyMoveCounter() {
        return fiftyMoveCounter;
    }

    long zobristHash;
    long moveStackData;

    private final int maxDepthAndArrayLength = 64;
    private final int maxNumberOfMovesInAnyPosition = 128;

    int[] moves = new int[maxNumberOfMovesInAnyPosition];

    private int[][] legalMoveStack = new int[maxDepthAndArrayLength][maxNumberOfMovesInAnyPosition];

    long[] zobristHashStack = new long[maxDepthAndArrayLength];
    long[] pastMoveStackArray = new long[maxDepthAndArrayLength];

    public boolean inCheckRecorder;
    public long pinnedPieces;
    public long[] pinnedPiecesArray = new long[maxDepthAndArrayLength];
    public boolean[] checkStack = new boolean[maxDepthAndArrayLength];


    /**
     * A new Chessboard in the starting position, white to play.
     */
    public Chessboard() {
        boardToHash();
        Setup.init(false);

        System.arraycopy(INITIAL_PIECES[BLACK], 0, this.pieces[BLACK], 0, INITIAL_PIECES[BLACK].length);
        System.arraycopy(INITIAL_PIECES[WHITE], 0, this.pieces[WHITE], 0, INITIAL_PIECES[WHITE].length);

        System.arraycopy(INITIAL_PIECE_SQUARES, 0, pieceSquareTable, 0, pieceSquareTable.length);

        turn = WHITE;
    }

    /**
     * Copy Constructor
     * @param board the chessboard you want an exact copy of
     */

    public Chessboard(Chessboard board) {
        this.turn = board.turn;
        this.castlingRights = board.castlingRights;
        this.fiftyMoveCounter = board.fiftyMoveCounter;
        this.zobristHash = board.zobristHash;
        this.moveStackData = board.moveStackData;
        this.inCheckRecorder = board.inCheckRecorder;
        this.pinnedPieces = board.pinnedPieces;
        this.legalMoveStackIndex = board.legalMoveStackIndex;
        this.masterIndex = board.masterIndex;
        this.moveStackIndex = board.moveStackIndex;
        System.arraycopy(board.pieces[WHITE], 0, this.pieces[WHITE], 0, 7);
        System.arraycopy(board.pieces[BLACK], 0, this.pieces[BLACK], 0, 7);
        System.arraycopy(board.moves, 0, this.moves, 0, board.moves.length);

        for (int i = 0; i < board.legalMoveStack.length; i++) {
            System.arraycopy(board.legalMoveStack[i], 0, this.legalMoveStack[i], 0, board.legalMoveStack[i].length);
        }

        System.arraycopy(board.zobristHashStack, 0, this.zobristHashStack, 0, board.zobristHashStack.length);
        System.arraycopy(board.pastMoveStackArray, 0, this.pastMoveStackArray, 0, board.pastMoveStackArray.length);
        System.arraycopy(board.pinnedPiecesArray, 0, this.pinnedPiecesArray, 0, board.pinnedPiecesArray.length);
        System.arraycopy(board.checkStack, 0, this.checkStack, 0, board.checkStack.length);

        System.arraycopy(board.pieceSquareTable, 0, pieceSquareTable, 0, board.pieceSquareTable.length);

        Setup.init(false);
    }

    /**
     * @return a String representation of the current board.
     */
    public String getFenRepresentation() {
        return "not yet";
    }

    /**
     * legal chess move generation
     * @return an array of length 128 populated with fully legal chess moves, and 0s.
     * Use @see com.github.louism33.chesscore.MoveParser.class for methods to interpret the move object
     */
    public int[] generateLegalMoves() {
        Arrays.fill(this.legalMoveStack[legalMoveStackIndex], 0);

        MoveGeneratorMaster.generateLegalMoves(this, turn,
                pieces, castlingRights, pieceSquareTable, hasPreviousMove(), moveStackArrayPeek(),
                this.legalMoveStack[legalMoveStackIndex]);

        return this.legalMoveStack[legalMoveStackIndex];
    }

    /**
     * Updates the board with the move you want.
     * @param move the non-0 move you want to make of this board.
     */
    public void makeMoveAndFlipTurnBetter(int move) {
        this.rotateMoveIndexUp();
        Assert.assertNotEquals(move, 0);
        masterStackPush();

        int sourceSquare = getSourceIndex(move);
        int destinationIndex = getDestinationIndex(move);
        int sourcePieceIdentifier = pieceSquareTable[sourceSquare] - 1;
        boolean captureMove = isCaptureMove(move);
        long destinationPiece = newPieceOnSquare(destinationIndex);
        long destinationZH = zobristHashPieces[destinationIndex][sourcePieceIdentifier];

        zobristHash ^= zobristHashPieces[sourceSquare][sourcePieceIdentifier];
        zobristHash ^= destinationZH;

        if (captureMove){
            zobristHash ^= zobristHashPieces[destinationIndex][pieceSquareTable[destinationIndex] - 1];
        }

        /* 
        "positive" EP flag is set in updateHashPostMove, in updateHashPreMove we cancel a previous EP flag
        */
        if (hasPreviousMove()){
            zobristHash = updateWithEPFlags(moveStackArrayPeek(), zobristHash);
        }

        if (move == 0) {
            moveStackArrayPush(buildStackDataBetter(0, turn, getFiftyMoveCounter(), castlingRights, NULL_MOVE));
            return;
        }

        boolean resetFifty = true;

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

                    int myRook = pieceSquareTable[originalRookIndex] - 1;
                    zobristHash ^= zobristHashPieces[originalRookIndex][myRook];
                    zobristHash ^= zobristHashPieces[newRookIndex][myRook];
                    
                    moveStackArrayPush(buildStackDataBetter(move, turn, getFiftyMoveCounter(), castlingRights, CASTLING));
                    castlingRights = makeCastlingMove(castlingRights, pieces, pieceSquareTable, move);
                    break;

                case ENPASSANT_MASK:
                    long victimPawn = turn == WHITE ? destinationPiece >>> 8 : destinationPiece << 8;
                    zobristHash ^= zobristHashPieces
                            [BitOperations.getIndexOfFirstPiece(victimPawn)]
                            [pieceSquareTable[getIndexOfFirstPiece(victimPawn)] - 1];
                    
                    moveStackArrayPush(buildStackDataBetter(move, turn, getFiftyMoveCounter(), castlingRights, ENPASSANTCAPTURE));
                    makeEnPassantMove(pieces, pieceSquareTable, turn, move);
                    break;

                case PROMOTION_MASK:
                    int whichPromotingPiece = 0;

                    switch (move & WHICH_PROMOTION){
                        case KNIGHT_PROMOTION_MASK:
                            whichPromotingPiece = 2 + (turn) * 6;
                            break;
                        case BISHOP_PROMOTION_MASK:
                            whichPromotingPiece = 3 + (turn) * 6;
                            break;
                        case ROOK_PROMOTION_MASK:
                            whichPromotingPiece = 4 + (turn) * 6;
                            break;
                        case QUEEN_PROMOTION_MASK:
                            whichPromotingPiece = 5 + (turn) * 6;
                            break;
                    }

                /*
                remove my pawn from zh
                 */
                    zobristHash ^= destinationZH;

                    Assert.assertTrue(whichPromotingPiece != 0);
                    long promotionZH = zobristHashPieces[destinationIndex][whichPromotingPiece - 1];
                    zobristHash ^= promotionZH;
                    
                    moveStackArrayPush(buildStackDataBetter(move, turn, getFiftyMoveCounter(), castlingRights, PROMOTION));
                    makePromotingMove(pieces, pieceSquareTable, turn, move);
                    break;
            }
        } else {
            if (captureMove) {
                moveStackArrayPush(buildStackDataBetter(move, turn, getFiftyMoveCounter(), castlingRights, BASICCAPTURE));
            } else if (enPassantPossibility(turn, pieces[turn][PAWN], move)) {
                int whichFile = 8 - getSourceIndex(move) % 8;
                moveStackArrayPush(buildStackDataBetter(move, turn, getFiftyMoveCounter(), castlingRights, ENPASSANTVICTIM, whichFile));
            } else {
                switch (pieceSquareTable[getSourceIndex(move)]) {
                    case WHITE_PAWN: // white pawn
                    case BLACK_PAWN: // black pawn
                        moveStackArrayPush(buildStackDataBetter(move, turn, getFiftyMoveCounter(), castlingRights, BASICLOUDPUSH));
                        break;
                    default:
                        // increment 50 move rule
                        resetFifty = false;
                        moveStackArrayPush(buildStackDataBetter(move, turn, getFiftyMoveCounter(), castlingRights, BASICQUIETPUSH));
                }

            }

            makeRegularMove(pieces, pieceSquareTable, move);
        }

        // todo update unmake move to compensate
//        if (resetFifty) {
//            setFiftyMoveCounter(0);
//        }
//        else {
//            setFiftyMoveCounter(getFiftyMoveCounter() + 1);
//        }


        castleFlagManager(move);

        Assert.assertTrue(hasPreviousMove());
        zobristHash = (updateHashPostMove(moveStackArrayPeek(), castlingRights, zobristHash));

        this.turn = 1 - this.turn;
    }

    private void castleFlagManager(int move) {
        // disable relevant castle flag whenever a piece moves into the relevant square.
        switch (getSourceIndex(move)) {
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
        switch (getDestinationIndex(move)) {
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

    private static boolean enPassantPossibility(int turn, long myPawns, int move) {
        // determine if flag should be added to enable EP on next turn
        long sourceSquare = newPieceOnSquare(getSourceIndex(move));
        long destinationSquare = newPieceOnSquare(getDestinationIndex(move));
        long HOME_RANK = PENULTIMATE_RANKS[1 - turn];
        long enPassantPossibilityRank = ENPASSANT_RANK[turn];

        if ((sourceSquare & HOME_RANK) == 0) {
            return false;
        }

        if ((sourceSquare & myPawns) == 0) {
            return false;
        }
        return (destinationSquare & enPassantPossibilityRank) != 0;
    }

    private void rotateMoveIndexUp() {
        this.legalMoveStackIndex = (this.legalMoveStackIndex + 1 + this.maxDepthAndArrayLength) % this.maxDepthAndArrayLength;
    }


    private void rotateMoveIndexDown() {
        this.legalMoveStackIndex = (this.legalMoveStackIndex - 1 + this.maxDepthAndArrayLength) % this.maxDepthAndArrayLength;
    }

    /**
     * Completely undoes the last made move, and changes the side to play
     */
    public void unMakeMoveAndFlipTurn() {
        this.rotateMoveIndexDown();

        Assert.assertTrue(hasPreviousMove());

        masterStackPop();

        long pop = moveStackData;

        if (StackDataUtil.getMove(pop) == 0) {
            turn = StackDataUtil.getTurn(pop);
            return;
        }

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

                switch (StackDataUtil.getTurn(pop)) {
                    case BLACK:
                        originalRook = newPieceOnSquare(pieceToMoveBackIndex == 1 ? 0 : 7);
                        newRook = newPieceOnSquare(pieceToMoveBackIndex == 1 ? pieceToMoveBackIndex + 1 : pieceToMoveBackIndex - 1);
                        togglePiecesFrom(pieces, pieceSquareTable, newKing, WHITE_KING);
                        togglePiecesFrom(pieces, pieceSquareTable, newRook, WHITE_ROOK);
                        break;

                    default:
                        originalRook = newPieceOnSquare(pieceToMoveBackIndex == 57 ? 56 : 63);
                        newRook = newPieceOnSquare(pieceToMoveBackIndex == 57 ? pieceToMoveBackIndex + 1 : pieceToMoveBackIndex - 1);
                        togglePiecesFrom(pieces, pieceSquareTable, newKing, BLACK_KING);
                        togglePiecesFrom(pieces, pieceSquareTable, newRook, BLACK_ROOK);
                        break;
                }

                pieces[1 - StackDataUtil.getTurn(pop)][KING] |= originalKing;
                pieces[1 - StackDataUtil.getTurn(pop)][ROOK] |= originalRook;

                pieceSquareTable[squareToMoveBackTo] = WHITE_KING + (1 - StackDataUtil.getTurn(pop)) * 6;
                pieceSquareTable[getIndexOfFirstPiece(originalRook)] = WHITE_ROOK + (1 - StackDataUtil.getTurn(pop)) * 6;
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

    static void makeRegularMove(long[][] pieces, int[] pieceSquareTable, int move) {
        final long destinationPiece = newPieceOnSquare(getDestinationIndex(move));
        removePieces(pieces, pieceSquareTable, newPieceOnSquare(getSourceIndex(move)), destinationPiece, move);
        togglePiecesFrom(pieces, pieceSquareTable, destinationPiece, getMovingPieceInt(move));
    }

    /**
     * Makes a null move on the board. Make sure to unmake it afterwards
     */
    public void makeNullMoveAndFlipTurn() {
        masterStackPush();

        if (hasPreviousMove()) {
            zobristHash = (updateWithEPFlags(moveStackArrayPeek(), zobristHash));
        }

        moveStackArrayPush(buildStackDataBetter(0, turn, getFiftyMoveCounter(), castlingRights, NULL_MOVE));

        zobristHash = zobristFlipTurn(zobristHash);

        this.turn = 1 - this.turn;
    }


    /**
     * Unmakes a null move on the board.
     */
    public void unMakeNullMoveAndFlipTurn() {
        Assert.assertTrue(hasPreviousMove());
        masterStackPop();
        this.turn = 1 - this.turn;
    }

    public boolean isWhiteTurn() {
        return this.turn == WHITE;
    }

    /**
     * Tells you if the specified player is in check
     *
     * @param white true if white to play
     * @return true if in check, otherwise false
     */
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

        return boardInCheck(white, myKing,
                enemyPawns, enemyKnights, enemyBishops, enemyRooks, enemyQueen, enemyKing,
                allPieces());

    }

    /**
     * @param white the player
     * @return true if it is a draw by repetition
     */
    public boolean drawByRepetition(boolean white) {
        return isDrawByRepetition(this);
    }

    /**
     * @param white the player
     * @return true if draw by repetition
     */
    private boolean drawByInsufficientMaterial(boolean white) {
        return isDrawByInsufficientMaterial(this);
    }

    /**
     * @param white the player
     * @return true if this side does not have enough pieces to ever win the game
     */
    private boolean colourHasInsufficientMaterialToMate(boolean white) {
        return CheckHelper.colourHasInsufficientMaterialToMate(this, white);
    }

    /**
     * @return true if in checkmate
     */
    public boolean inCheckmate() {
        if (!this.inCheck(isWhiteTurn())) {
            return false;
        }
        return this.generateLegalMoves().length == 0;
    }

    /**
     * @return true if in stalemate
     */
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
        return moveIsPawnPushSix(StackDataUtil.getMove(peek));
    }

    public boolean previousMoveWasPawnPushToSeven() {
        if (!hasPreviousMove()) {
            return false;
        }
        long peek = moveStackArrayPeek();
        return moveIsPawnPushSeven(StackDataUtil.getMove(peek));
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


    public long whitePieces() {
        this.pieces[WHITE][ALL_COLOUR_PIECES] = 0;
        for (int i = PAWN; i <= KING; i++) {
            this.pieces[WHITE][ALL_COLOUR_PIECES] |= this.pieces[WHITE][i];
        }
        return this.pieces[WHITE][ALL_COLOUR_PIECES];
    }

    public long getPieces(int turn) {
        if (turn == WHITE) {
            return whitePieces();
        } else {
            return blackPieces();
        }
    }

    public long blackPieces() {
        this.pieces[BLACK][ALL_COLOUR_PIECES] = 0;
        for (int i = PAWN; i <= KING; i++) {
            this.pieces[BLACK][ALL_COLOUR_PIECES] |= this.pieces[BLACK][i];
        }
        return this.pieces[BLACK][ALL_COLOUR_PIECES];
    }

    public long allPieces() {
        return whitePieces() | blackPieces();
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
                castlingRights == that.castlingRights &&
                fiftyMoveCounter == that.fiftyMoveCounter &&
                zobristHash == that.zobristHash &&
                masterIndex == that.masterIndex &&
                Arrays.deepEquals(pieces, that.pieces) &&
                Arrays.equals(zobristHashStack, that.zobristHashStack) &&
                Arrays.equals(pinnedPiecesArray, that.pinnedPiecesArray) &&
                Arrays.equals(checkStack, that.checkStack) &&
                Arrays.equals(pieceSquareTable, that.pieceSquareTable)
                ;
    }

    @Override
    public String toString() {
        String turn = isWhiteTurn() ? "It is white's turn." : "It is black's turn.";
        return "\n" + Art.boardArt(this) + "\n" + turn
                ;
    }

    private int legalMoveStackIndex = 0;
    private int masterIndex = 0;
    private int moveStackIndex = 0;

    private void masterStackPush() {
        checkStack[masterIndex] = this.inCheckRecorder;
        inCheckRecorder = false;

        pinnedPiecesArray[masterIndex] = this.pinnedPieces;
        pinnedPieces = 0;

        zobristHashStack[masterIndex] = zobristHash;
        masterIndex++;
    }

    private void masterStackPop() {
        masterIndex--;
        inCheckRecorder = checkStack[masterIndex];
        checkStack[masterIndex] = false;

        pinnedPieces = pinnedPiecesArray[masterIndex];
        pinnedPiecesArray[masterIndex] = 0;

        zobristHash = zobristHashStack[masterIndex];
        zobristHashStack[masterIndex] = 0;

        pastMoveStackArray[moveStackIndex] = 0;
        moveStackIndex--;
        moveStackData = pastMoveStackArray[moveStackIndex];
    }

    private void moveStackArrayPush(long l) {
        pastMoveStackArray[moveStackIndex] = l;
        moveStackIndex++;
    }

    long moveStackArrayPeek() {
        return moveStackIndex > 0 ? pastMoveStackArray[moveStackIndex - 1] : 0;
    }

    boolean hasPreviousMove() {
        return moveStackIndex > 0 && pastMoveStackArray[moveStackIndex - 1] != 0;
    }


    /**
     * New Chessboard based on a FEN string
     *
     * @param fen the String of pieces turn and castling rights and ep square and counters to make a board from
     */
    public Chessboard(String fen) {
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
                    pieceSquareTable[square] = whichPiece;

                    break;

                case 2: //player
                    if (c[i] == 'b') {
                        turn = BLACK;
                    }
                    else{
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
                    final long item = buildStackDataBetter(0, turn, fiftyMoveCounter,
                            castlingRights, ENPASSANTVICTIM, (int) c[i] - 96);
                    moveStackArrayPush(item);
                    phase++;
                    break;

                case 5:
                    fiftyMoveCounter = c[i];
                    phase++;
                    break;

                case 6:
                    fullMoveCounter = c[i];
                    phase++;
                    break;
            }
        }
        zobristHash = boardToHash();
        Setup.init(false);
    }


}
