// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   CharactersAlreadyInScript.java

package zombie.scripting.commands.Script;

import zombie.scripting.commands.BaseCommand;
import zombie.scripting.objects.Script;

public class CharactersAlreadyInScript extends BaseCommand
{

    public CharactersAlreadyInScript()
    {
        invert = false;
    }

    public boolean getValue()
    {
        if(invert)
            return !currentinstance.CharactersAlreadyInScript;
        else
            return currentinstance.CharactersAlreadyInScript;
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
        if(val != null && val.indexOf("!") == 0)
        {
            invert = true;
            val = val.substring(1);
        }
    }

    public boolean DoesInstantly()
    {
        return true;
    }

    public void begin()
    {
    }

    String position;
    String val;
    private boolean invert;
}
