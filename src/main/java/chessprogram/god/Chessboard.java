package chessprogram.god;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static chessprogram.god.MakeMoveAndHashUpdate.*;
import static chessprogram.god.MakeMoveAndHashUpdate.UnMakeMoveAndHashUpdate;

public class Chessboard {

    private ChessboardDetails details;
    private ZobristHash zobristHash;

    void makeZobrist(){
        this.zobristHash = new ZobristHash(this);
    }

    void cloneZobristStack(ZobristHash zobristHash){
        this.zobristHash.zobristStack = (Stack<Long>) zobristHash.getZobristStack().clone();
    }
    
    
    private void init(){
        this.details = new ChessboardDetails(true);
    }
    
    Chessboard(boolean blank){
        this.details = new ChessboardDetails();
        this.zobristHash = new ZobristHash(this);
    }
    
    public Chessboard() {
        init();
        makeZobrist();
    }

    // copy constructor
    private Chessboard(Chessboard board) {
    }
    
    public String getFenRepresentation(){
        return "not yet";
    }
    
    public void setBoardToFen(String fen){
        
    }
    
    public List<Move> generateLegalMoves(){
        return MoveGeneratorMaster.generateLegalMoves(this, isWhiteTurn());
    }

    private List<Move> generateCaptureMoves(){
        return null;
    }

    private List<Move> generateQuietMoves(){
        return null;
    }
    
    
    public void makeMove(Move move){
        makeMoveAndHashUpdate(this, move, this.zobristHash);
    }
    
    public void makeMoveAndFlipTurn(Move move){
        makeMoveAndHashUpdate(this, move, this.zobristHash);
        flipTurn();
    }
    
    public void makeNullMoveAndFlipTurn(){
        makeNullMoveAndHashUpdate(this, this.zobristHash);
        flipTurn();
    }
    public void unMakeNullMoveAndFlipTurn(){
        unMakeNullMove(this, this.zobristHash);
    }

    public void flipTurn(){
        MoveOrganiser.flipTurn(this);
    }
    
    public void unMakeMoveAndFlipTurn(){
        UnMakeMoveAndHashUpdate(this, this.zobristHash);
    }
    
    public boolean inCheck(boolean white){
        return CheckHelper.boardInCheck(this, white);
    }

    public boolean drawByRepetition (boolean white){
        return CheckHelper.isDrawByRepetition(this, this.zobristHash);
    }

    private boolean drawByInsufficientMaterial (boolean white){
        return CheckHelper.isDrawByInsufficientMaterial(this);
    }

    private boolean colourHasInsufficientMaterialToMate (boolean white){
        return CheckHelper.colourHasInsufficientMaterialToMate(this, white);
    }
    
    private void pinnedPieces(boolean white){
        
    }
    
    public Stack<StackMoveData> moveStack = new Stack<>();

    public List<Move> stackMoves(Stack<StackMoveData> stack){
        List<Move> moves = new ArrayList<>();
        Stack<StackMoveData> clone = (Stack<StackMoveData>) stack.clone();
        for (int s = 0; s < stack.size(); s++){
            Move move = clone.pop().move;
            moves.add(move);
        }
        return moves;
    }

    public long whitePieces(){
        return getWhitePawns() | getWhiteKnights() | getWhiteBishops() | getWhiteRooks() | getWhiteQueen() | getWhiteKing();
    }

    public long blackPieces(){
        return getBlackPawns() | getBlackKnights() | getBlackBishops() | getBlackRooks() | getBlackQueen() | getBlackKing();
    }

    public long allPieces(){
        return whitePieces() | blackPieces();
    }

    public boolean isWhiteTurn() {
        return this.details.whiteTurn;
    }

    public void setWhiteTurn(boolean whiteTurn) {
        this.details.whiteTurn = whiteTurn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Chessboard that = (Chessboard) o;
        return Objects.equals(details, that.details)
                && Objects.equals(zobristHash, that.zobristHash)
                && Objects.equals(moveStack, that.moveStack)
                ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(details, zobristHash, moveStack);
    }

    @Override
    public String toString() {
        String turn = isWhiteTurn() ? "It is white's turn." : "It is black's turn.";
        return "\n" + Art.boardArt(this) + "\n" + turn +"\n"+zobristHash.getBoardHash() +"\n";
    }

