// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ConversationOrder.java

package zombie.behaviors.survivor.orders;

import gnu.trove.map.hash.THashMap;
import java.util.Stack;
import zombie.behaviors.Behavior;
import zombie.characters.*;
import zombie.scripting.ScriptManager;
import zombie.scripting.objects.Script;

// Referenced classes of package zombie.behaviors.survivor.orders:
//            Order

public class ConversationOrder extends Order
{

    public ConversationOrder(IsoGameCharacter chr, String script)
    {
        super(chr);
        this.chr = chr;
        scriptName = script;
    }

    public zombie.behaviors.Behavior.BehaviorResult process()
    {
        if(inst == null || inst.finished())
            return zombie.behaviors.Behavior.BehaviorResult.Succeeded;
        else
            return zombie.behaviors.Behavior.BehaviorResult.Working;
    }

    public void initOrder()
    {
        Stack available = ((IsoSurvivor)chr).getAvailableMembers();
        THashMap map = new THashMap();
        map.put("Leader", chr);
        for(int n = 0; n < available.size(); n++)
            map.put((new StringBuilder()).append("Member").append(n + 1).toString(), available.get(n));

        if(!IsoPlayer.isGhostMode());
        inst = ScriptManager.instance.PlayInstanceScript(null, scriptName, map);
    }

    public boolean complete()
    {
        return inst == null || inst.finished();
    }

    public void update()
    {
    }

    zombie.scripting.objects.Script.ScriptInstance inst;
    String scriptName;
    IsoGameCharacter chr;
}
