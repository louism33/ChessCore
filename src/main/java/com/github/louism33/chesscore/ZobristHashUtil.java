package com.github.louism33.chesscore;

import org.junit.Assert;

import java.util.Arrays;
import java.util.Random;
import java.util.Stack;

import static com.github.louism33.chesscore.BitOperations.newPieceOnSquare;
import static com.github.louism33.chesscore.BitboardResources.INITIAL_BLACK_KING;
import static com.github.louism33.chesscore.BitboardResources.INITIAL_WHITE_KING;
import static com.github.louism33.chesscore.MakeMoveRegular.whichIntPieceOnSquare;
import static com.github.louism33.chesscore.MakeMoveRegular.whichPieceOnSquare;
import static com.github.louism33.chesscore.StackDataUtil.SpecialMove.ENPASSANTVICTIM;
import static com.github.louism33.chesscore.StackDataUtil.SpecialMove.NULL_MOVE;

class ZobristHashUtil {

    private static final long initHashSeed = 100;
    private static final long[][] zobristHashPieces = initPieceHash();
    private static final long[] zobristHashCastlingRights = initCastlingHash();
    private static final long[] zobristHashEPFiles = initEPHash();
    private static final long zobristHashColourBlack = initColourHash();

    static long zobristFlipTurn(long hash){
        return hash ^ zobristHashColourBlack;
    }

    static long updateWithEPFlags(Chessboard board, long hash){
        Assert.assertTrue(board.moveStack.size() > 0);

        Assert.assertTrue(board.hasPreviousMove());

        Long peek = board.moveStack.peek();
        long peekArray = board.moveStackArrayPeek();

        int epMove = StackDataUtil.getEPMove(peek);

        Assert.assertEquals(peek.longValue(), peekArray);

        if (StackDataUtil.SpecialMove.values()[StackDataUtil.getSpecialMove(peek)] == ENPASSANTVICTIM) {
            hash = hashEP(hash, peek);
        }

        if (StackDataUtil.SpecialMove.values()[StackDataUtil.getSpecialMove(peek)] == NULL_MOVE && board.moveStack.size() > 1){
            hash = nullMoveEP(board, hash);
        }

        if (StackDataUtil.SpecialMove.values()[StackDataUtil.getSpecialMove(peek)] == NULL_MOVE && board.hasPreviousMove()){
//            hash = nullMoveEP(board, hash);
        }

        return hash;
    }

    private static long nullMoveEP(Chessboard board, long hash) {
        Long pop = board.moveStack.pop();
        Long peekSecondElement = board.moveStack.peek();
        if (StackDataUtil.SpecialMove.values()[StackDataUtil.getSpecialMove(peekSecondElement)] == ENPASSANTVICTIM) {
            // file one = FILE_A
            hash = hashEP(hash, peekSecondElement);
        }

        board.moveStack.add(pop);

        long peekSecondElementArray = board.moveStackArrayPeek();
        if (StackDataUtil.SpecialMove.values()[StackDataUtil.getSpecialMove(peekSecondElementArray)] == ENPASSANTVICTIM) {
            // file one = FILE_A
//            hash = hashEP(hash, peekSecondElement);
        }



        return hash;
    }

    private static long hashEP(long hash, long peek) {
        return hash ^ zobristHashEPFiles[StackDataUtil.getEPMove(peek) - 1];
    }

    static long updateHashPostMove(Chessboard board, long boardHash, int move){
        /*
        invert colour
        */
        boardHash = zobristFlipTurn(boardHash);

        Assert.assertTrue(board.moveStack.size() > 0);
        Assert.assertTrue(board.hasPreviousMove());
        
        /*
        if move we just made raised EP flag, update hash
        */
        boardHash = updateWithEPFlags(board, boardHash);

        /*
        if castling rights changed, update hash
        */
        boardHash ^= postMoveCastlingRights(board);

        return boardHash;
    }

    private static long postMoveCastlingRights(Chessboard board){
        long updatedHashValue = 0;
        final long peek = board.moveStack.peek();
        final long peekArray = board.moveStackArrayPeek();

        int castlingRights = StackDataUtil.getCastlingRights(peek);
        
        /*
        undo previous castling rights
        */
        updatedHashValue ^= zobristHashCastlingRights[castlingRights];
        
        /*
        update with new castling rights
        */
        int numTo15Do = 0;
        if (board.isWhiteCanCastleK()){
            numTo15Do  += 1;
        }

        if (board.isWhiteCanCastleQ()){
            numTo15Do  += 2;
        }

        if (board.isBlackCanCastleK()){
            numTo15Do  += 4;
        }

        if (board.isBlackCanCastleQ()){
            numTo15Do  += 8;
        }

        updatedHashValue ^= zobristHashCastlingRights[numTo15Do];

        return updatedHashValue;
    }


