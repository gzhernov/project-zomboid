// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ItemContainerFiller.java

package zombie.inventory;

import java.util.*;
import zombie.core.Collections.NulledArrayList;
import zombie.core.Rand;
import zombie.core.properties.PropertyContainer;
import zombie.iso.*;
import zombie.iso.SpriteDetails.IsoFlagType;
import zombie.iso.areas.IsoBuilding;
import zombie.iso.areas.IsoRoom;

// Referenced classes of package zombie.inventory:
//            ItemContainer

public class ItemContainerFiller
{

    public ItemContainerFiller()
    {
    }

    public static void DistributeGoodItems(IsoCell cell)
    {
        PlaceOnRandomFloor(cell, "foodItems", "TinnedSoup", 8);
        PlaceOnRandomFloor(cell, "foodItems", "Crisps", 8);
        PlaceOnRandomFloor(cell, "foodItems", "Crisps2", 8);
        PlaceOnRandomFloor(cell, "foodItems", "Crisps3", 8);
        PlaceOnRandomFloor(cell, "foodItems", "Pop", 8);
        PlaceOnRandomFloor(cell, "foodItems", "Pop2", 8);
        PlaceOnRandomFloor(cell, "foodItems", "Pop3", 8);
        for(int n = 0; n < 6; n++)
        {
            ItemContainer container = getRandomContainer("counter,wardrobe,crate");
            if(container != null)
            {
                container.AddItem("Shotgun");
                container.AddItem("ShotgunShells");
            }
        }

        for(int n = 0; n < 15; n++)
        {
            ItemContainer container = getRandomContainer("counter,wardrobe,crate");
            if(container != null)
                container.AddItem("ShotgunShells");
        }

        for(int n = 0; n < 6; n++)
        {
            ItemContainer container = getRandomContainer("counter,wardrobe,crate");
            if(container != null)
                container.AddItem("Shotgun");
        }

        for(int n = 0; n < 8; n++)
        {
            ItemContainer container = getRandomContainer("counter,wardrobe,crate");
            if(container != null)
                container.AddItem("BaseballBat");
        }

        for(int n = 0; n < 30; n++)
        {
            ItemContainer container = getRandomContainer("counter,crate,sidetable");
            if(container != null)
                container.AddItem("Battery");
        }

        for(int n = 0; n < 6; n++)
        {
            ItemContainer container = getRandomContainer("crate");
            if(container != null)
                container.AddItem("PetrolCan");
        }

        for(int n = 0; n < 6; n++)
        {
            ItemContainer container = getRandomContainer("crate,counter");
            if(container != null)
                container.AddItem("Hammer");
        }

        for(int n = 0; n < 1; n++)
        {
            ItemContainer container = getRandomContainer("crate,counter");
            if(container != null)
                container.AddItem("Axe");
        }

        for(int n = 0; n < 4; n++)
        {
            ItemContainer container = getRandomContainer("crate,counter");
            if(container != null)
                container.AddItem("Axe");
        }

        for(int n = 0; n < 60; n++)
        {
            ItemContainer container = getRandomContainer("counter,crate,sidetable");
            if(container != null)
                container.AddItem("Nails");
        }

        for(int n = 0; n < 30; n++)
        {
            ItemContainer container = getRandomContainer("wardrobe");
            if(container != null)
                container.AddItem("Sheet");
        }

        for(int n = 0; n < 30; n++)
        {
            ItemContainer container = getRandomContainer("wardrobe");
            if(container != null)
                container.AddItem("Belt");
        }

        for(int n = 0; n < 30; n++)
        {
            ItemContainer container = getRandomContainer("wardrobe");
            if(container != null)
                container.AddItem("Socks");
        }

        for(int n = 0; n < 20; n++)
        {
            ItemContainer container = getRandomContainer("counter,crate,sidetable");
            if(container != null)
                container.AddItem("Lighter");
        }

        for(int n = 0; n < 30; n++)
        {
            ItemContainer container = getRandomContainer("counter,crate,sidetable,fridge");
            if(container != null)
                container.AddItem("WhiskeyFull");
        }

        for(int n = 0; n < 10; n++)
        {
            ItemContainer container = getRandomContainer("vendingsnacks");
            if(container != null)
            {
                container.AddItem("Crisps");
                container.AddItem("Crisps2");
                container.AddItem("Crisps3");
            }
        }

        for(int n = 0; n < 10; n++)
        {
            ItemContainer container = getRandomContainer("vendingpop");
            if(container != null)
            {
                container.AddItem("Pop");
                container.AddItem("Pop2");
                container.AddItem("Pop3");
            }
        }

        for(int n = 0; n < 10; n++)
        {
            ItemContainer container = getRandomContainer("counter,crate,sidetable,fridge");
            if(container != null)
                container.AddItem("Chocolate");
        }

        for(int n = 0; n < 5; n++)
        {
            ItemContainer container = getRandomContainer("counter,crate,sidetable,fridge");
            if(container != null)
                container.AddItem("Torch");
        }

        for(int n = 0; n < 10; n++)
        {
            ItemContainer container = getRandomContainer("fridge");
            if(container != null)
                container.AddItem("Bread");
        }

        for(int n = 0; n < 20; n++)
        {
            ItemContainer container = getRandomContainer("counter");
            if(container != null)
                container.AddItem("DishCloth");
        }

        for(int n = 0; n < 20; n++)
        {
            ItemContainer container = getRandomContainer("counter,sidetable");
            if(container != null)
                container.AddItem("Pen");
        }

        for(int n = 0; n < 20; n++)
        {
            ItemContainer container = getRandomContainer("counter,sidetable");
            if(container != null)
                container.AddItem("Pencil");
        }

        for(int n = 0; n < 20; n++)
        {
            ItemContainer container = getRandomContainer("fridge");
            if(container != null)
                container.AddItem("Carrots");
        }

        for(int n = 0; n < 20; n++)
        {
            ItemContainer container = getRandomContainer("fridge");
            if(container != null)
                container.AddItem("Steak");
        }

        for(int n = 0; n < 20; n++)
        {
            ItemContainer container = getRandomContainer("counter");
            if(container != null)
                container.AddItem("Carrots");
        }

        for(int n = 0; n < 20; n++)
        {
            ItemContainer container = getRandomContainer("counter");
            if(container != null)
                container.AddItem("Steak");
        }

        for(int n = 0; n < 30; n++)
        {
            ItemContainer container = getRandomContainer("medicine");
            if(container != null)
                container.AddItem("Pills");
        }

        for(int n = 0; n < 10; n++)
        {
            ItemContainer container = getRandomContainer("medicine");
            if(container != null)
                container.AddItem("PillsBeta");
        }

        for(int n = 0; n < 30; n++)
        {
            ItemContainer container = getRandomContainer("medicine");
            if(container != null)
                container.AddItem("PillsSleepingTablets");
        }

        for(int n = 0; n < 10; n++)
        {
            ItemContainer container = getRandomContainer("medicine");
            if(container != null)
                container.AddItem("PillsAntiDep");
        }

        for(int n = 0; n < 20; n++)
        {
            ItemContainer container = getRandomContainer("fridge");
            if(container != null)
                container.AddItem("Apple");
        }

        for(int n = 0; n < 6; n++)
        {
            ItemContainer container = getRandomContainer("counter");
            if(container != null)
                container.AddItem("TinOpener");
        }

        for(int n = 0; n < 30; n++)
        {
            ItemContainer container = getRandomContainer("crate");
            if(container != null)
                container.AddItem("Plank");
        }

        for(int n = 0; n < 3; n++)
        {
            ItemContainer container = getRandomContainer("counter,wardrobe,crate");
            if(container != null)
                container.AddItem("BaseballBat");
        }

        for(int n = 0; n < 12; n++)
        {
            ItemContainer container = getRandomContainer("counter,wardrobe,crate");
            if(container != null)
                container.AddItem("ShotgunShells");
        }

    }

