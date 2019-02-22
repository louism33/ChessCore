package com.github.louism33.chesscore;

import org.junit.Assert;

import java.util.Random;

import static com.github.louism33.chesscore.BitOperations.newPieceOnSquare;
import static com.github.louism33.chesscore.MakeMoveRegular.whichIntPieceOnSquare;
import static com.github.louism33.chesscore.MoveConstants.*;
import static com.github.louism33.chesscore.StackDataUtil.ENPASSANTVICTIM;
import static com.github.louism33.chesscore.StackDataUtil.NULL_MOVE;

final class ZobristHashUtil {

    private static final long initHashSeed = 100;
    private static final long[][] zobristHashPieces = initPieceHash();
    private static final long[] zobristHashCastlingRights = initCastlingHash();
    private static final long[] zobristHashEPFiles = initEPHash();
    private static final long zobristHashColourBlack = initColourHash();

    static long zobristFlipTurn(long hash){
        return hash ^ zobristHashColourBlack;
    }

    static long updateWithEPFlags(Chessboard board, long hash){
        Assert.assertTrue(board.hasPreviousMove());

        long peek = board.moveStackArrayPeek();

        if (StackDataUtil.getSpecialMove(peek) == ENPASSANTVICTIM) {
            hash = hashEP(hash, peek);
        }

        if (StackDataUtil.getSpecialMove(peek) == NULL_MOVE && board.hasPreviousMove()){
            hash = nullMoveEP(board, hash);
        }

        return hash;
    }

    private static long nullMoveEP(Chessboard board, long hash) {
        long peekSecondElement = board.moveStackArrayPeek();
        if (StackDataUtil.getSpecialMove(peekSecondElement) == ENPASSANTVICTIM) {
            hash = hashEP(hash, peekSecondElement);
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

        Assert.assertTrue(board.hasPreviousMove());
        
        /*
        if move we just made raised EP flag, update hash
        */
        boardHash = updateWithEPFlags(board, boardHash);

        /*
        if castling rights changed, update hash
        */
        boardHash ^= postMoveCastlingRights(board, move);

        return boardHash;
    }

    private static boolean castlingRightsAffectedByMove (int move){
        switch (MoveParser.getSourceIndex(move)) {
            case 0:
            case 3:
            case 7:
            case 56:
            case 59:
            case 63:
                return true;
        }
        switch (MoveParser.getDestinationIndex(move)) {
            case 0:
            case 3:
            case 7:
            case 56:
            case 59:
            case 63:
                return true;
        }
        return false;
    }

    private static long postMoveCastlingRights(Chessboard board, int move){
        long updatedHashValue = 0;
        final long peek = board.moveStackArrayPeek();

        int castlingRights = StackDataUtil.getCastlingRights(peek);
        
        /*
        castling rights may never return, so if 0, no need to update anything
         */
        if (castlingRights == 0 || !castlingRightsAffectedByMove(move)) {
            return 0;
        }

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
        int sourcePieceIdentifier = whichIntPieceOnSquare(board, newPieceOnSquare(sourceSquare)) - 1;

        boardHash ^= zobristHashPieces[sourceSquare][sourcePieceIdentifier];
        long destinationZH = zobristHashPieces[destinationSquareIndex][sourcePieceIdentifier];

        boardHash ^= destinationZH;

        int whiteTurn = board.isWhiteTurn() ? 1 : 0;
        /*
        captures
         */
        if (MoveParser.isCaptureMove(move)){
            int destinationPieceIdentifier = whichIntPieceOnSquare(board, newPieceOnSquare(destinationSquareIndex)) - 1;
            /*
            remove taken piece from hash
            */
            long victimZH = zobristHashPieces[destinationSquareIndex][destinationPieceIdentifier];
            boardHash ^= victimZH;
        }

        /* 
        "positive" EP flag is set in updateHashPostMove, in updateHashPreMove we cancel a previous EP flag
        */
        if (board.hasPreviousMove()){
            boardHash = updateWithEPFlags(board, boardHash);
        }

        long destinationPiece = newPieceOnSquare(MoveParser.getDestinationIndex(move));

        if (MoveParser.isSpecialMove(move)){
            if (MoveParser.isCastlingMove(move)) {
                int originalRookIndex = 0;
                int newRookIndex = 0;
                switch (MoveParser.getDestinationIndex(move)) {
                    case 1:
                        originalRookIndex = 0;
                        newRookIndex = MoveParser.getDestinationIndex(move) + 1;
                        break;
                    case 5:
                        originalRookIndex = 7;
                        newRookIndex = MoveParser.getDestinationIndex(move) - 1;
                        break;
                    case 57:
                        originalRookIndex = 56;
                        newRookIndex = MoveParser.getDestinationIndex(move) + 1;
                        break;
                    case 61:
                        originalRookIndex = 63;
                        newRookIndex = MoveParser.getDestinationIndex(move) - 1;
                        break;
                }

                int myRook = whichIntPieceOnSquare(board, newPieceOnSquare(originalRookIndex)) - 1;
                boardHash ^= zobristHashPieces[originalRookIndex][myRook];
                boardHash ^= zobristHashPieces[newRookIndex][myRook];
            }

            else if (MoveParser.isEnPassantMove(move)){
                long victimPawn = whiteTurn == 1 ? destinationPiece >>> 8 : destinationPiece << 8;
                boardHash ^= zobristHashPieces
                        [BitOperations.getIndexOfFirstPiece(victimPawn)]
                        [whichIntPieceOnSquare(board, victimPawn) - 1];
            }

            else if (MoveParser.isPromotionMove(move)){
                int whichPromotingPiece = 0;

                switch (move & WHICH_PROMOTION){
                    case KNIGHT_PROMOTION_MASK:
                        whichPromotingPiece = 2 + (1 - whiteTurn) * 6;
                        break;
                    case BISHOP_PROMOTION_MASK:
                        whichPromotingPiece = 3 + (1 - whiteTurn) * 6;
                        break;
                    case ROOK_PROMOTION_MASK:
                        whichPromotingPiece = 4 + (1 - whiteTurn) * 6;
                        break;
                    case QUEEN_PROMOTION_MASK:
                        whichPromotingPiece = 5 + (1 - whiteTurn) * 6;
                        break;
                }

                /*
                remove my pawn from zh
                 */
                boardHash ^= destinationZH;

                Assert.assertTrue(whichPromotingPiece != 0);
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

        if (board.hasPreviousMove()){
            hash = updateWithEPFlags(board, hash);
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
