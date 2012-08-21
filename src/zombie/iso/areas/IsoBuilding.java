// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   IsoBuilding.java

package zombie.iso.areas;

import gnu.trove.map.hash.THashMap;
import java.awt.Rectangle;
import java.util.*;
import zombie.SoundManager;
import zombie.WorldSoundManager;
import zombie.behaviors.survivor.orders.Needs.Need;
import zombie.characters.*;
import zombie.core.Collections.NulledArrayList;
import zombie.core.Core;
import zombie.core.Rand;
import zombie.core.properties.PropertyContainer;
import zombie.inventory.*;
import zombie.inventory.types.Food;
import zombie.inventory.types.HandWeapon;
import zombie.iso.*;
import zombie.iso.SpriteDetails.IsoFlagType;
import zombie.iso.objects.IsoDoor;
import zombie.iso.objects.IsoWindow;
import zombie.openal.Audio;

// Referenced classes of package zombie.iso.areas:
//            IsoArea, BuildingScore, IsoRoomExit, IsoRoom

public class IsoBuilding extends IsoArea
{

    public IsoBuilding()
    {
        Exits = new Vector();
        IsResidence = true;
        container = new NulledArrayList();
        Rooms = new Vector();
        Windows = new Vector();
        Alarmed = false;
        AlarmTriggered = false;
        AlarmTime = 1800;
        WarningTime = 240;
        burgSound = null;
        ID = 0;
        safety = 0;
        transparentWalls = 0;
        scoreUpdate = -1;
        ID = IDMax++;
        scoreUpdate = -120 + Rand.Next(120);
    }

    public void TriggerAlarm()
    {
        if(Alarmed)
            AlarmTriggered = true;
    }

    public IsoBuilding(IsoCell cell)
    {
        Exits = new Vector();
        IsResidence = true;
        container = new NulledArrayList();
        Rooms = new Vector();
        Windows = new Vector();
        Alarmed = false;
        AlarmTriggered = false;
        AlarmTime = 1800;
        WarningTime = 240;
        burgSound = null;
        ID = 0;
        safety = 0;
        transparentWalls = 0;
        scoreUpdate = -1;
        ID = IDMax++;
        scoreUpdate = -120 + Rand.Next(120);
    }

    public boolean ContainsAllItems(Stack items)
    {
        return false;
    }

    public float ScoreBuildingPersonSpecific(SurvivorDesc desc, boolean bFarGood)
    {
        float score = 0.0F;
        score += Rooms.size() * 5;
        score += Exits.size() * 15;
        score -= transparentWalls * 10;
        for(int n = 0; n < container.size(); n++)
        {
            ItemContainer con = (ItemContainer)container.get(n);
            score += con.Items.size() * 3;
        }

        BuildingScore s;
        if(!IsoWorld.instance.CurrentCell.getBuildingScores().containsKey(Integer.valueOf(ID)))
        {
            s = new BuildingScore(this);
            s.building = this;
            IsoWorld.instance.CurrentCell.getBuildingScores().put(Integer.valueOf(ID), s);
            ScoreBuildingGeneral(s);
        }
        s = (BuildingScore)IsoWorld.instance.CurrentCell.getBuildingScores().get(Integer.valueOf(ID));
        score += (s.defense + s.food + (float)s.size + s.weapons + s.wood) * 10F;
        int dx = -10000;
        int dy = -10000;
        if(!Exits.isEmpty())
        {
            IsoRoomExit exit = (IsoRoomExit)Exits.get(0);
            dx = exit.x;
            dy = exit.y;
        }
        float dist = IsoUtils.DistanceManhatten(desc.getInstance().getX(), desc.getInstance().getY(), dx, dy);
        if(dist > 0.0F)
            if(bFarGood)
                score *= dist * 0.5F;
            else
                score /= dist * 0.5F;
        return score;
    }

