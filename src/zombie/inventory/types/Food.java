// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Food.java

package zombie.inventory.types;

import java.io.*;
import java.nio.ByteBuffer;
import zombie.GameTime;
import zombie.characters.*;
import zombie.characters.skills.PerkFactory;
import zombie.core.Rand;
import zombie.inventory.*;
import zombie.iso.IsoCell;
import zombie.iso.IsoGridSquare;
import zombie.iso.objects.IsoFireManager;
import zombie.ui.ObjectTooltip;

public class Food extends InventoryItem
{

    public Food(String module, String name, String itemType, String texName)
    {
        super(module, name, itemType, texName);
        bBadCold = false;
        bGoodHot = false;
        Heat = 1.0F;
        endChange = 0.0F;
        hungChange = 0.0F;
        requireInHandOrInventory = null;
        useOnConsume = null;
        rotten = false;
        bDangerousUncooked = false;
        alcoholic = false;
        LastCookMinute = 0;
        thirstChange = 0.0F;
        cat = ItemType.Food;
    }

    public void update()
    {
        if(container != null)
        {
            float temp = container.getTemprature();
            if(Heat > temp)
            {
                Heat -= 0.001F * GameTime.instance.getMultiplier();
                if(Heat < temp)
                    Heat = temp;
            }
            if(Heat < temp)
            {
                Heat += 0.001F * GameTime.instance.getMultiplier();
                if(Heat > temp)
                    Heat = temp;
            }
            if(IsCookable && temp > 1.6F)
            {
                float time = GameTime.getInstance().getTimeOfDay();
                float CurrentCookMinute = time - (float)(int)time;
                CurrentCookMinute *= 60F;
                if((int)CurrentCookMinute != LastCookMinute)
                {
                    LastCookMinute = (int)CurrentCookMinute;
                    CookingTime += 1.0F * GameTime.instance.getMultiplier();
                    if(!isCooked() && !Burnt && CookingTime > MinutesToCook)
                    {
                        setCooked(true);
                        if(IsoPlayer.getInstance().getPerkLevel(zombie.characters.skills.PerkFactory.Perks.Cooking) >= 4)
                            uses++;
                        if(IsoPlayer.getInstance().getPerkLevel(zombie.characters.skills.PerkFactory.Perks.Cooking) == 5)
                            uses++;
                        IsoPlayer.getInstance().getXp().AddXP(zombie.characters.skills.PerkFactory.Perks.Cooking, 10);
                    }
                    if(CookingTime > MinutesToBurn)
                    {
                        Burnt = true;
                        setCooked(false);
                    }
                    if(GameTime.instance.NightsSurvived < 30 && Burnt && CookingTime >= MinutesToCook * 2.0F + MinutesToBurn / 2.0F)
                    {
                        if(container != null && !container.SourceGrid.getCell().IsZone("tutArea", container.SourceGrid.getX(), container.SourceGrid.getY()) && Burnt && IsoPlayer.getInstance().getPerkLevel(zombie.characters.skills.PerkFactory.Perks.Cooking) == 0 && Rand.Next(40) < 3)
                            IsoFireManager.StartFire(container.SourceGrid.getCell(), container.SourceGrid, true, 0x7a120);
                        IsCookable = false;
                    }
                }
            }
        }
    }

    public boolean CanStack(InventoryItem item)
    {
        return Math.abs(Heat - ((Food)item).Heat) < 0.1F && isCooked() == ((Food)item).isCooked() && Age == ((Food)item).Age && Burnt == ((Food)item).Burnt && name.equals(item.getName());
    }

    boolean CanStackNoTemp(InventoryItem item)
    {
        return isCooked() == ((Food)item).isCooked() && Age == ((Food)item).Age && Burnt == ((Food)item).Burnt && name.equals(item.getName());
    }

    public void save(DataOutputStream output)
        throws IOException
    {
        super.save(output);
        output.writeFloat(Heat);
        output.writeFloat(Age);
        output.writeInt(LastCookMinute);
        output.writeFloat(CookingTime);
        output.writeBoolean(Cooked);
        output.writeBoolean(Burnt);
    }

