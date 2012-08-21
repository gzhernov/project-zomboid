// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   SatisfyFatigueBehavior.java

package zombie.behaviors.survivor;

import java.util.Iterator;
import java.util.Vector;
import zombie.behaviors.Behavior;
import zombie.behaviors.DecisionPath;
import zombie.behaviors.general.PathFindBehavior;
import zombie.characters.IsoGameCharacter;
import zombie.core.Collections.NulledArrayList;
import zombie.iso.IsoGridSquare;
import zombie.iso.areas.IsoBuilding;
import zombie.iso.areas.IsoRoom;

public class SatisfyFatigueBehavior extends Behavior
{

    public SatisfyFatigueBehavior()
    {
        pathFind = new PathFindBehavior("Fatigue");
    }

    public zombie.behaviors.Behavior.BehaviorResult process(DecisionPath path, IsoGameCharacter character)
    {
        IsoGridSquare bed = null;
        zombie.behaviors.Behavior.BehaviorResult res = zombie.behaviors.Behavior.BehaviorResult.Failed;
        if(character.getCurrentSquare().getRoom() != null)
        {
            if(character.getCurrentSquare().getRoom().Beds.size() > 0)
            {
                bed = (IsoGridSquare)character.getCurrentSquare().getRoom().Beds.get(0);
                if(bed.getMovingObjects().size() > 0 && bed.getMovingObjects().get(0) != character)
                    bed = null;
            } else
            {
                Iterator i$ = character.getCurrentSquare().getRoom().building.Rooms.iterator();
                do
                {
                    if(!i$.hasNext())
                        break;
                    IsoRoom room = (IsoRoom)i$.next();
                    if(room.Beds.size() > 0)
                    {
                        bed = (IsoGridSquare)room.Beds.get(0);
                        if(bed.getMovingObjects().size() > 0 && bed.getMovingObjects().get(0) != character)
                            bed = null;
                    }
                } while(bed == null);
            }
            if(bed != null)
            {
                if(!pathFind.running(character))
                {
                    pathFind.sx = (int)character.getX();
                    pathFind.sy = (int)character.getY();
                    pathFind.sz = (int)character.getZ();
                }
                pathFind.tx = bed.getX();
                pathFind.ty = bed.getY();
                pathFind.tz = bed.getZ();
                res = pathFind.process(path, character);
                if(res == zombie.behaviors.Behavior.BehaviorResult.Succeeded)
                    character.setAsleep(true);
            }
        }
        return res;
    }

    public void reset()
    {
    }

    public boolean valid()
    {
        return true;
    }

    PathFindBehavior pathFind;
}