    public static void FillContainer(ItemContainer container, String roomDef)
    {
        String containerType = container.type;
        if(containerType.equals("counter"))
            DoCounter(container, roomDef);
        if(containerType.equals("wardrobe"))
            DoWardrobe(container, roomDef);
        if(containerType.equals("medicine"))
            DoMedicine(container, roomDef);
        if(roomDef.equals("rangerHut") && container.type.equals("counter"))
            container.AddItem("Axe");
        if(roomDef.equals("tutKitchen2") && container.type.equals("fridge"))
        {
            container.AddItem("Carrots");
            container.AddItem("Apple");
        }
    }

    public static void FillRoom(IsoRoom room)
    {
        if(room.RoomDef.equals("shopBig"))
            DoShopBig(room);
        if(room.RoomDef.equals("bar"))
            DoBar(room);
        ItemContainer container;
        for(Iterator i$ = room.Containers.iterator(); i$.hasNext(); FillContainer(container, room.RoomDef))
            container = (ItemContainer)i$.next();

    }

    public static void FillTable(IsoGridSquare sq, String roomDef)
    {
        boolean doE = false;
        boolean doS = false;
        if(sq.getProperties().Is(IsoFlagType.tableE))
            doE = true;
        if(sq.getProperties().Is(IsoFlagType.tableS))
            doS = true;
        if(doE)
        {
            for(int n = 0; n < 3; n++)
            {
                float height = 0.5F;
                float x = 0.45F + (float)Rand.Next(10) / 200F;
                float y = (float)n * 0.33F;
                if(Rand.Next(5) == 0 || roomDef.equals("shopGeneral") || roomDef.equals("tutKitchen1"))
                    AddShelfItem(roomDef, sq, x, y, height);
            }

        }
        if(doS)
        {
            for(int n = 0; n < 3; n++)
            {
                float height = 0.5F;
                float y = 0.45F + (float)Rand.Next(10) / 200F;
                float x = (float)n * 0.33F;
                if(Rand.Next(5) == 0 || roomDef.equals("shopGeneral") || roomDef.equals("tutKitchen1"))
                    AddShelfItem(roomDef, sq, x, y, height);
            }

        }
    }

