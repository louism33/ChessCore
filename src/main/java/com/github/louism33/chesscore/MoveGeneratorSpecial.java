package com.github.louism33.chesscore;

import org.junit.Assert;

import static com.github.louism33.chesscore.BitOperations.*;
import static com.github.louism33.chesscore.BitOperations.newPieceOnSquare;
import static com.github.louism33.chesscore.BoardConstants.*;
import static com.github.louism33.chesscore.CheckHelper.boardInCheck;
import static com.github.louism33.chesscore.CheckHelper.numberOfPiecesThatLegalThreatenSquare;
import static com.github.louism33.chesscore.MoveAdder.addMovesFromAttackTableMasterBetter;
import static com.github.louism33.chesscore.MoveAdder.addMovesFromAttackTableMasterPromotion;
import static com.github.louism33.chesscore.MoveConstants.CASTLING_MASK;
import static com.github.louism33.chesscore.MoveConstants.ENPASSANT_MASK;
import static com.github.louism33.chesscore.MoveMakingUtilities.togglePiecesFrom;
import static com.github.louism33.chesscore.MoveParser.buildMove;
import static com.github.louism33.chesscore.PieceMove.singlePawnCaptures;
import static com.github.louism33.chesscore.PieceMove.singlePawnPushes;
import static com.github.louism33.chesscore.StackDataUtil.ENPASSANTVICTIM;

class MoveGeneratorSpecial {

    static void addPromotionMoves(int[] moves, int turn, int[] pieceSquareTable,
                                  long ignoreThesePieces, long legalPushes, long legalCaptures,
                                  long myPawns,
                                  long enemies, long allPieces){

        long promotablePawns = myPawns & PENULTIMATE_RANKS[turn] & ~ignoreThesePieces;

        while (promotablePawns != 0){
            final long pawn = getFirstPiece(promotablePawns);
            long pawnMoves = singlePawnPushes(pawn, turn, (FINAL_RANKS[turn] & legalPushes), allPieces)
                    | singlePawnCaptures(pawn, turn, ((FINAL_RANKS[turn] & enemies) & legalCaptures));

            if (pawnMoves != 0) {
                addMovesFromAttackTableMasterPromotion(pieceSquareTable, moves, pawnMoves, BitOperations.getIndexOfFirstPiece(pawn),
                        PIECE[turn][PAWN]);

            }
            promotablePawns &= promotablePawns - 1;
        }
    }


