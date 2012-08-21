// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   IScriptObjectStore.java

package zombie.scripting;

import zombie.characters.IsoGameCharacter;
import zombie.scripting.objects.Inventory;
import zombie.scripting.objects.Item;
import zombie.scripting.objects.QuestTaskCondition;
import zombie.scripting.objects.Recipe;
import zombie.scripting.objects.Room;
import zombie.scripting.objects.Script;
import zombie.scripting.objects.ScriptActivatable;
import zombie.scripting.objects.ScriptCharacter;
import zombie.scripting.objects.ScriptContainer;
import zombie.scripting.objects.ScriptFlag;
import zombie.scripting.objects.Waypoint;
import zombie.scripting.objects.Zone;

public interface IScriptObjectStore
{

    public abstract Inventory getInventory(String s);

    public abstract ScriptCharacter getCharacter(String s);

    public abstract IsoGameCharacter getCharacterActual(String s);

    public abstract Waypoint getWaypoint(String s);

    public abstract ScriptContainer getScriptContainer(String s);

    public abstract Room getRoom(String s);

    public abstract ScriptActivatable getActivatable(String s);

    public abstract String getFlagValue(String s);

    public abstract ScriptFlag getFlag(String s);

    public abstract Zone getZone(String s);

    public abstract QuestTaskCondition getQuestCondition(String s);

    public abstract Item getItem(String s);

    public abstract Recipe getRecipe(String s);

    public abstract Script getScript(String s);
}
