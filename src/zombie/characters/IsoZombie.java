package zombie.characters;

import java.io.*;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import zombie.FrameLoader;
import zombie.GameTime;
import zombie.Lua.LuaEventManager;
import zombie.SoundManager;
import zombie.WorldSoundManager;
import zombie.ai.State;
import zombie.ai.StateMachine;
import zombie.ai.astar.*;
import zombie.ai.states.*;
import zombie.core.Collections.NulledArrayList;
import zombie.core.Rand;
import zombie.core.textures.ColorInfo;
import zombie.core.utils.OnceEvery;
import zombie.inventory.ItemContainer;
import zombie.inventory.types.HandWeapon;
import zombie.iso.*;
import zombie.iso.objects.interfaces.Thumpable;
import zombie.iso.sprite.*;
import zombie.ui.TutorialManager;

// Referenced classes of package zombie.characters:
//            IsoGameCharacter, ZombieGroup, IsoPlayer, SurvivorDesc, 
//            SurvivorFactory, IsoSurvivor

public class IsoZombie extends IsoGameCharacter
{

    public String getObjectName()
    {
        return "Zombie";
    }

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

    public IsoZombie(IsoCell cell)
    {
        
    	 super(cell, 0.0F, 0.0F, 0.0F);
    	 /*  199 */     this.Health = (1.5F + Rand.Next(0.0F, 0.3F));
    	 /*      */ 
    	 /*  201 */     this.weight = 0.7F;
    	 /*  202 */     this.dir = IsoDirections.fromIndex(Rand.Next(8));
    	 /*      */ 
    	 /*  204 */     int val = Rand.Next(10) + 1;
    	 /*      */ 
    	 /*  208 */     int r = Rand.Next(3) + 1;
    	 /*  209 */     this.palette = r;
    	 /*  210 */     this.SpriteName += r;
    	 /*      */ 
    	 /*  218 */     SurvivorDesc desc = SurvivorFactory.CreateSurvivor();
    	 /*  219 */     InitSpritePartsZombie(this.SpriteName, desc.legs, desc.torso, desc.head, desc.top, desc.bottoms, desc.shoes, desc.skinpal, desc.toppal, desc.bottomspal, desc.shoespal);
    	 /*      */ 
    	 /*  234 */     this.def.Frame = (short)Rand.Next(this.sprite.CurrentAnim.Frames.size());
    	 /*  235 */     this.sprite.def.tintr = (0.9F + (float)Rand.Next(10) / 100.0F);
    	 /*  236 */     this.sprite.def.tintg = (0.9F + (float)Rand.Next(10) / 100.0F);
    	 /*  237 */     this.sprite.def.tintb = (0.9F + (float)Rand.Next(10) / 100.0F);
    	 /*      */ 
    	 /*  239 */     this.defaultState = WanderState.instance();
    	 /*      */ 
    	 /*  241 */     this.stateMachine.changeState(this.defaultState);
    	 /*      */ 
    	 /*  243 */     DoZombieSpeeds();
    	 /*      */ 
    	 /*  245 */     this.width = 0.3F;
    	 /*      */ 
    	 /*  247 */     this.targetAlpha = 0.0F;
    	 /*  248 */     this.finder.maxSearchDistance = 20;
    	 /*  249 */     this.alpha = 0.0F;
    	 /*      */ 
    	 /*  251 */     this.group.zombies[0] = this;
    	 /*  252 */     this.group.Leader = this;
    	 /*      */ 
    	 /*  254 */     if (Rand.Next(3) == 0)
    	 /*  255 */       DoZombieInventory();
    }

