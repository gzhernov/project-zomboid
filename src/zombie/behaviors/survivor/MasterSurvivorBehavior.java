// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   MasterSurvivorBehavior.java

package zombie.behaviors.survivor;

import java.util.Stack;
import zombie.behaviors.Behavior;
import zombie.behaviors.DecisionPath;
import zombie.behaviors.survivor.orders.FollowOrder;
import zombie.behaviors.survivor.orders.Order;
import zombie.characters.IsoGameCharacter;
import zombie.characters.IsoSurvivor;
import zombie.core.Collections.NulledArrayList;
import zombie.iso.IsoCamera;
import zombie.iso.IsoGridSquare;

// Referenced classes of package zombie.behaviors.survivor:
//            FleeBehaviour, AttackBehavior, SatisfyIdleBehavior, ObeyOrders

public class MasterSurvivorBehavior extends Behavior
{

    public MasterSurvivorBehavior(IsoSurvivor survivor)
    {
        toProcess = null;
        sinceLastChanged = 0;
        sinceLastChangedMax = 120;
        timeTillProcessChange = 120;
        timeTillPathSpeedChange = 120;
        this.survivor = survivor;
        flee = new FleeBehaviour();
        attack = new AttackBehavior();
        idle = new SatisfyIdleBehavior();
        orders = new ObeyOrders(survivor);
    }

    public zombie.behaviors.Behavior.BehaviorResult process(DecisionPath path, IsoGameCharacter character)
    {
        character.setPath(null);
        int n;
        if(character == IsoCamera.CamCharacter)
            n = 0;
        if((toProcess instanceof ObeyOrders) && character.getDangerLevels() > 5F)
            timeTillProcessChange = -1;
        if(character.getPersonalNeed() != null && character.getPersonalNeed().isCritical())
        {
            if(!character.getPersonalNeed().bInit)
            {
                character.getPersonalNeed().initOrder();
                character.getPersonalNeed().bInit = true;
            }
            zombie.behaviors.Behavior.BehaviorResult res = character.getPersonalNeed().process();
            if(character.getPersonalNeed().complete())
            {
                character.getPersonalNeeds().pop();
                character.setPersonalNeed(null);
            } else
            {
                return res;
            }
        }
        flee.update();
        attack.update();
        idle.update();
        orders.update();
        timeTillProcessChange--;
        timeTillPathSpeedChange--;
        if(character.getStats().endurance < character.getStats().endurancewarn && character.getStats().endurancelast >= character.getStats().endurancewarn)
            timeTillPathSpeedChange = -1;
        if(character.getStats().endurance < character.getStats().endurancedanger && character.getStats().endurancelast >= character.getStats().endurancedanger)
            timeTillPathSpeedChange = -1;
        if(timeTillPathSpeedChange <= 0)
        {
            if(character == IsoCamera.CamCharacter)
                n = 0;
            timeTillPathSpeedChange = 100;
            if(toProcess != null)
                character.setPathSpeed(toProcess.getPathSpeed());
            else
                character.setPathSpeed(0.05F);
            if(character.getStats().endurance < character.getStats().endurancewarn)
            {
                character.setPathSpeed(0.05F);
                timeTillPathSpeedChange = 200;
            }
            if(character.getStats().endurance < character.getStats().endurancedanger)
            {
                character.setPathSpeed(0.04F);
                timeTillPathSpeedChange = 200;
            }
        }
        if(character.getPathSpeed() > 0.06F)
            character.getStats().endurance -= 0.005F;
        if(character.getPathSpeed() <= 0.06F)
            character.getStats().endurance += 0.0005F;
        if(character.getPathSpeed() <= 0.04F)
            character.getStats().endurance += 0.001F;
        if(attack == toProcess && ((IsoSurvivor)character).getVeryCloseEnemyList().size() > 3)
            timeTillProcessChange = -1;
        if(orders == toProcess && ((IsoSurvivor)character).getVeryCloseEnemyList().size() > 3)
            timeTillProcessChange = -1;
        if(timeTillProcessChange <= 0 || toProcess == null)
        {
            if(character == IsoCamera.CamCharacter)
                n = 0;
            float priorityToFollowOrder = -100000F;
            if(character.getOrder() != null)
                priorityToFollowOrder = character.getOrder().getPriority(character) * 5F;
            if(!((IsoSurvivor)character).getVeryCloseEnemyList().isEmpty())
            {
                timeTillProcessChange = -1;
                priorityToFollowOrder = -10000F;
            }
            float priorityToAttack = 0.0F;
            priorityToAttack = attack.getPriority(character);
            float priorityToFlee = flee.getPriority(character);
            float priorityToIdle = idle.getPriority(character);
            if(character.getOrder() instanceof FollowOrder)
                priorityToFlee = -1E+008F;
            if(character.getThreatLevel() > 0 && !character.getLocalRelevantEnemyList().isEmpty() && character.getCurrentSquare().getRoom() != null && priorityToAttack > 0.0F)
                priorityToAttack += 10000F;
            if(character.getThreatLevel() < 10 && character.getVeryCloseEnemyList().size() > 0 && priorityToAttack > 0.0F)
                priorityToAttack += 10000F;
            if(priorityToIdle > priorityToAttack && priorityToIdle > priorityToFollowOrder && priorityToIdle > priorityToFlee)
            {
                if(toProcess != idle)
                    idle.onSwitch();
                toProcess = idle;
                timeTillProcessChange = 90;
            } else
            {
                idle.reset();
            }
            if(priorityToAttack > priorityToFlee && priorityToAttack > priorityToIdle && priorityToAttack > priorityToFollowOrder)
            {
                if(toProcess != attack)
                    attack.onSwitch();
                toProcess = attack;
                timeTillProcessChange = 100;
            }
            if(priorityToFlee > priorityToAttack && priorityToFlee > priorityToIdle && priorityToFlee > priorityToFollowOrder)
            {
                if(toProcess != flee)
                    flee.onSwitch();
                toProcess = flee;
                timeTillProcessChange = 220;
            }
            if(priorityToFollowOrder > priorityToAttack && priorityToFollowOrder > priorityToIdle && priorityToFollowOrder > priorityToFlee)
            {
                if(toProcess != orders)
                    orders.onSwitch();
                toProcess = orders;
                timeTillProcessChange = 220;
            }
        }
        if(toProcess != null)
        {
            zombie.behaviors.Behavior.BehaviorResult res = toProcess.process(null, character);
            toProcess.last = res;
            return res;
        } else
        {
            return zombie.behaviors.Behavior.BehaviorResult.Succeeded;
        }
    }

    public void reset()
    {
    }

    public boolean valid()
    {
        return true;
    }

    public int renderDebug(int y)
    {
        int x = 50;
        y += 20;
        if(toProcess == null);
        return y;
    }

    IsoSurvivor survivor;
    public Behavior toProcess;
    int sinceLastChanged;
    int sinceLastChangedMax;
    FleeBehaviour flee;
    AttackBehavior attack;
    SatisfyIdleBehavior idle;
    ObeyOrders orders;
    int timeTillProcessChange;
    int timeTillPathSpeedChange;
    public static float FleeMultiplier = 0.05F;
    public static float AttackMultiplier = 15F;

}
