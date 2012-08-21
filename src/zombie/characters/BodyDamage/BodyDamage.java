// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   BodyDamage.java

package zombie.characters.BodyDamage;

import java.io.*;
import java.nio.ByteBuffer;
import zombie.*;
import zombie.ai.StateMachine;
import zombie.ai.states.DieState;
import zombie.ai.states.ReanimateState;
import zombie.characters.*;
import zombie.characters.Moodles.MoodleType;
import zombie.characters.Moodles.Moodles;
import zombie.core.Collections.NulledArrayList;
import zombie.core.Rand;
import zombie.core.SpriteRenderer;
import zombie.inventory.InventoryItem;
import zombie.inventory.types.*;
import zombie.iso.IsoCell;
import zombie.iso.IsoGridSquare;
import zombie.iso.objects.RainManager;
import zombie.iso.sprite.IsoAnim;
import zombie.iso.sprite.IsoSprite;
import zombie.network.DamagePlayer;
import zombie.network.GameServer;
import zombie.ui.Sidebar;

// Referenced classes of package zombie.characters.BodyDamage:
//            BodyPart, BodyPartType

public class BodyDamage
{

    public BodyDamage(IsoGameCharacter ParentCharacter)
    {
        BodyParts = new NulledArrayList(18);
        DamageModCount = 60;
        InfectionGrowthRate = 0.001F;
        InfectionLevel = 0.0F;
        FakeInfectionLevel = 0.0F;
        OverallBodyHealth = 100F;
        StandardHealthAddition = 0.005F;
        ReducedHealthAddition = 0.0025F;
        SeverlyReducedHealthAddition = 0.001F;
        SleepingHealthAddition = 0.02F;
        HealthFromFood = 0.035F;
        HealthReductionFromSevereBadMoodles = 0.0165F;
        InfectionHealthReductionAmmount = 0.0075F;
        StandardHealthFromFoodTime = 1600;
        HealthFromFoodTimer = 0;
        BoredomLevel = 0.0F;
        BoredomDecreaseFromReading = 0.5F;
        InitialThumpPain = 14F;
        InitialScratchPain = 18F;
        InitialBitePain = 25F;
        InitialWoundPain = 80F;
        ContinualPainIncrease = 0.001F;
        PainReductionFromMeds = 30F;
        StandardPainReductionWhenWell = 0.01F;
        OldNumZombiesVisible = 0;
        CurrentNumZombiesVisible = 0;
        PanicIncreaseValue = 7F;
        PanicReductionValue = 0.02F;
        DrunkIncreaseValue = 20.5F;
        DrunkReductionValue = 0.0042F;
        IsOnFire = false;
        BurntToDeath = false;
        WetnessIncreaseValue = 0.1F;
        WetnessReductionValue = 0.01F;
        Wetness = 0.0F;
        CatchAColdIncreaseRate = 0.005F;
        CatchAColdDecreaseRate = 0.175F;
        CatchACold = 0.0F;
        HasACold = false;
        ColdStrength = 0.0F;
        ColdProgressionRate = 0.0112F;
        TimeToSneezeOrCough = 0;
        MildColdSneezeTimerMin = 600;
        MildColdSneezeTimerMax = 800;
        ColdSneezeTimerMin = 300;
        ColdSneezeTimerMax = 600;
        NastyColdSneezeTimerMin = 200;
        NastyColdSneezeTimerMax = 300;
        SneezeCoughActive = 0;
        SneezeCoughTime = 0;
        SneezeCoughDelay = 25;
        UnhappynessLevel = 0.0F;
        BodyParts.add(new BodyPart(BodyPartType.Hand_L, ParentCharacter));
        BodyParts.add(new BodyPart(BodyPartType.Hand_R, ParentCharacter));
        BodyParts.add(new BodyPart(BodyPartType.ForeArm_L, ParentCharacter));
        BodyParts.add(new BodyPart(BodyPartType.ForeArm_R, ParentCharacter));
        BodyParts.add(new BodyPart(BodyPartType.UpperArm_L, ParentCharacter));
        BodyParts.add(new BodyPart(BodyPartType.UpperArm_R, ParentCharacter));
        BodyParts.add(new BodyPart(BodyPartType.Torso_Upper, ParentCharacter));
        BodyParts.add(new BodyPart(BodyPartType.Torso_Lower, ParentCharacter));
        BodyParts.add(new BodyPart(BodyPartType.Head, ParentCharacter));
        BodyParts.add(new BodyPart(BodyPartType.Neck, ParentCharacter));
        BodyParts.add(new BodyPart(BodyPartType.Groin, ParentCharacter));
        BodyParts.add(new BodyPart(BodyPartType.UpperLeg_L, ParentCharacter));
        BodyParts.add(new BodyPart(BodyPartType.UpperLeg_R, ParentCharacter));
        BodyParts.add(new BodyPart(BodyPartType.LowerLeg_L, ParentCharacter));
        BodyParts.add(new BodyPart(BodyPartType.LowerLeg_R, ParentCharacter));
        BodyParts.add(new BodyPart(BodyPartType.Foot_L, ParentCharacter));
        BodyParts.add(new BodyPart(BodyPartType.Foot_R, ParentCharacter));
        RestoreToFullHealth();
        ParentChar = ParentCharacter;
    }

    public BodyPart getBodyPart(BodyPartType type)
    {
        return (BodyPart)BodyParts.get(BodyPartType.ToIndex(type));
    }

    public void load(DataInputStream input)
        throws IOException
    {
        for(int n = 0; n < getBodyParts().size(); n++)
        {
            BodyPart p = (BodyPart)getBodyParts().get(n);
            p.SetBitten(input.readBoolean());
            p.SetScratched(input.readBoolean());
            p.SetBandaged(input.readBoolean());
            p.SetBleeding(input.readBoolean());
            p.SetWounded(input.readBoolean());
            p.SetFakeInfected(input.readBoolean());
            p.SetInfected(input.readBoolean());
            p.SetHealth(input.readFloat());
        }

        setInfectionLevel(input.readFloat());
        setFakeInfectionLevel(input.readFloat());
    }

    public void load(ByteBuffer input)
        throws IOException
    {
        for(int n = 0; n < getBodyParts().size(); n++)
        {
            BodyPart p = (BodyPart)getBodyParts().get(n);
            p.SetBitten(input.get() == 1);
            p.SetScratched(input.get() == 1);
            p.SetBandaged(input.get() == 1);
            p.SetBleeding(input.get() == 1);
            p.SetWounded(input.get() == 1);
            p.SetFakeInfected(input.get() == 1);
            p.SetInfected(input.get() == 1);
            p.SetHealth(input.getFloat());
        }

        setInfectionLevel(input.getFloat());
        setFakeInfectionLevel(input.getFloat());
    }

    public void save(DataOutputStream output)
        throws IOException
    {
        for(int n = 0; n < getBodyParts().size(); n++)
        {
            BodyPart p = (BodyPart)getBodyParts().get(n);
            output.writeBoolean(p.IsBitten());
            output.writeBoolean(p.IsScratched());
            output.writeBoolean(p.IsBandaged());
            output.writeBoolean(p.IsBleeding());
            output.writeBoolean(p.IsWounded());
            output.writeBoolean(p.IsFakeInfected());
            output.writeBoolean(p.IsInfected());
            output.writeFloat(p.getHealth());
        }

        output.writeFloat(getInfectionLevel());
        output.writeFloat(getFakeInfectionLevel());
    }

    public void save(ByteBuffer output)
        throws IOException
    {
        for(int n = 0; n < getBodyParts().size(); n++)
        {
            BodyPart p = (BodyPart)getBodyParts().get(n);
            output.put(((byte)(p.IsBitten() ? 1 : 0)));
            output.put(((byte)(p.IsScratched() ? 1 : 0)));
            output.put(((byte)(p.IsBandaged() ? 1 : 0)));
            output.put(((byte)(p.IsBleeding() ? 1 : 0)));
            output.put(((byte)(p.IsWounded() ? 1 : 0)));
            output.put(((byte)(p.IsFakeInfected() ? 1 : 0)));
            output.put(((byte)(p.IsInfected() ? 1 : 0)));
            output.putFloat(p.getHealth());
        }

        output.putFloat(getInfectionLevel());
        output.putFloat(getFakeInfectionLevel());
    }

    public boolean IsFakeInfected()
    {
        return isIsFakeInfected();
    }

    public void OnFire(boolean OnFire)
    {
        setIsOnFire(OnFire);
    }

    public boolean IsOnFire()
    {
        return isIsOnFire();
    }

    public boolean WasBurntToDeath()
    {
        return isBurntToDeath();
    }

