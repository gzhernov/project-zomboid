// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 13.08.2012 13:54:45
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   AStarPathFinder.java

package zombie.ai.astar;

import gnu.trove.set.hash.THashSet;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.opengl.Display;
import zombie.GameWindow;
import zombie.PathfindManager;
import zombie.characters.IsoGameCharacter;
import zombie.characters.IsoZombie;
import zombie.core.Core;
import zombie.core.properties.PropertyContainer;
import zombie.core.textures.Texture;
import zombie.core.textures.TextureID;
import zombie.gameStates.IngameState;
import zombie.iso.*;
import zombie.iso.SpriteDetails.IsoFlagType;
import zombie.iso.Vector2;

// Referenced classes of package zombie.ai.astar:
//            SearchData, AStarPathMap, Path, Mover, 
//            TileBasedMap, IPathfinder, AStarHeuristic

public class AStarPathFinder
{
    private class SortedList
    {

        public void add(int threadID, int id, AStarPathMap.Node o)
        {
            list.add(o);
            set.add(o.ID);
            AStarPathFinder.IDToUseInSort = id;
        }

        public void clear()
        {
            list.clear();
            set.clear();
        }

        public boolean contains(AStarPathMap.Node o)
        {
            return set.contains(o.ID);
        }

        public AStarPathMap.Node first()
        {
            return (AStarPathMap.Node)list.peek();
        }

        public void remove(int threadID, AStarPathMap.Node o)
        {
            list.remove(o);
            set.remove(o.ID);
        }

        public int size()
        {
            return list.size();
        }

        private PriorityQueue list;
        THashSet set;
       

        private SortedList()
        {
            list = new PriorityQueue(10000);
            set = new THashSet(10000);
        }

    }

    public static enum PathFindProgress
    /*      */   {
    /*  528 */     notrunning, 
    /*  529 */     failed, 
    /*  530 */     found, 
    /*  531 */     notyetfound;
    /*      */   }
    

    private IsoDirections DirectionWeFrom(AStarPathMap.Node current, AStarPathMap.Node parent)
    {
        if(parent == null)
            return IsoDirections.Max;
        if(parent.x > current.x)
        {
            if(parent.y < current.y)
                return IsoDirections.SW;
            if(parent.y > current.y)
                return IsoDirections.NW;
            else
                return IsoDirections.W;
        }
        if(parent.x < current.x)
        {
            if(parent.y < current.y)
                return IsoDirections.SE;
            if(parent.y > current.y)
                return IsoDirections.NE;
            else
                return IsoDirections.E;
        }
        if(parent.y < current.y)
            return IsoDirections.S;
        if(parent.y > current.y)
            return IsoDirections.N;
        else
            return IsoDirections.Max;
    }

    static boolean implies(int a, int b)
    {
        return a == 0 ? true : b != 0;
    }

    public static AStarPathMap.Node getNodeFrom(AStarPathMap.Node current, IsoDirections dir)
    {
        if(dir == IsoDirections.Max)
            return null;
        int sx = current.x;
        int sy = current.y;
        int sz = current.z;
        
       
        
        
        
        

        switch(dir.ordinal())
        {
        case 1: // '\001'
            sy--;
            break;

        case 2: // '\002'
            sy++;
            break;

        case 3: // '\003'
            sx++;
            break;

        case 4: // '\004'
            sx--;
            break;

        case 5: // '\005'
            sx++;
            sy--;
            break;

        case 6: // '\006'
            sx--;
            sy--;
            break;

        case 7: // '\007'
            sx++;
            sy++;
            break;

        case 8: // '\b'
            sx--;
            sy++;
            break;
        }
        if(sx < 0 || sy < 0 || sx >= IsoWorld.instance.CurrentCell.getWidthInTiles() || sy >= IsoWorld.instance.CurrentCell.getHeightInTiles())
            return null;
        else
            return astarmap.nodes[sx][sy][sz];
    }

