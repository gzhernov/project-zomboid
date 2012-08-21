// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 13.08.2012 13:54:45
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   GameTime.java

package zombie;

import java.io.*;
import java.util.*;
import org.lwjgl.input.Keyboard;
import zombie.characters.IsoGameCharacter;
import zombie.characters.IsoPlayer;
import zombie.characters.IsoSurvivor;
import zombie.characters.IsoZombie;
import zombie.characters.SurvivorDesc;
import zombie.characters.SurvivorGroup;
import zombie.core.Collections.NulledArrayList;
import zombie.core.Core;
import zombie.core.Rand;
import zombie.inventory.ItemContainer;
import zombie.iso.IsoCell;
import zombie.iso.IsoGridSquare;
import zombie.iso.IsoMovingObject;
import zombie.iso.IsoWorld;
import zombie.ui.SpeedControls;
import zombie.ui.UIManager;

// Referenced classes of package zombie:
//            FrameLoader

public class GameTime
{

    public GameTime()
    {
        millingtune = "longambient.ogg";
        zombieTune = "tune4.ogg";
        Ambient = 0.9F;
        AmbientMax = 0.6F;
        AmbientMin = 0.0F;
        Day = 22;
        StartDay = 22;
        MaxZombieCountStart = 750F;
        MinZombieCountStart = 750F;
        MaxZombieCount = 750F;
        MinZombieCount = 750F;
        Month = 1;
        StartMonth = 1;
        NightTint = 0.0F;
        TimeOfDay = 13F;
        StartTimeOfDay = 13F;
        ViewDist = 10F;
        ViewDistMax = 24F;
        ViewDistMin = 19F;
        Year = 2012;
        StartYear = 2012;
        NightsSurvived = 0;
        HoursSurvived = 0.0D;
        MinutesPerDay = 30F;
        SleepMultiplier = 1.0F;
        TargetZombies = (int)MinZombieCountStart;
        LastCookMinute = 0;
        RainingToday = true;
        Multiplier = 1.0F;
        FPSMultiplier = 1.0F;
        timeSinceLastMusicChange = 0;
        timeToMusicChange = 7200;
    }

    public static GameTime getInstance()
    {
        return instance;
    }

    public static void setInstance(GameTime aInstance)
    {
        instance = aInstance;
    }

    public float getRealworldSecondsSinceLastUpdate()
    {
        return 0.01666667F * FPSMultiplier;
    }

    public float getMultipliedSecondsSinceLastUpdate()
    {
        return 0.01666667F * getUnmoddedMultiplier();
    }

    public float getGameWorldSecondsSinceLastUpdate()
    {
        float dif = 1440F / getMinutesPerDay();
        return 0.01666667F * getMultiplier() * dif;
    }

    public int daysInMonth(int year, int month)
    {
        setCalender(new GregorianCalendar(getYear(), getMonth(), getDay(), (int)getTimeOfDay(), (int)((getTimeOfDay() - (float)(int)getTimeOfDay()) * 60F)));
        int daysInMonths[] = {
            31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 
            30, 31
        };
        daysInMonths[1] += getCalender().isLeapYear(year) ? 1 : 0;
        return daysInMonths[getMonth()];
    }

    public String getDeathString()
    {
        if(Core.bDebug)
            return "This was a debug run. No survival time for you!";
        String total = "You survived for ";
        int hours = (int)getHoursSurvived();
        Integer hoursLeft = Integer.valueOf(hours);
        Integer days = Integer.valueOf(hours / 24);
        hoursLeft = Integer.valueOf(hours % 24);
        Integer months = Integer.valueOf(days.intValue() / 30);
        days = Integer.valueOf(days.intValue() % 30);
        Integer years = Integer.valueOf(months.intValue() / 12);
        months = Integer.valueOf(months.intValue() % 12);
        String dayString = "day";
        String yearString = "year";
        String hourString = "hour";
        String monthString = "month";
        if(years.intValue() != 0)
        {
            if(years.intValue() > 1)
                yearString = "years";
            total = (new StringBuilder()).append(total).append(years).append(" ").append(yearString).append(", ").toString();
        }
        if(months.intValue() != 0)
        {
            if(months.intValue() > 1)
                monthString = "months";
            total = (new StringBuilder()).append(total).append(months).append(" ").append(monthString).append(", ").toString();
        }
        if(days.intValue() != 0)
        {
            if(days.intValue() > 1)
                dayString = "days";
            total = (new StringBuilder()).append(total).append(days).append(" ").append(dayString).append(", ").toString();
        }
        if(hoursLeft.intValue() != 0)
        {
            if(hoursLeft.intValue() > 1)
                hourString = "hours";
            total = (new StringBuilder()).append(total).append(hoursLeft).append(" ").append(hourString).append(".").toString();
        }
        return total;
    }

