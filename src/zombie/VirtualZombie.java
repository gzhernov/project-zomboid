// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 13.08.2012 13:54:45
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   VirtualZombie.java

package zombie;

import java.util.Stack;
import zombie.ai.astar.IPathfinder;
import zombie.ai.astar.Mover;
import zombie.ai.astar.Path;

public class VirtualZombie
    implements IPathfinder, Mover
{

    public VirtualZombie()
    {
        followhorde = true;
        path = null;
        PathIndex = 0;
        finding = false;
    }

    public void update()
    {
        if(!followhorde)
            return;
        int lowest = 0x1869f;
        if(moveDelay == 0)
        {
            if(targx < x)
                x--;
            if(targx > x)
                x++;
            if(targy < y)
                y--;
            if(targy > y)
                y++;
            moveDelay = 48;
        }
        moveDelay--;
    }

    public void Failed(Mover mover)
    {
        path = null;
        finding = false;
    }

    public void Succeeded(Path path, Mover mover)
    {
        Path p = this.path;
        if(p != null)
        {
            for(int n = 0; n < p.getLength(); n++)
                Path.stepstore.push(p.getStep(n));

        }
        this.path = path;
        PathIndex = 0;
        finding = false;
    }

    public String getName()
    {
        return "VZombie";
    }

    public int getID()
    {
        return 0;
    }

    public int getPathFindIndex()
    {
        return 0;
    }

    public int Count;
    public int targx;
    public int targy;
    public int x;
    public int y;
    public int xoff;
    public int yoff;
    public boolean followhorde;
    public static int moveDelay = 60;
    public Path path;
    public int PathIndex;
    public boolean finding;

}