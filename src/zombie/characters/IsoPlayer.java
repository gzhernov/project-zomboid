// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   IsoPlayer.java

package zombie.characters;

import java.io.*;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.input.Keyboard;
import zombie.*;
import zombie.Lua.LuaEventManager;
import zombie.SoundManager;
import zombie.ZomboidGlobals;
import zombie.ai.StateMachine;
import zombie.ai.states.*;
import zombie.characters.BodyDamage.BodyDamage;
import zombie.characters.BodyDamage.BodyPartType;
import zombie.characters.Moodles.MoodleType;
import zombie.characters.Moodles.Moodles;
import zombie.characters.skills.PerkFactory;
import zombie.core.Collections.NulledArrayList;
import zombie.core.*;
import zombie.core.input.Input;
import zombie.gameStates.*;
import zombie.input.Mouse;
import zombie.inventory.InventoryItem;
import zombie.inventory.ItemContainer;
import zombie.inventory.types.Drainable;
import zombie.inventory.types.HandWeapon;
import zombie.iso.*;
import zombie.iso.SpriteDetails.IsoObjectType;
import zombie.iso.Vector2;
import zombie.iso.objects.IsoDoor;
import zombie.iso.objects.IsoWheelieBin;
import zombie.iso.sprite.*;
import zombie.network.GameClient;
import zombie.scripting.ScriptManager;
import zombie.ui.*;

// Referenced classes of package zombie.characters:
//            IsoLivingCharacter, SurvivorDesc, SurvivorGroup, IsoSurvivor, 
//            IsoGameCharacter, IsoZombie

public class IsoPlayer extends IsoLivingCharacter
{

    public static byte[] createChecksum(String filename)
        throws Exception
    {
        InputStream fis = new FileInputStream(filename);
        byte buffer[] = new byte[1024];
        MessageDigest complete = MessageDigest.getInstance("MD5");
        int numRead;
        do
        {
            numRead = fis.read(buffer);
            if(numRead > 0)
                complete.update(buffer, 0, numRead);
        } while(numRead != -1);
        fis.close();
        return complete.digest();
    }

    public static String getMD5Checksum(String filename)
        throws Exception
    {
        byte b[] = createChecksum(filename);
        String result = "";
        for(int i = 0; i < b.length; i++)
            result = (new StringBuilder()).append(result).append(Integer.toString((b[i] & 0xff) + 256, 16).substring(1)).toString();

        return result;
    }

    public static boolean DoChecksumCheck(String str, String expected)
    {
        String checksum = "";
        try
        {
            checksum = getMD5Checksum(str);
            if(!checksum.equals(expected))
                return false;
        }
        catch(Exception ex)
        {
            checksum = "";
            try
            {
                checksum = getMD5Checksum((new StringBuilder()).append("D:/Dropbox/Zomboid/zombie/build/classes/").append(str).toString());
            }
            catch(Exception ex1)
            {
                return false;
            }
        }
        return checksum.equals(expected);
    }

    public static boolean DoChecksumCheck()
    {
        if(!DoChecksumCheck("zombie/GameWindow.class", "c4a62b8857f0fb6b9c103ff6ef127a9b"))
            return false;
        if(!DoChecksumCheck("zombie/GameWindow$1.class", "5d93dc446b2dc49092fe4ecb5edf5f17"))
            return false;
        if(!DoChecksumCheck("zombie/GameWindow$2.class", "a3e3d2c8cf6f0efaa1bf7f6ceb572073"))
            return false;
        if(!DoChecksumCheck("zombie/gameStates/MainScreenState.class", "206848ba7cb764293dd2c19780263854"))
            return false;
        if(!DoChecksumCheck("zombie/core/textures/LoginForm.class", "02b8abc1ed8fd75db69dc6e7e390db41"))
            return false;
        if(!DoChecksumCheck("zombie/FrameLoader$1.class", "0ebfcc9557cc28d53aa982a71616bf5b"))
            return false;
        if(!DoChecksumCheck("zombie/FrameLoader.class", "d5b1f7b2886a499d848c204f6a815776"))
            return false;
        return DoChecksumCheck("zombie/core/textures/AuthenticatingFrame.class", "54f57018c6405a0006ca6ea28d55aa17");
    }

    public static boolean isGhostMode()
    {
        return GhostMode;
    }

    public static void setGhostMode(boolean aGhostMode)
    {
        GhostMode = aGhostMode;
    }

    public static IsoPlayer getInstance()
    {
        return instance;
    }

    public static void setInstance(IsoPlayer aInstance)
    {
        instance = aInstance;
    }

    public static Vector2 getPlayerMoveDir()
    {
        return playerMoveDir;
    }

    public static void setPlayerMoveDir(Vector2 aPlayerMoveDir)
    {
        playerMoveDir = aPlayerMoveDir;
    }

    public static boolean isIsAiming()
    {
        return isAiming;
    }

    public static void setIsAiming(boolean aIsAiming)
    {
        isAiming = aIsAiming;
    }

    public static Stack getStaticTraits()
    {
        return StaticTraits;
    }

    public static void setStaticTraits(Stack aStaticTraits)
    {
        StaticTraits = aStaticTraits;
    }

    public static int getFollowDeadCount()
    {
        return FollowDeadCount;
    }

    public static void setFollowDeadCount(int aFollowDeadCount)
    {
        FollowDeadCount = aFollowDeadCount;
    }

    public IsoPlayer(IsoCell cell)
    {
        super(cell, 0.0F, 0.0F, 0.0F);
        angleCounter = 0;
        lastAngle = new Vector2();
        DialogMood = 1;
        ping = 0;
        FakeAttack = false;
        FakeAttackTarget = null;
        DragObject = null;
        AsleepTime = 0.0F;
        MainHot = new String[10];
        SecHot = new String[10];
        spottedList = new Stack();
        TicksSinceSeenZombie = 0x98967f;
        Waiting = true;
        DragCharacter = null;
        lastPos = new Stack();
        bDebounceLMB = false;
        heartDelay = 15;
        heartDelayMax = 15;
        DrunkOscilatorStepSin = 0.0F;
        DrunkOscilatorRateSin = 0.1F;
        DrunkOscilatorStepCos = 0.0F;
        DrunkOscilatorRateCos = 0.1F;
        DrunkOscilatorStepCos2 = 0.0F;
        DrunkOscilatorRateCos2 = 0.07F;
        DrunkSin = 0.0F;
        DrunkCos = 23784F;
        DrunkCos2 = 61616F;
        MinOscilatorRate = 0.01F;
        MaxOscilatorRate = 0.15F;
        DesiredSinRate = 0.0F;
        DesiredCosRate = 0.0F;
        OscilatorChangeRate = 0.05F;
        maxWeightDelta = 1.0F;
        Forname = "Bob";
        Surname = "Smith";
        TargetSpeed = 0.0F;
        CurrentSpeed = 0.0F;
        MaxSpeed = 0.09F;
        SpeedChange = 0.007F;
        GuardModeUI = 0;
        GuardChosen = null;
        GuardStand = null;
        GuardFace = null;
        isCharging = false;
        bSneaking = false;
        bRunning = false;
        bWasRunning = false;
        bRightClickMove = false;
        bChangeCharacterDebounce = false;
        runAngle = new Vector2();
        followID = 0;
        FollowCamStack = new Stack();
        bSeenThisFrame = false;
        bCouldBeSeenThisFrame = false;
        TimeSprinting = 0;
        TimeSinceRightClick = 0;
        TimeRightClicking = 1110;
        LastTimeRightClicking = 1110;
        JustMoved = false;
        bDoubleClick = false;
        AimRadius = 0.0F;
        DesiredAimRadius = 0.0F;
        EffectiveAimDistance = 0.0F;
        chargeTime = 0.0F;
        useChargeTime = 0.0F;
        useChargeDelta = 0.0F;
        timeSinceLastStab = 0;
        LastSpotted = new Stack();
        ClearSpottedTimer = -1;
        if(!Core.bDebug);
        GuardModeUISprite = new IsoSprite(getCell().SpriteManager);
        GuardModeUISprite.LoadFrameExplicit("TileFloorInt_0");
        for(int n = 0; n < StaticTraits.size(); n++)
            Traits.add(StaticTraits.get(n));

        StaticTraits.clear();
        dir = IsoDirections.W;
        descriptor = new SurvivorDesc();
        PathSpeed = 0.08F;
        descriptor.Group = new SurvivorGroup(descriptor);
        IsoWorld.instance.Groups.add(descriptor.Group);
        descriptor.Instance = this;
        instance = this;
        SpeakColour = new Color(192, 192, 255, 255);
        if(HasTrait("Strong"))
            maxWeightDelta = 1.5F;
        if(HasTrait("Weak"))
            maxWeightDelta = 0.75F;
        if(FrameLoader.bClient)
            GameClient.instance.SendPlayer(this);
        descriptor.temper = 5F;
        if(HasTrait("ShortTemper"))
            descriptor.temper = 7.5F;
        else
        if(HasTrait("Patient"))
            descriptor.temper = 2.5F;
    }