    public void IncreasePanic(int NumNewZombiesSeen)
    {
        float del = 1.0F - getParentChar().getBetaDelta();
        if(del > 1.0F)
            del = 1.0F;
        if(del < 0.0F)
            del = 0.0F;
        float traitPanic = 1.0F;
        if(getParentChar().HasTrait("Cowardly"))
            del *= 2.0F;
        if(getParentChar().HasTrait("Brave"))
            del *= 0.3F;
        if(getParentChar().getBetaEffect() > 0)
            ParentChar.getStats().Panic += getPanicIncreaseValue() * (float)NumNewZombiesSeen * del;
        else
            ParentChar.getStats().Panic += getPanicIncreaseValue() * (float)NumNewZombiesSeen;
        if(getParentChar().getStats().Panic > 100F)
            ParentChar.getStats().Panic = 100F;
    }

    public void ReducePanic()
    {
        ParentChar.getStats().Panic -= getPanicReductionValue();
        if(getParentChar().getStats().Panic < 0.0F)
            ParentChar.getStats().Panic = 0.0F;
    }

    public void UpdatePanicState()
    {
        int NumVisibleZombies = getParentChar().getStats().NumVisibleZombies;
        if(NumVisibleZombies > getOldNumZombiesVisible())
            IncreasePanic(NumVisibleZombies - getOldNumZombiesVisible());
        else
            ReducePanic();
        setOldNumZombiesVisible(NumVisibleZombies);
    }

    public void JustDrankBooze()
    {
        float del = 1.0F;
        if(getParentChar().HasTrait("HeavyDrinker"))
            del = 0.3F;
        if(getParentChar().HasTrait("LightDrinker"))
            del = 4F;
        ParentChar.getStats().Drunkenness += getDrunkIncreaseValue() * del;
        if(getParentChar().getStats().Drunkenness > 100F)
            ParentChar.getStats().Drunkenness = 100F;
    }

    public void JustAteFood(Food NewFood)
    {
        setBoredomLevel(getBoredomLevel() + NewFood.getBoredomChange());
        if(getBoredomLevel() < 0.0F)
            setBoredomLevel(0.0F);
        setUnhappynessLevel(getUnhappynessLevel() + NewFood.getUnhappyChange());
        if(getUnhappynessLevel() < 0.0F)
            setUnhappynessLevel(0.0F);
        if(getParentChar().getStats().hunger < 0.2F)
            ParentChar.getStats().hunger = 0.0F;
        if(getParentChar().getStats().hunger > 0.0F)
            return;
        setHealthFromFoodTimer((int)((float)getHealthFromFoodTimer() + (float)getStandardHealthFromFoodTime() * GameTime.instance.getMultiplier()));
        if(NewFood.isCooked())
            setHealthFromFoodTimer((int)((float)getHealthFromFoodTimer() + (float)getStandardHealthFromFoodTime() * GameTime.instance.getMultiplier()));
        if(getHealthFromFoodTimer() > getStandardHealthFromFoodTime() * 4)
            setHealthFromFoodTimer(getStandardHealthFromFoodTime() * 4);
        if(!NewFood.isCooked() && NewFood.isbDangerousUncooked())
        {
            setHealthFromFoodTimer(0);
            int IllnessChance = 75;
            if(getParentChar().HasTrait("Resilient"))
                IllnessChance /= 2;
            if(getParentChar().HasTrait("ProneToIllness"))
                IllnessChance *= 2;
            if(Rand.Next(100) < IllnessChance && !isInf())
                setIsFakeInfected(true);
            return;
        }
        if(NewFood.getAge() >= (float)NewFood.getOffAge())
        {
            float Offness = NewFood.getAge() - (float)NewFood.getOffAge();
            if(Offness == 0.0F)
                Offness = 1.0F;
            int IllnessChance;
            if(NewFood.getOffAgeMax() > NewFood.getOffAge())
                IllnessChance = (int)((Offness / (float)(NewFood.getOffAgeMax() - NewFood.getOffAge())) * 100F);
            else
                IllnessChance = 100;
            if(getParentChar().HasTrait("Resilient"))
                IllnessChance /= 2;
            if(getParentChar().HasTrait("ProneToIllness"))
                IllnessChance *= 2;
            if(Rand.Next(100) < IllnessChance && !isInf())
                setIsFakeInfected(true);
        }
    }

    public void JustReadSomething(Literature lit)
    {
        setBoredomLevel(getBoredomLevel() + lit.getBoredomChange());
        if(getBoredomLevel() < 0.0F)
            setBoredomLevel(0.0F);
        setUnhappynessLevel(getUnhappynessLevel() + lit.getUnhappyChange());
        if(getUnhappynessLevel() < 0.0F)
            setUnhappynessLevel(0.0F);
    }

    public void JustTookPainMeds()
    {
        ParentChar.getStats().Pain -= getPainReductionFromMeds();
        if(getParentChar().getStats().Pain < 0.0F)
            ParentChar.getStats().Pain = 0.0F;
    }

    public void UpdateWetness()
    {
        if(getParentChar().getCurrentSquare().getRoom() != null || !RainManager.IsRaining)
        {
            setWetness(getWetness() - getWetnessReductionValue() * GameTime.instance.getMultiplier());
            if(getWetness() < 0.0F)
                setWetness(0.0F);
        } else
        {
            float val = RainManager.RainIntensity;
            if((double)val < 0.20000000000000001D)
                val = 0.0F;
            setWetness(getWetness() + getWetnessIncreaseValue() * val * GameTime.instance.getMultiplier());
            if(getWetness() > 100F)
                setWetness(100F);
        }
        if(!ParentChar.HasTrait("Outdoorsman") && !isHasACold() && getParentChar().getMoodles().getMoodleLevel(MoodleType.Wet) > 1)
        {
            float Delta = 1.0F;
            if(getParentChar().getMoodles().getMoodleLevel(MoodleType.Wet) == 2)
                Delta = 1.0F;
            if(getParentChar().getMoodles().getMoodleLevel(MoodleType.Wet) == 3)
                Delta = 1.5F;
            if(getParentChar().getMoodles().getMoodleLevel(MoodleType.Wet) == 4)
                Delta = 2.0F;
            if(getParentChar().HasTrait("ProneToIllness"))
                Delta *= 1.7F;
            if(getParentChar().HasTrait("Resilient"))
                Delta *= 0.45F;
            setCatchACold(getCatchACold() + getCatchAColdIncreaseRate() * Delta * GameTime.instance.getMultiplier());
            if(getCatchACold() >= 100F)
            {
                setCatchACold(0.0F);
                setHasACold(true);
                setColdStrength(20F);
                setTimeToSneezeOrCough(0);
            }
        }
        if(getParentChar().getMoodles().getMoodleLevel(MoodleType.Wet) < 2)
        {
            setCatchACold(getCatchACold() - getCatchAColdDecreaseRate());
            if(getCatchACold() <= 0.0F)
                setCatchACold(0.0F);
        }
    }

    public void TriggerSneezeCough()
    {
        if(getSneezeCoughActive() > 0)
            return;
        if(Rand.Next(100) > 50)
            setSneezeCoughActive(1);
        else
            setSneezeCoughActive(2);
        if(getParentChar().getMoodles().getMoodleLevel(MoodleType.HasACold) == 2)
            setSneezeCoughActive(1);
        setSneezeCoughTime(getSneezeCoughDelay());
        if(getParentChar().getMoodles().getMoodleLevel(MoodleType.HasACold) == 2)
            setTimeToSneezeOrCough(getMildColdSneezeTimerMin() + Rand.Next(getMildColdSneezeTimerMax() - getMildColdSneezeTimerMin()));
        if(getParentChar().getMoodles().getMoodleLevel(MoodleType.HasACold) == 3)
            setTimeToSneezeOrCough(getColdSneezeTimerMin() + Rand.Next(getColdSneezeTimerMax() - getColdSneezeTimerMin()));
        if(getParentChar().getMoodles().getMoodleLevel(MoodleType.HasACold) == 4)
            setTimeToSneezeOrCough(getNastyColdSneezeTimerMin() + Rand.Next(getNastyColdSneezeTimerMax() - getNastyColdSneezeTimerMin()));
        boolean TissueConsumed = false;
        if(getParentChar().getPrimaryHandItem() != null && getParentChar().getPrimaryHandItem().getType().equals("Tissue"))
        {
            if(((Drainable)getParentChar().getPrimaryHandItem()).getUsedDelta() > 0.0F)
            {
                ((Drainable)getParentChar().getPrimaryHandItem()).setUsedDelta(((Drainable)getParentChar().getPrimaryHandItem()).getUsedDelta() - 0.1F);
                if(((Drainable)getParentChar().getPrimaryHandItem()).getUsedDelta() <= 0.0F)
                    getParentChar().getPrimaryHandItem().Use();
                TissueConsumed = true;
            }
        } else
        if(getParentChar().getSecondaryHandItem() != null && getParentChar().getSecondaryHandItem().getType().equals("Tissue") && ((Drainable)getParentChar().getSecondaryHandItem()).getUsedDelta() > 0.0F)
        {
            ((Drainable)getParentChar().getSecondaryHandItem()).setUsedDelta(((Drainable)getParentChar().getSecondaryHandItem()).getUsedDelta() - 0.1F);
            if(((Drainable)getParentChar().getSecondaryHandItem()).getUsedDelta() <= 0.0F)
                getParentChar().getSecondaryHandItem().Use();
            TissueConsumed = true;
        }
        if(TissueConsumed)
        {
            setSneezeCoughActive(getSneezeCoughActive() + 2);
        } else
        {
            int Dist = 20;
            int Vol = 20;
            if(getSneezeCoughActive() == 1)
            {
                Dist = 20;
                Vol = 25;
            }
            if(getSneezeCoughActive() == 2)
            {
                Dist = 35;
                Vol = 40;
            }
            WorldSoundManager.instance.addSound(getParentChar(), (int)getParentChar().getX(), (int)getParentChar().getY(), (int)getParentChar().getZ(), Dist, Vol, true);
        }
    }

