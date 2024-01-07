package com.chess.engine.piece;
import com.chess.engine.Team;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
//Import changed from List to Collection
import java.time.Period;
import java.util.Collection;
public abstract class Piece {
    protected final PieceType pieceType;
    protected final Team pieceTeam;
    protected final int piecePosition;
    private final boolean isFirstMove;
    private final int cachedHashCode;
    Piece( final PieceType pieceType, final int piecePosition, final Team pieceTeam, final boolean isFirstMove){
        this.pieceTeam = pieceTeam;
        this.piecePosition = piecePosition;
        this.pieceType= pieceType;
        this.isFirstMove = isFirstMove;
        this.cachedHashCode = computeHashCode();
    }
    //Hash code helps to identify each object by indexing them.
    private int computeHashCode() {
        int result = pieceType.hashCode();
        result = 31 * result + pieceTeam.hashCode();
        result = 31 * result + piecePosition;
        result = 31 * result + (isFirstMove ? 1 : 0);
        return result;
    }
    @Override
    public boolean equals(final Object other){
        if (this == other){
            return true;
        }
        if(!(other instanceof Piece)){
            return false;
        }
        final Piece otherPiece = (Piece) other;
        return this.piecePosition == otherPiece.piecePosition && this.pieceType == otherPiece.pieceType &&
                this.pieceTeam == otherPiece.pieceTeam && this.isFirstMove == otherPiece.isFirstMove;
    }
    @Override
    public int hashCode(){
        return this.cachedHashCode;
    }
    public int getPiecePosition(){
        return this.piecePosition;
    }
    public Team getPieceTeam(){
        return this.pieceTeam;
    }
    public boolean isFirstMove() {
        return this.isFirstMove;
    }
    public PieceType getPieceType(){
        return this.pieceType;
    }
    public int getPieceValue(){
        return this.pieceType.getPieceValue();
    }
    public abstract Collection<Move> calcLegalMoves(final Board board);
    public abstract Piece movePiece(Move move);
    public enum PieceType{
        PAWN(100, "P") {
            @Override
            public boolean isKing() {
                return false;
            }
            @Override
            public boolean isRook() {
                return false;
            }
        },
        KNIGHT(300, "N") {
            @Override
            public boolean isKing() {
                return false;
            }
            @Override
            public boolean isRook() {
                return false;
            }
        },
        BISHOP(300, "B") {
            @Override
            public boolean isKing() {
                return false;
            }
            @Override
            public boolean isRook() {
                return false;
            }
        },
        ROOK(500, "R") {
            @Override
            public boolean isKing() {
                return false;
            }
            @Override
            public boolean isRook() {
                return true;
            }
        },
        QUEEN(900, "Q") {
            @Override
            public boolean isKing() {
                return false;
            }
            @Override
            public boolean isRook() {
                return false;
            }
        },
        KING(10000, "K") {
            @Override
            public boolean isKing() {
                return true;
            }
            @Override
            public boolean isRook() {
                return false;
            }
        };
        private String pieceName;
        private int pieceValue;
        PieceType(final int pieceValue, final String pieceName){
            this.pieceName = pieceName;
            this.pieceValue = pieceValue;
        }
        @Override
        public String toString(){
            return this.pieceName;
        }
        public int getPieceValue(){
            return this.pieceValue;
        }
        public abstract boolean isKing();
        public abstract boolean isRook();
    }
}