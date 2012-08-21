// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Polygon.java

package zombie.core.collision;

import java.util.ArrayList;
import zombie.iso.Vector2;

public class Polygon
{

    public Polygon()
    {
        points = new ArrayList(4);
        edges = new ArrayList(4);
        x = 0.0F;
        y = 0.0F;
        x2 = 0.0F;
        y2 = 0.0F;
        vecs = new Vector2[4];
        eds = new Vector2[4];
    }

    public void Set(float x, float y, float x2, float y2)
    {
        this.x = x;
        this.y = y;
        this.x2 = x2;
        this.y2 = y2;
        points.clear();
        if(vecs[0] == null)
        {
            for(int n = 0; n < 4; n++)
            {
                vecs[n] = new Vector2();
                eds[n] = new Vector2();
            }

        }
        vecs[0].x = x;
        vecs[0].y = y;
        vecs[1].x = x2;
        vecs[1].y = y;
        vecs[2].x = x2;
        vecs[2].y = y2;
        vecs[3].x = x;
        vecs[3].y = y2;
        points.add(vecs[0]);
        points.add(vecs[1]);
        points.add(vecs[2]);
        points.add(vecs[3]);
        BuildEdges();
    }

    public Vector2 Center()
    {
        temp.x = x + (x2 - x) / 2.0F;
        temp.y = y + (y2 - y) / 2.0F;
        return temp;
    }

    public void BuildEdges()
    {
        edges.clear();
        for(int i = 0; i < points.size(); i++)
        {
            Vector2 p1 = (Vector2)points.get(i);
            Vector2 p2;
            if(i + 1 >= points.size())
                p2 = (Vector2)points.get(0);
            else
                p2 = (Vector2)points.get(i + 1);
            eds[i].x = p2.x - p1.x;
            eds[i].y = p2.y - p1.y;
            edges.add(eds[i]);
        }

    }

    public void Offset(float x, float y)
    {
        for(int i = 0; i < points.size(); i++)
        {
            Vector2 p = (Vector2)points.get(i);
            p.x += x;
            p.y += y;
        }

    }

    public void Offset(Vector2 v)
    {
        Offset(v.x, v.y);
    }

    public ArrayList points;
    public ArrayList edges;
    float x;
    float y;
    float x2;
    float y2;
    Vector2 vecs[];
    Vector2 eds[];
    static Vector2 temp = new Vector2();

}
