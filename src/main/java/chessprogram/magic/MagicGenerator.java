package chessprogram.magic;

import chessprogram.god.BitboardResources;
import chessprogram.god.Chessboard;
import chessprogram.god.Square;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static chessprogram.god.BitOperations.UNIVERSE;
import static chessprogram.god.BitOperations.populationCount;
import static chessprogram.god.Square.getPiecesOnSquare;
import static chessprogram.magic.MagicConstants.bishopShiftAmounts;
import static chessprogram.magic.Utils.*;

public class MagicGenerator {


    // h1, g1 ... a7

    // attack boards
    public static long[] rooksBlankClever = new long[]{
            282578800148862L, 565157600297596L, 1130315200595066L, 2260630401190006L, 4521260802379886L, 9042521604759646L, 18085043209519166L, 36170086419038334L, 282578800180736L, 565157600328704L, 1130315200625152L, 2260630401218048L, 4521260802403840L, 9042521604775424L, 18085043209518592L, 36170086419037696L, 282578808340736L, 565157608292864L, 1130315208328192L, 2260630408398848L, 4521260808540160L, 9042521608822784L, 18085043209388032L, 36170086418907136L, 282580897300736L, 565159647117824L, 1130317180306432L, 2260632246683648L, 4521262379438080L, 9042522644946944L, 18085043175964672L, 36170086385483776L, 283115671060736L, 565681586307584L, 1130822006735872L, 2261102847592448L, 4521664529305600L, 9042787892731904L, 18085034619584512L, 36170077829103616L, 420017753620736L, 699298018886144L, 1260057572672512L, 2381576680245248L, 4624614895390720L, 9110691325681664L, 18082844186263552L, 36167887395782656L, 35466950888980736L, 34905104758997504L, 34344362452452352L, 33222877839362048L, 30979908613181440L, 26493970160820224L, 17522093256097792L, 35607136465616896L, 9079539427579068672L, 8935706818303361536L, 8792156787827803136L, 8505056726876686336L, 7930856604974452736L, 6782456361169985536L, 4485655873561051136L, 9115426935197958144L,
    };

    public static long[] bishopBlankClever = new long[]{
            18049651735527936L, 70506452091904L, 275415828992L, 1075975168L, 38021120L, 8657588224L, 2216338399232L, 567382630219776L, 9024825867763712L, 18049651735527424L, 70506452221952L, 275449643008L, 9733406720L, 2216342585344L, 567382630203392L, 1134765260406784L, 4512412933816832L, 9024825867633664L, 18049651768822272L, 70515108615168L, 2491752130560L, 567383701868544L, 1134765256220672L, 2269530512441344L, 2256206450263040L, 4512412900526080L, 9024834391117824L, 18051867805491712L, 637888545440768L, 1135039602493440L, 2269529440784384L, 4539058881568768L, 1128098963916800L, 2256197927833600L, 4514594912477184L, 9592139778506752L, 19184279556981248L, 2339762086609920L, 4538784537380864L, 9077569074761728L, 562958610993152L, 1125917221986304L, 2814792987328512L, 5629586008178688L, 11259172008099840L, 22518341868716544L, 9007336962655232L, 18014673925310464L, 2216338399232L, 4432676798464L, 11064376819712L, 22137335185408L, 44272556441600L, 87995357200384L, 35253226045952L, 70506452091904L, 567382630219776L, 1134765260406784L, 2832480465846272L, 5667157807464448L, 11333774449049600L, 22526811443298304L, 9024825867763712L, 18049651735527936L
    };

    public static void main (String[] args){
//        new MagicGenerator();
        
        for (int i = 63; i >= 0; i--){
            Square square = Square.values()[i];
            System.out.println("SQUARE: "+ square);
            calculateMagicNoWrite(square);
        }

        for (int i = 0; i < 64; i++){
            System.out.print(bishopMagics[i]+"L,");
            if (i % 8 == 7){
                System.out.println();
            }
        }
    }

    static final Chessboard board = new Chessboard(true);

