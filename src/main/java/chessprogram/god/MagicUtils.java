package chessprogram.god;

import java.util.ArrayList;
import java.util.List;

import static chessprogram.god.BitOperations.getAllPieces;
import static chessprogram.god.BitboardResources.rookBlankBoardAttackMasks;

public class MagicUtils {

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

    public static List<Long> singleBishopAllVariations(Chessboard board, long piece, boolean white, long legalPushes, long legalCaptures) {


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


        List<Long> allPossibleEffectiveBlockers = new ArrayList<>();

//        Art.printLong(upleft);
//        Art.printLong(upright);
//        Art.printLong(downleft);
//        Art.printLong(downright);

        final List<Long> allupLeft = getAllPieces(upleft, 0);
        final List<Long> allupRight = getAllPieces(upright, 0);
        final List<Long> alldownleft = getAllPieces(downleft, 0);
        final List<Long> alldownright = getAllPieces(downright, 0);

        final List<Long> permute = permuteBishop(allupLeft, allupRight, alldownleft, alldownright);

//        allLeft.add(0L);
//        allRight.add(0L);
//        allUp.add(0L);
//        allDown.add(0L);

        return permute;
    } 

    public static List<Long> singleRookAllVariations(Chessboard board, long piece, boolean white, long legalPushes, long legalCaptures){
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

//        Art.printLong(left);
//        Art.printLong(right);
//        Art.printLong(up);
//        Art.printLong(down);

        final List<Long> allLeft = getAllPieces(left, 0);
        final List<Long> allRight = getAllPieces(right, 0);
        final List<Long> allUp = getAllPieces(up, 0);
        final List<Long> allDown = getAllPieces(down, 0);

        final List<Long> permute = permuteRook(allLeft, allRight, allDown, allUp);

//        allLeft.add(0L);
//        allRight.add(0L);
//        allUp.add(0L);
//        allDown.add(0L);

        return permute;
    }

    public static List<Long> permuteBishop(List<Long> allLeft , List<Long> allRight , List<Long> allDown , List<Long> allUp ){
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
//            Art.printLong(mask);
            allMasks.add(mask);
        }
        return allMasks;
    }
    
    public static List<Long> permuteRook(List<Long> allLeft , List<Long> allRight , List<Long> allDown , List<Long> allUp ){
        final List<Long> allMasks = new ArrayList<>();
        final List<Long> allPieces = new ArrayList<>();
        allPieces.addAll(allLeft);
        allPieces.addAll(allRight);
        allPieces.addAll(allUp);
        allPieces.addAll(allDown);

        final int LIMIT = allPieces.size();
        final double size = Math.pow(2, LIMIT);
//        System.out.println((int) size);
        
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
//            Art.printLong(mask);
            allMasks.add(mask);
        }
        return allMasks;
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

//        Art.printLong(left);
//        Art.printLong(right);
//        Art.printLong(up);
//        Art.printLong(down);

        final List<Long> allLeft = getAllPieces(left, 0);
        final List<Long> allRight = getAllPieces(right, 0);
        final List<Long> allUp = getAllPieces(up, 0);
        final List<Long> allDown = getAllPieces(down, 0);

        allLeft.add(0L);
        allRight.add(0L);
        allUp.add(0L);
        allDown.add(0L);
        int total = 0;

//        System.out.println("l: "+allLeft.size()+", r: "+allRight.size()+", u: "+allUp.size()+ ", d: "+allDown.size());
        
        for (Long l : allLeft){
//            System.out.println("left ");
            for (Long r : allRight){
//                System.out.println("right");
                for (Long u : allUp){
//                    System.out.println("up");
                    for (Long d : allDown) {
//                        System.out.println("down");
                        total++;
                        allPossibleEffectiveBlockers.add(l | r | u | r);
                    }
                }
            }
        }

        for (Long blocker : allPossibleEffectiveBlockers){
//            Art.printLong(blocker);
        }
        
//        System.out.println("total: "+total);
        
        return allPossibleEffectiveBlockers;
    }


    public static long singleBishopAllMovesFromOcc(long blockers, long piece){
        long ALL_PIECES = blockers,
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
        return answer;
    }

    public static long singleRookAllMovesFromOcc (long blockers, long piece){
        long allPieces = blockers;
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
        long allPieces = blockers;
        long answer = 0;
        long temp = piece;
        while (true) {
            if ((temp & BitboardResources.FILE_A) != 0) break;
            if ((temp & BitboardResources.FILE_B) != 0) break;
            temp <<= 1;
            answer |= temp;
            if ((temp & allPieces) != 0) break;
        }
        temp = piece;
        while (true) {
            if ((temp & BitboardResources.FILE_H) != 0) break;
            if ((temp & BitboardResources.FILE_G) != 0) break;
            temp >>>= 1;
            answer |= temp;
            if ((temp & allPieces) != 0) break;
        }
        temp = piece;
        while (true) {
            if ((temp & BitboardResources.RANK_EIGHT) != 0) break;
            if ((temp & BitboardResources.RANK_SEVEN) != 0) break;
            temp <<= 8;
            answer |= temp;
            if ((temp & allPieces) != 0) break;
        }
        temp = piece;
        while (true) {
            if ((temp & BitboardResources.RANK_ONE) != 0) break;
            if ((temp & BitboardResources.RANK_TWO) != 0) break;
            temp >>>= 8;
            answer |= temp;
            if ((temp & allPieces) != 0) break;
        }
        return answer & (legalPushes | legalCaptures);
    }


    List<Long> blankBoardBishops() {
        List<Long> blankBoardAttacks = new ArrayList<>();

        for (int row = 0; row < 8; row++) {
            final long ROW = BitboardResources.ROWS[row];
            for (int file = 0; file < 8; file++) {
                final long FILE = BitboardResources.FILES[file];
                final long pieceOnSquare = ROW & FILE;
                final long l = MagicUtils.singleBishopAllMovesCleverer(board, pieceOnSquare, true, 0xffffffffffffffffL, 0xffffffffffffffffL);
                blankBoardAttacks.add(l);
            }
        }
        return blankBoardAttacks;
    }

    List<Long> blankBoardRooks() {
        List<Long> blankBoardAttacks = new ArrayList<>();
        for (int row = 0; row < 8; row++) {
            final long ROW = BitboardResources.ROWS[row];
            for (int file = 0; file < 8; file++) {
                final long FILE = BitboardResources.FILES[file];
                final long pieceOnSquare = ROW & FILE;
//                final long l = MagicUtils.singleRookAllMovesCleverer(board, pieceOnSquare, true, 0xffffffffffffffffL, 0xffffffffffffffffL);
//                blankBoardAttacks.add(l);
            }
        }
        return blankBoardAttacks;
    }


    static int num (Square sq){
        int x = 63-sq.ordinal();
        double xx = Math.pow(2, BitOperations.populationCount(rookBlankBoardAttackMasks[x]));
        return (int) xx;
    }
    
    
}
