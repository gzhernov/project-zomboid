// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   AttackBehavior.java

package zombie.behaviors.survivor;

import zombie.behaviors.Behavior;
import zombie.behaviors.DecisionPath;
import zombie.behaviors.general.PathFindBehavior;
import zombie.characters.BodyDamage.BodyDamage;
import zombie.characters.*;
import zombie.core.Collections.NulledArrayList;
import zombie.inventory.*;
import zombie.inventory.types.HandWeapon;
import zombie.iso.*;
import zombie.ui.TextManager;
import zombie.ui.UIFont;

// Referenced classes of package zombie.behaviors.survivor:
//            MasterSurvivorBehavior

public class AttackBehavior extends Behavior
{

    public AttackBehavior()
    {
        HasRangeRequirement = false;
        RangeTest = null;
        TestRangeMax = 10000;
        thinkTime = 10;
        thinkTimeMax = 10;
        stayInside = false;
        pathFind = new PathFindBehavior("Attack");
        Target = null;
        weapon = null;
        timeout = 180;
        backuppoint = null;
        backingup = false;
        bWaitForThem = false;
        nextbackuptest = 0;
        failedTimeout = 60;
        a = new Vector2();
    }

    public zombie.behaviors.Behavior.BehaviorResult process(DecisionPath path, IsoGameCharacter character)
    {
        if(Target != null && !((IsoSurvivor)character).getLocalRelevantEnemyList().contains(Target))
        {
            Target = null;
            pathFind.reset();
            backuppoint = null;
            Target = null;
            weapon = null;
            bWaitForThem = false;
        }
        if(nextbackuptest <= 0 && ((IsoSurvivor)character).getVeryCloseEnemyList().size() > 2 && !backingup)
        {
            int n;
            if(IsoCamera.CamCharacter == character)
                n = 0;
            backuppoint = character.getLowDangerInVicinity(10, 8);
            nextbackuptest = 60;
        }
        nextbackuptest--;
        if(backuppoint != null)
        {
            if(!backingup || pathFind.sx == 0)
            {
                pathFind.reset();
                pathFind.sx = character.getCurrentSquare().getX();
                pathFind.sy = character.getCurrentSquare().getY();
                pathFind.sz = character.getCurrentSquare().getZ();
                pathFind.tx = backuppoint.getX();
                pathFind.ty = backuppoint.getY();
                pathFind.tz = backuppoint.getZ();
            }
            zombie.behaviors.Behavior.BehaviorResult progress = pathFind.process(path, character);
            if(progress == zombie.behaviors.Behavior.BehaviorResult.Working)
            {
                backingup = true;
                return zombie.behaviors.Behavior.BehaviorResult.Working;
            }
            pathFind.reset();
            backuppoint = null;
            Target = null;
            weapon = null;
            if(progress == zombie.behaviors.Behavior.BehaviorResult.Succeeded)
                bWaitForThem = true;
            nextbackuptest = 60;
        }
        boolean PickedTargetThisFrame = false;
        if(character.getLastTargettedBy() != null)
        {
            if(Target != character.getLastTargettedBy())
                PickedTargetThisFrame = true;
            Target = character.getLastTargettedBy();
            pathFind.sx = character.getCurrentSquare().getX();
            pathFind.sy = character.getCurrentSquare().getY();
            pathFind.sz = character.getCurrentSquare().getZ();
        }
        if(Target == null);
        if(Target != null && (Target.getHealth() <= 0.0F || Target.getBodyDamage().getHealth() <= 0.0F))
        {
            Target = null;
            weapon = null;
            character.getStats().idleboredom = 1.0F;
            timeout = 180;
            return zombie.behaviors.Behavior.BehaviorResult.Succeeded;
        }
        if(character.getPrimaryHandItem() != null && character.getPrimaryHandItem().getCat() == ItemType.Weapon)
        {
            HandWeapon weap = (HandWeapon)character.getPrimaryHandItem();
            if(!character.HasItem(weap.getAmmoType()) && weap.getAmmoType() != null)
                character.setPrimaryHandItem(null);
        }
        if(character.getPrimaryHandItem() == null || character.getPrimaryHandItem().getCat() != ItemType.Weapon)
            if(character.getInventory().HasType(ItemType.Weapon))
            {
                HandWeapon weap = (HandWeapon)character.getInventory().getBestWeapon(character.getDescriptor());
                if(weap != null && (character.HasItem(weap.getAmmoType()) || weap.getAmmoType() == null))
                {
                    character.setPrimaryHandItem(weap);
                    if(character.getPrimaryHandItem() == character.getSecondaryHandItem())
                        character.setSecondaryHandItem(null);
                }
            } else
            {
                timeout = 180;
                return zombie.behaviors.Behavior.BehaviorResult.Succeeded;
            }
        weapon = character.getPrimaryHandItem();
        thinkTime--;
        if(weapon != null && weapon.getCondition() <= 0)
            weapon = null;
        if(Target == null)
        {
            if(HasRangeRequirement)
                Target = character.getCurrentSquare().FindEnemy(character, character.getPersonality().getHuntZombieRange(), character.getLocalRelevantEnemyList(), RangeTest, TestRangeMax);
            else
                Target = character.getCurrentSquare().FindEnemy(character, character.getPersonality().getHuntZombieRange(), character.getLocalRelevantEnemyList());
            if(Target != null && Target.getCurrentSquare() != null)
                PickedTargetThisFrame = true;
            thinkTime = thinkTimeMax;
            pathFind.sx = character.getCurrentSquare().getX();
            pathFind.sy = character.getCurrentSquare().getY();
            pathFind.sz = character.getCurrentSquare().getZ();
        }
        if(Target == null)
        {
            weapon = null;
            timeout = 180;
            return zombie.behaviors.Behavior.BehaviorResult.Succeeded;
        }
        if(weapon == null)
            return zombie.behaviors.Behavior.BehaviorResult.Succeeded;
        IsoGridSquare mine = character.getCurrentSquare();
        IsoGridSquare theirs = Target.getCurrentSquare();
        if((weapon instanceof HandWeapon) && mine != null && theirs != null)
        {
            HandWeapon hand = (HandWeapon)weapon;
            float dist = IsoUtils.DistanceTo(character.getX(), character.getY(), Target.getX(), Target.getY());
            if(theirs.getZ() != mine.getZ() || hand.getMaxRange() * 0.9F < dist || LosUtil.lineClear(mine.getCell(), mine.getX(), mine.getY(), mine.getZ(), theirs.getX(), theirs.getY(), theirs.getZ(), false) != zombie.iso.LosUtil.TestResults.Clear)
            {
                if(!bWaitForThem)
                {
                    if(PickedTargetThisFrame)
                    {
                        pathFind.tx = theirs.getX();
                        pathFind.ty = theirs.getY();
                        pathFind.tz = theirs.getZ();
                    }
                    zombie.behaviors.Behavior.BehaviorResult res = pathFind.process(path, character);
                    if(res == zombie.behaviors.Behavior.BehaviorResult.Failed)
                    {
                        Target = null;
                        weapon = null;
                        thinkTime = thinkTimeMax;
                        return zombie.behaviors.Behavior.BehaviorResult.Succeeded;
                    }
                    if(res == zombie.behaviors.Behavior.BehaviorResult.Succeeded)
                    {
                        dist = IsoUtils.DistanceTo(character.getX(), character.getY(), Target.getX(), Target.getY());
                        if(dist > ((HandWeapon)weapon).getMaxRange())
                        {
                            pathFind.tx = theirs.getX();
                            pathFind.ty = theirs.getY();
                            pathFind.tz = theirs.getZ();
                        }
                        return zombie.behaviors.Behavior.BehaviorResult.Working;
                    }
                }
            } else
            {
                a.x = Target.getX();
                a.y = Target.getY();
                a.x -= character.getX();
                a.y -= character.getY();
                if(a.getLength() > 0.0F)
                {
                    a.normalize();
                    character.DirectionFromVector(a);
                    character.getAngle().x = a.x;
                    character.getAngle().y = a.y;
                    boolean bSuccess = ((IsoSurvivor)character).AttemptAttack(1.0F);
                    if(!bSuccess)
                    {
                        Target = null;
                        weapon = null;
                        return zombie.behaviors.Behavior.BehaviorResult.Failed;
                    }
                    character.PlayShootAnim();
                }
                bWaitForThem = false;
                timeout = 180;
                return zombie.behaviors.Behavior.BehaviorResult.Succeeded;
            }
        }
        return zombie.behaviors.Behavior.BehaviorResult.Working;
    }