    MagicGenerator(){

        final List<List<Long>> lists = forEachSquareCalculateVariations();

        for (int i = 0; i < lists.size(); i++) {
            List<Long> list = lists.get(i);
            String rank = "" + (8 - (i / 8));
            String file = ""+ (char) ('A' + ((i % 8)));
            String square = file+""+rank;
            String url = String.format("/home/louis/IdeaProjects/ChessCore/src/main/java/chessprogram/rookvar/rookVariations%s.txt", square);

//            printToFileSingle(list, url);
        }


        // A8 - B8 ---- A7 ------- H1
        for (int i = 0; i < lists.size(); i++) {
            List<Long> list = lists.get(i);

//            String url = "/home/louis/IdeaProjects/ChessCore/src/main/java/chessprogram/magic/H1.txt";

            String rank = "" + (8 - (i / 8));
            String file = ""+ (char) ('A' + ((i % 8)));
            String square = file+""+rank;

            String readFromHere = String.format("/home/louis/IdeaProjects/ChessCore/src/main/java/chessprogram/rookvar/rookVariations%s.txt", square);

//            String recordHere = String.format("/home/louis/IdeaProjects/ChessCore/src/main/java/chessprogram/rookdb/RookDatabase%s.java", square);

            String recordHere = String.format("/home/louis/IdeaProjects/ChessCore/src/main/java/chessprogram/rookdb/RookDatabaseMaster.java");

            String className = "RookDatabase"+square;
            int lengthOfArray = populationCount(list.get(0));

//            System.out.println("XXXXXXXXXXx");
//            System.out.println(readFromHere);
//            System.out.println(recordHere);
//            System.out.println("square : "+square);
//            Art.printLong(list.get(0));

//            initDatabaseMaster(recordHere, lengthOfArray);
//            initDatabase(recordHere, className, square, lengthOfArray);

            plop(readFromHere, recordHere, className, square, lengthOfArray);

//            writeMagic(rookMagics);
//            writeDB(rookMoves, recordHere);
        }
    }

    void writeDB(long[][] db, String url){
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(url));

            writer.write("package chessprogram.rookdb;\n"+
                    "\n" +
                    "public class RookDatabaseMaster {\n" +
                    "    \n" +
                    "    public static long[][] rookMovesDatabase = new long[][]{" +
                    "");

            for (int i = 0; i < db.length; i++) {
                String s = "{";
                for (int j = 0; j < db[i].length; j++){
                    final long l = db[i][j];
                    s += ""+l+"L, ";
                }
                writer.write(s+"\n}");
            }

            writer.write("};\n}");

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static long[] rookMagics = new long[64];
    private static long[] bishopMagics = new long[64];
    private static long[][] rookMoves = new long[64][];

    void calculateMagic(List<String> s, String urlUrl, String className, String square, int length) {

        long[] db = new long[(int) Math.pow(2, length)];

        final List<Long> variations = s.stream()
                .map(Long::parseLong)
                .collect(Collectors.toList());

        int total = 0;
        boolean success = false;
        int stopAt = 1_000_000_000;
        List<Integer> successRecorder = new ArrayList<>();

        final Square sqqq = Square.valueOf(square);

        final int ordinal = sqqq.ordinal();

//        System.out.println("square :" +square);
//        System.out.println("sqqq: " + sqqq);
//        System.out.println("to bit: ");
//        Art.printLong(sqqq.toBitboard());
//        System.out.println("ordinal: "+ ordinal);

//        long sq = sqqq.toBitboard();
        long sq = sqqq.toBitboard();

//        System.out.println("sq: ");
//        System.out.println();
//        Art.printLong(sq);

        long correctMagic = 0;

        newMagic:
        while (total < stopAt) {
            Random r = new Random(total);
            long magicNum = r.nextLong() & r.nextLong() & r.nextLong();

            db = new long[(int) Math.pow(2, length)];

            int successes = 0;
            for (Long variation : variations) {

                long correctRookMovesResultBitboard = Utils.singleRookAllMovesFromOcc(variation, sq);

                long mult = (magicNum * variation);
                int index = (int) (mult >>> (64 - length));
                long indexEntry = db[index];

                if (indexEntry == 0){
                    successes++;
                    db[index] = correctRookMovesResultBitboard;
                }

                else if ((indexEntry == correctRookMovesResultBitboard)) {
                    successes++;
                }
                else {
                    //clash
                    successRecorder.add(successes);
                    total++;
                    continue newMagic;
                }

                correctMagic = magicNum;
                success = true;
            }
//            System.out.println("break");


            break;
        }
        if (success) {

            System.out.println("success on square : " + square);
            System.out.println("number of magics tried: " + (total+1));
            System.out.println(urlUrl);


            recordDatabase(db, urlUrl, className, square, length);

            // careful of ordinal
            rookMagics[ordinal] = correctMagic;
        }
        else {
            System.out.println("failure");
            System.out.println("number of vars: " + variations.size());
            System.out.println("number of magics tried: " + successRecorder.size());
        }
    }


