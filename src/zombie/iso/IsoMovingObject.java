// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   IsoMovingObject.java

package zombie.iso;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.Stack;
import se.krka.kahlua.j2se.J2SEPlatform;
import se.krka.kahlua.vm.KahluaTable;
import zombie.CollisionManager;
import zombie.GameTime;
import zombie.Lua.LuaEventManager;
import zombie.Lua.LuaManager;
import zombie.ai.astar.Mover;
import zombie.characters.BodyDamage.BodyDamage;
import zombie.characters.*;
import zombie.core.Collections.NulledArrayList;
import zombie.core.bucket.Bucket;
import zombie.core.bucket.BucketManager;
import zombie.core.properties.PropertyContainer;
import zombie.core.textures.ColorInfo;
import zombie.inventory.types.HandWeapon;
import zombie.iso.SpriteDetails.IsoFlagType;
import zombie.iso.SpriteDetails.IsoObjectType;
import zombie.iso.objects.IsoDoor;
import zombie.iso.objects.IsoWindow;
import zombie.iso.objects.IsoZombieGiblets;
import zombie.iso.objects.interfaces.Thumpable;
import zombie.iso.sprite.IsoSprite;
import zombie.iso.sprite.IsoSpriteInstance;
import zombie.scripting.ScriptManager;
import zombie.scripting.objects.Script;
import zombie.scripting.objects.ScriptCharacter;

// Referenced classes of package zombie.iso:
//            IsoObject, Vector2, IsoCell, IsoGridSquare, 
//            IsoUtils, IsoDirections, IsoCamera

