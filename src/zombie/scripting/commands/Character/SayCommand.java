// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   SayCommand.java

package zombie.scripting.commands.Character;

import java.util.Stack;
import zombie.characters.IsoGameCharacter;
import zombie.characters.Talker;
import zombie.core.Rand;
import zombie.scripting.ScriptManager;
import zombie.scripting.commands.BaseCommand;
import zombie.scripting.objects.*;

// Referenced classes of package zombie.scripting.commands.Character:
//            StringFunctions

public class SayCommand extends BaseCommand
{

    public SayCommand()
    {
        say = new Stack();
        talkerobj = null;
        talker = false;
    }

    public boolean IsFinished()
    {
        if(talker && talkerobj != null)
            return !talkerobj.getActual().IsSpeaking();
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
        if(ScriptManager.instance.skipping)
            return;
        if(currentinstance != null && currentinstance.HasAlias(owner))
        {
            chr = currentinstance.getAlias(owner);
            String str = (String)say.get(Rand.Next(say.size()));
            str = StringFunctions.EscapeChar(chr, str);
            chr.Say(str);
            return;
        }
        if(module.getTalker(owner) != null)
        {
            talker = true;
            talkerobj = module.getTalker(owner);
            talkerobj.getActual().Say((String)say.get(Rand.Next(say.size())));
            return;
        }
        ScriptCharacter chra = module.getCharacter(owner);
        if(chra == null)
            return;
        chr = chra.Actual;
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
        return false;
    }

    public boolean DoesInstantly()
    {
        return false;
    }

    String owner;
    Stack say;
    IsoGameCharacter chr;
    ScriptTalker talkerobj;
    boolean talker;
}