    public IsoPlayer(IsoCell cell, SurvivorDesc desc, int x, int y, int z)
    {
        super(cell, x, y, z);
        angleCounter = 0;
        lastAngle = new Vector2();
        DialogMood = 1;
        ping = 0;
        FakeAttack = false;
        FakeAttackTarget = null;
        DragObject = null;
        AsleepTime = 0.0F;
        MainHot = new String[10];
        SecHot = new String[10];
        spottedList = new Stack();
        TicksSinceSeenZombie = 0x98967f;
        Waiting = true;
        DragCharacter = null;
        lastPos = new Stack();
        bDebounceLMB = false;
        heartDelay = 15;
        heartDelayMax = 15;
        DrunkOscilatorStepSin = 0.0F;
        DrunkOscilatorRateSin = 0.1F;
        DrunkOscilatorStepCos = 0.0F;
        DrunkOscilatorRateCos = 0.1F;
        DrunkOscilatorStepCos2 = 0.0F;
        DrunkOscilatorRateCos2 = 0.07F;
        DrunkSin = 0.0F;
        DrunkCos = 23784F;
        DrunkCos2 = 61616F;
        MinOscilatorRate = 0.01F;
        MaxOscilatorRate = 0.15F;
        DesiredSinRate = 0.0F;
        DesiredCosRate = 0.0F;
        OscilatorChangeRate = 0.05F;
        maxWeightDelta = 1.0F;
        Forname = "Bob";
        Surname = "Smith";
        TargetSpeed = 0.0F;
        CurrentSpeed = 0.0F;
        MaxSpeed = 0.09F;
        SpeedChange = 0.007F;
        GuardModeUI = 0;
        GuardChosen = null;
        GuardStand = null;
        GuardFace = null;
        isCharging = false;
        bSneaking = false;
        bRunning = false;
        bWasRunning = false;
        bRightClickMove = false;
        bChangeCharacterDebounce = false;
        runAngle = new Vector2();
        followID = 0;
        FollowCamStack = new Stack();
        bSeenThisFrame = false;
        bCouldBeSeenThisFrame = false;
        TimeSprinting = 0;
        TimeSinceRightClick = 0;
        TimeRightClicking = 1110;
        LastTimeRightClicking = 1110;
        JustMoved = false;
        bDoubleClick = false;
        AimRadius = 0.0F;
        DesiredAimRadius = 0.0F;
        EffectiveAimDistance = 0.0F;
        chargeTime = 0.0F;
        useChargeTime = 0.0F;
        useChargeDelta = 0.0F;
        timeSinceLastStab = 0;
        LastSpotted = new Stack();
        ClearSpottedTimer = -1;
        GameTime.instance.setHoursSurvived(0.0D);
        GuardModeUISprite = new IsoSprite(getCell().SpriteManager);
        GuardModeUISprite.LoadFrameExplicit("TileFloorInt_0");
        for(int n = 0; n < StaticTraits.size(); n++)
            Traits.add(StaticTraits.get(n));

        StaticTraits.clear();
        dir = IsoDirections.W;
        descriptor = new SurvivorDesc();
        PathSpeed = 0.08F;
        if(GameWindow.CharacterCreationStateHandle != null && GameWindow.CharacterCreationStateHandle.UseCustomChatacterData)
        {
            InitSpriteParts(GameWindow.CharacterCreationStateHandle.desc.legs, GameWindow.CharacterCreationStateHandle.desc.torso, GameWindow.CharacterCreationStateHandle.desc.head, GameWindow.CharacterCreationStateHandle.desc.top, GameWindow.CharacterCreationStateHandle.desc.bottoms, GameWindow.CharacterCreationStateHandle.desc.shoes, GameWindow.CharacterCreationStateHandle.desc.skinpal, GameWindow.CharacterCreationStateHandle.desc.toppal, GameWindow.CharacterCreationStateHandle.desc.bottomspal, GameWindow.CharacterCreationStateHandle.desc.shoespal);
            GameWindow.CharacterCreationStateHandle.UseCustomChatacterData = false;
            descriptor = GameWindow.CharacterCreationStateHandle.desc;
        } else
        {
            InitSpriteParts(desc.legs, desc.torso, desc.head, desc.top, desc.bottoms, desc.shoes, desc.skinpal, desc.toppal, desc.bottomspal, desc.shoespal);
            Dressup(desc);
            descriptor = desc;
        }
        descriptor.Group = new SurvivorGroup(descriptor);
        IsoWorld.instance.Groups.add(descriptor.Group);
        descriptor.Instance = this;
        instance = this;
        SpeakColour = new Color(192, 192, 255, 255);
        if(HasTrait("Strong"))
            maxWeightDelta = 1.5F;
        if(HasTrait("Weak"))
            maxWeightDelta = 0.75F;
        if(FrameLoader.bClient)
            GameClient.instance.SendPlayer(this);
        descriptor.temper = 5F;
        if(HasTrait("ShortTemper"))
            descriptor.temper = 7.5F;
        else
        if(HasTrait("Patient"))
            descriptor.temper = 2.5F;
        if(!LoadGamePopupState.bLoadCharacter)
        {
            inventory.AddItem("Base.WaterBottleEmpty");
            if(Core.bDebug)
            {
                inventory.AddItem("Base.Hammer");
                inventory.AddItem("Base.Plank");
                inventory.AddItem("Base.Plank");
                inventory.AddItem("Base.Plank");
                inventory.AddItem("Base.Nails");
            }
            if(GhostMode)
                if(!Core.bDebug);
        }
        IsoCamera.SetCharacterToFollow(this);
    }

    public boolean IsSneaking()
    {
        return bSneaking;
    }

    public void load(ByteBuffer input)
        throws IOException
    {
        input.get();
        input.getInt();
        super.load(input);
        GameTime.instance.setHoursSurvived(input.getDouble());
        SurvivorDesc desc = descriptor;
        InitSpriteParts(desc.legs, desc.torso, desc.head, desc.top, desc.bottoms, desc.shoes, desc.skinpal, desc.toppal, desc.bottomspal, desc.shoespal);
        instance = this;
        SpeakColour = new Color(192, 192, 255, 255);
        IsoCamera.SetCharacterToFollow(this);
        PathSpeed = 0.08F;
    }

    public void save(ByteBuffer output)
        throws IOException
    {
        super.save(output);
        output.putDouble(GameTime.instance.getHoursSurvived());
    }

    public String getObjectName()
    {
        return "Player";
    }

    public void collideWith(IsoObject isoobject)
    {
    }

    public Vector2 getControllerAimDir(Vector2 vec)
    {
        if(GameWindow.ActiveController != -1 && GameWindow.ActivatedJoyPad)
        {
            float xVal2 = GameWindow.GameInput.getAxisValue(GameWindow.ActiveController, GameWindow.XRAxis);
            float yVal2 = GameWindow.GameInput.getAxisValue(GameWindow.ActiveController, GameWindow.YRAxis);
            if(xVal2 > -0.3F && xVal2 < 0.3F)
                xVal2 = 0.0F;
            if(yVal2 > -0.3F && yVal2 < 0.3F)
                yVal2 = 0.0F;
            if(xVal2 != 0.0F || yVal2 != 0.0F)
            {
                Vector2 newVec = new Vector2(xVal2, yVal2);
                vec.x = newVec.x;
                vec.y = newVec.y;
                vec.normalize();
                vec.rotate(-0.7853982F);
                angle.x = vec.x;
                angle.y = vec.y;
            } else
            {
                return vec;
            }
        }
        return vec;
    }

    public IsoObject getInteract()
    {
        int x = 0;
        int y = 0;
        int fx = 0;
        int fy = 0;
        int cx = 0;
        int cy = 0;
        if(dir == IsoDirections.N)
        {
            fy--;
            cy--;
        }
        if(dir == IsoDirections.NE)
        {
            fy--;
            cy--;
            x++;
            cy++;
        }
        if(dir == IsoDirections.E)
        {
            x++;
            cx++;
        }
        if(dir == IsoDirections.SE)
        {
            x++;
            cx++;
            y++;
            cy++;
        }
        if(dir == IsoDirections.S)
        {
            y++;
            cy++;
        }
        if(dir == IsoDirections.SW)
        {
            y++;
            cy++;
            fx--;
            cx--;
        }
        if(dir == IsoDirections.W)
        {
            fx--;
            cx--;
        }
        if(dir == IsoDirections.NW)
        {
            fx--;
            fy--;
            cx--;
            cy--;
        }
        IsoGridSquare ca = getCell().getGridSquare((int)getX() + cx, (int)(getY() + (float)cy), (int)getZ());
        IsoGridSquare c = getCell().getGridSquare((int)getX(), (int)getY(), (int)getZ());
        IsoGridSquare a = getCell().getGridSquare((int)(getX() + (float)x), (int)getY(), (int)getZ());
        IsoGridSquare b = getCell().getGridSquare((int)getX(), (int)(getY() + (float)y), (int)getZ());
        IsoGridSquare d = getCell().getGridSquare((int)(getX() - (float)fx), (int)getY(), (int)getZ());
        IsoGridSquare e = getCell().getGridSquare((int)getX(), (int)(getY() - (float)fy), (int)getZ());
        if(c != null)
        {
            for(int n = 0; n < c.getObjects().size(); n++)
            {
                IsoObject obh = (IsoObject)c.getObjects().get(n);
                if(obh.container != null)
                    return obh;
            }

        }
        if(ca != null)
        {
            for(int n = 0; n < ca.getObjects().size(); n++)
            {
                IsoObject obh = (IsoObject)ca.getObjects().get(n);
                if(obh.container != null)
                    return obh;
            }

        }
        if(cx != 0 && cy != 0)
        {
            IsoGridSquare cax = getCell().getGridSquare((int)getX() + cx, (int)getY(), (int)getZ());
            IsoGridSquare cay = getCell().getGridSquare((int)getX(), (int)getY() + cy, (int)getZ());
            if(cax != null)
            {
                for(int n = 0; n < cax.getObjects().size(); n++)
                {
                    IsoObject obh = (IsoObject)cax.getObjects().get(n);
                    if(obh.container != null)
                        return obh;
                }

            }
            if(cay != null)
            {
                for(int n = 0; n < cay.getObjects().size(); n++)
                {
                    IsoObject obh = (IsoObject)cay.getObjects().get(n);
                    if(obh.container != null)
                        return obh;
                }

            }
        }
        if(c != null && c.getSpecialObjects().size() > 0)
        {
            for(int n = 0; n < c.getObjects().size(); n++)
            {
                IsoObject obh = (IsoObject)c.getObjects().get(n);
                if(obh instanceof IsoDoor)
                    return obh;
            }

        } else
        if(a != null && a.getSpecialObjects().size() > 0)
        {
            for(int n = 0; n < a.getSpecialObjects().size(); n++)
            {
                IsoObject obh = (IsoObject)a.getSpecialObjects().get(n);
                if(obh instanceof IsoDoor)
                    return obh;
            }

        } else
        if(b != null && b.getSpecialObjects().size() > 0)
        {
            for(int n = 0; n < b.getSpecialObjects().size(); n++)
            {
                IsoObject obh = (IsoObject)b.getSpecialObjects().get(n);
                if(obh instanceof IsoDoor)
                    return obh;
            }

        } else
        if(d != null && b.getSpecialObjects().size() > 0)
        {
            for(int n = 0; n < d.getSpecialObjects().size(); n++)
            {
                IsoObject obh = (IsoObject)d.getSpecialObjects().get(n);
                if(obh instanceof IsoDoor)
                    return obh;
            }

        } else
        if(e != null && b.getSpecialObjects().size() > 0)
        {
            for(int n = 0; n < e.getSpecialObjects().size(); n++)
            {
                IsoObject obh = (IsoObject)e.getSpecialObjects().get(n);
                if(obh instanceof IsoDoor)
                    return obh;
            }

        }
        return null;
    }

    public float getMoveSpeed()
    {
        return 0.06F;
    }

    public float getTorchStrength()
    {
        float strength = 0.0F;
        if(leftHandItem != null && leftHandItem.getType().equals("Torch") && ((Drainable)leftHandItem).getUsedDelta() > 0.0F)
            strength = 1.7F;
        if(rightHandItem != null && rightHandItem.getType().equals("Torch") && ((Drainable)rightHandItem).getUsedDelta() > 0.0F)
            strength = 1.7F;
        return strength;
    }

    public void pathFinished()
    {
        stateMachine.changeState(defaultState);
        path = null;
    }

    public void Scratched()
    {
        IsoSurvivor other;
        if(descriptor.Group != null && descriptor.Group.Members.size() > 0)
            other = (IsoSurvivor)descriptor.Group.getRandomMemberExcept(instance);
    }