    public float getNeedsScore(SurvivorGroup group)
    {
        float score = 0.0F;
        for(int n = 0; n < container.size(); n++)
        {
            ItemContainer con = (ItemContainer)container.get(n);
            for(int m = 0; m < group.GroupNeeds.size(); m++)
            {
                Need need = (Need)group.GroupNeeds.get(m);
                score += con.getNumItems(need.item) * 20;
            }

        }

        return score;
    }

    public float ScoreBuildingGroupSpecific(SurvivorGroup group)
    {
        float score = 0.0F;
        score += Rooms.size() * 5;
        score -= transparentWalls * 20;
        score -= Windows.size() * 10;
        for(int n = 0; n < container.size(); n++)
        {
            ItemContainer con = (ItemContainer)container.get(n);
            for(int m = 0; m < group.GroupNeeds.size(); m++)
            {
                Need need = (Need)group.GroupNeeds.get(m);
                score += con.getNumItems(need.item) * 100;
            }

            score += con.Items.size() * 3;
        }

        BuildingScore s;
        if(!IsoWorld.instance.CurrentCell.getBuildingScores().containsKey(Integer.valueOf(ID)))
        {
            s = new BuildingScore(this);
            s.building = this;
            IsoWorld.instance.CurrentCell.getBuildingScores().put(Integer.valueOf(ID), s);
            ScoreBuildingGeneral(s);
        }
        s = (BuildingScore)IsoWorld.instance.CurrentCell.getBuildingScores().get(Integer.valueOf(ID));
        score += (s.defense + s.food + (float)s.size + s.weapons + s.wood) * 10F;
        int dx = -10000;
        int dy = -10000;
        if(!Exits.isEmpty())
        {
            IsoRoomExit exit = (IsoRoomExit)Exits.get(0);
            dx = exit.x;
            dy = exit.y;
        }
        return score;
    }

    public void update()
    {
        if(Exits.isEmpty())
            return;
        int safescore = 0;
        int tilecount = 0;
        for(int n = 0; n < Rooms.size(); n++)
        {
            IsoRoom room = (IsoRoom)Rooms.get(n);
            if(room.layer != 0)
                continue;
            for(int z = 0; z < room.TileList.size(); z++)
            {
                tilecount++;
                IsoGridSquare sq = (IsoGridSquare)room.TileList.get(z);
            }

        }

        if(tilecount == 0)
            tilecount++;
        safescore = (int)((float)safescore / (float)tilecount);
        scoreUpdate--;
        if(scoreUpdate <= 0)
        {
            scoreUpdate += 120;
            BuildingScore score = null;
            if(IsoWorld.instance.CurrentCell.getBuildingScores().containsKey(Integer.valueOf(ID)))
            {
                score = (BuildingScore)IsoWorld.instance.CurrentCell.getBuildingScores().get(Integer.valueOf(ID));
            } else
            {
                score = new BuildingScore(this);
                score.building = this;
            }
            score = ScoreBuildingGeneral(score);
            score.defense += safescore * 10;
            safety = safescore;
            IsoWorld.instance.CurrentCell.getBuildingScores().put(Integer.valueOf(ID), score);
        }
        if(AlarmTriggered && AlarmTime > 0)
        {
            IsoGridSquare sq = IsoWorld.instance.CurrentCell.getGridSquare(((IsoRoomExit)Exits.get(0)).x, ((IsoRoomExit)Exits.get(0)).y, 0);
            if(WarningTime > 0)
            {
                WarningTime--;
                if(WarningTime == 0)
                    burgSound = SoundManager.instance.PlayWorldSoundWav("burgalarm", sq, 0.0F, 40F, 1.0F, true);
                if(WarningTime % 15 == 0 && WarningTime != 0)
                    SoundManager.instance.PlayWorldSound("burgalarmbeep", sq, 0.0F, 20F, 0.4F, false);
            } else
            {
                if(burgSound != null && AlarmTime % 18 == 0)
                    burgSound = SoundManager.instance.PlayWorldSoundWav("burgalarm", sq, 0.0F, 40F, 1.0F, true);
                AlarmTime--;
                if(AlarmTime % 120 == 0)
                    WorldSoundManager.instance.addSound(null, sq.getX(), sq.getY(), sq.getZ(), 90, 90);
                if(AlarmTime == 0 && burgSound != null)
                {
                    burgSound.stop();
                    burgSound = null;
                }
            }
        }
    }