    public IsoZombie(IsoCell cell, SurvivorDesc desc)
    {
    	super(cell, 0.0F, 0.0F, 0.0F);
    	/*  261 */     this.Health = (1.5F + Rand.Next(0.0F, 0.3F));
    	/*      */ 
    	/*  263 */     this.weight = 0.7F;
    	/*  264 */     this.dir = IsoDirections.fromIndex(Rand.Next(8));
    	/*      */ 
    	/*  266 */     int val = Rand.Next(10) + 1;
    	/*      */ 
    	/*  270 */     int r = Rand.Next(3) + 1;
    	/*  271 */     this.palette = r;
    	/*  272 */     this.SpriteName += r;
    	/*      */ 
    	/*  280 */     InitSpritePartsZombie(this.SpriteName, desc.legs, desc.torso, desc.head, desc.top, desc.bottoms, desc.shoes, desc.skinpal, desc.toppal, desc.bottomspal, desc.shoespal);
    	/*      */ 
    	/*  295 */     this.def.Frame = (short)Rand.Next(this.sprite.CurrentAnim.Frames.size());
    	/*  296 */     this.sprite.def.tintr = (0.9F +  (float)Rand.Next(10) / 100.0F);
    	/*  297 */     this.sprite.def.tintg = (0.9F +  (float)Rand.Next(10) / 100.0F);
    	/*  298 */     this.sprite.def.tintb = (0.9F +  (float)Rand.Next(10) / 100.0F);
    	/*      */ 
    	/*  300 */     this.defaultState = WanderState.instance();
    	/*      */ 
    	/*  302 */     this.stateMachine.changeState(this.defaultState);
    	/*      */ 
    	/*  304 */     DoZombieSpeeds();
    	/*      */ 
    	/*  306 */     this.width = 0.3F;
    	/*      */ 
    	/*  308 */     this.targetAlpha = 0.0F;
    	/*  309 */     this.finder.maxSearchDistance = 20;
    	/*  310 */     this.alpha = 0.0F;
    	/*      */ 
    	/*  312 */     this.group.zombies[0] = this;
    	/*  313 */     this.group.Leader = this;
    	/*      */ 
    	/*  315 */     if (Rand.Next(3) == 0)
    	/*  316 */       DoZombieInventory();
    }

    public void PathTo(int x, int y, int z, boolean critical)
    {
        if(AllowRepathDelay > 0 && stateMachine.getCurrent() == PathFindState.instance())
        {
            return;
        } else
        {
            super.PathTo(x, y, z, critical);
            return;
        }
    }

    public void PathTo(int x, int y, int z, boolean critical, int delay)
    {
        if(AllowRepathDelay > 0 && stateMachine.getCurrent() == PathFindState.instance())
        {
            return;
        } else
        {
            super.PathTo(x, y, z, critical, delay);
            return;
        }
    }

    public IsoZombie(IsoCell cell, int palette)
    {
    	super(cell, 0.0F, 0.0F, 0.0F);
    	/*      */ 
    	/*  342 */     this.Health = (1.5F + Rand.Next(0.0F, 0.3F));
    	/*  343 */     this.palette = palette;
    	/*  344 */     this.dir = IsoDirections.fromIndex(Rand.Next(8));
    	/*  345 */     this.weight = 0.7F;
    	/*      */ 
    	/*  348 */     int val = palette;
    	/*  349 */     String str = "Zombie_palette";
    	/*      */ 
    	/*  351 */     if (val == 10)
    	/*      */     {
    	/*  353 */       str = str + "10";
    	/*      */     }
    	/*      */     else
    	/*      */     {
    	/*  357 */       str = str + "0" + Integer.toString(val);
    	/*      */     }
    	/*      */ 
    	/*  361 */     int r = Rand.Next(3) + 1;
    	/*  362 */     this.SpriteName += r;
    	/*      */ 
    	/*  364 */     this.palette = r;
    	/*      */ 
    	/*  369 */     SurvivorDesc desc = SurvivorFactory.CreateSurvivor();
    	/*  370 */     InitSpritePartsZombie(this.SpriteName, desc.legs, desc.torso, desc.head, desc.top, desc.bottoms, desc.shoes, desc.skinpal, desc.toppal, desc.bottomspal, desc.shoespal);
    	/*      */ 
    	/*  374 */     this.def.Frame = (short)Rand.Next(this.sprite.CurrentAnim.Frames.size());
    	/*  375 */     this.sprite.def.tintr = (0.9F + (float)Rand.Next(10) / 100.0F);
    	/*  376 */     this.sprite.def.tintg = (0.9F + (float)Rand.Next(10) / 100.0F);
    	/*  377 */     this.sprite.def.tintb = (0.9F + (float)Rand.Next(10) / 100.0F);
    	/*      */ 
    	/*  379 */     this.defaultState = WanderState.instance();
    	/*      */ 
    	/*  381 */     this.stateMachine.changeState(this.defaultState);
    	/*      */ 
    	/*  383 */     DoZombieSpeeds();
    	/*  384 */     this.width = 0.3F;
    	/*      */ 
    	/*  386 */     this.finder.maxSearchDistance = 20;
    	/*  387 */     this.alpha = 0.0F;
    	/*  388 */     this.group.zombies[0] = this;
    	/*  389 */     this.group.Leader = this;
    	/*      */ 
    	/*  391 */     if (Rand.Next(3) == 0)
    	/*  392 */       DoZombieInventory();
    }

