// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 13.08.2012 13:54:46
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   LungeState.java

package zombie.ai.states;

import zombie.ai.State;
import zombie.characters.IsoGameCharacter;
import zombie.characters.IsoZombie;
import zombie.iso.IsoMovingObject;
import zombie.iso.Vector2;

public class LungeState extends State
{

    public LungeState()
    {
        temp = new Vector2();
    }

    public static LungeState instance()
    {
        return _instance;
    }

    public void execute(IsoGameCharacter owner)
    {
        if(owner instanceof IsoZombie)
        {
            IsoZombie zomb = (IsoZombie)owner;
            float twidth = 0.0F;
            if(zomb.target != null)
                twidth = zomb.getWidth() + zomb.target.getWidth();
            else
                twidth = zomb.getWidth() * 2.0F;
            if(zomb.vectorToTarget.getLength() - twidth >= 0.5F && zomb.LungeTimer < 0.0F)
            {
                zomb.Wander();
                return;
            }
            float len = zomb.vectorToTarget.getLength();
            if(len < 0.6F)
            {
                zomb.AttemptAttack();
                return;
            }
            if(zomb.bLunger)
                zomb.walkVariantUse = "ZombieWalk3";
            zomb.LungeTimer--;
            float del = zomb.LungeTimer / 180F;
            float speed = zomb.getPathSpeed() + 0.012F * del;
            zomb.vectorToTarget.normalize();
            temp.x = zomb.vectorToTarget.x;
            temp.y = zomb.vectorToTarget.y;
            temp.setLength(speed * zomb.getSpeedMod());
            zomb.Move(temp);
            zomb.updateFrameSpeed();
            zomb.DirectionFromVector(temp);
        }
    }

    static LungeState _instance = new LungeState();
    Vector2 temp;

}