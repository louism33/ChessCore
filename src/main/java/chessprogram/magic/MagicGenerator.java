package chessprogram.magic;

import chessprogram.god.*;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static chessprogram.god.BitOperations.*;
import static chessprogram.god.Square.*;
import static chessprogram.magic.Utils.*;

public class MagicGenerator {


    // h1, g1 ... a7
    private static long[] rooksBlankFull = new long[]{
            72340172838076926L, 144680345676153597L, 289360691352306939L, 578721382704613623L, 1157442765409226991L, 2314885530818453727L, 4629771061636907199L, -9187201950435737473L, 72340172838141441L, 144680345676217602L, 289360691352369924L, 578721382704674568L, 1157442765409283856L, 2314885530818502432L, 4629771061636939584L, -9187201950435737728L, 72340172854657281L, 144680345692602882L, 289360691368494084L, 578721382720276488L, 1157442765423841296L, 2314885530830970912L, 4629771061645230144L, -9187201950435803008L, 72340177082712321L, 144680349887234562L, 289360695496279044L, 578721386714368008L, 1157442769150545936L, 2314885534022901792L, 4629771063767613504L, -9187201950452514688L, 72341259464802561L, 144681423712944642L, 289361752209228804L, 578722409201797128L, 1157443723186933776L, 2314886351157207072L, 4629771607097753664L, -9187201954730704768L, 72618349279904001L, 144956323094725122L, 289632270724367364L, 578984165983651848L, 1157687956502220816L, 2315095537539358752L, 4629910699613634624L, -9187203049947365248L, 143553341945872641L, 215330564830528002L, 358885010599838724L, 645993902138460168L, 1220211685215703056L, 2368647251370188832L, 4665518383679160384L, -9187483425412448128L, -143832609275707135L, -215607624513486334L, -359157654989044732L, -646257715940161528L, -1220457837842395120L, -2368858081646862304L, -4665658569255796672L, 9187484529235886208L
    };

    private long[] bishopsBlankFull = new long[]{
            -9205322385119247872L, 36099303471056128L, 141012904249856L, 550848566272L, 6480472064L, 1108177604608L, 283691315142656L, 72624976668147712L, 4620710844295151618L, -9205322385119182843L, 36099303487963146L, 141017232965652L, 1659000848424L, 283693466779728L, 72624976676520096L, 145249953336262720L, 2310355422147510788L, 4620710844311799048L, -9205322380790986223L, 36100411639206946L, 424704217196612L, 72625527495610504L, 145249955479592976L, 290499906664153120L, 1155177711057110024L, 2310355426409252880L, 4620711952330133792L, -9205038694072573375L, 108724279602332802L, 145390965166737412L, 290500455356698632L, 580999811184992272L, 577588851267340304L, 1155178802063085600L, 2310639079102947392L, 4693335752243822976L, -9060072569221905919L, 326598935265674242L, 581140276476643332L, 1161999073681608712L, 288793334762704928L, 577868148797087808L, 1227793891648880768L, 2455587783297826816L, 4911175566595588352L, -8624392940535152127L, 1197958188344280066L, 2323857683139004420L, 144117404414255168L, 360293502378066048L, 720587009051099136L, 1441174018118909952L, 2882348036221108224L, 5764696068147249408L, -6917353036926680575L, 4611756524879479810L, 567382630219904L, 1416240237150208L, 2833579985862656L, 5667164249915392L, 11334324221640704L, 22667548931719168L, 45053622886727936L, 18049651735527937L
    };

    // attack boards
    public static long[] rooksBlankClever = new long[]{
            282578800148862L, 565157600297596L, 1130315200595066L, 2260630401190006L, 4521260802379886L, 9042521604759646L, 18085043209519166L, 36170086419038334L, 282578800180736L, 565157600328704L, 1130315200625152L, 2260630401218048L, 4521260802403840L, 9042521604775424L, 18085043209518592L, 36170086419037696L, 282578808340736L, 565157608292864L, 1130315208328192L, 2260630408398848L, 4521260808540160L, 9042521608822784L, 18085043209388032L, 36170086418907136L, 282580897300736L, 565159647117824L, 1130317180306432L, 2260632246683648L, 4521262379438080L, 9042522644946944L, 18085043175964672L, 36170086385483776L, 283115671060736L, 565681586307584L, 1130822006735872L, 2261102847592448L, 4521664529305600L, 9042787892731904L, 18085034619584512L, 36170077829103616L, 420017753620736L, 699298018886144L, 1260057572672512L, 2381576680245248L, 4624614895390720L, 9110691325681664L, 18082844186263552L, 36167887395782656L, 35466950888980736L, 34905104758997504L, 34344362452452352L, 33222877839362048L, 30979908613181440L, 26493970160820224L, 17522093256097792L, 35607136465616896L, 9079539427579068672L, 8935706818303361536L, 8792156787827803136L, 8505056726876686336L, 7930856604974452736L, 6782456361169985536L, 4485655873561051136L, 9115426935197958144L,
    };

