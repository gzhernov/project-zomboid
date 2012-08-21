// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   InventoryFlowControl.java

package zombie.ui;

import java.util.Stack;
import org.lwjgl.input.Keyboard;
import org.lwjgl.util.Rectangle;
import zombie.characters.IsoPlayer;
import zombie.core.Collections.NulledArrayList;
import zombie.core.Color;
import zombie.core.textures.Texture;
import zombie.input.Mouse;
import zombie.inventory.InventoryItem;
import zombie.inventory.ItemContainer;
import zombie.inventory.types.*;
import zombie.scripting.objects.Item;

// Referenced classes of package zombie.ui:
//            UIElement, ObjectTooltip, NewContainerPanel, SpeedControls, 
//            UIManager, PZConsole, NewCraftingPanel, VirtualItemSlot

public class InventoryFlowControl extends UIElement
{

    public InventoryFlowControl(int x, int y, int widthtiles, int heighttiles, ItemContainer container)
    {
        toolTip = new ObjectTooltip();
        clicked = false;
        hTiles = 0;
        InventoryDrawn = new Stack();
        InventoryItemsDrawn = new Stack();
        wTiles = 0;
        mouseOver = false;
        mouseItem = null;
        timeSinceClick = 0;
        startRow = 0;
        bkg = null;
        white = null;
        Inventory_TransferLeft = Texture.getSharedTexture("media/ui/Inventory_TransferLeft.png");
        Inventory_TransferRight = Texture.getSharedTexture("media/ui/Inventory_TransferRight.png");
        this.x = x;
        this.y = y;
        width = widthtiles * 32;
        height = heighttiles * 32;
        Container = container;
        wTiles = widthtiles;
        toolTip.alpha = toolTip.targetAlpha = 0.7F;
        hTiles = heighttiles;
        AddChild(toolTip);
        toolTip.visible = false;
    }

    public boolean onMouseDown(int x, int y)
    {
        if(timeSinceClick < 5)
            DoubleClick(x, y);
        clicked = true;
        setCapture(true);
        return false;
    }

    public boolean onMouseMove(int dx, int dy)
    {
        if(!isVisible() || SpeedControls.instance.getCurrentGameSpeed() == 0)
        {
            return true;
        } else
        {
            mouseOver = true;
            toolTip.setX(Mouse.getXA() - getAbsoluteX());
            toolTip.setY(Mouse.getYA() - getAbsoluteY());
            toolTip.setX(toolTip.getX() + 5F);
            toolTip.setY(toolTip.getY() + 5F);
            InventoryItem item = getInventoryFromRect((int)toolTip.getX(), (int)toolTip.getY());
            mouseItem = item;
            return true;
        }
    }

    public void onMouseMoveOutside(int dx, int dy)
    {
        toolTip.setVisible(false);
        mouseOver = false;
    }

