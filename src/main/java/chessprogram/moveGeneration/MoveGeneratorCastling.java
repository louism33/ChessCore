package chessprogram.moveGeneration;

import chessprogram.bitboards.BitBoardUtils;
import chessprogram.check.CheckChecker;
import chessprogram.chessboard.BitExtractor;
import chessprogram.chessboard.Chessboard;
import chessprogram.move.Move;
import org.junit.Assert;

import java.util.ArrayList;
import java.util.List;

class MoveGeneratorCastling {

    // checking if we are in check happens elsewhere
    static List<Move> generateCastlingMoves(Chessboard board, boolean white){
        List<Move> moves = new ArrayList<>();

        if (white){
            if(board.isWhiteCanCastleK()){
                if (areTheseSquaresEmpty(board, BitBoardUtils.whiteCastleKingEmpties)
                        && areTheseSquaresUnthreatened(board, true, BitBoardUtils.whiteCastleKingEmpties)
                        && ((board.getWhiteKing() & BitBoardUtils.WHITE_KING) != 0)
                        && ((board.getWhiteRooks() & BitBoardUtils.SOUTH_EAST_CORNER) != 0)){

                    Assert.assertTrue(areTheseSquaresUnthreatened(board, true, board.getWhiteKing()));
                    Assert.assertTrue(!CheckChecker.boardInCheck(board, true));
                    
                    Move whiteCastleSE = new Move(3, 1, true, false, false, false, false, false, false);
                    moves.add(whiteCastleSE);
                }
            }

            if(board.isWhiteCanCastleQ()){
                if (areTheseSquaresEmpty(board, BitBoardUtils.whiteCastleQueenEmpties)
                        && areTheseSquaresUnthreatened(board, true, BitBoardUtils.whiteCastleQueenUnthreateneds)
                        && ((board.getWhiteKing() & BitBoardUtils.WHITE_KING) != 0)
                        && ((board.getWhiteRooks() & BitBoardUtils.SOUTH_WEST_CORNER) != 0)){

                    Assert.assertTrue(areTheseSquaresUnthreatened(board, true, board.getWhiteKing()));
                    Assert.assertTrue(!CheckChecker.boardInCheck(board, true));

                    Move whiteCastleSW = new Move(3, 5, true, false, false, false, false, false, false);
                    moves.add(whiteCastleSW);
                }
            }


        }
        else {
            if(board.isBlackCanCastleK()){
                if (areTheseSquaresEmpty(board, BitBoardUtils.blackCastleKingEmpties)
                        && areTheseSquaresUnthreatened(board, false, BitBoardUtils.blackCastleKingEmpties)
                        && ((board.getBlackKing() & BitBoardUtils.BLACK_KING) != 0)
                        && ((board.getBlackRooks() & BitBoardUtils.NORTH_EAST_CORNER) != 0)){

                    Assert.assertTrue(areTheseSquaresUnthreatened(board, false, board.getBlackKing()));
                    Assert.assertTrue(!CheckChecker.boardInCheck(board, false));
                    
                    Move blackCastleNE = new Move(59, 57, true, false, false, false, false, false, false);
                    moves.add(blackCastleNE);
                }
            }

            if(board.isBlackCanCastleQ()){
                if (areTheseSquaresEmpty(board, BitBoardUtils.blackCastleQueenEmpties)
                        && areTheseSquaresUnthreatened(board, false, BitBoardUtils.blackCastleQueenUnthreateneds)
                        && ((board.getBlackKing() & BitBoardUtils.BLACK_KING) != 0)
                        && ((board.getBlackRooks() & BitBoardUtils.NORTH_WEST_CORNER) != 0)){

                    Assert.assertTrue(areTheseSquaresUnthreatened(board, false, board.getBlackKing()));
                    Assert.assertTrue(!CheckChecker.boardInCheck(board, false));
                    
                    Move blackCastleNW = new Move(59, 61, true, false, false, false, false, false, false);
                    moves.add(blackCastleNW);
                }
            }
        }

        return moves;
    }



    private static boolean areTheseSquaresUnthreatened(Chessboard board, boolean white, long squares){
        List<Long> squaresThatShouldBeUnthreatened = BitExtractor.getAllPieces(squares, 0);
        for (long square : squaresThatShouldBeUnthreatened) {
            int numberOfThreats = CheckChecker.numberOfPiecesThatLegalThreatenSquare(board, white, square); 
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