    // board.turn, board.pieceSquareTable, board.moveStackArrayPeek()
    static void addEnPassantMoves(int[] moves, Chessboard board, boolean white,
                                  long ignoreThesePieces, long legalPushes, long legalCaptures,
                                  long myPawns, long myKing,
                                  long enemyPawns, long enemyKnights, long enemyBishops, long enemyRooks, long enemyQueens, long enemyKing) {


        if (!board.hasPreviousMove()){
            return;
        }

        long previousMove = board.moveStackArrayPeek();
                
        /**
         * int turn, int[] pieceSquareTable, long previousMove
         * allPieces
         */
        int turn = board.turn;
        
        Assert.assertTrue(white ? turn == WHITE : turn == BLACK);

        int[] temp = new int[8];

        long enPassantTakingRank = ENPASSANT_RANK[1 - turn];

        long myPawnsInPosition = myPawns & enPassantTakingRank;
        if (myPawnsInPosition == 0) {
            return;
        }

        long enemyPawnsInPosition = enemyPawns & enPassantTakingRank;
        if (enemyPawnsInPosition == 0) {
            return;
        }

;

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
                        turn == WHITE ? WHITE_PAWN : BLACK_PAWN, board.pieceSquareTable);
            }
            myPawnsInPosition &= myPawnsInPosition - 1;
        }


        int index = 0;
        while (moves[index] != 0){
            index++;
        }

        Assert.assertEquals(index, moves[moves.length - 1]);

        // remove moves that would leave us in check
        for (int i = 0; i < temp.length; i++) {
            int move = temp[i];

            if (move == 0){
                break;
            }

            move |= ENPASSANT_MASK;

            long sourcePiece = newPieceOnSquare(MoveParser.getSourceIndex(move));
            long destinationPiece = newPieceOnSquare(MoveParser.getDestinationIndex(move));

            switch (turn) {
                case WHITE:
                    togglePiecesFrom(board.pieces, board.pieceSquareTable, sourcePiece, WHITE_PAWN);
                    togglePiecesFrom(board.pieces, board.pieceSquareTable, destinationPiece >>> 8, BLACK_PAWN);
                    board.pieces[WHITE][PAWN] |= destinationPiece;
                    board.pieceSquareTable[MoveParser.getDestinationIndex(move)] = WHITE_PAWN;
                    break;

                case BLACK:
                    togglePiecesFrom(board.pieces, board.pieceSquareTable, sourcePiece, BLACK_PAWN);
                    togglePiecesFrom(board.pieces, board.pieceSquareTable, destinationPiece << 8, WHITE_PAWN);
                    board.pieces[BLACK][PAWN] |= destinationPiece;
                    board.pieceSquareTable[MoveParser.getDestinationIndex(move)] = BLACK_PAWN;
            }

            enemyPawns = board.pieces[1 - turn][PAWN];

            boolean enPassantWouldLeadToCheck = boardInCheck(white, myKing,
                    enemyPawns, enemyKnights, enemyBishops, enemyRooks, enemyQueens, enemyKing,
                    board.allPieces());

            switch (turn) {
                case WHITE:
                    togglePiecesFrom(board.pieces, board.pieceSquareTable, sourcePiece, WHITE_PAWN);
                    togglePiecesFrom(board.pieces, board.pieceSquareTable, destinationPiece >>> 8, BLACK_PAWN);
                    board.pieces[WHITE][PAWN] ^= destinationPiece;
                    board.pieceSquareTable[MoveParser.getDestinationIndex(move)] = NO_PIECE;
                    break;

                case BLACK:
                    togglePiecesFrom(board.pieces, board.pieceSquareTable, sourcePiece, BLACK_PAWN);
                    togglePiecesFrom(board.pieces, board.pieceSquareTable, destinationPiece << 8, WHITE_PAWN);
                    board.pieces[BLACK][PAWN] ^= destinationPiece;
                    board.pieceSquareTable[MoveParser.getDestinationIndex(move)] = NO_PIECE;
            }

            if (enPassantWouldLeadToCheck) {
                continue;
            }
            moves[index + i] = move;
            moves[moves.length - 1]++;
        }
    }


    static void addEnPassantMovesBetter(int[] moves, int turn, int[] pieceSquareTable, long previousMove, boolean white,
                                        long ignoreThesePieces, long legalPushes, long legalCaptures,
                                        long myPawns, long myKing,
                                        long enemyPawns, long enemyKnights, long enemyBishops, long enemyRooks,
                                        long enemyQueens, long enemyKing, long allPieces) {

        int[] temp = new int[8];
        
        Assert.assertTrue(allPieces != 0);

        long enPassantTakingRank = white ? RANK_FIVE : RANK_FOUR;

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
                long takingSpot = white ? enemyPawn << 8 : enemyPawn >>> 8;
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

        while (myPawnsInPosition != 0){
            final long pawn = getFirstPiece(myPawnsInPosition);
            if ((pawn & ignoreThesePieces) == 0) {
                long pawnEnPassantCapture = singlePawnCaptures(pawn, white, enemyTakingSpots);

                addMovesFromAttackTableMasterBetter(temp, pawnEnPassantCapture, getIndexOfFirstPiece(pawn),
                        turn == WHITE ? WHITE_PAWN : BLACK_PAWN, pieceSquareTable);
            }
            myPawnsInPosition &= myPawnsInPosition - 1;
        }


        int index = 0;
        while (moves[index] != 0){
            index++;
        }

        Assert.assertEquals(index, moves[moves.length - 1]);

        // remove moves that would leave us in check
        for (int i = 0; i < temp.length; i++) {
            int move = temp[i];

            if (move == 0){
                break;
            }

            move |= ENPASSANT_MASK;

            long sourcePiece = newPieceOnSquare(MoveParser.getSourceIndex(move));
            long destinationPiece = newPieceOnSquare(MoveParser.getDestinationIndex(move));

            // in case more than one ep to check
            long enemyPawnsTemp = enemyPawns, allPiecesTemp = allPieces;
            enemyPawnsTemp ^= (turn == WHITE ? destinationPiece >>> 8 : destinationPiece << 8);
            allPiecesTemp ^= sourcePiece;
            allPiecesTemp |= destinationPiece;

            boolean enPassantWouldLeadToCheck = boardInCheck(white, myKing,
                    enemyPawnsTemp, enemyKnights, enemyBishops, enemyRooks, enemyQueens, enemyKing,
                    allPiecesTemp);

            if (enPassantWouldLeadToCheck) {
                continue;
            }
            moves[index + i] = move;
            moves[moves.length - 1]++;
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
                                    buildMove(3, WHITE_KING, 5, NO_PIECE) | CASTLING_MASK;
                            moves[moves.length - 1]++;

                        }

                        break;

                    case 0b0011:

                        if (areTheseSquaresEmpty(allPieces, castleEmpties[WHITE][Q])
                                && areTheseSquaresUnthreatened(turn, castleQueenNoThreat[WHITE],
                                enemyPawns, enemyKnights, enemyBishops, enemyRooks, enemyQueens, enemyKing,
                                allPieces)){

                            moves[moves[moves.length - 1]] = buildMove(3, WHITE_KING, 5, NO_PIECE) | CASTLING_MASK;
                            moves[moves.length - 1]++;
                        }

                    case 0b0010:// K

                        if (areTheseSquaresEmpty(allPieces, castleEmpties[WHITE][K])
                                && areTheseSquaresUnthreatened(turn, castleEmpties[WHITE][K],
                                enemyPawns, enemyKnights, enemyBishops, enemyRooks, enemyQueens, enemyKing,
                                allPieces)){


                            moves[moves[moves.length - 1]] = buildMove(3, WHITE_KING,1, NO_PIECE) | CASTLING_MASK;
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

                            moves[moves[moves.length - 1]] = buildMove(59, BLACK_KING, 61, NO_PIECE) | CASTLING_MASK;
                            moves[moves.length - 1]++;
                        }
                        break;

                    case 0b1100:

                        if (areTheseSquaresEmpty(allPieces, castleEmpties[BLACK][Q])
                                && areTheseSquaresUnthreatened(turn, castleQueenNoThreat[BLACK],
                                enemyPawns, enemyKnights, enemyBishops, enemyRooks, enemyQueens, enemyKing,
                                allPieces)){

                            moves[moves[moves.length - 1]] = buildMove(59, BLACK_KING, 61, NO_PIECE) | CASTLING_MASK;
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
