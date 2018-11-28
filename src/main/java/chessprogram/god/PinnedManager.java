package chessprogram.god;

import org.junit.Assert;

import static chessprogram.god.BitboardResources.*;

class PinnedManager {

    //todo, use magic here
    static long whichPiecesArePinned(Chessboard board, boolean white, long squareOfInterest){
        if (squareOfInterest == 0) {
            return 0;
        }

        if (false) {
            System.out.println("Testing Diagonal pin");

            final long l = diagonalPins(board, white, squareOfInterest);
            final long l1 = diagonalPinsMagic(board, white, squareOfInterest);
            if (l != l1) {
                Art.printLong(l);
                Art.printLong(l1);
            }
            Assert.assertEquals(l, l1);
        }

        
        
        long pinnedPieces = 0;
        pinnedPieces |= diagonalPins(board, white, squareOfInterest);
        pinnedPieces |= cardinalPins(board, white, squareOfInterest);
        return pinnedPieces;
    }

    private static long diagonalPinsMagic(Chessboard board, boolean white, long squareOfInterest) {
        return 0;
    }

    private static long diagonalPins(Chessboard board, boolean white, long squareOfInterest) {
        long ALL_PIECES = board.whitePieces() | board.blackPieces();
        long myPieces = (white) ? board.whitePieces() : board.blackPieces();
        long enemyPieces = (!white) ? board.whitePieces() : board.blackPieces();
        long diagonalThreats = (white) ? (board.getBlackBishops() | board.getBlackQueen()) : (board.getWhiteBishops() | board.getWhiteQueen());
        long diagonalPinnedPieces = 0;

        long temp = squareOfInterest;
        thisDirection:
        while (true) {
            if ((temp & NORTH_WEST) != 0) {
                break;
            }
            temp <<= 9;
            if ((temp & myPieces) != 0) {
                long possiblePin = temp;
                while ((temp & NORTH_WEST) == 0) {
                    temp <<= 9;
                    if ((temp & diagonalThreats) != 0) {
                        diagonalPinnedPieces |= possiblePin;
                        break thisDirection;
                    }
                    // end loop if encounter a non diagonal / cardinal pinner piece, or Another friendly piece
                    if ((temp & enemyPieces) != 0) { // these two can be combined
                        break thisDirection;
                    }
                    if ((temp & myPieces) != 0) {
                        break thisDirection;
                    }
                }
            }
            // end loop if encounter a non diagonal / cardinal pinner piece
            if ((temp & enemyPieces) != 0) {
                break;
            }
        }

        temp = squareOfInterest;
        thisDirection:
        while (true) {
            if ((temp & NORTH_EAST) != 0) {
                break;
            }
            temp <<= 7;
            if ((temp & myPieces) != 0) {
                long possiblePin = temp;
                while ((temp & NORTH_EAST) == 0) {
                    temp <<= 7;
                    if ((temp & diagonalThreats) != 0) {
                        diagonalPinnedPieces |= possiblePin;
                        break thisDirection;
                    }
                    if ((temp & enemyPieces) != 0) {
                        break thisDirection;
                    }
                    if ((temp & myPieces) != 0) {
                        break thisDirection;
                    }
                }
            }
            if ((temp & enemyPieces) != 0) {
                break;
            }
        }

        temp = squareOfInterest;
        thisDirection:
        while (true) {
            if ((temp & SOUTH_WEST) != 0) {
                break;
            }
            temp >>>= 7;
            if ((temp & myPieces) != 0) {
                long possiblePin = temp;
                while ((temp & SOUTH_WEST) == 0) {
                    temp >>>= 7;
                    if ((temp & diagonalThreats) != 0) {
                        diagonalPinnedPieces |= possiblePin;
                        break thisDirection;
                    }
                    if ((temp & enemyPieces) != 0) {
                        break thisDirection;
                    }
                    if ((temp & myPieces) != 0) {
                        break thisDirection;
                    }
                }
            }
            if ((temp & enemyPieces) != 0) {
                break;
            }
        }


        temp = squareOfInterest;
        thisDirection:
        while (true) {
            if ((temp & SOUTH_EAST) != 0) {
                break;
            }
            temp >>>= 9;
            if ((temp & myPieces) != 0) {
                long possiblePin = temp;
                while ((temp & SOUTH_EAST) == 0) {
                    temp >>>= 9;
                    if ((temp & diagonalThreats) != 0) {
                        diagonalPinnedPieces |= possiblePin;
                        break thisDirection;
                    }
                    if ((temp & enemyPieces) != 0) {
                        break thisDirection;
                    }
                    if ((temp & myPieces) != 0) {
                        break thisDirection;
                    }
                }
            }
            if ((temp & enemyPieces) != 0) {
                break;
            }
        }

        return diagonalPinnedPieces;
    }