    public void init()
    {
        if(!Core.GameMode.equals("Sandbox"));
        setDay(getStartDay());
        setTimeOfDay(getStartTimeOfDay());
        setMonth(getStartMonth());
        setYear(getStartYear());
        setMaxZombieCount(getMaxZombieCountStart());
        setMinZombieCount(getMinZombieCountStart());
        setTargetZombies((int)getMinZombieCountStart());
        if(IsoPlayer.DemoMode)
        {
            setMaxZombieCount(300F);
            setMinZombieCount(300F);
        }
    }

    public float Lerp(float start, float end, float delta)
    {
        if(delta < 0.0F)
            delta = 0.0F;
        if(delta >= 1.0F)
            delta = 1.0F;
        float amount = end - start;
        float result = amount * delta;
        return start + result;
    }

    public void RemoveZombies(int j)
    {
    }

    public void RemoveZombiesIndiscriminate(int i)
    {
        if(i == 0)
            return;
        for(int n = 0; n < IsoWorld.instance.CurrentCell.getZombieList().size(); n++)
        {
            IsoZombie zombie = (IsoZombie)IsoWorld.instance.CurrentCell.getZombieList().get(0);
            IsoWorld.instance.CurrentCell.getZombieList().remove(n);
            IsoWorld.instance.CurrentCell.getRemoveList().add(zombie);
            zombie.getCurrentSquare().getMovingObjects().remove(zombie);
            n--;
            if(--i == 0 || IsoWorld.instance.CurrentCell.getZombieList().isEmpty())
                return;
        }

    }

    public float TimeLerp(float startVal, float endVal, float startTime, float endTime)
    {
        float TimeOfDay = getInstance().getTimeOfDay();
        if(endTime < startTime)
            endTime += 24F;
        boolean bReverse = false;
        if(TimeOfDay > endTime && TimeOfDay > startTime || TimeOfDay < endTime && TimeOfDay < startTime)
        {
            startTime += 24F;
            bReverse = true;
            float temp = startTime;
            startTime = endTime;
            endTime = temp;
            if(TimeOfDay < startTime)
                TimeOfDay += 24F;
        }
        float dist = endTime - startTime;
        float current = TimeOfDay - startTime;
        float delta = 0.0F;
        if(current > dist)
            delta = 1.0F;
        if(current < dist && current > 0.0F)
            delta = current / dist;
        if(bReverse)
            delta = 1.0F - delta;
        float signval = 0.0F;
        delta = (delta - 0.5F) * 2.0F;
        if((double)delta < 0.0D)
            signval = -1F;
        else
            signval = 1.0F;
        delta = Math.abs(delta);
        delta = 1.0F - delta;
        delta = (float)Math.pow(delta, 8D);
        delta = 1.0F - delta;
        delta *= signval;
        delta = delta * 0.5F + 0.5F;
        return Lerp(startVal, endVal, delta);
    }

    public float TimeLerpCompressed(float startVal, float endVal, float startTime, float endTime)
    {
        float TimeOfDay = getInstance().getTimeOfDay();
        if(endTime < startTime)
            endTime += 24F;
        boolean bReverse = false;
        if(TimeOfDay > endTime && TimeOfDay > startTime || TimeOfDay < endTime && TimeOfDay < startTime)
        {
            startTime += 24F;
            bReverse = true;
            float temp = startTime;
            startTime = endTime;
            endTime = temp;
            if(TimeOfDay < startTime)
                TimeOfDay += 24F;
        }
        float dist = endTime - startTime;
        float current = TimeOfDay - startTime;
        float delta = 0.0F;
        if(current > dist)
            delta = 1.0F;
        if(current < dist && current > 0.0F)
            delta = current / dist;
        if(bReverse)
            delta = 1.0F - delta;
        float signval = 0.0F;
        delta = (delta - 0.5F) * 2.0F;
        if((double)delta < 0.0D)
            signval = -1F;
        else
            signval = 1.0F;
        delta = Math.abs(delta);
        delta = 1.0F - delta;
        delta = (float)Math.pow(delta, 8D);
        delta = 1.0F - delta;
        delta *= signval;
        delta = delta * 0.5F + 0.5F;
        delta = delta * delta * delta * delta;
        return Lerp(startVal, endVal, delta);
    }

