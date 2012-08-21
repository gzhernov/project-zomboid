// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   IsoFire.java

package zombie.iso.objects;

import zombie.core.Collections.NulledArrayList;
import zombie.core.Rand;
import zombie.core.properties.PropertyContainer;
import zombie.iso.*;
import zombie.iso.SpriteDetails.IsoFlagType;
import zombie.ui.TutorialManager;

// Referenced classes of package zombie.iso.objects:
//            IsoFireManager

public class IsoFire extends IsoObject
{

    public IsoFire(IsoCell cell)
    {
        super(cell);
        Age = 0;
        Energy = 0;
        MaxLife = 3000;
        MinLife = 800;
    }

    public String getObjectName()
    {
        return "Fire";
    }

    public IsoFire(IsoCell cell, IsoGridSquare gridSquare, boolean CanBurnAnywhere, int StartingEnergy)
    {
        Age = 0;
        Energy = 0;
        MaxLife = 3000;
        MinLife = 800;
        if(gridSquare == null)
            return;
        if(!CanBurnAnywhere && gridSquare.getProperties().Is(IsoFlagType.burntOut))
            return;
        if(gridSquare.getProperties().Is(IsoFlagType.burning))
            return;
        if(!CanBurnAnywhere && !Fire_IsSquareFlamable(gridSquare))
            return;
        square = gridSquare;
        DirtySlice();
        int NumFlameParticles = 2 + Rand.Next(2);
        for(int i = 0; i < NumFlameParticles; i++)
            AttachAnim("Fire", "00", 4, IsoFireManager.FireAnimDelay, -16 + (-16 + Rand.Next(32)), -85 + (-16 + Rand.Next(32)), true, 0, false, 0.7F, IsoFireManager.FireTintMod);

        Life = MinLife + Rand.Next(MaxLife - MinLife);
        SpreadDelay = SpreadTimer = Rand.Next(Life - Life / 2);
        LifeStage = 0;
        LifeStageTimer = LifeStageDuration = Life / 4;
        if(TutorialManager.instance.Active)
        {
            LifeStageTimer *= 2;
            LifeStageDuration *= 2;
            Life *= 2;
            SpreadDelay *= 3;
            SpreadTimer *= 3;
        }
        if(TutorialManager.instance.Active)
            SpreadDelay = SpreadTimer = SpreadTimer / 4;
        gridSquare.getProperties().Set(IsoFlagType.burning, "Burning");
        Energy = StartingEnergy;
        IsoFireManager.Add(this);
    }

    public boolean Fire_IsSquareFlamable(IsoGridSquare gridSquare)
    {
        return !gridSquare.getProperties().Is(IsoFlagType.unflamable);
    }

    public boolean HasTooltip()
    {
        return false;
    }

    public void Spread()
    {
        if(getCell() == null)
            return;
        if(square == null)
            return;
        IsoGridSquare NewSquare = null;
        int NumSpreads = Rand.Next(3) + 1;
        if(TutorialManager.instance.Active)
            NumSpreads += 15;
        for(int i = 0; i < NumSpreads; i++)
        {
            int SpreadDirection = Rand.Next(13);
            if(TutorialManager.instance.Active && Rand.Next(100) < 60)
                SpreadDirection = 4;
            switch(SpreadDirection)
            {
            case 0: // '\0'
                NewSquare = getCell().getGridSquare(square.getX(), square.getY() - 1, square.getZ());
                break;

            case 1: // '\001'
                NewSquare = getCell().getGridSquare(square.getX() + 1, square.getY() - 1, square.getZ());
                break;

            case 2: // '\002'
                NewSquare = getCell().getGridSquare(square.getX() + 1, square.getY(), square.getZ());
                break;

            case 3: // '\003'
                NewSquare = getCell().getGridSquare(square.getX() + 1, square.getY() + 1, square.getZ());
                break;

            case 4: // '\004'
                NewSquare = getCell().getGridSquare(square.getX(), square.getY() + 1, square.getZ());
                break;

            case 5: // '\005'
                NewSquare = getCell().getGridSquare(square.getX() - 1, square.getY() + 1, square.getZ());
                break;

            case 6: // '\006'
                NewSquare = getCell().getGridSquare(square.getX() - 1, square.getY(), square.getZ());
                break;

            case 7: // '\007'
                NewSquare = getCell().getGridSquare(square.getX() - 1, square.getY() - 1, square.getZ());
                break;

            case 8: // '\b'
            case 9: // '\t'
            case 10: // '\n'
            case 11: // '\013'
            case 12: // '\f'
                NewSquare = getCell().getGridSquare(square.getX(), square.getY(), square.getZ() + 1);
                break;
            }
            if(NewSquare == null)
                continue;
            int NewSquareEnergyRequirement = getSquaresEnergyRequirement(NewSquare);
            if(Energy >= NewSquareEnergyRequirement)
            {
                Energy -= NewSquareEnergyRequirement;
                NewSquare.getObjects().add(new IsoFire(getCell(), NewSquare, false, NewSquareEnergyRequirement));
            }
        }

    }

