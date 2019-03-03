package com.github.louism33.chesscore;

import static com.github.louism33.chesscore.BoardConstants.FILES;
import static com.github.louism33.chesscore.BoardConstants.ROWS;
import static com.github.louism33.chesscore.Setup.ready;
import static com.github.louism33.chesscore.Setup.setup;
import static java.lang.Long.*;

public final class BitOperations {

    public static long newPieceOnSquare (int x){
        return 0x0000000000000001L << x;
    }

    public static long squareCentredOnIndex(int x){
        return BoardConstants.immediateAdjacentSquares[x];
    }

    public static long bigSquareCentredOnIndex(int x){
        return BoardConstants.bigAdjacentSquares[x];
    }

    public static long enemyPawnKillZoneCentredOnIndex(int x, boolean myColour){
        if (myColour){
            return BoardConstants.blackPawnKillZone[x];
        }
        else {
            return BoardConstants.whitePawnKillZone[x];
        }
    }

    public static long fileForward(int x, boolean white){
        if (white){
            return BoardConstants.fileForwardWhite[x];
        }
        else {
            return BoardConstants.fileForwardBlack[x];
        }
    }
    
    public static int populationCount (long pieces) {
        return bitCount(pieces);
    }

    private static long[] getAllPiecesBetter(long pieces) {
        long[] indexes = new long[BitOperations.populationCount(pieces)];
        int index = 0;
        while (pieces != 0) {
            indexes[index] = getFirstPiece(pieces);
            pieces &= pieces - 1;
        }
        return indexes;
    }

    public static long getFirstPiece(long l) {
        return lowestOneBit(l);
    }

    public static long getLastPiece(long l) {
        return highestOneBit(l);
    }

    static long extractRayFromTwoPiecesBitboard(long pieceOne, long pieceTwo){
        return extractRayFromTwoPieces(numberOfTrailingZeros(pieceOne), numberOfTrailingZeros(pieceTwo));
    }

    static long extractRayFromTwoPiecesBitboardInclusive(long pieceOne, long pieceTwo){
        return extractRayFromTwoPieces(numberOfTrailingZeros(pieceOne), numberOfTrailingZeros(pieceTwo))
                | pieceOne | pieceTwo;
    }

    static long extractRayFromTwoPieces(int pieceOneIndex, int pieceTwoIndex){
        if (!ready){
            setup(false);
        }
        return BoardConstants.inBetweenSquares[pieceOneIndex][pieceTwoIndex]
                ^ (BitOperations.newPieceOnSquare(pieceOneIndex) | BitOperations.newPieceOnSquare(pieceTwoIndex));
    }

    public static int getRowNumber(long piece){
        return numberOfTrailingZeros(piece) / 8;
    }

    public static int getFileNumber(long piece){
        return numberOfTrailingZeros(piece)  % 8;
    }

    public static long getRow(long piece){
        return ROWS[numberOfTrailingZeros(piece)  / 8];
    }

    public static long getFile(long piece) {
        return FILES[numberOfTrailingZeros(piece) % 8];
    }
}
