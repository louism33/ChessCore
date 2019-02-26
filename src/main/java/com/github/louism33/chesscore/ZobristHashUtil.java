package com.github.louism33.chesscore;

import org.junit.Assert;

import java.util.Random;

import static com.github.louism33.chesscore.BitOperations.getIndexOfFirstPiece;
import static com.github.louism33.chesscore.BitOperations.newPieceOnSquare;
import static com.github.louism33.chesscore.MoveConstants.*;
import static com.github.louism33.chesscore.StackDataUtil.ENPASSANTVICTIM;
import static com.github.louism33.chesscore.StackDataUtil.NULL_MOVE;

final class ZobristHashUtil {

    private static final long initHashSeed = 100;
    static final long[][] zobristHashPieces = initPieceHash();
    static final long[] zobristHashCastlingRights = initCastlingHash();
    private static final long[] zobristHashEPFiles = initEPHash();
    private static final long zobristHashColourBlack = initColourHash();

    static long zobristFlipTurn(long hash){
        return hash ^ zobristHashColourBlack;
    }

    static long updateWithEPFlags(long moveStackPeek, long hash){
        if (StackDataUtil.getSpecialMove(moveStackPeek) == ENPASSANTVICTIM) {
            hash = hashEP(hash, moveStackPeek);
        }

        if (StackDataUtil.getSpecialMove(moveStackPeek) == NULL_MOVE){
            hash = nullMoveEP(moveStackPeek, hash);
        }

        return hash;
    }

    private static long nullMoveEP(long moveStackPeek, long hash) {
        if (StackDataUtil.getSpecialMove(moveStackPeek) == ENPASSANTVICTIM) {
            hash = hashEP(hash, moveStackPeek);
        }
        return hash;
    }

    private static long hashEP(long hash, long peek) {
        return hash ^ zobristHashEPFiles[StackDataUtil.getEPMove(peek) - 1];
    }

    static long updateHashPostMove(long moveStackPeek, int castlingRights, long boardHash){
        boardHash = zobristFlipTurn(boardHash);

        /*
        if move we just made raised EP flag, update hash
        */
        boardHash = updateWithEPFlags(moveStackPeek, boardHash);

        final int peekCastlingRights = StackDataUtil.getCastlingRights(moveStackPeek);
        
        /*
        if castling rights changed, update hash
        castling rights may never return, so if 0, no need to update anything
         */
        if (peekCastlingRights == 0 || peekCastlingRights == castlingRights) {
            return boardHash;
        }

        /*
        undo previous castling rights and update with new castling rights
        */
        boardHash ^= zobristHashCastlingRights[peekCastlingRights ^ castlingRights];
        
        return boardHash;
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