    public void Update(boolean bSleeping)
    {
        if(Rand.Next(300) == 0 && timeSinceLastMusicChange > timeToMusicChange)
        {
            timeSinceLastMusicChange = 0;
            if(Rand.Next(3) != 0);
        }
        float lastTimeOfDay = getTimeOfDay();
        if(UIManager.getSpeedControls().getCurrentGameSpeed() > 0)
        {
            float time = (1.0F / getMinutesPerDay() / 60F) * getMultiplier();
            if(bSleeping)
            {
                setTimeOfDay(getTimeOfDay() + time);
                setHoursSurvived(getHoursSurvived() + (double)time);
                IsoPlayer.instance.setAsleepTime(IsoPlayer.instance.getAsleepTime() + time);
            } else
            {
                if(!FrameLoader.bServer)
                    IsoPlayer.instance.setAsleepTime(0.0F);
                setTimeOfDay(getTimeOfDay() + time);
                setHoursSurvived(getHoursSurvived() + (double)time);
            }
        }
        if(lastTimeOfDay <= 7F && getTimeOfDay() > 7F)
            setNightsSurvived(getNightsSurvived() + 1);
        if(getTimeOfDay() >= 24F)
        {
            setTimeOfDay(getTimeOfDay() - 24F);
            setDay(getDay() + 1);
            for(int n = 0; n < IsoWorld.instance.CurrentCell.getObjectList().size(); n++)
            {
                IsoMovingObject ob = (IsoMovingObject)IsoWorld.instance.CurrentCell.getObjectList().get(n);
                if(ob instanceof IsoSurvivor)
                    ((IsoSurvivor)ob).nightsSurvived++;
            }

            if(getMaxZombieCount() >= 1000F);
            if(getDay() >= daysInMonth(getYear(), getMonth()))
            {
                setDay(0);
                setMonth(getMonth() + 1);
                if(getMonth() >= 12)
                {
                    setMonth(0);
                    setYear(getYear() + 1);
                }
            }
            for(int n = 0; n < IsoWorld.instance.CurrentCell.getContainers().size(); n++)
            {
                ItemContainer con = (ItemContainer)IsoWorld.instance.CurrentCell.getContainers().get(n);
                con.age();
            }

            for(int n = 0; n < IsoWorld.instance.CurrentCell.getObjectList().size(); n++)
            {
                IsoMovingObject obj = (IsoMovingObject)IsoWorld.instance.CurrentCell.getObjectList().get(n);
                if(obj instanceof IsoGameCharacter)
                    ((IsoGameCharacter)obj).getInventory().age();
            }

        }
        setAmbient(TimeLerp(getAmbientMin(), getAmbientMax(), 2.0F, 13F));
        setViewDist(TimeLerp(getViewDistMin(), getViewDistMax(), 2.0F, 13F));
        setNightTint(TimeLerp(1.0F, 0.0F, 2.0F, 13F));
        UpdateZombieCount();
    }

    public void UpdateZombieCount()
    {
    }

    private void AddZombies(int i)
    {
        if(i == 0)
            return;
        else
            return;
    }

    public float getAmbient()
    {
        return Ambient;
    }

    public void setAmbient(float Ambient)
    {
        this.Ambient = Ambient;
    }

    public float getAmbientMax()
    {
        return AmbientMax;
    }

    public void setAmbientMax(float AmbientMax)
    {
        this.AmbientMax = AmbientMax;
    }

    public float getAmbientMin()
    {
        return AmbientMin;
    }

    public void setAmbientMin(float AmbientMin)
    {
        this.AmbientMin = AmbientMin;
    }

    public int getDay()
    {
        return Day;
    }

    public void setDay(int Day)
    {
        this.Day = Day;
    }

    public int getStartDay()
    {
        return StartDay;
    }

    public void setStartDay(int StartDay)
    {
        this.StartDay = StartDay;
    }

    public float getMaxZombieCountStart()
    {
        return 0.0F;
    }

