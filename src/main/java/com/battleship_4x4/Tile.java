package com.battleship_4x4;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import static com.battleship_4x4.Setup.TILE_SIZE;
public class Tile extends Rectangle
{
    private Ship ship;

    public boolean hasShip()
    {
        return ship != null;
    }
    public Ship getShip()
    {
        return this.ship;
    }
    public void setShip(Ship ship)
    {
        this.ship = ship;
    }
    public Tile(boolean light, int x, int y)
    {
        setWidth(TILE_SIZE);
        setHeight(TILE_SIZE);

        relocate(x * TILE_SIZE, y * TILE_SIZE);

        setFill(light ? Color.BLUE : Color.CYAN);
    }
}