    public static AStarPathMap.Node getNodeFrom2(AStarPathMap.Node current, IsoDirections dir)
    {
        if(dir == IsoDirections.Max)
            return null;
        int sx = current.x;
        int sy = current.y;
        int sz = current.z;
        switch(dir.ordinal())
        {
        case 1: // '\001'
            sy--;
            break;

        case 2: // '\002'
            sy++;
            break;

        case 3: // '\003'
            sx++;
            break;

        case 4: // '\004'
            sx--;
            break;

        case 5: // '\005'
            sx++;
            sy--;
            break;

        case 6: // '\006'
            sx--;
            sy--;
            break;

        case 7: // '\007'
            sx++;
            sy++;
            break;

        case 8: // '\b'
            sx--;
            sy++;
            break;
        }
        if(sx < 0 || sy < 0 || sx >= IsoWorld.instance.CurrentCell.getWidthInTiles() || sy >= IsoWorld.instance.CurrentCell.getHeightInTiles())
            return null;
        else
            return astarmap.nodes[sx][sy][sz];
    }

    public int enterable(int d, AStarPathMap.Node current, int d2)
    {
        AStarPathMap.Node n = getNodeFrom(current, IsoDirections.fromIndex(d));
        if(n == null)
            return 0;
        AStarPathMap.Node from = getNodeFrom(current, IsoDirections.fromIndex(d2));
        if(from == null)
            return 0;
        return !astarmap.map.blocked(mover, n.x, n.y, n.z, from.x, from.y, from.z) ? 1 : 0;
    }

    public int enterable(int d, AStarPathMap.Node current, AStarPathMap.Node from)
    {
        AStarPathMap.Node n = getNodeFrom(current, IsoDirections.fromIndex(d));
        if(n == null)
            return 0;
        return !astarmap.map.blocked(mover, n.x, n.y, n.z, from.x, from.y, from.z) ? 1 : 0;
    }

    private int NaturalNeighbours(AStarPathMap.Node from, IsoDirections dir)
    {
        if(dir == IsoDirections.Max)
            return 255;
        if(from == null)
            return 255;
        calcSet.set = 0;
        set.AddDirection(dir);
        if(dir == IsoDirections.NE || dir == IsoDirections.NW || dir == IsoDirections.SE || dir == IsoDirections.SW)
        {
            set.AddDirection(IsoDirections.RotLeft(dir));
            set.AddDirection(IsoDirections.RotRight(dir));
        }
        return set.set;
    }

    private int ForcedNeighbours(AStarPathMap.Node current, AStarPathMap.Node from, IsoDirections dir)
    {
        calcSet.set = 0;
        if(dir == IsoDirections.Max)
            return 0;
        if(from == null)
            return 0;
        int d;
        if(dir == IsoDirections.NE || dir == IsoDirections.NW || dir == IsoDirections.SE || dir == IsoDirections.SW)
        {
            d = dir.index();
            if(!implies(enterable(d + 6, current, current), enterable(d + 5, current, from)))
                calcSet.AddDirection(d + 5);
            if(!implies(enterable(d + 2, current, current), enterable(d + 3, current, from)))
                calcSet.AddDirection(d + 3);
        } else
        {
            d = dir.index();
        }
        return calcSet.set;
    }

    public AStarPathMap.Node Jump(IsoDirections dir, AStarPathMap.Node start)
    {
        int index = dir.index();
        AStarPathMap.Node node = getNodeFrom(start, dir);
        if(node == null)
            return null;
        if(astarmap.map.blocked(mover, node.x, node.y, node.z, start.x, start.y, start.z))
            return start;
        if(astarmap.map.IsStairsNode(node, start, dir))
            return node;
        if(node.x == targetX && node.y == targetY && node.z == targetZ || ForcedNeighbours(node, start, dir) != 0)
            return node;
        if(dir == IsoDirections.NE || dir == IsoDirections.NW || dir == IsoDirections.SE || dir == IsoDirections.SW)
        {
            AStarPathMap.Node next = Jump(IsoDirectionSet.rotate(dir, 7), node);
            if(next != null)
                return node;
            next = Jump(IsoDirectionSet.rotate(dir, 1), node);
            if(next != null)
                return node;
        }
        AStarPathMap.Node jnode = Jump(dir, node);
        return jnode;
    }

