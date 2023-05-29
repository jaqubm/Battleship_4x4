package com.battleship_4x4;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import static com.battleship_4x4.Setup.TILE_SIZE;
public class Tile extends Rectangle
{
    private Segment segment;

    public boolean hasSegment()
    {
        return segment != null;
    }
    public Segment getSegment()
    {
        return this.segment;
    }
    public void setSegment(Segment segment)
    {
        this.segment = segment;
    }
    public Tile(boolean light, int x, int y)
    {
        setWidth(TILE_SIZE);
        setHeight(TILE_SIZE);

        relocate(x * TILE_SIZE, y * TILE_SIZE);

        setFill(light ? Color.BLUE : Color.CYAN);
    }
}
