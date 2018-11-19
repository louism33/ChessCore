package chessprogram.god;

import org.junit.Assert;

import java.util.ArrayList;
import java.util.List;

class MoveGeneratorCastling {

    // checking if we are in check happens elsewhere
    static List<Move> generateCastlingMoves(Chessboard board, boolean white){
        List<Move> moves = new ArrayList<>();

        if (white){
            if(board.isWhiteCanCastleK()){
                if (areTheseSquaresEmpty(board, BitboardResources.whiteCastleKingEmpties)
                        && areTheseSquaresUnthreatened(board, true, BitboardResources.whiteCastleKingEmpties)
                        && ((board.getWhiteKing() & BitboardResources.INITIAL_WHITE_KING) != 0)
                        && ((board.getWhiteRooks() & BitboardResources.SOUTH_EAST_CORNER) != 0)){

                    Assert.assertTrue(areTheseSquaresUnthreatened(board, true, board.getWhiteKing()));
                    Assert.assertTrue(!CheckHelper.boardInCheck(board, true));
                    
                    Move whiteCastleSE = new Move(3, 1, true, false, false, false, false, false, false);
                    moves.add(whiteCastleSE);
                }
            }

            if(board.isWhiteCanCastleQ()){
                if (areTheseSquaresEmpty(board, BitboardResources.whiteCastleQueenEmpties)
                        && areTheseSquaresUnthreatened(board, true, BitboardResources.whiteCastleQueenUnthreateneds)
                        && ((board.getWhiteKing() & BitboardResources.INITIAL_WHITE_KING) != 0)
                        && ((board.getWhiteRooks() & BitboardResources.SOUTH_WEST_CORNER) != 0)){

                    Assert.assertTrue(areTheseSquaresUnthreatened(board, true, board.getWhiteKing()));
                    Assert.assertTrue(!CheckHelper.boardInCheck(board, true));

                    Move whiteCastleSW = new Move(3, 5, true, false, false, false, false, false, false);
                    moves.add(whiteCastleSW);
                }
            }


        }
        else {
            if(board.isBlackCanCastleK()){
                if (areTheseSquaresEmpty(board, BitboardResources.blackCastleKingEmpties)
                        && areTheseSquaresUnthreatened(board, false, BitboardResources.blackCastleKingEmpties)
                        && ((board.getBlackKing() & BitboardResources.INITIAL_BLACK_KING) != 0)
                        && ((board.getBlackRooks() & BitboardResources.NORTH_EAST_CORNER) != 0)){

                    Assert.assertTrue(areTheseSquaresUnthreatened(board, false, board.getBlackKing()));
                    Assert.assertTrue(!CheckHelper.boardInCheck(board, false));
                    
                    Move blackCastleNE = new Move(59, 57, true, false, false, false, false, false, false);
                    moves.add(blackCastleNE);
                }
            }

            if(board.isBlackCanCastleQ()){
                if (areTheseSquaresEmpty(board, BitboardResources.blackCastleQueenEmpties)
                        && areTheseSquaresUnthreatened(board, false, BitboardResources.blackCastleQueenUnthreateneds)
                        && ((board.getBlackKing() & BitboardResources.INITIAL_BLACK_KING) != 0)
                        && ((board.getBlackRooks() & BitboardResources.NORTH_WEST_CORNER) != 0)){

                    Assert.assertTrue(areTheseSquaresUnthreatened(board, false, board.getBlackKing()));
                    Assert.assertTrue(!CheckHelper.boardInCheck(board, false));
                    
                    Move blackCastleNW = new Move(59, 61, true, false, false, false, false, false, false);
                    moves.add(blackCastleNW);
                }
            }
        }

        return moves;
    }



    private static boolean areTheseSquaresUnthreatened(Chessboard board, boolean white, long squares){
        List<Long> squaresThatShouldBeUnthreatened = BitOperations.getAllPieces(squares, 0);
        for (long square : squaresThatShouldBeUnthreatened) {
            int numberOfThreats = CheckHelper.numberOfPiecesThatLegalThreatenSquare(board, white, square); 
            if (numberOfThreats > 0){
                return false;
            }
        }
        return true;
    }

    private static boolean areTheseSquaresEmpty(Chessboard board, long squares){
        return ((board.allPieces() & squares) == 0);
    }
}
