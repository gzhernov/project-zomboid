// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 13.08.2012 13:54:45
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   VirtualZombieManager.java

package zombie;

import java.io.*;
import java.util.Iterator;
import java.util.Stack;
import org.lwjgl.input.Keyboard;
import zombie.ai.astar.Path;
import zombie.characters.IsoGameCharacter;
import zombie.characters.IsoZombie;
import zombie.characters.ZombieGroup;
import zombie.core.Collections.NulledArrayList;
import zombie.core.Core;
import zombie.core.Rand;
import zombie.core.utils.OnceEvery;
import zombie.input.Mouse;
import zombie.iso.IsoCamera;
import zombie.iso.IsoCell;
import zombie.iso.IsoGridSquare;
import zombie.iso.IsoUtils;
import zombie.iso.IsoWorld;

// Referenced classes of package zombie:
//            VirtualZombie, WorldSoundManager, PathfindManager

public class VirtualZombieManager
{

    public VirtualZombieManager()
    {
        ReusableZombies = new Stack();
        virtualZombies = new NulledArrayList();
        VirtualizedThisFrame = new NulledArrayList();
        TotalVirtualZombies = 0;
        MaxRealZombies = 150;
        SpawnCentreX = 0;
        SpawnCentreY = 0;
        SpawnRadius = 64;
        VirtualUpdater = new OnceEvery(0.5F);
        choices = new IsoGridSquare[9];
    }

    public void init()
    {
        IsoZombie zombie = null;
        for(int n = 0; n < MaxRealZombies; n++)
        {
            zombie = new IsoZombie(IsoWorld.instance.CurrentCell);
            IsoWorld.instance.CurrentCell.getObjectList().remove(zombie);
            zombie.group.Leader = zombie;
            zombie.group = new ZombieGroup();
            ZombieGroup g = zombie.group;
            g.zombies[0] = zombie;
            ReusableZombies.push(zombie);
        }

    }

    void addSwarm()
    {
    }