    public int IsSneezingCoughing()
    {
        return getSneezeCoughActive();
    }

    public void UpdateCold()
    {
        if(isHasACold())
        {
            boolean Recovering = true;
            if(getParentChar().getCurrentSquare().getRoom() == null || getParentChar().getMoodles().getMoodleLevel(MoodleType.Wet) > 0 || getParentChar().getStats().fatigue > 0.5F || getParentChar().getStats().hunger > 0.25F || getParentChar().getStats().thirst > 0.25F)
                Recovering = false;
            if(Recovering)
            {
                float Delta = 1.0F;
                if(getParentChar().HasTrait("ProneToIllness"))
                    Delta = 0.5F;
                if(getParentChar().HasTrait("Resilient"))
                    Delta = 1.5F;
                setColdStrength(getColdStrength() - getColdProgressionRate() * Delta * GameTime.instance.getMultiplier());
                if(getColdStrength() < 0.0F)
                {
                    setColdStrength(0.0F);
                    setHasACold(false);
                    setCatchACold(0.0F);
                }
            } else
            {
                float Delta = 1.0F;
                if(getParentChar().HasTrait("ProneToIllness"))
                    Delta = 1.2F;
                if(getParentChar().HasTrait("Resilient"))
                    Delta = 0.8F;
                setColdStrength(getColdStrength() + getColdProgressionRate() * Delta * GameTime.instance.getMultiplier());
                if(getColdStrength() > 100F)
                    setColdStrength(100F);
            }
            if(getSneezeCoughTime() > 0)
            {
                setSneezeCoughTime(getSneezeCoughTime() - 1);
                if(getSneezeCoughTime() == 0)
                    setSneezeCoughActive(0);
            }
            if(getParentChar().getMoodles().getMoodleLevel(MoodleType.HasACold) > 1 && getTimeToSneezeOrCough() >= 0 && !ParentChar.IsSpeaking())
            {
                setTimeToSneezeOrCough(getTimeToSneezeOrCough() - 1);
                if(getTimeToSneezeOrCough() <= 0)
                    TriggerSneezeCough();
            }
        }
    }

    public float getColdStrength()
    {
        if(isHasACold())
            return ColdStrength;
        else
            return 0.0F;
    }

    public float getWetness()
    {
        return Wetness;
    }

    public void AddDamage(BodyPartType BodyPart, float Val)
    {
        ((BodyPart)getBodyParts().get(BodyPartType.ToIndex(BodyPart))).AddDamage(Val);
        if(getOverallBodyHealth() > 0.0F)
            Sidebar.instance.TriggerHeartWiggle();
    }

    public void AddGeneralHealth(float Val)
    {
        int NumDamagedParts = 0;
        for(int i = 0; i < BodyPartType.ToIndex(BodyPartType.MAX); i++)
            if(((BodyPart)getBodyParts().get(i)).getHealth() < 100F)
                NumDamagedParts++;

        if(NumDamagedParts > 0)
        {
            float HealthPerPart = Val / (float)NumDamagedParts;
            for(int i = 0; i < BodyPartType.ToIndex(BodyPartType.MAX); i++)
                if(((BodyPart)getBodyParts().get(i)).getHealth() < 100F)
                    ((BodyPart)getBodyParts().get(i)).AddHealth(HealthPerPart);

        }
    }

    public void ReduceGeneralHealth(float Val)
    {
        float HealthPerPart = Val / (float)BodyPartType.ToIndex(BodyPartType.MAX);
        for(int i = 0; i < BodyPartType.ToIndex(BodyPartType.MAX); i++)
            ((BodyPart)getBodyParts().get(i)).ReduceHealth(HealthPerPart);

    }

    public void AddDamage(int BodyPartIndex, float val)
    {
        ((BodyPart)getBodyParts().get(BodyPartIndex)).AddDamage(val);
        if(getOverallBodyHealth() > 0.0F && getParentChar() == IsoPlayer.getInstance())
            Sidebar.instance.TriggerHeartWiggle();
    }

    public void AddRandomDamageFromZombie()
    {
        int DamageChance = 350;
        int PainType = 0;
        if("Idle".equals(getParentChar().getSprite().CurrentAnim.name))
            DamageChance *= 3;
        DamageChance *= CountSurroundingZombies(getParentChar().getCell(), getParentChar().getCurrentSquare());
        if(getParentChar().getBodyDamage().getHealth() <= 0.0F)
            DamageChance *= 10;
        int PartIndex = Rand.Next(BodyPartType.ToIndex(BodyPartType.MAX));
        if((PartIndex == BodyPartType.ToIndex(BodyPartType.Head) || PartIndex == BodyPartType.ToIndex(BodyPartType.Neck)) && Rand.Next(100) > 70)
        {
            boolean Done = false;
            do
            {
                if(Done)
                    break;
                Done = true;
                PartIndex = Rand.Next(BodyPartType.ToIndex(BodyPartType.MAX));
                if(PartIndex == BodyPartType.ToIndex(BodyPartType.Head) || PartIndex == BodyPartType.ToIndex(BodyPartType.Neck))
                    Done = false;
            } while(true);
        }
        float Damage = (float)Rand.Next(1000) / 1000F;
        Damage *= Rand.Next(10) + 10;
        AddDamage(PartIndex, Damage);
        boolean Scratch = false;
        boolean Bitten = false;
        if(Rand.Next(100) > 75)
        {
            Scratch = true;
            if(Rand.Next(100) > 75)
                Scratch = false;
            if(Scratch)
            {
                SetScratched(PartIndex, true);
                if(getHealth() > 0.0F)
                    SoundManager.instance.PlayWorldSound("zombiescratch", getParentChar().getCurrentSquare(), 0.0F, 15F, 0.5F, true);
                PainType = 1;
                getParentChar().Scratched();
            } else
            {
                if(getHealth() > 0.0F)
                    SoundManager.instance.PlayWorldSound("zombiebite", getParentChar().getCurrentSquare(), 0.0F, 15F, 1.7F, true);
                SetBitten(PartIndex, true);
                PainType = 2;
                getParentChar().Bitten();
                Bitten = true;
            }
        }
        if(FrameLoader.bServer)
            GameServer.instance.Damage(getParentChar().getRemoteID(), PartIndex, Damage, Scratch, Bitten, ((BodyPart)getBodyParts().get(PartIndex)).IsInfected());
        switch(PainType)
        {
        case 0: // '\0'
            ParentChar.getStats().Pain += getInitialThumpPain() * BodyPartType.getDamageModifyer(PartIndex);
            break;

        case 1: // '\001'
            ParentChar.getStats().Pain += getInitialScratchPain() * BodyPartType.getDamageModifyer(PartIndex);
            break;

        case 2: // '\002'
            ParentChar.getStats().Pain += getInitialBitePain() * BodyPartType.getDamageModifyer(PartIndex);
            break;
        }
        if(getParentChar().getStats().Pain > 100F)
            ParentChar.getStats().Pain = 100F;
        if(PainType > 0)
        {
            HurtBloodSplats(Rand.Next(2) + 1);
            HurtBloodSplats(Rand.Next(2) + 1);
            HurtBloodSplats(Rand.Next(2) + 1);
            HurtBloodSplats(Rand.Next(2) + 1);
        }
    }

    public void ApplyNetDamage(DamagePlayer response)
    {
        AddDamage(response.PartIndex, response.Damage);
        boolean Scratch = response.Scratch;
        boolean Bitten = response.Bitten;
        boolean infected = response.Infected;
        if(Scratch)
        {
            SetScratched(response.PartIndex, true);
            if(getHealth() > 0.0F)
                SoundManager.instance.PlayWorldSound("zombiescratch", getParentChar().getCurrentSquare(), 0.0F, 15F, 0.5F, true);
            getParentChar().Scratched();
        } else
        if(Bitten)
        {
            if(getHealth() > 0.0F)
                SoundManager.instance.PlayWorldSound("zombiebite", getParentChar().getCurrentSquare(), 0.0F, 15F, 1.7F, true);
            SetBitten(response.PartIndex, true, infected);
            getParentChar().Bitten();
        }
    }