public class IsoMovingObject extends IsoObject
    implements Mover
{

    public static int getIDCount()
    {
        return IDCount;
    }

    public static void setIDCount(int aIDCount)
    {
        IDCount = aIDCount;
    }

    public static Vector2 getTempo()
    {
        return tempo;
    }

    public static void setTempo(Vector2 aTempo)
    {
        tempo = aTempo;
    }

    public float getWeight()
    {
        return weight;
    }

    public float getWeight(float x, float y)
    {
        return weight;
    }

    public void onMouseRightClick(int lx, int ly)
    {
        if(square.getZ() == (int)IsoPlayer.getInstance().getZ() && DistToProper(IsoPlayer.getInstance()) <= 2.0F)
            IsoPlayer.instance.setDragObject(this);
    }

    public String getObjectName()
    {
        return "IsoMovingObject";
    }

    public void onMouseRightReleased()
    {
    }

    public IsoMovingObject(IsoCell cell)
    {
        TimeSinceZombieAttack = 0xf4240;
        noDamage = false;
        collidedE = false;
        collidedN = false;
        CollidedObject = null;
        collidedS = false;
        collidedThisFrame = false;
        collidedW = false;
        CollidedWithDoor = false;
        current = null;
        destroyed = false;
        firstUpdate = true;
        hitDir = new Vector2();
        AllowBehaviours = true;
        impulsex = 0.0F;
        impulsey = 0.0F;
        limpulsex = 0.0F;
        limpulsey = 0.0F;
        hitForce = 0.0F;
        ID = 0;
        last = null;
        PathFindIndex = -1;
        solid = true;
        StateEventDelayTimer = 0.0F;
        thumpTarget = null;
        width = 0.17F;
        bAltCollide = false;
        shootable = true;
        lastTargettedBy = null;
        Collidable = true;
        scriptnx = 0.0F;
        scriptny = 0.0F;
        reqMovement = new Vector2();
        ScriptModule = "none";
        ScriptName = "none";
        ActiveInInstances = new Stack();
        def = null;
        movementLastFrame = new Vector2();
        weight = 1.0F;
        feelersize = 0.5F;
        ID = IDCount++;
        sprite = IsoSprite.CreateSprite(BucketManager.Shared().SpriteManager);
        if(getCell().isSafeToAdd())
            getCell().getObjectList().add(this);
        else
            getCell().getAddList().add(this);
    }

    public IsoMovingObject(IsoCell cell, boolean bObjectListAdd)
    {
        TimeSinceZombieAttack = 0xf4240;
        noDamage = false;
        collidedE = false;
        collidedN = false;
        CollidedObject = null;
        collidedS = false;
        collidedThisFrame = false;
        collidedW = false;
        CollidedWithDoor = false;
        current = null;
        destroyed = false;
        firstUpdate = true;
        hitDir = new Vector2();
        AllowBehaviours = true;
        impulsex = 0.0F;
        impulsey = 0.0F;
        limpulsex = 0.0F;
        limpulsey = 0.0F;
        hitForce = 0.0F;
        ID = 0;
        last = null;
        PathFindIndex = -1;
        solid = true;
        StateEventDelayTimer = 0.0F;
        thumpTarget = null;
        width = 0.17F;
        bAltCollide = false;
        shootable = true;
        lastTargettedBy = null;
        Collidable = true;
        scriptnx = 0.0F;
        scriptny = 0.0F;
        reqMovement = new Vector2();
        ScriptModule = "none";
        ScriptName = "none";
        ActiveInInstances = new Stack();
        def = null;
        movementLastFrame = new Vector2();
        weight = 1.0F;
        feelersize = 0.5F;
        ID = IDCount++;
        sprite = IsoSprite.CreateSprite(BucketManager.Shared().SpriteManager);
        if(bObjectListAdd)
            if(getCell().isSafeToAdd())
                getCell().getObjectList().add(this);
            else
                getCell().getAddList().add(this);
    }

    public IsoMovingObject(IsoCell cell, IsoGridSquare square, IsoSprite spr, boolean bObjectListAdd)
    {
        TimeSinceZombieAttack = 0xf4240;
        noDamage = false;
        collidedE = false;
        collidedN = false;
        CollidedObject = null;
        collidedS = false;
        collidedThisFrame = false;
        collidedW = false;
        CollidedWithDoor = false;
        current = null;
        destroyed = false;
        firstUpdate = true;
        hitDir = new Vector2();
        AllowBehaviours = true;
        impulsex = 0.0F;
        impulsey = 0.0F;
        limpulsex = 0.0F;
        limpulsey = 0.0F;
        hitForce = 0.0F;
        ID = 0;
        last = null;
        PathFindIndex = -1;
        solid = true;
        StateEventDelayTimer = 0.0F;
        thumpTarget = null;
        width = 0.17F;
        bAltCollide = false;
        shootable = true;
        lastTargettedBy = null;
        Collidable = true;
        scriptnx = 0.0F;
        scriptny = 0.0F;
        reqMovement = new Vector2();
        ScriptModule = "none";
        ScriptName = "none";
        ActiveInInstances = new Stack();
        def = null;
        movementLastFrame = new Vector2();
        weight = 1.0F;
        feelersize = 0.5F;
        ID = IDCount++;
        sprite = spr;
        if(bObjectListAdd)
            if(getCell().isSafeToAdd())
                getCell().getObjectList().add(this);
            else
                getCell().getAddList().add(this);
    }

    public IsoMovingObject()
    {
        TimeSinceZombieAttack = 0xf4240;
        noDamage = false;
        collidedE = false;
        collidedN = false;
        CollidedObject = null;
        collidedS = false;
        collidedThisFrame = false;
        collidedW = false;
        CollidedWithDoor = false;
        current = null;
        destroyed = false;
        firstUpdate = true;
        hitDir = new Vector2();
        AllowBehaviours = true;
        impulsex = 0.0F;
        impulsey = 0.0F;
        limpulsex = 0.0F;
        limpulsey = 0.0F;
        hitForce = 0.0F;
        ID = 0;
        last = null;
        PathFindIndex = -1;
        solid = true;
        StateEventDelayTimer = 0.0F;
        thumpTarget = null;
        width = 0.17F;
        bAltCollide = false;
        shootable = true;
        lastTargettedBy = null;
        Collidable = true;
        scriptnx = 0.0F;
        scriptny = 0.0F;
        reqMovement = new Vector2();
        ScriptModule = "none";
        ScriptName = "none";
        ActiveInInstances = new Stack();
        def = null;
        movementLastFrame = new Vector2();
        weight = 1.0F;
        feelersize = 0.5F;
        ID = IDCount++;
        getCell().getAddList().add(this);
    }

    public void collideCharacter()
    {
    }

    public void collideWith(IsoObject obj)
    {
        if((this instanceof IsoGameCharacter) && (obj instanceof IsoGameCharacter))
            LuaEventManager.TriggerEvent("OnCharacterCollide", (IsoGameCharacter)this, (IsoGameCharacter)obj);
        else
            LuaEventManager.TriggerEvent("OnObjectCollide", this, obj);
    }

    public void doStairs()
    {
        if(current == null)
            return;
        if((this instanceof IsoGameCharacter) && (last.Has(IsoObjectType.stairsTN) || last.Has(IsoObjectType.stairsTW)))
            this.z = Math.round(this.z);
        float z = this.z;
        if(current.Has(IsoObjectType.stairsTN))
            z = (float)current.getZ() + GameTime.getInstance().Lerp(0.6666F, 1.0F, 1.0F - (y - (float)current.getY()));
        else
        if(current.Has(IsoObjectType.stairsTW))
            z = (float)current.getZ() + GameTime.getInstance().Lerp(0.6666F, 1.0F, 1.0F - (x - (float)current.getX()));
        else
        if(current.Has(IsoObjectType.stairsMN))
            z = (float)current.getZ() + GameTime.getInstance().Lerp(0.3333F, 0.6666F, 1.0F - (y - (float)current.getY()));
        else
        if(current.Has(IsoObjectType.stairsMW))
            z = (float)current.getZ() + GameTime.getInstance().Lerp(0.3333F, 0.6666F, 1.0F - (x - (float)current.getX()));
        else
        if(current.Has(IsoObjectType.stairsBN))
            z = (float)current.getZ() + GameTime.getInstance().Lerp(0.01F, 0.3333F, 1.0F - (y - (float)current.getY()));
        else
        if(current.Has(IsoObjectType.stairsBW))
            z = (float)current.getZ() + GameTime.getInstance().Lerp(0.01F, 0.3333F, 1.0F - (x - (float)current.getX()));
        if(Math.abs(z - this.z) < 0.95F)
            this.z = z;
    }

    public int getID()
    {
        return ID;
    }

    public int getPathFindIndex()
    {
        return PathFindIndex;
    }

    public float getScreenX()
    {
        return IsoUtils.XToScreen(x, y, z, 0);
    }

    public float getScreenY()
    {
        return IsoUtils.YToScreen(x, y, z, 0);
    }

    public float getSeeValue()
    {
        float del = (current.getLightInfo().r + current.getLightInfo().g + current.getLightInfo().b) / 3F;
        if(del < 0.5F)
            del = 1.0F;
        return del;
    }

    public Thumpable getThumpTarget()
    {
        return thumpTarget;
    }

    public Vector2 getVectorFromDirection()
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
        moveForwardVec.normalize();
        return moveForwardVec;
    }

    public float getX()
    {
        return x;
    }

    public float getY()
    {
        return y;
    }

    public float getZ()
    {
        return z;
    }

    public IsoGridSquare getSquare()
    {
        return current;
    }

    public void Hit(HandWeapon handweapon, IsoGameCharacter isogamecharacter, float f, boolean flag, float f1)
    {
    }

    public void Move(Vector2 dir)
    {
        int n;
        if(GameTime.instance.getMultiplier() > 3F)
            n = 0;
        nx += dir.x * GameTime.instance.getMultiplier();
        ny += dir.y * GameTime.instance.getMultiplier();
        reqMovement.x = dir.x;
        reqMovement.y = dir.y;
        int fds;
        if(reqMovement.getLength() > 0.1F)
            fds = 0;
    }

    public boolean isCharacter()
    {
        return this instanceof IsoGameCharacter;
    }

    public float DistTo(int x, int y)
    {
        return IsoUtils.DistanceManhatten(x, y, this.x, this.y);
    }

    public float DistTo(IsoMovingObject other)
    {
        return IsoUtils.DistanceManhatten(x, y, other.x, other.y);
    }

    public float DistToProper(IsoMovingObject other)
    {
        return IsoUtils.DistanceTo(x, y, other.x, other.y);
    }

    public boolean getAllowBehaviours()
    {
        if(this instanceof IsoZombie)
            return false;
        if(ScriptName.equals("none"))
            return AllowBehaviours;
        ScriptCharacter chr = ScriptManager.instance.getCharacter((new StringBuilder()).append(ScriptModule).append(".").append(ScriptName).toString());
        if(chr == null)
            return AllowBehaviours;
        else
            return AllowBehaviours && chr.AllowBehaviours();
    }

    public void load(ByteBuffer input)
        throws IOException
    {
        offsetX = input.getFloat();
        offsetY = input.getFloat();
        x = lx = nx = scriptnx = input.getFloat();
        y = ly = ny = scriptny = input.getFloat();
        z = lz = input.getFloat();
        dir = IsoDirections.fromIndex(input.getInt());
        if(input.get() != 0)
        {
            if(table == null)
                table = LuaManager.platform.newTable();
            table.load(input);
        }
    }

    public void save(ByteBuffer output)
        throws IOException
    {
        output.put(((byte)(Serialize() ? 1 : 0)));
        output.putInt(getObjectName().hashCode());
        output.putFloat(offsetX);
        output.putFloat(offsetY);
        output.putFloat(x);
        output.putFloat(y);
        output.putFloat(z);
        output.putInt(dir.index());
        if(table != null)
        {
            output.put((byte)1);
            table.save(output);
        } else
        {
            output.put((byte)0);
        }
    }

    public float getGlobalMovementMod()
    {
        if(current != null)
        {
            IsoGridSquare feeler = getFeelerTile(feelersize);
            if(feeler != null && feeler != current)
            {
                IsoWindow w = current.getWindowTo(feeler);
                if(w != null)
                    return !(this instanceof IsoZombie) ? 0.3F : 0.15F;
                if(current.getProperties().Is(IsoFlagType.HoppableW) && feeler.getX() < current.getX() || current.getProperties().Is(IsoFlagType.HoppableN) && feeler.getY() < current.getY())
                    return !(this instanceof IsoZombie) ? 0.4F : 0.2F;
                if(feeler.getProperties().Is(IsoFlagType.HoppableW) && feeler.getX() > current.getX() || feeler.getProperties().Is(IsoFlagType.HoppableN) && feeler.getY() > current.getY())
                    return !(this instanceof IsoZombie) ? 0.4F : 0.2F;
            }
        }
        return 1.0F;
    }

    public void postupdate()
    {
        int n;
        if(IsoCamera.CamCharacter == this)
            n = 0;
        if(lastTargettedBy != null && (lastTargettedBy.getHealth() <= 0.0F || lastTargettedBy.getBodyDamage().getHealth() <= 0.0F))
            lastTargettedBy = null;
        if(lastTargettedBy != null && TimeSinceZombieAttack > 120)
            lastTargettedBy = null;
        TimeSinceZombieAttack++;
        if(this instanceof IsoPlayer)
        {
            n = 0;
            ((IsoPlayer)this).setLastCollidedW(collidedW);
            ((IsoPlayer)this).setLastCollidedN(collidedN);
            IsoPlayer p = (IsoPlayer)this;
        }
        if(destroyed)
            return;
        if(!getAllowBehaviours() && (this instanceof IsoSurvivor))
        {
            nx = scriptnx;
            ny = scriptny;
        }
        collidedThisFrame = false;
        collidedN = false;
        collidedS = false;
        collidedW = false;
        collidedE = false;
        CollidedWithDoor = false;
        last = current;
        CollidedObject = null;
        nx += impulsex;
        ny += impulsey;
        impulsex = 0.0F;
        impulsey = 0.0F;
        float onx = nx;
        float ony = ny;
        float len = 0.0F;
        boolean bDidCollide = false;
        if(Collidable)
        {
            if(bAltCollide)
                DoCollide(2);
            else
                DoCollide(1);
            if(collidedN || collidedS)
            {
                ny = ly;
                DoCollideNorS();
            }
            if(collidedW || collidedE)
            {
                nx = lx;
                DoCollideWorE();
            }
            if(bAltCollide)
                DoCollide(1);
            else
                DoCollide(2);
            bAltCollide = !bAltCollide;
            if(collidedN || collidedS)
            {
                ny = ly;
                DoCollideNorS();
                bDidCollide = true;
            }
            if(collidedW || collidedE)
            {
                nx = lx;
                DoCollideWorE();
                bDidCollide = true;
            }
            len = Math.abs(nx - lx) + Math.abs(ny - ly);
            float lnx = nx;
            float lny = ny;
            nx = onx;
            ny = ony;
            if(Collidable && bDidCollide)
            {
                if(bAltCollide)
                    DoCollide(2);
                else
                    DoCollide(1);
                if(collidedN || collidedS)
                {
                    ny = ly;
                    DoCollideNorS();
                }
                if(collidedW || collidedE)
                {
                    nx = lx;
                    DoCollideWorE();
                }
                if(bAltCollide)
                    DoCollide(1);
                else
                    DoCollide(2);
                if(collidedN || collidedS)
                {
                    ny = ly;
                    DoCollideNorS();
                    bDidCollide = true;
                }
                if(collidedW || collidedE)
                {
                    nx = lx;
                    DoCollideWorE();
                    bDidCollide = true;
                }
                if(Math.abs(nx - lx) + Math.abs(ny - ly) < len)
                {
                    nx = lnx;
                    ny = lny;
                }
            }
        }
        if(collidedThisFrame)
            current = last;
        float dx = nx - x;
        float dy = ny - y;
        dx *= getGlobalMovementMod();
        dy *= getGlobalMovementMod();
        x += dx;
        y += dy;
        doStairs();
        current = getCell().getGridSquare((int)x, (int)y, (int)z);
        if(last != null)
        {
            last.getMovingObjects().remove(this);
            if(last.getMovingObjects().isEmpty())
                last.setHasZombies(false);
        }
        if(current != null)
        {
            current.getMovingObjects().add(this);
            if(this instanceof IsoZombie)
                current.setHasZombies(true);
        }
        ensureOnTile();
        square = current;
        scriptnx = nx;
        scriptny = ny;
        firstUpdate = false;
    }

    public void ensureOnTile()
    {
        if(current == null)
        {
            boolean bDo = true;
            int xr = 0;
            if(last != null && (last.Has(IsoObjectType.stairsTN) || last.Has(IsoObjectType.stairsTW)))
            {
                current = getCell().getGridSquare((int)x, (int)y, (int)z + 1);
                bDo = false;
            }
            do
            {
                if(current != null && (!bDo || current.isFree(false)))
                    break;
                xr++;
                current = getCell().getGridSquare((int)x - xr, (int)y, (int)z);
                if(current == null || !current.isFree(false))
                    current = getCell().getGridSquare((int)x, (int)y - xr, (int)z);
                if(current == null || !current.isFree(false))
                    current = getCell().getGridSquare((int)x - xr, (int)y - xr, (int)z);
                if(current == null || !current.isFree(false))
                    current = getCell().getGridSquare((int)x + xr, (int)y, (int)z);
                if(current == null || !current.isFree(false))
                    current = getCell().getGridSquare((int)x, (int)y + xr, (int)z);
                if(current == null || !current.isFree(false))
                    current = getCell().getGridSquare((int)x + xr, (int)y + xr, (int)z);
                if(xr > 40)
                {
                    x = nx = scriptnx = 0.0F;
                    y = ny = scriptny = 0.0F;
                    z = lz = 0.0F;
                }
            } while(true);
            if(bDo)
            {
                x = nx = scriptnx = (float)current.getX() + 0.5F;
                y = ny = scriptny = (float)current.getY() + 0.5F;
            }
            z = current.getZ();
        }
    }

    public void preupdate()
    {
        nx = x;
        ny = y;
    }

    public void renderlast()
    {
    }

    public void spotted(IsoMovingObject isomovingobject)
    {
    }

    public void update()
    {
        if(def == null)
            def = new IsoSpriteInstance(sprite);
        movementLastFrame.x = x - lx;
        movementLastFrame.y = y - ly;
        lx = x;
        ly = y;
        lz = z;
        square = current;
        if(sprite != null)
            sprite.update(def);
        StateEventDelayTimer--;
    }

    private void Collided()
    {
        collidedThisFrame = true;
    }

    public int compareToY(IsoMovingObject other)
    {
        if(sprite.lsy > other.sprite.lsy)
            return 1;
        return sprite.lsy >= other.sprite.lsy ? 0 : -1;
    }

    public void seperate()
    {
        if(current == null)
            return;
        if(!solid)
            return;
        if(z < 0.0F)
            z = 0.0F;
        for(int x = -1; x <= 1; x++)
        {
label0:
            for(int y = -1; y <= 1; y++)
            {
                IsoGridSquare sq = getCell().getGridSquare(current.getX() + x, current.getY() + y, (int)z);
                if(sq == null)
                    continue;
                Iterator it = sq.getMovingObjects().iterator();
                do
                {
                    IsoMovingObject obj;
                    float twidth;
                    do
                    {
                        do
                        {
                            if(it == null || !it.hasNext())
                                continue label0;
                            obj = (IsoMovingObject)it.next();
                        } while((obj instanceof IsoZombieGiblets) || obj == this || !obj.solid);
                        twidth = width + obj.width;
                        if(tempo == null)
                        {
                            tempo = new Vector2(nx, ny);
                        } else
                        {
                            tempo.x = nx;
                            tempo.y = ny;
                        }
                        tempo.x -= obj.nx;
                        tempo.y -= obj.ny;
                    } while(Math.abs(z - obj.z) > 0.3F);
                    if((this instanceof IsoGameCharacter) && (obj instanceof IsoGameCharacter))
                    {
                        float len = tempo.getLength();
                        if(len < twidth && (DistTo(IsoCamera.CamCharacter) < 60F && len < twidth))
                        {
                            tempo.setLength((len - twidth) / 8F);
                            nx -= tempo.x;
                            ny -= tempo.y;
                            obj.nx += tempo.x;
                            obj.ny += tempo.y;
                            collideWith(obj);
                            obj.collideWith(this);
                        }
                    } else
                    {
                        float len = tempo.getLength();
                        if(len < twidth * 1.2F)
                            CollisionManager.instance.AddContact(this, obj);
                    }
                } while(true);
            }

        }

    }

    private boolean DoCollide(int favour)
    {
        current = getCell().getGridSquare((int)nx, (int)ny, (int)z);
        int roundZ = (int)z;
        if(current != null)
        {
            if(!current.TreatAsSolidFloor())
                current = getCell().getGridSquare((int)nx, (int)ny, (int)z);
            if(current == null)
                return false;
            current = getCell().getGridSquare((int)nx, (int)ny, (int)z);
        }
        if(current != last && last != null && current != null)
        {
            int fdsf;
            if(this == IsoCamera.CamCharacter)
                fdsf = 0;
            int dx = current.getX() - last.getX();
            int dy = current.getY() - last.getY();
            int dz = current.getZ() - last.getZ();
            boolean keepy = false;
            boolean keepx = false;
            if(dx != 0 && dy == 0)
                keepy = true;
            boolean bCollide = false;
            if(last.testCollideAdjacent(this, dx, dy, dz) || current == null)
                bCollide = true;
            if(bCollide)
            {
                if(last.getX() < current.getX())
                    collidedE = true;
                if(last.getX() > current.getX())
                    collidedW = true;
                if(last.getY() < current.getY())
                    collidedS = true;
                if(last.getY() > current.getY())
                    collidedN = true;
                if(favour == 2)
                {
                    if((collidedS || collidedN) && (collidedE || collidedW))
                    {
                        collidedS = false;
                        collidedN = false;
                    }
                } else
                if(favour == 1 && (collidedS || collidedN) && (collidedE || collidedW))
                {
                    collidedW = false;
                    collidedE = false;
                }
                current = last;
                Collided();
                return true;
            }
        } else
        if(nx != lx || ny != ly)
            if(current == null)
            {
                nx = lx;
                ny = ly;
                current = last;
                Collided();
            } else
            {
                IsoGridSquare feeler = getFeelerTile(feelersize);
                if((this instanceof IsoPlayer) && feeler != current && ((IsoPlayer)this).TargetSpeed > 0.05F && !((IsoPlayer)this).JustMoved && feeler != null)
                {
                    IsoDoor d = current.getDoorTo(feeler);
                    if(d != null)
                    {
                        IsoGridSquare feeler2 = getFeelerTile(feelersize * 1.5F);
                        if(!d.open && feeler2 == feeler)
                            d.ToggleDoor((IsoPlayer)this);
                    }
                }
                if((this instanceof IsoGameCharacter) && ((IsoGameCharacter)this).isClimbing())
                    feeler = current;
                if(feeler != null && feeler != current && current != null)
                {
                    int fdsf;
                    if(this == IsoCamera.CamCharacter)
                        fdsf = 0;
                    if(current.testCollideAdjacent(this, feeler.getX() - current.getX(), feeler.getY() - current.getY(), feeler.getZ() - current.getZ()))
                    {
                        if(last != null)
                        {
                            if(current.getX() < feeler.getX())
                                collidedE = true;
                            if(current.getX() > feeler.getX())
                                collidedW = true;
                            if(current.getY() < feeler.getY())
                                collidedS = true;
                            if(current.getY() > feeler.getY())
                                collidedN = true;
                            if(favour == 2 && (collidedS || collidedN) && (collidedE || collidedW))
                            {
                                collidedS = false;
                                collidedN = false;
                            }
                            if(favour == 1 && (collidedS || collidedN) && (collidedE || collidedW))
                            {
                                collidedW = false;
                                collidedE = false;
                            }
                        }
                        Collided();
                        return true;
                    }
                }
            }
        return false;
    }

    public IsoGridSquare getFeelerTile(float dist)
    {
        Vector2 vec = tempo;
        vec.x = nx - lx;
        vec.y = ny - ly;
        if(vec.x < 0.0F)
            vec.x = -dist;
        if(vec.y < 0.0F)
            vec.y = -dist;
        if(vec.x > 0.0F)
            vec.x = dist;
        if(vec.y > 0.0F)
            vec.y = dist;
        return getCell().getGridSquare((int)(x + vec.x), (int)(y + vec.y), (int)z);
    }

    public void DoCollideNorS()
    {
        ny = ly;
    }

    public void DoCollideWorE()
    {
        nx = lx;
    }

    public int getTimeSinceZombieAttack()
    {
        return TimeSinceZombieAttack;
    }

    public void setTimeSinceZombieAttack(int TimeSinceZombieAttack)
    {
        this.TimeSinceZombieAttack = TimeSinceZombieAttack;
    }

    public boolean isCollidedE()
    {
        return collidedE;
    }

    public void setCollidedE(boolean collidedE)
    {
        this.collidedE = collidedE;
    }

    public boolean isCollidedN()
    {
        return collidedN;
    }

    public void setCollidedN(boolean collidedN)
    {
        this.collidedN = collidedN;
    }

    public IsoObject getCollidedObject()
    {
        return CollidedObject;
    }

    public void setCollidedObject(IsoObject CollidedObject)
    {
        this.CollidedObject = CollidedObject;
    }

    public boolean isCollidedS()
    {
        return collidedS;
    }

    public void setCollidedS(boolean collidedS)
    {
        this.collidedS = collidedS;
    }

    public boolean isCollidedThisFrame()
    {
        return collidedThisFrame;
    }

    public void setCollidedThisFrame(boolean collidedThisFrame)
    {
        this.collidedThisFrame = collidedThisFrame;
    }

    public boolean isCollidedW()
    {
        return collidedW;
    }

    public void setCollidedW(boolean collidedW)
    {
        this.collidedW = collidedW;
    }

    public boolean isCollidedWithDoor()
    {
        return CollidedWithDoor;
    }

    public void setCollidedWithDoor(boolean CollidedWithDoor)
    {
        this.CollidedWithDoor = CollidedWithDoor;
    }

    public IsoGridSquare getCurrentSquare()
    {
        return current;
    }

    public void setCurrent(IsoGridSquare current)
    {
        this.current = current;
    }

    public boolean isDestroyed()
    {
        return destroyed;
    }

    public void setDestroyed(boolean destroyed)
    {
        this.destroyed = destroyed;
    }

    public boolean isFirstUpdate()
    {
        return firstUpdate;
    }

    public void setFirstUpdate(boolean firstUpdate)
    {
        this.firstUpdate = firstUpdate;
    }

    public Vector2 getHitDir()
    {
        return hitDir;
    }

    public void setHitDir(Vector2 hitDir)
    {
        this.hitDir = hitDir;
    }

    public boolean isAllowBehaviours()
    {
        return AllowBehaviours;
    }

    public void setAllowBehaviours(boolean AllowBehaviours)
    {
        this.AllowBehaviours = AllowBehaviours;
    }

    public float getImpulsex()
    {
        return impulsex;
    }

    public void setImpulsex(float impulsex)
    {
        this.impulsex = impulsex;
    }

    public float getImpulsey()
    {
        return impulsey;
    }

    public void setImpulsey(float impulsey)
    {
        this.impulsey = impulsey;
    }

    public float getLimpulsex()
    {
        return limpulsex;
    }

    public void setLimpulsex(float limpulsex)
    {
        this.limpulsex = limpulsex;
    }

    public float getLimpulsey()
    {
        return limpulsey;
    }

    public void setLimpulsey(float limpulsey)
    {
        this.limpulsey = limpulsey;
    }

    public float getHitForce()
    {
        return hitForce;
    }

    public void setHitForce(float hitForce)
    {
        this.hitForce = hitForce;
    }

    public float getHitFromAngle()
    {
        return hitFromAngle;
    }

    public void setHitFromAngle(float hitFromAngle)
    {
        this.hitFromAngle = hitFromAngle;
    }

    public void setID(int ID)
    {
        this.ID = ID;
    }

    public IsoGridSquare getLastSquare()
    {
        return last;
    }

    public void setLast(IsoGridSquare last)
    {
        this.last = last;
    }

    public float getLx()
    {
        return lx;
    }

    public void setLx(float lx)
    {
        this.lx = lx;
    }

    public float getLy()
    {
        return ly;
    }

    public void setLy(float ly)
    {
        this.ly = ly;
    }

    public float getLz()
    {
        return lz;
    }

    public void setLz(float lz)
    {
        this.lz = lz;
    }

    public float getNx()
    {
        return nx;
    }

    public void setNx(float nx)
    {
        this.nx = nx;
    }

    public float getNy()
    {
        return ny;
    }

    public void setNy(float ny)
    {
        this.ny = ny;
    }

    public void setPathFindIndex(int PathFindIndex)
    {
        this.PathFindIndex = PathFindIndex;
    }

    public boolean isSolid()
    {
        return solid;
    }

    public void setSolid(boolean solid)
    {
        this.solid = solid;
    }

    public float getStateEventDelayTimer()
    {
        return StateEventDelayTimer;
    }

    public void setStateEventDelayTimer(float StateEventDelayTimer)
    {
        this.StateEventDelayTimer = StateEventDelayTimer;
    }

    public void setThumpTarget(Thumpable thumpTarget)
    {
        this.thumpTarget = thumpTarget;
    }

    public float getWidth()
    {
        return width;
    }

    public void setWidth(float width)
    {
        this.width = width;
    }

    public void setX(float x)
    {
        this.x = x;
        nx = x;
        scriptnx = x;
    }

    public void setY(float y)
    {
        this.y = y;
        ny = y;
        scriptny = y;
    }

    public void setZ(float z)
    {
        this.z = z;
        lz = z;
    }

    public boolean isbAltCollide()
    {
        return bAltCollide;
    }

    public void setbAltCollide(boolean bAltCollide)
    {
        this.bAltCollide = bAltCollide;
    }

    public boolean isShootable()
    {
        return shootable;
    }

    public void setShootable(boolean shootable)
    {
        this.shootable = shootable;
    }

    public IsoZombie getLastTargettedBy()
    {
        return lastTargettedBy;
    }

    public void setLastTargettedBy(IsoZombie lastTargettedBy)
    {
        this.lastTargettedBy = lastTargettedBy;
    }

    public boolean isCollidable()
    {
        return Collidable;
    }

    public void setCollidable(boolean Collidable)
    {
        this.Collidable = Collidable;
    }

    public float getScriptnx()
    {
        return scriptnx;
    }

    public void setScriptnx(float scriptnx)
    {
        this.scriptnx = scriptnx;
    }

    public float getScriptny()
    {
        return scriptny;
    }

    public void setScriptny(float scriptny)
    {
        this.scriptny = scriptny;
    }

    public String getScriptModule()
    {
        return ScriptModule;
    }

    public void setScriptModule(String ScriptModule)
    {
        this.ScriptModule = ScriptModule;
    }

    public String getScriptName()
    {
        return ScriptName;
    }

    public void setScriptName(String ScriptName)
    {
        this.ScriptName = ScriptName;
    }

    public Stack getActiveInInstances()
    {
        return ActiveInInstances;
    }

    public void setActiveInInstances(Stack ActiveInInstances)
    {
        this.ActiveInInstances = ActiveInInstances;
    }

    public Vector2 getMovementLastFrame()
    {
        return movementLastFrame;
    }

    public void setMovementLastFrame(Vector2 movementLastFrame)
    {
        this.movementLastFrame = movementLastFrame;
    }

    public void setWeight(float weight)
    {
        this.weight = weight;
    }

    public float getFeelersize()
    {
        return feelersize;
    }

    public void setFeelersize(float feelersize)
    {
        this.feelersize = feelersize;
    }

    private static int IDCount = 0;
    private int TimeSinceZombieAttack;
    public boolean noDamage;
    private static Vector2 moveForwardVec = new Vector2();
    private boolean collidedE;
    private boolean collidedN;
    private IsoObject CollidedObject;
    private boolean collidedS;
    private boolean collidedThisFrame;
    private boolean collidedW;
    private boolean CollidedWithDoor;
    protected IsoGridSquare current;
    private boolean destroyed;
    private boolean firstUpdate;
    protected Vector2 hitDir;
    private boolean AllowBehaviours;
    private float impulsex;
    private float impulsey;
    private float limpulsex;
    private float limpulsey;
    private float hitForce;
    private float hitFromAngle;
    private int ID;
    public IsoGridSquare last;
    protected float lx;
    protected float ly;
    protected float lz;
    protected float nx;
    protected float ny;
    private int PathFindIndex;
    protected boolean solid;
    private float StateEventDelayTimer;
    private Thumpable thumpTarget;
    protected float width;
    public float x;
    public float y;
    public float z;
    private boolean bAltCollide;
    protected boolean shootable;
    private IsoZombie lastTargettedBy;
    protected boolean Collidable;
    protected float scriptnx;
    protected float scriptny;
    public Vector2 reqMovement;
    protected String ScriptModule;
    protected String ScriptName;
    private Stack ActiveInInstances;
    public IsoSpriteInstance def;
    protected Vector2 movementLastFrame;
    protected float weight;
    private static Vector2 tempo = new Vector2();
    private float feelersize;

}