    public void load(ByteBuffer input)
        throws IOException
    {
    	super.load(input);
    	/*      */ 
    	/*  400 */     this.palette = input.getInt();
    	/*  401 */     int val = this.palette;
    	/*  402 */     String str = "Zombie_palette";
    	/*      */ 
    	/*  404 */     if (val == 10)
    	/*      */     {
    	/*  406 */       str = str + "10";
    	/*      */     }
    	/*      */     else
    	/*      */     {
    	/*  410 */       str = str + "0" + Integer.toString(val);
    	/*      */     }
    	/*      */ 
    	/*  413 */     this.walkVariant = "ZombieWalk";
    	/*      */ 
    	/*  416 */     this.SpriteName = "Zombie";
    	/*      */ 
    	/*  418 */     this.SpriteName += this.palette;
    	/*      */ 
    	/*  424 */     SurvivorDesc desc = SurvivorFactory.CreateSurvivor();
    	/*  425 */     InitSpritePartsZombie(this.SpriteName, desc.legs, desc.torso, desc.head, desc.top, desc.bottoms, desc.shoes, desc.skinpal, desc.toppal, desc.bottomspal, desc.shoespal);
    	/*      */ 
    	/*  429 */     this.def.Frame = (short)Rand.Next(this.sprite.CurrentAnim.Frames.size());
    	/*  430 */     this.sprite.def.tintr = (0.9F + (float)Rand.Next(10) / 100.0F);
    	/*  431 */     this.sprite.def.tintg = (0.9F + (float)Rand.Next(10) / 100.0F);
    	/*  432 */     this.sprite.def.tintb = (0.9F + (float)Rand.Next(10) / 100.0F);
    	/*      */ 
    	/*  434 */     this.defaultState = WanderState.instance();
    	/*      */ 
    	/*  436 */     this.stateMachine.changeState(this.defaultState);
    	/*      */ 
    	/*  438 */     DoZombieSpeeds();
    	/*      */ 
    	/*  440 */     this.PathSpeed = input.getFloat();
    	/*      */ 
    	/*  442 */     setWidth(0.3F);
    	/*      */ 
    	/*  444 */     this.TimeSinceSeenFlesh = input.getInt();
    	/*      */ 
    	/*  449 */     this.alpha = 0.0F;
    	/*  450 */     if (Rand.Next(3) == 0)
    	/*  451 */       DoZombieInventory();
    	/*  452 */     getCell().getZombieList().add(this);
    }

    public void save(ByteBuffer output)
        throws IOException
    {
        super.save(output);
        output.putInt(palette);
        output.putFloat(PathSpeed);
        output.putInt(TimeSinceSeenFlesh);
    }

    public boolean AttemptAttack()
    {
        if(stateMachine.getCurrent() == AttackState.instance())
            return false;
        if(!Ghost)
        {
            stateMachine.changeState(AttackState.instance());
            return true;
        } else
        {
            return false;
        }
    }

    public void collideWith(IsoObject obj)
    {
        if(Ghost)
            return;
        if(obj.rerouteCollide != null)
            obj = rerouteCollide;
        if((obj instanceof Thumpable) && (stateMachine.getCurrent() == PathFindState.instance() || stateMachine.getCurrent() == LungeState.instance() || stateMachine.getCurrent() == WalkTowardState.instance()) && !Ghost)
        {
            setThumpTarget((Thumpable)obj);
            path = null;
            stateMachine.changeState(ThumpState.instance());
        }
        super.collideWith(obj);
    }

    public void Hit(HandWeapon weapon, IsoGameCharacter wielder, float damageSplit, boolean bIgnoreDamage, float modDelta)
    {
        super.Hit(weapon, wielder, damageSplit, bIgnoreDamage, modDelta);
        target = wielder;
    }

