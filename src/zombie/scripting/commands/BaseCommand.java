// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   BaseCommand.java

package zombie.scripting.commands;

import zombie.scripting.objects.Script;
import zombie.scripting.objects.ScriptModule;

public abstract class BaseCommand
{

    public BaseCommand()
    {
        script = null;
        currentinstance = null;
    }

    public abstract void begin();

    public abstract boolean IsFinished();

    public abstract void update();

    public abstract void init(String s, String as[]);

    public abstract boolean DoesInstantly();

    public boolean getValue()
    {
        return false;
    }

    public void Finish()
    {
    }

    public boolean AllowCharacterBehaviour(String scriptCharacter)
    {
        return true;
    }

    public void updateskip()
    {
    }

    public ScriptModule module;
    public Script script;
    public zombie.scripting.objects.Script.ScriptInstance currentinstance;
}
