// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 13.08.2012 13:54:46
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   StaggerBackDieState.java

package zombie.ai.states;

import zombie.SoundManager;
import zombie.ai.State;
import zombie.ai.StateMachine;
import zombie.characters.IsoGameCharacter;
import zombie.characters.IsoZombie;
import zombie.core.Collections.NulledArrayList;
import zombie.core.Rand;
import zombie.iso.*;
import zombie.iso.objects.IsoDeadBody;
import zombie.iso.sprite.*;
import zombie.ui.TutorialManager;

public class StaggerBackDieState extends State
{

    public StaggerBackDieState()
    {
        dirThisFrame = new Vector2();
        AnimDelayRate = 10;
    }

    public static StaggerBackDieState instance()
    {
        return _instance;
    }

    public void enter(IsoGameCharacter owner)
    {
        int n;
        if(owner instanceof IsoZombie)
        {
            IsoZombie.ZombieDeaths++;
            owner.PlayAnim("ZombieStaggerBack");
            owner.def.setFrameSpeedPerFrame(0.3F);
        } else
        {
            n = 0;
        }
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
        owner.getStateMachine().Lock = true;
        owner.setReanimPhase(0);
        if(owner instanceof IsoZombie)
            owner.setReanimateTimer(Rand.Next(30) + 4);
        if(Rand.Next(5) == 0)
            owner.setReanimateTimer(Rand.Next(30) + 30);
        if(owner instanceof IsoZombie)
        {
            owner.setReanimAnimFrame(3);
            owner.setReanimAnimDelay(AnimDelayRate);
        }
        if(owner.getHealth() > 0.0F)
        {
            owner.setReanim(true);
            owner.setDieCount(owner.getDieCount() + 1);
        } else
        if(owner.getHealth() <= 0.0F)
            SoundManager.instance.PlayWorldSound("splat", owner.getCurrentSquare(), 0.4F, 10F, 0.8F, 2, false);
    }

    public void execute(IsoGameCharacter owner)
    {
        int n;
        if(!(owner instanceof IsoZombie))
            n = 0;
        if(!owner.isIgnoreStaggerBack() && owner.getReanimPhase() == 0)
        {
            dirThisFrame.x = owner.getHitDir().x;
            dirThisFrame.y = owner.getHitDir().y;
            owner.setShootable(false);
            float delta = owner.getStateEventDelayTimer() / (30F * owner.getHitForce() * owner.getStaggerTimeMod());
            if(delta < 0.0F)
                delta = 0.0F;
            if(delta > 1.0F)
                delta = 1.0F;
            dirThisFrame.x *= delta;
            dirThisFrame.y *= delta;
            if(!owner.isIgnoreStaggerBack())
                owner.Move(dirThisFrame);
            if(owner.getHitBy() != null)
                owner.getHitBy().getStats().stress -= 0.0016F;
        }
        if(owner.isIgnoreStaggerBack() || owner.getStateEventDelayTimer() <= 30F * owner.getHitForce() * owner.getStaggerTimeMod() && !owner.sprite.CurrentAnim.name.equals("ZombieDeath") && !owner.sprite.CurrentAnim.name.equals("ZombieGetUp") && !owner.sprite.CurrentAnim.name.equals("Die"))
        {
            owner.PlayAnimUnlooped("ZombieDeath");
            owner.setVisibleToNPCs(false);
            owner.setShootable(false);
        }
        if((owner.sprite.CurrentAnim.name.equals("ZombieGetUp") || owner.sprite.CurrentAnim.name.equals("ZombieDeath") || owner.sprite.CurrentAnim.name.equals("Die")) && (int)owner.def.Frame == owner.sprite.CurrentAnim.Frames.size() - 1)
        {
            if(owner.getReanimPhase() == 0)
            {
                owner.setReanimPhase(1);
                owner.def.Finished = true;
            }
            if(owner.isReanim())
            {
                if(owner.getReanimPhase() == 1)
                {
                    if(owner.getHealth() <= 0.0F)
                        owner.setHealth(0.5F);
                    owner.setReanimateTimer(owner.getReanimateTimer() - 1);
                    if(owner.getReanimateTimer() == 0)
                    {
                        owner.setReanimPhase(2);
                        owner.def.Frame = 0.0F;
                        owner.def.Finished = false;
                        owner.PlayAnimUnlooped("ZombieGetUp");
                        owner.def.setFrameSpeedPerFrame(0.2F);
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
                    owner.def.setFrameSpeedPerFrame(0.23F);
                    owner.def.AnimFrameIncrease *= ((IsoZombie)owner).getSpeedMod();
                    owner.PlayAnim(((IsoZombie)owner).walkVariantUse);
                    owner.sprite.Animate = true;
                    owner.getStateMachine().RevertToPrevious();
                    owner.setReanim(false);
                    owner.setShootable(true);
                }
            } else
            {
                if(!(owner instanceof IsoZombie))
                    n = 0;
                if(owner instanceof IsoZombie)
                    IsoWorld.instance.MetaCellGrid[IsoWorld.instance.x][IsoWorld.instance.y].zombieCount--;
                if(owner == TutorialManager.instance.wife)
                    owner.dir = IsoDirections.S;
                if(owner instanceof IsoZombie)
                    IsoWorld.instance.MetaCellGrid[IsoWorld.instance.x][IsoWorld.instance.y].zombieCount--;
                IsoDeadBody body = new IsoDeadBody(owner);
                if(owner.getAttackedBy() != null)
                    owner.getAttackedBy().setZombieKills(owner.getAttackedBy().getZombieKills() + 1);
            }
        }
    }

    public void exit(IsoGameCharacter owner)
    {
        owner.setIgnoreMovementForDirection(false);
    }

    static StaggerBackDieState _instance = new StaggerBackDieState();
    Vector2 dirThisFrame;
    int AnimDelayRate;

}