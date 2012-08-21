// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   VirtualItemSlot.java

package zombie.ui;

import se.krka.kahlua.integration.LuaCaller;
import se.krka.kahlua.vm.KahluaTable;
import zombie.Lua.LuaManager;
import zombie.characters.IsoGameCharacter;
import zombie.characters.IsoPlayer;
import zombie.core.Collections.NulledArrayList;
import zombie.core.Color;
import zombie.core.textures.Texture;
import zombie.inventory.*;
import zombie.inventory.types.*;
import zombie.scripting.objects.Item;

// Referenced classes of package zombie.ui:
//            UIElement, SpeedControls, UIManager, NewCraftingPanel, 
//            Sidebar, ClothingPanel

public class VirtualItemSlot extends UIElement
{

    public VirtualItemSlot(String type, int x, int y, String backgroundTex, IsoGameCharacter character)
    {
        alpha = 1.0F;
        index = 0;
        chr = character;
        this.type = type;
        tex = Texture.getSharedTexture(backgroundTex);
        this.x = x;
        this.y = y;
        width = tex.getWidth();
        height = tex.getHeight();
    }

    public boolean onMouseDown(int x, int y)
    {
        if(!isVisible() || SpeedControls.instance.getCurrentGameSpeed() == 0)
            return true;
        super.onMouseDown(x, y);
        if(item != null && UIManager.getDragInventory() == null && type.equals("CraftingResult"))
        {
            UIManager.setDragInventory(IsoPlayer.getInstance().getInventory().AddItem(item));
            if(UIManager.getDragInventory() instanceof Drainable)
                ((Drainable)UIManager.getDragInventory()).setUsedDelta(((Drainable)item).getUsedDelta());
            NewCraftingPanel.instance.PerformMakeItem();
            return true;
        }
        if(UIManager.getDragInventory() != null)
        {
            InventoryItem temp = UIManager.getDragInventory();
            if(type.equals("CraftingResult"))
            {
                if(item != null && UIManager.getDragInventory().getType().equals(item.getType()) && item.CanStack(temp))
                {
                    String s = item.getType();
                    if(!s.contains("."))
                        s = (new StringBuilder()).append(temp.getModule()).append(".").append(item.getType()).toString();
                    InventoryItem tempa = InventoryItemFactory.CreateItem(s);
                    UIManager.getDragInventory().setUses(UIManager.getDragInventory().getUses() + tempa.getUses());
                    NewCraftingPanel.instance.PerformMakeItem();
                }
                return true;
            }
            if(type.equals("CraftingIngredient"))
            {
                int in = ExistsInOtherSlot(temp, index);
                if(in != -1)
                    chr.setCraftingByIndex(in, null);
                if(temp.getContainer() == chr.getInventory())
                {
                    chr.setCraftingByIndex(index, temp);
                } else
                {
                    if(temp.getContainer() != null)
                    {
                        temp.getContainer().Items.remove(temp);
                        temp.getContainer().dirty = true;
                    }
                    temp = chr.getInventory().AddItem(temp);
                    temp.setContainer(chr.getInventory());
                    chr.setCraftingByIndex(index, temp);
                }
                item = chr.getCraftingByIndex(index);
                UIManager.setDragInventory(null);
            } else
            if(type.equals("Main"))
            {
                if(chr.getCraftIngredient1() == UIManager.getDragInventory())
                    chr.setCraftIngredient1(null);
                if(chr.getCraftIngredient2() == UIManager.getDragInventory())
                    chr.setCraftIngredient2(null);
                if(chr.getCraftIngredient3() == UIManager.getDragInventory())
                    chr.setCraftIngredient3(null);
                if(chr.getCraftIngredient4() == UIManager.getDragInventory())
                    chr.setCraftIngredient4(null);
                if(chr.getClothingItem_Head() == UIManager.getDragInventory())
                    chr.setClothingItem_Head(null);
                if(chr.getClothingItem_Torso() == UIManager.getDragInventory())
                    chr.setClothingItem_Torso(null);
                if(chr.getClothingItem_Hands() == UIManager.getDragInventory())
                    chr.setClothingItem_Hands(null);
                if(chr.getClothingItem_Legs() == UIManager.getDragInventory())
                    chr.setClothingItem_Legs(null);
                if(chr.getClothingItem_Feet() == UIManager.getDragInventory())
                    chr.setClothingItem_Feet(null);
                if(chr.getSecondaryHandItem() == temp)
                {
                    item = chr.getPrimaryHandItem();
                    chr.setPrimaryHandItem(temp);
                    chr.setSecondaryHandItem(item);
                } else
                if(temp.getContainer() == chr.getInventory())
                {
                    chr.setPrimaryHandItem(temp);
                } else
                {
                    if(temp.getContainer() != null)
                    {
                        temp.getContainer().Items.remove(temp);
                        temp.getContainer().dirty = true;
                    }
                    temp.setContainer(chr.getInventory());
                    chr.getInventory().AddItem(temp);
                    chr.setPrimaryHandItem(temp);
                }
                item = chr.getPrimaryHandItem();
                UIManager.setDragInventory(null);
            } else
            if(type.equals("Secondary"))
            {
                if(chr.getCraftIngredient1() == UIManager.getDragInventory())
                    chr.setCraftIngredient1(null);
                if(chr.getCraftIngredient2() == UIManager.getDragInventory())
                    chr.setCraftIngredient2(null);
                if(chr.getCraftIngredient3() == UIManager.getDragInventory())
                    chr.setCraftIngredient3(null);
                if(chr.getCraftIngredient4() == UIManager.getDragInventory())
                    chr.setCraftIngredient4(null);
                if(chr.getClothingItem_Head() == UIManager.getDragInventory())
                    chr.setClothingItem_Head(null);
                if(chr.getClothingItem_Torso() == UIManager.getDragInventory())
                    chr.setClothingItem_Torso(null);
                if(chr.getClothingItem_Hands() == UIManager.getDragInventory())
                    chr.setClothingItem_Hands(null);
                if(chr.getClothingItem_Legs() == UIManager.getDragInventory())
                    chr.setClothingItem_Legs(null);
                if(chr.getClothingItem_Feet() == UIManager.getDragInventory())
                    chr.setClothingItem_Feet(null);
                if(chr.getPrimaryHandItem() == temp)
                {
                    item = chr.getSecondaryHandItem();
                    chr.setSecondaryHandItem(temp);
                    chr.setPrimaryHandItem(item);
                } else
                if(temp.getContainer() == chr.getInventory())
                {
                    chr.setSecondaryHandItem(temp);
                } else
                {
                    if(temp.getContainer() != null)
                    {
                        temp.getContainer().Items.remove(temp);
                        temp.getContainer().dirty = true;
                    }
                    temp.setContainer(chr.getInventory());
                    chr.getInventory().AddItem(temp);
                    chr.setSecondaryHandItem(temp);
                }
                item = chr.getSecondaryHandItem();
                UIManager.setDragInventory(null);
            } else
            if(!type.equals("Head_Clothing"))
                if(type.equals("Torso_Clothing"))
                {
                    if((temp instanceof Clothing) && ((Clothing)temp).getBodyLocation() == zombie.scripting.objects.Item.ClothingBodyLocation.Top)
                    {
                        if(chr.getCraftIngredient1() == UIManager.getDragInventory())
                            chr.setCraftIngredient1(null);
                        if(chr.getCraftIngredient2() == UIManager.getDragInventory())
                            chr.setCraftIngredient2(null);
                        if(chr.getCraftIngredient3() == UIManager.getDragInventory())
                            chr.setCraftIngredient3(null);
                        if(chr.getCraftIngredient4() == UIManager.getDragInventory())
                            chr.setCraftIngredient4(null);
                        if(chr.getClothingItem_Head() == UIManager.getDragInventory())
                            chr.setClothingItem_Head(null);
                        if(chr.getClothingItem_Hands() == UIManager.getDragInventory())
                            chr.setClothingItem_Hands(null);
                        if(chr.getClothingItem_Legs() == UIManager.getDragInventory())
                            chr.setClothingItem_Legs(null);
                        if(chr.getClothingItem_Feet() == UIManager.getDragInventory())
                            chr.setClothingItem_Feet(null);
                        if(temp.getContainer() == chr.getInventory())
                        {
                            chr.setClothingItem_Torso(temp);
                            if(chr.getPrimaryHandItem() == temp)
                                chr.setPrimaryHandItem(null);
                            if(chr.getSecondaryHandItem() == temp)
                                chr.setSecondaryHandItem(null);
                            if(chr.getClothingItem_Head() == temp)
                                chr.setClothingItem_Head(null);
                            if(chr.getClothingItem_Hands() == temp)
                                chr.setClothingItem_Hands(null);
                            if(chr.getClothingItem_Legs() == temp)
                                chr.setClothingItem_Legs(null);
                            if(chr.getClothingItem_Feet() == temp)
                                chr.setClothingItem_Feet(null);
                            if(Sidebar.instance.MainHand.item == temp)
                                Sidebar.instance.MainHand.item = null;
                            if(Sidebar.instance.SecondHand.item == temp)
                                Sidebar.instance.SecondHand.item = null;
                            if(ClothingPanel.instance.HeadItem.item == temp)
                                ClothingPanel.instance.HeadItem.item = null;
                            if(ClothingPanel.instance.HandsItem.item == temp)
                                ClothingPanel.instance.HandsItem.item = null;
                            if(ClothingPanel.instance.LegsItem.item == temp)
                                ClothingPanel.instance.LegsItem.item = null;
                            if(ClothingPanel.instance.FeetItem.item == temp)
                                ClothingPanel.instance.FeetItem.item = null;
                        } else
                        {
                            if(temp.getContainer() != null)
                            {
                                temp.getContainer().Items.remove(temp);
                                temp.getContainer().dirty = true;
                            }
                            temp.setContainer(chr.getInventory());
                            chr.getInventory().AddItem(temp);
                            chr.setClothingItem_Torso(temp);
                        }
                        item = chr.getClothingItem_Torso();
                        IsoPlayer.getInstance().SetClothing(zombie.scripting.objects.Item.ClothingBodyLocation.Top, ((Clothing)item).getSpriteName(), ((Clothing)item).getPalette());
                        UIManager.setDragInventory(null);
                    }
                } else
                if(!type.equals("Hands_Clothing"))
                    if(type.equals("Legs_Clothing"))
                    {
                        if((temp instanceof Clothing) && ((Clothing)temp).getBodyLocation() == zombie.scripting.objects.Item.ClothingBodyLocation.Bottoms)
                        {
                            if(chr.getCraftIngredient1() == UIManager.getDragInventory())
                                chr.setCraftIngredient1(null);
                            if(chr.getCraftIngredient2() == UIManager.getDragInventory())
                                chr.setCraftIngredient2(null);
                            if(chr.getCraftIngredient3() == UIManager.getDragInventory())
                                chr.setCraftIngredient3(null);
                            if(chr.getCraftIngredient4() == UIManager.getDragInventory())
                                chr.setCraftIngredient4(null);
                            if(chr.getClothingItem_Head() == UIManager.getDragInventory())
                                chr.setClothingItem_Head(null);
                            if(chr.getClothingItem_Torso() == UIManager.getDragInventory())
                                chr.setClothingItem_Torso(null);
                            if(chr.getClothingItem_Hands() == UIManager.getDragInventory())
                                chr.setClothingItem_Hands(null);
                            if(chr.getClothingItem_Feet() == UIManager.getDragInventory())
                                chr.setClothingItem_Feet(null);
                            if(temp.getContainer() == chr.getInventory())
                            {
                                chr.setClothingItem_Legs(temp);
                                if(chr.getPrimaryHandItem() == temp)
                                    chr.setPrimaryHandItem(null);
                                if(chr.getSecondaryHandItem() == temp)
                                    chr.setSecondaryHandItem(null);
                                if(chr.getClothingItem_Head() == temp)
                                    chr.setClothingItem_Head(null);
                                if(chr.getClothingItem_Torso() == temp)
                                    chr.setClothingItem_Torso(null);
                                if(chr.getClothingItem_Hands() == temp)
                                    chr.setClothingItem_Hands(null);
                                if(chr.getClothingItem_Feet() == temp)
                                    chr.setClothingItem_Feet(null);
                                if(Sidebar.instance.MainHand.item == temp)
                                    Sidebar.instance.MainHand.item = null;
                                if(Sidebar.instance.SecondHand.item == temp)
                                    Sidebar.instance.SecondHand.item = null;
                                if(ClothingPanel.instance.HeadItem.item == temp)
                                    ClothingPanel.instance.HeadItem.item = null;
                                if(ClothingPanel.instance.TorsoItem.item == temp)
                                    ClothingPanel.instance.TorsoItem.item = null;
                                if(ClothingPanel.instance.HandsItem.item == temp)
                                    ClothingPanel.instance.HandsItem.item = null;
                                if(ClothingPanel.instance.FeetItem.item == temp)
                                    ClothingPanel.instance.FeetItem.item = null;
                            } else
                            {
                                if(temp.getContainer() != null)
                                {
                                    temp.getContainer().Items.remove(temp);
                                    temp.getContainer().dirty = true;
                                }
                                temp.setContainer(chr.getInventory());
                                chr.getInventory().AddItem(temp);
                                chr.setClothingItem_Legs(temp);
                            }
                            item = chr.getClothingItem_Legs();
                            IsoPlayer.getInstance().SetClothing(zombie.scripting.objects.Item.ClothingBodyLocation.Bottoms, ((Clothing)item).getSpriteName(), ((Clothing)item).getPalette());
                            UIManager.setDragInventory(null);
                        }
                    } else
                    if(type.equals("Feet_Clothing"))
                    {
                        if((temp instanceof Clothing) && ((Clothing)temp).getBodyLocation() == zombie.scripting.objects.Item.ClothingBodyLocation.Shoes)
                        {
                            if(chr.getCraftIngredient1() == UIManager.getDragInventory())
                                chr.setCraftIngredient1(null);
                            if(chr.getCraftIngredient2() == UIManager.getDragInventory())
                                chr.setCraftIngredient2(null);
                            if(chr.getCraftIngredient3() == UIManager.getDragInventory())
                                chr.setCraftIngredient3(null);
                            if(chr.getCraftIngredient4() == UIManager.getDragInventory())
                                chr.setCraftIngredient4(null);
                            if(chr.getClothingItem_Head() == UIManager.getDragInventory())
                                chr.setClothingItem_Head(null);
                            if(chr.getClothingItem_Torso() == UIManager.getDragInventory())
                                chr.setClothingItem_Torso(null);
                            if(chr.getClothingItem_Hands() == UIManager.getDragInventory())
                                chr.setClothingItem_Hands(null);
                            if(chr.getClothingItem_Legs() == UIManager.getDragInventory())
                                chr.setClothingItem_Legs(null);
                            if(temp.getContainer() == chr.getInventory())
                            {
                                chr.setClothingItem_Feet(temp);
                                if(chr.getPrimaryHandItem() == temp)
                                    chr.setPrimaryHandItem(null);
                                if(chr.getSecondaryHandItem() == temp)
                                    chr.setSecondaryHandItem(null);
                                if(chr.getClothingItem_Head() == temp)
                                    chr.setClothingItem_Head(null);
                                if(chr.getClothingItem_Torso() == temp)
                                    chr.setClothingItem_Torso(null);
                                if(chr.getClothingItem_Hands() == temp)
                                    chr.setClothingItem_Hands(null);
                                if(chr.getClothingItem_Legs() == temp)
                                    chr.setClothingItem_Legs(null);
                                if(Sidebar.instance.MainHand.item == temp)
                                    Sidebar.instance.MainHand.item = null;
                                if(Sidebar.instance.SecondHand.item == temp)
                                    Sidebar.instance.SecondHand.item = null;
                                if(ClothingPanel.instance.HeadItem.item == temp)
                                    ClothingPanel.instance.HeadItem.item = null;
                                if(ClothingPanel.instance.TorsoItem.item == temp)
                                    ClothingPanel.instance.TorsoItem.item = null;
                                if(ClothingPanel.instance.HandsItem.item == temp)
                                    ClothingPanel.instance.HandsItem.item = null;
                                if(ClothingPanel.instance.LegsItem.item == temp)
                                    ClothingPanel.instance.LegsItem.item = null;
                            } else
                            {
                                if(temp.getContainer() != null)
                                {
                                    temp.getContainer().Items.remove(temp);
                                    temp.getContainer().dirty = true;
                                }
                                temp.setContainer(chr.getInventory());
                                chr.getInventory().AddItem(temp);
                                chr.setClothingItem_Feet(temp);
                            }
                            item = chr.getClothingItem_Feet();
                            IsoPlayer.getInstance().SetClothing(zombie.scripting.objects.Item.ClothingBodyLocation.Shoes, ((Clothing)item).getSpriteName(), ((Clothing)item).getPalette());
                            UIManager.setDragInventory(null);
                        }
                    } else
                    if(UIManager.getDragInventory() != null)
                    {
                        item = UIManager.getDragInventory();
                        Object o[];
                        if(getTable() != null && getTable().rawget("onPlaceItem") != null)
                            o = LuaManager.caller.pcall(LuaManager.thread, getTable().rawget("onPlaceItem"), new Object[] {
                                table, item
                            });
                        UIManager.setDragInventory(null);
                    }
        } else
        {
            if(type.equals("Main"))
                item = chr.getPrimaryHandItem();
            else
            if(type.equals("Secondary"))
                item = chr.getSecondaryHandItem();
            else
            if(type.equals("Head_Clothing"))
                item = chr.getClothingItem_Head();
            else
            if(type.equals("Torso_Clothing"))
                item = chr.getClothingItem_Torso();
            else
            if(type.equals("Hands_Clothing"))
                item = chr.getClothingItem_Hands();
            else
            if(type.equals("Legs_Clothing"))
                item = chr.getClothingItem_Legs();
            else
            if(type.equals("Feet_Clothing"))
                item = chr.getClothingItem_Feet();
            UIManager.setDragInventory(item);
        }
        return true;
    }

