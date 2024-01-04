package com.chess.engine.board;

import static com.chess.engine.board.Board.*;

import com.chess.engine.piece.Pawn;
import com.chess.engine.piece.Piece;
import com.chess.engine.piece.Rook;

public abstract class Move {

    protected final Board board;
    protected final Piece movedPiece;
    protected final int destinedCoordinate;
    protected final boolean isFirstMove;

    public static final Move NULL_MOVE = new NullMove();

    private Move(final Board board, final Piece movedPiece, final int destinedCoordinate){

        this.board = board;
        this.movedPiece = movedPiece;
        this.destinedCoordinate = destinedCoordinate;
        this.isFirstMove = movedPiece.isFirstMove();
    }

    private Move(final Board board, final int destinedCoordinate){
        this.board = board;
        this.destinedCoordinate = destinedCoordinate;
        this.movedPiece = null;
        this.isFirstMove = false;
    }

    @Override
    public int hashCode(){
        final int prime = 31;
        int result = 1;
        result = prime * result + this.destinedCoordinate;
        result = prime * result + this.movedPiece.hashCode();
        result = prime * result + this.movedPiece.getPiecePosition();
        result = result + (isFirstMove ? 1 : 0);
        return result;
    }

    @Override
    public boolean equals(final Object other){
        if(this == other){
            return true;
        }
        if(!(other instanceof Move)){
            return false;
        }
        final Move otherMove = (Move) other;

        return getCurrentCoords() == otherMove.getCurrentCoords() &&
                getDestinationCoords() == otherMove.getDestinationCoords() &&
                getMovedPiece().equals(otherMove.getMovedPiece());
    }

    public Board getBoard() {
        return this.board;
    }
    public int getCurrentCoords(){
        return this.movedPiece.getPiecePosition();
    }
    public int getDestinationCoords() {
        return this.destinedCoordinate;
    }
    public Piece getMovedPiece(){
        return this.movedPiece;
    }
    public boolean isAttack(){
        return false;
    }
    public boolean isCastlingMove(){
        return false;
    }
    public Piece getAttackedPiece(){
        return null;
    }

    public Board execute() {
        final Builder builder = new Builder();

        for(final Piece piece : this.board.currentPlayer().getActivePieces()){
            if (!this.movedPiece.equals(piece)){
                builder.setPiece(piece);
            }
        }

        for (final Piece piece : this.board.currentPlayer().getOpponent().getActivePieces()){
            builder.setPiece(piece);
        }

        builder.setPiece(this.movedPiece.movePiece(this));
        builder.setMoveMaker(this.board.currentPlayer().getOpponent().getTeam());

        return builder.build();
    }

    public static class MajorMove extends Move{
        public MajorMove(final Board board, final Piece movedPiece, final int destinedCoordinate) {
            super(board, movedPiece, destinedCoordinate);
        }

        @Override
        public boolean equals(final Object other){
            return this == other || other instanceof MajorMove && super.equals(other);
        }

        @Override
        public String toString(){
            return movedPiece.getPieceType().toString() + BoardUtils.getPositionAtCoordinate(this.destinedCoordinate);
        }
    }

    public static class  MajorAttackMove extends Attack{

        public MajorAttackMove(final Board board, final Piece pieceMoved,
                               final int destinedCoordinate, final Piece pieceAttacked){
            super(board, pieceMoved, destinedCoordinate, pieceAttacked);
        }

        @Override
        public boolean equals(final Object other){
            return this == other || other instanceof MajorAttackMove && super.equals(other);
        }

        @Override
        public String toString(){
            return movedPiece.getPieceType() + BoardUtils.getPositionAtCoordinate(this.destinedCoordinate);
        }
    }

    public static class Attack extends Move{
        final Piece AttackedPiece;
        public Attack(final Board board, final Piece movedPiece, final int destinedCoordinate, final Piece AttackedPiece) {
            super(board, movedPiece, destinedCoordinate);
            this.AttackedPiece = AttackedPiece;
        }

        @Override
        public int hashCode(){
            return this.AttackedPiece.hashCode() + super.hashCode();
        }
        @Override
        public boolean equals(final Object other){

            if(this == other){
                return true;
            }
            if(!(other instanceof Attack)){
                return false;
            }

            final Attack otherAttack = (Attack) other;
            return super.equals(otherAttack) && getAttackedPiece().equals(otherAttack.getAttackedPiece());
        }