    public static void FillTable(IsoGridSquare sq, String roomDef, String type, float height)
    {
        boolean doE = false;
        boolean doS = false;
        if(sq.getProperties().Is(IsoFlagType.tableE))
            doE = true;
        if(sq.getProperties().Is(IsoFlagType.tableS))
            doS = true;
        if(doE)
        {
            for(int n = 0; n < 5; n++)
            {
                float x = 0.8F;
                float y = (float)n * 0.2F;
                AddShelfItem(roomDef, sq, x, y, height, type);
            }

        }
        if(doS)
        {
            for(int n = 0; n < 5; n++)
            {
                float y = 0.8F;
                float x = (float)n * 0.2F;
                AddShelfItem(roomDef, sq, x, y, height, type);
            }

        }
    }

    public static void FillTable(int lootval, IsoGridSquare sq, String roomDef, String type, float height)
    {
        boolean doE = false;
        boolean doS = false;
        if(sq.getProperties().Is(IsoFlagType.tableE))
            doE = true;
        if(sq.getProperties().Is(IsoFlagType.tableS))
            doS = true;
        if(doE)
        {
            for(int n = 0; n < 5; n++)
                if(Rand.Next(lootval) == 0)
                {
                    float x = 0.8F;
                    float y = (float)n * 0.2F;
                    AddShelfItem(roomDef, sq, x, y, height, type);
                }

        }
        if(doS)
        {
            for(int n = 0; n < 5; n++)
                if(Rand.Next(lootval) == 0)
                {
                    float y = 0.8F;
                    float x = (float)n * 0.2F;
                    AddShelfItem(roomDef, sq, x, y, height, type);
                }

        }
    }