    private void HurtBloodSplats(int num)
    {
        for(int i = 0; i < num; i++)
        {
            getParentChar().splatBlood(3, 0.3F);
            IsoGridSquare w = getParentChar().getCurrentSquare();
            if(Rand.Next(3) == 0)
                w = w.getW();
            else
            if(Rand.Next(3) == 0)
                w = w.getN();
            else
            if(Rand.Next(3) == 0)
                w = w.getE();
            else
            if(Rand.Next(3) == 0)
                w = w.getS();
            if(w == null)
                continue;
            if(Rand.Next(3) == 0)
            {
                Integer idf = Integer.valueOf(16);
                getParentChar().DoFloorSplat(w, (new StringBuilder()).append("BloodFloor_").append(idf).toString(), true, 0.0F, 0.2F);
                if(w != null)
                    getParentChar().DoFloorSplat(w.getW(), (new StringBuilder()).append("BloodFloor_").append(idf.intValue() + 1).toString(), true, 0.0F, 0.3F);
                continue;
            }
            if(Rand.Next(3) == 0)
            {
                Integer idf = Integer.valueOf(8);
                getParentChar().DoFloorSplat(w, (new StringBuilder()).append("BloodFloor_").append(idf).toString(), true, 0.0F, 0.3F);
                if(w != null)
                    getParentChar().DoFloorSplat(w.getS(), (new StringBuilder()).append("BloodFloor_").append(idf.intValue() + 1).toString(), true, 0.0F, 0.3F);
                continue;
            }
            if(Rand.Next(3) == 0)
            {
                Integer idf = Integer.valueOf(24);
                getParentChar().DoFloorSplat(w, (new StringBuilder()).append("BloodFloor_").append(idf).toString(), true, 0.0F, 0.3F);
                if(w == null)
                    continue;
                getParentChar().DoFloorSplat(w.getW(), (new StringBuilder()).append("BloodFloor_").append(idf.intValue() + 1).toString(), true, 0.0F, 0.3F);
                getParentChar().DoFloorSplat(w.getS(), (new StringBuilder()).append("BloodFloor_").append(idf.intValue() + 2).toString(), true, 0.0F, 0.3F);
                if(w.getS() != null)
                    getParentChar().DoFloorSplat(w.getS().getW(), (new StringBuilder()).append("BloodFloor_").append(idf.intValue() + 3).toString(), true, 0.0F, 0.3F);
            } else
            {
                Integer idf = Integer.valueOf(Rand.Next(3));
                getParentChar().DoFloorSplat(w, (new StringBuilder()).append("BloodFloor_").append(idf).toString(), true, 0.0F, 0.3F);
            }
        }

    }

    private int CountSurroundingZombies(IsoCell cell, IsoGridSquare gridSquare)
    {
        if(gridSquare == null)
            return 0;
        int NumFoundZombies = 0;
        IsoGridSquare NewSquare = null;
        NewSquare = cell.getGridSquare(gridSquare.getX(), gridSquare.getY() - 1, gridSquare.getZ());
        if(NewSquare != null)
            NumFoundZombies += NewSquare.getMovingObjects().size();
        NewSquare = cell.getGridSquare(gridSquare.getX() + 1, gridSquare.getY() - 1, gridSquare.getZ());
        if(NewSquare != null)
            NumFoundZombies += NewSquare.getMovingObjects().size();
        NewSquare = cell.getGridSquare(gridSquare.getX() + 1, gridSquare.getY(), gridSquare.getZ());
        if(NewSquare != null)
            NumFoundZombies += NewSquare.getMovingObjects().size();
        NewSquare = cell.getGridSquare(gridSquare.getX() + 1, gridSquare.getY() + 1, gridSquare.getZ());
        if(NewSquare != null)
            NumFoundZombies += NewSquare.getMovingObjects().size();
        NewSquare = cell.getGridSquare(gridSquare.getX(), gridSquare.getY() + 1, gridSquare.getZ());
        if(NewSquare != null)
            NumFoundZombies += NewSquare.getMovingObjects().size();
        NewSquare = cell.getGridSquare(gridSquare.getX() - 1, gridSquare.getY() + 1, gridSquare.getZ());
        if(NewSquare != null)
            NumFoundZombies += NewSquare.getMovingObjects().size();
        NewSquare = cell.getGridSquare(gridSquare.getX() - 1, gridSquare.getY(), gridSquare.getZ());
        if(NewSquare != null)
            NumFoundZombies += NewSquare.getMovingObjects().size();
        NewSquare = cell.getGridSquare(gridSquare.getX() - 1, gridSquare.getY() - 1, gridSquare.getZ());
        if(NewSquare != null)
            NumFoundZombies += NewSquare.getMovingObjects().size();
        NewSquare = cell.getGridSquare(gridSquare.getX(), gridSquare.getY(), gridSquare.getZ());
        if(NewSquare != null)
            NumFoundZombies += NewSquare.getMovingObjects().size();
        if(NumFoundZombies > 0)
            NumFoundZombies--;
        return NumFoundZombies;
    }

    public boolean DoesBodyPartHaveInjury(BodyPartType BodyPart)
    {
        return ((BodyPart)getBodyParts().get(BodyPartType.ToIndex(BodyPart))).HasInjury();
    }

    public void DrawUntexturedQuad(int X, int Y, int Width, int Height, float r, float g, float b, 
            float a)
    {
        SpriteRenderer.instance.render(null, X, Y, Width, Height, r, g, b, a);
    }

    public float getBodyPartHealth(BodyPartType BodyPart)
    {
        return ((BodyPart)getBodyParts().get(BodyPartType.ToIndex(BodyPart))).getHealth();
    }

    public float getBodyPartHealth(int BodyPartIndex)
    {
        return ((BodyPart)getBodyParts().get(BodyPartIndex)).getHealth();
    }

    public String getBodyPartName(BodyPartType BodyPart)
    {
        return BodyPartType.ToString(BodyPart);
    }

    public String getBodyPartName(int BodyPartIndex)
    {
        return BodyPartType.ToString(BodyPartType.FromIndex(BodyPartIndex));
    }

    public float getHealth()
    {
        return getOverallBodyHealth();
    }

    public float getInfectionLevel()
    {
        return getFakeInfectionLevel() <= InfectionLevel ? InfectionLevel : getFakeInfectionLevel();
    }

    public int getNumPartsBleeding()
    {
        int BleedingParts = 0;
        for(int i = 0; i < BodyPartType.ToIndex(BodyPartType.MAX); i++)
            if(((BodyPart)getBodyParts().get(i)).IsBleeding())
                BleedingParts++;

        return BleedingParts;
    }

    public int getNumPartsScratched()
    {
        int ScratchedParts = 0;
        for(int i = 0; i < BodyPartType.ToIndex(BodyPartType.MAX); i++)
            if(((BodyPart)getBodyParts().get(i)).IsScratched())
                ScratchedParts++;

        return ScratchedParts;
    }

    public int getNumPartsBitten()
    {
        int BittenParts = 0;
        for(int i = 0; i < BodyPartType.ToIndex(BodyPartType.MAX); i++)
            if(((BodyPart)getBodyParts().get(i)).IsBitten())
                BittenParts++;

        return BittenParts;
    }

    public boolean HasInjury()
    {
        for(int i = 0; i < BodyPartType.ToIndex(BodyPartType.MAX); i++)
            if(((BodyPart)getBodyParts().get(i)).HasInjury())
                return true;

        return false;
    }

    public boolean IsBandaged(BodyPartType BodyPart)
    {
        return ((BodyPart)getBodyParts().get(BodyPartType.ToIndex(BodyPart))).IsBandaged();
    }

    public boolean IsBandaged(int BodyPartIndex)
    {
        return ((BodyPart)getBodyParts().get(BodyPartIndex)).IsBandaged();
    }

    public boolean IsBitten(BodyPartType BodyPart)
    {
        return ((BodyPart)getBodyParts().get(BodyPartType.ToIndex(BodyPart))).IsBitten();
    }

    public boolean IsBitten(int BodyPartIndex)
    {
        return ((BodyPart)getBodyParts().get(BodyPartIndex)).IsBitten();
    }

    public boolean IsBleeding(BodyPartType BodyPart)
    {
        return ((BodyPart)getBodyParts().get(BodyPartType.ToIndex(BodyPart))).IsBleeding();
    }

    public boolean IsBleeding(int BodyPartIndex)
    {
        return ((BodyPart)getBodyParts().get(BodyPartIndex)).IsBleeding();
    }

