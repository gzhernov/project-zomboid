// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   NewCraftingPanel.java

package zombie.ui;

import java.util.Stack;
import se.krka.kahlua.integration.LuaCaller;
import se.krka.kahlua.vm.KahluaTable;
import zombie.Lua.LuaManager;
import zombie.characters.IsoGameCharacter;
import zombie.characters.IsoPlayer;
import zombie.characters.skills.PerkFactory;
import zombie.core.Core;
import zombie.core.textures.Texture;
import zombie.inventory.*;
import zombie.inventory.types.Drainable;
import zombie.inventory.types.Food;
import zombie.scripting.objects.Recipe;
import zombie.scripting.objects.ScriptModule;

// Referenced classes of package zombie.ui:
//            NewWindow, VirtualItemSlot, GenericButton, SpeedControls, 
//            Sidebar, HUDButton

public class NewCraftingPanel extends NewWindow
{

    public NewCraftingPanel(int x, int y)
    {
        super(x, y, 10, 10, true);
        RecipeList = null;
        activeRecipe = 0;
        ResizeToFitY = false;
        visible = false;
        instance = this;
        crafting = Texture.getSharedTexture("media/ui/Crafting_Overlay.png");
        width = 155;
        height = 84 + titleRight.getHeight() + 5;
        int cx = 6;
        int px = 37;
        ing1 = new VirtualItemSlot("CraftingIngredient", cx, 25, "media/ui/ItemBackground_Grey.png", IsoPlayer.getInstance());
        ing2 = new VirtualItemSlot("CraftingIngredient", cx + px, 25, "media/ui/ItemBackground_Grey.png", IsoPlayer.getInstance());
        ing3 = new VirtualItemSlot("CraftingIngredient", cx + px + px, 25, "media/ui/ItemBackground_Grey.png", IsoPlayer.getInstance());
        ing4 = new VirtualItemSlot("CraftingIngredient", cx + px + px + px, 25, "media/ui/ItemBackground_Grey.png", IsoPlayer.getInstance());
        result = new VirtualItemSlot("CraftingResult", cx + px + px / 2, 75, "media/ui/ItemBackground_Grey.png", IsoPlayer.getInstance());
        FaceCycleLeft = new GenericButton(this, (int)result.getX() - 12, (int)result.getY() + 5, 11, 17, "CycleFaceLeft", " ", Texture.getSharedTexture("media/ui/LeftArrow_Up.png"), Texture.getSharedTexture("media/ui/LeftArrow_Down.png"));
        FaceCycleRight = new GenericButton(this, (int)result.getX() + result.getWidth() + 1, (int)result.getY() + 5, 11, 17, "CycleFaceRight", " ", Texture.getSharedTexture("media/ui/RightArrow_Up.png"), Texture.getSharedTexture("media/ui/RightArrow_Down.png"));
        ing1.index = 0;
        ing2.index = 1;
        ing3.index = 2;
        ing4.index = 3;
        AddChild(ing1);
        AddChild(ing2);
        AddChild(ing3);
        AddChild(ing4);
        AddChild(result);
        AddChild(FaceCycleLeft);
        FaceCycleLeft.visible = false;
        FaceCycleRight.visible = false;
        AddChild(FaceCycleRight);
    }

    public void render()
    {
        if(!isVisible() || SpeedControls.instance.getCurrentGameSpeed() == 0)
        {
            return;
        } else
        {
            super.render();
            DrawTexture(crafting, 5, titleRight.getHeight() - 5, alpha);
            return;
        }
    }

