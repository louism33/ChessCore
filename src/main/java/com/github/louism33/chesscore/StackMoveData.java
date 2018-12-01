package com.github.louism33.chesscore;

@SuppressWarnings("CanBeFinal")
class StackMoveData {
    
    public int move;
    public int takenPiece = 0;
    private int fiftyMoveCounter;
    public boolean whiteTurn;
    
    public enum SpecialMove {
        BASICQUIETPUSH, BASICLOUDPUSH, BASICCAPTURE, ENPASSANTVICTIM, ENPASSANTCAPTURE, CASTLING, PROMOTION, NULL_MOVE
    }
    public SpecialMove typeOfSpecialMove;
    
    // file one : FILE_A 
    public int enPassantFile = -1;
    public boolean whiteCanCastleK, whiteCanCastleQ, blackCanCastleK, blackCanCastleQ;


    public StackMoveData(int move, Chessboard board, int fiftyMoveCounter, SpecialMove typeOfSpecialMove) {
        this.move = move;
        this.fiftyMoveCounter = fiftyMoveCounter;
        this.typeOfSpecialMove = typeOfSpecialMove;

        this.whiteTurn = board.isWhiteTurn();
        
        this.whiteCanCastleK = board.isWhiteCanCastleK();
        this.whiteCanCastleQ = board.isWhiteCanCastleQ();
        this.blackCanCastleK = board.isBlackCanCastleK();
        this.blackCanCastleQ = board.isBlackCanCastleQ();
    }

    
    public StackMoveData(int move, Chessboard board, int fiftyMoveCounter, SpecialMove typeOfSpecialMove, int takenPiece) {
        this.move = move;
        this.fiftyMoveCounter = fiftyMoveCounter;
        this.typeOfSpecialMove = typeOfSpecialMove;
        this.takenPiece = takenPiece;

        this.whiteTurn = board.isWhiteTurn();

        this.whiteCanCastleK = board.isWhiteCanCastleK();
        this.whiteCanCastleQ = board.isWhiteCanCastleQ();
        this.blackCanCastleK = board.isBlackCanCastleK();
        this.blackCanCastleQ = board.isBlackCanCastleQ();
    }


    public StackMoveData(int move, Chessboard board, int fiftyMoveCounter, int enPassantFile, SpecialMove typeOfSpecialMove) {
        this.move = move;
        this.fiftyMoveCounter = fiftyMoveCounter;
        this.typeOfSpecialMove = typeOfSpecialMove;
        
        if (this.typeOfSpecialMove == SpecialMove.ENPASSANTVICTIM){
            this.enPassantFile = enPassantFile;
        }

        this.whiteTurn = board.isWhiteTurn();

        this.whiteCanCastleK = board.isWhiteCanCastleK();
        this.whiteCanCastleQ = board.isWhiteCanCastleQ();
        this.blackCanCastleK = board.isBlackCanCastleK();
        this.blackCanCastleQ = board.isBlackCanCastleQ();
    }


    @Override
    public String toString() {
        return "StackMoveData{" +
                "move=" + move +
                ", takenPiece=" + takenPiece +
                ", fiftyMoveCounter=" + fiftyMoveCounter +
                ", whiteTurn=" + whiteTurn +
                ", typeOfSpecialMove=" + typeOfSpecialMove +
                ", enPassantFile=" + enPassantFile +
                ", whiteCanCastleK=" + whiteCanCastleK +
                ", whiteCanCastleQ=" + whiteCanCastleQ +
                ", blackCanCastleK=" + blackCanCastleK +
                ", blackCanCastleQ=" + blackCanCastleQ +
                '}';
    }
}