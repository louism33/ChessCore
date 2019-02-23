package com.github.louism33.chesscore;

import org.junit.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.github.louism33.chesscore.BitOperations.*;
import static com.github.louism33.chesscore.BoardConstants.*;

public enum Square {

    H1, G1, F1, E1, D1, C1, B1, A1,
    H2, G2, F2, E2, D2, C2, B2, A2,
    H3, G3, F3, E3, D3, C3, B3, A3,
    H4, G4, F4, E4, D4, C4, B4, A4,
    H5, G5, F5, E5, D5, C5, B5, A5,
    H6, G6, F6, E6, D6, C6, B6, A6,
    H7, G7, F7, E7, D7, C7, B7, A7,
    H8, G8, F8, E8, D8, C8, B8, A8;

    public static boolean squareThreatenend(Chessboard board, boolean white, long square){
        long myKing, enemyPawns, enemyKnights, enemyBishops, enemyRooks, enemyQueen, enemyKing, enemies, friends;
        if (white){
            myKing = board.pieces[WHITE][KING];
            enemyPawns = board.pieces[BLACK][PAWN];
            enemyKnights = board.pieces[BLACK][KNIGHT];
            enemyBishops = board.pieces[BLACK][BISHOP];
            enemyRooks = board.pieces[BLACK][ROOK];
            enemyQueen = board.pieces[BLACK][QUEEN];
            enemyKing = board.pieces[BLACK][KING];

            enemies = board.blackPieces();
            friends = board.whitePieces();
        } else {
            myKing = board.pieces[BLACK][KING];
            enemyPawns = board.pieces[WHITE][PAWN];
            enemyKnights = board.pieces[WHITE][KNIGHT];
            enemyBishops = board.pieces[WHITE][BISHOP];
            enemyRooks = board.pieces[WHITE][ROOK];
            enemyQueen = board.pieces[WHITE][QUEEN];
            enemyKing = board.pieces[WHITE][KING];

            enemies = board.whitePieces();
            friends = board.blackPieces();
        }

        return CheckHelper.numberOfPiecesThatLegalThreatenSquare(white, square,
                enemyPawns, enemyKnights, enemyBishops, enemyRooks, enemyQueen, enemyKing,
                board.allPieces()) > 0;
    }

    public static Square getSquareOfBitboard(long piece){
        return values()[getIndexOfFirstPiece(piece)];
    }
    
    public static List<Square> getPiecesOnSquare(long pieces){
        return getAllPieces(pieces, 0)
                .stream()
                .map(Square::getSquareOfBitboard)
                .collect(Collectors.toList());
    }
    
    public static long longFromSquare (Square square){
        final int ordinal = square.ordinal();
        return newPieceOnSquare(ordinal);
    }

    public static List<Square> squaresFromBitboard(long bitboard){
        List<Square> squares = new ArrayList<>();
        while (bitboard != 0){
            final long piece = BitOperations.getFirstPiece(bitboard);
            squares.add(squareFromSingleBitboard(piece));
            bitboard &= bitboard - 1;
        }
        return squares;
    }
    
    public static Square squareFromSingleBitboard(long bitboard){
        Assert.assertEquals(populationCount(bitboard), 1);
        final int index = BitOperations.getIndexOfFirstPiece(bitboard);
        return values()[index];
    }

    public long toBitboard (){
        return newPieceOnSquare(this.ordinal());
    }
    
    public int getRowNumber(){
        return this.ordinal() / 8;
    }

    /**
     * file A : 7, file H: 0
     * @return 0 - 7
     */
    public int getFileNumber(){
        return this.ordinal() % 8;
    }
    
    public static long getRow(int index){ 
        return ROWS[index / 8];
    }
    
    public static long getFile(int index){
        return FILES[index % 8];
    }
    
    
    
}
