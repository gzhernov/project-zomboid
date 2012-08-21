// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   IsoGameCharacter.java

package zombie.characters;

import gnu.trove.map.hash.THashMap;
import java.io.*;
import java.nio.ByteBuffer;
import java.util.*;
import zombie.*;
import zombie.Lua.LuaEventManager;
import zombie.Lua.LuaHookManager;
import zombie.*;
import zombie.ai.State;
import zombie.ai.StateMachine;
import zombie.ai.astar.*;
import zombie.ai.states.*;
import zombie.behaviors.*;
import zombie.behaviors.survivor.MasterSurvivorBehavior;
import zombie.behaviors.survivor.orders.LittleTasks.StopAndFaceForOrder;
import zombie.behaviors.survivor.orders.Order;
import zombie.characters.BodyDamage.BodyDamage;
import zombie.characters.CharacterTimedActions.BaseAction;
import zombie.characters.Moodles.MoodleType;
import zombie.characters.Moodles.Moodles;
import zombie.characters.skills.PerkFactory;
import zombie.core.Collections.NulledArrayList;
import zombie.core.*;
import zombie.core.bucket.Bucket;
import zombie.core.bucket.BucketManager;
import zombie.core.properties.PropertyContainer;
import zombie.core.textures.ColorInfo;
import zombie.core.utils.OnceEvery;
import zombie.interfaces.IUpdater;
import zombie.inventory.InventoryItem;
import zombie.inventory.ItemContainer;
import zombie.inventory.types.*;
import zombie.iso.*;
import zombie.iso.SpriteDetails.IsoFlagType;
import zombie.iso.SpriteDetails.IsoObjectType;
import zombie.iso.Vector2;
import zombie.iso.areas.IsoBuilding;
import zombie.iso.areas.IsoRoom;
import zombie.iso.objects.*;
import zombie.iso.sprite.*;
import zombie.scripting.ScriptManager;
import zombie.scripting.objects.Item;
import zombie.ui.*;

// Referenced classes of package zombie.characters:
//            IsoZombie, IsoPlayer, IsoSurvivor, SurvivorDesc, 
//            IsoLivingCharacter, Talker, WeaponOverlayUtils, SurvivorPersonality

