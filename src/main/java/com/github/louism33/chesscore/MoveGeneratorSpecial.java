package com.github.louism33.chesscore;

import static com.github.louism33.chesscore.BitOperations.getFirstPiece;
import static com.github.louism33.chesscore.BitOperations.populationCount;
import static com.github.louism33.chesscore.BoardConstants.*;
import static com.github.louism33.chesscore.CheckHelper.bitboardOfPiecesThatLegalThreatenSquare;
import static com.github.louism33.chesscore.CheckHelper.boardInCheck;
import static com.github.louism33.chesscore.MoveAdder.addMovesFromAttackTableMasterPromotion;
import static com.github.louism33.chesscore.MoveConstants.CASTLING_MASK;
import static com.github.louism33.chesscore.MoveConstants.ENPASSANT_MASK;
import static com.github.louism33.chesscore.MoveParser.buildMove;
import static com.github.louism33.chesscore.PieceMove.singlePawnCaptures;
import static com.github.louism33.chesscore.PieceMove.singlePawnPushes;
import static com.github.louism33.chesscore.StackDataUtil.ENPASSANTVICTIM;
import static java.lang.Long.numberOfTrailingZeros;

final class MoveGeneratorSpecial {

    static void addPromotionMoves(int[] moves, int turn, int[] pieceSquareTable,
                                  long ignoreThesePieces, long legalPushes, long legalCaptures,
                                  long myPawns,
                                  long enemies, long allPieces){

        long promotablePawns = myPawns & PENULTIMATE_RANKS[turn] & ~ignoreThesePieces;
        final long finalRank = FINAL_RANKS[turn];

        while (promotablePawns != 0){
            final long pawn = getFirstPiece(promotablePawns);
          
            final long captureTable = singlePawnCaptures(pawn, turn, ((finalRank & enemies) & legalCaptures));
            if (captureTable != 0) {
                addMovesFromAttackTableMasterPromotion(pieceSquareTable, moves, captureTable, numberOfTrailingZeros(pawn),
                        PIECE[turn][PAWN]);
            }  
            
            final long quietTable = singlePawnPushes(pawn, turn, (finalRank & legalPushes), allPieces);
            if (quietTable != 0) {
                addMovesFromAttackTableMasterPromotion(moves, quietTable, numberOfTrailingZeros(pawn),
                        PIECE[turn][PAWN]);
            }            
            
            promotablePawns &= promotablePawns - 1;
        }
    }


