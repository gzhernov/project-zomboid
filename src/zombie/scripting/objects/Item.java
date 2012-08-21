// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Item.java

package zombie.scripting.objects;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Stack;
import se.krka.kahlua.j2se.J2SEPlatform;
import se.krka.kahlua.vm.KahluaTable;
import zombie.Lua.LuaManager;
import zombie.core.Rand;
import zombie.inventory.InventoryItem;
import zombie.inventory.types.*;

// Referenced classes of package zombie.scripting.objects:
//            BaseScriptObject, ScriptModule

public class Item extends BaseScriptObject
{

	/*      */   public static enum ClothingBodyLocation
	/*      */   {
	/* 1136 */     None, 
	/* 1137 */     Head, 
	/* 1138 */     Top, 
	/* 1139 */     Bottoms, 
	/* 1140 */     Shoes;
	/*      */   }
	

	/*      */   public static enum Type
	/*      */   {
	/* 1148 */     Normal, 
	/* 1149 */     Weapon, 
	/* 1150 */     Food, 
	/* 1151 */     Literature, 
	/* 1152 */     Drainable, 
	/* 1153 */     Clothing;
	/*      */   }


    public Item()
    {
        DisplayName = "Blooo";
        Icon = "None";
        ActualWeight = 1.0F;
        HungerChange = 0.0F;
        Count = 1;
        DaysFresh = 0x3b9aca00;
        DaysTotallyRotten = 0x3b9aca00;
        MinutesToCook = 60;
        MinutesToBurn = 120;
        IsCookable = false;
        StressChange = 0.0F;
        BoredomChange = 0.0F;
        UnhappyChange = 0.0F;
        AlwaysWelcomeGift = false;
        ReplaceOnDeplete = null;
        Ranged = false;
        CanStoreWater = false;
        MaxRange = 1.0F;
        MinRange = 0.0F;
        ThistChange = 0.0F;
        MinAngle = 1.0F;
        MaxDamage = 1.5F;
        MinDamage = 0.0F;
        MinimumSwingTime = 0.0F;
        SwingSound = "batSwing";
        AngleFalloff = false;
        SoundVolume = 0;
        ToHitModifier = 1.0F;
        SoundRadius = 0;
        Categories = new ArrayList();
        ImpactSound = "zombieImpact";
        SwingTime = 1.0F;
        KnockBackOnNoDeath = true;
        SplatBloodOnNoDeath = false;
        SwingAmountBeforeImpact = 0.0F;
        AmmoType = null;
        DoorDamage = 1;
        ConditionLowerChance = 0xf4240;
        ConditionMax = 100;
        CanBandage = false;
        MaxHitCount = 1000;
        UseSelf = false;
        OtherHandUse = false;
        SwingAnim = "Rifle";
        WeaponWeight = 1.0F;
        EnduranceChange = 0.0F;
        IdleAnim = "Idle";
        RunAnim = "Run";
        RequireInHandOrInventory = null;
        DoorHitSound = "chopdoor";
        ReplaceOnUse = null;
        DangerousUncooked = false;
        Alcoholic = false;
        PushBackMod = 1.0F;
        SplatNumber = 2;
        NPCSoundBoost = 1.0F;
        RangeFalloff = false;
        UseEndurance = true;
        MultipleHitConditionAffected = true;
        ShareDamage = true;
        ShareEndurance = false;
        CanBarricade = false;
        UseWhileEquipped = true;
        DisappearOnUse = true;
        UseDelta = 0.03125F;
        AlwaysKnockdown = false;
        EnduranceMod = 1.0F;
        KnockdownMod = 1.0F;
        CantAttackWithLowestEndurance = false;
        ReplaceOnUseOn = null;
        IsWaterSource = false;
        DefaultModData = null;
        IsAimedFirearm = false;
        IsAimedHandWeapon = false;
        CanStack = true;
        AimingMod = 1.0F;
        ProjectileCount = 1;
        HitAngleMod = 0.0F;
        SplatSize = 1.0F;
        bodyLocation = ClothingBodyLocation.None;
        PaletteChoices = new Stack();
        SpriteName = null;
        PalettesStart = "";
        type = Type.Normal;
    }

    public String getDisplayName()
    {
        return DisplayName;
    }

    public void setDisplayName(String DisplayName)
    {
        this.DisplayName = DisplayName;
    }

    public String getIcon()
    {
        return Icon;
    }

    public void setIcon(String Icon)
    {
        this.Icon = Icon;
    }

    public float getActualWeight()
    {
        return ActualWeight;
    }

    public void setActualWeight(float ActualWeight)
    {
        this.ActualWeight = ActualWeight;
    }

    public float getHungerChange()
    {
        return HungerChange;
    }

    public void setHungerChange(float HungerChange)
    {
        this.HungerChange = HungerChange;
    }

    public int getCount()
    {
        return Count;
    }

    public void setCount(int Count)
    {
        this.Count = Count;
    }

    public int getDaysFresh()
    {
        return DaysFresh;
    }