    public void Lunge()
    {
        if(stateMachine.getCurrent() == LungeState.instance())
            return;
        if(stateMachine.getCurrent() == AttackState.instance())
            return;
        if(stateMachine.getCurrent() == StaggerBackDieState.instance())
            return;
        if(stateMachine.getCurrent() == StaggerBackState.instance())
        {
            return;
        } else
        {
            SoundManager.instance.PlayWorldSound("zombie_agg0", getCurrentSquare(), 0.5F, 20F, 0.6F, 4, false);
            stateMachine.changeState(LungeState.instance());
            LungeTimer = 180F;
            return;
        }
    }

    public void onMouseLeftClick()
    {
        if(IsoPlayer.isAiming)
            return;
        if(IsoPlayer.instance.IsAttackRange(getX(), getY(), getZ()))
        {
            Vector2 vec = new Vector2(getX(), getY());
            vec.x -= IsoPlayer.instance.getX();
            vec.y -= IsoPlayer.instance.getY();
            vec.normalize();
            IsoPlayer.instance.DirectionFromVector(vec);
            IsoPlayer.instance.AttemptAttack();
        }
    }

    public void pathFinished()
    {
        AllowRepathDelay = 0;
        if(finder.progress == zombie.ai.astar.AStarPathFinder.PathFindProgress.failed)
            AllowRepathDelayMax = 300;
        else
            AllowRepathDelayMax = 30;
        stateMachine.changeState(WanderState.instance());
    }

    public void render(float x, float y, float z, ColorInfo col, boolean bDoChild)
    {
        if(IsoCamera.CamCharacter != IsoPlayer.instance)
        {
            targetAlpha = 1.0F;
            alpha = 1.0F;
        }
        super.render(x, y, z, col, bDoChild);
    }

    public void RespondToSound()
    {
        if(Ghost)
            return;
        if(Deaf)
            return;
        zombie.WorldSoundManager.WorldSound sound = WorldSoundManager.instance.getBiggestSoundZomb((int)getX(), (int)getY(), (int)getZ(), true);
        if(sound != null)
        {
            int x = 0;
            int y = 0;
            IsoGridSquare sq = null;
            PathTo(sound.x, sound.y, sound.z, false);
            AllowRepathDelay = AllowRepathDelayMax;
        }
    }

