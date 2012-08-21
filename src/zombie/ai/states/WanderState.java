// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 13.08.2012 13:54:46
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   WanderState.java

package zombie.ai.states;

import zombie.GameTime;
import zombie.SoundManager;
import zombie.ai.State;
import zombie.characters.IsoGameCharacter;
import zombie.characters.IsoZombie;
import zombie.core.Rand;
import zombie.core.utils.OnceEvery;
import zombie.iso.*;

// Referenced classes of package zombie.ai.states:
//            PathFindState

public class WanderState extends State
{

    public WanderState()
    {
    }

    public static WanderState instance()
    {
        return _instance;
    }

    public void enter(IsoGameCharacter owner)
    {
        chooseNewDirection(owner);
    }

    public void chooseNewDirection(IsoGameCharacter owner)
    {
        int distTilHit = 0;
        int timeout = 0;
        if(owner.getCurrentSquare() == null)
            owner.ensureOnTile();
        do
        {
            timeout++;
            owner.setNextWander(300);
            owner.setPathTargetX(Rand.Next(IsoWorld.instance.CurrentCell.getWidthInTiles()));
            owner.setPathTargetY(Rand.Next(IsoWorld.instance.CurrentCell.getHeightInTiles()));
            distTilHit = LosUtil.lineClearCollideCount(owner, owner.getCell(), owner.getPathTargetX(), owner.getPathTargetY(), (int)owner.getZ(), (int)owner.getX(), (int)owner.getY(), (int)owner.getZ(), 1);
            vec.x = owner.getPathTargetX();
            vec.y = owner.getPathTargetY();
            vec.x -= owner.x;
            vec.y -= owner.y;
            vec.setLength(((IsoZombie)owner).wanderSpeed);
            owner.reqMovement.x = vec.x;
            owner.reqMovement.y = vec.y;
            owner.DirectionFromVector(vec);
        } while(distTilHit < 1 && timeout < 100);
    }

    public void execute(IsoGameCharacter owner)
    {
        IsoZombie z = (IsoZombie)owner;
        if(Rand.Next(200) == 0 || owner.isCollidedThisFrame() || Math.abs((float)owner.getPathTargetX() - owner.getX()) <= 1.0F && Math.abs((float)owner.getPathTargetY() - owner.getY()) <= 1.0F)
        {
            chooseNewDirection(owner);
            vec.x = owner.getPathTargetX();
            vec.y = owner.getPathTargetY();
            vec.x -= owner.x;
            vec.y -= owner.y;
            vec.setLength(((IsoZombie)owner).wanderSpeed);
            owner.DirectionFromVector(vec);
            if(GameTime.instance.getMultiplier() < 8F)
                owner.Move(vec);
            else
            if(PathFindState.fastMove.Check())
            {
                IsoGridSquare feeler = owner.getFeelerTile(1.0F);
                if(feeler != null)
                {
                    if(!owner.getCurrentSquare().testCollideAdjacent(owner, feeler.getX() - owner.getCurrentSquare().getX(), feeler.getY() - owner.getCurrentSquare().getY(), 0))
                    {
                        owner.setX((float)feeler.getX() + 0.5F);
                        owner.setY((float)feeler.getY() + 0.5F);
                    } else
                    {
                        chooseNewDirection(owner);
                    }
                } else
                {
                    chooseNewDirection(owner);
                }
            }
        } else
        {
            owner.Move(owner.reqMovement);
        }
    }

    void calculate()
    {
        SoundManager.instance.update1();
    }

    static WanderState _instance = new WanderState();
    static Vector2 vec = new Vector2();

}