    public void AddRoom(IsoRoom room)
    {
        Rooms.add(room);
        if(bounds == null)
            bounds = (Rectangle)room.bounds.clone();
        if(room != null && room.bounds != null)
            bounds.add(room.bounds);
    }

    public void AutoBarracade()
    {
        for(IsoRoomExit exit : this.Exits)
        {
            
            
            IsoGridSquare a = IsoWorld.instance.CurrentCell.getGridSquare(exit.x, exit.y, exit.layer);
            IsoGridSquare b = IsoWorld.instance.CurrentCell.getGridSquare(exit.To.x, exit.To.y, exit.To.layer);
            for(int n = 0; n < a.getSpecialObjects().size(); n++)
            {
                IsoObject spec = (IsoObject)a.getSpecialObjects().get(n);
                if(spec instanceof IsoDoor)
                    ((IsoDoor)spec).Barricade(null, null);
            }

            for(int n = 0; n < b.getSpecialObjects().size(); n++)
            {
                IsoObject spec = (IsoObject)b.getSpecialObjects().get(n);
                if(spec instanceof IsoDoor)
                    ((IsoDoor)spec).Barricade(null, null);
            }


            /* 354 */       for (IsoWindow window : this.Windows)
            /*     */       {
            /* 356 */         window.Barricade(null);
            /*     */       }
            

        }

    }

    public void CalculateExits()
    {
    	for (Iterator i$ = this.Rooms.iterator(); i$.hasNext(); ) {
			IsoRoom room = (IsoRoom)i$.next();
/*     */ 


/* 368 */       for (IsoRoomExit exit : room.Exits)
/*     */       {
/* 371 */         if ((exit.To.From == null) && (room.layer == 0))
/* 372 */           this.Exits.add(exit);
/*     */       }



/*     */     }
/*     */     IsoRoom room;
    }

    public void CalculateWindows()
    {
        for(Iterator i$ = Rooms.iterator(); i$.hasNext();)
        {
            IsoRoom room = (IsoRoom)i$.next();
            Iterator i$1 = room.TileList.iterator();
            while(i$1.hasNext()) 
            {
                IsoGridSquare sq = (IsoGridSquare)i$1.next();
                IsoGridSquare s = sq.getCell().getGridSquare(sq.getX(), sq.getY() + 1, sq.getZ());
                IsoGridSquare e = sq.getCell().getGridSquare(sq.getX() + 1, sq.getY(), sq.getZ());
                if(sq.getProperties().Is(IsoFlagType.collideN) && sq.getProperties().Is(IsoFlagType.transparentN))
                {
                    room.transparentWalls++;
                    transparentWalls++;
                }
                if(sq.getProperties().Is(IsoFlagType.collideW) && sq.getProperties().Is(IsoFlagType.transparentW))
                {
                    room.transparentWalls++;
                    transparentWalls++;
                }
                if(s != null)
                {
                    boolean bSameBuilding = s.getRoom() != null;
                    if(s.getRoom() != null && s.getRoom().building != room.building)
                        bSameBuilding = false;
                    if(s.getProperties().Is(IsoFlagType.collideN) && s.getProperties().Is(IsoFlagType.transparentN) && !bSameBuilding)
                    {
                        room.transparentWalls++;
                        transparentWalls++;
                    }
                }
                if(e != null)
                {
                    boolean bSameBuilding = e.getRoom() != null;
                    if(e.getRoom() != null && e.getRoom().building != room.building)
                        bSameBuilding = false;
                    if(e.getProperties().Is(IsoFlagType.collideW) && e.getProperties().Is(IsoFlagType.transparentW) && !bSameBuilding)
                    {
                        room.transparentWalls++;
                        transparentWalls++;
                    }
                }
                for(int n = 0; n < sq.getSpecialObjects().size(); n++)
                {
                    IsoObject spec = (IsoObject)sq.getSpecialObjects().get(n);
                    if(spec instanceof IsoWindow)
                        Windows.add((IsoWindow)spec);
                }

                if(s != null)
                {
                    for(int n = 0; n < s.getSpecialObjects().size(); n++)
                    {
                        IsoObject spec = (IsoObject)s.getSpecialObjects().get(n);
                        if(spec instanceof IsoWindow)
                            Windows.add((IsoWindow)spec);
                    }

                }
                if(e != null)
                {
                    int n = 0;
                    while(n < e.getSpecialObjects().size()) 
                    {
                        IsoObject spec = (IsoObject)e.getSpecialObjects().get(n);
                        if(spec instanceof IsoWindow)
                            Windows.add((IsoWindow)spec);
                        n++;
                    }
                }
            }
        }

    }