    public void spotted(IsoMovingObject other)
    {
        if(getCurrentSquare() == null)
            return;
        if(other == IsoPlayer.instance && !spottedPlayer.Check())
            return;
        if(other == IsoPlayer.instance && IsoPlayer.GhostMode)
            return;
        int chance = 200;
        float delta = (other.getCurrentSquare().getLightInfo().r + other.getCurrentSquare().getLightInfo().g + other.getCurrentSquare().getLightInfo().b) / 3F;
        float mdelta = (getCurrentSquare().getLightInfo().r + getCurrentSquare().getLightInfo().g + getCurrentSquare().getLightInfo().b) / 3F;
        if(delta > 1.0F)
            delta = 1.0F;
        if(delta < 0.0F)
            delta = 0.0F;
        if(mdelta > 1.0F)
            mdelta = 1.0F;
        if(mdelta < 0.0F)
            mdelta = 0.0F;
        float difdelta = 1.0F - (mdelta - delta);
        int n;
        if(other instanceof IsoPlayer)
            n = 0;
        if(getCurrentSquare() == null || other.getCurrentSquare() == null)
            ensureOnTile();
        if(other.getCurrentSquare().getRoom() != getCurrentSquare().getRoom())
        {
            chance = 100;
            if(other.getCurrentSquare().getRoom() != null && getCurrentSquare().getRoom() == null || other.getCurrentSquare().getRoom() == null && getCurrentSquare().getRoom() != null)
            {
                chance = 50;
                if(((IsoGameCharacter)other).IsSneaking())
                {
                    if(delta < 0.4F)
                        chance = 0;
                    else
                        chance = 10;
                } else
                if(other.getMovementLastFrame().getLength() <= 0.04F && delta < 0.4F)
                    chance = 10;
            }
        }
        tempo.x = other.getX();
        tempo.y = other.getY();
        tempo.x -= getX();
        tempo.y -= getY();
        if(other.getCurrentSquare().getZ() != current.getZ())
        {
            int dif = Math.abs(other.getCurrentSquare().getZ() - current.getZ());
            dif++;
            chance /= dif;
        }
        float dist = GameTime.getInstance().getViewDist();
        if(tempo.getLength() > dist)
            return;
        if(tempo.getLength() < dist)
            dist = tempo.getLength();
        if(dist > GameTime.getInstance().getViewDistMax())
            dist = GameTime.getInstance().getViewDistMax();
        tempo.normalize();
        Vector2 vecB = getVectorFromDirection();
        float angle = vecB.dot(tempo);
        if(dist > 1.0F)
            if(angle < -0.7F)
                chance = 0;
            else
            if(angle < -0.2F)
                chance /= 8;
            else
            if(angle < -0F)
                chance /= 4;
            else
            if(angle < 0.2F)
                chance /= 2;
        chance = (int)((float)chance * difdelta);
        int dif = (int)other.getZ() - (int)getZ();
        if(dif >= 1)
            chance /= dif;
        chance = (int)((float)chance * (1.0F - dist / GameTime.getInstance().getViewDist()));
        chance = (int)((float)chance * (1.0F - dist / GameTime.getInstance().getViewDist()));
        float movement = other.getMovementLastFrame().getLength();
        if(delta > 0.6F)
            delta = 1.0F;
        if(movement == 0.0F && delta <= 0.2F)
            delta = 0.0F;
        if(((IsoGameCharacter)other).IsSneaking() && (!(other instanceof IsoPlayer) || ((IsoPlayer)other).getTorchStrength() == 0.0F))
            chance = (int)((float)chance * delta);
        if(movement < 0.01F)
            chance = (int)((float)chance * 0.5F);
        else
        if(((IsoGameCharacter)other).IsSneaking())
            chance = (int)((float)chance * 0.6F);
        else
        if(movement < 0.06F)
            chance = (int)((float)chance * 0.8F);
        else
        if(movement > 0.06F)
            chance = (int)((float)chance * 3F);
        if(dist < 5F)
            chance *= 3;
        if(spottedLast == other && TimeSinceSeenFlesh < 200)
            chance = 1000;
        chance = (int)((float)chance * ((IsoGameCharacter)other).getSneakSpotMod());
        if(spottedLast == TutorialManager.instance.wife && other == IsoPlayer.instance && TimeSinceSeenFlesh < 30)
            return;
        if(spottedLast != other && target != null)
        {
            float distOther = IsoUtils.DistanceManhatten(getX(), getY(), other.getX(), other.getY());
            float distCurrent = IsoUtils.DistanceManhatten(getX(), getY(), target.getX(), target.getY());
            if(distOther > distCurrent)
                return;
        }
        if(Rand.Next(240) > chance)
        {
            if(chance > 20 && (other instanceof IsoPlayer))
                ((IsoPlayer)other).bCouldBeSeenThisFrame = true;
            return;
        }
        if(other instanceof IsoPlayer)
            ((IsoPlayer)other).bSeenThisFrame = true;
        spottedLast = other;
        BonusSpotTime = -1;
        LastTargetSeenX = (int)other.getX();
        LastTargetSeenY = (int)other.getY();
        LastTargetSeenZ = (int)other.getZ();
        if(stateMachine.getCurrent() == StaggerBackState.instance())
            return;
        target = other;
        vectorToTarget.x = other.getX();
        vectorToTarget.y = other.getY();
        vectorToTarget.x -= getX();
        vectorToTarget.y -= getY();
        float vecToTargetLength = vectorToTarget.getLength();
        TimeSinceSeenFlesh = 0;
        if(Rand.Next(400) != 0);
        if(getZ() == target.getZ() && vecToTargetLength <= 3.5F && getStateEventDelayTimer() <= 0.0F)
        {
            target = other;
            Lunge();
            return;
        }
        if(stateMachine.getCurrent() == PathFindState.instance() && AllowRepathDelay > 0 && !isCollidedThisFrame())
            return;
        if(!Ghost)
        {
            target = other;
            if(AllowRepathDelay > 0)
                return;
            PathTo((int)other.getX(), (int)other.getY(), (int)other.getZ(), false, Rand.Next(5));
            AllowRepathDelay = AllowRepathDelayMax * 4;
        }
    }