    static void calculateMagicNoWrite(Square square) {

        int length = bishopShiftAmounts[square.ordinal()];
        boolean success = false;
        long[] db;
        int stopAt = 1000000;
        long correctMagic = 0;
        int total = 0;

        List<Long> bishopVariations = bishopVariations(square);
        
        newMagic:
        while (total < stopAt) {
            Random r = new Random(total);
            long magicNum = r.nextLong() & r.nextLong() & r.nextLong();

            db = new long[(int) Math.pow(2, length)];


            long sq = square.toBitboard();

            for (Long variation : bishopVariations) {
                long correctBishopMovesResultBitboard = Utils.singleBishopAllMovesFromOcc(variation, sq);
                long mult = (magicNum * variation);
                int index = (int) (mult >>> (64 - length));

                long indexEntry = db[index];
                if (indexEntry == 0) {
                    db[index] = correctBishopMovesResultBitboard;
                    success = true;
                }
                else if (indexEntry == correctBishopMovesResultBitboard) {
                    success = true;
                }
                else {
                    total++;
                    success = false;
//                    System.out.println("clash");
                    continue newMagic;
                }
            }
            
            if (success){
                correctMagic = magicNum;
            }
            
            break;
        }

        if (success) {
            System.out.println("success on square : " + square);
            System.out.println("number of magics tried: " + (total + 1));
            bishopMagics[square.ordinal()] = correctMagic;
        }
        else {
            return;
        }
    }


    void writeMagic(long[] magics){
        String url = String.format("/home/louis/IdeaProjects/ChessCore/src/main/java/chessprogram/magic/RookMagics.java");
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(url));

            writer.write("package chessprogram.magic;\n" +
                    "\n" +
                    "public class RookMagics {\n" +
                    "    \n" +
                    "    public static long[] rookMagicNumbers = new long[]{\n");

            for (int i = 0; i < magics.length; i++) {
                Long aLong = magics[i];
                String s = "" + aLong + "L,\n";
                writer.write(s);
            }

            writer.write("};\n}");

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void initDatabaseMaster(String urlUrl, int length){
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(urlUrl));

            length = (int) Math.pow(2, length);

            writer.write(getDBStringMaster(urlUrl)+""+64+"][];\n}");

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static String getDBStringMaster(String urlUrl){
        return ("package chessprogram.rookdb;\n" +
                "public class RookDatabaseMaster {\n" +
                "public static long[][] rookMovesDatabase = new long[");
    }

    void recordDatabaseMaster(long[] correctDB, String urlUrl, String className, String square, int length){
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(urlUrl));
            writer.write(getDBStringMaster(urlUrl)+"]{\n");
            for (int i = 0; i < correctDB.length; i++) {
                Long aLong = correctDB[i];
                String s = "" + aLong + "L,\n";
                writer.write(s);
            }
            writer.append("};\n}");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void initDatabase(String urlUrl, String className, String square, int length){
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(urlUrl));

            length = (int) Math.pow(2, length);

            writer.write(getDBString(urlUrl, className, square)+""+length+"];\n}");

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static String getDBString(String urlUrl, String className, String square){
        return ("package chessprogram.rookdb;\n" +
                "public class "+className+" {\n" +
                "public static long[] rook"+square+" = new long[");
        //+length+"];\n}");
    }

