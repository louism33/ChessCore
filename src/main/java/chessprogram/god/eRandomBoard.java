package chessprogram.god;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class eRandomBoard {

    public static void printBoards (Chessboard[] bs){
        for (int i = 0; i < bs.length; i++) {
            Chessboard b = bs[i];
            System.out.println("--------- " + i + " -------------");
            System.out.println("\n--- " + i + " ---");
            String s = Art.boardArt(bs[i]);
            System.out.println(s);
        }
    }
    
    public static Chessboard[] boardForTests (){
        int num = 18;
        Chessboard[] bs = new Chessboard[num];
        for (int i = 0; i < num; i ++) {
            if (i > 0 && i % 3 == 0) {
                bs[i] = eRandomBoard.makeRandomBoard(i * 100);
            }
            else {
                bs[i] = eRandomBoard.makeRandomBoard(i);
            }
        }
        return bs;
    }
    
    private static List<Long> randomLongs(Random r, int num){
        List<Long> ans = new ArrayList<>();
        int i = 0;
        while (i < 12){
            int j = r.nextInt(63);
            double pow = Math.pow(2, j);
            long l = (long) pow;

            if (ans.contains(l)) {
                continue;
            }
            ans.add(l);
            i++;
        }
        return ans;
    }

    private static Chessboard makeRandomBoard(long seed){
        Chessboard board = new Chessboard();
        Random r = new Random(seed);
        int ii = 12;
        List<Long> longs = randomLongs(r, ii);

        board.setWhitePawns(longs.get(0));
        board.setWhiteKnights(longs.get(1));
        board.setWhiteBishops(longs.get(2));
        board.setWhiteRooks(longs.get(3));
        board.setWhiteQueen(longs.get(4));
        board.setWhiteKing(longs.get(5));

        board.setBlackPawns(longs.get(6));
        board.setBlackKnights(longs.get(7));
        board.setBlackBishops(longs.get(8));
        board.setBlackRooks(longs.get(9));
        board.setBlackQueen(longs.get(10));
        board.setBlackKing(longs.get(11));

        return board;
    }


}
