package dummy;

import chessprogram.god.Chessboard;


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
//        System.out.println(s);


    }
}
