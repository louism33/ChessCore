package com.github.louism33.chesscore;

import org.junit.Assert;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.github.louism33.chesscore.BitOperations.newPieceOnSquare;
import static com.github.louism33.chesscore.BoardConstants.*;
import static com.github.louism33.chesscore.CheckHelper.*;
import static com.github.louism33.chesscore.MakeMoveRegular.makeRegularMove;
import static com.github.louism33.chesscore.MakeMoveRegular.whichIntPieceOnSquare;
import static com.github.louism33.chesscore.MakeMoveSpecial.*;
import static com.github.louism33.chesscore.MoveConstants.*;
import static com.github.louism33.chesscore.MoveMakingUtilities.togglePiecesFrom;
import static com.github.louism33.chesscore.MoveParser.*;
import static com.github.louism33.chesscore.StackDataUtil.*;

public class Chessboard implements Cloneable{

    long[][] pieces = new long[2][7];
    // consider int[64] for all pieces + location
    int turn;
    /*
    castling rights bits:
    BK BA WK WQ
     */
    int castlingRights = 0xf;

    private int fiftyMoveCounter = 0;
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
        castlingRights = 0xf;

        ZobristHashUtil.boardToHash(this);

        Setup.init(false);

        System.arraycopy(INITIAL_PIECES[BLACK], 0, this.pieces[BLACK], 0, INITIAL_PIECES[BLACK].length);
        System.arraycopy(INITIAL_PIECES[WHITE], 0, this.pieces[WHITE], 0, INITIAL_PIECES[WHITE].length);

