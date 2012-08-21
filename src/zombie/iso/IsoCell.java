// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   IsoCell.java

package zombie.iso;

import gnu.trove.map.hash.THashMap;
import java.io.*;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import se.krka.kahlua.j2se.J2SEPlatform;
import se.krka.kahlua.vm.KahluaTable;
import zombie.*;
import zombie.Lua.*;
import zombie.*;
import zombie.ai.astar.*;
import zombie.characters.*;
import zombie.core.Collections.NulledArrayList;
import zombie.core.*;
import zombie.core.bucket.Bucket;
import zombie.core.bucket.BucketManager;
import zombie.core.properties.PropertyContainer;
import zombie.core.textures.ColorInfo;
import zombie.core.textures.Texture;
import zombie.core.utils.OnceEvery;
import zombie.inventory.InventoryItem;
import zombie.inventory.ItemContainer;
import zombie.iso.SpriteDetails.IsoFlagType;
import zombie.iso.SpriteDetails.IsoObjectType;
import zombie.iso.areas.BuildingScore;
import zombie.iso.areas.IsoBuilding;
import zombie.iso.areas.IsoRoom;
import zombie.iso.areas.IsoRoomExit;
import zombie.iso.objects.IsoBarricade;
import zombie.iso.objects.IsoCrate;
import zombie.iso.objects.IsoCurtain;
import zombie.iso.objects.IsoDeadBody;
import zombie.iso.objects.IsoDoor;
import zombie.iso.objects.IsoWindow;
import zombie.iso.sprite.IsoSprite;
import zombie.iso.sprite.IsoSpriteManager;
import zombie.scripting.ScriptManager;
import zombie.scripting.objects.Zone;
import zombie.ui.TutorialManager;
import zombie.ui.UIManager;

// Referenced classes of package zombie.iso:
//            SliceY, IsoMovingObject, IsoObject, IsoGridSquare, 
//            IsoLot, IsoPushableObject, IsoLightSource, IsoPhysicsObject, 
//            IsoDirections, BlockInfo, IsoWorld, IsoCamera, 
//            IsoUtils, LosUtil, CellLoader, Vector2, 
//            IsoObjectPicker