    public void render()
    {
        if(type.equals("CraftingIngredient"))
            item = chr.getCraftingByIndex(index);
        else
        if(type.equals("Main"))
        {
            if(chr.getPrimaryHandItem() != null && chr.getInventory() != chr.getPrimaryHandItem().getContainer())
                chr.setPrimaryHandItem(null);
            item = chr.getPrimaryHandItem();
        } else
        if(type.equals("Secondary"))
        {
            if(chr.getSecondaryHandItem() != null && chr.getInventory() != chr.getSecondaryHandItem().getContainer())
                chr.setSecondaryHandItem(null);
            item = chr.getSecondaryHandItem();
        } else
        if(type.equals("Head_Clothing"))
        {
            if(chr.getClothingItem_Head() != null && chr.getInventory() != chr.getClothingItem_Head().getContainer())
                chr.setClothingItem_Head(null);
            item = chr.getClothingItem_Head();
        } else
        if(type.equals("Torso_Clothing"))
        {
            if(chr.getClothingItem_Torso() != null && chr.getInventory() != chr.getClothingItem_Torso().getContainer())
                chr.setClothingItem_Torso(null);
            item = chr.getClothingItem_Torso();
        } else
        if(type.equals("Hands_Clothing"))
        {
            if(chr.getClothingItem_Hands() != null && chr.getInventory() != chr.getClothingItem_Hands().getContainer())
                chr.setClothingItem_Hands(null);
            item = chr.getClothingItem_Hands();
        } else
        if(type.equals("Legs_Clothing"))
        {
            if(chr.getClothingItem_Legs() != null && chr.getInventory() != chr.getClothingItem_Legs().getContainer())
                chr.setClothingItem_Legs(null);
            item = chr.getClothingItem_Legs();
        } else
        if(type.equals("Feet_Clothing"))
        {
            if(chr.getClothingItem_Feet() != null && chr.getInventory() != chr.getClothingItem_Feet().getContainer())
                chr.setClothingItem_Feet(null);
            item = chr.getClothingItem_Feet();
        }
        if(tex != null)
        {
            DrawTexture(tex, 0, 0, alpha);
            if(item != null)
            {
                if(item instanceof Food)
                {
                    float temp = ((Food)item).getHeat();
                    boolean red = false;
                    if(temp > 1.0F)
                        temp--;
                    Color C = new Color(70, 153, 211);
                    Color H = new Color(222, 70, 70);
                    Color G = new Color(162, 162, 162);
                }
                if(UIManager.getDragInventory() == item)
                    DrawTextureCol(item.getTex(), tex.getWidth() / 2 - 16, tex.getHeight() / 2 - 16, new Color(255, 255, 255, (byte)(int)(128F * alpha)));
                else
                    DrawTexture(item.getTex(), tex.getWidth() / 2 - 16, tex.getHeight() / 2 - 16, alpha);
                if(item instanceof Drainable)
                {
                    int wid = (int)(24F * ((Drainable)item).getUsedDelta());
                    DrawTextureScaledCol(Texture.getSharedTexture("media/white.png"), tex.getWidth() / 2 - 13, tex.getHeight() - 13, 26, 4, new Color(0, 0, 0, 255));
                    DrawTextureScaledCol(Texture.getSharedTexture("media/white.png"), (1 + tex.getWidth() / 2) - 13, (1 + tex.getHeight()) - 13, wid, 2, new Color(64, 150, 32, 255));
                }
                if(item instanceof HandWeapon)
                {
                    Integer n = Integer.valueOf((int)(((float)((HandWeapon)item).getCondition() / (float)((HandWeapon)item).getConditionMax()) * 5F));
                    if(item.getCondition() > 0 && n.intValue() == 0)
                        n = Integer.valueOf(1);
                    String str = (new StringBuilder()).append("media/ui/QualityStar_").append(n).append(".png").toString();
                    DrawTextureScaledCol(Texture.getSharedTexture(str), 1 + (tex.getWidth() / 2 - 16), 20 + (tex.getHeight() / 2 - 16), 11, 12, new Color(255, 255, 255, 255));
                }
                if(item.getUses() > 1)
                    DrawTextRight((new StringBuilder()).append("x").append((new Integer(item.getUses())).toString()).toString(), (tex.getWidth() / 2 - 16) + 32, tex.getHeight() / 2 - 16, 1.0F, 1.0F, 1.0F, 1.0F);
            }
        }
        super.render();
    }

    private int ExistsInOtherSlot(InventoryItem temp, int index)
    {
        for(int n = 0; n < 4; n++)
        {
            if(n == index)
                continue;
            InventoryItem test = null;
            if(n == 0)
                test = chr.getCraftIngredient1();
            if(n == 1)
                test = chr.getCraftIngredient2();
            if(n == 2)
                test = chr.getCraftIngredient3();
            if(n == 3)
                test = chr.getCraftIngredient4();
            if(test == temp)
                return n;
        }

        return -1;
    }

    public float alpha;
    public int index;
    public IsoGameCharacter chr;
    InventoryItem item;
    Texture tex;
    String type;
}
