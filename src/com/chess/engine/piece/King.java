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

public class King extends Piece{

    private final static int[] Candidate_Move_Coordinates = {-9,-8,-7,-1,1,7,8,9};

    private final boolean isCastled;
    private final boolean kingSideCastleCapable;
    private final boolean queenSideCastleCapable;

    public King(final Team pieceTeam, final int piecePosition, final boolean kingSideCastleCapable,
                final boolean queenSideCastleCapable) {
        super(PieceType.KING, piecePosition, pieceTeam, true);
        this.isCastled = false;
        this.kingSideCastleCapable = kingSideCastleCapable;
        this.queenSideCastleCapable = queenSideCastleCapable;
    }

    public King(final Team pieceTeam, final int piecePosition, final boolean isFirstMove, final boolean isCastled,
                final boolean kingSideCastleCapable,
                final boolean queenSideCastleCapable) {
        super(PieceType.KING, piecePosition, pieceTeam, isFirstMove);
        this.isCastled = isCastled;
        this.kingSideCastleCapable = kingSideCastleCapable;
        this.queenSideCastleCapable = queenSideCastleCapable;
    }

    public boolean isCastled(){
        return this.isCastled;
    }
    public boolean isKingSideCastleCapable(){
        return this.kingSideCastleCapable;
    }
    public boolean isQueenSideCastleCapable(){
        return this.queenSideCastleCapable;
    }

    @Override
    public Collection<Move> calcLegalMoves(Board board) {

        final List<Move> LegalMoves = new ArrayList<>();

        for(final int currentCandidateOffset : Candidate_Move_Coordinates){
            final int candidateDestinationCoordinate = this.piecePosition + currentCandidateOffset;

            if(FirstColumnExclusion(this.piecePosition,currentCandidateOffset) ||
                    EighthColumnExclusion(this.piecePosition, currentCandidateOffset)){
                continue;
            }

            if(BoardUtils.isValidTile(candidateDestinationCoordinate)){

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
    public King movePiece(final Move move) {
        return new King(move.getMovedPiece().pieceTeam, move.getDestinationCoords(),
                false, move.isCastlingMove(), false, false);
    }

    @Override
    public String toString(){
        return PieceType.KING.toString();
    }

    private static boolean FirstColumnExclusion(final int currentPosition, final int candidatePosition){
        return BoardUtils.First_Column[currentPosition] && ((candidatePosition == -9) || (candidatePosition == -1) || (candidatePosition == 7));
    }

    private static boolean EighthColumnExclusion(final int currentPosition, final int candidatePosition){
        return BoardUtils.Eighth_Column[currentPosition] && ((candidatePosition == -7) || (candidatePosition == 1) || (candidatePosition ==9));
    }

}