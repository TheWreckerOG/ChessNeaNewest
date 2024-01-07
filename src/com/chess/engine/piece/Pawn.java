package com.chess.engine.piece;
import com.chess.engine.Team;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import static com.chess.engine.board.Move.*;
public class Pawn extends Piece{
    private final static int[] Candidate_Move_Coordinates = {7,8,9,16};
    public Pawn(final Team pieceTeam, final int piecePosition) {
        super(PieceType.PAWN, piecePosition, pieceTeam, true);
    }
    public Pawn(final Team pieceTeam, final int piecePosition, final boolean isFirstMove) {
        super(PieceType.PAWN, piecePosition, pieceTeam, isFirstMove);
    }
    @Override
    public Collection<Move> calcLegalMoves(final Board board) {
        final List<Move> LegalMoves = new ArrayList<>();
        for(final int currentCandidateOffset : Candidate_Move_Coordinates) {
            final int candidateDestinationCoordinate = this.piecePosition + (this.pieceTeam.getDirection() * currentCandidateOffset);
            if (!BoardUtils.isValidTile(candidateDestinationCoordinate)) {
                continue;
            }
            if (currentCandidateOffset == 8 && !board.getTile(candidateDestinationCoordinate).isTileOccupied()) {
                if (this.pieceTeam.isPawnPromotionSquare(candidateDestinationCoordinate)) {
                    LegalMoves.add(new PawnPromotion(new PawnMove(board, this, candidateDestinationCoordinate)));
                } else {
                    LegalMoves.add(new PawnMove(board, this, candidateDestinationCoordinate));
                }
            } else if (currentCandidateOffset == 16 && this.isFirstMove() &&
                    ((BoardUtils.Seventh_Row[this.piecePosition] && this.getPieceTeam().isBlack()) ||
                            (BoardUtils.Second_Row[this.piecePosition] && this.getPieceTeam().isWhite()))) {
                final int behindCandidateCoordinate = this.piecePosition + (this.pieceTeam.getDirection() * 8);
                if (!board.getTile(behindCandidateCoordinate).isTileOccupied() && !board.getTile(candidateDestinationCoordinate).isTileOccupied()) {
                    LegalMoves.add(new PawnJump(board, this, candidateDestinationCoordinate));
                }
            } else if (currentCandidateOffset == 7 &&
                    !((BoardUtils.Eighth_Column[this.piecePosition] && this.pieceTeam.isWhite() ||
                            (BoardUtils.First_Column[this.piecePosition] && this.pieceTeam.isBlack())))) {
                if (board.getTile(candidateDestinationCoordinate).isTileOccupied()) {
                    final Piece pieceOnCandidate = board.getTile(candidateDestinationCoordinate).getPiece();
                    if (this.pieceTeam != pieceOnCandidate.getPieceTeam()) {
                        if (this.pieceTeam.isPawnPromotionSquare(candidateDestinationCoordinate)) {
                            LegalMoves.add(new PawnPromotion(new PawnAttack(board, this, candidateDestinationCoordinate, pieceOnCandidate)));
                        } else {
                            LegalMoves.add(new PawnAttack(board, this, candidateDestinationCoordinate, pieceOnCandidate));
                        }
                    }
                } else if (board.getEnPassantPawn() != null) {
                    if (board.getEnPassantPawn().getPiecePosition() == (this.piecePosition + (this.pieceTeam.getOppositeDirection()))) {
                        final Piece pieceOnCandidate = board.getEnPassantPawn();
                        if (this.pieceTeam != pieceOnCandidate.getPieceTeam()) {
                            LegalMoves.add(new PawnEnPassantAttack(board, this, candidateDestinationCoordinate, pieceOnCandidate));
                        }
                    }
                }
            }
            else if (currentCandidateOffset == 9 &&
                    !((BoardUtils.First_Column[this.piecePosition] && this.pieceTeam.isWhite() ||
                            (BoardUtils.Eighth_Column[this.piecePosition] && this.pieceTeam.isBlack())))) {
                if (board.getTile(candidateDestinationCoordinate).isTileOccupied()) {
                    final Piece pieceOnCandidate = board.getTile(candidateDestinationCoordinate).getPiece();
                    if (pieceOnCandidate != null && this.pieceTeam != pieceOnCandidate.getPieceTeam()) {
                        if (this.pieceTeam.isPawnPromotionSquare(candidateDestinationCoordinate)) {
                            LegalMoves.add(new PawnPromotion(new PawnAttack(board, this, candidateDestinationCoordinate, pieceOnCandidate)));
                        }
                        else{
                            LegalMoves.add(new PawnAttack(board, this, candidateDestinationCoordinate, pieceOnCandidate));
                        }
                    }
                }
                else if (board.getEnPassantPawn() != null) {
                    if (board.getEnPassantPawn().getPiecePosition() == (this.piecePosition - (this.pieceTeam.getOppositeDirection()))) {
                        final Piece pieceOnCandidate = board.getEnPassantPawn();
                        if (this.pieceTeam != pieceOnCandidate.getPieceTeam()) {
                            LegalMoves.add(new PawnEnPassantAttack(board, this, candidateDestinationCoordinate, pieceOnCandidate));
                        }
                    }
                }
            }
        }
        return Collections.unmodifiableList(LegalMoves);
    }
    @Override
    public String toString(){
        return PieceType.PAWN.toString();
    }
    @Override
    public Pawn movePiece(final Move move) {
        return new Pawn(move.getMovedPiece().pieceTeam, move.getDestinationCoords());
    }
    public Piece getPromotionPiece(){
        return new Queen(this.pieceTeam, this.piecePosition, false);
    }
}