    static long updateHashPreMove(Chessboard board, long boardHash, int move){

        int sourceSquare = MoveParser.getSourceIndex(move);
        int destinationSquareIndex = MoveParser.getDestinationIndex(move);

        long sourcePiece = newPieceOnSquare(sourceSquare);
        int sourcePieceIdentifier = whichIntPieceOnSquare(board, sourcePiece) - 1;

        final Piece piece = whichPieceOnSquare(board, sourcePiece);

        long sourceZH = zobristHashPieces[sourceSquare][sourcePieceIdentifier];

        long destinationSquare = newPieceOnSquare(destinationSquareIndex);
        long destinationZH = zobristHashPieces[destinationSquareIndex][sourcePieceIdentifier];

        boardHash ^= sourceZH;
        boardHash ^= destinationZH;
        
        /*
        captures
         */
        if ((destinationSquare & board.allPieces()) != 0){
            int destinationPieceIdentifier = whichIntPieceOnSquare(board, destinationSquare) - 1;
            /*
            remove taken piece from hash
            */
            long victimZH = zobristHashPieces[destinationSquareIndex][destinationPieceIdentifier];
            boardHash ^= victimZH;
        }

        /* 
        "positive" EP flag is set in updateHashPostMove, in updateHashPreMove we cancel a previous EP flag
        */
        // todo
        Stack<Long> moveStack = board.moveStack;
        if (board.hasPreviousMove()){
//            boardHash = updateWithEPFlags(board, boardHash);
        }
        if (moveStack.size() > 0){
            boardHash = updateWithEPFlags(board, boardHash);
        }

        long destinationPiece = newPieceOnSquare(MoveParser.getDestinationIndex(move));

        if (MoveParser.isSpecialMove(move)){
            if (MoveParser.isCastlingMove(move)) {
                int originalRookIndex = 0;
                int newRookIndex = 0;
                if ((sourcePiece & INITIAL_WHITE_KING) != 0){
                    if (MoveParser.getDestinationIndex(move) == 1){
                        originalRookIndex = 0;
                        newRookIndex = MoveParser.getDestinationIndex(move) + 1;
                    }
                    else if (MoveParser.getDestinationIndex(move) == 5){
                        originalRookIndex = 7;
                        newRookIndex = MoveParser.getDestinationIndex(move) - 1;
                    }
                }

                else if ((sourcePiece & INITIAL_BLACK_KING) != 0){
                    if (MoveParser.getDestinationIndex(move) == 57){
                        originalRookIndex = 56;
                        newRookIndex = MoveParser.getDestinationIndex(move) + 1;
                    }
                    else if (MoveParser.getDestinationIndex(move) == 61){
                        originalRookIndex = 63;
                        newRookIndex = MoveParser.getDestinationIndex(move) - 1;
                    }
                }
                else {
                    throw new RuntimeException("Mistake in Zobrist of castling");
                }

                int myRook = whichIntPieceOnSquare(board, newPieceOnSquare(originalRookIndex)) - 1;
                long originalRookZH = zobristHashPieces[originalRookIndex][myRook];
                long newRookZH = zobristHashPieces[newRookIndex][myRook];
                boardHash ^= originalRookZH;
                boardHash ^= newRookZH;
            }

            else if (MoveParser.isEnPassantMove(move)){
                if ((sourcePiece & board.getWhitePawns()) != 0){
                    long victimPawn = destinationPiece >>> 8;
                    int indexOfVictimPawn = BitOperations.getIndexOfFirstPiece(victimPawn);
                    int pieceToKill = whichIntPieceOnSquare(board, victimPawn) - 1;
                    long victimPawnZH = zobristHashPieces[indexOfVictimPawn][pieceToKill];
                    boardHash ^= victimPawnZH;
                }

                else if  ((sourcePiece & board.getBlackPawns()) != 0){
                    long victimPawn = destinationPiece << 8;
                    int indexOfVictimPawn = BitOperations.getIndexOfFirstPiece(victimPawn);
                    int pieceToKill = whichIntPieceOnSquare(board, victimPawn) - 1;
                    long victimPawnZH = zobristHashPieces[indexOfVictimPawn][pieceToKill];
                    boardHash ^= victimPawnZH;
                }
                else {
                    throw new RuntimeException("false EP move");
                }

            }

            else if (MoveParser.isPromotionMove(move)){
                int whichPromotingPiece = 0;
                if ((sourcePiece & board.getWhitePawns()) != 0){
                    if (MoveParser.isPromotionToKnight(move)){
                        whichPromotingPiece = 2;
                    }
                    else if (MoveParser.isPromotionToBishop(move)){
                        whichPromotingPiece = 3;
                    }
                    else if (MoveParser.isPromotionToRook(move)){
                        whichPromotingPiece = 4;
                    }
                    else if (MoveParser.isPromotionToQueen(move)){
                        whichPromotingPiece = 5;
                    }
                }

                else if ((sourcePiece & board.getBlackPawns()) != 0){
                    if (MoveParser.isPromotionToKnight(move)){
                        whichPromotingPiece = 8;
                    }
                    else if (MoveParser.isPromotionToBishop(move)){
                        whichPromotingPiece = 9;
                    }
                    else if (MoveParser.isPromotionToRook(move)){
                        whichPromotingPiece = 10;
                    }

                    else if (MoveParser.isPromotionToQueen(move)){
                        whichPromotingPiece = 11;
                    }
                }

                /*
                remove my pawn from zh
                 */
                boardHash ^= destinationZH;

                if (whichPromotingPiece == 0){
                    System.out.println(board);
                    System.out.println();
                }
                long promotionZH = zobristHashPieces[destinationSquareIndex][whichPromotingPiece - 1];
                boardHash ^= promotionZH;
            }
        }

        return boardHash;
    }

