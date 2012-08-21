// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   FollowOrder.java

package zombie.behaviors.survivor.orders;

import zombie.behaviors.Behavior;
import zombie.behaviors.general.PathFindBehavior;
import zombie.characters.*;
import zombie.core.Collections.NulledArrayList;
import zombie.iso.IsoCamera;
import zombie.iso.IsoGridSquare;
import zombie.iso.sprite.IsoAnim;
import zombie.iso.sprite.IsoSprite;
import zombie.ui.TextManager;
import zombie.ui.UIFont;

// Referenced classes of package zombie.behaviors.survivor.orders:
//            Order

public class FollowOrder extends Order
{

    public FollowOrder(IsoGameCharacter chr, IsoGameCharacter target, int range)
    {
        super(chr);
        Range = 1;
        pathfindtimer = 60;
        bStrict = false;
        PathFind = new PathFindBehavior();
        rangelast = 0.0F;
        lastDist = 0.0F;
        currentDist = 0.0F;
        this.target = target;
        Range = range;
        PathFind.reset();
        PathFind.name = "FollowOrder";
    }

    public FollowOrder(IsoGameCharacter chr)
    {
        super(chr);
        Range = 1;
        pathfindtimer = 60;
        bStrict = false;
        PathFind = new PathFindBehavior();
        rangelast = 0.0F;
        lastDist = 0.0F;
        currentDist = 0.0F;
    }

    public FollowOrder(IsoGameCharacter chr, IsoGameCharacter target, int range, boolean b)
    {
        super(chr);
        Range = 1;
        pathfindtimer = 60;
        bStrict = false;
        PathFind = new PathFindBehavior();
        rangelast = 0.0F;
        lastDist = 0.0F;
        currentDist = 0.0F;
        this.target = target;
        Range = range;
        PathFind.reset();
        PathFind.name = "FollowOrder";
        bStrict = true;
    }

    private boolean InDistanceOfPlayer(IsoGameCharacter chr, int x, int y)
    {
        if(target.getCurrentSquare().getRoom() != null && chr.getCurrentSquare().getRoom() != target.getCurrentSquare().getRoom())
            return false;
        if(chr.getCurrentSquare().getRoom() != null && target.getCurrentSquare().getRoom() == null)
        {
            return false;
        } else
        {
            rangelast = currentDist;
            return rangelast < (float)Range;
        }
    }

    public zombie.behaviors.Behavior.BehaviorResult process()
    {
        int n;
        if(character == IsoCamera.CamCharacter)
            n = 0;
        lastDist = currentDist;
        currentDist = character.DistTo(target);
        pathfindtimer--;
        boolean inDist = InDistanceOfPlayer(character, (int)character.getX(), (int)character.getY());
        if(!inDist && pathfindtimer < 0 && currentDist > (float)(Range * 2))
        {
            PathFind.reset();
            PathFind.sx = (int)character.getX();
            PathFind.sy = (int)character.getY();
            PathFind.sz = (int)character.getZ();
            PathFind.tx = (int)target.getX();
            PathFind.ty = (int)target.getY();
            PathFind.tz = (int)target.getZ();
            pathfindtimer = 120;
        }
        zombie.behaviors.Behavior.BehaviorResult res = PathFind.process(null, character);
        if(res != zombie.behaviors.Behavior.BehaviorResult.Working)
            pathfindtimer = -1;
        return res;
    }

    public boolean isCancelledOnAttack()
    {
        return false;
    }

    public float getPriority(IsoGameCharacter character)
    {
        float score = 0.0F;
        float dist = character.DistTo(target);
        lastDist = currentDist;
        currentDist = dist;
        dist -= Range;
        if(dist < 0.0F)
            dist = 0.0F;
        score += dist * 6F;
        score += character.getThreatLevel() * 5;
        score += character.getDescriptor().getLoyalty() * 5F;
        if(target.sprite.CurrentAnim.name.equals("Run"))
            score *= 2.0F;
        boolean bInRange = InDistanceOfPlayer(character, (int)character.getX(), (int)character.getY());
        if(bInRange)
            score = 0.0F;
        else
            score *= 20000F;
        if((character instanceof IsoSurvivor) && !((IsoSurvivor)character).getVeryCloseEnemyList().isEmpty())
            return 0.0F;
        else
            return score;
    }

    public float getPathSpeed()
    {
        return currentDist <= (float)Range * 3F ? 0.06F : 0.08F;
    }

    public int renderDebug(int y)
    {
        int x = 50;
        TextManager.instance.DrawString(UIFont.Small, x, y, "FollowOrder", 1.0F, 1.0F, 1.0F, 1.0F);
        return y += 30;
    }

    public boolean complete()
    {
        return target.isDead();
    }

    public void update()
    {
    }

    public int Range;
    public int pathfindtimer;
    public IsoGameCharacter target;
    public boolean bStrict;
    PathFindBehavior PathFind;
    float rangelast;
    public float lastDist;
    public float currentDist;
}