    public void reset()
    {
        Target = null;
        weapon = null;
        timeout = 180;
        pathFind.reset();
    }

    public boolean valid()
    {
        return true;
    }

    float getPriority(IsoGameCharacter character)
    {
        float score = 0.0F;
        if(!character.IsArmed())
            return -1E+007F;
        if(character.getLocalRelevantEnemyList().isEmpty())
            return -1E+007F;
        if(IsoPlayer.DemoMode)
            score += 1000F;
        if(character.getLocalRelevantEnemyList().size() < 5)
            score += character.getLocalRelevantEnemyList().size() * 5;
        if(character.getLocalRelevantEnemyList().size() > 10)
            score -= character.getLocalRelevantEnemyList().size() * 5;
        if(character.getLocalRelevantEnemyList().size() > 20)
            score -= character.getLocalRelevantEnemyList().size() * 10;
        if(character.getDangerLevels() > 300F)
            score -= 10000F;
        score += character.getLocalNeutralList().size() * 10;
        score += character.getDescriptor().getBravery() * 50F;
        score *= MasterSurvivorBehavior.AttackMultiplier;
        if(character.getTimeSinceZombieAttack() < 30)
        {
            score += 1000F;
            score *= 100F;
        }
        if(Target == null && score > 0.0F)
            score /= 100F;
        if(character.getStats().endurance < character.getStats().endurancedanger)
            score -= -10000F;
        if(character.getLocalRelevantEnemyList().isEmpty())
            return -1000000F;
        if(last == zombie.behaviors.Behavior.BehaviorResult.Failed && failedTimeout > 0)
        {
            failedTimeout--;
            return -100000F;
        } else
        {
            failedTimeout = 60;
            return score;
        }
    }

    public int renderDebug(int y)
    {
        int x = 50;
        TextManager.instance.DrawString(UIFont.Small, x, y, "AttackBehaviour", 1.0F, 1.0F, 1.0F, 1.0F);
        return y += 30;
    }

    public boolean HasRangeRequirement;
    public IsoGameCharacter RangeTest;
    public int TestRangeMax;
    public int thinkTime;
    public int thinkTimeMax;
    public boolean stayInside;
    PathFindBehavior pathFind;
    IsoGameCharacter Target;
    InventoryItem weapon;
    int timeout;
    IsoGridSquare backuppoint;
    boolean backingup;
    public boolean bWaitForThem;
    int nextbackuptest;
    private int failedTimeout;
    Vector2 a;
}
