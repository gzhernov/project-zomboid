// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   RainManager.java

package zombie.iso.objects;

import java.util.ArrayList;
import java.util.Stack;
import zombie.GameTime;
import zombie.SoundManager;
import zombie.characters.IsoPlayer;
import zombie.core.Collections.NulledArrayList;
import zombie.core.Rand;
import zombie.core.properties.PropertyContainer;
import zombie.core.textures.ColorInfo;
import zombie.iso.IsoCell;
import zombie.iso.IsoGridSquare;
import zombie.iso.SpriteDetails.IsoFlagType;
import zombie.openal.Audio;

// Referenced classes of package zombie.iso.objects:
//            IsoRaindrop, IsoRainSplash

public class RainManager
{

    public RainManager()
    {
    }

    public static void reset()
    {
        RainSplashStack.clear();
        RaindropStack.clear();
        RaindropReuseStack.clear();
        RainSplashReuseStack.clear();
        NumActiveRainSplashes = 0;
        NumActiveRaindrops = 0;
        PlayerLocation = null;
        PlayerOldLocation = null;
        PlayerCell = null;
        RainAmbient = null;
        ThunderAmbient = null;
    }

    public static void AddRaindrop(IsoRaindrop NewRaindrop)
    {
        if(NumActiveRaindrops < MaxRaindropObjects)
        {
            RaindropStack.add(NewRaindrop);
            NumActiveRaindrops++;
        } else
        {
            IsoRaindrop OldestRaindropObject = null;
            int OldestAge = -1;
            for(int i = 0; i < RaindropStack.size(); i++)
                if(((IsoRaindrop)RaindropStack.get(i)).Life > OldestAge)
                {
                    OldestAge = ((IsoRaindrop)RaindropStack.get(i)).Life;
                    OldestRaindropObject = (IsoRaindrop)RaindropStack.get(i);
                }

            if(OldestRaindropObject != null)
            {
                if(OldestRaindropObject.square != null)
                {
                    OldestRaindropObject.square.getProperties().UnSet(IsoFlagType.HasRaindrop);
                    OldestRaindropObject.square.getObjects().remove(OldestRaindropObject);
                }
                RemoveRaindrop(OldestRaindropObject);
                RaindropReuseStack.push(OldestRaindropObject);
                RaindropStack.add(NewRaindrop);
                NumActiveRaindrops++;
            }
        }
    }

    public static void AddRainSplash(IsoRainSplash NewRainSplash)
    {
        if(NumActiveRainSplashes < MaxRainSplashObjects)
        {
            RainSplashStack.add(NewRainSplash);
            NumActiveRainSplashes++;
        } else
        {
            IsoRainSplash OldestRainSplashObject = null;
            int OldestAge = -1;
            for(int i = 0; i < RainSplashStack.size(); i++)
                if(((IsoRainSplash)RainSplashStack.get(i)).Age > OldestAge)
                {
                    OldestAge = ((IsoRainSplash)RainSplashStack.get(i)).Age;
                    OldestRainSplashObject = (IsoRainSplash)RainSplashStack.get(i);
                }

            if(OldestRainSplashObject.square != null)
            {
                OldestRainSplashObject.square.getProperties().UnSet(IsoFlagType.HasRainSplashes);
                OldestRainSplashObject.square.getObjects().remove(OldestRainSplashObject);
            }
            RemoveRainSplash(OldestRainSplashObject);
            RainSplashReuseStack.push(OldestRainSplashObject);
            RainSplashStack.add(NewRainSplash);
            NumActiveRainSplashes++;
        }
    }

    public static void AddSplashes()
    {
        if(!PlayerMoved)
            return;
        if(AddNewSplashesTimer > 0)
        {
            AddNewSplashesTimer--;
        } else
        {
            AddNewSplashesTimer = AddNewSplashesDelay;
            PlayerMoved = false;
            IsoGridSquare NewSquare = null;
            for(int x = -RainRadius; x < RainRadius; x++)
            {
                for(int y = -RainRadius; y < RainRadius; y++)
                {
                    NewSquare = PlayerCell.getGridSquare(PlayerLocation.getX() + x, PlayerLocation.getY() + y, 0);
                    if(NewSquare != null && !NewSquare.getProperties().Is(IsoFlagType.vegitation) && NewSquare.getProperties().Is(IsoFlagType.exterior) && NewSquare.isCanSee())
                        StartRainSplash(PlayerCell, NewSquare, true);
                }

            }

        }
    }

