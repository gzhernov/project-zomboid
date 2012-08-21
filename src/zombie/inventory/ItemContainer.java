// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ItemContainer.java

package zombie.inventory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import zombie.GameTime;
import zombie.GameWindow;
import zombie.characters.*;
import zombie.core.Collections.NulledArrayList;
import zombie.core.Rand;
import zombie.inventory.types.Clothing;
import zombie.inventory.types.Drainable;
import zombie.inventory.types.Food;
import zombie.inventory.types.HandWeapon;
import zombie.iso.*;
import zombie.iso.objects.IsoStove;

// Referenced classes of package zombie.inventory:
//            InventoryItem, InventoryItemFactory, ItemType

public class ItemContainer
{

    public ItemContainer(String containerName, IsoGridSquare square, IsoObject parent, int wid, int hei)
    {
        active = false;
        dirty = true;
        IsDevice = false;
        ageFactor = 1.0F;
        CookingFactor = 1.0F;
        Items = new NulledArrayList();
        this.parent = null;
        SourceGrid = null;
        type = "none";
        ID = 0;
        ID = IDMax++;
        Width = wid;
        this.parent = parent;
        Height = hei;
        type = containerName;
        SourceGrid = square;
        if(containerName.equals("fridge"))
        {
            ageFactor = 0.02F;
            CookingFactor = 0.0F;
        }
    }

    public ItemContainer()
    {
        active = false;
        dirty = true;
        IsDevice = false;
        ageFactor = 1.0F;
        CookingFactor = 1.0F;
        Items = new NulledArrayList();
        parent = null;
        SourceGrid = null;
        type = "none";
        ID = 0;
        ID = IDMax++;
        Width = 6;
        Height = 2;
    }

    public InventoryItem AddItem(InventoryItem item)
    {
        if(item == null)
            return null;
        if(parent != null && !(parent instanceof IsoGameCharacter))
            parent.DirtySlice();
        InventoryItem find = FindAndReturn(item.type);
        if(find != null && find.CanStack(item))
        {
            find.uses += item.uses;
            if(!IsoWorld.instance.CurrentCell.getProcessItems().contains(find))
                IsoWorld.instance.CurrentCell.getProcessItems().add(find);
            return find;
        }
        item.container = this;
        Items.add(item);
        if(IsoWorld.instance.CurrentCell != null && !IsoWorld.instance.CurrentCell.getProcessItems().contains(item))
            IsoWorld.instance.CurrentCell.getProcessItems().add(item);
        return item;
    }

    public boolean AddItem(String type)
    {
        if(parent != null && !(parent instanceof IsoGameCharacter))
            dirty = true;
        InventoryItem find = FindAndReturn(type);
        InventoryItem item = InventoryItemFactory.CreateItem(type);
        if(item == null)
            return false;
        if(find != null && find.CanStackNoTemp(item))
        {
            find.uses += item.uses;
            return true;
        }
        item.container = this;
        Items.add(item);
        if(item instanceof Food)
            ((Food)item).setHeat(getTemprature());
        return true;
    }

    public boolean AddItem(String type, float useDelta)
    {
        if(parent != null && !(parent instanceof IsoGameCharacter))
            dirty = true;
        InventoryItem find = FindAndReturn(type);
        InventoryItem item = InventoryItemFactory.CreateItem(find.getFullType());
        if(item == null)
            return false;
        if(find != null && find.CanStack(item))
        {
            find.uses += item.uses;
            return true;
        }
        if(item instanceof Drainable)
            ((Drainable)item).setUsedDelta(useDelta);
        item.container = this;
        Items.add(item);
        return true;
    }