    public boolean IsBleedingStemmed(BodyPartType BodyPart)
    {
        return ((BodyPart)getBodyParts().get(BodyPartType.ToIndex(BodyPart))).IsBleedingStemmed();
    }

    public boolean IsBleedingStemmed(int BodyPartIndex)
    {
        return ((BodyPart)getBodyParts().get(BodyPartIndex)).IsBleedingStemmed();
    }

    public boolean IsCortorised(BodyPartType BodyPart)
    {
        return ((BodyPart)getBodyParts().get(BodyPartType.ToIndex(BodyPart))).IsCortorised();
    }

    public boolean IsCortorised(int BodyPartIndex)
    {
        return ((BodyPart)getBodyParts().get(BodyPartIndex)).IsCortorised();
    }

    public boolean IsInfected()
    {
        return isInf() | isIsFakeInfected();
    }

    public boolean IsInfected(BodyPartType BodyPart)
    {
        return ((BodyPart)getBodyParts().get(BodyPartType.ToIndex(BodyPart))).IsInfected();
    }

    public boolean IsInfected(int BodyPartIndex)
    {
        return ((BodyPart)getBodyParts().get(BodyPartIndex)).IsInfected();
    }

    public boolean IsFakeInfected(int BodyPartIndex)
    {
        return ((BodyPart)getBodyParts().get(BodyPartIndex)).IsFakeInfected();
    }

    public void DisableFakeInfection(int BodyPartIndex)
    {
        ((BodyPart)getBodyParts().get(BodyPartIndex)).DisableFakeInfection();
    }

    public boolean IsScratched(BodyPartType BodyPart)
    {
        return ((BodyPart)getBodyParts().get(BodyPartType.ToIndex(BodyPart))).IsScratched();
    }

    public boolean IsScratched(int BodyPartIndex)
    {
        return ((BodyPart)getBodyParts().get(BodyPartIndex)).IsScratched();
    }

    public boolean IsStitched(BodyPartType BodyPart)
    {
        return ((BodyPart)getBodyParts().get(BodyPartType.ToIndex(BodyPart))).IsStitched();
    }

    public boolean IsStitched(int BodyPartIndex)
    {
        return ((BodyPart)getBodyParts().get(BodyPartIndex)).IsStitched();
    }

    public boolean IsWounded(BodyPartType BodyPart)
    {
        return ((BodyPart)getBodyParts().get(BodyPartType.ToIndex(BodyPart))).IsWounded();
    }

    public boolean IsWounded(int BodyPartIndex)
    {
        return ((BodyPart)getBodyParts().get(BodyPartIndex)).IsWounded();
    }

    public void RestoreToFullHealth()
    {
        for(int i = 0; i < BodyPartType.ToIndex(BodyPartType.MAX); i++)
            ((BodyPart)getBodyParts().get(i)).RestoreToFullHealth();

        setInf(false);
        setIsFakeInfected(false);
        setOverallBodyHealth(100F);
        setInfectionLevel(0.0F);
        setFakeInfectionLevel(0.0F);
        setBoredomLevel(0.0F);
        setWetness(0.0F);
        setCatchACold(0.0F);
        setHasACold(false);
        setColdStrength(0.0F);
        setSneezeCoughActive(0);
        setSneezeCoughTime(0);
        setUnhappynessLevel(0.0F);
    }

    public void SetBandaged(BodyPartType BodyPart, boolean Bandaged)
    {
        ((BodyPart)getBodyParts().get(BodyPartType.ToIndex(BodyPart))).SetBandaged(Bandaged);
    }

    public void SetBandaged(int BodyPartIndex, boolean Bandaged)
    {
        ((BodyPart)getBodyParts().get(BodyPartIndex)).SetBandaged(Bandaged);
    }

    public void SetBitten(BodyPartType BodyPart, boolean Bitten)
    {
        ((BodyPart)getBodyParts().get(BodyPartType.ToIndex(BodyPart))).SetBitten(Bitten);
    }

    public void SetBitten(int BodyPartIndex, boolean Bitten)
    {
        ((BodyPart)getBodyParts().get(BodyPartIndex)).SetBitten(Bitten);
    }

    public void SetBitten(int BodyPartIndex, boolean Bitten, boolean Infected)
    {
        ((BodyPart)getBodyParts().get(BodyPartIndex)).SetBitten(Bitten, Infected);
    }

    public void SetBleeding(BodyPartType BodyPart, boolean Bleeding)
    {
        ((BodyPart)getBodyParts().get(BodyPartType.ToIndex(BodyPart))).SetBleeding(Bleeding);
    }

    public void SetBleeding(int BodyPartIndex, boolean Bleeding)
    {
        ((BodyPart)getBodyParts().get(BodyPartIndex)).SetBleeding(Bleeding);
    }

    public void SetBleedingStemmed(BodyPartType BodyPart, boolean BleedingStemmed)
    {
        ((BodyPart)getBodyParts().get(BodyPartType.ToIndex(BodyPart))).SetBleedingStemmed(BleedingStemmed);
    }

    public void SetBleedingStemmed(int BodyPartIndex, boolean BleedingStemmed)
    {
        ((BodyPart)getBodyParts().get(BodyPartIndex)).SetBleedingStemmed(BleedingStemmed);
    }

    public void SetCortorised(BodyPartType BodyPart, boolean Cortorised)
    {
        ((BodyPart)getBodyParts().get(BodyPartType.ToIndex(BodyPart))).SetCortorised(Cortorised);
    }

    public void SetCortorised(int BodyPartIndex, boolean Cortorised)
    {
        ((BodyPart)getBodyParts().get(BodyPartIndex)).SetCortorised(Cortorised);
    }

    public void SetScratched(BodyPartType BodyPart, boolean Scratched)
    {
        ((BodyPart)getBodyParts().get(BodyPartType.ToIndex(BodyPart))).SetScratched(Scratched);
    }

    public void SetScratched(int BodyPartIndex, boolean Scratched)
    {
        ((BodyPart)getBodyParts().get(BodyPartIndex)).SetScratched(Scratched);
    }

    public void SetWounded(BodyPartType BodyPart, boolean Wounded)
    {
        ((BodyPart)getBodyParts().get(BodyPartType.ToIndex(BodyPart))).SetWounded(Wounded);
    }

    public void SetWounded(int BodyPartIndex, boolean Wounded)
    {
        ((BodyPart)getBodyParts().get(BodyPartIndex)).SetWounded(Wounded);
    }

    public void ShowDebugInfo()
    {
        int InfoX = 70;
        int InfoY = 10;
        int InfoStep = 20;
        if(getDamageModCount() > 0)
            setDamageModCount(getDamageModCount() - 1);
    }

    public void UpdateBoredom()
    {
        if(getParentChar() instanceof IsoSurvivor)
            return;
        if(getParentChar() == IsoPlayer.instance && IsoPlayer.instance.Asleep)
            return;
        if(getParentChar().getCurrentSquare().getRoom() != null)
        {
            setBoredomLevel((float)((double)getBoredomLevel() + ZomboidGlobals.BoredomIncreaseRate * (double)GameTime.instance.getMultiplier()));
            if(getParentChar().IsSpeaking())
                setBoredomLevel((float)((double)getBoredomLevel() - ZomboidGlobals.BoredomDecreaseRate * (double)GameTime.instance.getMultiplier()));
            if(getParentChar().getNumSurvivorsInVicinity() > 0)
                setBoredomLevel((float)((double)getBoredomLevel() - ZomboidGlobals.BoredomDecreaseRate * 0.10000000149011612D * (double)GameTime.instance.getMultiplier()));
        } else
        {
            setBoredomLevel((float)((double)getBoredomLevel() - ZomboidGlobals.BoredomDecreaseRate * 0.10000000149011612D * (double)GameTime.instance.getMultiplier()));
        }
        if(getParentChar().getStats().Drunkenness > 20F)
            setBoredomLevel((float)((double)getBoredomLevel() - ZomboidGlobals.BoredomDecreaseRate * 2D * (double)GameTime.instance.getMultiplier()));
        if(getParentChar().getStats().Panic > 5F)
            setBoredomLevel(0.0F);
        if(getBoredomLevel() > 100F)
            setBoredomLevel(100F);
        if(getBoredomLevel() < 0.0F)
            setBoredomLevel(0.0F);
        if(getUnhappynessLevel() > 100F)
            setUnhappynessLevel(100F);
        if(getUnhappynessLevel() < 0.0F)
            setUnhappynessLevel(0.0F);
        if(getParentChar().getMoodles().getMoodleLevel(MoodleType.Bored) > 1)
            setUnhappynessLevel((float)((double)getUnhappynessLevel() + ZomboidGlobals.UnhappinessIncrease * (double)(float)getParentChar().getMoodles().getMoodleLevel(MoodleType.Bored) * (double)GameTime.instance.getMultiplier()));
    }

