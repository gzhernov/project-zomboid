// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   HandWeapon.java

package zombie.inventory.types;

import java.util.ArrayList;
import java.util.Stack;
import zombie.characters.CharacterTimedActions.BarricadeAction;
import zombie.characters.IsoGameCharacter;
import zombie.characters.SurvivorDesc;
import zombie.characters.skills.PerkFactory;
import zombie.inventory.*;
import zombie.scripting.objects.Item;
import zombie.ui.ObjectTooltip;

public class HandWeapon extends InventoryItem
{

    public float getSplatSize()
    {
        return SplatSize;
    }

    public boolean CanStack(InventoryItem item)
    {
        return "Plank".equals(type);
    }

    public HandWeapon(String module, String name, String itemType, String texName)
    {
        super(module, name, itemType, texName);
        SplatSize = 1.0F;
        ammoType = null;
        angleFalloff = false;
        bCanBarracade = false;
        directional = false;
        doSwingBeforeImpact = 0.0F;
        impactSound = "zombieImpact";
        knockBackOnNoDeath = true;
        maxAngle = 1.0F;
        maxDamage = 1.5F;
        maxHitCount = 1000;
        maxRange = 1.0F;
        ranged = false;
        minAngle = 0.5F;
        minDamage = 0.4F;
        minimumSwingTime = 0.5F;
        minRange = 0.0F;
        noiseFactor = 0.0F;
        otherHandRequire = null;
        otherHandUse = false;
        physicsObject = null;
        pushBackMod = 1.0F;
        rangeFalloff = false;
        shareDamage = true;
        soundRadius = 0;
        soundVolume = 0;
        splatBloodOnNoDeath = false;
        splatNumber = 2;
        swingSound = "batSwing";
        swingTime = 1.0F;
        toHitModifier = 1.0F;
        useEndurance = true;
        useSelf = false;
        weaponSprite = null;
        otherBoost = 1.0F;
        DoorDamage = 1;
        doorHitSound = "chopdoor";
        ConditionLowerChance = 10000;
        MultipleHitConditionAffected = true;
        shareEndurance = true;
        AlwaysKnockdown = false;
        EnduranceMod = 1.0F;
        KnockdownMod = 1.0F;
        CantAttackWithLowestEndurance = false;
        bIsAimedFirearm = false;
        bIsAimedHandWeapon = false;
        RunAnim = "Run";
        IdleAnim = "Idle";
        HitAngleMod = 0.0F;
        ProjectileCount = 1;
        aimingMod = 1.0F;
        cat = ItemType.Weapon;
    }

    public float getScore(SurvivorDesc desc)
    {
        float score = 0.0F;
        if(desc != null && !desc.getInstance().getCharacterActions().isEmpty() && (desc.getInstance().getCharacterActions().get(0) instanceof BarricadeAction) && bCanBarracade)
            return 1E+007F;
        if(ammoType != null && !ammoType.equals("none") && !container.contains(ammoType))
            score -= 100000F;
        if(Condition == 0)
            score -= 100000F;
        score += maxDamage * 10F;
        score += maxAngle * 5F;
        score -= minimumSwingTime * 0.1F;
        score -= swingTime;
        if(desc != null && desc.getInstance().getThreatLevel() <= 2 && soundRadius > 5)
        {
            if(score > 0.0F && (float)soundRadius > score)
                score = 1.0F;
            score -= soundRadius;
        }
        return score;
    }

    public boolean TestCanBarracade(IsoGameCharacter owner)
    {
        return owner.getInventory().contains("Nails") && owner.getInventory().contains("Plank");
    }

    public void DoTooltip(ObjectTooltip tooltipUI)
    {
        tooltipUI.render();
        super.DoTooltip(tooltipUI);
        int mid = 100;
        int y = tooltipUI.getHeight();
        y -= 19;
        if(Condition < ConditionMax)
        {
            tooltipUI.DrawText("Condition:", 5, y, 1.0F, 1.0F, 0.8F, 1.0F);
            Float delta = Float.valueOf((float)Condition / (float)ConditionMax);
            delta = Float.valueOf(delta.floatValue() * 100F);
            tooltipUI.DrawValueRight((int)delta.floatValue(), mid, y, true);
            y += 14;
        }
        if(CantAttackWithLowestEndurance)
        {
            y += 14;
            tooltipUI.DrawText("Unusable at max exertion.", 5, y, 1.0F, 0.0F, 0.0F, 1.0F);
            y += 14;
        }
    }

