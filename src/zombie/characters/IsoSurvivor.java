// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   IsoSurvivor.java

package zombie.characters;

import gnu.trove.map.hash.THashMap;
import java.io.*;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.util.Stack;
import java.util.Vector;
import zombie.*;
import zombie.Lua.LuaEventManager;
import zombie.WorldSoundManager;
import zombie.ai.StateMachine;
import zombie.ai.astar.AStarPathFinderResult;
import zombie.ai.states.*;
import zombie.behaviors.BehaviorHub;
import zombie.behaviors.SequenceBehavior;
import zombie.behaviors.survivor.MasterSurvivorBehavior;
import zombie.behaviors.survivor.orders.*;
import zombie.behaviors.survivor.orders.Needs.*;
import zombie.behaviors.survivor.orders.Order;
import zombie.characters.BodyDamage.BodyDamage;
import zombie.characters.Moodles.MoodleType;
import zombie.characters.Moodles.Moodles;
import zombie.core.Collections.NulledArrayList;
import zombie.core.*;
import zombie.core.utils.OnceEvery;
import zombie.inventory.InventoryItem;
import zombie.inventory.ItemContainer;
import zombie.inventory.types.HandWeapon;
import zombie.iso.*;
import zombie.iso.areas.IsoBuilding;
import zombie.iso.areas.IsoRoom;
import zombie.iso.objects.IsoDoor;
import zombie.iso.sprite.*;
import zombie.scripting.ScriptManager;
import zombie.ui.*;

// Referenced classes of package zombie.characters:
//            IsoLivingCharacter, IsoGameCharacter, SurvivorDesc, IsoZombie, 
//            IsoPlayer, SurvivorPersonality, SurvivorGroup

public class IsoSurvivor extends IsoLivingCharacter
{
	/*      */   public static enum SatisfiedBy
	/*      */   {
	/* 1578 */     Food, 
	/* 1579 */     Weapons, 
	/* 1580 */     Water;
	/*      */   }


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

    public String getObjectName()
    {
        return "Survivor";
    }

    public IsoSurvivor(IsoCell cell)
    {
        super(cell, 0.0F, 0.0F, 0.0F);
        NoGoreDeath = false;
        behaviours = new BehaviorHub();
        Draggable = false;
        following = null;
        repathDelay = 0;
        nightsSurvived = 0;
        ping = 0;
        bLastSpottedPlayer = false;
        bSpottedPlayer = false;
        bWillJoinPlayer = false;
        HasBeenDragged = false;
        ClosestTwoSurvivors = new IsoGameCharacter[2];
        NeightbourUpdate = 20;
        NeightbourUpdateMax = 20;
        lmove = new Vector2(0.0F, 0.0F);
        LastLocalNeutralList = new NulledArrayList();
        LOSUpdate = new OnceEvery(0.4F, true);
        dangerTile = 0;
        lastDangerTile = 0;
        aimAt = null;
        availableTemp = new Stack();
        bCollidedWithPushable = false;
        OutlineOnMouseover = true;
        getCell().getSurvivorList().add(this);
    }

    public IsoSurvivor(IsoCell cell, int x, int y, int z)
    {
        super(cell, x, y, z);
        NoGoreDeath = false;
        behaviours = new BehaviorHub();
        Draggable = false;
        following = null;
        repathDelay = 0;
        nightsSurvived = 0;
        ping = 0;
        bLastSpottedPlayer = false;
        bSpottedPlayer = false;
        bWillJoinPlayer = false;
        HasBeenDragged = false;
        ClosestTwoSurvivors = new IsoGameCharacter[2];
        NeightbourUpdate = 20;
        NeightbourUpdateMax = 20;
        lmove = new Vector2(0.0F, 0.0F);
        LastLocalNeutralList = new NulledArrayList();
        LOSUpdate = new OnceEvery(0.4F, true);
        dangerTile = 0;
        lastDangerTile = 0;
        aimAt = null;
        availableTemp = new Stack();
        bCollidedWithPushable = false;
        getCell().getSurvivorList().add(this);
        OutlineOnMouseover = true;
        descriptor = new SurvivorDesc();
        PathSpeed = 0.06F;
        NeightbourUpdate = Rand.Next(NeightbourUpdateMax);
        sprite.LoadFramesPcx("Wife", "death", 1);
        sprite.LoadFramesPcx("Wife", "dragged", 1);
        sprite.LoadFramesPcx("Wife", "asleep_normal", 1);
        sprite.LoadFramesPcx("Wife", "asleep_bandaged", 1);
        sprite.LoadFramesPcx("Wife", "asleep_bleeding", 1);
        solid = false;
        IgnoreStaggerBack = true;
        SpeakColour = new Color(204, 100, 100);
        dir = IsoDirections.S;
        OutlineOnMouseover = true;
        finder.maxSearchDistance = 120;
        CreateBehaviors();
    }

