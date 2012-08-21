// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   IsoLivingCharacter.java

package zombie.characters;

import zombie.GameTime;
import zombie.Lua.LuaHookManager;
import zombie.SoundManager;
import zombie.WorldSoundManager;
import zombie.ai.StateMachine;
import zombie.ai.states.SwipeState;
import zombie.characters.BodyDamage.BodyDamage;
import zombie.core.Collections.NulledArrayList;
import zombie.inventory.*;
import zombie.inventory.types.HandWeapon;
import zombie.iso.*;
import zombie.iso.sprite.*;
import zombie.ui.UIManager;

// Referenced classes of package zombie.characters:
//            IsoGameCharacter, IsoPlayer, IsoSurvivor, IsoZombie, 
//            SurvivorDesc

public class IsoLivingCharacter extends IsoGameCharacter
{

    public IsoLivingCharacter(IsoCell cell, float x, float y, float z)
    {
        super(cell, x, y, z);
        bDoShove = false;
        bareHands = (HandWeapon)InventoryItemFactory.CreateItem("Base.BareHands");
    }

    public boolean AttemptAttack(float ChargeDelta)
    {
        if(LuaHookManager.TriggerHook("Attack", this, Float.valueOf(ChargeDelta)))
            return false;
        else
            return DoAttack(ChargeDelta);
    }

    public boolean DoAttack(float ChargeDelta)
    {
        if(this instanceof IsoPlayer)
            ((IsoPlayer)(IsoPlayer)this).FakeAttack = false;
        if(Health <= 0.0F || BodyDamage.getHealth() < 0.0F)
            return false;
        if(leftHandItem != null && AttackDelay <= 0)
        {
            InventoryItem attackItem = leftHandItem;
            if(attackItem instanceof HandWeapon)
            {
                useHandWeapon = (HandWeapon)attackItem;
                if(useHandWeapon.getCondition() <= 0)
                    return false;
                if(useHandWeapon.isCantAttackWithLowestEndurance() && stats.endurance < stats.endurancedanger)
                    return false;
                int hitCount = 0;
                if((this instanceof IsoSurvivor) && useHandWeapon.isRanged() && hitCount < useHandWeapon.getMaxHitCount())
                {
                    for(int n = 0; n < getCell().getObjectList().size(); n++)
                    {
                        IsoMovingObject obj = (IsoMovingObject)getCell().getObjectList().get(n);
                        if(obj == this || !obj.isShootable() || !IsAttackRange(obj.getX(), obj.getY(), obj.getZ()) || useHandWeapon.isDirectional())
                            continue;
                        float delta = obj.getSeeValue();
                        if(!(this instanceof IsoPlayer))
                            delta = 1.0F;
                        if(delta <= 0.0F)
                            continue;
                        Vector2 oPos = new Vector2(getX(), getY());
                        Vector2 tPos = new Vector2(obj.getX(), obj.getY());
                        tPos.x -= oPos.x;
                        tPos.y -= oPos.y;
                        boolean bZero = false;
                        if(tPos.x == 0.0F && tPos.y == 0.0F)
                            bZero = true;
                        Vector2 dir = angle;
                        DirectionFromVector(dir);
                        tPos.normalize();
                        float dot = tPos.dot(dir);
                        if(bZero)
                            dot = 1.0F;
                        if(dot > 1.0F)
                            dot = 1.0F;
                        if(dot < -1F)
                            dot = -1F;
                        if(dot >= useHandWeapon.getMinAngle() && dot <= useHandWeapon.getMaxAngle())
                        {
                            if(descriptor != null && !(obj instanceof IsoZombie))
                            {
                                if(!descriptor.InGroupWith(obj));
                                return false;
                            }
                            hitCount++;
                        }
                        if(hitCount >= useHandWeapon.getMaxHitCount())
                            break;
                    }

                }
                if(UIManager.getPicked() != null)
                {
                    attackTargetSquare = UIManager.getPicked().square;
                    if(UIManager.getPicked().tile instanceof IsoMovingObject)
                        attackTargetSquare = ((IsoMovingObject)UIManager.getPicked().tile).getCurrentSquare();
                }
                if(useHandWeapon.getAmmoType() != null && !inventory.contains(useHandWeapon.getAmmoType()))
                    return false;
                if(useHandWeapon.getOtherHandRequire() != null && (rightHandItem == null || !rightHandItem.getType().equals(useHandWeapon.getOtherHandRequire())))
                    return false;
                if(attackItem.getSwingAnim() != null)
                {
                    def.Finished = false;
                    PlayAnimUnlooped((new StringBuilder()).append("Attack_").append(attackItem.getSwingAnim()).toString());
                    sprite.CurrentAnim.FinishUnloopedOnFrame = 0;
                }
                float val = useHandWeapon.getSwingTime();
                if(useHandWeapon.isUseEndurance())
                    val *= 1.0F - stats.endurance;
                if(val < useHandWeapon.getMinimumSwingTime())
                    val = useHandWeapon.getMinimumSwingTime();
                if(this instanceof IsoPlayer)
                    SoundManager.instance.PlaySound(useHandWeapon.getSwingSound(), false, 0.9F);
                else
                    SoundManager.instance.PlayWorldSound(useHandWeapon.getSwingSound(), getCurrentSquare(), 0.0F, (float)useHandWeapon.getSoundRadius() * useHandWeapon.getOtherBoost(), 1.0F, true);
                WorldSoundManager.instance.addSound(this, (int)getX(), (int)getY(), (int)getZ(), useHandWeapon.getSoundRadius(), useHandWeapon.getSoundVolume());
                val *= useHandWeapon.getSpeedMod(this);
                val *= 1.0F / GameTime.instance.getMultiplier();
                AttackDelayMax = AttackDelay = (int)(val * 60F);
                AttackDelayUse = (int)((float)AttackDelayMax * useHandWeapon.getDoSwingBeforeImpact());
                AttackDelayUse = AttackDelayMax - AttackDelayUse - 2;
                AttackWasSuperAttack = superAttack;
                stateMachine.changeState(SwipeState.instance());
                if(useHandWeapon.getAmmoType() != null)
                    if(this instanceof IsoPlayer)
                        IsoPlayer.instance.inventory.RemoveOneOf(useHandWeapon.getAmmoType());
                    else
                        inventory.RemoveOneOf(useHandWeapon.getAmmoType());
                if(useHandWeapon.isUseSelf() && leftHandItem != null)
                    leftHandItem.Use();
                if(useHandWeapon.isOtherHandUse() && rightHandItem != null)
                    rightHandItem.Use();
                return true;
            }
        }
        return false;
    }

    public HandWeapon bareHands;
    public boolean bDoShove;
}