        @Override
        public Piece getAttackedPiece() {
            return this.AttackedPiece;
        }
        @Override
        public boolean isAttack() {
            return true;
        }
    }

    public static final class PawnMove extends Move{
        public PawnMove(final Board board, final Piece movedPiece, final int destinedCoordinate) {
            super(board, movedPiece, destinedCoordinate);
        }

        @Override
        public boolean equals(final Object other){
            return this == other || other instanceof PawnMove && super.equals(other);
        }

        @Override
        public String toString() {
            return BoardUtils.getPositionAtCoordinate(this.destinedCoordinate);
        }
    }

    public static class PawnAttack extends Attack{
        public PawnAttack(final Board board, final Piece movedPiece, final int destinedCoordinate,
                          final Piece AttackedPiece) {
            super(board, movedPiece, destinedCoordinate, AttackedPiece);
        }

        @Override
        public boolean equals(final Object other){
            return this == other || other instanceof PawnAttack && super.equals(other);
        }

        @Override
        public String toString(){
            return BoardUtils.getPositionAtCoordinate(this.movedPiece.getPiecePosition()).substring(0, 1) +
                    "x" +
                    BoardUtils.getPositionAtCoordinate(this.destinedCoordinate);
        }
    }

    public static final class PawnEnPassantAttack extends PawnAttack{
        public PawnEnPassantAttack(final Board board,
                                   final Piece movedPiece,
                                   final int destinedCoordinate,
                                   final Piece AttackedPiece) {
            super(board, movedPiece, destinedCoordinate, AttackedPiece);
        }

        @Override
        public boolean equals(final Object other){
            return this == other || other instanceof PawnEnPassantAttack && super.equals(other);
        }

        @Override
        public Board execute(){
            final Builder builder = new Builder();

            for (final Piece piece : this.board.currentPlayer().getActivePieces()){
                if (!this.movedPiece.equals(piece)){
                    builder.setPiece(piece);
                }
            }
            for (final Piece piece : this.board.currentPlayer().getOpponent().getActivePieces()){
                if (!piece.equals(this.getAttackedPiece())){
                    builder.setPiece(piece);
                }
            }
            builder.setPiece(this.movedPiece.movePiece(this));
            builder.setMoveMaker(this.board.currentPlayer().getOpponent().getTeam());
            return builder.build();
        }
    }

    public static class PawnPromotion extends Move{
        final Move decoratedMove;
        final Pawn promotedPawn;

        public PawnPromotion(final Move decoratedMove) {
            super(decoratedMove.getBoard(), decoratedMove.getMovedPiece(), decoratedMove.getDestinationCoords());
            this.decoratedMove = decoratedMove;
            this.promotedPawn = (Pawn) decoratedMove.getMovedPiece();
        }

        @Override
        public int hashCode(){
            return decoratedMove.hashCode() + (31 * promotedPawn.hashCode());
        }
        @Override
        public boolean equals(final Object other){
            return this == other || other instanceof PawnPromotion && (super.equals(other));
        }
        @Override
        public Board execute(){
            final Board pawnMovedBoard = this.decoratedMove.execute();
            final Builder builder = new Builder();
            for (final Piece piece : pawnMovedBoard.currentPlayer().getActivePieces()){
                if(!this.promotedPawn.equals(piece)){
                    builder.setPiece(piece);
                }
            }
            for (final Piece piece : pawnMovedBoard.currentPlayer().getOpponent().getActivePieces()){
                builder.setPiece(piece);
            }

            builder.setPiece(this.promotedPawn.getPromotionPiece().movePiece(this));
            builder.setMoveMaker(pawnMovedBoard.currentPlayer().getTeam());

            return builder.build();
        }
        @Override
        public boolean isAttack(){
            return this.decoratedMove.isAttack();
        }
        @Override
        public Piece getAttackedPiece(){
            return this.decoratedMove.getAttackedPiece();
        }
        @Override
        public String toString(){
            return "";
        }
    }

    public static final class PawnJump extends Move{
        public PawnJump(final Board board, final Piece movedPiece, final int destinedCoordinate) {
            super(board, movedPiece, destinedCoordinate);
        }

        @Override
        public boolean equals(final Object other) {
            return this == other || other instanceof PawnJump && super.equals(other);
        }