    public float getUnhappynessLevel()
    {
        return UnhappynessLevel;
    }

    public float getBoredomLevel()
    {
        return BoredomLevel;
    }

    public void UpdateStrength()
    {
        if(getParentChar() != getParentChar())
            return;
        int MaximumCarryWeight = 300;
        int NumStrengthReducers = 0;
        if(getParentChar().getMoodles().getMoodleLevel(MoodleType.Hungry) == 2)
            NumStrengthReducers++;
        if(getParentChar().getMoodles().getMoodleLevel(MoodleType.Hungry) == 3)
            NumStrengthReducers += 2;
        if(getParentChar().getMoodles().getMoodleLevel(MoodleType.Hungry) == 4)
            NumStrengthReducers += 3;
        if(getParentChar().getMoodles().getMoodleLevel(MoodleType.Thirst) == 2)
            NumStrengthReducers++;
        if(getParentChar().getMoodles().getMoodleLevel(MoodleType.Thirst) == 3)
            NumStrengthReducers += 2;
        if(getParentChar().getMoodles().getMoodleLevel(MoodleType.Thirst) == 4)
            NumStrengthReducers += 3;
        if(getParentChar().getMoodles().getMoodleLevel(MoodleType.Sick) == 2)
            NumStrengthReducers++;
        if(getParentChar().getMoodles().getMoodleLevel(MoodleType.Sick) == 3)
            NumStrengthReducers += 2;
        if(getParentChar().getMoodles().getMoodleLevel(MoodleType.Sick) == 4)
            NumStrengthReducers += 3;
        if(getParentChar().getMoodles().getMoodleLevel(MoodleType.Bleeding) == 2)
            NumStrengthReducers++;
        if(getParentChar().getMoodles().getMoodleLevel(MoodleType.Bleeding) == 3)
            NumStrengthReducers += 2;
        if(getParentChar().getMoodles().getMoodleLevel(MoodleType.Bleeding) == 4)
            NumStrengthReducers += 3;
        if(getParentChar().getMoodles().getMoodleLevel(MoodleType.Injured) == 2)
            NumStrengthReducers++;
        if(getParentChar().getMoodles().getMoodleLevel(MoodleType.Injured) == 3)
            NumStrengthReducers += 2;
        if(getParentChar().getMoodles().getMoodleLevel(MoodleType.Injured) == 4)
            NumStrengthReducers += 3;
        getParentChar().setMaxWeight(Integer.valueOf(Integer.valueOf((int)((float)getParentChar().getMaxWeightBase() * getParentChar().getWeightMod())).intValue() - 8 * NumStrengthReducers));
        if(getParentChar().getMaxWeight().intValue() < 0)
            getParentChar().setMaxWeight(Integer.valueOf(0));
        if(getParentChar().getMoodles().getMoodleLevel(MoodleType.FoodEaten) > 0)
            getParentChar().setMaxWeight(Integer.valueOf(getParentChar().getMaxWeight().intValue() + 30));
        if(getParentChar().HasTrait("Weak"))
        {
            getParentChar().setMaxWeight(Integer.valueOf(getParentChar().getMaxWeight().intValue() - 100));
            if(getParentChar().getMaxWeight().intValue() < 0)
                getParentChar().setMaxWeight(Integer.valueOf(0));
        }
        if(getParentChar().HasTrait("Strong"))
            getParentChar().setMaxWeight(Integer.valueOf(getParentChar().getMaxWeight().intValue() + 100));
    }

    public void Update()
    {
        if(getParentChar() instanceof IsoZombie)
            return;
        int n = getNumPartsBleeding() * 2;
        n += getNumPartsScratched();
        n += getNumPartsBitten() * 6;
        if(n > 0 && getHealth() < 60F || n > 3)
        {
            float bleedChance = (1.0F / (float)n) * 200F * GameTime.instance.getInvMultiplier();
            if((float)Rand.Next((int)bleedChance) < bleedChance * 0.3F)
                getParentChar().dripBloodFloor(0.3F);
            if((float)Rand.Next((int)bleedChance) < bleedChance * 0.3F)
                getParentChar().splatBlood(1, 0.3F);
            if(Rand.Next((int)bleedChance) == 0)
                getParentChar().splatBloodFloor(0.3F);
        }
        UpdateWetness();
        UpdateCold();
        UpdateBoredom();
        UpdateStrength();
        UpdatePanicState();
        if(getOverallBodyHealth() == 0.0F)
            return;
        if(getInfectionLevel() == 100F)
            return;
        if(!isInf())
        {
            for(int i = 0; i < BodyPartType.ToIndex(BodyPartType.MAX); i++)
            {
                if(!IsInfected(i))
                    continue;
                setInf(true);
                if(IsFakeInfected(i))
                {
                    DisableFakeInfection(i);
                    setInfectionLevel(getFakeInfectionLevel());
                    setFakeInfectionLevel(0.0F);
                    setIsFakeInfected(false);
                }
            }

        }
        if(!isInf() && !isIsFakeInfected())
        {
            for(int i = 0; i < BodyPartType.ToIndex(BodyPartType.MAX); i++)
                if(IsFakeInfected(i))
                    setIsFakeInfected(true);

        }
        if(isInf())
        {
            float del = 1.0F;
            if(getParentChar().HasTrait("Resilient"))
                del *= 0.3F;
            if(getParentChar().HasTrait("ProneToIllness"))
                del *= 3F;
            setInfectionLevel(getInfectionLevel() + getInfectionGrowthRate() * del * GameTime.instance.getMultiplier());
            if(getInfectionLevel() > 100F)
                setInfectionLevel(100F);
        }
        if(isIsFakeInfected())
        {
            setFakeInfectionLevel(getFakeInfectionLevel() + getInfectionGrowthRate() * GameTime.instance.getMultiplier());
            if(getFakeInfectionLevel() > 100F)
                setFakeInfectionLevel(100F);
        }
        ParentChar.getStats().Drunkenness -= getDrunkReductionValue() * GameTime.instance.getMultiplier();
        if(getParentChar().getStats().Drunkenness < 0.0F)
            ParentChar.getStats().Drunkenness = 0.0F;
        float HealthToAdd = 0.0F;
        if(getHealthFromFoodTimer() > 0)
        {
            HealthToAdd += getHealthFromFood();
            setHealthFromFoodTimer(getHealthFromFoodTimer() - 1);
        }
        int Reduced = 0;
        if(getParentChar() == getParentChar() && (getParentChar().getMoodles().getMoodleLevel(MoodleType.Hungry) == 2 || getParentChar().getMoodles().getMoodleLevel(MoodleType.Sick) == 2 || getParentChar().getMoodles().getMoodleLevel(MoodleType.Thirst) == 2))
            Reduced = 1;
        if(getParentChar() == getParentChar() && (getParentChar().getMoodles().getMoodleLevel(MoodleType.Hungry) == 3 || getParentChar().getMoodles().getMoodleLevel(MoodleType.Sick) == 3 || getParentChar().getMoodles().getMoodleLevel(MoodleType.Thirst) == 3))
            Reduced = 2;
        switch(Reduced)
        {
        case 0: // '\0'
            HealthToAdd += getStandardHealthAddition() * GameTime.instance.getMultiplier();
            break;

        case 1: // '\001'
            HealthToAdd += getReducedHealthAddition() * GameTime.instance.getMultiplier();
            break;

        case 2: // '\002'
            HealthToAdd += getSeverlyReducedHealthAddition() * GameTime.instance.getMultiplier();
            break;
        }
        if(getParentChar().isAsleep())
            HealthToAdd += getSleepingHealthAddition() * GameTime.instance.getMultiplier();
        AddGeneralHealth(HealthToAdd);
        if(getParentChar() == getParentChar())
        {
            float ReductionAmmount = 0.0F;
            if(getParentChar().getMoodles().getMoodleLevel(MoodleType.Hungry) == 4)
                ReductionAmmount += getHealthReductionFromSevereBadMoodles() * GameTime.instance.getMultiplier();
            if(getParentChar().getMoodles().getMoodleLevel(MoodleType.Sick) == 4)
                ReductionAmmount += getHealthReductionFromSevereBadMoodles() * GameTime.instance.getMultiplier();
            if(getParentChar().getMoodles().getMoodleLevel(MoodleType.Bleeding) == 4)
                ReductionAmmount += getHealthReductionFromSevereBadMoodles() * GameTime.instance.getMultiplier();
            if(getParentChar().getMoodles().getMoodleLevel(MoodleType.Injured) == 4)
                ReductionAmmount += getHealthReductionFromSevereBadMoodles() * GameTime.instance.getMultiplier();
            if(getParentChar().getMoodles().getMoodleLevel(MoodleType.Thirst) == 4)
                ReductionAmmount += getHealthReductionFromSevereBadMoodles() * GameTime.instance.getMultiplier();
            ReduceGeneralHealth(ReductionAmmount);
        }
        if(getInfectionLevel() > 0.0F)
            ReduceGeneralHealth(getInfectionHealthReductionAmmount());
        for(int i = 0; i < BodyPartType.ToIndex(BodyPartType.MAX); i++)
            if(((BodyPart)getBodyParts().get(i)).getHealth() < 100F)
                ParentChar.getStats().Pain += getContinualPainIncrease() * BodyPartType.getDamageModifyer(i) * GameTime.instance.getMultiplier();

        if(getParentChar().getStats().Pain > 100F)
            ParentChar.getStats().Pain = 100F;
        float TotalDamage = 0.0F;
        for(int i = 0; i < BodyPartType.ToIndex(BodyPartType.MAX); i++)
        {
            ((BodyPart)getBodyParts().get(i)).DamageUpdate();
            TotalDamage += (100F - ((BodyPart)getBodyParts().get(i)).getHealth()) * BodyPartType.getDamageModifyer(i);
        }

        if(TotalDamage > 100F)
            TotalDamage = 100F;
        setOverallBodyHealth(100F - TotalDamage);
        if(getOverallBodyHealth() == 0.0F)
        {
            if(isIsOnFire())
            {
                setBurntToDeath(true);
                for(int i = 0; i < BodyPartType.ToIndex(BodyPartType.MAX); i++)
                    ((BodyPart)getBodyParts().get(i)).SetHealth(Rand.Next(90));

            } else
            {
                setBurntToDeath(false);
            }
            if(getInfectionLevel() > 0.0F)
                getParentChar().getStateMachine().changeState(ReanimateState.instance());
            else
                getParentChar().getStateMachine().changeState(DieState.instance());
        }
        if(getInfectionLevel() >= 100F && getOverallBodyHealth() > 0.0F)
            getParentChar().getStateMachine().changeState(ReanimateState.instance());
        if(getFakeInfectionLevel() >= 100F && getOverallBodyHealth() > 0.0F)
        {
            setIsFakeInfected(false);
            setFakeInfectionLevel(0.0F);
        }
    }

