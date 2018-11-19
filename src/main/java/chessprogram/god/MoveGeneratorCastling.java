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
                if (areTheseSquaresEmpty(board, bBitBoardUtils.whiteCastleKingEmpties)
                        && areTheseSquaresUnthreatened(board, true, bBitBoardUtils.whiteCastleKingEmpties)
                        && ((board.getWhiteKing() & bBitBoardUtils.WHITE_KING) != 0)
                        && ((board.getWhiteRooks() & bBitBoardUtils.SOUTH_EAST_CORNER) != 0)){

                    Assert.assertTrue(areTheseSquaresUnthreatened(board, true, board.getWhiteKing()));
                    Assert.assertTrue(!cCheckChecker.boardInCheck(board, true));
                    
                    Move whiteCastleSE = new Move(3, 1, true, false, false, false, false, false, false);
                    moves.add(whiteCastleSE);
                }
            }

            if(board.isWhiteCanCastleQ()){
                if (areTheseSquaresEmpty(board, bBitBoardUtils.whiteCastleQueenEmpties)
                        && areTheseSquaresUnthreatened(board, true, bBitBoardUtils.whiteCastleQueenUnthreateneds)
                        && ((board.getWhiteKing() & bBitBoardUtils.WHITE_KING) != 0)
                        && ((board.getWhiteRooks() & bBitBoardUtils.SOUTH_WEST_CORNER) != 0)){

                    Assert.assertTrue(areTheseSquaresUnthreatened(board, true, board.getWhiteKing()));
                    Assert.assertTrue(!cCheckChecker.boardInCheck(board, true));

                    Move whiteCastleSW = new Move(3, 5, true, false, false, false, false, false, false);
                    moves.add(whiteCastleSW);
                }
            }


        }
        else {
            if(board.isBlackCanCastleK()){
                if (areTheseSquaresEmpty(board, bBitBoardUtils.blackCastleKingEmpties)
                        && areTheseSquaresUnthreatened(board, false, bBitBoardUtils.blackCastleKingEmpties)
                        && ((board.getBlackKing() & bBitBoardUtils.BLACK_KING) != 0)
                        && ((board.getBlackRooks() & bBitBoardUtils.NORTH_EAST_CORNER) != 0)){

                    Assert.assertTrue(areTheseSquaresUnthreatened(board, false, board.getBlackKing()));
                    Assert.assertTrue(!cCheckChecker.boardInCheck(board, false));
                    
                    Move blackCastleNE = new Move(59, 57, true, false, false, false, false, false, false);
                    moves.add(blackCastleNE);
                }
            }

            if(board.isBlackCanCastleQ()){
                if (areTheseSquaresEmpty(board, bBitBoardUtils.blackCastleQueenEmpties)
                        && areTheseSquaresUnthreatened(board, false, bBitBoardUtils.blackCastleQueenUnthreateneds)
                        && ((board.getBlackKing() & bBitBoardUtils.BLACK_KING) != 0)
                        && ((board.getBlackRooks() & bBitBoardUtils.NORTH_WEST_CORNER) != 0)){

                    Assert.assertTrue(areTheseSquaresUnthreatened(board, false, board.getBlackKing()));
                    Assert.assertTrue(!cCheckChecker.boardInCheck(board, false));
                    
                    Move blackCastleNW = new Move(59, 61, true, false, false, false, false, false, false);
                    moves.add(blackCastleNW);
                }
            }
        }

        return moves;
    }



    private static boolean areTheseSquaresUnthreatened(Chessboard board, boolean white, long squares){
        List<Long> squaresThatShouldBeUnthreatened = dBitExtractor.getAllPieces(squares, 0);
        for (long square : squaresThatShouldBeUnthreatened) {
            int numberOfThreats = cCheckChecker.numberOfPiecesThatLegalThreatenSquare(board, white, square); 
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
