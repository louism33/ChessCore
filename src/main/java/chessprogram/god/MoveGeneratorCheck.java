package chessprogram.god;

import java.util.List;

import static chessprogram.god.BitboardResources.*;
import static chessprogram.god.BitOperations.getIndexOfFirstPiece;
import static chessprogram.god.MoveGeneratorEnPassant.*;
import static chessprogram.god.MoveGeneratorKingLegal.*;
import static chessprogram.god.MoveGeneratorPromotion.*;
import static chessprogram.god.MoveGeneratorPseudo.*;
import static chessprogram.god.PieceMoveKnight.*;
import static chessprogram.god.PieceMovePawns.*;

class MoveGeneratorCheck {

    static void addCheckEvasionMoves(List<Move> moves, Chessboard board, boolean white){
        long myKing = (white) ? board.getWhiteKing() : board.getBlackKing();
        long ignoreThesePieces = PinnedManager.whichPiecesArePinned(board, white, myKing);
        // if a piece in pinned to the king, it can never be used to block / capture a different checker
        allLegalCheckEscapeMoves(moves, board, white, ignoreThesePieces);
    }


    private static void allLegalCheckEscapeMoves(List<Move> moves, Chessboard board, boolean white, long ignoreThesePieces) {
        long myKing = (white) ? board.getWhiteKing() : board.getBlackKing();
        long blockingSquaresMask, checkingPieceMask;
        long jumper = inCheckByAJumper(board, white);
        if (jumper != 0){
            blockingSquaresMask = 0;
            checkingPieceMask = jumper;
        }
        else {
            long slider = inCheckByASlider(board, white);
            blockingSquaresMask = extractRayFromTwoPieces(board, myKing, slider) & (~slider);
            checkingPieceMask = slider;
        }
        long PENULTIMATE_RANK = white ? BitboardResources.RANK_SEVEN : BitboardResources.RANK_TWO;
        long myPawns = white ? board.getWhitePawns() : board.getBlackPawns();
        long promotablePawns = myPawns & PENULTIMATE_RANK;
        long piecesToIgnoreAndPromotingPawns = ignoreThesePieces | promotablePawns;
        
        addPromotionMoves(moves, board, white, ignoreThesePieces, blockingSquaresMask, checkingPieceMask);
        
        addAllMovesWithoutKing
                (moves, board, white, piecesToIgnoreAndPromotingPawns, blockingSquaresMask, checkingPieceMask);

        addKingLegalMovesOnly(moves, board, white);
        
        addEnPassantMoves(moves, board, white, piecesToIgnoreAndPromotingPawns, blockingSquaresMask, checkingPieceMask);

    }

