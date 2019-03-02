package com.github.louism33.chesscore;

public class Art {

    public static String boardArt(Chessboard board) {
        StringBuilder s = new StringBuilder(512);
        s.append("   a b c d e f g h\n");
        s.append("  +---------------+\n");
        for (int y = 7; y >= 0; y--) {
            s.append(y + 1).append(" |");
            for (int x = 7; x >= 0; x--) {
                s.append(pieceByNumberASCII(board.pieceSquareTable[x + y * 8]));
                if (x>0) s.append(' ');
            }
            s.append("| ").append(y + 1);
            s.append('\n');
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
        StringBuilder sb = new StringBuilder(32);
        while (sb.length() < numberOfPaddingZeros){
            sb.append('0');
        }

        String temp = sb + binaryString;
        return temp.substring(0, 4) + '\n' +
                temp.substring(4, 8) + '\n' +
                temp.substring(8, 12) + '\n' +
                temp.substring(12, 16) + '\n' +
                temp.substring(16, 20) + '\n' +
                temp.substring(20, 24) + '\n' +
                temp.substring(24, 28) + '\n' +
                temp.substring(28, 32) + '\n';
    }

    public static void printLong(long l){
        for (int y = 0; y < 8; y++) {
            for (int i = 0; i < 8; i++) {
                StringBuilder s = new StringBuilder(Long.toBinaryString(l));
                while (s.length() < 64) {
                    s.insert(0, '0');
                }
                System.out.print(s.charAt(y * 8 + i));
            }
            System.out.println();
        }
        System.out.println("---");
    }

    public static String pieceSquareTable(int[] pieceSquareTable){
        StringBuilder s = new StringBuilder(128);
        for (int i = 7; i >= 0; i--) {
            s.append("   ");
            for (int j = 7; j >= 0; j--) {
                int i1 = pieceSquareTable[i * 8 + j];
                String str = i1 == 0 ? "." : Integer.toHexString(i1);
                s.append(str).append(' ');
//                System.out.print(String.format("%4d", INITIAL_PIECE_SQUARES[i * 8 + j]));
            }
            s.append('\n');
        }
        return s.toString();
    }
}