    public IsoSurvivor(SurvivorDesc desc, IsoCell cell, int x, int y, int z)
    {
        super(cell, x, y, z);
        NoGoreDeath = false;
        behaviours = new BehaviorHub();
        Draggable = false;
        following = null;
        repathDelay = 0;
        nightsSurvived = 0;
        ping = 0;
        bLastSpottedPlayer = false;
        bSpottedPlayer = false;
        bWillJoinPlayer = false;
        HasBeenDragged = false;
        ClosestTwoSurvivors = new IsoGameCharacter[2];
        NeightbourUpdate = 20;
        NeightbourUpdateMax = 20;
        lmove = new Vector2(0.0F, 0.0F);
        LastLocalNeutralList = new NulledArrayList();
        LOSUpdate = new OnceEvery(0.4F, true);
        dangerTile = 0;
        lastDangerTile = 0;
        aimAt = null;
        availableTemp = new Stack();
        bCollidedWithPushable = false;
        descriptor = desc;
        PathSpeed = 0.06F;
        getCell().getSurvivorList().add(this);
        InitSpriteParts(desc.legs, desc.torso, desc.head, desc.top, desc.bottoms, desc.shoes, desc.skinpal, desc.toppal, desc.bottomspal, desc.shoespal);
        SpeakColour = new Color(Rand.Next(200) + 55, Rand.Next(200) + 55, Rand.Next(200) + 55, 255);
        finder.maxSearchDistance = 120;
        desc.forename = "Earl";
        inventory.AddItem("BaseballBat");
        inventory.AddItem("Shotgun");
        inventory.AddItem("ShotgunShells");
        inventory.AddItem("ShotgunShells");
        NeightbourUpdate = Rand.Next(NeightbourUpdateMax);
        OutlineOnMouseover = true;
    }

    public IsoSurvivor(SurvivorPersonality.Personality personality, SurvivorDesc desc, IsoCell cell, int x, int y, int z)
    {
        super(cell, x, y, z);
        NoGoreDeath = false;
        behaviours = new BehaviorHub();
        Draggable = false;
        following = null;
        repathDelay = 0;
        nightsSurvived = 0;
        ping = 0;
        bLastSpottedPlayer = false;
        bSpottedPlayer = false;
        bWillJoinPlayer = false;
        HasBeenDragged = false;
        ClosestTwoSurvivors = new IsoGameCharacter[2];
        NeightbourUpdate = 20;
        NeightbourUpdateMax = 20;
        lmove = new Vector2(0.0F, 0.0F);
        LastLocalNeutralList = new NulledArrayList();
        LOSUpdate = new OnceEvery(0.4F, true);
        dangerTile = 0;
        lastDangerTile = 0;
        aimAt = null;
        availableTemp = new Stack();
        bCollidedWithPushable = false;
        getCell().getSurvivorList().add(this);
        if(personality == SurvivorPersonality.Personality.Kate)
        {
            OutlineOnMouseover = true;
            sprite.LoadFramesPcx("Wife", "death", 1);
            sprite.LoadFramesPcx("Wife", "dragged", 1);
            sprite.LoadFramesPcx("Wife", "asleep_normal", 1);
            sprite.LoadFramesPcx("Wife", "asleep_bandaged", 1);
            sprite.LoadFramesPcx("Wife", "asleep_bleeding", 1);
            solid = false;
            IgnoreStaggerBack = true;
            SpeakColour = new Color(204, 100, 100);
            dir = IsoDirections.S;
            descriptor = desc;
            PathSpeed = 0.06F;
            finder.maxSearchDistance = 120;
            CreateBehaviors();
            bOnBed = true;
            offsetY += 5F;
            offsetX -= 21F;
            ApplyInBedOffset(true);
            NeightbourUpdate = Rand.Next(NeightbourUpdateMax);
            inflictWound(IsoGameCharacter.BodyLocation.Leg, 1.0F, false, 1.0F);
            OutlineOnMouseover = true;
            return;
        } else
        {
            OutlineOnMouseover = true;
            descriptor = desc;
            PathSpeed = 0.06F;
            String str = "Zombie_palette";
            str = (new StringBuilder()).append(str).append("01").toString();
            InitSpriteParts(desc.legs, desc.torso, desc.head, desc.top, desc.bottoms, desc.shoes, desc.skinpal, desc.toppal, desc.bottomspal, desc.shoespal);
            SpeakColour = new Color(Rand.Next(200) + 55, Rand.Next(200) + 55, Rand.Next(200) + 55, 255);
            finder.maxSearchDistance = 120;
            NeightbourUpdate = Rand.Next(NeightbourUpdateMax);
            Personality = SurvivorPersonality.CreatePersonality(personality);
            CreateBehaviors();
            Dressup(desc);
            return;
        }
    }

