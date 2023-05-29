package com.battleship_4x4;

import java.util.ArrayList;
import java.util.List;

public class Ship
{
    private int size;
    private int x;
    private int y;
    public List<Segment> segmentList=new ArrayList<Segment>();
    public Ship (int size, int x, int y)
    {
        this.size=size;
        this.x=x;
        this.y=y;
        for(int i=0;i<size;i++,y++)
        {
            segmentList.add(new Segment(x,y));
        }
    }
}
