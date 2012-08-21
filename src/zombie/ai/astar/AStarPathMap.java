// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 13.08.2012 13:54:45
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   AStarPathMap.java

package zombie.ai.astar;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import zombie.PathfindManager;
import zombie.characters.IsoGameCharacter;

// Referenced classes of package zombie.ai.astar:
//            TileBasedMap, AStarPathFinder, Mover, SearchData, 
//            INode

public class AStarPathMap
{
    public static class Node
        implements Comparable, INode
    {

        public Integer getID()
        {
            return ID;
        }

        public int compareTo(Object other)
        {
            Thread thread = Thread.currentThread();
            Node o = (Node)other;
            int id = 0;
            if(thread instanceof zombie.PathfindManager.PathfindThread)
                id = ((zombie.PathfindManager.PathfindThread)thread).ID;
            float f = searchData[id].heuristic + searchData[id].cost;
            float of = o.searchData[id].heuristic + o.searchData[id].cost;
            if(f < of)
                return -1;
            return f <= of ? 0 : 1;
        }

        public int setParent(int thread, int id, Node parent)
        {
            getSearchData(thread).parent = parent;
            int depth = searchData[thread].depth = (short)(parent.getSearchData(thread).depth + 1);
            return depth;
        }

        public SearchData getSearchData(int thread)
        {
            return searchData[thread];
        }

        public short x;
        public short y;
        public short z;
        public SearchData searchData[];
        static int IDMax = 0;
        public Integer ID;


        public Node()
        {
            ID = new Integer(0);
            ID = Integer.valueOf(IDMax++);
        }

        public Node(short x, short y, short z)
        {
            ID = new Integer(0);
            for(int n = 0; n < 8; n++);
            this.x = x;
            this.y = y;
            this.z = z;
            ID = Integer.valueOf(IDMax++);
            if(searchData == null)
                searchData = new SearchData[PathfindManager.instance.MaxThreads];
        }
    }


    public AStarPathMap(TileBasedMap map)
    {
        NodeMap = new ArrayList();
        this.map = map;
        float total = map.getWidthInTiles();
        float per = 20F / total;
        float percent = 60F;
        nodes = new Node[map.getWidthInTiles()][map.getHeightInTiles()][map.getElevInTiles()];
        for(int x = 0; x < map.getWidthInTiles(); x++)
        {
            for(int y = 0; y < map.getHeightInTiles(); y++)
            {
                for(int z = 0; z < map.getElevInTiles(); z++)
                {
                    nodes[x][y][z] = new Node((short)x, (short)y, (short)z);
                    NodeMap.add(nodes[x][y][z]);
                }

            }

        }

    }

    public void LoadPrecalcFlle()
        throws FileNotFoundException, IOException
    {
    }

    public void DoPrecalcFile(AStarPathFinder astarpathfinder)
    {
    }

    public int getFreeIndex(IsoGameCharacter character)
    {
        return 0;
    }

    public float getMovementCost(Mover mover, int sx, int sy, int sz, int tx, int ty, int tz)
    {
        return map.getCost(mover, sx, sy, sz, tx, ty, tz);
    }

    protected boolean isValidLocation(Mover mover, int sx, int sy, int sz, int x, int y, int z, 
            int lx, int ly, int lz)
    {
        boolean invalid = x < 0 || y < 0 || z < 0 || x >= map.getWidthInTiles() || y >= map.getHeightInTiles() || z >= map.getElevInTiles();
        if(invalid)
            return !invalid;
        if(nodes[x][y][z] == null)
            return false;
        if(sx != x || sy != y || sz != z)
            invalid = map.blocked(mover, x, y, z, lx, ly, lz);
        return !invalid;
    }

    public TileBasedMap map;
    public Node nodes[][][];
    public ArrayList NodeMap;
}