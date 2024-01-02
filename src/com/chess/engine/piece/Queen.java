package com.chess.engine.piece;

import com.chess.engine.Team;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Move.MajorAttackMove;
import com.chess.engine.board.Tile;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Queen extends Piece{

    private final static int[] Candidate_Vector_Coordinates = {-9,-8,-7,-1,1,7,8,9};

    public Queen(final Team pieceTeam, final int piecePosition) {
        super(PieceType.QUEEN, piecePosition, pieceTeam, true);
    }

    public Queen(final Team pieceTeam, final int piecePosition, final boolean isFirstMove) {
        super(PieceType.QUEEN, piecePosition, pieceTeam, isFirstMove);
    }

    @Override
    public Collection<Move> calcLegalMoves(final Board board) {

        final List<Move> LegalMoves = new ArrayList<>();

        for(final int candidatePosition: Candidate_Vector_Coordinates){

            int candidateDestinationCoordinate = this.piecePosition;

            while(BoardUtils.isValidTile(candidateDestinationCoordinate)){

                if(isFirstColumnExclusion(candidateDestinationCoordinate, candidatePosition) ||
                        isEighthColumnExclusion(candidateDestinationCoordinate, candidatePosition)){

                    break;
                }

                candidateDestinationCoordinate += candidatePosition;

                if(BoardUtils.isValidTile(candidateDestinationCoordinate)){

                    final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);

                    if(!candidateDestinationTile.isTileOccupied()){
                        LegalMoves.add(new Move.MajorMove(board, this, candidateDestinationCoordinate));
                    }

                    else{

                        final Piece pieceAtDestination = candidateDestinationTile.getPiece();
                        final Team PieceTeam = pieceAtDestination.getPieceTeam();

                        if(this.pieceTeam != PieceTeam){
                            LegalMoves.add(new MajorAttackMove(board, this, candidateDestinationCoordinate, pieceAtDestination));
                        }

                        break;
                    }
                }
            }
        }

        return Collections.unmodifiableList(LegalMoves);
    }

    @Override
    public Queen movePiece(final Move move) {
        return new Queen(move.getMovedPiece().pieceTeam, move.getDestinationCoords());
    }

    @Override
    public String toString(){
        return PieceType.QUEEN.toString();
    }

    private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffset){

        return BoardUtils.First_Column[currentPosition] && ((candidateOffset == -9) || (candidateOffset == 7) || (candidateOffset == -1));
    }

    private static boolean isEighthColumnExclusion(final int currentPosition, final int candidateOffset){

        return BoardUtils.Eighth_Column[currentPosition] && ((candidateOffset == -7) || (candidateOffset == 9)|| (candidateOffset == 1));
    }

}
