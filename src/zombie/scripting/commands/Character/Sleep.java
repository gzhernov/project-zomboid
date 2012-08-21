// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Sleep.java

package zombie.scripting.commands.Character;

import java.security.InvalidParameterException;
import zombie.characters.IsoGameCharacter;
import zombie.characters.IsoPlayer;
import zombie.scripting.commands.BaseCommand;
import zombie.scripting.objects.ScriptCharacter;
import zombie.scripting.objects.ScriptModule;
import zombie.ui.UIManager;

public class Sleep extends BaseCommand
{

    public Sleep()
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
        num = Float.parseFloat(params[0].trim());
    }

    public void begin()
    {
        if(module.getCharacter(owner).Actual == null)
            throw new InvalidParameterException();
        module.getCharacter(owner).Actual.setAsleep(true);
        if(module.getCharacter(owner).Actual == IsoPlayer.getInstance())
        {
            IsoPlayer.instance.setAsleepTime(0.0F);
            module.getCharacter(owner).Actual.setForceWakeUpTime(num);
            UIManager.setbFadeBeforeUI(true);
            UIManager.FadeOut(4);
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
