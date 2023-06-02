package com.battleship_4x4;

import javafx.scene.layout.AnchorPane;

public class GridBase {
    private final double planeWidth;
    private final double planeHeight;
    private final int tilesAcross;
    private final int tileAmount;
    private final int gridSize;
    private final int tilesDown;
    private final AnchorPane anchorPane;

    public GridBase(double planeWidth, double planeHeight, int gridSize, AnchorPane anchorPane) {
        this.planeWidth = planeWidth;
        this.planeHeight = planeHeight;
        this.gridSize = gridSize;
        this.anchorPane = anchorPane;

        tilesAcross = 8;
        tileAmount = 64;
        tilesDown = 8;
    }

    public double getPlaneWidth() {
        return planeWidth;
    }

    public double getPlaneHeight() {
        return planeHeight;
    }

    public int getTilesAcross() {
        return tilesAcross;
    }

    public int getTileAmount() {
        return tileAmount;
    }

    public int getGridSize() {
        return gridSize;
    }

    public int getTilesDown() {
        return tilesDown;
    }

    public AnchorPane getAnchorPane() {
        return anchorPane;
    }
}
