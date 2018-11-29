package chessprogram.god;

import java.util.ArrayList;
import java.util.List;

public class BitOperations {

    public static final long UNIVERSE = 0xffffffffffffffffL;

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

}
