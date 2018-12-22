package com.github.louism33.chesscore;

import static com.github.louism33.chesscore.BitOperations.getFirstPiece;
import static com.github.louism33.chesscore.BitOperations.getIndexOfFirstPiece;
import static com.github.louism33.chesscore.BitboardResources.*;
import static com.github.louism33.chesscore.CheckHelper.boardInCheck;
import static com.github.louism33.chesscore.CheckHelper.numberOfPiecesThatLegalThreatenSquare;
import static com.github.louism33.chesscore.ConstantsMove.ENPASSANT_MASK;
import static com.github.louism33.chesscore.MoveAdder.addMovesFromAttackTableMaster;
import static com.github.louism33.chesscore.MoveAdder.addMovesFromAttackTableMasterPromotion;
import static com.github.louism33.chesscore.PieceMove.singlePawnCaptures;
import static com.github.louism33.chesscore.PieceMove.singlePawnPushes;
import static com.github.louism33.chesscore.StackDataUtil.ENPASSANTVICTIM;

class MoveGeneratorSpecial {

    static void addPromotionMoves(int[] moves, Chessboard board, boolean white,
                                  long ignoreThesePieces, long legalPushes, long legalCaptures,
                                  long myPawns, long myKnights, long myBishops, long myRooks, long myQueens, long myKing,
                                  long enemyPawns, long enemyKnights, long enemyBishops, long enemyRooks, long enemyQueens, long enemyKing,
                                  long enemies, long friends, long allPieces){
        long penultimateRank;
        long finalRank;
        if (white) {
            penultimateRank = RANK_SEVEN;
            finalRank = RANK_EIGHT;
        }
        else {
            penultimateRank = RANK_TWO;
            finalRank = RANK_ONE;
        }

        long promotablePawns = myPawns & penultimateRank & ~ignoreThesePieces;

        while (promotablePawns != 0){
            final long pawn = getFirstPiece(promotablePawns);
            long pawnMoves = singlePawnPushes(board, pawn, board.isWhiteTurn(), (finalRank & legalPushes), allPieces)
                    | singlePawnCaptures(pawn, board.isWhiteTurn(), ((finalRank & enemies) & legalCaptures));

            if (pawnMoves != 0) {
                addMovesFromAttackTableMasterPromotion(board, moves, pawnMoves, BitOperations.getIndexOfFirstPiece(pawn), enemies);

            }
            promotablePawns &= promotablePawns - 1;
        }
    }

    static void addEnPassantMoves(int[] moves, Chessboard board, boolean white,
                                  long ignoreThesePieces, long legalPushes, long legalCaptures,
                                  long myPawns, long myKnights, long myBishops, long myRooks, long myQueens, long myKing,
                                  long enemyPawns, long enemyKnights, long enemyBishops, long enemyRooks, long enemyQueens, long enemyKing,
                                  long enemies, long friends, long allPieces) {

        int[] temp = new int[8];

        long enPassantTakingRank = white ? BitboardResources.RANK_FIVE : BitboardResources.RANK_FOUR;

        long myPawnsInPosition = myPawns & enPassantTakingRank;
        if (myPawnsInPosition == 0) {
            return;
        }

        long enemyPawnsInPosition = enemyPawns & enPassantTakingRank;
        if (enemyPawnsInPosition == 0) {
            return;
        }

        if (!board.hasPreviousMove()){
            return;
        }

        long previousMove = board.moveStackArrayPeek();

//        if (StackDataUtil.SpecialMove.values()[StackDataUtil.getSpecialMove(previousMove)] != ENPASSANTVICTIM){
//            return;
//        }

        if (StackDataUtil.getSpecialMove(previousMove) != ENPASSANTVICTIM){
            return;
        }

        long FILE = extractFileFromStack(StackDataUtil.getEPMove(previousMove));

        long enemyTakingSpots = 0;

        while (enemyPawnsInPosition != 0){
            long enemyPawn = BitOperations.getFirstPiece(enemyPawnsInPosition);

            if ((enemyPawn & ignoreThesePieces) == 0){
                long takingSpot = white ? enemyPawn << 8 : enemyPawn >>> 8;
                long potentialTakingSpot = takingSpot & FILE;

                if ((potentialTakingSpot & board.allPieces()) != 0){
                    enemyPawnsInPosition &= enemyPawnsInPosition - 1;
                    continue;
                }

                if (((enemyPawn & legalCaptures) == 0) && ((potentialTakingSpot & legalPushes) == 0)) {
                    enemyPawnsInPosition &= enemyPawnsInPosition - 1;
                    continue;
                }
                enemyTakingSpots |= potentialTakingSpot;
            }
            enemyPawnsInPosition &= enemyPawnsInPosition - 1;
        }

        if (enemyTakingSpots == 0){
            return;
        }

        while (myPawnsInPosition != 0){
            final long pawn = getFirstPiece(myPawnsInPosition);
            if ((pawn & ignoreThesePieces) == 0) {
                long pawnEnPassantCapture = singlePawnCaptures(pawn, white, enemyTakingSpots);
                addMovesFromAttackTableMaster(temp, pawnEnPassantCapture, getIndexOfFirstPiece(pawn), board);
            }
            myPawnsInPosition &= myPawnsInPosition - 1;
        }

        int index = 0;
        while (moves[index] != 0){
            index++;
        }

        // remove moves that would leave us in check
        for (int i = 0; i < temp.length; i++) {
            int move = temp[i];

            if (move == 0){
                break;
            }

            move |= ENPASSANT_MASK;

            board.makeMoveAndFlipTurn(move);

            enemyPawns = board.isWhiteTurn() ? board.getWhitePawns() : board.getBlackPawns();

            boolean enPassantWouldLeadToCheck = boardInCheck(board, white, myKing,
                    enemyPawns, enemyKnights, enemyBishops, enemyRooks, enemyQueens, enemyKing,
                    enemies, friends, board.allPieces());

            try {
                board.unMakeMoveAndFlipTurn();
            } catch (IllegalUnmakeException e) {
                e.printStackTrace();
            }

            if (enPassantWouldLeadToCheck) {
                continue;
            }
            moves[index + i] = move;
        }
    }

