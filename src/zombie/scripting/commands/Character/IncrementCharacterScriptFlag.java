// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   IncrementCharacterScriptFlag.java

package zombie.scripting.commands.Character;

import gnu.trove.map.hash.THashMap;
import zombie.characters.IsoGameCharacter;
import zombie.characters.SurvivorDesc;
import zombie.scripting.commands.BaseCommand;
import zombie.scripting.objects.*;

public class IncrementCharacterScriptFlag extends BaseCommand
{

    public IncrementCharacterScriptFlag()
    {
        modifier = 0;
        Other = "";
        invert = false;
    }

    public boolean IsFinished()
    {
        return true;
    }

    public void update()
    {
    }

    public void init(String object, String params[])
    {
        if(object.indexOf("!") == 0)
        {
            invert = true;
            object = object.substring(1);
        }
        owner = object;
        Other = params[0].trim();
        name = params[1].trim().replace("\"", "");
    }

    public boolean getValue()
    {
        return true;
    }

    public void begin()
    {
        if(currentinstance != null && currentinstance.HasAlias(owner))
        {
            chr = currentinstance.getAlias(owner);
        } else
        {
            if(module.getCharacter(owner) == null)
                return;
            if(module.getCharacter(owner).Actual == null)
                return;
            chr = module.getCharacter(owner).Actual;
        }
        IsoGameCharacter other;
        if(currentinstance != null && currentinstance.HasAlias(Other))
            other = currentinstance.getAlias(Other);
        else
        if(module.getCharacter(Other) == null)
            other = null;
        else
        if(module.getCharacter(Other).Actual == null)
            other = null;
        else
            other = module.getCharacter(Other).Actual;
        if(chr == null)
            return;
        String str = "";
        if(other != null)
            str = (new StringBuilder()).append(chr.getDescriptor().getID()).append("_").append(other.getDescriptor().getID()).append("_").append(name).toString();
        else
            str = (new StringBuilder()).append(chr.getDescriptor().getID()).append("_").append(name).toString();
        ScriptFlag flag = module.getFlag(name);
        if(flag == null)
        {
            flag = new ScriptFlag();
            flag.module = module;
            flag.name = name;
            flag.value = "1";
            module.FlagMap.put(name, flag);
        } else
        {
            flag.value = (new Integer(Integer.parseInt(flag.value) + 1)).toString();
        }
    }

    public boolean AllowCharacterBehaviour(String scriptCharacter)
    {
        return true;
    }

    public boolean DoesInstantly()
    {
        return true;
    }

    String owner;
    String stat;
    int modifier;
    IsoGameCharacter chr;
    public String name;
    String Other;
    boolean invert;
}
