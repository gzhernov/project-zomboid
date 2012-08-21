// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   IsoWorld.java

package zombie.iso;

import gnu.trove.map.hash.THashMap;
import java.io.*;
import java.security.MessageDigest;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import org.lwjgl.opengl.Display;
import org.xml.sax.SAXException;
import se.krka.kahlua.integration.LuaCaller;
import se.krka.kahlua.vm.KahluaTable;
import zombie.*;
import zombie.Lua.LuaManager;
import zombie.*;
import zombie.behaviors.survivor.orders.FollowOrder;
import zombie.characters.*;
import zombie.core.Collections.NulledArrayList;
import zombie.core.Core;
import zombie.core.Rand;
import zombie.core.properties.PropertyContainer;
import zombie.core.utils.OnceEvery;
import zombie.gameStates.LoadGamePopupState;
import zombie.input.Mouse;
import zombie.inventory.ItemContainer;
import zombie.inventory.ItemContainerFiller;
import zombie.iso.SpriteDetails.IsoFlagType;
import zombie.iso.SpriteDetails.IsoObjectType;
import zombie.iso.areas.IsoArea;
import zombie.iso.areas.IsoBuilding;
import zombie.iso.objects.RainManager;
import zombie.iso.sprite.IsoSprite;
import zombie.iso.sprite.IsoSpriteManager;
import zombie.loading.LoadingScreen;
import zombie.scripting.ScriptManager;
import zombie.scripting.objects.ScriptCharacter;
import zombie.ui.TextManager;
import zombie.ui.TutorialManager;

// Referenced classes of package zombie.iso:
//            IsoGridSquare, IsoCell, CellLoader, IsoDirections, 
//            IsoUtils, IsoObjectPicker, IsoCamera, IsoMovingObject

public class IsoWorld
{
    public class Frame
    {

        public NulledArrayList xPos;
        public NulledArrayList yPos;
        public NulledArrayList Type;
       

        public Frame()
        {
          
            xPos = new NulledArrayList();
            yPos = new NulledArrayList();
            Type = new NulledArrayList();
            Iterator it = IsoWorld.instance.CurrentCell.getObjectList().iterator();
            do
            {
                if(it == null || !it.hasNext())
                    break;
                IsoMovingObject o = (IsoMovingObject)it.next();
                int type = 2;
                if(o instanceof IsoPlayer)
                    type = 0;
                else
                if(o instanceof IsoSurvivor)
                {
                    type = 1;
                } else
                {
                    if(!(o instanceof IsoZombie) || ((IsoZombie)o).Ghost)
                        continue;
                    type = 2;
                }
                xPos.add(Integer.valueOf((int)o.getX()));
                yPos.add(Integer.valueOf((int)o.getY()));
                Type.add(Integer.valueOf(type));
            } while(true);
        }
    }

    public static class MetaCell
    {

        public int x;
        public int y;
        public int zombieCount;
        public IsoDirections zombieMigrateDirection;
        public int from[][];

        public MetaCell()
        {
            from = new int[3][3];
        }
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

    private void UpdateMetaZombiesTick()
    {
        int nPrefered = GetCellZombieCount(this.x, this.y);
        if(nPrefered > 660)
            nPrefered = 660;
        for(int x = 0; x < 100; x++)
        {
            for(int y = 0; y < 100; y++)
            {
                for(int n = 0; n <= 2; n++)
                {
                    for(int m = 0; m <= 2; m++)
                    {
                        if(n == 1 && m == 1 || MetaCellGrid[x][y].from[n][m] <= 0)
                            continue;
                        float am = MetaCellGrid[x][y].from[n][m];
                        MetaCellGrid[x][y].from[n][m] = 0;
                        MetaCellGrid[x][y].zombieCount += am;
                        if(x != this.x || y != this.y)
                            continue;
                        nPrefered = (int)((float)nPrefered + am);
                        if(nPrefered > 660)
                            nPrefered = 660;
                        int nZombies = instance.CurrentCell.getZombieList().size();
                        boolean bDoSound = false;
                        if(nPrefered <= nZombies)
                            continue;
                        if((float)nPrefered < (float)nZombies + am)
                            am = nPrefered - nZombies;
                        AddZombieGroup(am, n, m);
                        nZombies = instance.CurrentCell.getZombieList().size();
                        bDoSound = true;
                    }

                }

            }

        }

        if(IsoArea.Doobo)
            caboltoo = true;
    }