    public static long[] bishopBlankClever = new long[]{
            18049651735527936L, 70506452091904L, 275415828992L, 1075975168L, 38021120L, 8657588224L, 2216338399232L, 567382630219776L, 9024825867763712L, 18049651735527424L, 70506452221952L, 275449643008L, 9733406720L, 2216342585344L, 567382630203392L, 1134765260406784L, 4512412933816832L, 9024825867633664L, 18049651768822272L, 70515108615168L, 2491752130560L, 567383701868544L, 1134765256220672L, 2269530512441344L, 2256206450263040L, 4512412900526080L, 9024834391117824L, 18051867805491712L, 637888545440768L, 1135039602493440L, 2269529440784384L, 4539058881568768L, 1128098963916800L, 2256197927833600L, 4514594912477184L, 9592139778506752L, 19184279556981248L, 2339762086609920L, 4538784537380864L, 9077569074761728L, 562958610993152L, 1125917221986304L, 2814792987328512L, 5629586008178688L, 11259172008099840L, 22518341868716544L, 9007336962655232L, 18014673925310464L, 2216338399232L, 4432676798464L, 11064376819712L, 22137335185408L, 44272556441600L, 87995357200384L, 35253226045952L, 70506452091904L, 567382630219776L, 1134765260406784L, 2832480465846272L, 5667157807464448L, 11333774449049600L, 22526811443298304L, 9024825867763712L, 18049651735527936L
    };
    
    /*
     magic move-bitboard generation technique consists of four key steps:

Mask the relevant occupancy bits to form a key. For example if you had a rook on a1, the relevant occupancy bits will be from a2-a7 and b1-g1.
Multiply the key by a "magic number" to obtain an index mapping. This magic number can be generated by brute-force trial and error quite easily although it isn't 100% certain that the magic number is the best possible (see step 3).
Right shift the index mapping by 64-n bits to create an index, where n is the number of bits in the index. A better magic number will have less bits required in the index.
Use the index to reference a preinitialized move database.
The following illustration should give an impression, how magic bitboards work. All masked relevant occupied bits are perfectly hashed to the consecutive occupied state to index the pre-calculated attack-sets. Constructive collisions, where different occupancies map same attack-sets - since different bits are outer redundant bits "behind" the first blocker, are desired and even necessary to apply a perfect hashing with N bits.

((attack-bitboards-for-rooks[a1] AND occupancy) * magic-number[a1]) >> (64 - bits-required[a1])
     */

    public static void main (String[] args){
        new MagicGenerator();
    }

    final Chessboard board = new Chessboard(true);

    MagicGenerator(){



//        rookOccupancyBlockerBoard(board, board.getWhiteRooks());
//        findMagic();

//        calculateUniqueRookBlockers(H8);

//        final long piece = B4.toBitboard();
//        final List<Long> longs = singleRookAllEffectiveBoards(board, piece, board.isWhiteTurn(), UNIVERSE, 0);

//        final List<List<Long>> lists = calculateUniqueRookBlockers();
//        final List<List<Long>> correctRookMoves = calculateMovesForTheseBlockersRook(lists);
//        System.out.println(correctRookMoves);
//        System.out.println(correctRookMoves.size());

//        final String s = printHelperRook(correctRookMoves);
//        System.out.println(s);
        final long[][] rookDatabase = Databases.rookDatabase;
        for (int i = 0; i < rookDatabase.length; i++){
//            System.out.println(rookDatabase[i].length);
        }

        final List<List<Long>> lists = forEachSquareCalculateVariations();
        printToFile(lists);
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


    void printToFile(List<List<Long>> things){
        try {
            FileWriter writer = new FileWriter("RookVariations.java");
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
            writer.write(s.toString());
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

    //todo
    public static long[] rookSlidingMovesByIndex = new long[]{

    };




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

//        Art.printLong(baseSquare);
        final double num = Math.pow(2, 7);

        int i = 0;
        // 
        for (int r = 0; r < num; r++){
            for (int c = 0; c < num; c++){
                i++;
            }
        }


        System.out.println(i);

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
