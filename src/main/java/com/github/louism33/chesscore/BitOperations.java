package com.github.louism33.chesscore;

import java.util.ArrayList;
import java.util.List;

import static com.github.louism33.chesscore.BitboardResources.FILES;
import static com.github.louism33.chesscore.BitboardResources.ROWS;
import static com.github.louism33.chesscore.Setup.ready;
import static com.github.louism33.chesscore.Setup.setup;

class BitOperations {

    public static long newPieceOnSquare (int x){
        return 0x0000000000000001L << x;
    }

    public static long squareCentredOnIndex(int x){
        return BitboardResources.immediateAdjacentSquares[x];
    }

    public static long bigSquareCentredOnIndex(int x){
        return BitboardResources.bigAdjacentSquares[x];
    }

    public static long enemyPawnKillZoneCentredOnIndex(int x, boolean myColour){
        if (myColour){
            return BitboardResources.blackPawnKillZone[x];
        }
        else {
            return BitboardResources.whitePawnKillZone[x];
        }
    }

    public static long fileForward(int x, boolean white){
        if (white){
            return BitboardResources.fileForwardWhite[x];
        }
        else {
            return BitboardResources.fileForwardBlack[x];
        }
    }
    
    public static int getIndexOfFirstPiece (long pieces) {
        return Long.numberOfTrailingZeros(pieces);
    }

    public static int populationCount (long pieces) {
        return Long.bitCount(pieces);
    }
    
    public static List<Long> getAllPieces(long pieces, long ignoreThesePieces) {
        List<Long> indexes = new ArrayList<>();
        long temp = pieces & (~ignoreThesePieces);
        while (temp != 0) {
            indexes.add(getFirstPiece(temp));
            temp &= temp - 1;
        }
        return indexes;
    }

    public static long getFirstPiece(long l) {
        return Long.lowestOneBit(l);
    }

    public static long getLastPiece(long l) {
        return Long.highestOneBit(l);
    }

    static long extractRayFromTwoPieces(Piece pieceOne, Piece pieceTwo){
        return extractRayFromTwoPieces(pieceOne.ordinal(), pieceTwo.ordinal());
    }

    static long extractRayFromTwoPiecesBitboard(long pieceOne, long pieceTwo){
        return extractRayFromTwoPieces(getIndexOfFirstPiece(pieceOne), getIndexOfFirstPiece(pieceTwo));
    }

    static long extractRayFromTwoPiecesBitboardInclusive(long pieceOne, long pieceTwo){
        return extractRayFromTwoPieces(getIndexOfFirstPiece(pieceOne), getIndexOfFirstPiece(pieceTwo))
                | pieceOne | pieceTwo;
    }

    static long extractRayFromTwoPieces(int pieceOneIndex, int pieceTwoIndex){
        if (!ready){
            setup(false);
        }
        return BitboardResources.inBetweenSquares[pieceOneIndex][pieceTwoIndex]
                ^ (BitOperations.newPieceOnSquare(pieceOneIndex) | BitOperations.newPieceOnSquare(pieceTwoIndex));
    }

    public int getRowNumber(long piece){
        return getIndexOfFirstPiece(piece) / 8;
    }

    public int getFileNumber(long piece){
        return getIndexOfFirstPiece(piece)  % 8;
    }

    public long getRow(long piece){
        return ROWS[getIndexOfFirstPiece(piece)  / 8];
    }

    public long getFile(long piece) {
        return FILES[getIndexOfFirstPiece(piece) % 8];
    }
}