    public void Bitten()
    {
        IsoSurvivor other;
        if(descriptor.Group != null && descriptor.Group.Members.size() > 0)
            other = (IsoSurvivor)descriptor.Group.getRandomMemberExcept(instance);
    }

    public float getRadiusKickback(HandWeapon weapon)
    {
        return 15F * getInvAimingMod();
    }

    public int getChancesToHeadshotHandWeapon()
    {
        int level = getPerkLevel(zombie.characters.skills.PerkFactory.Perks.Aiming);
        if(level == 1)
            return 2;
        if(level == 2)
            return 2;
        if(level == 3)
            return 2;
        if(level == 4)
            return 3;
        return level != 5 ? 2 : 4;
    }

    public float getInvAimingMod()
    {
        int level = getPerkLevel(zombie.characters.skills.PerkFactory.Perks.Aiming);
        if(level == 1)
            return 0.9F;
        if(level == 2)
            return 0.8F;
        if(level == 3)
            return 0.7F;
        if(level == 4)
            return 0.6F;
        return level != 5 ? 1.0F : 0.5F;
    }

    public float getAimingMod()
    {
        int level = getPerkLevel(zombie.characters.skills.PerkFactory.Perks.Aiming);
        if(level == 1)
            return 1.1F;
        if(level == 2)
            return 1.2F;
        if(level == 3)
            return 1.3F;
        if(level == 4)
            return 1.4F;
        return level != 5 ? 1.0F : 1.5F;
    }

    public float getReloadingMod()
    {
        int level = getPerkLevel(zombie.characters.skills.PerkFactory.Perks.Reloading);
        if(level == 1)
            return 2.5F;
        if(level == 2)
            return 2.0F;
        if(level == 3)
            return 1.5F;
        if(level == 4)
            return 1.0F;
        return level != 5 ? 3F : 0.5F;
    }

    public float getAimingRangeMod()
    {
        int level = getPerkLevel(zombie.characters.skills.PerkFactory.Perks.Aiming);
        if(level == 1)
            return 1.2F;
        if(level == 2)
            return 1.4F;
        if(level == 3)
            return 1.6F;
        if(level == 4)
            return 1.8F;
        return level != 5 ? 1.0F : 2.0F;
    }

    public float getInvAimingRangeMod()
    {
        int level = getPerkLevel(zombie.characters.skills.PerkFactory.Perks.Aiming);
        if(level == 1)
            return 0.8F;
        if(level == 2)
            return 0.6F;
        if(level == 3)
            return 0.5F;
        if(level == 4)
            return 0.3F;
        return level != 5 ? 1.0F : 0.1F;
    }

    public void CalculateAim()
    {
        Vector2 vec = new Vector2(instance.getX(), instance.getY());
        int mx = Mouse.getX();
        int my = Mouse.getY();
        vec.x -= IsoUtils.XToIso(mx - 30, ((float)my - 356F) + 40F, instance.getZ());
        vec.y -= IsoUtils.YToIso(mx - 30, ((float)my - 356F) + 40F, instance.getZ());
        float dist = vec.getLength();
        EffectiveAimDistance = dist;
        float distDelta = dist / 10F;
        if(distDelta > 1.0F)
            distDelta *= 2.0F;
        if(distDelta < 0.05F)
            distDelta = 0.05F;
        distDelta *= getInvAimingRangeMod();
        if(IsUsingAimWeapon())
            DesiredAimRadius = distDelta * 60F;
        else
        if(IsUsingAimHandWeapon() && isCharging)
            DesiredAimRadius = 10F;
        else
        if(IsUsingAimHandWeapon() && !isCharging)
            AimRadius = 100F;
        if(IsUsingAimWeapon() && DesiredAimRadius < 10F)
            DesiredAimRadius = 10F;
        mx = Mouse.getXA();
        my = Mouse.getYA();
        float moveDist = IsoUtils.DistanceTo(mx, my, lmx, lmy);
        float moveDelta = moveDist / 30F;
        moveDelta *= getInvAimingMod();
        AimRadius += moveDelta * 5F;
        if(AimRadius > 70F)
            AimRadius = 70F;
        lmx = mx;
        lmy = my;
        float Dif = Math.abs(AimRadius - DesiredAimRadius) / 40F;
        Dif *= GameTime.instance.getMultiplier();
        if((getUseHandWeapon() == null || !getUseHandWeapon().isAimedHandWeapon()) && getUseHandWeapon() != null && !getUseHandWeapon().isAimed())
        {
            if(isCharging)
                DesiredAimRadius += chargeTime * 0.05F;
            else
                DesiredAimRadius = 10F;
            if(isCharging)
                AimRadius += chargeTime * 0.05F;
            else
                AimRadius = 10F;
        }
        if(getUseHandWeapon() != null)
            DesiredAimRadius *= getUseHandWeapon().getAimingMod();
        if(AimRadius > DesiredAimRadius)
        {
            if(distDelta <= 0.2F)
                Dif *= 2.0F;
            if(distDelta <= 0.5F)
                Dif *= 2.0F;
            Dif *= getAimingMod();
            if(getUseHandWeapon() != null)
                Dif *= getUseHandWeapon().getAimingMod();
            AimRadius -= Dif;
            if(AimRadius < DesiredAimRadius)
                AimRadius = DesiredAimRadius;
        } else
        if(AimRadius > DesiredAimRadius)
        {
            AimRadius += Dif;
            if(AimRadius > DesiredAimRadius)
                AimRadius = DesiredAimRadius;
        }
    }

