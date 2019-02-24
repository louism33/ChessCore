package com.github.louism33.chesscore;

class MovePrettifier {

    static String prettyMove(int move){
        if (move == 0) {
            return ".";
        }
        int sourceAsPiece = MoveParser.getSourceIndex(move);
        String file = getFile(sourceAsPiece);
        String rank = getRank(sourceAsPiece);
        int destination = MoveParser.getDestinationIndex(move);
        String destinationFile = getFile(destination);
        String destinationRank = getRank(destination);
        String m = ""+file+ rank+destinationFile+ destinationRank;

        if (MoveParser.isPromotionMove(move)){

            if (MoveParser.isPromotionToKnight(move)){
                m += "N";
            }
            else if (MoveParser.isPromotionToBishop(move)){
                m += "B";
            }
            else if (MoveParser.isPromotionToRook(move)){
                m += "R";
            }
            else if (MoveParser.isPromotionToQueen(move)){
                m += "Q";
            }
        }
        return m;
    }
    
    private static String getRank(int square){
        return (square / 8 + 1) + "";
    }

    private static String getFile(int square){
        int i = square % 8;
        String file = "";
        switch (i){
            case 0: 
                file = "h";
                break;
            case 1:
                file = "g";
                break;
            case 2:
                file = "f";
                break;
            case 3:
                file = "e";
                break;
            case 4:
                file = "d";
                break;
            case 5:
                file = "c";
                break;
            case 6:
                file = "b";
                break;
            case 7:
                file = "a";
                break;
        }
        return file;
    }
}