    static long extractFileFromStack(int file){
        if (file == 0){
            return 0;
        }
        return FILES[8-file];
    }



    // checking if we are in check happens elsewhere
    static void addCastlingMoves(int[] moves, Chessboard board, boolean white,
                                 long myPawns, long myKnights, long myBishops, long myRooks, long myQueens, long myKing,
                                 long enemyPawns, long enemyKnights, long enemyBishops, long enemyRooks, long enemyQueens, long enemyKing,
                                 long enemies, long friends, long allPieces){

        if (white){
            if(board.isWhiteCanCastleK()){
                if (areTheseSquaresEmpty(board, BitboardResources.whiteCastleKingEmpties)
                        && ((board.getWhiteKing() & BitboardResources.INITIAL_WHITE_KING) != 0)
                        && ((board.getWhiteRooks() & BitboardResources.SOUTH_EAST_CORNER) != 0)
                        && areTheseSquaresUnthreatened(board, true, BitboardResources.whiteCastleKingEmpties,
                        myPawns, myKnights, myBishops, myRooks, myQueens, myKing,
                        enemyPawns, enemyKnights, enemyBishops, enemyRooks, enemyQueens, enemyKing,
                        enemies, friends, allPieces)){

                    MoveAdder.addMovesFromAttackTableMasterCastling(board, moves, 3, 1);
                }
            }

            if(board.isWhiteCanCastleQ()){
                if (areTheseSquaresEmpty(board, BitboardResources.whiteCastleQueenEmpties)
                        && ((board.getWhiteKing() & BitboardResources.INITIAL_WHITE_KING) != 0)
                        && ((board.getWhiteRooks() & BitboardResources.SOUTH_WEST_CORNER) != 0)
                        && areTheseSquaresUnthreatened(board, true, BitboardResources.whiteCastleQueenUnthreateneds,
                        myPawns, myKnights, myBishops, myRooks, myQueens, myKing,
                        enemyPawns, enemyKnights, enemyBishops, enemyRooks, enemyQueens, enemyKing,
                        enemies, friends, allPieces)){

                    MoveAdder.addMovesFromAttackTableMasterCastling(board, moves, 3, 5);
                }
            }
        }

        else {
            if(board.isBlackCanCastleK()){
                if (areTheseSquaresEmpty(board, BitboardResources.blackCastleKingEmpties)
                        && ((board.getBlackKing() & BitboardResources.INITIAL_BLACK_KING) != 0)
                        && ((board.getBlackRooks() & BitboardResources.NORTH_EAST_CORNER) != 0)
                        && areTheseSquaresUnthreatened(board, false, BitboardResources.blackCastleKingEmpties,
                        myPawns, myKnights, myBishops, myRooks, myQueens, myKing,
                        enemyPawns, enemyKnights, enemyBishops, enemyRooks, enemyQueens, enemyKing,
                        enemies, friends, allPieces)){

                    MoveAdder.addMovesFromAttackTableMasterCastling(board, moves, 59, 57);
                }
            }

            if(board.isBlackCanCastleQ()){
                if (areTheseSquaresEmpty(board, BitboardResources.blackCastleQueenEmpties)
                        && ((board.getBlackKing() & BitboardResources.INITIAL_BLACK_KING) != 0)
                        && ((board.getBlackRooks() & BitboardResources.NORTH_WEST_CORNER) != 0)
                        && areTheseSquaresUnthreatened(board, false, BitboardResources.blackCastleQueenUnthreateneds,myPawns, myKnights, myBishops, myRooks, myQueens, myKing,
                        enemyPawns, enemyKnights, enemyBishops, enemyRooks, enemyQueens, enemyKing,
                        enemies, friends, allPieces)){

                    MoveAdder.addMovesFromAttackTableMasterCastling(board, moves, 59, 61);

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