    public void preupdate()
    {
        FollowCount = 0;
        super.preupdate();
    }

    public void postupdate()
    {
        super.postupdate();
        if(getCurrentSquare() == null)
            IsoWorld.instance.CurrentCell.Remove(this);
    }

    public void update()
    {
        int n;
        if((sprite.CurrentAnim.name.equals("ZombieDeath") || sprite.CurrentAnim.name.equals("Die")) && (int)def.Frame == sprite.CurrentAnim.Frames.size() - 1)
            n = 0;
        if(IsoCamera.CamCharacter == this)
            n = 0;
        LuaEventManager.TriggerEvent("OnZombieUpdate", this);
        BonusSpotTime--;
        if(BonusSpotTime > 0 && spottedLast != null && !((IsoGameCharacter)spottedLast).isDead())
            spotted(spottedLast);
        if(this == group.Leader || group.Leader == null || group.Leader.isDead())
            group.update();
        if(FrameLoader.bClient)
        {
            predTest.x = predXVel;
            predTest.y = predYVel;
            if(predTest.getLength() > PathSpeed)
            {
                predTest.setLength(PathSpeed);
                predXVel = predTest.x;
                predYVel = predTest.y;
            }
            setNx(getNx() + predXVel);
            setNy(getNy() + predYVel);
        }
        super.update();
        ensureOnTile();
        State cur = stateMachine.getCurrent();
        if(cur == StaggerBackState.instance() || cur == BurntToDeath.instance() || cur == StaggerBackDieState.instance() || cur == ReanimateState.instance())
            return;
        if(cur != AttackState.instance())
        {
            if(walkVariantUse == null || cur != LungeState.instance())
                walkVariantUse = walkVariant;
            if(cur != StaggerBackDieState.instance() && cur != StaggerBackState.instance() && cur != ThumpState.instance())
            {
                if(!sprite.CurrentAnim.name.contains("ZombieWalk"))
                    def.Frame = Rand.Next(sprite.CurrentAnim.Frames.size());
                PlayAnim(walkVariantUse);
                def.setFrameSpeedPerFrame(0.23F);
                def.AnimFrameIncrease *= speedMod;
                setShootable(true);
            }
        }
        shootable = true;
        solid = true;
        if(TutorialManager.instance.wife != null && TutorialManager.instance.wife.Health > 0.0F && TutorialManager.instance.wife.getCurrentSquare() != null && getCurrentSquare() != null && getCurrentSquare().getRoom() != null && getCurrentSquare().getRoom() == TutorialManager.instance.wife.getCurrentSquare().getRoom() && AllowRepathDelay <= 0)
            spotted(TutorialManager.instance.wife);
        AllowRepathDelay--;
        TimeSinceSeenFlesh++;
        if(LungeTimer < 0.0F)
            target = null;
        else
        if(target != null)
        {
            vectorToTarget.x = target.getX();
            vectorToTarget.y = target.getY();
            vectorToTarget.x -= getX();
            vectorToTarget.y -= getY();
        }
        move.x = getNx() - getLx();
        move.y = getNy() - getLy();
        float len = move.getLength();
        float del = 1.0F - len / 0.08F;
        if(len <= 0.0F);
        if(IsoPlayer.instance.Waiting && Rand.Next(1000) == 0 || !IsoPlayer.instance.Waiting && Rand.Next(360) == 0 && (stateMachine.getCurrent() == WalkTowardState.instance() || stateMachine.getCurrent() == PathFindState.instance()))
            if(Ghost)
            {
                if(GhostShow)
                    SoundManager.instance.PlayWorldSoundWav("zombierand", getCurrentSquare(), 0.8F, 15F, 0.5F * (1.0F - IsoPlayer.instance.stats.Sanity), 9, true);
            } else
            {
                SoundManager.instance.PlayWorldSoundWav("zombierand", getCurrentSquare(), 0.8F, 15F, 0.3F, 9, true);
            }
        if(TimeSinceSeenFlesh > 240)
            RespondToSound();
        seperate();
    }

    public void Wander()
    {
        stateMachine.changeState(defaultState);
    }

