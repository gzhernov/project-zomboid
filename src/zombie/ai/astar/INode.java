// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 13.08.2012 13:54:45
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   INode.java

package zombie.ai.astar;


// Referenced classes of package zombie.ai.astar:
//            AStarPathMap, SearchData

public interface INode
{

    public abstract Integer getID();

    public abstract int compareTo(Object obj);

    public abstract int setParent(int i, int j, AStarPathMap.Node node);

    public abstract SearchData getSearchData(int i);
}