    public void update()
    {
        if(!isVisible() || SpeedControls.instance.getCurrentGameSpeed() == 0)
            return;
        super.update();
        if(FaceCycleRight.clicked)
        {
            FaceCycleRight.clicked = false;
            activeRecipe++;
            RecalcRecipe();
        }
        if(FaceCycleLeft.clicked)
        {
            FaceCycleLeft.clicked = false;
            activeRecipe--;
            RecalcRecipe();
        }
        float absY = getAbsoluteY();
        float dif = absY - (Sidebar.Crafting.getY() - 70F);
        float val = (float)Core.getInstance().getOffscreenHeight() - absY;
        if(val > 0.0F)
            dif /= val;
        else
            dif = 1.0F;
        dif *= 4F;
        dif = 1.0F - dif;
        if(dif < 0.0F)
            dif = 0.0F;
        if(ing1.item != lastIng1Item)
            RecalcRecipe();
        if(ing2.item != lastIng2Item)
            RecalcRecipe();
        if(ing3.item != lastIng3Item)
            RecalcRecipe();
        if(ing4.item != lastIng4Item)
            RecalcRecipe();
        lastIng1Item = ing1.item;
        lastIng2Item = ing2.item;
        lastIng3Item = ing3.item;
        lastIng4Item = ing4.item;
    }

    void PerformMakeItem()
    {
        if(activeRecipe >= RecipeList.size())
            return;
        Recipe rec = (Recipe)RecipeList.get(activeRecipe);
        if(rec.LuaGrab != null)
        {
            InventoryItem items[] = new InventoryItem[5];
            items[rec.FindIndexOf(ing1.item)] = ing1.item;
            items[rec.FindIndexOf(ing2.item)] = ing2.item;
            items[rec.FindIndexOf(ing3.item)] = ing3.item;
            items[rec.FindIndexOf(ing4.item)] = ing4.item;
            InventoryItem aa = items[0];
            InventoryItem b = items[1];
            InventoryItem c = items[2];
            InventoryItem d = items[3];
            se.krka.kahlua.integration.LuaReturn ret = LuaManager.caller.protectedCall(LuaManager.thread, LuaManager.env.rawget(((Recipe)RecipeList.get(activeRecipe)).LuaGrab), new Object[] {
                aa, b, c, d, result.item
            });
        }
        if(result.item instanceof Food)
            IsoPlayer.getInstance().getXp().AddXP(zombie.characters.skills.PerkFactory.Perks.Cooking, 3);
        if(result.item.getType().contains("Plank"))
            IsoPlayer.getInstance().getXp().AddXP(zombie.characters.skills.PerkFactory.Perks.Woodwork, 3);
        if(ing1.item != null)
        {
            if(RecipeManager.DoesWipeUseDelta(ing1.item.getType(), result.item.getType()))
                ((Drainable)ing1.item).setUsedDelta(0.0F);
            if(RecipeManager.DoesUseItemUp(ing1.item.getType(), (Recipe)RecipeList.get(activeRecipe)))
            {
                int size = RecipeManager.UseAmount(ing1.item.getType(), (Recipe)RecipeList.get(activeRecipe));
                for(int n = 0; n < size; n++)
                    ing1.item.Use(true);

            }
            if(ing1.item.getType().contains("Plank"))
                IsoPlayer.getInstance().getXp().AddXP(zombie.characters.skills.PerkFactory.Perks.Woodwork, 3);
        }
        if(ing2.item != null)
        {
            if(RecipeManager.DoesWipeUseDelta(ing2.item.getType(), result.item.getType()))
                ((Drainable)ing2.item).setUsedDelta(0.0F);
            if(RecipeManager.DoesUseItemUp(ing2.item.getType(), (Recipe)RecipeList.get(activeRecipe)))
            {
                int size = RecipeManager.UseAmount(ing2.item.getType(), (Recipe)RecipeList.get(activeRecipe));
                for(int n = 0; n < size; n++)
                    ing2.item.Use(true);

            }
            if(ing2.item.getType().contains("Plank"))
                IsoPlayer.getInstance().getXp().AddXP(zombie.characters.skills.PerkFactory.Perks.Woodwork, 3);
        }
        if(ing3.item != null)
        {
            if(RecipeManager.DoesWipeUseDelta(ing3.item.getType(), result.item.getType()))
                ((Drainable)ing3.item).setUsedDelta(0.0F);
            if(RecipeManager.DoesUseItemUp(ing3.item.getType(), (Recipe)RecipeList.get(activeRecipe)))
            {
                int size = RecipeManager.UseAmount(ing3.item.getType(), (Recipe)RecipeList.get(activeRecipe));
                for(int n = 0; n < size; n++)
                    ing3.item.Use(true);

            }
            if(ing3.item.getType().contains("Plank"))
                IsoPlayer.getInstance().getXp().AddXP(zombie.characters.skills.PerkFactory.Perks.Woodwork, 3);
        }
        if(ing4.item != null)
        {
            if(RecipeManager.DoesWipeUseDelta(ing4.item.getType(), result.item.getType()))
                ((Drainable)ing4.item).setUsedDelta(0.0F);
            if(RecipeManager.DoesUseItemUp(ing4.item.getType(), (Recipe)RecipeList.get(activeRecipe)))
            {
                int size = RecipeManager.UseAmount(ing4.item.getType(), (Recipe)RecipeList.get(activeRecipe));
                for(int n = 0; n < size; n++)
                    ing4.item.Use(true);

            }
            if(ing4.item.getType().contains("Plank"))
                IsoPlayer.getInstance().getXp().AddXP(zombie.characters.skills.PerkFactory.Perks.Woodwork, 3);
        }
        RecalcRecipe();
    }

