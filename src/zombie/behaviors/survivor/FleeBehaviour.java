// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   FleeBehaviour.java

package zombie.behaviors.survivor;

import java.util.Iterator;
import java.util.Stack;
import zombie.behaviors.Behavior;
import zombie.behaviors.DecisionPath;
import zombie.behaviors.general.PathFindBehavior;
import zombie.behaviors.survivor.orders.FollowOrder;
import zombie.characters.BodyDamage.BodyDamage;
import zombie.characters.*;
import zombie.core.Collections.NulledArrayList;
import zombie.core.Rand;
import zombie.core.properties.PropertyContainer;
import zombie.iso.*;
import zombie.iso.SpriteDetails.IsoFlagType;
import zombie.iso.areas.IsoBuilding;
import zombie.iso.areas.IsoRoom;
import zombie.ui.TextManager;
import zombie.ui.UIFont;

// Referenced classes of package zombie.behaviors.survivor:
//            MasterSurvivorBehavior

public class FleeBehaviour extends Behavior
{

    public FleeBehaviour()
    {
        Started = false;
        OtherRoom = false;
        pathFind = new PathFindBehavior();
        sq = null;
        recalc = 240;
        bFollowFlee = false;
        bPickedFleeStyle = false;
    }

    public void onSwitch()
    {
        bFollowFlee = false;
        bPickedFleeStyle = false;
    }

    public zombie.behaviors.Behavior.BehaviorResult process(DecisionPath path, IsoGameCharacter character)
    {
        if(order == null)
            order = new FollowOrder(character);
        pathFind.name = "FleeBehaviour";
        this.character = character;
        if(!bPickedFleeStyle)
        {
            bFollowFlee = false;
            if(!character.getLocalGroupList().isEmpty())
            {
                Iterator it = character.getLocalGroupList().iterator();
                do
                {
                    if(it == null || !it.hasNext())
                        break;
                    IsoGameCharacter chr = (IsoGameCharacter)it.next();
                    if((chr instanceof IsoPlayer) && chr.DistTo(character) > 6F || !(chr instanceof IsoPlayer) && chr.getMasterProper().flee == chr.getMasterProper().toProcess && !chr.getMasterProper().flee.bFollowFlee)
                    {
                        order.Range = 2;
                        order.target = chr;
                        bFollowFlee = true;
                    }
                } while(true);
            }
            bPickedFleeStyle = true;
        }
        zombie.behaviors.Behavior.BehaviorResult res = zombie.behaviors.Behavior.BehaviorResult.Failed;
        recalc--;
        if(!Started)
        {
            Started = true;
            OtherRoom = false;
            float tx = 0.0F;
            float ty = 0.0F;
            if(character.getLocalEnemyList().isEmpty() && character.getCurrentSquare().getRoom() == null)
            {
                if(RunToBuilding(tx, ty, character))
                    return zombie.behaviors.Behavior.BehaviorResult.Succeeded;
            } else
            {
                int attempts = 30;
                int lowest = 1000;
                IsoGridSquare lowestg = null;
                for(int n = 0; n < attempts; n++)
                {
                    int x = Rand.Next(IsoWorld.instance.CurrentCell.getWidthInTiles());
                    int y = Rand.Next(IsoWorld.instance.CurrentCell.getHeightInTiles());
                    IsoGridSquare sqtest = IsoWorld.instance.CurrentCell.getGridSquare(x, y, 0);
                    if(sqtest != null)
                        lowestg = sqtest;
                }

                if(lowestg != null)
                    sq = lowestg;
                if(sq == null && RunToBuilding(tx, ty, character))
                    return zombie.behaviors.Behavior.BehaviorResult.Succeeded;
            }
        }
        if(sq != null)
        {
            if(!pathFind.running(character))
            {
                character.setPathSpeed(0.08F);
                pathFind.reset();
                pathFind.sx = (int)character.getX();
                pathFind.sy = (int)character.getY();
                pathFind.sz = (int)character.getZ();
                pathFind.tx = sq.getX();
                pathFind.ty = sq.getY();
                pathFind.tz = sq.getZ();
            }
            res = pathFind.process(path, character);
            if(res == zombie.behaviors.Behavior.BehaviorResult.Failed)
            {
                sq = null;
                reset();
            } else
            if(res == zombie.behaviors.Behavior.BehaviorResult.Succeeded)
            {
                character.getStats().idleboredom = 0.0F;
                sq = null;
                reset();
            }
        }
        return zombie.behaviors.Behavior.BehaviorResult.Working;
    }