    public void load(ByteBuffer input)
        throws IOException
    {
        super.load(input);
        SurvivorDesc desc = descriptor;
        Personality = SurvivorPersonality.CreatePersonality(SurvivorPersonality.Personality.GunNut);
        if(Personality.type == SurvivorPersonality.Personality.Kate)
        {
            OutlineOnMouseover = true;
            sprite.LoadFramesPcx("Wife", "death", 1);
            sprite.LoadFramesPcx("Wife", "dragged", 1);
            sprite.LoadFramesPcx("Wife", "asleep_normal", 1);
            sprite.LoadFramesPcx("Wife", "asleep_bandaged", 1);
            sprite.LoadFramesPcx("Wife", "asleep_bleeding", 1);
            setSolid(false);
            IgnoreStaggerBack = true;
            SpeakColour = new Color(204, 100, 100);
            dir = IsoDirections.S;
            descriptor = desc;
            PathSpeed = 0.06F;
            finder.maxSearchDistance = 120;
            CreateBehaviors();
            bOnBed = true;
            offsetY += 5F;
            offsetX -= 21F;
            ApplyInBedOffset(true);
            NeightbourUpdate = Rand.Next(NeightbourUpdateMax);
            return;
        } else
        {
            InitSpriteParts(desc.legs, desc.torso, desc.head, desc.top, desc.bottoms, desc.shoes, desc.skinpal, desc.toppal, desc.bottomspal, desc.shoespal);
            CreateBehaviors();
            SpeakColour = new Color(Rand.Next(200) + 55, Rand.Next(200) + 55, Rand.Next(200) + 55, 255);
            finder.maxSearchDistance = 120;
            PathSpeed = 0.06F;
            return;
        }
    }

    public void DoTooltip(ObjectTooltip tooltipUI)
    {
        int mid = 60;
        String name = "";
        name = (new StringBuilder()).append(descriptor.forename).append(" ").append(descriptor.surname).toString();
        int y = 5;
        tooltipUI.DrawText(name, 5, y, 1.0F, 1.0F, 0.8F, 1.0F);
        for(int n = 0; n < wounds.size(); n++)
        {
            IsoGameCharacter.Wound wound = (IsoGameCharacter.Wound)wounds.get(n);
            y += 25;
            tooltipUI.DrawText("Broken Leg", 5, y, 0.5F, 0.5F, 0.0F, 1.0F);
            y += 14;
            if(wound.tourniquet)
            {
                tooltipUI.DrawText("  Stemmed", 5, y, 0.0F, 1.0F, 0.0F, 1.0F);
                y += 14;
            }
            if(wound.bandaged)
            {
                tooltipUI.DrawText("  Bandaged", 5, y, 0.0F, 1.0F, 0.0F, 1.0F);
                y += 14;
            }
            if(wound.bleeding > 0.5F)
            {
                tooltipUI.DrawText("  Bleeding Badly", 5, y, 1.0F, 0.0F, 0.0F, 1.0F);
                continue;
            }
            if(wound.bleeding > 0.0F)
                tooltipUI.DrawText("  Bleeding", 5, y, 0.5F, 0.5F, 0.0F, 1.0F);
        }

        tooltipUI.setHeight(y + 32);
    }

    public boolean HasTooltip()
    {
        return true;
    }

    public void spotted(IsoMovingObject other)
    {
        if(other == IsoPlayer.instance)
        {
            ScriptManager.instance.Trigger("OnSpotPlayer", getScriptName());
            LastKnownLocation.put("Player", new IsoGameCharacter.Location((int)other.getX(), (int)other.getY(), (int)other.getZ()));
            bSpottedPlayer = true;
            
            if( getZ() == IsoPlayer.instance.getZ() && IsoUtils.DistanceManhatten(getX(), getY(), IsoPlayer.instance.getX(), IsoPlayer.instance.getY()) < 8F && getCurrentSquare().getRoom() == IsoPlayer.instance.getCurrentSquare().getRoom())
                Meet(IsoPlayer.instance);
        }
        
        
        for(int n = 0; n < EnemyList.size(); n++)
            if(((IsoGameCharacter)EnemyList.get(n)).descriptor.InGroupWith(this))
            {
                EnemyList.remove(n);
                n--;
            }

    }

    public boolean onMouseLeftClick(int x, int y)
    {
        if(IsoPlayer.isAiming)
            return false;
        if(IsoCamera.CamCharacter != IsoPlayer.instance && Core.bDebug)
            IsoCamera.CamCharacter = this;
        if(this != TutorialManager.instance.wife && UIManager.getDragInventory() == null)
            UIManager.LaunchRadial(this);
        if(IsoPlayer.instance.getCurrentSquare().getRoom() == getCurrentSquare().getRoom() && IsoPlayer.instance.DistTo(this) < 4F && UIManager.getDragInventory() != null)
            UIManager.getDragInventory().Use(this);
        if(Draggable && this == TutorialManager.instance.wife && IsoUtils.DistanceTo(IsoPlayer.instance.getX(), IsoPlayer.instance.getY(), getX(), getY()) < 2.0F)
        {
            Draggable = true;
            Dragging = !Dragging;
            if(Dragging)
            {
                IsoPlayer.instance.DragCharacter = this;
                sprite.PlayAnim("dragged");
                ApplyInBedOffset(false);
            } else
            {
                IsoPlayer.instance.DragCharacter = null;
            }
        }
        if(IsoPlayer.instance.Health <= 0.0F || IsoPlayer.instance.BodyDamage.getHealth() <= 0.0F)
            Dragging = false;
        return true;
    }