    public void setMaxZombieCountStart(float MaxZombieCountStart)
    {
        this.MaxZombieCountStart = MaxZombieCountStart;
    }

    public float getMinZombieCountStart()
    {
        return 0.0F;
    }

    public void setMinZombieCountStart(float MinZombieCountStart)
    {
        this.MinZombieCountStart = MinZombieCountStart;
    }

    public float getMaxZombieCount()
    {
        return MaxZombieCount;
    }

    public void setMaxZombieCount(float MaxZombieCount)
    {
        this.MaxZombieCount = MaxZombieCount;
    }

    public float getMinZombieCount()
    {
        return MinZombieCount;
    }

    public void setMinZombieCount(float MinZombieCount)
    {
        this.MinZombieCount = MinZombieCount;
    }

    public int getMonth()
    {
        return Month;
    }

    public void setMonth(int Month)
    {
        this.Month = Month;
    }

    public int getStartMonth()
    {
        return StartMonth;
    }

    public void setStartMonth(int StartMonth)
    {
        this.StartMonth = StartMonth;
    }

    public float getNightTint()
    {
        return NightTint;
    }

    public void setNightTint(float NightTint)
    {
        this.NightTint = NightTint;
    }

    public float getTimeOfDay()
    {
        return TimeOfDay;
    }

    public void setTimeOfDay(float TimeOfDay)
    {
        this.TimeOfDay = TimeOfDay;
    }

    public float getStartTimeOfDay()
    {
        return StartTimeOfDay;
    }

    public void setStartTimeOfDay(float StartTimeOfDay)
    {
        this.StartTimeOfDay = StartTimeOfDay;
    }

    public float getViewDist()
    {
        return ViewDist;
    }

    public void setViewDist(float ViewDist)
    {
        this.ViewDist = ViewDist;
    }

    public float getViewDistMax()
    {
        return ViewDistMax;
    }

    public void setViewDistMax(float ViewDistMax)
    {
        this.ViewDistMax = ViewDistMax;
    }

    public float getViewDistMin()
    {
        return ViewDistMin;
    }

    public void setViewDistMin(float ViewDistMin)
    {
        this.ViewDistMin = ViewDistMin;
    }

    public int getYear()
    {
        return Year;
    }

    public void setYear(int Year)
    {
        this.Year = Year;
    }

    public int getStartYear()
    {
        return StartYear;
    }

    public void setStartYear(int StartYear)
    {
        this.StartYear = StartYear;
    }

    public int getNightsSurvived()
    {
        return NightsSurvived;
    }

    public void setNightsSurvived(int NightsSurvived)
    {
        this.NightsSurvived = NightsSurvived;
    }

    public double getHoursSurvived()
    {
        return HoursSurvived;
    }

    public void setHoursSurvived(double HoursSurvived)
    {
        this.HoursSurvived = HoursSurvived;
    }

    public GregorianCalendar getCalender()
    {
        return Calender;
    }

    public void setCalender(GregorianCalendar Calender)
    {
        this.Calender = Calender;
    }

    public float getMinutesPerDay()
    {
        return MinutesPerDay;
    }

    public void setMinutesPerDay(float MinutesPerDay)
    {
        this.MinutesPerDay = MinutesPerDay;
    }

    public float getSleepMultiplier()
    {
        return SleepMultiplier;
    }

    public void setSleepMultiplier(float SleepMultiplier)
    {
        this.SleepMultiplier = SleepMultiplier;
    }

    public float getLastTimeOfDay()
    {
        return LastTimeOfDay;
    }

    public void setLastTimeOfDay(float LastTimeOfDay)
    {
        this.LastTimeOfDay = LastTimeOfDay;
    }

    public int getTargetZombies()
    {
        return (int)getMinZombieCountStart();
    }

    public void setTargetZombies(int TargetZombies)
    {
        this.TargetZombies = TargetZombies;
    }

    public int getLastCookMinute()
    {
        return LastCookMinute;
    }

    public void setLastCookMinute(int LastCookMinute)
    {
        this.LastCookMinute = LastCookMinute;
    }

    public boolean isRainingToday()
    {
        return RainingToday;
    }

    public void setRainingToday(boolean RainingToday)
    {
        this.RainingToday = RainingToday;
    }