    public void setDaysFresh(int DaysFresh)
    {
        this.DaysFresh = DaysFresh;
    }

    public int getDaysTotallyRotten()
    {
        return DaysTotallyRotten;
    }

    public void setDaysTotallyRotten(int DaysTotallyRotten)
    {
        this.DaysTotallyRotten = DaysTotallyRotten;
    }

    public int getMinutesToCook()
    {
        return MinutesToCook;
    }

    public void setMinutesToCook(int MinutesToCook)
    {
        this.MinutesToCook = MinutesToCook;
    }

    public int getMinutesToBurn()
    {
        return MinutesToBurn;
    }

    public void setMinutesToBurn(int MinutesToBurn)
    {
        this.MinutesToBurn = MinutesToBurn;
    }

    public boolean isIsCookable()
    {
        return IsCookable;
    }

    public void setIsCookable(boolean IsCookable)
    {
        this.IsCookable = IsCookable;
    }

    public float getStressChange()
    {
        return StressChange;
    }

    public void setStressChange(float StressChange)
    {
        this.StressChange = StressChange;
    }

    public float getBoredomChange()
    {
        return BoredomChange;
    }

    public void setBoredomChange(float BoredomChange)
    {
        this.BoredomChange = BoredomChange;
    }

    public float getUnhappyChange()
    {
        return UnhappyChange;
    }

    public void setUnhappyChange(float UnhappyChange)
    {
        this.UnhappyChange = UnhappyChange;
    }

    public boolean isAlwaysWelcomeGift()
    {
        return AlwaysWelcomeGift;
    }

    public void setAlwaysWelcomeGift(boolean AlwaysWelcomeGift)
    {
        this.AlwaysWelcomeGift = AlwaysWelcomeGift;
    }

    public boolean isRanged()
    {
        return Ranged;
    }

    public void setRanged(boolean Ranged)
    {
        this.Ranged = Ranged;
    }

    public float getMaxRange()
    {
        return MaxRange;
    }

    public void setMaxRange(float MaxRange)
    {
        this.MaxRange = MaxRange;
    }

    public float getMinAngle()
    {
        return MinAngle;
    }

    public void setMinAngle(float MinAngle)
    {
        this.MinAngle = MinAngle;
    }

    public float getMaxDamage()
    {
        return MaxDamage;
    }

    public void setMaxDamage(float MaxDamage)
    {
        this.MaxDamage = MaxDamage;
    }

    public float getMinDamage()
    {
        return MinDamage;
    }

    public void setMinDamage(float MinDamage)
    {
        this.MinDamage = MinDamage;
    }

    public float getMinimumSwingTime()
    {
        return MinimumSwingTime;
    }

    public void setMinimumSwingTime(float MinimumSwingTime)
    {
        this.MinimumSwingTime = MinimumSwingTime;
    }

    public String getSwingSound()
    {
        return SwingSound;
    }

    public void setSwingSound(String SwingSound)
    {
        this.SwingSound = SwingSound;
    }

    public String getWeaponSprite()
    {
        return WeaponSprite;
    }

    public void setWeaponSprite(String WeaponSprite)
    {
        this.WeaponSprite = WeaponSprite;
    }

    public boolean isAngleFalloff()
    {
        return AngleFalloff;
    }

    public void setAngleFalloff(boolean AngleFalloff)
    {
        this.AngleFalloff = AngleFalloff;
    }

    public int getSoundVolume()
    {
        return SoundVolume;
    }

    public void setSoundVolume(int SoundVolume)
    {
        this.SoundVolume = SoundVolume;
    }

    public float getToHitModifier()
    {
        return ToHitModifier;
    }

    public void setToHitModifier(float ToHitModifier)
    {
        this.ToHitModifier = ToHitModifier;
    }

    public int getSoundRadius()
    {
        return SoundRadius;
    }

    public void setSoundRadius(int SoundRadius)
    {
        this.SoundRadius = SoundRadius;
    }

    public float getOtherCharacterVolumeBoost()
    {
        return OtherCharacterVolumeBoost;
    }

    public void setOtherCharacterVolumeBoost(float OtherCharacterVolumeBoost)
    {
        this.OtherCharacterVolumeBoost = OtherCharacterVolumeBoost;
    }

    public ArrayList getCategories()
    {
        return Categories;
    }

    public void setCategories(ArrayList Categories)
    {
        this.Categories = Categories;
    }

    public String getImpactSound()
    {
        return ImpactSound;
    }

    public void setImpactSound(String ImpactSound)
    {
        this.ImpactSound = ImpactSound;
    }

    public float getSwingTime()
    {
        return SwingTime;
    }

    public void setSwingTime(float SwingTime)
    {
        this.SwingTime = SwingTime;
    }

    public boolean isKnockBackOnNoDeath()
    {
        return KnockBackOnNoDeath;
    }

    public void setKnockBackOnNoDeath(boolean KnockBackOnNoDeath)
    {
        this.KnockBackOnNoDeath = KnockBackOnNoDeath;
    }