    public void save(ByteBuffer output)
        throws IOException
    {
        super.save(output);
        output.putFloat(Heat);
        output.putFloat(Age);
        output.putInt(LastCookMinute);
        output.putFloat(CookingTime);
        output.put(((byte)(Cooked ? 1 : 0)));
        output.put(((byte)(Burnt ? 1 : 0)));
    }

    public void load(DataInputStream input)
        throws IOException
    {
        super.load(input);
        Heat = input.readFloat();
        Age = input.readFloat();
        LastCookMinute = input.readInt();
        CookingTime = input.readFloat();
        Cooked = input.readBoolean();
        Burnt = input.readBoolean();
    }

    public void load(ByteBuffer input)
        throws IOException
    {
        super.load(input);
        Heat = input.getFloat();
        Age = input.getFloat();
        LastCookMinute = input.getInt();
        CookingTime = input.getFloat();
        Cooked = input.get() == 1;
        Burnt = input.get() == 1;
    }

    public boolean finishupdate()
    {
        return !IsCookable;
    }

    public void DoTooltip(ObjectTooltip tooltipUI)
    {
        tooltipUI.render();
        super.DoTooltip(tooltipUI);
        int mid = 100;
        int y = tooltipUI.getHeight();
        y -= 19;
        if(getHungerChange() != 0.0F)
        {
            tooltipUI.DrawText("Hunger:", 5, y, 1.0F, 1.0F, 0.8F, 1.0F);
            int hunger = (int)(getHungerChange() * 100F);
            tooltipUI.DrawValueRight(hunger, mid, y, false);
            y += 14;
        }
        if(getThirstChange() != 0.0F)
        {
            tooltipUI.DrawText("Thirst:", 5, y, 1.0F, 1.0F, 0.8F, 1.0F);
            int hunger = (int)(getThirstChange() * 100F);
            tooltipUI.DrawValueRight(hunger, mid, y, false);
            y += 14;
        }
        if(getEnduranceChange() != 0.0F)
        {
            int end = (int)(getEnduranceChange() * 100F);
            tooltipUI.DrawText("Endurance:", 5, y, 1.0F, 1.0F, 0.8F, 1.0F);
            tooltipUI.DrawValueRight(end, mid, y, true);
            y += 14;
        }
        if(getStressChange() != 0.0F)
        {
            int stress = (int)(getStressChange() * 100F);
            tooltipUI.DrawText("Stress:", 5, y, 1.0F, 1.0F, 0.8F, 1.0F);
            tooltipUI.DrawValueRight(stress, mid, y, false);
            y += 14;
        }
        if(getBoredomChange() != 0.0F)
        {
            int stress = (int)getBoredomChange();
            tooltipUI.DrawText("Boredom:", 5, y, 1.0F, 1.0F, 0.8F, 1.0F);
            tooltipUI.DrawValueRight(stress, mid, y, false);
            y += 14;
        }
        if(getUnhappyChange() != 0.0F)
        {
            int stress = (int)getUnhappyChange();
            tooltipUI.DrawText("Unhappiness:", 5, y, 1.0F, 1.0F, 0.8F, 1.0F);
            tooltipUI.DrawValueRight(stress, mid, y, false);
        }
        y += 19;
        tooltipUI.setHeight(y);
    }

    public float getEnduranceChange()
    {
        if(Burnt)
            return endChange / 3F;
        if(Age > (float)OffAge)
            return endChange / 2.0F;
        if(isCooked())
            return endChange * 2.0F;
        else
            return endChange;
    }

    public float getUnhappyChange()
    {
        float hungChange = unhappyChange;
        if(Burnt)
            return hungChange + 20F;
        if(Age > (float)OffAge)
            return hungChange + 20F;
        if(isCooked())
        {
            int lev = IsoPlayer.getInstance().getPerkLevel(zombie.characters.skills.PerkFactory.Perks.Cooking);
            if(lev >= 2)
                hungChange -= 20F;
            return hungChange - 5F;
        } else
        {
            return hungChange;
        }
    }

