package com.github.louism33.chesscore;


import org.junit.Assert;

import static com.github.louism33.chesscore.BoardConstants.BLACK;
import static com.github.louism33.chesscore.BoardConstants.WHITE;
import static com.github.louism33.chesscore.StackConstants.*;

@SuppressWarnings("CanBeFinal")
class StackDataUtil {

    public static final int NONE = 0, BASICQUIETPUSH = 1, BASICLOUDPUSH = 2, 
            BASICCAPTURE = 3, ENPASSANTVICTIM = 4, ENPASSANTCAPTURE = 5, 
            CASTLING = 6, PROMOTION = 7, NULL_MOVE = 8;
    
    public static int getMove(long stackMoveData){
        return (int) (stackMoveData & SMD_MOVE_MASK);
    }
    
    public static int getFiftyPiece(long stackMoveData){
        return (int) ((stackMoveData & SMD_FIFTY_MOVES) >> smdFiftyPieceOffset);
    }

    public static int getTurn(long stackMoveData){
        return (int) ((stackMoveData & SMD_TURN) >> smdTurnOffset);
    }

    public static int getSpecialMove(long stackMoveData){
        return (int) ((stackMoveData & SMD_SPECIAL_MOVE) >> smdSpecialOffset);
    }

    public static int getEPMove(long stackMoveData){
        return (int) ((stackMoveData & SMD_EP_FILE) >> smdEPOffset);
    }

    public static int getCastlingRights(long stackMoveData){
        return (int) ((stackMoveData & SMD_CASTLE_FLAGS) >> smdCastleOffset);
    }

    public static long smdMakeMove(int move){
        return (long) move & SMD_MOVE_MASK;
    }

    public static long smdMakeFiftyPiece(int fifty){
        return (long) fifty << smdFiftyPieceOffset;
    }

    public static long smdMakeTurn(int turn){
        return (long) (1 - turn) << smdTurnOffset;
    }

    public static long smdMakeSpecialMove(long specialMove){
        return specialMove << smdSpecialOffset;
    }

//    public static long smdMakeSpecialMove(SpecialMove specialMove){
//        return (long) specialMove.ordinal() << smdSpecialOffset;
//    }

    public static long smdMakeEPMove(int epFile){
        return (long) epFile << smdEPOffset;
    }

    public static long smdMakeCastlingRights(Chessboard board){
        return smdMakeCastlingRights(board.castlingRights);
    }

    private static long smdMakeCastlingRights(int castlingRights){
        return (long) castlingRights << smdCastleOffset;
    }

    static long buildStackData(int move, Chessboard board,
                               long typeOfSpecialMove, int enPassantFile) {
        
        long epFile = smdMakeEPMove(enPassantFile);
        return buildStackData(move, board, typeOfSpecialMove) | epFile;
    }
    
    static long buildStackData(int move, Chessboard board, long typeOfSpecialMove) {
        long stackData = 0;

        Assert.assertTrue(board.isWhiteTurn() ? board.turn == WHITE : board.turn == BLACK);
        
        stackData |= smdMakeMove(move);
        stackData |= smdMakeFiftyPiece(board.getFiftyMoveCounter());
        stackData |= smdMakeTurn(board.turn);
        stackData |= smdMakeSpecialMove(typeOfSpecialMove);
        stackData |= smdMakeCastlingRights(board);

        return stackData;
    }
}