    public boolean onMouseUp(int x, int y)
    {
        if(!isVisible() || SpeedControls.instance.getCurrentGameSpeed() == 0)
            return true;
        if(UIManager.getDragInventory() != null)
        {
            if(IsoPlayer.getInstance().getCraftIngredient1() == UIManager.getDragInventory())
                IsoPlayer.getInstance().setCraftIngredient1(null);
            if(IsoPlayer.getInstance().getCraftIngredient2() == UIManager.getDragInventory())
                IsoPlayer.getInstance().setCraftIngredient2(null);
            if(IsoPlayer.getInstance().getCraftIngredient3() == UIManager.getDragInventory())
                IsoPlayer.getInstance().setCraftIngredient3(null);
            if(IsoPlayer.getInstance().getCraftIngredient4() == UIManager.getDragInventory())
                IsoPlayer.getInstance().setCraftIngredient4(null);
            if(IsoPlayer.getInstance().getClothingItem_Head() == UIManager.getDragInventory())
                IsoPlayer.getInstance().setClothingItem_Head(null);
            if(IsoPlayer.getInstance().getClothingItem_Torso() == UIManager.getDragInventory())
            {
                IsoPlayer.getInstance().SetClothing(zombie.scripting.objects.Item.ClothingBodyLocation.Top, null, null);
                IsoPlayer.getInstance().setClothingItem_Torso(null);
            }
            if(IsoPlayer.getInstance().getClothingItem_Hands() == UIManager.getDragInventory())
                IsoPlayer.getInstance().setClothingItem_Hands(null);
            if(IsoPlayer.getInstance().getClothingItem_Legs() == UIManager.getDragInventory())
            {
                IsoPlayer.getInstance().SetClothing(zombie.scripting.objects.Item.ClothingBodyLocation.Bottoms, null, null);
                IsoPlayer.getInstance().setClothingItem_Legs(null);
            }
            if(IsoPlayer.getInstance().getClothingItem_Feet() == UIManager.getDragInventory())
            {
                IsoPlayer.getInstance().SetClothing(zombie.scripting.objects.Item.ClothingBodyLocation.Shoes, null, null);
                IsoPlayer.getInstance().setClothingItem_Feet(null);
            }
        }
        if(clicked)
        {
            NewContainerPanel p = UIManager.getOpenContainer();
            if(p != null)
            {
                if(mouseItem != null && mouseItem.getContainer() == IsoPlayer.instance.getInventory())
                {
                    Rectangle rect = getInventoryRectFromRect((int)toolTip.getX(), (int)toolTip.getY());
                    if(rect != null && x - rect.getX() > 24)
                    {
                        p.Flow.Container.AddItem(mouseItem);
                        p.Flow.Container.dirty = true;
                        IsoPlayer.instance.getInventory().Items.remove(mouseItem);
                        IsoPlayer.instance.getInventory().dirty = true;
                        toolTip.setX(Mouse.getXA() - getAbsoluteX());
                        toolTip.setY(Mouse.getYA() - getAbsoluteY());
                        toolTip.setX(toolTip.getX() + 5F);
                        toolTip.setY(toolTip.getY() + 5F);
                        dorender(false);
                        InventoryItem item = getInventoryFromRect((int)toolTip.getX(), (int)toolTip.getY());
                        mouseItem = item;
                        mouseOver = true;
                        return true;
                    }
                }
                if(mouseItem != null && mouseItem.getContainer() == p.Flow.Container)
                {
                    Rectangle rect = getInventoryRectFromRect((int)toolTip.getX(), (int)toolTip.getY());
                    if(rect != null && x - rect.getX() <= 8)
                    {
                        IsoPlayer.instance.getInventory().AddItem(mouseItem);
                        IsoPlayer.instance.getInventory().dirty = true;
                        p.Flow.Container.Items.remove(mouseItem);
                        p.Flow.Container.dirty = true;
                        toolTip.setX(Mouse.getXA() - getAbsoluteX());
                        toolTip.setY(Mouse.getYA() - getAbsoluteY());
                        toolTip.setX(toolTip.getX() + 5F);
                        toolTip.setY(toolTip.getY() + 5F);
                        dorender(false);
                        InventoryItem item = getInventoryFromRect((int)toolTip.getX(), (int)toolTip.getY());
                        mouseItem = item;
                        mouseOver = true;
                        return true;
                    }
                }
            }
            if(clicked)
            {
                timeSinceClick = 0;
                if(UIManager.getDragInventory() == null)
                    UIManager.setDragInventory(getInventoryFromRect(x, y));
                else
                if(UIManager.getDragInventory() == IsoPlayer.getInstance().getPrimaryHandItem() && UIManager.getDragInventory().getContainer() == Container)
                {
                    IsoPlayer.getInstance().setPrimaryHandItem(null);
                    UIManager.setDragInventory(null);
                } else
                if(UIManager.getDragInventory() == IsoPlayer.getInstance().getSecondaryHandItem() && UIManager.getDragInventory().getContainer() == Container)
                {
                    IsoPlayer.getInstance().setSecondaryHandItem(null);
                    UIManager.setDragInventory(null);
                } else
                if(UIManager.getDragInventory() == IsoPlayer.getInstance().getClothingItem_Head() && UIManager.getDragInventory().getContainer() == Container)
                {
                    IsoPlayer.getInstance().setClothingItem_Head(null);
                    UIManager.setDragInventory(null);
                } else
                if(UIManager.getDragInventory() == IsoPlayer.getInstance().getClothingItem_Torso() && UIManager.getDragInventory().getContainer() == Container)
                {
                    IsoPlayer.getInstance().setClothingItem_Torso(null);
                    UIManager.setDragInventory(null);
                } else
                if(UIManager.getDragInventory() == IsoPlayer.getInstance().getClothingItem_Hands() && UIManager.getDragInventory().getContainer() == Container)
                {
                    IsoPlayer.getInstance().setClothingItem_Hands(null);
                    UIManager.setDragInventory(null);
                } else
                if(UIManager.getDragInventory() == IsoPlayer.getInstance().getClothingItem_Legs() && UIManager.getDragInventory().getContainer() == Container)
                {
                    IsoPlayer.getInstance().setClothingItem_Legs(null);
                    UIManager.setDragInventory(null);
                } else
                if(UIManager.getDragInventory() == IsoPlayer.getInstance().getClothingItem_Feet() && UIManager.getDragInventory().getContainer() == Container)
                {
                    IsoPlayer.getInstance().setClothingItem_Feet(null);
                    UIManager.setDragInventory(null);
                } else
                if(UIManager.getDragInventory().getContainer() != Container)
                {
                    if(UIManager.getDragInventory().getContainer() != null)
                    {
                        if(!PZConsole.instance.isVisible() && Keyboard.isKeyDown(29))
                        {
                            Container.AddItem(UIManager.getDragInventory(), UIManager.getDragInventory().getUses());
                            int uses = UIManager.getDragInventory().getUses();
                            for(int n = 0; n < uses; n++)
                                UIManager.getDragInventory().Use(true, true);

                        } else
                        {
                            Container.AddItem(UIManager.getDragInventory(), 1);
                            UIManager.getDragInventory().Use(true, true);
                        }
                        if(UIManager.getDragInventory() != null && UIManager.getDragInventory().getUses() <= 0)
                            UIManager.setDragInventory(null);
                        return true;
                    }
                    if(UIManager.getDragInventory() == NewCraftingPanel.instance.result.item)
                        NewCraftingPanel.instance.PerformMakeItem();
                    Container.AddItem(UIManager.getDragInventory());
                    UIManager.getDragInventory().setContainer(Container);
                    UIManager.setDragInventory(null);
                } else
                {
                    UIManager.setDragInventory(getInventoryFromRect(x, y));
                }
            }
        }
        clicked = false;
        setCapture(false);
        return false;
    }

