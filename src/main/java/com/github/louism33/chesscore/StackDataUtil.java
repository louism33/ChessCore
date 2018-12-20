package com.github.louism33.chesscore;


import static com.github.louism33.chesscore.ConstantsMoveStackData.*;

@SuppressWarnings("CanBeFinal")
class StackDataUtil {

//    public enum SpecialMove {
//        NONE, BASICQUIETPUSH, BASICLOUDPUSH, BASICCAPTURE, ENPASSANTVICTIM, ENPASSANTCAPTURE, CASTLING, PROMOTION, NULL_MOVE
//    }
    
    public static final long NONE = 0, BASICQUIETPUSH = 1, BASICLOUDPUSH = 2, 
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
        return (long) turn << smdTurnOffset;
    }

    public static long smdMakeTurn(boolean t){
        int turn = t ? 1 : 0;
        return (long) turn << smdTurnOffset;
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
        int numTo15 = 0;
        if (board.isWhiteCanCastleK()){
            numTo15 += 1;
        }
        if (board.isWhiteCanCastleQ()){
            numTo15 += 2;
        }
        if (board.isBlackCanCastleK()){
            numTo15 += 4;
        }
        if (board.isBlackCanCastleQ()){
            numTo15 += 8;
        }
        return smdMakeCastlingRights(numTo15);
    }

    private static long smdMakeCastlingRights(int castlingRights){
        return (long) castlingRights << smdCastleOffset;
    }

    static long buildStackData(int move, Chessboard board, int fiftyMoveCounter,
                               int enPassantFile) {
        
        long epFile = smdMakeEPMove(enPassantFile);
        return buildStackData(move, board, 50, StackDataUtil.ENPASSANTVICTIM) | epFile;
    }
    
    static long buildStackData(int move, Chessboard board, int fiftyMoveCounter, long typeOfSpecialMove) {
        long stackData = 0;
        
        stackData |= smdMakeMove(move);
        stackData |= smdMakeFiftyPiece(fiftyMoveCounter);
        stackData |= smdMakeTurn(board.isWhiteTurn());
        stackData |= smdMakeSpecialMove(typeOfSpecialMove);
        stackData |= smdMakeCastlingRights(board);

        return stackData;
    }
}