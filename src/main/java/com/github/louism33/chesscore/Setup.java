package com.github.louism33.chesscore;

import org.junit.Assert;

import static com.github.louism33.chesscore.BitOperations.getIndexOfFirstPiece;
import static com.github.louism33.chesscore.BitOperations.newPieceOnSquare;
import static com.github.louism33.chesscore.BoardConstants.*;

class Setup {

    public static void main(String[] args){
        setup(false);
    }

    static void setup(boolean force){
        init(force);
    }

    static boolean ready = false;

    static void init(boolean force){
        if (!ready || force) {
            calculateDatabases();
            calculateInBetweenSquares();
        }
        ready = true;
    }

    private static void calculateDatabases(){
        for (int i = 0; i < 64; i++) {
            rookDatabase[i] = makeRookDB(i);
            bishopDatabase[i] = makeBishopDB(i);
        }
    }

    private static long[] makeBishopDB(int squareIndex) {
        int length = bishopShiftAmounts[squareIndex];

        long sq = newPieceOnSquare(squareIndex);

        long[] bishopVariations = singleBishopAllVariationsBetter(sq, length);
        
        long[] db = new long[1 << length];

        for (int i = 0; i < bishopVariations.length; i++) {
            long variation = bishopVariations[i];
            long correctBishopMovesResultBitboard = singleBishopAllMovesFromOcc(variation, sq);
            long mult = (bishopMagicNumbers[squareIndex] * variation);
            int index = (int) (mult >>> (64 - length));
            db[index] = correctBishopMovesResultBitboard;
        }
        return db;
    }

    private static long[] makeRookDB(int squareIndex) {
        int length = rookShiftAmounts[squareIndex];

        long sq = newPieceOnSquare(squareIndex);
        long[] rookVariations = singleRookAllVariationsBetter(sq, length);

        long[] db = new long[1 << length];

        Assert.assertEquals(db.length, 1 << length);

        for (int i = 0; i < rookVariations.length; i++) {
            long variation = rookVariations[i];
            long correctRookMovesResultBitboard = singleRookAllMovesFromOcc(variation, sq);
            long mult = (rookMagicNumbers[squareIndex] * variation);
            int index = (int) (mult >>> (64 - length));
            db[index] = correctRookMovesResultBitboard;
        }
        return db;
    }

