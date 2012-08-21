// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   IsCharacterScriptFlagEqualTo.java

package zombie.scripting.commands.Character;

import zombie.characters.IsoGameCharacter;
import zombie.characters.SurvivorDesc;
import zombie.scripting.commands.BaseCommand;
import zombie.scripting.objects.*;

public class IsCharacterScriptFlagEqualTo extends BaseCommand
{

    public IsCharacterScriptFlagEqualTo()
    {
        modifier = 0;
        value = "";
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
        value = params[2].trim().replace("\"", "");
    }

    public boolean getValue()
    {
        if(currentinstance != null && currentinstance.HasAlias(owner))
        {
            chr = currentinstance.getAlias(owner);
        } else
        {
            if(module.getCharacter(owner) == null)
                return false;
            if(module.getCharacter(owner).Actual == null)
                return false;
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
            return false;
        String str = "";
        if(other != null)
            str = (new StringBuilder()).append(chr.getDescriptor().getID()).append("_").append(other.getDescriptor().getID()).append("_").append(name).toString();
        else
            str = (new StringBuilder()).append(chr.getDescriptor().getID()).append("_").append(name).toString();
        ScriptFlag flag = module.getFlag(name);
        if(flag == null)
            return false;
        else
            return flag.IsValue(value);
    }

    public void begin()
    {
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
    String value;
    String Other;
    boolean invert;
}
