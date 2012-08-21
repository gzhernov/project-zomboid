// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 13.08.2012 13:54:46
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   SwipeStatePlayer.java

package zombie.ai.states;

import java.util.Iterator;
import java.util.Stack;
import zombie.Lua.LuaEventManager;
import zombie.Lua.LuaHookManager;
import zombie.SoundManager;
import zombie.WorldSoundManager;
import zombie.ai.State;
import zombie.ai.StateMachine;
import zombie.characters.*;
import zombie.characters.skills.PerkFactory;
import zombie.core.Collections.NulledArrayList;
import zombie.core.Core;
import zombie.core.Rand;
import zombie.input.Mouse;
import zombie.inventory.*;
import zombie.inventory.types.HandWeapon;
import zombie.iso.*;
import zombie.iso.objects.*;
import zombie.iso.sprite.*;

public class SwipeStatePlayer extends State
{

    public SwipeStatePlayer()
    {
        HitList = new Stack();
        RemoveList = new Stack();
    }

    public static SwipeStatePlayer instance()
    {
        return _instance;
    }

    public void WeaponLowerCondition(HandWeapon weapon, IsoGameCharacter owner)
    {
        if(weapon.getUses() > 1)
        {
            weapon.Use();
            InventoryItem item = InventoryItemFactory.CreateItem((new StringBuilder()).append(weapon.getModule()).append(".").append(weapon.getType()).toString());
            ((HandWeapon)item).setCondition(weapon.getCondition() - 1);
            weapon.getContainer().AddItem(item);
            owner.setPrimaryHandItem(item);
            if(item.getCondition() <= 0)
            {
                HandWeapon weap = (HandWeapon)owner.getInventory().getBestWeapon(owner.getDescriptor());
                if(weap != null)
                    owner.setPrimaryHandItem(weap);
            }
        } else
        {
            weapon.setCondition(weapon.getCondition() - 1);
            if(weapon.getCondition() <= 0)
            {
                HandWeapon weap = (HandWeapon)owner.getInventory().getBestWeapon(owner.getDescriptor());
                if(weap != null)
                    owner.setPrimaryHandItem(weap);
            }
        }
    }

    public void enter(IsoGameCharacter owner)
    {
        HandWeapon weapon = owner.getUseHandWeapon();
        LuaEventManager.TriggerEvent("OnWeaponSwing", owner, weapon);
        if(LuaHookManager.TriggerHook("WeaponSwing", owner, weapon))
            owner.getStateMachine().RevertToPrevious();
        ((IsoLivingCharacter)owner).bDoShove = false;
        float lowestDist = 1E+008F;
        int n = 0;
        do
        {
            if(n >= owner.getCell().getObjectList().size())
                break;
            IsoMovingObject obj = (IsoMovingObject)owner.getCell().getObjectList().get(n);
            if(obj.isShootable() && (obj instanceof IsoZombie))
            {
                float dist = IsoUtils.DistanceTo(obj.x, obj.y, owner.x, owner.y);
                if(dist < lowestDist && dist < 3F)
                {
                    Vector2 oPos = new Vector2(owner.getX(), owner.getY());
                    Vector2 tPos = new Vector2(obj.getX(), obj.getY());
                    tPos.x -= oPos.x;
                    tPos.y -= oPos.y;
                    Vector2 dir = owner.getAngle();
                    tPos.normalize();
                    dir.normalize();
                    float dot = tPos.dot(dir);
                    if(dot > ((IsoLivingCharacter)owner).bareHands.getMinAngle())
                        lowestDist = dist;
                }
            }
            if(lowestDist < weapon.getMinRange())
            {
                ((IsoLivingCharacter)owner).bDoShove = true;
                if(((IsoLivingCharacter)owner).bareHands.getSwingAnim() != null)
                {
                    owner.PlayAnimUnlooped((new StringBuilder()).append("Attack_").append(((IsoLivingCharacter)owner).bareHands.getSwingAnim()).toString());
                    owner.def.setFrameSpeedPerFrame(1.0F / (((IsoLivingCharacter)owner).bareHands.getSwingTime() * 1.0F));
                }
                break;
            }
            n++;
        } while(true);
        if(!((IsoLivingCharacter)owner).bDoShove)
        {
            SoundManager.instance.PlaySound(weapon.getSwingSound(), false, 1.0F);
            WorldSoundManager.instance.addSound(owner, (int)owner.getX(), (int)owner.getY(), (int)owner.getZ(), weapon.getSoundRadius(), weapon.getSoundVolume());
        }
    }

