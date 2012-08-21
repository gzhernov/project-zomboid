// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   SayIdle.java

package zombie.scripting.commands.Character;

import java.util.Stack;
import zombie.characters.IsoGameCharacter;
import zombie.core.Rand;
import zombie.scripting.commands.BaseCommand;
import zombie.scripting.objects.*;

// Referenced classes of package zombie.scripting.commands.Character:
//            StringFunctions

public class SayIdle extends BaseCommand
{

    public SayIdle()
    {
        say = new Stack();
    }

    public boolean IsFinished()
    {
        if(chr == null)
            return true;
        else
            return !chr.isSpeaking();
    }

    public void update()
    {
    }

    public void init(String object, String params[])
    {
        owner = object;
        for(int n = 0; n < params.length; n++)
        {
            String total = params[n];
            total = module.getLanguage(total);
            if(total.indexOf("\"") == 0)
            {
                total = total.substring(1);
                total = total.substring(0, total.length() - 1);
            }
            say.add(total);
        }

    }

    public void begin()
    {
        if(currentinstance.HasAlias(owner))
        {
            chr = currentinstance.getAlias(owner);
            String str = (String)say.get(Rand.Next(say.size()));
            str = StringFunctions.EscapeChar(chr, str);
            chr.Say(str);
            return;
        }
        chr = module.getCharacter(owner).Actual;
        if(chr == null)
        {
            return;
        } else
        {
            String str = (String)say.get(Rand.Next(say.size()));
            str = StringFunctions.EscapeChar(chr, str);
            chr.Say(str);
            return;
        }
    }

    public boolean AllowCharacterBehaviour(String scriptCharacter)
    {
        return true;
    }

    public boolean DoesInstantly()
    {
        return false;
    }

    String owner;
    Stack say;
    IsoGameCharacter chr;
}
