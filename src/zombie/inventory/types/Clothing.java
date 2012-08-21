// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Clothing.java

package zombie.inventory.types;

import gnu.trove.map.hash.THashMap;
import zombie.characters.IsoGameCharacter;
import zombie.core.Collections.NulledArrayList;
import zombie.inventory.*;
import zombie.scripting.objects.Item;

public class Clothing extends InventoryItem
{

    public static THashMap getClothingPaletteMap()
    {
        return ClothingPaletteMap;
    }

    public static void setClothingPaletteMap(THashMap aClothingPaletteMap)
    {
        ClothingPaletteMap = aClothingPaletteMap;
    }

    public static THashMap getSpriteToItem()
    {
        return SpriteToItem;
    }

    public static void setSpriteToItem(THashMap aSpriteToItem)
    {
        SpriteToItem = aSpriteToItem;
    }

    public Clothing(String module, String name, String itemType, String texName, String palette, String SpriteName)
    {
        super(module, name, itemType, texName);
        bodyLocation = zombie.scripting.objects.Item.ClothingBodyLocation.None;
        this.SpriteName = null;
        this.module = module;
        this.SpriteName = SpriteName;
        if(palette != null)
            SpriteName = (new StringBuilder()).append(SpriteName).append("_").append(palette).toString();
        if(palette != null)
            SpriteToItem.put(SpriteName, (new StringBuilder()).append(module).append(".").append(itemType).append("#").append(palette).toString());
        else
            SpriteToItem.put(SpriteName, (new StringBuilder()).append(module).append(".").append(itemType).toString());
        if(!ClothingPaletteMap.containsKey(this.SpriteName))
            ClothingPaletteMap.put(this.SpriteName, new NulledArrayList());
        if(!((NulledArrayList)ClothingPaletteMap.get(this.SpriteName)).contains(palette))
            ((NulledArrayList)ClothingPaletteMap.get(this.SpriteName)).add(palette);
        this.palette = palette;
    }

    public void Unwear()
    {
        if(container.parent instanceof IsoGameCharacter)
        {
            IsoGameCharacter c = (IsoGameCharacter)container.parent;
            if(bodyLocation == zombie.scripting.objects.Item.ClothingBodyLocation.Bottoms && c.getClothingItem_Legs() == this)
            {
                c.setClothingItem_Legs(null);
                c.SetClothing(zombie.scripting.objects.Item.ClothingBodyLocation.Bottoms, null, null);
            }
            if(bodyLocation == zombie.scripting.objects.Item.ClothingBodyLocation.Top && c.getClothingItem_Torso() == this)
            {
                c.setClothingItem_Torso(null);
                c.SetClothing(zombie.scripting.objects.Item.ClothingBodyLocation.Top, null, null);
            }
            if(bodyLocation == zombie.scripting.objects.Item.ClothingBodyLocation.Shoes && c.getClothingItem_Feet() == this)
            {
                c.setClothingItem_Feet(null);
                c.SetClothing(zombie.scripting.objects.Item.ClothingBodyLocation.Shoes, null, null);
            }
        }
    }

    public void Use(boolean bCrafting, boolean bInContainer)
    {
        if(uses <= 1)
            Unwear();
        super.Use(bCrafting, bInContainer);
    }

    public boolean CanStack(InventoryItem item)
    {
        return palette == null && ((Clothing)item).palette == null || palette.equals(((Clothing)item).palette);
    }

    public static Clothing CreateFromSprite(String Sprite)
    {
       
        try
        {
            Clothing clothing = null;
            String itemsplit[] = Sprite.split("#");
            String gah = Sprite.replace("#", "_");
            String item = (String)SpriteToItem.get(gah);
            String itemsplit2[] = item.split("#");
            if(itemsplit.length == 2)
                clothing = (Clothing)InventoryItemFactory.CreateItem(itemsplit2[0], 1.0F, itemsplit[1]);
            else
                clothing = (Clothing)InventoryItemFactory.CreateItem(itemsplit2[0], 1.0F);
            return clothing;
        }
        catch(Exception ex1)
        {
            
        }
        return null;
    }

    public zombie.scripting.objects.Item.ClothingBodyLocation getBodyLocation()
    {
        return bodyLocation;
    }

    public void setBodyLocation(zombie.scripting.objects.Item.ClothingBodyLocation bodyLocation)
    {
        this.bodyLocation = bodyLocation;
    }

    public String getSpriteName()
    {
        return SpriteName;
    }

    public void setSpriteName(String SpriteName)
    {
        this.SpriteName = SpriteName;
    }

    public String getPalette()
    {
        return palette;
    }

    public void setPalette(String palette)
    {
        this.palette = palette;
    }

    protected static THashMap ClothingPaletteMap = new THashMap();
    protected static THashMap SpriteToItem = new THashMap();
    protected zombie.scripting.objects.Item.ClothingBodyLocation bodyLocation;
    protected String SpriteName;
    protected String palette;

}
