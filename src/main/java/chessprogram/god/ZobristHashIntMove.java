package chessprogram.god;

import org.junit.Assert;

import java.util.Objects;
import java.util.Random;
import java.util.Stack;

import static chessprogram.god.BitOperations.newPieceOnSquare;
import static chessprogram.god.BitboardResources.INITIAL_BLACK_KING;
import static chessprogram.god.BitboardResources.INITIAL_WHITE_KING;
import static chessprogram.god.MoveMakerIntMove.whichPieceOnSquare;
import static chessprogram.god.StackMoveDataIntMove.SpecialMove.ENPASSANTVICTIM;
import static chessprogram.god.StackMoveDataIntMove.SpecialMove.NULL_MOVE;

class ZobristHashIntMove {
    private static final long initHashSeed = 100;
    Stack<Long> zobristStack = new Stack<>();
    private static final long[][] zobristHashPieces = initPieceHash();
    private static final long[] zobristHashCastlingRights = initCastlingHash();
    private static final long[] zobristHashEPFiles = initEPHash();
    static final long zobristHashColourBlack = initColourHash();
    private long boardHash;

    ZobristHashIntMove(ChessboardIntMove board) {
        this.boardHash = boardToHash(board);
    }

    long updateWithEPFlags(ChessboardIntMove board){
        Assert.assertTrue(board.moveStack.size() > 0);
        long hash = 0;
        StackMoveDataIntMove peek = board.moveStack.peek();
        if (peek.typeOfSpecialMove == ENPASSANTVICTIM) {
            hash = hashEP(hash, peek);
        }
        
        if (peek.typeOfSpecialMove == NULL_MOVE && board.moveStack.size() > 1){
            hash = nullMoveEP(board, hash);
        }
        
        return hash;
    }

    private long nullMoveEP(ChessboardIntMove board, long hash) {
        StackMoveDataIntMove pop = board.moveStack.pop();
        StackMoveDataIntMove peekSecondElement = board.moveStack.peek();
        if (peekSecondElement.typeOfSpecialMove == ENPASSANTVICTIM) {
            // file one = FILE_A
            hash = hashEP(hash, peekSecondElement);
        }
        board.moveStack.add(pop);
        return hash;
    }

    private long hashEP(long hash, StackMoveDataIntMove peek) {
        hash ^= zobristHashEPFiles[peek.enPassantFile - 1];
        return hash;
    }

    void updateHashPostMove(ChessboardIntMove board, int move){
        /*
        invert colour
        */
        boardHash ^= zobristHashColourBlack;

        Assert.assertTrue(board.moveStack.size() > 0);
        
        /*
        if move we just made raised EP flag, update hash
        */
        boardHash ^= updateWithEPFlags(board);

        /*
        if castling rights changed, update hash
        */
        boardHash ^= postMoveCastlingRights(board);

    }

    private long postMoveCastlingRights(ChessboardIntMove board){
        long updatedHashValue = 0;
        StackMoveDataIntMove peek = board.moveStack.peek();
        /*
        undo previous castling rights
        */
        int numTo15Undo = 0;
        if (peek.whiteCanCastleK){
            numTo15Undo += 1;
        }
        if (peek.whiteCanCastleQ){
            numTo15Undo += 2;
        }
        if (peek.blackCanCastleK){
            numTo15Undo += 4;
        }
        if (peek.blackCanCastleQ){
            numTo15Undo += 8;
        }
        Assert.assertTrue(numTo15Undo >= 0 && numTo15Undo <= 15);
        updatedHashValue ^= zobristHashCastlingRights[numTo15Undo];
 
        /*
        update with new castling rights
        */
        int numTo15Do = 0;
        if (board.isWhiteCanCastleK()){
            numTo15Do  += 1;
        }

        if (board.isWhiteCanCastleQ()){
            numTo15Do  += 2;
        }

        if (board.isBlackCanCastleK()){
            numTo15Do  += 4;
        }

        if (board.isBlackCanCastleQ()){
            numTo15Do  += 8;
        }

        updatedHashValue ^= zobristHashCastlingRights[numTo15Do];
        
        return updatedHashValue;
    }


