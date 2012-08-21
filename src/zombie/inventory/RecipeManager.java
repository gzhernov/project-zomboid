// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   RecipeManager.java

package zombie.inventory;

import java.util.ArrayList;
import java.util.Stack;
import se.krka.kahlua.integration.LuaCaller;
import se.krka.kahlua.integration.LuaReturn;
import se.krka.kahlua.vm.KahluaTable;
import zombie.Lua.LuaManager;
import zombie.characters.IsoPlayer;
import zombie.characters.skills.PerkFactory;
import zombie.inventory.types.Drainable;
import zombie.scripting.ScriptManager;
import zombie.scripting.objects.Recipe;

// Referenced classes of package zombie.inventory:
//            InventoryItem, InventoryItemFactory

public class RecipeManager
{

    public RecipeManager()
    {
    }

    public static boolean DoesUseItemUp(String itemToUse, Recipe recipe)
    {
        for(int n = 0; n < recipe.Source.size(); n++)
            if(itemToUse.equals(((zombie.scripting.objects.Recipe.Source)recipe.Source.get(n)).type) && ((zombie.scripting.objects.Recipe.Source)recipe.Source.get(n)).keep)
                return false;

        return true;
    }

    public static boolean DoesWipeUseDelta(String itemToUse, String itemToMake)
    {
        return "Battery".equals(itemToMake);
    }

    public static int UseAmount(String itemToUse, Recipe recipe)
    {
        for(int n = 0; n < recipe.Source.size(); n++)
            if(itemToUse.equals(((zombie.scripting.objects.Recipe.Source)recipe.Source.get(n)).type))
            {
                int num = ((zombie.scripting.objects.Recipe.Source)recipe.Source.get(n)).count;
                if((itemToUse.contains("Plank") || itemToUse.contains("Nails")) && IsoPlayer.getInstance().getPerkLevel(zombie.characters.skills.PerkFactory.Perks.Woodwork) == 5 && num > 1)
                    num /= 2;
                return num;
            }

        return 1;
    }

    public static Stack getRecipeItems(InventoryItem a, InventoryItem b, InventoryItem c, InventoryItem d)
    {
        RecipeList.clear();
        Stack ri = ScriptManager.instance.getAllRecipes();
        for(int m = 0; m < ri.size(); m++)
        {
            Recipe r = (Recipe)ri.get(m);
            int n = 0;
            if(((zombie.scripting.objects.Recipe.Source)r.Source.get(0)).type == null)
                n++;
            else
            if(!Has(((zombie.scripting.objects.Recipe.Source)r.Source.get(0)).type, a, b, c, d, ((zombie.scripting.objects.Recipe.Source)r.Source.get(0)).count))
                continue;
            if(((zombie.scripting.objects.Recipe.Source)r.Source.get(1)).type == null)
                n++;
            else
            if(!Has(((zombie.scripting.objects.Recipe.Source)r.Source.get(1)).type, a, b, c, d, ((zombie.scripting.objects.Recipe.Source)r.Source.get(1)).count))
                continue;
            if(((zombie.scripting.objects.Recipe.Source)r.Source.get(2)).type == null)
                n++;
            else
            if(!Has(((zombie.scripting.objects.Recipe.Source)r.Source.get(2)).type, a, b, c, d, ((zombie.scripting.objects.Recipe.Source)r.Source.get(2)).count))
                continue;
            if(((zombie.scripting.objects.Recipe.Source)r.Source.get(3)).type == null)
                n++;
            else
            if(!Has(((zombie.scripting.objects.Recipe.Source)r.Source.get(3)).type, a, b, c, d, ((zombie.scripting.objects.Recipe.Source)r.Source.get(3)).count))
                continue;
            if(!HasBlank(n, a, b, c, d))
                continue;
            if(r.LuaTest != null)
            {
                InventoryItem items[] = new InventoryItem[5];
                items[r.FindIndexOf(a)] = a;
                items[r.FindIndexOf(b)] = b;
                items[r.FindIndexOf(c)] = c;
                items[r.FindIndexOf(d)] = d;
                a = items[0];
                b = items[1];
                c = items[2];
                d = items[3];
                LuaReturn ret = LuaManager.caller.protectedCall(LuaManager.thread, LuaManager.env.rawget(r.LuaTest), new Object[] {
                    a, b, c, d
                });
                if(!((Boolean)ret.getFirst()).booleanValue())
                    continue;
            }
            RecipeList.add(r);
        }

        return RecipeList;
    }

    static InventoryItem DoBatteriesCrafting(String itemToUse, InventoryItem a, InventoryItem b, InventoryItem c, InventoryItem d)
    {
        if(Has(itemToUse, a, b, c, d) && Has("Battery", a, b, c, d) && HasBlank(2, a, b, c, d))
        {
            float delta = UseDelta(itemToUse, a, b, c, d);
            float batDelta = UseDelta("Battery", a, b, c, d);
            if(delta == 0.0F && batDelta > 0.0F)
                return InventoryItemFactory.CreateItem(itemToUse, batDelta);
        }
        if(Has(itemToUse, a, b, c, d) && HasBlank(3, a, b, c, d))
        {
            float delta = UseDelta(itemToUse, a, b, c, d);
            if(delta > 0.0F)
                return InventoryItemFactory.CreateItem("Battery", delta);
        }
        return null;
    }

    static boolean Has(String type, InventoryItem a, InventoryItem b, InventoryItem c, InventoryItem d)
    {
        return a != null && a.type.equals(type) || b != null && b.type.equals(type) || c != null && c.type.equals(type) || d != null && d.type.equals(type);
    }

    static boolean Has(String type, InventoryItem a, InventoryItem b, InventoryItem c, InventoryItem d, int num)
    {
        String typeArray[] = type.split("/");
        for(int n = 0; n < typeArray.length; n++)
        {
            type = typeArray[n];
            int oldnum = num;
            if((type.contains("Plank") || type.contains("Nails")) && IsoPlayer.getInstance().getPerkLevel(zombie.characters.skills.PerkFactory.Perks.Woodwork) == 5 && num > 1)
                num /= 2;
            if(a != null && a.type.equals(type) || b != null && b.type.equals(type) || c != null && c.type.equals(type) || d != null && d.type.equals(type))
            {
                if(a != null && a.type.equals(type) && a.uses >= num)
                    return true;
                if(b != null && b.type.equals(type) && b.uses >= num)
                    return true;
                if(c != null && c.type.equals(type) && c.uses >= num)
                    return true;
                if(d != null && d.type.equals(type) && d.uses >= num)
                    return true;
            }
            num = oldnum;
        }

        return false;
    }

    static boolean HasBlank(int n, InventoryItem a, InventoryItem b, InventoryItem c, InventoryItem d)
    {
        int cn = 0;
        if(a == null)
            cn++;
        if(b == null)
            cn++;
        if(c == null)
            cn++;
        if(d == null)
            cn++;
        return cn == n;
    }

    static float UseDelta(String type, InventoryItem a, InventoryItem b, InventoryItem c, InventoryItem d)
    {
        if(a != null && a.type.equals(type) && (a instanceof Drainable))
            return ((Drainable)a).getUsedDelta();
        if(b != null && b.type.equals(type) && (b instanceof Drainable))
            return ((Drainable)b).getUsedDelta();
        if(c != null && c.type.equals(type) && (c instanceof Drainable))
            return ((Drainable)c).getUsedDelta();
        if(d != null && d.type.equals(type) && (d instanceof Drainable))
            return ((Drainable)d).getUsedDelta();
        else
            return 1.0F;
    }

    private static Stack RecipeList = new Stack();

}
