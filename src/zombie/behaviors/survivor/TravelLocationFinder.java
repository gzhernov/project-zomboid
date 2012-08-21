// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   TravelLocationFinder.java

package zombie.behaviors.survivor;

import zombie.characters.SurvivorDesc;
import zombie.core.Rand;
import zombie.core.properties.PropertyContainer;
import zombie.iso.*;
import zombie.iso.SpriteDetails.IsoFlagType;
import zombie.iso.areas.IsoBuilding;
import zombie.iso.areas.IsoRoom;

public class TravelLocationFinder
{

    public TravelLocationFinder()
    {
    }

    private static float ScoreLocation(SurvivorDesc desc, IsoGridSquare sq)
    {
        float score = 1.0F;
        if(sq.getRoom() != null)
        {
            score += 10F;
            score += sq.getRoom().building.ScoreBuildingPersonSpecific(desc, false);
        }
        return score;
    }

    public static IsoGridSquare FindLocation(SurvivorDesc desc, float x1, float y1, float x2, float y2, int attempts)
    {
        IsoGridSquare best = null;
        float bestscore = 0.0F;
        int timeout = 100;
        for(int n = 0; n < attempts; n++)
        {
            if(--timeout <= 0)
                return null;
            float score = 0.0F;
            int x = Rand.Next((int)x1, (int)x2);
            int y = Rand.Next((int)y1, (int)y2);
            IsoGridSquare sq = IsoWorld.instance.CurrentCell.getGridSquare(x, y, 0);
            if(sq == null || sq.getProperties().Is(IsoFlagType.solidtrans) || sq.getProperties().Is(IsoFlagType.solid))
            {
                n--;
                continue;
            }
            score = ScoreLocation(desc, sq);
            if(score > bestscore)
            {
                best = sq;
                bestscore = score;
            }
        }

        if(bestscore > 0.0F)
            return best;
        else
            return null;
    }
}