    // todo: combine and use queen magic ?
    private static long cardinalPinsMagicToSquare(Chessboard board, boolean white, Square squareOfInterest) {
        return cardinalPinsMagicToSquare(board, white, squareOfInterest.toBitboard());
    }
    
    private static long cardinalPinsMagicToSquare(Chessboard board, boolean white, long squareOfInterest) {
        long myPieces, enemyRooks, enemyQueens, allPieces = board.allPieces();
        
        if (white){
            myPieces = board.whitePieces();
            enemyQueens = board.getBlackQueen();
            enemyRooks = board.getBlackRooks();
        } else {
            myPieces = board.blackPieces();
            enemyQueens = board.getWhiteQueen();
            enemyRooks = board.getWhiteRooks();
        }
        
        long pinnedPieces = 0;
        
        long pinners = PieceMoveSliding.xrayRookAttacks(allPieces, myPieces, squareOfInterest);
        long pinningPieces = pinners & (enemyRooks | enemyQueens);
        
        while (pinningPieces != 0){
            final int indexOfPinningPiece = BitOperations.getIndexOfFirstPiece(pinningPieces);
            final long ray = Magic.extractRayFromTwoPieces(indexOfPinningPiece, BitOperations.getIndexOfFirstPiece(squareOfInterest));
            pinnedPieces |= (ray & myPieces);
            pinningPieces &= pinningPieces - 1;
        }
        
        return pinnedPieces;
    }

    private static long cardinalPins(Chessboard board, boolean white, long squareOfInterest) {
        long ALL_PIECES = board.whitePieces() | board.blackPieces();
        long myPieces = (white) ? board.whitePieces() : board.blackPieces();
        long enemyPieces = (!white) ? board.whitePieces() : board.blackPieces();
        long cardinalThreats = (white) ? (board.getBlackRooks() | board.getBlackQueen()) : (board.getWhiteRooks() | board.getWhiteQueen());

        long cardinalPinnedPieces = 0;

        long temp = squareOfInterest;
        thisDirection:
        while (true) {
            if ((temp & FILE_A) != 0) {
                break;
            }
            temp <<= 1;
            if ((temp & myPieces) != 0) {
                long possiblePin = temp;
                while ((temp & FILE_A) == 0) {
                    temp <<= 1;
                    if ((temp & cardinalThreats) != 0) {
                        cardinalPinnedPieces |= possiblePin;
                        break thisDirection;
                    }
                    if ((temp & enemyPieces) != 0) {
                        break thisDirection;
                    }
                    if ((temp & myPieces) != 0) {
                        break thisDirection;
                    }
                }
            }
            if ((temp & enemyPieces) != 0) {
                break;
            }
        }

        temp = squareOfInterest;
        thisDirection:
        while (true) {
            if ((temp & RANK_EIGHT) != 0) {
                break;
            }
            temp <<= 8;
            if ((temp & myPieces) != 0) {
                long possiblePin = temp;
                while ((temp & RANK_EIGHT) == 0) {
                    temp <<= 8;
                    if ((temp & cardinalThreats) != 0) {
                        cardinalPinnedPieces |= possiblePin;
                        break thisDirection;
                    }
                    if ((temp & enemyPieces) != 0) {
                        break thisDirection;
                    }
                    if ((temp & myPieces) != 0) {
                        break thisDirection;
                    }
                }
            }
            if ((temp & enemyPieces) != 0) {
                break;
            }
        }

        temp = squareOfInterest;
        thisDirection:
        while (true) {
            if ((temp & FILE_H) != 0) {
                break;
            }
            temp >>>= 1;
            if ((temp & myPieces) != 0) {
                long possiblePin = temp;
                while ((temp & FILE_H) == 0) {
                    temp >>>= 1;
                    if ((temp & cardinalThreats) != 0) {
                        cardinalPinnedPieces |= possiblePin;
                        break thisDirection;
                    }
                    if ((temp & enemyPieces) != 0) {
                        break thisDirection;
                    }
                    if ((temp & myPieces) != 0) {
                        break thisDirection;
                    }
                }
            }
            if ((temp & enemyPieces) != 0){
                break;
            }
        }


        temp = squareOfInterest;
        thisDirection:
        while (true) {
            if ((temp & RANK_ONE) != 0) {
                break;
            }
            temp >>>= 8;
            if ((temp & myPieces) != 0) {
                long possiblePin = temp;
                while ((temp & RANK_ONE) == 0) {
                    temp >>>= 8;
                    if ((temp & cardinalThreats) != 0) {
                        cardinalPinnedPieces |= possiblePin;
                        break thisDirection;
                    }
                    if ((temp & enemyPieces) != 0) {
                        break thisDirection;
                    }
                    if ((temp & myPieces) != 0) {
                        break thisDirection;
                    }
                }
            }
            if ((temp & enemyPieces) != 0) {
                break;
            }
        }

        return cardinalPinnedPieces;
    }


}
