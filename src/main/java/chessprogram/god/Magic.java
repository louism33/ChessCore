package chessprogram.god;

import chessprogram.magic.Utils;
import org.junit.Assert;

import java.util.List;

import static chessprogram.god.BitOperations.*;
import static chessprogram.god.MagicConstants.*;
import static chessprogram.magic.Utils.singleBishopAllVariations;
import static chessprogram.magic.Utils.singleRookAllVariations;

public class Magic {
    
    private static boolean ready = false;
    
    private static void init(){
        calculateRookDatabase();
        calculateBishopDatabase();

        System.out.println("get ready");
        ready = true;
    }

    static long singleRookMagicMoves(Chessboard board, boolean white, long rook, long legalMovesMask){
        if (!ready){
            init();
        }
        Assert.assertTrue(ready);
        Assert.assertEquals(populationCount(rook), 1);

        final int rookIndex = getIndexOfFirstPiece(rook);
        final long rookMagicNumber = MagicConstants.rookMagicNumbers[rookIndex];

        final int index = (int) (((board.allPieces() & rookBlankBoardAttackMasks[rookIndex]) * rookMagicNumber)
                >>> (64 - (rookShiftAmounts[rookIndex])));

        final long legalMoves = rookDatabase[rookIndex][index];

        return legalMoves & legalMovesMask;
    }

    static long singleBishopMagicMoves(Chessboard board, boolean white, long bishop, long legalMovesMask){
        if (!ready){
            init();
        }
        Assert.assertTrue(ready);
        Assert.assertEquals(populationCount(bishop), 1);

        final int bishopIndex = getIndexOfFirstPiece(bishop);
        final long bishopMagicNumber = MagicConstants.bishopMagicNumbers[bishopIndex];

        final int index = (int) (((board.allPieces() & bishopBlankBoardAttackMasks[bishopIndex]) * bishopMagicNumber)
                >>> (64 - (bishopShiftAmounts[bishopIndex])));

        final long legalMoves = bishopDatabase[bishopIndex][index];

        return legalMoves & legalMovesMask;
    }

    private static void calculateRookDatabase(){
        for (Square square : Square.values()) {
            rookDatabase[square.ordinal()] = makeRookDB(square);
        }
    }

    private static void calculateBishopDatabase(){
        for (Square square : Square.values()) {
            bishopDatabase[square.ordinal()] = makeBishopDB(square);
        }
    }

    private static List<Long> bishopVariations(Square square) {
        return singleBishopAllVariations(new Chessboard(true), square.toBitboard(), true, UNIVERSE, 0);
    }

    private static long[] makeBishopDB(Square square) {
        int length = bishopShiftAmounts[square.ordinal()];
        long magic = MagicConstants.bishopMagicNumbers[square.ordinal()];

        List<Long> bishopVariations = bishopVariations(square);
        long sq = square.toBitboard();
        long[] db = new long[(int) Math.pow(2, length)];

        for (Long variation : bishopVariations) {
            long correctBishopMovesResultBitboard = Utils.singleBishopAllMovesFromOcc(variation, sq);
            long mult = (magic * variation);
            int index = (int) (mult >>> (64 - length));
            db[index] = correctBishopMovesResultBitboard;
        }
        return db;
    }

    private static List<Long> rookVariations(Square square) {
        return singleRookAllVariations(new Chessboard(true), square.toBitboard(), true, UNIVERSE, 0);
    }

    private static long[] makeRookDB(Square square) {
        int length = rookShiftAmounts[square.ordinal()];
        long magic = MagicConstants.rookMagicNumbers[square.ordinal()];

        List<Long> rookVariations = rookVariations(square);
        long sq = square.toBitboard();
        long[] db = new long[(int) Math.pow(2, length)];

        for (Long variation : rookVariations) {
            long correctRookMovesResultBitboard = Utils.singleRookAllMovesFromOcc(variation, sq);
            long mult = (magic * variation);
            int index = (int) (mult >>> (64 - length));
            db[index] = correctRookMovesResultBitboard;
        }
        return db;
    }

}