    private boolean RunToBuilding(float tx, float ty, IsoGameCharacter character)
    {
        boolean bDone = false;
        int timeout = 0;
        do
        {
            if(bDone)
                break;
            timeout++;
            int xx = 0;
            int yy = 0;
            int a = Rand.Next(5) + 1;
            xx = (int)(tx * (float)a);
            yy = (int)(ty * (float)a);
            xx += Rand.Next(4) - 2;
            yy += Rand.Next(4) - 2;
            IsoBuilding building = character.getDescriptor().getGroup().Safehouse;
            if(building == null)
                return false;
            sq = building.getRandomRoom().getFreeTile();
            if(timeout >= 20)
                return true;
            if(sq != null && !sq.getProperties().Is(IsoFlagType.solidtrans) && !sq.getProperties().Is(IsoFlagType.solid))
                bDone = true;
        } while(true);
        return false;
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

    public float getPriority(IsoGameCharacter character)
    {
        float score = 0.0F;
        int fdsf;
        if(character == IsoCamera.CamCharacter)
            fdsf = 0;
        if(IsoPlayer.DemoMode && character.IsArmed())
            return character.getStats().endurance >= character.getStats().endurancewarn ? -100000F : 1E+008F;
        if(!character.IsArmed())
            score += 50F;
        if(!character.IsArmed() && character.getDangerLevels() > 5F)
            score += 10000F;
        if(character.getLocalRelevantEnemyList().size() > 20)
            score += 1000F;
        if(character.getThreatLevel() == 0)
            score -= 50F;
        if(character.getThreatLevel() == 0 && character.getCurrentSquare().getRoom() != null)
            score -= 11150F;
        score -= character.getLocalNeutralList().size() * 5;
        score *= character.getThreatLevel() + 1;
        if(character.getThreatLevel() > 1)
            score *= character.getThreatLevel() * 10;
        score += 100F - character.getBodyDamage().getHealth();
        score += character.getBodyDamage().getNumPartsBitten() * 20;
        score += character.getBodyDamage().getNumPartsScratched() * 20;
        if(character.getTimeSinceZombieAttack() < 60)
            score += 50F;
        score *= 1.0F + (1.0F - character.getStats().endurance);
        score /= character.getDescriptor().getBravery();
        score *= MasterSurvivorBehavior.FleeMultiplier;
        score += character.getDangerLevels() * 3F;
        score *= 0.005F;
        if(character.getVeryCloseEnemyList().size() > 7)
            score *= 1000F;
        score *= 0.001F;
        score *= 10F - character.getDescriptor().getBravery();
        score *= 10F - character.getDescriptor().getBravery();
        return score;
    }

    public float getPathSpeed()
    {
        return character.getLocalRelevantEnemyList().size() <= 5 ? 0.05F : 0.08F;
    }

    public int renderDebug(int y)
    {
        int x = 50;
        TextManager.instance.DrawString(UIFont.Small, x, y, "FleeBehaviour", 1.0F, 1.0F, 1.0F, 1.0F);
        return y += 30;
    }

    public boolean Started;
    boolean OtherRoom;
    PathFindBehavior pathFind;
    IsoGridSquare sq;
    IsoGameCharacter character;
    int recalc;
    FollowOrder order;
    public boolean bFollowFlee;
    public boolean bPickedFleeStyle;
}
