package chessprogram.god;

import static chessprogram.god.BitOperations.newPieceOnSquare;
import static chessprogram.god.MoveMakingCastlingIntMove.castleFlagManager;
import static chessprogram.god.MoveMakingCastlingIntMove.makeCastlingMove;
import static chessprogram.god.MoveMakingEnPassantIntMove.makeEnPassantMove;
import static chessprogram.god.MovePromotionIntMove.makePromotingMove;
import static chessprogram.god.MoveRegularIntMove.makeRegularMove;
import static chessprogram.god.StackMoveDataIntMove.SpecialMove;
import static chessprogram.god.StackMoveDataIntMove.SpecialMove.*;

class MoveMakerIntMove {

    static void flipTurn(ChessboardIntMove board){
        board.setWhiteTurn(!board.isWhiteTurn());
    }

    static void makeMoveMaster(ChessboardIntMove board, int move) {

        if(move == 0){
            StackMoveDataIntMove stackMoveData = new StackMoveDataIntMove(0, board, 50, SpecialMove.NULL_MOVE);
            board.moveStack.push(stackMoveData);
            return;
        }
        
        if (MoveParserIntMove.isSpecialMove(move)){
            if (MoveParserIntMove.isCastlingMove(move)) {
                StackMoveDataIntMove stackMoveData = new StackMoveDataIntMove(move, board, 50, CASTLING);
                board.moveStack.push(stackMoveData);
                makeCastlingMove(board, move);
                castleFlagManager(board, move);
            }

            else if (MoveParserIntMove.isEnPassantMove(move)){
                StackMoveDataIntMove stackMoveData = new StackMoveDataIntMove
                        (move, board, 50, ENPASSANTCAPTURE);
                board.moveStack.push(stackMoveData);
                makeEnPassantMove(board, move);
                castleFlagManager(board, move);
            }

            else if (MoveParserIntMove.isPromotionMove(move)){
                int destination = MoveParserIntMove.getDestinationIndex(move);
                long destSquare = newPieceOnSquare(destination);
                boolean capturePromotion = (destSquare & board.allPieces()) != 0;
                if (capturePromotion) {
                    long destinationPiece = newPieceOnSquare(MoveParserIntMove.getDestinationIndex(move));
                    int takenPiece = whichPieceOnSquare(board, destinationPiece);

                    StackMoveDataIntMove stackMoveData = new StackMoveDataIntMove(move, board, 50, PROMOTION, takenPiece);
                    board.moveStack.push(stackMoveData);
                    makePromotingMove(board, move);
                    castleFlagManager(board, move);
                }
                else {
                    StackMoveDataIntMove stackMoveData = new StackMoveDataIntMove(move, board, 50, PROMOTION);
                    board.moveStack.push(stackMoveData);
                    makePromotingMove(board, move);
                    castleFlagManager(board, move);
                }

            }
        }


        else {
            int destination = MoveParserIntMove.getDestinationIndex(move);
            long destSquare = newPieceOnSquare(destination);
            boolean captureMove = (destSquare & board.allPieces()) != 0;
            if (captureMove) {
                long destinationPiece = newPieceOnSquare(MoveParserIntMove.getDestinationIndex(move));
                int takenPiece = whichPieceOnSquare(board, destinationPiece);
                StackMoveDataIntMove stackMoveData = new StackMoveDataIntMove
                        (move, board, 50, BASICCAPTURE, takenPiece);
                board.moveStack.push(stackMoveData);
                makeRegularMove(board, move);
                castleFlagManager(board, move);
            }
            
            else if (enPassantPossibility(board, move)){
                int sourceAsPiece = MoveParserIntMove.getSourceIndex(move);
                int whichFile = 8 - sourceAsPiece % 8;
                StackMoveDataIntMove stackMoveData = new StackMoveDataIntMove
                        (move, board, 50, whichFile, ENPASSANTVICTIM);
                board.moveStack.push(stackMoveData);
                makeRegularMove(board, move);
                castleFlagManager(board, move);
            }

            else {
                long destinationPiece = newPieceOnSquare(MoveParserIntMove.getSourceIndex(move));
                int movingPiece = whichPieceOnSquare(board, destinationPiece);
                if (movingPiece == 1 || movingPiece == 7){
                    StackMoveDataIntMove stackMoveData = new StackMoveDataIntMove
                            (move, board, 50, BASICLOUDPUSH);
                    board.moveStack.push(stackMoveData);
                    makeRegularMove(board, move);
                    castleFlagManager(board, move);
                }
                else {
                    // increment 50 move rule
                    StackMoveDataIntMove stackMoveData = new StackMoveDataIntMove
                            (move, board, 50, BASICQUIETPUSH);
                    board.moveStack.push(stackMoveData);
                    makeRegularMove(board, move);
                    castleFlagManager(board, move);
                }
            }
        }
    }

    private static boolean enPassantPossibility(ChessboardIntMove board, int move){
        // determine if flag should be added to enable EP on next turn
        long sourceSquare = newPieceOnSquare(MoveParserIntMove.getSourceIndex(move));
        long destinationSquare = newPieceOnSquare(MoveParserIntMove.getDestinationIndex(move));
        long HOME_RANK = (board.isWhiteTurn()) ? BitboardResources.RANK_TWO : BitboardResources.RANK_SEVEN;
        long MY_PAWNS = (board.isWhiteTurn()) ? board.getWhitePawns() : board.getBlackPawns();
        long enPassantPossibilityRank = (board.isWhiteTurn()) ? BitboardResources.RANK_FOUR : BitboardResources.RANK_FIVE;

        if ((sourceSquare & HOME_RANK) == 0){
            return false;
        }

        if ((sourceSquare & MY_PAWNS) == 0){
            return false;
        }
        return (destinationSquare & enPassantPossibilityRank) != 0;
    }


    public static int whichPieceOnSquare(ChessboardIntMove board, long destinationPiece){

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