    public void ClearZombies()
    {
        for(Iterator i$ = Rooms.iterator(); i$.hasNext();)
        {
            IsoRoom room = (IsoRoom)i$.next();
            int n = 0;
            while(n < IsoWorld.instance.CurrentCell.getObjectList().size()) 
            {
                IsoMovingObject mv = (IsoMovingObject)IsoWorld.instance.CurrentCell.getObjectList().get(n);
                if(mv instanceof IsoZombie)
                {
                    IsoWorld.instance.CurrentCell.getRemoveList().add(mv);
                    n--;
                }
                n++;
            }
        }

    }

    public void FillContainers()
    {
        boolean bIsTutHouse = false;
        for(Iterator i$ = Rooms.iterator(); i$.hasNext();)
        {
            IsoRoom room = (IsoRoom)i$.next();
            if(room.RoomDef != null && room.RoomDef.contains("tutorial"))
                bIsTutHouse = true;
            if(!room.TileList.isEmpty())
            {
                IsoGridSquare sq2 = (IsoGridSquare)room.TileList.get(0);
                if(sq2.getX() < 74 && sq2.getY() < 32)
                    bIsTutHouse = true;
            }
            if(room.RoomDef.contains("shop"))
                IsResidence = false;
            Iterator i$1 = room.TileList.iterator();
            while(i$1.hasNext()) 
            {
                IsoGridSquare sq = (IsoGridSquare)i$1.next();
                for(int n = 0; n < sq.getObjects().size(); n++)
                {
                    IsoObject obj = (IsoObject)sq.getObjects().get(n);
                    if(obj.hasWater())
                        room.getWaterSources().add(obj);
                    if(obj.container != null)
                    {
                        container.add(obj.container);
                        room.Containers.add(obj.container);
                    }
                }

                if(sq.getProperties().Is(IsoFlagType.bed))
                    room.Beds.add(sq);
            }
        }

        if(!bIsTutHouse || !Core.GameMode.equals("KateBaldspot"))
            ItemContainerFiller.DistributionTarget.add(this);
    }

    public boolean FullyBarricaded()
    {
        Iterator i$ = Exits.iterator();
        do
        {
            if(!i$.hasNext())
                break;
            IsoRoomExit exit = (IsoRoomExit)i$.next();
            IsoGridSquare a = IsoWorld.instance.CurrentCell.getGridSquare(exit.x, exit.y, exit.layer);
            IsoGridSquare b = IsoWorld.instance.CurrentCell.getGridSquare(exit.To.x, exit.To.y, exit.To.layer);
            if(a.getZ() == 0)
            {
                int n;
                for(n = 0; n < a.getSpecialObjects().size(); n++)
                {
                    IsoObject spec = (IsoObject)a.getSpecialObjects().get(n);
                    if((spec instanceof IsoDoor) && ((IsoDoor)spec).Barricaded == 0)
                        return false;
                }

                n = 0;
                while(n < b.getSpecialObjects().size()) 
                {
                    IsoObject spec = (IsoObject)b.getSpecialObjects().get(n);
                    if((spec instanceof IsoDoor) && ((IsoDoor)spec).Barricaded == 0)
                        return false;
                    n++;
                }
            }
        } while(true);
        for(i$ = Windows.iterator(); i$.hasNext();)
        {
            IsoWindow window = (IsoWindow)i$.next();
            if(window.square.getZ() == 0 && window.Barricaded == 0)
                return false;
        }

        return true;
    }

