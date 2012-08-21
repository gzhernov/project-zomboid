// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 13.08.2012 13:54:46
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   AttackState.java

package zombie.ai.states;

import zombie.SoundManager;
import zombie.ai.State;
import zombie.ai.StateMachine;
import zombie.characters.BodyDamage.BodyDamage;
import zombie.characters.IsoGameCharacter;
import zombie.characters.IsoZombie;
import zombie.core.Rand;
import zombie.iso.IsoMovingObject;
import zombie.iso.Vector2;
import zombie.iso.sprite.IsoSpriteInstance;
import zombie.ui.TutorialManager;

// Referenced classes of package zombie.ai.states:
//            WanderState

public class AttackState extends State
{

    public AttackState()
    {
        temp = new Vector2();
    }

    public static AttackState instance()
    {
        return _instance;
    }

    public void enter(IsoGameCharacter owner)
    {
        if(owner instanceof IsoZombie)
        {
            IsoZombie zomb = (IsoZombie)owner;
            zomb.PlayAnim("ZombieBite");
            zomb.def.Frame = 0.0F;
        }
    }

    public void execute(IsoGameCharacter owner)
    {
        if(owner instanceof IsoZombie)
        {
            IsoZombie zomb = (IsoZombie)owner;
            if(zomb.target != null)
            {
                zomb.DirectionFromVector(zomb.vectorToTarget);
                if((int)zomb.getZ() != (int)zomb.target.getZ())
                {
                    zomb.target = null;
                    zomb.Wander();
                    return;
                }
            }
            float len = zomb.vectorToTarget.getLength();
            if(len > 1.0F)
            {
                zomb.Wander();
                zomb.Lunge();
                return;
            }
            if(zomb.target == null)
            {
                zomb.Wander();
                return;
            }
            zomb.target.setTimeSinceZombieAttack(0);
            zomb.target.setLastTargettedBy(zomb);
            zomb.AttackAnimTime--;
            zomb.PlayAnim("ZombieBite");
            ((IsoGameCharacter)zomb.target).setLeaveBodyTimedown(((IsoGameCharacter)zomb.target).getLeaveBodyTimedown() - 1);
            if(((IsoGameCharacter)zomb.target).getLeaveBodyTimedown() <= 0 && ((IsoGameCharacter)zomb.target).getBodyDamage().getHealth() <= 0.0F)
                zomb.getStateMachine().changeState(WanderState._instance);
            if(zomb.HurtPlayerTimer <= 0 && !TutorialManager.instance.ActiveControlZombies && zomb.def.Frame >= 10F && zomb.def.Frame <= 16F)
            {
                if(zomb.target != null)
                {
                    boolean bAlive = false;
                    if(((IsoGameCharacter)zomb.target).getBodyDamage().getHealth() > 0.0F)
                        bAlive = true;
                    ((IsoGameCharacter)zomb.target).getBodyDamage().AddRandomDamageFromZombie();
                    if(bAlive)
                    {
                        ((IsoGameCharacter)zomb.target).getBodyDamage().Update();
                        if(((IsoGameCharacter)zomb.target).getBodyDamage().getHealth() <= 0.0F)
                        {
                            SoundManager.instance.PlayWorldSound("zombieeating", zomb.target.getCurrentSquare(), 0.0F, 15F, 1.3F, true);
                            Integer a = Integer.valueOf(Rand.Next(3) + 1);
                            SoundManager.instance.PlayWorldSound((new StringBuilder()).append("scream").append(a).toString(), zomb.target.getCurrentSquare(), 0.3F, 10F, 0.5F, true);
                            ((IsoGameCharacter)zomb.target).setLeaveBodyTimedown(1500);
                        }
                    }
                    ((IsoGameCharacter)zomb.target).setSlowTimer(30);
                    ((IsoGameCharacter)zomb.target).setSlowFactor(((IsoGameCharacter)zomb.target).getSlowFactor() + 0.05F);
                    if(((IsoGameCharacter)zomb.target).getSlowFactor() >= 0.7F)
                        ((IsoGameCharacter)zomb.target).setSlowFactor(0.7F);
                }
                zomb.HurtPlayerTimer = 20;
            } else
            {
                zomb.HurtPlayerTimer--;
            }
        }
    }

    static AttackState _instance = new AttackState();
    Vector2 temp;

}