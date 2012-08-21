// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ScriptModule.java

package zombie.scripting.objects;

import gnu.trove.map.hash.THashMap;
import java.util.ArrayList;
import java.util.Stack;
import javax.swing.JOptionPane;
import zombie.characters.IsoGameCharacter;
import zombie.characters.IsoPlayer;
import zombie.core.Rand;
import zombie.scripting.*;

// Referenced classes of package zombie.scripting.objects:
//            BaseScriptObject, Zone, Waypoint, Room, 
//            ScriptCharacter, Item, Door, ScriptActivatable, 
//            ScriptTalker, LanguageDefinition, ScriptContainer, QuestTaskCondition, 
//            Recipe, RandomSelector, Inventory, ScriptFlag, 
//            Script, Trigger, ContainerDistribution, FloorDistribution, 
//            ShelfDistribution

public class ScriptModule extends BaseScriptObject
    implements IScriptObjectStore
{
    public static class Exit
    {

        public int fromX1;
        public int fromY1;
        public int fromZ1;
        public int toX1;
        public int toY1;
        public int toZ1;
        public int fromX2;
        public int fromY2;
        public int fromZ2;
        public int toX2;
        public int toY2;
        public int toZ2;
        public String map;

        public Exit()
        {
        }
    }


    public ScriptModule()
    {
        ValidMaps = new Stack();
        ExitPoints = new Stack();
        WaypointMap = new THashMap();
        RoomMap = new THashMap();
        RoomList = new Stack();
        DoorMap = new THashMap();
        ItemMap = new THashMap();
        ScriptMap = new THashMap();
        CharacterMap = new THashMap();
        RecipeMap = new THashMap();
        InventoryMap = new THashMap();
        ActivatableMap = new THashMap();
        TalkerMap = new THashMap();
        ScriptContainerMap = new THashMap();
        ConditionMap = new THashMap();
        FlagMap = new THashMap();
        ZoneMap = new THashMap();
        ZoneList = new Stack();
        RandomSelectorMap = new THashMap();
        ContainerDistributions = new Stack();
        FloorDistributions = new Stack();
        ShelfDistributions = new Stack();
        Imports = new Stack();
        disabled = false;
        LanguageMap = new THashMap();
    }

    public boolean ValidMapCheck(String filename)
    {
        if(ValidMaps.isEmpty())
            return true;
        return ValidMaps.contains(filename);
    }

    public void Load(String name, String strArray)
    {
        this.name = name;
        value = strArray.trim();
        ScriptManager.instance.CurrentLoadingModule = this;
        ParseScriptPP(value);
        ParseScript(value);
        value = "";
    }

    private void CreateFromTokenPP(String token)
    {
        token = token.trim();
        if(token.indexOf("zone") == 0)
        {
            String waypoint[] = token.split("[{}]");
            String name = waypoint[0];
            name = name.replace("zone", "");
            name = name.trim();
            String coords[] = waypoint[1].split(",");
            Zone way = new Zone();
            ZoneMap.put(name, way);
            ZoneList.add(way);
        } else
        if(token.indexOf("waypoint") == 0)
        {
            String waypoint[] = token.split("[{}]");
            String name = waypoint[0];
            name = name.replace("waypoint", "");
            name = name.trim();
            String coords[] = waypoint[1].split(",");
            Waypoint way = new Waypoint();
            WaypointMap.put(name, way);
        } else
        if(token.indexOf("room") == 0)
        {
            String waypoint[] = token.split("[{}]");
            String name = waypoint[0];
            name = name.replace("room", "");
            name = name.trim();
            String coords[] = waypoint[1].split(",");
            Room way = new Room();
            RoomMap.put(name, way);
            RoomList.add(way);
        } else
        if(token.indexOf("character") == 0)
        {
            String waypoint[] = token.split("[{}]");
            String name = waypoint[0];
            name = name.replace("character", "");
            name = name.trim();
            String coords[] = waypoint[1].split(",");
            ScriptCharacter way = new ScriptCharacter();
            CharacterMap.put(name, way);
        } else
        if(token.indexOf("item") == 0)
        {
            String waypoint[] = token.split("[{}]");
            String name = waypoint[0];
            name = name.replace("item", "");
            name = name.trim();
            String coords[] = waypoint[1].split(",");
            Item way = new Item();
            ItemMap.put(name, way);
        } else
        if(token.indexOf("door") == 0)
        {
            String waypoint[] = token.split("[{}]");
            String name = waypoint[0];
            name = name.replace("door", "");
            name = name.trim();
            String coords[] = waypoint[1].split(",");
            Door way = new Door();
            DoorMap.put(name, way);
        } else
        if(token.indexOf("activatable") == 0)
        {
            String waypoint[] = token.split("[{}]");
            String name = waypoint[0];
            name = name.replace("activatable", "");
            name = name.trim();
            String coords[] = waypoint[1].split(",");
            ScriptActivatable way = new ScriptActivatable();
            ActivatableMap.put(name, way);
        } else
        if(token.indexOf("talker") == 0)
        {
            String waypoint[] = token.split("[{}]");
            String name = waypoint[0];
            name = name.replace("talker", "");
            name = name.trim();
            String coords[] = waypoint[1].split(",");
            ScriptTalker way = new ScriptTalker();
            TalkerMap.put(name, way);
        } else
        if(token.indexOf("language") == 0)
        {
            String waypoint[] = token.split("[{}]");
            String coords[] = ScriptParsingUtils.SplitExceptInbetween(waypoint[1], ",", "\"");
            String name = waypoint[0];
            name = name.replace("language", "");
            name = new String(name.trim());
            LanguageDefinition way = new LanguageDefinition();
            way.Load(name, coords);
            LanguageMap.put(name, way);
        } else
        if(token.indexOf("container ") == 0)
        {
            String waypoint[] = token.split("[{}]");
            String name = waypoint[0];
            name = name.replace("container", "");
            name = new String(name.trim());
            String coords[] = waypoint[1].split(",");
            ScriptContainer way = new ScriptContainer();
            ScriptContainerMap.put(name, way);
        } else
        if(token.indexOf("questcondition") == 0)
        {
            String waypoint[] = token.split("[{}]");
            String name = waypoint[0];
            name = name.replace("questcondition", "");
            name = name.trim();
            String coords[] = waypoint[1].split(",");
            QuestTaskCondition way = new QuestTaskCondition();
            ConditionMap.put(name, way);
        } else
        if(token.indexOf("recipe") == 0)
        {
            String waypoint[] = token.split("[{}]");
            String name = waypoint[0];
            name = name.replace("recipe", "");
            name = name.trim();
            String coords[] = waypoint[1].split(",");
            Recipe way = new Recipe();
            RecipeMap.put(name, way);
        } else
        if(token.indexOf("randomselector") == 0)
        {
            String waypoint[] = token.split("[{}]");
            String name = waypoint[0];
            name = name.replace("randomselector", "");
            name = name.trim();
            String coords[] = waypoint[1].split(",");
            RandomSelector way = new RandomSelector();
            RandomSelectorMap.put(name, way);
        } else
        if(token.indexOf("inventory") == 0)
        {
            String waypoint[] = token.split("[{}]");
            String name = waypoint[0];
            name = name.replace("inventory", "");
            name = name.trim();
            Inventory way = new Inventory();
            InventoryMap.put(name, way);
        } else
        if(token.indexOf("scriptflag") == 0)
        {
            String waypoint[] = token.split("[{}]");
            String name = waypoint[0];
            name = name.replace("scriptflag", "");
            name = name.trim();
            String coords[] = waypoint[1].split(",");
            ScriptFlag way = new ScriptFlag();
            FlagMap.put(name, way);
        } else
        if(token.indexOf("instancescript") == 0)
        {
            int firstOf = token.indexOf("{");
            String waypoint[] = new String[2];
            waypoint[0] = token.substring(0, firstOf).trim();
            waypoint[1] = token.substring(firstOf + 1);
            waypoint[1] = waypoint[1].substring(0, waypoint[1].length() - 1);
            String name = waypoint[0];
            name = name.replace("instancescript", "");
            name = name.trim();
            String lines[] = waypoint[1].split(";");
            Script way = new Script();
            way.Instancable = true;
            ScriptMap.put(name, way);
        } else
        if(token.indexOf("script") == 0)
        {
            int firstOf = token.indexOf("{");
            String waypoint[] = new String[2];
            waypoint[0] = token.substring(0, firstOf).trim();
            waypoint[1] = token.substring(firstOf + 1);
            waypoint[1] = waypoint[1].substring(0, waypoint[1].length() - 1);
            String name = waypoint[0];
            name = name.replace("script", "");
            name = name.trim();
            String lines[] = waypoint[1].split(";");
            Script way = new Script();
            ScriptMap.put(name, way);
        }
    }

    private void CreateFromToken(String token)
    {
        token = token.trim();
        if(token.indexOf("zone") == 0)
        {
            String waypoint[] = token.split("[{}]");
            String name = waypoint[0];
            name = name.replace("zone", "");
            name = name.trim();
            String coords[] = waypoint[1].split(",");
            Zone way = (Zone)ZoneMap.get(name);
            way.module = this;
            way.Load(name, coords);
        } else
        if(token.indexOf("waypoint") == 0)
        {
            String waypoint[] = token.split("[{}]");
            String name = waypoint[0];
            name = name.replace("waypoint", "");
            name = name.trim();
            String coords[] = waypoint[1].split(",");
            Waypoint way = (Waypoint)WaypointMap.get(name);
            way.module = this;
            way.Load(name, coords);
        } else
        if(token.indexOf("imports") == 0)
        {
            String waypoint[] = token.split("[{}]");
            String name = waypoint[0];
            name = name.replace("waypoint", "");
            name = name.trim();
            String coords[] = waypoint[1].split(",");
            for(int n = 0; n < coords.length; n++)
                if(coords[n].trim().length() > 0)
                    Imports.add(coords[n].trim());

        } else
        if(token.indexOf("validmaps") == 0)
        {
            String waypoint[] = token.split("[{}]");
            String name = waypoint[0];
            name = name.replace("validmaps", "");
            name = name.trim();
            String coords[] = waypoint[1].split(",");
            for(int n = 0; n < coords.length; n++)
                if(coords[n].trim().length() > 0)
                    ValidMaps.add(coords[n].trim());

        } else
        if(token.indexOf("cellexit") == 0)
        {
            String waypoint[] = token.split("[{}]");
            String name = waypoint[0];
            name = name.replace("validmaps", "");
            name = name.trim();
            String coords[] = waypoint[1].split(",");
            Exit exit = new Exit();
            exit.fromX1 = Integer.parseInt(coords[0].trim());
            exit.fromY1 = Integer.parseInt(coords[1].trim());
            exit.fromZ1 = Integer.parseInt(coords[2].trim());
            exit.fromX2 = Integer.parseInt(coords[3].trim());
            exit.fromY2 = Integer.parseInt(coords[4].trim());
            exit.fromZ2 = Integer.parseInt(coords[5].trim());
            exit.map = coords[6].trim();
            exit.toX1 = Integer.parseInt(coords[7].trim());
            exit.toY1 = Integer.parseInt(coords[8].trim());
            exit.toZ1 = Integer.parseInt(coords[9].trim());
            exit.toX2 = Integer.parseInt(coords[10].trim());
            exit.toY2 = Integer.parseInt(coords[11].trim());
            exit.toZ2 = Integer.parseInt(coords[12].trim());
            ExitPoints.add(exit);
        } else
        if(token.indexOf("room") == 0)
        {
            String waypoint[] = token.split("[{}]");
            String name = waypoint[0];
            name = name.replace("room", "");
            name = name.trim();
            String coords[] = waypoint[1].split(",");
            Room way = (Room)RoomMap.get(name);
            way.module = this;
            way.Load(name, coords);
        } else
        if(token.indexOf("character") == 0)
        {
            String waypoint[] = token.split("[{}]");
            String name = waypoint[0];
            name = name.replace("character", "");
            name = name.trim();
            String coords[] = waypoint[1].split(",");
            ScriptCharacter way = (ScriptCharacter)CharacterMap.get(name);
            way.module = this;
            way.Load(name, coords);
        } else
        if(token.indexOf("talker") == 0)
        {
            String waypoint[] = token.split("[{}]");
            String name = waypoint[0];
            name = name.replace("talker", "");
            name = name.trim();
            String coords[] = waypoint[1].split(",");
            ScriptTalker way = (ScriptTalker)TalkerMap.get(name);
            way.module = this;
            way.Load(name, coords);
        } else
        if(token.indexOf("activatable") == 0)
        {
            String waypoint[] = token.split("[{}]");
            String name = waypoint[0];
            name = name.replace("activatable", "");
            name = name.trim();
            String coords[] = waypoint[1].split(",");
            ScriptActivatable way = (ScriptActivatable)ActivatableMap.get(name);
            way.module = this;
            way.Load(name, coords);
        } else
        if(token.indexOf("container ") == 0)
        {
            String waypoint[] = token.split("[{}]");
            String name = waypoint[0];
            name = name.replace("container ", "");
            name = name.trim();
            String coords[] = waypoint[1].split(",");
            ScriptContainer way = (ScriptContainer)ScriptContainerMap.get(name);
            way.module = this;
            way.Load(name, coords);
        } else
        if(token.indexOf("questcondition") == 0)
        {
            String waypoint[] = token.split("[{}]");
            String name = waypoint[0];
            name = name.replace("questcondition", "");
            name = name.trim();
            String coords[] = waypoint[1].split(",");
            QuestTaskCondition way = (QuestTaskCondition)ConditionMap.get(name);
            way.module = this;
            way.Load(name, coords);
        } else
        if(token.indexOf("door") == 0)
        {
            String waypoint[] = token.split("[{}]");
            String name = waypoint[0];
            name = name.replace("door", "");
            name = name.trim();
            String coords[] = waypoint[1].split(",");
            Door way = (Door)DoorMap.get(name);
            way.module = this;
            way.Load(name, coords);
        } else
        if(token.indexOf("item") == 0)
        {
            String waypoint[] = token.split("[{}]");
            String name = waypoint[0];
            name = name.replace("item", "");
            name = name.trim();
            String coords[] = waypoint[1].split(",");
            Item way = (Item)ItemMap.get(name);
            way.module = this;
            Exception exception;
            try
            {
                way.Load(name, coords);
            }
            catch(Exception ex)
            {
                exception = ex;
            }
        } else
        if(token.indexOf("randomselector") == 0)
        {
            String waypoint[] = token.split("[{}]");
            String name = waypoint[0];
            name = name.replace("randomselector", "");
            name = name.trim();
            String coords[] = waypoint[1].split(",");
            RandomSelector way = (RandomSelector)RandomSelectorMap.get(name);
            way.module = this;
            way.Load(name, coords);
        } else
        if(token.indexOf("trigger") == 0)
        {
            String waypoint[] = token.split("[{}]");
            String name = waypoint[0];
            name = name.replace("trigger", "");
            name = name.trim();
            String coords[] = ScriptParsingUtils.SplitExceptInbetween(waypoint[1], ",", "(", ")");
            Trigger way = new Trigger();
            name = name.toLowerCase();
            way.module = this;
            way.Load(name, coords);
            if(ScriptManager.instance.TriggerMap.containsKey(name))
            {
                ((Stack)ScriptManager.instance.TriggerMap.get(name)).add(way);
            } else
            {
                ScriptManager.instance.TriggerMap.put(name, new Stack());
                ((Stack)ScriptManager.instance.TriggerMap.get(name)).add(way);
                ScriptManager.instance.CustomTriggerLastRan.put(way.name, Integer.valueOf(0));
            }
        } else
        if(token.indexOf("customtrigger") == 0)
        {
            String waypoint[] = token.split("[{}]");
            String name = waypoint[0];
            name = name.replace("customtrigger", "");
            name = name.trim();
            String coords[] = ScriptParsingUtils.SplitExceptInbetween(waypoint[1], ",", "(", ")");
            Trigger way = new Trigger();
            name = name.toLowerCase();
            way.module = this;
            way.Load(name, coords);
            way.Locked = true;
            if(ScriptManager.instance.CustomTriggerMap.containsKey(name))
            {
                ((Stack)ScriptManager.instance.CustomTriggerMap.get(name)).add(way);
            } else
            {
                ScriptManager.instance.CustomTriggerMap.put(name, new Stack());
                ((Stack)ScriptManager.instance.CustomTriggerMap.get(name)).add(way);
                ScriptManager.instance.CustomTriggerLastRan.put(way.name, Integer.valueOf(0));
            }
        } else
        if(token.indexOf("inventory") == 0)
        {
            String waypoint[] = token.split("[{}]");
            String name = waypoint[0];
            name = name.replace("inventory", "");
            name = name.trim();
            String coords[] = null;
            if(waypoint.length > 1)
                coords = waypoint[1].split(",");
            else
                coords = new String[1];
            Inventory way = (Inventory)InventoryMap.get(name);
            way.module = this;
            way.Load(name, coords);
        } else
        if(token.indexOf("scriptflag") == 0)
        {
            String waypoint[] = token.split("[{}]");
            String name = waypoint[0];
            name = name.replace("scriptflag", "");
            name = name.trim();
            String coords[] = waypoint[1].split(",");
            ScriptFlag way = (ScriptFlag)FlagMap.get(name);
            way.module = this;
            way.Load(name, coords);
        } else
        if(token.indexOf("script") == 0)
        {
            int firstOf = token.indexOf("{");
            String waypoint[] = new String[2];
            waypoint[0] = token.substring(0, firstOf).trim();
            waypoint[1] = token.substring(firstOf + 1);
            waypoint[1] = waypoint[1].substring(0, waypoint[1].length() - 1);
            String name = waypoint[0];
            name = name.replace("script", "");
            name = name.trim();
            Script way = (Script)ScriptMap.get(name);
            way.module = this;
            try
            {
                way.DoScriptParsing(name, waypoint[1]);
            }
            catch(Exception ex)
            {
                way = way;
            }
        } else
        if(token.indexOf("instancescript") == 0)
        {
            int firstOf = token.indexOf("{");
            String waypoint[] = new String[2];
            waypoint[0] = token.substring(0, firstOf).trim();
            waypoint[1] = token.substring(firstOf + 1);
            waypoint[1] = waypoint[1].substring(0, waypoint[1].length() - 1);
            String name = waypoint[0];
            name = name.replace("instancescript", "");
            name = name.trim();
            Script way = (Script)ScriptMap.get(name);
            way.module = this;
            try
            {
                way.DoScriptParsing(name, waypoint[1]);
            }
            catch(Exception ex)
            {
                way = way;
            }
        } else
        if(token.indexOf("recipe") == 0)
        {
            String waypoint[] = token.split("[{}]");
            String name = waypoint[0];
            name = name.replace("recipe", "");
            name = name.trim();
            String coords[] = waypoint[1].split(",");
            Recipe way = (Recipe)RecipeMap.get(name);
            way.module = this;
            way.Load(name, coords);
        } else
        if(token.indexOf("containeritemdistribution") == 0)
        {
            int firstOf = token.indexOf("{");
            String waypoint[] = new String[2];
            waypoint[0] = token.substring(0, firstOf).trim();
            waypoint[1] = token.substring(firstOf + 1);
            waypoint[1] = waypoint[1].substring(0, waypoint[1].length() - 1);
            String name = waypoint[0];
            name = name.replace("containeritemdistribution", "");
            name = name.trim();
            String coords[] = waypoint[1].split(",");
            ContainerDistribution way = new ContainerDistribution();
            way.module = this;
            way.Load(name, coords);
            ContainerDistributions.add(way);
        } else
        if(token.indexOf("flooritemdistribution") == 0)
        {
            int firstOf = token.indexOf("{");
            String waypoint[] = new String[2];
            waypoint[0] = token.substring(0, firstOf).trim();
            waypoint[1] = token.substring(firstOf + 1);
            waypoint[1] = waypoint[1].substring(0, waypoint[1].length() - 1);
            String name = waypoint[0];
            name = name.replace("flooritemdistribution", "");
            name = name.trim();
            String coords[] = waypoint[1].split(",");
            FloorDistribution way = new FloorDistribution();
            way.module = this;
            way.Load(name, coords);
            FloorDistributions.add(way);
        } else
        if(token.indexOf("shelfitemdistribution") == 0)
        {
            int firstOf = token.indexOf("{");
            String waypoint[] = new String[2];
            waypoint[0] = token.substring(0, firstOf).trim();
            waypoint[1] = token.substring(firstOf + 1);
            waypoint[1] = waypoint[1].substring(0, waypoint[1].length() - 1);
            String name = waypoint[0];
            name = name.replace("shelfitemdistribution", "");
            name = name.trim();
            String coords[] = waypoint[1].split(",");
            ShelfDistribution way = new ShelfDistribution();
            way.module = this;
            way.Load(name, coords);
            ShelfDistributions.add(way);
        }
    }

    public void ParseScript(String totalFile)
    {
        boolean done = false;
        Stack Tokens = new Stack();
        int depth = 0;
        int nextindexOfOpen = 0;
        int nextindexOfClosed = 0;
        do
        {
            if(done)
                break;
            depth = 0;
            nextindexOfOpen = 0;
            nextindexOfClosed = 0;
            if(totalFile.indexOf("}", nextindexOfOpen + 1) == -1)
            {
                done = true;
                break;
            }
            do
            {
                nextindexOfOpen = totalFile.indexOf("{", nextindexOfOpen + 1);
                nextindexOfClosed = totalFile.indexOf("}", nextindexOfClosed + 1);
                if(nextindexOfClosed < nextindexOfOpen && nextindexOfClosed != -1 || nextindexOfOpen == -1)
                {
                    nextindexOfOpen = nextindexOfClosed;
                    depth--;
                } else
                if(nextindexOfOpen != -1)
                {
                    nextindexOfClosed = nextindexOfOpen;
                    depth++;
                }
            } while(depth > 0);
            Tokens.add(totalFile.substring(0, nextindexOfOpen + 1));
            totalFile = totalFile.substring(nextindexOfOpen + 1);
        } while(true);
        if(totalFile.trim().length() > 0)
            Tokens.add(totalFile.trim());
        for(int n = 0; n < Tokens.size(); n++)
        {
            String token = (String)Tokens.get(n);
            CreateFromToken(token);
        }

    }

    public void ParseScriptPP(String totalFile)
    {
        boolean done = false;
        Stack Tokens = new Stack();
        int depth = 0;
        int nextindexOfOpen = 0;
        int nextindexOfClosed = 0;
        do
        {
            if(done)
                break;
            depth = 0;
            nextindexOfOpen = 0;
            nextindexOfClosed = 0;
            if(totalFile.indexOf("}", nextindexOfOpen + 1) == -1)
            {
                done = true;
                break;
            }
            do
            {
                nextindexOfOpen = totalFile.indexOf("{", nextindexOfOpen + 1);
                nextindexOfClosed = totalFile.indexOf("}", nextindexOfClosed + 1);
                if(nextindexOfClosed < nextindexOfOpen && nextindexOfClosed != -1 || nextindexOfOpen == -1)
                {
                    nextindexOfOpen = nextindexOfClosed;
                    depth--;
                } else
                if(nextindexOfOpen != -1)
                {
                    nextindexOfClosed = nextindexOfOpen;
                    depth++;
                }
            } while(depth > 0);
            Tokens.add(totalFile.substring(0, nextindexOfOpen + 1));
            totalFile = totalFile.substring(nextindexOfOpen + 1);
        } while(true);
        if(totalFile.trim().length() > 0)
            Tokens.add(totalFile.trim());
        for(int n = 0; n < Tokens.size(); n++)
        {
            String token = (String)Tokens.get(n);
            CreateFromTokenPP(token);
        }

    }

    public void PlayScript(String script)
    {
        if(script.contains("."))
        {
            ScriptManager.instance.PlayScript(script);
            return;
        }
        if(ScriptMap.containsKey(script))
        {
            Script tscript = (Script)ScriptMap.get(script);
            Script.ScriptInstance inst = new Script.ScriptInstance();
            inst.theScript = tscript;
            inst.ID = name;
            ScriptManager.instance.PlayingScripts.add(inst);
            inst.begin();
        } else
        if(RandomSelectorMap.containsKey(script))
        {
            ((RandomSelector)RandomSelectorMap.get(script)).Process();
        } else
        {
            for(int n = 0; n < Imports.size(); n++)
            {
                Script inv = ScriptManager.instance.getModule((String)Imports.get(n)).getScript(name);
                if(inv != null)
                {
                    ScriptManager.instance.getModule((String)Imports.get(n)).PlayScript(inv.name);
                    return;
                }
            }

            JOptionPane.showMessageDialog(null, (new StringBuilder()).append("Module: ").append(name).append(" cannot find script: ").append(script).toString(), "Error", 0);
        }
    }

    public Script.ScriptInstance PlayScript(String script, Script.ScriptInstance aliases)
    {
        if(script.contains("."))
            return ScriptManager.instance.PlayScript(script, aliases);
        if(ScriptMap.containsKey(script))
        {
            Script tscript = (Script)ScriptMap.get(script);
            Script.ScriptInstance inst = new Script.ScriptInstance();
            inst.theScript = tscript;
            inst.ID = name;
            inst.CopyAliases(aliases);
            ScriptManager.instance.PlayingScripts.add(inst);
            inst.begin();
            return inst;
        }
        if(RandomSelectorMap.containsKey(script))
        {
            ((RandomSelector)RandomSelectorMap.get(script)).Process(aliases);
        } else
        {
            for(int n = 0; n < Imports.size(); n++)
            {
                Script inv = ScriptManager.instance.getModule((String)Imports.get(n)).getScript(name);
                if(inv != null)
                    return ScriptManager.instance.getModule((String)Imports.get(n)).PlayScript(inv.name, aliases);
            }

            JOptionPane.showMessageDialog(null, (new StringBuilder()).append("Module: ").append(name).append(" cannot find script: ").append(script).toString(), "Error", 0);
        }
        return null;
    }

    public Script.ScriptInstance PlayScript(Script.ScriptInstance inst)
    {
        ScriptManager.instance.PlayingScripts.add(inst);
        inst.begin();
        return inst;
    }

    public Inventory getInventory(String name)
    {
        if(name.contains("."))
            return ScriptManager.instance.getInventory(name);
        if(!InventoryMap.containsKey(name))
        {
            for(int n = 0; n < Imports.size(); n++)
            {
                Inventory inv = ScriptManager.instance.getModule((String)Imports.get(n)).getInventory(name);
                if(inv != null)
                    return inv;
            }

            return null;
        } else
        {
            return (Inventory)InventoryMap.get(name);
        }
    }

    public ScriptCharacter getCharacter(String name)
    {
        if(name.contains("."))
            return ScriptManager.instance.getCharacter(name);
        if(!CharacterMap.containsKey(name))
        {
            for(int n = 0; n < Imports.size(); n++)
            {
                ScriptCharacter inv = ScriptManager.instance.getModule((String)Imports.get(n)).getCharacter(name);
                if(inv != null)
                    return inv;
            }

            return null;
        } else
        {
            return (ScriptCharacter)CharacterMap.get(name);
        }
    }

    public IsoGameCharacter getCharacterActual(String name)
    {
        if(name.contains("."))
            return ScriptManager.instance.getCharacterActual(name);
        if(!CharacterMap.containsKey(name))
        {
            for(int n = 0; n < Imports.size(); n++)
            {
                ScriptCharacter inv = ScriptManager.instance.getModule((String)Imports.get(n)).getCharacter(name);
                if(inv != null)
                    return inv.Actual;
            }

            return null;
        } else
        {
            return ((ScriptCharacter)CharacterMap.get(name)).Actual;
        }
    }

    public int getFlagIntValue(String name)
    {
        if(name.contains("."))
            return ScriptManager.instance.getFlagIntValue(name);
        if(!FlagMap.containsKey(name))
        {
            for(int n = 0; n < Imports.size(); n++)
            {
                ScriptFlag inv = ScriptManager.instance.getModule((String)Imports.get(n)).getFlag(name);
                if(inv != null)
                    return Integer.parseInt(inv.value);
            }

            return 0;
        } else
        {
            return Integer.parseInt(((ScriptFlag)FlagMap.get(name)).value);
        }
    }

    public String getFlagValue(String name)
    {
        if(name.contains("."))
            return ScriptManager.instance.getFlagValue(name);
        if(!FlagMap.containsKey(name))
        {
            for(int n = 0; n < Imports.size(); n++)
            {
                ScriptFlag inv = ScriptManager.instance.getModule((String)Imports.get(n)).getFlag(name);
                if(inv != null)
                    return inv.value;
            }

            return null;
        } else
        {
            return ((ScriptFlag)FlagMap.get(name)).value;
        }
    }

    public Waypoint getWaypoint(String name)
    {
        if(name.contains("."))
            return ScriptManager.instance.getWaypoint(name);
        if(!WaypointMap.containsKey(name))
        {
            for(int n = 0; n < Imports.size(); n++)
            {
                Waypoint inv = ScriptManager.instance.getModule((String)Imports.get(n)).getWaypoint(name);
                if(inv != null)
                    return inv;
            }

            return null;
        } else
        {
            return (Waypoint)WaypointMap.get(name);
        }
    }

    public ScriptContainer getScriptContainer(String name)
    {
        if(name.contains("."))
            return ScriptManager.instance.getScriptContainer(name);
        if(!ScriptContainerMap.containsKey(name))
        {
            for(int n = 0; n < Imports.size(); n++)
            {
                ScriptContainer inv = ScriptManager.instance.getModule((String)Imports.get(n)).getScriptContainer(name);
                if(inv != null)
                    return inv;
            }

            return null;
        } else
        {
            return (ScriptContainer)ScriptContainerMap.get(name);
        }
    }

    public Room getRoom(String name)
    {
        if(name.contains("."))
            return ScriptManager.instance.getRoom(name);
        if(!RoomMap.containsKey(name))
        {
            for(int n = 0; n < Imports.size(); n++)
            {
                Room inv = ScriptManager.instance.getModule((String)Imports.get(n)).getRoom(name);
                if(inv != null)
                    return inv;
            }

            return null;
        } else
        {
            return (Room)RoomMap.get(name);
        }
    }

    public ScriptActivatable getActivatable(String name)
    {
        if(name.contains("."))
            return ScriptManager.instance.getActivatable(name);
        if(!ActivatableMap.containsKey(name))
        {
            for(int n = 0; n < Imports.size(); n++)
            {
                ScriptActivatable inv = ScriptManager.instance.getModule((String)Imports.get(n)).getActivatable(name);
                if(inv != null)
                    return inv;
            }

            return null;
        } else
        {
            return (ScriptActivatable)ActivatableMap.get(name);
        }
    }

    public ScriptTalker getTalker(String name)
    {
        if(name.contains("."))
            return ScriptManager.instance.getTalker(name);
        if(!TalkerMap.containsKey(name))
        {
            for(int n = 0; n < Imports.size(); n++)
            {
                ScriptTalker inv = ScriptManager.instance.getModule((String)Imports.get(n)).getTalker(name);
                if(inv != null)
                    return inv;
            }

            return null;
        } else
        {
            return (ScriptTalker)TalkerMap.get(name);
        }
    }

    public LanguageDefinition getLanguageDef(String name)
    {
        if(name.contains("."))
            return ScriptManager.instance.getLanguageDef(name);
        if(!LanguageMap.containsKey(name))
        {
            for(int n = 0; n < Imports.size(); n++)
            {
                LanguageDefinition inv = ScriptManager.instance.getModule((String)Imports.get(n)).getLanguageDef(name);
                if(inv != null)
                    return inv;
            }

            return null;
        } else
        {
            return (LanguageDefinition)LanguageMap.get(name);
        }
    }

    public String getLanguage(String id)
    {
        if(!id.contains("@"))
            return id;
        id = id.substring(1);
        if(id.contains("."))
        {
            return ScriptManager.instance.getLanguage(id);
        } else
        {
            String split[] = id.split("-");
            LanguageDefinition def = getLanguageDef(split[0]);
            String s = def.get(Integer.parseInt(split[1]));
            s = s.substring(1);
            s = s.substring(0, s.length() - 1);
            return s;
        }
    }

    public ScriptFlag getFlag(String name)
    {
        if(name.contains("."))
            return ScriptManager.instance.getFlag(name);
        if(!FlagMap.containsKey(name))
        {
            for(int n = 0; n < Imports.size(); n++)
            {
                ScriptFlag inv = ScriptManager.instance.getModule((String)Imports.get(n)).getFlag(name);
                if(inv != null)
                    return inv;
            }

            return null;
        } else
        {
            return (ScriptFlag)FlagMap.get(name);
        }
    }

    public Zone getZone(String name)
    {
        if(name.contains("."))
            return ScriptManager.instance.getZone(name);
        if(!ZoneMap.containsKey(name))
        {
            for(int n = 0; n < Imports.size(); n++)
            {
                Zone inv = ScriptManager.instance.getModule((String)Imports.get(n)).getZone(name);
                if(inv != null)
                    return inv;
            }

            return null;
        } else
        {
            return (Zone)ZoneMap.get(name);
        }
    }

    public QuestTaskCondition getQuestCondition(String name)
    {
        if(name.contains("."))
            return ScriptManager.instance.getQuestCondition(name);
        if(!ConditionMap.containsKey(name))
        {
            for(int n = 0; n < Imports.size(); n++)
            {
                QuestTaskCondition inv = ScriptManager.instance.getModule((String)Imports.get(n)).getQuestCondition(name);
                if(inv != null)
                    return inv;
            }

            return null;
        } else
        {
            return (QuestTaskCondition)ConditionMap.get(name);
        }
    }

    public Item getItem(String name)
    {
        if(name.contains("."))
            return ScriptManager.instance.getItem(name);
        if(!ItemMap.containsKey(name))
        {
            for(int n = 0; n < Imports.size(); n++)
            {
                Item inv = ScriptManager.instance.getModule((String)Imports.get(n)).getItem(name);
                if(inv != null)
                    return inv;
            }

            return null;
        } else
        {
            return (Item)ItemMap.get(name);
        }
    }

    public Recipe getRecipe(String name)
    {
        if(name.contains("."))
            return ScriptManager.instance.getRecipe(name);
        if(!RecipeMap.containsKey(name))
        {
            for(int n = 0; n < Imports.size(); n++)
            {
                Recipe inv = ScriptManager.instance.getModule((String)Imports.get(n)).getRecipe(name);
                if(inv != null)
                    return inv;
            }

            return null;
        } else
        {
            return (Recipe)RecipeMap.get(name);
        }
    }

    public boolean CheckExitPoints()
    {
        for(int n = 0; n < ExitPoints.size(); n++)
        {
            Exit ex = (Exit)ExitPoints.get(n);
            if(IsoPlayer.getInstance().getX() >= (float)ex.fromX1 && (int)IsoPlayer.getInstance().getX() <= ex.fromX2 && (int)IsoPlayer.getInstance().getY() >= ex.fromY1 && (int)IsoPlayer.getInstance().getY() <= ex.fromY2 && (int)IsoPlayer.getInstance().getZ() >= ex.fromZ1 && (int)IsoPlayer.getInstance().getZ() <= ex.fromZ2)
            {
                IsoPlayer.getInstance().TravelTo(ex.map, (float)ex.toX1 + (IsoPlayer.getInstance().getX() - (float)ex.fromX1), (float)ex.toY1 + (IsoPlayer.getInstance().getY() - (float)ex.fromY1), (float)ex.toZ1 + (IsoPlayer.getInstance().getZ() - (float)ex.fromZ1));
                return true;
            }
        }

        return false;
    }

    public Script getScript(String name)
    {
        if(name.contains("."))
            return ScriptManager.instance.getScript(name);
        if(RandomSelectorMap.containsKey(name))
            name = (String)((RandomSelector)RandomSelectorMap.get(name)).scriptsToCall.get(Rand.Next(((RandomSelector)RandomSelectorMap.get(name)).scriptsToCall.size()));
        if(!ScriptMap.containsKey(name))
        {
            for(int n = 0; n < Imports.size(); n++)
            {
                Script inv = ScriptManager.instance.getModule((String)Imports.get(n)).getScript(name);
                if(inv != null)
                    return inv;
            }

            return null;
        } else
        {
            return (Script)ScriptMap.get(name);
        }
    }

    public Stack ValidMaps;
    public Stack ExitPoints;
    public String name;
    public String value;
    public THashMap WaypointMap;
    public THashMap RoomMap;
    public Stack RoomList;
    public THashMap DoorMap;
    public THashMap ItemMap;
    public THashMap ScriptMap;
    public THashMap CharacterMap;
    public THashMap RecipeMap;
    public THashMap InventoryMap;
    public THashMap ActivatableMap;
    public THashMap TalkerMap;
    public THashMap ScriptContainerMap;
    public THashMap ConditionMap;
    public THashMap FlagMap;
    public THashMap ZoneMap;
    public Stack ZoneList;
    public THashMap RandomSelectorMap;
    public Stack ContainerDistributions;
    public Stack FloorDistributions;
    public Stack ShelfDistributions;
    public Stack Imports;
    public boolean disabled;
    public THashMap LanguageMap;
}
