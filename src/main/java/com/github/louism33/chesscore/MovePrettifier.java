package com.github.louism33.chesscore;

class MovePrettifier {

    static String prettyMove(int move){
        if (move == 0) {
            return ".";
        }
        int sourceAsPiece = MoveParser.getSourceIndex(move);
        String file = Character.toString('h' - (sourceAsPiece % (8)));
        String rank = (sourceAsPiece / 8 + 1) + "";
        int destination = MoveParser.getDestinationIndex(move);
        String destinationFile = Character.toString('h' - (destination % (8)));
        String destinationRank = (destination / 8 + 1) + "";
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

}
