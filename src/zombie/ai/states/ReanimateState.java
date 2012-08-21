// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 13.08.2012 13:54:46
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   ReanimateState.java

package zombie.ai.states;

import java.io.File;
import zombie.GameWindow;
import zombie.ai.State;
import zombie.ai.StateMachine;
import zombie.characters.*;
import zombie.core.Collections.NulledArrayList;
import zombie.core.Rand;
import zombie.iso.*;
import zombie.iso.sprite.*;

// Referenced classes of package zombie.ai.states:
//            WanderState

public class ReanimateState extends State
{

    public ReanimateState()
    {
        AnimDelayRate = 10;
    }

    public static ReanimateState instance()
    {
        return _instance;
    }

    public void enter(IsoGameCharacter owner)
    {
        owner.PlayAnim("ZombieDeath");
        owner.def.Frame = 0.0F;
        owner.def.Looped = false;
        owner.setDefaultState(this);
        owner.getStateMachine().Lock = true;
        owner.setReanimPhase(0);
        owner.setReanimateTimer(Rand.Next(250) + 150);
        if(Rand.Next(4) == 0)
            owner.setReanimateTimer(owner.getReanimateTimer() * 3);
        owner.setCollidable(false);
        if(owner instanceof IsoPlayer)
        {
            File file = new File((new StringBuilder()).append(GameWindow.getGameModeCacheDir()).append(File.separator).append("map_p.bin").toString());
            if(file.exists())
                file.delete();
        }
        owner.setReanimAnimFrame(3);
        owner.setReanimAnimDelay(AnimDelayRate);
    }

    public void execute(IsoGameCharacter owner)
    {
        if(owner.getReanimPhase() == 0 && (int)owner.def.Frame == owner.sprite.CurrentAnim.Frames.size() - 1)
        {
            owner.setReanimPhase(1);
            owner.sprite.Animate = false;
            owner.setCollidable(false);
        }
        if(owner.getReanimPhase() == 1)
        {
            owner.setReanimateTimer(owner.getReanimateTimer() - 1);
            if(owner.getReanimateTimer() == 0)
            {
                owner.getCurrentSquare().getCell().getRemoveList().add(owner);
                owner.getCurrentSquare().getMovingObjects().remove(owner);
                IsoZombie zombie = new IsoZombie(owner.getCell(), owner.getDescriptor());
                zombie.setCurrent(owner.getCurrentSquare());
                zombie.getCurrentSquare().getMovingObjects().add(zombie);
                zombie.setX(owner.getX());
                zombie.setY(owner.getY());
                zombie.setZ(owner.getZ());
                zombie.setInventory(owner.getInventory());
                owner.getCell().getZombieList().add(zombie);
                zombie.setDir(owner.getDir());
                zombie.setPathSpeed(zombie.getPathSpeed() * 1.2F);
                zombie.wanderSpeed = zombie.getPathSpeed() * 0.5F * zombie.getSpeedMod();
                zombie.setHealth(zombie.getHealth() * 5F);
                zombie.PlayAnim("ZombieDeath");
                zombie.def.Frame = zombie.sprite.CurrentAnim.Frames.size() - 1;
                zombie.def.Looped = false;
                if(IsoCamera.CamCharacter == owner)
                    IsoCamera.SetCharacterToFollow(zombie);
                zombie.def.Finished = false;
                zombie.PlayAnimUnlooped("ZombieGetUp");
                zombie.def.setFrameSpeedPerFrame(0.2F);
                zombie.getStateMachine().setCurrent(this);
                zombie.setReanimPhase(2);
                zombie.setVisibleToNPCs(false);
                zombie.setShootable(false);
                zombie.getStateMachine().Lock = true;
            }
        }
        if(owner.getReanimPhase() == 2)
        {
            owner.setVisibleToNPCs(false);
            owner.setShootable(false);
            if((int)owner.def.Frame >= owner.sprite.CurrentAnim.Frames.size() - 2)
            {
                owner.getStateMachine().Lock = false;
                owner.setReanimPhase(3);
                owner.setVisibleToNPCs(true);
                owner.setShootable(true);
            }
        }
        if(owner.getReanimPhase() == 3)
        {
            owner.getStateMachine().Lock = false;
            owner.getStateMachine().setCurrent(WanderState._instance);
        }
    }

    static ReanimateState _instance = new ReanimateState();
    int AnimDelayRate;

}