    public void update()
    {
        bCollidedWithPushable = false;
        if(getCurrentSquare() == null)
            ensureOnTile();
        LuaEventManager.TriggerEvent("OnNPCSurvivorUpdate", this);
        lastDangerTile = dangerTile;
        if(LOSUpdate.Check())
            LOSThread.instance.AddJob(this);
        if(getLastSquare() != getCurrentSquare() && getCurrentSquare() != null && getLastSquare() != null)
        {
            IsoDoor door = getCurrentSquare().getDoorFrameTo(getLastSquare());
            if(door != null && door.open && RemoteID == -1)
                door.ToggleDoor(this);
        }
        int n;
        if(IsoCamera.CamCharacter == this)
            n = 0;
        if(descriptor.Instance == null)
            descriptor.Instance = this;
        leftHandItem = inventory.getBestWeapon(descriptor);
        if(Health <= 0.0F || BodyDamage.getHealth() <= 0.0F)
        {
            stateMachine.changeState(DieState.instance());
            stateMachine.Lock = true;
            super.update();
            return;
        }
        if(descriptor.Group.Safehouse != null && !descriptor.IsLeader() && RemoteID == -1 && Order == null && !IsInBuilding(descriptor.Group.Safehouse))
            GiveOrder(new GotoBuildingOrder(this, descriptor.Group.Safehouse), false);
        if(descriptor.IsLeader() && RemoteID == -1)
            if(descriptor.Group.Safehouse == null && IsoWorld.instance.CurrentCell.getBuildingList().size() > 5)
            {
                IsoBuilding b;
                for(b = null; b == null || b.Exits.isEmpty() || b.Rooms.size() <= 1; b = (IsoBuilding)IsoWorld.instance.CurrentCell.getBuildingList().get(Rand.Next(IsoWorld.instance.CurrentCell.getBuildingList().size())));
                IsoBuilding best = b;
                if(best != null)
                {
                    descriptor.Group.Safehouse = best;
                    GiveOrder(new FindSafehouse(this), true);
                }
            } else
            {
                if(descriptor.Group.Safehouse != null && !descriptor.Group.Safehouse.Exits.isEmpty() && Order == null && !IsInBuilding(descriptor.Group.Safehouse) && Rand.Next(800) == 0)
                    GiveOrder(new GotoBuildingOrder(this, descriptor.Group.Safehouse), true);
                if(descriptor.Group.Safehouse != null && Order == null && (IsoPlayer.DemoMode || descriptor.Group.getTotalNeedPriority() > 50 && descriptor.Group.Safehouse.safety >= 0 && Rand.Next(1000) == 0 && GameTime.getInstance().getTimeOfDay() > 8F && GameTime.getInstance().getTimeOfDay() < 13F))
                    GiveOrder(new LootMission(this), true);
            }
        if((BodyDamage.getNumPartsScratched() > 0 || BodyDamage.getNumPartsBitten() > 0) && !HasPersonalNeed("StopBleeding") && !IsoPlayer.DemoMode)
            PersonalNeeds.add(new StopBleeding(this));
        if((Moodles.getMoodleLevel(MoodleType.Hungry) > 1 || BodyDamage.getHealth() < 100F && Moodles.getMoodleLevel(MoodleType.FoodEaten) < 2) && !HasPersonalNeed("Heal"))
            PersonalNeeds.add(new Heal(this));
        if(Moodles.getMoodleLevel(MoodleType.Thirst) > 0 && !HasPersonalNeed("DrinkWater"))
            PersonalNeeds.add(new DrinkWater(this));
        DoRandomTalk();
        int range = 10;
        stats.fatigue = 0.0F;
        NeightbourUpdate--;
        int clsest = 0;
        stats.stress += 1E-006F * (float)LocalRelevantEnemyList.size();
        if(NeightbourUpdate <= 0)
        {
            if(IsoPlayer.DemoMode)
                WorldSoundManager.instance.addSound(this, (int)getX(), (int)getY(), (int)getZ(), 90, 90);
            LastLocalNeutralList.clear();
            LastLocalNeutralList.addAll(LocalNeutralList);
            LocalNeutralList.clear();
            NeightbourUpdate = NeightbourUpdateMax;
            VeryCloseEnemyList.clear();
            ClosestTwoSurvivors[0] = null;
            ClosestTwoSurvivors[1] = null;
            LastLocalEnemies = LocalEnemyList.size();
            LocalEnemyList.clear();
            LocalRelevantEnemyList.clear();
            dangerLevels = 0.0F;
            synchronized(LocalList)
            {
                for(n = 0; n < LocalList.size(); n++)
                {
                    IsoMovingObject mov = (IsoMovingObject)LocalList.get(n);
                    if(mov == this || !(mov instanceof IsoGameCharacter) || (mov instanceof IsoZombie) && ((IsoZombie)mov).Ghost || (mov instanceof IsoGameCharacter) && !((IsoGameCharacter)mov).VisibleToNPCs || mov.getCurrentSquare() == null)
                        continue;
                    int x = (int)(getX() - mov.getX());
                    int y = (int)(getY() - mov.getY());
                    int xdist = Math.abs(x);
                    int ydist = Math.abs(y);
                    if(xdist < 1)
                        xdist = 1;
                    if(ydist < 1)
                        ydist = 1;
                    if((mov instanceof IsoZombie) && mov.getCurrentSquare() != null && mov.getCurrentSquare().getRoom() == getCurrentSquare().getRoom() && mov.getZ() == getZ())
                    {
                        float xdanger = 5F / (float)xdist;
                        float ydanger = 5F / (float)ydist;
                        dangerLevels += xdanger + ydanger;
                    }
                    if(xdist < 8 && ydist < 8 && mov.getCurrentSquare().getRoom() == getCurrentSquare().getRoom())
                    {
                        if((mov instanceof IsoSurvivor) && !LastLocalNeutralList.contains(mov))
                            Meet((IsoSurvivor)mov);
                        if((mov instanceof IsoSurvivor) || (mov instanceof IsoPlayer))
                            if(ClosestTwoSurvivors[0] == null)
                                ClosestTwoSurvivors[0] = (IsoGameCharacter)mov;
                            else
                            if(ClosestTwoSurvivors[1] == null)
                                ClosestTwoSurvivors[1] = (IsoGameCharacter)mov;
                        if(xdist < 3 && ydist < 3 && getZ() == mov.getZ() && mov.getCurrentSquare().getRoom() == getCurrentSquare().getRoom() && (mov instanceof IsoZombie))
                            VeryCloseEnemyList.add(mov);
                    }
                    if(mov.getCurrentSquare() != null && mov.getCurrentSquare().getRoom() == getCurrentSquare().getRoom() && mov.getCurrentSquare() != null && getCurrentSquare() != null && !(mov instanceof IsoZombie))
                        if(!EnemyList.contains((IsoGameCharacter)mov));
                    if(!(this instanceof IsoGameCharacter))
                        continue;
                    if((mov instanceof IsoZombie) || EnemyList.contains((IsoGameCharacter)mov))
                    {
                        if(!(mov instanceof IsoZombie) || !((IsoZombie)mov).Ghost)
                        {
                            LocalRelevantEnemyList.add((IsoGameCharacter)mov);
                            LocalEnemyList.add((IsoGameCharacter)mov);
                            mov.spotted(this);
                        }
                        continue;
                    }
                    if(descriptor.Group == ((IsoGameCharacter)mov).descriptor.Group)
                        LocalGroupList.add((IsoGameCharacter)mov);
                    LocalNeutralList.add((IsoGameCharacter)mov);
                }

            }
            if(LastLocalEnemies >= LocalEnemyList.size());
        }
        if(!getAllowBehaviours())
        {
            setNx(getScriptnx());
            setNy(getScriptny());
        }
        if(getTimeSinceZombieAttack() == 1)
            masterBehaviorList.reset();
        super.update();
        if(stateMachine.getCurrent() == StaggerBackState.instance() || stateMachine.getCurrent() == StaggerBackDieState.instance() || stateMachine.getCurrent() == ReanimateState.instance())
            return;
        if(behaviours != null)
        {
            behaviours.SetTriggerValue("Hunger", stats.hunger);
            behaviours.SetTriggerValue("IdleBoredom", stats.idleboredom);
        }
        Vector2 move = new Vector2(getNx() - getLx(), getNy() - getLy());
        if((Health <= 0.0F || BodyDamage.getHealth() < 0.0F) && this == TutorialManager.instance.wife && !NoGoreDeath)
            PlayAnim("death");
        move.x *= getGlobalMovementMod();
        move.y *= getGlobalMovementMod();
        if(Dragging)
        {
            HasBeenDragged = true;
            if(IsoPlayer.instance.dir == IsoDirections.N || IsoPlayer.instance.dir == IsoDirections.S || IsoPlayer.instance.dir == IsoDirections.E || IsoPlayer.instance.dir == IsoDirections.W)
                dir = IsoPlayer.instance.dir;
        }
        if(HasBeenDragged)
            sprite.PlayAnim("dragged");
        if(this == TutorialManager.instance.wife)
            return;
        if(RemoteID == -1 && !isDead())
            if(move.getLength() > 0.0F)
            {
                if(move.getLength() > 0.075F && lmove.getLength() > 0.075F)
                    PlayAnimNoReset("Run");
                else
                    PlayAnimNoReset("Walk");
            } else
            if(lmove.getLength() == 0.0F)
                if(legsSprite != null && !legsSprite.CurrentAnim.name.contains("Attack_"))
                {
                    if(aimAt == null)
                    {
                        PlayAnim("Idle");
                    } else
                    {
                        InventoryItem attackItem = leftHandItem;
                        if((attackItem instanceof HandWeapon) && attackItem.getSwingAnim() != null)
                        {
                            useHandWeapon = (HandWeapon)attackItem;
                            PlayAnimFrame((new StringBuilder()).append("Attack_").append(attackItem.getSwingAnim()).toString(), 0);
                        } else
                        {
                            PlayAnim("Idle");
                        }
                    }
                } else
                if(aimAt != null)
                {
                    InventoryItem attackItem = leftHandItem;
                    if((attackItem instanceof HandWeapon) && attackItem.getSwingAnim() != null)
                    {
                        useHandWeapon = (HandWeapon)attackItem;
                        PlayAnimFrame((new StringBuilder()).append("Attack_").append(attackItem.getSwingAnim()).toString(), 0);
                    } else
                    {
                        PlayAnim("Idle");
                    }
                    Vector2 oPos = new Vector2(getX(), getY());
                    Vector2 tPos = new Vector2(aimAt.getX(), aimAt.getY());
                    tPos.x -= oPos.x;
                    tPos.y -= oPos.y;
                    tPos.normalize();
                    DirectionFromVector(tPos);
                    angle.x = tPos.x;
                    angle.y = tPos.y;
                    if(aimAt.Health <= 0.0F || aimAt.BodyDamage.getHealth() <= 0.0F)
                        aimAt = null;
                }
        seperate();
        lmove.x = move.x;
        lmove.y = move.y;
        repathDelay--;
    }