    public void execute(IsoGameCharacter owner)
    {
        owner.StopAllActionQueue();
        HandWeapon weapon = owner.getUseHandWeapon();
        if(weapon.getCondition() <= 0)
            weapon = null;
        if(IsoBarricade.BarricadeFed)
            IsoObject.setDefaultCondition(10);
        if(((IsoLivingCharacter)owner).bDoShove)
            weapon = ((IsoLivingCharacter)owner).bareHands;
        RemoveList.clear();
        if(weapon != null && owner.getAttackDelay() == owner.getAttackDelayUse())
        {
            LuaEventManager.TriggerEvent("OnWeaponSwingHitPoint", owner, weapon);
            owner.getXp().AddXP(zombie.characters.skills.PerkFactory.Perks.Swinging, 1);
            HitList.clear();
            if(weapon.isUseEndurance())
                owner.getStats().endurance -= weapon.getWeight() * IsoPlayer.instance.useChargeDelta * 0.17F * weapon.getFatigueMod(owner) * owner.getFatigueMod() * weapon.getEnduranceMod();
            if(weapon.getPhysicsObject() != null)
                owner.Throw(weapon.getPhysicsObject());
            boolean bIgnoreDamage = false;
            int hitCount = 0;
            if((owner instanceof IsoPlayer) && weapon.isAimedFirearm())
            {
                bIgnoreDamage = true;
                float mx = Mouse.getX();
                float my = Mouse.getY();
                Vector2 vec = new Vector2(0.0F, 0.0F);
                float rad = (float)Rand.Next((int)(IsoPlayer.instance.AimRadius * 1000F)) / 1000F;
                if(Core.bDoubleSize)
                    rad *= 2.0F;
                int a = 1 * owner.getHitChancesMod();
                if(IsoPlayer.instance.EffectiveAimDistance < 1.0F)
                    a = 20;
                int pc = weapon.getProjectileCount();
                a *= pc;
                a = (int)((float)a * weapon.getToHitModifier());
                int n = 0;
                do
                {
                    if(n >= a)
                        break;
                    vec.x = (float)Rand.Next(2000) / 1000F - 1.0F;
                    vec.y = (float)Rand.Next(2000) / 1000F - 1.0F;
                    vec.normalize();
                    vec.setLength(rad);
                    IsoMovingObject o = IsoObjectPicker.Instance.PickTarget((int)(vec.x + mx), (int)(vec.y + my));
                    if(o != null)
                        if(IsoPlayer.instance.EffectiveAimDistance > 1.0F && !IsoObjectPicker.Instance.IsHeadShot(o, (int)(vec.x + mx), (int)(vec.y + my)))
                        {
                            if(!HitList.contains(o))
                            {
                                o.noDamage = true;
                                owner.getXp().AddXP(zombie.characters.skills.PerkFactory.Perks.Aiming, 3);
                                hitCount++;
                                HitList.add(o);
                                if(hitCount >= weapon.getMaxHitCount())
                                    break;
                            }
                        } else
                        {
                            bIgnoreDamage = false;
                            o.noDamage = false;
                            if(!HitList.contains(o))
                            {
                                owner.getXp().AddXP(zombie.characters.skills.PerkFactory.Perks.Aiming, 3);
                                hitCount++;
                                HitList.add(o);
                                if(hitCount >= weapon.getMaxHitCount())
                                    break;
                            }
                        }
                    if(o != null && n == a - 1 && !HitList.contains(o))
                    {
                        hitCount++;
                        HitList.add(o);
                        if(hitCount >= weapon.getMaxHitCount())
                            break;
                    }
                    n++;
                } while(true);
            } else
            {
                CheckObjectHit(owner, weapon);
                if(hitCount < weapon.getMaxHitCount())
                    if(((IsoLivingCharacter)owner).bDoShove)
                    {
                        int n = 0;
                        do
                        {
                            if(n >= owner.getCell().getObjectList().size())
                                break;
                            IsoMovingObject obj = (IsoMovingObject)owner.getCell().getObjectList().get(n);
                            if(obj.isShootable() && owner.IsAttackRange(weapon, obj.getX(), obj.getY(), obj.getZ()) && !weapon.isDirectional())
                            {
                                Vector2 oPos = new Vector2(owner.getX(), owner.getY());
                                Vector2 tPos = new Vector2(obj.getX(), obj.getY());
                                tPos.x -= oPos.x;
                                tPos.y -= oPos.y;
                                Vector2 dir = owner.getAngle();
                                owner.DirectionFromVector(dir);
                                tPos.normalize();
                                float dot = tPos.dot(dir);
                                if(!weapon.isRanged() && owner.getDescriptor() != null && !(obj instanceof IsoZombie))
                                {
                                    if(!owner.getDescriptor().InGroupWith(obj) && (owner instanceof IsoSurvivor) && (obj instanceof IsoGameCharacter))
                                        if(owner.getEnemyList().contains((IsoGameCharacter)obj));
                                    RemoveList.add(obj);
                                }
                                if(dot > 1.0F)
                                    dot = 1.0F;
                                if(dot < -1F)
                                    dot = -1F;
                                if(dot >= weapon.getMinAngle() && dot <= weapon.getMaxAngle())
                                {
                                    HitList.add(obj);
                                    obj.setHitFromAngle(dot);
                                    hitCount++;
                                }
                                if(hitCount >= weapon.getMaxHitCount())
                                    break;
                            }
                            n++;
                        } while(true);
                    } else
                    {
                        float mx = Mouse.getX();
                        float my = Mouse.getY();
                        Vector2 vec = new Vector2(0.0F, 0.0F);
                        int a = weapon.getMaxHitCount() - hitCount;
                        boolean Accuracy = weapon.isAimedHandWeapon();
                        if(Accuracy)
                            a = 20;
                        int chancesToHeadShot = IsoPlayer.instance.getChancesToHeadshotHandWeapon();
                        chancesToHeadShot *= weapon.getProjectileCount();
                        for(int n = 0; n < a; n++)
                        {
                            float rad = (float)Rand.Next((int)(IsoPlayer.instance.AimRadius * 1000F)) / 1000F;
                            if(Core.bDoubleSize)
                                rad *= 2.0F;
                            vec.x = (float)Rand.Next(2000) / 1000F - 1.0F;
                            vec.y = (float)Rand.Next(2000) / 1000F - 1.0F;
                            vec.normalize();
                            vec.setLength(rad);
                            IsoMovingObject o = IsoObjectPicker.Instance.PickTarget((int)(vec.x + mx), (int)(vec.y + my));
                            if(o == null || !o.isShootable() || !owner.IsAttackRange(weapon, o.getX(), o.getY(), o.getZ()))
                                continue;
                            Vector2 oPos = new Vector2(owner.getX(), owner.getY());
                            Vector2 tPos = new Vector2(o.getX(), o.getY());
                            tPos.x -= oPos.x;
                            tPos.y -= oPos.y;
                            Vector2 dir = owner.getAngle();
                            owner.DirectionFromVector(dir);
                            tPos.normalize();
                            float dot = tPos.dot(dir);
                            if(dot > 1.0F)
                                dot = 1.0F;
                            if(dot < -1F)
                                dot = -1F;
                            if(dot < weapon.getMinAngle() || dot > weapon.getMaxAngle())
                                continue;
                            if(!HitList.contains(o) && hitCount < weapon.getMaxHitCount())
                            {
                                HitList.add(o);
                                o.setHitFromAngle(dot);
                                if(Accuracy)
                                {
                                    o.noDamage = IsoObjectPicker.Instance.IsHeadShot(o, (int)(vec.x + mx), (int)(vec.y + my));
                                    if(chancesToHeadShot == 0)
                                        break;
                                    chancesToHeadShot--;
                                } else
                                {
                                    o.noDamage = false;
                                }
                                hitCount++;
                                continue;
                            }
                            if(o.noDamage && IsoObjectPicker.Instance.IsHeadShot(o, (int)(vec.x + mx), (int)(vec.y + my)))
                                o.noDamage = false;
                        }

                    }
                if(RemoveList.size() != HitList.size())
                {
                    HitList.removeAll(RemoveList);
                    hitCount = HitList.size();
                }
                if((owner instanceof IsoPlayer) && ((IsoPlayer)(IsoPlayer)owner).isFakeAttack())
                {
                    HitList.clear();
                    hitCount = 0;
                    ((IsoPlayer)(IsoPlayer)owner).setFakeAttack(false);
                    ((IsoPlayer)(IsoPlayer)owner).getFakeAttackTarget().AttackObject(owner);
                }
            }
            if(owner.getStats().endurance > owner.getStats().endurancewarn && !weapon.isRanged())
                owner.getXp().AddXP(zombie.characters.skills.PerkFactory.Perks.Swinging, 1);
            if(!weapon.isRanged() && hitCount > 1)
                owner.getXp().AddXP(zombie.characters.skills.PerkFactory.Perks.Hitting, hitCount / 2);
            if(hitCount > 0)
                owner.getXp().AddXP(weapon, 1);
            if(hitCount > 0 && weapon.getImpactSound() != null && (owner instanceof IsoPlayer))
                SoundManager.instance.PlaySound(weapon.getImpactSound(), false, 0.2F);
            if(!weapon.isMultipleHitConditionAffected() && Rand.Next(weapon.getConditionLowerChance()) == 0)
                WeaponLowerCondition(weapon, owner);
            int split = 1;
            Iterator i$ = HitList.iterator();
            do
            {
                if(!i$.hasNext())
                    break;
                IsoMovingObject obj = (IsoMovingObject)i$.next();
                if(LosUtil.lineClear(owner.getCell(), (int)owner.getX(), (int)owner.getY(), (int)owner.getZ(), (int)obj.getX(), (int)obj.getY(), (int)obj.getZ(), false) != zombie.iso.LosUtil.TestResults.Blocked)
                {
                    float dam = weapon.getMinDamage();
                    float dif = weapon.getMaxDamage() - weapon.getMinDamage();
                    if(dif == 0.0F)
                        dam += 0.0F;
                    else
                        dam += (float)Rand.Next((int)(dif * 1000F)) / 1000F;
                    if(!weapon.isRanged())
                        dam *= weapon.getDamageMod(owner) * owner.getHittingMod();
                    float damageSplit = dam / (float)split;
                    if(owner.isAttackWasSuperAttack())
                        damageSplit *= 10F;
                    split += 2;
                    if(weapon.isUseEndurance() && weapon.isShareEndurance())
                    {
                        owner.getStats().endurance -= weapon.getWeight() * IsoPlayer.instance.useChargeDelta * 0.04F * weapon.getFatigueMod(owner) * owner.getFatigueMod() * weapon.getEnduranceMod();
                        damageSplit *= owner.getStats().endurance;
                    }
                    if(weapon.isMultipleHitConditionAffected() && Rand.Next(weapon.getConditionLowerChance()) == 0)
                        WeaponLowerCondition(weapon, owner);
                    Vector2 oPos = new Vector2(owner.getX(), owner.getY());
                    Vector2 tPos = new Vector2(obj.getX(), obj.getY());
                    tPos.x -= oPos.x;
                    tPos.y -= oPos.y;
                    float dist = tPos.getLength();
                    float rangeDel = 1.0F;
                    if(!weapon.isRangeFalloff())
                        rangeDel = dist / weapon.getMaxRange();
                    if(rangeDel < 0.3F)
                        rangeDel = 1.0F;
                    obj.Hit(weapon, owner, damageSplit, bIgnoreDamage, rangeDel);
                    if((obj instanceof IsoGameCharacter) && ((IsoGameCharacter)obj).getHealth() <= 0.0F)
                    {
                        owner.getStats().stress -= 0.02F;
                        if(owner instanceof IsoSurvivor)
                            ((IsoSurvivor)owner).Killed((IsoGameCharacter)obj);
                    }
                }
            } while(true);
            if((owner instanceof IsoPlayer) && weapon.isAimedFirearm())
                ((IsoPlayer)owner).AimRadius += ((IsoPlayer)owner).getRadiusKickback(weapon);
        }
        if(owner.getAttackDelay() <= -5 || owner.def.Frame >= (float)(owner.sprite.CurrentAnim.Frames.size() - 1))
        {
            owner.setAttackDelay(-1);
            owner.getStateMachine().RevertToPrevious();
            owner.getStateMachine().Lock = false;
            if(owner.sprite.CurrentAnim.name.contains("Shove"))
                owner.PlayAnim("Idle");
        }
    }

