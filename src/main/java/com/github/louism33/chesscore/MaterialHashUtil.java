package com.github.louism33.chesscore;

import static com.github.louism33.chesscore.BoardConstants.*;

public final class MaterialHashUtil {
    /*
    stronger pieces are stored higher, so we can easily see if queens are missing for ex
    
    00QQQqqq
    RRrrBBbb
    WWwwNNnn
    PPPPpppp
    
    
    p: black pawn
    n: black knight
    w: black w bishop
    b: black b bishop
    r: black rook
    q: black queen
    
    P: white pawn
    N: white knight
    W: white w bishop
    B: white b bishop
    R: white rook
    Q: white queen
     */



    public static final int whiteQueen = 0x8000000;
    public static final int blackQueen = 0x1000000;
    public static final int whiteRook = 0x400000;
    public static final int blackRook = 0x100000;
    public static final int whiteBlackBishop = 0x40000;
    public static final int blackBlackBishop = 0x10000;
    public static final int whiteWhiteBishop = 0x4000;
    public static final int blackWhiteBishop = 0x1000;

    public static final int whiteKnight = 0x400;
    public static final int blackKnight = 0x100;
    public static final int whitePawn = 0x10;
    public static final int blackPawn = 0x1;

    public static final int startingMaterialHash =
            whitePawn * 8
                    + blackPawn * 8
                    + whiteKnight * 2
                    + blackKnight * 2
                    + whiteWhiteBishop
                    + whiteBlackBishop
                    + blackWhiteBishop
                    + blackBlackBishop
                    + whiteRook * 2
                    + blackRook * 2
                    + whiteQueen
                    + blackQueen
            ;



    public static final int whiteQueenMask = 0x38000000;
    public static final int blackQueenMask = 0x7000000;
    public static final int whiteRookMask = 0xc00000;
    public static final int blackRookMask = 0x300000;
    public static final int whiteBlackBishopMask = 0xc0000;
    public static final int blackBlackBishopMask = 0x30000;
    public static final int whiteWhiteBishopMask = 0xc000;
    public static final int blackWhiteBishopMask = 0x3000;

    public static final int whiteKnightMask = 0xc00;
    public static final int blackKnightMask = 0x300;
    public static final int whitePawnMask = 0xf0;
    public static final int blackPawnMask = 0xf;



    public static final int[] basicallyDrawnPositions = {
            0,
            // 1 piece
            blackKnight,
            whiteKnight,
            whiteWhiteBishop,
            blackWhiteBishop,
            whiteBlackBishop,
            blackBlackBishop,

            // 2 piece
            // combinations that don't work
            blackKnight + blackKnight,
            whiteKnight + whiteKnight,
            whiteWhiteBishop + whiteWhiteBishop,
            blackWhiteBishop + blackWhiteBishop,
            whiteBlackBishop + whiteBlackBishop,
            blackBlackBishop + blackBlackBishop,

            blackKnight + whiteKnight,
            whiteKnight + blackKnight,
            whiteWhiteBishop + blackWhiteBishop,
            whiteWhiteBishop + blackBlackBishop,
            whiteBlackBishop + blackBlackBishop,
            whiteBlackBishop + blackWhiteBishop,
            
            
            // 3 piece
            // 2 minor pieces v 1 draw, apart from bishop pair
            blackKnight + blackKnight + whiteWhiteBishop,
            blackKnight + blackKnight + whiteBlackBishop,
            blackKnight + blackKnight + whiteKnight,
            
            whiteKnight + whiteKnight + blackWhiteBishop,
            whiteKnight + whiteKnight + blackBlackBishop,
            whiteKnight + whiteKnight + blackKnight,

            whiteWhiteBishop + whiteWhiteBishop + blackWhiteBishop,
            whiteWhiteBishop + whiteWhiteBishop + blackBlackBishop,
            whiteWhiteBishop + whiteWhiteBishop + blackKnight,
            blackWhiteBishop + blackWhiteBishop + whiteWhiteBishop,
            blackWhiteBishop + blackWhiteBishop + whiteBlackBishop,
            blackWhiteBishop + blackWhiteBishop + whiteKnight,

            whiteBlackBishop + whiteBlackBishop + blackBlackBishop,
            whiteBlackBishop + whiteBlackBishop + blackWhiteBishop,
            whiteBlackBishop + whiteBlackBishop + blackKnight,
            blackBlackBishop + blackBlackBishop + whiteBlackBishop,
            blackBlackBishop + blackBlackBishop + whiteWhiteBishop,
            blackBlackBishop + blackBlackBishop + whiteKnight,
    };

