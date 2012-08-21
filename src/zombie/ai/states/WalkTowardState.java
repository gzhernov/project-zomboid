// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 13.08.2012 13:54:46
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   WalkTowardState.java

package zombie.ai.states;

import zombie.ai.State;
import zombie.ai.StateMachine;
import zombie.characters.IsoGameCharacter;
import zombie.characters.IsoZombie;
import zombie.iso.*;

// Referenced classes of package zombie.ai.states:
//            WanderState, PathFindState

public class WalkTowardState extends State
{

    public WalkTowardState()
    {
        temp = new Vector2();
    }

    public static WalkTowardState instance()
    {
        return _instance;
    }

    public void execute(IsoGameCharacter owner)
    {
        if(owner instanceof IsoZombie)
        {
            IsoZombie zomb = (IsoZombie)owner;
            if(zomb.target != null)
            {
                zomb.setPathTargetX((int)zomb.target.getX());
                zomb.setPathTargetY((int)zomb.target.getY());
                zomb.setPathTargetZ((int)zomb.target.getZ());
            }
            if(zomb.getPathTargetX() == (int)zomb.getX() && zomb.getPathTargetY() == (int)zomb.getY() && zomb.getPathTargetZ() == (int)zomb.getZ())
                zomb.getStateMachine().changeState(WanderState.instance());
            zomb.LungeTimer--;
            if(zomb.isCollidedThisFrame())
            {
                if(IsoCamera.CamCharacter instanceof IsoZombie)
                    if(((IsoZombie)IsoCamera.CamCharacter).getStateMachine().getCurrent() == PathFindState._instance);
                zomb.getStateMachine().changeState(PathFindState._instance);
                return;
            }
            float speed = zomb.getPathSpeed();
            temp.x = (float)zomb.getPathTargetX() + 0.5F;
            temp.y = (float)zomb.getPathTargetY() + 0.5F;
            temp.x -= zomb.getX();
            temp.y -= zomb.getY();
            temp.normalize();
            zomb.reqMovement.normalize();
            if(Math.abs(temp.x - zomb.reqMovement.x) > 0.0001F || Math.abs(temp.y - zomb.reqMovement.y) > 0.0001F)
                zomb.DirectionFromVector(temp);
            temp.setLength(speed * zomb.getSpeedMod() + 0.006F);
            zomb.Move(temp);
            zomb.updateFrameSpeed();
            zomb.DirectionFromVector(temp);
        }
    }

    static WalkTowardState _instance = new WalkTowardState();
    Vector2 temp;

}