    private void CheckObjectHit(IsoGameCharacter owner, HandWeapon weapon)
    {
        IsoDirections dir = IsoDirections.fromAngle(owner.getAngle());
        int x = 0;
        int y = 0;
        if(dir == IsoDirections.NE || dir == IsoDirections.N || dir == IsoDirections.NW)
            y--;
        if(dir == IsoDirections.SE || dir == IsoDirections.S || dir == IsoDirections.SW)
            y++;
        if(dir == IsoDirections.NW || dir == IsoDirections.W || dir == IsoDirections.SW)
            x--;
        if(dir == IsoDirections.NE || dir == IsoDirections.E || dir == IsoDirections.SE)
            x++;
        boolean bDoorOnSrc = false;
        IsoGridSquare next = owner.getCurrentSquare().getCell().getGridSquare(owner.getCurrentSquare().getX() + x, owner.getCurrentSquare().getY() + y, owner.getCurrentSquare().getZ());
        if(next != null)
        {
            for(int n = 0; n < next.getSpecialObjects().size(); n++)
            {
                IsoObject special = (IsoObject)next.getSpecialObjects().get(n);
                if(special instanceof IsoBarricade)
                    ((IsoBarricade)special).WeaponHit(owner, weapon);
                if(special instanceof IsoWindow)
                    ((IsoWindow)special).WeaponHit(owner, weapon);
            }

            for(int n = 0; n < next.getObjects().size(); n++)
            {
                IsoObject special = (IsoObject)next.getObjects().get(n);
                if(special instanceof IsoTree)
                    ((IsoTree)special).WeaponHit(owner, weapon);
            }

        }
        if(dir == IsoDirections.NE || dir == IsoDirections.N || dir == IsoDirections.NW)
        {
            for(int n = 0; n < owner.getCurrentSquare().getSpecialObjects().size(); n++)
            {
                IsoObject special = (IsoObject)owner.getCurrentSquare().getSpecialObjects().get(n);
                if((special instanceof IsoDoor) && ((IsoDoor)special).north && !((IsoDoor)special).open)
                    ((IsoDoor)special).WeaponHit(owner, weapon);
                if((special instanceof IsoWindow) && ((IsoWindow)special).north)
                    ((IsoWindow)special).WeaponHit(owner, weapon);
            }

        }
        if(dir == IsoDirections.SE || dir == IsoDirections.S || dir == IsoDirections.SW)
        {
            IsoGridSquare sq = owner.getCell().getGridSquare(owner.getCurrentSquare().getX(), owner.getCurrentSquare().getY() + 1, owner.getCurrentSquare().getZ());
            if(sq != null)
            {
                for(int n = 0; n < sq.getSpecialObjects().size(); n++)
                {
                    IsoObject special = (IsoObject)sq.getSpecialObjects().get(n);
                    if((special instanceof IsoDoor) && ((IsoDoor)special).north)
                        ((IsoDoor)special).WeaponHit(owner, weapon);
                    if((special instanceof IsoWindow) && ((IsoWindow)special).north)
                        ((IsoWindow)special).WeaponHit(owner, weapon);
                }

            }
        }
        if(dir == IsoDirections.SE || dir == IsoDirections.E || dir == IsoDirections.NE)
        {
            IsoGridSquare sq = owner.getCell().getGridSquare(owner.getCurrentSquare().getX() + 1, owner.getCurrentSquare().getY(), owner.getCurrentSquare().getZ());
            if(sq != null)
            {
                for(int n = 0; n < sq.getSpecialObjects().size(); n++)
                {
                    IsoObject special = (IsoObject)sq.getSpecialObjects().get(n);
                    if((special instanceof IsoDoor) && !((IsoDoor)special).north)
                        ((IsoDoor)special).WeaponHit(owner, weapon);
                    if((special instanceof IsoWindow) && !((IsoWindow)special).north)
                        ((IsoWindow)special).WeaponHit(owner, weapon);
                }

            }
        }
        if(dir == IsoDirections.NW || dir == IsoDirections.W || dir == IsoDirections.SW)
        {
            for(int n = 0; n < owner.getCurrentSquare().getSpecialObjects().size(); n++)
            {
                IsoObject special = (IsoObject)owner.getCurrentSquare().getSpecialObjects().get(n);
                if((special instanceof IsoDoor) && !((IsoDoor)special).north)
                    ((IsoDoor)special).WeaponHit(owner, weapon);
                if((special instanceof IsoWindow) && !((IsoWindow)special).north)
                    ((IsoWindow)special).WeaponHit(owner, weapon);
            }

        }
    }

    static SwipeStatePlayer _instance = new SwipeStatePlayer();
    Stack HitList;
    Stack RemoveList;

}