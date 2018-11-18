package chessprogram.graphicsandui;

import chessprogram.chessboard.Chessboard;

import static chessprogram.bitboards.BitManipulations.newPieceOnSquare;

public class Art {
    
    public static void main (String[] args){
        Chessboard board = new Chessboard();
        System.out.println("hi");
    }

    private static String pieceByNumberASCII(int s){
        if (s == 1) return ("P");
        if (s == 2) return ("N");
        if (s == 3) return ("B");
        if (s == 4) return ("R");
        if (s == 5) return ("Q");
        if (s == 6) return ("K");

        if (s == 7) return ("p");
        if (s == 8) return ("n");
        if (s == 9) return ("b");
        if (s == 10) return ("r");
        if (s == 11) return ("q");
        if (s == 12) return ("k");
        else return (".");
    }

    public static String boardArt (Chessboard board) {
        StringBuilder s = new StringBuilder();
        s.append("   a b c d e f g h\n");
        s.append("  +---------------+\n");
        for (int y = 7; y >= 0; y--) {
            s.append(y + 1).append(" |");
            for (int x = 7; x >= 0; x--) {
                s.append(pieceByNumberASCII(pieceOnSquare(board, x + y * 8)));
                if (x>0) s.append(" ");
            }
            s.append("| ").append(y + 1);
            s.append("\n");
        }
        s.append("  +---------------+\n");
        s.append("   a b c d e f g h\n");

        return s.toString();
    }


    public static String makeMoveToString (int l){
        String binaryString = Integer.toBinaryString(l);
        int numberOfPaddingZeros = 16 - binaryString.length();
        StringBuilder sb = new StringBuilder();
        while (sb.length() < numberOfPaddingZeros){
            sb.append("0");
        }

        String temp = sb.toString() + "" + binaryString;
        return temp.substring(0, 6) +"\n" +
                temp.substring(6, 12) + "\n" +
                temp.substring(12, 16);
    }

    public static void printLong(long l){
        for (int y = 0; y < 8; y++) {
            for (int i = 0; i < 8; i++) {
                StringBuilder s = new StringBuilder(Long.toBinaryString(l));
                while (s.length() < 64) {
                    s.insert(0, "0");
                }
                System.out.print(s.charAt(y * 8 + i));
            }
            System.out.println();
        }
        System.out.println("---");
    }

    private static int pieceOnSquare(Chessboard board, int s){
        long square = newPieceOnSquare(s);

        if ((square & board.getWhitePawns()) != 0) return 1;
        if ((square & board.getWhiteKnights()) != 0) return 2;
        if ((square & board.getWhiteBishops()) != 0) return 3;
        if ((square & board.getWhiteRooks()) != 0) return 4;
        if ((square & board.getWhiteQueen()) != 0) return 5;
        if ((square & board.getWhiteKing()) != 0) return 6;

        if ((square & board.getBlackPawns()) != 0) return 7;
        if ((square & board.getBlackKnights()) != 0) return 8;
        if ((square & board.getBlackBishops()) != 0)  return 9;
        if ((square & board.getBlackRooks()) != 0) return 10;
        if ((square & board.getBlackQueen()) != 0) return 11;
        if ((square & board.getBlackKing()) != 0) return 12;

        else return 0;
    }

}