    public InventoryItem AddItem(InventoryItem item2, int uses)
    {
        if(parent != null && !(parent instanceof IsoGameCharacter))
            dirty = true;
        InventoryItem find = FindAndReturnStack(item2);
        if(find != null && item2.CanStack(find))
        {
            find.uses += uses;
            if(!IsoWorld.instance.CurrentCell.getProcessItems().contains(find))
                IsoWorld.instance.CurrentCell.getProcessItems().add(find);
            return find;
        }
        float heat = 1.0F;
        boolean cooked = false;
        boolean burnt = false;
        int Condition = item2.Condition;
        if(item2 instanceof Food)
        {
            heat = ((Food)item2).getHeat();
            cooked = ((Food)item2).isCooked();
            burnt = ((Food)item2).Burnt;
        }
        float i = item2.Age;
        InventoryItem olditem = item2;
        InventoryItem item = InventoryItemFactory.CreateItem(item2.getFullType());
        item.uses = uses;
        if(item instanceof Food)
        {
            ((Food)item).setHeat(heat);
            ((Food)item).setCooked(cooked);
            ((Food)item).Burnt = burnt;
        }
        item.Condition = Condition;
        if(item instanceof Clothing)
        {
            ((Clothing)item).setPalette(((Clothing)olditem).getPalette());
            ((Clothing)item).setSpriteName(((Clothing)olditem).getSpriteName());
            ((Clothing)item).texture = ((Clothing)olditem).texture;
        }
        item.Age = i;
        if(item instanceof Drainable)
            ((Drainable)item).setUsedDelta(((Drainable)item).getUsedDelta());
        item.container = this;
        Items.add(item);
        if(!IsoWorld.instance.CurrentCell.getProcessItems().contains(item))
            IsoWorld.instance.CurrentCell.getProcessItems().add(item);
        return item;
    }

    public boolean contains(String type)
    {
        if(type.contains("Type:"))
        {
            for(int n = 0; n < Items.size(); n++)
            {
                InventoryItem item = (InventoryItem)Items.get(n);
                if(type.contains("Food") && (item instanceof Food))
                    return true;
                if(type.contains("Weapon") && (item instanceof HandWeapon))
                    return true;
            }

        } else
        if(type.contains("/"))
        {
            String sp[] = type.split("/");
            for(int n = 0; n < sp.length; n++)
            {
                for(int m = 0; m < Items.size(); m++)
                {
                    InventoryItem item = (InventoryItem)Items.get(m);
                    if(item.type.equals(sp[n].trim()))
                        return true;
                }

            }

        } else
        {
            for(int n = 0; n < Items.size(); n++)
            {
                InventoryItem item = (InventoryItem)Items.get(n);
                if(item == null)
                {
                    Items.remove(n);
                    n--;
                    continue;
                }
                if(item.type.equals(type.trim()))
                    return true;
            }

        }
        return false;
    }

    public void FillTest()
    {
        int x = 0;
        int y = 0;
        for(int n = 0; n < 2; n++)
            AddItem("Bread");

    }

    public InventoryItem getBestCondition(String String)
    {
        if(String == null)
            return null;
        if(String.contains("."))
            String = String.substring(String.indexOf(".") + 1);
        InventoryItem best = null;
        int bestCondition = 0;
        for(int m = 0; m < Items.size(); m++)
        {
            InventoryItem item = (InventoryItem)Items.get(m);
            if(item.type != null && item.type.equals(String) && item.Condition > bestCondition)
            {
                bestCondition = item.Condition;
                best = item;
            }
        }

        return best;
    }

    public InventoryItem FindAndReturn(String String)
    {
        if(String == null)
            return null;
        if(String.contains("."))
            String = String.substring(String.indexOf(".") + 1);
        if(String.contains("/"))
        {
            String split[] = String.split("/");
            for(int x = 0; x < split.length; x++)
            {
                for(int m = 0; m < Items.size(); m++)
                {
                    InventoryItem item = (InventoryItem)Items.get(m);
                    if(item.type != null && item.type.equals(split[x]))
                        return item;
                }

            }

        } else
        {
            for(int m = 0; m < Items.size(); m++)
            {
                InventoryItem item = (InventoryItem)Items.get(m);
                if(item.type != null && item.type.equals(String))
                    return item;
            }

        }
        return null;
    }