    public boolean isWhiteCanCastleK() {
        return this.details.whiteCanCastleK;
    }

    public void setWhiteCanCastleK(boolean whiteCanCastleK) {
        this.details.whiteCanCastleK = whiteCanCastleK;
    }

    public boolean isWhiteCanCastleQ() {
        return details.whiteCanCastleQ;
    }

    public void setWhiteCanCastleQ(boolean whiteCanCastleQ) {
        this.details.whiteCanCastleQ = whiteCanCastleQ;
    }

    public boolean isBlackCanCastleK() {
        return details.blackCanCastleK;
    }

    public void setBlackCanCastleK(boolean blackCanCastleK) {
        this.details.blackCanCastleK = blackCanCastleK;
    }

    public boolean isBlackCanCastleQ() {
        return details.blackCanCastleQ;
    }

    public void setBlackCanCastleQ(boolean blackCanCastleQ) {
        this.details.blackCanCastleQ = blackCanCastleQ;
    }

    public long getWhitePawns() {
        return this.details.whitePawns;
    }

    public void setWhitePawns(long whitePawns) {
        this.details.whitePawns = whitePawns;
    }

    public long getWhiteKnights() {
        return this.details.whiteKnights;
    }

    public void setWhiteKnights(long whiteKnights) {
        this.details.whiteKnights = whiteKnights;
    }

    public long getWhiteBishops() {
        return this.details.whiteBishops;
    }

    public void setWhiteBishops(long whiteBishops) {
        this.details.whiteBishops = whiteBishops;
    }

    public long getWhiteRooks() {
        return this.details.whiteRooks;
    }

    public void setWhiteRooks(long whiteRooks) {
        this.details.whiteRooks = whiteRooks;
    }

    public long getWhiteQueen() {
        return this.details.whiteQueen;
    }

    public void setWhiteQueen(long whiteQueen) {
        this.details.whiteQueen = whiteQueen;
    }

    public long getWhiteKing() {
        return this.details.whiteKing;
    }

    public void setWhiteKing(long whiteKing) {
        this.details.whiteKing = whiteKing;
    }

    public long getBlackPawns() {
        return this.details.blackPawns;
    }

    public void setBlackPawns(long blackPawns) {
        this.details.blackPawns = blackPawns;
    }

    public long getBlackKnights() {
        return this.details.blackKnights;
    }

    public void setBlackKnights(long blackKnights) {
        this.details.blackKnights = blackKnights;
    }

    public long getBlackBishops() {
        return this.details.blackBishops;
    }

    public void setBlackBishops(long blackBishops) {
        this.details.blackBishops = blackBishops;
    }

    public long getBlackRooks() {
        return this.details.blackRooks;
    }

    public void setBlackRooks(long blackRooks) {
        this.details.blackRooks = blackRooks;
    }

    public long getBlackQueen() {
        return this.details.blackQueen;
    }

    public void setBlackQueen(long blackQueen) {
        this.details.blackQueen = blackQueen;
    }

    public long getBlackKing() {
        return this.details.blackKing;
    }

    public void setBlackKing(long blackKing) {
        this.details.blackKing = blackKing;
    }











    public Chessboard(String fen) {
        details = new ChessboardDetails();
        makeBoardBasedOnFENSpecific(fen);
        this.zobristHash = new ZobristHash(this);
    }
    

    private void makeBoardBasedOnFENSpecific(String fen){
//        System.out.println(fen);

        parseFenStringSpecific(fen);
        
        this.setWhiteTurn(isItWhitesTurnSpecific(fen));

        boolean[] castlingRights = castlingRightsSpecific(fen);
        this.setWhiteCanCastleK(castlingRights[0]);
        this.setWhiteCanCastleQ(castlingRights[1]);
        this.setBlackCanCastleK(castlingRights[2]);
        this.setBlackCanCastleQ(castlingRights[3]);

        if (isEPFlagSetSpecific(fen)){
            epFlagSpecific(fen);
        }
    }