    public float getDamageMod(IsoGameCharacter chr)
    {
        int BluntLevel = chr.getPerkLevel(zombie.characters.skills.PerkFactory.Perks.Blunt);
        if(ScriptItem.Categories.contains("Blunt"))
        {
            if(BluntLevel == 1)
                return 1.1F;
            if(BluntLevel >= 2)
                return 1.2F;
        }
        int AxeLevel = chr.getPerkLevel(zombie.characters.skills.PerkFactory.Perks.Axe);
        if(ScriptItem.Categories.contains("Axe"))
        {
            if(AxeLevel == 1)
                return 1.1F;
            if(AxeLevel >= 2)
                return 1.2F;
        }
        return 1.0F;
    }

    public float getRangeMod(IsoGameCharacter chr)
    {
        int BluntLevel = chr.getPerkLevel(zombie.characters.skills.PerkFactory.Perks.Blunt);
        if(ScriptItem.Categories.contains("Blunt") && BluntLevel >= 4)
            return 1.2F;
        int AxeLevel = chr.getPerkLevel(zombie.characters.skills.PerkFactory.Perks.Axe);
        return !ScriptItem.Categories.contains("Axe") || AxeLevel < 4 ? 1.0F : 1.2F;
    }

    public float getFatigueMod(IsoGameCharacter chr)
    {
        int BluntLevel = chr.getPerkLevel(zombie.characters.skills.PerkFactory.Perks.Blunt);
        if(ScriptItem.Categories.contains("Blunt") && BluntLevel >= 5)
            return 0.8F;
        int AxeLevel = chr.getPerkLevel(zombie.characters.skills.PerkFactory.Perks.Axe);
        return !ScriptItem.Categories.contains("Axe") || AxeLevel < 5 ? 1.0F : 0.8F;
    }

    public float getKnockbackMod(IsoGameCharacter chr)
    {
        int AxeLevel = chr.getPerkLevel(zombie.characters.skills.PerkFactory.Perks.Axe);
        return !ScriptItem.Categories.contains("Axe") || AxeLevel < 3 ? 1.0F : 2.0F;
    }

    public float getSpeedMod(IsoGameCharacter chr)
    {
        int BluntLevel = chr.getPerkLevel(zombie.characters.skills.PerkFactory.Perks.Blunt);
        return !ScriptItem.Categories.contains("Blunt") || BluntLevel < 3 ? 1.0F : 0.75F;
    }

    public float getToHitMod(IsoGameCharacter chr)
    {
        int BluntLevel = chr.getPerkLevel(zombie.characters.skills.PerkFactory.Perks.Blunt);
        if(ScriptItem.Categories.contains("Blunt"))
        {
            if(BluntLevel == 1)
                return 1.2F;
            if(BluntLevel == 2)
                return 1.5F;
            if(BluntLevel == 3)
                return 1.75F;
            if(BluntLevel == 4)
                return 2.0F;
            if(BluntLevel == 5)
                return 100F;
        }
        int AxeLevel = chr.getPerkLevel(zombie.characters.skills.PerkFactory.Perks.Axe);
        if(ScriptItem.Categories.contains("Axe"))
        {
            if(AxeLevel == 1)
                return 1.2F;
            if(AxeLevel == 2)
                return 1.5F;
            if(AxeLevel == 3)
                return 1.75F;
            if(AxeLevel == 4)
                return 2.0F;
            if(AxeLevel == 5)
                return 100F;
        }
        return 1.0F;
    }

    public String getAmmoType()
    {
        return ammoType;
    }

    public void setAmmoType(String ammoType)
    {
        this.ammoType = ammoType;
    }

    public boolean isAngleFalloff()
    {
        return angleFalloff;
    }

    public void setAngleFalloff(boolean angleFalloff)
    {
        this.angleFalloff = angleFalloff;
    }

    public boolean isCanBarracade()
    {
        return bCanBarracade;
    }

    public void setCanBarracade(boolean bCanBarracade)
    {
        this.bCanBarracade = bCanBarracade;
    }

    public boolean isDirectional()
    {
        return directional;
    }

    public void setDirectional(boolean directional)
    {
        this.directional = directional;
    }

    public float getDoSwingBeforeImpact()
    {
        return doSwingBeforeImpact;
    }

    public void setDoSwingBeforeImpact(float doSwingBeforeImpact)
    {
        this.doSwingBeforeImpact = doSwingBeforeImpact;
    }

    public String getImpactSound()
    {
        return impactSound;
    }

    public void setImpactSound(String impactSound)
    {
        this.impactSound = impactSound;
    }

    public boolean isKnockBackOnNoDeath()
    {
        return knockBackOnNoDeath;
    }