    public static void FillTable(int lootval, IsoGridSquare sq, String roomDef, String type, float height, float depth)
    {
        boolean doE = false;
        boolean doS = false;
        if(sq.getProperties().Is(IsoFlagType.tableE))
            doE = true;
        if(sq.getProperties().Is(IsoFlagType.tableS))
            doS = true;
        if(doE)
        {
            for(int n = 0; n < 5; n++)
                if(Rand.Next(lootval) == 0)
                {
                    float x = depth;
                    float y = (float)n * 0.2F;
                    AddShelfItem(roomDef, sq, x, y, height, type);
                }

        }
        if(doS)
        {
            for(int n = 0; n < 5; n++)
                if(Rand.Next(lootval) == 0)
                {
                    float y = depth;
                    float x = (float)n * 0.2F;
                    AddShelfItem(roomDef, sq, x, y, height, type);
                }

        }
    }

    public static void FillTable(IsoGridSquare sq, String roomDef, String type, float height, float depth)
    {
        boolean doE = false;
        boolean doS = false;
        if(sq.getProperties().Is(IsoFlagType.tableE))
            doE = true;
        if(sq.getProperties().Is(IsoFlagType.tableS))
            doS = true;
        if(doE)
        {
            for(int n = 0; n < 5; n++)
                if(Rand.Next(4) == 0)
                {
                    float x = depth;
                    float y = (float)n * 0.2F;
                    AddShelfItem(roomDef, sq, x, y, height, type);
                }

        }
        if(doS)
        {
            for(int n = 0; n < 5; n++)
                if(Rand.Next(4) == 0)
                {
                    float y = depth;
                    float x = (float)n * 0.2F;
                    AddShelfItem(roomDef, sq, x, y, height, type);
                }

        }
    }

    static void AddShelfItem(String roomDef, IsoGridSquare sq, float x, float y, float height)
    {
        if(roomDef.equals("tutKitchen1") && sq.getX() == 40 && sq.getY() == 25 && x == 0.33F)
            sq.AddWorldInventoryItem("Pot", x, y, height);
        if(roomDef.equals("kitchen"))
            switch(Rand.Next(4))
            {
            case 0: // '\0'
                sq.AddWorldInventoryItem("WhiskeyHalf", x, y, height);
                break;

            case 1: // '\001'
                sq.AddWorldInventoryItem("WhiskeyFull", x, y, height);
                break;

            case 2: // '\002'
                sq.AddWorldInventoryItem("Bread", x, y, height);
                break;

            case 3: // '\003'
                sq.AddWorldInventoryItem("TinnedSoup", x, y, height);
                break;
            }
    }

    private static void AddShelfItem(String roomDef, IsoGridSquare sq, float x, float y, float height, String String)
    {
        sq.AddWorldInventoryItem(String, x, y, height);
    }

    private static void AddToRandomContainer(IsoRoom room, String item)
    {
        int n = Rand.Next(room.Containers.size());
        ((ItemContainer)room.Containers.get(n)).AddItem(item);
    }

    private static void AddToRandomContainer(IsoRoom room, String item, String containerType)
    {
        Stack containers = new Stack();
        Iterator i$ = room.Containers.iterator();
        do
        {
            if(!i$.hasNext())
                break;
            ItemContainer c = (ItemContainer)i$.next();
            if(c.type.equals(containerType))
                containers.add(c);
        } while(true);
        int n = Rand.Next(containers.size());
        ((ItemContainer)containers.get(n)).AddItem(item);
    }

