// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   InRange.java

package zombie.scripting.commands.Character;

import zombie.characters.IsoGameCharacter;
import zombie.iso.IsoUtils;
import zombie.scripting.commands.BaseCommand;
import zombie.scripting.objects.*;

public class InRange extends BaseCommand
{

    public InRange()
    {
        min = 0;
        bChar = false;
        invert = false;
    }

    public void init(String object, String params[])
    {
        owner = object;
        if(owner.indexOf("!") == 0)
        {
            invert = true;
            owner = owner.substring(1);
        }
        if(params.length == 2)
        {
            Waypoint pt = module.getWaypoint(params[0].trim());
            if(pt != null)
            {
                x = pt.x;
                y = pt.y;
                z = pt.z;
            } else
            {
                bChar = true;
                other = params[0].trim();
            }
            min = Integer.parseInt(params[1].trim());
        }
    }

    public boolean getValue()
    {
        IsoGameCharacter actual = null;
        
        if(currentinstance.HasAlias(owner))
        {
            actual = currentinstance.getAlias(owner);
        } else
        {
            actual = module.getCharacterActual(owner);
            ScriptCharacter chr = this.module.getCharacter(this.owner);
            
            if(((ScriptCharacter) (chr)).Actual == null)
                return false;
            
        }
        
        IsoGameCharacter chr = actual;
        
        if(chr.isDead())
            return true;
        if(bChar)
        {
            if(currentinstance.HasAlias(other))
            {
                actual = currentinstance.getAlias(other);
            } else
            {
                actual = module.getCharacterActual(other);
                ScriptCharacter chra = module.getCharacter(other);
                if(chra.Actual == null)
                    return false;
            }
            IsoGameCharacter chr2 = actual;
            if(chr2.isDead())
                return true;
            if(invert)
                return IsoUtils.DistanceManhatten(chr2.getX(), chr2.getY(), chr.getX(), chr.getY()) > (float)min || chr.getZ() != chr2.getZ();
            else
                return IsoUtils.DistanceManhatten(chr2.getX(), chr2.getY(), chr.getX(), chr.getY()) <= (float)min && chr.getZ() == chr2.getZ();
        }
        if(invert)
            return IsoUtils.DistanceManhatten(x, y, chr.getX(), chr.getY()) > (float)min || chr.getZ() != (float)z;
        else
            return IsoUtils.DistanceManhatten(x, y, chr.getX(), chr.getY()) <= (float)min && chr.getZ() == (float)z;
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
    String other;
    int min;
    int x;
    int y;
    int z;
    boolean bChar;
    boolean invert;
}
