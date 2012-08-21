// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 13.08.2012 13:54:46
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   StaggerBackState.java

package zombie.ai.states;

import zombie.ai.State;
import zombie.ai.StateMachine;
import zombie.characters.IsoGameCharacter;
import zombie.characters.IsoZombie;
import zombie.iso.IsoDirections;
import zombie.iso.Vector2;

// Referenced classes of package zombie.ai.states:
//            LungeState

public class StaggerBackState extends State
{

    public StaggerBackState()
    {
        dirThisFrame = new Vector2();
    }

    public static StaggerBackState instance()
    {
        return _instance;
    }

    public void enter(IsoGameCharacter owner)
    {
        if(owner instanceof IsoZombie)
            owner.PlayAnim("ZombieStaggerBack");
        owner.setStateEventDelayTimer(30F * owner.getHitForce() * owner.getStaggerTimeMod());
        owner.getHitDir().x *= owner.getHitForce();
        owner.getHitDir().y *= owner.getHitForce();
        owner.getHitDir().x *= 0.08F;
        owner.getHitDir().y *= 0.08F;
        if(owner.getHitDir().getLength() > 0.08F)
            owner.getHitDir().setLength(0.08F);
        dirThisFrame.x = owner.getHitDir().x;
        dirThisFrame.y = owner.getHitDir().y;
        dirThisFrame.normalize();
        owner.setDir(IsoDirections.reverse(IsoDirections.fromAngle(dirThisFrame)));
        owner.setIgnoreMovementForDirection(true);
    }

    public void execute(IsoGameCharacter owner)
    {
        dirThisFrame.x = owner.getHitDir().x;
        dirThisFrame.y = owner.getHitDir().y;
        float delta = owner.getStateEventDelayTimer() / (30F * owner.getHitForce() * owner.getStaggerTimeMod());
        dirThisFrame.x *= delta;
        dirThisFrame.y *= delta;
        owner.Move(dirThisFrame);
        if(owner instanceof IsoZombie)
        {
            if(owner.getStateEventDelayTimer() <= 0.0F)
            {
                owner.getStateMachine().changeState(LungeState.instance());
                owner.setStateEventDelayTimer(40F);
            }
        } else
        if(owner.getStateEventDelayTimer() <= 0.0F)
            owner.getStateMachine().changeState(owner.getDefaultState());
    }

    public void exit(IsoGameCharacter owner)
    {
        owner.setIgnoreMovementForDirection(false);
    }

    static StaggerBackState _instance = new StaggerBackState();
    Vector2 dirThisFrame;

}