    public float getMultiplier()
    {
        float Multiplier = this.Multiplier * FPSMultiplier;
        Multiplier *= 0.5F;
        if(IsoPlayer.instance.Asleep)
            return 60F;
        if(Keyboard.isKeyDown(28))
        {
            return Multiplier * 3F;
        } else
        {
            Multiplier *= 1.4F;
            return Multiplier;
        }
    }

    public float getUnmoddedMultiplier()
    {
        float Multiplier = this.Multiplier * FPSMultiplier;
        if(IsoPlayer.instance.Asleep)
            return 60F;
        if(Keyboard.isKeyDown(28))
            return Multiplier * 3F;
        else
            return Multiplier;
    }

    public float getInvMultiplier()
    {
        float Multiplier = getMultiplier();
        return 1.0F / Multiplier;
    }

    public float getTrueMultiplier()
    {
        float Multiplier = this.Multiplier;
        if(Keyboard.isKeyDown(28))
            return Multiplier * 3F;
        else
            return Multiplier;
    }

    public void setMultiplier(float Multiplier)
    {
        this.Multiplier = Multiplier;
    }

    public void save(DataOutputStream output)
        throws IOException
    {
        output.writeFloat(Multiplier);
        output.writeInt(NightsSurvived);
        output.writeInt(TargetZombies);
        output.writeFloat(LastTimeOfDay);
        output.writeFloat(TimeOfDay);
        output.writeInt(Day);
        output.writeInt(Month);
        output.writeInt(Year);
        output.writeFloat(0.0F);
        output.writeFloat(0.0F);
        for(int x = 0; x < 100; x++)
        {
            for(int y = 0; y < 100; y++)
                output.writeInt(IsoWorld.instance.MetaCellGrid[x][y].zombieCount);

        }

        output.writeInt(IsoWorld.instance.Groups.size());
        for(int n = 0; n < IsoWorld.instance.Groups.size(); n++)
        {
            SurvivorGroup gr = (SurvivorGroup)IsoWorld.instance.Groups.get(n);
            output.writeInt(gr.Leader.getID());
            output.writeInt(gr.Members.size());
            for(int m = 0; m < gr.Members.size(); m++)
                output.writeInt(((SurvivorDesc)gr.Members.get(m)).getID());

        }

    }

    public void load(DataInputStream input)
        throws IOException
    {
        Multiplier = input.readFloat();
        NightsSurvived = input.readInt();
        TargetZombies = input.readInt();
        LastTimeOfDay = input.readFloat();
        TimeOfDay = input.readFloat();
        Day = input.readInt();
        Month = input.readInt();
        Year = input.readInt();
        input.readFloat();
        input.readFloat();
        for(int x = 0; x < 100; x++)
        {
            for(int y = 0; y < 100; y++)
                IsoWorld.instance.MetaCellGrid[x][y].zombieCount = input.readInt();

        }

        int nGroups = input.readInt();
        for(int n = 0; n < nGroups; n++)
        {
            int ID = input.readInt();
            SurvivorGroup gr = new SurvivorGroup((SurvivorDesc)IsoWorld.instance.SurvivorDescriptors.get(Integer.valueOf(ID)));
            int members = input.readInt();
            for(int m = 0; m < members; m++)
            {
                SurvivorDesc d = (SurvivorDesc)IsoWorld.instance.SurvivorDescriptors.get(Integer.valueOf(input.readInt()));
                if(d != null)
                    gr.AddMember(d);
            }

            IsoWorld.instance.Groups.add(gr);
        }

    }

    public String millingtune;
    public String zombieTune;
    public static GameTime instance = new GameTime();
    public float Ambient;
    public float AmbientMax;
    public float AmbientMin;
    public int Day;
    public int StartDay;
    public float MaxZombieCountStart;
    public float MinZombieCountStart;
    public float MaxZombieCount;
    public float MinZombieCount;
    public int Month;
    public int StartMonth;
    public float NightTint;
    public float TimeOfDay;
    public float StartTimeOfDay;
    public float ViewDist;
    public float ViewDistMax;
    public float ViewDistMin;
    public int Year;
    public int StartYear;
    public int NightsSurvived;
    public double HoursSurvived;
    public GregorianCalendar Calender;
    public float MinutesPerDay;
    public float SleepMultiplier;
    public float LastTimeOfDay;
    public int TargetZombies;
    public int LastCookMinute;
    public boolean RainingToday;
    private float Multiplier;
    public float FPSMultiplier;
    int timeSinceLastMusicChange;
    int timeToMusicChange;

}