// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   GotoOrder.java

package zombie.behaviors.survivor.orders;

import java.util.Stack;
import zombie.behaviors.Behavior;
import zombie.behaviors.general.PathFindBehavior;
import zombie.characters.IsoGameCharacter;
import zombie.core.Rand;
import zombie.iso.*;
import zombie.ui.TextManager;
import zombie.ui.UIFont;

// Referenced classes of package zombie.behaviors.survivor.orders:
//            Order

public class GotoOrder extends Order
{
    public static class Waypoint
    {

        public int x;
        public int y;
        public int z;

        public Waypoint(int x, int y, int z)
        {
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }


    private Waypoint AddIntermediate(float dist, int sx, int sy, int sz, int x, int y, int z)
    {
        Vector2 vecT = new Vector2(x, y);
        Vector2 vecS = new Vector2(sx, sy);
        Vector2 vecD = new Vector2(x - sx, y - sy);
        vecD.setLength(dist / 2.0F);
        vecS.x += vecD.x;
        vecS.y += vecD.y;
        int range = 0;
        IsoGridSquare sq = null;
        do
        {
            int tries = range * 3;
            if(tries == 0)
                tries = 1;
            for(int n = 0; n < tries; n++)
            {
                int nx = (int)vecS.x + Rand.Next(-range, range);
                int ny = (int)vecS.y + Rand.Next(-range, range);
                sq = IsoWorld.instance.CurrentCell.getGridSquare(nx, ny, 0);
                if(!sq.isFree(false))
                    sq = null;
                if(sq != null)
                    n = tries;
            }

            if(sq == null)
                range++;
        } while(sq == null);
        return new Waypoint(sq.getX(), sq.getY(), 0);
    }

    private void SplitWaypoints(int sx, int sy, int sz, int x, int y, int z)
    {
        Stack Temp = new Stack();
        sz = 0;
        int size = Waypoints.size();
        for(int n = 0; n < size; n++)
        {
            int xx = x;
            int yy = y;
            int zz = z;
            if(size > n + 1)
            {
                xx = ((Waypoint)Waypoints.get(n + 1)).x;
                yy = ((Waypoint)Waypoints.get(n + 1)).y;
            }
            float dist = IsoUtils.DistanceManhatten(sx, sy, ((Waypoint)Waypoints.get(n)).x, ((Waypoint)Waypoints.get(n)).y);
            Waypoint add = AddIntermediate(dist, sx, sy, sz, xx, yy, zz);
            sx = ((Waypoint)Waypoints.get(n)).x;
            sy = ((Waypoint)Waypoints.get(n)).y;
            Temp.add(add);
            Temp.add(Waypoints.get(n));
        }

        Waypoints.clear();
        Waypoints.addAll(Temp);
    }

    public int getAttackIfEnemiesAroundBias()
    {
        return character.getCurrentSquare().getRoom() == null ? 0 : -1000;
    }

    private void CalculateWaypoints(IsoGameCharacter chr, int sx, int sy, int sz, int x, int y, int z)
    {
        Waypoints.clear();
        Waypoints.add(new Waypoint(x, y, z));
        float dist = IsoUtils.DistanceManhatten(sx, sy, x, y);
        int split = (int)(dist / 60F);
        if(failedcount > 2 && dist > 60F)
            split += failedcount - 2;
        if(split > 4)
            split = 4;
    }

    public GotoOrder(IsoGameCharacter chr, int x, int y, int z)
    {
        super(chr);
        failedcount = 0;
        Waypoints = new Stack();
        currentwaypoint = 0;
        nextpathfind = 120F;
        PathFind = new PathFindBehavior("Goto");
        res = zombie.behaviors.Behavior.BehaviorResult.Working;
        this.chr = chr;
        this.x = x;
        this.y = y;
        this.z = z;
        PathFind.tx = x;
        PathFind.ty = y;
        PathFind.tz = z;
        nextpathfind = Rand.Next(120);
        CalculateWaypoints(chr, (int)chr.getX(), (int)chr.getY(), (int)chr.getZ(), x, y, z);
        currentwaypoint = 0;
        PathFind.reset();
        PathFind.sx = (int)chr.getX();
        PathFind.sy = (int)chr.getY();
        PathFind.sz = (int)chr.getZ();
        PathFind.tx = ((Waypoint)Waypoints.get(currentwaypoint)).x;
        PathFind.ty = ((Waypoint)Waypoints.get(currentwaypoint)).y;
        PathFind.tz = ((Waypoint)Waypoints.get(currentwaypoint)).z;
    }

    public GotoOrder(IsoGameCharacter chr)
    {
        super(chr);
        failedcount = 0;
        Waypoints = new Stack();
        currentwaypoint = 0;
        nextpathfind = 120F;
        PathFind = new PathFindBehavior("Goto");
        res = zombie.behaviors.Behavior.BehaviorResult.Working;
        this.chr = chr;
    }