    private static boolean totalMovesSpecific(String fen){
        //todo
        String boardPattern = " (.) (\\w+|-) (\\w+|-)";
        Pattern r = Pattern.compile(boardPattern);
        Matcher m = r.matcher(fen);

        String epFlags = "";

        if (m.find()){
            epFlags = m.group(3);
        }
        if (epFlags.length() == 0){
            throw new RuntimeException("Could not Parse board rep of fen string");
        }

        return !epFlags.equals("-");
    }


    private static boolean fiftyMovesSpecific(String fen){
        //todo
        String boardPattern = " (.) (\\w+|-) (\\w+|-)";
        Pattern r = Pattern.compile(boardPattern);
        Matcher m = r.matcher(fen);

        String epFlags = "";

        if (m.find()){
            epFlags = m.group(3);
        }
        if (epFlags.length() == 0){
            throw new RuntimeException("Could not Parse board rep of fen string");
        }

        return !epFlags.equals("-");
    }

    private void epFlagSpecific(String fen){
        String boardPattern = " (.) (\\w+|-) (\\w|-)";
        Pattern r = Pattern.compile(boardPattern);
        Matcher m = r.matcher(fen);

        String epFlags = "";

        if (m.find()){
            epFlags = m.group(3);
        }
        if (epFlags.length() == 0){
            throw new RuntimeException("Could not Parse board rep of fen string");
        }

        switch (epFlags) {
            case "a": {
                StackMoveData previousMoveForEPPurposes = new StackMoveData
                        (null, this, 50, 1, StackMoveData.SpecialMove.ENPASSANTVICTIM);
                this.moveStack.push(previousMoveForEPPurposes);
                break;
            }
            case "b": {
                StackMoveData previousMoveForEPPurposes = new StackMoveData
                        (null, this, 50, 2, StackMoveData.SpecialMove.ENPASSANTVICTIM);
                this.moveStack.push(previousMoveForEPPurposes);
                break;
            }
            case "c": {
                StackMoveData previousMoveForEPPurposes = new StackMoveData
                        (null, this, 50, 3, StackMoveData.SpecialMove.ENPASSANTVICTIM);
                this.moveStack.push(previousMoveForEPPurposes);
                break;
            }
            case "d": {
                StackMoveData previousMoveForEPPurposes = new StackMoveData
                        (null, this, 50, 4, StackMoveData.SpecialMove.ENPASSANTVICTIM);
                this.moveStack.push(previousMoveForEPPurposes);
                break;
            }
            case "e": {
                StackMoveData previousMoveForEPPurposes = new StackMoveData
                        (null, this, 50, 5, StackMoveData.SpecialMove.ENPASSANTVICTIM);
                this.moveStack.push(previousMoveForEPPurposes);
                break;
            }
            case "f": {
                StackMoveData previousMoveForEPPurposes = new StackMoveData
                        (null, this, 50, 6, StackMoveData.SpecialMove.ENPASSANTVICTIM);
                this.moveStack.push(previousMoveForEPPurposes);
                break;
            }
            case "g": {
                StackMoveData previousMoveForEPPurposes = new StackMoveData
                        (null, this, 50, 7, StackMoveData.SpecialMove.ENPASSANTVICTIM);
                this.moveStack.push(previousMoveForEPPurposes);
                break;
            }
            case "h": {
                StackMoveData previousMoveForEPPurposes = new StackMoveData
                        (null, this, 50, 8, StackMoveData.SpecialMove.ENPASSANTVICTIM);
                this.moveStack.push(previousMoveForEPPurposes);
                break;
            }
            default:
                System.out.println("problem with EP flag");
        }
    }

    private static boolean isEPFlagSetSpecific(String fen){
        String boardPattern = " (.) (\\w+|-) (\\w+|-)";
        Pattern r = Pattern.compile(boardPattern);
        Matcher m = r.matcher(fen);

        String epFlags = "";

        if (m.find()){
            epFlags = m.group(3);
        }
        if (epFlags.length() == 0){
            throw new RuntimeException("Could not Parse board rep of fen string");
        }

        return !epFlags.equals("-");
    }

