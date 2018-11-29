package chessprogram.god;

class MovePrettifier {

    static String prettyMoveInt(int move){
        int sourceAsPiece = MoveParserIntMove.getSourceIndex(move);
        String file = getFile(sourceAsPiece);
        String rank = getRank(sourceAsPiece);
        int destination = MoveParserIntMove.getDestinationIndex(move);
        String destinationFile = getFile(destination);
        String destinationRank = getRank(destination);
        String m = ""+file+ rank+destinationFile+ destinationRank;

        if (MoveParserIntMove.isCastlingMove(move)){
            m += "";
        }
        else if (MoveParserIntMove.isEnPassantMove(move)){
            m += "";
        }

        else if (MoveParserIntMove.isPromotionMove(move)){
            m += "";

            if (MoveParserIntMove.isPromotionToKnight(move)){
                m += "N";
            }
            else if (MoveParserIntMove.isPromotionToBishop(move)){
                m += "B";
            }
            else if (MoveParserIntMove.isPromotionToRook(move)){
                m += "R";
            }
            else if (MoveParserIntMove.isPromotionToQueen(move)){
                m += "Q";
            }
        }
        return m;
    }
    
    static String prettyMove(Move move){
        int sourceAsPiece = move.getSourceIndex();
        String file = getFile(sourceAsPiece);
        String rank = getRank(sourceAsPiece);
        int destination = move.getDestinationIndex();
        String destinationFile = getFile(destination);
        String destinationRank = getRank(destination);
        String m = ""+file+ rank+destinationFile+ destinationRank;

        if (move.isCastlingMove()){
            m += "";
        }
        else if (move.isEnPassantMove()){
            m += "";
        }

        else if (move.isPromotionMove()){
            m += "";

            if (move.isPromotionToKnight()){
                m += "N";
            }
            else if (move.isPromotionToBishop()){
                m += "B";
            }
            else if (move.isPromotionToRook()){
                m += "R";
            }
            else if (move.isPromotionToQueen()){
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