    public InventoryItem FindAndReturnStack(String String)
    {
        if(String.contains("."))
            String = String.substring(String.indexOf(".") + 1);
        for(int m = 0; m < Items.size(); m++)
        {
            InventoryItem item = (InventoryItem)Items.get(m);
            InventoryItem test = InventoryItemFactory.CreateItem((new StringBuilder()).append(item.module).append(".").append(String).toString());
            if((item.type != null ? item.type.equals(String) : String == null) && item.CanStack(test))
                return item;
        }

        return null;
    }

    public InventoryItem FindAndReturnStack(InventoryItem itemlike)
    {
        String String = itemlike.type;
        for(int m = 0; m < Items.size(); m++)
        {
            InventoryItem item = (InventoryItem)Items.get(m);
            if((item.type != null ? item.type.equals(String) : String == null) && item.CanStack(itemlike))
                return item;
        }

        return null;
    }

    public Vector2 getNextSlot()
    {
        boolean found = false;
        for(int y = 0; y < Height; y++)
        {
            for(int x = 0; x < Width; x++)
            {
                found = false;
                for(int m = 0; m < Items.size(); m++)
                    if(((InventoryItem)Items.get(m)).containerX == x && ((InventoryItem)Items.get(m)).containerY == y)
                        found = true;

                if(!found)
                    return new Vector2(x, y);
            }

        }

        return new Vector2(-1F, -1F);
    }

    public boolean HasType(ItemType itemType)
    {
        for(int m = 0; m < Items.size(); m++)
        {
            InventoryItem item = (InventoryItem)Items.get(m);
            if(item.cat == itemType)
                return true;
        }

        return false;
    }

    public void Remove(InventoryItem item)
    {
        if(parent != null)
            dirty = true;
        for(int m = 0; m < Items.size(); m++)
        {
            InventoryItem item2 = (InventoryItem)Items.get(m);
            if(item2 == item)
            {
                if(item.uses > 1)
                    item.uses--;
                else
                    Items.remove(item);
                dirty = true;
                return;
            }
        }

    }

    public void Remove(String itemTypes)
    {
        if(parent != null)
            dirty = true;
        for(int m = 0; m < Items.size(); m++)
        {
            InventoryItem item = (InventoryItem)Items.get(m);
            if(item.type.equals(itemTypes))
            {
                if(item.uses > 1)
                    item.uses--;
                else
                    Items.remove(item);
                dirty = true;
                return;
            }
        }

    }

    public InventoryItem Remove(ItemType itemType)
    {
        if(parent != null)
            dirty = true;
        for(int m = 0; m < Items.size(); m++)
        {
            InventoryItem item = (InventoryItem)Items.get(m);
            if(item.cat == itemType)
            {
                Items.remove(item);
                dirty = true;
                return item;
            }
        }

        return null;
    }

    public InventoryItem Find(ItemType itemType)
    {
        for(int m = 0; m < Items.size(); m++)
        {
            InventoryItem item = (InventoryItem)Items.get(m);
            if(item.cat == itemType)
                return item;
        }

        return null;
    }

    public void RemoveOneOf(String String)
    {
        if(parent != null && !(parent instanceof IsoGameCharacter))
            dirty = true;
        for(int m = 0; m < Items.size(); m++)
        {
            InventoryItem item = (InventoryItem)Items.get(m);
            if(item.type.equals(String))
            {
                if(item.uses > 1)
                    item.uses--;
                else
                    Items.remove(item);
                dirty = true;
                return;
            }
        }

    }

    private void DoKitchenContainer()
    {
        if(Rand.Next(5) == 0)
            AddItem("Bread");
        if(Rand.Next(10) == 0)
            AddItem("BaseballBat");
        if(Rand.Next(2) == 0)
            AddItem("Hammer");
        if(Rand.Next(2) == 0)
            AddItem("Nails");
        if(Rand.Next(2) == 0)
            AddItem("Plank");
        if(Rand.Next(10) == 0)
            AddItem("WhiskeyFull");
    }