    private void UpdateMetaZombies()
    {
        for(int x = 0; x < 100; x++)
        {
            for(int y = 0; y < 100; y++)
            {
                if(Rand.Next(24) == 0)
                {
                    int numToMigrate = Rand.Next(MetaCellGrid[x][y].zombieCount);
                    if(numToMigrate > 30)
                        numToMigrate = 30;
                    if(numToMigrate == 0 && MetaCellGrid[x][y].zombieCount > 0)
                        numToMigrate = 1;
                    int tx = x;
                    int ty = y;
                    tx += Rand.Next(2) - 1;
                    ty += Rand.Next(2) - 1;
                    if(tx < 0 || ty < 0 || tx >= 100 || ty >= 100)
                        continue;
                    if(tx != x || ty != y)
                    {
                        MetaCellGrid[x][y].zombieCount -= numToMigrate;
                        MetaCellGrid[tx][ty].from[2 - ((tx - x) + 1)][2 - ((ty - y) + 1)] += numToMigrate;
                    }
                }
                if(Rand.Next(3) != 0)
                    continue;
                double a = 0.0D;
                KahluaTable table = LuaManager.env;
                a = 1.0D;
                int numToMigrate = Rand.Next(MetaCellGrid[x][y].zombieCount);
                if(numToMigrate > 30)
                    numToMigrate = 30;
                numToMigrate = (int)((double)numToMigrate * a);
                if(numToMigrate == 0 && MetaCellGrid[x][y].zombieCount > 0)
                    numToMigrate = 1;
                int tx = x;
                int ty = y;
                if(x < this.x)
                    tx++;
                else
                if(x > this.x)
                    tx--;
                if(y < this.y)
                    ty++;
                else
                if(y > this.y)
                    ty--;
                if(tx != x || ty != y)
                {
                    MetaCellGrid[x][y].zombieCount -= numToMigrate;
                    MetaCellGrid[tx][ty].from[2 - ((tx - x) + 1)][2 - ((ty - y) + 1)] += numToMigrate;
                }
                if((x == 0 || y == 0 || x == 99 || y == 99) && Rand.Next(8) == 0)
                    MetaCellGrid[x][y].zombieCount++;
            }

        }

    }

    private void LoadRemotenessVars()
    {
        KahluaTable table = LuaManager.env;
        Object o[] = LuaManager.caller.pcall(LuaManager.thread, table.rawget("cellSurvivorSpawns"), new Object[] {
            Integer.valueOf(instance.x), Integer.valueOf(instance.y)
        });
        if(o.length > 1)
            cellSurvivorSpawns = ((Double)o[1]).intValue();
        o = LuaManager.caller.pcall(LuaManager.thread, table.rawget("cellSurvivorRemoteness"), new Object[] {
            Integer.valueOf(instance.x), Integer.valueOf(instance.y)
        });
        if(o.length > 1)
            cellRemoteness = ((Double)o[1]).intValue();
    }

    public IsoWorld()
    {
        movex = 0;
        movey = 0;
        x = 50;
        y = 50;
        playerCell = "suburbs1";
        Groups = new Stack();
        TotalSurvivorsDead = 0;
        TotalSurvivorNights = 0;
        SurvivorSurvivalRecord = 0;
        SurvivorDescriptors = new HashMap();
        caboltoo = false;
        MetaCellGrid = new MetaCell[100][100];
        bLoaded = false;
        cellMap = new String[10][10];
        worldX = 0;
        worldY = 0;
    }

    public void CreateSurvivorGroup(IsoGridSquare sq, IsoPlayer player)
    {
        int c = Rand.Next(4);
        SurvivorDesc desc = SurvivorFactory.CreateSurvivor();
        IsoSurvivor m = CreateRandomSurvivor(desc, sq, player);
        if(m == null)
            return;
        instance.Groups.add(desc.getGroup());
        if(IsoPlayer.DemoMode)
            c = 0;
        for(int n = 0; n < c; n++)
        {
            SurvivorDesc descB = SurvivorFactory.CreateSurvivor();
            IsoGridSquare or = sq;
            do
            {
                sq = or.getCell().getGridSquare(or.getX() + (Rand.Next(10) - 5), or.getY() + (Rand.Next(10) - 5), or.getZ());
                if(sq != null)
                    sq.setCachedIsFree(false);
            } while(sq == null || !sq.isFree(true));
            IsoSurvivor s = CreateRandomSurvivor(descB, sq, player);
            if(s != null)
            {
                descB.AddToGroup(desc.getGroup());
                descB.getMetCount().put(Integer.valueOf(desc.getID()), Integer.valueOf(100));
                desc.getMetCount().put(Integer.valueOf(descB.getID()), Integer.valueOf(100));
                descB.getInstance().GiveOrder(new FollowOrder(descB.getInstance(), desc.getInstance(), 3), true);
            }
        }

    }