    public static void RemoveRaindrop(IsoRaindrop DyingRaindrop)
    {
        RaindropStack.remove(DyingRaindrop);
        NumActiveRaindrops--;
    }

    public static void RemoveRainSplash(IsoRainSplash DyingRainSplash)
    {
        RainSplashStack.remove(DyingRainSplash);
        NumActiveRainSplashes--;
    }

    public static void SetPlayerLocation(IsoCell cell, IsoGridSquare PlayerCurrentSquare)
    {
        PlayerOldLocation = PlayerLocation;
        PlayerLocation = PlayerCurrentSquare;
        PlayerCell = cell;
        if(PlayerOldLocation != PlayerLocation)
            PlayerMoved = true;
    }

    public static void SetRaining(boolean ShouldRain)
    {
        boolean WasRaining = IsRaining;
        IsRaining = ShouldRain;
        if(IsRaining)
            if(RainAmbient == null)
                RainAmbient = SoundManager.instance.PlaySound("rain", true, 0.0F);
            else
            if(IsoPlayer.getInstance().getCurrentSquare().getRoom() == null)
                SoundManager.instance.BlendVolume(RainAmbient, 0.16F);
            else
                SoundManager.instance.BlendVolume(RainAmbient, 0.01F);
        if(!IsRaining && WasRaining)
        {
            for(int i = 0; i < RainSplashStack.size(); i++)
            {
                ((IsoRainSplash)RainSplashStack.get(i)).RemoveAttachedAnims();
                RainSplashStack.remove(i);
                NumActiveRainSplashes--;
            }

            NumActiveRainSplashes = 0;
        }
    }

    public static void StartRaindrop(IsoCell cell, IsoGridSquare gridSquare, boolean CanSee)
    {
        if(!gridSquare.getProperties().Is(IsoFlagType.HasRaindrop))
        {
            IsoRaindrop NewRaindrop = null;
            if(!RaindropReuseStack.isEmpty())
            {
                if(CanSee)
                {
                    NewRaindrop = (IsoRaindrop)RaindropReuseStack.pop();
                    NewRaindrop.Reset(gridSquare, CanSee);
                    gridSquare.getObjects().add(NewRaindrop);
                }
            } else
            if(CanSee)
            {
                NewRaindrop = new IsoRaindrop(cell, gridSquare, CanSee);
                gridSquare.getObjects().add(NewRaindrop);
            }
        }
    }

    public static void StartRainSplash(IsoCell cell, IsoGridSquare gridSquare, boolean CanSee)
    {
        if(gridSquare.getProperties().Is(IsoFlagType.HasRainSplashes))
            return;
        IsoRainSplash NewRainSplash = null;
        if(!RainSplashReuseStack.isEmpty())
        {
            NewRainSplash = (IsoRainSplash)RainSplashReuseStack.pop();
            NewRainSplash.Reset(gridSquare);
        } else
        {
            NewRainSplash = new IsoRainSplash(cell, gridSquare);
        }
        gridSquare.getObjects().add(NewRainSplash);
        StartRaindrop(cell, gridSquare, CanSee);
    }

