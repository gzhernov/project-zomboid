// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   StopAction.java

package zombie.scripting.commands.Character;

import java.security.InvalidParameterException;
import zombie.characters.IsoGameCharacter;
import zombie.scripting.commands.BaseCommand;
import zombie.scripting.objects.ScriptCharacter;
import zombie.scripting.objects.ScriptModule;

public class StopAction extends BaseCommand
{

    public StopAction()
    {
        num = 1.0F;
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
        owner = object;
    }

    public void begin()
    {
        if(module.getCharacter(owner).Actual == null)
        {
            throw new InvalidParameterException();
        } else
        {
            module.getCharacter(owner).Actual.StopAllActionQueue();
            return;
        }
    }

    public boolean DoesInstantly()
    {
        return true;
    }

    String owner;
    IsoGameCharacter chr;
    float num;
}