        turn = WHITE;
    }

    /**
     * New Chessboard based on a FEN string
     * @param fen the String of pieces turn and castling rights and ep square and counters to make a board from
     */
    public Chessboard(String fen) {
        makeBoardBasedOnFENSpecific(fen);
        this.zobristHash = ZobristHashUtil.boardToHash(this);
        Setup.init(false);
    }

    Chessboard(boolean blank){
        this.zobristHash = ZobristHashUtil.boardToHash(this);
    }

    /**
     * Copy Constructor
     * @param board the chessboard you want an exact copy of
     */
    public Chessboard(Chessboard board) {
        System.arraycopy(board.pastMoveStackArray, 0, this.pastMoveStackArray, 0, board.pastMoveStackArray.length);
        System.arraycopy(board.zobristHashStack, 0, this.zobristHashStack, 0, board.zobristHashStack.length);

        System.arraycopy(board.checkStack, 0, this.checkStack, 0, board.checkStack.length);
        System.arraycopy(board.pinnedPiecesArray, 0, this.pinnedPiecesArray, 0, board.pinnedPiecesArray.length);


        this.inCheckRecorder = board.inCheckRecorder;
        this.pinnedPieces = board.pinnedPieces;

        this.moveStackIndex = board.moveStackIndex;
        this.masterIndex = board.masterIndex;

        System.arraycopy(board.pieces[WHITE], 0, this.pieces[WHITE], 0, 7);
        System.arraycopy(board.pieces[BLACK], 0, this.pieces[BLACK], 0, 7);


        this.castlingRights = board.castlingRights;
        this.turn = board.turn;

        this.zobristHash = board.zobristHash;
        Setup.init(false);
    }

    /**
     * @return a String representation of the current board.
     */
    public String getFenRepresentation(){
        return "not yet";
    }

    /**
     * legal chess move generation
     * @return an array of length 128 populated with fully legal chess moves, and 0s. 
     * Use @see com.github.louism33.chesscore.MoveParser.class for methods to interpret the move object
     */
    public int[] generateLegalMoves() {
        Arrays.fill(this.legalMoveStack[legalMoveStackIndex], 0);

        MoveGeneratorMaster.generateLegalMoves(this,
                this.legalMoveStack[legalMoveStackIndex], turn);

        return this.legalMoveStack[legalMoveStackIndex];
    }
    /**
     * Updates the board with the move you want.
     * @param move the non-0 move you want to make of this board.
     */
    public void makeMoveAndFlipTurn(int move){
        this.rotateMoveIndexUp();

        Assert.assertNotEquals(move, 0);

        masterStackPush();

        zobristHash = ZobristHashUtil.updateHashPreMove(this, zobristHash, move);

        if (move == 0){
            moveStackArrayPush(buildStackDataBetter(0, turn, getFiftyMoveCounter(), castlingRights, NULL_MOVE));
            return;
        }

        boolean resetFifty = true;

        if (MoveParser.isSpecialMove(move)) {
            switch (move & SPECIAL_MOVE_MASK) {
                case CASTLING_MASK:
                    moveStackArrayPush(buildStackDataBetter(move, turn, getFiftyMoveCounter(), castlingRights, CASTLING));
                    castlingRights = makeCastlingMove(castlingRights, pieces, move);
                    break;

                case ENPASSANT_MASK:
                    moveStackArrayPush(buildStackDataBetter(move, turn, getFiftyMoveCounter(), castlingRights, ENPASSANTCAPTURE));
                    makeEnPassantMove(pieces, turn, move);
                    break;

                case PROMOTION_MASK:
                    moveStackArrayPush(buildStackDataBetter(move, turn, getFiftyMoveCounter(), castlingRights, PROMOTION));
                    makePromotingMove(pieces, turn, move);
                    break;
            }
        }

        else {
            if (MoveParser.isCaptureMove(move)) {
                moveStackArrayPush(buildStackDataBetter(move, turn, getFiftyMoveCounter(), castlingRights, BASICCAPTURE));
            }

            else if (enPassantPossibility(turn, pieces[turn][PAWN], move)){
                int whichFile = 8 - MoveParser.getSourceIndex(move) % 8;
                moveStackArrayPush(buildStackDataBetter(move, turn, getFiftyMoveCounter(), castlingRights, ENPASSANTVICTIM, whichFile));
            }

            else {
                switch (Piece.pieceOnSquareInt(this, newPieceOnSquare(MoveParser.getSourceIndex(move)))){
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
            makeRegularMove(pieces, move);
        }

        // todo update unmake move to compensate
//        if (resetFifty) {
//            setFiftyMoveCounter(0);
//        }
//        else {
//            setFiftyMoveCounter(getFiftyMoveCounter() + 1);
//        }


        castleFlagManager(move);

        zobristHash = (ZobristHashUtil.updateHashPostMove(this, zobristHash, move));

        this.turn = 1 - this.turn;
    }

    void castleFlagManager (int move){
        // disable relevant castle flag whenever a piece moves into the relevant square.
        switch (MoveParser.getSourceIndex(move)) {
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
        switch (MoveParser.getDestinationIndex(move)) {
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

    private static boolean enPassantPossibility(int turn, long myPawns, int move){
        // determine if flag should be added to enable EP on next turn
        long sourceSquare = newPieceOnSquare(MoveParser.getSourceIndex(move));
        long destinationSquare = newPieceOnSquare(MoveParser.getDestinationIndex(move));
        long HOME_RANK = PENULTIMATE_RANKS[1 - turn];
        long enPassantPossibilityRank = ENPASSANT_RANK[turn];

        if ((sourceSquare & HOME_RANK) == 0){
            return false;
        }

        if ((sourceSquare & myPawns) == 0){
            return false;
        }
        return (destinationSquare & enPassantPossibilityRank) != 0;
    }

    private void rotateMoveIndexUp(){
        this.legalMoveStackIndex = (this.legalMoveStackIndex + 1 + this.maxDepthAndArrayLength) % this.maxDepthAndArrayLength;
    }


    private void rotateMoveIndexDown(){
        this.legalMoveStackIndex = (this.legalMoveStackIndex - 1 + this.maxDepthAndArrayLength) % this.maxDepthAndArrayLength;
    }
    /**
     * Completely undoes the last made move, and changes the side to play
     */
    public void unMakeMoveAndFlipTurn(){
        this.rotateMoveIndexDown();

        Assert.assertTrue(hasPreviousMove());

        masterStackPop();

        long pop = moveStackData;

        if (StackDataUtil.getMove(pop) == 0){
            turn = StackDataUtil.getTurn(pop);
            return;
        }

        int pieceToMoveBackIndex = getDestinationIndex(StackDataUtil.getMove(pop));
        int squareToMoveBackTo = getSourceIndex(StackDataUtil.getMove(pop));
        int basicReversedMove = buildBetterMove(pieceToMoveBackIndex, whichIntPieceOnSquare(this, newPieceOnSquare(pieceToMoveBackIndex)), squareToMoveBackTo, NO_PIECE);

        switch (StackDataUtil.getSpecialMove(pop)) {
            //double pawn push
            case ENPASSANTVICTIM:
            case BASICQUIETPUSH:
            case BASICLOUDPUSH:
                makeRegularMove(this, basicReversedMove);
                break;

            case BASICCAPTURE:
                makeRegularMove(this, basicReversedMove);
                int takenPiece = MoveParser.getVictimPieceInt(StackDataUtil.getMove(pop));
                if (MoveParser.getVictimPieceInt(StackDataUtil.getMove(pop)) != 0){
                    togglePiecesFrom(pieces, newPieceOnSquare(pieceToMoveBackIndex), takenPiece);
                }
                break;

            case ENPASSANTCAPTURE:
                makeRegularMove(this, basicReversedMove);
                if (StackDataUtil.getTurn(pop) == BLACK) {
                    togglePiecesFrom(pieces, newPieceOnSquare(pieceToMoveBackIndex - 8), BLACK_PAWN);
                }
                else {
                    togglePiecesFrom(pieces, newPieceOnSquare(pieceToMoveBackIndex + 8), WHITE_PAWN);
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
                        togglePiecesFrom(pieces, newKing, WHITE_KING);
                        togglePiecesFrom(pieces, newRook, WHITE_ROOK);
                        break;

                    default:
                        originalRook = newPieceOnSquare(pieceToMoveBackIndex == 57 ? 56 : 63);
                        newRook = newPieceOnSquare(pieceToMoveBackIndex == 57 ? pieceToMoveBackIndex + 1 : pieceToMoveBackIndex - 1);
                        togglePiecesFrom(pieces, newKing, BLACK_KING);
                        togglePiecesFrom(pieces, newRook, BLACK_ROOK);
                        break;
                }

                pieces[1 - StackDataUtil.getTurn(pop)][KING] |= originalKing;
                pieces[1 - StackDataUtil.getTurn(pop)][ROOK] |= originalRook;
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

                togglePiecesFrom(pieces, destinationSquare, 
                        StackDataUtil.getTurn(pop) == 1 ? WHITE_PAWN : BLACK_PAWN);
                int takenPiecePromotion = MoveParser.getVictimPieceInt(StackDataUtil.getMove(pop));
                if (takenPiecePromotion > 0){
                    togglePiecesFrom(pieces, sourceSquare, takenPiecePromotion);
                }
                break;
        }

        castlingRights = StackDataUtil.getCastlingRights(pop);
        turn = 1 - turn;
    }

    /**
     * Makes a null move on the board. Make sure to unmake it afterwards
     */
    public void makeNullMoveAndFlipTurn(){
        masterStackPush();

        if (hasPreviousMove()){
            zobristHash = (ZobristHashUtil.updateWithEPFlags(this, zobristHash));
        }

        moveStackArrayPush(buildStackDataBetter(0, turn, getFiftyMoveCounter(), castlingRights, NULL_MOVE));

        zobristHash = ZobristHashUtil.zobristFlipTurn(zobristHash);

        this.turn = 1 - this.turn;
    }


    /**
     * Unmakes a null move on the board.
     */
    public void unMakeNullMoveAndFlipTurn(){
        Assert.assertTrue(hasPreviousMove());
        masterStackPop();
        this.turn = 1 - this.turn;
    }

    public boolean isWhiteTurn() {
        return this.turn == WHITE;
    }

    /**
     * Tells you if the specified player is in check
     * @param white true if white to play
     * @return true if in check, otherwise false
     */
    public boolean inCheck(boolean white){
        long myKing, enemyPawns, enemyKnights, enemyBishops, enemyRooks, enemyQueen, enemyKing, enemies, friends;
        if (white){
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
    public boolean drawByRepetition (boolean white){
        return isDrawByRepetition(this);
    }

    /**
     * @param white the player
     * @return true if draw by repetition
     */
    private boolean drawByInsufficientMaterial (boolean white){
        return isDrawByInsufficientMaterial(this);
    }

    /**
     * @param white the player
     * @return true if this side does not have enough pieces to ever win the game
     */
    private boolean colourHasInsufficientMaterialToMate (boolean white){
        return CheckHelper.colourHasInsufficientMaterialToMate(this, white);
    }

    /**
     * @return true if in checkmate
     */
    public boolean inCheckmate(){
        if (!this.inCheck(isWhiteTurn())){
            return false;
        }
        return this.generateLegalMoves().length == 0;
    }

    /**
     * @return true if in stalemate
     */
    public boolean inStalemate(){
        if (this.inCheck(isWhiteTurn())){
            return false;
        }
        return this.generateLegalMoves().length == 0;
    }

    /**
     * Expensive operation to determine pinned pieces to king
     * @param white the player
     * @return a list of squares that have pinned pieces to the king on them
     */
    private List<Square> pinnedPiecesToKing(boolean white){
        long myKing = white ? pieces[WHITE][KING] : pieces[BLACK][KING];
        return pinnedPiecesToSquare(white, Square.getSquareOfBitboard(myKing));
    }

    /**
     * Expensive operation to determine pinned pieces to a particular square
     * @param white the player 
     * @param square the square you are interested in seeing the pins to
     * @return a list of squares that have pinned pieces on them
     */
    public List<Square> pinnedPiecesToSquare(boolean white, Square square){
        return Square.squaresFromBitboard(pinnedPiecesToSquareBitBoard(white, square));
    }

    /**
     * Expensive operation to determine pinned pieces to a particular square
     * @param white the player 
     * @param square the square you are interested in seeing the pins to
     * @return a list of squares that have pinned pieces on them
     */
    public long pinnedPiecesToSquareBitBoard(boolean white, Square square){

        long myPawns, myKnights, myBishops, myRooks, myQueen, myKing,
                enemyPawns, enemyKnights, enemyBishops, enemyRooks, enemyQueen, enemyKing,
                enemies, friends;
        if (isWhiteTurn()){
            myPawns = pieces[WHITE][PAWN];
            myKnights = pieces[WHITE][KNIGHT];
            myBishops = pieces[WHITE][BISHOP];
            myRooks = pieces[WHITE][ROOK];
            myQueen = pieces[WHITE][QUEEN];
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
            myPawns = pieces[BLACK][PAWN];
            myKnights = pieces[BLACK][KNIGHT];
            myBishops = pieces[BLACK][BISHOP];
            myRooks = pieces[BLACK][ROOK];
            myQueen = pieces[BLACK][QUEEN];
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

        return PinnedManager.whichPiecesArePinned(white ? pieces[WHITE][KING] : pieces[BLACK][KING],
                enemyBishops, enemyRooks, enemyQueen,
                friends, allPieces());
    }

    public boolean previousMoveWasPawnPushToSix(){
        if (!hasPreviousMove()){
            return false;
        }
        long peek = moveStackArrayPeek();
        return MoveParser.moveIsPawnPushSix(StackDataUtil.getMove(peek));
    }

    public boolean previousMoveWasPawnPushToSeven(){
        if (!hasPreviousMove()){
            return false;
        }
        long peek = moveStackArrayPeek();
        return MoveParser.moveIsPawnPushSeven(StackDataUtil.getMove(peek));
    }

    public boolean moveIsCaptureOfLastMovePiece(int move){
        if (!hasPreviousMove()){
            return false;
        }

        long peek = moveStackArrayPeek();
        if (StackDataUtil.getMove(peek) == 0){
            return false;
        }
        int previousMoveDestinationIndex = MoveParser.getDestinationIndex(StackDataUtil.getMove(peek));
        return (MoveParser.getDestinationIndex(move) == previousMoveDestinationIndex);
    }


    public long whitePieces(){
        this.pieces[WHITE][ALL_COLOUR_PIECES] = 0;
        for (int i = PAWN; i <= KING; i++) {
            this.pieces[WHITE][ALL_COLOUR_PIECES] |= this.pieces[WHITE][i];
        }
        return this.pieces[WHITE][ALL_COLOUR_PIECES];
    }

    public long getPieces(int turn){
        if (turn == WHITE) {
            return whitePieces();
        }
        else {
            return blackPieces();
        }
    }

    public long blackPieces(){
        this.pieces[BLACK][ALL_COLOUR_PIECES] = 0;
        for (int i = PAWN; i <= KING; i++) {
            this.pieces[BLACK][ALL_COLOUR_PIECES] |= this.pieces[BLACK][i];
        }
        return this.pieces[BLACK][ALL_COLOUR_PIECES];
    }

    public long allPieces(){
        return whitePieces() | blackPieces();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Chessboard that = (Chessboard) o;
        return this.turn == that.turn
                && this.castlingRights == that.castlingRights
                && Objects.equals(zobristHash, that.zobristHash)
                && Arrays.equals(zobristHashStack, that.zobristHashStack)
                && Arrays.equals(pinnedPiecesArray, that.pinnedPiecesArray)
                && Arrays.equals(checkStack, that.checkStack)
                ;
    }

    @Override
    public String toString() {
        String turn = isWhiteTurn() ? "It is white's turn." : "It is black's turn.";
        return "\n" + Art.boardArt(this) + "\n" + turn
                +"\n" + this.zobristHash
                ;
    }

    private int legalMoveStackIndex = 0;
    private int masterIndex = 0;
    private int moveStackIndex = 0;

    void masterStackPush() {
        checkStack[masterIndex] = this.inCheckRecorder;
        inCheckRecorder = false;

        pinnedPiecesArray[masterIndex] = this.pinnedPieces;
        pinnedPieces = 0;

        zobristHashStack[masterIndex] = zobristHash;
        masterIndex++;
    }

    void masterStackPop(){
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

    private void moveStackArrayPush(long l){
        pastMoveStackArray[moveStackIndex] = l;
        moveStackIndex++;
    }

    long moveStackArrayPeek(){
        return pastMoveStackArray[moveStackIndex -1];
    }

    boolean hasPreviousMove(){
        return moveStackIndex > 0 && pastMoveStackArray[moveStackIndex - 1] != 0;
    }

    private void makeBoardBasedOnFENSpecific(String fen){
        parseFenStringSpecific(fen);

        this.turn = isItWhitesTurnSpecific(fen);

        boolean[] castlingRights = castlingRightsSpecific(fen);

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                this.castlingRights &= castlingRightsMask[i][1-j];
                this.castlingRights |= castlingRights[i*2+j] ? castlingRightsOn[i][1-j] : 0;
            }
        }

        if (isEPFlagSetSpecific(fen)){
            epFlagSpecific(fen);
        }
    }


    private static boolean totalMovesSpecific(String fen){
        String boardPattern = " (.) (\\w+|-) (\\w+|-)";
        Pattern r = Pattern.compile(boardPattern);
        Matcher m = r.matcher(fen);

        String epFlags = "";

        if (m.find()){
            epFlags = m.group(3);
        }
        if (epFlags.length() == 0){
            throw new RuntimeException("Could not Parse board rep of fen string");
        }

        return !epFlags.equals("-");
    }


    private static boolean fiftyMovesSpecific(String fen){
        String boardPattern = " (.) (\\w+|-) (\\w+|-)";
        Pattern r = Pattern.compile(boardPattern);
        Matcher m = r.matcher(fen);

        String epFlags = "";

        if (m.find()){
            epFlags = m.group(3);
        }
        if (epFlags.length() == 0){
            throw new RuntimeException("Could not Parse board rep of fen string");
        }

        return !epFlags.equals("-");
    }

    private void epFlagSpecific(String fen){
        String boardPattern = " (.) (\\w+|-) (\\w|-)";
        Pattern r = Pattern.compile(boardPattern);
        Matcher m = r.matcher(fen);

        String epFlags = "";

        if (m.find()){
            epFlags = m.group(3);
        }
        if (epFlags.length() == 0){
            throw new RuntimeException("Could not Parse board rep of fen string");
        }

        int epIndex = (int) epFlags.charAt(0);
        if (epIndex < 97 || epIndex > 104) {
            return;
        }
        int epFlag = epIndex - 96;

        final long item = buildStackDataBetter(0, turn, fiftyMoveCounter, castlingRights, ENPASSANTVICTIM, epFlag);
        moveStackArrayPush(item);
    }

    private static boolean isEPFlagSetSpecific(String fen){
        String boardPattern = " (.) (\\w+|-) (\\w+|-)";
        Pattern r = Pattern.compile(boardPattern);
        Matcher m = r.matcher(fen);

        String epFlags = "";

        if (m.find()){
            epFlags = m.group(3);
        }
        if (epFlags.length() == 0){
            throw new RuntimeException("Could not Parse board rep of fen string");
        }

        return !epFlags.equals("-");
    }

    private static boolean[] castlingRightsSpecific(String fen){
        boolean[] castlingRights = {
                false, false, false, false,
        };
        String boardPattern = " (.) (\\w+|-)";
        Pattern r = Pattern.compile(boardPattern);
        Matcher m = r.matcher(fen);
        String castleString = "";
        if (m.find()){
            castleString = m.group(2);
        }
        if (castleString.length() == 0){
            throw new RuntimeException("Could not Parse board rep of fen string");
        }

        if (castleString.equals("-")){
            return castlingRights;
        }

        if (castleString.contains("K")){
            castlingRights[0] = true;
        }
        if (castleString.contains("Q")){
            castlingRights[1] = true;
        }
        if (castleString.contains("k")){
            castlingRights[2] = true;
        }
        if (castleString.contains("q")){
            castlingRights[3] = true;
        }

        return castlingRights;
    }

    private int isItWhitesTurnSpecific(String fen){
        String boardPattern = " (.)";
        Pattern r = Pattern.compile(boardPattern);
        Matcher m = r.matcher(fen);
        String player = "";
        if (m.find()){
            player = m.group(1);
        }
        if (player.length() == 0){
            throw new RuntimeException("Could not Parse board rep of fen string");
        }
        return player.equals("w") ? WHITE : BLACK;
    }

    private void parseFenStringSpecific (String fen){
        String boardRepresentation = boardRepSpecific(fen);
        int length = boardRepresentation.length();
        int index = -1;
        int square = 63;
        while (true){
            index++;
            if (index >= length){
                break;
            }
            if (square < 0){
                break;
            }
            String entry = Character.toString(boardRepresentation.charAt(index));
            if (entry.equals("/")){
                continue;
            }
            try {
                int i = Integer.parseInt(entry);
                square -= (i);
                continue;
            }
            catch (NumberFormatException ignored){
            }
            long pieceFromFen = BitOperations.newPieceOnSquare(square);
            square--;
            int whichPiece = 0;
            switch (entry) {
                case "P":
                    whichPiece = WHITE_PAWN;
                    break;
                case "N":
                    whichPiece = WHITE_KNIGHT;
                    break;
                case "B":
                    whichPiece = WHITE_BISHOP;
                    break;
                case "R":
                    whichPiece = WHITE_ROOK;
                    break;
                case "Q":
                    whichPiece = WHITE_QUEEN;
                    break;
                case "K":
                    whichPiece = WHITE_KING;
                    break;

                case "p":
                    whichPiece = BLACK_PAWN;
                    break;
                case "n":
                    whichPiece = BLACK_KNIGHT;
                    break;
                case "b":
                    whichPiece = BLACK_BISHOP;
                    break;
                case "r":
                    whichPiece = BLACK_ROOK;
                    break;
                case "q":
                    whichPiece = BLACK_QUEEN;
                    break;
                case "k":
                    whichPiece = BLACK_KING;
                    break;
                default:
                    throw new RuntimeException("Could not parse fen string");
            }
            this.pieces[whichPiece / 7][whichPiece < 7 ? whichPiece : whichPiece - 6] |= pieceFromFen;
        }
    }

    private static String boardRepSpecific(String fen){
        String boardPattern = "^[\\w*/]*";
        Pattern r = Pattern.compile(boardPattern);
        Matcher m = r.matcher(fen);
        String boardRepresentation = "";
        if (m.find()){
            boardRepresentation = m.group();
        }
        if (boardRepresentation.length() == 0){
            throw new RuntimeException("Could not Parse board rep of fen string");
        }

        return boardRepresentation;
    }
}
