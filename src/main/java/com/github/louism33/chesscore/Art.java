package com.github.louism33.chesscore;

import static com.github.louism33.chesscore.BitOperations.newPieceOnSquare;

public class Art {

    public static String boardArt(Chessboard board) {
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

    private static String pieceByNumberASCII(int s){
        switch (s) {
            case 1: return ("P");
            case 2: return ("N");
            case 3: return ("B");
            case 4: return ("R");
            case 5: return ("Q");
            case 6: return ("K");
            case 7: return ("p");
            case 8: return ("n");
            case 9: return ("b");
            case 10: return ("r");
            case 11: return ("q");
            case 12: return ("k");
            default: return (".");
        }
    }

    public static String makeMoveToStringTEMP (int l){
        String binaryString = Integer.toBinaryString(l);
        int numberOfPaddingZeros = 32 - binaryString.length();
        StringBuilder sb = new StringBuilder();
        while (sb.length() < numberOfPaddingZeros){
            sb.append("0");
        }

        String temp = sb.toString() + "" + binaryString;
        return temp.substring(0, 4) +"\n" +
                temp.substring(4, 8) + "\n" +
                temp.substring(8, 12) +"\n" +
                temp.substring(12, 16) + "\n" +
                temp.substring(16, 20) +"\n" +
                temp.substring(20, 24) +"\n" +
                temp.substring(24, 28) +"\n" +
                temp.substring(28, 32) +"\n";
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