    public void init(int x, int y, int z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        PathFind.tx = x;
        PathFind.ty = y;
        PathFind.tz = z;
        nextpathfind = Rand.Next(120);
        CalculateWaypoints(chr, (int)chr.getX(), (int)chr.getY(), (int)chr.getZ(), x, y, z);
        currentwaypoint = 0;
        PathFind.reset();
        PathFind.sx = (int)chr.getX();
        PathFind.sy = (int)chr.getY();
        PathFind.sz = (int)chr.getZ();
        PathFind.tx = ((Waypoint)Waypoints.get(currentwaypoint)).x;
        PathFind.ty = ((Waypoint)Waypoints.get(currentwaypoint)).y;
        PathFind.tz = ((Waypoint)Waypoints.get(currentwaypoint)).z;
        PathFind.osx = PathFind.sx;
        PathFind.osy = PathFind.sy;
        PathFind.osz = PathFind.sz;
        PathFind.otx = PathFind.tx;
        PathFind.oty = PathFind.ty;
        PathFind.otz = PathFind.tz;
    }

    public boolean complete()
    {
        return currentwaypoint >= Waypoints.size() || failedcount > 20;
    }

    public void update()
    {
        if(res != zombie.behaviors.Behavior.BehaviorResult.Working)
            nextpathfind--;
    }

    public zombie.behaviors.Behavior.BehaviorResult process()
    {
        int n;
        if(character == IsoCamera.CamCharacter)
            n = 0;
        res = PathFind.process(null, character);
        if(res == zombie.behaviors.Behavior.BehaviorResult.Succeeded && currentwaypoint < Waypoints.size())
        {
            Waypoint p = (Waypoint)Waypoints.get(currentwaypoint);
            if((int)character.getX() != p.x || (int)character.getY() != p.y || (int)character.getZ() != p.z)
                res = zombie.behaviors.Behavior.BehaviorResult.Failed;
            else
                nextpathfind = -1F;
        }
        if(res == zombie.behaviors.Behavior.BehaviorResult.Failed && nextpathfind < 0.0F)
        {
            failedcount++;
            if(failedcount <= 100);
            if(character == IsoCamera.CamCharacter)
                n = 0;
            nextpathfind = 120F;
            CalculateWaypoints(chr, (int)chr.getX(), (int)chr.getY(), (int)chr.getZ(), this.x, this.y, this.z);
            currentwaypoint = 0;
            PathFind.reset();
            PathFind.sx = (int)chr.getX();
            PathFind.sy = (int)chr.getY();
            PathFind.sz = (int)chr.getZ();
            PathFind.tx = ((Waypoint)Waypoints.get(currentwaypoint)).x;
            PathFind.ty = ((Waypoint)Waypoints.get(currentwaypoint)).y;
            PathFind.tz = ((Waypoint)Waypoints.get(currentwaypoint)).z;
            PathFind.osx = PathFind.sx;
            PathFind.osy = PathFind.sy;
            PathFind.osz = PathFind.sz;
            PathFind.otx = PathFind.tx;
            PathFind.oty = PathFind.ty;
            PathFind.otz = PathFind.tz;
            res = PathFind.process(null, character);
        }
        if(res == zombie.behaviors.Behavior.BehaviorResult.Succeeded && currentwaypoint < Waypoints.size() && nextpathfind < 0.0F)
        {
            if(character == IsoCamera.CamCharacter)
                n = 0;
            currentwaypoint++;
            nextpathfind = 120F;
            if(currentwaypoint < Waypoints.size())
            {
                int x = ((Waypoint)Waypoints.get(currentwaypoint)).x;
                int y = ((Waypoint)Waypoints.get(currentwaypoint)).y;
                int z = ((Waypoint)Waypoints.get(currentwaypoint)).z;
                this.x = x;
                this.y = y;
                this.z = z;
                PathFind.reset();
                PathFind.tx = x;
                PathFind.ty = y;
                PathFind.tz = z;
                PathFind.sx = (int)chr.getX();
                PathFind.sy = (int)chr.getY();
                PathFind.sz = (int)chr.getZ();
                res = PathFind.process(null, character);
            } else
            {
                res = zombie.behaviors.Behavior.BehaviorResult.Succeeded;
            }
        }
        nextpathfind--;
        if(currentwaypoint >= Waypoints.size())
            return zombie.behaviors.Behavior.BehaviorResult.Succeeded;
        else
            return zombie.behaviors.Behavior.BehaviorResult.Working;
    }

    public int renderDebug(int y)
    {
        int x = 50;
        TextManager.instance.DrawString(UIFont.Small, x, y, "GotoOrder", 1.0F, 1.0F, 1.0F, 1.0F);
        y += 30;
        for(int n = currentwaypoint; n < Waypoints.size(); n++)
        {
            Waypoint w = (Waypoint)Waypoints.get(n);
            Integer xx = Integer.valueOf(w.x);
            Integer yy = Integer.valueOf(w.y);
            Integer zz = Integer.valueOf(w.z);
            TextManager.instance.DrawString(UIFont.Small, x, y, (new StringBuilder()).append("Waypoint ").append(n).append(" - x: ").append(xx).append(" y: ").append(yy).append(" z: ").append(zz).toString(), 1.0F, 1.0F, 1.0F, 1.0F);
            y += 30;
        }

        PathFind.renderDebug(y);
        return y;
    }

    public float getPriority(IsoGameCharacter character)
    {
        return res != zombie.behaviors.Behavior.BehaviorResult.Working && nextpathfind >= 0.0F ? -100F : 200F;
    }

    public int failedcount;
    int x;
    int y;
    int z;
    public Stack Waypoints;
    public int currentwaypoint;
    float nextpathfind;
    PathFindBehavior PathFind;
    public IsoGameCharacter chr;
    zombie.behaviors.Behavior.BehaviorResult res;
}
