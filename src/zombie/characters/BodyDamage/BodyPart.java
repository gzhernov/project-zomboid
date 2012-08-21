// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   BodyPart.java

package zombie.characters.BodyDamage;

import zombie.characters.IsoGameCharacter;
import zombie.core.Rand;

// Referenced classes of package zombie.characters.BodyDamage:
//            BodyPartType

public class BodyPart
{

    public BodyPart(BodyPartType ChosenType, IsoGameCharacter PC)
    {
        BiteDamage = 3.5F;
        BleedDamage = 2.0F;
        DamageScaler = 0.005714286F;
        ScratchDamage = 1.5F;
        WoundDamage = 6F;
        Type = ChosenType;
        ParentChar = PC;
        RestoreToFullHealth();
    }

    public void AddDamage(float Val)
    {
        Health -= Val;
        if(Health < 0.0F)
            Health = 0.0F;
    }

    public void DamageUpdate()
    {
        if(IsWounded)
            Health -= WoundDamage * DamageScaler;
        if(IsScratched)
            Health -= ScratchDamage * DamageScaler;
        if(IsBitten)
            Health -= BiteDamage * DamageScaler;
        if(IsBleeding)
            Health -= BleedDamage * DamageScaler;
        if(Health < 0.0F)
            Health = 0.0F;
    }

    public float getHealth()
    {
        return Health;
    }

    public void SetHealth(float NewHealth)
    {
        Health = NewHealth;
    }

    public void AddHealth(float Val)
    {
        Health += Val;
        if(Health > 100F)
            Health = 100F;
    }

    public void ReduceHealth(float Val)
    {
        Health -= Val;
        if(Health < 0.0F)
            Health = 0.0F;
    }

    public boolean HasInjury()
    {
        return IsBitten | IsScratched | IsWounded | IsBleeding;
    }

    public boolean IsBandaged()
    {
        return IsBandaged;
    }

    public boolean IsBitten()
    {
        return IsBitten;
    }

    public boolean IsBleeding()
    {
        return IsBleeding;
    }

    public boolean IsBleedingStemmed()
    {
        return IsBleedingStemmed;
    }

    public boolean IsCortorised()
    {
        return IsCortorised;
    }

    public boolean IsInfected()
    {
        return IsInfected;
    }

    public void SetInfected(boolean inf)
    {
        IsInfected = inf;
    }

    public void SetFakeInfected(boolean inf)
    {
        IsFakeInfected = inf;
    }

    public boolean IsFakeInfected()
    {
        return IsFakeInfected;
    }

    public void DisableFakeInfection()
    {
        IsFakeInfected = false;
    }

    public boolean IsScratched()
    {
        return IsScratched;
    }

    public boolean IsStitched()
    {
        return IsStitched;
    }

    public boolean IsWounded()
    {
        return IsWounded;
    }

    public void RestoreToFullHealth()
    {
        Health = 100F;
        IsWounded = false;
        IsBleeding = false;
        IsBleedingStemmed = false;
        IsBandaged = false;
        IsCortorised = false;
        IsBitten = false;
        IsScratched = false;
        IsInfected = false;
        IsFakeInfected = false;
    }

    public void SetBandaged(boolean Bandaged)
    {
        if(Bandaged)
        {
            if(IsBleeding)
                IsBleeding = false;
            IsBitten = false;
            IsScratched = false;
        }
        IsBandaged = Bandaged;
    }

    public void SetBitten(boolean Bitten)
    {
        IsBitten = Bitten;
        if(Bitten)
        {
            IsBleeding = true;
            IsBleedingStemmed = false;
            IsCortorised = false;
            IsBandaged = false;
            if(ParentChar.HasTrait("ThickSkinned"))
            {
                if(Rand.Next(100) < 70)
                {
                    IsInfected = true;
                    IsFakeInfected = false;
                }
            } else
            {
                IsInfected = true;
                IsFakeInfected = false;
            }
        }
    }

    public void SetBitten(boolean Bitten, boolean Infected)
    {
        IsBitten = Bitten;
        if(Bitten)
        {
            IsBleeding = true;
            IsBleedingStemmed = false;
            IsCortorised = false;
            IsBandaged = false;
            if(Infected)
                IsInfected = true;
            IsFakeInfected = false;
        }
    }

    public void SetBleeding(boolean Bleeding)
    {
        IsBleeding = Bleeding;
    }

    public void SetBleedingStemmed(boolean BleedingStemmed)
    {
        if(IsBleeding)
        {
            IsBleeding = false;
            IsBleedingStemmed = true;
        }
    }

    public void SetCortorised(boolean Cortorised)
    {
        IsCortorised = Cortorised;
        if(Cortorised)
        {
            IsBleeding = false;
            IsBleedingStemmed = false;
            IsWounded = false;
            IsBandaged = false;
        }
    }

    public void SetScratched(boolean Scratched)
    {
        IsScratched = Scratched;
        if(Scratched)
        {
            IsBandaged = false;
            if(ParentChar.HasTrait("ThickSkinned"))
            {
                if(Rand.Next(100) < 12)
                    IsInfected = true;
            } else
            if(Rand.Next(100) < 25)
                IsInfected = true;
            if(!IsInfected && ParentChar.HasTrait("Hypercondriac"))
                IsFakeInfected = true;
        }
    }

    public void SetStitched(boolean Stitched)
    {
        IsStitched = Stitched;
        if(IsStitched)
        {
            IsBleeding = false;
            IsBleedingStemmed = false;
            IsWounded = false;
            IsBandaged = false;
        }
    }

    public void SetWounded(boolean Wounded)
    {
        IsWounded = Wounded;
        if(Wounded)
        {
            IsBleeding = true;
            IsBleedingStemmed = false;
            IsCortorised = false;
            IsBandaged = false;
        }
    }

    BodyPartType Type;
    private float BiteDamage;
    private float BleedDamage;
    private float DamageScaler;
    private float Health;
    private boolean IsBandaged;
    private boolean IsBitten;
    private boolean IsBleeding;
    private boolean IsBleedingStemmed;
    private boolean IsCortorised;
    private boolean IsScratched;
    private boolean IsStitched;
    private boolean IsWounded;
    private boolean IsInfected;
    private boolean IsFakeInfected;
    private IsoGameCharacter ParentChar;
    private float ScratchDamage;
    private float WoundDamage;
}