    // todo magic
    private static long extractRayFromTwoPieces(Chessboard board, long pieceOne, long pieceTwo){
        if (pieceOne == pieceTwo) return 0;
        long ALL_PIECES_TO_AVOID = board.whitePieces() | board.blackPieces();

        ALL_PIECES_TO_AVOID ^= pieceTwo;
        ALL_PIECES_TO_AVOID ^= pieceOne;
        
        // necessary as java offers signed ints, which get confused if talking about square 63
        int indexOfPieceOne = getIndexOfFirstPiece(pieceOne);
        int indexOfPieceTwo = getIndexOfFirstPiece(pieceTwo);
        long bigPiece = (indexOfPieceOne > indexOfPieceTwo) ? pieceOne : pieceTwo;
        long smallPiece = (indexOfPieceOne > indexOfPieceTwo) ? pieceTwo : pieceOne;
        long possibleAnswer = 0;
        
        while (true) {
            if ((smallPiece & BitboardResources.FILE_A) != 0) {
                break;
            }
            smallPiece <<= 1;
            if ((smallPiece & ALL_PIECES_TO_AVOID) != 0) {
                break;
            }
            if ((smallPiece & bigPiece) != 0) {
                return possibleAnswer;
            }
            possibleAnswer |= smallPiece;
        }

        bigPiece = (indexOfPieceOne > indexOfPieceTwo) ? pieceOne : pieceTwo;
        smallPiece = (indexOfPieceOne > indexOfPieceTwo) ? pieceTwo : pieceOne;
        possibleAnswer = 0;
        
        while (true) {
            if ((smallPiece & NORTH_WEST) != 0) {
                break;
            }
            smallPiece <<= 9;
            if ((smallPiece & ALL_PIECES_TO_AVOID) != 0) {
                break;
            }
            if ((smallPiece & bigPiece) != 0) {
                return possibleAnswer;
            }
            possibleAnswer |= smallPiece;
        }

        bigPiece = (indexOfPieceOne > indexOfPieceTwo) ? pieceOne : pieceTwo;
        smallPiece = (indexOfPieceOne > indexOfPieceTwo) ? pieceTwo : pieceOne;
        possibleAnswer = 0;

        while (true) {
            if ((smallPiece & BitboardResources.RANK_EIGHT) != 0) {
                break;
            }
            smallPiece <<= 8;
            if ((smallPiece & ALL_PIECES_TO_AVOID) != 0) {
                break;
            }
            if ((smallPiece & bigPiece) != 0) {
                return possibleAnswer;
            }
            possibleAnswer |= smallPiece;
        }

        bigPiece = (indexOfPieceOne > indexOfPieceTwo) ? pieceOne : pieceTwo;
        smallPiece = (indexOfPieceOne > indexOfPieceTwo) ? pieceTwo : pieceOne;
        possibleAnswer = 0;

        while (true) {
            if ((smallPiece & NORTH_EAST) != 0) {
                break;
            }
            smallPiece <<= 7;
            if ((smallPiece & ALL_PIECES_TO_AVOID) != 0) {
                break;
            }
            if ((smallPiece & bigPiece) != 0) {
                return possibleAnswer;
            }
            possibleAnswer |= smallPiece;
        }

        return 0;
    }