    public boolean UseBandageOnMostNeededPart()
    {
        int highestScore = 0;
        BodyPart part = null;
        for(int n = 0; n < getBodyParts().size(); n++)
        {
            int score = 0;
            if(((BodyPart)getBodyParts().get(n)).IsBandaged())
                continue;
            if(((BodyPart)getBodyParts().get(n)).IsBleeding())
                score += 100;
            if(((BodyPart)getBodyParts().get(n)).IsScratched())
                score += 50;
            if(((BodyPart)getBodyParts().get(n)).IsBitten())
                score += 50;
            if(score > highestScore)
            {
                highestScore = score;
                part = (BodyPart)getBodyParts().get(n);
            }
        }

        if(highestScore > 0 && part != null)
        {
            part.SetBandaged(true);
            return true;
        } else
        {
            return false;
        }
    }

    public void ReduceFactor()
    {
        setInf(true);
    }

    public NulledArrayList getBodyParts()
    {
        return BodyParts;
    }

    public void setBodyParts(NulledArrayList BodyParts)
    {
        this.BodyParts = BodyParts;
    }

    public int getDamageModCount()
    {
        return DamageModCount;
    }

    public void setDamageModCount(int DamageModCount)
    {
        this.DamageModCount = DamageModCount;
    }

    public float getInfectionGrowthRate()
    {
        return InfectionGrowthRate;
    }

    public void setInfectionGrowthRate(float InfectionGrowthRate)
    {
        this.InfectionGrowthRate = InfectionGrowthRate;
    }

    public void setInfectionLevel(float InfectionLevel)
    {
        this.InfectionLevel = InfectionLevel;
    }

    public boolean isInf()
    {
        return inf;
    }

    public void setInf(boolean inf)
    {
        this.inf = inf;
    }

    public float getFakeInfectionLevel()
    {
        return FakeInfectionLevel;
    }

    public void setFakeInfectionLevel(float FakeInfectionLevel)
    {
        this.FakeInfectionLevel = FakeInfectionLevel;
    }

    public boolean isIsFakeInfected()
    {
        return IsFakeInfected;
    }

    public void setIsFakeInfected(boolean IsFakeInfected)
    {
        this.IsFakeInfected = IsFakeInfected;
    }

    public float getOverallBodyHealth()
    {
        return OverallBodyHealth;
    }

    public void setOverallBodyHealth(float OverallBodyHealth)
    {
        this.OverallBodyHealth = OverallBodyHealth;
    }

    public float getStandardHealthAddition()
    {
        return StandardHealthAddition;
    }

    public void setStandardHealthAddition(float StandardHealthAddition)
    {
        this.StandardHealthAddition = StandardHealthAddition;
    }

    public float getReducedHealthAddition()
    {
        return ReducedHealthAddition;
    }

    public void setReducedHealthAddition(float ReducedHealthAddition)
    {
        this.ReducedHealthAddition = ReducedHealthAddition;
    }

    public float getSeverlyReducedHealthAddition()
    {
        return SeverlyReducedHealthAddition;
    }

    public void setSeverlyReducedHealthAddition(float SeverlyReducedHealthAddition)
    {
        this.SeverlyReducedHealthAddition = SeverlyReducedHealthAddition;
    }

    public float getSleepingHealthAddition()
    {
        return SleepingHealthAddition;
    }

    public void setSleepingHealthAddition(float SleepingHealthAddition)
    {
        this.SleepingHealthAddition = SleepingHealthAddition;
    }

    public float getHealthFromFood()
    {
        return HealthFromFood;
    }

    public void setHealthFromFood(float HealthFromFood)
    {
        this.HealthFromFood = HealthFromFood;
    }

    public float getHealthReductionFromSevereBadMoodles()
    {
        return HealthReductionFromSevereBadMoodles;
    }

    public void setHealthReductionFromSevereBadMoodles(float HealthReductionFromSevereBadMoodles)
    {
        this.HealthReductionFromSevereBadMoodles = HealthReductionFromSevereBadMoodles;
    }

    public float getInfectionHealthReductionAmmount()
    {
        return InfectionHealthReductionAmmount;
    }

    public void setInfectionHealthReductionAmmount(float InfectionHealthReductionAmmount)
    {
        this.InfectionHealthReductionAmmount = InfectionHealthReductionAmmount;
    }

    public int getStandardHealthFromFoodTime()
    {
        return StandardHealthFromFoodTime;
    }

    public void setStandardHealthFromFoodTime(int StandardHealthFromFoodTime)
    {
        this.StandardHealthFromFoodTime = StandardHealthFromFoodTime;
    }

    public int getHealthFromFoodTimer()
    {
        return HealthFromFoodTimer;
    }

    public void setHealthFromFoodTimer(int HealthFromFoodTimer)
    {
        this.HealthFromFoodTimer = HealthFromFoodTimer;
    }

    public void setBoredomLevel(float BoredomLevel)
    {
        this.BoredomLevel = BoredomLevel;
    }

    public float getBoredomDecreaseFromReading()
    {
        return BoredomDecreaseFromReading;
    }

    public void setBoredomDecreaseFromReading(float BoredomDecreaseFromReading)
    {
        this.BoredomDecreaseFromReading = BoredomDecreaseFromReading;
    }

    public float getInitialThumpPain()
    {
        return InitialThumpPain;
    }

    public void setInitialThumpPain(float InitialThumpPain)
    {
        this.InitialThumpPain = InitialThumpPain;
    }

    public float getInitialScratchPain()
    {
        return InitialScratchPain;
    }

    public void setInitialScratchPain(float InitialScratchPain)
    {
        this.InitialScratchPain = InitialScratchPain;
    }

    public float getInitialBitePain()
    {
        return InitialBitePain;
    }

    public void setInitialBitePain(float InitialBitePain)
    {
        this.InitialBitePain = InitialBitePain;
    }

    public float getInitialWoundPain()
    {
        return InitialWoundPain;
    }

    public void setInitialWoundPain(float InitialWoundPain)
    {
        this.InitialWoundPain = InitialWoundPain;
    }

    public float getContinualPainIncrease()
    {
        return ContinualPainIncrease;
    }

    public void setContinualPainIncrease(float ContinualPainIncrease)
    {
        this.ContinualPainIncrease = ContinualPainIncrease;
    }

    public float getPainReductionFromMeds()
    {
        return PainReductionFromMeds;
    }

    public void setPainReductionFromMeds(float PainReductionFromMeds)
    {
        this.PainReductionFromMeds = PainReductionFromMeds;
    }

    public float getStandardPainReductionWhenWell()
    {
        return StandardPainReductionWhenWell;
    }