    public void SetAllFrames(short Frame)
    {
        def.Frame = Frame;
    }

    public void renderlast()
    {
        super.renderlast();
        if(IsoCamera.CamCharacter == this)
        {
            IndieGL.End();
            int y = 50;
            y += 20;
            masterProper.renderDebug(y);
        }
    }

    private void CreateBehaviors()
    {
        if(Personality != null)
            Personality.CreateBehaviours(this);
    }

    protected void OnDeath()
    {
        if(this == TutorialManager.instance.wife && !NoGoreDeath)
            PlayAnimUnlooped("death");
    }

    public void Aim(IsoGameCharacter other)
    {
        aimAt = other;
    }

    private void Meet(IsoGameCharacter survivor)
    {
        if(RemoteID != -1)
            return;
        if(survivor.getCurrentSquare().getRoom() != getCurrentSquare().getRoom())
            return;
        descriptor.meet(survivor.descriptor);
        if(!MeetList.contains(Integer.valueOf(survivor.descriptor.ID)))
        {
            if(!survivor.getActiveInInstances().isEmpty() || !getActiveInInstances().isEmpty())
                return;
            if(survivor.Speaking || Speaking)
                return;
            MeetList.add(Integer.valueOf(survivor.descriptor.ID));
            survivor.MeetList.add(Integer.valueOf(descriptor.ID));
            if(survivor.descriptor.Group != descriptor.Group && !(survivor instanceof IsoPlayer))
                MeetFirstTime(survivor);
            LuaEventManager.TriggerEvent("OnCharacterMeet", this, survivor, Integer.valueOf(0));
        } else
        {
            if(!survivor.getActiveInInstances().isEmpty() || !getActiveInInstances().isEmpty())
                return;
            if(survivor.Speaking || Speaking)
                return;
            LuaEventManager.TriggerEvent("OnCharacterMeet", this, survivor, Integer.valueOf(((Integer)descriptor.MetCount.get(Integer.valueOf(survivor.descriptor.ID))).intValue() - 1));
        }
    }