    private static void DoBar(IsoRoom room)
    {
        Iterator i$ = room.TileList.iterator();
        do
        {
            if(!i$.hasNext())
                break;
            IsoGridSquare sq = (IsoGridSquare)i$.next();
            if(sq.getProperties().Is(IsoFlagType.shelfS))
            {
                boolean isCounter = false;
                for(int n = 0; n < sq.getObjects().size(); n++)
                {
                    IsoObject obj = (IsoObject)sq.getObjects().get(n);
                    if(obj.container != null && obj.container.type.equals("counter"))
                        isCounter = true;
                }

                if(isCounter)
                    continue;
                String type = "WhiskeyFull";
                switch(Rand.Next(5))
                {
                case 0: // '\0'
                    type = "WhiskeyFull";
                    break;

                case 1: // '\001'
                    type = "WhiskeyHalf";
                    break;

                case 2: // '\002'
                    type = "WhiskeyEmpty";
                    break;

                case 3: // '\003'
                    type = "WineEmpty";
                    break;

                case 4: // '\004'
                    type = "WineEmpty2";
                    break;
                }
                FillTable(sq, room.RoomDef, type, 0.75F);
            }
            if(sq.getProperties().Is(IsoFlagType.tableS))
            {
                boolean isCounter = false;
                for(int n = 0; n < sq.getObjects().size(); n++)
                {
                    IsoObject obj = (IsoObject)sq.getObjects().get(n);
                    if(obj.container != null && obj.container.type.equals("counter"))
                        isCounter = true;
                }

                if(isCounter)
                    continue;
                String type = "WhiskeyFull";
                switch(Rand.Next(5))
                {
                case 0: // '\0'
                    type = "WhiskeyFull";
                    break;

                case 1: // '\001'
                    type = "WhiskeyHalf";
                    break;

                case 2: // '\002'
                    type = "WhiskeyEmpty";
                    break;

                case 3: // '\003'
                    type = "WineEmpty";
                    break;

                case 4: // '\004'
                    type = "WineEmpty2";
                    break;
                }
                FillTable(sq, room.RoomDef, type, 0.45F);
            }
            if(sq.getProperties().Is(IsoFlagType.floorS))
            {
                boolean isCounter = false;
                for(int n = 0; n < sq.getObjects().size(); n++)
                {
                    IsoObject obj = (IsoObject)sq.getObjects().get(n);
                    if(obj.container != null && obj.container.type.equals("counter"))
                        isCounter = true;
                }

                if(!isCounter)
                {
                    String type = "WhiskeyFull";
                    switch(Rand.Next(5))
                    {
                    case 0: // '\0'
                        type = "WhiskeyFull";
                        break;

                    case 1: // '\001'
                        type = "WhiskeyHalf";
                        break;

                    case 2: // '\002'
                        type = "WhiskeyEmpty";
                        break;

                    case 3: // '\003'
                        type = "WineEmpty";
                        break;

                    case 4: // '\004'
                        type = "WineEmpty2";
                        break;
                    }
                    FillTable(sq, room.RoomDef, type, 0.15F);
                }
            }
        } while(true);
    }

    private static void DoCounter(ItemContainer container, String roomDef)
    {
        FillTable(container.SourceGrid, roomDef);
        if(!roomDef.equals("tutKitchen2"))
            if(roomDef.equals("shopGeneral"))
            {
                int m = Rand.Next(3) + 1;
                for(int n = 0; n < m; n++)
                    container.AddItem("Bread");

                m = Rand.Next(3) + 1;
                for(int n = 0; n < m; n++)
                    container.AddItem("WhiskeyFull");

                if(Rand.Next(10) == 0)
                    container.AddItem("BaseballBat");
            } else
            if(roomDef.equals("shed"))
            {
                int m = 10;
                for(int n = 0; n < m; n++)
                    container.AddItem("Plank");

                m = 3;
                for(int n = 0; n < m; n++)
                    container.AddItem("Nails");

            }
    }

    private static void DoMedicine(ItemContainer container, String roomDef)
    {
        if(roomDef.equals("tutorialBathroom"))
            container.AddItem("Pills");
    }