    public static boolean isBasicallyDrawn(Chessboard board){
        return contains(basicallyDrawnPositions, board.materialHash);
    }
    
    public static int addPieceToMaterialHash(int hash, int piece, long destinationSquare) {
        switch (piece) {
            case WHITE_PAWN:
                return hash + whitePawn;
            case BLACK_PAWN:
                return hash + blackPawn;
            case WHITE_KNIGHT:
                return hash + whiteKnight;
            case BLACK_KNIGHT:
                return hash + blackKnight;
            case WHITE_BISHOP:
                return ((destinationSquare & WHITE_COLOURED_SQUARES) == 0) ? hash + whiteBlackBishop : hash + whiteWhiteBishop;
            case BLACK_BISHOP:
                return ((destinationSquare & WHITE_COLOURED_SQUARES) == 0) ? hash + blackBlackBishop : hash + blackWhiteBishop;
            case WHITE_ROOK:
                return hash + whiteRook;
            case BLACK_ROOK:
                return hash + blackRook;
            case WHITE_QUEEN:
                return hash + whiteQueen;
            case BLACK_QUEEN:
                return hash + blackQueen;
            default: // ignore king
                return hash;
        }
    }
    
    public static int removePieceFromMaterialHash(int hash, int piece, long destinationSquare) {
        switch (piece) {
            case WHITE_PAWN:
                return hash - whitePawn;
            case BLACK_PAWN:
                return hash - blackPawn;
            case WHITE_KNIGHT:
                return hash - whiteKnight;
            case BLACK_KNIGHT:
                return hash - blackKnight;
            case WHITE_BISHOP:
                return ((destinationSquare & WHITE_COLOURED_SQUARES) == 0) ? hash - whiteBlackBishop : hash - whiteWhiteBishop;
            case BLACK_BISHOP:
                return ((destinationSquare & WHITE_COLOURED_SQUARES) == 0) ? hash - blackBlackBishop : hash - blackWhiteBishop;
            case WHITE_ROOK:
                return hash - whiteRook;
            case BLACK_ROOK:
                return hash - blackRook;
            case WHITE_QUEEN:
                return hash - whiteQueen;
            case BLACK_QUEEN:
                return hash - blackQueen;
            default:
                return hash;
        }
    }

    public static int makeMaterialHash(Chessboard board) {
        int hash = 0;
        final int length = board.pieceSquareTable.length;
        for (int i = 0; i < length; i++) {
            final int piece = board.pieceSquareTable[i];
            if (piece != NO_PIECE) {
                hash = addPieceToMaterialHash(hash, piece, BitOperations.newPieceOnSquare(i));
            }
        }
        return hash;
    }



    public static boolean contains(int[] ints, int target) {
        for (int i = 0; i < ints.length; i++) {
            if (ints[i] == target) {
                return true;
            }
        }
        return false;
    }


    public static void main(String[] args) {
//        Art.printLong(whiteQueen);
//        Art.printLong(blackQueen);
//        Art.printLong(whiteRook);
//        Art.printLong(blackRook);
//        Art.printLong(whiteBlackBishop);
//        Art.printLong(blackBlackBishop);
//        Art.printLong(whiteWhiteBishop);
//        Art.printLong(blackWhiteBishop);
//        Art.printLong(whiteKnight);
//        Art.printLong(blackKnight);
        Art.printLong(whitePawn);
        Art.printLong(addPieceToMaterialHash(0, WHITE_PAWN, 0));
        Art.printLong(whitePawn+whitePawn);
        Art.printLong(addPieceToMaterialHash(whitePawn, WHITE_PAWN, 0));
//        Art.printLong(blackPawn);
//
//        Art.printLong(whiteQueenMask);
//        Art.printLong(blackQueenMask);
//        Art.printLong(whiteRookMask);
//        Art.printLong(blackRookMask);
//        Art.printLong(whiteBlackBishopMask);
//        Art.printLong(blackBlackBishopMask);
//        Art.printLong(whiteWhiteBishopMask);
//        Art.printLong(blackWhiteBishopMask);
//        Art.printLong(whiteKnightMask);
//        Art.printLong(blackKnightMask);
//        Art.printLong(whitePawnMask);
//        Art.printLong(blackPawnMask);
//        Perft.perftTest(6, new Chessboard(), 8);
    }
}