    public void setStandardPainReductionWhenWell(float StandardPainReductionWhenWell)
    {
        this.StandardPainReductionWhenWell = StandardPainReductionWhenWell;
    }

    public int getOldNumZombiesVisible()
    {
        return OldNumZombiesVisible;
    }

    public void setOldNumZombiesVisible(int OldNumZombiesVisible)
    {
        this.OldNumZombiesVisible = OldNumZombiesVisible;
    }

    public int getCurrentNumZombiesVisible()
    {
        return CurrentNumZombiesVisible;
    }

    public void setCurrentNumZombiesVisible(int CurrentNumZombiesVisible)
    {
        this.CurrentNumZombiesVisible = CurrentNumZombiesVisible;
    }

    public float getPanicIncreaseValue()
    {
        return PanicIncreaseValue;
    }

    public void setPanicIncreaseValue(float PanicIncreaseValue)
    {
        this.PanicIncreaseValue = PanicIncreaseValue;
    }

    public float getPanicReductionValue()
    {
        return PanicReductionValue;
    }

    public void setPanicReductionValue(float PanicReductionValue)
    {
        this.PanicReductionValue = PanicReductionValue;
    }

    public float getDrunkIncreaseValue()
    {
        return DrunkIncreaseValue;
    }

    public void setDrunkIncreaseValue(float DrunkIncreaseValue)
    {
        this.DrunkIncreaseValue = DrunkIncreaseValue;
    }

    public float getDrunkReductionValue()
    {
        return DrunkReductionValue;
    }

    public void setDrunkReductionValue(float DrunkReductionValue)
    {
        this.DrunkReductionValue = DrunkReductionValue;
    }

    public boolean isIsOnFire()
    {
        return IsOnFire;
    }

    public void setIsOnFire(boolean IsOnFire)
    {
        this.IsOnFire = IsOnFire;
    }

    public boolean isBurntToDeath()
    {
        return BurntToDeath;
    }

    public void setBurntToDeath(boolean BurntToDeath)
    {
        this.BurntToDeath = BurntToDeath;
    }

    public float getWetnessIncreaseValue()
    {
        return WetnessIncreaseValue;
    }

    public void setWetnessIncreaseValue(float WetnessIncreaseValue)
    {
        this.WetnessIncreaseValue = WetnessIncreaseValue;
    }

    public float getWetnessReductionValue()
    {
        return WetnessReductionValue;
    }

    public void setWetnessReductionValue(float WetnessReductionValue)
    {
        this.WetnessReductionValue = WetnessReductionValue;
    }

    public void setWetness(float Wetness)
    {
        this.Wetness = Wetness;
    }

    public float getCatchAColdIncreaseRate()
    {
        return CatchAColdIncreaseRate;
    }

    public void setCatchAColdIncreaseRate(float CatchAColdIncreaseRate)
    {
        this.CatchAColdIncreaseRate = CatchAColdIncreaseRate;
    }

    public float getCatchAColdDecreaseRate()
    {
        return CatchAColdDecreaseRate;
    }

    public void setCatchAColdDecreaseRate(float CatchAColdDecreaseRate)
    {
        this.CatchAColdDecreaseRate = CatchAColdDecreaseRate;
    }

    public float getCatchACold()
    {
        return CatchACold;
    }

    public void setCatchACold(float CatchACold)
    {
        this.CatchACold = CatchACold;
    }

    public boolean isHasACold()
    {
        return HasACold;
    }

    public void setHasACold(boolean HasACold)
    {
        this.HasACold = HasACold;
    }

    public void setColdStrength(float ColdStrength)
    {
        this.ColdStrength = ColdStrength;
    }

    public float getColdProgressionRate()
    {
        return ColdProgressionRate;
    }

    public void setColdProgressionRate(float ColdProgressionRate)
    {
        this.ColdProgressionRate = ColdProgressionRate;
    }

    public int getTimeToSneezeOrCough()
    {
        return TimeToSneezeOrCough;
    }

    public void setTimeToSneezeOrCough(int TimeToSneezeOrCough)
    {
        this.TimeToSneezeOrCough = TimeToSneezeOrCough;
    }

    public int getMildColdSneezeTimerMin()
    {
        return MildColdSneezeTimerMin;
    }

    public void setMildColdSneezeTimerMin(int MildColdSneezeTimerMin)
    {
        this.MildColdSneezeTimerMin = MildColdSneezeTimerMin;
    }

    public int getMildColdSneezeTimerMax()
    {
        return MildColdSneezeTimerMax;
    }

    public void setMildColdSneezeTimerMax(int MildColdSneezeTimerMax)
    {
        this.MildColdSneezeTimerMax = MildColdSneezeTimerMax;
    }

    public int getColdSneezeTimerMin()
    {
        return ColdSneezeTimerMin;
    }

    public void setColdSneezeTimerMin(int ColdSneezeTimerMin)
    {
        this.ColdSneezeTimerMin = ColdSneezeTimerMin;
    }

    public int getColdSneezeTimerMax()
    {
        return ColdSneezeTimerMax;
    }

    public void setColdSneezeTimerMax(int ColdSneezeTimerMax)
    {
        this.ColdSneezeTimerMax = ColdSneezeTimerMax;
    }

    public int getNastyColdSneezeTimerMin()
    {
        return NastyColdSneezeTimerMin;
    }

    public void setNastyColdSneezeTimerMin(int NastyColdSneezeTimerMin)
    {
        this.NastyColdSneezeTimerMin = NastyColdSneezeTimerMin;
    }

    public int getNastyColdSneezeTimerMax()
    {
        return NastyColdSneezeTimerMax;
    }

    public void setNastyColdSneezeTimerMax(int NastyColdSneezeTimerMax)
    {
        this.NastyColdSneezeTimerMax = NastyColdSneezeTimerMax;
    }

    public int getSneezeCoughActive()
    {
        return SneezeCoughActive;
    }

    public void setSneezeCoughActive(int SneezeCoughActive)
    {
        this.SneezeCoughActive = SneezeCoughActive;
    }

    public int getSneezeCoughTime()
    {
        return SneezeCoughTime;
    }

    public void setSneezeCoughTime(int SneezeCoughTime)
    {
        this.SneezeCoughTime = SneezeCoughTime;
    }

    public int getSneezeCoughDelay()
    {
        return SneezeCoughDelay;
    }

    public void setSneezeCoughDelay(int SneezeCoughDelay)
    {
        this.SneezeCoughDelay = SneezeCoughDelay;
    }

    public void setUnhappynessLevel(float UnhappynessLevel)
    {
        this.UnhappynessLevel = UnhappynessLevel;
    }

    public IsoGameCharacter getParentChar()
    {
        return ParentChar;
    }

    public void setParentChar(IsoGameCharacter ParentChar)
    {
        this.ParentChar = ParentChar;
    }

    public NulledArrayList BodyParts;
    public int DamageModCount;
    public float InfectionGrowthRate;
    public float InfectionLevel;
    public boolean inf;
    public float FakeInfectionLevel;
    public boolean IsFakeInfected;
    public float OverallBodyHealth;
    public float StandardHealthAddition;
    public float ReducedHealthAddition;
    public float SeverlyReducedHealthAddition;
    public float SleepingHealthAddition;
    public float HealthFromFood;
    public float HealthReductionFromSevereBadMoodles;
    public float InfectionHealthReductionAmmount;
    public int StandardHealthFromFoodTime;
    public int HealthFromFoodTimer;
    public float BoredomLevel;
    public float BoredomDecreaseFromReading;
    public float InitialThumpPain;
    public float InitialScratchPain;
    public float InitialBitePain;
    public float InitialWoundPain;
    public float ContinualPainIncrease;
    public float PainReductionFromMeds;
    public float StandardPainReductionWhenWell;
    public int OldNumZombiesVisible;
    public int CurrentNumZombiesVisible;
    public float PanicIncreaseValue;
    public float PanicReductionValue;
    public float DrunkIncreaseValue;
    public float DrunkReductionValue;
    public boolean IsOnFire;
    public boolean BurntToDeath;
    public float WetnessIncreaseValue;
    public float WetnessReductionValue;
    public float Wetness;
    public float CatchAColdIncreaseRate;
    public float CatchAColdDecreaseRate;
    public float CatchACold;
    public boolean HasACold;
    public float ColdStrength;
    public float ColdProgressionRate;
    public int TimeToSneezeOrCough;
    public int MildColdSneezeTimerMin;
    public int MildColdSneezeTimerMax;
    public int ColdSneezeTimerMin;
    public int ColdSneezeTimerMax;
    public int NastyColdSneezeTimerMin;
    public int NastyColdSneezeTimerMax;
    public int SneezeCoughActive;
    public int SneezeCoughTime;
    public int SneezeCoughDelay;
    public float UnhappynessLevel;
    public IsoGameCharacter ParentChar;
}
