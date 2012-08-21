// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   PathFindBehavior.java

package zombie.behaviors.general;

import java.util.Stack;
import zombie.GameTime;
import zombie.PathfindManager;
import zombie.ai.astar.*;
import zombie.ai.states.PathFindState;
import zombie.behaviors.Behavior;
import zombie.behaviors.DecisionPath;
import zombie.characters.*;
import zombie.core.Collections.NulledArrayList;
import zombie.core.utils.OnceEvery;
import zombie.iso.*;
import zombie.iso.objects.IsoDoor;
import zombie.iso.objects.IsoWindow;
import zombie.ui.TextManager;
import zombie.ui.UIFont;

public class PathFindBehavior extends Behavior
    implements IPathfinder
{

    public String getName()
    {
        return name;
    }

    public PathFindBehavior()
    {
        pathIndex = 0;
        useScriptXY = false;
        finder = new AStarPathFinderResult();
        name = "unnamed";
        lastCancel = 0xf4240;
        chr = null;
    }

    public PathFindBehavior(String name)
    {
        pathIndex = 0;
        useScriptXY = false;
        finder = new AStarPathFinderResult();
        this.name = "unnamed";
        lastCancel = 0xf4240;
        chr = null;
        this.name = name;
    }

    public PathFindBehavior(boolean useScriptXY)
    {
        pathIndex = 0;
        this.useScriptXY = false;
        finder = new AStarPathFinderResult();
        name = "unnamed";
        lastCancel = 0xf4240;
        chr = null;
        this.useScriptXY = useScriptXY;
    }

    public zombie.behaviors.Behavior.BehaviorResult process(DecisionPath dpath, IsoGameCharacter character)
    {
        PathFindState.fastMove.SetFrequency(1.0F / GameTime.instance.getMultiplier());
        PathFindState.fastMoveTwo.SetFrequency(1.0F / GameTime.instance.getMultiplier());
        chr = character;
        int n;
        if(character == IsoCamera.CamCharacter)
            n = 0;
        finder.maxSearchDistance = 800;
        if((character instanceof IsoSurvivor) && lastCancel > 120 && ((IsoSurvivor)character).dangerTile - ((IsoSurvivor)character).lastDangerTile > 60 && ((IsoSurvivor)character).dangerTile > 0)
        {
            finder.progress = zombie.ai.astar.AStarPathFinder.PathFindProgress.notrunning;
            path = null;
            sx = (int)character.getX();
            sy = (int)character.getY();
            sz = (int)character.getZ();
            lastCancel = 0;
            return zombie.behaviors.Behavior.BehaviorResult.Working;
        }
        lastCancel++;
        if(this.tx == 0 && this.ty == 0 && tz == 0)
        {
            reset();
            finder.progress = zombie.ai.astar.AStarPathFinder.PathFindProgress.notrunning;
            return zombie.behaviors.Behavior.BehaviorResult.Failed;
        }
        if(sx == this.tx && sy == this.ty && sz == tz)
        {
            reset();
            finder.progress = zombie.ai.astar.AStarPathFinder.PathFindProgress.notrunning;
            return zombie.behaviors.Behavior.BehaviorResult.Succeeded;
        }
        if(this.tx != otx || this.ty != oty || tz != otz || sx != osx || sy != osy || sz != osz)
            if(this.tx == otx && this.ty == oty && tz == otz && path != null)
            {
                boolean bOnPath = false;
                n = 0;
                n = pathIndex - 1;
                do
                {
                    if(n >= path.getLength())
                        break;
                    if(n < 0)
                        n = 0;
                    int x = path.getX(n);
                    int y = path.getY(n);
                    int z = path.getZ(n);
                    if((int)character.getX() == sx && (int)character.getY() == sy && (int)character.getZ() == sz)
                    {
                        bOnPath = true;
                        break;
                    }
                    if(x == (int)character.getX() && y == (int)character.getY() && z == (int)character.getZ())
                    {
                        bOnPath = true;
                        break;
                    }
                    if(Math.abs(x - (int)character.getX()) <= 1 && Math.abs(y - (int)character.getY()) <= 1 && Math.abs(z - (int)character.getZ()) <= 1)
                    {
                        bOnPath = true;
                        break;
                    }
                    n++;
                } while(true);
                if(bOnPath)
                {
                    pathIndex = n;
                } else
                {
                    finder.progress = zombie.ai.astar.AStarPathFinder.PathFindProgress.notrunning;
                    path = null;
                }
            } else
            {
                finder.progress = zombie.ai.astar.AStarPathFinder.PathFindProgress.notrunning;
                path = null;
            }
        if((int)character.getX() == this.tx && (int)character.getY() == this.ty && (int)character.getZ() == tz && Math.abs(character.getX() - (float)this.tx - 0.5F) < 0.2F && Math.abs(character.getY() - (float)this.ty - 0.5F) < 0.2F)
        {
            reset();
            finder.progress = zombie.ai.astar.AStarPathFinder.PathFindProgress.notrunning;
            return zombie.behaviors.Behavior.BehaviorResult.Succeeded;
        }
        if(path == null)
        {
            if(finder.progress == zombie.ai.astar.AStarPathFinder.PathFindProgress.notyetfound)
                return zombie.behaviors.Behavior.BehaviorResult.Working;
            if(finder.progress == zombie.ai.astar.AStarPathFinder.PathFindProgress.failed)
            {
                reset();
                finder.progress = zombie.ai.astar.AStarPathFinder.PathFindProgress.notrunning;
                return zombie.behaviors.Behavior.BehaviorResult.Failed;
            }
            if(finder.progress == zombie.ai.astar.AStarPathFinder.PathFindProgress.notrunning)
            {
                if(sx != (int)character.getX())
                    sx = (int)character.getX();
                if(sy != (int)character.getY())
                    sy = (int)character.getY();
                if(sz != (int)character.getZ())
                    sz = (int)character.getZ();
                PathfindManager.instance.AddJob(this, character, sx, sy, sz, this.tx, this.ty, tz);
                osx = sx;
                osy = sy;
                osz = sz;
                otx = this.tx;
                oty = this.ty;
                otz = tz;
                finder.progress = zombie.ai.astar.AStarPathFinder.PathFindProgress.notyetfound;
                return zombie.behaviors.Behavior.BehaviorResult.Working;
            }
        }
        if(path == null)
        {
            reset();
            finder.progress = zombie.ai.astar.AStarPathFinder.PathFindProgress.notrunning;
            return zombie.behaviors.Behavior.BehaviorResult.Failed;
        }
        int ahead = pathIndex + 3;
        if(ahead > path.getLength())
            ahead = path.getLength();
        
label0:
        for(n = pathIndex + 1; n < ahead; n++)
        {
            int x = path.getX(n);
            int y = path.getY(n);
            int z = path.getZ(n);
            IsoGridSquare sq = character.getCell().getGridSquare(x, y, z);
            
            if(sq.getMovingObjects().isEmpty())
                continue;
            
            int q = 0;
            do
            {
                if(q >= sq.getMovingObjects().size())
                    continue label0;
                
                if(lastCancel > 120 && (sq.getMovingObjects().get(q) instanceof IsoZombie))
                {
                    path = null;
                    sx = (int)character.getX();
                    sy = (int)character.getY();
                    sz = (int)character.getZ();
                    finder.progress = zombie.ai.astar.AStarPathFinder.PathFindProgress.notrunning;
                    lastCancel = 0;
                    return zombie.behaviors.Behavior.BehaviorResult.Working;
                }
                q++;
            } while(true);
        }

        boolean bOnPath = false;
        n = 0;
        do
        {
            if(n >= path.getLength())
                break;
            int x = path.getX(n);
            int y = path.getY(n);
            int z = path.getZ(n);
            if((int)character.getX() == sx && (int)character.getY() == sy && (int)character.getZ() == sz)
            {
                bOnPath = true;
                break;
            }
            if(x == (int)character.getX() && y == (int)character.getY() && z == (int)character.getZ())
            {
                bOnPath = true;
                break;
            }
            if(Math.abs(x - (int)character.getX()) <= 1 && Math.abs(y - (int)character.getY()) <= 1 && Math.abs(z - (int)character.getZ()) <= 1)
            {
                bOnPath = true;
                break;
            }
            n++;
        } while(true);
        if(!bOnPath)
        {
            if(character == IsoCamera.CamCharacter)
                n = 0;
            reset();
            finder.progress = zombie.ai.astar.AStarPathFinder.PathFindProgress.notrunning;
            path = null;
            return zombie.behaviors.Behavior.BehaviorResult.Failed;
        }
        character.setPath(path);
        IsoGameCharacter owner = character;
        if(((IsoSurvivor)owner).isCollidedWithPushableThisFrame())
        {
            IsoPushableObject o = ((IsoSurvivor)owner).collidePushable;
            tempo.x = owner.getMovementLastFrame().x;
            tempo.y = owner.getMovementLastFrame().y;
            tempo.normalize();
            tempo.rotate((float)Math.toRadians(-90D));
            float xa = o.x + tempo.x * 5F;
            float ya = o.y + tempo.y * 5F;
            float xb = o.x - tempo.x * 5F;
            float yb = o.y - tempo.y * 5F;
            int ca = LosUtil.lineClearCollideCount(chr, IsoWorld.instance.CurrentCell, (int)o.x, (int)o.y, (int)o.z, (int)xa, (int)ya, (int)o.z);
            int cb = LosUtil.lineClearCollideCount(chr, IsoWorld.instance.CurrentCell, (int)o.x, (int)o.y, (int)o.z, (int)xb, (int)yb, (int)o.z);
            if(ca > cb)
            {
                o.setImpulsex(o.getImpulsex() + tempo.x * 0.1F);
                o.setImpulsey(o.getImpulsey() + tempo.y * 0.1F);
            } else
            if(ca < cb)
            {
                o.setImpulsex(o.getImpulsex() - tempo.x * 0.1F);
                o.setImpulsey(o.getImpulsey() - tempo.y * 0.1F);
            }
        }
        if(owner.isCollidedThisFrame())
        {
            if(Math.abs((float)this.tx - owner.getX()) < 1.0F && Math.abs((float)this.ty - owner.getY()) < 1.0F && Math.abs((float)tz - owner.getZ()) == 0.0F)
            {
                reset();
                finder.progress = zombie.ai.astar.AStarPathFinder.PathFindProgress.notrunning;
                return zombie.behaviors.Behavior.BehaviorResult.Succeeded;
            }
            if(owner.isCollidedE() || owner.isCollidedN() || owner.isCollidedS() || owner.isCollidedW())
            {
                if(owner == IsoCamera.CamCharacter)
                    n = 0;
                reset();
                finder.progress = zombie.ai.astar.AStarPathFinder.PathFindProgress.notrunning;
                return zombie.behaviors.Behavior.BehaviorResult.Failed;
            }
        }
        if(pathIndex >= path.getLength())
            return zombie.behaviors.Behavior.BehaviorResult.Succeeded;
        Vector2 pathTarget = new Vector2(this.tx, this.ty);
        float lx = path.getX(pathIndex);
        float ly = path.getY(pathIndex);
        float delta = 1.0F;
        float speed = owner.getPathSpeed();
        int ttx = path.getX(pathIndex);
        int tty = path.getY(pathIndex);
        int ttz = path.getZ(pathIndex);
        if((int)character.getX() == ttx && (int)character.getY() == tty && (int)character.getZ() == ttz)
        {
            float tx = (float)path.getX(pathIndex) + 0.5F;
            float ty = (float)path.getY(pathIndex) + 0.5F;
            pathTarget.x = tx;
            pathTarget.y = ty;
            if(GameTime.instance.getMultiplier() >= 10F && PathFindState.fastMoveTwo.Check() || IsoUtils.DistanceManhatten(character.getX(), character.getY(), tx, ty) < speed * GameTime.instance.getMultiplier() * 1.2F)
            {
                pathIndex++;
                if(pathIndex >= path.getLength())
                {
                    path = null;
                    reset();
                    finder.progress = zombie.ai.astar.AStarPathFinder.PathFindProgress.notrunning;
                    return zombie.behaviors.Behavior.BehaviorResult.Succeeded;
                }
                if(GameTime.instance.getMultiplier() >= 10F)
                {
                    owner.setX((float)path.getX(pathIndex) + 0.5F);
                    owner.setY((float)path.getY(pathIndex) + 0.5F);
                } else
                {
                    owner.setX((float)path.getX(pathIndex - 1) + 0.5F);
                    owner.setY((float)path.getY(pathIndex - 1) + 0.5F);
                }
                int nx = path.getX(pathIndex);
                int ny = path.getY(pathIndex);
                pathTarget.x = nx;
                pathTarget.y = ny;
                IsoGridSquare next = IsoWorld.instance.CurrentCell.getGridSquare(nx, ny, (int)character.getZ());
                if(next != null)
                {
                    IsoDoor door = character.getCurrentSquare().getDoorTo(next);
                    if(door != null)
                        door.ToggleDoor(character);
                    IsoWindow w = character.getCurrentSquare().getWindow(next);
                    if(w != null)
                        if(w.locked)
                            w.WeaponHit(owner, null);
                        else
                            w.ToggleWindow(character);
                }
            }
            IsoDirections dir = owner.dir;
            owner.FaceNextPathNode(path.getX(pathIndex), path.getY(pathIndex));

            if(IsoCamera.CamCharacter == owner && IsoDirections.reverse(dir) == owner.dir)
                n = 0;
            pathTarget.x -= character.getX();
            pathTarget.y -= character.getY();
            if(pathTarget.getLength() > 0.0F)
                pathTarget.normalize();
            if(GameTime.instance.getMultiplier() < 10F)
                owner.MoveForward(speed, pathTarget.x, pathTarget.y, delta);
        } else
        {
            owner.FaceNextPathNode(path.getX(pathIndex), path.getY(pathIndex));
            float tx = (float)path.getX(pathIndex) + 0.5F;
            float ty = (float)path.getY(pathIndex) + 0.5F;
            pathTarget.x = tx;
            pathTarget.y = ty;
            pathTarget.x -= character.getX();
            pathTarget.y -= character.getY();
            if(pathTarget.getLength() > 0.0F)
                pathTarget.normalize();
            owner.MoveForward(speed, pathTarget.x, pathTarget.y, delta);
        }
        if(useScriptXY)
        {
            owner.setScriptnx(owner.getNx());
            owner.setScriptny(owner.getNy());
        }
        return zombie.behaviors.Behavior.BehaviorResult.Working;
    }

    public void reset()
    {
        int n;
        if(chr != null && chr == IsoCamera.CamCharacter)
            n = 0;
        path = null;
        sx = 0;
        sy = 0;
        sz = 0;
        tx = 0;
        ty = 0;
        tz = 0;
        osx = 0;
        osy = 0;
        osz = 0;
        otx = 0;
        oty = 0;
        otz = 0;
        if(finder != null)
            finder.progress = zombie.ai.astar.AStarPathFinder.PathFindProgress.notrunning;
    }

    public boolean running(IsoGameCharacter character)
    {
        if(finder == null)
            return false;
        else
            return finder.progress != zombie.ai.astar.AStarPathFinder.PathFindProgress.notrunning;
    }

    public boolean valid()
    {
        return true;
    }

    public int renderDebug(int y)
    {
        int x = 300;
        int yb = 50;
        TextManager.instance.DrawString(UIFont.Small, x, yb, "Pathfind", 1.0F, 1.0F, 1.0F, 1.0F);
        yb += 30;
        if(path == null)
            return y;
        int lastx = -1000;
        int lasty = 0;
        int lastz = 0;
        for(int n = pathIndex; n < path.getLength(); n++)
        {
            Integer xx = Integer.valueOf(path.getX(n));
            Integer yy = Integer.valueOf(path.getY(n));
            Integer zz = Integer.valueOf(path.getZ(n));
            if(lastx != -1000)
            {
                xx = Integer.valueOf(xx.intValue() - lastx);
                yy = Integer.valueOf(yy.intValue() - lasty);
                zz = Integer.valueOf(zz.intValue() - lastz);
            }
            lastx = xx.intValue();
            lasty = yy.intValue();
            lastz = zz.intValue();
            TextManager.instance.DrawString(UIFont.Small, x, yb, (new StringBuilder()).append("PathNode ").append(n).append(" - x: ").append(xx).append(" y: ").append(yy).append(" z: ").append(zz).toString(), 0.0F, 1.0F, 1.0F, 0.4F);
            yb += 30;
        }

        return y;
    }

    public void Failed(Mover mover)
    {
        finder.progress = zombie.ai.astar.AStarPathFinder.PathFindProgress.failed;
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
        pathIndex = 0;
        finder.progress = zombie.ai.astar.AStarPathFinder.PathFindProgress.found;
    }

    public Path path;
    public int pathIndex;
    public int sx;
    public int sy;
    public int sz;
    public int tx;
    public int ty;
    public int tz;
    public int osx;
    public int osy;
    public int osz;
    public int otx;
    public int oty;
    public int otz;
    boolean useScriptXY;
    public AStarPathFinderResult finder;
    public String name;
    public int lastCancel;
    IsoGameCharacter chr;
    static Vector2 tempo = new Vector2(0.0F, 0.0F);

}