    public void update()
    {
        LuaEventManager.TriggerEvent("OnPlayerUpdate", this);
        if(Health <= 0.0F || BodyDamage.getHealth() <= 0.0F)
        {
            stateMachine.changeState(DieState.instance());
            stateMachine.Lock = true;
            super.update();
            return;
        }
        CalculateAim();
        if(bCouldBeSeenThisFrame && !bSeenThisFrame)
        {
            xp.AddXP(zombie.characters.skills.PerkFactory.Perks.Sneak, 1);
            xp.AddXP(zombie.characters.skills.PerkFactory.Perks.Lightfoot, 1);
        }
        bSeenThisFrame = false;
        bCouldBeSeenThisFrame = false;
        if(!IsoCamera.CamCharacter.isDead());
        if(!FrameLoader.bServer)
        {
            if(Core.bDebug && Keyboard.isKeyDown(45))
                instance.xp.AddXP(zombie.characters.skills.PerkFactory.Perks.Axe, 10);
            if(Core.bDebug && Keyboard.isKeyDown(49))
                GhostMode = true;
            if(Core.bDebug && Keyboard.isKeyDown(22))
            {
                if(!bChangeCharacterDebounce)
                {
                    FollowCamStack.clear();
                    bChangeCharacterDebounce = true;
                    for(int n = 0; n < getCell().getObjectList().size(); n++)
                    {
                        IsoMovingObject obj = (IsoMovingObject)getCell().getObjectList().get(n);
                        if(obj instanceof IsoSurvivor)
                            FollowCamStack.add((IsoSurvivor)obj);
                    }

                    if(followID >= FollowCamStack.size())
                        followID = 0;
                    IsoCamera.SetCharacterToFollow((IsoGameCharacter)FollowCamStack.get(followID));
                    UIManager.sidebar.InventoryFlow.Container = IsoCamera.CamCharacter.inventory;
                    UIManager.sidebar.MainHand.chr = IsoCamera.CamCharacter;
                    UIManager.sidebar.SecondHand.chr = IsoCamera.CamCharacter;
                    followID++;
                }
            } else
            if(Keyboard.isKeyDown(34))
            {
                if(!bChangeCharacterDebounce)
                {
                    bChangeCharacterDebounce = true;
                    Core.getInstance().nGraphicLevel++;
                    if(Core.getInstance().nGraphicLevel >= 5)
                        Core.getInstance().nGraphicLevel = 0;
                }
            } else
            {
                bChangeCharacterDebounce = false;
            }
            if(current.Has(IsoObjectType.stairsBN) || current.Has(IsoObjectType.stairsMN) || current.Has(IsoObjectType.stairsTN))
            {
                float xl = x - (float)(int)x;
                if(xl < 0.5F)
                {
                    setX(x + 0.1F);
                    xl = x - (float)(int)x;
                    if(xl > 0.5F)
                        xl = 0.5F;
                } else
                if(xl < 0.5F)
                {
                    setX(x - 0.1F);
                    xl = x - (float)(int)x;
                    if(xl > 0.5F)
                        xl = 0.5F;
                }
            }
        }
        if(GhostMode)
        {
            stats.hunger = 0.0F;
            stats.thirst = 0.0F;
        }
        boolean isAttacking = false;
        bRunning = false;
        bSneaking = false;
        TimeSinceRightClick++;
        useChargeTime = chargeTime;
        if(!FrameLoader.bServer)
        {
            heartDelay--;
            if(heartDelay <= 0)
            {
                heartDelayMax = (int)((1.0F - (stats.Panic / 100F) * 0.7F) * 25F);
                heartDelay = heartDelayMax;
                SoundManager.instance.PlaySound("heart", false, (stats.Panic / 100F) * 0.35F);
            }
            if(Core.bDebug && !UITextBox2.ConsoleHasFocus && Keyboard.isKeyDown(23))
            {
                IsoCamera.CamCharacter = instance;
                UIManager.sidebar.InventoryFlow.Container = IsoCamera.CamCharacter.inventory;
                UIManager.sidebar.MainHand.chr = IsoCamera.CamCharacter;
                UIManager.sidebar.SecondHand.chr = IsoCamera.CamCharacter;
            }
            isAiming = !PZConsole.instance.isVisible() && (Keyboard.isKeyDown(29) || Keyboard.isKeyDown(157) || Keyboard.isKeyDown(44));
            if(isCharging)
                chargeTime += 1.0F * GameTime.instance.getMultiplier();
            isAttacking = isCharging && !Mouse.isButtonDown(0);
            if(!isCharging)
                chargeTime = 0.0F;
            isCharging = Mouse.isButtonDown(0);
            if(isAiming)
                UIManager.setDoMouseControls(true);
            Waiting = !PZConsole.instance.isVisible() && Keyboard.isKeyDown(28);
            bRunning = !PZConsole.instance.isVisible() && (Keyboard.isKeyDown(42) || Keyboard.isKeyDown(54));
            bWasRunning = bRightClickMove;
            bRightClickMove = Mouse.isRightDown();
            if(bRightClickMove && !bWasRunning && TimeSinceRightClick < 30)
            {
                TimeRightClicking = 0;
                if(LastTimeRightClicking < 30)
                    bDoubleClick = true;
            }
            if(bRightClickMove)
                TimeRightClicking++;
            if(!bRightClickMove)
            {
                if(bWasRunning && TimeRightClicking > 0)
                    LastTimeRightClicking = TimeRightClicking;
                TimeRightClicking = 0;
                TimeSinceRightClick = 0;
                bDoubleClick = false;
            } else
            {
                TimeSinceRightClick = 0;
            }
            if(bDoubleClick)
                bRunning = true;
            if(bRunning)
                bDoubleClick = true;
            TimeSinceRightClick++;
            bSneaking = isAiming;
            if(getX() != getLx() || getY() != getLy())
                lastPos.add(new Vector2(getX(), getY()));
            if(lastPos.size() > 9)
                lastPos.remove(0);
            if(DragCharacter != null && lastPos.size() > 1)
            {
                DragCharacter.setX(((Vector2)lastPos.get(0)).x);
                DragCharacter.setY(((Vector2)lastPos.get(0)).y);
                DragCharacter.setZ(getZ());
                if(DragCharacter.Health <= 0.0F)
                    DragCharacter = null;
                if(DragCharacter.BodyDamage.getHealth() <= 0.0F)
                    DragCharacter = null;
            }
            TicksSinceSeenZombie++;
        }
        super.update();
        int fds;
        if(bWasRunning && !bRightClickMove)
            fds = 0;
        movementLastFrame.x = playerMoveDir.x;
        movementLastFrame.y = playerMoveDir.y;
        if(sprite != null)
        {
            if(sprite.CurrentAnim == null)
                return;
            if(sprite.CurrentAnim.name == null)
                return;
            if(sprite.CurrentAnim.name.equals("Die"))
                return;
            if(sprite.CurrentAnim.name.equals("ZombieDeath"))
                return;
        }
        if(!FrameLoader.bServer)
        {
            DoHotKey(0, 2);
            DoHotKey(1, 3);
            DoHotKey(2, 4);
            DoHotKey(3, 5);
            DoHotKey(4, 6);
            DoHotKey(5, 7);
            DoHotKey(6, 8);
            DoHotKey(7, 9);
            DoHotKey(8, 10);
            DoHotKey(9, 11);
        }
        if(stateMachine.getCurrent() == StaggerBackState.instance() || stateMachine.getCurrent() == StaggerBackDieState.instance() || stateMachine.getCurrent() == DieState.instance() || stateMachine.getCurrent() == ReanimateState.instance())
            return;
        JustMoved = false;
        float nax = 0.0F;
        float nay = 0.0F;
        float moveDirX = nax;
        float moveDirY = nay;
        IsoDirections NewFacing = this.dir;
        if(!FrameLoader.bServer && !TutorialManager.instance.StealControl)
        {
            if(GameWindow.ActiveController != -1 && GameWindow.ActivatedJoyPad)
            {
                if(GameWindow.GameInput.isButtonPressed(5, GameWindow.ActiveController))
                    isAttacking = true;
                if(GameWindow.GameInput.isButtonPressedD(0, GameWindow.ActiveController))
                    PressedA();
                float xVal = GameWindow.GameInput.getAxisValue(GameWindow.ActiveController, GameWindow.XLAxis);
                float yVal = GameWindow.GameInput.getAxisValue(GameWindow.ActiveController, GameWindow.YLAxis);
                float xVal2 = GameWindow.GameInput.getAxisValue(GameWindow.ActiveController, GameWindow.XRAxis);
                float yVal2 = GameWindow.GameInput.getAxisValue(GameWindow.ActiveController, GameWindow.YRAxis);
                if(xVal2 > -0.3F && xVal2 < 0.3F)
                    xVal2 = 0.0F;
                if(yVal2 > -0.3F && yVal2 < 0.3F)
                    yVal2 = 0.0F;
                Vector2 dir = new Vector2(xVal, yVal);
                if(Math.abs(xVal) > 0.3F)
                {
                    UIManager.setDoMouseControls(false);
                    playerMoveDir.x += 0.04F * xVal;
                    playerMoveDir.y -= 0.04F * xVal;
                }
                if(Math.abs(yVal) > 0.3F)
                {
                    UIManager.setDoMouseControls(false);
                    playerMoveDir.y += 0.04F * yVal;
                    playerMoveDir.x += 0.04F * yVal;
                }
                if(dir.getLength() > 0.8F)
                    bRunning = true;
                else
                if(dir.getLength() < 0.4F)
                    bSneaking = true;
                if(xVal2 != 0.0F || yVal2 != 0.0F)
                {
                    UIManager.setDoMouseControls(false);
                    Vector2 newVec = new Vector2(xVal2, yVal2);
                    IsoDirections temp = this.dir;
                    newVec.normalize();
                    newVec.rotate(-0.7853982F);
                    DirectionFromVector(newVec);
                    NewFacing = this.dir;
                    bSneaking = true;
                    isAiming = true;
                    this.dir = temp;
                } else
                {
                    Vector2 newVec = new Vector2(playerMoveDir.x, playerMoveDir.y);
                    newVec.normalize();
                    IsoDirections temp = this.dir;
                    DirectionFromVector(newVec);
                    NewFacing = this.dir;
                    this.dir = temp;
                }
            }
            if(!PZConsole.instance.isVisible())
            {
                if(!Speaking && Keyboard.isKeyDown(16))
                    Callout();
                if(Core.bAltMoveMethod)
                {
                    if(Keyboard.isKeyDown(30) || Keyboard.isKeyDown(203) || Keyboard.isKeyDown(32) || Keyboard.isKeyDown(205) || Keyboard.isKeyDown(17) || Keyboard.isKeyDown(200) || Keyboard.isKeyDown(31) || Keyboard.isKeyDown(208))
                    {
                        nax = 0.0F;
                        nay = 0.0F;
                    }
                    if(Keyboard.isKeyDown(30) || Keyboard.isKeyDown(203))
                    {
                        UIManager.setDoMouseControls(true);
                        nax -= 0.04F;
                        NewFacing = IsoDirections.W;
                        if(stateMachine.getCurrent() == PathFindState.instance())
                            stateMachine.setCurrent(defaultState);
                        JustMoved = true;
                    }
                    if(Keyboard.isKeyDown(32) || Keyboard.isKeyDown(205))
                    {
                        UIManager.setDoMouseControls(true);
                        nax += 0.04F;
                        NewFacing = IsoDirections.E;
                        if(stateMachine.getCurrent() == PathFindState.instance())
                            stateMachine.setCurrent(defaultState);
                        JustMoved = true;
                    }
                    if(Keyboard.isKeyDown(17) || Keyboard.isKeyDown(200))
                    {
                        UIManager.setDoMouseControls(true);
                        nay -= 0.04F;
                        if(NewFacing == IsoDirections.W)
                            NewFacing = IsoDirections.NW;
                        else
                        if(NewFacing == IsoDirections.E)
                            NewFacing = IsoDirections.NE;
                        else
                            NewFacing = IsoDirections.N;
                        if(stateMachine.getCurrent() == PathFindState.instance())
                            stateMachine.setCurrent(defaultState);
                        JustMoved = true;
                    }
                    if(Keyboard.isKeyDown(31) || Keyboard.isKeyDown(208))
                    {
                        UIManager.setDoMouseControls(true);
                        nay += 0.04F;
                        if(NewFacing == IsoDirections.W)
                            NewFacing = IsoDirections.SW;
                        else
                        if(NewFacing == IsoDirections.E)
                            NewFacing = IsoDirections.SE;
                        else
                            NewFacing = IsoDirections.S;
                        if(stateMachine.getCurrent() == PathFindState.instance())
                            stateMachine.setCurrent(defaultState);
                        JustMoved = true;
                    }
                } else
                {
                    if(Keyboard.isKeyDown(30) || Keyboard.isKeyDown(203) || Keyboard.isKeyDown(32) || Keyboard.isKeyDown(205) || Keyboard.isKeyDown(17) || Keyboard.isKeyDown(200) || Keyboard.isKeyDown(31) || Keyboard.isKeyDown(208))
                    {
                        nax = 0.0F;
                        nay = 0.0F;
                    }
                    if(Keyboard.isKeyDown(30) || Keyboard.isKeyDown(203))
                    {
                        UIManager.setDoMouseControls(true);
                        nax -= 0.04F;
                        nay += 0.04F;
                        NewFacing = IsoDirections.SW;
                        if(stateMachine.getCurrent() == PathFindState.instance())
                            stateMachine.setCurrent(defaultState);
                        JustMoved = true;
                    }
                    if(Keyboard.isKeyDown(32) || Keyboard.isKeyDown(205))
                    {
                        UIManager.setDoMouseControls(true);
                        nax += 0.04F;
                        nay -= 0.04F;
                        NewFacing = IsoDirections.NE;
                        if(stateMachine.getCurrent() == PathFindState.instance())
                            stateMachine.setCurrent(defaultState);
                        JustMoved = true;
                    }
                    if(Keyboard.isKeyDown(17) || Keyboard.isKeyDown(200))
                    {
                        UIManager.setDoMouseControls(true);
                        nay -= 0.04F;
                        nax -= 0.04F;
                        if(NewFacing == IsoDirections.SW)
                            NewFacing = IsoDirections.W;
                        else
                        if(NewFacing == IsoDirections.NE)
                            NewFacing = IsoDirections.N;
                        else
                            NewFacing = IsoDirections.NW;
                        if(stateMachine.getCurrent() == PathFindState.instance())
                            stateMachine.setCurrent(defaultState);
                        JustMoved = true;
                    }
                    if(Keyboard.isKeyDown(31) || Keyboard.isKeyDown(208))
                    {
                        UIManager.setDoMouseControls(true);
                        nay += 0.04F;
                        nax += 0.04F;
                        if(NewFacing == IsoDirections.SW)
                            NewFacing = IsoDirections.S;
                        else
                        if(NewFacing == IsoDirections.NE)
                            NewFacing = IsoDirections.E;
                        else
                            NewFacing = IsoDirections.SE;
                        if(stateMachine.getCurrent() == PathFindState.instance())
                            stateMachine.setCurrent(defaultState);
                        JustMoved = true;
                    }
                }
            }
            moveDirX = nax;
            moveDirY = nay;
            if(JustMoved)
            {
                if(!isAiming)
                {
                    lastAngle.x = angle.x;
                    lastAngle.y = angle.y;
                    angle.x = nax;
                    angle.y = nay;
                    moveDirX = angle.x;
                    moveDirY = angle.y;
                }
                bRightClickMove = true;
                angle.normalize();
                UIManager.speedControls.SetCurrentGameSpeed(1);
            } else
            {
                if(nax != 0.0F || nay != 0.0F)
                {
                    angle.x = nax;
                    angle.y = nay;
                }
                moveDirX = nax;
                moveDirY = nay;
            }
            DrunkOscilatorStepSin += DrunkOscilatorRateSin;
            DrunkOscilatorStepCos += DrunkOscilatorRateCos;
            DrunkOscilatorStepCos2 += DrunkOscilatorRateCos2;
            DrunkSin = (float)Math.sin(DrunkOscilatorStepSin);
            DrunkCos = (float)Math.cos(DrunkOscilatorStepCos);
            DrunkCos2 = (float)Math.sin(DrunkOscilatorStepCos2);
            if(DrunkOscilatorRateSin >= DesiredSinRate)
            {
                DrunkOscilatorRateSin -= OscilatorChangeRate;
                if(DrunkOscilatorRateSin < DesiredSinRate)
                    DesiredSinRate = MinOscilatorRate + ((float)Rand.Next(10000) / 10000F) * (MaxOscilatorRate - MinOscilatorRate);
            }
            if(DrunkOscilatorRateSin <= DesiredSinRate)
            {
                DrunkOscilatorRateSin += OscilatorChangeRate;
                if(DrunkOscilatorRateSin > DesiredSinRate)
                    DesiredSinRate = MinOscilatorRate + ((float)Rand.Next(10000) / 10000F) * (MaxOscilatorRate - MinOscilatorRate);
            }
            if(DrunkOscilatorRateCos >= DesiredCosRate)
            {
                DrunkOscilatorRateCos -= OscilatorChangeRate;
                if(DrunkOscilatorRateCos < DesiredCosRate)
                    DesiredCosRate = MinOscilatorRate + ((float)Rand.Next(10000) / 10000F) * (MaxOscilatorRate - MinOscilatorRate);
            }
            if(DrunkOscilatorRateCos <= DesiredCosRate)
            {
                DrunkOscilatorRateCos += OscilatorChangeRate;
                if(DrunkOscilatorRateCos > DesiredCosRate)
                    DesiredCosRate = MinOscilatorRate + ((float)Rand.Next(10000) / 10000F) * (MaxOscilatorRate - MinOscilatorRate);
            }
            if(bRightClickMove)
            {
                if(UIManager.getSpeedControls().getCurrentGameSpeed() > 1)
                    UIManager.getSpeedControls().SetCurrentGameSpeed(1);
                float WobbleFactor = 0.0F;
                if(Moodles.getMoodleLevel(MoodleType.Drunk) == 2)
                    WobbleFactor = 0.03F;
                if(Moodles.getMoodleLevel(MoodleType.Drunk) == 3)
                    WobbleFactor = 0.04F;
                if(Moodles.getMoodleLevel(MoodleType.Drunk) == 4)
                    WobbleFactor = 0.06F;
                playerMoveDir.x += DrunkSin * WobbleFactor;
                playerMoveDir.y += DrunkCos * WobbleFactor;
                if(WobbleFactor > 0.0F)
                {
                    playerMoveDir.x *= (DrunkCos2 + 1.0F) * WobbleFactor;
                    playerMoveDir.y *= (DrunkCos2 + 1.0F) * WobbleFactor;
                    if(playerMoveDir.x != 0.0F || playerMoveDir.y != 0.0F)
                    {
                        Vector2 newVec = new Vector2(playerMoveDir.x, playerMoveDir.y);
                        newVec.normalize();
                        IsoDirections temp = this.dir;
                        DirectionFromVector(newVec);
                        NewFacing = this.dir;
                        this.dir = temp;
                    }
                }
            } else
            if(stats.endurance < stats.endurancedanger && Rand.Next((int)(60F * GameTime.instance.getInvMultiplier())) == 0)
                xp.AddXP(zombie.characters.skills.PerkFactory.Perks.Recovery, 1);
        }
        if(!FrameLoader.bServer)
        {
            float len = getMoveSpeed();
            float delta = 1.0F;
            if(instance.BodyDamage.IsSneezingCoughing() > 0)
            {
                len = 0.0F;
                playerMoveDir.x = 0.0F;
                playerMoveDir.y = 0.0F;
            }
            Integer weight = Integer.valueOf(instance.inventory.getWeight());
            Integer maxWeight = Integer.valueOf(instance.maxWeight.intValue());
            float d = 0.0F;
            if(bRightClickMove)
            {
                if(JustMoved)
                {
                    if(!bRunning)
                        d = 1.0F;
                    else
                        d = 1.6F;
                } else
                if(!isAiming)
                    d = runAngle.getLength() / 4F;
                if(d > 1.6F)
                    d = 1.6F;
            }
            float newDelta = d;
            delta *= d;
            if(runAngle.getLength() == 0.0F && !JustMoved)
                delta = 0.0F;
            if(delta > 1.0F)
                delta *= getSprintMod();
            float animDelta = (CurrentSpeed / 0.06F) * getGlobalMovementMod();
            if(delta > 1.0F && HasTrait("Athletic"))
                delta *= 1.2F;
            if(delta > 1.0F && HasTrait("Overweight"))
                delta *= 0.8F;
            float rundelta = CurrentSpeed / 0.06F;
            if(animDelta > 1.0F || delta > 1.0F)
            {
                if(delta < 1.0F)
                    rundelta *= 0.3F;
                float enddelta = 1.4F;
                if(HasTrait("Overweight"))
                    enddelta = 1.6F;
                if(HasTrait("Athletic"))
                    enddelta = 0.8F;
                enddelta *= 3F;
                enddelta *= getPacingMod();
                if(weight.intValue() > maxWeight.intValue() / 2)
                {
                    weight = Integer.valueOf(weight.intValue() - maxWeight.intValue() / 2);
                    maxWeight = Integer.valueOf(maxWeight.intValue() / 2);
                    newDelta = (float)weight.intValue() / (float)maxWeight.intValue();
                    newDelta++;
                    stats.endurance -= ZomboidGlobals.RunningEnduranceReduce * (double)newDelta * (double)enddelta * (double)rundelta * 0.69999998807907104D;
                } else
                {
                    stats.endurance -= ZomboidGlobals.RunningEnduranceReduce * (double)enddelta * (double)(rundelta * 0.5F) * 0.69999998807907104D;
                }
            } else
            {
                stats.endurance += ZomboidGlobals.ImobileEnduranceReduce * (double)getRecoveryMod();
            }
            if(TutorialManager.instance.ActiveControlZombies && !IsoWorld.instance.CurrentCell.IsZone("tutArea", (int)x, (int)y))
                TutorialManager.instance.ActiveControlZombies = false;
            if(bSneaking && JustMoved)
                delta *= 0.5F;
            if(bSneaking)
                delta *= getNimbleMod();
            if(delta > 0.0F)
            {
                if(delta < 0.5F)
                    bSneaking = true;
                if(delta > 1.3F)
                    bRunning = true;
                if(bSneaking && Rand.Next((int)(80F * GameTime.instance.getInvMultiplier())) == 0)
                    xp.AddXP(zombie.characters.skills.PerkFactory.Perks.Nimble, 1);
                if((float)inventory.getWeight() > (float)maxWeight.intValue() * 0.5F && Rand.Next((int)(80F * GameTime.instance.getInvMultiplier())) == 0)
                {
                    xp.AddXP(zombie.characters.skills.PerkFactory.Perks.Hauling, 1);
                    getXp().AddXP(zombie.characters.skills.PerkFactory.Perks.Strength, 1);
                }
                if(bRunning && stats.endurance > stats.endurancewarn)
                {
                    if((float)inventory.getWeight() > (float)maxWeight.intValue() * 0.5F && Rand.Next((int)(80F * GameTime.instance.getInvMultiplier())) == 0)
                    {
                        xp.AddXP(zombie.characters.skills.PerkFactory.Perks.Hauling, 1);
                        getXp().AddXP(zombie.characters.skills.PerkFactory.Perks.Strength, 1);
                    }
                    if(Rand.Next((int)(80F * GameTime.instance.getInvMultiplier())) == 0)
                        xp.AddXP(zombie.characters.skills.PerkFactory.Perks.Pacing, 1);
                    if(Rand.Next((int)(80F * GameTime.instance.getInvMultiplier())) == 0)
                        xp.AddXP(zombie.characters.skills.PerkFactory.Perks.Sprinting, 1);
                }
            }
            if(JustMoved || bRightClickMove)
                sprite.Animate = true;
            if((bRightClickMove && !JustMoved || isAiming) && CanAttack())
            {
                if(DragCharacter != null)
                {
                    DragObject = null;
                    DragCharacter.Dragging = false;
                    DragCharacter = null;
                }
                if(leftHandItem != null && AttackDelay <= 0 && isAiming && !JustMoved && DoAimAnimOnAiming())
                    PlayShootAnim();
                if(isAttacking)
                {
                    sprite.Animate = true;
                    Vector2 vec = new Vector2(instance.getX(), instance.getY());
                    int mx = Mouse.getX();
                    int my = Mouse.getY();
                    vec.x -= IsoUtils.XToIso(mx - 30, ((float)my - 356F) + 40F, instance.getZ());
                    vec.y -= IsoUtils.YToIso(mx - 30, ((float)my - 356F) + 40F, instance.getZ());
                    vec.x = -vec.x;
                    vec.y = -vec.y;
                    vec.normalize();
                    if(!UIManager.isDoMouseControls() && GameWindow.ActivatedJoyPad)
                        vec = getControllerAimDir(vec);
                    DirectionFromVector(vec);
                    AttemptAttack(useChargeTime);
                    bDebounceLMB = true;
                } else
                {
                    bDebounceLMB = false;
                }
                if(!JustMoved || isAiming)
                {
                    Vector2 vec = new Vector2(getX(), getY());
                    int mx = Mouse.getX();
                    int my = Mouse.getY();
                    vec.x -= IsoUtils.XToIso(mx - 30, ((float)my - 356F) + 40F, instance.getZ());
                    vec.y -= IsoUtils.YToIso(mx - 30, ((float)my - 356F) + 40F, instance.getZ());
                    runAngle.x = vec.x;
                    runAngle.y = vec.y;
                    if(runAngle.getLength() < 0.3F)
                        runAngle.setLength(0.0F);
                    else
                        runAngle.setLength(runAngle.getLength() - 0.3F);
                    vec.x = -vec.x;
                    vec.y = -vec.y;
                    vec.normalize();
                    lastAngle.x = angle.x;
                    lastAngle.y = angle.y;
                    angleCounter++;
                    angle.x = vec.x;
                    angle.y = vec.y;
                    angleCounter = 0;
                    if(!UIManager.isDoMouseControls() && GameWindow.ActivatedJoyPad)
                        vec = getControllerAimDir(vec);
                    DirectionFromVector(vec);
                } else
                {
                    DirectionFromVector(angle);
                }
                NewFacing = this.dir;
            }
            if(DragCharacter != null)
                delta = 0.4F;
            if(stats.endurance < 0.0F)
                stats.endurance = 0.0F;
            if(stats.endurance > 1.0F)
                stats.endurance = 1.0F;
            if(stats.endurance < stats.endurancewarn)
            {
                float mul = stats.endurance;
                if(mul < 0.3F)
                    delta *= 0.3F;
                else
                    delta *= stats.endurance;
            } else
            {
                delta *= 1.0F;
            }
            if(playerMoveDir.getLength() > 0.0F)
                UIManager.CloseContainers();
            int dif = Math.abs(this.dir.index() - NewFacing.index());
            if(dif > 4)
                dif = 4 - (dif - 4);
            if(dif > 2 && stats.Sanity < 0.5F && (float)Rand.Next(100) > stats.Sanity * 100F && Rand.Next(20) == 0)
            {
                IsoGridSquare sq = null;
                if(NewFacing == IsoDirections.N)
                    sq = getCell().getGridSquare((int)getX(), (int)getY() - 1, (int)getZ());
                if(NewFacing == IsoDirections.S)
                    sq = getCell().getGridSquare((int)getX(), (int)getY() + 1, (int)getZ());
                if(NewFacing == IsoDirections.E)
                    sq = getCell().getGridSquare((int)getX() + 1, (int)getY(), (int)getZ());
                if(NewFacing == IsoDirections.W)
                    sq = getCell().getGridSquare((int)getX() - 1, (int)getY(), (int)getZ());
                if(NewFacing == IsoDirections.NE)
                    sq = getCell().getGridSquare((int)getX() + 1, (int)getY() - 1, (int)getZ());
                if(NewFacing == IsoDirections.SE)
                    sq = getCell().getGridSquare((int)getX() - 1, (int)getY() + 1, (int)getZ());
                if(NewFacing == IsoDirections.NW)
                    sq = getCell().getGridSquare((int)getX() - 1, (int)getY() - 1, (int)getZ());
                if(NewFacing == IsoDirections.SW)
                    sq = getCell().getGridSquare((int)getX() - 1, (int)getY() + 1, (int)getZ());
                if(sq != null)
                {
                    for(int n = 0; n < 4; n++)
                    {
                        IsoZombie zombie = new IsoZombie(IsoWorld.instance.CurrentCell);
                        zombie.setX(sq.getX());
                        zombie.setX(zombie.getX() + (float)Rand.Next(1000) / 1000F);
                        zombie.setY(sq.getY());
                        zombie.setY(zombie.getY() + (float)Rand.Next(1000) / 1000F);
                        zombie.setZ(sq.getZ());
                        zombie.setCurrent(sq);
                        zombie.GhostLife = 30F;
                        zombie.Ghost = true;
                        Vector2 vec = new Vector2(instance.getX(), instance.getY());
                        vec.x -= zombie.getX();
                        vec.y -= zombie.getY();
                        vec.normalize();
                        zombie.DirectionFromVector(vec);
                        zombie.target = instance;
                        zombie.spotted(instance);
                        IsoWorld.instance.CurrentCell.getZombieList().add(zombie);
                        IsoWorld.instance.CurrentCell.getGhostList().add(zombie);
                    }

                }
            }
            if(!bFalling && !isAiming)
                this.dir = NewFacing;
            weight = Integer.valueOf(instance.inventory.getWeight());
            maxWeight = instance.maxWeight;
            if(weight.intValue() > maxWeight.intValue() / 2)
            {
                weight = Integer.valueOf(weight.intValue() - maxWeight.intValue() / 2);
                maxWeight = Integer.valueOf(maxWeight.intValue() / 2);
                newDelta = (float)weight.intValue() / (float)maxWeight.intValue();
                newDelta = 1.0F - newDelta;
                if(newDelta <= 0.0F)
                {
                    if(delta != 0.0F)
                        if(BodyDamage.getBodyPartHealth(BodyPartType.Torso_Upper) > 55F)
                        {
                            BodyDamage.AddDamage(BodyPartType.Torso_Upper, 0.2F);
                            if(newDelta < 0.3F)
                                newDelta = 0.3F;
                        } else
                        if(BodyDamage.getBodyPartHealth(BodyPartType.Torso_Lower) > 55F)
                        {
                            BodyDamage.AddDamage(BodyPartType.Torso_Lower, 0.2F);
                            if(newDelta < 0.3F)
                                newDelta = 0.3F;
                        }
                    if(newDelta < 0.3F)
                        newDelta = 0.3F;
                } else
                if(newDelta < 0.3F)
                    newDelta = 0.3F;
                delta *= newDelta;
            }
            if(current.Has(IsoObjectType.stairsBN) || current.Has(IsoObjectType.stairsMN) || current.Has(IsoObjectType.stairsTN))
            {
                moveDirX = 0.0F;
                if(!JustMoved && bRightClickMove)
                {
                    angle.x = 0.0F;
                    angle.normalize();
                }
            }
            if(current.Has(IsoObjectType.stairsBW) || current.Has(IsoObjectType.stairsMW) || current.Has(IsoObjectType.stairsTW))
            {
                moveDirY = 0.0F;
                if(!JustMoved && bRightClickMove)
                {
                    angle.y = 0.0F;
                    angle.normalize();
                }
            }
            if(isAiming)
            {
                playerMoveDir.x = moveDirX;
                playerMoveDir.y = moveDirY;
            }
            if(!isAiming && bRightClickMove)
            {
                moveDirX = angle.x;
                moveDirY = angle.y;
                playerMoveDir.x = moveDirX;
                playerMoveDir.y = moveDirY;
            }
            float animdelaymult = 1.0F + (1.0F - (animDelta <= 1.0F ? animDelta : 1.0F));
            if(path == null)
                if(CurrentSpeed > 0.0F)
                {
                    if(!bClimbing || lastFallSpeed > 0.0F)
                    {
                        if(animDelta > 1.3F)
                        {
                            StopAllActionQueueRunning();
                            AimRadius += 10F;
                            def.setFrameSpeedPerFrame(0.24F);
                            if(leftHandItem instanceof HandWeapon)
                                PlayAnim(((HandWeapon)leftHandItem).RunAnim);
                            else
                                PlayAnim("Run");
                        } else
                        {
                            AimRadius += 0.8F;
                            StopAllActionQueueWalking();
                            def.setFrameSpeedPerFrame(0.2F);
                            PlayAnim("Walk");
                        }
                    } else
                    if(!sprite.CurrentAnim.name.contains("Attack_"))
                    {
                        def.setFrameSpeedPerFrame(0.1F);
                        if(leftHandItem instanceof HandWeapon)
                            PlayAnim(((HandWeapon)leftHandItem).IdleAnim);
                        else
                            PlayAnim("Idle");
                    }
                } else
                if(!sprite.CurrentAnim.name.contains("Attack_"))
                {
                    def.setFrameSpeedPerFrame(0.1F);
                    if(leftHandItem instanceof HandWeapon)
                        PlayAnim(((HandWeapon)leftHandItem).IdleAnim);
                    else
                        PlayAnim("Idle");
                }
            if(delta > 1.3F)
            {
                float sprintdelta = 1.0F;
                float sprintcutoff = 180F;
                if((float)TimeSprinting >= sprintcutoff)
                    sprintdelta = 1.0F - ((float)TimeSprinting - sprintcutoff) / 360F;
                else
                    sprintdelta = 1.0F - (sprintcutoff - (float)TimeSprinting) / 360F;
                sprintdelta *= 0.1F;
                sprintdelta++;
                if(sprintdelta < 0.0F)
                    sprintdelta = 0.0F;
                TimeSprinting++;
                TargetSpeed = len * sprintdelta * delta * 1.2F;
            } else
            {
                TargetSpeed = len * delta;
                if(CurrentSpeed < 0.08F)
                    TimeSprinting = 0;
            }
            float useSpeedChange = SpeedChange;
            if(CurrentSpeed < 0.06F)
                useSpeedChange *= 5F;
            if(slowTimer > 0)
                TargetSpeed *= 1.0F - slowFactor;
            if(CurrentSpeed < TargetSpeed)
            {
                CurrentSpeed += useSpeedChange / 3F;
                if(CurrentSpeed > TargetSpeed)
                    CurrentSpeed = TargetSpeed;
            } else
            if(CurrentSpeed > TargetSpeed)
                if(CurrentSpeed < 0.03F)
                {
                    CurrentSpeed = TargetSpeed;
                } else
                {
                    CurrentSpeed -= useSpeedChange;
                    if(CurrentSpeed < TargetSpeed)
                        CurrentSpeed = TargetSpeed;
                }
            if(slowTimer > 0)
            {
                slowTimer--;
                CurrentSpeed *= 1.0F - slowFactor;
            } else
            {
                slowFactor = 0.0F;
            }
            len = CurrentSpeed;
            playerMoveDir.setLength(len);
            if(playerMoveDir.x != 0.0F || playerMoveDir.y != 0.0F)
                ScriptManager.instance.Trigger("OnPlayerMoved");
            if(bSneaking)
                DoFootstepSound(CurrentSpeed * 0.3F * (2.0F - getNimbleMod()));
            else
                DoFootstepSound(CurrentSpeed * 0.3F);
            if(DragObject != null)
            {
                Vector2 vec = new Vector2(instance.getX(), instance.getY());
                vec.x -= DragObject.getX();
                vec.y -= DragObject.getY();
                vec.x = -vec.x;
                vec.y = -vec.y;
                vec.normalize();
                DirectionFromVectorNoDiags(vec);
                if((this.dir == IsoDirections.W || this.dir == IsoDirections.S || this.dir == IsoDirections.N || this.dir == IsoDirections.E) && (DragObject instanceof IsoWheelieBin))
                    DragObject.dir = this.dir;
            }
            if(DragObject != null && (DragObject instanceof IsoWheelieBin))
                DragObject.dir = this.dir;
            if(DragObject != null)
            {
                float tweight = DragObject.getWeight(playerMoveDir.x, playerMoveDir.y) + getWeight(playerMoveDir.x, playerMoveDir.y);
                float del = getWeight(playerMoveDir.x, playerMoveDir.y) / tweight;
                playerMoveDir.x *= del;
                playerMoveDir.y *= del;
            }
            if(DragObject != null && playerMoveDir.getLength() != 0.0F)
            {
                DragObject.setImpulsex(DragObject.getImpulsex() + playerMoveDir.x);
                DragObject.setImpulsey(DragObject.getImpulsey() + playerMoveDir.y);
            }
            Move(playerMoveDir);
            if(DragCharacter != null)
            {
                Vector2 vec = new Vector2(instance.getX(), instance.getY());
                vec.x -= DragCharacter.getX();
                vec.y -= DragCharacter.getY();
                vec.x = -vec.x;
                vec.y = -vec.y;
                vec.normalize();
                DirectionFromVector(vec);
                if(this.dir == IsoDirections.W || this.dir == IsoDirections.S || this.dir == IsoDirections.N || this.dir == IsoDirections.E)
                    DragCharacter.dir = this.dir;
            }
            updateLOS();
        }
        seperate();
        if(getCurrentSquare() != null)
            if(getCurrentSquare().getRoom() == null);
    }

