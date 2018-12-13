package com.github.louism33.chesscore;

import java.util.ArrayList;
import java.util.List;

import static com.github.louism33.chesscore.BitOperations.getAllPieces;
import static com.github.louism33.chesscore.BitOperations.getIndexOfFirstPiece;
import static com.github.louism33.chesscore.BitboardResources.*;

class Setup {

    public static void main(String[] args){
        setup(false);
    }

    static void setup(boolean force){
        init(false);
    }

    static boolean ready = false;

    static void init(boolean force){
        if (!ready || force) {
            calculateRookDatabase();
            calculateBishopDatabase();
            calculateInBetweenSquares();
        }
        ready = true;
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
        long magic = bishopMagicNumbers[square.ordinal()];

        List<Long> bishopVariations = bishopVariations(square);
        long sq = square.toBitboard();
        long[] db = new long[(int) Math.pow(2, length)];

        for (Long variation : bishopVariations) {
            long correctBishopMovesResultBitboard = singleBishopAllMovesFromOcc(variation, sq);
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
        long magic = rookMagicNumbers[square.ordinal()];

        List<Long> rookVariations = rookVariations(square);
        long sq = square.toBitboard();
        long[] db = new long[(int) Math.pow(2, length)];

        for (Long variation : rookVariations) {
            long correctRookMovesResultBitboard = singleRookAllMovesFromOcc(variation, sq);
            long mult = (magic * variation);
            int index = (int) (mult >>> (64 - length));
            db[index] = correctRookMovesResultBitboard;
        }
        return db;
    }

    private static void calculateInBetweenSquares(){
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

    private static long singleBishopAllMovesFromOcc(long blockers, long piece){
        long NORTH_WEST = BitboardResources.FILE_A | BitboardResources.RANK_EIGHT,
                NORTH_EAST = BitboardResources.FILE_H | BitboardResources.RANK_EIGHT,
                SOUTH_WEST = BitboardResources.FILE_A | BitboardResources.RANK_ONE,
                SOUTH_EAST = BitboardResources.FILE_H | BitboardResources.RANK_ONE;

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
            if ((temp & BitboardResources.FILE_A) != 0) break;
            temp <<= 1;
            answer |= temp;
            if ((temp & blockers) != 0) break;
        }
        temp = piece;
        while (true) {
            if ((temp & BitboardResources.FILE_H) != 0) break;
            temp >>>= 1;
            answer |= temp;
            if ((temp & blockers) != 0) break;
        }
        temp = piece;
        while (true) {
            if ((temp & BitboardResources.RANK_EIGHT) != 0) break;
            temp <<= 8;
            answer |= temp;
            if ((temp & blockers) != 0) break;
        }
        temp = piece;
        while (true) {
            if ((temp & BitboardResources.RANK_ONE) != 0) break;
            temp >>>= 8;
            answer |= temp;
            if ((temp & blockers) != 0) break;
        }
        return answer;
    }

    public static long singleRookAllMoves(Chessboard board, long piece, boolean white, long legalPushes, long legalCaptures){
        long allPieces = board.whitePieces() | board.blackPieces();
        long answer = 0;
        long temp = piece;
        while (true) {
            if ((temp & BitboardResources.FILE_A) != 0) break;
            temp <<= 1;
            answer |= temp;
            if ((temp & allPieces) != 0) break;
        }
        temp = piece;
        while (true) {
            if ((temp & BitboardResources.FILE_H) != 0) break;
            temp >>>= 1;
            answer |= temp;
            if ((temp & allPieces) != 0) break;
        }
        temp = piece;
        while (true) {
            if ((temp & BitboardResources.RANK_EIGHT) != 0) break;
            temp <<= 8;
            answer |= temp;
            if ((temp & allPieces) != 0) break;
        }
        temp = piece;
        while (true) {
            if ((temp & BitboardResources.RANK_ONE) != 0) break;
            temp >>>= 8;
            answer |= temp;
            if ((temp & allPieces) != 0) break;
        }
        return answer & (legalPushes | legalCaptures);
    }

    public static long singleRookAllMovesCleverer(long blockers, long piece, boolean white, long legalPushes, long legalCaptures){
        long answer = 0;
        long temp = piece;
        while (true) {
            if ((temp & BitboardResources.FILE_A) != 0) break;
            if ((temp & BitboardResources.FILE_B) != 0) break;
            temp <<= 1;
            answer |= temp;
            if ((temp & blockers) != 0) break;
        }
        temp = piece;
        while (true) {
            if ((temp & BitboardResources.FILE_H) != 0) break;
            if ((temp & BitboardResources.FILE_G) != 0) break;
            temp >>>= 1;
            answer |= temp;
            if ((temp & blockers) != 0) break;
        }
        temp = piece;
        while (true) {
            if ((temp & BitboardResources.RANK_EIGHT) != 0) break;
            if ((temp & BitboardResources.RANK_SEVEN) != 0) break;
            temp <<= 8;
            answer |= temp;
            if ((temp & blockers) != 0) break;
        }
        temp = piece;
        while (true) {
            if ((temp & BitboardResources.RANK_ONE) != 0) break;
            if ((temp & BitboardResources.RANK_TWO) != 0) break;
            temp >>>= 8;
            answer |= temp;
            if ((temp & blockers) != 0) break;
        }
        return answer & (legalPushes | legalCaptures);
    }


    private static List<Long> singleRookAllVariations(Chessboard board, long piece, boolean white, long legalPushes, long legalCaptures){
        long allPieces = board.whitePieces() | board.blackPieces();
        long answer = 0;
        long temp = piece;

        long left = 0, right = 0, up = 0, down = 0;

        while (true) {
            if ((temp & BitboardResources.FILE_A) != 0) break;
            if ((temp & BitboardResources.FILE_B) != 0) break;
            temp <<= 1;
            answer |= temp;
            left |= temp;
            if ((temp & allPieces) != 0) break;
        }
        temp = piece;
        while (true) {
            if ((temp & BitboardResources.FILE_H) != 0) break;
            if ((temp & BitboardResources.FILE_G) != 0) break;
            temp >>>= 1;
            answer |= temp;
            right |= temp;
            if ((temp & allPieces) != 0) break;
        }
        temp = piece;
        while (true) {
            if ((temp & BitboardResources.RANK_EIGHT) != 0) break;
            if ((temp & BitboardResources.RANK_SEVEN) != 0) break;
            temp <<= 8;
            answer |= temp;
            up |= temp;
            if ((temp & allPieces) != 0) break;
        }
        temp = piece;
        while (true) {
            if ((temp & BitboardResources.RANK_ONE) != 0) break;
            if ((temp & BitboardResources.RANK_TWO) != 0) break;
            temp >>>= 8;
            answer |= temp;
            down |= temp;
            if ((temp & allPieces) != 0) break;
        }

        final List<Long> allLeft = getAllPieces(left, 0);
        final List<Long> allRight = getAllPieces(right, 0);
        final List<Long> allUp = getAllPieces(up, 0);
        final List<Long> allDown = getAllPieces(down, 0);

        return permuteRook(allLeft, allRight, allDown, allUp);
    }


    private static List<Long> permuteBishop(List<Long> allLeft, List<Long> allRight, List<Long> allDown, List<Long> allUp){
        final List<Long> allMasks = new ArrayList<>();
        final List<Long> allPieces = new ArrayList<>();
        allPieces.addAll(allLeft);
        allPieces.addAll(allRight);
        allPieces.addAll(allUp);
        allPieces.addAll(allDown);

        final int LIMIT = allPieces.size();
        final double size = Math.pow(2, LIMIT);

        for (int t = 0; t < size; t++){
            long mask = 0;
            if ((t & 1) == 0) mask |= allPieces.get(0);
            if (LIMIT > 1) {
                if ((t & 2) == 0) mask |= allPieces.get(1);
            }
            if (LIMIT > 2) {
                if ((t & 4) == 0) mask |= allPieces.get(2);
            }
            if (LIMIT > 3) {
                if ((t & 8) == 0) mask |= allPieces.get(3);
            }
            if (LIMIT > 4) {
                if ((t & 16) == 0) mask |= allPieces.get(4);
            }
            if (LIMIT > 5) {
                if ((t & 32) == 0) mask |= allPieces.get(5);
            }
            if (LIMIT > 6) {
                if ((t & 64) == 0) mask |= allPieces.get(6);
            }
            if (LIMIT > 7) {
                if ((t & 128) == 0) mask |= allPieces.get(7);
            }
            if (LIMIT > 8) {
                if ((t & 256) == 0) mask |= allPieces.get(8);
            }
            if (LIMIT > 9) {
                if ((t & 512) == 0) mask |= allPieces.get(9);
            }
            if (LIMIT > 10) {
                if ((t & 1024) == 0) mask |= allPieces.get(10);
            }
            if (LIMIT > 11) {
                if ((t & 2048) == 0) mask |= allPieces.get(11);
            }
            allMasks.add(mask);
        }
        return allMasks;
    }

    private static List<Long> permuteRook(List<Long> allLeft, List<Long> allRight, List<Long> allDown, List<Long> allUp){
        final List<Long> allMasks = new ArrayList<>();
        final List<Long> allPieces = new ArrayList<>();
        allPieces.addAll(allLeft);
        allPieces.addAll(allRight);
        allPieces.addAll(allUp);
        allPieces.addAll(allDown);

        final int LIMIT = allPieces.size();
        final double size = Math.pow(2, LIMIT);

        for (int t = 0; t < size; t++){
            long mask = 0;
            if ((t & 1) == 0) mask |= allPieces.get(0);
            if ((t & 2) == 0) mask |= allPieces.get(1);
            if ((t & 4) == 0) mask |= allPieces.get(2);
            if ((t & 8) == 0) mask |= allPieces.get(3);
            if ((t & 16) == 0) mask |= allPieces.get(4);
            if ((t & 32) == 0) mask |= allPieces.get(5);
            if ((t & 64) == 0) mask |= allPieces.get(6);
            if ((t & 128) == 0) mask |= allPieces.get(7);
            if ((t & 256) == 0) mask |= allPieces.get(8);
            if ((t & 512) == 0) mask |= allPieces.get(9);
            if (LIMIT > 10) {
                if ((t & 1024) == 0) mask |= allPieces.get(10);
            }
            if (LIMIT > 11) {
                if ((t & 2048) == 0) mask |= allPieces.get(11);
            }
            allMasks.add(mask);
        }
        return allMasks;
    }



    private Chessboard board = new Chessboard(true);

    public static long singleBishopAllMoves(Chessboard board, long piece, boolean white, long legalPushes, long legalCaptures){
        long ALL_PIECES = board.whitePieces() | board.blackPieces(),
                NORTH_WEST = BitboardResources.FILE_A | BitboardResources.RANK_EIGHT,
                NORTH_EAST = BitboardResources.FILE_H | BitboardResources.RANK_EIGHT,
                SOUTH_WEST = BitboardResources.FILE_A | BitboardResources.RANK_ONE,
                SOUTH_EAST = BitboardResources.FILE_H | BitboardResources.RANK_ONE;

        long answer = 0;
        long temp = piece;

        while (true) {
            if ((temp & NORTH_WEST) != 0) break;
            temp <<= 9;
            answer |= temp;
            if ((temp & ALL_PIECES) != 0) break;
        }
        temp = piece;
        while (true) {
            if ((temp & NORTH_EAST) != 0) break;
            temp <<= 7;
            answer |= temp;
            if ((temp & ALL_PIECES) != 0) break;
        }
        temp = piece;
        while (true) {
            if ((temp & SOUTH_WEST) != 0) break;
            temp >>>= 7;
            answer |= temp;
            if ((temp & ALL_PIECES) != 0) break;
        }
        temp = piece;
        while (true) {
            if ((temp & SOUTH_EAST) != 0) break;
            temp >>>= 9;
            answer |= temp;
            if ((temp & ALL_PIECES) != 0) break;
        }
        return answer & (legalPushes | legalCaptures);
    }


    public static long singleBishopAllMovesCleverer(Chessboard board, long piece, boolean white, long legalPushes, long legalCaptures){
        long ALL_PIECES = board.whitePieces() | board.blackPieces(),
                NORTH_WEST = BitboardResources.FILE_A | BitboardResources.RANK_EIGHT,
                NORTH_WEST1 = BitboardResources.FILE_B | BitboardResources.RANK_SEVEN,

                NORTH_EAST = BitboardResources.FILE_H | BitboardResources.RANK_EIGHT,
                NORTH_EAST1 = BitboardResources.FILE_G | BitboardResources.RANK_SEVEN,

                SOUTH_WEST = BitboardResources.FILE_A | BitboardResources.RANK_ONE,
                SOUTH_WEST1 = BitboardResources.FILE_B | BitboardResources.RANK_TWO,

                SOUTH_EAST = BitboardResources.FILE_H | BitboardResources.RANK_ONE,
                SOUTH_EAST1 = BitboardResources.FILE_G | BitboardResources.RANK_TWO;

        long answer = 0;
        long temp = piece;

        while (true) {
            if ((temp & NORTH_WEST) != 0) break;
            if ((temp & NORTH_WEST1) != 0) break;
            temp <<= 9;
            answer |= temp;
            if ((temp & ALL_PIECES) != 0) break;
        }
        temp = piece;
        while (true) {
            if ((temp & NORTH_EAST) != 0) break;
            if ((temp & NORTH_EAST1) != 0) break;
            temp <<= 7;
            answer |= temp;
            if ((temp & ALL_PIECES) != 0) break;
        }
        temp = piece;
        while (true) {
            if ((temp & SOUTH_WEST) != 0) break;
            if ((temp & SOUTH_WEST1) != 0) break;
            temp >>>= 7;
            answer |= temp;
            if ((temp & ALL_PIECES) != 0) break;
        }
        temp = piece;
        while (true) {
            if ((temp & SOUTH_EAST) != 0) break;
            if ((temp & SOUTH_EAST1) != 0) break;
            temp >>>= 9;
            answer |= temp;
            if ((temp & ALL_PIECES) != 0) break;
        }
        return answer & (legalPushes | legalCaptures);
    }

    private static List<Long> singleBishopAllVariations(Chessboard board, long piece, boolean white, long legalPushes, long legalCaptures) {


        long ALL_PIECES = board.whitePieces() | board.blackPieces(),
                NORTH_WEST = BitboardResources.FILE_A | BitboardResources.RANK_EIGHT,
                NORTH_WEST1 = BitboardResources.FILE_B | BitboardResources.RANK_SEVEN,

                NORTH_EAST = BitboardResources.FILE_H | BitboardResources.RANK_EIGHT,
                NORTH_EAST1 = BitboardResources.FILE_G | BitboardResources.RANK_SEVEN,

                SOUTH_WEST = BitboardResources.FILE_A | BitboardResources.RANK_ONE,
                SOUTH_WEST1 = BitboardResources.FILE_B | BitboardResources.RANK_TWO,

                SOUTH_EAST = BitboardResources.FILE_H | BitboardResources.RANK_ONE,
                SOUTH_EAST1 = BitboardResources.FILE_G | BitboardResources.RANK_TWO;

        long answer = 0;
        long temp = piece;

        long upleft = 0, downleft = 0, upright = 0, downright = 0;

        while (true) {
            if ((temp & NORTH_WEST) != 0) break;
            if ((temp & NORTH_WEST1) != 0) break;
            temp <<= 9;
            answer |= temp;
            upleft |= temp;
            if ((temp & ALL_PIECES) != 0) break;
        }
        temp = piece;
        while (true) {
            if ((temp & NORTH_EAST) != 0) break;
            if ((temp & NORTH_EAST1) != 0) break;
            temp <<= 7;
            answer |= temp;
            upright |= temp;
            if ((temp & ALL_PIECES) != 0) break;
        }
        temp = piece;
        while (true) {
            if ((temp & SOUTH_WEST) != 0) break;
            if ((temp & SOUTH_WEST1) != 0) break;
            temp >>>= 7;
            answer |= temp;
            downleft |= temp;
            if ((temp & ALL_PIECES) != 0) break;
        }
        temp = piece;
        while (true) {
            if ((temp & SOUTH_EAST) != 0) break;
            if ((temp & SOUTH_EAST1) != 0) break;
            temp >>>= 9;
            answer |= temp;
            downright |= temp;
            if ((temp & ALL_PIECES) != 0) break;
        }

        final List<Long> allupLeft = getAllPieces(upleft, 0);
        final List<Long> allupRight = getAllPieces(upright, 0);
        final List<Long> alldownleft = getAllPieces(downleft, 0);
        final List<Long> alldownright = getAllPieces(downright, 0);

        return permuteBishop(allupLeft, allupRight, alldownleft, alldownright);
    }




    public static List<Long> singleRookAllEffectiveBoards(Chessboard board, long piece, boolean white, long legalPushes, long legalCaptures){
        long allPieces = board.whitePieces() | board.blackPieces();
        long answer = 0;
        long temp = piece;

        long left = 0, right = 0, up = 0, down = 0;

        while (true) {
            if ((temp & BitboardResources.FILE_A) != 0) break;
            if ((temp & BitboardResources.FILE_B) != 0) break;
            temp <<= 1;
            answer |= temp;
            left |= temp;
            if ((temp & allPieces) != 0) break;
        }
        temp = piece;
        while (true) {
            if ((temp & BitboardResources.FILE_H) != 0) break;
            if ((temp & BitboardResources.FILE_G) != 0) break;
            temp >>>= 1;
            answer |= temp;
            right |= temp;
            if ((temp & allPieces) != 0) break;
        }
        temp = piece;
        while (true) {
            if ((temp & BitboardResources.RANK_EIGHT) != 0) break;
            if ((temp & BitboardResources.RANK_SEVEN) != 0) break;
            temp <<= 8;
            answer |= temp;
            up |= temp;
            if ((temp & allPieces) != 0) break;
        }
        temp = piece;
        while (true) {
            if ((temp & BitboardResources.RANK_ONE) != 0) break;
            if ((temp & BitboardResources.RANK_TWO) != 0) break;
            temp >>>= 8;
            answer |= temp;
            down |= temp;
            if ((temp & allPieces) != 0) break;
        }

        List<Long> allPossibleEffectiveBlockers = new ArrayList<>();

        final List<Long> allLeft = getAllPieces(left, 0);
        final List<Long> allRight = getAllPieces(right, 0);
        final List<Long> allUp = getAllPieces(up, 0);
        final List<Long> allDown = getAllPieces(down, 0);

        allLeft.add(0L);
        allRight.add(0L);
        allUp.add(0L);
        allDown.add(0L);
        int total = 0;

        for (int i = 0; i < allLeft.size(); i++) {
            long l = allLeft.get(i);
            for (int i1 = 0; i1 < allRight.size(); i1++) {
                long r = allRight.get(i1);
                for (int i2 = 0; i2 < allUp.size(); i2++) {
                    long u = allUp.get(i2);
                    for (int i3 = 0; i3 < allDown.size(); i3++) {
                        long d = allDown.get(i3);
                        total++;
                        allPossibleEffectiveBlockers.add(l | r | u | r);
                    }
                }
            }
        }

        return allPossibleEffectiveBlockers;
    }




}
