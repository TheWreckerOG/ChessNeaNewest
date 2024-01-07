package com.chess.engine.player;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
public class MoveTransition {
    private final Board transitionBoard;
    private final Move move;
    private final MoveStatus moveStatus;
    private final Board toBoard;
    public MoveTransition(final Board transitionBoard,  final Board toBoard, final Move move, final MoveStatus moveStatus){
        this.transitionBoard = transitionBoard;
        this.move = move;
        this.moveStatus = moveStatus;
        this.toBoard = toBoard;
    }
    public MoveStatus getMoveStatus() {
        return this.moveStatus;
    }
    public Board getTransitionBoard(){
        return this.transitionBoard;
    }
    public Board getToBoard() {
        return this.toBoard;
    }
}