    /*
    create almost unique long to identify current board
     */
    static long boardToHash(Chessboard board){
        long hash = 0;
        for (int sq = 0; sq < 64; sq++) {
            long pieceOnSquare = newPieceOnSquare(sq);
            int pieceIndex = whichIntPieceOnSquare(board, pieceOnSquare) - 1;
            if (pieceIndex != -1) {
                hash ^= zobristHashPieces[sq][pieceIndex];
            }
        }

        hash ^= castlingRightsToHash(board);

        if (!board.isWhiteTurn()){
            hash = zobristFlipTurn(hash);
        }

        if (board.moveStack.size() > 0){
            hash = updateWithEPFlags(board, hash);
        }
        if (board.hasPreviousMove()){
//            hash = updateWithEPFlags(board, hash);
        }

        return hash;
    }

    private static long castlingRightsToHash(Chessboard board){
        int numTo15 = 0;
        if (board.isWhiteCanCastleK()){
            numTo15 += 1;
        }
        if (board.isWhiteCanCastleQ()){
            numTo15 += 2;
        }
        if (board.isBlackCanCastleK()){
            numTo15 += 4;
        }
        if (board.isBlackCanCastleQ()){
            numTo15 += 8;
        }
        Assert.assertTrue(numTo15 >= 0 && numTo15 <= 15);

        return zobristHashCastlingRights[numTo15];
    }

    /*
    create values for every possible piece on every possible square
     */
    private static long[][] initPieceHash(){
        Random r = new Random(initHashSeed);
        long[][] zobristHash = new long[64][12];
        for (int outer = 0; outer < 64; outer++){
            for (int inner = 0; inner < 12; inner++){
                zobristHash[outer][inner] = r.nextLong();
            }
        }
        return zobristHash;
    }

    /*
    create values for every possible combination of castling right
    */
    private static long[] initCastlingHash(){
        Random r = new Random(initHashSeed + 1);
        long[] zobristHash = new long[16];
        zobristHash[0] = 0;
        for (int cr = 1; cr < zobristHash.length; cr++){
            zobristHash[cr] = r.nextLong();
        }
        return zobristHash;
    }

    /*
    create values for every possible EP file
    */
    private static long[] initEPHash(){
        Random r = new Random(initHashSeed + 2);
        long[] zobristHash = new long[8];
        for (int cr = 0; cr < zobristHash.length; cr++){
            zobristHash[cr] = r.nextLong();
        }
        return zobristHash;
    }

    /*
    create value for the player being black
    */
    private static long initColourHash(){
        Random r = new Random(initHashSeed + 3);
        return r.nextLong();
    }

}