    public int getWeight()
    {
        if(parent instanceof IsoPlayer)
        {
            IsoPlayer _tmp = (IsoPlayer)parent;
            if(IsoPlayer.GhostMode)
                return 20;
        }
        float total = 0.0F;
        for(int m = 0; m < Items.size(); m++)
        {
            InventoryItem item = (InventoryItem)Items.get(m);
            total += item.ActualWeight * (float)item.uses;
        }

        return (int)total;
    }

    public void age()
    {
        for(int n = 0; n < Items.size(); n++)
        {
            if(!(Items.get(n) instanceof Food))
                continue;
            Food food = (Food)Items.get(n);
            if(GameTime.instance.NightsSurvived < 30)
                food.Age += ageFactor;
        }

    }

    public float getTemprature()
    {
        if(GameTime.instance.NightsSurvived < 30 && type.equals("fridge"))
            return 0.2F;
        return GameTime.instance.NightsSurvived >= 30 || !type.equals("stove") || !(parent instanceof IsoStove) || !((IsoStove)parent).Activated() ? 1.0F : 1.8F;
    }

    public boolean RequireUpdate()
    {
        return type.equals("stove") && (parent instanceof IsoStove) && ((IsoStove)parent).Activated();
    }

    public void save(ByteBuffer output)
        throws IOException
    {
        GameWindow.WriteString(output, type);
        output.putInt(Items.size());
        for(int m = 0; m < Items.size(); m++)
        {
            InventoryItem item = (InventoryItem)Items.get(m);
            item.save(output);
        }

    }

    public void load(ByteBuffer input)
        throws IOException
    {
        type = GameWindow.ReadString(input);
        int fdsf;
        if(type.equals("stove"))
            fdsf = 0;
        int count = input.getInt();
        for(int n = 0; n < count; n++)
        {
            String t = GameWindow.ReadString(input);
            InventoryItem item = InventoryItemFactory.CreateItem(t);
            item.load(input);
            item.container = this;
            Items.add(item);
        }

        dirty = false;
    }

    public InventoryItem getBestWeapon(SurvivorDesc desc)
    {
        InventoryItem best = null;
        float bestscore = 0.0F;
        for(int n = 0; n < Items.size(); n++)
        {
            InventoryItem item = (InventoryItem)Items.get(n);
            if(!(item instanceof HandWeapon))
                continue;
            float score = ((HandWeapon)item).getScore(desc);
            if(score >= bestscore)
            {
                bestscore = score;
                best = item;
            }
        }

        return best;
    }

    public InventoryItem getBestWeapon()
    {
        InventoryItem best = null;
        float bestscore = 0.0F;
        for(int n = 0; n < Items.size(); n++)
        {
            InventoryItem item = (InventoryItem)Items.get(n);
            if(!(item instanceof HandWeapon))
                continue;
            float score = ((HandWeapon)item).getScore(null);
            if(score >= bestscore)
            {
                bestscore = score;
                best = item;
            }
        }

        return best;
    }

    public float getTotalFoodScore(SurvivorDesc desc)
    {
        float score = 0.0F;
        for(int n = 0; n < Items.size(); n++)
        {
            InventoryItem item = (InventoryItem)Items.get(n);
            if(item instanceof Food)
                score += item.getScore(desc);
        }

        return score;
    }

    public float getTotalWeaponScore(SurvivorDesc desc)
    {
        float score = 0.0F;
        for(int n = 0; n < Items.size(); n++)
        {
            InventoryItem item = (InventoryItem)Items.get(n);
            if(item instanceof HandWeapon)
                score += item.getScore(desc);
        }

        return score;
    }

    public InventoryItem getBestFood(SurvivorDesc descriptor)
    {
        InventoryItem best = null;
        float bestscore = 0.0F;
        for(int n = 0; n < Items.size(); n++)
        {
            InventoryItem item = (InventoryItem)Items.get(n);
            if(!(item instanceof Food))
                continue;
            float score = item.getScore(descriptor);
            if(((Food)item).isbDangerousUncooked() && !((Food)item).isCooked())
                score *= 0.2F;
            if(((Food)item).Age > (float)item.OffAge)
                score *= 0.2F;
            if(score >= bestscore)
            {
                bestscore = score;
                best = item;
            }
        }

        return best;
    }