public class IsoCell extends Bucket
    implements TileBasedMap
{
    public static class Zone
    {

        public int getH()
        {
            return H;
        }

        public void setH(int H)
        {
            this.H = H;
        }

        public boolean isHasHeight()
        {
            return HasHeight;
        }

        public void setHasHeight(boolean HasHeight)
        {
            this.HasHeight = HasHeight;
        }

        public String getName()
        {
            return Name;
        }

        public void setName(String Name)
        {
            this.Name = Name;
        }

        public int getW()
        {
            return W;
        }

        public void setW(int W)
        {
            this.W = W;
        }

        public int getX()
        {
            return X;
        }

        public void setX(int X)
        {
            this.X = X;
        }

        public int getY()
        {
            return Y;
        }

        public void setY(int Y)
        {
            this.Y = Y;
        }

        public int getZ()
        {
            return Z;
        }

        public void setZ(int Z)
        {
            this.Z = Z;
        }

        public int H;
        public boolean HasHeight;
        public String Name;
        public int W;
        public int X;
        public int Y;
        public int Z;

        public Zone(String name, int x, int y, int w, int h, int z)
        {
            HasHeight = false;
            Z = 0;
            Name = name;
            X = x;
            Y = y;
            W = w;
            H = h;
            Z = z;
            HasHeight = true;
        }
    }

    /*      */   public static enum BuildingSearchCriteria
    /*      */   {
    /* 3063 */     Food, 
    /* 3064 */     Defense, 
    /* 3065 */     Wood, 
    /* 3066 */     Weapons, 
    /* 3067 */     General;
    /*      */   }


    public static int getMaxHeight()
    {
        return MaxHeight;
    }

    public static void setMaxHeight(int aMaxHeight)
    {
        MaxHeight = aMaxHeight;
    }

    public static int getBarricadeDoorFrame()
    {
        return BarricadeDoorFrame;
    }

    public static void setBarricadeDoorFrame(int aBarricadeDoorFrame)
    {
        BarricadeDoorFrame = aBarricadeDoorFrame;
    }

    public static int getSheetCurtains()
    {
        return SheetCurtains;
    }

    public static void setSheetCurtains(int aSheetCurtains)
    {
        SheetCurtains = aSheetCurtains;
    }

    public static Stack getBuildings()
    {
        return buildingscores;
    }

    public static void setBuildings(Stack scores)
    {
        buildingscores = scores;
    }

    public IsoCell(int width, int height)
    {
        BuildingList = new NulledArrayList();
        ObjectList = new NulledArrayList();
        PushableObjectList = new NulledArrayList();
        BuildingScores = new THashMap();
        RoomList = new NulledArrayList();
        StaticUpdaterObjectList = new NulledArrayList();
        wallArray = new NulledArrayList();
        ZombieList = new NulledArrayList();
        RemoteSurvivorList = new NulledArrayList();
        GhostList = new NulledArrayList();
        ZoneStack = new NulledArrayList();
        removeList = new NulledArrayList();
        addList = new NulledArrayList();
        RenderJobsArray = new NulledArrayList();
        ProcessItems = new NulledArrayList();
        ProcessItemsRemove = new NulledArrayList();
        RenderJobsMapArray = new THashMap();
        Containers = new Stack();
        ContainerMap = new HashMap();
        safeToAdd = true;
        LamppostPositions = new Stack();
        roomDefs = new NulledArrayList();
        everDone = false;
        recalcFloors = false;
        stairsNodes = new HashSet();
        bDoLotConnect = true;
        SurvivorList = new NulledArrayList();
        jumptot = 0;
        jumpcount = 0;
        jumpavr = 0;
        tempZoneStack = new Stack();
        vertLights = (int[][][][])null;
        buildVertLights = (int[][][][])null;
        currentLX = 0;
        currentLY = 0;
        currentLZ = 0;
        recalcShading = 30;
        lastMinX = 0xffed2979;
        lastMinY = 0xffed2979;
        lightUpdate = new OnceEvery(0.05F);
        alt = 0;
        staticBlack = new Color(0, 0, 0, 0);
        lightingThread = null;
        lightUpdateCount = 0;
        bSwappingLightBuffers = false;
        bRendering = false;
        woodWallN = null;
        woodWallW = null;
        woodDWallN = null;
        woodDWallW = null;
        woodWWallN = null;
        woodWWallW = null;
        woodDoorW = null;
        woodDoorN = null;
        woodFloor = null;
        woodBarricade = null;
        woodCrate = null;
        woodStairsNB = null;
        woodStairsNM = null;
        woodStairsNT = null;
        woodStairsWB = null;
        woodStairsWM = null;
        woodStairsWT = null;
        dangerUpdate = new OnceEvery(0.4F, false);
        LightInfoUpdate = null;
        IsoWorld.instance.CurrentCell = this;
        this.width = width;
        this.height = height;
        slices = new SliceY[height];
        for(int n = 0; n < height; n++)
            slices[n] = new SliceY(this, width, 16, n);

    }

    public IsoCell(IsoSpriteManager spr, int width, int height)
    {
        super(spr);
        BuildingList = new NulledArrayList();
        ObjectList = new NulledArrayList();
        PushableObjectList = new NulledArrayList();
        BuildingScores = new THashMap();
        RoomList = new NulledArrayList();
        StaticUpdaterObjectList = new NulledArrayList();
        wallArray = new NulledArrayList();
        ZombieList = new NulledArrayList();
        RemoteSurvivorList = new NulledArrayList();
        GhostList = new NulledArrayList();
        ZoneStack = new NulledArrayList();
        removeList = new NulledArrayList();
        addList = new NulledArrayList();
        RenderJobsArray = new NulledArrayList();
        ProcessItems = new NulledArrayList();
        ProcessItemsRemove = new NulledArrayList();
        RenderJobsMapArray = new THashMap();
        Containers = new Stack();
        ContainerMap = new HashMap();
        safeToAdd = true;
        LamppostPositions = new Stack();
        roomDefs = new NulledArrayList();
        everDone = false;
        recalcFloors = false;
        stairsNodes = new HashSet();
        bDoLotConnect = true;
        SurvivorList = new NulledArrayList();
        jumptot = 0;
        jumpcount = 0;
        jumpavr = 0;
        tempZoneStack = new Stack();
        vertLights = (int[][][][])null;
        buildVertLights = (int[][][][])null;
        currentLX = 0;
        currentLY = 0;
        currentLZ = 0;
        recalcShading = 30;
        lastMinX = 0xffed2979;
        lastMinY = 0xffed2979;
        lightUpdate = new OnceEvery(0.05F);
        alt = 0;
        staticBlack = new Color(0, 0, 0, 0);
        lightingThread = null;
        lightUpdateCount = 0;
        bSwappingLightBuffers = false;
        bRendering = false;
        woodWallN = null;
        woodWallW = null;
        woodDWallN = null;
        woodDWallW = null;
        woodWWallN = null;
        woodWWallW = null;
        woodDoorW = null;
        woodDoorN = null;
        woodFloor = null;
        woodBarricade = null;
        woodCrate = null;
        woodStairsNB = null;
        woodStairsNM = null;
        woodStairsNT = null;
        woodStairsWB = null;
        woodStairsWM = null;
        woodStairsWT = null;
        dangerUpdate = new OnceEvery(0.4F, false);
        LightInfoUpdate = null;
        IsoWorld.instance.CurrentCell = this;
        this.width = width;
        this.height = height;
        slices = new SliceY[height];
        for(int n = 0; n < height; n++)
            slices[n] = new SliceY(this, width, 16, n);

    }

    public IsoCell(IsoSpriteManager spr, int width, int height, boolean vvv)
    {
        super(spr);
        BuildingList = new NulledArrayList();
        ObjectList = new NulledArrayList();
        PushableObjectList = new NulledArrayList();
        BuildingScores = new THashMap();
        RoomList = new NulledArrayList();
        StaticUpdaterObjectList = new NulledArrayList();
        wallArray = new NulledArrayList();
        ZombieList = new NulledArrayList();
        RemoteSurvivorList = new NulledArrayList();
        GhostList = new NulledArrayList();
        ZoneStack = new NulledArrayList();
        removeList = new NulledArrayList();
        addList = new NulledArrayList();
        RenderJobsArray = new NulledArrayList();
        ProcessItems = new NulledArrayList();
        ProcessItemsRemove = new NulledArrayList();
        RenderJobsMapArray = new THashMap();
        Containers = new Stack();
        ContainerMap = new HashMap();
        safeToAdd = true;
        LamppostPositions = new Stack();
        roomDefs = new NulledArrayList();
        everDone = false;
        recalcFloors = false;
        stairsNodes = new HashSet();
        bDoLotConnect = true;
        SurvivorList = new NulledArrayList();
        jumptot = 0;
        jumpcount = 0;
        jumpavr = 0;
        tempZoneStack = new Stack();
        vertLights = (int[][][][])null;
        buildVertLights = (int[][][][])null;
        currentLX = 0;
        currentLY = 0;
        currentLZ = 0;
        recalcShading = 30;
        lastMinX = 0xffed2979;
        lastMinY = 0xffed2979;
        lightUpdate = new OnceEvery(0.05F);
        alt = 0;
        staticBlack = new Color(0, 0, 0, 0);
        lightingThread = null;
        lightUpdateCount = 0;
        bSwappingLightBuffers = false;
        bRendering = false;
        woodWallN = null;
        woodWallW = null;
        woodDWallN = null;
        woodDWallW = null;
        woodWWallN = null;
        woodWWallW = null;
        woodDoorW = null;
        woodDoorN = null;
        woodFloor = null;
        woodBarricade = null;
        woodCrate = null;
        woodStairsNB = null;
        woodStairsNM = null;
        woodStairsNT = null;
        woodStairsWB = null;
        woodStairsWM = null;
        woodStairsWT = null;
        dangerUpdate = new OnceEvery(0.4F, false);
        LightInfoUpdate = null;
        IsoWorld.instance.CurrentCell = this;
        this.width = width;
        this.height = height;
        slices = new SliceY[height];
        for(int n = 0; n < height; n++)
            slices[n] = new SliceY(this, width, 16, n);

    }

    public void BlendUpperFloorToBlack(int i, int j, int k)
    {
    }

    public void CalcLightInfos(int MaxHeight)
    {
        for(int zz = 0; zz < MaxHeight; zz++)
        {
            for(int n = minY; n < maxY; n++)
            {
                for(int m = minX; m < maxX; m++)
                {
                    IsoGridSquare sqThis = getGridSquare(m, n, zz);
                    if(sqThis != null)
                        sqThis.CalcLightInfo();
                }

            }

        }

    }

    public void CalculateVertShading(int ma)
    {
        for(int zz = 0; zz < ma; zz++)
        {
            for(int n = minY; n < maxY; n++)
            {
                for(int m = minX; m < maxX; m++)
                {
                    int x = m - minX;
                    int y = n - minY;
                    IsoGridSquare sqThis = getGridSquare(m, n, zz);
                    if(sqThis != null)
                    {
                        CalculateVertColoursForTile(sqThis, m, n, zz);
                        continue;
                    }
                    if(zz > 0)
                        BlendUpperFloorToBlack(m, n, zz);
                }

            }

        }

    }

    public void CalculateVertColoursForTile(IsoGridSquare sqThis, int x, int y, int zz)
    {
        IsoGridSquare sqUL = sqThis.nw;
        IsoGridSquare sqU = sqThis.n;
        IsoGridSquare sqUR = sqThis.ne;
        IsoGridSquare sqR = sqThis.e;
        IsoGridSquare sqDR = sqThis.se;
        IsoGridSquare sqD = sqThis.s;
        IsoGridSquare sqDL = sqThis.sw;
        IsoGridSquare sqL = sqThis.w;
        buildVertLights[x][y][zz][0] = CalculateColor(sqUL, sqU, sqL, sqThis, buildVertLights[x][y][zz][0]);
        buildVertLights[x][y][zz][1] = CalculateColor(sqU, sqUR, sqR, sqThis, buildVertLights[x][y][zz][1]);
        buildVertLights[x][y][zz][2] = CalculateColor(sqDR, sqD, sqR, sqThis, buildVertLights[x][y][zz][2]);
        buildVertLights[x][y][zz][3] = CalculateColor(sqDL, sqD, sqL, sqThis, buildVertLights[x][y][zz][3]);
        MergeColor(sqThis, x, y, zz, 0);
    }

    public void ClearVertArrays()
    {
        everDone = true;
        int aa = MaxHeight;
    }

    public void DrawStencilMask()
    {
        IndieGL.glStencilMask(255);
        IndieGL.glClear(1280);
        int px = Core.getInstance().getOffscreenWidth() / 2;
        int py = Core.getInstance().getOffscreenHeight() / 2;
        px -= 118;
        py -= 134;
        Texture tex2 = Texture.getSharedTexture("mask_circledither.png");
        IndieGL.glEnable(2960);
        IndieGL.glEnable(3008);
        IndieGL.glAlphaFunc(516, 0.1F);
        IndieGL.glStencilFunc(519, 128, 255);
        IndieGL.glStencilOp(7680, 7680, 7681);
        tex2.renderstrip(px - (int)IsoCamera.RightClickX, py - (int)IsoCamera.RightClickY, tex2.getWidth(), tex2.getHeight(), 1.0F, 1.0F, 1.0F, 1.0F);
        IndieGL.glStencilFunc(519, 0, 255);
        IndieGL.glStencilOp(7680, 7680, 7680);
        IndieGL.glStencilMask(127);
        IndieGL.glAlphaFunc(519, 0.0F);
    }

    public void RenderTiles(int MaxHeight)
    {
        for(int zza = 0; zza < MaxHeight + 1; zza++)
        {
            if(zza < maxZ)
            {
                for(int n = minY; n < maxY; n++)
                    slices[n].renderFloor(minX, maxX, zza, n - minY);

            }
            RenderFloorShading(zza);
            LuaEventManager.TriggerEvent("OnPostFloorLayerDraw", Integer.valueOf(zza));
            for(int n = minY; n < maxY; n++)
            {
                currentLY = n - minY;
                currentLZ = zza;
                slices[n].renderMinusFloor(minX, maxX, zza, n - minY, maxZ);
            }

        }

    }

    public void RenderFloorShading(int zza)
    {
        if(zza < maxZ && Core.getInstance().nGraphicLevel >= 3)
        {
            IndieGL.End();
            IndieGL.glBlendFunc(0, 768);
            for(int n = minY; n < maxY; n++)
            {
                for(int m = minX; m < maxX; m++)
                {
                    int x = m - minX;
                    int y = n - minY;
                    IsoGridSquare sqUL = getGridSquare(m, n, zza);
                    if(sqUL == null || !sqUL.IsOnScreen() || (float)sqUL.getZ() > IsoCamera.CamCharacter.z && Core.getInstance().nGraphicLevel <= 3)
                        continue;
                    if(texWhite == null)
                        texWhite = Texture.getSharedTexture("media/ui/white.png");
                    Texture tex = texWhite;
                    float offX = 0.0F;
                    float offY = 0.0F;
                    float offZ = 0.0F;
                    float sx = IsoUtils.XToScreen((float)m + offX, (float)n + offY, (float)zza + offZ, 0);
                    float sy = IsoUtils.YToScreen((float)m + offX, (float)n + offY, (float)zza + offZ, 0);
                    sx = (int)sx;
                    sy = (int)sy;
                    sx -= (int)IsoCamera.getOffX();
                    sy -= (int)IsoCamera.getOffY();
                    sy += 128F;
                    sy += 128F;
                    sy += 128F;
                    colu = vertLights[m][n][zza][0];
                    colr = vertLights[m][n][zza][1];
                    cold = vertLights[m][n][zza][2];
                    coll = vertLights[m][n][zza][3];
                    if(sqUL != null && sqUL.getProperties().Is(IsoFlagType.solidfloor) && tex != null)
                        tex.renderdiamond((int)sx, (int)sy, 64, 32, colu, cold, coll, colr);
                }

            }

            IndieGL.End();
            IndieGL.glBlendFunc(770, 771);
        }
    }

    public IsoBuilding getClosestBuildingExcept(IsoGameCharacter chr, IsoRoom except)
    {
        IsoBuilding building = null;
        float lowest = 1000000F;
        for(int n = 0; n < BuildingList.size(); n++)
        {
            IsoBuilding b = (IsoBuilding)BuildingList.get(n);
            for(int m = 0; m < b.Exits.size(); m++)
            {
                float dist = chr.DistTo(((IsoRoomExit)b.Exits.get(m)).x, ((IsoRoomExit)b.Exits.get(m)).y);
                if(dist < lowest && (except == null || except.building != b))
                {
                    building = b;
                    lowest = dist;
                }
            }

        }

        return building;
    }

    private void ClearCache()
    {
        if(IsoCamera.CamCharacter != null && IsoCamera.CamCharacter.getCurrentSquare() != null && IsoCamera.CamCharacter.getCurrentSquare() != IsoCamera.CamCharacter.getLastSquare())
        {
            for(int x = 0; x < 200; x++)
            {
                for(int y = 0; y < 200; y++)
                {
                    for(int z = 0; z < 32; z++)
                        LosUtil.cachedresults[x][y][z] = 0;

                }

            }

            LosUtil.cachecleared = true;
        }
    }

    private void DangerUpdate()
    {
        if(IsoSprite.maxCount == 124)
            recalcFloors = true;
        for(int x = 0; x < width; x++)
        {
            for(int y = 0; y < height; y++)
                if(DangerScore[x][y] > -1000)
                    DangerScore[x][y] -= 14;

        }

        Iterator it = ZombieList.iterator();
        do
        {
            if(it == null || !it.hasNext())
                break;
            IsoZombie z = (IsoZombie)it.next();
            if(z.getZ() <= 0.0F)
            {
                int x = (int)(z.getX() - 16F);
                while((float)x < z.getX() + 16F) 
                {
                    for(int y = (int)(z.getY() - 16F); (float)y < z.getY() + 16F; y++)
                    {
                        if(x >= width || x < 0 || y >= height || y < 0 || DangerScore[x][y] >= 1000)
                            continue;
                        int val = 8;
                        if(DangerScore[x][y] < 0)
                            val += 2;
                        DangerScore[x][y] += val;
                    }

                    x++;
                }
            }
        } while(true);
    }

    private void ObjectDeletionAddition()
    {
        ClearCache();
        for(int n = 0; n < removeList.size(); n++)
        {
            IsoMovingObject r = (IsoMovingObject)removeList.get(n);
            ObjectList.remove(r);
            r.getCurrentSquare().getMovingObjects().remove(r);
        }

        removeList.clear();
        for(int n = 0; n < addList.size(); n++)
        {
            IsoMovingObject r = (IsoMovingObject)addList.get(n);
            ObjectList.add(r);
        }

        addList.clear();
    }

    private void ProcessItems(Iterator it2)
    {
        do
        {
            if(it2 == null || !it2.hasNext())
                break;
            InventoryItem i = (InventoryItem)it2.next();
            i.update();
            if(i.finishupdate())
                ProcessItemsRemove.add(i);
        } while(true);
    }

    private void ProcessObjects(Iterator it)
    {
        GameProfiler.instance.Start("Update Objects", 1.0F, 1.0F, 1.0F);
        while(it != null && it.hasNext()) 
        {
            IsoMovingObject o = (IsoMovingObject)it.next();
            try
            {
                o.preupdate();
                o.update();
            }
            catch(Exception ex)
            {
                Logger.getLogger(GameApplet.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        GameProfiler.instance.End("Update Objects");
    }

    private void ProcessRemoveItems(Iterator it2)
    {
        do
        {
            if(it2 == null || !it2.hasNext())
                break;
            InventoryItem i = (InventoryItem)it2.next();
            i.update();
            if(i.finishupdate())
                ProcessItems.remove(i);
        } while(true);
    }

    private void ProcessStaticUpdaters()
    {
        for(int n = 0; n < StaticUpdaterObjectList.size(); n++)
            try
            {
                ((IsoObject)StaticUpdaterObjectList.get(n)).update();
            }
            catch(Exception ex)
            {
                Logger.getLogger(GameApplet.class.getName()).log(Level.SEVERE, null, ex);
            }

    }

    public IsoSurvivor getNetworkPlayer(int RemoteID)
    {
        for(int n = 0; n < RemoteSurvivorList.size(); n++)
            if(((IsoGameCharacter)RemoteSurvivorList.get(n)).getRemoteID() == RemoteID)
                return (IsoSurvivor)RemoteSurvivorList.get(n);

        return null;
    }

    public boolean IsStairsNode(zombie.ai.astar.AStarPathMap.Node node, zombie.ai.astar.AStarPathMap.Node prev, IsoDirections directions)
    {
        if(recalcFloors)
            IsoBarricade.BarricadeFed = true;
        if(stairsNodes.contains(node.ID))
        {
            IsoGridSquare sq = getGridSquare(node.x, node.y, node.z);
            if(sq.Has(IsoObjectType.stairsTN))
                return directions == IsoDirections.N;
            if(sq.Has(IsoObjectType.stairsTW))
                return directions == IsoDirections.W;
            return prev.z == node.z;
        } else
        {
            return false;
        }
    }

    public void InitNodeMap(int MaxThreads)
    {
        for(int x = 0; x < width; x++)
        {
            for(int y = 0; y < height; y++)
            {
                for(int z = 0; z < MaxHeight; z++)
                {
                    if(PathMap.nodes[x][y][z] == null)
                        continue;
                    PathMap.nodes[x][y][z].searchData = new SearchData[MaxThreads];
                    for(int n = 0; n < MaxThreads; n++)
                        PathMap.nodes[x][y][z].searchData[n] = new SearchData();

                }

            }

        }

    }

    void ConnectNewSquare(IsoGridSquare newSquare, boolean bDoSurrounds)
    {
        newSquare.setCachedIsFree(false);
        newSquare.getSlice().Squares[newSquare.getX()][newSquare.getZ()] = newSquare;
        for(int x = -1; x <= 1; x++)
        {
            if(newSquare.getX() + x >= width || newSquare.getX() + x < 0)
                continue;
            for(int y = -1; y <= 1; y++)
            {
                if(newSquare.getY() + y >= height || newSquare.getY() + y < 0)
                    continue;
                for(int z = -1; z <= 1; z++)
                {
                    if(newSquare.getZ() + z >= MaxHeight || newSquare.getZ() + z < 0)
                        continue;
                    IsoGridSquare sq = getGridSquare(x + newSquare.getX(), y + newSquare.getY(), z + newSquare.getZ());
                    if(x == 0 && y == 0 && z == 0)
                        continue;
                    if(sq != null)
                    {
                        sq.setCachedIsFree(false);
                        sq.ReCalculateCollide(newSquare);
                        sq.ReCalculateVisionBlocked(newSquare);
                        sq.ReCalculatePathFind(newSquare);
                        newSquare.ReCalculateCollide(sq);
                        newSquare.ReCalculatePathFind(sq);
                        newSquare.ReCalculateVisionBlocked(sq);
                        if(z != 0)
                            continue;
                        if(x == 0 && y == -1 && !newSquare.testCollideAdjacent(null, x, y, z))
                        {
                            newSquare.setN(sq);
                            sq.setS(newSquare);
                        }
                        if(x == 0 && y == 1 && !newSquare.testCollideAdjacent(null, x, y, z))
                        {
                            newSquare.setS(sq);
                            sq.setN(newSquare);
                        }
                        if(x == -1 && y == 0 && !newSquare.testCollideAdjacent(null, x, y, z))
                        {
                            newSquare.setW(sq);
                            sq.setE(newSquare);
                        }
                        if(x == 1 && y == 0 && !newSquare.testCollideAdjacent(null, x, y, z))
                        {
                            newSquare.setE(sq);
                            sq.setW(newSquare);
                        }
                        continue;
                    }
                    if(sq == null && bDoSurrounds)
                    {
                        sq = new IsoGridSquare(this, slices[y + newSquare.getY()], x + newSquare.getX(), y + newSquare.getY(), z + newSquare.getZ());
                        ConnectNewSquare(sq, false);
                    }
                }

            }

        }

    }

    public void PlaceLot(String filename, int sx, int sy, int sz, boolean bClearExisting)
    {
        IsoLot lot = new IsoLot(filename);
        PlaceLot(lot, sx, sy, sz, bClearExisting);
    }

    public void PlaceLot(IsoLot lot, int sx, int sy, int sz, boolean bClearExisting)
    {
        int NewMaxHeight = MaxHeight;
        Stack BedList = new Stack();
        for(int x = sx; x < sx + lot.width; x++)
        {
            for(int y = sy; y < sy + lot.height; y++)
            {
                boolean bDoIt = true;
                for(int z = sz; z < sz + lot.levels; z++)
                {
                    bDoIt = false;
                    if(x >= width || y >= height || x < 0 || y < 0 || z < 0)
                        continue;
                    ArrayList ints = lot.data[x - sx][y - sy][z - sz];
                    if(ints == null)
                        continue;
                    int s = ints.size();
                    int n = 0;
                    IsoGridSquare square = getGridSquare(x, y, z);
                    if(square == null)
                    {
                        square = new IsoGridSquare(this, slices[y], x, y, z);
                        square.getSlice().Squares[x][z] = square;
                        if(bDoLotConnect)
                            ConnectNewSquare(square, true);
                    }
                    if(s > 0 && z > MaxHeight)
                        MaxHeight = z;
                    for(n = 0; n < s; n++)
                    {
                        String tile = (String)lot.tilesUsed.get(((Integer)ints.get(n)).intValue());
                        IsoSprite spr = (IsoSprite)SpriteManager.NamedMap.get(tile);
                        if(spr == null)
                        {
                            Logger.getLogger(GameApplet.class.getName()).log(Level.SEVERE, (new StringBuilder()).append("Missing tile definition: ").append(tile).toString());
                            continue;
                        }
                        square = getGridSquare(x, y, z);
                        if(square == null)
                        {
                            square = new IsoGridSquare(this, slices[y], x, y, z);
                            square.getSlice().Squares[x][z] = square;
                        } else
                        {
                            square.getSlice().Squares[x][z] = square;
                            if(bClearExisting && n == 0 && spr.getProperties().Is(IsoFlagType.solidfloor) && (!spr.Properties.Is(IsoFlagType.hidewalls) || ints.size() > 1))
                                bDoIt = true;
                            if(bDoIt && n == 0)
                                square.getObjects().clear();
                        }
                        CellLoader.DoTileObjectCreation(spr, spr.getType(), this, x, y, z, BedList, false, tile);
                    }

                }

            }

        }

        int n;
        for(n = 0; n < lot.zones.size(); n++)
        {
            IsoLot.Zone z = (IsoLot.Zone)lot.zones.get(n);
            if(z.name.equals("lot"))
            {
                IsoLot slot = new IsoLot((new StringBuilder()).append("media/lots/").append(z.val).append(".lot").toString());
                PlaceLot(slot, z.x, z.y, z.z, true);
                continue;
            }
            Zone zz = new Zone(z.val, sx + z.x, sy + z.y, z.w, z.h, sz + z.z);
            if("roomDef".equals(z.name))
            {
                roomDefs.add(zz);
            } else
            {
                getZoneStack().add(zz);
                ScriptManager.instance.AddZone("Base", z.val, new zombie.scripting.objects.Zone(z.val, sx + z.x, sy + z.y, sx + z.x + z.w, sy + z.y + z.h));
            }
        }

        n = 0;
        for(int x = sx - 1; x < sx + lot.width + 1; x++)
        {
            for(int y = sy - 1; y < sy + lot.height + 1; y++)
            {
                for(int z = sz; z < sz + lot.levels; z++)
                {
                    IsoGridSquare square = getGridSquare(x, y, z);
                    if(square != null)
                        square.RecalcAllWithNeighbours(false);
                }

            }

        }

    }

    public boolean DoBuilding(boolean bRender)
    {
        if(UIManager.getDragInventory() != null && UIManager.getPickedTile() != null)
        {
            IsoDirections dir = IsoDirections.N;
            dir = FromMouseTile();
            int ox = (int)UIManager.getPickedTile().x;
            int oy = (int)UIManager.getPickedTile().y;
            int oz = (int)IsoCamera.CamCharacter.getZ();
            boolean north = false;
            IsoGridSquare square = getGridSquare((int)UIManager.getPickedTile().x, (int)UIManager.getPickedTile().y, (int)IsoCamera.CamCharacter.getZ());
            IsoGridSquare orig = square;
            if(dir == IsoDirections.S)
            {
                square = getGridSquare((int)UIManager.getPickedTile().x, (int)UIManager.getPickedTile().y + 1, (int)IsoCamera.CamCharacter.getZ());
                ox = (int)UIManager.getPickedTile().x;
                oy = (int)UIManager.getPickedTile().y + 1;
                north = true;
            }
            if(dir == IsoDirections.E)
            {
                square = getGridSquare((int)UIManager.getPickedTile().x + 1, (int)UIManager.getPickedTile().y, (int)IsoCamera.CamCharacter.getZ());
                ox = (int)UIManager.getPickedTile().x + 1;
                oy = (int)UIManager.getPickedTile().y;
            }
            if(dir == IsoDirections.N)
                north = true;
            LuaEventManager.TriggerEvent("OnDoTileBuilding", Boolean.valueOf(bRender), Integer.valueOf((int)UIManager.getPickedTile().x), Integer.valueOf((int)UIManager.getPickedTile().y), Integer.valueOf((int)IsoCamera.CamCharacter.getZ()), orig, UIManager.getDragInventory());
            if(UIManager.getDragInventory() == null)
                return true;
            if("Plank".equals(UIManager.getDragInventory().getType()))
            {
                IsoObject tile = null;
                if(UIManager.Picked != null)
                {
                    for(tile = UIManager.Picked.tile; tile.rerouteMask != null; tile = tile.rerouteMask);
                    if((tile instanceof IsoDoor) || (tile instanceof IsoWindow) || (tile instanceof IsoCurtain))
                        return false;
                }
                if(dir == IsoDirections.S)
                {
                    square = getGridSquare((int)UIManager.getPickedTile().x, (int)UIManager.getPickedTile().y, (int)IsoCamera.CamCharacter.getZ());
                    north = true;
                }
                if(dir == IsoDirections.E)
                    square = getGridSquare((int)UIManager.getPickedTile().x, (int)UIManager.getPickedTile().y, (int)IsoCamera.CamCharacter.getZ());
                if(IsoPlayer.getInstance().hasEquipped("Hammer"))
                {
                    boolean bCanDo = false;
                    IsoGridSquare n = getGridSquare((int)UIManager.getPickedTile().x, (int)UIManager.getPickedTile().y - 1, (int)IsoCamera.CamCharacter.getZ());
                    IsoGridSquare s = getGridSquare((int)UIManager.getPickedTile().x, (int)UIManager.getPickedTile().y + 1, (int)IsoCamera.CamCharacter.getZ());
                    IsoGridSquare e = getGridSquare((int)UIManager.getPickedTile().x + 1, (int)UIManager.getPickedTile().y, (int)IsoCamera.CamCharacter.getZ());
                    IsoGridSquare w = getGridSquare((int)UIManager.getPickedTile().x - 1, (int)UIManager.getPickedTile().y, (int)IsoCamera.CamCharacter.getZ());
                    if(n != null && n.getProperties().Is(IsoFlagType.solidfloor))
                        bCanDo = true;
                    if(s != null && s.getProperties().Is(IsoFlagType.solidfloor))
                        bCanDo = true;
                    if(e != null && e.getProperties().Is(IsoFlagType.solidfloor))
                        bCanDo = true;
                    if(w != null && w.getProperties().Is(IsoFlagType.solidfloor))
                        bCanDo = true;
                    if(DistanceFromSupport((int)UIManager.getPickedTile().x, (int)UIManager.getPickedTile().y, (int)IsoCamera.CamCharacter.getZ()) > 1.0F)
                        return false;
                    if(!bCanDo)
                        return false;
                    if(bRender)
                        IndieGL.glBlendFunc(770, 1);
                    if(bRender)
                    {
                        woodFloor.render(null, (int)UIManager.getPickedTile().x, (int)UIManager.getPickedTile().y, (int)IsoCamera.CamCharacter.getZ(), IsoDirections.N, 0.0F, -144F, IsoGridSquare.getDefColorInfo());
                    } else
                    {
                        if(square == null)
                        {
                            square = new IsoGridSquare(this, slices[(int)UIManager.getPickedTile().y], (int)UIManager.getPickedTile().x, (int)UIManager.getPickedTile().y, (int)IsoCamera.CamCharacter.getZ());
                            ConnectNewSquare(square, true);
                        }
                        for(int nn = 0; nn < square.getObjects().size(); nn++)
                        {
                            IsoObject o = (IsoObject)square.getObjects().get(nn);
                            IsoSprite ss = o.sprite;
                            if(ss != null && (ss.getProperties().Is(IsoFlagType.solidfloor) || ss.getProperties().Is(IsoFlagType.noStart) || ss.getProperties().Is(IsoFlagType.vegitation) && o.getType() != IsoObjectType.tree))
                            {
                                square.getObjects().remove(nn);
                                nn--;
                            }
                        }

                        IsoObject obj = new IsoObject(this, square, "TileFloorInt_15");
                        obj.sprite.getProperties().Set(IsoFlagType.solidfloor, "");
                        square.getObjects().add(obj);
                        square.EnsureSurroundNotNull();
                        square.RecalcProperties();
                        square.getProperties().UnSet(IsoFlagType.exterior);
                        zombie.ai.astar.AStarPathMap.Node node = PathMap.nodes[square.getX()][square.getY()][square.getZ()];
                        if(node == null)
                        {
                            PathMap.nodes[square.getX()][square.getY()][square.getZ()] = new zombie.ai.astar.AStarPathMap.Node((short)square.getX(), (short)square.getY(), (short)square.getZ());
                            PathMap.NodeMap.add(PathMap.nodes[square.getX()][square.getY()][square.getZ()]);
                        }
                        SoundManager.instance.PlayWorldSound("hammernail", square, 0.2F, 10F, 0.4F, true);
                        LosUtil.cachecleared = true;
                        IsoGridSquare.setRecalcLightTime(-1);
                        UIManager.getDragInventory().Use(true);
                        IsoRoom room = null;
                        square.RecalcAllWithNeighbours(true);
                        if(n != null && n.getProperties().Is(IsoFlagType.solidfloor))
                        {
                            BlockInfo inf = n.testAdjacentRoomTransition(0, 1, 0);
                            if(n.getRoom() != null && !inf.ThroughDoor && !n.testCollideAdjacent(null, 0, 1, 0))
                                room = n.getRoom();
                            bCanDo = true;
                        }
                        if(s != null && s.getProperties().Is(IsoFlagType.solidfloor))
                        {
                            BlockInfo inf = s.testAdjacentRoomTransition(0, -1, 0);
                            if(s.getRoom() != null && !inf.ThroughDoor && !s.testCollideAdjacent(null, 0, -1, 0))
                                room = s.getRoom();
                            bCanDo = true;
                        }
                        if(e != null && e.getProperties().Is(IsoFlagType.solidfloor))
                        {
                            BlockInfo inf = e.testAdjacentRoomTransition(-1, 0, 0);
                            if(e.getRoom() != null && !inf.ThroughDoor && !e.testCollideAdjacent(null, -1, 0, 0))
                                room = e.getRoom();
                            bCanDo = true;
                        }
                        if(w != null && w.getProperties().Is(IsoFlagType.solidfloor))
                        {
                            BlockInfo inf = w.testAdjacentRoomTransition(1, 0, 0);
                            if(w.getRoom() != null && !inf.ThroughDoor && !w.testCollideAdjacent(null, 1, 0, 0))
                                room = w.getRoom();
                            bCanDo = true;
                        }
                        if(room != null)
                        {
                            square.setRoom(room);
                            room.TileList.add(square);
                        } else
                        {
                            square.setRoom(new IsoRoom());
                            square.getRoom().building = new IsoBuilding();
                            square.getRoom().building.Rooms.add(square.getRoom());
                            BuildingList.add(square.getRoom().building);
                        }
                        square.setCachedIsFree(false);
                        return true;
                    }
                }
            } else
            if("Stairs".equals(UIManager.getDragInventory().getType()))
            {
                if(dir == IsoDirections.S)
                {
                    square = getGridSquare((int)UIManager.getPickedTile().x, (int)UIManager.getPickedTile().y, (int)IsoCamera.CamCharacter.getZ());
                    north = true;
                }
                if(dir == IsoDirections.E)
                    square = getGridSquare((int)UIManager.getPickedTile().x, (int)UIManager.getPickedTile().y, (int)IsoCamera.CamCharacter.getZ());
                if(IsoPlayer.getInstance().hasEquipped("Hammer"))
                {
                    if(bRender)
                        IndieGL.glBlendFunc(770, 1);
                    if(UIManager.getPicked() != null)
                    {
                        IsoGridSquare a = null;
                        IsoGridSquare b = null;
                        if(dir == IsoDirections.N || dir == IsoDirections.S)
                        {
                            north = true;
                            a = getGridSquare((int)UIManager.getPickedTile().x, (int)UIManager.getPickedTile().y - 1, (int)IsoCamera.CamCharacter.getZ());
                            if(a != null)
                                b = getGridSquare((int)UIManager.getPickedTile().x, (int)UIManager.getPickedTile().y - 2, (int)IsoCamera.CamCharacter.getZ());
                        } else
                        if(dir == IsoDirections.W || dir == IsoDirections.E)
                        {
                            north = false;
                            a = getGridSquare((int)UIManager.getPickedTile().x - 1, (int)UIManager.getPickedTile().y, (int)IsoCamera.CamCharacter.getZ());
                            if(a != null)
                                b = getGridSquare((int)UIManager.getPickedTile().x - 2, (int)UIManager.getPickedTile().y, (int)IsoCamera.CamCharacter.getZ());
                        } else
                        {
                            return false;
                        }
                        if(!bRender)
                        {
                            if(a == null)
                                if(north)
                                {
                                    a = new IsoGridSquare(this, slices[(int)UIManager.getPickedTile().y - 1], (int)UIManager.getPickedTile().x, (int)UIManager.getPickedTile().y - 1, (int)IsoCamera.CamCharacter.getZ());
                                    ConnectNewSquare(a, true);
                                } else
                                {
                                    a = new IsoGridSquare(this, slices[(int)UIManager.getPickedTile().y], (int)UIManager.getPickedTile().x - 1, (int)UIManager.getPickedTile().y, (int)IsoCamera.CamCharacter.getZ());
                                    ConnectNewSquare(a, true);
                                }
                            if(b == null)
                                if(north)
                                {
                                    b = new IsoGridSquare(this, slices[(int)UIManager.getPickedTile().y - 2], (int)UIManager.getPickedTile().x, (int)UIManager.getPickedTile().y - 2, (int)IsoCamera.CamCharacter.getZ());
                                    ConnectNewSquare(b, true);
                                } else
                                {
                                    b = new IsoGridSquare(this, slices[(int)UIManager.getPickedTile().y], (int)UIManager.getPickedTile().x - 2, (int)UIManager.getPickedTile().y, (int)IsoCamera.CamCharacter.getZ());
                                    ConnectNewSquare(b, true);
                                }
                        }
                        boolean atest = false;
                        boolean btest = false;
                        if(square != null && a != null)
                        {
                            atest = !a.isFreeOrMidair(false);
                            if(north && !atest)
                                atest = square.getProperties().Is(IsoFlagType.collideN);
                            else
                            if(!north && !atest)
                                atest = square.getProperties().Is(IsoFlagType.collideW);
                        }
                        if(square != null && b != null)
                        {
                            atest = !b.isFreeOrMidair(false);
                            if(a != null)
                                if(north && !atest)
                                    atest = a.getProperties().Is(IsoFlagType.collideN);
                                else
                                if(!north && !atest)
                                    atest = a.getProperties().Is(IsoFlagType.collideW);
                        }
                        if(a == null || !a.isFreeOrMidair(true) || atest)
                            a = null;
                        if(a == null || b == null || !b.isFreeOrMidair(true) || btest)
                            b = null;
                        if(bRender && a != null && !a.isFreeOrMidair(true))
                            return false;
                        if(bRender && b != null && !b.isFreeOrMidair(true))
                            return false;
                        if(bRender || b != null && a != null)
                            if(bRender)
                            {
                                if(north)
                                {
                                    woodStairsNB.render(null, (int)UIManager.getPickedTile().x, (int)UIManager.getPickedTile().y, (int)IsoCamera.CamCharacter.getZ(), IsoDirections.N, 0.0F, -144F, IsoGridSquare.getDefColorInfo());
                                    woodStairsNM.render(null, (int)UIManager.getPickedTile().x, (int)UIManager.getPickedTile().y - 1, (int)IsoCamera.CamCharacter.getZ(), IsoDirections.N, 0.0F, -144F, IsoGridSquare.getDefColorInfo());
                                    woodStairsNT.render(null, (int)UIManager.getPickedTile().x, (int)UIManager.getPickedTile().y - 2, (int)IsoCamera.CamCharacter.getZ(), IsoDirections.N, 0.0F, -144F, IsoGridSquare.getDefColorInfo());
                                } else
                                {
                                    woodStairsWB.render(null, (int)UIManager.getPickedTile().x, (int)UIManager.getPickedTile().y, (int)IsoCamera.CamCharacter.getZ(), IsoDirections.N, 0.0F, -144F, IsoGridSquare.getDefColorInfo());
                                    woodStairsWM.render(null, (int)UIManager.getPickedTile().x - 1, (int)UIManager.getPickedTile().y, (int)IsoCamera.CamCharacter.getZ(), IsoDirections.N, 0.0F, -144F, IsoGridSquare.getDefColorInfo());
                                    woodStairsWT.render(null, (int)UIManager.getPickedTile().x - 2, (int)UIManager.getPickedTile().y, (int)IsoCamera.CamCharacter.getZ(), IsoDirections.N, 0.0F, -144F, IsoGridSquare.getDefColorInfo());
                                }
                            } else
                            {
                                if(square == null)
                                {
                                    square = new IsoGridSquare(this, slices[(int)UIManager.getPickedTile().y], (int)UIManager.getPickedTile().x, (int)UIManager.getPickedTile().y, (int)IsoCamera.CamCharacter.getZ());
                                    ConnectNewSquare(square, true);
                                }
                                square.AddStairs(north, 0, UIManager.getDragInventory().getType());
                                a.AddStairs(north, 1, UIManager.getDragInventory().getType());
                                b.AddStairs(north, 2, UIManager.getDragInventory().getType());
                                square.RecalcAllWithNeighbours(true);
                                a.RecalcAllWithNeighbours(true);
                                b.RecalcAllWithNeighbours(true);
                                SoundManager.instance.PlayWorldSound("hammernail", square, 0.2F, 10F, 0.4F, true);
                                LosUtil.cachecleared = true;
                                IsoGridSquare.setRecalcLightTime(-1);
                                UIManager.getDragInventory().Use(true);
                                return true;
                            }
                    }
                }
            } else
            if(!"Flatpack".equals(UIManager.getDragInventory().getType()))
                if("Wall".equals(UIManager.getDragInventory().getType()) || "DoorFrame".equals(UIManager.getDragInventory().getType()) || "WindowFrame".equals(UIManager.getDragInventory().getType()))
                {
                    if(IsoPlayer.getInstance().hasEquipped("Hammer"))
                    {
                        if(bRender)
                            IndieGL.glBlendFunc(770, 1);
                        if(bRender)
                        {
                            if("Wall".equals(UIManager.getDragInventory().getType()))
                                if(north)
                                    woodWallN.render(null, ox, oy, IsoCamera.CamCharacter.getZ(), IsoDirections.N, 0.0F, -144F, IsoGridSquare.getDefColorInfo());
                                else
                                    woodWallW.render(null, ox, oy, IsoCamera.CamCharacter.getZ(), IsoDirections.N, 0.0F, -144F, IsoGridSquare.getDefColorInfo());
                            if("DoorFrame".equals(UIManager.getDragInventory().getType()))
                                if(north)
                                    woodDWallN.render(null, ox, oy, IsoCamera.CamCharacter.getZ(), IsoDirections.N, 0.0F, -144F, IsoGridSquare.getDefColorInfo());
                                else
                                    woodDWallW.render(null, ox, oy, IsoCamera.CamCharacter.getZ(), IsoDirections.N, 0.0F, -144F, IsoGridSquare.getDefColorInfo());
                            if("WindowFrame".equals(UIManager.getDragInventory().getType()))
                                if(north)
                                    woodWWallN.render(null, ox, oy, IsoCamera.CamCharacter.getZ(), IsoDirections.N, 0.0F, -144F, IsoGridSquare.getDefColorInfo());
                                else
                                    woodWWallW.render(null, ox, oy, IsoCamera.CamCharacter.getZ(), IsoDirections.N, 0.0F, -144F, IsoGridSquare.getDefColorInfo());
                        } else
                        {
                            if(square == null)
                            {
                                square = new IsoGridSquare(this, slices[oy], ox, oy, (int)IsoCamera.CamCharacter.getZ());
                                ConnectNewSquare(square, true);
                            }
                            square.AddWoodWall(north, UIManager.getDragInventory().getType());
                            SoundManager.instance.PlayWorldSound("hammernail", square, 0.2F, 10F, 0.4F, true);
                            LosUtil.cachecleared = true;
                            IsoGridSquare.setRecalcLightTime(-1);
                            UIManager.getDragInventory().Use(true);
                            square.RecalcAllWithNeighbours(true);
                            return true;
                        }
                    }
                } else
                if(square != null && "Crate".equals(UIManager.getDragInventory().getType()) && square.HasNoCharacters())
                {
                    if(bRender)
                        IndieGL.glBlendFunc(770, 1);
                    if(bRender)
                    {
                        woodCrate.render(null, orig.getX(), orig.getY(), orig.getZ(), IsoDirections.N, 0.0F, -144F, IsoGridSquare.getDefColorInfo());
                    } else
                    {
                        IsoCrate b = new IsoCrate(this, square, -1);
                        square.getSpecialObjects().add(b);
                        square.getObjects().add(b);
                        LosUtil.cachecleared = true;
                        IsoGridSquare.setRecalcLightTime(-1);
                        UIManager.getDragInventory().Use(true);
                        return true;
                    }
                } else
                if("Barricade".equals(UIManager.getDragInventory().getType()) && square.HasNoCharacters())
                {
                    if(IsoPlayer.getInstance().hasEquipped("Hammer"))
                    {
                        if(bRender)
                            IndieGL.glBlendFunc(770, 1);
                        if(bRender)
                        {
                            woodBarricade.render(null, orig.getX(), orig.getY(), orig.getZ(), IsoDirections.N, 0.0F, -144F, IsoGridSquare.getDefColorInfo());
                        } else
                        {
                            IsoBarricade b = new IsoBarricade(this, square, -1);
                            square.getSpecialObjects().add(b);
                            square.getObjects().add(b);
                            SoundManager.instance.PlayWorldSound("hammernail", square, 0.2F, 10F, 0.4F, true);
                            LosUtil.cachecleared = true;
                            IsoGridSquare.setRecalcLightTime(-1);
                            UIManager.getDragInventory().Use(true);
                        }
                    }
                } else
                if("Door".equals(UIManager.getDragInventory().getType()) && square != null && !square.HasDoor(north))
                {
                    if(bRender)
                        IndieGL.glBlendFunc(770, 1);
                    if(bRender)
                    {
                        if(north)
                            woodDoorN.render(null, square.getX(), square.getY(), square.getZ(), IsoDirections.N, 0.0F, -144F, IsoGridSquare.getDefColorInfo());
                        else
                            woodDoorW.render(null, square.getX(), square.getY(), square.getZ(), IsoDirections.N, 0.0F, -144F, IsoGridSquare.getDefColorInfo());
                    } else
                    {
                        if(north && !square.getProperties().Is(IsoFlagType.cutN))
                        {
                            IsoObject obj = new IsoObject(this, square, "TileWalls3_44");
                            square.AddTileObject(obj);
                        }
                        if(!north && !square.getProperties().Is(IsoFlagType.cutW))
                        {
                            IsoObject obj = new IsoObject(this, square, "TileWalls3_43");
                            square.AddTileObject(obj);
                        }
                        IsoDoor b = new IsoDoor(this, square, north ? "TileDoors_17" : "TileDoors_16", north);
                        square.getSpecialObjects().add(b);
                        square.getObjects().add(b);
                        SoundManager.instance.PlayWorldSound("hammernail", square, 0.2F, 10F, 0.4F, true);
                        LosUtil.cachecleared = true;
                        IsoGridSquare.setRecalcLightTime(-1);
                        UIManager.getDragInventory().Use(true);
                    }
                }
        }
        return false;
    }

    public float DistanceFromSupport(int x, int y, int z)
    {
        return 0.0F;
    }

    public NulledArrayList getBuildingList()
    {
        return BuildingList;
    }

    public void setBuildingList(NulledArrayList BuildingList)
    {
        this.BuildingList = BuildingList;
    }

    public NulledArrayList getObjectList()
    {
        return ObjectList;
    }

    public void setObjectList(NulledArrayList ObjectList)
    {
        this.ObjectList = ObjectList;
    }

    public NulledArrayList getPushableObjectList()
    {
        return PushableObjectList;
    }

    public void setPushableObjectList(NulledArrayList PushableObjectList)
    {
        this.PushableObjectList = PushableObjectList;
    }

    public THashMap getBuildingScores()
    {
        return BuildingScores;
    }

    public void setBuildingScores(THashMap BuildingScores)
    {
        this.BuildingScores = BuildingScores;
    }

    public AStarPathMap getPathMap()
    {
        return PathMap;
    }

    public void setPathMap(AStarPathMap PathMap)
    {
        this.PathMap = PathMap;
    }

    public NulledArrayList getRoomList()
    {
        return RoomList;
    }

    public void setRoomList(NulledArrayList RoomList)
    {
        this.RoomList = RoomList;
    }

    public NulledArrayList getStaticUpdaterObjectList()
    {
        return StaticUpdaterObjectList;
    }

    public void setStaticUpdaterObjectList(NulledArrayList StaticUpdaterObjectList)
    {
        this.StaticUpdaterObjectList = StaticUpdaterObjectList;
    }

    public NulledArrayList getWallArray()
    {
        return wallArray;
    }

    public void setWallArray(NulledArrayList wallArray)
    {
        this.wallArray = wallArray;
    }

    public NulledArrayList getZombieList()
    {
        return ZombieList;
    }

    public void setZombieList(NulledArrayList ZombieList)
    {
        this.ZombieList = ZombieList;
    }

    public NulledArrayList getRemoteSurvivorList()
    {
        return RemoteSurvivorList;
    }

    public void setRemoteSurvivorList(NulledArrayList RemoteSurvivorList)
    {
        this.RemoteSurvivorList = RemoteSurvivorList;
    }

    public NulledArrayList getGhostList()
    {
        return GhostList;
    }

    public void setGhostList(NulledArrayList GhostList)
    {
        this.GhostList = GhostList;
    }

    public NulledArrayList getZoneStack()
    {
        return ZoneStack;
    }

    public void setZoneStack(NulledArrayList ZoneStack)
    {
        this.ZoneStack = ZoneStack;
    }

    public NulledArrayList getRemoveList()
    {
        return removeList;
    }

    public void setRemoveList(NulledArrayList removeList)
    {
        this.removeList = removeList;
    }

    public NulledArrayList getAddList()
    {
        return addList;
    }

    public void setAddList(NulledArrayList addList)
    {
        this.addList = addList;
    }

    public NulledArrayList getRenderJobsArray()
    {
        return RenderJobsArray;
    }

    public void setRenderJobsArray(NulledArrayList RenderJobsArray)
    {
        this.RenderJobsArray = RenderJobsArray;
    }

    public NulledArrayList getProcessItems()
    {
        return ProcessItems;
    }

    public void setProcessItems(NulledArrayList ProcessItems)
    {
        this.ProcessItems = ProcessItems;
    }

    public NulledArrayList getProcessItemsRemove()
    {
        return ProcessItemsRemove;
    }

    public void setProcessItemsRemove(NulledArrayList ProcessItemsRemove)
    {
        this.ProcessItemsRemove = ProcessItemsRemove;
    }

    public THashMap getRenderJobsMapArray()
    {
        return RenderJobsMapArray;
    }

    public void setRenderJobsMapArray(THashMap RenderJobsMapArray)
    {
        this.RenderJobsMapArray = RenderJobsMapArray;
    }

    public SliceY[] getSlices()
    {
        return slices;
    }

    public void setSlices(SliceY slices[])
    {
        this.slices = slices;
    }

    public int getHeight()
    {
        return height;
    }

    public void setHeight(int height)
    {
        this.height = height;
    }

    public int getWidth()
    {
        return width;
    }

    public void setWidth(int width)
    {
        this.width = width;
    }

    public int getWorldX()
    {
        return worldX;
    }

    public void setWorldX(int worldX)
    {
        this.worldX = worldX;
    }

    public int getWorldY()
    {
        return worldY;
    }

    public void setWorldY(int worldY)
    {
        this.worldY = worldY;
    }

    public Stack getContainers()
    {
        return Containers;
    }

    public void setContainers(Stack Containers)
    {
        this.Containers = Containers;
    }

    public String getFilename()
    {
        return filename;
    }

    public void setFilename(String filename)
    {
        this.filename = filename;
    }

    public boolean isSafeToAdd()
    {
        return safeToAdd;
    }

    public void setSafeToAdd(boolean safeToAdd)
    {
        this.safeToAdd = safeToAdd;
    }

    public Stack getLamppostPositions()
    {
        return LamppostPositions;
    }

    public void setLamppostPositions(Stack LamppostPositions)
    {
        this.LamppostPositions = LamppostPositions;
    }

    public HashSet getStairsNodes()
    {
        return stairsNodes;
    }

    public void setStairsNodes(HashSet stairsNodes)
    {
        this.stairsNodes = stairsNodes;
    }

    public Stack getTempZoneStack()
    {
        return tempZoneStack;
    }

    public void setTempZoneStack(Stack tempZoneStack)
    {
        this.tempZoneStack = tempZoneStack;
    }

    public int getCurrentLightX()
    {
        return currentLX;
    }

    public void setCurrentLightX(int currentLX)
    {
        this.currentLX = currentLX;
    }

    public int getCurrentLightY()
    {
        return currentLY;
    }

    public void setCurrentLightY(int currentLY)
    {
        this.currentLY = currentLY;
    }

    public int getCurrentLightZ()
    {
        return currentLZ;
    }

    public void setCurrentLightZ(int currentLZ)
    {
        this.currentLZ = currentLZ;
    }

    public IsoSprite getWoodWallN()
    {
        return woodWallN;
    }

    public void setWoodWallN(IsoSprite woodWallN)
    {
        this.woodWallN = woodWallN;
    }

    public IsoSprite getWoodWallW()
    {
        return woodWallW;
    }

    public void setWoodWallW(IsoSprite woodWallW)
    {
        this.woodWallW = woodWallW;
    }

    public IsoSprite getWoodDWallN()
    {
        return woodDWallN;
    }

    public void setWoodDWallN(IsoSprite woodDWallN)
    {
        this.woodDWallN = woodDWallN;
    }

    public IsoSprite getWoodDWallW()
    {
        return woodDWallW;
    }

    public void setWoodDWallW(IsoSprite woodDWallW)
    {
        this.woodDWallW = woodDWallW;
    }

    public IsoSprite getWoodWWallN()
    {
        return woodWWallN;
    }

    public void setWoodWWallN(IsoSprite woodWWallN)
    {
        this.woodWWallN = woodWWallN;
    }

    public IsoSprite getWoodWWallW()
    {
        return woodWWallW;
    }

    public void setWoodWWallW(IsoSprite woodWWallW)
    {
        this.woodWWallW = woodWWallW;
    }

    public IsoSprite getWoodDoorW()
    {
        return woodDoorW;
    }

    public void setWoodDoorW(IsoSprite woodDoorW)
    {
        this.woodDoorW = woodDoorW;
    }

    public IsoSprite getWoodDoorN()
    {
        return woodDoorN;
    }

    public void setWoodDoorN(IsoSprite woodDoorN)
    {
        this.woodDoorN = woodDoorN;
    }

    public IsoSprite getWoodFloor()
    {
        return woodFloor;
    }

    public void setWoodFloor(IsoSprite woodFloor)
    {
        this.woodFloor = woodFloor;
    }

    public IsoSprite getWoodBarricade()
    {
        return woodBarricade;
    }

    public void setWoodBarricade(IsoSprite woodBarricade)
    {
        this.woodBarricade = woodBarricade;
    }

    public IsoSprite getWoodCrate()
    {
        return woodCrate;
    }

    public void setWoodCrate(IsoSprite woodCrate)
    {
        this.woodCrate = woodCrate;
    }

    public IsoSprite getWoodStairsNB()
    {
        return woodStairsNB;
    }

    public void setWoodStairsNB(IsoSprite woodStairsNB)
    {
        this.woodStairsNB = woodStairsNB;
    }

    public IsoSprite getWoodStairsNM()
    {
        return woodStairsNM;
    }

    public void setWoodStairsNM(IsoSprite woodStairsNM)
    {
        this.woodStairsNM = woodStairsNM;
    }

    public IsoSprite getWoodStairsNT()
    {
        return woodStairsNT;
    }

    public void setWoodStairsNT(IsoSprite woodStairsNT)
    {
        this.woodStairsNT = woodStairsNT;
    }

    public IsoSprite getWoodStairsWB()
    {
        return woodStairsWB;
    }

    public void setWoodStairsWB(IsoSprite woodStairsWB)
    {
        this.woodStairsWB = woodStairsWB;
    }

    public IsoSprite getWoodStairsWM()
    {
        return woodStairsWM;
    }

    public void setWoodStairsWM(IsoSprite woodStairsWM)
    {
        this.woodStairsWM = woodStairsWM;
    }

    public IsoSprite getWoodStairsWT()
    {
        return woodStairsWT;
    }

    public void setWoodStairsWT(IsoSprite woodStairsWT)
    {
        this.woodStairsWT = woodStairsWT;
    }

    public int getMinX()
    {
        return minX;
    }

    public void setMinX(int minX)
    {
        this.minX = minX;
    }

    public int getMaxX()
    {
        return maxX;
    }

    public void setMaxX(int maxX)
    {
        this.maxX = maxX;
    }

    public int getMinY()
    {
        return minY;
    }

    public void setMinY(int minY)
    {
        this.minY = minY;
    }

    public int getMaxY()
    {
        return maxY;
    }

    public void setMaxY(int maxY)
    {
        this.maxY = maxY;
    }

    public int getMinZ()
    {
        return minZ;
    }

    public void setMinZ(int minZ)
    {
        this.minZ = minZ;
    }

    public int getMaxZ()
    {
        return maxZ;
    }

    public void setMaxZ(int maxZ)
    {
        this.maxZ = maxZ;
    }

    public OnceEvery getDangerUpdate()
    {
        return dangerUpdate;
    }

    public void setDangerUpdate(OnceEvery dangerUpdate)
    {
        this.dangerUpdate = dangerUpdate;
    }

    public Thread getLightInfoUpdate()
    {
        return LightInfoUpdate;
    }

    public void setLightInfoUpdate(Thread LightInfoUpdate)
    {
        this.LightInfoUpdate = LightInfoUpdate;
    }

    public NulledArrayList getSurvivorList()
    {
        return SurvivorList;
    }

    public static int getRComponent(int col)
    {
        return col & 0xff;
    }

    public static int getGComponent(int col)
    {
        return (col & 0xff00) >> 8;
    }

    public static int getBComponent(int col)
    {
        return (col & 0xff0000) >> 16;
    }

    public static int toIntColor(float r, float g, float b, float a)
    {
        return (int)(r * 255F) << 0 | (int)(g * 255F) << 8 | (int)(b * 255F) << 16 | (int)(a * 255F) << 24;
    }

    private void MergeColor(IsoGridSquare sqThis, int x, int y, int zz, int i)
    {
        if(i == 0)
        {
            float totalr = 0.0F;
            float totalg = 0.0F;
            float totalb = 0.0F;
            int div = 0;
            div++;
            totalr += getRComponent(buildVertLights[x][y][zz][0]);
            totalg += getGComponent(buildVertLights[x][y][zz][0]);
            totalb += getBComponent(buildVertLights[x][y][zz][0]);
            if(x > 0 && y > 0)
            {
                div++;
                totalr += getRComponent(buildVertLights[x - 1][y - 1][zz][2]);
                totalg += getGComponent(buildVertLights[x - 1][y - 1][zz][2]);
                totalb += getBComponent(buildVertLights[x - 1][y - 1][zz][2]);
            }
            if(y > 0)
            {
                div++;
                totalr += getRComponent(buildVertLights[x][y - 1][zz][3]);
                totalg += getGComponent(buildVertLights[x][y - 1][zz][3]);
                totalb += getBComponent(buildVertLights[x][y - 1][zz][3]);
            }
            if(x > 0)
            {
                div++;
                totalr += getRComponent(buildVertLights[x - 1][y][zz][1]);
                totalg += getGComponent(buildVertLights[x - 1][y][zz][1]);
                totalb += getBComponent(buildVertLights[x - 1][y][zz][1]);
            }
            totalr /= div;
            totalg /= div;
            totalb /= div;
            int c = (int)totalr << 0 | (int)totalg << 8 | (int)totalb << 16 | 0xff000000;
            buildVertLights[x][y][zz][i] = c;
            if(x > 0 && y > 0)
                buildVertLights[x - 1][y - 1][zz][2] = c;
            if(y > 0)
                buildVertLights[x][y - 1][zz][3] = c;
            if(x > 0)
                buildVertLights[x - 1][y][zz][1] = c;
        }
    }

    public IsoGridSquare getRandomOutdoorTile()
    {
        IsoGridSquare sq = null;
        do
        {
            sq = getGridSquare(Rand.Next(width), Rand.Next(height), 0);
            sq.setCachedIsFree(false);
        } while(sq == null || !sq.isFree(false) || sq.getRoom() != null);
        return sq;
    }

    int[][][][] getVertLights()
    {
        return vertLights;
    }

    private static void InsertAt(int a, BuildingScore score, BuildingScore array[])
    {
        for(int n = array.length - 1; n > a; n--)
            array[n] = array[n - 1];

        array[a] = score;
    }

    static void Place(BuildingScore score, BuildingScore array[], BuildingSearchCriteria criteria)
    {
        for(int a = 0; a < array.length; a++)
        {
            boolean bHigher = false;
           

            if(array[a] == null)
                bHigher = true;
            else
                switch(criteria.ordinal())
                {
                default:
                    break;

                case 1: // '\001'
                    if(array[a].food + array[a].defense + (float)array[a].size + array[a].weapons < score.food + score.defense + (float)score.size + score.weapons)
                        bHigher = true;
                    break;

                case 2: // '\002'
                    if(array[a].food < score.food)
                        bHigher = true;
                    break;

                case 3: // '\003'
                    if(array[a].wood < score.wood)
                        bHigher = true;
                    break;

                case 4: // '\004'
                    if(array[a].weapons < score.weapons)
                        bHigher = true;
                    break;

                case 5: // '\005'
                    if(array[a].defense < score.defense)
                        bHigher = true;
                    break;
                }
            if(bHigher)
            {
                InsertAt(a, score, array);
                return;
            }
        }

    }

    public Stack getBestBuildings(BuildingSearchCriteria criteria, int count)
    {
        BuildingScore b[] = new BuildingScore[count];
        if(BuildingScores.isEmpty())
        {
            for(int n = 0; n < BuildingList.size(); n++)
                ((IsoBuilding)BuildingList.get(n)).update();

        }
        BuildingScore score;
        for(Iterator scores = BuildingScores.values().iterator(); scores != null && scores.hasNext(); Place(score, b, criteria))
            score = (BuildingScore)scores.next();

        buildingscores.clear();
        buildingscores.addAll(Arrays.asList(b));
        return buildingscores;
    }

    public void AddZone(String name, int tileX, int tileY, int tileW, int tileH, int Z)
    {
        ZoneStack.add(new Zone(name, tileX, tileY, tileW, tileH, Z));
    }

    public boolean blocked(Mover mover, int x, int y, int z, int lx, int ly, int lz)
    {
        IsoGridSquare spfrom = getGridSquare(lx, ly, lz);
        if(spfrom == null)
            return true;
        if(mover instanceof VirtualZombie)
        {
            IsoGridSquare to = getGridSquare(x, y, z);
            if(to != null && to.room != null)
                return true;
        }
        if(z == lz)
            if(x > lx)
            {
                if(y == ly)
                    return spfrom.e == null;
                if(y < ly)
                    return spfrom.ne == null;
                if(y > ly)
                    return spfrom.se == null;
            } else
            if(x == lx)
            {
                if(y < ly)
                    return spfrom.n == null;
                if(y > ly)
                    return spfrom.s == null;
            } else
            {
                if(y == ly)
                    return spfrom.w == null;
                if(y < ly)
                    return spfrom.nw == null;
                if(y > ly)
                    return spfrom.sw == null;
            }
        if(spfrom == null)
            return true;
        if(mover instanceof IsoMovingObject)
        {
            if(spfrom.testPathFindAdjacent((IsoMovingObject)mover, x - lx, y - ly, z - lz))
                return true;
        } else
        if(spfrom.testPathFindAdjacent(null, x - lx, y - ly, z - lz))
            return true;
        return false;
    }

    public void Dispose()
    {
        for(int n = 0; n < ObjectList.size(); n++)
        {
            IsoMovingObject o = (IsoMovingObject)ObjectList.get(n);
            if(o instanceof IsoZombie)
                VirtualZombieManager.instance.ReusableZombies.add((IsoZombie)o);
        }

        stopLightingThread();
        super.Dispose();
        ItemContainer.IDMax = 0;
        for(int n = 0; n < RoomList.size(); n++)
        {
            ((IsoRoom)RoomList.get(n)).TileList.clear();
            ((IsoRoom)RoomList.get(n)).Exits.clear();
            ((IsoRoom)RoomList.get(n)).WaterSources.clear();
            ((IsoRoom)RoomList.get(n)).lightSwitches.clear();
            ((IsoRoom)RoomList.get(n)).Beds.clear();
        }

        for(int n = 0; n < BuildingList.size(); n++)
        {
            ((IsoBuilding)BuildingList.get(n)).Exits.clear();
            ((IsoBuilding)BuildingList.get(n)).Rooms.clear();
            ((IsoBuilding)BuildingList.get(n)).container.clear();
            ((IsoBuilding)BuildingList.get(n)).Windows.clear();
        }

        LuaEventManager.clear();
        LuaHookManager.clear();
        ZoneStack.clear();
        LamppostPositions.clear();
        PathMap.map = null;
        PathMap = null;
        GhostList.clear();
        ProcessItems.clear();
        ProcessItemsRemove.clear();
        BuildingScores.clear();
        Containers.clear();
        ContainerMap.clear();
        BuildingList.clear();
        ProcessItems.clear();
        PushableObjectList.clear();
        RoomList.clear();
        SurvivorList.clear();
        ObjectList.clear();
        ZombieList.clear();
        for(int n = 0; n < height; n++)
        {
            slices[n].cell = null;
            for(int m = 0; m < width; m++)
            {
                for(int z = 0; z < MaxHeight; z++)
                    if(slices[n].Squares[m][z] != null)
                    {
                        slices[n].Squares[m][z].w = null;
                        slices[n].Squares[m][z].nw = null;
                        slices[n].Squares[m][z].n = null;
                        slices[n].Squares[m][z].ne = null;
                        slices[n].Squares[m][z].e = null;
                        slices[n].Squares[m][z].se = null;
                        slices[n].Squares[m][z].s = null;
                        slices[n].Squares[m][z].sw = null;
                        slices[n].Squares[m][z].room = null;
                        slices[n].Squares[m][z].getObjects().clear();
                        slices[n].Squares[m][z].getMovingObjects().clear();
                        slices[n].Squares[m][z].getSpecialObjects().clear();
                        slices[n].Squares[m][z].getStaticMovingObjects().clear();
                        slices[n].Squares[m][z] = null;
                    }

            }

            slices[n] = null;
        }

    }

    public float getCost(Mover mover, int sx, int sy, int sz, int tx, int ty, int tz)
    {
        float val = 0.0F;
        if(sx != tx && sy != ty)
            val += 1.41421F;
        else
            val++;
        if(mover instanceof IsoZombie)
        {
            IsoGridSquare sq = getGridSquare(sx, sy, sz);
            val *= (sq.getMovingObjects().size() + 1) * 3;
            return val;
        }
        if(mover instanceof IsoSurvivor)
        {
            IsoGridSquare ssq = getGridSquare(sx, sy, sz);
            IsoGridSquare sq = getGridSquare(tx, ty, tz);
            if(sq != null)
            {
                if(ssq.IsWindow(sq.getX() - ssq.getX(), sq.getY() - ssq.getY(), 0))
                    val += 100000F;
                if(sq.getMovingObjects().size() > 0)
                    val += sq.getMovingObjects().size() * 600;
                if(!sq.getProperties().Is(IsoFlagType.solidfloor))
                    val += 100000F;
            }
            int dan = 0;
            if(DangerScore[sx][sy] > 0)
                dan += DangerScore[sx][sy] / 10;
            if(dan > 1)
                val += 1 + dan;
        }
        return val;
    }

    public int getElevInTiles()
    {
        return MaxHeight;
    }

    IsoGridSquare getFreeTile(String zone)
    {
        Iterator it = ZoneStack.iterator();
        do
        {
            if(it == null || !it.hasNext())
                break;
            Zone z = (Zone)it.next();
            if(z.Name.equals(zone))
                tempZoneStack.add(z);
        } while(true);
        if(tempZoneStack.isEmpty())
        {
            return null;
        } else
        {
            Zone z = (Zone)tempZoneStack.get(Rand.Next(tempZoneStack.size()));
            tempZoneStack.clear();
            return getFreeTile(z);
        }
    }

    public IsoGridSquare getFreeTile(Zone zone)
    {
        NulledArrayList freeTileTemp = new NulledArrayList();
        for(int x = 0; x < zone.W; x++)
        {
            for(int y = 0; y < zone.H; y++)
            {
                IsoGridSquare sq = getGridSquare(x + zone.X, y + zone.Y, zone.Z);
                if(sq != null && !sq.getProperties().Is(IsoFlagType.solid) && !sq.getProperties().Is(IsoFlagType.solidtrans))
                    freeTileTemp.add(sq);
            }

        }

        if(!freeTileTemp.isEmpty())
            return (IsoGridSquare)freeTileTemp.get(Rand.Next(freeTileTemp.size()));
        else
            return null;
    }

    public IsoGridSquare getGridSquare(double x, double y, double z)
    {
        return getGridSquare((int)x, (int)y, (int)z);
    }

    public IsoGridSquare getOrCreateGridSquare(double x, double y, double z)
    {
        IsoGridSquare sq = getGridSquare((int)x, (int)y, (int)z);
        if(sq == null)
        {
            sq = new IsoGridSquare(this, slices[(int)y], (int)x, (int)y, (int)z);
            ConnectNewSquare(sq, true);
        }
        return sq;
    }

    public IsoGridSquare getGridSquare(int x, int y, int z)
    {
        if(x < 0)
            return null;
        if(x >= width)
            return null;
        if(y < 0)
            return null;
        if(y >= height)
            return null;
        if(z >= MaxHeight)
            return null;
        int n = slices[y].Squares[x].length;
        if(z >= n)
            return null;
        if(z < 0)
            return null;
        else
            return slices[y].Squares[x][z];
    }

    public void DeleteAllMovingObjects()
    {
        ObjectList.clear();
    }

    public int getMaxFloors()
    {
        return MaxHeight;
    }

    public KahluaTable getLuaObjectList()
    {
        KahluaTable table = LuaManager.platform.newTable();
        LuaManager.env.rawset("Objects", table);
        for(int n = 0; n < ObjectList.size(); n++)
            table.rawset(n + 1, ObjectList.get(n));

        return table;
    }

    public int getHeightInTiles()
    {
        return height;
    }

    public int getWidthInTiles()
    {
        return width;
    }

    public boolean isNull(int x, int y, int z)
    {
        if(x < 0 || y < 0 || z < 0 || x >= width || y >= height || z >= MaxHeight)
        {
            return true;
        } else
        {
            IsoGridSquare sq = slices[y].Squares[x][z];
            return sq == null || !sq.isFree(false);
        }
    }

    public boolean IsZone(String name, int x, int y)
    {
        for(int n = 0; n < ZoneStack.size(); n++)
        {
            Zone zone = (Zone)ZoneStack.get(n);
            if(zone.Name.equals(name) && x >= zone.X && x < zone.X + zone.W && y >= zone.Y && y < zone.Y + zone.H)
                return true;
        }

        return false;
    }

    public void pathFinderVisited(int i, int j, int k)
    {
    }

    public void Remove(IsoMovingObject obj)
    {
        removeList.add(obj);
    }

    boolean isBlocked(IsoGridSquare from, IsoGridSquare to)
    {
        return from.room != to.room;
    }

    private int CalculateColor(IsoGridSquare sqUL, IsoGridSquare sqU, IsoGridSquare sqL, IsoGridSquare sqThis, int col)
    {
        float r = 0.0F;
        float g = 0.0F;
        float b = 0.0F;
        float a = 1.0F;
        float div = 0.0F;
        boolean bTest = true;
        if(sqUL != null && sqThis.room == sqUL.room)
        {
            div++;
            ColorInfo inf = sqUL.lightInfo;
            r += inf.r;
            g += inf.g;
            b += inf.b;
        }
        if(sqU != null && sqThis.room == sqU.room)
        {
            div++;
            ColorInfo inf = sqU.lightInfo;
            r += inf.r;
            g += inf.g;
            b += inf.b;
        }
        if(sqL != null && sqThis.room == sqL.room)
        {
            div++;
            ColorInfo inf = sqL.lightInfo;
            r += inf.r;
            g += inf.g;
            b += inf.b;
        }
        if(sqThis != null)
        {
            div++;
            ColorInfo inf = sqThis.lightInfo;
            r += inf.r;
            g += inf.g;
            b += inf.b;
        }
        if(div != 0.0F)
        {
            r /= div;
            g /= div;
            b /= div;
        }
        if(r > 1.0F)
            r = 1.0F;
        if(g > 1.0F)
            g = 1.0F;
        if(b > 1.0F)
            b = 1.0F;
        if(r < 0.0F)
            r = 0.0F;
        if(g < 0.0F)
            g = 0.0F;
        if(b < 0.0F)
            b = 0.0F;
        col = (int)(r * 255F) << 0 | (int)(g * 255F) << 8 | (int)(b * 255F) << 16 | (int)(sqThis.lightInfo.a * 255F) << 24;
        return col;
    }

    public void stopLightingThread()
    {
        if(lightingThread == null)
        {
            return;
        } else
        {
            lightingThread.stop();
            lightingThread = null;
            return;
        }
    }

    public void initLightingThread()
    {
        if(lightingThread == null)
        {
            lightingThread = new Thread(new Runnable() {

                public void run()
                {
                    if(vertLights == null)
                    {
                        vertLights = new int[width][height][IsoCell.MaxHeight][4];
                        buildVertLights = new int[width][height][IsoCell.MaxHeight][4];
                        for(int x = 0; x < width; x++)
                        {
                            for(int y = 0; y < height; y++)
                            {
                                for(int z = 0; z < IsoCell.MaxHeight; z++)
                                    if(getGridSquare(x, y, z) != null)
                                    {
                                        vertLights[x][y][z][0] = 0x7fffffff;
                                        vertLights[x][y][z][1] = 0x7fffffff;
                                        vertLights[x][y][z][2] = 0x7fffffff;
                                        vertLights[x][y][z][3] = 0x7fffffff;
                                        buildVertLights[x][y][z][0] = 0x7fffffff;
                                        buildVertLights[x][y][z][1] = 0x7fffffff;
                                        buildVertLights[x][y][z][2] = 0x7fffffff;
                                        buildVertLights[x][y][z][3] = 0x7fffffff;
                                    } else
                                    {
                                        vertLights[x][y][z][0] = 0x80000000;
                                        vertLights[x][y][z][1] = 0x80000000;
                                        vertLights[x][y][z][2] = 0x80000000;
                                        vertLights[x][y][z][3] = 0x80000000;
                                        buildVertLights[x][y][z][0] = 0x80000000;
                                        buildVertLights[x][y][z][1] = 0x80000000;
                                        buildVertLights[x][y][z][2] = 0x80000000;
                                        buildVertLights[x][y][z][3] = 0x80000000;
                                    }

                            }

                        }

                        recalcShading = -1;
                    }
                    
                    
                    while(Core.getInstance() != null) 
                    {
                        if(Core.getInstance().nGraphicLevel >= 3)
                        {
                            float topLeftX = IsoUtils.XToIso(-128F, -256F, 0.0F);
                            float topLeftY = IsoUtils.YToIso(-128F, -256F, 0.0F);
                            float topRightX = IsoUtils.XToIso(Core.getInstance().getOffscreenWidth() + 128, -256F, 0.0F);
                            float topRightY = IsoUtils.YToIso(Core.getInstance().getOffscreenWidth() + 128, -256F, 0.0F);
                            float bottomRightX = IsoUtils.XToIso(Core.getInstance().getOffscreenWidth() + 128, Core.getInstance().getOffscreenHeight() + 256, 6F);
                            float bottomRightY = IsoUtils.YToIso(Core.getInstance().getOffscreenWidth() + 128, Core.getInstance().getOffscreenHeight() + 256, 6F);
                            float bottomLeftX = IsoUtils.XToIso(Core.getInstance().getOffscreenWidth() + 128, Core.getInstance().getOffscreenHeight() + 256, 6F);
                            float bottomLeftY = IsoUtils.YToIso(Core.getInstance().getOffscreenWidth() + 128, Core.getInstance().getOffscreenHeight() + 256, 6F);
                            minY = (int)topRightY;
                            maxY = (int)bottomLeftY;
                            minX = (int)topLeftX;
                            maxX = (int)bottomRightX;
                            minX-=  1;
                            minY-=  1;
                            if(minX < 0)
                                minX = 0;
                            if(maxY > height)
                                maxY = height;
                            if(minY < 0)
                                minY = 0;
                            if(minY < 0)
                                minY = 0;
                            if(maxX > width)
                                maxX = width;
                            maxZ = IsoCell.MaxHeight;
                            if(IsoCamera.CamCharacter.getCurrentSquare() != null && (IsoCamera.CamCharacter.getCurrentSquare().getProperties().Is(IsoFlagType.hidewalls) || !IsoCamera.CamCharacter.current.getProperties().Is(IsoFlagType.exterior)))
                                maxZ = (int)IsoCamera.CamCharacter.getZ() + 1;
                            if(IsoPlayer.DemoMode)
                                maxZ = (int)IsoCamera.CamCharacter.getZ() + 1;
                            try
                            {
                                CalculateVertShading(IsoCell.MaxHeight);
                                while(bRendering) 
                                    Thread.sleep(20L);
                                if(!bRendering)
                                {
                                    bSwappingLightBuffers = true;
                                    int temp[][][][] = vertLights;
                                    vertLights = buildVertLights;
                                    buildVertLights = temp;
                                    lightUpdateCount++;
                                    bSwappingLightBuffers = false;
                                }
                            }
                            catch(Exception ex)
                            {
                                Logger.getLogger(IsoCell.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            try
                            {
                                Thread.sleep(20L);
                            }
                            catch(InterruptedException ex)
                            {
                                Logger.getLogger(IsoCell.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                        try
                        {
                            Thread.sleep(20L);
                        }
                        catch(InterruptedException ex)
                        {
                            Logger.getLogger(IsoCell.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }

              

  
            }
);
            lightingThread.setPriority(3);
            lightingThread.start();
        }
    }

    public void render()
        throws InterruptedException
    {
        int MaxHeight = this.MaxHeight;
        if(MaxHeight < 16)
            MaxHeight++;
        recalcShading--;
        if(IsoCamera.CamCharacter == null || IsoCamera.CamCharacter.getCurrentSquare() == null)
            return;
        if(Core.getInstance().nGraphicLevel < 3)
        {
            float topLeftX = IsoUtils.XToIso(-128F, -256F, 0.0F);
            float topLeftY = IsoUtils.YToIso(-128F, -256F, 0.0F);
            float topRightX = IsoUtils.XToIso(Core.getInstance().getOffscreenWidth() + 128, -256F, 0.0F);
            float topRightY = IsoUtils.YToIso(Core.getInstance().getOffscreenWidth() + 128, -256F, 0.0F);
            float bottomRightX = IsoUtils.XToIso(Core.getInstance().getOffscreenWidth() + 128, Core.getInstance().getOffscreenHeight() + 256, 6F);
            float bottomRightY = IsoUtils.YToIso(Core.getInstance().getOffscreenWidth() + 128, Core.getInstance().getOffscreenHeight() + 256, 6F);
            float bottomLeftX = IsoUtils.XToIso(Core.getInstance().getOffscreenWidth() + 128, Core.getInstance().getOffscreenHeight() + 256, 6F);
            float bottomLeftY = IsoUtils.YToIso(Core.getInstance().getOffscreenWidth() + 128, Core.getInstance().getOffscreenHeight() + 256, 6F);
            minY = (int)topRightY;
            maxY = (int)bottomLeftY;
            minX = (int)topLeftX;
            maxX = (int)bottomRightX;
            minX--;
            minY--;
            if(minX < 0)
                minX = 0;
            if(maxY > height)
                maxY = height;
            if(minY < 0)
                minY = 0;
            if(minY < 0)
                minY = 0;
            if(maxX > width)
                maxX = width;
            maxZ = MaxHeight;
            if(IsoCamera.CamCharacter.getCurrentSquare().getProperties().Is(IsoFlagType.hidewalls) || !IsoCamera.CamCharacter.current.getProperties().Is(IsoFlagType.exterior))
                maxZ = (int)IsoCamera.CamCharacter.getZ() + 1;
            if(IsoPlayer.DemoMode)
                maxZ = (int)IsoCamera.CamCharacter.getZ() + 1;
        }
        IsoGridSquare.rmod = GameTime.getInstance().Lerp(1.0F, 0.1F, GameTime.getInstance().getNightTint());
        IsoGridSquare.gmod = GameTime.getInstance().Lerp(1.0F, 0.2F, GameTime.getInstance().getNightTint());
        IsoGridSquare.bmod = GameTime.getInstance().Lerp(1.0F, 0.45F, GameTime.getInstance().getNightTint());
        GameProfiler.instance.Start("Calculate Lights", 1.0F, 1.0F, 1.0F);
        CalcLightInfos(MaxHeight);
        GameProfiler.instance.End("Calculate Lights");
        if(Core.getInstance().nGraphicLevel >= 3)
            initLightingThread();
        int ma = maxZ + 2;
        if(ma > MaxHeight)
            ma = MaxHeight;
        alt++;
        if(alt == 3)
            alt = 0;
        if(Core.getInstance().nGraphicLevel >= 3)
            DrawStencilMask();
        lastMinX = minX;
        lastMinY = minY;
        GameProfiler.instance.Start("Render Scene", 1.0F, 1.0F, 1.0F);
        while(bSwappingLightBuffers) 
            Thread.sleep(10L);
        bRendering = true;
        try
        {
            if(vertLights == null)
                RenderTiles(MaxHeight);
            else
                RenderTiles(MaxHeight);
        }
        catch(Exception ex)
        {
            bRendering = false;
        }
        bRendering = false;
        GameProfiler.instance.End("Render Scene");
        if(IsoGridSquare.getRecalcLightTime() < 0)
            IsoGridSquare.setRecalcLightTime(60);
        if(IsoGridSquare.getLightcache() <= 0)
            IsoGridSquare.setLightcache(90);
        for(int n = 0; n < ObjectList.size(); n++)
        {
            IsoMovingObject obj = (IsoMovingObject)ObjectList.get(n);
            obj.renderlast();
        }

        for(int i = 0; i < StaticUpdaterObjectList.size(); i++)
        {
            IsoObject obj = (IsoObject)StaticUpdaterObjectList.get(i);
            obj.renderlast();
        }

        if(UIManager.getLastPicked() != null && UIManager.getLastPicked() != null && UIManager.getLastPicked().OutlineOnMouseover && TutorialManager.instance.AllowUse(UIManager.getLastPicked()) && UIManager.getLastPicked().targetAlpha > 0.0F)
            if((UIManager.getLastPicked() instanceof IsoPushableObject) && ((IsoPushableObject)UIManager.getLastPicked()).connectList != null)
            {
                for(int n = 0; n < ((IsoPushableObject)UIManager.getLastPicked()).connectList.size(); n++)
                {
                    IsoPushableObject p = (IsoPushableObject)((IsoPushableObject)UIManager.getLastPicked()).connectList.get(n);
                    ColorInfo col = new ColorInfo();
                    col.r = 1.0F;
                    col.g = 1.0F;
                    col.b = 1.0F;
                    float alpha = p.alpha;
                    float alpha2 = p.alpha;
                    p.alpha = 0.4F;
                    IndieGL.glBlendFunc(770, 1);
                    p.render(p.getX(), p.getY(), p.getZ(), col, true);
                    IndieGL.End();
                    UIManager.LastPicked.alpha = alpha;
                }

            } else
            {
                ColorInfo col = new ColorInfo();
                col.r = 1.0F;
                col.g = 1.0F;
                col.b = 1.0F;
                float alpha = UIManager.getLastPicked().alpha;
                float alpha2 = UIManager.getLastPicked().alpha;
                UIManager.LastPicked.alpha = 0.4F;
                IndieGL.glBlendFunc(770, 1);
                if(UIManager.getLastPicked() instanceof IsoMovingObject)
                    UIManager.getLastPicked().render(((IsoMovingObject)UIManager.getLastPicked()).getX(), ((IsoMovingObject)UIManager.getLastPicked()).getY(), ((IsoMovingObject)UIManager.getLastPicked()).getZ(), col, true);
                else
                    UIManager.getLastPicked().render(UIManager.getLastPicked().square.getX(), UIManager.getLastPicked().square.getY(), UIManager.getLastPicked().square.getZ(), col, true);
                IndieGL.End();
                UIManager.LastPicked.alpha = alpha;
            }
        if(woodDoorW == null)
        {
            woodDoorW = IsoSprite.CreateSprite(BucketManager.Shared().SpriteManager);
            woodDoorW.LoadFramesNoDirPageSimple("TileDoors_8");
        }
        if(woodDoorN == null)
        {
            woodDoorN = IsoSprite.CreateSprite(BucketManager.Shared().SpriteManager);
            woodDoorN.LoadFramesNoDirPageSimple("TileDoors_9");
        }
        if(woodStairsNB == null)
        {
            woodStairsNB = IsoSprite.CreateSprite(BucketManager.Shared().SpriteManager);
            woodStairsNB.LoadFramesNoDirPageSimple("TileStairs_8");
        }
        if(woodStairsNM == null)
        {
            woodStairsNM = IsoSprite.CreateSprite(BucketManager.Shared().SpriteManager);
            woodStairsNM.LoadFramesNoDirPageSimple("TileStairs_9");
        }
        if(woodStairsNT == null)
        {
            woodStairsNT = IsoSprite.CreateSprite(BucketManager.Shared().SpriteManager);
            woodStairsNT.LoadFramesNoDirPageSimple("TileStairs_10");
        }
        if(woodStairsWB == null)
        {
            woodStairsWB = IsoSprite.CreateSprite(BucketManager.Shared().SpriteManager);
            woodStairsWB.LoadFramesNoDirPageSimple("TileStairs_0");
        }
        if(woodStairsWM == null)
        {
            woodStairsWM = IsoSprite.CreateSprite(BucketManager.Shared().SpriteManager);
            woodStairsWM.LoadFramesNoDirPageSimple("TileStairs_1");
        }
        if(woodStairsWT == null)
        {
            woodStairsWT = IsoSprite.CreateSprite(BucketManager.Shared().SpriteManager);
            woodStairsWT.LoadFramesNoDirPageSimple("TileStairs_2");
        }
        if(woodCrate == null)
        {
            woodCrate = IsoSprite.CreateSprite(BucketManager.Shared().SpriteManager);
            woodCrate.LoadFramesNoDirPageSimple("TileObjects2_0");
        }
        if(woodBarricade == null)
        {
            woodBarricade = IsoSprite.CreateSprite(BucketManager.Shared().SpriteManager);
            woodBarricade.LoadFramesNoDirPageSimple("TileObjects2_26");
        }
        if(woodWallN == null)
        {
            woodWallN = IsoSprite.CreateSprite(BucketManager.Shared().SpriteManager);
            woodWallN.LoadFramesNoDirPageSimple("TileWalls_49");
        }
        if(woodWallW == null)
        {
            woodWallW = IsoSprite.CreateSprite(BucketManager.Shared().SpriteManager);
            woodWallW.LoadFramesNoDirPageSimple("TileWalls_48");
        }
        if(woodDWallN == null)
        {
            woodDWallN = IsoSprite.CreateSprite(BucketManager.Shared().SpriteManager);
            woodDWallN.LoadFramesNoDirPageSimple("TileWalls_59");
        }
        if(woodDWallW == null)
        {
            woodDWallW = IsoSprite.CreateSprite(BucketManager.Shared().SpriteManager);
            woodDWallW.LoadFramesNoDirPageSimple("TileWalls_58");
        }
        if(woodWWallN == null)
        {
            woodWWallN = IsoSprite.CreateSprite(BucketManager.Shared().SpriteManager);
            woodWWallN.LoadFramesNoDirPageSimple("TileWalls_57");
        }
        if(woodWWallW == null)
        {
            woodWWallW = IsoSprite.CreateSprite(BucketManager.Shared().SpriteManager);
            woodWWallW.LoadFramesNoDirPageSimple("TileWalls_56");
        }
        if(woodFloor == null)
        {
            woodFloor = IsoSprite.CreateSprite(BucketManager.Shared().SpriteManager);
            woodFloor.LoadFramesNoDirPageSimple("TileFloorInt_15");
        }
        DoBuilding(true);
        IndieGL.glBlendFunc(770, 771);
    }

    public static IsoDirections FromMouseTile()
    {
        IsoDirections dir = IsoDirections.N;
        float x = UIManager.getPickedTileLocal().x;
        float y = UIManager.getPickedTileLocal().y;
        float centrenessY = 0.5F - Math.abs(0.5F - y);
        float centrenessX = 0.5F - Math.abs(0.5F - x);
        if(x > 0.5F && centrenessX < centrenessY)
            dir = IsoDirections.E;
        else
        if(y > 0.5F && centrenessX > centrenessY)
            dir = IsoDirections.S;
        else
        if(x < 0.5F && centrenessX < centrenessY)
            dir = IsoDirections.W;
        else
        if(y < 0.5F && centrenessX > centrenessY)
            dir = IsoDirections.N;
        return dir;
    }

    public void renderListClear()
    {
        for(int n = 0; n < RenderJobsArray.size(); n++)
            ((NulledArrayList)RenderJobsArray.get(n)).clear();

    }

    public void update()
    {
        if(DangerScore == null)
            DangerScore = new int[width][height];
        if(dangerUpdate.Check())
            DangerUpdate();
        Iterator it2 = ProcessItems.iterator();
        ProcessItems(it2);
        it2 = ProcessItemsRemove.iterator();
        ProcessRemoveItems(it2);
        ProcessItemsRemove.clear();
        int zsize = ObjectList.size();
        safeToAdd = false;
        Iterator it = ObjectList.iterator();
        ProcessObjects(it);
        safeToAdd = true;
        ProcessStaticUpdaters();
        ObjectDeletionAddition();
        IsoGridSquare.setLightcache(IsoGridSquare.getLightcache() - 1);
        IsoGridSquare.setRecalcLightTime(IsoGridSquare.getRecalcLightTime() - 1);
    }

    void renderAllJobs()
    {
    }

    void renderListAdd(IsoObject obj)
    {
        Texture tex = obj.getCurrentFrameTex();
        int id = 0;
        if(tex == null)
            id = 4999;
        else
            id = tex.getID();
        if(RenderJobsMapArray.containsKey(Integer.valueOf(id)))
        {
            id = ((Integer)RenderJobsMapArray.get(Integer.valueOf(id))).intValue();
        } else
        {
            RenderJobsMapArray.put(Integer.valueOf(id), Integer.valueOf(RenderJobsArray.size()));
            id = RenderJobsArray.size();
        }
        if(RenderJobsArray.size() <= id)
            RenderJobsArray.add(new NulledArrayList());
        ((NulledArrayList)RenderJobsArray.get(id)).add(obj);
    }

    IsoGridSquare getRandomFreeTile()
    {
        IsoGridSquare tile = null;
        boolean bDone = true;
        do
        {
            bDone = true;
            tile = getGridSquare(Rand.Next(width), Rand.Next(height), 0);
            if(tile == null)
                bDone = false;
            else
            if(!tile.isFree(false))
                bDone = false;
            else
            if(tile.getProperties().Is(IsoFlagType.solid) || tile.getProperties().Is(IsoFlagType.solidtrans))
                bDone = false;
            else
            if(tile.getMovingObjects().size() > 0)
                bDone = false;
            else
            if(tile.Has(IsoObjectType.stairsBN) || tile.Has(IsoObjectType.stairsMN) || tile.Has(IsoObjectType.stairsTN))
                bDone = false;
            else
            if(tile.Has(IsoObjectType.stairsBW) || tile.Has(IsoObjectType.stairsMW) || tile.Has(IsoObjectType.stairsTW))
                bDone = false;
        } while(!bDone);
        return tile;
    }

    IsoGridSquare getRandomOutdoorFreeTile()
    {
        IsoGridSquare tile = null;
        boolean bDone = true;
        do
        {
            bDone = true;
            tile = getGridSquare(Rand.Next(width), Rand.Next(height), 0);
            if(tile == null)
                bDone = false;
            else
            if(!tile.isFree(false))
                bDone = false;
            else
            if(tile.getRoom() != null)
                bDone = false;
            else
            if(tile.getProperties().Is(IsoFlagType.solid) || tile.getProperties().Is(IsoFlagType.solidtrans))
                bDone = false;
            else
            if(tile.getMovingObjects().size() > 0)
                bDone = false;
            else
            if(tile.Has(IsoObjectType.stairsBN) || tile.Has(IsoObjectType.stairsMN) || tile.Has(IsoObjectType.stairsTN))
                bDone = false;
            else
            if(tile.Has(IsoObjectType.stairsBW) || tile.Has(IsoObjectType.stairsMW) || tile.Has(IsoObjectType.stairsTW))
                bDone = false;
        } while(!bDone);
        return tile;
    }

    public IsoGridSquare getRandomFreeTileInRoom()
    {
        Stack rooms = new Stack();
        for(int n = 0; n < RoomList.size(); n++)
            if(((IsoRoom)RoomList.get(n)).TileList.size() > 9 && !((IsoRoom)RoomList.get(n)).Exits.isEmpty() && ((IsoGridSquare)((IsoRoom)RoomList.get(n)).TileList.get(0)).getProperties().Is(IsoFlagType.solidfloor))
                rooms.add(RoomList.get(n));

        IsoRoom room = (IsoRoom)rooms.get(Rand.Next(rooms.size()));
        return room.getFreeTile();
    }

    public void save(DataOutputStream output, boolean bDoChars)
        throws IOException
    {
        FileOutputStream outStream;
        Throwable throwable;
        if(SliceY.SliceBuffer == null)
            SliceY.SliceBuffer = ByteBuffer.allocate(0x989680);
        output.writeInt(width);
        output.writeInt(height);
        output.writeInt(MaxHeight);
        output.writeInt(ZoneStack.size());
        for(int n = 0; n < ZoneStack.size(); n++)
        {
            Zone z = (Zone)ZoneStack.get(n);
            output.writeInt(z.X);
            output.writeInt(z.Y);
            output.writeInt(z.W);
            output.writeInt(z.H);
            GameWindow.WriteString(output, z.Name);
        }

        NulledArrayList toSave = new NulledArrayList();
        for(int n = 0; n < LamppostPositions.size(); n++)
        {
            IsoLightSource z = (IsoLightSource)LamppostPositions.get(n);
            if(z.switches.isEmpty())
                toSave.add(z);
        }

        output.writeInt(toSave.size());
        for(int n = 0; n < toSave.size(); n++)
        {
            IsoLightSource z = (IsoLightSource)toSave.get(n);
            if(z.switches.isEmpty())
                output.writeInt(z.x);
            output.writeInt(z.y);
            output.writeInt(z.z);
            output.writeInt(z.radius);
            output.writeFloat(z.r);
            output.writeFloat(z.g);
            output.writeFloat(z.b);
            output.writeBoolean(z.bActive);
            output.writeBoolean(z.bWasActive);
        }

        VirtualZombieManager.instance.save(output);
        for(int n = 0; n < slices.length; n++)
            if(slices[n].bSaveDirty)
                slices[n].save(null);

        File outFile = new File((new StringBuilder()).append(GameWindow.getGameModeCacheDir()).append(File.separator).append(IsoWorld.instance.playerCell).append("map_mov.bin").toString());
        outStream = new FileOutputStream(outFile);
        throwable = null;
        try
        {
            BufferedOutputStream output2 = new BufferedOutputStream(outStream);
            SliceY.SliceBuffer.rewind();
            ArrayList ToSave = new ArrayList();
            for(int n = 0; n < ObjectList.size(); n++)
            {
                IsoMovingObject m = (IsoMovingObject)ObjectList.get(n);
                if(!(m instanceof IsoPhysicsObject) && !(m instanceof IsoDeadBody) && !(m instanceof IsoPlayer) && m.Serialize())
                    ToSave.add(m);
            }

            SliceY.SliceBuffer.putInt(ToSave.size());
            for(int n = 0; n < ToSave.size(); n++)
                ((IsoMovingObject)ToSave.get(n)).save(SliceY.SliceBuffer);

            output2.write(SliceY.SliceBuffer.array(), 0, SliceY.SliceBuffer.position());
            output2.flush();
        }
        catch(Throwable throwable1)
        {
            throwable = throwable1;
            throw throwable1;
        }
        /*      */     finally
        /*      */     {
        /* 4702 */       if (outStream != null) 
        						if (throwable != null) 
        							try { outStream.close(); } 
        						    catch (Throwable x2) { throwable.addSuppressed(x2); } 
        						else outStream.close();
        /*      */ 
        /*      */     }
        
        
        
        outFile = new File((new StringBuilder()).append(GameWindow.getGameModeCacheDir()).append(File.separator).append("map_t.bin").toString());
        outStream = new FileOutputStream(outFile);
        output = new DataOutputStream(outStream);
        GameTime.instance.save(output);
        outStream.close();
        if(!IsoPlayer.instance.isDead() && IsoPlayer.instance == IsoCamera.CamCharacter)
        {
            SliceY.SliceBuffer.rewind();
            outFile = new File((new StringBuilder()).append(GameWindow.getGameModeCacheDir()).append(File.separator).append("map_p.bin").toString());
            outStream = new FileOutputStream(outFile);
            BufferedOutputStream output2 = new BufferedOutputStream(outStream);
            IsoPlayer.getInstance().save(SliceY.SliceBuffer);
            output2.write(SliceY.SliceBuffer.array(), 0, SliceY.SliceBuffer.position());
            output2.flush();
            outStream.close();
        } else
        {
            File file = new File((new StringBuilder()).append(GameWindow.getGameModeCacheDir()).append(File.separator).append("map_p.bin").toString());
            if(file.exists())
                file.delete();
        }
        Iterator it = ContainerMap.values().iterator();
        do
        {
            if(it == null || !it.hasNext())
                break;
            ItemContainer c = (ItemContainer)it.next();
            int nfds;
            try
            {
                if(c.dirty)
                {
                    SliceY.SliceBuffer.rewind();
                    outFile = new File((new StringBuilder()).append(GameWindow.getGameModeCacheDir()).append(File.separator).append(IsoWorld.instance.playerCell).append("map_con_").append(c.ID).append(".bin").toString());
                    outStream = new FileOutputStream(outFile);
                    BufferedOutputStream output2 = new BufferedOutputStream(outStream);
                    c.save(SliceY.SliceBuffer);
                    output2.write(SliceY.SliceBuffer.array(), 0, SliceY.SliceBuffer.position());
                    output2.flush();
                    outStream.close();
                }
            }
            catch(Exception ex)
            {
                nfds = 0;
            }
        } while(true);
        return;
    }

    void LoadPlayer()
        throws FileNotFoundException, IOException
    {
        File inFile = new File((new StringBuilder()).append(GameWindow.getGameModeCacheDir()).append(File.separator).append("map_p.bin").toString());
        FileInputStream inStream = new FileInputStream(inFile);
        BufferedInputStream input = new BufferedInputStream(inStream);
        SliceY.SliceBuffer.rewind();
        input.read(SliceY.SliceBuffer.array());
        IsoPlayer.getInstance().load(SliceY.SliceBuffer);
        inStream.close();
    }

    public void loadCharacters()
        throws FileNotFoundException, IOException
    {
        File inFile = new File((new StringBuilder()).append(GameWindow.getGameModeCacheDir()).append(File.separator).append(IsoWorld.instance.playerCell).append("map_mov.bin").toString());
        FileInputStream inStream = new FileInputStream(inFile);
        BufferedInputStream input = new BufferedInputStream(inStream);
        SliceY.SliceBuffer.rewind();
        input.read(SliceY.SliceBuffer.array());
        int c = SliceY.SliceBuffer.getInt();
        for(int n = 0; n < c; n++)
        {
            IsoObject o = IsoObject.factoryFromFileInput(this, SliceY.SliceBuffer);
            if(o == null)
                continue;
            int fdsf;
            if(!(o instanceof IsoMovingObject))
                fdsf = 0;
            IsoMovingObject obj = (IsoMovingObject)o;
            if(obj != null)
                obj.load(SliceY.SliceBuffer);
        }

        for(int x = 0; x < width; x++)
        {
            for(int y = 0; y < height; y++)
            {
                for(int z = 0; z < MaxHeight; z++)
                {
                    IsoGridSquare sq = getGridSquare(x, y, z);
                    if(sq != null)
                        sq.RecalcShortcutPtrs(false);
                }

            }

        }

        inStream.close();
    }

    public void load(DataInputStream input, boolean loadPlayer)
        throws FileNotFoundException, IOException, ClassNotFoundException
    {
        int nZones = input.readInt();
        for(int n = 0; n < nZones; n++)
        {
            int x = input.readInt();
            int y = input.readInt();
            int w = input.readInt();
            int h = input.readInt();
            String str = GameWindow.ReadString(input);
            ZoneStack.add(new Zone(str, x, y, w, h, 0));
        }

        int nLamp = input.readInt();
        for(int n = 0; n < nLamp; n++)
        {
            int x = input.readInt();
            int y = input.readInt();
            int z = input.readInt();
            int rad = input.readInt();
            float r = input.readFloat();
            float g = input.readFloat();
            float b = input.readFloat();
            boolean bActive = input.readBoolean();
            boolean bWasActive = input.readBoolean();
            IsoLightSource l = new IsoLightSource(x, y, z, r, g, b, rad);
            l.bActive = bActive;
            l.bWasActive = bWasActive;
            LamppostPositions.add(l);
        }

        VirtualZombieManager.instance.load(input);
        for(int n = 0; n < height; n++)
        {
            slices[n] = new SliceY(this, width, 16, n);
            File inFile = new File((new StringBuilder()).append(GameWindow.getGameModeCacheDir()).append(File.separator).append(IsoWorld.instance.playerCell).append("map_").append(new Integer(n)).append(".bin").toString());
            FileInputStream inStream = new FileInputStream(inFile);
            BufferedInputStream input2 = new BufferedInputStream(inStream);
            slices[n].load(input2, null);
            inStream.close();
            slices[n].bSaveDirty = false;
        }

        for(Iterator it = ContainerMap.values().iterator(); it != null && it.hasNext();)
        {
            ItemContainer c = (ItemContainer)it.next();
            try
            {
                File inFile = new File((new StringBuilder()).append(GameWindow.getGameModeCacheDir()).append(File.separator).append(IsoWorld.instance.playerCell).append("map_con_").append(new Integer(c.ID)).append(".bin").toString());
                FileInputStream inStream = new FileInputStream(inFile);
                BufferedInputStream input2 = new BufferedInputStream(inStream);
                SliceY.SliceBuffer.rewind();
                input2.read(SliceY.SliceBuffer.array());
                c.load(SliceY.SliceBuffer);
                IsoWorld.instance.CurrentCell.ContainerMap.put(Integer.valueOf(c.ID), c);
                c.dirty = false;
                inStream.close();
            }
            catch(Exception ex) { }
        }

        for(int x = 0; x < width; x++)
        {
            for(int y = 0; y < height; y++)
            {
                for(int z = 0; z < MaxHeight; z++)
                    if(slices[y].Squares[x][z] != null)
                    {
                        slices[y].Squares[x][z].RecalcProperties();
                        slices[y].Squares[x][z].RecalcShortcutPtrs(false);
                        slices[y].bSaveDirty = false;
                    }

            }

        }

        if(FrameLoader.bServer)
            loadPlayer = false;
    }

    public static int MaxHeight = 1;
    private static int BarricadeDoorFrame = -1;
    private NulledArrayList BuildingList;
    private NulledArrayList ObjectList;
    private NulledArrayList PushableObjectList;
    private THashMap BuildingScores;
    private AStarPathMap PathMap;
    private NulledArrayList RoomList;
    private NulledArrayList StaticUpdaterObjectList;
    private NulledArrayList wallArray;
    private NulledArrayList ZombieList;
    private NulledArrayList RemoteSurvivorList;
    private NulledArrayList GhostList;
    private NulledArrayList ZoneStack;
    private NulledArrayList removeList;
    private NulledArrayList addList;
    private NulledArrayList RenderJobsArray;
    private NulledArrayList ProcessItems;
    private NulledArrayList ProcessItemsRemove;
    private THashMap RenderJobsMapArray;
    private SliceY slices[];
    private int height;
    private int width;
    private int worldX;
    private int worldY;
    private Stack Containers;
    public HashMap ContainerMap;
    private static int SheetCurtains = -1;
    public String filename;
    public int DangerScore[][];
    private boolean safeToAdd;
    private Stack LamppostPositions;
    public NulledArrayList roomDefs;
    private static Stack buildingscores = new Stack();
    boolean everDone;
    public boolean recalcFloors;
    private HashSet stairsNodes;
    public boolean bDoLotConnect;
    NulledArrayList SurvivorList;
    int jumptot;
    int jumpcount;
    int jumpavr;
    private Stack tempZoneStack;
    public int vertLights[][][][];
    private int buildVertLights[][][][];
    static int colu = 0;
    static int coll = 0;
    static int colr = 0;
    static int cold = 0;
    private static Texture texWhite;
    private int currentLX;
    private int currentLY;
    private int currentLZ;
    int recalcShading;
    int lastMinX;
    int lastMinY;
    OnceEvery lightUpdate;
    int alt;
    public Color staticBlack;
    Thread lightingThread;
    int lightUpdateCount;
    public boolean bSwappingLightBuffers;
    public boolean bRendering;
    private IsoSprite woodWallN;
    private IsoSprite woodWallW;
    private IsoSprite woodDWallN;
    private IsoSprite woodDWallW;
    private IsoSprite woodWWallN;
    private IsoSprite woodWWallW;
    private IsoSprite woodDoorW;
    private IsoSprite woodDoorN;
    private IsoSprite woodFloor;
    private IsoSprite woodBarricade;
    private IsoSprite woodCrate;
    private IsoSprite woodStairsNB;
    private IsoSprite woodStairsNM;
    private IsoSprite woodStairsNT;
    private IsoSprite woodStairsWB;
    private IsoSprite woodStairsWM;
    private IsoSprite woodStairsWT;
    private int minX;
    private int maxX;
    private int minY;
    private int maxY;
    private int minZ;
    private int maxZ;
    private OnceEvery dangerUpdate;
    private Thread LightInfoUpdate;
















}
