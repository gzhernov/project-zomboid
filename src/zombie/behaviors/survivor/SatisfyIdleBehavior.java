// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   SatisfyIdleBehavior.java

package zombie.behaviors.survivor;

import zombie.behaviors.Behavior;
import zombie.behaviors.DecisionPath;
import zombie.behaviors.general.PathFindBehavior;
import zombie.characters.*;
import zombie.core.Rand;
import zombie.core.properties.PropertyContainer;
import zombie.iso.IsoGridSquare;
import zombie.iso.IsoUtils;
import zombie.iso.SpriteDetails.IsoFlagType;
import zombie.iso.Vector2;
import zombie.iso.areas.IsoBuilding;
import zombie.iso.areas.IsoRoom;
import zombie.ui.TextManager;
import zombie.ui.UIFont;

// Referenced classes of package zombie.behaviors.survivor:
//            TravelLocationFinder

public class SatisfyIdleBehavior extends Behavior
{

    public SatisfyIdleBehavior()
    {
        Started = false;
        OtherRoom = false;
        pathFind = new PathFindBehavior("Idle");
        sq = null;
        timeout = 0;
    }

    public zombie.behaviors.Behavior.BehaviorResult process(DecisionPath path, IsoGameCharacter character)
    {
        zombie.behaviors.Behavior.BehaviorResult res = zombie.behaviors.Behavior.BehaviorResult.Failed;
        this.timeout--;
        if(!Started && this.timeout <= 0)
        {
            this.timeout = 150 + Rand.Next(40);
            Started = true;
            OtherRoom = false;
            if(character.getCurrentSquare().getRoom() != null)
            {
                if(Rand.Next(2) == 0)
                    OtherRoom = true;
                IsoRoom room = character.getCurrentSquare().getRoom();
                if(OtherRoom)
                    room = character.getCurrentSquare().getRoom().building.getRandomRoom();
                boolean bDone = false;
                sq = room.getFreeTile();
                bDone = true;
                if(sq == null)
                {
                    this.timeout = 600;
                    reset();
                }
                if(sq != null && !pathFind.running(character))
                {
                    pathFind.sx = (int)character.getX();
                    pathFind.sy = (int)character.getY();
                    pathFind.sz = (int)character.getZ();
                    pathFind.tx = sq.getX();
                    pathFind.ty = sq.getY();
                    pathFind.tz = sq.getZ();
                }
            } else
            {
                boolean bDone = false;
                int timeout = 100;
                do
                {
                    if(bDone || timeout <= 0)
                        break;
                    timeout--;
                    sq = TravelLocationFinder.FindLocation(character.getDescriptor(), character.getX() - 5F, character.getY() - 5F, character.getX() + 5F, character.getY() + 5F, 10);
                    if(sq != null && !sq.getProperties().Is(IsoFlagType.solidtrans) && !sq.getProperties().Is(IsoFlagType.solid))
                        bDone = true;
                    if(sq != null && !InDistanceOfPlayer(character, sq.getX(), sq.getY()))
                        bDone = false;
                } while(true);
                if(sq != null && !pathFind.running(character))
                {
                    pathFind.sx = (int)character.getX();
                    pathFind.sy = (int)character.getY();
                    pathFind.sz = (int)character.getZ();
                    pathFind.tx = sq.getX();
                    pathFind.ty = sq.getY();
                    pathFind.tz = sq.getZ();
                }
            }
        }
        if(!Started && Rand.Next(200) == 0)
        {
            Vector2 vec = new Vector2(character.getAngle().x, character.getAngle().y);
            vec.x += (float)Rand.Next(-500, 500) / 2000F;
            vec.y += (float)Rand.Next(-500, 500) / 2000F;
            vec.normalize();
            character.DirectionFromVector(vec);
        }
        if(sq != null)
        {
            res = pathFind.process(path, character);
            if(res == zombie.behaviors.Behavior.BehaviorResult.Failed)
            {
                this.timeout = 600;
                reset();
            }
            if(res == zombie.behaviors.Behavior.BehaviorResult.Succeeded)
            {
                this.timeout = 600;
                character.getStats().idleboredom = 0.0F;
                reset();
            }
        }
        return res;
    }

    public void reset()
    {
        Started = false;
        sq = null;
        pathFind.reset();
    }

    public boolean valid()
    {
        return true;
    }

    private boolean InDistanceOfPlayer(IsoGameCharacter chr, int x, int y)
    {
        if(chr.getDescriptor().getGroup().Leader == chr.getDescriptor())
            return true;
        if(chr.getDescriptor().getGroup().Leader.getInstance().getCurrentSquare().getRoom() != null && chr.getCurrentSquare().getRoom() == null)
            return false;
        if(chr.getDescriptor().getGroup().Leader.getInstance().getCurrentSquare().getRoom() == null && chr.getCurrentSquare().getRoom() != null)
            return false;
        if(chr.getDescriptor().getGroup().Leader.getInstance().getCurrentSquare().getRoom() != null && chr.getCurrentSquare().getRoom() != null && chr.getCurrentSquare().getRoom().building == chr.getDescriptor().getGroup().Leader.getInstance().getCurrentSquare().getRoom().building && chr.getThreatLevel() == 0)
            return true;
        else
            return IsoUtils.DistanceManhatten(x, y, (int)chr.getDescriptor().getGroup().Leader.getInstance().getX(), (int)chr.getDescriptor().getGroup().Leader.getInstance().getY()) < chr.getPersonality().getPlayerDistanceComfort();
    }

    public float getPriority(IsoGameCharacter character)
    {
        float score = 1.0F;
        if(character.getThreatLevel() > 0)
            score -= 1000000F;
        if(character.getTimeSinceZombieAttack() < 30)
            score = -1000000F;
        return score;
    }

    public int renderDebug(int y)
    {
        int x = 50;
        TextManager.instance.DrawString(UIFont.Small, x, y, "SatisfyIdleBehaviour", 1.0F, 1.0F, 1.0F, 1.0F);
        return y += 30;
    }

    public boolean Started;
    boolean OtherRoom;
    PathFindBehavior pathFind;
    IsoGridSquare sq;
    int timeout;
}
