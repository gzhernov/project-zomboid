// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   IsInRoom.java

package zombie.scripting.commands.Character;

import zombie.characters.IsoGameCharacter;
import zombie.iso.IsoGridSquare;
import zombie.iso.areas.IsoRoom;
import zombie.scripting.commands.BaseCommand;
import zombie.scripting.objects.Room;
import zombie.scripting.objects.ScriptModule;

public class IsInRoom extends BaseCommand
{

    public IsInRoom()
    {
        invert = false;
    }

    public void init(String object, String params[])
    {
        if(params[0].contains(")"))
            object = object;
        owner = object;
        if(owner.indexOf("!") == 0)
        {
            invert = true;
            owner = owner.substring(1);
        }
        room = params[0].trim();
    }

    public boolean getValue()
    {
        IsoGameCharacter chr = module.getCharacterActual(owner);
        if(chr == null)
            return false;
        Room room = module.getRoom(this.room);
        if(room != null)
        {
            if(chr.getCurrentSquare().getRoom() == null)
                return false;
            if(invert)
                return !room.name.equals(chr.getCurrentSquare().getRoom().RoomDef);
            else
                return room.name.equals(chr.getCurrentSquare().getRoom().RoomDef);
        }
        IsoGameCharacter chr2 = module.getCharacterActual(this.room);
        if(chr2 == null)
            return false;
        boolean bTrue = false;
        if(chr.getCurrentSquare() == null || chr2.getCurrentSquare() == null)
            return false;
        if(chr.getCurrentSquare().getRoom() == chr2.getCurrentSquare().getRoom())
            bTrue = true;
        if(invert)
            return !bTrue;
        else
            return bTrue;
    }

    public void begin()
    {
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
    String room;
    boolean invert;
}
