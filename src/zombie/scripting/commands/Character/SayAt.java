// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   SayAt.java

package zombie.scripting.commands.Character;

import gnu.trove.map.hash.THashMap;
import java.security.InvalidParameterException;
import java.util.Stack;
import zombie.characters.IsoGameCharacter;
import zombie.characters.Talker;
import zombie.core.Rand;
import zombie.scripting.ScriptManager;
import zombie.scripting.commands.BaseCommand;
import zombie.scripting.objects.*;

// Referenced classes of package zombie.scripting.commands.Character:
//            StringFunctions

public class SayAt extends BaseCommand
{

    public SayAt()
    {
        chrs = new Stack();
        say = new Stack();
        talkerobj = null;
        talker = false;
        chras = new Stack();
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
            if(total.contains("\""))
            {
                total = module.getLanguage(total);
                if(total.indexOf("\"") == 0)
                {
                    total = total.substring(1);
                    total = total.substring(0, total.length() - 1);
                }
                say.add(total);
            } else
            {
                chrs.add(total.trim());
            }
        }

    }

    public void begin()
    {
        if(ScriptManager.instance.skipping)
            return;
        if(currentinstance != null && currentinstance.HasAlias(owner))
        {
            chr = currentinstance.getAlias(owner);
            for(int n = 0; n < chrs.size(); n++)
                if(currentinstance.HasAlias((String)chrs.get(n)))
                    chras.add(currentinstance.CharacterAliases.get(chrs.get(n)));

            String str = (String)say.get(Rand.Next(say.size()));
            str = StringFunctions.EscapeChar(chr, chras, str);
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
        if(module.getCharacter(owner).Actual == null)
            throw new InvalidParameterException();
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
        return false;
    }

    public boolean DoesInstantly()
    {
        return false;
    }

    String owner;
    Stack chrs;
    Stack say;
    IsoGameCharacter chr;
    ScriptTalker talkerobj;
    boolean talker;
    Stack chras;
}