    public void render()
    {
        dorender(true);
    }

    public void dorender(boolean bDrawTextures)
    {
        InventoryDrawn.clear();
        InventoryItemsDrawn.clear();
        int x = 0;
        int y = 0;
        if(bDrawTextures && bkg == null)
            bkg = Texture.getSharedTexture("media/ui/ItemBackground_Grey.png");
        for(int m = 0; m < Container.Items.size(); m++)
        {
            InventoryItem item = (InventoryItem)Container.Items.get(m);
            if(item == null)
            {
                int n = 0;
                continue;
            }
            if(y < startRow)
            {
                if(++x >= wTiles)
                {
                    y++;
                    x = 0;
                }
                continue;
            }
            if(y - startRow >= hTiles)
                break;
            if(bDrawTextures)
            {
                DrawTextureCol(bkg, x * 32, (y - startRow) * 32, Color.white);
                if(item instanceof Food)
                {
                    float temp = ((Food)item).getHeat();
                    boolean red = false;
                    if(temp > 1.0F)
                    {
                        temp--;
                        red = true;
                    }
                    if(red)
                    {
                        tempcol.setColor(G, H, temp);
                        DrawTextureScaledCol(null, x * 32, (y - startRow) * 32, 32, 32, tempcol);
                    } else
                    {
                        tempcol.setColor(C, G, temp);
                        DrawTextureScaledCol(null, x * 32, (y - startRow) * 32, 32, 32, tempcol);
                    }
                }
                Exception exception;
                try
                {
                    if(UIManager.getDragInventory() == item)
                        DrawTextureCol(item.getTex(), x * 32, (y - startRow) * 32, trans);
                    else
                        DrawTextureCol(item.getTex(), x * 32, (y - startRow) * 32, Color.white);
                }
                catch(Exception ex)
                {
                    exception = ex;
                }
                if(item instanceof Drainable)
                {
                    int wid = (int)(24F * ((Drainable)item).getUsedDelta());
                    DrawTextureScaledCol(null, (x * 32 + item.getTex().getWidth() / 2) - 13, ((y - startRow) * 32 + 32) - 5, 26, 4, Color.black);
                    DrawTextureScaledCol(null, (1 + x * 32 + item.getTex().getWidth() / 2) - 13, (1 + (y - startRow) * 32 + 32) - 5, wid, 2, colDrain);
                }
                if(item instanceof HandWeapon)
                {
                    Integer n = Integer.valueOf((int)(((float)((HandWeapon)item).getCondition() / (float)((HandWeapon)item).getConditionMax()) * 5F));
                    if(item.getCondition() > 0 && n.intValue() == 0)
                        n = Integer.valueOf(1);
                    String str = (new StringBuilder()).append("media/ui/QualityStar_").append(n).append(".png").toString();
                    DrawTextureScaledCol(Texture.getSharedTexture(str), 1 + x * 32, 20 + (y - startRow) * 32, 11, 12, Color.white);
                }
                if(item.getUses() > 1)
                    DrawTextRight((new StringBuilder()).append("x").append((new Integer(item.getUses())).toString()).toString(), x * 32 + 32, (y - startRow) * 32, 1.0F, 1.0F, 1.0F, 1.0F);
                if(mouseItem == item)
                {
                    Texture tex = null;
                    if(Container.parent instanceof IsoPlayer)
                        tex = Inventory_TransferRight;
                    else
                        tex = Inventory_TransferLeft;
                    NewContainerPanel p = UIManager.getOpenContainer();
                    if(p != null)
                        DrawTextureCol(tex, x * 32, (y - startRow) * 32, Color.white);
                }
            }
            InventoryDrawn.add(new Rectangle(x * 32, (y - startRow) * 32, 34, 34));
            InventoryItemsDrawn.add(item);
            if(++x >= wTiles)
            {
                y++;
                x = 0;
            }
        }

        if(bDrawTextures)
            do
            {
                if(y - startRow >= hTiles)
                    break;
                if(bkg != null)
                    DrawTextureCol(bkg, x * 32, (y - startRow) * 32, Color.white);
                if(++x >= wTiles)
                {
                    y++;
                    x = 0;
                }
            } while(true);
        if(bDrawTextures && mouseOver)
            if(mouseItem != null)
            {
                toolTip.setVisible(true);
                mouseItem.DoTooltip(toolTip);
            } else
            {
                toolTip.setVisible(false);
            }
    }

