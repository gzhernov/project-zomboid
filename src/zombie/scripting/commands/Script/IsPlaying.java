// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   IsPlaying.java

package zombie.scripting.commands.Script;

import zombie.scripting.ScriptManager;
import zombie.scripting.commands.BaseCommand;

public class IsPlaying extends BaseCommand
{

    public IsPlaying()
    {
        invert = false;
    }

    public void begin()
    {
    }

    public boolean getValue()
    {
        boolean bVal = ScriptManager.instance.IsScriptPlaying(val);
        if(invert)
            return !bVal;
        else
            return bVal;
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
        val = object;
        if(val.indexOf("!") == 0)
        {
            invert = true;
            val = val.substring(1);
        }
    }

    public boolean DoesInstantly()
    {
        return true;
    }

    boolean invert;
    String val;
}
