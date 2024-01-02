package com.chess.engine.piece;

import com.chess.engine.Team;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static com.chess.engine.board.Move.*;

public class Knight extends Piece {

    private final static int[] Candidate_Move_Coordinates = {-17, -15, -10, -6, 6, 10, 15, 17};

    //Auto created constructor
    public Knight(final Team pieceTeam, final int piecePosition) {
        super(PieceType.KNIGHT, piecePosition, pieceTeam, true);
    }

    public Knight(final Team pieceTeam, final int piecePosition, final boolean isFirstMove) {
        super(PieceType.KNIGHT, piecePosition, pieceTeam, isFirstMove);
    }

    @Override
    public Collection<Move> calcLegalMoves(final Board board){

        final List<Move> LegalMoves = new ArrayList<>();

        for(final int currentCandidateOffset : Candidate_Move_Coordinates){

            final int candidateDestinationCoordinate = this.piecePosition + currentCandidateOffset;

            if(BoardUtils.isValidTile(candidateDestinationCoordinate)){

                if(FirstColumnExclusion(this.piecePosition, currentCandidateOffset)||
                        SecondColumnExclusion(this.piecePosition, currentCandidateOffset)||
                        SeventhColumnExclusion(this.piecePosition,currentCandidateOffset)||
                        EighthColumnExclusion(this.piecePosition,currentCandidateOffset)){
                    continue;
                }

                final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);

                if(!candidateDestinationTile.isTileOccupied()){
                    LegalMoves.add(new MajorMove(board, this, candidateDestinationCoordinate));
                }

                else{

                    final Piece pieceAtDestination = candidateDestinationTile.getPiece();
                    final Team PieceTeam = pieceAtDestination.getPieceTeam();

                    if(this.pieceTeam != PieceTeam){
                        LegalMoves.add(new MajorAttackMove(board, this, candidateDestinationCoordinate, pieceAtDestination));
                    }
                }
            }
        }

        return Collections.unmodifiableList(LegalMoves);
    }

    @Override
    public Knight movePiece(final Move move) {
        return new Knight(move.getMovedPiece().pieceTeam, move.getDestinationCoords());
    }

    @Override
    public String toString(){
        return PieceType.KNIGHT.toString();
    }

    private static boolean FirstColumnExclusion(final int currentPosition, final int currentCandidateOffset){
        return BoardUtils.First_Column[currentPosition] && ((currentCandidateOffset == -17) ||
                (currentCandidateOffset == -10) || (currentCandidateOffset == 6) || (currentCandidateOffset == 15));
    }

    private static boolean SecondColumnExclusion(final int currentPosition, final int currentCandidateOffset){
        return BoardUtils.Second_Column[currentPosition] && ((currentCandidateOffset == -10) || (currentCandidateOffset == 6));
    }

    private static boolean SeventhColumnExclusion(final int currentPosition, final int currentCandidateOffset){
        return BoardUtils.Seventh_Column[currentPosition] && ((currentCandidateOffset == -6) || (currentCandidateOffset == 10));
    }

    private static boolean EighthColumnExclusion(final int currentPosition, final int currentCandidateOffset){
        return BoardUtils.Eighth_Column[currentPosition] && ((currentCandidateOffset == -15) ||
                (currentCandidateOffset == -6) || (currentCandidateOffset == 10) || (currentCandidateOffset == 17));
    }
}