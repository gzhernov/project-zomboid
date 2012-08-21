// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 13.08.2012 13:54:46
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   ThumpState.java

package zombie.ai.states;

import zombie.SoundManager;
import zombie.ai.State;
import zombie.ai.StateMachine;
import zombie.characters.IsoGameCharacter;
import zombie.characters.IsoZombie;
import zombie.core.Collections.NulledArrayList;
import zombie.core.Rand;
import zombie.iso.IsoDirections;
import zombie.iso.IsoGridSquare;
import zombie.iso.objects.IsoDoor;
import zombie.iso.objects.interfaces.Thumpable;
import zombie.iso.sprite.IsoSpriteInstance;

// Referenced classes of package zombie.ai.states:
//            WanderState

public class ThumpState extends State
{

    public ThumpState()
    {
    }

    public static ThumpState instance()
    {
        return _instance;
    }

    public void execute(IsoGameCharacter owner)
    {
        Thumpable thump = owner.getThumpTarget();
        owner.PlayAnim("ZombieDoor");
        owner.def.setFrameSpeedPerFrame(0.15F);
        if(owner.getStateEventDelayTimer() <= 0.0F)
        {
            owner.setTimeThumping(owner.getTimeThumping() + 1);
            if(((IsoZombie)owner).TimeSinceSeenFlesh < 5)
                owner.setTimeThumping(0);
            if(owner.getTimeThumping() > owner.getPatience())
            {
                owner.getStateMachine().setCurrent(WanderState._instance);
                owner.dir = IsoDirections.reverse(owner.dir);
                return;
            }
            int count = 1;
            if(owner.getCurrentSquare() != null)
                count = owner.getCurrentSquare().getMovingObjects().size();
            for(int n = 0; n < count; n++)
            {
                if(owner.getThumpTarget() == null)
                {
                    owner.setDefaultState();
                    owner.setTimeThumping(0);
                    return;
                }
                owner.getThumpTarget().Thump(owner);
            }

            if(owner.getThumpTarget() instanceof IsoDoor)
                SoundManager.instance.PlayWorldSound("thumpa2", owner.getCurrentSquare(), 0.0F, 20F, 0.9F, true);
            else
            if(Rand.Next(3) == 0)
            {
                SoundManager.instance.PlayWorldSound("thumpa2", owner.getCurrentSquare(), 0.0F, 20F, 0.4F, true);
                SoundManager.instance.PlayWorldSound("thumpsqueak", owner.getCurrentSquare(), 0.5F, 20F, 1.4F, 6, true);
            } else
            {
                SoundManager.instance.PlayWorldSound("thumpsqueak", owner.getCurrentSquare(), 0.5F, 20F, 1.4F, 6, true);
            }
            owner.setStateEventDelayTimer((30 + Rand.Next(4)) * 3);
        }
        if(thump == null || thump.isDestroyed() || (thump instanceof IsoDoor) && ((IsoDoor)thump).open)
        {
            owner.setThumpTarget(null);
            if(owner instanceof IsoZombie)
            {
                IsoZombie z = (IsoZombie)owner;
                if(z.LastTargetSeenX != -1)
                {
                    owner.PathTo(z.LastTargetSeenX, z.LastTargetSeenY, z.LastTargetSeenZ, false);
                    owner.setTimeThumping(0);
                    return;
                }
            }
            owner.setDefaultState();
        }
    }

    void calculate()
    {
        SoundManager.instance.update2();
    }

    static ThumpState _instance = new ThumpState();

}