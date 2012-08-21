// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   IsoGridSquare.java

package zombie.iso;

import java.awt.event.ActionEvent;
import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import se.krka.kahlua.j2se.J2SEPlatform;
import se.krka.kahlua.vm.KahluaTable;
import zombie.*;
import zombie.Lua.LuaManager;
import zombie.ZomboidBitFlag;
import zombie.ai.astar.AStarPathMap;
import zombie.characters.*;
import zombie.characters.Moodles.MoodleType;
import zombie.characters.Moodles.Moodles;
import zombie.core.Collections.NulledArrayList;
import zombie.core.Color;
import zombie.core.Core;
import zombie.core.properties.PropertyContainer;
import zombie.core.textures.ColorInfo;
import zombie.core.textures.Texture;
import zombie.core.utils.OnceEvery;
import zombie.inventory.InventoryItem;
import zombie.inventory.InventoryItemFactory;
import zombie.iso.SpriteDetails.IsoFlagType;
import zombie.iso.SpriteDetails.IsoObjectType;
import zombie.iso.areas.IsoBuilding;
import zombie.iso.areas.IsoRoom;
import zombie.iso.objects.IsoBarricade;
import zombie.iso.objects.IsoCurtain;
import zombie.iso.objects.IsoDeadBody;
import zombie.iso.objects.IsoDoor;
import zombie.iso.objects.IsoFireManager;
import zombie.iso.objects.IsoWindow;
import zombie.iso.objects.IsoWorldInventoryObject;
import zombie.iso.sprite.IsoSprite;
import zombie.iso.sprite.IsoSpriteManager;
import zombie.ui.UIManager;

// Referenced classes of package zombie.iso:
//            IsoObject, IsoMovingObject, IsoLightSource, BlockInfo, 
//            Vector2, IsoCell, SliceY, IsoUtils, 
//            IsoCamera, IsoWorld, IsoDirections, LosUtil, 
//            IsoObjectPicker

public class IsoGridSquare
{
    public class Authenticator extends AbstractAction
    {

        public void actionPerformed(ActionEvent actionevent)
        {
        }

       

        public Authenticator()
        {
          
        }
    }