    void updateHashPreMove(ChessboardIntMove board, int move){
        int sourceSquare = MoveParserIntMove.getSourceIndex(move);
        int destinationSquareIndex = MoveParserIntMove.getDestinationIndex(move);

        long sourcePiece = newPieceOnSquare(sourceSquare);
        int sourcePieceIdentifier = whichPieceOnSquare(board, sourcePiece) - 1;
        long sourceZH = zobristHashPieces[sourceSquare][sourcePieceIdentifier];

        long destinationSquare = newPieceOnSquare(destinationSquareIndex);
        long destinationZH = zobristHashPieces[destinationSquareIndex][sourcePieceIdentifier];

        boardHash ^= sourceZH;
        boardHash ^= destinationZH;
        
        /*
        captures
         */
        if ((destinationSquare & board.allPieces()) != 0){
            int destinationPieceIdentifier = whichPieceOnSquare(board, destinationSquare) - 1;
            /*
            remove taken piece from hash
            */
            long victimZH = zobristHashPieces[destinationSquareIndex][destinationPieceIdentifier];
            boardHash ^= victimZH;
        }

        /* 
        "positive" EP flag is set in updateHashPostMove, in updateHashPreMove we cancel a previous EP flag
        */
        Stack<StackMoveDataIntMove> moveStack = board.moveStack;
        if (moveStack.size() > 0){
            boardHash ^= updateWithEPFlags(board);
        }

        long destinationPiece = newPieceOnSquare(MoveParserIntMove.getDestinationIndex(move));

        if (MoveParserIntMove.isSpecialMove(move)){
            if (MoveParserIntMove.isCastlingMove(move)) {
                int originalRookIndex = 0;
                int newRookIndex = 0;
                if ((sourcePiece & INITIAL_WHITE_KING) != 0){
                    if (MoveParserIntMove.getDestinationIndex(move) == 1){
                        originalRookIndex = 0;
                        newRookIndex = MoveParserIntMove.getDestinationIndex(move) + 1;
                    }
                    else if (MoveParserIntMove.getDestinationIndex(move) == 5){
                        originalRookIndex = 7;
                        newRookIndex = MoveParserIntMove.getDestinationIndex(move) - 1;
                    }
                }

                else if ((sourcePiece & INITIAL_BLACK_KING) != 0){
                    if (MoveParserIntMove.getDestinationIndex(move) == 57){
                        originalRookIndex = 56;
                        newRookIndex = MoveParserIntMove.getDestinationIndex(move) + 1;
                    }
                    else if (MoveParserIntMove.getDestinationIndex(move) == 61){
                        originalRookIndex = 63;
                        newRookIndex = MoveParserIntMove.getDestinationIndex(move) - 1;
                    }
                }
                else {
                    throw new RuntimeException("Mistake in Zobrist of castling");
                }

                int myRook = whichPieceOnSquare(board, newPieceOnSquare(originalRookIndex)) - 1;
                long originalRookZH = zobristHashPieces[originalRookIndex][myRook];
                long newRookZH = zobristHashPieces[newRookIndex][myRook];
                boardHash ^= originalRookZH;
                boardHash ^= newRookZH;
            }

            else if (MoveParserIntMove.isEnPassantMove(move)){
                if ((sourcePiece & board.getWhitePawns()) != 0){
                    long victimPawn = destinationPiece >>> 8;
                    int indexOfVictimPawn = BitOperations.getIndexOfFirstPiece(victimPawn);
                    int pieceToKill = whichPieceOnSquare(board, victimPawn) - 1;
                    long victimPawnZH = zobristHashPieces[indexOfVictimPawn][pieceToKill];
                    boardHash ^= victimPawnZH;
                }

                else if  ((sourcePiece & board.getBlackPawns()) != 0){
                    long victimPawn = destinationPiece << 8;
                    int indexOfVictimPawn = BitOperations.getIndexOfFirstPiece(victimPawn);
                    int pieceToKill = whichPieceOnSquare(board, victimPawn) - 1;
                    long victimPawnZH = zobristHashPieces[indexOfVictimPawn][pieceToKill];
                    boardHash ^= victimPawnZH;
                }
                else {
                    throw new RuntimeException("false EP move");
                }

            }

            else if (MoveParserIntMove.isPromotionMove(move)){
                int whichPromotingPiece = 0;
                if ((sourcePiece & board.getWhitePawns()) != 0){
                    if (MoveParserIntMove.isPromotionToKnight(move)){
                        whichPromotingPiece = 2;
                    }
                    else if (MoveParserIntMove.isPromotionToBishop(move)){
                        whichPromotingPiece = 3;
                    }
                    else if (MoveParserIntMove.isPromotionToRook(move)){
                        whichPromotingPiece = 4;
                    }
                    else if (MoveParserIntMove.isPromotionToQueen(move)){
                        whichPromotingPiece = 5;
                    }
                }

                else if ((sourcePiece & board.getBlackPawns()) != 0){
                    if (MoveParserIntMove.isPromotionToKnight(move)){
                        whichPromotingPiece = 8;
                    }
                    else if (MoveParserIntMove.isPromotionToBishop(move)){
                        whichPromotingPiece = 9;
                    }
                    else if (MoveParserIntMove.isPromotionToRook(move)){
                        whichPromotingPiece = 10;
                    }
                    
                    else if (MoveParserIntMove.isPromotionToQueen(move)){
                        whichPromotingPiece = 11;
                    }
                }

                /*
                remove my pawn from zh
                 */
                boardHash ^= destinationZH;
                
                if (whichPromotingPiece == 0){
                    System.out.println(board);
                    System.out.println();
                }
                long promotionZH = zobristHashPieces[destinationSquareIndex][whichPromotingPiece - 1];
                boardHash ^= promotionZH;
            }
        }
    }

