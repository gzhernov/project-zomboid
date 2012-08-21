// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Die.java

package zombie.scripting.commands.Character;

import zombie.characters.IsoGameCharacter;
import zombie.iso.objects.IsoDeadBody;
import zombie.scripting.commands.BaseCommand;
import zombie.scripting.objects.ScriptModule;

public class Die extends BaseCommand
{

    public Die()
    {
        bGory = false;
    }

    public void init(String object, String params[])
    {
        owner = object;
        bGory = params[0].equals("true");
    }

    public void begin()
    {
        IsoGameCharacter actual = module.getCharacterActual(owner);
        if(actual != null)
        {
            actual.setHealth(0.0F);
            IsoDeadBody body = new IsoDeadBody(actual);
        }
    }

    public void Finish()
    {
    }

    public boolean IsFinished()
    {
        return true;
    }

    public void update()
    {
    }

    public boolean DoesInstantly()
    {
        return true;
    }

    String owner;
    boolean bGory;
}
