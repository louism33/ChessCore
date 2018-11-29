package chessprogram.god;

import java.util.List;

import static chessprogram.god.BitboardResources.UNIVERSE;
import static chessprogram.god.MoveGeneratorEnPassantIntMove.addEnPassantMoves;
import static chessprogram.god.MoveGeneratorKingLegalIntMove.addKingLegalMovesOnly;
import static chessprogram.god.MoveGeneratorPromotionIntMove.addPromotionMoves;
import static chessprogram.god.MoveGeneratorPseudoIntMove.addAllMovesWithoutKing;
import static chessprogram.god.PinnedManagerIntMove.whichPiecesArePinned;

class MoveGeneratorCheckIntMove {

    static void addCheckEvasionMoves(List<Integer> moves, ChessboardIntMove board, boolean white){
        long myKing = (white) ? board.getWhiteKing() : board.getBlackKing();
        long ignoreThesePieces = whichPiecesArePinned(board, white, myKing);
        // if a piece in pinned to the king, it can never be used to block / capture a different checker
        allLegalCheckEscapeMoves(moves, board, white, ignoreThesePieces);
    }


    private static void allLegalCheckEscapeMoves(List<Integer> moves, ChessboardIntMove board, boolean white, long ignoreThesePieces) {
        long myKing = (white) ? board.getWhiteKing() : board.getBlackKing();
        long blockingSquaresMask, checkingPieceMask;
        long jumper = inCheckByAJumper(board, white);
        if (jumper != 0){
            blockingSquaresMask = 0;
            checkingPieceMask = jumper;
        }
        else {
            long slider = inCheckByASlider(board, white);
            blockingSquaresMask = Magic.extractRayFromTwoPiecesBitboard(myKing, slider) & (~slider);
            checkingPieceMask = slider;
        }
        long PENULTIMATE_RANK = white ? BitboardResources.RANK_SEVEN : BitboardResources.RANK_TWO;
        long myPawns = white ? board.getWhitePawns() : board.getBlackPawns();
        long promotablePawns = myPawns & PENULTIMATE_RANK;
        long piecesToIgnoreAndPromotingPawns = ignoreThesePieces | promotablePawns;
        
        addPromotionMoves(moves, board, white, ignoreThesePieces, blockingSquaresMask, checkingPieceMask);
        
        addAllMovesWithoutKing (moves, board, white, piecesToIgnoreAndPromotingPawns, blockingSquaresMask, checkingPieceMask);

        addKingLegalMovesOnly(moves, board, white);
        
        addEnPassantMoves(moves, board, white, piecesToIgnoreAndPromotingPawns, blockingSquaresMask, checkingPieceMask);

    }

    private static long inCheckByAJumper(ChessboardIntMove board, boolean white){
        long pawns, knights;
        if (!white){
            pawns = board.getWhitePawns();
            knights = board.getWhiteKnights();
        }
        else {
            pawns = board.getBlackPawns();
            knights = board.getBlackKnights();
        }
        long myKing = (white) ? board.getWhiteKing() : board.getBlackKing();

        long possiblePawn = PieceMovePawnsIntMove.singlePawnCaptures(myKing, white, pawns);
        if (possiblePawn != 0) {
            return possiblePawn;
        }
        long possibleKnight = PieceMoveKnight.singleKnightTable(myKing, UNIVERSE) & knights;
        if (possibleKnight != 0) {
            return possibleKnight;
        }

        return 0;
    }

    private static long inCheckByASlider(ChessboardIntMove board, boolean white){
        long bishops, rooks, queens, allPieces = board.allPieces();
        if (!white){
            bishops = board.getWhiteBishops();
            rooks = board.getWhiteRooks();
            queens = board.getWhiteQueen();
        }
        else {
            bishops = board.getBlackBishops();
            rooks = board.getBlackRooks();
            queens = board.getBlackQueen();
        }
        long myKing = (white) ? board.getWhiteKing() : board.getBlackKing();

        long possibleBishop = PieceMoveSliding.singleBishopTable(allPieces, white, myKing, bishops);
        if (possibleBishop != 0) {
            return possibleBishop;
        }
        long possibleRook = PieceMoveSliding.singleRookTable(allPieces, white, myKing, rooks);
        if (possibleRook != 0){
            return possibleRook;
        }
        long possibleQueen = PieceMoveSliding.singleQueenTable(allPieces, white, myKing, queens);
        if (possibleQueen != 0){
            return possibleQueen;
        }
        return 0;
    }

}
