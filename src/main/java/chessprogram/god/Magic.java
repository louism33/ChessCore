package chessprogram.god;

import org.junit.Assert;

import java.util.List;

import static chessprogram.god.BitOperations.*;
import static chessprogram.god.BitboardResources.NORTH_EAST;
import static chessprogram.god.BitboardResources.NORTH_WEST;
import static chessprogram.god.MagicConstants.*;
import static chessprogram.god.MagicUtils.singleBishopAllVariations;
import static chessprogram.god.MagicUtils.singleRookAllVariations;

public class Magic {
    
    private static boolean ready = false;
    
    public static void init(){
        calculateRookDatabase();
        calculateBishopDatabase();
        
        calculateInBetweenSquares();

        System.out.println("get ready");
        ready = true;
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
            init();
        }
        return BitboardResources.inBetweenSquares[pieceOneIndex][pieceTwoIndex] 
                ^ (BitOperations.newPieceOnSquare(pieceOneIndex) | BitOperations.newPieceOnSquare(pieceTwoIndex));
    }
    
    static long singleRookMagicMoves(long occupancy, long rook, long legalMovesMask){
        if (!ready){
            init();
        }
        Assert.assertTrue(ready);
        Assert.assertEquals(populationCount(rook), 1);

        final int rookIndex = getIndexOfFirstPiece(rook);
        final long rookMagicNumber = MagicConstants.rookMagicNumbers[rookIndex];

        final int index = (int) (((occupancy & rookBlankBoardAttackMasks[rookIndex]) * rookMagicNumber)
                >>> (64 - (rookShiftAmounts[rookIndex])));

        final long legalMoves = rookDatabase[rookIndex][index];

        return legalMoves & legalMovesMask;
    }

    static long singleBishopMagicMoves(long allPieces, long bishop, long legalMovesMask){
        if (!ready){
            init();
        }
        Assert.assertTrue(ready);
        Assert.assertEquals(populationCount(bishop), 1);

        final int bishopIndex = getIndexOfFirstPiece(bishop);
        final long bishopMagicNumber = MagicConstants.bishopMagicNumbers[bishopIndex];

        final int index = (int) (((allPieces & bishopBlankBoardAttackMasks[bishopIndex]) * bishopMagicNumber)
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
            long correctBishopMovesResultBitboard = MagicUtils.singleBishopAllMovesFromOcc(variation, sq);
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
            long correctRookMovesResultBitboard = MagicUtils.singleRookAllMovesFromOcc(variation, sq);
            long mult = (magic * variation);
            int index = (int) (mult >>> (64 - length));
            db[index] = correctRookMovesResultBitboard;
        }
        return db;
    }

    public static void calculateInBetweenSquares(){
        final int length = BitboardResources.inBetweenSquares.length;
        for (int i = 0; i < length; i++){
            long pieceOne = BitOperations.newPieceOnSquare(i);
            for (int j = 0; j < 64; j++){
                long pieceTwo = BitOperations.newPieceOnSquare(j);
                BitboardResources.inBetweenSquares[i][j] = extractRayFromTwoPieces(pieceOne, pieceTwo);
            }
        }
    }

    private static long extractRayFromTwoPieces(long pieceOne, long pieceTwo){
        if (pieceOne == pieceTwo) return 0;

        long originalPieces = pieceOne | pieceTwo;
        
        // necessary as java offers signed ints, which get confused if talking about square 63
        int indexOfPieceOne = getIndexOfFirstPiece(pieceOne);
        int indexOfPieceTwo = getIndexOfFirstPiece(pieceTwo);
        long bigPiece = (indexOfPieceOne > indexOfPieceTwo) ? pieceOne : pieceTwo;
        long smallPiece = (indexOfPieceOne > indexOfPieceTwo) ? pieceTwo : pieceOne;
        long possibleAnswer = 0;

        while (true) {
            if ((smallPiece & BitboardResources.FILE_A) != 0) {
                break;
            }
            smallPiece <<= 1;
            if ((smallPiece & bigPiece) != 0) {
                return possibleAnswer | originalPieces;
            }
            possibleAnswer |= smallPiece;
        }

        bigPiece = (indexOfPieceOne > indexOfPieceTwo) ? pieceOne : pieceTwo;
        smallPiece = (indexOfPieceOne > indexOfPieceTwo) ? pieceTwo : pieceOne;
        possibleAnswer = 0;

        while (true) {
            if ((smallPiece & NORTH_WEST) != 0) {
                break;
            }
            smallPiece <<= 9;
            if ((smallPiece & bigPiece) != 0) {
                return possibleAnswer | originalPieces;
            }
            possibleAnswer |= smallPiece;
        }

        bigPiece = (indexOfPieceOne > indexOfPieceTwo) ? pieceOne : pieceTwo;
        smallPiece = (indexOfPieceOne > indexOfPieceTwo) ? pieceTwo : pieceOne;
        possibleAnswer = 0;

        while (true) {
            if ((smallPiece & BitboardResources.RANK_EIGHT) != 0) {
                break;
            }
            smallPiece <<= 8;
            if ((smallPiece & bigPiece) != 0) {
                return possibleAnswer | originalPieces;
            }
            possibleAnswer |= smallPiece;
        }

        bigPiece = (indexOfPieceOne > indexOfPieceTwo) ? pieceOne : pieceTwo;
        smallPiece = (indexOfPieceOne > indexOfPieceTwo) ? pieceTwo : pieceOne;
        possibleAnswer = 0;

        while (true) {
            if ((smallPiece & NORTH_EAST) != 0) {
                break;
            }
            smallPiece <<= 7;
            if ((smallPiece & bigPiece) != 0) {
                return possibleAnswer | originalPieces;
            }
            possibleAnswer |= smallPiece;
        }

        return 0;
    }

}
