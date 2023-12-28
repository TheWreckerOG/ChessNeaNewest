package com.chess.engine.board;

import com.chess.engine.piece.Piece;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class Tile {

    protected final int TileCoordinate;
    //protected means it can only be accessed by sub classes and final means it is only set here

    private static final Map<Integer, EmptyTile> Empty_Tiles = createAllPossibleEmptyTiles();

    private static Map<Integer, EmptyTile> createAllPossibleEmptyTiles() {

        final Map<Integer, EmptyTile> emptyTileMap = new HashMap<>();

        for (int i = 0; i < BoardUtils.Num_Tiles; i++){
            emptyTileMap.put(i, new EmptyTile(i));
        }

        return Collections.unmodifiableMap(emptyTileMap);
    }

    public static Tile createTile(final int TileCoordinate, final Piece piece){
        return piece != null ? new OccupiedTile(TileCoordinate, piece): Empty_Tiles.get(TileCoordinate);
    }

    private Tile(final int TileCoordinate){
        this.TileCoordinate = TileCoordinate;
    }

    public abstract boolean isTileOccupied();
    public abstract Piece getPiece();

    public int getTileCoordinate(){
        return this.TileCoordinate;
    }

    public static final class EmptyTile extends Tile{

        private EmptyTile(final int coordinate){
            super(coordinate);
        }

        @Override
        public String toString(){
            return "-";
        }
        @Override
        public boolean isTileOccupied(){
            return false;
        }
        @Override
        public Piece getPiece(){
            return null;
        }
    }

    public static final class OccupiedTile extends Tile{

        private final Piece pieceOnTile;

        private OccupiedTile(int TileCoordinate, final Piece pieceOnTile){
            super(TileCoordinate);
            this.pieceOnTile = pieceOnTile;
        }

        @Override
        public String toString(){
            return getPiece().getPieceTeam().isBlack() ? getPiece().toString().toLowerCase(): getPiece().toString();
        }

        @Override
        public boolean isTileOccupied(){
            return true;
        }
        @Override
        public Piece getPiece(){
            return this.pieceOnTile;
        }
    }   
}