    public void setKnockBackOnNoDeath(boolean knockBackOnNoDeath)
    {
        this.knockBackOnNoDeath = knockBackOnNoDeath;
    }

    public float getMaxAngle()
    {
        return maxAngle;
    }

    public void setMaxAngle(float maxAngle)
    {
        this.maxAngle = maxAngle;
    }

    public float getMaxDamage()
    {
        return maxDamage;
    }

    public void setMaxDamage(float maxDamage)
    {
        this.maxDamage = maxDamage;
    }

    public int getMaxHitCount()
    {
        return maxHitCount;
    }

    public void setMaxHitCount(int maxHitCount)
    {
        this.maxHitCount = maxHitCount;
    }

    public float getMaxRange()
    {
        return maxRange;
    }

    public void setMaxRange(float maxRange)
    {
        this.maxRange = maxRange;
    }

    public boolean isRanged()
    {
        return ranged;
    }

    public void setRanged(boolean ranged)
    {
        this.ranged = ranged;
    }

    public float getMinAngle()
    {
        return minAngle;
    }

    public void setMinAngle(float minAngle)
    {
        this.minAngle = minAngle;
    }

    public float getMinDamage()
    {
        return minDamage;
    }

    public void setMinDamage(float minDamage)
    {
        this.minDamage = minDamage;
    }

    public float getMinimumSwingTime()
    {
        return minimumSwingTime;
    }

    public void setMinimumSwingTime(float minimumSwingTime)
    {
        this.minimumSwingTime = minimumSwingTime;
    }

    public float getMinRange()
    {
        return minRange;
    }

    public void setMinRange(float minRange)
    {
        this.minRange = minRange;
    }

    public float getNoiseFactor()
    {
        return noiseFactor;
    }

    public void setNoiseFactor(float noiseFactor)
    {
        this.noiseFactor = noiseFactor;
    }

    public String getOtherHandRequire()
    {
        return otherHandRequire;
    }

    public void setOtherHandRequire(String otherHandRequire)
    {
        this.otherHandRequire = otherHandRequire;
    }

    public boolean isOtherHandUse()
    {
        return otherHandUse;
    }

    public void setOtherHandUse(boolean otherHandUse)
    {
        this.otherHandUse = otherHandUse;
    }

    public String getPhysicsObject()
    {
        return physicsObject;
    }

    public void setPhysicsObject(String physicsObject)
    {
        this.physicsObject = physicsObject;
    }

    public float getPushBackMod()
    {
        return pushBackMod;
    }

    public void setPushBackMod(float pushBackMod)
    {
        this.pushBackMod = pushBackMod;
    }

    public boolean isRangeFalloff()
    {
        return rangeFalloff;
    }

    public void setRangeFalloff(boolean rangeFalloff)
    {
        this.rangeFalloff = rangeFalloff;
    }

    public boolean isShareDamage()
    {
        return shareDamage;
    }

    public void setShareDamage(boolean shareDamage)
    {
        this.shareDamage = shareDamage;
    }

    public int getSoundRadius()
    {
        return soundRadius;
    }

    public void setSoundRadius(int soundRadius)
    {
        this.soundRadius = soundRadius;
    }

    public int getSoundVolume()
    {
        return soundVolume;
    }

    public void setSoundVolume(int soundVolume)
    {
        this.soundVolume = soundVolume;
    }

    public boolean isSplatBloodOnNoDeath()
    {
        return splatBloodOnNoDeath;
    }

    public void setSplatBloodOnNoDeath(boolean splatBloodOnNoDeath)
    {
        this.splatBloodOnNoDeath = splatBloodOnNoDeath;
    }

    public int getSplatNumber()
    {
        return splatNumber;
    }

    public void setSplatNumber(int splatNumber)
    {
        this.splatNumber = splatNumber;
    }

    public String getSwingSound()
    {
        return swingSound;
    }

    public void setSwingSound(String swingSound)
    {
        this.swingSound = swingSound;
    }

    public float getSwingTime()
    {
        return swingTime;
    }

    public void setSwingTime(float swingTime)
    {
        this.swingTime = swingTime;
    }

    public float getToHitModifier()
    {
        return toHitModifier;
    }

    public void setToHitModifier(float toHitModifier)
    {
        this.toHitModifier = toHitModifier;
    }

    public boolean isUseEndurance()
    {
        return useEndurance;
    }

    public void setUseEndurance(boolean useEndurance)
    {
        this.useEndurance = useEndurance;
    }

    public boolean isUseSelf()
    {
        return useSelf;
    }