    public Path FindPath(int sx, int sy, int sz, int tx, int ty, int tz)
    {
        if(getCurrentSquare() == null)
            return null;
        Path path = new Path();
        int iterations = 20;
        int curX = sx;
        int curY = sy;
        int curZ = sz;
        IsoDirections startDir = IsoDirections.Max;
        doneGrids.clear();
        doneGrids.add(getCurrentSquare());
        do
        {
            if(iterations <= 0)
                break;
            choiceGrids.clear();
            int n;
            if(curX == 88 && curY == 23)
                n = 0;
            float bestDist = 1E+007F;
            IsoGridSquare best = null;
            int lcx = 0;
            int lcy = 0;
            int lcz = 0;
            for(int ox = -1; ox <= 1; ox++)
            {
                for(int oy = -1; oy <= 1; oy++)
                {
                    for(int oz = -1; oz <= 1; oz++)
                    {
                        if(ox == 0 && oy == 0 && oz == 0 || getCell().blocked(this, curX + ox, curY + oy, curZ + oz, curX, curY, curZ))
                            continue;
                        IsoGridSquare sq = getCell().getGridSquare(curX + ox, curY + oy, curZ + oz);
                        if(sq != null && !doneGrids.contains(sq))
                        {
                            choiceGrids.add(sq);
                            lcx = ox;
                            lcy = oy;
                            lcz = oz;
                        }
                    }

                }

            }

            int nXInDir = curX;
            int nYInDir = curY;
            if(startDir == IsoDirections.N || startDir == IsoDirections.NE || startDir == IsoDirections.NW)
                nYInDir--;
            if(startDir == IsoDirections.S || startDir == IsoDirections.SE || startDir == IsoDirections.SW)
                nYInDir++;
            if(startDir == IsoDirections.E || startDir == IsoDirections.NE || startDir == IsoDirections.SE)
                nXInDir++;
            if(startDir == IsoDirections.W || startDir == IsoDirections.NW || startDir == IsoDirections.SW)
                nXInDir--;
            float requiredToChange = IsoUtils.DistanceManhattenSquare(nXInDir, nYInDir, tx, ty);
            if(startDir != IsoDirections.Max)
                best = getCell().getGridSquare(nXInDir, nYInDir, curZ);
            NulledArrayList finalChoiceGrids = new NulledArrayList();
            if(best == null)
                requiredToChange = 1000000F;
            else
                finalChoiceGrids.add(best);
            for(n = 0; n < choiceGrids.size(); n++)
            {
                IsoGridSquare sq = (IsoGridSquare)choiceGrids.get(n);
                float dist = IsoUtils.DistanceManhatten(sq.getX(), sq.getY(), tx, ty, sq.getZ(), tz);
                if(dist < requiredToChange && sq != best)
                    finalChoiceGrids.add(sq);
            }

            if(!finalChoiceGrids.isEmpty())
                best = (IsoGridSquare)finalChoiceGrids.get(Rand.Next(finalChoiceGrids.size()));
            finalChoiceGrids.clear();
            iterations--;
            if(best == null)
                break;
            doneGrids.add(best);
            if(best.getX() > curX)
            {
                if(best.getY() < curY)
                    startDir = IsoDirections.NE;
                else
                if(best.getY() < curY)
                    startDir = IsoDirections.SE;
                else
                    startDir = IsoDirections.E;
            } else
            if(best.getX() < curX)
            {
                if(best.getY() < curY)
                    startDir = IsoDirections.NW;
                else
                if(best.getY() < curY)
                    startDir = IsoDirections.SW;
                else
                    startDir = IsoDirections.W;
            } else
            if(best.getY() < curY)
                startDir = IsoDirections.N;
            else
            if(best.getY() < curY)
                startDir = IsoDirections.S;
            curX = best.getX();
            curY = best.getY();
            curZ = best.getZ();
        } while(curX != tx || curY != ty || curZ != tz);
        for(int n = 0; n < doneGrids.size(); n++)
        {
            IsoGridSquare s = (IsoGridSquare)doneGrids.get(n);
            if(s != null)
                path.appendStep(s.getX(), s.getY(), s.getZ());
        }

        return path;
    }

    public void updateFrameSpeed()
    {
        move.x = getNx() - getLx();
        move.y = getNy() - getLy();
        float del = 1.0F - move.getLength() / 0.08F;
    }

