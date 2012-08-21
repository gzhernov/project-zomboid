// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   SurvivorGroup.java

package zombie.characters;

import java.util.Stack;
import zombie.behaviors.survivor.orders.Needs.Need;
import zombie.core.Collections.NulledArrayList;
import zombie.core.Rand;
import zombie.iso.areas.IsoBuilding;

// Referenced classes of package zombie.characters:
//            SurvivorDesc, IsoGameCharacter

public class SurvivorGroup
{

    public SurvivorGroup(SurvivorDesc leader)
    {
        Safehouse = null;
        Members = new NulledArrayList();
        Leader = null;
        GroupNeeds = new Stack();
        Leader = leader;
        AddMember(leader);
    }

    public void AddMember(SurvivorDesc member)
    {
        if(member == null)
            return;
        member.Group = this;
        if(!Members.contains(member))
            Members.add(member);
        if(Leader == null || Leader.Group != this)
            Leader = member;
    }

    public boolean IsMember(SurvivorDesc desc)
    {
        return Members.contains(desc);
    }

    public boolean IsMember(IsoGameCharacter chr)
    {
        if(chr.descriptor == null)
            return false;
        else
            return Members.contains(chr.descriptor);
    }

    public boolean IsLeader(SurvivorDesc desc)
    {
        return Leader == desc;
    }

    public boolean IsLeader(IsoGameCharacter chr)
    {
        if(chr.descriptor == null)
            return false;
        else
            return Leader == chr.descriptor;
    }

    public void update()
    {
        for(int n = 0; n < Members.size(); n++)
            if(((SurvivorDesc)Members.get(n)).Group != this)
            {
                Members.remove(n);
                n--;
            }

        if(Leader == null || Leader.Group != this || Leader.Instance != null && Leader.Instance.isDead())
            PickNewLeader();
        if(!Members.contains(Leader) && Leader != null)
            Members.add(Leader);
    }

    private void PickNewLeader()
    {
        if(Members.isEmpty())
        {
            return;
        } else
        {
            Leader = (SurvivorDesc)Members.get(Rand.Next(Members.size()));
            return;
        }
    }

    IsoGameCharacter getRandomMemberExcept(IsoGameCharacter instance)
    {
        if(Members.size() == 1)
            return null;
        IsoGameCharacter chr = null;
        do
            chr = ((SurvivorDesc)Members.get(Rand.Next(Members.size()))).Instance;
        while(chr == instance);
        return chr;
    }

    public boolean HasOtherMembers(SurvivorDesc descriptor)
    {
        return Members.contains(descriptor) && Members.size() > 1;
    }

    public int getTotalNeedPriority()
    {
        int tot = 0;
        for(int n = 0; n < GroupNeeds.size(); n++)
            tot += ((Need)GroupNeeds.get(n)).priority;

        return tot;
    }

    public void AddNeed(String type, int priority)
    {
        for(int n = 0; n < GroupNeeds.size(); n++)
            if(((Need)GroupNeeds.get(n)).item.equals(type))
            {
                ((Need)GroupNeeds.get(n)).numToSatisfy++;
                if(((Need)GroupNeeds.get(n)).priority < priority)
                    ((Need)GroupNeeds.get(n)).priority = priority;
                return;
            }

        GroupNeeds.add(new Need(type, priority));
    }

    public boolean HasNeed(String type)
    {
        for(int n = 0; n < GroupNeeds.size(); n++)
            if(((Need)GroupNeeds.get(n)).item.equals(type))
                return true;

        return false;
    }

    public IsoBuilding Safehouse;
    public NulledArrayList Members;
    public SurvivorDesc Leader;
    public Stack GroupNeeds;
}
