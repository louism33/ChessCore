package chessprogram.god;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Stack;

public class Chessboard {

    private ChessboardDetails details;
    private ZobristHash zobristHash;
    
    private void init(){

        
        this.details = new ChessboardDetails();
        this.zobristHash = new ZobristHash(this);
    }
    
    public Chessboard() {
        init();
    }

//    public Chessboard chessboardBasedOnFen(String fen) {
//        this = eFenParser.makeBoardBasedOnFEN(fen);
//        this.zobristHash = new ZobristHash(this);
//        return board;
//    }

    // copy constructor
    private Chessboard(Chessboard board) {
        init();
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
        MoveOrganiser.makeMoveMaster(this, move);
    }
    
    public void makeMoveAndFlipTurn(Move move){
        MoveOrganiser.makeMoveMaster(this, move);
        MoveOrganiser.flipTurn(this);
    }
    
    public void flipTurn(){
        MoveOrganiser.flipTurn(this);
    }
    
    public void unMakeMoveAndFlipTurn(){
        MoveUnmaker.unMakeMoveMaster(this);
    }
    
    public boolean inCheck(boolean white){
        return cCheckChecker.boardInCheck(this, white);
    }

    public boolean drawByRepetition (boolean white){
        return cCheckChecker.isDrawByRepetition(this, this.zobristHash);
    }

    private boolean drawByInsufficientMaterial (boolean white){
        return cCheckChecker.isDrawByInsufficientMaterial(this);
    }

    private boolean colourHasInsufficientMaterialToMate (boolean white){
        return cCheckChecker.colourHasInsufficientMaterialToMate(this, white);
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
        return isWhiteTurn() == that.isWhiteTurn() &&
                isWhiteCanCastleK() == that.isWhiteCanCastleK() &&
                isWhiteCanCastleQ() == that.isWhiteCanCastleQ() &&
                isBlackCanCastleK() == that.isBlackCanCastleK() &&
                isBlackCanCastleQ() == that.isBlackCanCastleQ() &&
                getWhitePawns() == that.getWhitePawns() &&
                getWhiteKnights() == that.getWhiteKnights() &&
                getWhiteBishops() == that.getWhiteBishops() &&
                getWhiteRooks() == that.getWhiteRooks() &&
                getWhiteQueen() == that.getWhiteQueen() &&
                getWhiteKing() == that.getWhiteKing() &&
                getBlackPawns() == that.getBlackPawns() &&
                getBlackKnights() == that.getBlackKnights() &&
                getBlackBishops() == that.getBlackBishops() &&
                getBlackRooks() == that.getBlackRooks() &&
                getBlackQueen() == that.getBlackQueen() &&
                getBlackKing() == that.getBlackKing() &&
                Objects.equals(moveStack, that.moveStack);
    }

    @Override
    public int hashCode() {
        return Objects.hash(moveStack, isWhiteTurn(), isWhiteCanCastleK(), isWhiteCanCastleQ(), isBlackCanCastleK(), isBlackCanCastleQ(), getWhitePawns(), getWhiteKnights(), getWhiteBishops(), getWhiteRooks(), getWhiteQueen(), getWhiteKing(), getBlackPawns(), getBlackKnights(), getBlackBishops(), getBlackRooks(), getBlackQueen(), getBlackKing());
    }

    @Override
    public String toString() {
        return "Chessboard{" + Art.boardArt(this) +
                "whiteTurn=" + isWhiteTurn() +
                '}';
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
}