    private void MeetAgain(IsoGameCharacter survivor)
    {
        if(survivor.BodyDamage.getNumPartsBitten() > 0 || survivor.BodyDamage.getNumPartsScratched() > 0)
        {
            if(BodyDamage.getNumPartsBitten() <= 0 && BodyDamage.getNumPartsScratched() <= 0)
            {
                Integer count = Integer.valueOf(ScriptManager.instance.getFlagIntValue("Base.YouBeenBitCount"));
                if(!IsoPlayer.GhostMode);
                ScriptManager.instance.PlayInstanceScript(null, (new StringBuilder()).append("Base.YouBeenBit").append(Rand.Next(count.intValue()) + 1).toString(), "Bitten", survivor, "Other", this);
            }
        } else
        {
            String thing = null;
            thing = "Base.MeetAgain";
            IsoGameCharacter a = null;
            IsoGameCharacter b = null;
            if(survivor instanceof IsoSurvivor)
            {
                if(Rand.Next(2) == 0)
                {
                    a = this;
                    b = survivor;
                } else
                {
                    a = survivor;
                    b = this;
                }
            } else
            {
                a = this;
                b = survivor;
            }
            if(!IsoPlayer.GhostMode);
            Integer count = Integer.valueOf(ScriptManager.instance.getFlagIntValue((new StringBuilder()).append(thing).append("Count").toString()));
            ScriptManager.instance.PlayInstanceScript(null, (new StringBuilder()).append(thing).append(Rand.Next(count.intValue()) + 1).toString(), "Met", b, "Other", a);
        }
    }

    public void FollowMe(IsoGameCharacter leader)
    {
        String thing = null;
        thing = "Base.FollowMe";
        IsoGameCharacter a = leader;
        IsoGameCharacter b = this;
        ScriptManager.instance.PlayInstanceScript(null, thing, "Follower", b, "Leader", a);
    }

    public void StayHere(IsoGameCharacter leader)
    {
        String thing = null;
        thing = "Base.StayHere";
        IsoGameCharacter a = leader;
        IsoGameCharacter b = this;
        ScriptManager.instance.PlayInstanceScript(null, thing, "Follower", b, "Leader", a);
    }

    public void Guard(IsoPlayer leader)
    {
        leader.GuardModeUI = 1;
        leader.GuardChosen = this;
        String thing = null;
        thing = "Base.GuardA";
        IsoGameCharacter a = leader;
        IsoGameCharacter b = this;
        ScriptManager.instance.PlayInstanceScript(null, thing, "Follower", b, "Leader", a);
    }

    public void DoGuard(IsoPlayer leader)
    {
        String thing = null;
        thing = "Base.GuardB";
        IsoGameCharacter a = leader;
        IsoGameCharacter b = this;
        ScriptManager.instance.PlayInstanceScript(null, thing, "Follower", b, "Leader", a);
        Orders.push(new GuardOrder(this, leader.GuardStand, leader.GuardFace));
    }