    static void addEnPassantMoves(int[] moves, long previousMove, int turn,
                                  long ignoreThesePieces, long legalPushes, long legalCaptures,
                                  long myPawns, long myKing,
                                  long enemyPawns, long enemyKnights, long enemyBishops, long enemyRooks,
                                  long enemyQueens, long enemyKing, long allPieces) {

        long enPassantTakingRank = ENPASSANT_RANK[1 - turn];

        long myPawnsInPosition = myPawns & enPassantTakingRank;
        if (myPawnsInPosition == 0) {
            return;
        }

        long enemyPawnsInPosition = enemyPawns & enPassantTakingRank;
        if (enemyPawnsInPosition == 0) {
            return;
        }

        if (StackDataUtil.getSpecialMove(previousMove) != ENPASSANTVICTIM){
            return;
        }

        long FILE = extractFileFromStack(StackDataUtil.getEPMove(previousMove));

        long enemyTakingSpots = 0;

        while (enemyPawnsInPosition != 0){
            long enemyPawn = BitOperations.getFirstPiece(enemyPawnsInPosition);

            if ((enemyPawn & ignoreThesePieces) == 0){
                long takingSpot = turn == WHITE ? enemyPawn << 8 : enemyPawn >>> 8;
                long potentialTakingSpot = takingSpot & FILE;

                if ((potentialTakingSpot & allPieces) != 0){
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

        long allPiecesAreAwesome, enemyPawnsAreAwesome;

        while (myPawnsInPosition != 0){
            final long pawn = getFirstPiece(myPawnsInPosition);
            if ((pawn & ignoreThesePieces) == 0) {
                long pawnEnPassantCapture = singlePawnCaptures(pawn, turn, enemyTakingSpots);

                while (pawnEnPassantCapture != 0) {
                    long pawnEnPassantCaptureSpecific = getFirstPiece(pawnEnPassantCapture);
                    allPiecesAreAwesome = allPieces;
                    enemyPawnsAreAwesome = enemyPawns;

                    long token = turn == WHITE ? pawnEnPassantCaptureSpecific >>> 8 : pawnEnPassantCaptureSpecific << 8;
                    allPiecesAreAwesome ^= token;
                    enemyPawnsAreAwesome ^= token;
                    allPiecesAreAwesome ^= pawn;
                    allPiecesAreAwesome ^= pawnEnPassantCaptureSpecific;

                    // todo, make cheaper by checking star of king, and by checking if EP would be direct check
                    final boolean enPassantWouldLeadToCheck = boardInCheck(turn, myKing,
                            enemyPawnsAreAwesome, enemyKnights, enemyBishops, enemyRooks, enemyQueens, enemyKing,
                            allPiecesAreAwesome);

                    if (!enPassantWouldLeadToCheck) {
                        moves[moves[moves.length - 1]++] = buildMove(
                                numberOfTrailingZeros(pawn),
                                PIECE[turn][PAWN],
                                numberOfTrailingZeros(pawnEnPassantCaptureSpecific)) | ENPASSANT_MASK;
                    }
                    pawnEnPassantCapture &= pawnEnPassantCapture - 1;
                }
            }
            myPawnsInPosition &= myPawnsInPosition - 1;
        }
    }


    static long extractFileFromStack(int file){
        if (file == 0){
            return 0;
        }
        return FILES[8-file];
    }

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

                            moves[moves[moves.length - 1]] =
                                    buildMove(3, WHITE_KING, 5) | CASTLING_MASK;
                            moves[moves.length - 1]++;

                        }

                        break;

                    case 0b0011:

                        if (areTheseSquaresEmpty(allPieces, castleEmpties[WHITE][Q])
                                && areTheseSquaresUnthreatened(turn, castleQueenNoThreat[WHITE],
                                enemyPawns, enemyKnights, enemyBishops, enemyRooks, enemyQueens, enemyKing,
                                allPieces)){

                            moves[moves[moves.length - 1]] = buildMove(3, WHITE_KING, 5) | CASTLING_MASK;
                            moves[moves.length - 1]++;
                        }

                    case 0b0010:// K

                        if (areTheseSquaresEmpty(allPieces, castleEmpties[WHITE][K])
                                && areTheseSquaresUnthreatened(turn, castleEmpties[WHITE][K],
                                enemyPawns, enemyKnights, enemyBishops, enemyRooks, enemyQueens, enemyKing,
                                allPieces)){


                            moves[moves[moves.length - 1]] = buildMove(3, WHITE_KING,1) | CASTLING_MASK;
                            moves[moves.length - 1]++;
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

                            moves[moves[moves.length - 1]] = buildMove(59, BLACK_KING, 61) | CASTLING_MASK;
                            moves[moves.length - 1]++;
                        }
                        break;

                    case 0b1100:

                        if (areTheseSquaresEmpty(allPieces, castleEmpties[BLACK][Q])
                                && areTheseSquaresUnthreatened(turn, castleQueenNoThreat[BLACK],
                                enemyPawns, enemyKnights, enemyBishops, enemyRooks, enemyQueens, enemyKing,
                                allPieces)){

                            moves[moves[moves.length - 1]] = buildMove(59, BLACK_KING, 61) | CASTLING_MASK;
                            moves[moves.length - 1]++;
                        }

                    case 0b1000:// K

                        if (areTheseSquaresEmpty(allPieces, castleEmpties[BLACK][K])
                                && areTheseSquaresUnthreatened(turn, castleEmpties[BLACK][K],
                                enemyPawns, enemyKnights, enemyBishops, enemyRooks, enemyQueens, enemyKing,
                                allPieces)){

                            moves[moves[moves.length - 1]] = buildMove( 59, BLACK_KING, 57, NO_PIECE) | CASTLING_MASK;
                            moves[moves.length - 1]++;
                        }
                        break;
                }
                break;
        }

    }




    private static boolean areTheseSquaresUnthreatened(int turn, long squares,
                                                       long enemyPawns, long enemyKnights, long enemyBishops, long enemyRooks, long enemyQueens, long enemyKing,
                                                       long allPieces){

        while (squares != 0){
            final long square = BitOperations.getFirstPiece(squares);
            int numberOfThreats = populationCount(bitboardOfPiecesThatLegalThreatenSquare(turn, square,
                    enemyPawns, enemyKnights, enemyBishops, enemyRooks, enemyQueens, enemyKing,
                    allPieces, 1));
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