    public boolean TestCollide(IsoMovingObject obj, IsoGridSquare PassedObjectSquare)
    {
        return square == PassedObjectSquare;
    }

    public zombie.iso.IsoObject.VisionResult TestVision(IsoGridSquare from, IsoGridSquare to)
    {
        return zombie.iso.IsoObject.VisionResult.NoEffect;
    }

    public void update()
    {
        Age++;
        if(LifeStageTimer > 0)
        {
            LifeStageTimer--;
            if(LifeStageTimer == 0)
                switch(LifeStage)
                {
                case 0: // '\0'
                    LifeStage = 1;
                    LifeStageTimer = LifeStageDuration;
                    AttachAnim("Fire", "01", 4, IsoFireManager.FireAnimDelay, -16, -72, true, 0, false, 0.7F, IsoFireManager.FireTintMod);
                    break;

                case 1: // '\001'
                    LifeStage = 2;
                    LifeStageTimer = LifeStageDuration;
                    AttachAnim("Smoke", "00", 4, IsoFireManager.SmokeAnimDelay, -9, 12, true, 0, false, 0.7F, IsoFireManager.SmokeTintMod);
                    AttachAnim("Fire", "02", 4, IsoFireManager.FireAnimDelay, -9, -52, true, 0, false, 0.7F, IsoFireManager.FireTintMod);
                    square.Burn();
                    break;

                case 2: // '\002'
                    LifeStage = 3;
                    LifeStageTimer = LifeStageDuration / 3;
                    RemoveAttachedAnims();
                    AttachAnim("Smoke", "01", 4, IsoFireManager.SmokeAnimDelay, 0, 12, true, 0, false, 0.7F, IsoFireManager.SmokeTintMod);
                    AttachAnim("Fire", "01", 4, IsoFireManager.FireAnimDelay, -16, -72, true, 0, false, 0.7F, IsoFireManager.FireTintMod);
                    break;

                case 3: // '\003'
                    LifeStage = 4;
                    LifeStageTimer = LifeStageDuration / 3;
                    RemoveAttachedAnims();
                    AttachAnim("Smoke", "02", 4, IsoFireManager.SmokeAnimDelay, 0, 12, true, 0, false, 0.7F, IsoFireManager.SmokeTintMod);
                    AttachAnim("Fire", "00", 4, IsoFireManager.FireAnimDelay, -16, -85, true, 0, false, 0.7F, IsoFireManager.FireTintMod);
                    break;

                case 4: // '\004'
                    LifeStage = 5;
                    LifeStageTimer = LifeStageDuration / 3;
                    RemoveAttachedAnims();
                    AttachAnim("Smoke", "00", 4, IsoFireManager.SmokeAnimDelay, -9, 12, true, 0, false, 0.7F, IsoFireManager.SmokeTintMod);
                    break;
                }
        }
        if(Life > 0)
        {
            Life--;
            if(SpreadTimer > 0)
            {
                SpreadTimer--;
                if(SpreadTimer == 0)
                {
                    if(LifeStage != 5)
                        Spread();
                    SpreadTimer = SpreadDelay;
                }
            }
        } else
        {
            square.getProperties().UnSet(IsoFlagType.burning);
            square.getProperties().Set(IsoFlagType.burntOut, "Burnt Out");
            RemoveAttachedAnims();
            IsoFireManager.Remove(this);
        }
    }

    int getSquaresEnergyRequirement(IsoGridSquare TestSquare)
    {
        int EnergyRequirementVal = 30;
        if(TestSquare.getProperties().Is(IsoFlagType.vegitation))
            EnergyRequirementVal = 15;
        if(!TestSquare.getProperties().Is(IsoFlagType.exterior))
            EnergyRequirementVal = 40;
        if(TutorialManager.instance.Active)
            return EnergyRequirementVal / 4;
        else
            return EnergyRequirementVal;
    }

    public int Age;
    public int Energy;
    public int Life;
    public int LifeStage;
    public int LifeStageDuration;
    public int LifeStageTimer;
    public int MaxLife;
    public int MinLife;
    public int SpreadDelay;
    public int SpreadTimer;
}