    private static void DoShopBig(IsoRoom room)
    {
        Iterator i$ = room.TileList.iterator();
        do
        {
            if(!i$.hasNext())
                break;
            IsoGridSquare sq = (IsoGridSquare)i$.next();
            if(sq.getProperties().Is(IsoFlagType.tableS))
            {
                boolean isCounter = false;
                for(int n = 0; n < sq.getObjects().size(); n++)
                {
                    IsoObject obj = (IsoObject)sq.getObjects().get(n);
                    if(obj.container != null && obj.container.type.equals("counter"))
                        isCounter = true;
                }

                if(isCounter)
                    continue;
                String type = "None";
                switch(Rand.Next(9))
                {
                case 0: // '\0'
                    type = "Bread";
                    break;

                case 1: // '\001'
                    type = "TinnedSoup";
                    break;

                case 2: // '\002'
                    type = "WhiskeyFull";
                    break;

                case 3: // '\003'
                    type = "Pop";
                    break;

                case 4: // '\004'
                    type = "Pop2";
                    break;

                case 5: // '\005'
                    type = "Pop3";
                    break;

                case 6: // '\006'
                    type = "Crisps";
                    break;

                case 7: // '\007'
                    type = "Crisps2";
                    break;

                case 8: // '\b'
                    type = "Crisps3";
                    break;
                }
                FillTable(sq, room.RoomDef, type, 0.5F);
            }
            if(sq.getProperties().Is(IsoFlagType.floorS))
            {
                boolean isCounter = false;
                for(int n = 0; n < sq.getObjects().size(); n++)
                {
                    IsoObject obj = (IsoObject)sq.getObjects().get(n);
                    if(obj.container != null && obj.container.type.equals("counter"))
                        isCounter = true;
                }

                if(!isCounter)
                {
                    String type = "None";
                    switch(Rand.Next(9))
                    {
                    case 0: // '\0'
                        type = "Bread";
                        break;

                    case 1: // '\001'
                        type = "TinnedSoup";
                        break;

                    case 2: // '\002'
                        type = "WhiskeyFull";
                        break;

                    case 3: // '\003'
                        type = "Pop";
                        break;

                    case 4: // '\004'
                        type = "Pop2";
                        break;

                    case 5: // '\005'
                        type = "Pop3";
                        break;

                    case 6: // '\006'
                        type = "Crisps";
                        break;

                    case 7: // '\007'
                        type = "Crisps2";
                        break;

                    case 8: // '\b'
                        type = "Crisps3";
                        break;
                    }
                    FillTable(sq, room.RoomDef, type, 0.15F);
                }
            }
        } while(true);
    }

    private static void DoWardrobe(ItemContainer container, String roomDef)
    {
        if(roomDef.equals("tutorialBedroom"))
        {
            container.AddItem("Sheet");
            container.AddItem("Pillow");
        }
    }

    private static ItemContainer getRandomContainer(String containerTypes)
    {
        NulledArrayList choices = new NulledArrayList();
        if(DistributionTarget.isEmpty())
            return null;
        String con[] = containerTypes.split(",");
        boolean bDone = false;
        do
        {
            if(bDone)
                break;
            int n = Rand.Next(DistributionTarget.size());
            IsoBuilding b = (IsoBuilding)DistributionTarget.get(n);
            for(int l = 0; l < b.container.size(); l++)
            {
                for(int k = 0; k < con.length; k++)
                    if(((ItemContainer)b.container.get(l)).type.equals(con[k].trim()))
                        choices.add(b.container.get(l));

            }

            if(!choices.isEmpty())
                bDone = true;
        } while(true);
        return (ItemContainer)choices.get(Rand.Next(choices.size()));
    }

    private static void PlaceOnRandomFloor(IsoCell cell, String zoneTypes, String objectType, int count)
    {
        NulledArrayList choices = new NulledArrayList();
        String con[] = zoneTypes.split(",");
        boolean bDone = false;
        do
        {
            if(bDone)
                break;
            for(int l = 0; l < cell.getZoneStack().size(); l++)
            {
                zombie.iso.IsoCell.Zone b = (zombie.iso.IsoCell.Zone)cell.getZoneStack().get(l);
                for(int k = 0; k < con.length; k++)
                    if(b.Name.equals(con[k].trim()))
                        choices.add(b);

            }

            if(!choices.isEmpty())
                bDone = true;
        } while(true);
        for(int n = 0; n < count; n++)
        {
            zombie.iso.IsoCell.Zone choice = (zombie.iso.IsoCell.Zone)choices.get(Rand.Next(choices.size()));
            IsoGridSquare sq = cell.getFreeTile(choice);
            if(sq != null)
                sq.AddWorldInventoryItem(objectType, (float)(100 + Rand.Next(400)) / 1000F, (float)(100 + Rand.Next(400)) / 1000F, 0.0F);
        }

    }

    public static NulledArrayList DistributionTarget = new NulledArrayList();
    public static NulledArrayList Containers = new NulledArrayList();

}