    public ItemContainer getContainerWith(ItemType itemType)
    {
    	for (IsoRoom room : this.Rooms)
    	/*     */     {
    	/* 630 */       for (ItemContainer container : room.Containers)
    	/*     */       {
    	/* 633 */         if (container.HasType(itemType))
    	/*     */         {
    	/* 635 */           return container;
    	/*     */         }
    	/*     */       }
    	/*     */ 
    	/*     */     }
    	/*     */ 
    	/* 641 */     return null;
    }

    public IsoRoom getRandomRoom()
    {
        IsoRoom room;
        for(room = (IsoRoom)Rooms.get(Rand.Next(Rooms.size())); room.TileList.isEmpty(); room = (IsoRoom)Rooms.get(Rand.Next(Rooms.size())));
        return room;
    }

    private BuildingScore ScoreBuildingGeneral(BuildingScore score)
    {
        score.food = 0.0F;
        score.defense = 0.0F;
        score.weapons = 0.0F;
        score.wood = 0.0F;
        score.building = this;
        score.size = 0;
        score.defense += (Exits.size() - 1) * 140;
        score.defense -= transparentWalls * 40;
        score.size = Rooms.size() * 10;
        score.size += container.size() * 10;
        for(int n = 0; n < IsoWorld.instance.Groups.size(); n++)
            if(((SurvivorGroup)IsoWorld.instance.Groups.get(n)).Safehouse == this)
                score.size -= 0xf4240;

        for(int n = 0; n < Rooms.size(); n++)
            score.size += ((IsoRoom)Rooms.get(n)).TileList.size() * 4;

        for(int n = 0; n < container.size(); n++)
        {
            ItemContainer con = (ItemContainer)container.get(n);
            for(int m = 0; m < con.Items.size(); m++)
            {
                InventoryItem item = (InventoryItem)con.Items.get(m);
                if(item instanceof Food)
                    score.food -= ((Food)item).getHungerChange() * 200F;
                if(!(item instanceof HandWeapon))
                    continue;
                float s = ((HandWeapon)item).getScore(null);
                if(s < 0.0F)
                    s = 0.0F;
                score.weapons += s;
            }

        }

        return score;
    }

    public IsoGridSquare getFreeTile()
    {
        IsoGridSquare sq = null;
        do
        {
            IsoRoom room = (IsoRoom)Rooms.get(Rand.Next(Rooms.size()));
            sq = room.getFreeTile();
        } while(sq == null);
        return sq;
    }

    public boolean hasWater()
    {
        for(Iterator it = Rooms.iterator(); it != null && it.hasNext();)
        {
            IsoRoom r = (IsoRoom)it.next();
            if(!r.WaterSources.isEmpty())
            {
                IsoObject waterSource = null;
                int i = 0;
                do
                {
                    if(i >= r.WaterSources.size())
                        break;
                    if(((IsoObject)r.WaterSources.get(i)).hasWater())
                    {
                        waterSource = (IsoObject)r.WaterSources.get(i);
                        break;
                    }
                    i++;
                } while(true);
                if(waterSource != null)
                    return true;
            }
        }

        return false;
    }

    public Rectangle bounds;
    public Vector<IsoRoomExit> Exits;
    public boolean IsResidence;
    public NulledArrayList container;
    public Vector<IsoRoom> Rooms;
    public Vector<IsoWindow> Windows;
    public boolean Alarmed;
    public boolean AlarmTriggered;
    public int AlarmTime;
    public int WarningTime;
    private Audio burgSound;
    public int ID;
    public static int IDMax = 0;
    public int safety;
    public int transparentWalls;
    public static float PoorBuildingScore = 10F;
    public static float GoodBuildingScore = 100F;
    public int scoreUpdate;

}
