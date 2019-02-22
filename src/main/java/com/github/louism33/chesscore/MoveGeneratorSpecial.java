package com.github.louism33.chesscore;

import static com.github.louism33.chesscore.BitOperations.getFirstPiece;
import static com.github.louism33.chesscore.BitOperations.getIndexOfFirstPiece;
import static com.github.louism33.chesscore.BoardConstants.*;
import static com.github.louism33.chesscore.CheckHelper.boardInCheck;
import static com.github.louism33.chesscore.CheckHelper.numberOfPiecesThatLegalThreatenSquare;
import static com.github.louism33.chesscore.MoveAdder.addMovesFromAttackTableMaster;
import static com.github.louism33.chesscore.MoveAdder.addMovesFromAttackTableMasterPromotion;
import static com.github.louism33.chesscore.MoveConstants.ENPASSANT_MASK;
import static com.github.louism33.chesscore.PieceMove.singlePawnCaptures;
import static com.github.louism33.chesscore.PieceMove.singlePawnPushes;
import static com.github.louism33.chesscore.StackDataUtil.ENPASSANTVICTIM;

class MoveGeneratorSpecial {

    static void addPromotionMoves(int[] moves, Chessboard board, boolean white,
                                  long ignoreThesePieces, long legalPushes, long legalCaptures,
                                  long myPawns,
                                  long enemies, long allPieces){
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
            long pawnMoves = singlePawnPushes(pawn, board.isWhiteTurn(), (finalRank & legalPushes), allPieces)
                    | singlePawnCaptures(pawn, board.isWhiteTurn(), ((finalRank & enemies) & legalCaptures));

            if (pawnMoves != 0) {
                addMovesFromAttackTableMasterPromotion(board, moves, pawnMoves, BitOperations.getIndexOfFirstPiece(pawn), enemies);

            }
            promotablePawns &= promotablePawns - 1;
        }
    }

    static void addEnPassantMoves(int[] moves, Chessboard board, boolean white,
                                  long ignoreThesePieces, long legalPushes, long legalCaptures,
                                  long myPawns, long myKing,
                                  long enemyPawns, long enemyKnights, long enemyBishops, long enemyRooks, long enemyQueens, long enemyKing) {

        int[] temp = new int[8];

        long enPassantTakingRank = white ? BoardConstants.RANK_FIVE : BoardConstants.RANK_FOUR;

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

            boolean enPassantWouldLeadToCheck = boardInCheck(white, myKing,
                    enemyPawns, enemyKnights, enemyBishops, enemyRooks, enemyQueens, enemyKing,
                    board.allPieces());

            board.unMakeMoveAndFlipTurn();

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
                                 long enemyPawns, long enemyKnights, long enemyBishops, long enemyRooks, long enemyQueens, long enemyKing,
                                 long allPieces){

        if (white){
            if(board.isWhiteCanCastleK()){
                if (areTheseSquaresEmpty(board, BoardConstants.whiteCastleKingEmpties)
                        && ((board.getWhiteKing() & BoardConstants.INITIAL_WHITE_KING) != 0)
                        && ((board.getWhiteRooks() & BoardConstants.SOUTH_EAST_CORNER) != 0)
                        && areTheseSquaresUnthreatened(true, BoardConstants.whiteCastleKingEmpties,
                        enemyPawns, enemyKnights, enemyBishops, enemyRooks, enemyQueens, enemyKing,
                        allPieces)){

                    MoveAdder.addMovesFromAttackTableMasterCastling(board, moves, 3, 1);
                }
            }

            if(board.isWhiteCanCastleQ()){
                if (areTheseSquaresEmpty(board, BoardConstants.whiteCastleQueenEmpties)
                        && ((board.getWhiteKing() & BoardConstants.INITIAL_WHITE_KING) != 0)
                        && ((board.getWhiteRooks() & BoardConstants.SOUTH_WEST_CORNER) != 0)
                        && areTheseSquaresUnthreatened(true, BoardConstants.whiteCastleQueenUnthreateneds,
                        enemyPawns, enemyKnights, enemyBishops, enemyRooks, enemyQueens, enemyKing,
                        allPieces)){

                    MoveAdder.addMovesFromAttackTableMasterCastling(board, moves, 3, 5);
                }
            }
        }

        else {
            if(board.isBlackCanCastleK()){
                if (areTheseSquaresEmpty(board, BoardConstants.blackCastleKingEmpties)
                        && ((board.getBlackKing() & BoardConstants.INITIAL_BLACK_KING) != 0)
                        && ((board.getBlackRooks() & BoardConstants.NORTH_EAST_CORNER) != 0)
                        && areTheseSquaresUnthreatened(false, BoardConstants.blackCastleKingEmpties,
                        enemyPawns, enemyKnights, enemyBishops, enemyRooks, enemyQueens, enemyKing,
                        allPieces)){

                    MoveAdder.addMovesFromAttackTableMasterCastling(board, moves, 59, 57);
                }
            }

            if(board.isBlackCanCastleQ()){
                if (areTheseSquaresEmpty(board, BoardConstants.blackCastleQueenEmpties)
                        && ((board.getBlackKing() & BoardConstants.INITIAL_BLACK_KING) != 0)
                        && ((board.getBlackRooks() & BoardConstants.NORTH_WEST_CORNER) != 0)
                        && areTheseSquaresUnthreatened(false, BoardConstants.blackCastleQueenUnthreateneds,
                        enemyPawns, enemyKnights, enemyBishops, enemyRooks, enemyQueens, enemyKing,
                        allPieces)){

                    MoveAdder.addMovesFromAttackTableMasterCastling(board, moves, 59, 61);

                }
            }
        }
    }

    private static boolean areTheseSquaresUnthreatened(boolean white, long squares,
                                                       long enemyPawns, long enemyKnights, long enemyBishops, long enemyRooks, long enemyQueens, long enemyKing,
                                                       long allPieces){
        while (squares != 0){
            final long square = BitOperations.getFirstPiece(squares);
            int numberOfThreats = numberOfPiecesThatLegalThreatenSquare(white, square,
                    enemyPawns, enemyKnights, enemyBishops, enemyRooks, enemyQueens, enemyKing,
                    allPieces);
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