    public void update()
    {
        if(Keyboard.isKeyDown(15))
        {
            int tilew = 2;
            int offx = Core.getInstance().getOffscreenWidth() - IsoWorld.instance.CurrentCell.getWidthInTiles() * tilew;
            int offy = Core.getInstance().getOffscreenHeight() - IsoWorld.instance.CurrentCell.getHeightInTiles() * tilew;
            SpawnCentreX = Mouse.getXA();
            SpawnCentreY = Mouse.getYA();
            SpawnCentreX -= offx;
            SpawnCentreY -= offy;
            SpawnCentreX /= tilew;
            SpawnCentreY /= tilew;
        }
        SpawnCentreX = (int)IsoCamera.CamCharacter.x;
        SpawnCentreY = (int)IsoCamera.CamCharacter.y;
        RealifiedThisFrame.clear();
        Iterator it = virtualZombies.iterator();
        do
        {
            if(it == null || !it.hasNext())
                break;
            VirtualZombie z = (VirtualZombie)it.next();
            float dist = IsoUtils.DistanceManhatten(z.x, z.y, SpawnCentreX, SpawnCentreY);
            if(dist < (float)SpawnRadius && dist > (float)(SpawnRadius / 2) && MaxRealZombies > IsoWorld.instance.CurrentCell.getZombieList().size())
            {
                RealifiedThisFrame.add(z);
                int nChoices = 1;
                IsoGridSquare sq = IsoWorld.instance.CurrentCell.getGridSquare(z.x, z.y, 0);
                choices[0] = sq;
                if(sq.w != null)
                {
                    choices[nChoices] = sq.w;
                    nChoices++;
                }
                if(sq.s != null)
                {
                    choices[nChoices] = sq.s;
                    nChoices++;
                }
                if(sq.e != null)
                {
                    choices[nChoices] = sq.e;
                    nChoices++;
                }
                if(sq.n != null)
                {
                    choices[nChoices] = sq.n;
                    nChoices++;
                }
                if(sq.sw != null)
                {
                    choices[nChoices] = sq.sw;
                    nChoices++;
                }
                if(sq.se != null)
                {
                    choices[nChoices] = sq.se;
                    nChoices++;
                }
                if(sq.ne != null)
                {
                    choices[nChoices] = sq.ne;
                    nChoices++;
                }
                if(sq.nw != null)
                {
                    choices[nChoices] = sq.nw;
                    nChoices++;
                }
                int n = 0;
                while(n < z.Count) 
                {
                    IsoZombie zombie = null;
                    if(ReusableZombies.empty())
                    {
                        zombie = new IsoZombie(IsoWorld.instance.CurrentCell);
                    } else
                    {
                        zombie = (IsoZombie)ReusableZombies.pop();
                        IsoWorld.instance.CurrentCell.getObjectList().add(zombie);
                    }
                    float specX = Rand.Next(0, 1000);
                    float specY = Rand.Next(0, 1000);
                    specX /= 1000F;
                    specY /= 1000F;
                    IsoGridSquare choice = choices[Rand.Next(nChoices)];
                    specX += choice.getX();
                    specY += choice.getY();
                    zombie.setX(specX);
                    zombie.setY(specY);
                    IsoWorld.instance.CurrentCell.getZombieList().add(zombie);
                    n++;
                }
            }
        } while(true);
        VirtualZombie z;
        for(it = RealifiedThisFrame.iterator(); it != null && it.hasNext(); virtualZombies.remove(z))
            z = (VirtualZombie)it.next();

        RealifiedThisFrame.clear();
        VirtualizedThisFrame.clear();
        Iterator it2 = IsoWorld.instance.CurrentCell.getZombieList().iterator();
        
        IsoZombie z1;
        do
        {
            if(it2 == null || !it2.hasNext())
                break;
            z1 = (IsoZombie)it2.next();
            float dist = IsoUtils.DistanceManhatten(SpawnCentreX, SpawnCentreY, z1.getX(), z1.getY());
            dist -= 10F;
            if(dist >= (float)SpawnRadius && !z1.KeepItReal)
                VirtualizedThisFrame.add(z1);
        } while(true);
        int toAdd = VirtualizedThisFrame.size();
        if(toAdd > 20)
        {
            Iterator it3 = VirtualizedThisFrame.iterator();
            do
            {
                if(it3 == null || !it3.hasNext())
                    break;
                IsoZombie z11 = (IsoZombie)it3.next();
                IsoWorld.instance.CurrentCell.getZombieList().remove(z11);
                IsoWorld.instance.CurrentCell.getObjectList().remove(z11);
                if(z11.getCurrentSquare() != null)
                {
                    z11.getCurrentSquare().getMovingObjects().remove(z11);
                    z11.getLastSquare().getMovingObjects().remove(z11);
                }
                z11.setCurrent(null);
                z11.setLast(null);
                ReusableZombies.push(z11);
                float smallestDist = 1E+007F;
                VirtualZombie vz = null;
                it = virtualZombies.iterator();
                do
                {
                    if(it == null || !it.hasNext())
                        break;
                    VirtualZombie v = (VirtualZombie)it.next();
                    float tDist = IsoUtils.DistanceManhatten(v.x, v.y, z11.x, z11.y);
                    if(tDist < smallestDist)
                    {
                        smallestDist = tDist;
                        vz = v;
                    }
                } while(true);
                if(smallestDist < 40F && vz.Count > 10)
                {
                    vz.Count++;
                    toAdd--;
                }
            } while(true);
            while(toAdd > MaxRealZombies / 2) 
            {
                int x = (int)((IsoZombie)VirtualizedThisFrame.get(0)).getX();
                int y = (int)((IsoZombie)VirtualizedThisFrame.get(0)).getY();
                VirtualZombie v = new VirtualZombie();
                v.Count = MaxRealZombies / 2;
                v.x = x;
                v.y = y;
                virtualZombies.add(v);
                toAdd -= MaxRealZombies / 2;
                VirtualizedThisFrame.remove(0);
            }
            if(toAdd > 0)
            {
                int x = (int)((IsoZombie)VirtualizedThisFrame.get(0)).getX();
                int y = (int)((IsoZombie)VirtualizedThisFrame.get(0)).getY();
                VirtualZombie v = new VirtualZombie();
                v.Count = toAdd;
                v.x = x;
                v.y = y;
                virtualZombies.add(v);
            }
        }
        for(int n = 0; n < virtualZombies.size(); n++)
        {
            VirtualZombie a = (VirtualZombie)virtualZombies.get(n);
            for(int m = n + 1; m < virtualZombies.size(); m++)
            {
                VirtualZombie b = (VirtualZombie)virtualZombies.get(m);
                if(a != b && a.Count + b.Count <= MaxRealZombies / 2 && a.Count > 1 && b.Count > 1 && IsoUtils.DistanceManhatten(a.x, a.y, b.x, b.y) < 30F)
                {
                    a.Count += b.Count;
                    virtualZombies.remove(b);
                }
            }

        }

        it = virtualZombies.iterator();
        do
        {
            if(it == null || !it.hasNext())
                break;
            VirtualZombie z2 = (VirtualZombie)it.next();
            WorldSoundManager.WorldSound s = WorldSoundManager.instance.getBiggestSoundZomb(z2.x, z2.y, 0, true);
            if(s != null)
            {
                PathfindManager.instance.AddJob(z2, null, z2.x, z2.y, 0, s.x, s.y, 0);
                z2.finding = true;
                z2.path = null;
            }
        } while(true);
        if(VirtualUpdater.Check())
        {
            it = virtualZombies.iterator();
            do
            {
                if(it == null || !it.hasNext())
                    break;
                VirtualZombie z3 = (VirtualZombie)it.next();
                if(z3.path != null)
                {
                    z3.x = z3.path.getX(z3.PathIndex);
                    z3.y = z3.path.getY(z3.PathIndex);
                    z3.PathIndex++;
                    if(z3.PathIndex >= z3.path.getLength())
                    {
                        z3.path = null;
                        z3.finding = false;
                    }
                }
                if(!z3.finding && z3.path == null)
                {
                    IsoGridSquare sq = IsoWorld.instance.CurrentCell.getRandomOutdoorTile();
                    PathfindManager.instance.AddJob(z3, z3, z3.x, z3.y, 0, sq.getX(), sq.getY(), 0);
                    z3.finding = true;
                }
            } while(true);
        }
    }

