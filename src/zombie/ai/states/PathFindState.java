// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 13.08.2012 13:54:46
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   PathFindState.java

package zombie.ai.states;

import gnu.trove.set.hash.THashSet;
import java.util.Stack;
import zombie.GameTime;
import zombie.PathfindManager;
import zombie.ai.State;
import zombie.ai.StateMachine;
import zombie.ai.astar.*;
import zombie.characters.IsoGameCharacter;
import zombie.characters.IsoZombie;
import zombie.core.utils.OnceEvery;
import zombie.iso.*;
import zombie.iso.sprite.IsoSprite;

// Referenced classes of package zombie.ai.states:
//            ThumpState, LuaState, ExploreState, WanderState

public class PathFindState extends State
    implements IPathfinder
{

    public PathFindState()
    {
    }

    public static PathFindState instance()
    {
        return _instance;
    }

    public void enter(IsoGameCharacter owner)
    {
        owner.setPath(null);
        PathfindManager.instance.AddJob(this, owner, (int)owner.getX(), (int)owner.getY(), (int)owner.getZ(), owner.getPathTargetX(), owner.getPathTargetY(), owner.getPathTargetZ());
        owner.getFinder().progress = zombie.ai.astar.AStarPathFinder.PathFindProgress.notyetfound;
        owner.setPathIndex(0);
    }

    public void execute(IsoGameCharacter owner)
    {
        int n;
        if(IsoCamera.CamCharacter == owner)
            n = 0;
        if(owner.getFinder().progress == zombie.ai.astar.AStarPathFinder.PathFindProgress.notyetfound)
            return;
        if(owner.getFinder().progress == zombie.ai.astar.AStarPathFinder.PathFindProgress.failed)
        {
            owner.setPathFindIndex(-1);
            Finished(owner);
            return;
        }
        if(owner.getFinder().progress == zombie.ai.astar.AStarPathFinder.PathFindProgress.found)
        {
            Path path = owner.getPath();
            if(path == null)
            {
                owner.getStateMachine().RevertToPrevious();
                return;
            }
            float x = owner.getX();
            float y = owner.getY();
            float z = owner.getZ();
            if(owner.getPathIndex() >= path.getLength())
            {
                path = null;
                Finished(owner);
                return;
            }
            int lx = path.getX(owner.getPathIndex());
            int ly = path.getY(owner.getPathIndex());
            int lz = path.getZ(owner.getPathIndex());
            float delta = 1.0F;
            float speed = owner.getPathSpeed();
            int pathIndex = owner.getPathIndex();
            if(path != null)
            {
                int ttx = lx;
                int tty = ly;
                int ttz = lz;
                if((int)x == ttx && (int)y == tty && (int)z == ttz)
                {
                    float tx = (float)path.getX(pathIndex) + 0.5F;
                    float ty = (float)path.getY(pathIndex) + 0.5F;
                    pathTarget.x = tx;
                    pathTarget.y = ty;
                    if(IsoUtils.DistanceManhatten(x, y, tx, ty) < speed * 2.2F || GameTime.instance.getMultiplier() >= 30F && fastMove.Check())
                    {
                        owner.setPathIndex(owner.getPathIndex() + 1);
                        if(owner.getPathIndex() >= path.getLength())
                        {
                            path = null;
                            Finished(owner);
                            return;
                        }
                        if(GameTime.instance.getMultiplier() >= 10F)
                        {
                            owner.setX((float)path.getX(owner.getPathIndex()) + 0.5F);
                            owner.setY((float)path.getY(owner.getPathIndex()) + 0.5F);
                        } else
                        {
                            owner.setX((float)lx + 0.5F);
                            owner.setY((float)ly + 0.5F);
                        }
                    }
                    if(THashSet.Waddo)
                    {
                        ThumpState._instance.calculate();
                        LuaState._instance.calculate();
                        ExploreState._instance.calculate();
                        WanderState._instance.calculate();
                    }
                    pathTarget.x -= x;
                    pathTarget.y -= y;
                    if(pathTarget.getLength() > 0.0F)
                        pathTarget.normalize();
                    owner.DirectionFromVector(pathTarget);
                    if(GameTime.instance.getMultiplier() < 10F)
                        owner.MoveForward(speed, pathTarget.x, pathTarget.y, delta);
                    ((IsoZombie)owner).updateFrameSpeed();
                    owner.sprite.PlayAnim("walk");
                } else
                {
                    float tx = (float)lx + 0.5F;
                    float ty = (float)ly + 0.5F;
                    pathTarget.x = tx;
                    pathTarget.y = ty;
                    pathTarget.x -= x;
                    pathTarget.y -= y;
                    if(pathTarget.getLength() > 0.0F)
                        pathTarget.normalize();
                    owner.DirectionFromVector(pathTarget);
                    if(GameTime.instance.getMultiplier() < 10F)
                        owner.MoveForward(speed, pathTarget.x, pathTarget.y, delta);
                    owner.sprite.PlayAnim("walk");
                }
            }
        }
    }

    public void exit(IsoGameCharacter isogamecharacter)
    {
    }

    private void Finished(IsoGameCharacter owner)
    {
        owner.pathFinished();
    }

    public void Failed(Mover mover)
    {
        IsoGameCharacter owner = (IsoGameCharacter)mover;
        owner.getFinder().progress = zombie.ai.astar.AStarPathFinder.PathFindProgress.failed;
    }

    public void Succeeded(Path path, Mover mover)
    {
        IsoGameCharacter owner = (IsoGameCharacter)mover;
        owner.setPathIndex(0);
        Path p = owner.getPath();
        if(p != null)
        {
            for(int n = 0; n < p.getLength(); n++)
                Path.stepstore.push(p.getStep(n));

        }
        owner.setPath(path);
        owner.getFinder().progress = zombie.ai.astar.AStarPathFinder.PathFindProgress.found;
    }

    public String getName()
    {
        return "ZombiePathfinding";
    }

    static PathFindState _instance = new PathFindState();
    static Vector2 pathTarget = new Vector2(0.0F, 0.0F);
    public static OnceEvery fastMove = new OnceEvery(0.2F);
    public static OnceEvery fastMoveTwo = new OnceEvery(0.2F);

}