    public static void Update()
    {
        if(IsoPlayer.getInstance() == null)
            return;
        if(IsoPlayer.getInstance().getCurrentSquare() == null)
            return;
        if(!IsRaining)
            return;
        if(ThunderAmbient == null || !ThunderAmbient.isPlaying())
            if(Rand.Next(1000) != 0);
        if(IsoPlayer.getInstance().getCurrentSquare().getRoom() == null)
        {
            SoundManager.instance.BlendVolume(RainAmbient, 0.16F * RainIntensity);
            if(ThunderAmbient == null);
        } else
        {
            SoundManager.instance.BlendVolume(RainAmbient, 0.1F * RainIntensity);
            if(ThunderAmbient == null);
        }
        AddSplashes();
        if(RainIntensity < RainDesiredIntensity)
        {
            RainIntensity += 0.0005F;
            if(RainIntensity > RainDesiredIntensity)
                RainIntensity = RainDesiredIntensity;
        }
        if(RainIntensity > RainDesiredIntensity)
        {
            RainIntensity -= 0.0005F;
            if(RainIntensity < RainDesiredIntensity)
                RainIntensity = RainDesiredIntensity;
        }
        if(RainIntensity == RainDesiredIntensity && RainChangeTimer > 0.0F)
        {
            RainChangeTimer -= RainChangeRate;
            if(RainChangeTimer <= 0.0F)
            {
                RainChangeTimer = 1.0F;
                RainChangeRate = RainChangeRateMin + ((float)Rand.Next(10000) / 10000F) * (RainChangeRateMax - RainChangeRateMin);
                if(GameTime.getInstance().isRainingToday())
                {
                    RainDesiredIntensity = (float)Rand.Next(10000) / 10000F;
                    if(RainDesiredIntensity < 0.3F)
                        RainDesiredIntensity = 0.0F;
                } else
                if(Rand.Next(2) == 0)
                    RainDesiredIntensity = 0.0F;
            }
        }
        for(int i = 0; i < RainSplashStack.size(); i++)
        {
            AdjustedRainSplashTintMod.r = RainSplashTintMod.r;
            AdjustedRainSplashTintMod.g = RainSplashTintMod.g;
            AdjustedRainSplashTintMod.b = RainSplashTintMod.b;
            AdjustedRainSplashTintMod.a = RainSplashTintMod.a;
            ((IsoRainSplash)RainSplashStack.get(i)).ChangeTintMod(AdjustedRainSplashTintMod);
            ((IsoRainSplash)RainSplashStack.get(i)).update();
        }

        for(int i = 0; i < RaindropStack.size(); i++)
        {
            AdjustedRainSplashTintMod.r = RaindropTintMod.r;
            AdjustedRainSplashTintMod.g = RaindropTintMod.g;
            AdjustedRainSplashTintMod.b = RaindropTintMod.b;
            AdjustedRainSplashTintMod.a = RaindropTintMod.a;
            ((IsoRaindrop)RaindropStack.get(i)).ChangeTintMod(AdjustedRainSplashTintMod);
            ((IsoRaindrop)RaindropStack.get(i)).update();
        }

    }

    public static boolean IsRaining = false;
    public static int NumActiveRainSplashes = 0;
    public static int NumActiveRaindrops = 0;
    public static int MaxRainSplashObjects = 1160;
    public static int MaxRaindropObjects = 1160;
    public static int RainSplashAnimDelay = 3;
    public static int AddNewSplashesDelay;
    public static int AddNewSplashesTimer;
    public static float RaindropGravity = 0.065F;
    public static float GravModMin = 0.56F;
    public static float GravModMax = 1.0F;
    public static float RaindropStartDistance = 850F;
    public static IsoGridSquare PlayerLocation = null;
    public static IsoGridSquare PlayerOldLocation = null;
    public static boolean PlayerMoved = true;
    public static IsoCell PlayerCell = null;
    public static int RainRadius = 17;
    public static Audio RainAmbient;
    public static Audio ThunderAmbient = null;
    public static ColorInfo RainSplashTintMod = new ColorInfo(0.8F, 0.9F, 1.0F, 0.8F);
    public static ColorInfo RaindropTintMod = new ColorInfo(0.8F, 0.9F, 1.0F, 0.6F);
    public static ColorInfo DarkRaindropTintMod = new ColorInfo(0.8F, 0.9F, 1.0F, 0.3F);
    public static ArrayList RainSplashStack = new ArrayList(1600);
    public static ArrayList RaindropStack = new ArrayList(1600);
    public static Stack RainSplashReuseStack = new Stack();
    public static Stack RaindropReuseStack = new Stack();
    private static float RainChangeTimer = 1.0F;
    private static float RainChangeRate = 0.01F;
    private static float RainChangeRateMin = 0.06F;
    private static float RainChangeRateMax = 0.1F;
    public static float RainIntensity = 1.0F;
    private static float RainDesiredIntensity = 1.0F;
    static Audio OutsideAmbient = null;
    static Audio OutsideNightAmbient = null;
    static ColorInfo AdjustedRainSplashTintMod = new ColorInfo();

    static 
    {
        AddNewSplashesDelay = 30;
        AddNewSplashesTimer = AddNewSplashesDelay;
    }
}