    public boolean AttemptAttack()
    {
        return DoAttack(useChargeTime);
    }

    public boolean DoAttack(float chargeDelta)
    {
        if(chargeDelta > 90F)
            chargeDelta = 90F;
        chargeDelta /= 25F;
        useChargeDelta = chargeDelta;
        if(useChargeDelta < 0.1F)
            useChargeDelta = 1.0F;
        if(this instanceof IsoPlayer)
            FakeAttack = false;
        if(Health <= 0.0F || BodyDamage.getHealth() < 0.0F)
            return false;
        if(leftHandItem != null && AttackDelay <= 0 && (!sprite.CurrentAnim.name.contains("Attack") || def.Frame >= (float)(sprite.CurrentAnim.Frames.size() - 1)) || def.Frame == 0.0F)
        {
            InventoryItem attackItem = leftHandItem;
            if(attackItem instanceof HandWeapon)
            {
                useHandWeapon = (HandWeapon)attackItem;
                if(useHandWeapon.isCantAttackWithLowestEndurance() && stats.endurance < stats.endurancedanger)
                    return false;
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
                    def.setFrameSpeedPerFrame(1.0F / ((HandWeapon)attackItem).getSwingTime());
                    sprite.CurrentAnim.FinishUnloopedOnFrame = 0;
                }
                float val = useHandWeapon.getSwingTime();
                if(useHandWeapon.isUseEndurance())
                    val *= 1.0F - stats.endurance;
                if(val < useHandWeapon.getMinimumSwingTime())
                    val = useHandWeapon.getMinimumSwingTime();
                val *= useHandWeapon.getSpeedMod(this);
                val *= 1.0F / GameTime.instance.getMultiplier();
                AttackDelayMax = AttackDelay = (int)(val * 60F);
                AttackDelayUse = (int)((float)AttackDelayMax * useHandWeapon.getDoSwingBeforeImpact());
                AttackDelayUse = AttackDelayMax - AttackDelayUse - 2;
                AttackWasSuperAttack = superAttack;
                if(stateMachine.getCurrent() != SwipeStatePlayer.instance())
                    stateMachine.changeState(SwipeStatePlayer.instance());
                if(useHandWeapon.getAmmoType() != null)
                    if(this instanceof IsoPlayer)
                        instance.inventory.RemoveOneOf(useHandWeapon.getAmmoType());
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

    public void updateLOS()
    {
        Vector2 vectorToTarget = new Vector2();
        Vector2 vecA = new Vector2(getX(), getY());
        spottedList.clear();
        stats.NumVisibleZombies = 0;
        int close = 0;
        NumSurvivorsInVicinity = 0;
        int s = getCell().getObjectList().size();
        for(int n = 0; n < s; n++)
        {
            IsoMovingObject chr = (IsoMovingObject)getCell().getObjectList().get(n);
            if(chr == this)
            {
                spottedList.add(chr);
                continue;
            }
            float dist = IsoUtils.DistanceManhatten(chr.getX(), chr.getY(), getX(), getY());
            if(dist < 20F)
                close++;
            if(dist > GameTime.getInstance().getViewDist() || chr.getCurrentSquare() == null)
                continue;
            if(chr.getCurrentSquare().isCanSee() || dist < 2.5F && chr.getCurrentSquare().isCouldSee())
            {
                TestZombieSpotPlayer(vecA, chr);
                if((chr instanceof IsoGameCharacter) && ((IsoGameCharacter)chr).SpottedSinceAlphaZero)
                {
                    if(chr instanceof IsoSurvivor)
                        NumSurvivorsInVicinity++;
                    if(chr instanceof IsoZombie)
                    {
                        float vecToTargetLength = vecA.getLength();
                        float dist2 = vecToTargetLength / 8F;
                        if(vecToTargetLength == 0.0F)
                            dist2 = 0.0F;
                        if(dist2 > 1.0F)
                            dist2 = 1.0F;
                        stats.stress += 0.0001F * (1.0F - dist2);
                        if(chr.getZ() >= getZ() - 1.0F && dist < 14F && !((IsoZombie)(IsoZombie)chr).Ghost && chr.getCurrentSquare().getRoom() == getCurrentSquare().getRoom())
                            TicksSinceSeenZombie = 0;
                    }
                    spottedList.add(chr);
                    chr.targetAlpha = 1.0F;
                    float maxdist = 4F;
                    if(stats.NumVisibleZombies > 4)
                        maxdist = 7F;
                    if(dist < maxdist * 2.0F && (chr instanceof IsoZombie) && (int)chr.getZ() == (int)getZ())
                    {
                        GameTime.instance.setMultiplier(1.0F);
                        UIManager.getSpeedControls().SetCurrentGameSpeed(1);
                    }
                    if(dist < maxdist && (chr instanceof IsoZombie) && (int)chr.getZ() == (int)getZ() && !LastSpotted.contains(chr))
                        stats.NumVisibleZombies += 2;
                } else
                {
                    chr.targetAlpha = 1.0F;
                }
            } else
            {
                if(!(chr instanceof IsoPlayer))
                    chr.targetAlpha = 0.0F;
                if(chr.getCurrentSquare().isCouldSee())
                    TestZombieSpotPlayer(vecA, chr);
            }
            if(GhostMode)
                chr.targetAlpha = 1.0F;
        }

        if(stats.NumVisibleZombies > 0 && timeSinceLastStab <= 0)
        {
            Integer rand = Integer.valueOf(Rand.Next(3) + 1);
            timeSinceLastStab = 1800;
            SoundManager.instance.PlaySound((new StringBuilder()).append("stab").append(rand.toString()).toString(), false, 0.6F);
        }
        timeSinceLastStab--;
        float del = (float)close / 20F;
        if(del > 1.0F)
            del = 1.0F;
        del *= 0.6F;
        SoundManager.instance.BlendVolume(MainScreenState.ambient, del);
        int actualSpotted = 0;
        for(int n = 0; n < spottedList.size(); n++)
        {
            if(!LastSpotted.contains(spottedList.get(n)))
                LastSpotted.add(spottedList.get(n));
            if(spottedList.get(n) instanceof IsoZombie)
                actualSpotted++;
        }

        if(ClearSpottedTimer <= 0 && actualSpotted == 0)
        {
            LastSpotted.clear();
            ClearSpottedTimer = 1000;
        } else
        {
            ClearSpottedTimer--;
        }
    }

    void DoHotKey(int n, int key)
    {
        if(!PZConsole.instance.isVisible() && Keyboard.isKeyDown(key))
            if(Keyboard.isKeyDown(42) && Keyboard.isKeyDown(29))
            {
                UIManager.setDoMouseControls(true);
                if(leftHandItem != null)
                    MainHot[n] = leftHandItem.getType();
                else
                    MainHot[n] = null;
                if(rightHandItem != null)
                    SecHot[n] = rightHandItem.getType();
                else
                    SecHot[n] = null;
            } else
            {
                leftHandItem = inventory.FindAndReturn(MainHot[n]);
                rightHandItem = inventory.FindAndReturn(SecHot[n]);
            }
    }

    private void PressedA()
    {
        IsoObject inter = getInteract();
        if(inter != null)
        {
            if(inter instanceof IsoDoor)
                ((IsoDoor)inter).ToggleDoor(this);
            if(inter.container != null)
                inter.onMouseLeftClick(0, 0);
        }
    }

    public void TravelTo(String map, float x, float y, float z)
    {
        try
        {
            GameWindow.save(true);
        }
        catch(FileNotFoundException ex)
        {
            Logger.getLogger(IsoPlayer.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch(IOException ex)
        {
            Logger.getLogger(IsoPlayer.class.getName()).log(Level.SEVERE, null, ex);
        }
        IsoWorld.instance.moveArea(map, x, y, z);
    }

    public int getAngleCounter()
    {
        return angleCounter;
    }

    public void setAngleCounter(int angleCounter)
    {
        this.angleCounter = angleCounter;
    }

    public Vector2 getLastAngle()
    {
        return lastAngle;
    }

    public void setLastAngle(Vector2 lastAngle)
    {
        this.lastAngle = lastAngle;
    }

    public int getDialogMood()
    {
        return DialogMood;
    }

    public void setDialogMood(int DialogMood)
    {
        this.DialogMood = DialogMood;
    }

    public int getPing()
    {
        return ping;
    }

    public void setPing(int ping)
    {
        this.ping = ping;
    }

    public boolean isFakeAttack()
    {
        return FakeAttack;
    }

    public void setFakeAttack(boolean FakeAttack)
    {
        this.FakeAttack = FakeAttack;
    }

    public IsoObject getFakeAttackTarget()
    {
        return FakeAttackTarget;
    }

    public void setFakeAttackTarget(IsoObject FakeAttackTarget)
    {
        this.FakeAttackTarget = FakeAttackTarget;
    }

    public IsoMovingObject getDragObject()
    {
        return DragObject;
    }

    public void setDragObject(IsoMovingObject DragObject)
    {
        this.DragObject = DragObject;
    }

    public float getAsleepTime()
    {
        return AsleepTime;
    }

    public void setAsleepTime(float AsleepTime)
    {
        this.AsleepTime = AsleepTime;
    }

    public String[] getMainHot()
    {
        return MainHot;
    }

    public void setMainHot(String MainHot[])
    {
        this.MainHot = MainHot;
    }

    public String[] getSecHot()
    {
        return SecHot;
    }

    public void setSecHot(String SecHot[])
    {
        this.SecHot = SecHot;
    }

    public Stack getSpottedList()
    {
        return spottedList;
    }

    public void setSpottedList(Stack spottedList)
    {
        this.spottedList = spottedList;
    }

    public int getTicksSinceSeenZombie()
    {
        return TicksSinceSeenZombie;
    }

    public void setTicksSinceSeenZombie(int TicksSinceSeenZombie)
    {
        this.TicksSinceSeenZombie = TicksSinceSeenZombie;
    }

    public boolean isWaiting()
    {
        return Waiting;
    }

    public void setWaiting(boolean Waiting)
    {
        this.Waiting = Waiting;
    }

    public IsoSurvivor getDragCharacter()
    {
        return DragCharacter;
    }

    public void setDragCharacter(IsoSurvivor DragCharacter)
    {
        this.DragCharacter = DragCharacter;
    }

    public Stack getLastPos()
    {
        return lastPos;
    }

    public void setLastPos(Stack lastPos)
    {
        this.lastPos = lastPos;
    }

    public boolean isbDebounceLMB()
    {
        return bDebounceLMB;
    }

    public void setbDebounceLMB(boolean bDebounceLMB)
    {
        this.bDebounceLMB = bDebounceLMB;
    }

    public int getHeartDelay()
    {
        return heartDelay;
    }

    public void setHeartDelay(int heartDelay)
    {
        this.heartDelay = heartDelay;
    }

    public int getHeartDelayMax()
    {
        return heartDelayMax;
    }

    public void setHeartDelayMax(int heartDelayMax)
    {
        this.heartDelayMax = heartDelayMax;
    }

    public float getDrunkOscilatorStepSin()
    {
        return DrunkOscilatorStepSin;
    }

    public void setDrunkOscilatorStepSin(float DrunkOscilatorStepSin)
    {
        this.DrunkOscilatorStepSin = DrunkOscilatorStepSin;
    }

    public float getDrunkOscilatorRateSin()
    {
        return DrunkOscilatorRateSin;
    }

    public void setDrunkOscilatorRateSin(float DrunkOscilatorRateSin)
    {
        this.DrunkOscilatorRateSin = DrunkOscilatorRateSin;
    }

    public float getDrunkOscilatorStepCos()
    {
        return DrunkOscilatorStepCos;
    }

    public void setDrunkOscilatorStepCos(float DrunkOscilatorStepCos)
    {
        this.DrunkOscilatorStepCos = DrunkOscilatorStepCos;
    }

    public float getDrunkOscilatorRateCos()
    {
        return DrunkOscilatorRateCos;
    }

    public void setDrunkOscilatorRateCos(float DrunkOscilatorRateCos)
    {
        this.DrunkOscilatorRateCos = DrunkOscilatorRateCos;
    }

    public float getDrunkOscilatorStepCos2()
    {
        return DrunkOscilatorStepCos2;
    }

    public void setDrunkOscilatorStepCos2(float DrunkOscilatorStepCos2)
    {
        this.DrunkOscilatorStepCos2 = DrunkOscilatorStepCos2;
    }

    public float getDrunkOscilatorRateCos2()
    {
        return DrunkOscilatorRateCos2;
    }

    public void setDrunkOscilatorRateCos2(float DrunkOscilatorRateCos2)
    {
        this.DrunkOscilatorRateCos2 = DrunkOscilatorRateCos2;
    }

    public float getDrunkSin()
    {
        return DrunkSin;
    }

    public void setDrunkSin(float DrunkSin)
    {
        this.DrunkSin = DrunkSin;
    }

    public float getDrunkCos()
    {
        return DrunkCos;
    }

    public void setDrunkCos(float DrunkCos)
    {
        this.DrunkCos = DrunkCos;
    }

    public float getDrunkCos2()
    {
        return DrunkCos2;
    }

    public void setDrunkCos2(float DrunkCos2)
    {
        this.DrunkCos2 = DrunkCos2;
    }

    public float getMinOscilatorRate()
    {
        return MinOscilatorRate;
    }

    public void setMinOscilatorRate(float MinOscilatorRate)
    {
        this.MinOscilatorRate = MinOscilatorRate;
    }

    public float getMaxOscilatorRate()
    {
        return MaxOscilatorRate;
    }

    public void setMaxOscilatorRate(float MaxOscilatorRate)
    {
        this.MaxOscilatorRate = MaxOscilatorRate;
    }

    public float getDesiredSinRate()
    {
        return DesiredSinRate;
    }

    public void setDesiredSinRate(float DesiredSinRate)
    {
        this.DesiredSinRate = DesiredSinRate;
    }

    public float getDesiredCosRate()
    {
        return DesiredCosRate;
    }

    public void setDesiredCosRate(float DesiredCosRate)
    {
        this.DesiredCosRate = DesiredCosRate;
    }

    public float getOscilatorChangeRate()
    {
        return OscilatorChangeRate;
    }

    public void setOscilatorChangeRate(float OscilatorChangeRate)
    {
        this.OscilatorChangeRate = OscilatorChangeRate;
    }

    public float getMaxWeightDelta()
    {
        return maxWeightDelta;
    }

    public void setMaxWeightDelta(float maxWeightDelta)
    {
        this.maxWeightDelta = maxWeightDelta;
    }

    public String getForname()
    {
        return Forname;
    }

    public void setForname(String Forname)
    {
        this.Forname = Forname;
    }

    public String getSurname()
    {
        return Surname;
    }

    public void setSurname(String Surname)
    {
        this.Surname = Surname;
    }

    public IsoSprite getGuardModeUISprite()
    {
        return GuardModeUISprite;
    }

    public void setGuardModeUISprite(IsoSprite GuardModeUISprite)
    {
        this.GuardModeUISprite = GuardModeUISprite;
    }

    public int getGuardModeUI()
    {
        return GuardModeUI;
    }

    public void setGuardModeUI(int GuardModeUI)
    {
        this.GuardModeUI = GuardModeUI;
    }

    public IsoSurvivor getGuardChosen()
    {
        return GuardChosen;
    }

    public void setGuardChosen(IsoSurvivor GuardChosen)
    {
        this.GuardChosen = GuardChosen;
    }

    public IsoGridSquare getGuardStand()
    {
        return GuardStand;
    }

    public void setGuardStand(IsoGridSquare GuardStand)
    {
        this.GuardStand = GuardStand;
    }

    public IsoGridSquare getGuardFace()
    {
        return GuardFace;
    }

    public void setGuardFace(IsoGridSquare GuardFace)
    {
        this.GuardFace = GuardFace;
    }

    public boolean isbSneaking()
    {
        return bSneaking;
    }

    public void setbSneaking(boolean bSneaking)
    {
        this.bSneaking = bSneaking;
    }

    public boolean isbChangeCharacterDebounce()
    {
        return bChangeCharacterDebounce;
    }

    public void setbChangeCharacterDebounce(boolean bChangeCharacterDebounce)
    {
        this.bChangeCharacterDebounce = bChangeCharacterDebounce;
    }

    public int getFollowID()
    {
        return followID;
    }

    public void setFollowID(int followID)
    {
        this.followID = followID;
    }

    public boolean isbSeenThisFrame()
    {
        return bSeenThisFrame;
    }

    public void setbSeenThisFrame(boolean bSeenThisFrame)
    {
        this.bSeenThisFrame = bSeenThisFrame;
    }

    public boolean isbCouldBeSeenThisFrame()
    {
        return bCouldBeSeenThisFrame;
    }

    public void setbCouldBeSeenThisFrame(boolean bCouldBeSeenThisFrame)
    {
        this.bCouldBeSeenThisFrame = bCouldBeSeenThisFrame;
    }

    public int getTimeSinceLastStab()
    {
        return timeSinceLastStab;
    }

    public void setTimeSinceLastStab(int timeSinceLastStab)
    {
        this.timeSinceLastStab = timeSinceLastStab;
    }

    public Stack getLastSpotted()
    {
        return LastSpotted;
    }

    public void setLastSpotted(Stack LastSpotted)
    {
        this.LastSpotted = LastSpotted;
    }

    public int getClearSpottedTimer()
    {
        return ClearSpottedTimer;
    }

    public void setClearSpottedTimer(int ClearSpottedTimer)
    {
        this.ClearSpottedTimer = ClearSpottedTimer;
    }

    public boolean IsRunning()
    {
        return bRunning;
    }

    public void InitSpriteParts()
    {
        SurvivorDesc desc = descriptor;
        InitSpriteParts(desc.legs, desc.torso, desc.head, desc.top, desc.bottoms, desc.shoes, desc.skinpal, desc.toppal, desc.bottomspal, desc.shoespal);
    }

    public boolean IsAiming()
    {
        return isAiming;
    }

    public boolean IsUsingAimWeapon()
    {
        if(leftHandItem == null)
            return false;
        if(!(leftHandItem instanceof HandWeapon))
            return false;
        if(!isAiming)
            return false;
        return ((HandWeapon)leftHandItem).bIsAimedFirearm;
    }

    private boolean IsUsingAimHandWeapon()
    {
        if(leftHandItem == null)
            return false;
        if(!(leftHandItem instanceof HandWeapon))
            return false;
        if(!isAiming)
            return false;
        return ((HandWeapon)leftHandItem).bIsAimedHandWeapon;
    }

    private boolean DoAimAnimOnAiming()
    {
        if(isCharging)
            return true;
        return IsUsingAimWeapon();
    }

    public static boolean GhostMode = false;
    public static IsoPlayer instance;
    protected static Vector2 playerMoveDir = new Vector2(0.0F, 0.0F);
    protected static boolean isAiming = false;
    protected static Stack StaticTraits = new Stack();
    protected int angleCounter;
    public Vector2 lastAngle;
    protected int DialogMood;
    protected int ping;
    protected boolean FakeAttack;
    protected IsoObject FakeAttackTarget;
    protected IsoMovingObject DragObject;
    protected float AsleepTime;
    protected String MainHot[];
    protected String SecHot[];
    protected Stack spottedList;
    protected int TicksSinceSeenZombie;
    protected boolean Waiting;
    protected IsoSurvivor DragCharacter;
    protected Stack lastPos;
    protected boolean bDebounceLMB;
    protected int heartDelay;
    protected int heartDelayMax;
    protected float DrunkOscilatorStepSin;
    protected float DrunkOscilatorRateSin;
    protected float DrunkOscilatorStepCos;
    protected float DrunkOscilatorRateCos;
    protected float DrunkOscilatorStepCos2;
    protected float DrunkOscilatorRateCos2;
    protected float DrunkSin;
    protected float DrunkCos;
    protected float DrunkCos2;
    protected float MinOscilatorRate;
    protected float MaxOscilatorRate;
    protected float DesiredSinRate;
    protected float DesiredCosRate;
    protected float OscilatorChangeRate;
    protected float maxWeightDelta;
    protected String Forname;
    protected String Surname;
    public float TargetSpeed;
    public float CurrentSpeed;
    public float MaxSpeed;
    public float SpeedChange;
    protected IsoSprite GuardModeUISprite;
    protected int GuardModeUI;
    protected IsoSurvivor GuardChosen;
    protected IsoGridSquare GuardStand;
    protected IsoGridSquare GuardFace;
    public boolean isCharging;
    protected boolean bSneaking;
    protected boolean bRunning;
    protected boolean bWasRunning;
    public boolean bRightClickMove;
    protected boolean bChangeCharacterDebounce;
    protected Vector2 runAngle;
    protected int followID;
    protected Stack FollowCamStack;
    public static boolean DemoMode = false;
    protected static int FollowDeadCount = 240;
    protected boolean bSeenThisFrame;
    protected boolean bCouldBeSeenThisFrame;
    public int TimeSprinting;
    public int TimeSinceRightClick;
    public int TimeRightClicking;
    public int LastTimeRightClicking;
    public boolean JustMoved;
    public boolean bDoubleClick;
    public float AimRadius;
    public float DesiredAimRadius;
    static int lmx = -1;
    static int lmy = -1;
    public float EffectiveAimDistance;
    public float chargeTime;
    public float useChargeTime;
    public float useChargeDelta;
    protected int timeSinceLastStab;
    protected Stack LastSpotted;
    protected int ClearSpottedTimer;

}
