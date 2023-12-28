package com.chess.engine.board;

import java.util.*;

public class BoardUtils {

    public static final boolean[] First_Column = initColumn(0);
    public static final boolean[] Second_Column = initColumn(1);
    public static final boolean[] Seventh_Column = initColumn(6);
    public static final boolean[] Eighth_Column = initColumn(7);

    public static final boolean[] Eighth_Row = initRow(0);
    public static final boolean[] Seventh_Row = initRow(8);
    public static final boolean[] Sixth_Row = initRow(16);
    public static final boolean[] Fifth_Row = initRow(24);
    public static final boolean[] Fourth_Row = initRow(32);
    public static final boolean[] Third_Row = initRow(40);
    public static final boolean[] Second_Row = initRow(48);
    public static final boolean[] First_Row = initRow(56);

    public static final String[] ALGEBRAIC_NOTATION = initialiseAlgebraicNotation();
    public static final Map<String, Integer> POSITION_TO_COORDINATE = initialisePositionToCoordinateMap();


    public static final int Num_Tiles = 64;
    public static final int Num_Tiles_Row = 8;

    private BoardUtils(){

        throw new RuntimeException("Cant Do that");
    }

    private static String[] initialiseAlgebraicNotation(){
        return new String[]{
                "a8", "b8", "c8", "d8", "e8", "f8", "g8", "h8",
                "a7", "b7", "c7", "d7", "e7", "f7", "g7", "h7",
                "a6", "b6", "c6", "d6", "e6", "f6", "g6", "h6",
                "a5", "b5", "c5", "d5", "e5", "f5", "g5", "h5",
                "a4", "b4", "c4", "d4", "e4", "f4", "g4", "h4",
                "a3", "b3", "c3", "d3", "e3", "f3", "g3", "h3",
                "a2", "b2", "c2", "d2", "e2", "f2", "g2", "h2",
                "a1", "b1", "c1", "d1", "e1", "f1", "g1", "h1"
        };
    }

    private static Map<String, Integer> initialisePositionToCoordinateMap() {
        final Map<String, Integer> positionToCoordinate = new HashMap<>();

        for (int i = 0; i < Num_Tiles; i++) {
            positionToCoordinate.put(ALGEBRAIC_NOTATION[i], i);
        }

        return Collections.unmodifiableMap(positionToCoordinate);
    }

    private static boolean[] initColumn(int ColumnNumber) {

        final boolean[] column = new boolean[Num_Tiles];

        do{
            column[ColumnNumber] = true;
            ColumnNumber += Num_Tiles_Row;
        }

        while(ColumnNumber < Num_Tiles);
        return column;
    }



    private static boolean[] initRow(int rowNumber){

        final boolean[] row = new boolean[Num_Tiles];
        Arrays.fill(row, false);
        do{

            row[rowNumber] = true;
            rowNumber++;
        }
        while(rowNumber % Num_Tiles_Row != 0);

        return row;
    }

    public static boolean isValidTile(final int coordinate) {
        return coordinate >= 0 && coordinate < Num_Tiles;
    }

    public static int getCoordinateAtPosition(final String position){
        return POSITION_TO_COORDINATE.get(position);
    }

    public static String getPositionAtCoordinate(final int coordinate){
        return ALGEBRAIC_NOTATION[coordinate];
    }
}