    public IsoSurvivor CreateRandomSurvivor(SurvivorDesc desc, IsoGridSquare sq, IsoPlayer player)
    {
        int nCount = 0;
        if(sq.getW() != null)
            nCount++;
        if(sq.getS() != null)
            nCount++;
        if(sq.getN() != null)
            nCount++;
        if(sq.getE() != null)
            nCount++;
        if(nCount <= 1)
            return null;
        IsoSurvivor gunnut = null;
        gunnut = new IsoSurvivor(zombie.characters.SurvivorPersonality.Personality.GunNut, desc, instance.CurrentCell, sq.getX(), sq.getY(), sq.getZ());
        if(Rand.Next(4) == 0)
            gunnut.getInventory().AddItem("Base.Plank");
        if(Rand.Next(4) == 0)
            gunnut.getInventory().AddItem("Base.Plank");
        if(Rand.Next(4) == 0)
            gunnut.getInventory().AddItem("Base.Nails");
        if(Rand.Next(4) == 0)
            gunnut.getInventory().AddItem("Base.Nails");
        if(Rand.Next(4) == 0)
            gunnut.getInventory().AddItem("Base.Nails");
        if(Rand.Next(4) == 0)
            gunnut.getInventory().AddItem("Base.Hammer");
        if(Rand.Next(4) == 0)
            gunnut.getInventory().AddItem("Base.Sheet");
        if(Rand.Next(4) == 0)
            gunnut.getInventory().AddItem("Base.Sheet");
        if(Rand.Next(4) == 0)
            gunnut.getInventory().AddItem("Base.Sheet");
        if(Rand.Next(4) == 0)
            gunnut.getInventory().AddItem("Base.Torch");
        gunnut.getInventory().AddItem("Base.WaterBottleFull");
        desc.setInstance(gunnut);
        switch(Rand.Next(11))
        {
        case 0: // '\0'
        case 1: // '\001'
            gunnut.getInventory().AddItem("Base.Hammer");
            break;

        case 2: // '\002'
        case 3: // '\003'
            gunnut.getInventory().AddItem("Base.Plank");
            break;

        case 4: // '\004'
            gunnut.getInventory().AddItem("Base.BaseballBatNails");
            break;

        case 5: // '\005'
        case 6: // '\006'
            gunnut.getInventory().AddItem("Base.Axe");
            break;

        case 7: // '\007'
        case 8: // '\b'
            gunnut.getInventory().AddItem("Base.BaseballBat");
            break;

        case 9: // '\t'
        case 10: // '\n'
            gunnut.getInventory().AddItem("Base.Shotgun");
            gunnut.getInventory().AddItem("Base.ShotgunShells");
            gunnut.getInventory().AddItem("Base.ShotgunShells");
            gunnut.getInventory().AddItem("Base.ShotgunShells");
            break;
        }
        gunnut.setAllowBehaviours(true);
        return gunnut;
    }

    public void CreateSwarm(int i, int j, int k, int l, int i1)
    {
    }

    public void ForceKillAllZombies()
    {
        GameTime.getInstance().RemoveZombiesIndiscriminate(1000);
    }

    public static int readInt(RandomAccessFile in)
        throws EOFException, IOException
    {
        int ch1 = in.read();
        int ch2 = in.read();
        int ch3 = in.read();
        int ch4 = in.read();
        if((ch1 | ch2 | ch3 | ch4) < 0)
            throw new EOFException();
        else
            return (ch1 << 0) + (ch2 << 8) + (ch3 << 16) + (ch4 << 24);
    }

    public static String readString(RandomAccessFile in)
        throws EOFException, IOException
    {
        String str = in.readLine();
        return str;
    }