    public static boolean DoChecksumCheck(String str, String expected)
    {
        String checksum = "";
        try
        {
            checksum = IsoObject.getMD5Checksum(str);
            if(!checksum.equals(expected))
                return false;
        }
        catch(Exception ex)
        {
            checksum = "";
            try
            {
                checksum = IsoObject.getMD5Checksum((new StringBuilder()).append("D:/Dropbox/Zomboid/zombie/build/classes/").append(str).toString());
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

    private static boolean validateUser(String usr, String pwd)
        throws MalformedURLException, IOException
    {
        URL yahoo = new URL((new StringBuilder()).append("http://www.projectzomboid.com/scripts/auth.php?username=").append(usr).append("&password=").append(pwd).toString());
        URLConnection yc = yahoo.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
        String inputLine;
        while((inputLine = in.readLine()) != null) 
            if(inputLine.contains("success"))
                return true;
        return false;
    }

    public float DistTo(int x, int y)
    {
        return IsoUtils.DistanceManhatten((float)x + 0.5F, (float)y + 0.5F, this.x, this.y);
    }

    public float DistTo(IsoGridSquare sq)
    {
        return IsoUtils.DistanceManhatten((float)x + 0.5F, (float)y + 0.5F, (float)sq.x + 0.5F, (float)sq.y + 0.5F);
    }

    public float DistTo(IsoMovingObject other)
    {
        return IsoUtils.DistanceManhatten((float)x + 0.5F, (float)y + 0.5F, other.getX(), other.getY());
    }

    public float DistToProper(IsoMovingObject other)
    {
        return IsoUtils.DistanceTo((float)x + 0.5F, (float)y + 0.5F, other.getX(), other.getY());
    }

    public static boolean auth(String username, char pw[])
    {
        if(username.length() > 64)
            return false;
        String password = pw.toString();
        if(password.length() > 64)
            return false;
        try
        {
            return validateUser(username, password);
        }
        catch(MalformedURLException ex)
        {
            Logger.getLogger(IsoGridSquare.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch(IOException ex)
        {
            Logger.getLogger(IsoGridSquare.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public int DoWallLightingN(IsoObject obj, int stenciled, int ax, int ay)
    {
        if((float)z != IsoCamera.CamCharacter.z)
            CircleStencil = false;
        if(room == IsoCamera.CamCharacter.current.getRoom() && IsoCamera.CamCharacter.current.getRoom() != null && room != null)
            CircleStencil = false;
        if(obj.sprite.getType() == IsoObjectType.doorFrN || obj.sprite.getType() == IsoObjectType.doorW || obj.sprite.getType() == IsoObjectType.doorN || (obj instanceof IsoWindow))
        {
            CircleStencil = false;
            stenciled += 5;
        }
        colu = getCell().vertLights[x][y][z][0];
        coll = getCell().vertLights[x][y][z][1];
        colu2 = colu;
        coll2 = coll;
        if(z < IsoCell.MaxHeight - 1)
        {
            colu2 = getCell().vertLights[x][y][z + 1][0];
            coll2 = getCell().vertLights[x][y][z + 1][1];
        }
        IndieGL.End();
        stenciled++;
        if(CircleStencil)
        {
            IndieGL.glEnable(2960);
            IndieGL.glEnable(3008);
            IndieGL.glAlphaFunc(516, 0.1F);
            IndieGL.glStencilFunc(517, 128, 128);
            IndieGL.glStencilOp(7680, 7680, 7680);
            obj.render(x, y, z, defColorInfo, false);
            IndieGL.glStencilFunc(519, stenciled, 255);
            obj.alpha = 0.21F;
        } else
        {
            IndieGL.glEnable(3008);
            IndieGL.glAlphaFunc(516, 0.1F);
            IndieGL.glStencilFunc(519, stenciled, 127);
        }
        IndieGL.glStencilOp(7680, 7680, 7681);
        obj.render(x, y, z, defColorInfo, true);
        obj.alpha = 1.0F;
        IndieGL.End();
        IndieGL.glColorMask(true, true, true, true);
        IndieGL.glBlendFunc(0, 768);
        IndieGL.glAlphaFunc(516, 0.0F);
        if(CircleStencil)
            IndieGL.glStencilFunc(514, stenciled, 255);
        else
            IndieGL.glStencilFunc(514, stenciled, 127);
        IndieGL.glStencilOp(7680, 7680, 7680);
        if(texWhite == null)
            texWhite = Texture.getSharedTexture("media/ui/white.png");
        Texture tex = texWhite;
        float offX = 0.0F;
        float offY = 0.0F;
        float offZ = 0.0F;
        float sx = IsoUtils.XToScreen((float)x + offX, (float)y + offY, (float)z + offZ, 0);
        float sy = IsoUtils.YToScreen((float)x + offX, (float)y + offY, (float)z + offZ, 0);
        sx = (int)sx;
        sy = (int)sy;
        sx -= (int)IsoCamera.getOffX();
        sy -= (int)IsoCamera.getOffY();
        sy += 128F;
        sy += 128F;
        sy += 128F;
        if(tex != null)
            tex.renderwalln((int)sx, (int)sy, 64, 32, colu, coll, colu2, coll2);
        IndieGL.End();
        IndieGL.glStencilFunc(519, 1, 255);
        IndieGL.glStencilOp(7680, 7680, 7680);
        IndieGL.glBlendFunc(770, 771);
        return stenciled;
    }

    public int DoWallLightingNW(int ax, int ay, IsoObject obj, int stenciled, boolean CircleStencil)
    {
        if((float)z != IsoCamera.CamCharacter.z)
            CircleStencil = false;
        if(room == IsoCamera.CamCharacter.current.getRoom() && IsoCamera.CamCharacter.current.getRoom() != null && room != null)
            CircleStencil = false;
        colu = getCell().getVertLights()[x][y][z][0];
        coll = getCell().getVertLights()[x][y][z][3];
        colr = getCell().getVertLights()[x][y][z][1];
        colu2 = colu;
        coll2 = coll;
        colr2 = colr;
        if(z < IsoCell.MaxHeight - 1)
        {
            colu2 = getCell().getVertLights()[x][y][z + 1][0];
            coll2 = getCell().getVertLights()[x][y][z + 1][3];
            colr2 = getCell().getVertLights()[x][y][z + 1][1];
        }
        IndieGL.End();
        stenciled++;
        if(CircleStencil)
        {
            IndieGL.glEnable(2960);
            IndieGL.glEnable(3008);
            IndieGL.glAlphaFunc(516, 0.1F);
            IndieGL.glStencilFunc(517, 128, 128);
            IndieGL.glStencilOp(7680, 7680, 7680);
            obj.render(x, y, z, defColorInfo, false);
            obj.alpha = 0.21F;
            IndieGL.glStencilFunc(519, stenciled, 255);
        } else
        {
            IndieGL.glEnable(3008);
            IndieGL.glAlphaFunc(516, 0.1F);
            IndieGL.glStencilFunc(519, stenciled, 127);
        }
        IndieGL.glStencilOp(7680, 7680, 7681);
        if(CircleStencil)
        {
            float val = GameTime.getInstance().getAmbient();
            float rmod = GameTime.getInstance().Lerp(1.0F, 0.1F, GameTime.getInstance().getNightTint());
            float gmod = GameTime.getInstance().Lerp(1.0F, 0.2F, GameTime.getInstance().getNightTint());
            float bmod = GameTime.getInstance().Lerp(1.0F, 0.45F, GameTime.getInstance().getNightTint());
            defColorInfo.r = val * rmod;
            defColorInfo.g = val * gmod;
            defColorInfo.b = val * bmod;
            obj.render(x, y, z, defColorInfo, true);
            defColorInfo.r = 1.0F;
            defColorInfo.g = 1.0F;
            defColorInfo.b = 1.0F;
        } else
        {
            obj.render(x, y, z, defColorInfo, true);
        }
        obj.alpha = 1.0F;
        IndieGL.End();
        IndieGL.glColorMask(true, true, true, true);
        IndieGL.glAlphaFunc(516, 0.0F);
        IndieGL.glBlendFunc(0, 768);
        if(CircleStencil)
            IndieGL.glStencilFunc(514, stenciled, 255);
        else
            IndieGL.glStencilFunc(514, stenciled, 127);
        IndieGL.glStencilOp(7680, 7680, 7680);
        if(texWhite == null)
            texWhite = Texture.getSharedTexture("media/ui/white.png");
        Texture tex = texWhite;
        float offX = 0.0F;
        float offY = 0.0F;
        float offZ = 0.0F;
        float sx = IsoUtils.XToScreen((float)x + offX, (float)y + offY, (float)z + offZ, 0);
        float sy = IsoUtils.YToScreen((float)x + offX, (float)y + offY, (float)z + offZ, 0);
        sx = (int)sx;
        sy = (int)sy;
        sx -= (int)IsoCamera.getOffX();
        sy -= (int)IsoCamera.getOffY();
        sy += 128F;
        sy += 128F;
        sy += 128F;
        if(tex != null)
        {
            tex.renderwallw((int)sx, (int)sy, 60, 32, colu, coll, colu2, coll2);
            tex.renderwalln((int)sx + 4, (int)sy, 64, 32, colu, colr, colu2, colr2);
        }
        IndieGL.End();
        IndieGL.glStencilFunc(519, 1, 255);
        IndieGL.glStencilOp(7680, 7680, 7680);
        IndieGL.glBlendFunc(770, 771);
        return stenciled;
    }

    public int DoWallLightingW(IsoObject obj, int stenciled, int ax, int ay)
    {
        if((float)z != IsoCamera.CamCharacter.z)
            CircleStencil = false;
        if(room == IsoCamera.CamCharacter.current.getRoom() && IsoCamera.CamCharacter.current.getRoom() != null && room != null)
            CircleStencil = false;
        if(obj.sprite.getType() == IsoObjectType.doorFrW || obj.sprite.getType() == IsoObjectType.doorW || obj.sprite.getType() == IsoObjectType.doorN || (obj instanceof IsoWindow))
        {
            stenciled += 5;
            CircleStencil = false;
        }
        colu = getCell().getVertLights()[x][y][z][0];
        coll = getCell().getVertLights()[x][y][z][3];
        colu2 = colu;
        coll2 = coll;
        if(z < IsoCell.MaxHeight - 1)
        {
            colu2 = getCell().getVertLights()[x][y][z + 1][0];
            coll2 = getCell().getVertLights()[x][y][z + 1][3];
        }
        IndieGL.End();
        stenciled++;
        if(CircleStencil)
        {
            IndieGL.glEnable(2960);
            IndieGL.glEnable(3008);
            IndieGL.glAlphaFunc(516, 0.1F);
            IndieGL.glStencilFunc(517, 128, 128);
            IndieGL.glStencilOp(7680, 7680, 7680);
            obj.render(x, y, z, defColorInfo, false);
            IndieGL.glStencilFunc(519, stenciled, 255);
            obj.alpha = 0.21F;
        } else
        {
            IndieGL.glEnable(3008);
            IndieGL.glAlphaFunc(516, 0.1F);
            IndieGL.glStencilFunc(519, stenciled, 127);
        }
        IndieGL.glStencilOp(7680, 7680, 7681);
        if(CircleStencil)
        {
            float val = GameTime.getInstance().getAmbient();
            defColorInfo.r = val * rmod;
            defColorInfo.g = val * gmod;
            defColorInfo.b = val * bmod;
            obj.render(x, y, z, defColorInfo, true);
            defColorInfo.r = 1.0F;
            defColorInfo.g = 1.0F;
            defColorInfo.b = 1.0F;
        } else
        {
            obj.render(x, y, z, defColorInfo, true);
        }
        obj.alpha = 1.0F;
        IndieGL.End();
        IndieGL.glColorMask(true, true, true, true);
        IndieGL.glAlphaFunc(516, 0.0F);
        if(CircleStencil)
            IndieGL.glStencilFunc(514, stenciled, 255);
        else
            IndieGL.glStencilFunc(514, stenciled, 127);
        IndieGL.glStencilOp(7680, 7680, 7680);
        IndieGL.glBlendFunc(0, 768);
        if(texWhite == null)
            texWhite = Texture.getSharedTexture("media/ui/white.png");
        Texture tex = texWhite;
        float offX = 0.0F;
        float offY = 0.0F;
        float offZ = 0.0F;
        float sx = IsoUtils.XToScreen((float)x + offX, (float)y + offY, (float)z + offZ, 0);
        float sy = IsoUtils.YToScreen((float)x + offX, (float)y + offY, (float)z + offZ, 0);
        sx = (int)sx;
        sy = (int)sy;
        sx -= (int)IsoCamera.getOffX();
        sy -= (int)IsoCamera.getOffY();
        sy += 128F;
        sy += 128F;
        sy += 128F;
        if(tex != null)
            tex.renderwallw((int)sx, (int)sy, 64, 32, colu, coll, colu2, coll2);
        IndieGL.End();
        IndieGL.glStencilFunc(519, 1, 255);
        IndieGL.glStencilOp(7680, 7680, 7680);
        IndieGL.glBlendFunc(770, 771);
        return stenciled;
    }

    public KahluaTable getLuaMovingObjectList()
    {
        KahluaTable table = LuaManager.platform.newTable();
        LuaManager.env.rawset("Objects", table);
        for(int n = 0; n < MovingObjects.size(); n++)
            table.rawset(n + 1, MovingObjects.get(n));

        return table;
    }

    public boolean Has(IsoObjectType type)
    {
        return hasTypes.isSet(type);
    }

    public boolean isZone(String zone)
    {
        return IsoWorld.instance.CurrentCell.IsZone(zone, x, y);
    }

    public void DeleteTileObject(IsoObject obj)
    {
        Objects.remove(obj);
        RecalcAllWithNeighbours(true);
    }

    public KahluaTable getLuaTileObjectList()
    {
        KahluaTable table = LuaManager.platform.newTable();
        LuaManager.env.rawset("Objects", table);
        for(int n = 0; n < Objects.size(); n++)
            table.rawset(n + 1, Objects.get(n));

        return table;
    }

    boolean HasDoor(boolean north)
    {
        for(int n = 0; n < SpecialObjects.size(); n++)
            if((SpecialObjects.get(n) instanceof IsoDoor) && ((IsoDoor)SpecialObjects.get(n)).north == north)
                return true;

        return false;
    }

    private void fudgeShadowsToAlpha(IsoObject obj, Color colu2)
    {
        float invAlpha = 1.0F - obj.alpha;
        if(colu2.r < invAlpha)
            colu2.r = invAlpha;
        if(colu2.g < invAlpha)
            colu2.g = invAlpha;
        if(colu2.b < invAlpha)
            colu2.b = invAlpha;
    }

    private void writeObject(ObjectOutputStream out)
        throws IOException
    {
        out.defaultWriteObject();
    }

    private void readObject(ObjectInputStream in)
        throws IOException, ClassNotFoundException
    {
        in.defaultReadObject();
    }

    public void setDirty()
    {
        bDirty = true;
    }

    public void save(ByteBuffer output, ObjectOutputStream outputObj)
        throws IOException
    {
        int fds;
        if(x == 98 && y == 12 && z == 1)
            fds = 0;
        output.putInt(Objects.size());
        for(int n = 0; n < Objects.size(); n++)
        {
            if(SpecialObjects.contains(Objects.get(n)))
                output.put((byte)1);
            else
                output.put((byte)0);
            ((IsoObject)Objects.get(n)).save(output);
        }

        NulledArrayList bodyList = new NulledArrayList();
        for(int n = 0; n < StaticMovingObjects.size(); n++)
            if(StaticMovingObjects.get(n) instanceof IsoDeadBody)
                bodyList.add((IsoDeadBody)StaticMovingObjects.get(n));

        output.putInt(bodyList.size());
        for(int n = 0; n < bodyList.size(); n++)
            ((IsoDeadBody)bodyList.get(n)).save(output);

        output.putInt(CollisionMatrixPrototypes.instance.ToBitMatrix(pathMatrix));
        output.putInt(CollisionMatrixPrototypes.instance.ToBitMatrix(visionMatrix));
        output.putInt(CollisionMatrixPrototypes.instance.ToBitMatrix(collideMatrix));
        output.putInt(CollisionMatrixPrototypes.instance.ToBitMatrix(pathWithDoorsMatrix));
        bDirty = false;
    }

    static void loadmatrix(boolean aflag[][][], DataInputStream datainputstream)
        throws IOException
    {
    }

    static void savematrix(boolean matrix[][][], DataOutputStream output)
        throws IOException
    {
        for(int x = 0; x < 3; x++)
        {
            for(int y = 0; y < 3; y++)
            {
                for(int z = 0; z < 3; z++)
                    output.writeBoolean(matrix[x][y][z]);

            }

        }

    }

    public boolean isCommonGrass()
    {
        if(Objects.isEmpty())
            return false;
        IsoObject o = (IsoObject)Objects.get(0);
        return o.sprite.getProperties().Is(IsoFlagType.solidfloor) && ("TileFloorExt_3".equals(o.tile) || "TileFloorExt_4".equals(o.tile));
    }

    public static boolean toBoolean(byte data[])
    {
        return data != null && data.length != 0 ? data[0] != 0 : false;
    }

    public int loadbytestream(BufferedInputStream input, int index, byte databytes[])
        throws IOException
    {
        bSeen = databytes[index] != 0;
        index++;
        index = Properties.loadStream(databytes, index);
        return index;
    }

    public void loadbytestream(ByteBuffer output)
        throws IOException
    {
        bSeen = output.get() != 0;
    }

    public void savebytestream(ByteBuffer output)
        throws IOException
    {
        output.put(((byte)(bSeen ? 1 : 0)));
    }

    public int savebytestream(BufferedOutputStream output, int index, byte databytes[])
        throws IOException
    {
        int gfdg;
        if(index >= databytes.length)
            gfdg = 0;
        databytes[index] = ((byte)(bSeen ? 1 : 0));
        index++;
        index = Properties.saveStream(databytes, index);
        return index;
    }

    void load(ByteBuffer b)
        throws IOException
    {
        int fds;
        if(x == 98 && y == 12 && z == 1)
            fds = 0;
        int objs = b.getInt();
        for(int n = 0; n < objs; n++)
        {
            boolean bSpecial = b.get() != 0;
            IsoObject obj = null;
            obj = IsoObject.factoryFromFileInput(getCell(), b);
            if(obj == null)
                continue;
            obj.square = this;
            obj.load(b);
            Objects.add(obj);
            if(bSpecial)
                SpecialObjects.add(obj);
        }

        objs = b.getInt();
        for(int n = 0; n < objs; n++)
        {
            IsoMovingObject obj = null;
            obj = (IsoMovingObject)IsoObject.factoryFromFileInput(getCell(), b);
            if(obj != null)
            {
                obj.square = this;
                obj.load(b);
                StaticMovingObjects.add(obj);
            }
        }

        pathMatrix = CollisionMatrixPrototypes.instance.Add(b.getInt());
        visionMatrix = CollisionMatrixPrototypes.instance.Add(b.getInt());
        collideMatrix = CollisionMatrixPrototypes.instance.Add(b.getInt());
        pathWithDoorsMatrix = CollisionMatrixPrototypes.instance.Add(b.getInt());
    }

    public float scoreAsWaypoint(int x, int y)
    {
        float score = 2.0F;
        score -= IsoUtils.DistanceManhatten(x, y, getX(), getY()) * 5F;
        return score;
    }

    public void InvalidateSpecialObjectPaths()
    {
        for(int x = -1; x <= 1; x++)
        {
            for(int y = -1; y <= 1; y++)
            {
                for(int z = -1; z <= 1; z++)
                {
                    if(this.z == 0 && z < 0)
                        continue;
                    IsoGridSquare sq = getCell().getGridSquare(this.x + x, this.y + y, this.z + z);
                    if(sq != null)
                        sq.pathWithDoorsInvalidated = true;
                }

            }

        }

    }

    public boolean isFree(boolean bCountOtherCharacters)
    {
        if(bCountOtherCharacters && MovingObjects.size() > 0)
            return false;
        if(CachedIsFree)
            return CacheIsFree;
        CachedIsFree = true;
        CacheIsFree = true;
        if(Properties.Is(IsoFlagType.solid) || Properties.Is(IsoFlagType.solidtrans))
            CacheIsFree = false;
        if(!Properties.Is(IsoFlagType.solidfloor))
            CacheIsFree = false;
        if(Has(IsoObjectType.stairsBN) || Has(IsoObjectType.stairsMN) || Has(IsoObjectType.stairsTN))
            CacheIsFree = true;
        else
        if(Has(IsoObjectType.stairsBW) || Has(IsoObjectType.stairsMW) || Has(IsoObjectType.stairsTW))
            CacheIsFree = true;
        else
        if(n == null && s == null && w == null && e == null)
            CacheIsFree = false;
        return CacheIsFree;
    }

    public boolean isFreeOrMidair(boolean bCountOtherCharacters)
    {
        if(bCountOtherCharacters && MovingObjects.size() > 0)
            return false;
        boolean CacheIsFree = true;
        if(Properties.Is(IsoFlagType.solid) || Properties.Is(IsoFlagType.solidtrans))
            CacheIsFree = false;
        if(Has(IsoObjectType.stairsBN) || Has(IsoObjectType.stairsMN) || Has(IsoObjectType.stairsTN))
            CacheIsFree = true;
        else
        if(Has(IsoObjectType.stairsBW) || Has(IsoObjectType.stairsMW) || Has(IsoObjectType.stairsTW))
            CacheIsFree = true;
        return CacheIsFree;
    }

    public boolean isNotBlocked(boolean bCountOtherCharacters)
    {
        if(!CachedIsFree)
        {
            CacheIsFree = true;
            CachedIsFree = true;
            if(Properties.Is(IsoFlagType.solid) || Properties.Is(IsoFlagType.solidtrans))
                CacheIsFree = false;
            if(!Properties.Is(IsoFlagType.solidfloor))
                CacheIsFree = false;
        } else
        if(!CacheIsFree)
            return false;
        return !bCountOtherCharacters || MovingObjects.size() <= 0;
    }

    public IsoWindow getWindow(IsoGridSquare next)
    {
        NulledArrayList special = null;
        if(next.y < y || next.x < x)
            special = SpecialObjects;
        else
            special = next.SpecialObjects;
        for(int n = 0; n < special.size(); n++)
        {
            if(!(special.get(n) instanceof IsoWindow))
                continue;
            IsoWindow door = (IsoWindow)special.get(n);
            boolean no = door.north;
            if(door.open)
                continue;
            if(no && next.y != y)
                return door;
            if(!no && next.x != x)
                return door;
        }

        return null;
    }

    public IsoDoor getDoorTo(IsoGridSquare next)
    {
        NulledArrayList special = null;
        if(next.y < y && next.x == x || next.x < x && next.y == y)
            special = SpecialObjects;
        else
        if(next.y > y && next.x == x || next.x > x && next.y == y)
            special = next.SpecialObjects;
        else
            return null;
        for(int n = 0; n < special.size(); n++)
        {
            if(!(special.get(n) instanceof IsoDoor))
                continue;
            IsoDoor door = (IsoDoor)special.get(n);
            boolean no = door.north;
            if(door.open)
                no = !no;
            if(no && next.y != y)
                return door;
            if(!no && next.x != x)
                return door;
        }

        return null;
    }

    public IsoWindow getWindowTo(IsoGridSquare next)
    {
        NulledArrayList special = null;
        if(next.y < y || next.x < x)
            special = SpecialObjects;
        else
            special = next.SpecialObjects;
        for(int n = 0; n < special.size(); n++)
        {
            if(!(special.get(n) instanceof IsoWindow))
                continue;
            IsoWindow door = (IsoWindow)special.get(n);
            boolean no = door.north;
            if(no && next.y != y)
                return door;
            if(!no && next.x != x)
                return door;
        }

        return null;
    }

    public IsoDoor getDoorFrameTo(IsoGridSquare next)
    {
        NulledArrayList special = null;
        if(next.y < y || next.x < x)
            special = SpecialObjects;
        else
            special = next.SpecialObjects;
        for(int n = 0; n < special.size(); n++)
        {
            if(!(special.get(n) instanceof IsoDoor))
                continue;
            IsoDoor door = (IsoDoor)special.get(n);
            boolean no = door.north;
            if(no && next.y != y)
                return door;
            if(!no && next.x != x)
                return door;
        }

        return null;
    }

    public IsoGridSquare()
    {
        bCouldSee = false;
        bCanSee = false;
        lightInfo = new ColorInfo();
        StaticMovingObjects = new NulledArrayList(0);
        MovingObjects = new NulledArrayList(0);
        Objects = new NulledArrayList(2);
        pathWithDoorsInvalidated = true;
        Properties = new PropertyContainer();
        room = null;
        SpecialObjects = new NulledArrayList(0);
        lampostTotalR = 0.0F;
        lampostTotalG = 0.0F;
        lampostTotalB = 0.0F;
        bSeen = false;
        darkMulti = 0.0F;
        lights = new NulledArrayList();
        hasTypes = new ZomboidBitFlag(IsoObjectType.MAX.index());
        targetDarkMulti = 0.0F;
        isTorchDot = false;
        hasZombies = false;
        DeferedCharacters = new NulledArrayList();
        bDirty = true;
        CacheIsFree = false;
        CachedIsFree = false;
        ID = 0;
        bLightDirty = true;
        bStairsTN = false;
        bStairsTW = false;
        SolidFloorCached = false;
        SolidFloor = false;
        lastLightInfo = new ColorInfo();
    }

    public IsoGridSquare(IsoCell cell, SliceY slice, int x, int y, int z)
    {
        bCouldSee = false;
        bCanSee = false;
        lightInfo = new ColorInfo();
        StaticMovingObjects = new NulledArrayList(0);
        MovingObjects = new NulledArrayList(0);
        Objects = new NulledArrayList(2);
        pathWithDoorsInvalidated = true;
        Properties = new PropertyContainer();
        room = null;
        SpecialObjects = new NulledArrayList(0);
        lampostTotalR = 0.0F;
        lampostTotalG = 0.0F;
        lampostTotalB = 0.0F;
        bSeen = false;
        darkMulti = 0.0F;
        lights = new NulledArrayList();
        hasTypes = new ZomboidBitFlag(IsoObjectType.MAX.index());
        targetDarkMulti = 0.0F;
        isTorchDot = false;
        hasZombies = false;
        DeferedCharacters = new NulledArrayList();
        bDirty = true;
        CacheIsFree = false;
        CachedIsFree = false;
        ID = 0;
        bLightDirty = true;
        bStairsTN = false;
        bStairsTW = false;
        SolidFloorCached = false;
        SolidFloor = false;
        lastLightInfo = new ColorInfo();
        if(ZombieHall == null)
        {
            ZombieHall = new IsoSprite(getCell().SpriteManager);
            ZombieHall.LoadFramesPalette("Zombie", "walk", 4, "Zombie_palette10");
        }
        ID = ++IDMax;
        slice.Squares[x][z] = this;
        this.x = x;
        this.y = y;
        this.z = z;
        this.slice = slice;
        int col = 0;
        int path = 0;
        int pathdoor = 0;
        int vision = 0;
        for(int xx = 0; xx < 3; xx++)
        {
            for(int yy = 0; yy < 3; yy++)
            {
                for(int zz = 0; zz < 3; zz++)
                {
                    col = BitMatrix.Set(col, xx - 1, yy - 1, zz - 1, true);
                    path = BitMatrix.Set(path, xx - 1, yy - 1, zz - 1, true);
                    pathdoor = BitMatrix.Set(pathdoor, xx - 1, yy - 1, zz - 1, true);
                    vision = BitMatrix.Set(vision, xx - 1, yy - 1, zz - 1, false);
                }

            }

        }

        collideMatrix = CollisionMatrixPrototypes.instance.Add(col);
        pathMatrix = CollisionMatrixPrototypes.instance.Add(path);
        pathWithDoorsMatrix = CollisionMatrixPrototypes.instance.Add(pathdoor);
        visionMatrix = CollisionMatrixPrototypes.instance.Add(vision);
    }

    public void init(IsoCell cell, SliceY slice, int x, int y, int z)
    {
        if(ZombieHall == null)
        {
            ZombieHall = new IsoSprite(getCell().SpriteManager);
            ZombieHall.LoadFramesPalette("Zombie", "walk", 4, "Zombie_palette10");
        }
        slice.Squares[x][z] = this;
        this.x = x;
        this.y = y;
        this.z = z;
        this.slice = slice;
    }

    public IsoGridSquare getTileInDirection(IsoDirections directions)
    {
        if(directions == IsoDirections.N)
            return getCell().getGridSquare(x, y - 1, z);
        if(directions == IsoDirections.NE)
            return getCell().getGridSquare(x + 1, y - 1, z);
        if(directions == IsoDirections.NW)
            return getCell().getGridSquare(x - 1, y - 1, z);
        if(directions == IsoDirections.E)
            return getCell().getGridSquare(x + 1, y, z);
        if(directions == IsoDirections.W)
            return getCell().getGridSquare(x - 1, y, z);
        if(directions == IsoDirections.SE)
            return getCell().getGridSquare(x + 1, y + 1, z);
        if(directions == IsoDirections.SW)
            return getCell().getGridSquare(x - 1, y + 1, z);
        else
            return null;
    }

    IsoObject getWall()
    {
        for(int n = 0; n < Objects.size(); n++)
            if(((IsoObject)Objects.get(n)).sprite.getProperties().Is(IsoFlagType.cutW) || ((IsoObject)Objects.get(n)).sprite.getProperties().Is(IsoFlagType.cutN))
                return (IsoObject)Objects.get(n);

        return null;
    }

    public IsoObject getWall(boolean bNorth)
    {
        for(int n = 0; n < Objects.size(); n++)
            if(((IsoObject)Objects.get(n)).getType() == IsoObjectType.wall && (((IsoObject)Objects.get(n)).sprite.getProperties().Is(IsoFlagType.cutN) && bNorth || ((IsoObject)Objects.get(n)).sprite.getProperties().Is(IsoFlagType.cutW) && !bNorth))
                return (IsoObject)Objects.get(n);

        return null;
    }

    public IsoObject getFloor()
    {
        for(int n = 0; n < Objects.size(); n++)
            if(((IsoObject)Objects.get(n)).sprite.getProperties().Is(IsoFlagType.solidfloor))
                return (IsoObject)Objects.get(n);

        return null;
    }

    public void interpolateLight(ColorInfo inf, float x, float y)
    {
        if(getCell().getCurrentLightX() >= getCell().getVertLights().length || getCell().getCurrentLightY() > getCell().getVertLights()[0].length)
        {
            return;
        } else
        {
            int coltl = getCell().getVertLights()[this.x][this.y][z][0];
            int coltr = getCell().getVertLights()[this.x][this.y][z][1];
            int colbr = getCell().getVertLights()[this.x][this.y][z][2];
            int colbl = getCell().getVertLights()[this.x][this.y][z][3];
            tl.fromColor(coltl);
            bl.fromColor(colbl);
            tr.fromColor(coltr);
            br.fromColor(colbr);
            tl.interp(tr, x, interp1);
            bl.interp(br, x, interp2);
            interp1.interp(interp2, y, finalCol);
            inf.r = finalCol.r;
            inf.g = finalCol.g;
            inf.b = finalCol.b;
            inf.a = finalCol.a;
            return;
        }
    }

    void AddWoodWall(boolean north, String type)
    {
        int n = 48;
        if(north)
            n++;
        if(type.equals("DoorFrame"))
            n += 10;
        if(type.equals("WindowFrame"))
            n += 8;
        String str = (new StringBuilder()).append("TileWalls_").append(n).toString();
        for(n = 0; n < lights.size(); n++)
        {
            if(((IsoLightSource)lights.get(n)).bActive)
                ((IsoLightSource)lights.get(n)).bWasActive = true;
            else
                ((IsoLightSource)lights.get(n)).bWasActive = false;
            if(((IsoLightSource)lights.get(n)).bWasActive)
            {
                ((IsoLightSource)lights.get(n)).bActive = !((IsoLightSource)lights.get(n)).bActive;
                ((IsoLightSource)lights.get(n)).update();
            }
        }

        boolean bIsV = false;
        int VSquareX = 0;
        int VSquareY = 0;
        if(north)
        {
            IsoGridSquare other = getCell().getGridSquare(x + 1, y - 1, z);
            if(other != null && other.Properties.Is(IsoFlagType.cutW))
            {
                bIsV = true;
                VSquareX = x + 1;
                VSquareY = y;
            }
        } else
        {
            IsoGridSquare other = getCell().getGridSquare(x - 1, y + 1, z);
            if(other != null && other.Properties.Is(IsoFlagType.cutN))
            {
                bIsV = true;
                VSquareX = x;
                VSquareY = y + 1;
            }
        }
        IsoObject obj = new IsoObject(getCell(), this, str);
        if(north)
        {
            if(!type.equals("DoorFrame") && !type.equals("WindowFrame"))
                obj.sprite.getProperties().Set(IsoFlagType.collideN, "");
            obj.sprite.getProperties().Set(IsoFlagType.cutN, "");
            if(type.equals("WindowFrame"))
                obj.sprite.getProperties().Set(IsoFlagType.transparentN, "");
        }
        if(!north)
        {
            if(!type.equals("DoorFrame") && !type.equals("WindowFrame"))
                obj.sprite.getProperties().Set(IsoFlagType.collideW, "");
            obj.sprite.getProperties().Set(IsoFlagType.cutW, "");
            if(type.equals("WindowFrame"))
                obj.sprite.getProperties().Set(IsoFlagType.transparentW, "");
        }
        if(bIsV)
        {
            IsoGridSquare vSquare = getCell().getGridSquare(VSquareX, VSquareY, z);
            if(vSquare == null)
            {
                vSquare = new IsoGridSquare(getCell(), getCell().getSlices()[VSquareY], VSquareX, VSquareY, z);
                getCell().ConnectNewSquare(vSquare, true);
            }
            IsoObject obj2 = new IsoObject(getCell(), vSquare, "TileWalls_51");
            obj2.sprite.getProperties().Set(IsoFlagType.cutN, "");
            obj2.sprite.getProperties().Set(IsoFlagType.cutW, "");
            vSquare.Objects.insert(obj2, 0);
            vSquare.RecalcProperties();
        }
        Objects.insert(obj, 0);
        if(type.equals("DoorFrame"))
        {
            if(!north)
                str = "TileFrames_14";
            else
                str = "TileFrames_15";
            obj = new IsoObject(getCell(), this, str);
            if(north)
                obj.setType(IsoObjectType.doorFrN);
            if(!north)
                obj.setType(IsoObjectType.doorFrW);
            Objects.insert(obj, 1);
        }
        RecalcAllWithNeighbours(true);
        if(!north && type.equals("Wall"))
            Properties.UnSet(IsoFlagType.transparentW);
        else
        if(type.equals("Wall"))
            Properties.UnSet(IsoFlagType.transparentN);
    }

    public void EnsureSurroundNotNull()
    {
        for(int x = -1; x <= 1; x++)
        {
            for(int y = -1; y <= 1; y++)
            {
                IsoGridSquare above = getCell().getGridSquare(this.x + x, this.x + y, z + z);
                if(above == null)
                {
                    above = new IsoGridSquare(getCell(), getCell().getSlices()[this.x + y], this.x + x, this.x + y, z + z);
                    getCell().ConnectNewSquare(above, true);
                }
            }

        }

    }

    void AddStairs(boolean north, int level, String type)
    {
        EnsureSurroundNotNull();
        boolean floating = !TreatAsSolidFloor();
        CachedIsFree = false;
        int n = 0;
        if(north)
            n += 8;
        zombie.ai.astar.AStarPathMap.Node node = getCell().getPathMap().nodes[this.x][this.y][this.z];
        if(node == null)
        {
            getCell().getPathMap().nodes[this.x][this.y][this.z] = new zombie.ai.astar.AStarPathMap.Node((short)this.x, (short)this.y, (short)this.z);
            getCell().getPathMap().NodeMap.add(getCell().getPathMap().nodes[this.x][this.y][this.z]);
        }
        node = getCell().getPathMap().nodes[this.x][this.y][this.z];
        n += level;
        String str = (new StringBuilder()).append("TileStairs_").append(n).toString();
        IsoObject obj = new IsoObject(this, str, str, false);
        if(north)
        {
            if(level == 0)
                obj.setType(IsoObjectType.stairsBN);
            if(level == 1)
                obj.setType(IsoObjectType.stairsMN);
            if(level == 2)
                obj.setType(IsoObjectType.stairsTN);
        }
        if(!north)
        {
            if(level == 0)
                obj.setType(IsoObjectType.stairsBW);
            if(level == 1)
                obj.setType(IsoObjectType.stairsMW);
            if(level == 2)
                obj.setType(IsoObjectType.stairsTW);
        }
        Objects.add(obj);
        Properties.Clear();
        for(n = 0; n < Objects.size(); n++)
        {
            IsoObject nobj = (IsoObject)Objects.get(n);
            if(nobj.sprite != null)
                Properties.AddProperties(nobj.sprite.getProperties());
        }

        if(floating)
        {
            IsoGridSquare sq = getCell().getGridSquare(this.x, this.y, this.z - 1);
            boolean bLast = false;
            do
            {
                if(sq == null)
                    break;
                String str2 = "TileStairs_6";
                if(north)
                    str2 = "TileStairs_7";
                IsoObject obj2 = new IsoObject(getCell(), this, str2);
                obj2.sprite.getProperties().Set(IsoFlagType.solidtrans, "");
                sq.Objects.add(obj2);
                sq.RecalcAllWithNeighbours(true);
                if(sq.TreatAsSolidFloor())
                    break;
                if(getCell().getGridSquare(sq.x, sq.y, sq.z - 1) == null)
                {
                    sq = new IsoGridSquare(getCell(), getCell().getSlices()[sq.y], sq.x, sq.y, sq.z - 1);
                    getCell().ConnectNewSquare(sq, true);
                } else
                {
                    sq = getCell().getGridSquare(sq.x, sq.y, sq.z - 1);
                }
            } while(true);
        }
        if(level == 2)
        {
            IsoGridSquare above = null;
            getCell().getStairsNodes().add(node.ID);
            if(north)
            {
                above = getCell().getGridSquare(this.x, this.y - 1, this.z + 1);
                if(above == null || !above.Properties.Is(IsoFlagType.solidfloor))
                {
                    if(above == null)
                    {
                        above = new IsoGridSquare(getCell(), getCell().getSlices()[this.y - 1], this.x, this.y - 1, this.z + 1);
                        getCell().ConnectNewSquare(above, true);
                    }
                    above.CachedIsFree = false;
                    above.EnsureSurroundNotNull();
                    IsoObject obj2 = new IsoObject(getCell(), above, "TileFloorInt_15");
                    obj2.sprite.getProperties().Set(IsoFlagType.solidfloor);
                    IsoGridSquare an = getCell().getGridSquare((int)UIManager.getPickedTile().x, (int)UIManager.getPickedTile().y - 1, (int)IsoCamera.CamCharacter.getZ());
                    IsoGridSquare s = getCell().getGridSquare((int)UIManager.getPickedTile().x, (int)UIManager.getPickedTile().y + 1, (int)IsoCamera.CamCharacter.getZ());
                    IsoGridSquare e = getCell().getGridSquare((int)UIManager.getPickedTile().x + 1, (int)UIManager.getPickedTile().y, (int)IsoCamera.CamCharacter.getZ());
                    IsoGridSquare w = getCell().getGridSquare((int)UIManager.getPickedTile().x - 1, (int)UIManager.getPickedTile().y, (int)IsoCamera.CamCharacter.getZ());
                    IsoRoom room = null;
                    above.Objects.add(obj2);
                    above.RecalcAllWithNeighbours(true);
                    node = getCell().getPathMap().nodes[above.getX()][above.getY()][above.getZ()];
                    if(node == null)
                    {
                        getCell().getPathMap().nodes[above.getX()][above.getY()][above.getZ()] = new zombie.ai.astar.AStarPathMap.Node((short)above.getX(), (short)above.getY(), (short)above.getZ());
                        node = getCell().getPathMap().nodes[above.getX()][above.getY()][above.getZ()];
                        getCell().getPathMap().NodeMap.add(node);
                    }
                    getCell().getStairsNodes().add(node.ID);
                    if(an != null && an.Properties.Is(IsoFlagType.solidfloor))
                    {
                        BlockInfo inf = an.testAdjacentRoomTransition(0, 1, 0);
                        if(an.room != null && !inf.ThroughDoor && !an.testCollideAdjacent(null, 0, 1, 0))
                            room = an.room;
                    }
                    if(s != null && s.Properties.Is(IsoFlagType.solidfloor))
                    {
                        BlockInfo inf = s.testAdjacentRoomTransition(0, -1, 0);
                        if(s.room != null && !inf.ThroughDoor && !s.testCollideAdjacent(null, 0, -1, 0))
                            room = s.room;
                    }
                    if(e != null && e.Properties.Is(IsoFlagType.solidfloor))
                    {
                        BlockInfo inf = e.testAdjacentRoomTransition(-1, 0, 0);
                        if(e.room != null && !inf.ThroughDoor && !e.testCollideAdjacent(null, -1, 0, 0))
                            room = e.room;
                    }
                    if(w != null && w.Properties.Is(IsoFlagType.solidfloor))
                    {
                        BlockInfo inf = w.testAdjacentRoomTransition(1, 0, 0);
                        if(w.room != null && !inf.ThroughDoor && !w.testCollideAdjacent(null, 1, 0, 0))
                            room = w.room;
                    }
                    if(room != null)
                    {
                        above.room = room;
                        room.TileList.add(above);
                    } else
                    {
                        above.room = new IsoRoom();
                        above.room.building = new IsoBuilding();
                        above.room.building.Rooms.add(above.room);
                        getCell().getBuildingList().add(above.room.building);
                    }
                }
                above.CachedIsFree = false;
            } else
            {
                above = getCell().getGridSquare(this.x - 1, this.y, this.z + 1);
                if(above == null || !above.Properties.Is(IsoFlagType.solidfloor))
                {
                    if(above == null)
                    {
                        above = new IsoGridSquare(getCell(), getCell().getSlices()[this.y], this.x - 1, this.y, this.z + 1);
                        getCell().ConnectNewSquare(above, true);
                    }
                    IsoObject obj2 = new IsoObject(getCell(), above, "TileFloorInt_15");
                    obj2.sprite.getProperties().Set(IsoFlagType.solidfloor);
                    above.Objects.add(obj2);
                    above.RecalcProperties();
                    IsoGridSquare an = getCell().getGridSquare((int)UIManager.getPickedTile().x, (int)UIManager.getPickedTile().y - 1, (int)IsoCamera.CamCharacter.getZ());
                    IsoGridSquare s = getCell().getGridSquare((int)UIManager.getPickedTile().x, (int)UIManager.getPickedTile().y + 1, (int)IsoCamera.CamCharacter.getZ());
                    IsoGridSquare e = getCell().getGridSquare((int)UIManager.getPickedTile().x + 1, (int)UIManager.getPickedTile().y, (int)IsoCamera.CamCharacter.getZ());
                    IsoGridSquare w = getCell().getGridSquare((int)UIManager.getPickedTile().x - 1, (int)UIManager.getPickedTile().y, (int)IsoCamera.CamCharacter.getZ());
                    IsoRoom room = null;
                    above.RecalcAllWithNeighbours(true);
                    if(an != null && an.Properties.Is(IsoFlagType.solidfloor))
                    {
                        BlockInfo inf = an.testAdjacentRoomTransition(0, 1, 0);
                        if(an.room != null && !inf.ThroughDoor && !an.testCollideAdjacent(null, 0, 1, 0))
                            room = an.room;
                    }
                    if(s != null && s.Properties.Is(IsoFlagType.solidfloor))
                    {
                        BlockInfo inf = s.testAdjacentRoomTransition(0, -1, 0);
                        if(s.room != null && !inf.ThroughDoor && !s.testCollideAdjacent(null, 0, -1, 0))
                            room = s.room;
                    }
                    if(e != null && e.Properties.Is(IsoFlagType.solidfloor))
                    {
                        BlockInfo inf = e.testAdjacentRoomTransition(-1, 0, 0);
                        if(e.room != null && !inf.ThroughDoor && !e.testCollideAdjacent(null, -1, 0, 0))
                            room = e.room;
                    }
                    if(w != null && w.Properties.Is(IsoFlagType.solidfloor))
                    {
                        BlockInfo inf = w.testAdjacentRoomTransition(1, 0, 0);
                        if(w.room != null && !inf.ThroughDoor && !w.testCollideAdjacent(null, 1, 0, 0))
                            room = w.room;
                    }
                    if(room != null)
                    {
                        above.room = room;
                        room.TileList.add(above);
                    } else
                    {
                        above.room = new IsoRoom();
                        above.room.building = new IsoBuilding();
                        above.room.building.Rooms.add(above.room);
                        getCell().getBuildingList().add(above.room.building);
                    }
                    above.CachedIsFree = false;
                }
            }
            above = getCell().getGridSquare(this.x, this.y, this.z + 1);
            if(above == null)
            {
                above = new IsoGridSquare(getCell(), getCell().getSlices()[this.y], this.x, this.y, this.z + 1);
                getCell().ConnectNewSquare(above, true);
                above.CachedIsFree = false;
            }
        }
        for(int x = getX() - 1; x <= getX() + 1; x++)
        {
            for(int y = getY() - 1; y <= getY() + 1; y++)
            {
                for(int z = getZ() - 1; z <= getZ() + 1; z++)
                {
                    IsoGridSquare sq = getCell().getGridSquare(x, y, z);
                    if(sq != null)
                    {
                        sq.ReCalculateCollide(this);
                        sq.ReCalculateVisionBlocked(this);
                        sq.ReCalculatePathFind(this);
                        ReCalculateCollide(sq);
                        ReCalculatePathFind(sq);
                        ReCalculateVisionBlocked(sq);
                        sq.CachedIsFree = false;
                    }
                }

            }

        }

        for(n = 0; n < lights.size(); n++)
            if(((IsoLightSource)lights.get(n)).bWasActive)
            {
                ((IsoLightSource)lights.get(n)).bActive = !((IsoLightSource)lights.get(n)).bActive;
                ((IsoLightSource)lights.get(n)).update();
            }

    }

    void ReCalculateAll(IsoGridSquare a)
    {
        getSlice().Squares[getX()][getZ()] = this;
        SolidFloorCached = false;
        RecalcProperties();
        ReCalculateCollide(a);
        a.ReCalculateCollide(this);
        ReCalculatePathFind(a);
        a.ReCalculatePathFind(this);
        ReCalculateVisionBlocked(a);
        a.ReCalculateVisionBlocked(this);
        setBlockedGridPointers();
        a.setBlockedGridPointers();
    }

    void ReCalculateMineOnly(IsoGridSquare a)
    {
        getSlice().Squares[getX()][getZ()] = this;
        SolidFloorCached = false;
        RecalcProperties();
        ReCalculateCollide(a);
        ReCalculatePathFind(a);
        ReCalculateVisionBlocked(a);
        setBlockedGridPointers();
    }

    public void RecalcAllWithNeighbours(boolean bDoReverse)
    {
        getSlice().Squares[getX()][getZ()] = this;
        slice.bSaveDirty = true;
        SolidFloorCached = false;
        RecalcProperties();
        for(int x = getX() - 1; x <= getX() + 1; x++)
        {
            for(int y = getY() - 1; y <= getY() + 1; y++)
            {
                for(int z = getZ() - 1; z <= getZ() + 1; z++)
                {
                    if(z < 0)
                        continue;
                    int lx = x - getX();
                    int ly = y - getY();
                    int lz = z - getZ();
                    if(lx == 0 && ly == 0 && lz == 0)
                        continue;
                    IsoGridSquare sq = getCell().getGridSquare(x, y, z);
                    if(sq == null)
                        continue;
                    sq.DirtySlice();
                    ReCalculateAll(sq);
                    IsoGridSquare newSquare = this;
                    if(lz != 0)
                        continue;
                    if(lx == 0 && ly == -1)
                    {
                        if(newSquare.testCollideAdjacent(null, lx, ly, 0))
                        {
                            newSquare.n = null;
                            sq.s = null;
                        }
                        continue;
                    }
                    if(lx == 0 && ly == 1)
                    {
                        if(newSquare.testCollideAdjacent(null, lx, ly, 0))
                        {
                            newSquare.s = null;
                            sq.n = null;
                        }
                        continue;
                    }
                    if(lx == -1 && ly == 0)
                    {
                        if(newSquare.testCollideAdjacent(null, lx, ly, 0))
                        {
                            newSquare.w = null;
                            sq.e = null;
                        }
                        continue;
                    }
                    if(lx == 1 && ly == 0)
                    {
                        if(newSquare.testCollideAdjacent(null, lx, ly, 0))
                        {
                            newSquare.e = null;
                            sq.w = null;
                        }
                        continue;
                    }
                    if(lx == 1 && ly == 1)
                    {
                        if(!newSquare.testCollideAdjacent(null, lx, ly, 0))
                        {
                            newSquare.se = null;
                            sq.nw = null;
                        }
                        continue;
                    }
                    if(lx == -1 && ly == 1)
                    {
                        if(newSquare.testCollideAdjacent(null, lx, ly, 0))
                        {
                            newSquare.sw = null;
                            sq.ne = null;
                        }
                        continue;
                    }
                    if(lx == -1 && ly == -1)
                    {
                        if(newSquare.testCollideAdjacent(null, lx, ly, 0))
                        {
                            newSquare.nw = null;
                            sq.se = null;
                        }
                        continue;
                    }
                    if(lx == 1 && ly == -1 && newSquare.testCollideAdjacent(null, lx, ly, 0))
                    {
                        newSquare.ne = null;
                        sq.sw = null;
                    }
                }

            }

        }

    }

    public void RecalcShortcutPtrs(boolean bDoReverse)
    {
        getSlice().Squares[getX()][getZ()] = this;
        for(int x = getX() - 1; x <= getX() + 1; x++)
        {
            for(int y = getY() - 1; y <= getY() + 1; y++)
            {
                for(int z = getZ() - 1; z <= getZ() + 1; z++)
                {
                    if(z < 0)
                        continue;
                    int lx = x - getX();
                    int ly = y - getY();
                    int lz = z - getZ();
                    if(lx == 0 && ly == 0 && lz == 0)
                        continue;
                    IsoGridSquare sq = getCell().getGridSquare(x, y, z);
                    if(sq == null)
                        continue;
                    IsoGridSquare newSquare = this;
                    if(lz != 0)
                        continue;
                    if(lx == 0 && ly == -1)
                    {
                        if(newSquare.testCollideAdjacent(null, lx, ly, 0))
                        {
                            newSquare.n = null;
                            sq.s = null;
                        } else
                        {
                            newSquare.n = sq;
                            sq.s = newSquare;
                        }
                        continue;
                    }
                    if(lx == 0 && ly == 1)
                    {
                        if(newSquare.testCollideAdjacent(null, lx, ly, 0))
                        {
                            newSquare.s = null;
                            sq.n = null;
                        } else
                        {
                            newSquare.s = sq;
                            sq.n = newSquare;
                        }
                        continue;
                    }
                    if(lx == -1 && ly == 0)
                    {
                        if(newSquare.testCollideAdjacent(null, lx, ly, 0))
                        {
                            newSquare.w = null;
                            sq.e = null;
                        } else
                        {
                            newSquare.w = sq;
                            sq.e = newSquare;
                        }
                        continue;
                    }
                    if(lx == 1 && ly == 0)
                    {
                        if(newSquare.testCollideAdjacent(null, lx, ly, 0))
                        {
                            newSquare.e = null;
                            sq.w = null;
                        } else
                        {
                            newSquare.e = sq;
                            sq.w = newSquare;
                        }
                        continue;
                    }
                    if(lx == 1 && ly == 1)
                    {
                        if(!newSquare.testCollideAdjacent(null, lx, ly, 0))
                        {
                            newSquare.se = null;
                            sq.nw = null;
                        } else
                        {
                            newSquare.se = sq;
                            sq.nw = newSquare;
                        }
                        continue;
                    }
                    if(lx == -1 && ly == 1)
                    {
                        if(newSquare.testCollideAdjacent(null, lx, ly, 0))
                        {
                            newSquare.sw = null;
                            sq.ne = null;
                        } else
                        {
                            newSquare.sw = sq;
                            sq.ne = newSquare;
                        }
                        continue;
                    }
                    if(lx == -1 && ly == -1)
                    {
                        if(newSquare.testCollideAdjacent(null, lx, ly, 0))
                        {
                            newSquare.nw = null;
                            sq.se = null;
                        } else
                        {
                            newSquare.nw = sq;
                            sq.se = newSquare;
                        }
                        continue;
                    }
                    if(lx != 1 || ly != -1)
                        continue;
                    if(newSquare.testCollideAdjacent(null, lx, ly, 0))
                    {
                        newSquare.ne = null;
                        sq.sw = null;
                    } else
                    {
                        newSquare.ne = sq;
                        sq.sw = newSquare;
                    }
                }

            }

        }

    }

    boolean IsWindow(int sx, int sy, int sz)
    {
        int difx = sx;
        int dify = sy;
        int difz = sz;
        if(difx > 0 || dify > 0)
        {
            IsoGridSquare sq = getCell().getGridSquare(x + sx, y + sy, z + sz);
            if(sq != null)
            {
                if(difx != 0 && sq.Properties.Is(IsoFlagType.windowW))
                    return true;
                if(dify != 0 && sq.Properties.Is(IsoFlagType.windowN))
                    return true;
            }
        } else
        if(difx < 0 || dify < 0)
        {
            if(difx != 0 && Properties.Is(IsoFlagType.windowW))
                return true;
            if(dify != 0 && Properties.Is(IsoFlagType.windowN))
                return true;
        }
        return false;
    }

    void RemoveAllWith(IsoFlagType propertyType)
    {
        for(int n = 0; n < Objects.size(); n++)
        {
            IsoObject o = (IsoObject)Objects.get(n);
            if(o.sprite != null && o.sprite.getProperties().Is(propertyType))
            {
                Objects.remove(o);
                SpecialObjects.remove(o);
                n--;
            }
        }

        RecalcAllWithNeighbours(true);
    }

    public boolean hasSupport()
    {
        IsoGridSquare s = getCell().getGridSquare(x, y + 1, z);
        IsoGridSquare e = getCell().getGridSquare(x + 1, y, z);
        for(int n = 0; n < Objects.size(); n++)
        {
            IsoObject o = (IsoObject)Objects.get(n);
            if(o.sprite != null && (o.sprite.getProperties().Is(IsoFlagType.solid) || (o.sprite.getProperties().Is(IsoFlagType.cutW) || o.sprite.getProperties().Is(IsoFlagType.cutN)) && !o.sprite.Properties.Is(IsoFlagType.halfheight)))
                return true;
        }

        if(s != null && s.Properties.Is(IsoFlagType.cutN) && !s.Properties.Is(IsoFlagType.halfheight))
            return true;
        return e != null && e.Properties.Is(IsoFlagType.cutW) && !s.Properties.Is(IsoFlagType.halfheight);
    }

    public zombie.ai.astar.AStarPathMap.Node getNode()
    {
        return getCell().getPathMap().nodes[x][y][z];
    }

    public int getID()
    {
        return ID;
    }

    public void setID(int ID)
    {
        this.ID = ID;
    }

    boolean isLightDirty()
    {
        return bLightDirty;
    }

    private int savematrix(boolean pathMatrix[][][], byte databytes[], int index)
    {
        for(int x = 0; x <= 2; x++)
        {
            for(int y = 0; y <= 2; y++)
            {
                for(int z = 0; z <= 2; z++)
                {
                    databytes[index] = ((byte)(pathMatrix[x][y][z] ? 1 : 0));
                    index++;
                }

            }

        }

        return index;
    }

    private int loadmatrix(boolean pathMatrix[][][], byte databytes[], int index)
    {
        for(int x = 0; x <= 2; x++)
        {
            for(int y = 0; y <= 2; y++)
            {
                for(int z = 0; z <= 2; z++)
                {
                    pathMatrix[x][y][z] = databytes[index] != 0;
                    index++;
                }

            }

        }

        return index;
    }

    private void savematrix(boolean pathMatrix[][][], ByteBuffer databytes)
    {
        for(int x = 0; x <= 2; x++)
        {
            for(int y = 0; y <= 2; y++)
            {
                for(int z = 0; z <= 2; z++)
                    databytes.put(((byte)(pathMatrix[x][y][z] ? 1 : 0)));

            }

        }

    }

    private void loadmatrix(boolean pathMatrix[][][], ByteBuffer databytes)
    {
        for(int x = 0; x <= 2; x++)
        {
            for(int y = 0; y <= 2; y++)
            {
                for(int z = 0; z <= 2; z++)
                    pathMatrix[x][y][z] = databytes.get() != 0;

            }

        }

    }

    public void DirtySlice()
    {
        slice.bSaveDirty = true;
    }

    public void ClearTileObjects()
    {
        Objects.clear();
        RecalcProperties();
    }

    public void ClearTileObjectsExceptFloor()
    {
        for(int n = 0; n < Objects.size(); n++)
        {
            IsoObject o = (IsoObject)Objects.get(n);
            if(o.sprite == null || !o.sprite.getProperties().Is(IsoFlagType.solidfloor))
            {
                Objects.remove(o);
                n--;
            }
        }

        RecalcProperties();
    }

    public void RemoveTileObject(IsoObject obj)
    {
        Objects.remove(obj);
        RecalcProperties();
    }

    public void AddTileObject(IsoObject obj)
    {
        Objects.add(obj);
        RecalcProperties();
    }

    public InventoryItem AddWorldInventoryItem(String String, float x, float y, float height)
    {
        InventoryItem item = InventoryItemFactory.CreateItem(String);
        IsoWorldInventoryObject obj = new IsoWorldInventoryObject(item, this, x, y, height);
        Objects.add(obj);
        return item;
    }

    public void Burn()
    {
        if(getCell() == null)
            return;
        BurnWalls();
        IsoGridSquare NewSquare = getCell().getGridSquare(x + 1, y, z);
        if(NewSquare != null)
            NewSquare.BurnWalls();
        NewSquare = getCell().getGridSquare(x, y + 1, z);
        if(NewSquare != null)
            NewSquare.BurnWalls();
        NewSquare = getCell().getGridSquare(x + 1, y + 1, z);
        if(NewSquare != null)
            NewSquare.BurnWallsTCOnly();
        SpecialObjects.clear();
        for(int n = 0; n < Objects.size(); n++)
        {
            IsoObject obj = (IsoObject)Objects.get(n);
            if(obj.sprite == null)
                continue;
            if(obj.sprite.getProperties().Is(IsoFlagType.solidfloor))
            {
                if(!obj.sprite.Properties.Is(IsoFlagType.unflamable))
                    obj.sprite = getCell().SpriteManager.getSprite(897);
                continue;
            }
            if(obj.getType() != IsoObjectType.wall)
            {
                Objects.remove(n);
                n--;
            }
        }

    }

    public void BurnWalls()
    {
        SpecialObjects.clear();
        for(int n = 0; n < Objects.size(); n++)
        {
            IsoObject obj = (IsoObject)Objects.get(n);
            if(obj.sprite != null);
        }

    }

    public void BurnWallsTCOnly()
    {
        for(int n = 0; n < Objects.size(); n++)
        {
            IsoObject obj = (IsoObject)Objects.get(n);
            if(obj.sprite != null);
        }

    }

    public boolean CalculateCollide(IsoGridSquare gridSquare, boolean bVision, boolean bPathfind, boolean bIgnoreSolidTrans)
    {
        if(gridSquare == null && bPathfind)
            return true;
        if(gridSquare == null)
            return false;
        if(bVision && gridSquare.Properties.Is(IsoFlagType.trans))
            return false;
        boolean testW = false;
        boolean testE = false;
        boolean testN = false;
        boolean testS = false;
        if(gridSquare.x < x)
            testW = true;
        if(gridSquare.y < y)
            testN = true;
        if(gridSquare.x > x)
            testE = true;
        if(gridSquare.y > y)
            testS = true;
        if(gridSquare.Properties.Is(IsoFlagType.solid))
            return true;
        if(!bIgnoreSolidTrans && gridSquare.Properties.Is(IsoFlagType.solidtrans) && !gridSquare.Properties.Is(IsoFlagType.windowW) && !gridSquare.Properties.Is(IsoFlagType.windowN))
            return true;
        if(bPathfind && gridSquare.z < z && !Properties.Is(IsoFlagType.solidfloor))
            return gridSquare.Has(IsoObjectType.stairsTN) || gridSquare.Has(IsoObjectType.stairsTW);
        if(bPathfind && gridSquare.z == z)
        {
            if(gridSquare.x > x && gridSquare.y == y && gridSquare.Properties.Is(IsoFlagType.windowW))
                return false;
            if(gridSquare.y > y && gridSquare.x == x && gridSquare.Properties.Is(IsoFlagType.windowN))
                return false;
            if(gridSquare.x < x && gridSquare.y == y && Properties.Is(IsoFlagType.windowW))
                return false;
            if(gridSquare.y < y && gridSquare.x == x && Properties.Is(IsoFlagType.windowN))
                return false;
        }
        if(gridSquare.x != x && gridSquare.z == z && gridSquare.Has(IsoObjectType.stairsTN))
            return true;
        if(gridSquare.y > y && gridSquare.x == x && gridSquare.z == z && gridSquare.Has(IsoObjectType.stairsTN))
            return true;
        if(gridSquare.x > x && gridSquare.y == y && gridSquare.z == z && gridSquare.Has(IsoObjectType.stairsTW))
            return true;
        if(gridSquare.y != y && gridSquare.z == z && gridSquare.Has(IsoObjectType.stairsTW))
            return true;
        if(gridSquare.x != x && gridSquare.z == z && gridSquare.Has(IsoObjectType.stairsMN))
            return true;
        if(gridSquare.y != y && gridSquare.z == z && gridSquare.Has(IsoObjectType.stairsMW))
            return true;
        if(gridSquare.x != x && gridSquare.z == z && gridSquare.Has(IsoObjectType.stairsBN))
            return true;
        if(gridSquare.y != y && gridSquare.z == z && gridSquare.Has(IsoObjectType.stairsBW))
            return true;
        if(gridSquare.x != x && gridSquare.z == z && Has(IsoObjectType.stairsTN))
            return true;
        if(gridSquare.y != y && gridSquare.z == z && Has(IsoObjectType.stairsTW))
            return true;
        if(gridSquare.x != x && gridSquare.z == z && Has(IsoObjectType.stairsMN))
            return true;
        if(gridSquare.y != y && gridSquare.z == z && Has(IsoObjectType.stairsMW))
            return true;
        if(gridSquare.x != x && gridSquare.z == z && Has(IsoObjectType.stairsBN))
            return true;
        if(gridSquare.y != y && gridSquare.z == z && Has(IsoObjectType.stairsBW))
            return true;
        if(gridSquare.y < y && gridSquare.x == x && gridSquare.z > z && Has(IsoObjectType.stairsTN))
            return false;
        if(gridSquare.x < x && gridSquare.y == y && gridSquare.z > z && Has(IsoObjectType.stairsTW))
            return false;
        if(gridSquare.y > y && gridSquare.x == x && gridSquare.z < z && gridSquare.Has(IsoObjectType.stairsTN))
            return false;
        if(gridSquare.x > x && gridSquare.y == y && gridSquare.z < z && gridSquare.Has(IsoObjectType.stairsTW))
            return false;
        if(gridSquare.z == z && !gridSquare.TreatAsSolidFloor() && gridSquare.z > 0)
        {
            IsoGridSquare belowTarg = getCell().getGridSquare(gridSquare.x, gridSquare.y, gridSquare.z - 1);
            if(belowTarg == null)
                return true;
        }
        if(z != gridSquare.z)
            return gridSquare.z >= z || gridSquare.x != x || gridSquare.y != y || TreatAsSolidFloor();
        boolean colN = testN && Properties.Is(IsoFlagType.collideN);
        boolean colW = testW && Properties.Is(IsoFlagType.collideW);
        boolean colS = testS && gridSquare.Properties.Is(IsoFlagType.collideN);
        boolean colE = testE && gridSquare.Properties.Is(IsoFlagType.collideW);
        boolean diag = gridSquare.x != x && gridSquare.y != y;
        if(diag)
        {
            IsoGridSquare betweenA = getCell().getGridSquare(x, gridSquare.y, z);
            IsoGridSquare betweenB = getCell().getGridSquare(gridSquare.x, y, z);
            if(CalculateCollide(betweenA, bVision, bPathfind, bIgnoreSolidTrans))
                return true;
            if(CalculateCollide(betweenB, bVision, bPathfind, bIgnoreSolidTrans))
                return true;
            if(gridSquare.CalculateCollide(betweenA, bVision, bPathfind, bIgnoreSolidTrans))
                return true;
            if(gridSquare.CalculateCollide(betweenB, bVision, bPathfind, bIgnoreSolidTrans))
                return true;
        }
        return colN || colW || colS || colE;
    }

    public boolean CalculateVisionBlocked(IsoGridSquare gridSquare)
    {
        if(gridSquare == null)
            return false;
        boolean testW = false;
        boolean testE = false;
        boolean testN = false;
        boolean testS = false;
        if(gridSquare.x < x)
            testW = true;
        if(gridSquare.y < y)
            testN = true;
        if(gridSquare.x > x)
            testE = true;
        if(gridSquare.y > y)
            testS = true;
        if(gridSquare.Properties.Is(IsoFlagType.trans) || Properties.Is(IsoFlagType.trans))
            return false;
        if(z != gridSquare.z)
            if(gridSquare.z > z)
            {
                if(gridSquare.TreatAsSolidFloor())
                    return true;
                if(Properties.Is(IsoFlagType.noStart))
                    return true;
                IsoGridSquare sq = getCell().getGridSquare(x, y, gridSquare.z);
                if(sq == null)
                    return false;
                if(sq.TreatAsSolidFloor())
                    return true;
            } else
            {
                if(TreatAsSolidFloor())
                    return true;
                if(Properties.Is(IsoFlagType.noStart))
                    return true;
                IsoGridSquare sq = getCell().getGridSquare(gridSquare.x, gridSquare.y, z);
                if(sq == null)
                    return false;
                if(sq.TreatAsSolidFloor())
                    return true;
            }
        if(gridSquare.x > x && gridSquare.Properties.Is(IsoFlagType.transparentW))
            return false;
        if(gridSquare.y > y && gridSquare.Properties.Is(IsoFlagType.transparentN))
            return false;
        if(gridSquare.x < x && Properties.Is(IsoFlagType.transparentW))
            return false;
        if(gridSquare.y < y && Properties.Is(IsoFlagType.transparentN))
            return false;
        boolean colN = testN && Properties.Is(IsoFlagType.collideN);
        boolean colW = testW && Properties.Is(IsoFlagType.collideW);
        boolean colS = testS && gridSquare.Properties.Is(IsoFlagType.collideN);
        boolean colE = testE && gridSquare.Properties.Is(IsoFlagType.collideW);
        boolean diag = gridSquare.x != x && gridSquare.y != y;
        boolean colSolid = false;
        if(gridSquare.Properties.Is(IsoFlagType.solid))
            colSolid = true;
        if(diag)
        {
            IsoGridSquare betweenA = getCell().getGridSquare(x, gridSquare.y, z);
            IsoGridSquare betweenB = getCell().getGridSquare(gridSquare.x, y, z);
            if(CalculateVisionBlocked(betweenA))
                return true;
            if(CalculateVisionBlocked(betweenB))
                return true;
            if(gridSquare.CalculateVisionBlocked(betweenA))
                return true;
            if(gridSquare.CalculateVisionBlocked(betweenB))
                return true;
        }
        return colN || colW || colS || colE || colSolid;
    }

    public void DoCachingVisibility()
    {
        CalcVisibility();
    }

    public IsoGameCharacter FindFriend(IsoGameCharacter g, int range, Stack EnemyList)
    {
        Stack zombieList = new Stack();
        for(int n = 0; n < g.getLocalList().size(); n++)
        {
            IsoMovingObject obj = (IsoMovingObject)g.getLocalList().get(n);
            if(obj != g && obj != g.getFollowingTarget() && (obj instanceof IsoGameCharacter) && !(obj instanceof IsoZombie) && !EnemyList.contains((IsoGameCharacter)obj))
                zombieList.add((IsoGameCharacter)obj);
        }

        float lowestDist = 1000000F;
        IsoGameCharacter lowest = null;
        Iterator i$ = zombieList.iterator();
        do
        {
            if(!i$.hasNext())
                break;
            IsoGameCharacter z = (IsoGameCharacter)i$.next();
            float Dist = 0.0F;
            Dist += Math.abs((float)getX() - z.getX());
            Dist += Math.abs((float)getY() - z.getY());
            Dist += Math.abs((float)getZ() - z.getZ());
            if(Dist < lowestDist)
            {
                lowest = z;
                lowestDist = Dist;
            }
            if(z == IsoPlayer.getInstance())
            {
                lowest = z;
                float f = 0.0F;
            }
        } while(true);
        if(lowestDist > (float)range)
            return null;
        else
            return lowest;
    }

    public IsoGameCharacter FindEnemy(IsoGameCharacter g, int range, NulledArrayList EnemyList, IsoGameCharacter RangeTest, int TestRangeMax)
    {
        float lowestDist = 1000000F;
        IsoGameCharacter lowest = null;
        for(int n = 0; n < EnemyList.size(); n++)
        {
            IsoGameCharacter z = (IsoGameCharacter)EnemyList.get(n);
            float Dist = 0.0F;
            Dist += Math.abs((float)getX() - z.getX());
            Dist += Math.abs((float)getY() - z.getY());
            Dist += Math.abs((float)getZ() - z.getZ());
            if(Dist < (float)range && Dist < lowestDist && z.DistTo(RangeTest) < (float)TestRangeMax)
            {
                lowest = z;
                lowestDist = Dist;
            }
        }

        if(lowestDist > (float)range)
            return null;
        else
            return lowest;
    }

    public IsoGameCharacter FindEnemy(IsoGameCharacter g, int range, NulledArrayList EnemyList)
    {
        float lowestDist = 1000000F;
        IsoGameCharacter lowest = null;
        for(int n = 0; n < EnemyList.size(); n++)
        {
            IsoGameCharacter z = (IsoGameCharacter)EnemyList.get(n);
            float Dist = 0.0F;
            Dist += Math.abs((float)getX() - z.getX());
            Dist += Math.abs((float)getY() - z.getY());
            Dist += Math.abs((float)getZ() - z.getZ());
            if(Dist < lowestDist)
            {
                lowest = z;
                lowestDist = Dist;
            }
        }

        if(lowestDist > (float)range)
            return null;
        else
            return lowest;
    }

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }

    public int getZ()
    {
        return z;
    }

    public void RecalcProperties()
    {
        slice.bSaveDirty = true;
        CachedIsFree = false;
        bDirty = true;
        Properties.Clear();
        hasTypes.clear();
        for(int n = 0; n < Objects.size(); n++)
        {
            IsoObject obj = (IsoObject)Objects.get(n);
            hasTypes.set(obj.getType(), true);
            if(obj.sprite != null)
                Properties.AddProperties(obj.sprite.getProperties());
        }

    }

    public void ReCalculateCollide(IsoGridSquare square)
    {
        boolean b = CalculateCollide(square, false, false, false);
        if(b == collideMatrix[1 + (square.x - x)][1 + (square.y - y)][1 + (square.z - z)])
        {
            return;
        } else
        {
            int bit = CollisionMatrixPrototypes.instance.ToBitMatrix(collideMatrix);
            bit = BitMatrix.Set(bit, square.x - x, square.y - y, square.z - z, b);
            collideMatrix = CollisionMatrixPrototypes.instance.Add(bit);
            return;
        }
    }

    public void ReCalculatePathFind(IsoGridSquare square)
    {
        boolean b = CalculateCollide(square, false, true, false);
        if(b == pathMatrix[1 + (square.x - x)][1 + (square.y - y)][1 + (square.z - z)])
        {
            return;
        } else
        {
            int bit = CollisionMatrixPrototypes.instance.ToBitMatrix(pathMatrix);
            bit = BitMatrix.Set(bit, square.x - x, square.y - y, square.z - z, b);
            pathMatrix = CollisionMatrixPrototypes.instance.Add(bit);
            return;
        }
    }

    public void ReCalculateVisionBlocked(IsoGridSquare square)
    {
        boolean b = CalculateVisionBlocked(square);
        if(b == visionMatrix[1 + (square.x - x)][1 + (square.y - y)][1 + (square.z - z)])
        {
            return;
        } else
        {
            int bit = CollisionMatrixPrototypes.instance.ToBitMatrix(visionMatrix);
            bit = BitMatrix.Set(bit, square.x - x, square.y - y, square.z - z, b);
            visionMatrix = CollisionMatrixPrototypes.instance.Add(bit);
            return;
        }
    }

    public boolean testCollideAdjacent(IsoMovingObject collideObject, int x, int y, int z)
    {
        if(x < -1 || x > 1 || y < -1 || y > 1 || z < -1 || z > 1)
            return true;
        int n;
        if(collideObject == IsoCamera.CamCharacter)
            n = 0;
        IsoGridSquare sq = getCell().getGridSquare(this.x + x, this.y + y, this.z + z);
        if(sq != null && collideObject != null)
        {
            if(!sq.SpecialObjects.isEmpty())
            {
                for(n = 0; n < sq.SpecialObjects.size(); n++)
                {
                    IsoObject obj = (IsoObject)sq.SpecialObjects.get(n);
                    if(obj.TestCollide(collideObject, this, sq))
                    {
                        if(obj instanceof IsoDoor)
                            collideObject.setCollidedWithDoor(true);
                        int d;
                        if(collideObject == IsoCamera.CamCharacter)
                            d = 0;
                        collideObject.setCollidedObject(obj);
                        return true;
                    }
                }

            }
            if(!SpecialObjects.isEmpty())
            {
                for(n = 0; n < SpecialObjects.size(); n++)
                {
                    IsoObject obj = (IsoObject)SpecialObjects.get(n);
                    if(obj.TestCollide(collideObject, this, sq))
                    {
                        if(obj instanceof IsoDoor)
                            collideObject.setCollidedWithDoor(true);
                        int d;
                        if(collideObject == IsoCamera.CamCharacter)
                            d = 0;
                        collideObject.setCollidedObject(obj);
                        return true;
                    }
                }

            }
        }
        if(UseSlowCollision)
        {
            int gfdsfd;
            if(collideObject == IsoCamera.CamCharacter)
                gfdsfd = 0;
            return CalculateCollide(sq, false, false, false);
        } else
        {
            return collideMatrix[x + 1][y + 1][z + 1];
        }
    }

    public static void setCollisionMode()
    {
        UseSlowCollision = !UseSlowCollision;
    }

    public boolean testPathFindAdjacent(IsoMovingObject mover, int x, int y, int z)
    {
        if(x < -1 || x > 1 || y < -1 || y > 1 || z < -1 || z > 1)
            return true;
        if(pathWithDoorsInvalidated)
        {
            pathWithDoorsInvalidated = false;
            int pathWDoors = 0;
            for(int xx = -1; xx <= 1; xx++)
            {
                for(int yy = -1; yy <= 1; yy++)
                {
                    for(int zz = -1; zz <= 1; zz++)
                    {
                        pathWDoors = BitMatrix.Set(pathWDoors, xx, yy, zz, false);
                        if(z == 0 && zz < 0 || this.z + zz < 0)
                            continue;
                        IsoGridSquare sq = getCell().getGridSquare(this.x + xx, this.y + yy, this.z + zz);
                        if(sq != null && getDoorFrameTo(sq) != null && xx != 0 && yy != 0)
                        {
                            pathWDoors = BitMatrix.Set(pathWDoors, xx, yy, zz, true);
                            continue;
                        }
                        if(sq == null)
                            continue;
                        if(!sq.SpecialObjects.isEmpty())
                        {
                            for(int n = 0; n < sq.SpecialObjects.size(); n++)
                            {
                                IsoObject obj = (IsoObject)sq.SpecialObjects.get(n);
                                if(obj.TestPathfindCollide(mover, this, sq))
                                    pathWDoors = BitMatrix.Set(pathWDoors, xx, yy, zz, true);
                            }

                        }
                        if(SpecialObjects.isEmpty())
                            continue;
                        for(int n = 0; n < SpecialObjects.size(); n++)
                        {
                            IsoObject obj = (IsoObject)SpecialObjects.get(n);
                            if(obj.TestPathfindCollide(mover, this, sq))
                                pathWDoors = BitMatrix.Set(pathWDoors, xx, yy, zz, true);
                        }

                    }

                }

            }

            pathWithDoorsMatrix = CollisionMatrixPrototypes.instance.Add(pathWDoors);
        }
        if(bStairsTN || bStairsTW)
        {
            IsoGridSquare gridSquare = getCell().getGridSquare(x + this.x, y + this.y, z + this.z);
            if(gridSquare == null)
                return true;
            if(bStairsTN && gridSquare.y < this.y && gridSquare.z == this.z)
                return true;
            if(bStairsTW && gridSquare.x < this.x && gridSquare.z == this.z)
                return true;
        }
        if(bDoSlowPathfinding)
        {
            IsoGridSquare gridSquare = getCell().getGridSquare(x + this.x, y + this.y, z + this.z);
            return CalculateCollide(gridSquare, false, true, false);
        } else
        {
            return pathMatrix[x + 1][y + 1][z + 1] || pathWithDoorsMatrix[x + 1][y + 1][z + 1];
        }
    }

    public LosUtil.TestResults testVisionAdjacent(int x, int y, int z, boolean specialDiag, boolean bIgnoreDoors)
    {
        if(x < -1 || x > 1 || y < -1 || y > 1 || z < -1 || z > 1)
            return LosUtil.TestResults.Blocked;
        LosUtil.TestResults test = LosUtil.TestResults.Clear;
        if(x != 0 && y != 0 && specialDiag)
        {
            test = DoDiagnalCheck(x, y, z, bIgnoreDoors);
            if(test == LosUtil.TestResults.Clear)
            {
                IsoGridSquare sq = getCell().getGridSquare(this.x + x, this.y + y, this.z + z);
                if(sq != null)
                    test = sq.DoDiagnalCheck(-x, -y, -z, bIgnoreDoors);
            }
        } else
        {
            IsoGridSquare sq = getCell().getGridSquare(this.x + x, this.y + y, this.z + z);
            LosUtil.TestResults ret = LosUtil.TestResults.Clear;
            if(sq != null && sq.z == this.z)
            {
                if(!SpecialObjects.isEmpty())
                {
                    for(int n = 0; n < SpecialObjects.size(); n++)
                    {
                        IsoObject obj = (IsoObject)SpecialObjects.get(n);
                        IsoObject.VisionResult vis = obj.TestVision(this, sq);
                        if(vis == IsoObject.VisionResult.NoEffect)
                            continue;
                        if(vis == IsoObject.VisionResult.Unblocked && (obj instanceof IsoDoor))
                        {
                            ret = LosUtil.TestResults.ClearThroughOpenDoor;
                            continue;
                        }
                        if(vis == IsoObject.VisionResult.Unblocked && (obj instanceof IsoWindow))
                        {
                            ret = LosUtil.TestResults.ClearThroughWindow;
                            continue;
                        }
                        if(vis == IsoObject.VisionResult.Blocked && (obj instanceof IsoDoor) && !bIgnoreDoors)
                            return LosUtil.TestResults.Blocked;
                        if(vis == IsoObject.VisionResult.Blocked && (obj instanceof IsoCurtain))
                            return LosUtil.TestResults.Blocked;
                        if(vis == IsoObject.VisionResult.Blocked && (obj instanceof IsoWindow))
                            return LosUtil.TestResults.Blocked;
                        if(vis == IsoObject.VisionResult.Blocked && (obj instanceof IsoBarricade))
                            return LosUtil.TestResults.Blocked;
                    }

                }
                if(!sq.SpecialObjects.isEmpty())
                {
                    for(int n = 0; n < sq.SpecialObjects.size(); n++)
                    {
                        IsoObject obj = (IsoObject)sq.SpecialObjects.get(n);
                        IsoObject.VisionResult vis = obj.TestVision(this, sq);
                        if(vis == IsoObject.VisionResult.NoEffect)
                            continue;
                        if(vis == IsoObject.VisionResult.Unblocked && (obj instanceof IsoDoor))
                        {
                            ret = LosUtil.TestResults.ClearThroughOpenDoor;
                            continue;
                        }
                        if(vis == IsoObject.VisionResult.Unblocked && (obj instanceof IsoWindow))
                        {
                            ret = LosUtil.TestResults.ClearThroughWindow;
                            continue;
                        }
                        if(vis == IsoObject.VisionResult.Blocked && (obj instanceof IsoDoor) && !bIgnoreDoors)
                            return LosUtil.TestResults.Blocked;
                        if(vis == IsoObject.VisionResult.Blocked && (obj instanceof IsoCurtain))
                            return LosUtil.TestResults.Blocked;
                        if(vis == IsoObject.VisionResult.Blocked && (obj instanceof IsoWindow))
                            return LosUtil.TestResults.Blocked;
                        if(vis == IsoObject.VisionResult.Blocked && (obj instanceof IsoBarricade))
                            return LosUtil.TestResults.Blocked;
                    }

                }
            }
            test = ret;
            return visionMatrix[x + 1][y + 1][z + 1] ? LosUtil.TestResults.Blocked : test;
        }
        return test;
    }

    public boolean TreatAsSolidFloor()
    {
        if(SolidFloorCached)
            return SolidFloor;
        if(Properties.Is(IsoFlagType.solidfloor) || Has(IsoObjectType.stairsBN) || Has(IsoObjectType.stairsTN) || Has(IsoObjectType.stairsMN) || Has(IsoObjectType.stairsBW) || Has(IsoObjectType.stairsMW) || Has(IsoObjectType.stairsTW))
            SolidFloor = true;
        else
            SolidFloor = false;
        SolidFloorCached = true;
        return SolidFloor;
    }

    public void AddSpecialTileObject(IsoObject obj)
    {
        SpecialObjects.add(obj);
        Objects.add(obj);
    }

    public static void bubbleSort3(ArrayList x)
    {
        int n = x.size();
        for(boolean doMore = true; doMore;)
        {
            n--;
            doMore = false;
            int i = 0;
            while(i < n) 
            {
                IsoMovingObject a = (IsoMovingObject)x.get(i);
                IsoMovingObject b = (IsoMovingObject)x.get(i + 1);
                if(a.compareToY(b) == 1)
                {
                    x.set(i, b);
                    x.set(i + 1, a);
                    doMore = true;
                }
                i++;
            }
        }

    }

    void renderCharacters(int maxZ)
    {
        if(z >= maxZ)
            return;
        if(!isOnScreenLast)
            return;
        IndieGL.glBlendFunc(770, 771);
        int n;
        if(!DeferedCharacters.isEmpty())
        {
            for(n = 0; n < DeferedCharacters.size(); n++)
            {
                ((IsoGameCharacter)DeferedCharacters.get(n)).setbDoDefer(false);
                ((IsoGameCharacter)DeferedCharacters.get(n)).render(((IsoGameCharacter)DeferedCharacters.get(n)).getX(), ((IsoGameCharacter)DeferedCharacters.get(n)).getY(), ((IsoGameCharacter)DeferedCharacters.get(n)).getZ(), lightInfo, true);
                ((IsoGameCharacter)DeferedCharacters.get(n)).renderObjectPicker(((IsoGameCharacter)DeferedCharacters.get(n)).getX(), ((IsoGameCharacter)DeferedCharacters.get(n)).getY(), ((IsoGameCharacter)DeferedCharacters.get(n)).getZ(), lightInfo);
                ((IsoGameCharacter)DeferedCharacters.get(n)).setbDoDefer(true);
            }

            DeferedCharacters.clear();
        }
        if(comp == null)
            comp = new Comparator() {

                public int compare(Object a, Object b)
                {
                    return ((IsoMovingObject)a).compareToY((IsoMovingObject)b);
                }

              

            
         
            };

        if(!MovingObjects.isEmpty() || !StaticMovingObjects.isEmpty())
            IndieGL.End();
        
        if(MovingObjects.size() > 1)
        {
            Iterator it = MovingObjects.iterator();
            boolean bDoIt = false;
            float cur;
            for(float last = -1000F; it != null && it.hasNext(); last = cur)
            {
                cur = ((IsoMovingObject)it.next()).sprite.lsy;
                if(cur < last)
                    bDoIt = true;
            }

            if(bDoIt)
                bubbleSort3(MovingObjects.list);
        } else
        {
            
        }
        
        for(int it = 0; it < StaticMovingObjects.size(); it++)
        {
            IsoMovingObject mov = (IsoMovingObject)StaticMovingObjects.get(it);
            mov.render(mov.getX(), mov.getY(), mov.getZ(), lightInfo, true);
            mov.renderObjectPicker(mov.getX(), mov.getY(), mov.getZ(), lightInfo);
        }

        for(int it = 0; it < MovingObjects.size(); it++)
        {
            IsoMovingObject mov = (IsoMovingObject)MovingObjects.get(it);
            if(mov == null)
            {
                MovingObjects.remove(it);
                it--;
            } else
            {
                if(mov != IsoCamera.CamCharacter);
                mov.render(mov.getX(), mov.getY(), mov.getZ(), lightInfo, true);
                mov.renderObjectPicker(mov.getX(), mov.getY(), mov.getZ(), lightInfo);
            }
        }

    }

    public boolean IsOnScreen()
    {
        float sx = IsoUtils.XToScreen(x, y, z, 0);
        float sy = IsoUtils.YToScreen(x, y, z, 0);
        sx = (int)sx;
        sy = (int)sy;
        sx -= (int)IsoCamera.getOffX();
        sy -= (int)IsoCamera.getOffY();
        sy += 128F;
        sy += 128F;
        if(sx + 64F < -32F)
            return false;
        if(sy + 128F < -32F)
            return false;
        if(sx > (float)(Core.getInstance().getOffscreenWidth() + 32))
            return false;
        return sy <= (float)(Core.getInstance().getOffscreenHeight() + 32);
    }

    void renderFloor(int xx, int yy)
    {
        if(!IsOnScreen())
            return;
        try
        {
            Iterator it = Objects.iterator();
            do
            {
                if(it == null || !it.hasNext())
                    break;
                IsoObject obj = (IsoObject)it.next();
                boolean bDoIt = true;
                if(obj.sprite != null && !obj.sprite.Properties.Is(IsoFlagType.solidfloor))
                    bDoIt = false;
                if(bDoIt)
                {
                    IndieGL.glAlphaFunc(516, 0.0F);
                    if(Core.getInstance().nGraphicLevel >= 3 && ((float)z <= IsoCamera.CamCharacter.z || Core.getInstance().nGraphicLevel >= 4))
                        obj.render(x, y, z, defColorInfo, true);
                    else
                        obj.render(x, y, z, lightInfo, true);
                    obj.renderObjectPicker(x, y, z, lightInfo);
                }
            } while(true);
        }
        catch(Exception ex)
        {
            IndieGL.End();
            Logger.getLogger(GameApplet.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(IsoPlayer.getInstance().getGuardModeUI() == 1 && UIManager.getPicked() != null && UIManager.getPicked().tile.square.isFree(false) && UIManager.getPicked().tile.square == this)
            IsoPlayer.getInstance().getGuardModeUISprite().render(null, x, y, z, IsoDirections.N, 0.0F, -144F, defColorInfo);
        if(IsoPlayer.getInstance().getGuardModeUI() == 2 && UIManager.getPicked() != null && UIManager.getPicked().tile.square == this)
            IsoPlayer.getInstance().getGuardModeUISprite().render(null, x, y, z, IsoDirections.N, 0.0F, -144F, defColorInfo);
    }

    void renderMinusFloor(int ay, int ax, int maxZ)
    {
        if(!IsOnScreen())
            return;
        IndieGL.glBlendFunc(770, 771);
        int stenciled = z * 3 + ax;
        int n;
        if(stenciled >= 255)
            n = 0;
        isOnScreenLast = IsOnScreen();
        lightInfo.a = 1.0F;
        defColorInfo.r = 1.0F;
        defColorInfo.g = 1.0F;
        defColorInfo.b = 1.0F;
        defColorInfo.a = 1.0F;
        try
        {
            Iterator it = Objects.iterator();
            do
            {
                if(it == null || !it.hasNext())
                    break;
                IsoObject obj = (IsoObject)it.next();
                boolean bDoIt = true;
                CircleStencil = false;
                if(obj.sprite != null && obj.sprite.getProperties().Is(IsoFlagType.solidfloor))
                    bDoIt = false;
                if(z >= maxZ && (obj.sprite == null || !obj.sprite.Properties.Is(IsoFlagType.alwaysDraw)))
                    bDoIt = false;
                if(z == maxZ - 1 && obj.sprite != null && obj.sprite.getProperties().Is("WestRoofB"))
                    bDoIt = false;
                if(bDoIt)
                {
                    IndieGL.glAlphaFunc(516, 0.0F);
                    if(obj.sprite != null && (obj.sprite.getType() == IsoObjectType.doorFrW || obj.sprite.getType() == IsoObjectType.doorFrN || obj.sprite.getType() == IsoObjectType.doorW || obj.sprite.getType() == IsoObjectType.doorN || obj.sprite.getProperties().Is(IsoFlagType.cutW) || obj.sprite.getProperties().Is(IsoFlagType.cutN)) && getCell().getVertLights() != null && Core.getInstance().nGraphicLevel >= 3 && ((float)z <= IsoCamera.CamCharacter.z || getRoom() == null || !Properties.Is(IsoFlagType.solidfloor)))
                    {
                        if(obj.targetAlpha < 1.0F)
                        {
                            CircleStencil = true;
                            obj.targetAlpha = 1.0F;
                            obj.alpha = 1.0F;
                        }
                        if(obj.sprite.getType() != IsoObjectType.WestRoofB && obj.sprite.getType() != IsoObjectType.WestRoofM)
                            if(obj.sprite.getType() != IsoObjectType.WestRoofT);
                        if(obj.sprite.getProperties().Is(IsoFlagType.cutW) && obj.sprite.getProperties().Is(IsoFlagType.cutN))
                        {
                            GameProfiler.instance.Start("RenderWallLighting");
                            stenciled = DoWallLightingNW(ax, ay, obj, stenciled, CircleStencil);
                            GameProfiler.instance.End("RenderWallLighting");
                        } else
                        if(obj.sprite.getType() == IsoObjectType.doorFrW || obj.sprite.getType() == IsoObjectType.doorW || obj.sprite.getProperties().Is(IsoFlagType.cutW))
                        {
                            GameProfiler.instance.Start("RenderWallLighting");
                            stenciled = DoWallLightingW(obj, stenciled, ax, ay);
                            GameProfiler.instance.End("RenderWallLighting");
                        } else
                        if(obj.sprite.getType() == IsoObjectType.doorFrN || obj.sprite.getType() == IsoObjectType.doorN || obj.sprite.getProperties().Is(IsoFlagType.cutN))
                        {
                            GameProfiler.instance.Start("RenderWallLighting");
                            stenciled = DoWallLightingN(obj, stenciled, ax, ay);
                            GameProfiler.instance.End("RenderWallLighting");
                        }
                    } else
                    {
                        obj.render(x, y, z, lightInfo, true);
                    }
                    if(obj.sprite != null)
                        obj.renderObjectPicker(x, y, z, lightInfo);
                }
            } while(true);
        }
        catch(Exception ex)
        {
            IndieGL.End();
            Logger.getLogger(FrameLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void RereouteWallMaskTo(IsoObject obj)
    {
        for(int n = 0; n < Objects.size(); n++)
        {
            IsoObject objTest = (IsoObject)Objects.get(n);
            if(objTest.sprite.getProperties().Is(IsoFlagType.collideW) || objTest.sprite.getProperties().Is(IsoFlagType.collideN))
                objTest.rerouteMask = obj;
        }

    }

    void setBlockedGridPointers()
    {
        w = getCell().getGridSquare(x - 1, y, z);
        e = getCell().getGridSquare(x + 1, y, z);
        s = getCell().getGridSquare(x, y + 1, z);
        n = getCell().getGridSquare(x, y - 1, z);
        ne = getCell().getGridSquare(x + 1, y - 1, z);
        nw = getCell().getGridSquare(x - 1, y - 1, z);
        se = getCell().getGridSquare(x + 1, y + 1, z);
        sw = getCell().getGridSquare(x - 1, y + 1, z);
    }

    BlockInfo testAdjacentRoomTransition(int x, int y, int z)
    {
        blockInfo.ThroughDoor = false;
        blockInfo.ThroughWindow = false;
        blockInfo.ThroughStairs = false;
        if(x < -1 || x > 1 || y < -1 || y > 1 || z < -1 || z > 1)
            return blockInfo;
        int lx = this.x;
        int ly = this.y;
        int lz = this.z;
        x = this.x + x;
        y = this.y + y;
        z = this.z + z;
        IsoGridSquare lg = getCell().getGridSquare(lx, ly, lz);
        IsoGridSquare g = getCell().getGridSquare(x, y, z);
        if(g == null)
            return blockInfo;
        if(lx < x)
        {
            if(g.Has(IsoObjectType.doorFrW) || g.Properties.Is(IsoFlagType.doorW))
                blockInfo.ThroughDoor = true;
            if(g.Properties.Is(IsoFlagType.windowW))
                blockInfo.ThroughWindow = true;
        }
        if(lx > x)
        {
            if(lg.Has(IsoObjectType.doorFrW) || lg.Properties.Is(IsoFlagType.doorW))
                blockInfo.ThroughDoor = true;
            if(lg.Properties.Is(IsoFlagType.windowW))
                blockInfo.ThroughWindow = true;
        }
        if(ly < y)
        {
            if(g.Has(IsoObjectType.doorFrN) || g.Properties.Is(IsoFlagType.doorN))
                blockInfo.ThroughDoor = true;
            if(g.Properties.Is(IsoFlagType.windowN))
                blockInfo.ThroughWindow = true;
        }
        if(ly > y)
        {
            if(lg.Has(IsoObjectType.doorFrN) || lg.Properties.Is(IsoFlagType.doorN))
                blockInfo.ThroughDoor = true;
            if(lg.Properties.Is(IsoFlagType.windowN))
                blockInfo.ThroughWindow = true;
        }
        if(lz > z && g.Has(IsoObjectType.stairsTN))
            blockInfo.ThroughStairs = true;
        if(lz < z && lg.Has(IsoObjectType.stairsTN))
            blockInfo.ThroughStairs = true;
        return blockInfo;
    }

    public void CalcLightInfo()
    {
        float dist = IsoUtils.DistanceManhatten(IsoCamera.CamCharacter.getX(), IsoCamera.CamCharacter.getY(), x, y, IsoCamera.CamCharacter.getZ(), z);
        if(this == IsoCamera.CamCharacter.getCurrentSquare())
            dist = 0.0F;
        if(!IsoPlayer.DemoMode);
        float val = dist;
        if(IsoPlayer.getInstance().HasTrait("ShortSighted"))
            val *= 1.5F;
        if(val > GameTime.getInstance().getViewDistMax())
            val = GameTime.getInstance().getViewDistMax();
        val = 1.0F - val / GameTime.getInstance().getViewDist();
        if(val < 0.0F)
            val = 0.0F;
        if(val > 1.0F)
            val = 1.0F;
        if(val < GameTime.getInstance().getAmbient())
            val = GameTime.getInstance().getAmbient();
        IsoGridSquare _tmp = this;
        
        float darkStep = this.darkStep;
        
        if(this == IsoCamera.CamCharacter.getCurrentSquare())
            darkStep = 10F;
        if(darkMulti < targetDarkMulti && dist < 20F && IsoPlayer.getInstance().getTorchStrength() > 0.0F)
            darkStep *= 5F;
        if(darkMulti > targetDarkMulti && dist < 20F && IsoPlayer.getInstance().getTorchStrength() > 0.0F)
            darkStep *= 4F;
        if(IsoPlayer.getInstance().HasTrait("ShortSighted"))
            darkStep *= 0.7F;
        if(dist <= GameTime.getInstance().getViewDistMax() + 10F && (IsoCamera.CamCharacter.getCurrentSquare() == this || IsoCamera.CamCharacter.getCurrentSquare() != IsoCamera.CamCharacter.getLastSquare() || RecalcLightTime < 0 || IsoCamera.CamCharacter.dir != IsoCamera.CamCharacter.getLastdir() || IsoPlayer.getInstance().getMoodles().getMoodleLevel(MoodleType.Drunk) > 0 || IsoPlayer.getInstance().angle.x != IsoPlayer.getInstance().lastAngle.x || IsoPlayer.getInstance().angle.y != IsoPlayer.getInstance().lastAngle.y || IsoPlayer.getInstance().JustMoved))
            CalcVisibility();
        if(val > 1.0F)
            val = 1.0F;
        if(darkMulti < targetDarkMulti)
        {
            darkMulti += darkStep;
            if(darkMulti > targetDarkMulti)
                darkMulti = targetDarkMulti;
        } else
        if(darkMulti > targetDarkMulti)
        {
            darkMulti -= darkStep;
            if(darkMulti < targetDarkMulti)
                darkMulti = targetDarkMulti;
        }
        float multiUse = darkMulti;
        if(dist < 576F)
        {
            int div = 1;
            if(w != null && w.room == room && w.darkMulti > darkMulti)
            {
                if(w.bSeen)
                    multiUse += w.darkMulti;
                div++;
            }
            if(n != null && n.room == room && n.darkMulti > darkMulti)
            {
                if(n.bSeen)
                    multiUse += n.darkMulti;
                div++;
            }
            if(e != null && e.room == room && e.darkMulti > darkMulti)
            {
                if(e.bSeen)
                    multiUse += e.darkMulti;
                div++;
            }
            if(s != null && s.room == room && s.darkMulti > darkMulti)
            {
                if(s.bSeen)
                    multiUse += s.darkMulti;
                div++;
            }
            multiUse /= div;
        }
        lightInfo.r = multiUse;
        lightInfo.g = multiUse;
        lightInfo.b = multiUse;
        if(rmod < 0.0F)
            rmod = 0.0F;
        if(gmod < 0.0F)
            gmod = 0.0F;
        if(bmod < 0.0F)
            bmod = 0.0F;
        lightInfo.r *= val * rmod;
        lightInfo.g *= val * gmod;
        lightInfo.b *= val * bmod;
        if(lampostTotalR > 0.0F || lampostTotalG > 0.0F || lampostTotalB > 0.0F)
        {
            lightInfo.r += lampostTotalR;
            lightInfo.g += lampostTotalG;
            lightInfo.b += lampostTotalB;
        }
        if(bSeen)
            IsoFireManager.LightTileWithFire(this);
        if(lightInfo.r != lastLightInfo.r || lightInfo.g != lastLightInfo.g || lightInfo.b != lastLightInfo.b)
            bLightDirty = true;
    }

    public void StartFire()
    {
        IsoFireManager.StartFire(getCell(), this, true, 0x186a0);
    }

    private void CalcVisibility()
    {
        if(IsoCamera.CamCharacter == null)
            return;
        int n;
        if(x == 38 && y == 24 && z == 0)
            n = 0;
        bCanSee = false;
        isTorchDot = false;
        tempo.x = (float)x + 0.5F;
        tempo.y = (float)y + 0.5F;
        tempo2.x = IsoCamera.CamCharacter.getX();
        tempo2.y = IsoCamera.CamCharacter.getY();
        tempo2.x -= tempo.x;
        tempo2.y -= tempo.y;
        Vector2 dir = IsoCamera.CamCharacter.getVectorFromDirection();
        float dist = tempo2.getLength();
        tempo2.normalize();
        dir.x = IsoCamera.CamCharacter.getAngle().x;
        dir.y = IsoCamera.CamCharacter.getAngle().y;
        dir.normalize();
        if(IsoPlayer.getInstance().getMoodles().getMoodleLevel(MoodleType.Drunk) > 0)
        {
            float WobbleFactor = 0.0F;
            if(IsoPlayer.getInstance().getMoodles().getMoodleLevel(MoodleType.Drunk) == 2)
                WobbleFactor = 0.03F;
            if(IsoPlayer.getInstance().getMoodles().getMoodleLevel(MoodleType.Drunk) == 3)
                WobbleFactor = 0.04F;
            if(IsoPlayer.getInstance().getMoodles().getMoodleLevel(MoodleType.Drunk) == 4)
                WobbleFactor = 0.06F;
            dir.x += IsoPlayer.getInstance().getDrunkSin() * WobbleFactor;
            dir.y += IsoPlayer.getInstance().getDrunkCos() * WobbleFactor;
            if(WobbleFactor > 0.0F)
            {
                dir.x += (IsoPlayer.getInstance().getDrunkCos2() + 0.2F) * WobbleFactor;
                dir.y += (IsoPlayer.getInstance().getDrunkCos2() + 0.2F) * WobbleFactor;
            }
            dir.normalize();
        }
        float dot = tempo2.dot(dir);
        if(dot < -0.95F || IsoCamera.CamCharacter.getCurrentSquare() == this)
            isTorchDot = true;
        if(IsoCamera.CamCharacter.getCurrentSquare() == this)
            dot = -1F;
        float tireddel = IsoCamera.CamCharacter.getStats().fatigue - 0.6F;
        if(tireddel < 0.0F)
            tireddel = 0.0F;
        tireddel *= 2.5F;
        if(IsoCamera.CamCharacter.HasTrait("HardOfHearing") && tireddel < 0.7F)
            tireddel = 0.7F;
        float ndist = 2.0F;
        if(IsoPlayer.getInstance().HasTrait("KeenHearing"))
            ndist += 3F;
        if(dist < ndist * (1.0F - tireddel))
            dot = -1F;
        LosUtil.TestResults test = LosUtil.lineClear(getCell(), x, y, z, (int)IsoCamera.CamCharacter.getX(), (int)IsoCamera.CamCharacter.getY(), (int)IsoCamera.CamCharacter.getZ(), false);
        float cone = -0.2F;
        cone -= IsoCamera.CamCharacter.getStats().fatigue - 0.6F;
        if(cone > -0.2F)
            cone = -0.2F;
        if(IsoCamera.CamCharacter.getStats().fatigue >= 1.0F)
            cone -= 0.2F;
        if(IsoPlayer.getInstance().getMoodles().getMoodleLevel(MoodleType.Panic) == 4)
            cone -= 0.2F;
        if(cone < -0.9F)
            cone = -0.9F;
        if(IsoPlayer.getInstance().HasTrait("EagleEyed"))
            cone += 0.2F;
        if(dot > cone || test == LosUtil.TestResults.Blocked)
        {
            if(test == LosUtil.TestResults.Blocked)
                bCouldSee = false;
            else
                bCouldSee = true;
            if(bSeen)
                targetDarkMulti = GameTime.getInstance().getAmbient();
            else
                targetDarkMulti = 0.0F;
        } else
        {
            bCouldSee = true;
            bCanSee = true;
            bSeen = true;
            targetDarkMulti = 1.0F;
        }
        if(room == null || IsoCamera.CamCharacter.getCurrentSquare().room != room)
            targetDarkMulti *= 1.0F - GameTime.getInstance().getNightTint() * 0.4F;
        if(bCanSee && isTorchDot && dist < 30F && z == (int)IsoCamera.CamCharacter.getZ() || this == IsoCamera.CamCharacter.getCurrentSquare())
            targetDarkMulti += IsoPlayer.getInstance().getTorchStrength() * 1.0F * (1.0F - dist / 30F);
    }

    private LosUtil.TestResults DoDiagnalCheck(int x, int y, int z, boolean bIgnoreDoors)
    {
        LosUtil.TestResults res = testVisionAdjacent(x, 0, z, false, bIgnoreDoors);
        LosUtil.TestResults res2 = testVisionAdjacent(0, y, z, false, bIgnoreDoors);
        if(res == LosUtil.TestResults.Blocked || res2 == LosUtil.TestResults.Blocked)
            return LosUtil.TestResults.Blocked;
        if(res == LosUtil.TestResults.ClearThroughWindow || res2 == LosUtil.TestResults.ClearThroughWindow)
            return LosUtil.TestResults.ClearThroughWindow;
        else
            return testVisionAdjacent(x, y, z, false, bIgnoreDoors);
    }

    boolean HasNoCharacters()
    {
        for(int n = 0; n < MovingObjects.size(); n++)
            if(MovingObjects.get(n) instanceof IsoGameCharacter)
                return false;

        for(int n = 0; n < SpecialObjects.size(); n++)
            if(SpecialObjects.get(n) instanceof IsoBarricade)
                return false;

        return true;
    }

    public static int getFireRecalc()
    {
        return FireRecalc;
    }

    public static void setFireRecalc(int aFireRecalc)
    {
        FireRecalc = aFireRecalc;
    }

    public static float getDarkStep()
    {
        return darkStep;
    }

    public static void setDarkStep(float aDarkStep)
    {
        darkStep = aDarkStep;
    }

    public static BlockInfo getBlockInfo()
    {
        return blockInfo;
    }

    public static void setBlockInfo(BlockInfo aBlockInfo)
    {
        blockInfo = aBlockInfo;
    }

    public static int getRecalcLightTime()
    {
        return RecalcLightTime;
    }

    public static void setRecalcLightTime(int aRecalcLightTime)
    {
        RecalcLightTime = aRecalcLightTime;
    }

    public static int getLightcache()
    {
        return lightcache;
    }

    public static void setLightcache(int aLightcache)
    {
        lightcache = aLightcache;
    }

    public boolean isCouldSee()
    {
        return bCouldSee;
    }

    public void setCouldSee(boolean bCouldSee)
    {
        this.bCouldSee = bCouldSee;
    }

    public boolean isCanSee()
    {
        return bCanSee;
    }

    public void setCanSee(boolean canSee)
    {
        bCanSee = canSee;
    }

    public IsoCell getCell()
    {
        return IsoWorld.instance.CurrentCell;
    }

    public IsoGridSquare getE()
    {
        return e;
    }

    public void setE(IsoGridSquare e)
    {
        this.e = e;
    }

    public NulledArrayList getLightInfluenceB()
    {
        return LightInfluenceB;
    }

    public void setLightInfluenceB(NulledArrayList LightInfluenceB)
    {
        this.LightInfluenceB = LightInfluenceB;
    }

    public NulledArrayList getLightInfluenceG()
    {
        return LightInfluenceG;
    }

    public void setLightInfluenceG(NulledArrayList LightInfluenceG)
    {
        this.LightInfluenceG = LightInfluenceG;
    }

    public NulledArrayList getLightInfluenceR()
    {
        return LightInfluenceR;
    }

    public void setLightInfluenceR(NulledArrayList LightInfluenceR)
    {
        this.LightInfluenceR = LightInfluenceR;
    }

    public ColorInfo getLightInfo()
    {
        return lightInfo;
    }

    public void setLightInfo(ColorInfo lightInfo)
    {
        this.lightInfo = lightInfo;
    }

    public NulledArrayList getStaticMovingObjects()
    {
        return StaticMovingObjects;
    }

    public void setStaticMovingObjects(NulledArrayList StaticMovingObjects)
    {
        this.StaticMovingObjects = StaticMovingObjects;
    }

    public NulledArrayList getMovingObjects()
    {
        return MovingObjects;
    }

    public void setMovingObjects(NulledArrayList MovingObjects)
    {
        this.MovingObjects = MovingObjects;
    }

    public IsoGridSquare getN()
    {
        return n;
    }

    public void setN(IsoGridSquare n)
    {
        this.n = n;
    }

    public NulledArrayList getObjects()
    {
        return Objects;
    }

    public void setObjects(NulledArrayList Objects)
    {
        this.Objects = Objects;
    }

    public boolean isPathWithDoorsInvalidated()
    {
        return pathWithDoorsInvalidated;
    }

    public void setPathWithDoorsInvalidated(boolean pathWithDoorsInvalidated)
    {
        this.pathWithDoorsInvalidated = pathWithDoorsInvalidated;
    }

    public PropertyContainer getProperties()
    {
        return Properties;
    }

    public void setProperties(PropertyContainer Properties)
    {
        this.Properties = Properties;
    }

    public IsoRoom getRoom()
    {
        return room;
    }

    public void setRoom(IsoRoom room)
    {
        this.room = room;
    }

    public IsoGridSquare getS()
    {
        return s;
    }

    public void setS(IsoGridSquare s)
    {
        this.s = s;
    }

    public NulledArrayList getSpecialObjects()
    {
        return SpecialObjects;
    }

    public void setSpecialObjects(NulledArrayList SpecialObjects)
    {
        this.SpecialObjects = SpecialObjects;
    }

    public IsoGridSquare getW()
    {
        return w;
    }

    public void setW(IsoGridSquare w)
    {
        this.w = w;
    }

    public float getLampostTotalR()
    {
        return lampostTotalR;
    }

    public void setLampostTotalR(float lampostTotalR)
    {
        this.lampostTotalR = lampostTotalR;
    }

    public float getLampostTotalG()
    {
        return lampostTotalG;
    }

    public void setLampostTotalG(float lampostTotalG)
    {
        this.lampostTotalG = lampostTotalG;
    }

    public float getLampostTotalB()
    {
        return lampostTotalB;
    }

    public void setLampostTotalB(float lampostTotalB)
    {
        this.lampostTotalB = lampostTotalB;
    }

    public boolean isbSeen()
    {
        return bSeen;
    }

    public void setbSeen(boolean bSeen)
    {
        this.bSeen = bSeen;
    }

    public float getDarkMulti()
    {
        return darkMulti;
    }

    public void setDarkMulti(float darkMulti)
    {
        this.darkMulti = darkMulti;
    }

    public NulledArrayList getLights()
    {
        return lights;
    }

    public void setLights(NulledArrayList lights)
    {
        this.lights = lights;
    }

    public SliceY getSlice()
    {
        return slice;
    }

    public void setSlice(SliceY slice)
    {
        this.slice = slice;
    }

    public float getTargetDarkMulti()
    {
        return targetDarkMulti;
    }

    public void setTargetDarkMulti(float targetDarkMulti)
    {
        this.targetDarkMulti = targetDarkMulti;
    }

    public void setX(int x)
    {
        this.x = x;
    }

    public void setY(int y)
    {
        this.y = y;
    }

    public void setZ(int z)
    {
        this.z = z;
    }

    public boolean isHasZombies()
    {
        return hasZombies;
    }

    public void setHasZombies(boolean hasZombies)
    {
        this.hasZombies = hasZombies;
    }

    public NulledArrayList getDeferedCharacters()
    {
        return DeferedCharacters;
    }

    public void setDeferedCharacters(NulledArrayList DeferedCharacters)
    {
        this.DeferedCharacters = DeferedCharacters;
    }

    public boolean isCacheIsFree()
    {
        return CacheIsFree;
    }

    public void setCacheIsFree(boolean CacheIsFree)
    {
        this.CacheIsFree = CacheIsFree;
    }

    public boolean isCachedIsFree()
    {
        return CachedIsFree;
    }

    public void setCachedIsFree(boolean CachedIsFree)
    {
        this.CachedIsFree = CachedIsFree;
    }

    public boolean isbStairsTN()
    {
        return bStairsTN;
    }

    public void setbStairsTN(boolean bStairsTN)
    {
        this.bStairsTN = bStairsTN;
    }

    public boolean isbStairsTW()
    {
        return bStairsTW;
    }

    public void setbStairsTW(boolean bStairsTW)
    {
        this.bStairsTW = bStairsTW;
    }

    public static boolean isbDoSlowPathfinding()
    {
        return bDoSlowPathfinding;
    }

    public static void setbDoSlowPathfinding(boolean abDoSlowPathfinding)
    {
        bDoSlowPathfinding = abDoSlowPathfinding;
    }

    public boolean isSolidFloorCached()
    {
        return SolidFloorCached;
    }

    public void setSolidFloorCached(boolean SolidFloorCached)
    {
        this.SolidFloorCached = SolidFloorCached;
    }

    public boolean isSolidFloor()
    {
        return SolidFloor;
    }

    public void setSolidFloor(boolean SolidFloor)
    {
        this.SolidFloor = SolidFloor;
    }

    public static Comparator getComp()
    {
        return comp;
    }

    public static void setComp(Comparator aComp)
    {
        comp = aComp;
    }

    public static ColorInfo getDefColorInfo()
    {
        return defColorInfo;
    }

    public static void setDefColorInfo(ColorInfo aDefColorInfo)
    {
        defColorInfo = aDefColorInfo;
    }

    private static int FireRecalc = 30;
    private static float darkStep = 0.06F;
    private static BlockInfo blockInfo = new BlockInfo();
    public static int RecalcLightTime = 0;
    private static int lightcache = 0;
    private static IsoSprite ZombieHall;
    private boolean bCouldSee;
    private boolean bCanSee;
    private NulledArrayList LightInfluenceB;
    private NulledArrayList LightInfluenceG;
    private NulledArrayList LightInfluenceR;
    public ColorInfo lightInfo;
    private NulledArrayList StaticMovingObjects;
    private NulledArrayList MovingObjects;
    private NulledArrayList Objects;
    public boolean collideMatrix[][][];
    public boolean pathMatrix[][][];
    public boolean pathWithDoorsMatrix[][][];
    public boolean visionMatrix[][][];
    private boolean pathWithDoorsInvalidated;
    private PropertyContainer Properties;
    public IsoRoom room;
    private NulledArrayList SpecialObjects;
    public IsoGridSquare w;
    public IsoGridSquare nw;
    public IsoGridSquare sw;
    public IsoGridSquare s;
    public IsoGridSquare n;
    public IsoGridSquare ne;
    public IsoGridSquare se;
    public IsoGridSquare e;
    private float lampostTotalR;
    private float lampostTotalG;
    private float lampostTotalB;
    private boolean bSeen;
    private float darkMulti;
    private NulledArrayList lights;
    ZomboidBitFlag hasTypes;
    private SliceY slice;
    private float targetDarkMulti;
    private int x;
    private int y;
    private int z;
    private boolean isTorchDot;
    private boolean hasZombies;
    private NulledArrayList DeferedCharacters;
    public boolean bDirty;
    private boolean CacheIsFree;
    private boolean CachedIsFree;
    public int ID;
    public static int IDMax = -1;
    static Color tr = new Color(1, 1, 1, 1);
    static Color tl = new Color(1, 1, 1, 1);
    static Color br = new Color(1, 1, 1, 1);
    static Color bl = new Color(1, 1, 1, 1);
    static Color interp1 = new Color(1, 1, 1, 1);
    static Color interp2 = new Color(1, 1, 1, 1);
    static Color finalCol = new Color(1, 1, 1, 1);
    public boolean bLightDirty;
    public static boolean UseSlowCollision = false;
    private boolean bStairsTN;
    private boolean bStairsTW;
    private static boolean bDoSlowPathfinding = false;
    private boolean SolidFloorCached;
    private boolean SolidFloor;
    private static Comparator comp;
    public static boolean isOnScreenLast = false;
    private static Texture texWhite;
    private static ColorInfo defColorInfo = new ColorInfo();
    static int colu = 0;
    static int coll = 0;
    static int colr = 0;
    static int colu2 = 0;
    static int coll2 = 0;
    static int colr2 = 0;
    static boolean CircleStencil = false;
    static OnceEvery every = new OnceEvery(0.025F);
    static float rmod = 0.0F;
    static float gmod = 0.0F;
    static float bmod = 0.0F;
    public ColorInfo lastLightInfo;
    static Vector2 tempo = new Vector2();
    static Vector2 tempo2 = new Vector2();

}
