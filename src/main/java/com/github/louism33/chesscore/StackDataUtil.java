package com.github.louism33.chesscore;


import static com.github.louism33.chesscore.StackConstants.*;

@SuppressWarnings("CanBeFinal")
final class StackDataUtil {

    static final int NONE = 0, BASICQUIETPUSH = 1, BASICLOUDPUSH = 2, 
            BASICCAPTURE = 3, ENPASSANTVICTIM = 4, ENPASSANTCAPTURE = 5, 
            CASTLING = 6, PROMOTION = 7, NULL_MOVE = 8;
    
    static int getMove(long stackMoveData){
        return (int) (stackMoveData & SMD_MOVE_MASK);
    }
    
    public static int getFiftyPiece(long stackMoveData){
        return (int) ((stackMoveData & SMD_FIFTY_MOVES) >> smdFiftyPieceOffset);
    }

    static int getTurn(long stackMoveData){
        return (int) ((stackMoveData & SMD_TURN) >> smdTurnOffset);
    }

    static int getSpecialMove(long stackMoveData){
        return (int) ((stackMoveData & SMD_SPECIAL_MOVE) >> smdSpecialOffset);
    }

    static int getEPMove(long stackMoveData){
        return (int) ((stackMoveData & SMD_EP_FILE) >> smdEPOffset);
    }

    static int getCastlingRights(long stackMoveData){
        return (int) ((stackMoveData & SMD_CASTLE_FLAGS) >> smdCastleOffset);
    }

    static long buildStackDataBetter(int move, int turn, int fiftyCounter, int castlingRights, long typeOfSpecialMove, int enPassantFile) {
        return buildStackDataBetter(move, turn, fiftyCounter, castlingRights, typeOfSpecialMove) | ((long) enPassantFile << smdEPOffset);
    }
    
    static long buildStackDataBetter(int move, int turn, int fiftyCounter, int castlingRights, long typeOfSpecialMove) {
        long stackData = 0;

        stackData |= ((long) move & SMD_MOVE_MASK);
        stackData |= ((long) fiftyCounter << smdFiftyPieceOffset);
        stackData |= ((long) (1 - turn) << smdTurnOffset);
        stackData |= (typeOfSpecialMove << smdSpecialOffset);
        stackData |= ((long) castlingRights << smdCastleOffset);

        return stackData;
    }
    
}