    private void DoZombieInventory()
    {
        if(Rand.Next(12) == 0)
            getInventory().AddItem("Base.Nails");
        if(Rand.Next(12) == 0)
            getInventory().AddItem("Base.Nails");
        if(Rand.Next(12) == 0)
            getInventory().AddItem("Base.Nails");
        if(Rand.Next(12) == 0)
            getInventory().AddItem("Base.Hammer");
        if(Rand.Next(12) == 0)
            getInventory().AddItem("Base.Sheet");
        if(Rand.Next(12) == 0)
            getInventory().AddItem("Base.Sheet");
        if(Rand.Next(12) == 0)
            getInventory().AddItem("Base.Sheet");
        if(Rand.Next(12) == 0)
            getInventory().AddItem("Base.Torch");
        if(Rand.Next(12) == 0)
            getInventory().AddItem("Base.Pen");
        if(Rand.Next(12) == 0)
            getInventory().AddItem("Base.Pencil");
        if(Rand.Next(12) == 0)
            getInventory().AddItem("Base.Lighter");
        if(Rand.Next(12) == 0)
            getInventory().AddItem("Base.Apple");
    }

    private void DoZombieSpeeds()
    {
    	/* 1340 */     if (Rand.Next(2) != 0)
    	/*      */     {
    	/* 1342 */       this.speedMod = 0.6F;
    	/* 1343 */       this.speedMod += Rand.Next(1500) / 10000.0F;
    	/* 1344 */       this.walkVariant += "1";
    	/* 1345 */       this.def.setFrameSpeedPerFrame(0.23F);
    	/* 1346 */       this.def.AnimFrameIncrease *= this.speedMod;
    	/*      */     }
    	/*      */     else
    	/*      */     {
    	/* 1351 */       this.bLunger = true;
    	/* 1352 */       this.speedMod = 0.85F;
    	/*      */ 
    	/* 1354 */       this.walkVariant += "2";
    	/*      */ 
    	/* 1356 */       this.speedMod += Rand.Next(1500) / 10000.0F;
    	/* 1357 */       this.def.setFrameSpeedPerFrame(0.23F);
    	/* 1358 */       this.def.AnimFrameIncrease *= this.speedMod;
    	/*      */     }
    	/*      */ 
    	/* 1361 */     this.PathSpeed = (baseSpeed * this.speedMod);
    	/*      */ 
    	/* 1363 */     this.wanderSpeed = this.PathSpeed;
    }

    public static float baseSpeed = 0.031F;
    static int AllowRepathDelayMax = 120;
    public static int ZombieDeaths = 0;
    public int HurtPlayerTimer = 10;
    public int LastTargetSeenX = -1;
    public int LastTargetSeenY = -1;
    public int LastTargetSeenZ = -1;
    public boolean Ghost = false;
    public float LungeTimer = 0.0F;
    public IsoMovingObject target = null;

    public int TimeSinceSeenFlesh = 100000;
    public int FollowCount = 0;
    public float GhostLife = 0.0F;
    public float wanderSpeed = 0.018F;
    public float predXVel = 0.0F;
    public float predYVel = 0.0F;
    public int ZombieID = 0;

    public boolean bRightie = false;
    private int BonusSpotTime = 0;

    public String SpriteName = "Zombie";
    public Vector2 vectorToTarget = new Vector2();
    public ZombieGroup group = new ZombieGroup();
    int AllowRepathDelay = 0;
    IsoDirections lastDir;
    IsoDirections lastlastDir;
    public boolean KeepItReal = false;
    public boolean Deaf = false;

    public int palette = 0;

    public int AttackAnimTime = 50;
    public static int AttackAnimTimeMax = 50;

    boolean GhostShow = false;

    public IsoMovingObject spottedLast = null;
    OnceEvery spottedPlayer = new OnceEvery(0.7F, true);

    static Vector2 move = new Vector2(0.0F, 0.0F);
    static Vector2 predTest = new Vector2();

    NulledArrayList<IsoGridSquare> doneGrids = new NulledArrayList();
    NulledArrayList<IsoGridSquare> choiceGrids = new NulledArrayList();

    public String walkVariantUse = null;
    public String walkVariant = "ZombieWalk";
    public boolean bLunger = false;


}