    public boolean isSplatBloodOnNoDeath()
    {
        return SplatBloodOnNoDeath;
    }

    public void setSplatBloodOnNoDeath(boolean SplatBloodOnNoDeath)
    {
        this.SplatBloodOnNoDeath = SplatBloodOnNoDeath;
    }

    public float getSwingAmountBeforeImpact()
    {
        return SwingAmountBeforeImpact;
    }

    public void setSwingAmountBeforeImpact(float SwingAmountBeforeImpact)
    {
        this.SwingAmountBeforeImpact = SwingAmountBeforeImpact;
    }

    public String getAmmoType()
    {
        return AmmoType;
    }

    public void setAmmoType(String AmmoType)
    {
        this.AmmoType = AmmoType;
    }

    public int getDoorDamage()
    {
        return DoorDamage;
    }

    public void setDoorDamage(int DoorDamage)
    {
        this.DoorDamage = DoorDamage;
    }

    public int getConditionLowerChance()
    {
        return ConditionLowerChance;
    }

    public void setConditionLowerChance(int ConditionLowerChance)
    {
        this.ConditionLowerChance = ConditionLowerChance;
    }

    public int getConditionMax()
    {
        return ConditionMax;
    }

    public void setConditionMax(int ConditionMax)
    {
        this.ConditionMax = ConditionMax;
    }

    public boolean isCanBandage()
    {
        return CanBandage;
    }