    public static long extractInfiniteRayFromTwoPieces(Chessboard board, long pieceOne, long pieceTwo){
        if (pieceOne == pieceTwo) return 0;

        int indexOfPieceOne = getIndexOfFirstPiece(pieceOne);
        int indexOfPieceTwo = getIndexOfFirstPiece(pieceTwo);
        long bigPiece = (indexOfPieceOne > indexOfPieceTwo) ? pieceOne : pieceTwo;
        long smallPiece = (indexOfPieceOne > indexOfPieceTwo) ? pieceTwo : pieceOne;
        long possibleAnswer = 0;
        long answer = 0;
        
        boolean thisOne = false;
        while (true) {
            if ((smallPiece & BitboardResources.FILE_A) != 0) {
                if (thisOne) {
                    answer |= possibleAnswer;
                }
                break;
            }
            smallPiece <<= 1;
            if ((smallPiece & bigPiece) != 0) {
                thisOne = true;
            }
            possibleAnswer |= smallPiece;
        }

        bigPiece = !(indexOfPieceOne > indexOfPieceTwo) ? pieceOne : pieceTwo;
        smallPiece = !(indexOfPieceOne > indexOfPieceTwo) ? pieceTwo : pieceOne;
        possibleAnswer = 0;
        
        thisOne = false;
        while (true) {
            if ((smallPiece & BitboardResources.FILE_H) != 0) {
                if (thisOne) {
                    answer |= possibleAnswer;
                }
                break;
            }
            smallPiece >>>= 1;
            if ((smallPiece & bigPiece) != 0) {
                thisOne = true;
            }
            possibleAnswer |= smallPiece;
        }

        bigPiece = (indexOfPieceOne > indexOfPieceTwo) ? pieceOne : pieceTwo;
        smallPiece = (indexOfPieceOne > indexOfPieceTwo) ? pieceTwo : pieceOne;
        possibleAnswer = 0;
        
        thisOne = false;
        while (true) {
            if ((smallPiece & NORTH_WEST) != 0) {
                if (thisOne) {
                    answer |= possibleAnswer;
                }
                break;
            }
            smallPiece <<= 9;
            if ((smallPiece & bigPiece) != 0) {
                thisOne = true;
            }
            possibleAnswer |= smallPiece;
        }

        bigPiece = !(indexOfPieceOne > indexOfPieceTwo) ? pieceOne : pieceTwo;
        smallPiece = !(indexOfPieceOne > indexOfPieceTwo) ? pieceTwo : pieceOne;
        possibleAnswer = 0;
        
        thisOne = false;
        while (true) {
            if ((smallPiece & SOUTH_EAST) != 0) {
                if (thisOne) {
                    answer |= possibleAnswer;
                }
                break;
            }
            smallPiece >>>= 9;
            if ((smallPiece & bigPiece) != 0) {
                thisOne = true;
            }
            possibleAnswer |= smallPiece;
        }

        bigPiece = (indexOfPieceOne > indexOfPieceTwo) ? pieceOne : pieceTwo;
        smallPiece = (indexOfPieceOne > indexOfPieceTwo) ? pieceTwo : pieceOne;
        possibleAnswer = 0;
        
        thisOne = false;
        while (true) {
            if ((smallPiece & BitboardResources.RANK_EIGHT) != 0) {
                if (thisOne) {
                    answer |= possibleAnswer;
                }
                break;
            }
            smallPiece <<= 8;
            if ((smallPiece & bigPiece) != 0) {
                thisOne = true;
            }
            possibleAnswer |= smallPiece;
        }

        bigPiece = !(indexOfPieceOne > indexOfPieceTwo) ? pieceOne : pieceTwo;
        smallPiece = !(indexOfPieceOne > indexOfPieceTwo) ? pieceTwo : pieceOne;
        possibleAnswer = 0;
        
        thisOne = false;
        while (true) {
            if ((smallPiece & BitboardResources.RANK_ONE) != 0) {
                if (thisOne) {
                    answer |= possibleAnswer;
                }
                break;
            }
            smallPiece >>>= 8;
            if ((smallPiece & bigPiece) != 0) {
                thisOne = true;
            }
            possibleAnswer |= smallPiece;
        }

        bigPiece = (indexOfPieceOne > indexOfPieceTwo) ? pieceOne : pieceTwo;
        smallPiece = (indexOfPieceOne > indexOfPieceTwo) ? pieceTwo : pieceOne;
        possibleAnswer = 0;
        
        thisOne = false;
        while (true) {
            if ((smallPiece & NORTH_EAST) != 0) {
                if (thisOne) {
                    answer |= possibleAnswer;
                }
                break;
            }
            smallPiece <<= 7;
            if ((smallPiece & bigPiece) != 0) {
                thisOne = true;
            }
            possibleAnswer |= smallPiece;
        }

        bigPiece = !(indexOfPieceOne > indexOfPieceTwo) ? pieceOne : pieceTwo;
        smallPiece = !(indexOfPieceOne > indexOfPieceTwo) ? pieceTwo : pieceOne;
        possibleAnswer = 0;
        
        thisOne = false;
        while (true) {
            if ((smallPiece & SOUTH_WEST) != 0) {
                if (thisOne) {
                    answer |= possibleAnswer;
                }
                break;
            }
            smallPiece >>>= 7;
            if ((smallPiece & bigPiece) != 0) {
                thisOne = true;
            }
            possibleAnswer |= smallPiece;
        }
        return answer;
    }


    private static long inCheckByAJumper(Chessboard board, boolean white){
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

        long possiblePawn = singlePawnCaptures(myKing, white, pawns);
        if (possiblePawn != 0) {
            return possiblePawn;
        }
        long possibleKnight = singleKnightTable(myKing, UNIVERSE) & knights;
        if (possibleKnight != 0) {
            return possibleKnight;
        }

        return 0;
    }

    private static long inCheckByASlider(Chessboard board, boolean white){
        long ans = 0, bishops, rooks, queens;
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

//        long possibleBishop = PieceMoveSliding.singleBishopTable(board, myKing, white, bishops);
        long possibleBishop = PieceMoveSliding.singleBishopTable(board, white, myKing, bishops);
        if (possibleBishop != 0) {
            return possibleBishop;
        }
        long possibleRook = PieceMoveSliding.singleRookTable(board, white, myKing, rooks);
        if (possibleRook != 0){
            return possibleRook;
        }
        long possibleQueen = PieceMoveSliding.singleQueenTable(board, white, myKing, queens);
        if (possibleQueen != 0){
            return possibleQueen;
        }
        return 0;
    }

}