    public void update()
    {
        super.update();
        timeSinceClick++;
        if(startRow < 0)
            startRow = 0;
        if(Container.Items.size() < wTiles * hTiles)
            startRow = 0;
        else
        if(startRow + hTiles > Container.Items.size())
            startRow = Container.Items.size() - hTiles;
    }

    private InventoryItem getInventoryFromRect(int mx, int my)
    {
        for(int n = 0; n < InventoryDrawn.size(); n++)
            if(((Rectangle)InventoryDrawn.get(n)).contains(mx, my))
                return (InventoryItem)InventoryItemsDrawn.get(n);

        return null;
    }

    private Rectangle getInventoryRectFromRect(int mx, int my)
    {
        for(int n = 0; n < InventoryDrawn.size(); n++)
            if(((Rectangle)InventoryDrawn.get(n)).contains(mx, my))
                return (Rectangle)InventoryDrawn.get(n);

        return null;
    }

    private void DoubleClick(int mx, int my)
    {
        InventoryItem item = getInventoryFromRect(mx, my);
        if(Container == IsoPlayer.getInstance().getInventory())
            return;
        if(UIManager.getDragInventory() == item)
        {
            Container.Items.remove(item);
            Container.dirty = true;
            IsoPlayer.getInstance().getInventory().AddItem(item);
            UIManager.setDragInventory(null);
        }
    }

    ObjectTooltip toolTip;
    boolean clicked;
    public ItemContainer Container;
    int hTiles;
    Stack InventoryDrawn;
    Stack InventoryItemsDrawn;
    int wTiles;
    private boolean mouseOver;
    public Texture Inventory_TransferLeft;
    public Texture Inventory_TransferRight;
    InventoryItem mouseItem;
    int timeSinceClick;
    public int startRow;
    Texture bkg;
    Texture white;
    static Color C = new Color(70, 153, 211);
    static Color H = new Color(222, 70, 70);
    static Color G = new Color(162, 162, 162, 0);
    static Color tempcol = new Color(162, 162, 162, 0);
    static Color colDrain = new Color(64, 150, 32, 255);
    static Color trans = new Color(255, 255, 255, 128);

}