    public void AddZombiesToMap(int nPrefered)
    {
        int groupCount = nPrefered / MaxRealZombies;
        int groupSize = MaxRealZombies / 2;
        for(int n = 0; n < groupCount - 1; n++)
        {
            IsoGridSquare sq = IsoWorld.instance.CurrentCell.getRandomOutdoorTile();
            VirtualZombie v = new VirtualZombie();
            v.Count = groupSize;
            v.x = sq.getX();
            v.y = sq.getY();
            virtualZombies.add(v);
        }

        groupCount = 5;
        groupSize /= groupCount;
        for(int n = 0; n < groupCount; n++)
        {
            IsoGridSquare sq = IsoWorld.instance.CurrentCell.getRandomOutdoorTile();
            VirtualZombie v = new VirtualZombie();
            v.Count = groupSize;
            v.x = sq.getX();
            v.y = sq.getY();
            virtualZombies.add(v);
        }

    }

    public void load(DataInputStream input)
        throws IOException
    {
        int c = input.readInt();
        for(int n = 0; n < c; n++)
        {
            VirtualZombie z = new VirtualZombie();
            z.Count = input.readInt();
            z.x = input.readInt();
            z.y = input.readInt();
            z.targx = input.readInt();
            z.targy = input.readInt();
            virtualZombies.add(z);
        }

    }

    public void save(DataOutputStream output)
        throws IOException
    {
        output.writeInt(virtualZombies.size());
        for(int n = 0; n < virtualZombies.size(); n++)
        {
            VirtualZombie z = (VirtualZombie)virtualZombies.get(n);
            output.writeInt(z.Count);
            output.writeInt(z.x);
            output.writeInt(z.y);
            output.writeInt(z.targx);
            output.writeInt(z.targy);
        }

    }

    public Stack ReusableZombies;
    public NulledArrayList virtualZombies;
    public static VirtualZombieManager instance = new VirtualZombieManager();
    public NulledArrayList VirtualizedThisFrame;
    static NulledArrayList RealifiedThisFrame = new NulledArrayList();
    public int TotalVirtualZombies;
    public int MaxRealZombies;
    public int SpawnCentreX;
    public int SpawnCentreY;
    public int SpawnRadius;
    public OnceEvery VirtualUpdater;
    IsoGridSquare choices[];

}