    public void MeetFirstTime(IsoGameCharacter survivor, boolean reverseRoles, boolean Checkbitten)
    {
        if(Checkbitten && (survivor.BodyDamage.getNumPartsBitten() > 0 || survivor.BodyDamage.getNumPartsScratched() > 0))
        {
            if(BodyDamage.getNumPartsBitten() <= 0 && BodyDamage.getNumPartsScratched() <= 0)
            {
                Integer count = Integer.valueOf(ScriptManager.instance.getFlagIntValue("Base.YouBeenBitCount"));
                ScriptManager.instance.PlayInstanceScript(null, (new StringBuilder()).append("Base.YouBeenBit").append(Rand.Next(count.intValue()) + 1).toString(), "Bitten", survivor, "Other", this);
            }
        } else
        {
            String thing = null;
            thing = "Base.FirstMeet";
            IsoGameCharacter a = null;
            IsoGameCharacter b = null;
            if(survivor instanceof IsoSurvivor)
            {
                if(Rand.Next(2) == 0)
                {
                    a = this;
                    b = survivor;
                } else
                {
                    a = survivor;
                    b = this;
                }
            } else
            if(reverseRoles)
            {
                b = this;
                a = survivor;
            } else
            {
                a = this;
                b = survivor;
            }
            if(!IsoPlayer.GhostMode);
            Integer count = Integer.valueOf(ScriptManager.instance.getFlagIntValue((new StringBuilder()).append(thing).append("Count").toString()));
            ScriptManager.instance.PlayInstanceScript(null, (new StringBuilder()).append(thing).append(Rand.Next(count.intValue()) + 1).toString(), "Met", b, "Other", a);
        }
    }

    public void MeetFirstTime(IsoGameCharacter survivor)
    {
        MeetFirstTime(survivor, false, true);
    }

    public void Killed(IsoGameCharacter gameCharacter)
    {
        if(Speaking)
            return;
        if(!getActiveInInstances().isEmpty())
            return;
        if(Rand.Next(30) != 0)
            return;
        IsoGameCharacter a = this;
        IsoGameCharacter b = ClosestTwoSurvivors[0];
        IsoGameCharacter c = ClosestTwoSurvivors[1];
        Integer n = Integer.valueOf(3);
        if(c == null || !c.getActiveInInstances().isEmpty() || c.DistTo(this) > 8F)
        {
            c = null;
            Integer integer = n;
            Integer integer3 = n = Integer.valueOf(n.intValue() - 1);
            Integer _tmp = integer;
        }
        if(b != null && (!b.getActiveInInstances().isEmpty() || b.DistTo(this) > 8F))
        {
            b = c;
            c = null;
            Integer integer1 = n;
            Integer integer4 = n = Integer.valueOf(n.intValue() - 1);
            Integer _tmp1 = integer1;
        } else
        if(b == null)
        {
            Integer integer2 = n;
            Integer integer5 = n = Integer.valueOf(n.intValue() - 1);
            Integer _tmp2 = integer2;
        }
        String thing = "Base.Killed";
        n = Integer.valueOf(Rand.Next(n.intValue()) + 1);
        if(gameCharacter instanceof IsoZombie)
            thing = (new StringBuilder()).append(thing).append("Zombie_").toString();
        else
            thing = (new StringBuilder()).append(thing).append("Survivor_").toString();
        thing = (new StringBuilder()).append(thing).append(n).append("Man").toString();
        Integer count = Integer.valueOf(ScriptManager.instance.getFlagIntValue((new StringBuilder()).append(thing).append("Count").toString()));
        if(n.intValue() == 3)
            ScriptManager.instance.PlayInstanceScript(null, (new StringBuilder()).append(thing).append(Rand.Next(count.intValue()) + 1).toString(), "B", c, "A", b, "Killer", a);
        if(n.intValue() == 2)
            ScriptManager.instance.PlayInstanceScript(null, (new StringBuilder()).append(thing).append(Rand.Next(count.intValue()) + 1).toString(), "A", b, "Killer", a);
        if(n.intValue() == 1)
            ScriptManager.instance.PlayInstanceScript(null, (new StringBuilder()).append(thing).append(Rand.Next(count.intValue()) + 1).toString(), "Killer", a);
    }

    public void ChewedByZombies()
    {
        IsoGameCharacter a = this;
        IsoGameCharacter b = ClosestTwoSurvivors[0];
        IsoGameCharacter c = ClosestTwoSurvivors[1];
        Integer n = Integer.valueOf(3);
        if(c == null || !c.getActiveInInstances().isEmpty())
        {
            c = null;
            Integer integer = n;
            Integer integer3 = n = Integer.valueOf(n.intValue() - 1);
            Integer _tmp = integer;
        }
        if(b != null && (!b.getActiveInInstances().isEmpty() || b.DistTo(this) > 8F))
        {
            b = c;
            c = null;
            Integer integer1 = n;
            Integer integer4 = n = Integer.valueOf(n.intValue() - 1);
            Integer _tmp1 = integer1;
        } else
        if(b == null)
        {
            Integer integer2 = n;
            Integer integer5 = n = Integer.valueOf(n.intValue() - 1);
            Integer _tmp2 = integer2;
        }
        if(n.intValue() == 3)
            n = Integer.valueOf(2);
        n = Integer.valueOf(Rand.Next(n.intValue()) + 1);
        String thing = "Base.ChewedByZombies";
        thing = (new StringBuilder()).append(thing).append("_").append(n).append("Man").toString();
        Integer count = Integer.valueOf(ScriptManager.instance.getFlagIntValue((new StringBuilder()).append(thing).append("Count").toString()));
        if(n.intValue() == 2)
            ScriptManager.instance.PlayInstanceScript(null, (new StringBuilder()).append(thing).append(Rand.Next(count.intValue()) + 1).toString(), "A", b, "Chewed", a);
        if(n.intValue() == 1)
            ScriptManager.instance.PlayInstanceScript(null, (new StringBuilder()).append(thing).append(Rand.Next(count.intValue()) + 1).toString(), "Chewed", a);
    }

