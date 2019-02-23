package com.github.louism33.chesscore;

import org.junit.Assert;

import static com.github.louism33.chesscore.BitOperations.getFirstPiece;
import static com.github.louism33.chesscore.BitOperations.getIndexOfFirstPiece;
import static com.github.louism33.chesscore.BoardConstants.*;
import static com.github.louism33.chesscore.CheckHelper.boardInCheck;
import static com.github.louism33.chesscore.CheckHelper.numberOfPiecesThatLegalThreatenSquare;
import static com.github.louism33.chesscore.MoveAdder.*;
import static com.github.louism33.chesscore.MoveConstants.CASTLING_MASK;
import static com.github.louism33.chesscore.MoveConstants.ENPASSANT_MASK;
import static com.github.louism33.chesscore.MoveParser.*;
import static com.github.louism33.chesscore.PieceMove.singlePawnCaptures;
import static com.github.louism33.chesscore.PieceMove.singlePawnPushes;
import static com.github.louism33.chesscore.StackDataUtil.ENPASSANTVICTIM;

class MoveGeneratorSpecial {

    static void addPromotionMoves(int[] moves, Chessboard board, boolean white,
                                  long ignoreThesePieces, long legalPushes, long legalCaptures,
                                  long myPawns,
                                  long enemies, long allPieces){
        int turn = white ? WHITE : BLACK;

        long promotablePawns = myPawns & PENULTIMATE_RANKS[turn] & ~ignoreThesePieces;
        
        Assert.assertEquals(board.isWhiteTurn(), white);

        while (promotablePawns != 0){
            final long pawn = getFirstPiece(promotablePawns);
            long pawnMoves = singlePawnPushes(pawn, board.turn, (FINAL_RANKS[turn] & legalPushes), allPieces)
                    | singlePawnCaptures(pawn, board.isWhiteTurn(), ((FINAL_RANKS[turn] & enemies) & legalCaptures));

            if (pawnMoves != 0) {
                addMovesFromAttackTableMasterPromotion(board, moves, pawnMoves, BitOperations.getIndexOfFirstPiece(pawn), 
                        board.turn == WHITE ? WHITE_PAWN : BLACK_PAWN, enemies);

            }
            promotablePawns &= promotablePawns - 1;
        }
    }

    static void addEnPassantMoves(int[] moves, Chessboard board, boolean white,
                                  long ignoreThesePieces, long legalPushes, long legalCaptures,
                                  long myPawns, long myKing,
                                  long enemyPawns, long enemyKnights, long enemyBishops, long enemyRooks, long enemyQueens, long enemyKing) {

        int[] temp = new int[8];

        long enPassantTakingRank = white ? RANK_FIVE : RANK_FOUR;

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
                
                
                addMovesFromAttackTableMasterBetter(temp, pawnEnPassantCapture, getIndexOfFirstPiece(pawn), 
                        board.turn == WHITE ? WHITE_PAWN : BLACK_PAWN, board);

                        
                        
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
    static void addCastlingMoves(int[] moves, int turn, int castlingRights,
                                 long enemyPawns, long enemyKnights, long enemyBishops, long enemyRooks, long enemyQueens, long enemyKing,
                                 long allPieces){


        if (castlingRights == 0) {
            return;
        }

        switch (turn) {
            case WHITE:

                if ((castlingRights & castlingRightsMask[WHITE][KQ]) == 0) {
                    break;
                }

                switch (castlingRights & castlingRightsMask[WHITE][KQ]) {
                    case 0b0001:// Q

                        if (areTheseSquaresEmpty(allPieces, castleEmpties[WHITE][Q])
                                && areTheseSquaresUnthreatened(turn, castleQueenNoThreat[WHITE],
                                enemyPawns, enemyKnights, enemyBishops, enemyRooks, enemyQueens, enemyKing,
                                allPieces)){

                            moves[numberOfRealMoves(moves)] = 
                                    buildBetterMove(3, WHITE_KING, 5, NO_PIECE) | CASTLING_MASK;
                        }

                        break;

                    case 0b0011:

                        if (areTheseSquaresEmpty(allPieces, castleEmpties[WHITE][Q])
                                && areTheseSquaresUnthreatened(turn, castleQueenNoThreat[WHITE],
                                enemyPawns, enemyKnights, enemyBishops, enemyRooks, enemyQueens, enemyKing,
                                allPieces)){

                            moves[numberOfRealMoves(moves)] = buildBetterMove(3, WHITE_KING, 5, NO_PIECE) | CASTLING_MASK;
                        }

                    case 0b0010:// K

                        if (areTheseSquaresEmpty(allPieces, castleEmpties[WHITE][K])
                                && areTheseSquaresUnthreatened(turn, castleEmpties[WHITE][K],
                                enemyPawns, enemyKnights, enemyBishops, enemyRooks, enemyQueens, enemyKing,
                                allPieces)){


                            moves[numberOfRealMoves(moves)] = buildBetterMove(3, WHITE_KING,1, NO_PIECE) | CASTLING_MASK;
                        }

                        break;
                }

                break;


            case BLACK:
                switch ((castlingRights & castlingRightsMask[BLACK][KQ])) {
                    case 0b0100:// Q
                        if (areTheseSquaresEmpty(allPieces, castleEmpties[BLACK][Q])
                                && areTheseSquaresUnthreatened(turn, castleQueenNoThreat[BLACK],
                                enemyPawns, enemyKnights, enemyBishops, enemyRooks, enemyQueens, enemyKing,
                                allPieces)){

                            moves[numberOfRealMoves(moves)] = buildBetterMove(59, BLACK_KING, 61, NO_PIECE) | CASTLING_MASK;
                        }
                        break;
                        
                    case 0b1100:

                        if (areTheseSquaresEmpty(allPieces, castleEmpties[BLACK][Q])
                                && areTheseSquaresUnthreatened(turn, castleQueenNoThreat[BLACK],
                                enemyPawns, enemyKnights, enemyBishops, enemyRooks, enemyQueens, enemyKing,
                                allPieces)){

                            moves[numberOfRealMoves(moves)] = buildBetterMove(59, BLACK_KING, 61, NO_PIECE) | CASTLING_MASK;
                        }

                    case 0b1000:// K

                        if (areTheseSquaresEmpty(allPieces, castleEmpties[BLACK][K])
                                && areTheseSquaresUnthreatened(turn, castleEmpties[BLACK][K],
                                enemyPawns, enemyKnights, enemyBishops, enemyRooks, enemyQueens, enemyKing,
                                allPieces)){

                            moves[numberOfRealMoves(moves)] = buildBetterMove( 59, BLACK_KING, 57, NO_PIECE) | CASTLING_MASK;
                        }
                        break;
                }
                break;
        }

    }




    private static boolean areTheseSquaresUnthreatened(int turn, long squares,
                                                       long enemyPawns, long enemyKnights, long enemyBishops, long enemyRooks, long enemyQueens, long enemyKing,
                                                       long allPieces){

        boolean white = turn == WHITE;

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

    private static boolean areTheseSquaresEmpty(long allPieces, long squares){
        return ((allPieces & squares) == 0);
    }

}
