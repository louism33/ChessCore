package chessprogram.god;

import java.util.ArrayList;
import java.util.List;

import static chessprogram.god.BitboardResources.boardWithoutEdges;

public class BitOperations {

    public static long newPieceOnSquare (int x){
        return 0x0000000000000001L << x;
    }

    public static long squareCentredOnIndexNaive(int x){
        long piece = newPieceOnSquare(x);
        return squareCentredOnPieceNaive(piece);
    }

    private static long squareCentredOnPieceNaive(long piece){
        long answer = 0;

        answer += piece
                + (piece << 9)
                + (piece << 8)
                + (piece << 7)

                + (piece << 1)
                + (piece >>> 1)

                + (piece >>> 7)
                + (piece >>> 8)
                + (piece >>> 9)
        ;
        return answer;
    }


    private static long squareCentredOnIndexSmart(int x){
        long piece = newPieceOnSquare(x);
        return squareCentredOnPieceSmart(piece);
    }


    private static long squareCentredOnPieceSmart(long piece){
        long answer = 0;

        if ((boardWithoutEdges & piece) != 0){
            return squareCentredOnPieceNaive(piece);
        }
        else {
            throw new RuntimeException("unfinished");
        }

//        if ((RANK_EIGHT & piece) != 0) {
//            return ((piece << 1)
//                    + (piece >>> 1)
//
//                    + (piece >>> 7)
//                    + (piece >>> 8)
//                    + (piece >>> 9));
//        }
//        if ((RANK_ONE & piece) != 0) {
//            return ((piece << 1)
//                    + (piece >>> 1)
//                    + (piece << 9)
//                    + (piece << 8)
//                    + (piece << 7));
//                    
//        }

                
    }


    public static final long UNIVERSE = 0xffffffffffffffffL;

    public static int getIndexOfFirstPiece (long pieces) {
        if (pieces == 0) return -1;
        long finder = pieces;
        int i = 0;
        while (((finder % 2) != 1) && ((finder % 2) != -1)){
            finder >>>= 1;
            i++;
        }
        return i;
    }

    public static List<Integer> getIndexOfAllPieces(long pieces){
        List<Integer> indexes = new ArrayList<>();
        long temp = pieces;
        long endSquareMask = 0x0000000000000001L;
        int i = 0;
        while (temp != 0){
            if ((temp & endSquareMask) == endSquareMask) indexes.add(i);
            temp >>>= 1;
            i++;
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
            long firstPiece = getFirstPiece(temp);
            indexes.add(firstPiece);
            temp ^= firstPiece;
        }
        return indexes;
    }

    private static long getFirstPiece(long l) {
        return Long.highestOneBit(l);
    }
    
}
