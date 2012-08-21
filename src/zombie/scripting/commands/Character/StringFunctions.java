// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   StringFunctions.java

package zombie.scripting.commands.Character;

import java.util.Stack;
import zombie.characters.IsoGameCharacter;
import zombie.characters.SurvivorDesc;

class StringFunctions
{

    StringFunctions()
    {
    }

    static String EscapeChar(IsoGameCharacter chr, String str)
    {
        str = str.replace("$FIRSTNAME$", chr.getDescriptor().getForename());
        str = str.replace("$SURNAME$", chr.getDescriptor().getSurname());
        return str;
    }

    static String EscapeChar(IsoGameCharacter chr, Stack chrs, String str)
    {
        str = EscapeChar(chr, str);
        for(int n = 0; n < chrs.size(); n++)
        {
            str = str.replace((new StringBuilder()).append("$FIRSTNAME").append(n + 1).append("$").toString(), ((IsoGameCharacter)chrs.get(n)).getDescriptor().getForename());
            str = str.replace((new StringBuilder()).append("$SURNAME").append(n + 1).append("$").toString(), ((IsoGameCharacter)chrs.get(n)).getDescriptor().getSurname());
        }

        return str;
    }
}