    private void RecalcRecipe()
    {
        RecipeList = RecipeManager.getRecipeItems(ing1.item, ing2.item, ing3.item, ing4.item);
        if(RecipeList.isEmpty())
        {
            result.item = null;
            FaceCycleLeft.setVisible(false);
            FaceCycleRight.setVisible(false);
            return;
        }
        if(RecipeList.size() > 1)
        {
            FaceCycleLeft.setVisible(true);
            FaceCycleRight.setVisible(true);
        } else
        {
            FaceCycleLeft.setVisible(false);
            FaceCycleRight.setVisible(false);
        }
        if(activeRecipe > RecipeList.size() - 1)
            activeRecipe = activeRecipe % RecipeList.size();
        if(activeRecipe < 0)
            activeRecipe = RecipeList.size() - 1;
        String a = ((Recipe)RecipeList.get(activeRecipe)).Result.type;
        if(!a.contains("."))
            a = (new StringBuilder()).append(((Recipe)RecipeList.get(activeRecipe)).module.name).append(".").append(a).toString();
        InventoryItem res = InventoryItemFactory.CreateItem(a);
        result.item = res;
        Recipe rec = (Recipe)RecipeList.get(activeRecipe);
        if(rec.LuaCreate != null)
        {
            InventoryItem items[] = new InventoryItem[5];
            items[rec.FindIndexOf(ing1.item)] = ing1.item;
            items[rec.FindIndexOf(ing2.item)] = ing2.item;
            items[rec.FindIndexOf(ing3.item)] = ing3.item;
            items[rec.FindIndexOf(ing4.item)] = ing4.item;
            InventoryItem aa = items[0];
            InventoryItem b = items[1];
            InventoryItem c = items[2];
            InventoryItem d = items[3];
            se.krka.kahlua.integration.LuaReturn ret = LuaManager.caller.protectedCall(LuaManager.thread, LuaManager.env.rawget(((Recipe)RecipeList.get(activeRecipe)).LuaCreate), new Object[] {
                aa, b, c, d, result.item
            });
        }
    }

    public static NewCraftingPanel instance;
    public Texture crafting;
    public VirtualItemSlot ing1;
    public VirtualItemSlot ing2;
    public VirtualItemSlot ing3;
    public VirtualItemSlot ing4;
    public VirtualItemSlot result;
    GenericButton FaceCycleLeft;
    GenericButton FaceCycleRight;
    InventoryItem lastIng1Item;
    InventoryItem lastIng2Item;
    InventoryItem lastIng3Item;
    InventoryItem lastIng4Item;
    Stack RecipeList;
    public int activeRecipe;
}