    private static boolean[] castlingRightsSpecific(String fen){
        boolean[] castlingRights = {
                false, false, false, false,
        };
        String boardPattern = " (.) (\\w+|-)";
        Pattern r = Pattern.compile(boardPattern);
        Matcher m = r.matcher(fen);
        String castleString = "";
        if (m.find()){
            castleString = m.group(2);
        }
        if (castleString.length() == 0){
            throw new RuntimeException("Could not Parse board rep of fen string");
        }

        if (castleString.equals("-")){
            return castlingRights;
        }

        if (castleString.contains("K")){
            castlingRights[0] = true;
        }
        if (castleString.contains("Q")){
            castlingRights[1] = true;
        }
        if (castleString.contains("k")){
            castlingRights[2] = true;
        }
        if (castleString.contains("q")){
            castlingRights[3] = true;
        }

        return castlingRights;
    }

    private boolean isItWhitesTurnSpecific(String fen){
        String boardPattern = " (.)";
        Pattern r = Pattern.compile(boardPattern);
        Matcher m = r.matcher(fen);
        String player = "";
        if (m.find()){
            player = m.group(1);
        }
        if (player.length() == 0){
            throw new RuntimeException("Could not Parse board rep of fen string");
        }
        return player.equals("w");
    }

    private void parseFenStringSpecific (String fen){
        String boardRepresentation = boardRepSpecific(fen);
        int length = boardRepresentation.length();
        int index = -1;
        int square = 63;
        while (true){
            index++;
            if (index >= length){
                break;
            }
            if (square < 0){
                break;
            }
            String entry = Character.toString(boardRepresentation.charAt(index));
            if (entry.equals("/")){
                continue;
            }
            try {
                int i = Integer.parseInt(entry);
                square -= (i);
                continue;
            }
            catch (NumberFormatException ignored){
            }
            long pieceFromFen = BitOperations.newPieceOnSquare(square);
            square--;
            switch (entry) {
                case "P":
                    this.setWhitePawns(this.getWhitePawns() | pieceFromFen);
                    break;
                case "N":
                    this.setWhiteKnights(this.getWhiteKnights() | pieceFromFen);
                    break;
                case "B":
                    this.setWhiteBishops(this.getWhiteBishops() | pieceFromFen);
                    break;
                case "R":
                    this.setWhiteRooks(this.getWhiteRooks() | pieceFromFen);
                    break;
                case "Q":
                    this.setWhiteQueen(this.getWhiteQueen() | pieceFromFen);
                    break;
                case "K":
                    this.setWhiteKing(this.getWhiteKing() | pieceFromFen);
                    break;

                case "p":
                    this.setBlackPawns(this.getBlackPawns() | pieceFromFen);
                    break;
                case "n":
                    this.setBlackKnights(this.getBlackKnights() | pieceFromFen);
                    break;
                case "b":
                    this.setBlackBishops(this.getBlackBishops() | pieceFromFen);
                    break;
                case "r":
                    this.setBlackRooks(this.getBlackRooks() | pieceFromFen);
                    break;
                case "q":
                    this.setBlackQueen(this.getBlackQueen() | pieceFromFen);
                    break;
                case "k":
                    this.setBlackKing(this.getBlackKing() | pieceFromFen);
                    break;
                default:
                    System.out.println("I don't know this Piece");
            }
        }
    }

    private static String boardRepSpecific(String fen){
        String boardPattern = "^[\\w*/]*";
        Pattern r = Pattern.compile(boardPattern);
        Matcher m = r.matcher(fen);
        String boardRepresentation = "";
        if (m.find()){
            boardRepresentation = m.group();
        }
        if (boardRepresentation.length() == 0){
            throw new RuntimeException("Could not Parse board rep of fen string");
        }

        return boardRepresentation;
    }

    
    void makeSeriesOfANMoves(String an){
        
    }

    public ChessboardDetails getDetails() {
        return details;
    }

    public void setDetails(ChessboardDetails details) {
        this.details = details;
    }

    public ZobristHash getZobristHash() {
        return zobristHash;
    }

    public void setZobristHash(ZobristHash zobristHash) {
        this.zobristHash = zobristHash;
    }

    public Stack<StackMoveData> getMoveStack() {
        return moveStack;
    }

    public void setMoveStack(Stack<StackMoveData> moveStack) {
        this.moveStack = moveStack;
    }
}