    private void DoRandomTalk()
    {
    }

    public void GivenItemBy(IsoGameCharacter survivor, String type, boolean bNeeded)
    {
        if(Speaking)
            return;
        if(!getActiveInInstances().isEmpty())
            return;
        String thing = null;
        thing = "Base.GivenItem";
        if(bNeeded)
            thing = (new StringBuilder()).append(thing).append("Needed").toString();
        else
            thing = (new StringBuilder()).append(thing).append("Unneeded").toString();
        IsoGameCharacter a = null;
        IsoGameCharacter b = null;
        a = this;
        b = survivor;
        Integer count = Integer.valueOf(ScriptManager.instance.getFlagIntValue((new StringBuilder()).append(thing).append("Count").toString()));
        ScriptManager.instance.PlayInstanceScript(null, (new StringBuilder()).append(thing).append(Rand.Next(count.intValue()) + 1).toString(), "Giver", b, "Taker", a);
    }

    public void PatchedUpBy(IsoGameCharacter survivor)
    {
        if(Speaking)
            return;
        if(!getActiveInInstances().isEmpty())
        {
            return;
        } else
        {
            String thing = null;
            thing = "Base.PatchedUp";
            IsoGameCharacter a = null;
            IsoGameCharacter b = null;
            a = this;
            b = survivor;
            Integer count = Integer.valueOf(ScriptManager.instance.getFlagIntValue((new StringBuilder()).append(thing).append("Count").toString()));
            ScriptManager.instance.PlayInstanceScript(null, (new StringBuilder()).append(thing).append(Rand.Next(count.intValue()) + 1).toString(), "Medic", b, "Hurt", a);
            return;
        }
    }

    public Stack getAvailableMembers()
    {
        availableTemp.clear();
        for(int n = 0; n < descriptor.Group.Members.size(); n++)
        {
            boolean bAvail = false;
            SurvivorDesc desc = (SurvivorDesc)descriptor.Group.Members.get(n);
            if(desc == descriptor)
                continue;
            if(desc.Instance.getCurrentSquare().getRoom() != null && getCurrentSquare().getRoom() != null && desc.Instance.getCurrentSquare().getRoom().building == getCurrentSquare().getRoom().building)
                bAvail = true;
            if(desc.Instance.DistTo(this) < 10F)
                bAvail = true;
            if(bAvail)
                availableTemp.add(desc.Instance);
        }

        return availableTemp;
    }

    private boolean HasPersonalNeed(String type)
    {
        for(int n = 0; n < PersonalNeeds.size(); n++)
            if(((Order)PersonalNeeds.get(n)).type.equals(type))
                return true;

        return false;
    }

    public boolean isCollidedWithPushableThisFrame()
    {
        return bCollidedWithPushable;
    }

    public boolean SatisfiedWithInventory(zombie.behaviors.survivor.orders.LootBuilding.LootStyle lootStyle, SatisfiedBy satisfiedBy)
    {
        float score = 0.0F;
       

        switch(satisfiedBy.ordinal())
        {
        case 1: // '\001'
            score = inventory.getTotalFoodScore(descriptor);
            if(score > (float)SatisfiedByFoodLevel)
                return true;
            // fall through

        case 2: // '\002'
            score = inventory.getTotalWeaponScore(descriptor);
            if(score > (float)SatisfiedByWeaponLevel)
                return true;
            // fall through

        default:
            return false;
        }
    }

    public boolean NoGoreDeath;
    public BehaviorHub behaviours;
    public boolean Draggable;
    public IsoGameCharacter following;
    boolean Dragging;
    int repathDelay;
    public int nightsSurvived;
    public int ping;
    public IsoPushableObject collidePushable;
    public boolean bLastSpottedPlayer;
    public boolean bSpottedPlayer;
    public boolean bWillJoinPlayer;
    public boolean HasBeenDragged;
    public IsoGameCharacter ClosestTwoSurvivors[];
    int NeightbourUpdate;
    int NeightbourUpdateMax;
    public Vector2 lmove;
    public NulledArrayList LastLocalNeutralList;
    OnceEvery LOSUpdate;
    public int dangerTile;
    public int lastDangerTile;
    IsoGameCharacter aimAt;
    Stack availableTemp;
    public boolean bCollidedWithPushable;
    public static int SatisfiedByFoodLevel = 100;
    public static int SatisfiedByWeaponLevel = 80;

}