    private void PathfindNode(int threadID, AStarPathMap.Node current, Mover mover, AStarPathMap.Node newNode, int tx, int ty, int tz, 
            int nodesTried)
    {
        if(newNode.searchData[threadID] == null)
            newNode.searchData[threadID] = new SearchData();
        float nextStepCost = current.searchData[threadID].cost + astarmap.getMovementCost(mover, current.x, current.y, current.z, newNode.x, newNode.y, newNode.z);
        if(nextStepCost < newNode.searchData[threadID].cost)
        {
            if(inOpenList(newNode))
                removeFromOpen(threadID, newNode);
            if(inClosedList(newNode))
                removeFromClosed(newNode);
        }
        if(!inOpenList(newNode) && !inClosedList(newNode))
        {
            newNode.searchData[threadID].cost = nextStepCost;
            newNode.searchData[threadID].heuristic = getHeuristicCost(mover, newNode.x, newNode.y, newNode.z, tx, ty, tz);
            maxDepth = Math.max(maxDepth, newNode.setParent(threadID, 0, current));
            addToOpen(threadID, 0, newNode);
            nodesTried++;
        }
    }

    public void findPathActualZombie(Mover mover, int sx, int sy, int sz, int tx, int ty, int tz)
    {
        zfindangle.x = (float)tx + 0.5F;
        zfindangle.y = (float)ty + 0.5F;
        zfindangle.x -= ((IsoZombie)mover).getX();
        zfindangle.y -= ((IsoZombie)mover).getY();
        zfindangle.normalize();
        IsoDirections dir = IsoDirections.fromAngle(zfindangle);
        IsoGridSquare current = IsoWorld.instance.CurrentCell.getGridSquare(sx, sy, sz);
        IsoGridSquare target = IsoWorld.instance.CurrentCell.getGridSquare(tx, ty, tz);
        int timeout = 100;
        while(current != target) ;
    }

    private boolean IsInDirection(AStarPathMap.Node start, IsoDirections isoDirections)
    {
        if(targetX == -1)
            return false;
        if(start.z != targetZ)
            return false;
        switch(isoDirections.ordinal())
        {
        default:
            break;

        case 1: // '\001'
            if(start.x == targetX && start.y > targetY)
                return true;
            break;

        case 2: // '\002'
            if(start.x == targetX && start.y < targetY)
                return true;
            break;

        case 6: // '\006'
            if(targetX < start.x && targetY < start.y && targetX - start.x == targetY - start.y)
                return true;
            break;

        case 5: // '\005'
            if(targetX > start.x && targetY < start.y && -(targetX - start.x) == targetY - start.y)
                return true;
            break;

        case 8: // '\b'
            if(targetX < start.x && targetY > start.y && targetX - start.x == -(targetY - start.y))
                return true;
            break;

        case 7: // '\007'
            if(targetX > start.x && targetY > start.y && -(targetX - start.x) == -(targetY - start.y))
                return true;
            break;

        case 3: // '\003'
            if(targetX > start.x && startY == targetY)
                return true;
            break;

        case 4: // '\004'
            if(targetX < start.x && startY == targetY)
                return true;
            break;
        }
        return false;
    }

    public AStarPathFinder(IsoGameCharacter character, AStarPathMap finder, int maxSearchDistance, boolean allowDiagMovement, AStarHeuristic heuristic)
    {
        calcSet = new IsoDirectionSet();
        cyclesPerSlice = 3;
        delay = 0;
        progress = PathFindProgress.notrunning;
        startX = 0;
        startY = 0;
        startZ = 0;
        targetX = 0;
        targetY = 0;
        targetZ = 0;
        maxDepth = 0;
        this.character = null;
        closed = new ArrayList(10000);
        open = new SortedList();
        closedSet = new THashSet(10000);
        NodesUnfurled = new ArrayList(0);
        set = new IsoDirectionSet();
        this.character = character;
        AStarPathFinder _tmp = this;
        astarmap = finder;
        this.heuristic = heuristic;
        this.maxSearchDistance = maxSearchDistance;
        this.allowDiagMovement = allowDiagMovement;
    }

