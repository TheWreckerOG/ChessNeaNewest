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

public class Rook extends Piece{

    private final static int[] Candidate_Vector_Coordinates = {-8,-1,1,8};

    public Rook(final Team pieceTeam, final int piecePosition) {
        super(PieceType.ROOK, piecePosition, pieceTeam, true);
    }

    public Rook(final Team pieceTeam, final int piecePosition, final boolean isFirstMove){
        super(PieceType.ROOK, piecePosition, pieceTeam, isFirstMove);
    }

    @Override
    public Collection<Move> calcLegalMoves(final Board board) {

        final List<Move> LegalMoves = new ArrayList<>();

        for(final int candidatePosition: Candidate_Vector_Coordinates){

            int PossibleDestination = this.piecePosition;

            while(BoardUtils.isValidTile(PossibleDestination)){

                if(isFirstColumnExclusion(PossibleDestination, candidatePosition) ||
                        isEighthColumnExclusion(PossibleDestination, candidatePosition)){

                    break;
                }

                PossibleDestination += candidatePosition;

                if(BoardUtils.isValidTile(PossibleDestination)){

                    final Tile PossibleDestinationTile = board.getTile(PossibleDestination);

                    if(!PossibleDestinationTile.isTileOccupied()){
                        LegalMoves.add(new Move.MajorMove(board, this, PossibleDestination));
                    }

                    else{

                        final Piece pieceAtDestination = PossibleDestinationTile.getPiece();
                        final Team PieceTeam = pieceAtDestination.getPieceTeam();

                        if(this.pieceTeam != PieceTeam){
                            LegalMoves.add(new MajorAttackMove(board, this, PossibleDestination, pieceAtDestination));
                        }

                        break;
                    }
                }
            }
        }

        return Collections.unmodifiableList(LegalMoves);
    }

    @Override
    public Rook movePiece(final Move move) {
        return new Rook(move.getMovedPiece().pieceTeam, move.getDestinationCoords());
    }

    @Override
    public String toString(){
        return PieceType.ROOK.toString();
    }

    private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffset){

        return BoardUtils.First_Column[currentPosition] && ((candidateOffset == -1));
    }

    private static boolean isEighthColumnExclusion(final int currentPosition, final int candidateOffset){

        return BoardUtils.Eighth_Column[currentPosition] && ((candidateOffset == 1));
    }

}