    void recordDatabase(long[] correctDB, String urlUrl, String className, String square, int length){
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(urlUrl));
            writer.write(getDBString(urlUrl, className, square)+"]{\n");
            for (int i = 0; i < correctDB.length; i++) {
                Long aLong = correctDB[i];
                String s = "" + aLong + "L,\n";
                writer.write(s);
            }
            writer.append("};\n}");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    private void plop(String readFromHere, String writeToHere, String className, String square, int lengthOfArray) {
        try {
            final List<String> strings = Files.readAllLines(Paths.get(readFromHere));
            calculateMagic(strings, writeToHere, className, square, lengthOfArray);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    List<List<Long>> forEachSquareCalculateVariations() {
        List<List<Long>> allBlockersForAllSquares = new ArrayList<>();
        final Square[] values = Square.values();
        for (int i = 0; i < values.length; i++) {
            Square square = values[i];
            allBlockersForAllSquares.add(rookVariations(square));
        }
        return allBlockersForAllSquares;
    }

    List<Long> rookVariations(Square square){
        return singleRookAllVariations(board, square.toBitboard(), true, UNIVERSE, 0);
    }

    static List<Long> bishopVariations(Square square){
        return singleBishopAllVariations(board, square.toBitboard(), true, UNIVERSE, 0);
    }

    void printToFileSingle(List<Long> things, String url){
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(url));
            for (int i = 0; i < things.size(); i++) {
                Long aLong = things.get(i);
                String s = "" + aLong + "\n";
                writer.write(s);
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    void printToFile(List<List<Long>> things, String url){
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(url));
            for (List<Long> list : things) {
                for (int l = 0; l < list.size(); l++) {
                    final Long aLong = list.get(l);
                    writer.write(aLong+"L\n");
                }
                writer.write("\n");
            }
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    String printHelperRook(List<List<Long>> things){
        StringBuilder s = new StringBuilder("public static long[][] rookDatabase = new long[][]{");
        for (int i = 0; i < things.size(); i++) {
            final List<Long> longs = things.get(i);
            s.append("{");
            for (Long l : longs) {
                s.append(l);
                s.append("L, ");
            }
            s.append("},\n");
        }
        s.append("\n};");
        return s.toString();
    }


    List<List<Long>> calculateMovesForTheseBlockersRook(List<List<Long>> uniqueBlockers){
        List<List<Long>> correctMoveForAllBlockers = new ArrayList<>();

        for (int i = 0; i < uniqueBlockers.size(); i++) {
            List<Long> squareBlockers = uniqueBlockers.get(i);
            Square square = Square.values()[i];

            List<Long> correctMoveForBlocker = new ArrayList<>();
            for (Long particularBlockerSetup : squareBlockers) {
                final long l = Utils.singleRookAllMovesCleverer(particularBlockerSetup, square.toBitboard(), true, UNIVERSE, 0);
                correctMoveForBlocker.add(l);
            }
            correctMoveForAllBlockers.add(correctMoveForBlocker);
        }
        return correctMoveForAllBlockers;
    }

    List<List<Long>> calculateUniqueRookBlockers() {
        List<List<Long>> allBlockersForAllSquares = new ArrayList<>();
        final Square[] values = Square.values();
        for (Square square : values){
            final List<Long> longs = singleRookAllEffectiveBoards(board, square.toBitboard(), true, UNIVERSE, 0);
            allBlockersForAllSquares.add(longs);
        }
        return allBlockersForAllSquares;
    }


    void rookOccupancyBlockerBoard(Chessboard board, Square square){
        final long rookAttack = rooksBlankClever[square.ordinal()];
        final long occupancyBoard = rookAttack & board.allPieces();
//        Art.printLong(occupancyBoard);
    }


    void rookOccupancyBlockerBoard(Chessboard board, long p){
        final List<Square> piecesOnSquare = getPiecesOnSquare(p);

        for (Square piece : piecesOnSquare){
            rookOccupancyBlockerBoard(board, piece);
        }

    }

    List<Long> findMagic(){
        Random r = new Random(1000);
        List<Long> answers = new ArrayList<>();
        for (int row = 0; row < 8; row++){
            final long ROW = BitboardResources.ROWS[row];
            for (int file = 0; file < 8; file++){
                final long FILE = BitboardResources.FILES[file];
                getSquareVariations(ROW, FILE);
            }
        }
        return answers;
    }


    // 16384 each
    static List<Long> getSquareVariations(long rank, long file){
        List<Long> variations = new ArrayList<>();
        final long baseSquare = rank & file;

        final double num = Math.pow(2, 7);

        int i = 0;
        // 
        for (int r = 0; r < num; r++){
            for (int c = 0; c < num; c++){
                i++;
            }
        }


        return variations;
    }


    private static long firstBit = 0x8000000000000000L;

    long destinationMagic(long l){
        final int i = populationCount(l);
        long destinationMagic = 0x8000000000000000L;
        for (int s = 0; s < i; s++){
            destinationMagic |= (firstBit >>> s);
        }
        return destinationMagic;
    }

}
