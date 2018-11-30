package chessprogram.god;

import java.util.ArrayList;
import java.util.List;

import static chessprogram.god.Setup.ready;
import static chessprogram.god.Setup.setup;

public class BitOperations {

    public static long newPieceOnSquare (int x){
        return 0x0000000000000001L << x;
    }

    public static long squareCentredOnIndex(int x){
        return BitboardResources.immediateAdjacentSquares[x];
    }

    public static int getIndexOfFirstPiece (long pieces) {
        return Long.numberOfTrailingZeros(pieces);
    }

    public static List<Integer> getIndexOfAllPieces(long pieces){
        List<Integer> indexes = new ArrayList<>();
        while (pieces != 0){
            indexes.add(getIndexOfFirstPiece(pieces));
            pieces &= pieces - 1;
        }
        return indexes;
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
            setup();
        }
        return BitboardResources.inBetweenSquares[pieceOneIndex][pieceTwoIndex]
                ^ (BitOperations.newPieceOnSquare(pieceOneIndex) | BitOperations.newPieceOnSquare(pieceTwoIndex));
    }
    
}
