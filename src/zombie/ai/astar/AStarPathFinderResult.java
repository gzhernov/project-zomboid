// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 13.08.2012 13:54:45
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   AStarPathFinderResult.java

package zombie.ai.astar;


// Referenced classes of package zombie.ai.astar:
//            AStarPathFinder

public class AStarPathFinderResult
{

    public AStarPathFinderResult()
    {
        progress = AStarPathFinder.PathFindProgress.notrunning;
        maxSearchDistance = 120;
    }

    public AStarPathFinder.PathFindProgress progress;
    public int maxSearchDistance;
}