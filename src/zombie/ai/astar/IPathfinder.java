// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 13.08.2012 13:54:45
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   IPathfinder.java

package zombie.ai.astar;


// Referenced classes of package zombie.ai.astar:
//            Mover, Path

public interface IPathfinder
{

    public abstract void Failed(Mover mover);

    public abstract void Succeeded(Path path, Mover mover);

    public abstract String getName();
}