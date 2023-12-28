package com.chess.engine.player;

import com.chess.engine.Team;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.piece.King;
import com.chess.engine.piece.Piece;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static com.chess.engine.piece.Piece.PieceType.KING;

public abstract class Player {

    protected final Board board;
    protected final King playerKing;
    protected final Collection<Move> LegalMoves;
    private final boolean isInCheck;

    Player(final Board board, final Collection<Move> playerLegals, final Collection<Move> opponentLegals){

        this.board = board;
        this.playerKing = establishKing();
        List<Move> modifiedMoves = new ArrayList<>(playerLegals);
        modifiedMoves.addAll(calculateKingCastle(playerLegals, opponentLegals));
        this.LegalMoves = Collections.unmodifiableCollection(modifiedMoves);
        this.isInCheck = !calculateAttacksOnTile(this.playerKing.getPiecePosition(), opponentLegals).isEmpty();
    }

    public King getPlayerKing(){
        return this.playerKing;
    }

    public Collection<Move> getLegalMoves(){
        return this.LegalMoves;
    }

    protected static Collection<Move> calculateAttacksOnTile(int piecePosition, Collection<Move> moves) {
        final List<Move> attackMove = new ArrayList<>();

        for(final  Move move: moves){
            if(piecePosition == move.getDestinationCoords()){
                attackMove.add(move);
            }
        }
        return Collections.unmodifiableList(attackMove);
    }

    private King establishKing() {
        for (final Piece piece : getActivePieces()) {
            if (piece.getPieceType() == Piece.PieceType.KING) {
                return (King) piece;
            }
        }
        throw new RuntimeException("No king found among active pieces");
    }

    public boolean isMoveLegal(final Move move){
        return this.LegalMoves.contains(move);

    }

    public boolean isInCheck(){
        return this.isInCheck;
    }

    public boolean isInCheckMate(){
        return this.isInCheck && !escapeMoves();
    }

    protected boolean escapeMoves() {

        for(final Move move : this.LegalMoves){
            final MoveTransition transition = makeMove(move);
            if (transition.getMoveStatus().isDone()){
                return true;
            }
        }
        return false;
    }

    public boolean isInStaleMate(){
        return !this.isInCheck && !escapeMoves();
    }

    public boolean isCastled(){
        return false;
    }

    public MoveTransition makeMove(final Move move){
        if(!this.LegalMoves.contains(move)){
            return new MoveTransition(this.board, this.board, move, MoveStatus.ILLEGAL_MOVE);
        }

        final Board transitionBoard = move.execute();

        final Collection<Move> kingAttacks = Player.calculateAttacksOnTile(
                transitionBoard.currentPlayer().getOpponent().getPlayerKing().getPiecePosition(),
                transitionBoard.currentPlayer().getLegalMoves());

        if (kingAttacks.isEmpty()){
            return new MoveTransition(this.board,this.board, move, MoveStatus.LEAVES_PLAYER_IN_CHECK);
        }

        return new MoveTransition(transitionBoard,transitionBoard, move, MoveStatus.DONE);
    }

    public  abstract Collection<Piece> getActivePieces();
    public abstract Team getTeam();
    public abstract Player getOpponent();
    protected abstract Collection<Move> calculateKingCastle(Collection<Move> playerLegals, Collection<Move> opponentLegals);

}
