package chessprogram.moveMaking;

import chessprogram.bitboards.BitBoardUtils;
import chessprogram.chessboard.Chessboard;
import chessprogram.move.Move;

import static chessprogram.bitboards.BitManipulations.newPieceOnSquare;
import static chessprogram.moveMaking.StackMoveData.SpecialMove.*;

public class MoveOrganiser {

    public static void flipTurn(Chessboard board){
        board.setWhiteTurn(!board.isWhiteTurn());
    }

    public static int captures = 0;
    public static int promotions = 0;
    public static int castlings = 0;
    public static int epNum = 0;
    
    public static void makeMoveMaster(Chessboard board, Move move) {

        if(move== null){
            System.out.println("null move passed to makeMove Master");
            return;
        }
        
        if (move.isSpecialMove()){
            if (move.isCastlingMove()) {
                castlings++;
                StackMoveData stackMoveData = new StackMoveData(move, board, 50, CASTLING);
                board.moveStack.push(stackMoveData);
                MoveCastling.makeCastlingMove(board, move);
                MoveCastling.castleFlagManager(board, move);
            }

            else if (move.isEnPassantMove()){
                epNum++;
                StackMoveData stackMoveData = new StackMoveData
                        (move, board, 50, ENPASSANTCAPTURE);
                board.moveStack.push(stackMoveData);
                MoveEnPassant.makeEnPassantMove(board, move);
                MoveCastling.castleFlagManager(board, move);
            }

            else if (move.isPromotionMove()){
                int destination = move.getDestinationIndex();
                long destSquare = newPieceOnSquare(destination);
                boolean capturePromotion = (destSquare & board.allPieces()) != 0;
                if (capturePromotion) {
                    long destinationPiece = newPieceOnSquare(move.getDestinationIndex());
                    int takenPiece = whichPieceOnSquare(board, destinationPiece);

                    promotions++;
                    captures++;
                    StackMoveData stackMoveData = new StackMoveData(move, board, 50, PROMOTION, takenPiece);
                    board.moveStack.push(stackMoveData);
                    MovePromotion.makePromotingMove(board, move);
                    MoveCastling.castleFlagManager(board, move);
                }
                else {
                    promotions++;
                    StackMoveData stackMoveData = new StackMoveData(move, board, 50, PROMOTION);
                    board.moveStack.push(stackMoveData);
                    MovePromotion.makePromotingMove(board, move);
                    MoveCastling.castleFlagManager(board, move);
                }

            }
        }


        else {
            int destination = move.getDestinationIndex();
            long destSquare = newPieceOnSquare(destination);
            boolean captureMove = (destSquare & board.allPieces()) != 0;
            if (captureMove) {
                captures++;
                long destinationPiece = newPieceOnSquare(move.getDestinationIndex());
                int takenPiece = whichPieceOnSquare(board, destinationPiece);
                StackMoveData stackMoveData = new StackMoveData
                        (move, board, 50, BASICCAPTURE, takenPiece);
                board.moveStack.push(stackMoveData);
                MoveRegular.makeRegularMove(board, move);
                MoveCastling.castleFlagManager(board, move);
            }
            
            else if (enPassantPossibility(board, move)){
                int sourceAsPiece = move.getSourceIndex();
                int whichFile = 8 - sourceAsPiece % 8;
                StackMoveData stackMoveData = new StackMoveData
                        (move, board, 50, whichFile, ENPASSANTVICTIM);
                board.moveStack.push(stackMoveData);
                MoveRegular.makeRegularMove(board, move);
                MoveCastling.castleFlagManager(board, move);
            }

            else {
                long destinationPiece = newPieceOnSquare(move.getSourceIndex());
                int movingPiece = whichPieceOnSquare(board, destinationPiece);
                if (movingPiece == 1 || movingPiece == 7){
                    StackMoveData stackMoveData = new StackMoveData
                            (move, board, 50, BASICLOUDPUSH);
                    board.moveStack.push(stackMoveData);
                    MoveRegular.makeRegularMove(board, move);
                    MoveCastling.castleFlagManager(board, move);
                }
                else {
                    // increment 50 move rule
                    StackMoveData stackMoveData = new StackMoveData
                            (move, board, 50, BASICQUIETPUSH);
                    board.moveStack.push(stackMoveData);
                    MoveRegular.makeRegularMove(board, move);
                    MoveCastling.castleFlagManager(board, move);
                }
            }
        }
    }

    private static boolean enPassantPossibility(Chessboard board, Move move){
        // determine if flag should be added to enable EP on next turn
        long sourceSquare = newPieceOnSquare(move.getSourceIndex());
        long destinationSquare = newPieceOnSquare(move.getDestinationIndex());
        long HOME_RANK = (board.isWhiteTurn()) ? BitBoardUtils.RANK_TWO : BitBoardUtils.RANK_SEVEN;
        long MY_PAWNS = (board.isWhiteTurn()) ? board.getWhitePawns() : board.getBlackPawns();
        long enPassantPossibilityRank = (board.isWhiteTurn()) ? BitBoardUtils.RANK_FOUR : BitBoardUtils.RANK_FIVE;

        if ((sourceSquare & HOME_RANK) == 0){
            return false;
        }

        if ((sourceSquare & MY_PAWNS) == 0){
            return false;
        }
        return (destinationSquare & enPassantPossibilityRank) != 0;
    }


    public static int whichPieceOnSquare(Chessboard board, long destinationPiece){

        if ((destinationPiece & board.allPieces()) == 0){
            return 0;
        }

        if ((destinationPiece & board.getWhitePawns()) != 0){
            return 1;
        }
        else if ((destinationPiece & board.getWhiteKnights()) != 0){
            return 2;
        }
        else if ((destinationPiece & board.getWhiteBishops()) != 0){
            return 3;
        }
        else if ((destinationPiece & board.getWhiteRooks()) != 0){
            return 4;
        }
        else if ((destinationPiece & board.getWhiteQueen()) != 0){
            return 5;
        }
        else if ((destinationPiece & board.getWhiteKing()) != 0){
            return 6;
        }

        else if ((destinationPiece & board.getBlackPawns()) != 0){
            return 7;
        }
        else if ((destinationPiece & board.getBlackKnights()) != 0){
            return 8;
        }
        else if ((destinationPiece & board.getBlackBishops()) != 0){
            return 9;
        }
        else if ((destinationPiece & board.getBlackRooks()) != 0){
            return 10;
        }
        else if ((destinationPiece & board.getBlackQueen()) != 0){
            return 11;
        }
        else if ((destinationPiece & board.getBlackKing()) != 0) {
            return 12;
        }
        else {
            throw new RuntimeException("false entry");
        }
    }

}