    public void LoadTileDefinitions(IsoSpriteManager sprMan, String filename)
    {
        try
        {
            File fo = new File(filename);
            RandomAccessFile in = new RandomAccessFile(fo.getAbsolutePath(), "r");
            int numTilesheets = readInt(in);
            for(int n = 0; n < numTilesheets; n++)
            {
                String str = readString(in);
                String name = str.trim();
                readString(in);
                int fd;
                if(name.contains("Barricade"))
                    fd = 0;
                int wTiles = readInt(in);
                int hTiles = readInt(in);
                int nTiles = readInt(in);
                for(int m = 0; m < nTiles; m++)
                {
                    IsoSprite spr = sprMan.AddSprite((new StringBuilder()).append(name).append("_").append(m).toString());
                    spr.setName((new StringBuilder()).append(name).append("_").append(m).toString());
                    int fdfd;
                    if(spr.name.equals("TileFrames_13"))
                        fdfd = 0;
                    int nProps = readInt(in);
                    for(int l = 0; l < nProps; l++)
                    {
                        str = readString(in);
                        String prop = str.trim();
                        str = readString(in);
                        String val = str.trim();
                        IsoObjectType type = IsoObjectType.FromString(prop);
                        if(type != IsoObjectType.MAX)
                            spr.setType(type);
                        int fds;
                        if("windowW".equals(prop))
                            fds = 0;
                        spr.getProperties().Set(prop, val);
                        if(prop.equals("interior") && val.equals("false"))
                            spr.getProperties().Set(IsoFlagType.exterior);
                        if(prop.equals("WallN"))
                        {
                            spr.getProperties().Set(IsoFlagType.collideN);
                            spr.getProperties().Set(IsoFlagType.cutN);
                        } else
                        if(prop.equals("WallNTrans"))
                        {
                            spr.getProperties().Set(IsoFlagType.collideN, "true");
                            spr.getProperties().Set(IsoFlagType.cutN, "true");
                            spr.getProperties().Set(IsoFlagType.transparentN, "true");
                            spr.setType(IsoObjectType.wall);
                        } else
                        if(prop.equals("WallW"))
                        {
                            spr.getProperties().Set(IsoFlagType.collideW);
                            spr.getProperties().Set(IsoFlagType.cutW);
                            spr.setType(IsoObjectType.wall);
                        } else
                        if(prop.equals("WallWTrans"))
                        {
                            spr.getProperties().Set(IsoFlagType.collideW, "true");
                            spr.getProperties().Set(IsoFlagType.transparentW, "true");
                            spr.getProperties().Set(IsoFlagType.cutW, "true");
                            spr.setType(IsoObjectType.wall);
                        } else
                        if(prop.equals("DoorWallN"))
                            spr.getProperties().Set(IsoFlagType.cutN);
                        else
                        if(prop.equals("DoorWallW"))
                            spr.getProperties().Set(IsoFlagType.cutW);
                        else
                        if(prop.equals("WallNW"))
                        {
                            spr.getProperties().Set(IsoFlagType.collideN, "true");
                            spr.getProperties().Set(IsoFlagType.cutN, "true");
                            spr.getProperties().Set(IsoFlagType.collideW, "true");
                            spr.getProperties().Set(IsoFlagType.cutW, "true");
                            spr.setType(IsoObjectType.wall);
                        } else
                        if(prop.equals("WallNWTrans"))
                        {
                            spr.getProperties().Set(IsoFlagType.collideN, "true");
                            spr.getProperties().Set(IsoFlagType.cutN, "true");
                            spr.getProperties().Set(IsoFlagType.collideW, "true");
                            spr.getProperties().Set(IsoFlagType.transparentN, "true");
                            spr.getProperties().Set(IsoFlagType.transparentW, "true");
                            spr.getProperties().Set(IsoFlagType.cutW, "true");
                            spr.setType(IsoObjectType.wall);
                        } else
                        if(prop.equals("WallSE"))
                            spr.getProperties().Set(IsoFlagType.cutW, "true");
                        else
                        if(prop.equals("WindowW"))
                        {
                            spr.getProperties().Set(IsoFlagType.cutW, "true");
                            spr.getProperties().Set(IsoFlagType.transparentW, "true");
                            spr.setType(IsoObjectType.windowFW);
                        } else
                        if(prop.equals("WindowN"))
                        {
                            spr.getProperties().Set(IsoFlagType.cutN, "true");
                            spr.getProperties().Set(IsoFlagType.transparentN, "true");
                            spr.setType(IsoObjectType.windowFN);
                        } else
                        if(prop.equals("UnbreakableWindowW"))
                        {
                            spr.getProperties().Set(IsoFlagType.cutW, "true");
                            spr.getProperties().Set(IsoFlagType.transparentW, "true");
                            spr.getProperties().Set(IsoFlagType.collideW, "true");
                            spr.setType(IsoObjectType.wall);
                        } else
                        if(prop.equals("UnbreakableWindowN"))
                        {
                            spr.getProperties().Set(IsoFlagType.cutN, "true");
                            spr.getProperties().Set(IsoFlagType.transparentN, "true");
                            spr.getProperties().Set(IsoFlagType.collideN, "true");
                            spr.setType(IsoObjectType.wall);
                        } else
                        if(prop.equals("UnbreakableWindowNW"))
                        {
                            spr.getProperties().Set(IsoFlagType.cutN, "true");
                            spr.getProperties().Set(IsoFlagType.transparentN, "true");
                            spr.getProperties().Set(IsoFlagType.collideN, "true");
                            spr.getProperties().Set(IsoFlagType.cutN, "true");
                            spr.getProperties().Set(IsoFlagType.collideW, "true");
                            spr.getProperties().Set(IsoFlagType.cutW, "true");
                            spr.setType(IsoObjectType.wall);
                        }
                        if(prop.equals("name"))
                            spr.setParentObjectName(val);
                    }

                    if(spr.getProperties().Is(IsoFlagType.HoppableW))
                        spr.getProperties().UnSet(IsoFlagType.collideW);
                    if(spr.getProperties().Is(IsoFlagType.HoppableN))
                        spr.getProperties().UnSet(IsoFlagType.collideN);
                }

            }

        }
        catch(Exception ex)
        {
            Logger.getLogger(GameApplet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void LoadPlayerCell()
        throws FileNotFoundException, IOException
    {
        File inFile = new File((new StringBuilder()).append(GameWindow.getGameModeCacheDir()).append(File.separator).append("pc.bin").toString());
        if(inFile.exists())
        {
            FileInputStream inStream = new FileInputStream(inFile);
            DataInputStream input = new DataInputStream(inStream);
            instance.x = input.readInt();
            instance.y = input.readInt();
            instance.playerCell = GameWindow.ReadString(input);
            inStream.close();
        }
    }

    public int GetCellZombieCount(int x, int y)
    {
        if(x < 0 || y < 0 || x >= 100 || y >= 100)
            return 660;
        else
            return MetaCellGrid[x][y].zombieCount;
    }

    public void init()
    {
        MetaCellGrid = new MetaCell[100][100];
        for(int x = 0; x < 100; x++)
        {
            for(int y = 0; y < 100; y++)
            {
                MetaCellGrid[x][y] = new MetaCell();
                MetaCellGrid[x][y].x = x;
                MetaCellGrid[x][y].y = y;
                KahluaTable table = LuaManager.env;
                Object o[] = LuaManager.caller.pcall(LuaManager.thread, table.rawget("getStartZombiesByGrid"), new Object[] {
                    Integer.valueOf(x), Integer.valueOf(y)
                });
                if(o.length > 1)
                    MetaCellGrid[x][y].zombieCount = ((Double)o[1]).intValue();
                else
                    MetaCellGrid[x][y].zombieCount = 0;
            }

        }

        SurvivorDescriptors.clear();
        ItemContainer.IDMax = 0;
        spriteManager = new IsoSpriteManager();
        LoadTileDefinitions(spriteManager, "media/tiledefinitions.tiles");
        try
        {
            if(LoadGamePopupState.bLoadCharacter)
                LoadPlayerCell();
            String oplayerCell = playerCell;
            playerCell = (new StringBuilder()).append(this.x).append("_").append(this.y).toString();
            KahluaTable table = LuaManager.env;
            Object o[] = LuaManager.caller.pcall(LuaManager.thread, table.rawget("getMainCellLot"), new Object[] {
                Integer.valueOf(instance.x), Integer.valueOf(instance.y)
            });
            if(o.length > 1)
                oplayerCell = (String)o[1];
            LoadRemotenessVars();
            File file = new File((new StringBuilder()).append(GameWindow.getGameModeCacheDir()).append(File.separator).append(playerCell).append("map.bin").toString());
            if(file.exists() && LoadGamePopupState.bLoadGame)
            {
                bLoaded = true;
                CellLoader.wanderX = 0;
                CellLoader.wanderY = 0;
                CellLoader.wanderRoom = null;
                CurrentCell = CellLoader.LoadCellFromData(spriteManager, playerCell, true);
                if(LoadGamePopupState.bLoadCharacter)
                {
                    SurvivorDesc desca = SurvivorFactory.CreateSurvivor();
                    IsoGridSquare sq = null;
                    if(!CurrentCell.getRoomList().isEmpty())
                        sq = CurrentCell.getRandomFreeTileInRoom();
                    else
                        sq = CurrentCell.getRandomFreeTile();
                    IsoPlayer player = new IsoPlayer(instance.CurrentCell);
                    CurrentCell.LoadPlayer();
                } else
                {
                    ScriptCharacter splayer = ScriptManager.instance.FindCharacter("Player");
                    if(splayer == null)
                    {
                        SurvivorDesc desc2 = SurvivorFactory.CreateSurvivor();
                        IsoGridSquare sq = null;
                        if(!CurrentCell.getRoomList().isEmpty())
                            sq = CurrentCell.getRandomFreeTileInRoom();
                        else
                            sq = CurrentCell.getRandomFreeTile();
                        IsoPlayer player = new IsoPlayer(instance.CurrentCell, desc2, sq.getX(), sq.getY(), sq.getZ());
                    }
                }
            } else
            {
                CurrentCell = CellLoader.LoadCellBinary(spriteManager, oplayerCell, (new StringBuilder()).append("media/lots/").append(oplayerCell).append(".lot").toString(), mapUseJar);
                PopulateCellWithZombies();
            }
        }
        catch(ParserConfigurationException ex)
        {
            Logger.getLogger(IsoWorld.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch(SAXException ex)
        {
            Logger.getLogger(IsoWorld.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch(IOException ex)
        {
            Logger.getLogger(IsoWorld.class.getName()).log(Level.SEVERE, null, ex);
        }
        TutorialManager.instance.ActiveControlZombies = false;
        SurvivorDesc desc2 = null;
        if(!bLoaded && !FrameLoader.bServer && !FrameLoader.bClient)
        {
            ScriptCharacter splayer = ScriptManager.instance.FindCharacter("Player");
            if(splayer == null)
            {
                desc2 = SurvivorFactory.CreateSurvivor();
                IsoGridSquare sq = null;
                if(!CurrentCell.getRoomList().isEmpty())
                    sq = CurrentCell.getRandomFreeTileInRoom();
                else
                    sq = CurrentCell.getRandomFreeTile();
                IsoPlayer player = new IsoPlayer(instance.CurrentCell, desc2, sq.getX(), sq.getY(), sq.getZ());
                player.dir = IsoDirections.SE;
                CurrentCell.getZoneStack().add(new IsoCell.Zone("tutArea", sq.getX() - 15, sq.getY() - 15, 30, 30, 0));
                int count = 0;
                int maxcount = cellSurvivorSpawns;
                if(!FrameLoader.bClient && IsoPlayer.instance != null)
                    while(count < maxcount) 
                    {
                        boolean bDone = false;
                        do
                        {
                            if(bDone)
                                break;
                            bDone = true;
                            sq = CurrentCell.getGridSquare(Rand.Next(CurrentCell.getWidthInTiles()), Rand.Next(CurrentCell.getHeightInTiles()), 0);
                            if(sq != null)
                            {
                                if(sq.getProperties().Is(IsoFlagType.solidtrans) || sq.getProperties().Is(IsoFlagType.solid) || !sq.getProperties().Is(IsoFlagType.solidfloor))
                                    bDone = false;
                                else
                                if(!IsoPlayer.DemoMode && IsoUtils.DistanceManhatten(sq.getX(), sq.getY(), IsoPlayer.instance.x, IsoPlayer.instance.y) < 20F)
                                    bDone = false;
                                else
                                if(bDone)
                                    CreateSurvivorGroup(sq, IsoPlayer.instance);
                            } else
                            {
                                bDone = false;
                            }
                        } while(true);
                        count = 0;
                        Iterator it = CurrentCell.getObjectList().iterator();
                        while(it != null && it.hasNext()) 
                            if(it.next() instanceof IsoSurvivor)
                                count++;
                    }
            }
        }
        if(IsoPlayer.getInstance() != null)
            IsoPlayer.getInstance().setCurrent(CurrentCell.getGridSquare((int)IsoPlayer.getInstance().getX(), (int)IsoPlayer.getInstance().getY(), (int)IsoPlayer.getInstance().getZ()));
        if(!bLoaded)
        {
            if(!CurrentCell.getBuildingList().isEmpty())
            {
                int houses = 30;
                KahluaTable table = LuaManager.env;
                Object o[] = LuaManager.caller.pcall(LuaManager.thread, table.rawget("getStartIndoorZombiesByGrid"), new Object[] {
                    Integer.valueOf(this.x), Integer.valueOf(this.y)
                });
                if(o.length > 1)
                    houses = ((Double)o[1]).intValue();
            }
            ScriptCharacter skate = ScriptManager.instance.getCharacter("KateAndBaldspot.Kate");
            if(skate != null)
                TutorialManager.instance.wife = (IsoSurvivor)skate.Actual;
            if(!bLoaded)
                PopulateCellWithSurvivors();
        }
    }

    public void moveArea(String map, float x, float y, float z)
    {
        playerCell = map;
        CurrentCell.Dispose();
        System.gc();
        IsoPlayer.getInstance().setCurrent(null);
        IsoPlayer.getInstance().setLast(null);
        Core.getInstance().StartFrame();
        Core.getInstance().EndFrame();
        Core.getInstance().StartFrameUI();
        TextManager.instance.DrawStringCentre(Core.getInstance().getOffscreenWidth() / 2, Core.getInstance().getOffscreenHeight() / 2, "Loading", 1.0F, 1.0F, 1.0F, 1.0F);
        Core.getInstance().EndFrameUI();
        boolean bLoaded = false;
        try
        {
            File file = new File((new StringBuilder()).append(GameWindow.getGameModeCacheDir()).append(File.separator).append(playerCell).append("map.bin").toString());
            if(file.exists())
                bLoaded = true;
            else
                CurrentCell = CellLoader.LoadCell((String)ScriptManager.instance.MapMap.get(playerCell), mapUseJar);
        }
        catch(ParserConfigurationException ex)
        {
            Logger.getLogger(IsoWorld.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch(SAXException ex)
        {
            Logger.getLogger(IsoWorld.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch(IOException ex)
        {
            Logger.getLogger(IsoWorld.class.getName()).log(Level.SEVERE, null, ex);
        }
        IsoPlayer.getInstance().setX(IsoPlayer.instance.lx = IsoPlayer.instance.nx = IsoPlayer.instance.scriptnx = x);
        IsoPlayer.getInstance().setY(IsoPlayer.instance.ly = IsoPlayer.instance.ny = IsoPlayer.instance.scriptny = y);
        IsoPlayer.getInstance().setZ(IsoPlayer.instance.lz = z);
        CurrentCell.getAddList().add(IsoPlayer.getInstance());
        if(!bLoaded)
        {
            PopulateCellWithSurvivors();
            PopulateCellWithZombies();
        }
        IsoPlayer.getInstance().setCurrent(CurrentCell.getGridSquare((int)IsoPlayer.getInstance().getX(), (int)IsoPlayer.getInstance().getY(), (int)IsoPlayer.getInstance().getZ()));
        IsoPlayer.getInstance().setLast(null);
    }

    public void KillCell()
    {
        CollisionManager.instance.ContactMap.clear();
        PathfindManager.instance.stop();
        IsoObjectPicker.Instance.Init();
        WorldSoundManager.instance.SoundList.clear();
        for(int n = 0; n < WorldSoundManager.instance.Cache.length; n++)
            WorldSoundManager.instance.Cache[n] = null;

        CurrentCell.Dispose();
        CurrentCell = null;
        CellLoader.wanderRoom = null;
        IsoGameCharacter.getSurvivorMap().clear();
        IsoPlayer.getInstance().setCurrent(null);
        IsoPlayer.getInstance().setLast(null);
        IsoPlayer.getInstance().square = null;
        ItemContainerFiller.Containers.clear();
        ItemContainerFiller.DistributionTarget.clear();
        instance.Groups.clear();
        RainManager.reset();
        System.gc();
    }

    public void moveArea(int x, int y)
        throws ParserConfigurationException, SAXException, IOException
    {
        bLoaded = false;
        GameWindow.save(true);
        Core.getInstance().StartFrame();
        Core.getInstance().EndFrame();
        Core.getInstance().StartFrameUI();
        TextManager.instance.DrawStringCentre(Core.getInstance().getOffscreenWidth() / 2, Core.getInstance().getOffscreenHeight() / 2, "Loading", 1.0F, 1.0F, 1.0F, 1.0F);
        Core.getInstance().EndFrameUI();
        Display.update();
        if(x == 1)
            IsoPlayer.getInstance().setX(1.0F);
        else
        if(x == -1)
            IsoPlayer.getInstance().setX((float)(CurrentCell.getWidthInTiles() - 1) - 0.5F);
        if(y == 1)
            IsoPlayer.getInstance().setY(1.0F);
        else
        if(y == -1)
            IsoPlayer.getInstance().setY((float)(CurrentCell.getHeightInTiles() - 1) - 0.5F);
        KillCell();
        instance.x += x;
        instance.y += y;
        String oplayerCell = playerCell;
        KahluaTable table = LuaManager.env;
        Object o[] = LuaManager.caller.pcall(LuaManager.thread, table.rawget("getMainCellLot"), new Object[] {
            Integer.valueOf(instance.x), Integer.valueOf(instance.y)
        });
        if(o.length > 1)
            oplayerCell = (String)o[1];
        playerCell = (new StringBuilder()).append(instance.x).append("_").append(instance.y).toString();
        File file = new File((new StringBuilder()).append(GameWindow.getGameModeCacheDir()).append(File.separator).append(playerCell).append("map.bin").toString());
        if(file.exists())
        {
            bLoaded = true;
            CurrentCell = CellLoader.LoadCellFromData(spriteManager, playerCell, true);
        } else
        {
            CurrentCell = CellLoader.LoadCellBinary(spriteManager, oplayerCell, (new StringBuilder()).append("media/lots/").append(oplayerCell).append(".lot").toString(), mapUseJar);
        }
        LoadRemotenessVars();
        PathfindManager.instance.init();
        CurrentCell.getAddList().add(IsoPlayer.getInstance());
        IsoPlayer.getInstance().setCurrent(CurrentCell.getGridSquare((int)IsoPlayer.getInstance().getX(), (int)IsoPlayer.getInstance().getY(), (int)IsoPlayer.getInstance().getZ()));
    }

    public void moveAreaNextFrame(int x, int y)
    {
        movex = x;
        movey = y;
    }

    public void PopulateCellWithZombies()
    {
        if(bLoaded)
            return;
        VirtualZombieManager.instance = new VirtualZombieManager();
        VirtualZombieManager.instance.init();
        int nPrefered = GetCellZombieCount(x, y);
        if(nPrefered < instance.CurrentCell.getZombieList().size())
        {
            int dif = instance.CurrentCell.getZombieList().size() - nPrefered;
            for(int n = 0; n < dif && !CurrentCell.getZombieList().isEmpty(); n++)
            {
                IsoZombie z = (IsoZombie)CurrentCell.getZombieList().get(Rand.Next(instance.CurrentCell.getZombieList().size()));
                CurrentCell.getZombieList().remove(z);
                CurrentCell.getObjectList().remove(z);
                z.current.getMovingObjects().remove(z);
            }

        }
        VirtualZombieManager.instance.AddZombiesToMap(nPrefered);
        float percent = 60F;
        int nZombies = 50;
        float percentPerZombie = 40F / (float)nZombies;
        Mouse.loop();
        if(!IsoPlayer.DemoMode)
            while(!LoadingScreen.RenderLoadingScreen((int)percent, true)) ;
    }

    public void PopulateCellWithZombies(int i)
    {
    }

    public void render()
    {
        if(caboltoo)
            IsoSprite.maxCount = 124;
        try
        {
            CurrentCell.render();
        }
        catch(InterruptedException ex)
        {
            Logger.getLogger(IsoWorld.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void UpdateZombies()
    {
        int nZombies = instance.CurrentCell.getZombieList().size();
        int nPrefered = GetCellZombieCount(x, y);
        if(nPrefered > 660)
            nPrefered = 660;
        boolean bDoSound = false;
        if(nPrefered > nZombies)
        {
            nZombies = instance.CurrentCell.getZombieList().size();
            bDoSound = true;
        }
        if(!bDoSound);
    }

    private void AddZombieGroup(float am, int cx, int cy)
    {
        IsoGridSquare sq = null;
        boolean bAnywhere = false;
    }

    private void AddZombieGroup(boolean flag, int i)
    {
    }

    public void primUpdate()
    {
    }

    public void update()
    {
        UpdateMetaZombiesTick();
        if(Rand.Next((int)(12000F * GameTime.instance.getInvMultiplier())) == 0)
            UpdateMetaZombies();
        for(int n = 0; n < Groups.size(); n++)
        {
            SurvivorGroup g = (SurvivorGroup)Groups.get(n);
            boolean bRemove = true;
            for(int m = 0; m < g.Members.size(); m++)
                if(((SurvivorDesc)g.Members.get(m)).getInstance() != null && !((SurvivorDesc)g.Members.get(m)).getInstance().isDead())
                    bRemove = false;

            if(bRemove)
            {
                Groups.remove(g);
                n--;
            } else
            {
                g.update();
            }
        }

        IsoCamera.update();
        WorldSoundManager.instance.initFrame();
        OnceEvery.update();
        UpdateZombies();
        CollisionManager.instance.initUpdate();
        int count = 0;
        for(int n = 0; n < CurrentCell.getObjectList().size(); n++)
            if(CurrentCell.getObjectList().get(n) instanceof IsoSurvivor)
                count++;

        int maxcount = cellSurvivorSpawns;
        if(IsoPlayer.DemoMode)
            maxcount = 14;
        for(int n = 0; n < CurrentCell.getBuildingList().size(); n++)
            ((IsoBuilding)CurrentCell.getBuildingList().get(n)).update();

        if(movex != 0 || movey != 0)
        {
            try
            {
                moveArea(movex, movey);
                PopulateCellWithZombies();
            }
            catch(ParserConfigurationException ex)
            {
                Logger.getLogger(IsoWorld.class.getName()).log(Level.SEVERE, null, ex);
            }
            catch(SAXException ex)
            {
                Logger.getLogger(IsoWorld.class.getName()).log(Level.SEVERE, null, ex);
            }
            catch(IOException ex)
            {
                Logger.getLogger(IsoWorld.class.getName()).log(Level.SEVERE, null, ex);
            }
            movex = 0;
            movey = 0;
            return;
        }
        CurrentCell.update();
        if(IsoPlayer.getInstance() != null)
        {
            if(IsoPlayer.getInstance().getY() <= 0.5F)
            {
                int lwX = worldX;
                int lwY = worldY;
                try
                {
                    instance.moveArea(0, -1);
                    PopulateCellWithZombies();
                }
                catch(ParserConfigurationException ex)
                {
                    Logger.getLogger(IsoWorld.class.getName()).log(Level.SEVERE, null, ex);
                }
                catch(SAXException ex)
                {
                    Logger.getLogger(IsoWorld.class.getName()).log(Level.SEVERE, null, ex);
                }
                catch(IOException ex)
                {
                    Logger.getLogger(IsoWorld.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if(IsoPlayer.getInstance().getY() >= (float)(instance.getCell().getHeightInTiles() - 1) + 0.5F)
                try
                {
                    instance.moveArea(0, 1);
                    PopulateCellWithZombies();
                }
                catch(ParserConfigurationException ex)
                {
                    Logger.getLogger(IsoWorld.class.getName()).log(Level.SEVERE, null, ex);
                }
                catch(SAXException ex)
                {
                    Logger.getLogger(IsoWorld.class.getName()).log(Level.SEVERE, null, ex);
                }
                catch(IOException ex)
                {
                    Logger.getLogger(IsoWorld.class.getName()).log(Level.SEVERE, null, ex);
                }
            if(IsoPlayer.getInstance().getX() <= 0.5F)
            {
                int lwX = worldX;
                int lwY = worldY;
                try
                {
                    instance.moveArea(-1, 0);
                    PopulateCellWithZombies();
                }
                catch(ParserConfigurationException ex)
                {
                    Logger.getLogger(IsoWorld.class.getName()).log(Level.SEVERE, null, ex);
                }
                catch(SAXException ex)
                {
                    Logger.getLogger(IsoWorld.class.getName()).log(Level.SEVERE, null, ex);
                }
                catch(IOException ex)
                {
                    Logger.getLogger(IsoWorld.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if(IsoPlayer.getInstance().getX() >= (float)(instance.getCell().getWidthInTiles() - 1) + 0.5F)
                try
                {
                    instance.moveArea(1, 0);
                    PopulateCellWithZombies();
                }
                catch(ParserConfigurationException ex)
                {
                    Logger.getLogger(IsoWorld.class.getName()).log(Level.SEVERE, null, ex);
                }
                catch(SAXException ex)
                {
                    Logger.getLogger(IsoWorld.class.getName()).log(Level.SEVERE, null, ex);
                }
                catch(IOException ex)
                {
                    Logger.getLogger(IsoWorld.class.getName()).log(Level.SEVERE, null, ex);
                }
            ScriptManager.instance.CheckExitPoints();
        }
        CollisionManager.instance.ResolveContacts();
    }

    int AddZombieImmuneGroup(int num, int x, int y, int z, int range)
    {
        if(FrameLoader.bClient)
            return 0;
        for(int n = 0; n < num; n++)
        {
            int ax = x;
            if(range > 1)
                ax = x + (Rand.Next(range) - range / 2);
            int ay = y;
            if(range > 1)
                ay = y + (Rand.Next(range) - range / 2);
            int az = z;
            IsoGridSquare sq = CurrentCell.getGridSquare(ax, ay, az);
            if(sq != null)
            {
                IsoZombie zombie = new IsoZombie(CurrentCell);
                zombie.setX(ax);
                zombie.setY(ay);
                zombie.setZ(az);
                zombie.setNx(ax);
                zombie.setNy(ay);
                zombie.setCurrent(sq);
                zombie.KeepItReal = true;
                zombie.Deaf = true;
                CurrentCell.getZombieList().add(zombie);
            }
        }

        return num;
    }

    int AddZombieGhostGroup(int num, int x, int y, int i, int j)
    {
        return 0;
    }

    public IsoCell getCell()
    {
        return CurrentCell;
    }

    private void PopulateCellWithSurvivors()
    {
    }

    int movex;
    int movey;
    public int x;
    public int y;
    public String playerCell;
    public IsoCell CurrentCell;
    public static IsoWorld instance = new IsoWorld();
    public Stack Groups;
    public int TotalSurvivorsDead;
    public int TotalSurvivorNights;
    public int SurvivorSurvivalRecord;
    public HashMap SurvivorDescriptors;
    private int cellSurvivorSpawns;
    private int cellRemoteness;
    boolean caboltoo;
    public MetaCell MetaCellGrid[][];
    public static String mapPath = "media/";
    public static boolean mapUseJar = true;
    boolean bLoaded;
    public IsoSpriteManager spriteManager;
    public String cellMap[][];
    static OnceEvery e = new OnceEvery(0.4F, false);
    int worldX;
    int worldY;
    static SurvivorGroup TestGroup = null;

}