    public PathFindProgress Cycle(int threadID)
    {
        if(astarmap == null)
        {
            astarmap = new AStarPathMap(IsoWorld.instance.CurrentCell);
            IsoWorld.instance.CurrentCell.setPathMap(astarmap);
        }
        foundPath = findPath(threadID, mover, startX, startY, startZ, targetX, targetY, targetZ);
        if(foundPath != null && foundPath.getLength() > 0)
            progress = PathFindProgress.found;
        else
            progress = PathFindProgress.failed;
        return progress;
    }

    public Path findPath(int threadID, Mover mover, int sx, int sy, int sz, int tx, int ty, 
            int tz)
    {
        int nodesTried;
        maxSearchDistance = 600;
        NodesUnfurled.clear();
        targetX = tx;
        targetY = ty;
        targetZ = tz;
        nodesTried = 0;
        if(astarmap == null)
        {
            astarmap = new AStarPathMap(IsoWorld.instance.CurrentCell);
            IsoWorld.instance.CurrentCell.setPathMap(astarmap);
        }
        if(tx < 0 || ty < 0 || tx >= IsoWorld.instance.CurrentCell.getWidthInTiles() || ty >= IsoWorld.instance.CurrentCell.getHeightInTiles())
            return null;
        try
        {
        	
        int n;
        if(mover == IsoCamera.CamCharacter)
            n = 19;
        if(astarmap.nodes[sx][sy][sz] == null)
            return null;
        if(sx >= astarmap.nodes.length || sy >= astarmap.nodes[0].length || sz >= astarmap.nodes[0][0].length)
            return null;
        if(sx < 0 || sy < 0 || sz < 0)
            return null;
        IsoGridSquare sq;
        sq = IsoWorld.instance.CurrentCell.getGridSquare(tx, ty, tz);
        if(sq == null)
            return null;
        if(!sq.isFree(false))
            return null;
        if(sq.getN() == null && sq.getS() == null && sq.getE() == null && sq.getW() == null)
            return null;
        Thread thread;
        astarmap.nodes[sx][sy][sz].searchData[threadID].cost = 0.0F;
        astarmap.nodes[sx][sy][sz].searchData[threadID].depth = 0;
        astarmap.nodes[sx][sy][sz].searchData[threadID].parent = null;
        closed.clear();
        open.clear();
        closedSet.clear();
        open.add(threadID, 0, astarmap.nodes[sx][sy][sz]);
        if(astarmap.nodes[tx][ty][tz].searchData[threadID] == null)
            astarmap.nodes[tx][ty][tz].searchData[threadID] = new SearchData();
        astarmap.nodes[tx][ty][tz].searchData[threadID].parent = null;
        maxDepth = 0;
        thread = Thread.currentThread();
        do
        {
            if(maxDepth >= maxSearchDistance || open.size() == 0)
                break;
            if(IngameState.DebugPathfinding && mover == IsoCamera.CamCharacter && !(thread instanceof zombie.PathfindManager.PathfindThread) || IngameState.AlwaysDebugPathfinding)
                Core.getInstance().StartFrame();
            AStarPathMap.Node current = getFirstInOpen();
            if(IngameState.AlwaysDebugPathfinding && !NodesUnfurled.contains(current))
                NodesUnfurled.add(current);
            if(current == astarmap.nodes[tx][ty][tz])
                break;
            removeFromOpen(threadID, current);
            addToClosed(current);
            for(int z = -1; z <= 1; z++)
            {
                for(int x = -1; x <= 1; x++)
                {
                    for(int y = -1; y <= 1; y++)
                    {
                        if(!astarmap.isValidLocation(mover, current.x, current.y, current.z, current.x + x, current.y + y, current.z + z, current.x, current.y, current.z))
                            continue;
                        AStarPathMap.Node newNode = astarmap.nodes[current.x + x][current.y + y][current.z + z];
                        if(newNode != null)
                            PathfindNode(threadID, current, mover, newNode, tx, ty, tz, nodesTried);
                    }

                }

            }

            if(IngameState.AlwaysDebugPathfinding)
            {
                Core.getInstance().EndFrame();
                Core.getInstance().StartFrameUI();
                renderOverhead();
                Core.getInstance().EndFrameUI();
                Display.update();
                Thread.sleep(30L);
            }
            
        } while(true);
        
        
        
        if(NodesUnfurled.size() > unfurledmax)
            unfurledmax = NodesUnfurled.size();
        if(astarmap.nodes[tx][ty][tz].searchData[threadID].parent == null)
            return null;
      
            Path path = new Path();
            AStarPathMap.Node target = astarmap.nodes[tx][ty][tz];
            path.cost += target.searchData[threadID].cost;
            int ltx = target.x;
            int lty = target.y;
            int ltz = target.z;
            for(; target != astarmap.nodes[sx][sy][sz]; target = target.searchData[threadID].parent)
            {
                int ttx = target.x;
                int tty = target.y;
                int ttz = target.z;
                if(ttx == ltx && tty == lty && ttz == ltz)
                    path.prependStep(ltx, lty, ltz);
                for(; ttx != ltx || tty != lty || ttz != ltz; path.prependStep(ltx, lty, ltz))
                {
                    if(ttx > ltx)
                        ltx++;
                    else
                    if(ttx < ltx)
                        ltx--;
                    if(tty > lty)
                        lty++;
                    else
                    if(tty < lty)
                        lty--;
                    if(ttz > ltz)
                    {
                        ltz++;
                        continue;
                    }
                    if(ttz < ltz)
                        ltz--;
                }

                ltx = target.x;
                lty = target.y;
                ltz = target.z;
            }

            if(sx == ltx && sy == lty && sz == ltz)
                path.prependStep(ltx, lty, ltz);
            for(; ltx != sx || lty != sy || ltz != sz; path.prependStep(ltx, lty, ltz))
            {
                if(sx > ltx)
                    ltx++;
                else
                if(sx < ltx)
                    ltx--;
                if(sy > lty)
                    lty++;
                else
                if(sy < lty)
                    lty--;
                if(sz > ltz)
                {
                    ltz++;
                    continue;
                }
                if(sz < ltz)
                    ltz--;
            }

            if(IngameState.DebugPathfinding && mover == IsoCamera.CamCharacter && !(thread instanceof zombie.PathfindManager.PathfindThread) || IngameState.AlwaysDebugPathfinding)
            {
                Core.getInstance().StartFrame();
                renderOverhead();
                renderPath(path);
                Core.getInstance().EndFrame();
                Display.update();
                Thread.sleep(2000L);
                IngameState.DebugPathfinding = false;
            }
            return path;
        }
        catch(Exception ex)
        {
        	Logger.getLogger(GameWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public Path findPathb(int threadID, Mover mover, int sx, int sy, int sz, int tx, int ty, 
            int tz)
    {
        int nodesTried;
        maxSearchDistance = 600;
        NodesUnfurled.clear();
        targetX = tx;
        targetY = ty;
        targetZ = tz;
        nodesTried = 0;
        if(astarmap == null)
        {
            astarmap = new AStarPathMap(IsoWorld.instance.CurrentCell);
            IsoWorld.instance.CurrentCell.setPathMap(astarmap);
        }
        if(tx < 0 || ty < 0 || tx >= IsoWorld.instance.CurrentCell.getWidthInTiles() || ty >= IsoWorld.instance.CurrentCell.getHeightInTiles())
            return null;
        try
        {
        
        if(astarmap.nodes[sx][sy][sz] == null)
            return null;
        if(sx >= astarmap.nodes.length || sy >= astarmap.nodes[0].length || sz >= astarmap.nodes[0][0].length)
            return null;
        if(sx < 0 || sy < 0 || sz < 0)
            return null;
        IsoGridSquare sq;
        sq = IsoWorld.instance.CurrentCell.getGridSquare(tx, ty, tz);
        if(sq == null)
            return null;
        if(!sq.isFree(false))
            return null;
        if(sq.getN() == null && sq.getS() == null && sq.getE() == null && sq.getW() == null)
            return null;
        Thread thread;
        astarmap.nodes[sx][sy][sz].searchData[threadID].cost = 0.0F;
        astarmap.nodes[sx][sy][sz].searchData[threadID].depth = 0;
        astarmap.nodes[sx][sy][sz].searchData[threadID].parent = null;
        closed.clear();
        open.clear();
        closedSet.clear();
        open.add(threadID, 0, astarmap.nodes[sx][sy][sz]);
        if(astarmap.nodes[tx][ty][tz].searchData[threadID] == null)
            astarmap.nodes[tx][ty][tz].searchData[threadID] = new SearchData();
        astarmap.nodes[tx][ty][tz].searchData[threadID].parent = null;
        maxDepth = 0;
        thread = Thread.currentThread();
        do
        {
            if(maxDepth >= maxSearchDistance || open.size() == 0)
                break;
            AStarPathMap.Node current = getFirstInOpen();
            if(IngameState.AlwaysDebugPathfinding && !NodesUnfurled.contains(current))
                NodesUnfurled.add(current);
            if(current == astarmap.nodes[tx][ty][tz])
                break;
            int n;
            if(current.x == 132 && current.y == 122 && current.z == 1)
                n = 0;
            removeFromOpen(threadID, current);
            addToClosed(current);
            IsoDirections dirFrom = DirectionWeFrom(current, current.searchData[threadID].parent);
            AStarPathMap.Node last = getNodeFrom(current, IsoDirections.reverse(dirFrom));
            set.set = 0;
            set.set = NaturalNeighbours(last, dirFrom) | ForcedNeighbours(current, last, dirFrom);
            do
            {
                if(set.set == 0)
                    break;
                IsoDirections dir = set.getNext();
                AStarPathMap.Node newNode = Jump(dir, current);
                if(newNode != null && !closedSet.contains(newNode.ID))
                    PathfindNode(threadID, current, mover, newNode, tx, ty, tz, nodesTried);
            } while(true);
            if(IsoWorld.instance.CurrentCell.getStairsNodes().contains(current.ID))
            {
                for(int x = -1; x <= 1; x++)
                {
                    for(int y = -1; y <= 1; y++)
                    {
                        for(int z = -1; z <= 1; z++)
                        {
                            if(z == 0 || x != 0 && y != 0 || x == 0 && y == 0 || !astarmap.isValidLocation(mover, current.x, current.y, current.z, current.x + x, current.y + y, current.z + z, current.x, current.y, current.z))
                                continue;
                            AStarPathMap.Node newNode = astarmap.nodes[current.x + x][current.y + y][current.z + z];
                            if(newNode != null)
                                PathfindNode(threadID, current, mover, newNode, tx, ty, tz, nodesTried);
                        }

                    }

                }

            }
            if(IngameState.AlwaysDebugPathfinding)
            {
                Core.getInstance().StartFrame();
                Core.getInstance().EndFrame();
                Core.getInstance().StartFrameUI();
                renderOverhead();
                Core.getInstance().EndFrameUI();
                Display.update();
                Thread.sleep(30L);
            }
        } while(true);
        if(NodesUnfurled.size() > unfurledmax)
            unfurledmax = NodesUnfurled.size();
        if(astarmap.nodes[tx][ty][tz].searchData[threadID].parent == null)
            return null;
        
            Path path = new Path();
            AStarPathMap.Node target = astarmap.nodes[tx][ty][tz];
            path.cost += target.searchData[threadID].cost;
            int ltx = target.x;
            int lty = target.y;
            int ltz = target.z;
            for(; target != astarmap.nodes[sx][sy][sz]; target = target.searchData[threadID].parent)
            {
                int ttx = target.x;
                int tty = target.y;
                int ttz = target.z;
                if(ttx == ltx && tty == lty && ttz == ltz)
                    path.prependStep(ltx, lty, ltz);
                for(; ttx != ltx || tty != lty || ttz != ltz; path.prependStep(ltx, lty, ltz))
                {
                    if(ttx > ltx)
                        ltx++;
                    else
                    if(ttx < ltx)
                        ltx--;
                    if(tty > lty)
                        lty++;
                    else
                    if(tty < lty)
                        lty--;
                    if(ttz > ltz)
                    {
                        ltz++;
                        continue;
                    }
                    if(ttz < ltz)
                        ltz--;
                }

                ltx = target.x;
                lty = target.y;
                ltz = target.z;
            }

            if(sx == ltx && sy == lty && sz == ltz)
                path.prependStep(ltx, lty, ltz);
            for(; ltx != sx || lty != sy || ltz != sz; path.prependStep(ltx, lty, ltz))
            {
                if(sx > ltx)
                    ltx++;
                else
                if(sx < ltx)
                    ltx--;
                if(sy > lty)
                    lty++;
                else
                if(sy < lty)
                    lty--;
                if(sz > ltz)
                {
                    ltz++;
                    continue;
                }
                if(sz < ltz)
                    ltz--;
            }

            if(IngameState.DebugPathfinding && mover == IsoCamera.CamCharacter && !(thread instanceof zombie.PathfindManager.PathfindThread) || IngameState.AlwaysDebugPathfinding)
            {
                Core.getInstance().StartFrame();
                renderOverhead();
                renderPath(path);
                Core.getInstance().EndFrame();
                Display.update();
                Thread.sleep(2000L);
                IngameState.DebugPathfinding = false;
            }
            return path;
        }
        catch(Exception ex)
        {
        	Logger.getLogger(GameWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private void renderPath(Path path)
    {
        TextureID.UseFiltering = true;
        Texture.getSharedTexture("media/ui/white.png");
        IsoCell cell = IsoWorld.instance.CurrentCell;
        Texture tex = Texture.getSharedTexture("media/ui/white.png");
        int drawFloor = 0;
        int tilew = 2;
        int offx = Core.getInstance().getOffscreenWidth() - cell.getWidthInTiles() * tilew;
        int offy = Core.getInstance().getOffscreenHeight() - cell.getHeightInTiles() * tilew;
        for(int n = 0; n < path.getLength() - 1; n++)
        {
            int cx = path.getX(n);
            int cy = path.getY(n);
            int nx = path.getX(n + 1);
            for(int ny = path.getY(n + 1); cx != nx || cy != ny;)
            {
                if(cx < nx)
                    cx++;
                if(cx > nx)
                    cx--;
                if(cy < ny)
                    cy++;
                if(cy > ny)
                    cy--;
                tex.render(offx + cx * tilew, offy + cy * tilew, tilew, tilew, 0.0F, 0.0F, 1.0F, 1.0F);
            }

        }

    }

    private void renderOverhead()
    {
        TextureID.UseFiltering = true;
        Texture.getSharedTexture("media/ui/white.png");
        IsoCell cell = IsoWorld.instance.CurrentCell;
        Texture tex = Texture.getSharedTexture("media/ui/white.png");
        int drawFloor = 0;
        int tilew = 2;
        int offx = Core.getInstance().getOffscreenWidth() - cell.getWidthInTiles() * tilew;
        int offy = Core.getInstance().getOffscreenHeight() - cell.getHeightInTiles() * tilew;
        tex.render(offx, offy, tilew * cell.getWidthInTiles(), tilew * cell.getHeightInTiles(), 0.7F, 0.7F, 0.7F, 1.0F);
        for(int x = 0; x < cell.getWidthInTiles(); x++)
        {
            for(int y = 0; y < cell.getHeightInTiles(); y++)
            {
                IsoGridSquare sq = cell.getGridSquare(x, y, drawFloor);
                if(sq == null)
                    continue;
                if(sq.getProperties().Is(IsoFlagType.solid) || sq.getProperties().Is(IsoFlagType.solidtrans))
                    tex.render(offx + x * tilew, offy + y * tilew, tilew, tilew, 0.5F, 0.5F, 0.5F, 255F);
                else
                if(!sq.getProperties().Is(IsoFlagType.exterior))
                    tex.render(offx + x * tilew, offy + y * tilew, tilew, tilew, 0.8F, 0.8F, 0.8F, 1.0F);
                if(sq.getProperties().Is(IsoFlagType.collideN))
                    tex.render(offx + x * tilew, offy + y * tilew, tilew, 1, 0.2F, 0.2F, 0.2F, 1.0F);
                if(sq.getProperties().Is(IsoFlagType.collideW))
                    tex.render(offx + x * tilew, offy + y * tilew, 1, tilew, 0.2F, 0.2F, 0.2F, 1.0F);
            }

        }

        for(int n = 0; n < NodesUnfurled.size(); n++)
        {
            AStarPathMap.Node node = (AStarPathMap.Node)NodesUnfurled.get(n);
            tex.render(offx + node.x * tilew, offy + node.y * tilew, tilew, tilew, 1.0F, 0.0F, 0.0F, 1.0F);
        }

        tex.render(offx + startX * tilew, offy + startY * tilew, tilew, tilew, 0.0F, 1.0F, 0.0F, 1.0F);
        tex.render(offx + targetX * tilew, offy + targetY * tilew, tilew, tilew, 1.0F, 1.0F, 0.0F, 1.0F);
        TextureID.UseFiltering = false;
    }

    public PathFindProgress findPathActual(Mover mover, int sx, int sy, int sz, int tx, int ty, int tz)
    {
        if(astarmap == null)
        {
            astarmap = new AStarPathMap(IsoWorld.instance.CurrentCell);
            IsoWorld.instance.CurrentCell.setPathMap(astarmap);
        }
        foundPath = null;
        targetX = tx;
        targetY = ty;
        targetZ = tz;
        startX = sx;
        startY = sy;
        startZ = sz;
        this.mover = mover;
        progress = PathFindProgress.notyetfound;
        return PathFindProgress.notyetfound;
    }

    public PathFindProgress findPathSlice(int threadID, IPathfinder pathfinder, Mover mover, int sx, int sy, int sz, int tx, 
            int ty, int tz)
    {
        if(!IngameState.DebugPathfinding && !IngameState.AlwaysDebugPathfinding)
        {
            PathfindManager.instance.AddJob(pathfinder, mover, sx, sy, sz, tx, ty, tz);
        } else
        {
            startX = sx;
            startY = sy;
            startZ = sz;
            targetX = tx;
            targetY = ty;
            targetZ = tz;
            this.mover = mover;
            maxSearchDistance = 1000;
            Cycle(threadID);
            if(progress == PathFindProgress.found)
                pathfinder.Succeeded(getPath(), mover);
            else
                pathfinder.Failed(mover);
            return progress;
        }
        progress = PathFindProgress.notyetfound;
        return PathFindProgress.notyetfound;
    }

    public int getFreeIndex(IsoGameCharacter character)
    {
        if(astarmap == null)
        {
            astarmap = new AStarPathMap(IsoWorld.instance.CurrentCell);
            IsoWorld.instance.CurrentCell.setPathMap(astarmap);
        }
        return 0;
    }

    public float getHeuristicCost(Mover mover, int x, int y, int z, int tx, int ty, int tz)
    {
        return heuristic.getCost(astarmap.map, mover, x, y, z, tx, ty, tz);
    }

    public Path getPath()
    {
        return foundPath;
    }

    protected void addToClosed(AStarPathMap.Node node)
    {
        closed.add(node);
        closedSet.add(node.ID);
    }

    protected void addToOpen(int threadID, int id, AStarPathMap.Node node)
    {
        open.add(threadID, id, node);
    }

    protected AStarPathMap.Node getFirstInOpen()
    {
        return open.first();
    }

    protected boolean inClosedList(AStarPathMap.Node node)
    {
        return closedSet.contains(node.ID);
    }

    protected boolean inOpenList(AStarPathMap.Node node)
    {
        return open.contains(node);
    }

    protected void removeFromClosed(AStarPathMap.Node node)
    {
        closed.remove(node);
        closedSet.remove(node.ID);
    }

    protected void removeFromOpen(int threadID, AStarPathMap.Node node)
    {
        open.remove(threadID, node);
    }

    public static int IDToUseInSort = 0;
    IsoDirectionSet calcSet;
    static Vector2 zfindangle = new Vector2();
    public boolean allowDiagMovement;
    public int cyclesPerSlice;
    public int delay;
    public int maxSearchDistance;
    public PathFindProgress progress;
    public int startX;
    public int startY;
    public int startZ;
    public int targetX;
    public int targetY;
    public int targetZ;
    static AStarPathMap astarmap;
    Path foundPath;
    int maxDepth;
    Mover mover;
    public IsoGameCharacter character;
    private ArrayList closed;
    private AStarHeuristic heuristic;
    private SortedList open;
    private THashSet closedSet;
    public ArrayList NodesUnfurled;
    static int unfurledmax = 0;
    IsoDirectionSet set;

}