    public float getBoredomChange()
    {
        float hungChange = boredomChange;
        if(Burnt)
            return hungChange + 20F;
        if(Age > (float)OffAge)
            return hungChange + 20F;
        if(isCooked())
        {
            int lev = IsoPlayer.getInstance().getPerkLevel(zombie.characters.skills.PerkFactory.Perks.Cooking);
            if(lev >= 2)
                hungChange -= 40F;
            return hungChange - 20F;
        } else
        {
            return hungChange;
        }
    }

    public float getHungerChange()
    {
        float hungChange = this.hungChange;
        if(Burnt)
            return hungChange / 3F;
        if(Age > (float)OffAge)
            return hungChange / 2.0F;
        if(isCooked())
        {
            int lev = IsoPlayer.getInstance().getPerkLevel(zombie.characters.skills.PerkFactory.Perks.Cooking);
            if(lev == 1)
                hungChange *= 1.2F;
            if(lev >= 2)
                hungChange *= 1.4F;
            return hungChange * 2.0F;
        } else
        {
            return hungChange;
        }
    }

    public float getStressChange()
    {
        if(Burnt)
            return stressChange / 4F;
        if(Age > (float)OffAge)
            return stressChange / 2.0F;
        if(isCooked())
            return stressChange * 2.0F;
        else
            return stressChange;
    }

    public float getScore(SurvivorDesc desc)
    {
        float score = 0.0F;
        score -= getHungerChange() * 100F;
        return score;
    }

    public boolean isBadCold()
    {
        return bBadCold;
    }

    public void setBadCold(boolean bBadCold)
    {
        this.bBadCold = bBadCold;
    }

    public boolean isGoodHot()
    {
        return bGoodHot;
    }

    public void setGoodHot(boolean bGoodHot)
    {
        this.bGoodHot = bGoodHot;
    }

    public float getHeat()
    {
        return Heat;
    }

    public void setHeat(float Heat)
    {
        this.Heat = Heat;
    }

    public float getEndChange()
    {
        return endChange;
    }

    public void setEndChange(float endChange)
    {
        this.endChange = endChange;
    }

    public float getBaseHungChange()
    {
        return hungChange;
    }

    public void setHungChange(float hungChange)
    {
        this.hungChange = hungChange;
    }

    public String getRequireInHandOrInventory()
    {
        return requireInHandOrInventory;
    }

    public void setRequireInHandOrInventory(String requireInHandOrInventory)
    {
        this.requireInHandOrInventory = requireInHandOrInventory;
    }

    public String getUseOnConsume()
    {
        return useOnConsume;
    }

    public void setUseOnConsume(String useOnConsume)
    {
        this.useOnConsume = useOnConsume;
    }

    public boolean isRotten()
    {
        return rotten;
    }

    public void setRotten(boolean rotten)
    {
        this.rotten = rotten;
    }

    public boolean isbDangerousUncooked()
    {
        return bDangerousUncooked;
    }

    public void setbDangerousUncooked(boolean bDangerousUncooked)
    {
        this.bDangerousUncooked = bDangerousUncooked;
    }

    public boolean isAlcoholic()
    {
        return alcoholic;
    }

    public void setAlcoholic(boolean alcoholic)
    {
        this.alcoholic = alcoholic;
    }

    public int getLastCookMinute()
    {
        return LastCookMinute;
    }

    public void setLastCookMinute(int LastCookMinute)
    {
        this.LastCookMinute = LastCookMinute;
    }

    public float getThirstChange()
    {
        return thirstChange;
    }

    public void setThirstChange(float thirstChange)
    {
        this.thirstChange = thirstChange;
    }

    protected boolean bBadCold;
    protected boolean bGoodHot;
    protected float Heat;
    protected float endChange;
    protected float hungChange;
    protected String requireInHandOrInventory;
    protected String useOnConsume;
    protected boolean rotten;
    protected boolean bDangerousUncooked;
    protected boolean alcoholic;
    protected int LastCookMinute;
    public float thirstChange;
}
