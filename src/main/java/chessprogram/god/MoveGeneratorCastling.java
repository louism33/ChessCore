package chessprogram.god;

import static chessprogram.god.CheckHelper.numberOfPiecesThatLegalThreatenSquare;

class MoveGeneratorCastling {


    // checking if we are in check happens elsewhere
    static void addCastlingMoves(int[] moves, Chessboard board, boolean white,
                                 long myPawns, long myKnights, long myBishops, long myRooks, long myQueens, long myKing,
                                 long enemyPawns, long enemyKnights, long enemyBishops, long enemyRooks, long enemyQueens, long enemyKing,
                                 long enemies, long friends, long allPieces){

        if (white){
            if(board.isWhiteCanCastleK()){
                if (areTheseSquaresEmpty(board, BitboardResources.whiteCastleKingEmpties)
                        && areTheseSquaresUnthreatened(board, true, BitboardResources.whiteCastleKingEmpties,
                        myPawns, myKnights, myBishops, myRooks, myQueens, myKing,
                        enemyPawns, enemyKnights, enemyBishops, enemyRooks, enemyQueens, enemyKing,
                        enemies, friends, allPieces)
                        && ((board.getWhiteKing() & BitboardResources.INITIAL_WHITE_KING) != 0)
                        && ((board.getWhiteRooks() & BitboardResources.SOUTH_EAST_CORNER) != 0)){

                    MoveGenerationUtilities.addMovesFromAttackTableMasterCastling(moves, 3, 1);
                }
            }

            if(board.isWhiteCanCastleQ()){
                if (areTheseSquaresEmpty(board, BitboardResources.whiteCastleQueenEmpties)
                        && areTheseSquaresUnthreatened(board, true, BitboardResources.whiteCastleQueenUnthreateneds,
                        myPawns, myKnights, myBishops, myRooks, myQueens, myKing,
                        enemyPawns, enemyKnights, enemyBishops, enemyRooks, enemyQueens, enemyKing,
                        enemies, friends, allPieces)
                        && ((board.getWhiteKing() & BitboardResources.INITIAL_WHITE_KING) != 0)
                        && ((board.getWhiteRooks() & BitboardResources.SOUTH_WEST_CORNER) != 0)){

                    MoveGenerationUtilities.addMovesFromAttackTableMasterCastling(moves, 3, 5);
                }
            }
        }

        else {
            if(board.isBlackCanCastleK()){
                if (areTheseSquaresEmpty(board, BitboardResources.blackCastleKingEmpties)
                        && areTheseSquaresUnthreatened(board, false, BitboardResources.blackCastleKingEmpties,
                        myPawns, myKnights, myBishops, myRooks, myQueens, myKing,
                        enemyPawns, enemyKnights, enemyBishops, enemyRooks, enemyQueens, enemyKing,
                        enemies, friends, allPieces)
                        && ((board.getBlackKing() & BitboardResources.INITIAL_BLACK_KING) != 0)
                        && ((board.getBlackRooks() & BitboardResources.NORTH_EAST_CORNER) != 0)){

                    MoveGenerationUtilities.addMovesFromAttackTableMasterCastling(moves, 59, 57);
                }
            }

            if(board.isBlackCanCastleQ()){
                if (areTheseSquaresEmpty(board, BitboardResources.blackCastleQueenEmpties)
                        && areTheseSquaresUnthreatened(board, false, BitboardResources.blackCastleQueenUnthreateneds,myPawns, myKnights, myBishops, myRooks, myQueens, myKing,
                        enemyPawns, enemyKnights, enemyBishops, enemyRooks, enemyQueens, enemyKing,
                        enemies, friends, allPieces)
                        && ((board.getBlackKing() & BitboardResources.INITIAL_BLACK_KING) != 0)
                        && ((board.getBlackRooks() & BitboardResources.NORTH_WEST_CORNER) != 0)){

                    MoveGenerationUtilities.addMovesFromAttackTableMasterCastling(moves, 59, 61);

                }
            }
        }
    }
    
    private static boolean areTheseSquaresUnthreatened(Chessboard board, boolean white, long squares,
                                                       long myPawns, long myKnights, long myBishops, long myRooks, long myQueens, long myKing,
                                                       long enemyPawns, long enemyKnights, long enemyBishops, long enemyRooks, long enemyQueens, long enemyKing,
                                                       long enemies, long friends, long allPieces){
        while (squares != 0){
            final long square = BitOperations.getFirstPiece(squares);
            int numberOfThreats = numberOfPiecesThatLegalThreatenSquare(board, white, square,
//                    myPawns, myKnights, myBishops, myRooks, myQueens, myKing,
                    enemyPawns, enemyKnights, enemyBishops, enemyRooks, enemyQueens, enemyKing,
                    enemies, friends, allPieces);
            if (numberOfThreats > 0){
                return false;
            }
            squares &= squares - 1;
        }
        return true;
    }

    private static boolean areTheseSquaresEmpty(Chessboard board, long squares){
        return ((board.allPieces() & squares) == 0);
    }
}
