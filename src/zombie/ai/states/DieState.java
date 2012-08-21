// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 13.08.2012 13:54:46
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   DieState.java

package zombie.ai.states;

import zombie.ai.State;
import zombie.ai.StateMachine;
import zombie.characters.BodyDamage.BodyDamage;
import zombie.characters.IsoGameCharacter;
import zombie.characters.IsoSurvivor;
import zombie.core.Collections.NulledArrayList;
import zombie.iso.IsoDirections;
import zombie.iso.objects.IsoDeadBody;
import zombie.iso.sprite.*;
import zombie.ui.TutorialManager;

// Referenced classes of package zombie.ai.states:
//            ReanimateState

public class DieState extends State
{

    public DieState()
    {
    }

    public static DieState instance()
    {
        return _instance;
    }

    public void enter(IsoGameCharacter owner)
    {
        owner.PlayAnim("ZombieDeath");
        owner.def.Frame = 0.0F;
        owner.setDefaultState(this);
        owner.getStateMachine().Lock = true;
        if((owner instanceof IsoSurvivor) && owner.getTimeSinceZombieAttack() < 10)
            ((IsoSurvivor)owner).ChewedByZombies();
    }

    public void execute(IsoGameCharacter owner)
    {
        if((int)owner.def.Frame == owner.sprite.CurrentAnim.Frames.size() - 1)
        {
            if(owner instanceof IsoSurvivor)
                ((IsoSurvivor)owner).SetAllFrames((short)(int)owner.def.Frame);
            if(owner == TutorialManager.instance.wife)
                owner.dir = IsoDirections.S;
            IsoDeadBody body = new IsoDeadBody(owner);
            if(owner.getBodyDamage().getInfectionLevel() > 60F)
                owner.getStateMachine().changeState(ReanimateState.instance());
        }
    }

    static DieState _instance = new DieState();

}