    public InventoryItem getBestBandage(SurvivorDesc descriptor)
    {
        InventoryItem best = null;
        float bestscore = 0.0F;
        for(int n = 0; n < Items.size(); n++)
        {
            InventoryItem item = (InventoryItem)Items.get(n);
            if(item.CanBandage)
                return item;
        }

        return best;
    }

    public int getNumItems(String item)
    {
        int ncount = 0;
        if(item.contains("Type:"))
        {
            for(int m = 0; m < Items.size(); m++)
            {
                if((Items.get(m) instanceof Food) && item.contains("Food"))
                    ncount += ((InventoryItem)Items.get(m)).uses;
                if((Items.get(m) instanceof HandWeapon) && item.contains("Weapon"))
                    ncount += ((InventoryItem)Items.get(m)).uses;
            }

        } else
        {
            for(int m = 0; m < Items.size(); m++)
                if(((InventoryItem)Items.get(m)).type.equals(item))
                    ncount += ((InventoryItem)Items.get(m)).uses;

        }
        return ncount;
    }

    public boolean isActive()
    {
        return active;
    }

    public void setActive(boolean active)
    {
        this.active = active;
    }

    public boolean isDirty()
    {
        return dirty;
    }

    public void setDirty(boolean dirty)
    {
        this.dirty = dirty;
    }

    public int getHeight()
    {
        return Height;
    }

    public void setHeight(int Height)
    {
        this.Height = Height;
    }

    public boolean isIsDevice()
    {
        return IsDevice;
    }

    public void setIsDevice(boolean IsDevice)
    {
        this.IsDevice = IsDevice;
    }

    public float getAgeFactor()
    {
        return ageFactor;
    }

    public void setAgeFactor(float ageFactor)
    {
        this.ageFactor = ageFactor;
    }

    public float getCookingFactor()
    {
        return CookingFactor;
    }

    public void setCookingFactor(float CookingFactor)
    {
        this.CookingFactor = CookingFactor;
    }

    public NulledArrayList getItems()
    {
        return Items;
    }

    public void setItems(NulledArrayList Items)
    {
        this.Items = Items;
    }

    public IsoObject getParent()
    {
        return parent;
    }

    public void setParent(IsoObject parent)
    {
        this.parent = parent;
    }

    public IsoGridSquare getSourceGrid()
    {
        return SourceGrid;
    }

    public void setSourceGrid(IsoGridSquare SourceGrid)
    {
        this.SourceGrid = SourceGrid;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public int getWidth()
    {
        return Width;
    }

    public void setWidth(int Width)
    {
        this.Width = Width;
    }

    public int getWaterContainerCount()
    {
        int c = 0;
        for(int n = 0; n < Items.size(); n++)
        {
            InventoryItem item = (InventoryItem)Items.get(n);
            if(item.CanStoreWater)
                c++;
        }

        return c;
    }

    public InventoryItem FindWaterSource()
    {
        for(int n = 0; n < Items.size(); n++)
        {
            InventoryItem item = (InventoryItem)Items.get(n);
            if(!item.isWaterSource())
                continue;
            if(item instanceof Drainable)
            {
                if(((Drainable)item).getUsedDelta() > 0.0F)
                    return item;
            } else
            {
                return item;
            }
        }

        return null;
    }

    public ArrayList getAllWaterFillables()
    {
        tempList.clear();
        for(int n = 0; n < Items.size(); n++)
        {
            InventoryItem item = (InventoryItem)Items.get(n);
            if(item.CanStoreWater)
                tempList.add(item);
        }

        return tempList;
    }

    public boolean active;
    public boolean dirty;
    public int Height;
    public boolean IsDevice;
    public float ageFactor;
    public float CookingFactor;
    public NulledArrayList Items;
    public IsoObject parent;
    public IsoGridSquare SourceGrid;
    public String type;
    public int Width;
    public static int IDMax = 0;
    public int ID;
    static ArrayList tempList = new ArrayList();

}
