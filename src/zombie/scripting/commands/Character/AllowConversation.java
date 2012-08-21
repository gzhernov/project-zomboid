// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   AllowConversation.java

package zombie.scripting.commands.Character;

import zombie.characters.IsoGameCharacter;
import zombie.scripting.commands.BaseCommand;
import zombie.scripting.objects.*;

public class AllowConversation extends BaseCommand
{

    public AllowConversation()
    {
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
        String total = "";
        bAllow = params[0].trim().equals("true");
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
        if(bAllow)
            chr.setAllowConversation(true);
        else
            chr.setAllowConversation(false);
    }

    public boolean DoesInstantly()
    {
        return true;
    }

    String owner;
    String say;
    String Other;
    IsoGameCharacter chr;
    boolean bAllow;
}