        @Override
        public Board execute(){
            final Builder builder = new Builder();
            for(final Piece piece : this.board.currentPlayer().getActivePieces()){
                if (!this.movedPiece.equals(piece)){
                    builder.setPiece(piece);
                }
            }
            for (final Piece piece : this.board.currentPlayer().getOpponent().getActivePieces()){
                builder.setPiece(piece);
            }
            final Pawn movedPawn = (Pawn) this.movedPiece.movePiece(this);
            builder.setPiece(movedPawn);
            builder.setEnPassantPawn(movedPawn);
            builder.setMoveMaker(this.board.currentPlayer().getOpponent().getTeam());
            return builder.build();
        }

        @Override
        public String toString(){
            return BoardUtils.getPositionAtCoordinate(this.destinedCoordinate);
        }
    }
    static abstract class CastleMove extends Move {
        protected final Rook castleRook;
        protected final int castleRookStart;
        protected final int castleRookDestination;

        public CastleMove(final Board board,
                          final Piece movedPiece,
                          final int destinedCoordinate,
                          final Rook castleRook,
                          final int castleRookStart,
                          final int castleRookDestination) {
            super(board, movedPiece, destinedCoordinate);

            this.castleRook = castleRook;
            this.castleRookStart = castleRookStart;
            this.castleRookDestination = castleRookDestination;
        }

        public Rook getCastleRook() {
            return this.castleRook;
        }

        @Override
        public boolean isCastlingMove(){
            return true;
        }

        @Override
        public Board execute(){
            final Builder builder = new Builder();

            for(final Piece piece : this.board.currentPlayer().getActivePieces()){
                if (!this.movedPiece.equals(piece) && !this.castleRook.equals(piece)){
                    builder.setPiece(piece);
                }
            }
            for (final Piece piece : this.board.currentPlayer().getOpponent().getActivePieces()){
                builder.setPiece(piece);
            }

            builder.setPiece(this.movedPiece.movePiece(this));
            builder.setPiece(new Rook(this.castleRook.getPieceTeam(), this.castleRookDestination));
            builder.setMoveMaker(this.board.currentPlayer().getOpponent().getTeam());
            return builder.build();
        }

        @Override
        public int hashCode(){
            final int prime = 31;
            int result = super.hashCode();
            result = prime * result + this.castleRook.hashCode();
            result = prime * result + this.castleRookDestination;
            return result;
        }

        @Override
        public boolean equals(final Object other){
            if (this == other){
                return true;
            }

            if (!(other instanceof CastleMove)){
                return false;
            }

            final CastleMove otherCastleMove = (CastleMove) other;
            return super.equals(otherCastleMove) && this.castleRook.equals(otherCastleMove.getCastleRook());
        }
    }
    public static final class KingSideCastleMove extends CastleMove{
        public KingSideCastleMove(final Board board,
                                  final Piece movedPiece,
                                  final int destinedCoordinate,
                                  final Rook castleRook,
                                  final int castleRookStart,
                                  final int castleRookDestination) {
            super(board, movedPiece, destinedCoordinate, castleRook, castleRookStart, castleRookDestination);
        }

        @Override
        public boolean equals(final Object other){
            return this == other || other instanceof KingSideCastleMove && super.equals(other);
        }

        @Override
        public String toString(){
            return "O-O";
        }
    }

    public static final class QueenSideCastleMove extends CastleMove{
        public QueenSideCastleMove(final Board board,
                                   final Piece movedPiece,
                                   final int destinedCoordinate,
                                   final Rook castleRook,
                                   final int castleRookStart,
                                   final int castleRookDestination) {
            super(board, movedPiece, destinedCoordinate, castleRook, castleRookStart, castleRookDestination);
        }

        @Override
        public boolean equals(final Object other){
            return this == other || other instanceof QueenSideCastleMove && super.equals(other);
        }

        @Override
        public String toString(){
            return "O-O-O";
        }
    }

    public static final class NullMove extends Move{
        public NullMove(){
            super(null,-1);
        }
        @Override
        public int getCurrentCoords(){
            return -1;
        }
        @Override
        public Board execute(){
            throw new RuntimeException("Cant execute null move");
        }
        @Override
        public String toString() {
            return "Null Move";
        }
    }

    public static class MoveFactory{

        private MoveFactory(){
            throw new RuntimeException("Not instantiatable");
        }

        public static Move createMove(final Board board, final int currentCoords, final int destinedCoordinate){
            for(final Move move : board.getAllLegalMoves()){

                if(move.getCurrentCoords() == currentCoords && move.getDestinationCoords() == destinedCoordinate){
                    return move;
                }
            }

            return NULL_MOVE;
        }
    }

}