    public void setUseSelf(boolean useSelf)
    {
        this.useSelf = useSelf;
    }

    public String getWeaponSprite()
    {
        return weaponSprite;
    }

    public void setWeaponSprite(String weaponSprite)
    {
        this.weaponSprite = weaponSprite;
    }

    public float getOtherBoost()
    {
        return otherBoost;
    }

    public void setOtherBoost(float otherBoost)
    {
        this.otherBoost = otherBoost;
    }

    public int getDoorDamage()
    {
        return DoorDamage;
    }

    public void setDoorDamage(int DoorDamage)
    {
        this.DoorDamage = DoorDamage;
    }

    public String getDoorHitSound()
    {
        return doorHitSound;
    }

    public void setDoorHitSound(String doorHitSound)
    {
        this.doorHitSound = doorHitSound;
    }

    public int getConditionLowerChance()
    {
        return ConditionLowerChance;
    }

    public void setConditionLowerChance(int ConditionLowerChance)
    {
        this.ConditionLowerChance = ConditionLowerChance;
    }

    public boolean isMultipleHitConditionAffected()
    {
        return MultipleHitConditionAffected;
    }

    public void setMultipleHitConditionAffected(boolean MultipleHitConditionAffected)
    {
        this.MultipleHitConditionAffected = MultipleHitConditionAffected;
    }

    public boolean isShareEndurance()
    {
        return shareEndurance;
    }

    public void setShareEndurance(boolean shareEndurance)
    {
        this.shareEndurance = shareEndurance;
    }

    public boolean isAlwaysKnockdown()
    {
        return AlwaysKnockdown;
    }

    public void setAlwaysKnockdown(boolean AlwaysKnockdown)
    {
        this.AlwaysKnockdown = AlwaysKnockdown;
    }

    public float getEnduranceMod()
    {
        return EnduranceMod;
    }

    public void setEnduranceMod(float EnduranceMod)
    {
        this.EnduranceMod = EnduranceMod;
    }

    public float getKnockdownMod()
    {
        return KnockdownMod;
    }

    public void setKnockdownMod(float KnockdownMod)
    {
        this.KnockdownMod = KnockdownMod;
    }

    public boolean isCantAttackWithLowestEndurance()
    {
        return CantAttackWithLowestEndurance;
    }

    public void setCantAttackWithLowestEndurance(boolean CantAttackWithLowestEndurance)
    {
        this.CantAttackWithLowestEndurance = CantAttackWithLowestEndurance;
    }

    public boolean isAimedFirearm()
    {
        return bIsAimedFirearm;
    }

    public boolean isAimedHandWeapon()
    {
        return bIsAimedHandWeapon;
    }

    public int getProjectileCount()
    {
        return ProjectileCount;
    }

    public float getAimingMod()
    {
        return aimingMod;
    }

    public boolean isAimed()
    {
        return bIsAimedFirearm || bIsAimedHandWeapon;
    }

    public float SplatSize;
    protected String ammoType;
    protected boolean angleFalloff;
    protected boolean bCanBarracade;
    protected boolean directional;
    protected float doSwingBeforeImpact;
    protected String impactSound;
    protected boolean knockBackOnNoDeath;
    protected float maxAngle;
    protected float maxDamage;
    protected int maxHitCount;
    protected float maxRange;
    protected boolean ranged;
    protected float minAngle;
    protected float minDamage;
    protected float minimumSwingTime;
    protected float minRange;
    protected float noiseFactor;
    protected String otherHandRequire;
    protected boolean otherHandUse;
    protected String physicsObject;
    protected float pushBackMod;
    protected boolean rangeFalloff;
    protected boolean shareDamage;
    protected int soundRadius;
    protected int soundVolume;
    protected boolean splatBloodOnNoDeath;
    protected int splatNumber;
    protected String swingSound;
    protected float swingTime;
    protected float toHitModifier;
    protected boolean useEndurance;
    protected boolean useSelf;
    protected String weaponSprite;
    protected float otherBoost;
    protected int DoorDamage;
    protected String doorHitSound;
    protected int ConditionLowerChance;
    protected boolean MultipleHitConditionAffected;
    protected boolean shareEndurance;
    protected boolean AlwaysKnockdown;
    protected float EnduranceMod;
    protected float KnockdownMod;
    protected boolean CantAttackWithLowestEndurance;
    public boolean bIsAimedFirearm;
    public boolean bIsAimedHandWeapon;
    public String RunAnim;
    public String IdleAnim;
    public float HitAngleMod;
    public int ProjectileCount;
    public float aimingMod;
}