    /*
    create almost unique long to identify current board
     */
    private long boardToHash(ChessboardIntMove board){
        long hash = 0;
        for (int sq = 0; sq < 64; sq++) {
            long pieceOnSquare = newPieceOnSquare(sq);
            int pieceIndex = whichPieceOnSquare(board, pieceOnSquare) - 1;
            if (pieceIndex != -1) {
                hash ^= zobristHashPieces[sq][pieceIndex];
            }
        }

        hash ^= castlingRightsToHash(board);

        if (!board.isWhiteTurn()){
            hash ^= zobristHashColourBlack;
        }
        
        if (board.moveStack.size() > 0){
            hash ^= updateWithEPFlags(board);
        }

        return hash;
    }

    private long castlingRightsToHash(ChessboardIntMove board){
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
        Assert.assertTrue(numTo15 >= 0 && numTo15 <= 15);

        return zobristHashCastlingRights[numTo15];
    }

    /*
    create values for every possible piece on every possible square
     */
    private static long[][] initPieceHash(){
        Random r = new Random(initHashSeed);
        long[][] zobristHash = new long[64][12];
        for (int outer = 0; outer < 64; outer++){
            for (int inner = 0; inner < 12; inner++){
                zobristHash[outer][inner] = r.nextLong();
            }
        }
        return zobristHash;
    }

    /*
    create values for every possible combination of castling right
    */
    private static long[] initCastlingHash(){
        Random r = new Random(initHashSeed + 1);
        long[] zobristHash = new long[16];
        zobristHash[0] = 0;
        for (int cr = 1; cr < zobristHash.length; cr++){
            zobristHash[cr] = r.nextLong();
        }
        return zobristHash;
    }

    /*
    create values for every possible EP file
    */
    private static long[] initEPHash(){
        Random r = new Random(initHashSeed + 2);
        long[] zobristHash = new long[8];
        for (int cr = 0; cr < zobristHash.length; cr++){
            zobristHash[cr] = r.nextLong();
        }
        return zobristHash;
    }
    
    /*
    create value for the player being black
    */
    private static long initColourHash(){
        Random r = new Random(initHashSeed + 3);
        return r.nextLong();
    }

    public long getBoardHash() {
        return boardHash;
    }

    void setBoardHash(long boardHash) {
        this.boardHash = boardHash;
    }

    public Stack<Long> getZobristStack() {
        return zobristStack;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ZobristHashIntMove that = (ZobristHashIntMove) o;
        return boardHash == that.boardHash
                && Objects.equals(zobristStack, that.zobristStack)
        ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(zobristStack, boardHash);
    }

    @Override
    public String toString() {
        return "ZobristHash{" +
                "zobristStack=" + zobristStack +
                ", boardHash=" + boardHash +
                '}';
    }


    
}