public class IsoGameCharacter extends IsoMovingObject
    implements Talker
{
    public class Stats
    {

        public void load(DataInputStream input)
            throws IOException
        {
            Anger = input.readFloat();
            boredom = input.readFloat();
            endurance = input.readFloat();
            fatigue = input.readFloat();
            fitness = input.readFloat();
            hunger = input.readFloat();
            morale = input.readFloat();
            stress = input.readFloat();
            Fear = input.readFloat();
            Panic = input.readFloat();
            Sanity = input.readFloat();
            Sickness = input.readFloat();
            Boredom = input.readFloat();
            Pain = input.readFloat();
            Drunkenness = input.readFloat();
            thirst = input.readFloat();
        }

        public void load(ByteBuffer input)
            throws IOException
        {
            Anger = input.getFloat();
            boredom = input.getFloat();
            endurance = input.getFloat();
            fatigue = input.getFloat();
            fitness = input.getFloat();
            hunger = input.getFloat();
            morale = input.getFloat();
            stress = input.getFloat();
            Fear = input.getFloat();
            Panic = input.getFloat();
            Sanity = input.getFloat();
            Sickness = input.getFloat();
            Boredom = input.getFloat();
            Pain = input.getFloat();
            Drunkenness = input.getFloat();
            thirst = input.getFloat();
        }

        public void save(DataOutputStream output)
            throws IOException
        {
            output.writeFloat(Anger);
            output.writeFloat(boredom);
            output.writeFloat(endurance);
            output.writeFloat(fatigue);
            output.writeFloat(fitness);
            output.writeFloat(hunger);
            output.writeFloat(morale);
            output.writeFloat(stress);
            output.writeFloat(Fear);
            output.writeFloat(Panic);
            output.writeFloat(Sanity);
            output.writeFloat(Sickness);
            output.writeFloat(Boredom);
            output.writeFloat(Pain);
            output.writeFloat(Drunkenness);
            output.writeFloat(thirst);
        }

        public void save(ByteBuffer output)
            throws IOException
        {
            output.putFloat(Anger);
            output.putFloat(boredom);
            output.putFloat(endurance);
            output.putFloat(fatigue);
            output.putFloat(fitness);
            output.putFloat(hunger);
            output.putFloat(morale);
            output.putFloat(stress);
            output.putFloat(Fear);
            output.putFloat(Panic);
            output.putFloat(Sanity);
            output.putFloat(Sickness);
            output.putFloat(Boredom);
            output.putFloat(Pain);
            output.putFloat(Drunkenness);
            output.putFloat(thirst);
        }

        public float getAnger()
        {
            return Anger;
        }

        public void setAnger(float Anger)
        {
            this.Anger = Anger;
        }

        public float getBoredom()
        {
            return boredom;
        }

        public void setBoredom(float boredom)
        {
            this.boredom = boredom;
        }

        public float getEndurance()
        {
            return endurance;
        }

        public void setEndurance(float endurance)
        {
            this.endurance = endurance;
        }

        public float getEndurancelast()
        {
            return endurancelast;
        }

        public void setEndurancelast(float endurancelast)
        {
            this.endurancelast = endurancelast;
        }

        public float getEndurancedanger()
        {
            return endurancedanger;
        }

        public void setEndurancedanger(float endurancedanger)
        {
            this.endurancedanger = endurancedanger;
        }

        public float getEndurancewarn()
        {
            return endurancewarn;
        }

        public void setEndurancewarn(float endurancewarn)
        {
            this.endurancewarn = endurancewarn;
        }

        public float getFatigue()
        {
            return fatigue;
        }

        public void setFatigue(float fatigue)
        {
            this.fatigue = fatigue;
        }

        public float getFitness()
        {
            return fitness;
        }

        public void setFitness(float fitness)
        {
            this.fitness = fitness;
        }

        public float getHunger()
        {
            return hunger;
        }

        public void setHunger(float hunger)
        {
            this.hunger = hunger;
        }

        public float getIdleboredom()
        {
            return idleboredom;
        }

        public void setIdleboredom(float idleboredom)
        {
            this.idleboredom = idleboredom;
        }

        public float getMorale()
        {
            return morale;
        }

        public void setMorale(float morale)
        {
            this.morale = morale;
        }

        public float getStress()
        {
            return stress;
        }

        public void setStress(float stress)
        {
            this.stress = stress;
        }

        public float getFear()
        {
            return Fear;
        }

        public void setFear(float Fear)
        {
            this.Fear = Fear;
        }

        public float getPanic()
        {
            return Panic;
        }

        public void setPanic(float Panic)
        {
            this.Panic = Panic;
        }

        public float getSanity()
        {
            return Sanity;
        }

        public void setSanity(float Sanity)
        {
            this.Sanity = Sanity;
        }

        public float getSickness()
        {
            return Sickness;
        }

        public void setSickness(float Sickness)
        {
            this.Sickness = Sickness;
        }

        public float getPain()
        {
            return Pain;
        }

        public void setPain(float Pain)
        {
            this.Pain = Pain;
        }

        public float getDrunkenness()
        {
            return Drunkenness;
        }

        public void setDrunkenness(float Drunkenness)
        {
            this.Drunkenness = Drunkenness;
        }

        public int getNumVisibleZombies()
        {
            return NumVisibleZombies;
        }

        public void setNumVisibleZombies(int NumVisibleZombies)
        {
            this.NumVisibleZombies = NumVisibleZombies;
        }

        public boolean isTripping()
        {
            return Tripping;
        }

        public void setTripping(boolean Tripping)
        {
            this.Tripping = Tripping;
        }

        public float getTrippingRotAngle()
        {
            return TrippingRotAngle;
        }

        public void setTrippingRotAngle(float TrippingRotAngle)
        {
            this.TrippingRotAngle = TrippingRotAngle;
        }

        public float getThirst()
        {
            return thirst;
        }

        public void setThirst(float thirst)
        {
            this.thirst = thirst;
        }

        public float Anger;
        public float boredom;
        public float endurance;
        public float endurancelast;
        public float endurancedanger;
        public float endurancewarn;
        public float fatigue;
        public float fitness;
        public float hunger;
        public float idleboredom;
        public float morale;
        public float stress;
        public float Fear;
        public float Panic;
        public float Sanity;
        public float Sickness;
        public float Boredom;
        public float Pain;
        public float Drunkenness;
        public int NumVisibleZombies;
        public boolean Tripping;
        public float TrippingRotAngle;
        public float thirst;
       

        public Stats()
        {
          
            Anger = 0.0F;
            boredom = 0.0F;
            endurance = 1.0F;
            endurancelast = 1.0F;
            endurancedanger = 0.25F;
            endurancewarn = 0.5F;
            fatigue = 0.0F;
            fitness = 1.0F;
            hunger = 0.0F;
            idleboredom = 0.0F;
            morale = 0.5F;
            stress = 0.0F;
            Fear = 0.0F;
            Panic = 0.0F;
            Sanity = 1.0F;
            Sickness = 0.0F;
            Boredom = 0.0F;
            Pain = 0.0F;
            Drunkenness = 0.0F;
            NumVisibleZombies = 0;
            Tripping = false;
            TrippingRotAngle = 0.0F;
            thirst = 0.0F;
        }
    }

    public class Wound
    {

        public boolean bandaged;
        public float bleeding;
        public float infectAmount;
        public boolean infectedNormal;
        public boolean infectedZombie;
        public BodyLocation loc;
        public boolean tourniquet;
       

        public Wound()
        {

            bandaged = false;
            bleeding = 0.0F;
            infectAmount = 0.0F;
            infectedNormal = false;
            infectedZombie = false;
            tourniquet = false;
        }
    }

    /*      */   public static enum BodyLocation
    /*      */   {
    /* 3619 */     Head, 
    /* 3620 */     Leg, 
    /* 3621 */     Arm, 
    /* 3622 */     Chest, 
    /* 3623 */     Stomach, 
    /* 3624 */     Foot, 
    /* 3625 */     Hand;
    /*      */   }
    
    
    

    public class Location
    {

        public int x;
        public int y;
        public int z;
        

        public Location(int x, int y, int z)
        {
           
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }

    public class XP
    {

        public void AddXP(zombie.characters.skills.PerkFactory.Perks type, int amount)
        {
            if(XPMap.containsKey(type))
            {
                int newTot = ((Integer)XPMap.get(type)).intValue() + amount;
                XPMap.put(type, Integer.valueOf(newTot));
                PerkFactory.CheckForUnlockedPerks(chr);
            } else
            {
                XPMap.put(type, Integer.valueOf(amount));
                PerkFactory.CheckForUnlockedPerks(chr);
            }
            if(NumberOfPerksToPick == 0)
                TotalXP += amount;
            int am = 0;
            if(level < IsoGameCharacter.LevelUpLevels.length)
                am = (int)((float)IsoGameCharacter.LevelUpLevels[level] * LevelUpMultiplier);
            else
                am = (int)((float)IsoGameCharacter.LevelUpLevels[IsoGameCharacter.LevelUpLevels.length - 1] + (float)((level - IsoGameCharacter.LevelUpLevels.length) * 400) * LevelUpMultiplier);
            while(TotalXP > am) 
            {
                level++;
                chr.NumberOfPerksToPick++;
                if(!IsoPlayer.instance.CanUpgradePerk.isEmpty() && IsoPlayer.instance.NumberOfPerksToPick > 0)
                    SoundManager.instance.PlaySound("levelup", false, 0.6F);
                if(level < IsoGameCharacter.LevelUpLevels.length)
                    am = (int)((float)IsoGameCharacter.LevelUpLevels[level] * LevelUpMultiplier);
                else
                    am = (int)((float)IsoGameCharacter.LevelUpLevels[IsoGameCharacter.LevelUpLevels.length - 1] + (float)((level - IsoGameCharacter.LevelUpLevels.length) * 400) * LevelUpMultiplier);
            }
            if(UIManager.getLevelup() != null && (chr instanceof IsoPlayer))
                UIManager.getLevelup().reset();
        }

        public int getXP(zombie.characters.skills.PerkFactory.Perks type)
        {
            if(XPMap.containsKey(type))
                return ((Integer)XPMap.get(type)).intValue();
            else
                return 0;
        }

        public void AddXP(HandWeapon weapon, int amount)
        {
            if(weapon == null)
                return;
            if(weapon.getScriptItem().Categories.contains("Axe"))
                AddXP(zombie.characters.skills.PerkFactory.Perks.Axe, amount);
            if(weapon.getScriptItem().Categories.contains("Blunt"))
                AddXP(zombie.characters.skills.PerkFactory.Perks.Blunt, amount);
        }

        private void load(ByteBuffer input)
            throws IOException
        {
            int nTraits = input.getInt();
            for(int n = 0; n < nTraits; n++)
                chr.Traits.add(GameWindow.ReadString(input));

            TotalXP = input.getInt();
            level = input.getInt();
            lastlevel = input.getInt();
            int x = input.getInt();
            for(int n = 0; n < x; n++)
                XPMap.put(zombie.characters.skills.PerkFactory.Perks.fromIndex(input.getInt()), Integer.valueOf(input.getInt()));

            int a = input.getInt();
            for(int n = 0; n < a; n++)
                chr.CanUpgradePerk.add(zombie.characters.skills.PerkFactory.Perks.fromIndex(input.getInt()));

            int nperks = input.getInt();
            for(int n = 0; n < nperks; n++)
            {
                zombie.characters.skills.PerkFactory.Perks p = zombie.characters.skills.PerkFactory.Perks.fromIndex(input.getInt());
                zombie.characters.skills.PerkFactory.Perk theperk = (zombie.characters.skills.PerkFactory.Perk)PerkFactory.PerkMap.get(p);
                PerkInfo info = new PerkInfo();
                info.perk = theperk;
                info.perkType = p;
                info.level = input.getInt();
                PerkList.add(info);
            }

        }

        private void load(DataInputStream input)
            throws IOException
        {
            int nTraits = input.readInt();
            for(int n = 0; n < nTraits; n++)
                chr.Traits.add(GameWindow.ReadString(input));

            TotalXP = input.readInt();
            level = input.readInt();
            lastlevel = input.readInt();
            int x = input.readInt();
            for(int n = 0; n < x; n++)
                XPMap.put(zombie.characters.skills.PerkFactory.Perks.fromIndex(input.readInt()), Integer.valueOf(input.readInt()));

            int a = input.readInt();
            for(int n = 0; n < a; n++)
                chr.CanUpgradePerk.add(zombie.characters.skills.PerkFactory.Perks.fromIndex(input.readInt()));

            int nperks = input.readInt();
            for(int n = 0; n < nperks; n++)
            {
                zombie.characters.skills.PerkFactory.Perks p = zombie.characters.skills.PerkFactory.Perks.fromIndex(input.readInt());
                zombie.characters.skills.PerkFactory.Perk theperk = (zombie.characters.skills.PerkFactory.Perk)PerkFactory.PerkMap.get(p);
                PerkInfo info = new PerkInfo();
                info.perk = theperk;
                info.perkType = p;
                info.level = input.readInt();
                PerkList.add(info);
            }

        }

        private void save(DataOutputStream output)
            throws IOException
        {
            output.writeInt(chr.Traits.size());
            for(int n = 0; n < chr.Traits.size(); n++)
                GameWindow.WriteString(output, (String)chr.Traits.get(n));

            output.writeInt(TotalXP);
            output.writeInt(level);
            output.writeInt(lastlevel);
            output.writeInt(XPMap.size());
            java.util.Map.Entry e;
            for(Iterator it = XPMap.entrySet().iterator(); it != null && it.hasNext(); output.writeInt(((Integer)e.getValue()).intValue()))
            {
                e = (java.util.Map.Entry)it.next();
                output.writeInt(((zombie.characters.skills.PerkFactory.Perks)e.getKey()).index());
            }

            output.writeInt(chr.CanUpgradePerk.size());
            for(int n = 0; n < chr.CanUpgradePerk.size(); n++)
            {
                int a = ((zombie.characters.skills.PerkFactory.Perks)chr.CanUpgradePerk.get(n)).index();
                output.writeInt(a);
            }

            output.writeInt(PerkList.size());
            for(int n = 0; n < PerkList.size(); n++)
            {
                output.writeInt(((PerkInfo)PerkList.get(n)).perkType.index());
                output.writeInt(((PerkInfo)PerkList.get(n)).level);
            }

        }

        private void save(ByteBuffer output)
            throws IOException
        {
            output.putInt(chr.Traits.size());
            for(int n = 0; n < chr.Traits.size(); n++)
                GameWindow.WriteString(output, (String)chr.Traits.get(n));

            output.putInt(TotalXP);
            output.putInt(level);
            output.putInt(lastlevel);
            output.putInt(XPMap.size());
            java.util.Map.Entry e;
            for(Iterator it = XPMap.entrySet().iterator(); it != null && it.hasNext(); output.putInt(((Integer)e.getValue()).intValue()))
            {
                e = (java.util.Map.Entry)it.next();
                output.putInt(((zombie.characters.skills.PerkFactory.Perks)e.getKey()).index());
            }

            output.putInt(chr.CanUpgradePerk.size());
            for(int n = 0; n < chr.CanUpgradePerk.size(); n++)
            {
                int a = ((zombie.characters.skills.PerkFactory.Perks)chr.CanUpgradePerk.get(n)).index();
                output.putInt(a);
            }

            output.putInt(PerkList.size());
            for(int n = 0; n < PerkList.size(); n++)
            {
                output.putInt(((PerkInfo)PerkList.get(n)).perkType.index());
                output.putInt(((PerkInfo)PerkList.get(n)).level);
            }

        }

        public int level;
        public int lastlevel;
        public int TotalXP;
        public THashMap XPMap;
        IsoGameCharacter chr;
        



        public XP(IsoGameCharacter chr)
        {
           
            level = 0;
            lastlevel = 0;
            TotalXP = 0;
            XPMap = new THashMap();
            this.chr = null;
            this.chr = chr;
        }
    }

    public class PerkInfo
    {

        public int level;
        public zombie.characters.skills.PerkFactory.Perk perk;
        public zombie.characters.skills.PerkFactory.Perks perkType;
       

        public PerkInfo()
        {
           
            level = 0;
        }
    }


    public String getTalkerType()
    {
        return "character";
    }

    public static THashMap getSurvivorMap()
    {
        return SurvivorMap;
    }

    public static void setSurvivorMap(THashMap aSurvivorMap)
    {
        SurvivorMap = aSurvivorMap;
    }

    public static int[] getLevelUpLevels()
    {
        return LevelUpLevels;
    }

    public static void setLevelUpLevels(int aLevelUpLevels[])
    {
        LevelUpLevels = aLevelUpLevels;
    }

    public static Vector2 getTempo()
    {
        return tempo;
    }

    public static void setTempo(Vector2 aTempo)
    {
        tempo = aTempo;
    }

    public static ColorInfo getInf()
    {
        return inf;
    }

    public static void setInf(ColorInfo aInf)
    {
        inf = aInf;
    }

    public static OnceEvery getTestPlayerSpotInDarkness()
    {
        return testPlayerSpotInDarkness;
    }

    public static void setTestPlayerSpotInDarkness(OnceEvery aTestPlayerSpotInDarkness)
    {
        testPlayerSpotInDarkness = aTestPlayerSpotInDarkness;
    }

    public void DoDeath(HandWeapon weapon, IsoGameCharacter wielder)
    {
        OnDeath();
        if(Health < 0.0F)
        {
            if(bUseParts && weapon != null && weapon.getType().equals("Shotgun"))
                headSprite = null;
            if(weapon != null)
            {
                for(int n = 0; n < weapon.getSplatNumber(); n++)
                    splatBlood(3, 0.3F);

            }
            splatBloodFloor(1.0F);
            if(wielder != null)
                wielder.xp.AddXP(weapon, 3);
            IsoZombieGiblets gibs = new IsoZombieGiblets(zombie.iso.objects.IsoZombieGiblets.GibletType.A, getCell(), getX(), getY(), getZ() + 0.6F, getHitDir().x * 1.5F, getHitDir().y * 1.5F);
            tempo.x = getHitDir().x;
            tempo.y = getHitDir().y;
            if(Rand.Next(3) == 0)
                gibs = new IsoZombieGiblets(zombie.iso.objects.IsoZombieGiblets.GibletType.A, getCell(), getX(), getY(), getZ() + 0.6F, getHitDir().x * 1.5F, getHitDir().y * 1.5F);
            if(Rand.Next(3) == 0)
                gibs = new IsoZombieGiblets(zombie.iso.objects.IsoZombieGiblets.GibletType.A, getCell(), getX(), getY(), getZ() + 0.6F, getHitDir().x * 1.5F, getHitDir().y * 1.5F);
            if(Rand.Next(3) == 0)
                gibs = new IsoZombieGiblets(zombie.iso.objects.IsoZombieGiblets.GibletType.A, getCell(), getX(), getY(), getZ() + 0.6F, getHitDir().x * 1.8F, getHitDir().y * 1.8F);
            if(Rand.Next(3) == 0)
                gibs = new IsoZombieGiblets(zombie.iso.objects.IsoZombieGiblets.GibletType.A, getCell(), getX(), getY(), getZ() + 0.6F, getHitDir().x * 1.9F, getHitDir().y * 1.9F);
            gibs = new IsoZombieGiblets(zombie.iso.objects.IsoZombieGiblets.GibletType.A, getCell(), getX(), getY(), getZ() + 0.6F, getHitDir().x * 3.5F, getHitDir().y * 3.5F);
            gibs = new IsoZombieGiblets(zombie.iso.objects.IsoZombieGiblets.GibletType.A, getCell(), getX(), getY(), getZ() + 0.6F, getHitDir().x * 3.8F, getHitDir().y * 3.8F);
            gibs = new IsoZombieGiblets(zombie.iso.objects.IsoZombieGiblets.GibletType.A, getCell(), getX(), getY(), getZ() + 0.6F, getHitDir().x * 3.9F, getHitDir().y * 3.9F);
            gibs = new IsoZombieGiblets(zombie.iso.objects.IsoZombieGiblets.GibletType.A, getCell(), getX(), getY(), getZ() + 0.6F, getHitDir().x * 1.5F, getHitDir().y * 1.5F);
            gibs = new IsoZombieGiblets(zombie.iso.objects.IsoZombieGiblets.GibletType.A, getCell(), getX(), getY(), getZ() + 0.6F, getHitDir().x * 3.8F, getHitDir().y * 3.8F);
            gibs = new IsoZombieGiblets(zombie.iso.objects.IsoZombieGiblets.GibletType.A, getCell(), getX(), getY(), getZ() + 0.6F, getHitDir().x * 3.9F, getHitDir().y * 3.9F);
            if(Rand.Next(6) == 0)
                gibs = new IsoZombieGiblets(zombie.iso.objects.IsoZombieGiblets.GibletType.Eye, getCell(), getX(), getY(), getZ() + 0.6F, getHitDir().x * 0.8F, getHitDir().y * 0.8F);
        }
        stateMachine.changeState(StaggerBackDieState.instance());
        stateMachine.Lock = true;
    }

    private void TestIfSeen()
    {
        if(this == IsoPlayer.instance)
            return;
        float dist = DistTo(IsoPlayer.instance);
        if(dist > GameTime.getInstance().getViewDist())
            return;
        float delta = (getCurrentSquare().getLightInfo().r + getCurrentSquare().getLightInfo().g + getCurrentSquare().getLightInfo().b) / 3F;
        if(delta > 0.4F)
            delta = 1.0F;
        float delta2 = 1.0F - dist / GameTime.getInstance().getViewDist();
        if(delta == 1.0F && delta2 > 0.3F)
            delta2 = 1.0F;
        tempo.x = getX();
        tempo.y = getY();
        tempo.x -= IsoPlayer.instance.getX();
        tempo.y -= IsoPlayer.instance.getY();
        Vector2 vecB = IsoPlayer.instance.getVectorFromDirection();
        tempo.normalize();
        float angle = vecB.dot(tempo);
        if(angle < 0.5F)
            angle = 0.5F;
        delta *= angle;
        if(delta < 0.0F)
            delta = 0.0F;
        if(dist <= 1.0F)
        {
            delta2 = 1.0F;
            delta *= 2.0F;
        }
        delta *= delta2;
        delta *= 100F;
        if((float)Rand.Next(100) < delta || !(this instanceof IsoGameCharacter))
        {
            SpottedSinceAlphaZero = true;
            timeTillForgetLocation = 600;
        }
    }

    private void DoLand()
    {
        if(fallTime < 20)
            return;
        if(!(this instanceof IsoZombie))
            BodyDamage.ReduceGeneralHealth(fallTime * 2);
    }

    public IsoGameCharacter getFollowingTarget()
    {
        return FollowingTarget;
    }

    public void setFollowingTarget(IsoGameCharacter FollowingTarget)
    {
        this.FollowingTarget = FollowingTarget;
    }

    public NulledArrayList getLocalList()
    {
        return LocalList;
    }

    public void setLocalList(NulledArrayList LocalList)
    {
        this.LocalList = LocalList;
    }

    public NulledArrayList getLocalNeutralList()
    {
        return LocalNeutralList;
    }

    public void setLocalNeutralList(NulledArrayList LocalNeutralList)
    {
        this.LocalNeutralList = LocalNeutralList;
    }

    public NulledArrayList getLocalGroupList()
    {
        return LocalGroupList;
    }

    public void setLocalGroupList(NulledArrayList LocalGroupList)
    {
        this.LocalGroupList = LocalGroupList;
    }

    public NulledArrayList getLocalRelevantEnemyList()
    {
        return LocalRelevantEnemyList;
    }

    public void setLocalRelevantEnemyList(NulledArrayList LocalRelevantEnemyList)
    {
        this.LocalRelevantEnemyList = LocalRelevantEnemyList;
    }

    public float getDangerLevels()
    {
        return dangerLevels;
    }

    public void setDangerLevels(float dangerLevels)
    {
        this.dangerLevels = dangerLevels;
    }

    public Stack getMeetList()
    {
        return MeetList;
    }

    public void setMeetList(Stack MeetList)
    {
        this.MeetList = MeetList;
    }

    public Order getOrder()
    {
        return Order;
    }

    public void setOrder(Order Order)
    {
        this.Order = Order;
    }

    public Stack getOrders()
    {
        return Orders;
    }

    public void setOrders(Stack Orders)
    {
        this.Orders = Orders;
    }

    public ArrayList getPerkList()
    {
        return PerkList;
    }

    public void setPerkList(ArrayList PerkList)
    {
        this.PerkList = PerkList;
    }

    public Order getPersonalNeed()
    {
        return PersonalNeed;
    }

    public void setPersonalNeed(Order PersonalNeed)
    {
        this.PersonalNeed = PersonalNeed;
    }

    public Stack getPersonalNeeds()
    {
        return PersonalNeeds;
    }

    public void setPersonalNeeds(Stack PersonalNeeds)
    {
        this.PersonalNeeds = PersonalNeeds;
    }

    public int getLeaveBodyTimedown()
    {
        return leaveBodyTimedown;
    }

    public void setLeaveBodyTimedown(int leaveBodyTimedown)
    {
        this.leaveBodyTimedown = leaveBodyTimedown;
    }

    public boolean isAllowConversation()
    {
        return AllowConversation;
    }

    public void setAllowConversation(boolean AllowConversation)
    {
        this.AllowConversation = AllowConversation;
    }

    public int getReanimPhase()
    {
        return ReanimPhase;
    }

    public void setReanimPhase(int ReanimPhase)
    {
        this.ReanimPhase = ReanimPhase;
    }

    public int getReanimateTimer()
    {
        return ReanimateTimer;
    }

    public void setReanimateTimer(int ReanimateTimer)
    {
        this.ReanimateTimer = ReanimateTimer;
    }

    public int getReanimAnimFrame()
    {
        return ReanimAnimFrame;
    }

    public void setReanimAnimFrame(int ReanimAnimFrame)
    {
        this.ReanimAnimFrame = ReanimAnimFrame;
    }

    public int getReanimAnimDelay()
    {
        return ReanimAnimDelay;
    }

    public void setReanimAnimDelay(int ReanimAnimDelay)
    {
        this.ReanimAnimDelay = ReanimAnimDelay;
    }

    public boolean isReanim()
    {
        return Reanim;
    }

    public void setReanim(boolean Reanim)
    {
        this.Reanim = Reanim;
    }

    public boolean isVisibleToNPCs()
    {
        return VisibleToNPCs;
    }

    public void setVisibleToNPCs(boolean VisibleToNPCs)
    {
        this.VisibleToNPCs = VisibleToNPCs;
    }

    public int getDieCount()
    {
        return DieCount;
    }

    public void setDieCount(int DieCount)
    {
        this.DieCount = DieCount;
    }

    public float getLlx()
    {
        return llx;
    }

    public void setLlx(float llx)
    {
        this.llx = llx;
    }

    public float getLly()
    {
        return lly;
    }

    public void setLly(float lly)
    {
        this.lly = lly;
    }

    public float getLlz()
    {
        return llz;
    }

    public void setLlz(float llz)
    {
        this.llz = llz;
    }

    public int getRemoteID()
    {
        return RemoteID;
    }

    public void setRemoteID(int RemoteID)
    {
        this.RemoteID = RemoteID;
    }

    public int getNumSurvivorsInVicinity()
    {
        return NumSurvivorsInVicinity;
    }

    public void setNumSurvivorsInVicinity(int NumSurvivorsInVicinity)
    {
        this.NumSurvivorsInVicinity = NumSurvivorsInVicinity;
    }

    public float getLevelUpMultiplier()
    {
        return LevelUpMultiplier;
    }

    public void setLevelUpMultiplier(float LevelUpMultiplier)
    {
        this.LevelUpMultiplier = LevelUpMultiplier;
    }

    public XP getXp()
    {
        return xp;
    }

    public void setXp(XP xp)
    {
        this.xp = xp;
    }

    public int getNumberOfPerksToPick()
    {
        return NumberOfPerksToPick;
    }

    public void setNumberOfPerksToPick(int NumberOfPerksToPick)
    {
        this.NumberOfPerksToPick = NumberOfPerksToPick;
    }

    public NulledArrayList getCanUpgradePerk()
    {
        return CanUpgradePerk;
    }

    public void setCanUpgradePerk(NulledArrayList CanUpgradePerk)
    {
        this.CanUpgradePerk = CanUpgradePerk;
    }

    public int getLastLocalEnemies()
    {
        return LastLocalEnemies;
    }

    public void setLastLocalEnemies(int LastLocalEnemies)
    {
        this.LastLocalEnemies = LastLocalEnemies;
    }

    public NulledArrayList getVeryCloseEnemyList()
    {
        return VeryCloseEnemyList;
    }

    public void setVeryCloseEnemyList(NulledArrayList VeryCloseEnemyList)
    {
        this.VeryCloseEnemyList = VeryCloseEnemyList;
    }

    public THashMap getLastKnownLocation()
    {
        return LastKnownLocation;
    }

    public void setLastKnownLocation(THashMap LastKnownLocation)
    {
        this.LastKnownLocation = LastKnownLocation;
    }

    public IsoGameCharacter getAttackedBy()
    {
        return AttackedBy;
    }

    public void setAttackedBy(IsoGameCharacter AttackedBy)
    {
        this.AttackedBy = AttackedBy;
    }

    public boolean isIgnoreStaggerBack()
    {
        return IgnoreStaggerBack;
    }

    public void setIgnoreStaggerBack(boolean IgnoreStaggerBack)
    {
        this.IgnoreStaggerBack = IgnoreStaggerBack;
    }

    public boolean isAttackWasSuperAttack()
    {
        return AttackWasSuperAttack;
    }

    public void setAttackWasSuperAttack(boolean AttackWasSuperAttack)
    {
        this.AttackWasSuperAttack = AttackWasSuperAttack;
    }

    public int getTimeThumping()
    {
        return TimeThumping;
    }

    public void setTimeThumping(int TimeThumping)
    {
        this.TimeThumping = TimeThumping;
    }

    public int getPatienceMax()
    {
        return PatienceMax;
    }

    public void setPatienceMax(int PatienceMax)
    {
        this.PatienceMax = PatienceMax;
    }

    public int getPatienceMin()
    {
        return PatienceMin;
    }

    public void setPatienceMin(int PatienceMin)
    {
        this.PatienceMin = PatienceMin;
    }

    public int getPatience()
    {
        return Patience;
    }

    public void setPatience(int Patience)
    {
        this.Patience = Patience;
    }

    public Stack getCharacterActions()
    {
        return CharacterActions;
    }

    public void setCharacterActions(Stack CharacterActions)
    {
        this.CharacterActions = CharacterActions;
    }

    public Vector2 getAngle()
    {
        return angle;
    }

    public void setAngle(Vector2 angle)
    {
        this.angle = angle;
    }

    public boolean isAsleep()
    {
        return Asleep;
    }

    public void setAsleep(boolean Asleep)
    {
        this.Asleep = Asleep;
    }

    public int getAttackDelay()
    {
        return AttackDelay;
    }

    public void setAttackDelay(int AttackDelay)
    {
        this.AttackDelay = AttackDelay;
    }

    public int getAttackDelayMax()
    {
        return AttackDelayMax;
    }

    public void setAttackDelayMax(int AttackDelayMax)
    {
        this.AttackDelayMax = AttackDelayMax;
    }

    public int getAttackDelayUse()
    {
        return AttackDelayUse;
    }

    public void setAttackDelayUse(int AttackDelayUse)
    {
        this.AttackDelayUse = AttackDelayUse;
    }

    public int getZombieKills()
    {
        return ZombieKills;
    }

    public void setZombieKills(int ZombieKills)
    {
        this.ZombieKills = ZombieKills;
    }

    public int getLastZombieKills()
    {
        return LastZombieKills;
    }

    public void setLastZombieKills(int LastZombieKills)
    {
        this.LastZombieKills = LastZombieKills;
    }

    public boolean isSuperAttack()
    {
        return superAttack;
    }

    public void setSuperAttack(boolean superAttack)
    {
        this.superAttack = superAttack;
    }

    public float getForceWakeUpTime()
    {
        return ForceWakeUpTime;
    }

    public void setForceWakeUpTime(float ForceWakeUpTime)
    {
        this.ForceWakeUpTime = ForceWakeUpTime;
    }

    public BodyDamage getBodyDamage()
    {
        return BodyDamage;
    }

    public void setBodyDamage(BodyDamage BodyDamage)
    {
        this.BodyDamage = BodyDamage;
    }

    public InventoryItem getCraftIngredient1()
    {
        return craftIngredient1;
    }

    public void setCraftIngredient1(InventoryItem craftIngredient1)
    {
        this.craftIngredient1 = craftIngredient1;
    }

    public InventoryItem getCraftIngredient2()
    {
        return craftIngredient2;
    }

    public void setCraftIngredient2(InventoryItem craftIngredient2)
    {
        this.craftIngredient2 = craftIngredient2;
    }

    public InventoryItem getCraftIngredient3()
    {
        return craftIngredient3;
    }

    public void setCraftIngredient3(InventoryItem craftIngredient3)
    {
        this.craftIngredient3 = craftIngredient3;
    }

    public InventoryItem getCraftIngredient4()
    {
        return craftIngredient4;
    }

    public void setCraftIngredient4(InventoryItem craftIngredient4)
    {
        this.craftIngredient4 = craftIngredient4;
    }

    public State getDefaultState()
    {
        return defaultState;
    }

    public void setDefaultState(State defaultState)
    {
        this.defaultState = defaultState;
    }

    public SurvivorDesc getDescriptor()
    {
        return descriptor;
    }

    public void setDescriptor(SurvivorDesc descriptor)
    {
        this.descriptor = descriptor;
    }

    public Stack getFamiliarBuildings()
    {
        return FamiliarBuildings;
    }

    public void setFamiliarBuildings(Stack FamiliarBuildings)
    {
        this.FamiliarBuildings = FamiliarBuildings;
    }

    public AStarPathFinderResult getFinder()
    {
        return finder;
    }

    public void setFinder(AStarPathFinderResult finder)
    {
        this.finder = finder;
    }

    public float getFireKillRate()
    {
        return FireKillRate;
    }

    public void setFireKillRate(float FireKillRate)
    {
        this.FireKillRate = FireKillRate;
    }

    public int getFireSpreadProbability()
    {
        return FireSpreadProbability;
    }

    public void setFireSpreadProbability(int FireSpreadProbability)
    {
        this.FireSpreadProbability = FireSpreadProbability;
    }

    public int getFootStepCounter()
    {
        return footStepCounter;
    }

    public void setFootStepCounter(int footStepCounter)
    {
        this.footStepCounter = footStepCounter;
    }

    public int getFootStepCounterMax()
    {
        return footStepCounterMax;
    }

    public void setFootStepCounterMax(int footStepCounterMax)
    {
        this.footStepCounterMax = footStepCounterMax;
    }

    public float getHealth()
    {
        return Health;
    }

    public void setHealth(float Health)
    {
        this.Health = Health;
    }

    public MasterSurvivorBehavior getMasterProper()
    {
        return masterProper;
    }

    public void setMasterProper(MasterSurvivorBehavior masterProper)
    {
        this.masterProper = masterProper;
    }

    public IsoGameCharacter getHitBy()
    {
        return hitBy;
    }

    public void setHitBy(IsoGameCharacter hitBy)
    {
        this.hitBy = hitBy;
    }

    public String getHurtSound()
    {
        return hurtSound;
    }

    public void setHurtSound(String hurtSound)
    {
        this.hurtSound = hurtSound;
    }

    public boolean isIgnoreMovementForDirection()
    {
        return IgnoreMovementForDirection;
    }

    public void setIgnoreMovementForDirection(boolean IgnoreMovementForDirection)
    {
        this.IgnoreMovementForDirection = IgnoreMovementForDirection;
    }

    public ItemContainer getInventory()
    {
        return inventory;
    }

    public void setInventory(ItemContainer inventory)
    {
        this.inventory = inventory;
    }

    public IsoDirections getLastdir()
    {
        return lastdir;
    }

    public void setLastdir(IsoDirections lastdir)
    {
        this.lastdir = lastdir;
    }

    public InventoryItem getPrimaryHandItem()
    {
        return leftHandItem;
    }

    public void setPrimaryHandItem(InventoryItem leftHandItem)
    {
        this.leftHandItem = leftHandItem;
    }

    public InventoryItem getClothingItem_Head()
    {
        return ClothingItem_Head;
    }

    public void setClothingItem_Head(InventoryItem ClothingItem_Head)
    {
        this.ClothingItem_Head = ClothingItem_Head;
    }

    public InventoryItem getClothingItem_Torso()
    {
        return ClothingItem_Torso;
    }

    public void setClothingItem_Torso(InventoryItem ClothingItem_Torso)
    {
        this.ClothingItem_Torso = ClothingItem_Torso;
    }

    public InventoryItem getClothingItem_Hands()
    {
        return ClothingItem_Hands;
    }

    public void setClothingItem_Hands(InventoryItem ClothingItem_Hands)
    {
        this.ClothingItem_Hands = ClothingItem_Hands;
    }

    public InventoryItem getClothingItem_Legs()
    {
        return ClothingItem_Legs;
    }

    public void setClothingItem_Legs(InventoryItem ClothingItem_Legs)
    {
        this.ClothingItem_Legs = ClothingItem_Legs;
    }

    public InventoryItem getClothingItem_Feet()
    {
        return ClothingItem_Feet;
    }

    public void setClothingItem_Feet(InventoryItem ClothingItem_Feet)
    {
        this.ClothingItem_Feet = ClothingItem_Feet;
    }

    public SequenceBehavior getMasterBehaviorList()
    {
        return masterBehaviorList;
    }

    public void setMasterBehaviorList(SequenceBehavior masterBehaviorList)
    {
        this.masterBehaviorList = masterBehaviorList;
    }

    public int getNextWander()
    {
        return NextWander;
    }

    public void setNextWander(int NextWander)
    {
        this.NextWander = NextWander;
    }

    public boolean isOnFire()
    {
        return OnFire;
    }

    public void setOnFire(boolean OnFire)
    {
        this.OnFire = OnFire;
    }

    public Path getPath()
    {
        return path;
    }

    public void setPath(Path path)
    {
        this.path = path;
    }

    public int getPathIndex()
    {
        return pathIndex;
    }

    public void setPathIndex(int pathIndex)
    {
        this.pathIndex = pathIndex;
    }

    public float getPathSpeed()
    {
        return PathSpeed;
    }

    public void setPathSpeed(float PathSpeed)
    {
        int n;
        if(this == IsoCamera.CamCharacter)
            n = 0;
        this.PathSpeed = PathSpeed;
    }

    public int getPathTargetX()
    {
        return PathTargetX;
    }

    public void setPathTargetX(int PathTargetX)
    {
        this.PathTargetX = PathTargetX;
    }

    public int getPathTargetY()
    {
        return PathTargetY;
    }

    public void setPathTargetY(int PathTargetY)
    {
        this.PathTargetY = PathTargetY;
    }

    public int getPathTargetZ()
    {
        return PathTargetZ;
    }

    public void setPathTargetZ(int PathTargetZ)
    {
        this.PathTargetZ = PathTargetZ;
    }

    public SurvivorPersonality getPersonality()
    {
        return Personality;
    }

    public void setPersonality(SurvivorPersonality Personality)
    {
        this.Personality = Personality;
    }

    public InventoryItem getSecondaryHandItem()
    {
        return rightHandItem;
    }

    public void setSecondaryHandItem(InventoryItem rightHandItem)
    {
        this.rightHandItem = rightHandItem;
    }

    public String getSayLine()
    {
        return sayLine;
    }

    public void setSayLine(String sayLine)
    {
        this.sayLine = sayLine;
    }

    public Color getSpeakColour()
    {
        return SpeakColour;
    }

    public void setSpeakColour(Color SpeakColour)
    {
        this.SpeakColour = SpeakColour;
    }

    public float getSlowFactor()
    {
        return slowFactor;
    }

    public void setSlowFactor(float slowFactor)
    {
        this.slowFactor = slowFactor;
    }

    public int getSlowTimer()
    {
        return slowTimer;
    }

    public void setSlowTimer(int slowTimer)
    {
        this.slowTimer = slowTimer;
    }

    public boolean isbUseParts()
    {
        return bUseParts;
    }

    public void setbUseParts(boolean bUseParts)
    {
        this.bUseParts = bUseParts;
    }

    public boolean isSpeaking()
    {
        return Speaking;
    }

    public void setSpeaking(boolean Speaking)
    {
        this.Speaking = Speaking;
    }

    public int getSpeakTime()
    {
        return SpeakTime;
    }

    public void setSpeakTime(int SpeakTime)
    {
        this.SpeakTime = SpeakTime;
    }

    public float getSpeedMod()
    {
        return speedMod;
    }

    public void setSpeedMod(float speedMod)
    {
        this.speedMod = speedMod;
    }

    public float getStaggerTimeMod()
    {
        return staggerTimeMod;
    }

    public void setStaggerTimeMod(float staggerTimeMod)
    {
        this.staggerTimeMod = staggerTimeMod;
    }

    public StateMachine getStateMachine()
    {
        return stateMachine;
    }

    public void setStateMachine(StateMachine stateMachine)
    {
        this.stateMachine = stateMachine;
    }

    public Moodles getMoodles()
    {
        return Moodles;
    }

    public void setMoodles(Moodles Moodles)
    {
        this.Moodles = Moodles;
    }

    public Stats getStats()
    {
        return stats;
    }

    public void setStats(Stats stats)
    {
        this.stats = stats;
    }

    public Stack getTagGroup()
    {
        return TagGroup;
    }

    public void setTagGroup(Stack TagGroup)
    {
        this.TagGroup = TagGroup;
    }

    public Stack getUsedItemsOn()
    {
        return UsedItemsOn;
    }

    public void setUsedItemsOn(Stack UsedItemsOn)
    {
        this.UsedItemsOn = UsedItemsOn;
    }

    public HandWeapon getUseHandWeapon()
    {
        return useHandWeapon;
    }

    public void setUseHandWeapon(HandWeapon useHandWeapon)
    {
        this.useHandWeapon = useHandWeapon;
    }

    public IsoSprite getTorsoSprite()
    {
        return torsoSprite;
    }

    public void setTorsoSprite(IsoSprite torsoSprite)
    {
        this.torsoSprite = torsoSprite;
    }

    public IsoSprite getLegsSprite()
    {
        return legsSprite;
    }

    public void setLegsSprite(IsoSprite legsSprite)
    {
        this.legsSprite = legsSprite;
    }

    public IsoSprite getHeadSprite()
    {
        return headSprite;
    }

    public void setHeadSprite(IsoSprite headSprite)
    {
        this.headSprite = headSprite;
    }

    public IsoSprite getShoeSprite()
    {
        return shoeSprite;
    }

    public void setShoeSprite(IsoSprite shoeSprite)
    {
        this.shoeSprite = shoeSprite;
    }

    public IsoSprite getTopSprite()
    {
        return topSprite;
    }

    public void setTopSprite(IsoSprite topSprite)
    {
        this.topSprite = topSprite;
    }

    public IsoSprite getBottomsSprite()
    {
        return bottomsSprite;
    }

    public void setBottomsSprite(IsoSprite bottomsSprite)
    {
        this.bottomsSprite = bottomsSprite;
    }

    public Stack getWounds()
    {
        return wounds;
    }

    public void setWounds(Stack wounds)
    {
        this.wounds = wounds;
    }

    public IsoGridSquare getAttackTargetSquare()
    {
        return attackTargetSquare;
    }

    public void setAttackTargetSquare(IsoGridSquare attackTargetSquare)
    {
        this.attackTargetSquare = attackTargetSquare;
    }

    public float getBloodImpactX()
    {
        return BloodImpactX;
    }

    public void setBloodImpactX(float BloodImpactX)
    {
        this.BloodImpactX = BloodImpactX;
    }

    public float getBloodImpactY()
    {
        return BloodImpactY;
    }

    public void setBloodImpactY(float BloodImpactY)
    {
        this.BloodImpactY = BloodImpactY;
    }

    public float getBloodImpactZ()
    {
        return BloodImpactZ;
    }

    public void setBloodImpactZ(float BloodImpactZ)
    {
        this.BloodImpactZ = BloodImpactZ;
    }

    public IsoSprite getBloodSplat()
    {
        return bloodSplat;
    }

    public void setBloodSplat(IsoSprite bloodSplat)
    {
        this.bloodSplat = bloodSplat;
    }

    public boolean isbOnBed()
    {
        return bOnBed;
    }

    public void setbOnBed(boolean bOnBed)
    {
        this.bOnBed = bOnBed;
    }

    public Vector2 getMoveForwardVec()
    {
        return moveForwardVec;
    }

    public void setMoveForwardVec(Vector2 moveForwardVec)
    {
        this.moveForwardVec = moveForwardVec;
    }

    public boolean isPathing()
    {
        return pathing;
    }

    public void setPathing(boolean pathing)
    {
        this.pathing = pathing;
    }

    public Stack getLocalEnemyList()
    {
        return LocalEnemyList;
    }

    public void setLocalEnemyList(Stack LocalEnemyList)
    {
        this.LocalEnemyList = LocalEnemyList;
    }

    public Stack getEnemyList()
    {
        return EnemyList;
    }

    public void setEnemyList(Stack EnemyList)
    {
        this.EnemyList = EnemyList;
    }

    public NulledArrayList getTraits()
    {
        return Traits;
    }

    public void setTraits(NulledArrayList Traits)
    {
        this.Traits = Traits;
    }

    public Integer getMaxWeight()
    {
        return maxWeight;
    }

    public void setMaxWeight(Integer maxWeight)
    {
        this.maxWeight = maxWeight;
    }

    public int getMaxWeightBase()
    {
        return maxWeightBase;
    }

    public void setMaxWeightBase(int maxWeightBase)
    {
        this.maxWeightBase = maxWeightBase;
    }

    public float getSleepingTabletDelta()
    {
        return SleepingTabletDelta;
    }

    public void setSleepingTabletDelta(float SleepingTabletDelta)
    {
        this.SleepingTabletDelta = SleepingTabletDelta;
    }

    public int getBetaEffect()
    {
        return BetaEffect;
    }

    public void setBetaEffect(int BetaEffect)
    {
        this.BetaEffect = BetaEffect;
    }

    public int getDepressEffect()
    {
        return DepressEffect;
    }

    public void setDepressEffect(int DepressEffect)
    {
        this.DepressEffect = DepressEffect;
    }

    public int getSleepingTabletEffect()
    {
        return SleepingTabletEffect;
    }

    public void setSleepingTabletEffect(int SleepingTabletEffect)
    {
        this.SleepingTabletEffect = SleepingTabletEffect;
    }

    public float getBetaDelta()
    {
        return BetaDelta;
    }

    public void setBetaDelta(float BetaDelta)
    {
        this.BetaDelta = BetaDelta;
    }

    public float getDepressDelta()
    {
        return DepressDelta;
    }

    public void setDepressDelta(float DepressDelta)
    {
        this.DepressDelta = DepressDelta;
    }

    public int getPainEffect()
    {
        return PainEffect;
    }

    public void setPainEffect(int PainEffect)
    {
        this.PainEffect = PainEffect;
    }

    public float getPainDelta()
    {
        return PainDelta;
    }

    public void setPainDelta(float PainDelta)
    {
        this.PainDelta = PainDelta;
    }

    public boolean isSpottedSinceAlphaZero()
    {
        return SpottedSinceAlphaZero;
    }

    public void setSpottedSinceAlphaZero(boolean SpottedSinceAlphaZero)
    {
        this.SpottedSinceAlphaZero = SpottedSinceAlphaZero;
    }

    public boolean isbDoDefer()
    {
        return bDoDefer;
    }

    public void setbDoDefer(boolean bDoDefer)
    {
        this.bDoDefer = bDoDefer;
    }

    public Location getLastHeardSound()
    {
        return LastHeardSound;
    }

    public void setLastHeardSound(Location LastHeardSound)
    {
        this.LastHeardSound = LastHeardSound;
    }

    public float getLrx()
    {
        return lrx;
    }

    public void setLrx(float lrx)
    {
        this.lrx = lrx;
    }

    public float getLry()
    {
        return lry;
    }

    public void setLry(float lry)
    {
        this.lry = lry;
    }

    public boolean isClimbing()
    {
        return bClimbing;
    }

    public void setbClimbing(boolean bClimbing)
    {
        this.bClimbing = bClimbing;
    }

    public boolean isLastCollidedW()
    {
        return lastCollidedW;
    }

    public void setLastCollidedW(boolean lastCollidedW)
    {
        this.lastCollidedW = lastCollidedW;
    }

    public boolean isLastCollidedN()
    {
        return lastCollidedN;
    }

    public void setLastCollidedN(boolean lastCollidedN)
    {
        this.lastCollidedN = lastCollidedN;
    }

    public int getTimeTillForgetLocation()
    {
        return timeTillForgetLocation;
    }

    public void setTimeTillForgetLocation(int timeTillForgetLocation)
    {
        this.timeTillForgetLocation = timeTillForgetLocation;
    }

    public int getFallTime()
    {
        return fallTime;
    }

    public void setFallTime(int fallTime)
    {
        this.fallTime = fallTime;
    }

    public float getLastFallSpeed()
    {
        return lastFallSpeed;
    }

    public void setLastFallSpeed(float lastFallSpeed)
    {
        this.lastFallSpeed = lastFallSpeed;
    }

    public boolean isbFalling()
    {
        return bFalling;
    }

    public void setbFalling(boolean bFalling)
    {
        this.bFalling = bFalling;
    }

    public IsoBuilding getCurrentBuilding()
    {
        if(current == null)
            return null;
        if(current.getRoom() == null)
            return null;
        else
            return current.getRoom().building;
    }

    public boolean IsSneaking()
    {
        return getMovementLastFrame().getLength() < 0.04F;
    }

    public float getHammerSoundMod()
    {
        int level = getPerkLevel(zombie.characters.skills.PerkFactory.Perks.Woodwork);
        if(level == 2)
            return 0.8F;
        if(level == 3)
            return 0.6F;
        if(level == 4)
            return 0.4F;
        return level != 5 ? 1.0F : 0.4F;
    }

    public float getBarricadeTimeMod()
    {
        int level = getPerkLevel(zombie.characters.skills.PerkFactory.Perks.Woodwork);
        if(level == 1)
            return 0.8F;
        if(level == 2)
            return 0.6F;
        if(level == 3)
            return 0.4F;
        if(level == 4)
            return 0.2F;
        return level != 5 ? 1.0F : 0.2F;
    }

    public float getBarricadeStrengthMod()
    {
        int level = getPerkLevel(zombie.characters.skills.PerkFactory.Perks.Woodwork);
        if(level == 2)
            return 1.1F;
        if(level == 3)
            return 1.2F;
        if(level == 4)
            return 1.5F;
        return level != 5 ? 1.0F : 1.5F;
    }

    public float getSneakSpotMod()
    {
        int level = getPerkLevel(zombie.characters.skills.PerkFactory.Perks.Sneak);
        if(level == 1)
            return 0.9F;
        if(level == 2)
            return 0.8F;
        if(level == 3)
            return 0.7F;
        if(level == 4)
            return 0.6F;
        return level != 5 ? 1.0F : 0.4F;
    }

    public float getNimbleMod()
    {
        int level = getPerkLevel(zombie.characters.skills.PerkFactory.Perks.Nimble);
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

    public float getFatigueMod()
    {
        int level = getPerkLevel(zombie.characters.skills.PerkFactory.Perks.Swinging);
        if(level == 1)
            return 0.95F;
        if(level == 2)
            return 0.9F;
        if(level == 3)
            return 0.85F;
        if(level == 4)
            return 0.8F;
        return level != 5 ? 1.0F : 0.75F;
    }

    public float getLightfootMod()
    {
        int level = getPerkLevel(zombie.characters.skills.PerkFactory.Perks.Lightfoot);
        if(level == 1)
            return 0.9F;
        if(level == 2)
            return 0.8F;
        if(level == 3)
            return 0.6F;
        if(level == 4)
            return 0.4F;
        return level != 5 ? 1.0F : 0.2F;
    }

    public float getPacingMod()
    {
        int level = getPerkLevel(zombie.characters.skills.PerkFactory.Perks.Pacing);
        if(level == 1)
            return 0.6F;
        if(level == 2)
            return 0.4F;
        if(level == 3)
            return 0.3F;
        if(level == 4)
            return 0.2F;
        return level != 5 ? 0.8F : 0.1F;
    }

    public float getHittingMod()
    {
        int level = getPerkLevel(zombie.characters.skills.PerkFactory.Perks.Hitting);
        if(level == 1)
            return 1.05F;
        if(level == 2)
            return 1.1F;
        if(level == 3)
            return 1.15F;
        if(level == 4)
            return 1.2F;
        return level != 5 ? 1.0F : 1.25F;
    }

    public float getShovingMod()
    {
        int level = getPerkLevel(zombie.characters.skills.PerkFactory.Perks.Shoving);
        if(level == 1)
            return 1.05F;
        if(level == 2)
            return 1.1F;
        if(level == 3)
            return 1.15F;
        if(level == 4)
            return 1.2F;
        return level != 5 ? 1.0F : 1.25F;
    }

    public float getRecoveryMod()
    {
        int level = getPerkLevel(zombie.characters.skills.PerkFactory.Perks.Recovery);
        if(level == 1)
            return 1.2F;
        if(level == 2)
            return 1.3F;
        if(level == 3)
            return 1.4F;
        if(level == 4)
            return 1.5F;
        return level != 5 ? 1.1F : 1.6F;
    }

    public float getWeightMod()
    {
        int level = getPerkLevel(zombie.characters.skills.PerkFactory.Perks.Hauling);
        if(level == 1)
            return 1.2F;
        if(level == 2)
            return 1.5F;
        if(level == 3)
            return 1.75F;
        if(level == 4)
            return 2.0F;
        return level != 5 ? 1.0F : 2.5F;
    }

    public int getHitChancesMod()
    {
        int level = getPerkLevel(zombie.characters.skills.PerkFactory.Perks.Aiming);
        if(level == 1)
            return 1;
        if(level == 2)
            return 2;
        if(level == 3)
            return 3;
        if(level == 4)
            return 4;
        return level != 5 ? 1 : 5;
    }

    public float getSprintMod()
    {
        int level = getPerkLevel(zombie.characters.skills.PerkFactory.Perks.Sprinting);
        if(level == 1)
            return 1.1F;
        if(level == 2)
            return 1.2F;
        if(level == 3)
            return 1.3F;
        if(level == 4)
            return 1.4F;
        return level != 5 ? 1.0F : 1.6F;
    }

    public int getPerkLevel(zombie.characters.skills.PerkFactory.Perks perks)
    {
        for(int n = 0; n < IsoPlayer.instance.PerkList.size(); n++)
        {
            PerkInfo info = (PerkInfo)IsoPlayer.instance.PerkList.get(n);
            if(info.perkType == perks)
                return info.level;
        }

        return 0;
    }

    public void TestZombieSpotPlayer(Vector2 vecA, IsoMovingObject chr)
    {
        chr.spotted(this);
    }

    public void LevelPerk(zombie.characters.skills.PerkFactory.Perks perk)
    {
        NumberOfPerksToPick--;
        PerkInfo info;
        for(int n = 0; n < IsoPlayer.instance.PerkList.size(); n++)
        {
            info = (PerkInfo)IsoPlayer.instance.PerkList.get(n);
            if(info.perkType == perk)
            {
                info.level++;
                if(info.level > 5)
                    info.level = 5;
                return;
            }
        }

        zombie.characters.skills.PerkFactory.Perk theperk = (zombie.characters.skills.PerkFactory.Perk)PerkFactory.PerkMap.get(perk);
        info = new PerkInfo();
        info.perk = theperk;
        info.perkType = perk;
        info.level = 1;
        PerkList.add(info);
    }

    public void LevelUp()
    {
        NumberOfPerksToPick++;
    }

    public void GiveOrder(Order order, boolean bCancelOrderStack)
    {
        if(bCancelOrderStack)
            Orders.clear();
        if(order.character != this)
            order.character = this;
        Orders.push(order);
    }

    public void GivePersonalNeed(Order personalNeed)
    {
        if(personalNeed.character != this)
            personalNeed.character = this;
        if(personalNeed.isCritical())
            PersonalNeeds.push(personalNeed);
        else
            PersonalNeeds.insertElementAt(personalNeed, 0);
    }

    public Location getLastKnownLocationOf(String character)
    {
        if(LastKnownLocation.containsKey(character))
            return (Location)LastKnownLocation.get(character);
        else
            return null;
    }

    public boolean ReadLiterature(InventoryItem info)
    {
        if(info instanceof Literature)
        {
            Literature literature = (Literature)info;
            if(literature.requireInHandOrInventory != null && literature.useOnConsume != null)
            {
                InventoryItem item = null;
                item = FindAndReturn(literature.requireInHandOrInventory, leftHandItem);
                if(item != null && literature.useOnConsume.equals(literature.requireInHandOrInventory))
                    inventory.RemoveOneOf(literature.useOnConsume);
                if(item == null)
                {
                    item = FindAndReturn(literature.requireInHandOrInventory, rightHandItem);
                    if(item != null && literature.useOnConsume.equals(literature.requireInHandOrInventory))
                        inventory.RemoveOneOf(literature.useOnConsume);
                }
                if(item == null)
                {
                    item = inventory.FindAndReturn(literature.requireInHandOrInventory);
                    if(item != null && literature.useOnConsume.equals(literature.requireInHandOrInventory))
                        inventory.RemoveOneOf(literature.useOnConsume);
                }
                if(item == null)
                    return false;
            }
            stats.boredom -= literature.getBoredomChange();
            stats.stress -= literature.getStressChange();
            return true;
        } else
        {
            return false;
        }
    }

    protected void OnDeath()
    {
        if(this instanceof IsoPlayer)
        {
            File file = new File((new StringBuilder()).append(GameWindow.getGameModeCacheDir()).append(File.separator).append("map_p.bin").toString());
            if(file.exists())
                file.delete();
        }
    }

    public boolean IsArmed()
    {
        return inventory.getBestWeapon(descriptor) != null;
    }

    public void dripBloodFloor(float alpha)
    {
        Integer idf = Integer.valueOf(32 + Rand.Next(8));
        DoFloorSplat(getCurrentSquare(), (new StringBuilder()).append("BloodFloor_").append(idf).toString(), Rand.Next(2) == 0, 0.0F, alpha);
    }

    public void splatBloodFloor(float alpha)
    {
        IsoGridSquare w = getCurrentSquare();
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
        if(w != null)
            if(Rand.Next(3) == 0)
            {
                Integer idf = Integer.valueOf(16);
                DoFloorSplat(w, (new StringBuilder()).append("BloodFloor_").append(idf).toString(), isSolid(), 0.0F, alpha);
                if(w != null)
                    DoFloorSplat(w.getW(), (new StringBuilder()).append("BloodFloor_").append(idf.intValue() + 1).toString(), isSolid(), 0.0F, alpha);
            } else
            if(Rand.Next(3) == 0)
            {
                Integer idf = Integer.valueOf(8);
                DoFloorSplat(w, (new StringBuilder()).append("BloodFloor_").append(idf).toString(), isSolid(), 0.0F, alpha);
                if(w != null)
                    DoFloorSplat(w.getS(), (new StringBuilder()).append("BloodFloor_").append(idf.intValue() + 1).toString(), isSolid(), 0.0F, alpha);
            } else
            if(Rand.Next(3) == 0)
            {
                Integer idf = Integer.valueOf(24);
                DoFloorSplat(w, (new StringBuilder()).append("BloodFloor_").append(idf).toString(), isSolid(), 0.0F, alpha);
                if(w != null)
                {
                    DoFloorSplat(w.getW(), (new StringBuilder()).append("BloodFloor_").append(idf.intValue() + 1).toString(), isSolid(), 0.0F, alpha);
                    DoFloorSplat(w.getS(), (new StringBuilder()).append("BloodFloor_").append(idf.intValue() + 2).toString(), isSolid(), 0.0F, alpha);
                    if(w.getS() != null)
                        DoFloorSplat(w.getS().getW(), (new StringBuilder()).append("BloodFloor_").append(idf.intValue() + 3).toString(), isSolid(), 0.0F, alpha);
                }
            } else
            {
                Integer idf = Integer.valueOf(Rand.Next(3));
                DoFloorSplat(w, (new StringBuilder()).append("BloodFloor_").append(idf).toString(), isSolid(), 0.0F, alpha);
            }
    }

    public void Scratched()
    {
    }

    public void Bitten()
    {
    }

    public int getThreatLevel()
    {
        int total = LocalRelevantEnemyList.size();
        total += VeryCloseEnemyList.size() * 10;
        if(total > 20)
            return 3;
        if(total > 10)
            return 2;
        return total <= 0 ? 0 : 1;
    }

    public boolean InBuildingWith(IsoGameCharacter chr)
    {
        if(getCurrentSquare() == null)
            return false;
        if(chr.getCurrentSquare() == null)
            return false;
        if(getCurrentSquare().getRoom() == null)
            return false;
        if(chr.getCurrentSquare().getRoom() == null)
            return false;
        else
            return chr.getCurrentSquare().getRoom().building == getCurrentSquare().getRoom().building;
    }

    public boolean InRoomWith(IsoGameCharacter chr)
    {
        if(getCurrentSquare() == null)
            return false;
        if(chr.getCurrentSquare() == null)
            return false;
        if(getCurrentSquare().getRoom() == null)
            return false;
        if(chr.getCurrentSquare().getRoom() == null)
            return false;
        else
            return chr.getCurrentSquare().getRoom() == getCurrentSquare().getRoom();
    }

    public boolean isDead()
    {
        return Health <= 0.0F || BodyDamage.getHealth() <= 0.0F;
    }

    public boolean IsInBuilding(IsoBuilding building)
    {
        if(getCurrentSquare() == null)
            return false;
        if(getCurrentSquare().getRoom() == null)
            return false;
        else
            return getCurrentSquare().getRoom().building == building;
    }

    public void Seen(Stack SeenList)
    {
        synchronized(LocalList)
        {
            LocalList.clear();
            LocalList.addAll(SeenList);
        }
    }

    public boolean CanSee(IsoMovingObject obj)
    {
        return LosUtil.lineClear(getCell(), (int)getX(), (int)getY(), (int)getZ(), (int)obj.getX(), (int)obj.getY(), (int)obj.getZ(), false) != zombie.iso.LosUtil.TestResults.Blocked;
    }

    public IsoGridSquare getLowDangerInVicinity(int attempts, int range)
    {
        float highscore = -1000000F;
        IsoGridSquare chosen = null;
        for(int n = 0; n < attempts; n++)
        {
            float score = 0.0F;
            int randx = Rand.Next(-range, range);
            int randy = Rand.Next(-range, range);
            IsoGridSquare sq = getCell().getGridSquare((int)getX() + randx, (int)getY() + randy, (int)getZ());
            if(sq == null || !sq.isFree(true))
                continue;
            float total = sq.getMovingObjects().size();
            if(sq.getE() != null)
                total += sq.getE().getMovingObjects().size();
            if(sq.getS() != null)
                total += sq.getS().getMovingObjects().size();
            if(sq.getW() != null)
                total += sq.getW().getMovingObjects().size();
            if(sq.getN() != null)
                total += sq.getN().getMovingObjects().size();
            score -= total * 1000F;
            if(score > highscore)
            {
                highscore = score;
                chosen = sq;
            }
        }

        return chosen;
    }

    public void SetAnim(int animID)
    {
        if(!bUseParts)
        {
            sprite.CurrentAnim = (IsoAnim)sprite.AnimStack.get(animID);
        } else
        {
            legsSprite.CurrentAnim = (IsoAnim)legsSprite.AnimStack.get(animID);
            if(torsoSprite != null)
                torsoSprite.CurrentAnim = (IsoAnim)torsoSprite.AnimStack.get(animID);
            if(headSprite != null)
                headSprite.CurrentAnim = (IsoAnim)headSprite.AnimStack.get(animID);
            if(bottomsSprite != null)
                bottomsSprite.CurrentAnim = (IsoAnim)bottomsSprite.AnimStack.get(animID);
            if(shoeSprite != null)
                shoeSprite.CurrentAnim = (IsoAnim)shoeSprite.AnimStack.get(animID);
            if(topSprite != null)
                topSprite.CurrentAnim = (IsoAnim)topSprite.AnimStack.get(animID);
            EnforceAnims();
        }
    }

    private void EnforceAnims()
    {
        if(headSprite == null);
        if(bottomsSprite == null);
        if(shoeSprite == null);
        if(topSprite == null);
    }

    public void Anger(int amount)
    {
        float angerchance = descriptor.temper * 10F;
        if((float)Rand.Next(100) < angerchance)
            amount *= 2;
        amount = (int)((float)amount * (stats.stress + 1.0F));
        amount = (int)((float)amount * (BodyDamage.getUnhappynessLevel() / 100F + 1.0F));
        stats.Anger += (float)amount / 100F;
    }

    public boolean hasEquipped(String String)
    {
        if(leftHandItem != null && leftHandItem.getType().equals(String))
            return true;
        return rightHandItem != null && rightHandItem.getType().equals(String);
    }

    public void setDir(IsoDirections directions)
    {
        int fdsfsd;
        if(stateMachine.getCurrent() == StaggerBackDieState.instance() && dir != directions)
            fdsfsd = 0;
        dir = directions;
        angle.x = getVectorFromDirection().x;
        angle.y = getVectorFromDirection().y;
    }

    public void SetClothing(zombie.scripting.objects.Item.ClothingBodyLocation loc, String sprite, String palette)
    {
        if(loc == zombie.scripting.objects.Item.ClothingBodyLocation.Top)
            if(sprite == null)
            {
                topSprite = null;
            } else
            {
                topSprite = IsoSprite.CreateSprite(BucketManager.Shared().SpriteManager);
                DoCharacterPart(palette, topSprite);
            }
        if(loc == zombie.scripting.objects.Item.ClothingBodyLocation.Bottoms)
            if(sprite == null)
            {
                bottomsSprite = null;
            } else
            {
                bottomsSprite = IsoSprite.CreateSprite(BucketManager.Shared().SpriteManager);
                DoCharacterPart(palette, bottomsSprite);
            }
        if(loc == zombie.scripting.objects.Item.ClothingBodyLocation.Shoes)
            if(sprite == null)
            {
                shoeSprite = null;
            } else
            {
                shoeSprite = IsoSprite.CreateSprite(BucketManager.Shared().SpriteManager);
                DoCharacterPart(palette, shoeSprite);
            }
    }

    public void Callout()
    {
        switch(Rand.Next(3))
        {
        case 0: // '\0'
            Say("Hey!");
            break;

        case 1: // '\001'
            Say("Over here!");
            break;

        case 2: // '\002'
            Say("Hey you!");
            break;
        }
        WorldSoundManager.instance.addSound(this, (int)getX(), (int)getY(), (int)getZ(), 20, 50, false);
        for(int n = 0; n < getCell().getObjectList().size(); n++)
        {
            IsoMovingObject o = (IsoMovingObject)getCell().getObjectList().get(n);
            if(!(o instanceof IsoSurvivor))
                continue;
            float dist = o.DistTo(this);
            if(dist < 15F)
                ((IsoSurvivor)o).Orders.push(new StopAndFaceForOrder((IsoGameCharacter)o, this, 60));
        }

    }

    public void Kill(IsoGameCharacter killer)
    {
        Health = -1F;
        DoDeath(null, killer);
    }

    public void load(ByteBuffer input)
        throws IOException
    {
        super.load(input);
        if(input.get() == 1)
        {
            descriptor = new SurvivorDesc(true);
            descriptor.load(input, this);
        }
        inventory.load(input);
        Asleep = input.get() == 1;
        ForceWakeUpTime = input.getFloat();
        if(!(this instanceof IsoZombie))
        {
            NumberOfPerksToPick = input.getInt();
            stats.load(input);
            BodyDamage.load(input);
            xp.load(input);
            if(input.get() == 1)
                leftHandItem = inventory.getBestCondition(GameWindow.ReadString(input));
            if(input.get() == 1)
                rightHandItem = inventory.getBestCondition(GameWindow.ReadString(input));
        }
    }

    public void save(ByteBuffer output)
        throws IOException
    {
        super.save(output);
        if(descriptor == null)
        {
            output.put((byte)0);
        } else
        {
            output.put((byte)1);
            descriptor.save(output);
        }
        inventory.save(output);
        output.put(((byte)(Asleep ? 1 : 0)));
        output.putFloat(ForceWakeUpTime);
        if(!(this instanceof IsoZombie))
        {
            output.putInt(NumberOfPerksToPick);
            stats.save(output);
            BodyDamage.save(output);
            xp.save(output);
            if(leftHandItem != null)
            {
                output.put((byte)1);
                GameWindow.WriteString(output, leftHandItem.getFullType());
            } else
            {
                output.put((byte)0);
            }
            if(rightHandItem != null)
            {
                output.put((byte)1);
                GameWindow.WriteString(output, rightHandItem.getFullType());
            } else
            {
                output.put((byte)0);
            }
        }
    }

    public void StartAction(BaseAction act)
    {
        CharacterActions.insertElementAt(act, 0);
        if(act.valid())
            act.start();
    }

    public void QueueAction(BaseAction act)
    {
        CharacterActions.add(act);
    }

    public void StopAllActionQueue()
    {
        if(CharacterActions.size() == 0)
            return;
        BaseAction act = (BaseAction)CharacterActions.get(0);
        act.stop();
        CharacterActions.clear();
        if(this == IsoPlayer.instance)
            UIManager.getProgressBar().setValue(0.0F);
    }

    public void StopAllActionQueueRunning()
    {
        if(CharacterActions.size() == 0)
            return;
        BaseAction act = (BaseAction)CharacterActions.get(0);
        if(!act.StopOnRun)
            return;
        act.stop();
        CharacterActions.clear();
        if(this == IsoPlayer.instance)
            UIManager.getProgressBar().setValue(0.0F);
    }

    public void StopAllActionQueueWalking()
    {
        if(CharacterActions.size() == 0)
            return;
        BaseAction act = (BaseAction)CharacterActions.get(0);
        if(!act.StopOnWalk)
            return;
        act.stop();
        CharacterActions.clear();
        if(this == IsoPlayer.instance)
            UIManager.getProgressBar().setValue(0.0F);
    }

    public IsoGameCharacter(IsoCell cell, float x, float y, float z)
    {
        super(cell);
        FollowingTarget = null;
        LocalList = new NulledArrayList();
        LocalNeutralList = new NulledArrayList();
        LocalGroupList = new NulledArrayList();
        LocalRelevantEnemyList = new NulledArrayList();
        dangerLevels = 0.0F;
        MeetList = new Stack();
        Order = null;
        Orders = new Stack();
        PerkList = new ArrayList();
        PersonalNeed = null;
        PersonalNeeds = new Stack();
        leaveBodyTimedown = -1;
        AllowConversation = true;
        Reanim = false;
        VisibleToNPCs = true;
        DieCount = 0;
        llx = 0.0F;
        lly = 0.0F;
        llz = 0.0F;
        RemoteID = -1;
        NumSurvivorsInVicinity = 0;
        LevelUpMultiplier = 5F;
        xp = null;
        NumberOfPerksToPick = 0;
        CanUpgradePerk = new NulledArrayList();
        LastLocalEnemies = 0;
        VeryCloseEnemyList = new NulledArrayList();
        LastKnownLocation = new THashMap();
        AttackedBy = null;
        IgnoreStaggerBack = false;
        AttackWasSuperAttack = false;
        TimeThumping = 0;
        PatienceMax = 150;
        PatienceMin = 20;
        Patience = 20;
        CharacterActions = new Stack();
        angle = new Vector2();
        Asleep = false;
        AttackDelay = 0;
        AttackDelayMax = 0;
        AttackDelayUse = 0;
        ZombieKills = 0;
        LastZombieKills = 0;
        superAttack = false;
        ForceWakeUpTime = -1F;
        BodyDamage = null;
        FamiliarBuildings = new Stack();
        finder = new AStarPathFinderResult();
        FireKillRate = 0.0038F;
        FireSpreadProbability = 6;
        footStepCounter = 0;
        footStepCounterMax = 10;
        Health = 1.0F;
        masterProper = null;
        hurtSound = "zombiehit";
        IgnoreMovementForDirection = false;
        inventory = new ItemContainer();
        masterBehaviorList = new SequenceBehavior();
        NextWander = 200;
        OnFire = false;
        pathIndex = 0;
        PathSpeed = 0.03F;
        PathTargetX = -1;
        PathTargetY = -1;
        PathTargetZ = -1;
        Personality = null;
        SpeakColour = new Color(1.0F, 1.0F, 1.0F, 1.0F);
        slowFactor = 0.0F;
        slowTimer = 0;
        bUseParts = false;
        Speaking = false;
        SpeakTime = 0;
        speedMod = 1.0F;
        staggerTimeMod = 1.0F;
        stats = new Stats();
        TagGroup = new Stack();
        UsedItemsOn = new Stack();
        useHandWeapon = null;
        wounds = new Stack();
        BloodImpactX = 0.0F;
        BloodImpactY = 0.0F;
        BloodImpactZ = 0.0F;
        bOnBed = false;
        moveForwardVec = new Vector2();
        pathing = false;
        LocalEnemyList = new Stack();
        EnemyList = new Stack();
        Traits = new NulledArrayList(1);
        maxWeight = Integer.valueOf(150);
        maxWeightBase = 150;
        SleepingTabletDelta = 1.0F;
        BetaEffect = 0;
        DepressEffect = 0;
        SleepingTabletEffect = 0;
        BetaDelta = 0.0F;
        DepressDelta = 0.0F;
        PainEffect = 0;
        PainDelta = 0.0F;
        SpottedSinceAlphaZero = false;
        bDoDefer = true;
        LastHeardSound = new Location(-1, -1, -1);
        lrx = 0.0F;
        lry = 0.0F;
        bClimbing = false;
        lastCollidedW = false;
        lastCollidedN = false;
        timeTillForgetLocation = 0;
        fallTime = 0;
        lastFallSpeed = 0.0F;
        bFalling = false;
        if(def == null)
            def = new IsoSpriteInstance(sprite);
        if(!(this instanceof IsoZombie))
        {
            Moodles = new Moodles(this);
            xp = new XP(this);
        }
        Patience = Rand.Next(PatienceMin, PatienceMax);
        BodyDamage = new BodyDamage(this);
        this.x = x + 0.5F;
        this.y = y + 0.5F;
        this.z = z;
        scriptnx = lx = nx = x;
        scriptny = ly = ny = y;
        current = getCell().getGridSquare((int)x, (int)y, (int)z);
        offsetY = -254F;
        offsetX = -32F;
        stateMachine = new StateMachine(this);
        defaultState = IdleState.instance();
        inventory.parent = this;
    }

    public void SleepingTablet(float SleepingTabletDelta)
    {
        SleepingTabletEffect = 6600;
        this.SleepingTabletDelta += 0.1F;
    }

    public void BetaBlockers(float delta)
    {
        BetaEffect = 6600;
        BetaDelta += delta;
    }

    public void BetaAntiDepress(float delta)
    {
        BetaEffect = 1800;
        DepressEffect = 6600;
        BetaDelta += delta;
        DepressDelta += delta;
    }

    public void PainMeds(float delta)
    {
        PainEffect = 300;
        PainDelta += delta;
    }

    public void DoCharacterPart(String partName, IsoSprite spr)
    {
        spr.LoadFrames(partName, "Run", 11);
        spr.LoadFrames(partName, "Run_Weapon2", 11);
        spr.LoadFrames(partName, "Idle_Weapon2", 6);
        spr.LoadFrames(partName, "Walk", 11);
        spr.LoadFrames(partName, "ZombieDeath", 14);
        spr.LoadFrames(partName, "Attack_Bat", 15);
        spr.LoadFrames(partName, "Attack_Sledgehammer", 15);
        spr.LoadFrames(partName, "Attack_Handgun", 6);
        spr.LoadFrames(partName, "Attack_Rifle", 6);
        spr.LoadFrames(partName, "Attack_Stab", 8);
        spr.LoadFrames(partName, "Attack_Shove", 11);
        spr.LoadFrames(partName, "Idle", 6);
    }

    public void DoZombiePart(String partName, IsoSprite spr)
    {
        spr.LoadFrames(partName, "ZombieDoor", 11);
        spr.LoadFrames(partName, "ZombieBite", 21);
        spr.LoadFrames(partName, "ZombieDeath", 14);
        spr.LoadFrames(partName, "ZombieStaggerBack", 10);
        spr.LoadFrames(partName, "ZombieGetUp", 15);
        spr.LoadFrames(partName, "ZombieWalk1", 10);
        spr.LoadFrames(partName, "ZombieWalk2", 10);
        spr.LoadFrames(partName, "ZombieWalk3", 10);
    }

    public void InitSpriteParts(String legs, String torso, String head, String top, String bottoms, String shoe, String skinpal, 
            String toppal, String bottomspal, String shoespal)
    {
        legsSprite = sprite;
        DoCharacterPart("Bob", legsSprite);
        topSprite = IsoSprite.CreateSprite(BucketManager.Shared().SpriteManager);
        DoCharacterPart(toppal, topSprite);
        bottomsSprite = IsoSprite.CreateSprite(BucketManager.Shared().SpriteManager);
        DoCharacterPart(bottomspal, bottomsSprite);
        bUseParts = true;
    }

    public void InitSpritePartsZombie(String SpriteName, String legs, String torso, String head, String top, String bottoms, String shoe, 
            String skinpal, String toppal, String bottomspal, String shoespal)
    {
        legsSprite = sprite;
        if(IsoSprite.HasCache(SpriteName))
        {
            legsSprite.LoadCache(SpriteName);
        } else
        {
            DoZombiePart(SpriteName, legsSprite);
            legsSprite.CacheAnims(SpriteName);
        }
        topSprite = IsoSprite.CreateSprite(BucketManager.Shared().SpriteManager);
        if(IsoSprite.HasCache((new StringBuilder()).append(toppal).append("z").toString()))
        {
            topSprite.LoadCache((new StringBuilder()).append(toppal).append("z").toString());
        } else
        {
            DoZombiePart(toppal, topSprite);
            topSprite.CacheAnims((new StringBuilder()).append(toppal).append("z").toString());
        }
        bottomsSprite = IsoSprite.CreateSprite(BucketManager.Shared().SpriteManager);
        if(IsoSprite.HasCache((new StringBuilder()).append(bottomspal).append("z").toString()))
        {
            bottomsSprite.LoadCache((new StringBuilder()).append(bottomspal).append("z").toString());
        } else
        {
            DoZombiePart(bottomspal, bottomsSprite);
            bottomsSprite.CacheAnims((new StringBuilder()).append(bottomspal).append("z").toString());
        }
        bUseParts = true;
    }

    public boolean HasTrait(String trait)
    {
        return Traits.contains(trait);
    }

    public void ApplyInBedOffset(boolean apply)
    {
        if(apply)
        {
            if(!bOnBed)
            {
                offsetX -= 20F;
                offsetY += 21F;
                bOnBed = true;
            }
        } else
        if(bOnBed)
        {
            offsetX += 20F;
            offsetY -= 21F;
            bOnBed = false;
        }
    }

    public void Dressup(SurvivorDesc desc)
    {
        InventoryItem bottoms = null;
        InventoryItem top = null;
        InventoryItem shoes = null;
        if(desc.bottomspal != null)
            bottoms = inventory.AddItem(Clothing.CreateFromSprite((new StringBuilder()).append(desc.bottoms).append("#").append(desc.bottomspal).toString()));
        else
            bottoms = inventory.AddItem(Clothing.CreateFromSprite(desc.bottoms));
        if(desc.toppal != null)
            top = inventory.AddItem(Clothing.CreateFromSprite((new StringBuilder()).append(desc.top).append("#").append(desc.toppal).toString()));
        else
            top = inventory.AddItem(Clothing.CreateFromSprite(desc.top));
        if(desc.shoespal != null)
            shoes = inventory.AddItem(Clothing.CreateFromSprite((new StringBuilder()).append(desc.shoes).append("#").append(desc.shoespal).toString()));
        else
            shoes = inventory.AddItem(Clothing.CreateFromSprite(desc.shoes));
        if(shoes != null)
            ClothingItem_Feet = shoes;
        if(top != null)
            ClothingItem_Torso = top;
        if(bottoms != null)
            ClothingItem_Legs = bottoms;
    }

    public void PlayAnimNoReset(String string)
    {
        int n;
        if(this instanceof IsoPlayer)
            n = 0;
        if("Run".equals(string))
            def.setFrameSpeedPerFrame(0.24F);
        else
        if("Walk".equals(string))
            def.setFrameSpeedPerFrame(0.2F);
        if(sprite.CurrentAnim.name.equals("Die"))
            return;
        if(!bUseParts)
        {
            sprite.PlayAnimNoReset(string);
        } else
        {
            legsSprite.PlayAnimNoReset(string);
            if(torsoSprite != null)
                torsoSprite.PlayAnimNoReset(string);
            if(headSprite != null)
                headSprite.PlayAnimNoReset(string);
            if(bottomsSprite != null)
                bottomsSprite.PlayAnimNoReset(string);
            if(shoeSprite != null)
                shoeSprite.PlayAnimNoReset(string);
            if(topSprite != null)
                topSprite.PlayAnimNoReset(string);
        }
    }

    public void PlayAnim(String string)
    {
        int n;
        if(this instanceof IsoPlayer)
            n = 0;
        def.Looped = true;
        def.Finished = false;
        if("Run".equals(string))
            def.setFrameSpeedPerFrame(0.24F);
        else
        if("Walk".equals(string))
            def.setFrameSpeedPerFrame(0.2F);
        if(sprite.CurrentAnim.name.equals("Die"))
            return;
        if(!bUseParts)
        {
            sprite.PlayAnim(string);
        } else
        {
            IsoAnim anim = legsSprite.CurrentAnim;
            legsSprite.PlayAnim(string);
            if(torsoSprite != null)
                torsoSprite.PlayAnim(string);
            if(headSprite != null)
                headSprite.PlayAnim(string);
            if(bottomsSprite != null)
                bottomsSprite.PlayAnim(string);
            if(shoeSprite != null)
                shoeSprite.PlayAnim(string);
            if(topSprite != null)
                topSprite.PlayAnim(string);
            if(anim != legsSprite.CurrentAnim && (float)legsSprite.CurrentAnim.Frames.size() <= def.Frame)
                def.Frame = 0.0F;
            EnforceAnims();
        }
    }

    public void PlayAnimUnlooped(String string)
    {
        int n;
        if(this instanceof IsoPlayer)
            n = 0;
        if(sprite.CurrentAnim.name.equals("Die"))
            return;
        if((this instanceof IsoLivingCharacter) && sprite.CurrentAnim.name.equals("ZombieDeath"))
            return;
        if(!bUseParts)
        {
            sprite.PlayAnimUnlooped(string);
            def.Looped = false;
        } else
        {
            IsoAnim anim = legsSprite.CurrentAnim;
            def.Looped = false;
            if(anim.name.equals(string) && (def.Finished || def.Frame != 0.0F))
                return;
            legsSprite.PlayAnimUnlooped(string);
            if(torsoSprite != null)
                torsoSprite.PlayAnimUnlooped(string);
            if(headSprite != null)
                headSprite.PlayAnimUnlooped(string);
            if(bottomsSprite != null)
                bottomsSprite.PlayAnimUnlooped(string);
            if(shoeSprite != null)
                shoeSprite.PlayAnimUnlooped(string);
            if(topSprite != null)
                topSprite.PlayAnimUnlooped(string);
            def.Frame = 0.0F;
            def.Finished = false;
        }
    }

    public void PlayAnimFrame(String string, int frame)
    {
        if(!bUseParts)
        {
            sprite.PlayAnim(string);
            def.Frame = (short)frame;
            def.Finished = true;
        } else
        {
            legsSprite.PlayAnimUnlooped(string);
            def.Finished = true;
            if(torsoSprite != null)
                torsoSprite.PlayAnimUnlooped(string);
            if(torsoSprite != null)
                def.Finished = true;
            if(headSprite != null)
            {
                headSprite.PlayAnimUnlooped(string);
                def.Finished = true;
            }
            if(bottomsSprite != null)
            {
                bottomsSprite.PlayAnimUnlooped(string);
                def.Finished = true;
            }
            if(shoeSprite != null)
            {
                shoeSprite.PlayAnimUnlooped(string);
                def.Finished = true;
            }
            if(topSprite != null)
            {
                topSprite.PlayAnimUnlooped(string);
                def.Finished = true;
            }
        }
    }

    public void DirectionFromVectorNoDiags(Vector2 vecA)
    {
        if(IgnoreMovementForDirection)
            return;
        if(Math.abs(vecA.x) < Math.abs(vecA.y))
        {
            tempo.x = vecA.x;
            tempo.y = vecA.y;
            tempo.x = 0.0F;
            tempo.normalize();
            dir = IsoDirections.fromAngle(tempo);
        } else
        {
            tempo.x = vecA.x;
            tempo.y = vecA.y;
            tempo.y = 0.0F;
            tempo.normalize();
            dir = IsoDirections.fromAngle(tempo);
        }
    }

    public void DirectionFromVector(Vector2 vecA)
    {
        if(IgnoreMovementForDirection)
        {
            return;
        } else
        {
            dir = IsoDirections.fromAngle(vecA);
            return;
        }
    }

    public void DoFootstepSound(float dist)
    {
        if(this instanceof IsoZombie)
            return;
        if(dist == 0.0F)
            return;
        float vol = 0.1F;
        if(this != IsoCamera.CamCharacter)
        {
            vol = 0.7F;
            if(dist >= 0.06F && dist > 0.06F)
                vol *= 0.7F;
        }
        if(dist < 0.06F)
            vol *= 0.2F;
        else
        if(dist <= 0.06F)
            vol *= 0.7F;
        footStepCounter--;
        footStepCounterMax = (int)((1.0F / dist) * 1.0F);
        if(dist <= 0.06F)
            footStepCounterMax *= 0.7F;
        if(HasTrait("Graceful"))
            vol *= 0.6F;
        if(HasTrait("Clumsy"))
            vol *= 1.2F;
        vol *= getLightfootMod();
        if(footStepCounter <= 0 && vol > 0.0F)
        {
            footStepCounter = footStepCounterMax;
            String sound = "foottile";
            if(getCurrentSquare().getRoom() == null && dist > 0.06F)
            {
                sound = "foottileecho";
                if(this == IsoCamera.CamCharacter)
                    vol *= 0.4F;
            }
            if(this != IsoCamera.CamCharacter)
                SoundManager.instance.PlayWorldSoundWav(sound, getCurrentSquare(), 0.0F, 10F, vol, 4, false);
            else
                SoundManager.instance.PlaySoundWav(sound, 4, false, vol * 1.3F);
            int rad = (int)(dist * 80F * getLightfootMod());
            if(getCurrentSquare().getRoom() != null)
                rad = (int)((float)rad * 0.5F);
            if(HasTrait("Graceful"))
                rad = (int)((float)rad * 0.6F);
            if(HasTrait("Clumsy"))
                rad = (int)((float)rad * 1.6F);
            WorldSoundManager.instance.addSound(this, (int)getX(), (int)getY(), (int)getZ(), rad, rad, false);
        }
        if(footStepCounter > footStepCounterMax)
            footStepCounter = footStepCounterMax;
    }

    public boolean Eat(InventoryItem info)
    {
        if(info instanceof Food)
        {
            Food food = (Food)info;
            if(food.getRequireInHandOrInventory() != null && food.getUseOnConsume() != null)
            {
                InventoryItem item = null;
                item = FindAndReturn(food.getRequireInHandOrInventory(), leftHandItem);
                if(item != null && food.getUseOnConsume().equals(food.getRequireInHandOrInventory()))
                    inventory.RemoveOneOf(food.getUseOnConsume());
                if(item == null)
                {
                    item = FindAndReturn(food.getRequireInHandOrInventory(), rightHandItem);
                    if(item != null && food.getUseOnConsume().equals(food.getRequireInHandOrInventory()))
                        inventory.RemoveOneOf(food.getUseOnConsume());
                }
                if(item == null)
                {
                    item = inventory.FindAndReturn(food.getRequireInHandOrInventory());
                    if(item != null && food.getUseOnConsume().equals(food.getRequireInHandOrInventory()))
                        inventory.RemoveOneOf(food.getUseOnConsume());
                }
                if(item == null)
                    return false;
            }
            stats.thirst += food.getThirstChange();
            stats.hunger += food.getHungerChange();
            stats.endurance += food.getEnduranceChange();
            stats.stress += food.getStressChange();
            return true;
        } else
        {
            return false;
        }
    }

    public void FaceNextPathNode()
    {
        if(path == null)
            return;
        if(pathIndex <= path.getLength())
        {
            boolean bIsNorth = false;
            boolean bIsSouth = false;
            boolean bIsEast = false;
            boolean bIsWest = false;
            float ax = (float)path.getX(pathIndex) + 0.5F;
            float ay = (float)path.getY(pathIndex) + 0.5F;
            if(ax > getX() && Math.abs(ax - getX()) >= 0.1F)
                bIsEast = true;
            if(ax < getX() && Math.abs(ax - getX()) >= 0.1F)
                bIsWest = true;
            if(ay > getY() && Math.abs(ay - getY()) >= 0.1F)
                bIsSouth = true;
            if(ay < getY() && Math.abs(ay - getY()) >= 0.1F)
                bIsNorth = true;
            if(bIsNorth && bIsWest)
                dir = IsoDirections.NW;
            else
            if(bIsNorth && bIsEast)
                dir = IsoDirections.NE;
            else
            if(bIsSouth && bIsEast)
                dir = IsoDirections.SE;
            else
            if(bIsSouth && bIsWest)
                dir = IsoDirections.SW;
            else
            if(bIsSouth)
                dir = IsoDirections.S;
            else
            if(bIsNorth)
                dir = IsoDirections.N;
            else
            if(bIsEast)
                dir = IsoDirections.E;
            else
            if(bIsWest)
                dir = IsoDirections.W;
        }
    }

    public void FaceNextPathNode(int xo, int yo)
    {
        tempo.x = (float)xo + 0.5F;
        tempo.y = (float)yo + 0.5F;
        tempo.x -= getX();
        tempo.y -= getY();
        DirectionFromVector(tempo);
    }

    public void FireCheck()
    {
        if(OnFire)
            return;
        if(square != null && square.getProperties().Is(IsoFlagType.burning) && (!(this instanceof IsoZombie) || (this instanceof IsoZombie) && !((IsoZombie)this).Ghost))
            SetOnFire();
    }

    public InventoryItem getCraftingByIndex(int index)
    {
        if(index == 0)
            return craftIngredient1;
        if(index == 1)
            return craftIngredient2;
        if(index == 2)
            return craftIngredient3;
        if(index == 3)
            return craftIngredient4;
        else
            return null;
    }

    public String getPrimaryHandType()
    {
        if(leftHandItem == null)
            return null;
        else
            return leftHandItem.getType();
    }

    public float getMoveSpeed()
    {
        tempo2.x = getX() - getLx();
        tempo2.y = getY() - getLy();
        return tempo2.getLength();
    }

    public String getSecondaryHandType()
    {
        if(rightHandItem == null)
            return null;
        else
            return rightHandItem.getType();
    }

    public boolean HasItem(String string)
    {
        if(string == null)
            return true;
        return string.equals(getSecondaryHandType()) || string.equals(getPrimaryHandType()) || inventory.contains(string);
    }

    public void changeState(State state)
    {
        stateMachine.changeState(state);
    }

    public State getCurrentState()
    {
        return stateMachine.getCurrent();
    }

    public void setLockStates(boolean bVal)
    {
        stateMachine.Lock = bVal;
    }

    public void Hit(HandWeapon weapon, IsoGameCharacter wielder, float damageSplit, boolean bIgnoreDamage, float modDelta)
    {
        if(noDamage)
            bIgnoreDamage = true;
        noDamage = false;
        if(weapon.getName().contains("BareHands") || wielder.sprite.CurrentAnim.name.contains("Shove"))
        {
            bIgnoreDamage = true;
            noDamage = true;
        }
        LuaEventManager.TriggerEvent("OnWeaponHitCharacter", wielder, this, weapon, Float.valueOf(damageSplit));
        if(LuaHookManager.TriggerHook("HookWeaponHitCharacter", wielder, this, weapon, Float.valueOf(damageSplit)))
            return;
        if(Health <= 0.0F || BodyDamage.getHealth() <= 0.0F)
        {
            setSolid(false);
            return;
        }
        if((this instanceof IsoSurvivor) && !EnemyList.contains(wielder))
            EnemyList.add(wielder);
        staggerTimeMod = weapon.getPushBackMod() * weapon.getKnockbackMod(wielder) * wielder.getShovingMod();
        if(this instanceof IsoZombie)
            SoundManager.instance.PlayWorldSound(hurtSound, getCurrentSquare(), 0.4F, 10F, 0.6F, 5, false);
        float damage = 0.0F;
        if((wielder instanceof IsoPlayer) && !weapon.bIsAimedFirearm)
        {
            float chDel = IsoPlayer.instance.useChargeDelta;
            if(chDel > 1.0F)
                chDel = 1.0F;
            if(chDel < 0.0F)
                chDel = 0.0F;
            float hf = weapon.getMinDamage() + (weapon.getMaxDamage() - weapon.getMinDamage()) * chDel;
            damage = hf;
        } else
        {
            damage = (float)Rand.Next((int)((weapon.getMaxDamage() - weapon.getMinDamage()) * 1000F)) / 1000F + weapon.getMinDamage();
        }
        damage *= modDelta;
        setHitForce(damage * weapon.getKnockbackMod(wielder) * wielder.getShovingMod());
        if(wielder.HasTrait("Strong") && !weapon.isRanged())
            setHitForce(getHitForce() * 1.4F);
        if(wielder.HasTrait("Weak") && !weapon.isRanged())
            setHitForce(getHitForce() * 0.6F);
        AttackedBy = wielder;
        float dist = IsoUtils.DistanceTo(wielder.getX(), wielder.getY(), getX(), getY());
        dist -= weapon.getMinRange();
        dist /= weapon.getMaxRange();
        dist = 1.0F - dist;
        hitDir.x = getX();
        hitDir.y = getY();
        hitDir.x -= wielder.getX();
        hitDir.y -= wielder.getY();
        getHitDir().normalize();
        hitDir.x *= weapon.getPushBackMod();
        hitDir.y *= weapon.getPushBackMod();
        hitDir.rotate(weapon.HitAngleMod);
        float del = wielder.stats.endurance;
        hitBy = wielder;
        if(del < 0.5F)
        {
            del *= 1.3F;
            if(del < 0.4F)
                del = 0.4F;
            setHitForce(getHitForce() * del);
        }
        if(!weapon.isRangeFalloff())
            dist = 1.0F;
        if(!weapon.isShareDamage())
            damageSplit = 1.0F;
        if(wielder instanceof IsoPlayer)
            setHitForce(getHitForce() * 2.0F);
        Vector2 oPos = new Vector2(getX(), getY());
        Vector2 tPos = new Vector2(IsoPlayer.instance.getX(), IsoPlayer.instance.getY());
        oPos.x -= tPos.x;
        oPos.y -= tPos.y;
        Vector2 dir = getVectorFromDirection();
        oPos.normalize();
        float dot = oPos.dot(dir);
        if(dot > -0.3F)
            damage *= 1.5F;
        if((this instanceof IsoSurvivor) || (this instanceof IsoPlayer))
            damage *= 0.4F;
        if(wielder instanceof IsoPlayer)
        {
            if(!bIgnoreDamage)
                if(weapon.isAimedFirearm())
                    Health -= damage;
                else
                    Health -= damage;
        } else
        if(!bIgnoreDamage)
            Health -= damage;
        float KnockdownChance = 9F;
        if(wielder.stats.endurance < wielder.stats.endurancewarn)
            KnockdownChance = 12F;
        else
        if(wielder.stats.endurance < wielder.stats.endurancedanger)
            KnockdownChance = 14F;
        KnockdownChance /= weapon.getKnockdownMod();
        if((wielder instanceof IsoPlayer) && !weapon.isAimedHandWeapon())
        {
            KnockdownChance /= 2.0F;
            KnockdownChance *= 2.0F - IsoPlayer.instance.useChargeDelta;
        }
        boolean bKnockdown = Rand.Next((int)KnockdownChance) == 0;
        if(Health <= 0.0F || (weapon.isAlwaysKnockdown() || bKnockdown) && (this instanceof IsoZombie))
        {
            DoDeath(weapon, wielder);
        } else
        {
            if(weapon.isSplatBloodOnNoDeath())
                splatBlood(3, 0.3F);
            if(weapon.isKnockBackOnNoDeath())
            {
                wielder.xp.AddXP(zombie.characters.skills.PerkFactory.Perks.Shoving, 2);
                stateMachine.changeState(StaggerBackState.instance());
            }
        }
    }

    public void inflictWound(BodyLocation loc, float bleeding, boolean infectedZombie, float chanceOfInfect)
    {
        Wound wound = new Wound();
        wound.loc = loc;
        wound.bleeding = bleeding;
        wound.infectedZombie = false;
        if(Rand.Next(100) < (int)chanceOfInfect * 100)
            wound.infectedNormal = true;
        wounds.add(wound);
    }

    public boolean IsAttackRange(float x, float y, float z)
    {
        float maxrange = 1.0F;
        float minrange = 0.0F;
        if(leftHandItem != null)
        {
            InventoryItem attackItem = leftHandItem;
            if(attackItem instanceof HandWeapon)
            {
                maxrange = ((HandWeapon)attackItem).getMaxRange();
                minrange = ((HandWeapon)attackItem).getMinRange();
                maxrange *= ((HandWeapon)leftHandItem).getRangeMod(this);
            }
        }
        if(Math.abs(z - getZ()) > 0.3F)
            return false;
        float dist = IsoUtils.DistanceTo(x, y, getX(), getY());
        return dist < maxrange && dist > minrange;
    }

    public boolean IsAttackRange(HandWeapon we, float x, float y, float z)
    {
        float maxrange = 1.0F;
        float minrange = 0.0F;
        InventoryItem attackItem = we;
        if(attackItem instanceof HandWeapon)
        {
            maxrange = ((HandWeapon)attackItem).getMaxRange();
            minrange = ((HandWeapon)attackItem).getMinRange();
            maxrange *= we.getRangeMod(this);
        }
        if(Math.abs(z - getZ()) > 0.3F)
            return false;
        float dist = IsoUtils.DistanceTo(x, y, getX(), getY());
        return dist < maxrange && dist > minrange;
    }

    public boolean IsSpeaking()
    {
        return Speaking;
    }

    public void MoveForward(float dist)
    {
        moveForwardVec.x = 0.0F;
        moveForwardVec.y = 0.0F;
        
        

        switch(dir.ordinal())
        {
        case 1: // '\001'
            moveForwardVec.x = 0.0F;
            moveForwardVec.y = 1.0F;
            break;

        case 2: // '\002'
            moveForwardVec.x = 0.0F;
            moveForwardVec.y = -1F;
            break;

        case 3: // '\003'
            moveForwardVec.x = 1.0F;
            moveForwardVec.y = 0.0F;
            break;

        case 4: // '\004'
            moveForwardVec.x = -1F;
            moveForwardVec.y = 0.0F;
            break;

        case 5: // '\005'
            moveForwardVec.x = -1F;
            moveForwardVec.y = -1F;
            break;

        case 6: // '\006'
            moveForwardVec.x = 1.0F;
            moveForwardVec.y = -1F;
            break;

        case 7: // '\007'
            moveForwardVec.x = -1F;
            moveForwardVec.y = 1.0F;
            break;

        case 8: // '\b'
            moveForwardVec.x = 1.0F;
            moveForwardVec.y = 1.0F;
            break;
        }
        moveForwardVec.setLength(dist);
        setNx(getNx() + moveForwardVec.x * GameTime.instance.getMultiplier());
        setNy(getNy() + moveForwardVec.y * GameTime.instance.getMultiplier());
        DoFootstepSound(dist);
        if(this instanceof IsoZombie);
    }

    public void MoveForward(float dist, float x, float y, float soundDelta)
    {
        dist *= GameTime.instance.getMultiplier();
        setNx(getNx() + x * dist);
        setNy(getNy() + y * dist);
        DoFootstepSound(dist);
        if(this instanceof IsoZombie);
    }

    public void pathFinished()
    {
        setPathFindIndex(-1);
        stateMachine.changeState(defaultState);
    }

    public void PathTo(int x, int y, int z, boolean critical)
    {
        if(PathTargetX == x && PathTargetY == y && PathTargetZ == z && finder.progress != zombie.ai.astar.AStarPathFinder.PathFindProgress.notrunning)
        {
            return;
        } else
        {
            PathTargetX = x;
            PathTargetY = y;
            PathTargetZ = z;
            if(getPathFindIndex() == -1);
            setPathFindIndex(0);
            stateMachine.changeState(WalkTowardState.instance());
            return;
        }
    }

    public void PathTo(int x, int y, int z, boolean critical, int delay)
    {
        IsoGridSquare t = getCell().getGridSquare(x, y, z);
        boolean bOk = true;
        if(t.getRoom() != null)
        {
            bOk = false;
            int c = IsoWorld.instance.CurrentCell.getObjectList().size();
            if(getCurrentSquare().getRoom() != null && t.getRoom().building == getCurrentSquare().getRoom().building)
            {
                bOk = true;
            } else
            {
                for(int n = 0; n < c; n++)
                {
                    IsoMovingObject o = (IsoMovingObject)IsoWorld.instance.CurrentCell.getObjectList().get(n);
                    if((o instanceof IsoSurvivor) && o.getCurrentSquare().getRoom() != null && o.getCurrentSquare().getRoom().building == t.getRoom().building)
                        bOk = true;
                    if((o instanceof IsoPlayer) && o.getCurrentSquare().getRoom() != null && o.getCurrentSquare().getRoom().building == t.getRoom().building)
                        bOk = true;
                }

            }
        }
        if(!bOk)
            return;
        if(PathTargetX == x && PathTargetY == y && PathTargetZ == z && finder.progress != zombie.ai.astar.AStarPathFinder.PathFindProgress.notrunning)
        {
            return;
        } else
        {
            PathTargetX = x;
            PathTargetY = y;
            PathTargetZ = z;
            if(getPathFindIndex() == -1);
            setPathFindIndex(0);
            stateMachine.changeState(WalkTowardState.instance());
            return;
        }
    }

    public boolean CanAttack()
    {
        InventoryItem attackItem = leftHandItem;
        if((attackItem instanceof HandWeapon) && attackItem.getSwingAnim() != null)
            useHandWeapon = (HandWeapon)attackItem;
        if(useHandWeapon == null)
            return true;
        if(useHandWeapon.getCondition() <= 0)
            return false;
        return !useHandWeapon.isCantAttackWithLowestEndurance() || stats.endurance >= stats.endurancedanger;
    }

    public void PlayShootAnim()
    {
        if(!sprite.CurrentAnim.name.contains("Attack_"))
        {
            InventoryItem attackItem = leftHandItem;
            if((attackItem instanceof HandWeapon) && attackItem.getSwingAnim() != null)
                if(bUseParts)
                {
                    useHandWeapon = (HandWeapon)attackItem;
                    if(useHandWeapon.getCondition() <= 0)
                        return;
                    if(useHandWeapon.isCantAttackWithLowestEndurance() && stats.endurance < stats.endurancedanger)
                        return;
                    PlayAnimUnlooped((new StringBuilder()).append("Attack_").append(attackItem.getSwingAnim()).toString());
                    legsSprite.PlayAnimUnlooped((new StringBuilder()).append("Attack_").append(attackItem.getSwingAnim()).toString());
                    def.Frame = 0.0F;
                    def.Finished = true;
                    legsSprite.CurrentAnim.FinishUnloopedOnFrame = 0;
                    if(torsoSprite != null)
                    {
                        torsoSprite.PlayAnimUnlooped((new StringBuilder()).append("Attack_").append(attackItem.getSwingAnim()).toString());
                        def.Finished = true;
                        torsoSprite.CurrentAnim.FinishUnloopedOnFrame = 0;
                    }
                    if(headSprite != null)
                    {
                        headSprite.PlayAnimUnlooped((new StringBuilder()).append("Attack_").append(attackItem.getSwingAnim()).toString());
                        def.Finished = true;
                        headSprite.CurrentAnim.FinishUnloopedOnFrame = 0;
                    }
                    if(bottomsSprite != null)
                    {
                        bottomsSprite.PlayAnimUnlooped((new StringBuilder()).append("Attack_").append(attackItem.getSwingAnim()).toString());
                        def.Finished = true;
                        bottomsSprite.CurrentAnim.FinishUnloopedOnFrame = 0;
                    }
                    if(shoeSprite != null)
                    {
                        shoeSprite.PlayAnimUnlooped((new StringBuilder()).append("Attack_").append(attackItem.getSwingAnim()).toString());
                        def.Finished = true;
                        shoeSprite.CurrentAnim.FinishUnloopedOnFrame = 0;
                    }
                    if(topSprite != null)
                    {
                        topSprite.PlayAnimUnlooped((new StringBuilder()).append("Attack_").append(attackItem.getSwingAnim()).toString());
                        def.Finished = true;
                        topSprite.CurrentAnim.FinishUnloopedOnFrame = 0;
                    }
                    def.Frame = 0.0F;
                    sprite.Animate = false;
                    def.setFrameSpeedPerFrame(1.0F / useHandWeapon.getSwingTime());
                } else
                {
                    useHandWeapon = (HandWeapon)attackItem;
                    if(useHandWeapon.getCondition() <= 0)
                        return;
                    if(useHandWeapon.isCantAttackWithLowestEndurance() && stats.endurance < stats.endurancedanger)
                        return;
                    PlayAnimUnlooped((new StringBuilder()).append("Attack_").append(attackItem.getSwingAnim()).toString());
                    def.Frame = 0.0F;
                    sprite.Animate = false;
                    def.setFrameSpeedPerFrame(1.0F / useHandWeapon.getSwingTime());
                }
        }
    }

    public void ReduceHealthWhenBurning()
    {
        if(!OnFire)
            return;
        if(Health > 0.0F)
        {
            if(!(this instanceof IsoPlayer))
            {
                Health -= FireKillRate;
            } else
            {
                IsoPlayer.instance.BodyDamage.ReduceGeneralHealth(FireKillRate * 60F);
                IsoPlayer.instance.BodyDamage.OnFire(true);
            }
            if(Health <= 0.0F)
            {
                IsoFireManager.RemoveBurningCharacter(this);
                stateMachine.changeState(BurntToDeath.instance());
                stateMachine.Lock = true;
            }
        }
    }

    public void DrawSneezeText()
    {
        if(this == IsoPlayer.instance && IsoPlayer.instance.BodyDamage.IsSneezingCoughing() > 0)
        {
            String SneezeText = null;
            if(IsoPlayer.instance.BodyDamage.IsSneezingCoughing() == 1)
                SneezeText = "Ah-choo!";
            if(IsoPlayer.instance.BodyDamage.IsSneezingCoughing() == 2)
                SneezeText = "Cough!";
            if(IsoPlayer.instance.BodyDamage.IsSneezingCoughing() == 3)
                SneezeText = "Ah-fmmph!";
            if(IsoPlayer.instance.BodyDamage.IsSneezingCoughing() == 4)
                SneezeText = "fmmmph!";
            float sx = sprite.lsx;
            float sy = sprite.lsy;
            sx = (int)sx;
            sy = (int)sy;
            sx -= offsetX;
            sy -= offsetY;
            sx -= (int)IsoCamera.getOffX();
            sy -= (int)IsoCamera.getOffY();
            sx += 8F;
            sy += 32F;
            if(SneezeText != null)
            {
                IndieGL.End();
                TextManager.instance.DrawStringCentre(UIFont.Medium, (int)sx, (int)sy, SneezeText, SpeakColour.r, SpeakColour.g, SpeakColour.b, SpeakColour.a);
            }
        }
    }

    public void render(float x, float y, float z, ColorInfo col, boolean bDoChild)
    {
        if(!bUseParts && def == null)
            def = new IsoSpriteInstance(sprite);
        if(z - (float)(int)z > 0.2F)
        {
            IsoGridSquare above = getCell().getGridSquare((int)x, (int)y, (int)z + 1);
            if(above != null && bDoDefer)
                above.getDeferedCharacters().add(this);
        }
        if(Core.getInstance().nGraphicLevel >= 3 && getCurrentSquare() != null)
        {
            getCurrentSquare().interpolateLight(inf, x - (float)getCurrentSquare().getX(), y - (float)getCurrentSquare().getY());
        } else
        {
            inf.r = col.r;
            inf.g = col.g;
            inf.b = col.b;
            inf.a = col.a;
        }
        int n;
        if(this instanceof IsoPlayer)
            n = 0;
        if(dir == IsoDirections.Max)
            dir = IsoDirections.N;
        checkDrawWeaponPre(x, y, z, col);
        lastRenderedRendered = lastRendered;
        lastRendered = this;
        if(sprite != null && sprite.getProperties().Is(IsoFlagType.invisible))
            return;
        float mul = 2.0F;
        float div = 1.5F;
        if(IsoPlayer.instance.HasTrait("ShortSighted"))
            mul = 1.0F;
        if(IsoPlayer.instance.HasTrait("EagleEyed"))
        {
            mul = 3F;
            div = 2.0F;
        }
        if(this == IsoCamera.CamCharacter)
            targetAlpha = 1.0F;
        if(alphaStep == -100F)
            IsoPlayer.getInstance().getBodyDamage().ReduceFactor();
        if(alpha < targetAlpha)
        {
            alpha += alphaStep * mul;
            if(alpha > targetAlpha)
                alpha = targetAlpha;
        } else
        if(alpha > targetAlpha)
        {
            alpha -= alphaStep / div;
            if(alpha < targetAlpha)
                alpha = targetAlpha;
        }
        if(alpha < 0.0F)
            alpha = 0.0F;
        if(alpha > 1.0F)
            alpha = 1.0F;
        if(sprite != null)
            if(!bUseParts)
            {
                sprite.render(def, this, x, y, z, dir, offsetX, offsetY, inf);
            } else
            {
                EnforceAnims();
                legsSprite.render(def, this, x, y, z, dir, offsetX, offsetY, inf);
                if(torsoSprite != null)
                    torsoSprite.render(def, this, x, y, z, dir, offsetX, offsetY, inf);
                if(shoeSprite != null)
                    shoeSprite.render(def, this, x, y, z, dir, offsetX, offsetY, inf);
                if(bottomsSprite != null)
                    bottomsSprite.render(def, this, x, y, z, dir, offsetX, offsetY, inf);
                if(topSprite != null)
                    topSprite.render(def, this, x, y, z, dir, offsetX, offsetY, inf);
                if(headSprite != null)
                    headSprite.render(def, this, x, y, z, dir, offsetX, offsetY, inf);
            }
        if(AttachedAnimSprite != null)
        {
            for(n = 0; n < AttachedAnimSprite.size(); n++)
            {
                IsoSpriteInstance spr = (IsoSpriteInstance)AttachedAnimSprite.get(n);
                spr.render(this, x, y, z, dir, offsetX, offsetY, inf);
            }

        }
        if(AttachedAnimSprite != null)
        {
            for(int i = 0; i < AttachedAnimSprite.size(); i++)
            {
                ((IsoSpriteInstance)AttachedAnimSprite.get(i)).update();
                ((IsoSpriteInstance)AttachedAnimSprite.get(i)).SetTargetAlpha(targetAlpha);
                ((IsoSpriteInstance)AttachedAnimSprite.get(i)).render(this, x, y, z, dir, offsetX, offsetY, inf);
            }

        }
        checkDrawWeaponPost(x, y, z, inf);
        for(n = 0; n < inventory.Items.size(); n++)
        {
            InventoryItem item = (InventoryItem)inventory.Items.get(n);
            if(item instanceof IUpdater)
                ((IUpdater)item).render();
        }

    }

    public void renderObjectPicker(float x, float y, float z, ColorInfo lightInfo)
    {
        if(!bUseParts)
        {
            sprite.renderObjectPicker(this, x, y, z, dir, offsetX, offsetY, lightInfo);
        } else
        {
            legsSprite.renderObjectPicker(this, x, y, z, dir, offsetX, offsetY, lightInfo);
            if(torsoSprite != null)
                torsoSprite.renderObjectPicker(this, x, y, z, dir, offsetX, offsetY, lightInfo);
            if(bottomsSprite != null)
                bottomsSprite.renderObjectPicker(this, x, y, z, dir, offsetX, offsetY, lightInfo);
            if(shoeSprite != null)
                shoeSprite.renderObjectPicker(this, x, y, z, dir, offsetX, offsetY, lightInfo);
            if(topSprite != null)
                topSprite.renderObjectPicker(this, x, y, z, dir, offsetX, offsetY, lightInfo);
            if(headSprite != null)
                headSprite.renderObjectPicker(this, x, y, z, dir, offsetX, offsetY, lightInfo);
        }
    }

    public boolean isMaskClicked(int x, int y, boolean flip)
    {
        if(sprite == null)
            return false;
        if(!bUseParts)
            return super.isMaskClicked(x, y, flip);
        boolean bClicked = false;
        bClicked = legsSprite.isMaskClicked(dir, x, y, flip);
        if(torsoSprite != null)
            bClicked |= torsoSprite.isMaskClicked(dir, x, y, flip);
        if(bottomsSprite != null)
            bClicked |= bottomsSprite.isMaskClicked(dir, x, y, flip);
        if(shoeSprite != null)
            bClicked |= shoeSprite.isMaskClicked(dir, x, y, flip);
        if(topSprite != null)
            bClicked |= topSprite.isMaskClicked(dir, x, y, flip);
        if(headSprite != null)
            bClicked |= headSprite.isMaskClicked(dir, x, y, flip);
        return bClicked;
    }

    public void renderlast()
    {
        if(!Speaking)
            DrawSneezeText();
        if(Speaking && !IsoPlayer.DemoMode)
        {
            boolean bDo = true;
            float dist = DistTo(IsoCamera.CamCharacter);
            if(dist > 16F)
                bDo = false;
            if(dist > 10F && getCurrentSquare().getRoom() != IsoCamera.CamCharacter.getCurrentSquare().getRoom())
                bDo = false;
            if(bDo)
            {
                float sx = sprite.lsx;
                float sy = sprite.lsy;
                sx = (int)sx;
                sy = (int)sy;
                sx -= offsetX;
                sy -= offsetY;
                sx -= (int)IsoCamera.getOffX();
                sy -= (int)IsoCamera.getOffY();
                sx += 8F;
                sy += 32F;
                if(sayLine != null)
                {
                    IndieGL.End();
                    TextManager.instance.DrawStringCentre(UIFont.Medium, (int)sx, (int)sy, sayLine, SpeakColour.r, SpeakColour.g, SpeakColour.b, SpeakColour.a);
                }
            }
        }
        for(int n = 0; n < inventory.Items.size(); n++)
        {
            InventoryItem item = (InventoryItem)inventory.Items.get(n);
            if(item instanceof IUpdater)
                ((IUpdater)item).renderlast();
        }

    }

    public void rendertalk(float yoff)
    {
        if(Speaking)
        {
            float sx = sprite.lsx;
            float sy = sprite.lsy;
            sx = (int)sx;
            sy = (int)sy;
            sx -= offsetX;
            sy -= offsetY;
            sx -= (int)IsoCamera.getOffX();
            sy -= (int)IsoCamera.getOffY();
            sx += 8F;
            sy += 32F;
            sy += yoff;
            if(sayLine != null)
                TextManager.instance.DrawStringCentre(UIFont.Medium, (int)sx, (int)sy, sayLine, SpeakColour.r, SpeakColour.g, SpeakColour.b, SpeakColour.a);
        }
    }

    public void Say(String line)
    {
        if(!AllowConversation)
            return;
        SpeakTime = line.length() * 3;
        if(SpeakTime < 60)
            SpeakTime = 60;
        sayLine = line;
        if(TutorialManager.instance.ProfanityFilter)
        {
            sayLine = sayLine.replace("Fuck", "****");
            sayLine = sayLine.replace("fuck", "****");
            sayLine = sayLine.replace("Shit", "****");
            sayLine = sayLine.replace("shit", "****");
            sayLine = sayLine.replace("FUCK", "****");
            sayLine = sayLine.replace("SHIT", "****");
        }
        Speaking = true;
    }

    public void setCraftingByIndex(int index, InventoryItem temp)
    {
        if(index == 0)
            craftIngredient1 = temp;
        if(index == 1)
            craftIngredient2 = temp;
        if(index == 2)
            craftIngredient3 = temp;
        if(index == 3)
            craftIngredient4 = temp;
    }

    public void setDefaultState()
    {
        stateMachine.changeState(defaultState);
    }

    public void SetOnFire()
    {
        if(OnFire)
        {
            return;
        } else
        {
            OnFire = true;
            AttachAnim("Fire", "00", 4, IsoFireManager.FireAnimDelay, -12 + Rand.Next(24), Rand.Next(50), true, 0, false, 0.7F, IsoFireManager.FireTintMod);
            IsoFireManager.AddBurningCharacter(this);
            return;
        }
    }

    public void SpreadFire()
    {
        if(!OnFire)
            return;
        if(square != null && !square.getProperties().Is(IsoFlagType.burning) && Rand.Next(6000) < FireSpreadProbability)
            IsoFireManager.StartFire(getCell(), square, false, 50);
    }

    public void Throw(String physicsObject)
    {
        if(physicsObject.equals("MolotovCocktail"))
        {
            float x = (float)attackTargetSquare.getX() - getX();
            float y = (float)attackTargetSquare.getY() - getY();
            IsoMolotovCocktail gibs = new IsoMolotovCocktail(getCell(), getX(), getY(), getZ() + 0.6F, x * 0.4F, y * 0.4F);
        }
    }

    public void update()
    {
        if(current == null)
            return;
        if(!current.isCanSee())
        {
            timeTillForgetLocation--;
            if(timeTillForgetLocation < 0)
                SpottedSinceAlphaZero = false;
            if(!(this instanceof IsoPlayer))
                targetAlpha = 0.0F;
        } else
        if(testPlayerSpotInDarkness.Check())
            TestIfSeen();
        llx = getLx();
        lly = getLy();
        setLx(getX());
        setLy(getY());
        setLz(getZ());
        float fallSpeed = 0.125F;
        IsoDirections ropeDir = IsoDirections.Max;
        bClimbing = false;
        if(z > 0.0F)
        {
            if(!(this instanceof IsoZombie))
            {
                if(getCurrentSquare().getProperties().Is(IsoFlagType.climbSheetW) && (!current.getProperties().Is(IsoFlagType.climbSheetTopW) || getZ() - (float)(int)getZ() < 0.1F))
                {
                    ropeDir = IsoDirections.W;
                    bClimbing = true;
                    fallSpeed *= 0.3F;
                    if(lastCollidedW)
                        fallSpeed *= -0.5F;
                    bFalling = true;
                }
                if(getCurrentSquare().getProperties().Is(IsoFlagType.climbSheetN) && (!current.getProperties().Is(IsoFlagType.climbSheetTopN) || getZ() - (float)(int)getZ() < 0.1F))
                {
                    ropeDir = IsoDirections.N;
                    fallSpeed *= 0.3F;
                    bClimbing = true;
                    bFalling = true;
                    if(lastCollidedN)
                        fallSpeed *= -0.5F;
                }
                if(lastCollidedW && getCurrentSquare().getProperties().Is(IsoFlagType.climbSheetTopW) && getZ() - (float)(int)getZ() >= 0.1F)
                    fallSpeed = 0.0F;
                if(lastCollidedN && getCurrentSquare().getProperties().Is(IsoFlagType.climbSheetTopN) && getZ() - (float)(int)getZ() >= 0.1F)
                    fallSpeed = 0.0F;
                if(ropeDir != IsoDirections.Max)
                    dir = ropeDir;
            }
            lastFallSpeed = fallSpeed;
            float temp = fallSpeed;
            boolean bBack = false;
            if(lastFallSpeed < 0.0F && fallSpeed >= 0.0F)
            {
                fallSpeed = lastFallSpeed;
                bBack = true;
            }
            if(!current.TreatAsSolidFloor())
            {
                if(ropeDir != IsoDirections.Max)
                    dir = ropeDir;
                fallTime += 6 + Rand.Next(3);
                if(ropeDir != IsoDirections.Max)
                    fallTime = 0;
                setZ(getZ() - fallSpeed);
            } else
            if(getZ() > (float)(int)getZ() || fallSpeed < 0.0F)
            {
                if(ropeDir != IsoDirections.Max)
                    dir = ropeDir;
                if(!current.Has(IsoObjectType.stairsBN) && !current.Has(IsoObjectType.stairsTN) && !current.Has(IsoObjectType.stairsMN) && !current.Has(IsoObjectType.stairsBW) && !current.Has(IsoObjectType.stairsMW) && !current.Has(IsoObjectType.stairsTW))
                {
                    fallTime += 6 + Rand.Next(3);
                    if(ropeDir != IsoDirections.Max)
                        fallTime = 0;
                    setZ(getZ() - fallSpeed);
                    if(z < (float)(int)llz)
                    {
                        z = (int)llz;
                        DoLand();
                        fallTime = 0;
                        bFalling = false;
                    }
                } else
                {
                    fallTime = 0;
                    bFalling = false;
                }
            } else
            {
                DoLand();
                fallTime = 0;
                bFalling = false;
            }
            if(bBack)
                fallSpeed = temp;
        } else
        {
            DoLand();
            fallTime = 0;
            bFalling = false;
        }
        llz = lz;
        if(!FrameLoader.bClient)
        {
            if(!Orders.isEmpty())
                Order = (Order)Orders.peek();
            else
                Order = null;
            if(!PersonalNeeds.isEmpty())
                PersonalNeed = (Order)PersonalNeeds.peek();
            else
                PersonalNeed = null;
        }
        if(descriptor != null)
            descriptor.Instance = this;
        if(getCurrentSquare() != null)
            if(getCurrentSquare().getRoom() == null);
        if(!(this instanceof IsoZombie))
        {
            if(!FrameLoader.bClient)
            {
                if(HasTrait("Agoraphobic") && getCurrentSquare().getRoom() == null)
                    stats.Panic += 0.5F;
                if(HasTrait("Claustophobic") && getCurrentSquare().getRoom() != null)
                {
                    float del = 1.0F;
                    int n = getCurrentSquare().getRoom().TileList.size();
                    del = 1.0F - (float)n / 70F;
                    if(del < 0.0F)
                        del = 0.0F;
                    stats.Panic += 0.6F * del;
                }
            }
            Moodles.Update();
            if(!FrameLoader.bClient)
            {
                if(Asleep)
                {
                    BetaEffect = 0;
                    DepressEffect = 0;
                    SleepingTabletEffect = 0;
                }
                if(BetaEffect > 0)
                {
                    BetaEffect--;
                    stats.Panic -= 0.5F;
                    if(stats.Panic < 0.0F)
                        stats.Panic = 0.0F;
                } else
                {
                    BetaDelta = 0.0F;
                }
                if(DepressEffect > 0)
                    DepressEffect--;
                else
                    DepressDelta = 0.0F;
                if(PainEffect > 0)
                {
                    stats.Pain -= 0.1333333F;
                    PainEffect--;
                } else
                {
                    PainDelta = 0.0F;
                }
                if(SleepingTabletEffect > 0)
                {
                    SleepingTabletEffect--;
                    stats.fatigue += 0.001666667F * SleepingTabletDelta;
                } else
                {
                    SleepingTabletDelta = 0.0F;
                }
                int panic = Moodles.getMoodleLevel(MoodleType.Panic);
                if(panic == 2)
                    stats.Sanity -= 3.2E-007F;
                else
                if(panic == 3)
                    stats.Sanity -= 4.8E-007F;
                else
                if(panic == 4)
                    stats.Sanity -= 8E-007F;
                else
                if(panic == 0)
                    stats.Sanity += 1E-007F;
                int fatigue = Moodles.getMoodleLevel(MoodleType.Tired);
                if(fatigue == 4)
                    stats.Sanity -= 2E-006F;
                if(stats.Sanity < 0.0F)
                    stats.Sanity = 0.0F;
                if(stats.Sanity > 1.0F)
                    stats.Sanity = 1.0F;
            }
        }
        if(!FrameLoader.bClient)
        {
            if(!CharacterActions.isEmpty())
            {
                BaseAction act = (BaseAction)CharacterActions.get(0);
                if(act.valid())
                    act.update();
                if(!act.valid() || act.finished())
                {
                    if(act.finished())
                        act.perform();
                    act.stop();
                    CharacterActions.removeElement(act);
                }
            }
            for(int a = 0; a < EnemyList.size(); a++)
            {
                IsoGameCharacter b = (IsoGameCharacter)EnemyList.get(a);
                if(b.Health < 0.0F)
                {
                    EnemyList.remove(b);
                    a--;
                }
            }

        }
        BodyDamage.Update();
        if(!FrameLoader.bClient)
        {
            if(this == IsoPlayer.instance)
            {
                if(craftIngredient1 != null && craftIngredient1.getUses() <= 0)
                    craftIngredient1 = null;
                if(craftIngredient2 != null && craftIngredient2.getUses() <= 0)
                    craftIngredient2 = null;
                if(craftIngredient3 != null && craftIngredient3.getUses() <= 0)
                    craftIngredient3 = null;
                if(craftIngredient4 != null && craftIngredient4.getUses() <= 0)
                    craftIngredient4 = null;
                if(leftHandItem != null && leftHandItem.getUses() <= 0)
                    leftHandItem = null;
                if(rightHandItem != null && rightHandItem.getUses() <= 0)
                    rightHandItem = null;
            }
            lastdir = dir;
            AttackDelay--;
            calculateStats();
            UpdateWounds();
        }
        moveForwardVec.x = 0.0F;
        moveForwardVec.y = 0.0F;
        if(!Asleep || !(this instanceof IsoPlayer))
        {
            setLx(getX());
            setLy(getY());
            setLz(getZ());
            square = getCurrentSquare();
            if(sprite != null)
                if(!bUseParts)
                    sprite.update(def);
                else
                    legsSprite.update(def);
            setStateEventDelayTimer(getStateEventDelayTimer() - 1.0F);
        }
        if(!FrameLoader.bClient)
            stateMachine.update();
        if(!FrameLoader.bClient)
        {
            if(((this instanceof IsoPlayer) || (this instanceof IsoSurvivor)) && RemoteID == -1)
            {
                if(Health > 0.0F && BodyDamage.getHealth() > 0.0F && !Asleep && getAllowBehaviours())
                    masterBehaviorList.process(new DecisionPath(), this);
                if(!Asleep && (this instanceof IsoPlayer))
                    RainManager.SetPlayerLocation(getCell(), getCurrentSquare());
            }
            FireCheck();
            SpreadFire();
            ReduceHealthWhenBurning();
        }
        SpeakTime--;
        if(SpeakTime <= 0 || Health <= 0.0F || BodyDamage.getHealth() <= 0.0F)
            Speaking = false;
        if(stateMachine.getCurrent() == StaggerBackDieState.instance())
        {
            if(getStateEventDelayTimer() > 20F)
            {
                BloodImpactX = getX();
                BloodImpactY = getY();
                BloodImpactZ = getZ();
            }
        } else
        {
            BloodImpactX = getX();
            BloodImpactY = getY();
            BloodImpactZ = getZ();
        }
        if(!FrameLoader.bClient && !(this instanceof IsoZombie))
        {
            for(int m = 0; m < inventory.Items.size(); m++)
            {
                InventoryItem item = (InventoryItem)inventory.Items.get(m);
                if(item instanceof IUpdater)
                    ((IUpdater)item).update();
            }

        }
        LastZombieKills = ZombieKills;
    }

    public void DoFloorSplat(IsoGridSquare sq, String id, boolean bFlip, float offZ, float alpha)
    {
        if(sq == null)
            return;
        sq.DirtySlice();
        boolean bDone = false;
        IsoObject best = null;
        for(int i = 0; i < sq.getObjects().size(); i++)
        {
            IsoObject obj = (IsoObject)sq.getObjects().get(i);
            if(obj.sprite != null && obj.sprite.getProperties().Is(IsoFlagType.solidfloor) && best == null)
                best = obj;
        }

        if(best != null && best.sprite != null && (best.sprite.getProperties().Is(IsoFlagType.vegitation) || best.sprite.getProperties().Is(IsoFlagType.solidfloor)))
        {
            IsoSprite spr1 = IsoSprite.getSprite(getCell().SpriteManager, id, 0);
            if(spr1 == null)
                return;
            if(best.AttachedAnimSprite.size() > 7)
                return;
            IsoSpriteInstance spr = new IsoSpriteInstance(spr1);
            best.AttachedAnimSprite.add(spr);
            ((IsoSpriteInstance)best.AttachedAnimSprite.get(best.AttachedAnimSprite.size() - 1)).Flip = bFlip;
            ((IsoSpriteInstance)best.AttachedAnimSprite.get(best.AttachedAnimSprite.size() - 1)).tintr = 0.5F + (float)Rand.Next(100) / 2000F;
            ((IsoSpriteInstance)best.AttachedAnimSprite.get(best.AttachedAnimSprite.size() - 1)).tintg = 0.7F + (float)Rand.Next(300) / 1000F;
            ((IsoSpriteInstance)best.AttachedAnimSprite.get(best.AttachedAnimSprite.size() - 1)).tintb = 0.7F + (float)Rand.Next(300) / 1000F;
            ((IsoSpriteInstance)best.AttachedAnimSprite.get(best.AttachedAnimSprite.size() - 1)).SetAlpha(0.4F * alpha * 0.6F);
            ((IsoSpriteInstance)best.AttachedAnimSprite.get(best.AttachedAnimSprite.size() - 1)).SetTargetAlpha(0.4F * alpha * 0.6F);
            ((IsoSpriteInstance)best.AttachedAnimSprite.get(best.AttachedAnimSprite.size() - 1)).offZ = -offZ;
            float offX = 0.0F;
            ((IsoSpriteInstance)best.AttachedAnimSprite.get(best.AttachedAnimSprite.size() - 1)).offX = offX;
            bDone = true;
        }
    }

    void DoSplat(IsoGridSquare sq, String id, boolean bFlip, IsoFlagType prop, float offX, float offZ, float alpha)
    {
        if(sq == null)
            return;
        boolean bDone = false;
        sq.DirtySlice();
        for(int i = 0; i < sq.getObjects().size(); i++)
        {
            IsoObject obj = (IsoObject)sq.getObjects().get(i);
            if(obj.sprite == null || !obj.sprite.getProperties().Is(prop))
                continue;
            IsoSprite spr1 = IsoSprite.getSprite(getCell().SpriteManager, id, 0);
            if(spr1 == null)
                return;
            IsoSpriteInstance spr = new IsoSpriteInstance(spr1);
            obj.AttachedAnimSprite.add(spr);
            ((IsoSpriteInstance)obj.AttachedAnimSprite.get(obj.AttachedAnimSprite.size() - 1)).Flip = bFlip;
            ((IsoSpriteInstance)obj.AttachedAnimSprite.get(obj.AttachedAnimSprite.size() - 1)).tintr = 0.7F + (float)Rand.Next(300) / 1000F;
            ((IsoSpriteInstance)obj.AttachedAnimSprite.get(obj.AttachedAnimSprite.size() - 1)).tintg = 0.9F + (float)Rand.Next(300) / 3000F;
            ((IsoSpriteInstance)obj.AttachedAnimSprite.get(obj.AttachedAnimSprite.size() - 1)).tintb = 0.9F + (float)Rand.Next(300) / 3000F;
            ((IsoSpriteInstance)obj.AttachedAnimSprite.get(obj.AttachedAnimSprite.size() - 1)).SetAlpha(0.4F * alpha);
            ((IsoSpriteInstance)obj.AttachedAnimSprite.get(obj.AttachedAnimSprite.size() - 1)).SetTargetAlpha(0.4F * alpha);
            ((IsoSpriteInstance)obj.AttachedAnimSprite.get(obj.AttachedAnimSprite.size() - 1)).offZ = -offZ;
            if(bFlip)
            {
                Vector2 vec = new Vector2(32F, 16F);
                offX = -(2.0F / vec.getLength());
            }
            ((IsoSpriteInstance)obj.AttachedAnimSprite.get(obj.AttachedAnimSprite.size() - 1)).offX = offX;
            bDone = true;
        }

    }

    InventoryItem FindAndReturn(String require, InventoryItem item)
    {
        if(item == null)
            return null;
        if(require == null)
            return item;
        if(require.equals(item.getType()))
            return item;
        else
            return null;
    }

    public boolean onMouseLeftClick(int x, int y)
    {
        if(IsoCamera.CamCharacter != IsoPlayer.instance && Core.bDebug)
            IsoCamera.CamCharacter = this;
        return super.onMouseLeftClick(x, y);
    }

    private void calculateStats()
    {
        if(this instanceof IsoZombie)
            return;
        if(LuaHookManager.TriggerHook("CalculateStats", this))
            return;
        stats.endurancelast = stats.endurance;
        if(stats.Tripping)
            stats.TrippingRotAngle += 0.06F;
        else
            stats.TrippingRotAngle += 0.0F;
        if(this != IsoPlayer.instance || !IsoPlayer.GhostMode)
            if(this == IsoPlayer.instance && Asleep)
                stats.thirst += ZomboidGlobals.ThirstSleepingIncrease;
            else
                stats.thirst += ZomboidGlobals.ThirstIncrease;
        if(stats.thirst > 0.1F)
        {
            InventoryItem item = inventory.FindWaterSource();
            if(item != null)
            {
                stats.thirst -= 0.1F;
                item.Use();
            }
        }
        float hungerMult = 1.0F - stats.hunger;
        if(HasTrait("HeartyAppitite"))
            hungerMult *= 1.5F;
        if(HasTrait("LightEater"))
            hungerMult *= 0.75F;
        float traitPanic = 1.0F;
        if(HasTrait("Cowardly"))
            traitPanic = 2.0F;
        if(HasTrait("Brave"))
            traitPanic = 0.3F;
        if(stats.Panic > 100F)
            stats.Panic = 100F;
        stats.stress += (double)WorldSoundManager.instance.getStressFromSounds((int)getX(), (int)getY(), (int)getZ()) * ZomboidGlobals.StressFromSoundsMultiplier;
        if(BodyDamage.getNumPartsBitten() > 0)
            stats.stress += ZomboidGlobals.StressFromBiteOrScratch * (double)GameTime.instance.getMultiplier();
        if(BodyDamage.getNumPartsScratched() > 0)
            stats.stress += ZomboidGlobals.StressFromBiteOrScratch * (double)GameTime.instance.getMultiplier();
        if(BodyDamage.IsInfected() || BodyDamage.IsFakeInfected())
            stats.stress += ZomboidGlobals.StressFromBiteOrScratch * (double)GameTime.instance.getMultiplier();
        if(HasTrait("Brooding"))
            stats.Anger -= ZomboidGlobals.AngerDecrease * ZomboidGlobals.BroodingAngerDecreaseMultiplier * (double)GameTime.instance.getMultiplier();
        else
            stats.Anger -= ZomboidGlobals.AngerDecrease * (double)GameTime.instance.getMultiplier();
        if(stats.Anger > 1.0F)
            stats.Anger = 1.0F;
        if(stats.Anger < 0.0F)
            stats.Anger = 0.0F;
        if(IsoPlayer.instance == this && IsoPlayer.instance.Asleep)
        {
            if(stats.fatigue > 0.0F)
                stats.fatigue -= ZomboidGlobals.SleepFatigueReduction * (double)GameTime.instance.getMultiplier();
            if(IsoPlayer.instance.Moodles.getMoodleLevel(MoodleType.FoodEaten) == 0)
                IsoPlayer.instance.stats.hunger += ZomboidGlobals.HungerIncreaseWhileAsleep * (double)hungerMult * (double)GameTime.instance.getMultiplier();
            else
                IsoPlayer.instance.stats.hunger += (float)(ZomboidGlobals.HungerIncreaseWhenWellFed * ZomboidGlobals.HungerIncreaseWhileAsleep * (double)GameTime.instance.getMultiplier());
            if(ForceWakeUpTime == 0.0F)
                ForceWakeUpTime = 9F;
            if(Asleep && GameTime.getInstance().getTimeOfDay() > ForceWakeUpTime && GameTime.getInstance().getLastTimeOfDay() < ForceWakeUpTime)
            {
                UIManager.setbFadeBeforeUI(true);
                UIManager.FadeIn(4);
                ScriptManager.instance.Trigger("OnPlayerWake");
                UIManager.getSpeedControls().SetCurrentGameSpeed(1);
                ForceWakeUpTime = -1F;
                Asleep = false;
                stats.fatigue = 0.0F;
                TutorialManager.instance.StealControl = false;
            }
        } else
        {
            stats.stress -= ZomboidGlobals.StressReduction * (double)GameTime.instance.getMultiplier();
            float fatiguemod = 1.0F - stats.endurance;
            if(fatiguemod < 0.3F)
                fatiguemod = 0.3F;
            stats.fatigue += ZomboidGlobals.FatigueIncrease * (double)fatiguemod * (double)GameTime.instance.getMultiplier();
            if(this == IsoPlayer.instance)
            {
                if(IsoPlayer.instance.Moodles.getMoodleLevel(MoodleType.FoodEaten) == 0)
                    IsoPlayer.instance.stats.hunger += ZomboidGlobals.HungerIncrease * (double)hungerMult * (double)GameTime.instance.getMultiplier();
                else
                    IsoPlayer.instance.stats.hunger += (float)ZomboidGlobals.HungerIncreaseWhenWellFed * GameTime.instance.getMultiplier();
            } else
            {
                stats.hunger += 2.5E-005F * GameTime.instance.getMultiplier();
            }
            if(getCurrentSquare() == getLastSquare())
            {
                stats.idleboredom += 5E-005F * GameTime.instance.getMultiplier();
                stats.idleboredom += (float)Rand.Next(5) / 2000F;
            }
            if(getCurrentSquare() != null && getLastSquare() != null && getCurrentSquare().getRoom() == getLastSquare().getRoom() && getCurrentSquare().getRoom() != null)
            {
                stats.idleboredom += 0.0001F * GameTime.instance.getMultiplier();
                stats.idleboredom += ((float)Rand.Next(10) / 4000F) * GameTime.instance.getMultiplier();
            }
        }
        if(getLx() != getX() || getLy() != getY())
        {
            tempo.x = getX() - getLx();
            tempo.y = getY() - getLy();
            Vector2 moveVec = tempo;
            float del = 1.0F;
            float useDel = moveVec.getLength() / getMoveSpeed();
            if(useDel < 1.0F)
                del = 0.5F;
            if(useDel > 1.0F)
                del = 4F;
            float dif = moveVec.getLength() * 0.0018F * del;
            dif /= 0.06F;
            if(dif > 0.0F && dif < 0.3F)
                dif = 0.3F;
            dif = 1.0F - dif;
            if(dif < 0.0F)
                dif = 1.0F;
            if(dif > 1.0F)
                dif = 1.0F;
            stats.endurance += 0.0004F * getRecoveryMod() * GameTime.instance.getMultiplier() * dif;
        }
        if(stats.endurance < 0.0F)
            stats.endurance = 0.0F;
        if(stats.endurance > 1.0F)
            stats.endurance = 1.0F;
        if(stats.hunger > 1.0F)
            stats.hunger = 1.0F;
        if(stats.hunger < 0.0F)
            stats.hunger = 0.0F;
        if(stats.stress > 1.0F)
            stats.stress = 1.0F;
        if(stats.stress < 0.0F)
            stats.stress = 0.0F;
        if(stats.fatigue > 1.0F)
            stats.fatigue = 1.0F;
        if(stats.fatigue < 0.0F)
            stats.fatigue = 0.0F;
        float mod = 1.0F - stats.stress - 0.5F;
        mod *= 0.0001F;
        if(mod > 0.0F)
            mod += 0.5F;
        stats.morale += mod;
        if(stats.morale > 1.0F)
            stats.morale = 1.0F;
        if(stats.morale < 0.0F)
            stats.morale = 0.0F;
        if(stats.endurance < 0.0F)
            stats.endurance = 0.0F;
    }

    private void checkDrawWeaponPost(float x, float y, float z, ColorInfo col)
    {
        if(leftHandItem instanceof HandWeapon)
            useHandWeapon = (HandWeapon)leftHandItem;
        WeaponOverlayUtils.DrawWeapon(useHandWeapon, this, sprite, x, y, z, col);
    }

    private void checkDrawWeaponPre(float x, float y, float z, ColorInfo col)
    {
        if(dir == IsoDirections.S || dir == IsoDirections.SE || dir == IsoDirections.E || dir == IsoDirections.NE || dir == IsoDirections.SW)
            return;
        if(!sprite.CurrentAnim.name.contains("Attack_"))
            return;
        else
            return;
    }

    public void splatBlood(int dist, float alpha)
    {
        IsoGridSquare n = getCurrentSquare();
        IsoGridSquare w = getCurrentSquare();
        for(int dUse = 0; dUse < dist; dUse++)
        {
            if(n != null)
                n = getCell().getGridSquare((int)getX(), (int)getY() - dUse, (int)getZ());
            if(w != null)
                w = getCell().getGridSquare((int)getX() - dUse, (int)getY(), (int)getZ());
            float offX = 0.0F;
            boolean bLeft;
            boolean bRight;
            int min;
            int max;
            int range;
            boolean bDoTwo;
            int startUse;
            int endUse;
            float offZ;
            if(w != null && w.testCollideAdjacent(null, -1, 0, 0))
            {
                bLeft = false;
                bRight = false;
                min = 0;
                max = 0;
                if(w.getS() != null && w.getS().testCollideAdjacent(null, -1, 0, 0))
                    bLeft = true;
                if(w.getN() != null && w.getN().testCollideAdjacent(null, -1, 0, 0))
                    bRight = true;
                if(bLeft)
                    min = -1;
                if(bRight)
                    max = 1;
                range = max - min;
                bDoTwo = false;
                startUse = 0;
                endUse = 0;
                if(range > 0 && Rand.Next(2) == 0)
                {
                    bDoTwo = true;
                    if(range > 1)
                    {
                        if(Rand.Next(2) == 0)
                        {
                            startUse = -1;
                            endUse = 0;
                        } else
                        {
                            startUse = 0;
                            endUse = 1;
                        }
                    } else
                    {
                        startUse = min;
                        endUse = max;
                    }
                }
                offZ = (float)Rand.Next(100) / 300F;
                if(bDoTwo)
                {
                    Integer id = Integer.valueOf(24 + Rand.Next(2) * 2);
                    if(Rand.Next(2) == 0)
                        id = Integer.valueOf(id.intValue() + 8);
                    IsoGridSquare a = getCell().getGridSquare(w.getX(), w.getY() + startUse, w.getZ());
                    IsoGridSquare b = getCell().getGridSquare(w.getX(), w.getY() + endUse, w.getZ());
                    DoSplat(a, (new StringBuilder()).append("BloodWalls_").append(id.intValue() + 1).toString(), false, IsoFlagType.cutW, offX, offZ, alpha);
                    DoSplat(b, (new StringBuilder()).append("BloodWalls_").append(id).toString(), false, IsoFlagType.cutW, offX, offZ, alpha);
                } else
                {
                    int id = 0;
                    switch(Rand.Next(3))
                    {
                    case 0: // '\0'
                        id = 8 + Rand.Next(3);
                        break;

                    case 1: // '\001'
                    case 2: // '\002'
                        id = Rand.Next(5);
                        break;
                    }
                    DoSplat(w, (new StringBuilder()).append("BloodWalls_").append(id).toString(), false, IsoFlagType.cutW, offX, offZ, alpha);
                }
                w = null;
            }
            if(n == null || !n.testCollideAdjacent(null, 0, -1, 0))
                continue;
            bLeft = false;
            bRight = false;
            min = 0;
            max = 0;
            if(n.getW() != null && n.getW().testCollideAdjacent(null, 0, -1, 0))
                bLeft = true;
            if(n.getE() != null && n.getE().testCollideAdjacent(null, 0, -1, 0))
                bRight = true;
            if(bLeft)
                min = -1;
            if(bRight)
                max = 1;
            range = max - min;
            bDoTwo = false;
            startUse = 0;
            endUse = 0;
            if(range > 0 && Rand.Next(2) == 0)
            {
                bDoTwo = true;
                if(range > 1)
                {
                    if(Rand.Next(2) == 0)
                    {
                        startUse = -1;
                        endUse = 0;
                    } else
                    {
                        startUse = 0;
                        endUse = 1;
                    }
                } else
                {
                    startUse = min;
                    endUse = max;
                }
            }
            offZ = (float)Rand.Next(100) / 300F;
            if(bDoTwo)
            {
                Integer id = Integer.valueOf(24 + Rand.Next(2) * 2);
                if(Rand.Next(2) == 0)
                    id = Integer.valueOf(id.intValue() + 8);
                IsoGridSquare a = getCell().getGridSquare(n.getX(), n.getY() + startUse, n.getZ());
                IsoGridSquare b = getCell().getGridSquare(n.getX(), n.getY() + endUse, n.getZ());
                DoSplat(a, (new StringBuilder()).append("BloodWalls_").append(id.intValue() + 1).toString(), true, IsoFlagType.cutN, offX, offZ, alpha);
                DoSplat(b, (new StringBuilder()).append("BloodWalls_").append(id).toString(), true, IsoFlagType.cutN, offX, offZ, alpha);
            } else
            {
                int id = 0;
                switch(Rand.Next(3))
                {
                case 0: // '\0'
                    id = 8 + Rand.Next(3);
                    break;

                case 1: // '\001'
                case 2: // '\002'
                    id = Rand.Next(5);
                    break;
                }
                DoSplat(n, (new StringBuilder()).append("BloodWalls_").append(id).toString(), true, IsoFlagType.cutN, offX, offZ, alpha);
            }
            n = null;
        }

    }

    private void UpdateWounds()
    {
        for(int n = 0; n < wounds.size(); n++)
        {
            Wound wound = (Wound)wounds.get(n);
            if(wound.tourniquet)
                wound.bleeding -= 5E-005F;
            if(wound.bandaged)
                wound.bleeding -= 2E-005F;
        }

    }

    protected static THashMap SurvivorMap = new THashMap();
    protected IsoGameCharacter FollowingTarget;
    protected NulledArrayList LocalList;
    protected NulledArrayList LocalNeutralList;
    protected NulledArrayList LocalGroupList;
    protected NulledArrayList LocalRelevantEnemyList;
    protected float dangerLevels;
    protected Stack MeetList;
    protected Order Order;
    protected Stack Orders;
    protected ArrayList PerkList;
    protected Order PersonalNeed;
    protected Stack PersonalNeeds;
    protected int leaveBodyTimedown;
    protected boolean AllowConversation;
    protected int ReanimPhase;
    protected int ReanimateTimer;
    protected int ReanimAnimFrame;
    protected int ReanimAnimDelay;
    protected boolean Reanim;
    protected boolean VisibleToNPCs;
    protected int DieCount;
    protected float llx;
    protected float lly;
    protected float llz;
    protected int RemoteID;
    protected int NumSurvivorsInVicinity;
    protected float LevelUpMultiplier;
    protected static int LevelUpLevels[] = {
        25, 75, 150, 225, 300, 400, 500, 600, 700, 800, 
        900, 1000, 1200, 1400, 1600, 1800, 2000, 2200, 2400, 2600, 
        2800, 3000, 3200, 3400, 3600, 3800, 4000, 4400, 4800, 5200, 
        5600, 6000
    };
    protected XP xp;
    protected int NumberOfPerksToPick;
    protected NulledArrayList CanUpgradePerk;
    protected int LastLocalEnemies;
    protected NulledArrayList VeryCloseEnemyList;
    protected THashMap LastKnownLocation;
    protected IsoGameCharacter AttackedBy;
    protected boolean IgnoreStaggerBack;
    protected boolean AttackWasSuperAttack;
    protected int TimeThumping;
    protected int PatienceMax;
    protected int PatienceMin;
    protected int Patience;
    protected Stack CharacterActions;
    public Vector2 angle;
    public boolean Asleep;
    protected int AttackDelay;
    protected int AttackDelayMax;
    protected int AttackDelayUse;
    protected int ZombieKills;
    protected int LastZombieKills;
    protected boolean superAttack;
    protected float ForceWakeUpTime;
    protected BodyDamage BodyDamage;
    protected InventoryItem craftIngredient1;
    protected InventoryItem craftIngredient2;
    protected InventoryItem craftIngredient3;
    protected InventoryItem craftIngredient4;
    protected State defaultState;
    protected SurvivorDesc descriptor;
    protected Stack FamiliarBuildings;
    protected AStarPathFinderResult finder;
    protected float FireKillRate;
    protected int FireSpreadProbability;
    protected int footStepCounter;
    protected int footStepCounterMax;
    protected float Health;
    protected MasterSurvivorBehavior masterProper;
    protected IsoGameCharacter hitBy;
    protected String hurtSound;
    protected boolean IgnoreMovementForDirection;
    protected ItemContainer inventory;
    protected IsoDirections lastdir;
    protected InventoryItem leftHandItem;
    protected InventoryItem ClothingItem_Head;
    protected InventoryItem ClothingItem_Torso;
    protected InventoryItem ClothingItem_Hands;
    protected InventoryItem ClothingItem_Legs;
    protected InventoryItem ClothingItem_Feet;
    protected SequenceBehavior masterBehaviorList;
    protected int NextWander;
    protected boolean OnFire;
    protected Path path;
    protected int pathIndex;
    protected float PathSpeed;
    protected int PathTargetX;
    protected int PathTargetY;
    protected int PathTargetZ;
    protected SurvivorPersonality Personality;
    protected InventoryItem rightHandItem;
    protected String sayLine;
    protected Color SpeakColour;
    protected float slowFactor;
    protected int slowTimer;
    protected boolean bUseParts;
    protected boolean Speaking;
    protected int SpeakTime;
    protected float speedMod;
    protected float staggerTimeMod;
    protected StateMachine stateMachine;
    protected Moodles Moodles;
    protected Stats stats;
    protected Stack TagGroup;
    protected Stack UsedItemsOn;
    protected HandWeapon useHandWeapon;
    protected IsoSprite torsoSprite;
    protected IsoSprite legsSprite;
    protected IsoSprite headSprite;
    protected IsoSprite shoeSprite;
    protected IsoSprite topSprite;
    protected IsoSprite bottomsSprite;
    protected Stack wounds;
    protected IsoGridSquare attackTargetSquare;
    protected float BloodImpactX;
    protected float BloodImpactY;
    protected float BloodImpactZ;
    protected IsoSprite bloodSplat;
    protected boolean bOnBed;
    protected Vector2 moveForwardVec;
    protected boolean pathing;
    protected Stack LocalEnemyList;
    protected Stack EnemyList;
    protected NulledArrayList Traits;
    protected Integer maxWeight;
    protected int maxWeightBase;
    protected float SleepingTabletDelta;
    protected int BetaEffect;
    protected int DepressEffect;
    protected int SleepingTabletEffect;
    protected float BetaDelta;
    protected float DepressDelta;
    protected int PainEffect;
    protected float PainDelta;
    protected static Vector2 tempo = new Vector2();
    static Vector2 tempo2 = new Vector2();
    protected boolean SpottedSinceAlphaZero;
    protected static ColorInfo inf = new ColorInfo();
    protected boolean bDoDefer;
    protected Location LastHeardSound;
    protected float lrx;
    protected float lry;
    protected boolean bClimbing;
    protected boolean lastCollidedW;
    protected boolean lastCollidedN;
    protected static OnceEvery testPlayerSpotInDarkness = new OnceEvery(0.15F, true);
    protected int timeTillForgetLocation;
    protected int fallTime;
    protected float lastFallSpeed;
    protected boolean bFalling;

}
