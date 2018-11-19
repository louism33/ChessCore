package dummy;

import chessprogram.god.Art;
import chessprogram.god.Chessboard;
import chessprogram.god.Move;

public class Dummy {
    
    public static void main (String[] args){


        boardVisible();
        
        
        moveVisible();
        
    }
    
    
    static void boardVisible(){

        String fen = "r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq -";
        
        Chessboard board = new Chessboard();
//        System.out.println(board);
        
        Chessboard fb = new Chessboard(fen);
//        System.out.println(fb);
    }

    static void moveVisible(){
        Move move = new Move(10, 20);

        final String s = Art.makeMoveToStringTEMP(move.getMove());
        System.out.println(s);

//        final String s1 = Art.makeMoveToStringTEMP(QUEEN_PROMOTION_MASK);
//        System.out.println(s1);
//        final String s2 = Art.makeMoveToStringTEMP(1);
//        System.out.println(s2);
    }
}
