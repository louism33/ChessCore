package chessprogram.god;

import org.junit.Assert;

import java.util.List;

import static chessprogram.god.CheckHelperIntMove.*;

class MoveGeneratorCastlingIntMoves {

    // checking if we are in check happens elsewhere
    static void addCastlingMoves(List<Integer> moves, ChessboardIntMove board, boolean white){

        if (white){
            if(board.isWhiteCanCastleK()){
                if (areTheseSquaresEmpty(board, BitboardResources.whiteCastleKingEmpties)
                        && areTheseSquaresUnthreatened(board, true, BitboardResources.whiteCastleKingEmpties)
                        && ((board.getWhiteKing() & BitboardResources.INITIAL_WHITE_KING) != 0)
                        && ((board.getWhiteRooks() & BitboardResources.SOUTH_EAST_CORNER) != 0)){

                    Assert.assertTrue(areTheseSquaresUnthreatened(board, true, board.getWhiteKing()));
                    Assert.assertTrue(!boardInCheck(board, true));
                    
                    moves.add(MoveParserIntMove.makeSpecialMove(3, 1, true, false, false, false, false, false, false));
                }
            }

            if(board.isWhiteCanCastleQ()){
                if (areTheseSquaresEmpty(board, BitboardResources.whiteCastleQueenEmpties)
                        && areTheseSquaresUnthreatened(board, true, BitboardResources.whiteCastleQueenUnthreateneds)
                        && ((board.getWhiteKing() & BitboardResources.INITIAL_WHITE_KING) != 0)
                        && ((board.getWhiteRooks() & BitboardResources.SOUTH_WEST_CORNER) != 0)){

                    Assert.assertTrue(areTheseSquaresUnthreatened(board, true, board.getWhiteKing()));
                    Assert.assertTrue(!boardInCheck(board, true));

                    moves.add(MoveParserIntMove.makeSpecialMove(3, 5, true, false, false, false, false, false, false));
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
                    Assert.assertTrue(!boardInCheck(board, false));
                    
                    moves.add(MoveParserIntMove.makeSpecialMove(59, 57, true, false, false, false, false, false, false));
                }
            }

            if(board.isBlackCanCastleQ()){
                if (areTheseSquaresEmpty(board, BitboardResources.blackCastleQueenEmpties)
                        && areTheseSquaresUnthreatened(board, false, BitboardResources.blackCastleQueenUnthreateneds)
                        && ((board.getBlackKing() & BitboardResources.INITIAL_BLACK_KING) != 0)
                        && ((board.getBlackRooks() & BitboardResources.NORTH_WEST_CORNER) != 0)){

                    Assert.assertTrue(areTheseSquaresUnthreatened(board, false, board.getBlackKing()));
                    Assert.assertTrue(!boardInCheck(board, false));
                    
                    moves.add(MoveParserIntMove.makeSpecialMove(59, 61, true, false, false, false, false, false, false));
                }
            }
        }
    }

    private static boolean areTheseSquaresUnthreatened(ChessboardIntMove board, boolean white, long squares){
        while (squares != 0){
            final long square = BitOperations.getFirstPiece(squares);
            int numberOfThreats = numberOfPiecesThatLegalThreatenSquare(board, white, square);
            if (numberOfThreats > 0){
                return false;
            }
            squares &= squares - 1;
        }
        return true;
    }

    private static boolean areTheseSquaresEmpty(ChessboardIntMove board, long squares){
        return ((board.allPieces() & squares) == 0);
    }
}