    public void setCanBandage(boolean CanBandage)
    {
        this.CanBandage = CanBandage;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public int getMaxHitCount()
    {
        return MaxHitCount;
    }

    public void setMaxHitCount(int MaxHitCount)
    {
        this.MaxHitCount = MaxHitCount;
    }

    public boolean isUseSelf()
    {
        return UseSelf;
    }

    public void setUseSelf(boolean UseSelf)
    {
        this.UseSelf = UseSelf;
    }

    public boolean isOtherHandUse()
    {
        return OtherHandUse;
    }

    public void setOtherHandUse(boolean OtherHandUse)
    {
        this.OtherHandUse = OtherHandUse;
    }

    public String getOtherHandRequire()
    {
        return OtherHandRequire;
    }

    public void setOtherHandRequire(String OtherHandRequire)
    {
        this.OtherHandRequire = OtherHandRequire;
    }

    public String getPhysicsObject()
    {
        return PhysicsObject;
    }

    public void setPhysicsObject(String PhysicsObject)
    {
        this.PhysicsObject = PhysicsObject;
    }

    public String getSwingAnim()
    {
        return SwingAnim;
    }

    public void setSwingAnim(String SwingAnim)
    {
        this.SwingAnim = SwingAnim;
    }

    public float getWeaponWeight()
    {
        return WeaponWeight;
    }

    public void setWeaponWeight(float WeaponWeight)
    {
        this.WeaponWeight = WeaponWeight;
    }

    public float getEnduranceChange()
    {
        return EnduranceChange;
    }

    public void setEnduranceChange(float EnduranceChange)
    {
        this.EnduranceChange = EnduranceChange;
    }

    public String getRequireInHandOrInventory()
    {
        return RequireInHandOrInventory;
    }

    public void setRequireInHandOrInventory(String RequireInHandOrInventory)
    {
        this.RequireInHandOrInventory = RequireInHandOrInventory;
    }

    public String getDoorHitSound()
    {
        return DoorHitSound;
    }

    public void setDoorHitSound(String DoorHitSound)
    {
        this.DoorHitSound = DoorHitSound;
    }

    public String getReplaceOnUse()
    {
        return ReplaceOnUse;
    }

    public void setReplaceOnUse(String ReplaceOnUse)
    {
        this.ReplaceOnUse = ReplaceOnUse;
    }

    public boolean isDangerousUncooked()
    {
        return DangerousUncooked;
    }

    public void setDangerousUncooked(boolean DangerousUncooked)
    {
        this.DangerousUncooked = DangerousUncooked;
    }

    public boolean isAlcoholic()
    {
        return Alcoholic;
    }

    public void setAlcoholic(boolean Alcoholic)
    {
        this.Alcoholic = Alcoholic;
    }

    public float getPushBackMod()
    {
        return PushBackMod;
    }

    public void setPushBackMod(float PushBackMod)
    {
        this.PushBackMod = PushBackMod;
    }

    public int getSplatNumber()
    {
        return SplatNumber;
    }

    public void setSplatNumber(int SplatNumber)
    {
        this.SplatNumber = SplatNumber;
    }

    public float getNPCSoundBoost()
    {
        return NPCSoundBoost;
    }

    public void setNPCSoundBoost(float NPCSoundBoost)
    {
        this.NPCSoundBoost = NPCSoundBoost;
    }

    public boolean isRangeFalloff()
    {
        return RangeFalloff;
    }

    public void setRangeFalloff(boolean RangeFalloff)
    {
        this.RangeFalloff = RangeFalloff;
    }

    public boolean isUseEndurance()
    {
        return UseEndurance;
    }

    public void setUseEndurance(boolean UseEndurance)
    {
        this.UseEndurance = UseEndurance;
    }

    public boolean isMultipleHitConditionAffected()
    {
        return MultipleHitConditionAffected;
    }

    public void setMultipleHitConditionAffected(boolean MultipleHitConditionAffected)
    {
        this.MultipleHitConditionAffected = MultipleHitConditionAffected;
    }

    public boolean isShareDamage()
    {
        return ShareDamage;
    }

    public void setShareDamage(boolean ShareDamage)
    {
        this.ShareDamage = ShareDamage;
    }

    public boolean isShareEndurance()
    {
        return ShareEndurance;
    }

    public void setShareEndurance(boolean ShareEndurance)
    {
        this.ShareEndurance = ShareEndurance;
    }

    public boolean isCanBarricade()
    {
        return CanBarricade;
    }

    public void setCanBarricade(boolean CanBarricade)
    {
        this.CanBarricade = CanBarricade;
    }

    public boolean isUseWhileEquipped()
    {
        return UseWhileEquipped;
    }

    public void setUseWhileEquipped(boolean UseWhileEquipped)
    {
        this.UseWhileEquipped = UseWhileEquipped;
    }

    public boolean isDisappearOnUse()
    {
        return DisappearOnUse;
    }

    public void setDisappearOnUse(boolean DisappearOnUse)
    {
        this.DisappearOnUse = DisappearOnUse;
    }

    public float getUseDelta()
    {
        return UseDelta;
    }

    public void setUseDelta(float UseDelta)
    {
        this.UseDelta = UseDelta;
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

    public ClothingBodyLocation getBodyLocation()
    {
        return bodyLocation;
    }

    public void setBodyLocation(ClothingBodyLocation bodyLocation)
    {
        this.bodyLocation = bodyLocation;
    }

    public Stack getPaletteChoices()
    {
        return PaletteChoices;
    }

    public void setPaletteChoices(Stack PaletteChoices)
    {
        this.PaletteChoices = PaletteChoices;
    }

    public String getSpriteName()
    {
        return SpriteName;
    }

    public void setSpriteName(String SpriteName)
    {
        this.SpriteName = SpriteName;
    }

    public String getPalettesStart()
    {
        return PalettesStart;
    }

    public void setPalettesStart(String PalettesStart)
    {
        this.PalettesStart = PalettesStart;
    }

    public Type getType()
    {
        return type;
    }

    public void setType(Type type)
    {
        this.type = type;
    }

    public void Load(String name, String strArray[])
    {
        this.name = name;
        String arr$[] = strArray;
        int len$ = arr$.length;
        for(int i$ = 0; i$ < len$; i$++)
        {
            String str = arr$[i$];
            DoParam(str);
        }

        if(type == Type.Clothing)
        {
            for(int n = 0; n < PaletteChoices.size(); n++)
                InstanceItem((String)PaletteChoices.get(n));

            if(PaletteChoices.isEmpty())
                InstanceItem(null);
        }
    }

    public InventoryItem InstanceItem(String param)
    {
        InventoryItem item = null;
        if(type == Type.Food)
        {
            item = new Food(module.name, DisplayName, name, (new StringBuilder()).append("Item_").append(Icon).toString());
            Food food = (Food)item;
            food.setThirstChange(ThistChange / 100F);
            food.setHungChange(HungerChange / 100F);
            food.setEndChange(EnduranceChange / 100F);
            food.setReplaceOnUse(ReplaceOnUse);
            food.setOffAge(DaysFresh);
            food.setOffAgeMax(DaysTotallyRotten);
            food.setIsCookable(IsCookable);
            food.setMinutesToCook(MinutesToCook);
            food.setMinutesToBurn(MinutesToBurn);
            food.setbDangerousUncooked(DangerousUncooked);
            food.setRequireInHandOrInventory(RequireInHandOrInventory);
            food.setReplaceOnUse(ReplaceOnUse);
            food.setAlcoholic(Alcoholic);
        }
        if(type == Type.Literature)
        {
            item = new Literature(module.name, DisplayName, name, (new StringBuilder()).append("Item_").append(Icon).toString());
            Literature literature = (Literature)item;
            literature.setReplaceOnUse(ReplaceOnUse);
            literature.requireInHandOrInventory = RequireInHandOrInventory;
        } else
        if(type == Type.Weapon)
        {
            item = new HandWeapon(module.name, DisplayName, name, (new StringBuilder()).append("Item_").append(Icon).toString());
            HandWeapon weapon = (HandWeapon)item;
            weapon.setMultipleHitConditionAffected(MultipleHitConditionAffected);
            weapon.setConditionLowerChance(ConditionLowerChance);
            weapon.SplatSize = SplatSize;
            weapon.aimingMod = AimingMod;
            weapon.ProjectileCount = ProjectileCount;
            weapon.setMinDamage(MinDamage);
            weapon.setMaxDamage(MaxDamage);
            weapon.setPhysicsObject(PhysicsObject);
            weapon.setOtherHandRequire(OtherHandRequire);
            weapon.setOtherHandUse(OtherHandUse);
            weapon.setMaxRange(MaxRange);
            weapon.setMinRange(MinRange);
            weapon.setShareEndurance(ShareEndurance);
            weapon.setKnockdownMod(KnockdownMod);
            weapon.bIsAimedFirearm = IsAimedFirearm;
            weapon.RunAnim = RunAnim;
            weapon.IdleAnim = IdleAnim;
            weapon.HitAngleMod = (float)Math.toRadians(HitAngleMod);
            weapon.bIsAimedHandWeapon = IsAimedHandWeapon;
            weapon.setCantAttackWithLowestEndurance(CantAttackWithLowestEndurance);
            weapon.setAlwaysKnockdown(AlwaysKnockdown);
            weapon.setEnduranceMod(EnduranceMod);
            weapon.setUseSelf(UseSelf);
            weapon.setSwingAnim(SwingAnim);
            weapon.setMaxHitCount(MaxHitCount);
            weapon.setMinimumSwingTime(MinimumSwingTime);
            weapon.setSwingTime(SwingTime);
            weapon.setSwingAnim(SwingAnim);
            weapon.setDoSwingBeforeImpact(SwingAmountBeforeImpact);
            weapon.setMinAngle(MinAngle);
            weapon.setWeaponSprite(WeaponSprite);
            weapon.setDoorDamage(DoorDamage);
            weapon.setDoorHitSound(DoorHitSound);
            weapon.setPushBackMod(PushBackMod);
            weapon.setWeight(WeaponWeight);
            weapon.setImpactSound(ImpactSound);
            weapon.setSplatNumber(SplatNumber);
            weapon.setKnockBackOnNoDeath(KnockBackOnNoDeath);
            weapon.setSplatBloodOnNoDeath(SplatBloodOnNoDeath);
            weapon.setSwingSound(SwingSound);
            weapon.setAngleFalloff(AngleFalloff);
            weapon.setSoundVolume(SoundVolume);
            weapon.setSoundRadius(SoundRadius);
            weapon.setToHitModifier(ToHitModifier);
            weapon.setOtherBoost(NPCSoundBoost);
            weapon.setRanged(Ranged);
            weapon.setRangeFalloff(RangeFalloff);
            weapon.setUseEndurance(UseEndurance);
            weapon.setShareDamage(ShareDamage);
            weapon.setAmmoType(AmmoType);
            weapon.setCanBarracade(CanBarricade);
            weapon.setWeaponSprite(WeaponSprite);
        } else
        if(type == Type.Normal)
            item = new ComboItem(module.name, DisplayName, name, (new StringBuilder()).append("Item_").append(Icon).toString());
        else
        if(type == Type.Clothing)
        {
            String col = "";
            String pal = null;
            if(!PaletteChoices.isEmpty() || param != null)
            {
                int ran = Rand.Next(PaletteChoices.size());
                pal = (String)PaletteChoices.get(ran);
                if(param != null)
                    pal = param;
                col = (new StringBuilder()).append("_").append(pal.replace(PalettesStart, "")).toString();
            }
            item = new Clothing(module.name, DisplayName, name, (new StringBuilder()).append("Item_").append(Icon).append(col).toString(), pal, SpriteName);
            ((Clothing)item).setBodyLocation(bodyLocation);
        } else
        if(type == Type.Drainable)
        {
            item = new DrainableComboItem(module.name, DisplayName, name, (new StringBuilder()).append("Item_").append(Icon).toString());
            DrainableComboItem drain = (DrainableComboItem)item;
            drain.setUseWhileEquiped(UseWhileEquipped);
            drain.setDisappearOnUse(DisappearOnUse);
            drain.setUseDelta(UseDelta);
            drain.setReplaceOnDeplete(ReplaceOnDeplete);
        }
        item.setCondition(ConditionMax);
        item.setConditionMax(ConditionMax);
        item.setActualWeight(ActualWeight);
        item.setUses(Count);
        item.setAlwaysWelcomeGift(AlwaysWelcomeGift);
        item.setCanBandage(CanBandage);
        item.setScriptItem(this);
        item.setBoredomChange(BoredomChange);
        item.setStressChange(StressChange);
        item.setUnhappyChange(UnhappyChange);
        item.setReplaceOnUseOn(ReplaceOnUseOn);
        item.setIsWaterSource(IsWaterSource);
        item.CanStoreWater = CanStoreWater;
        item.CanStack = CanStack;
        item.CopyModData(DefaultModData);
        return item;
    }

    public void DoParam(String str)
    {
        if(str.trim().length() == 0)
            return;
        try
        {
            String p[] = str.split("=");
            String param = p[0];
            String val = p[1];
            val = val.trim();
            val = new String(val);
            param = new String(param.trim());
            if(param.trim().equalsIgnoreCase("BodyLocation"))
                bodyLocation = bodyLocation.valueOf(val.trim());
            else
            if(param.trim().equalsIgnoreCase("Palettes"))
            {
                String split[] = val.split("/");
                for(int n = 0; n < split.length; n++)
                    PaletteChoices.add(split[n].trim());

            } else
            if(param.trim().equalsIgnoreCase("PalettesStart"))
                PalettesStart = val.trim();
            else
            if(param.trim().equalsIgnoreCase("DisplayName"))
                DisplayName = val.trim();
            else
            if(param.trim().equalsIgnoreCase("SpriteName"))
                SpriteName = val.trim();
            else
            if(param.trim().equalsIgnoreCase("Type"))
            {
                type = Type.valueOf(val.trim());
                if(type == Type.Clothing)
                    type = type;
            } else
            if(param.trim().equalsIgnoreCase("SplatSize"))
                SplatSize = Float.parseFloat(val);
            else
            if(param.trim().equalsIgnoreCase("CanStoreWater"))
                CanStoreWater = val.equalsIgnoreCase("true");
            else
            if(param.trim().equalsIgnoreCase("IsWaterSource"))
                IsWaterSource = val.equalsIgnoreCase("true");
            else
            if(param.trim().equalsIgnoreCase("SwingAnim"))
                SwingAnim = val;
            else
            if(param.trim().equalsIgnoreCase("Icon"))
                Icon = val;
            else
            if(param.trim().equalsIgnoreCase("DoorHitSound"))
                DoorHitSound = val;
            else
            if(param.trim().equalsIgnoreCase("Weight"))
                ActualWeight = Float.parseFloat(val);
            else
            if(param.trim().equalsIgnoreCase("HungerChange"))
                HungerChange = Float.parseFloat(val);
            else
            if(param.trim().equalsIgnoreCase("ThirstChange"))
                ThistChange = Float.parseFloat(val);
            else
            if(param.trim().equalsIgnoreCase("EnduranceChange"))
                EnduranceChange = Float.parseFloat(val);
            else
            if(param.trim().equalsIgnoreCase("DaysFresh"))
                DaysFresh = Integer.parseInt(val);
            else
            if(param.trim().equalsIgnoreCase("DaysTotallyRotten"))
                DaysTotallyRotten = Integer.parseInt(val);
            else
            if(param.trim().equalsIgnoreCase("IsCookable"))
                IsCookable = val.equalsIgnoreCase("true");
            else
            if(param.trim().equalsIgnoreCase("MinutesToCook"))
                MinutesToCook = Integer.parseInt(val);
            else
            if(param.trim().equalsIgnoreCase("MinutesToBurn"))
                MinutesToBurn = Integer.parseInt(val);
            else
            if(param.trim().equalsIgnoreCase("BoredomChange"))
                BoredomChange = Integer.parseInt(val);
            else
            if(param.trim().equalsIgnoreCase("StressChange"))
                StressChange = Integer.parseInt(val);
            else
            if(param.trim().equalsIgnoreCase("UnhappyChange"))
                UnhappyChange = Integer.parseInt(val);
            else
            if(param.trim().equalsIgnoreCase("ReplaceOnDeplete"))
                ReplaceOnDeplete = val;
            else
            if(param.trim().equalsIgnoreCase("ReplaceOnUseOn"))
                ReplaceOnUseOn = val;
            else
            if(param.trim().equalsIgnoreCase("Ranged"))
                Ranged = val.equalsIgnoreCase("true");
            else
            if(param.trim().equalsIgnoreCase("UseSelf"))
                UseSelf = val.equalsIgnoreCase("true");
            else
            if(param.trim().equalsIgnoreCase("OtherHandUse"))
                OtherHandUse = val.equalsIgnoreCase("true");
            else
            if(param.trim().equalsIgnoreCase("DangerousUncooked"))
                DangerousUncooked = val.equalsIgnoreCase("true");
            else
            if(param.trim().equalsIgnoreCase("MaxRange"))
                MaxRange = Float.parseFloat(val);
            else
            if(param.trim().equalsIgnoreCase("MinRange"))
                MinRange = Float.parseFloat(val);
            else
            if(param.trim().equalsIgnoreCase("MinAngle"))
                MinAngle = Float.parseFloat(val);
            else
            if(param.trim().equalsIgnoreCase("MaxDamage"))
                MaxDamage = Float.parseFloat(val);
            else
            if(param.trim().equalsIgnoreCase("ConditionLowerChanceOneIn"))
                ConditionLowerChance = Integer.parseInt(val);
            else
            if(param.trim().equalsIgnoreCase("MultipleHitConditionAffected"))
                MultipleHitConditionAffected = val.equalsIgnoreCase("true");
            else
            if(param.trim().equalsIgnoreCase("CanBandage"))
                CanBandage = val.equalsIgnoreCase("true");
            else
            if(param.trim().equalsIgnoreCase("ConditionMax"))
                ConditionMax = Integer.parseInt(val);
            else
            if(param.trim().equalsIgnoreCase("MinDamage"))
                MinDamage = Float.parseFloat(val);
            else
            if(param.trim().equalsIgnoreCase("MinimumSwingTime"))
                MinimumSwingTime = Float.parseFloat(val);
            else
            if(param.trim().equalsIgnoreCase("SwingSound"))
                SwingSound = val;
            else
            if(param.trim().equalsIgnoreCase("ReplaceOnUse"))
                ReplaceOnUse = val;
            else
            if(param.trim().equalsIgnoreCase("WeaponSprite"))
                WeaponSprite = val;
            else
            if(param.trim().equalsIgnoreCase("AngleFalloff"))
                AngleFalloff = val.equalsIgnoreCase("true");
            else
            if(param.trim().equalsIgnoreCase("SoundVolume"))
                SoundVolume = Integer.parseInt(val);
            else
            if(param.trim().equalsIgnoreCase("ToHitModifier"))
                ToHitModifier = Float.parseFloat(val);
            else
            if(param.trim().equalsIgnoreCase("SoundRadius"))
                SoundRadius = Integer.parseInt(val);
            else
            if(param.trim().equalsIgnoreCase("Categories"))
            {
                String s[] = val.split(",");
                for(int n = 0; n < s.length; n++)
                    Categories.add(s[n].trim());

            } else
            if(param.trim().equalsIgnoreCase("OtherCharacterVolumeBoost"))
                OtherCharacterVolumeBoost = Float.parseFloat(val);
            else
            if(param.trim().equalsIgnoreCase("ImpactSound"))
            {
                ImpactSound = val;
                if(ImpactSound.equals("null"))
                    ImpactSound = null;
            } else
            if(param.trim().equalsIgnoreCase("SwingTime"))
                SwingTime = Float.parseFloat(val);
            else
            if(param.trim().equalsIgnoreCase("KnockBackOnNoDeath"))
                KnockBackOnNoDeath = val.equalsIgnoreCase("true");
            else
            if(param.trim().equalsIgnoreCase("Alcoholic"))
                Alcoholic = val.equalsIgnoreCase("true");
            else
            if(param.trim().equalsIgnoreCase("SplatBloodOnNoDeath"))
                SplatBloodOnNoDeath = val.equalsIgnoreCase("true");
            else
            if(param.trim().equalsIgnoreCase("SwingAmountBeforeImpact"))
                SwingAmountBeforeImpact = Float.parseFloat(val);
            else
            if(param.trim().equalsIgnoreCase("AmmoType"))
                AmmoType = val;
            else
            if(param.trim().equalsIgnoreCase("HitAngleMod"))
                HitAngleMod = Float.parseFloat(val);
            else
            if(param.trim().equalsIgnoreCase("OtherHandRequire"))
                OtherHandRequire = val;
            else
            if(param.trim().equalsIgnoreCase("AlwaysWelcomeGift"))
                AlwaysWelcomeGift = val.equalsIgnoreCase("true");
            else
            if(param.trim().equalsIgnoreCase("CantAttackWithLowestEndurance"))
                CantAttackWithLowestEndurance = val.equalsIgnoreCase("true");
            else
            if(param.trim().equalsIgnoreCase("EnduranceMod"))
                EnduranceMod = Float.parseFloat(val);
            else
            if(param.trim().equalsIgnoreCase("KnockdownMod"))
                KnockdownMod = Float.parseFloat(val);
            else
            if(param.trim().equalsIgnoreCase("DoorDamage"))
                DoorDamage = Integer.parseInt(val);
            else
            if(param.trim().equalsIgnoreCase("MaxHitCount"))
                MaxHitCount = Integer.parseInt(val);
            else
            if(param.trim().equalsIgnoreCase("PhysicsObject"))
                PhysicsObject = val;
            else
            if(param.trim().equalsIgnoreCase("Count"))
                Count = Integer.parseInt(val);
            else
            if(param.trim().equalsIgnoreCase("SwingAnim"))
                SwingAnim = val;
            else
            if(param.trim().equalsIgnoreCase("WeaponWeight"))
                WeaponWeight = Float.parseFloat(val);
            else
            if(param.trim().equalsIgnoreCase("IdleAnim"))
                IdleAnim = val;
            else
            if(param.trim().equalsIgnoreCase("RunAnim"))
                RunAnim = val;
            else
            if(param.trim().equalsIgnoreCase("RequireInHandOrInventory"))
                RequireInHandOrInventory = val;
            else
            if(param.trim().equalsIgnoreCase("PushBackMod"))
                PushBackMod = Float.parseFloat(val);
            else
            if(param.trim().equalsIgnoreCase("NPCSoundBoost"))
                NPCSoundBoost = Float.parseFloat(val);
            else
            if(param.trim().equalsIgnoreCase("SplatNumber"))
                SplatNumber = Integer.parseInt(val);
            else
            if(param.trim().equalsIgnoreCase("RangeFalloff"))
                RangeFalloff = val.equalsIgnoreCase("true");
            else
            if(param.trim().equalsIgnoreCase("UseEndurance"))
                UseEndurance = val.equalsIgnoreCase("true");
            else
            if(param.trim().equalsIgnoreCase("ShareDamage"))
                ShareDamage = val.equalsIgnoreCase("true");
            else
            if(param.trim().equalsIgnoreCase("ShareEndurance"))
                ShareEndurance = val.equalsIgnoreCase("true");
            else
            if(param.trim().equalsIgnoreCase("AlwaysKnockdown"))
                AlwaysKnockdown = val.equalsIgnoreCase("true");
            else
            if(param.trim().equalsIgnoreCase("IsAimedFirearm"))
                IsAimedFirearm = val.equalsIgnoreCase("true");
            else
            if(param.trim().equalsIgnoreCase("IsAimedHandWeapon"))
                IsAimedHandWeapon = val.equalsIgnoreCase("true");
            else
            if(param.trim().equalsIgnoreCase("AimingMod"))
                AimingMod = Float.parseFloat(val);
            else
            if(param.trim().equalsIgnoreCase("ProjectileCount"))
                ProjectileCount = Integer.parseInt(val);
            else
            if(param.trim().equalsIgnoreCase("CanStack"))
                IsAimedFirearm = val.equalsIgnoreCase("true");
            else
            if(param.trim().equalsIgnoreCase("CanBarricade"))
                CanBarricade = val.equalsIgnoreCase("true");
            else
            if(param.trim().equalsIgnoreCase("UseWhileEquipped"))
                UseWhileEquipped = val.equalsIgnoreCase("true");
            else
            if(param.trim().equalsIgnoreCase("DisappearOnUse"))
                DisappearOnUse = val.equalsIgnoreCase("true");
            else
            if(param.trim().equalsIgnoreCase("UseDelta"))
            {
                UseDelta = Float.parseFloat(val);
            } else
            {
                if(DefaultModData == null)
                    DefaultModData = LuaManager.platform.newTable();
                try
                {
                    Double tryConv = Double.valueOf(Double.parseDouble(val.trim()));
                    DefaultModData.rawset(param.trim(), tryConv);
                }
                catch(Exception ex)
                {
                    DefaultModData.rawset(param.trim(), val);
                }
            }
        }
        catch(Exception ex)
        {
            throw new InvalidParameterException((new StringBuilder()).append("Error: ").append(str.trim()).append(" is not a valid parameter in item: ").append(name).toString());
        }
    }

    public String DisplayName;
    public String Icon;
    public float ActualWeight;
    public float HungerChange;
    public int Count;
    public int DaysFresh;
    public int DaysTotallyRotten;
    public int MinutesToCook;
    public int MinutesToBurn;
    public boolean IsCookable;
    public float StressChange;
    public float BoredomChange;
    public float UnhappyChange;
    public boolean AlwaysWelcomeGift;
    public String ReplaceOnDeplete;
    public boolean Ranged;
    public boolean CanStoreWater;
    public float MaxRange;
    public float MinRange;
    public float ThistChange;
    public float MinAngle;
    public float MaxDamage;
    public float MinDamage;
    public float MinimumSwingTime;
    public String SwingSound;
    public String WeaponSprite;
    public boolean AngleFalloff;
    public int SoundVolume;
    public float ToHitModifier;
    public int SoundRadius;
    public float OtherCharacterVolumeBoost;
    public ArrayList Categories;
    public String ImpactSound;
    public float SwingTime;
    public boolean KnockBackOnNoDeath;
    public boolean SplatBloodOnNoDeath;
    public float SwingAmountBeforeImpact;
    public String AmmoType;
    public int DoorDamage;
    public int ConditionLowerChance;
    public int ConditionMax;
    public boolean CanBandage;
    public String name;
    public int MaxHitCount;
    public boolean UseSelf;
    public boolean OtherHandUse;
    public String OtherHandRequire;
    public String PhysicsObject;
    public String SwingAnim;
    public float WeaponWeight;
    public float EnduranceChange;
    public String IdleAnim;
    public String RunAnim;
    public String RequireInHandOrInventory;
    public String DoorHitSound;
    public String ReplaceOnUse;
    public boolean DangerousUncooked;
    public boolean Alcoholic;
    public float PushBackMod;
    public int SplatNumber;
    public float NPCSoundBoost;
    public boolean RangeFalloff;
    public boolean UseEndurance;
    public boolean MultipleHitConditionAffected;
    public boolean ShareDamage;
    public boolean ShareEndurance;
    public boolean CanBarricade;
    public boolean UseWhileEquipped;
    public boolean DisappearOnUse;
    public float UseDelta;
    public boolean AlwaysKnockdown;
    public float EnduranceMod;
    public float KnockdownMod;
    public boolean CantAttackWithLowestEndurance;
    public String ReplaceOnUseOn;
    public boolean IsWaterSource;
    public KahluaTable DefaultModData;
    public boolean IsAimedFirearm;
    public boolean IsAimedHandWeapon;
    public boolean CanStack;
    public float AimingMod;
    public int ProjectileCount;
    public float HitAngleMod;
    public float SplatSize;
    public ClothingBodyLocation bodyLocation;
    public Stack PaletteChoices;
    public String SpriteName;
    public String PalettesStart;
    public Type type;
}
