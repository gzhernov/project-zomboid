// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   IsSpeaking.java

package zombie.scripting.commands.Character;

import zombie.characters.IsoGameCharacter;
import zombie.scripting.commands.BaseCommand;
import zombie.scripting.objects.ScriptModule;

public class IsSpeaking extends BaseCommand
{

    public IsSpeaking()
    {
        invert = false;
    }

    public void begin()
    {
    }

    public boolean getValue()
    {
        IsoGameCharacter chr = module.getCharacterActual(character);
        if(chr == null)
            return false;
        if(invert)
            return !chr.IsSpeaking();
        else
            return chr.IsSpeaking();
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
        character = object;
        if(character.indexOf("!") == 0)
        {
            invert = true;
            character = character.substring(1);
        }
    }

    public boolean DoesInstantly()
    {
        return true;
    }

    boolean invert;
    String character;
}