    private static void calculateInBetweenSquares(){
        final int length = BoardConstants.inBetweenSquares.length;
        for (int i = 0; i < length; i++){
            long pieceOne = BitOperations.newPieceOnSquare(i);
            for (int j = 0; j < 64; j++){
                long pieceTwo = BitOperations.newPieceOnSquare(j);
                BoardConstants.inBetweenSquares[i][j] = extractRayFromTwoPieces(pieceOne, pieceTwo);
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
            if ((smallPiece & BoardConstants.FILE_A) != 0) {
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
            if ((smallPiece & BoardConstants.RANK_EIGHT) != 0) {
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

    private static long singleBishopAllMovesFromOcc(long blockers, long piece){
        long NORTH_WEST = BoardConstants.FILE_A | BoardConstants.RANK_EIGHT,
                NORTH_EAST = BoardConstants.FILE_H | BoardConstants.RANK_EIGHT,
                SOUTH_WEST = BoardConstants.FILE_A | BoardConstants.RANK_ONE,
                SOUTH_EAST = BoardConstants.FILE_H | BoardConstants.RANK_ONE;

        long answer = 0;
        long temp = piece;

        while (true) {
            if ((temp & NORTH_WEST) != 0) break;
            temp <<= 9;
            answer |= temp;
            if ((temp & blockers) != 0) break;
        }
        temp = piece;
        while (true) {
            if ((temp & NORTH_EAST) != 0) break;
            temp <<= 7;
            answer |= temp;
            if ((temp & blockers) != 0) break;
        }
        temp = piece;
        while (true) {
            if ((temp & SOUTH_WEST) != 0) break;
            temp >>>= 7;
            answer |= temp;
            if ((temp & blockers) != 0) break;
        }
        temp = piece;
        while (true) {
            if ((temp & SOUTH_EAST) != 0) break;
            temp >>>= 9;
            answer |= temp;
            if ((temp & blockers) != 0) break;
        }
        return answer;
    }


    private static long singleRookAllMovesFromOcc(long blockers, long piece){
        long answer = 0;
        long temp = piece;
        while (true) {
            if ((temp & BoardConstants.FILE_A) != 0) break;
            temp <<= 1;
            answer |= temp;
            if ((temp & blockers) != 0) break;
        }
        temp = piece;
        while (true) {
            if ((temp & BoardConstants.FILE_H) != 0) break;
            temp >>>= 1;
            answer |= temp;
            if ((temp & blockers) != 0) break;
        }
        temp = piece;
        while (true) {
            if ((temp & BoardConstants.RANK_EIGHT) != 0) break;
            temp <<= 8;
            answer |= temp;
            if ((temp & blockers) != 0) break;
        }
        temp = piece;
        while (true) {
            if ((temp & BoardConstants.RANK_ONE) != 0) break;
            temp >>>= 8;
            answer |= temp;
            if ((temp & blockers) != 0) break;
        }
        return answer;
    }


    private static long[] singleRookAllVariationsBetter(long piece, int length){
        long temp = piece;
        long[] indexes = new long[length];
        int index = 0;
                
        while (true) {
            if ((temp & BoardConstants.FILE_A) != 0) break;
            if ((temp & BoardConstants.FILE_B) != 0) break;
            temp <<= 1;

            indexes[index++] = temp;

        }
        temp = piece;
        while (true) {
            if ((temp & BoardConstants.FILE_H) != 0) break;
            if ((temp & BoardConstants.FILE_G) != 0) break;
            temp >>>= 1;

            indexes[index++] = temp;
        }
        temp = piece;
        while (true) {
            if ((temp & BoardConstants.RANK_EIGHT) != 0) break;
            if ((temp & BoardConstants.RANK_SEVEN) != 0) break;
            temp <<= 8;

            indexes[index++] = temp;
        }
        temp = piece;
        while (true) {
            if ((temp & BoardConstants.RANK_ONE) != 0) break;
            if ((temp & BoardConstants.RANK_TWO) != 0) break;
            temp >>>= 8;

            indexes[index++] = temp;
        }

        return permuteRookBetter(indexes, length);
    }

    private static long NORTH_WEST = BoardConstants.FILE_A | BoardConstants.RANK_EIGHT,
            NORTH_WEST1 = BoardConstants.FILE_B | BoardConstants.RANK_SEVEN,

    NORTH_EAST = BoardConstants.FILE_H | BoardConstants.RANK_EIGHT,
            NORTH_EAST1 = BoardConstants.FILE_G | BoardConstants.RANK_SEVEN,

    SOUTH_WEST = BoardConstants.FILE_A | BoardConstants.RANK_ONE,
            SOUTH_WEST1 = BoardConstants.FILE_B | BoardConstants.RANK_TWO,

    SOUTH_EAST = BoardConstants.FILE_H | BoardConstants.RANK_ONE,
            SOUTH_EAST1 = BoardConstants.FILE_G | BoardConstants.RANK_TWO;

    private static long[] singleBishopAllVariationsBetter(long piece, int length) {
        
        long temp = piece;
        long[] indexes = new long[length];
        int index = 0;

        while (true) {
            if ((temp & NORTH_WEST) != 0) break;
            if ((temp & NORTH_WEST1) != 0) break;
            temp <<= 9;
            indexes[index++] = temp;
        }
        temp = piece;
        while (true) {
            if ((temp & NORTH_EAST) != 0) break;
            if ((temp & NORTH_EAST1) != 0) break;
            temp <<= 7;
            indexes[index++] = temp;
        }
        temp = piece;
        while (true) {
            if ((temp & SOUTH_WEST) != 0) break;
            if ((temp & SOUTH_WEST1) != 0) break;
            temp >>>= 7;
            indexes[index++] = temp;
        }
        temp = piece;
        while (true) {
            if ((temp & SOUTH_EAST) != 0) break;
            if ((temp & SOUTH_EAST1) != 0) break;
            temp >>>= 9;
            indexes[index++] = temp;
        }

        return permuteBishopBetter(indexes, length);
    }


    private static long[] permuteRookBetter(long[] pieces, int length){
        final int size = 1 << length;
        long[] answers = new long[size];

        for (int t = 0; t < size; t++){
            long mask = 0;
            if ((t & 1) == 0) mask |= pieces[0];
            if ((t & 2) == 0) mask |= pieces[1];
            if ((t & 4) == 0) mask |= pieces[2];
            if ((t & 8) == 0) mask |= pieces[3];
            if ((t & 16) == 0) mask |= pieces[4];
            if ((t & 32) == 0) mask |= pieces[5];
            if ((t & 64) == 0) mask |= pieces[6];
            if ((t & 128) == 0) mask |= pieces[7];
            if ((t & 256) == 0) mask |= pieces[8];
            if ((t & 512) == 0) mask |= pieces[9];
            if (length > 10) {
                if ((t & 1024) == 0) mask |= pieces[10];
            }
            if (length > 11) {
                if ((t & 2048) == 0) mask |= pieces[11];
            }
            answers[t] = mask;
        }

        return answers;
    }

    private static long[] permuteBishopBetter(long[] pieces, int length){
        final int size = 1 << length;
        long[] answers = new long[size];

        for (int t = 0; t < size; t++){
            long mask = 0;
            if ((t & 1) == 0) mask |= pieces[0];
            if (length > 1) {
                if ((t & 2) == 0) mask |= pieces[1];
            }
            if (length > 2) {
                if ((t & 4) == 0) mask |= pieces[2];
            }
            if (length > 3) {
                if ((t & 8) == 0) mask |= pieces[3];
            }
            if (length > 4) {
                if ((t & 16) == 0) mask |= pieces[4];
            }
            if (length > 5) {
                if ((t & 32) == 0) mask |= pieces[5];
            }
            if (length > 6) {
                if ((t & 64) == 0) mask |= pieces[6];
            }
            if (length > 7) {
                if ((t & 128) == 0) mask |= pieces[7];
            }
            if (length > 8) {
                if ((t & 256) == 0) mask |= pieces[8];
            }
            if (length > 9) {
                if ((t & 512) == 0) mask |= pieces[9];
            }
            if (length > 10) {
                if ((t & 1024) == 0) mask |= pieces[10];
            }
            if (length > 11) {
                if ((t & 2048) == 0) mask |= pieces[11];
            }
            answers[t] = mask;
        }
        return answers;
    }


}
