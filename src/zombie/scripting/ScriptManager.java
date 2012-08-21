// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ScriptManager.java

package zombie.scripting;

import gnu.trove.map.hash.THashMap;
import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.input.Keyboard;
import zombie.GameWindow;
import zombie.characters.IsoGameCharacter;
import zombie.core.Collections.NulledArrayList;
import zombie.core.IndieFileLoader;
import zombie.core.Rand;
import zombie.inventory.*;
import zombie.iso.IsoCell;
import zombie.iso.IsoWorld;
import zombie.scripting.objects.ContainerDistribution;
import zombie.scripting.objects.FloorDistribution;
import zombie.scripting.objects.Inventory;
import zombie.scripting.objects.Item;
import zombie.scripting.objects.LanguageDefinition;
import zombie.scripting.objects.QuestTaskCondition;
import zombie.scripting.objects.Recipe;
import zombie.scripting.objects.Room;
import zombie.scripting.objects.Script;
import zombie.scripting.objects.ScriptActivatable;
import zombie.scripting.objects.ScriptCharacter;
import zombie.scripting.objects.ScriptContainer;
import zombie.scripting.objects.ScriptFlag;
import zombie.scripting.objects.ScriptModule;
import zombie.scripting.objects.ScriptTalker;
import zombie.scripting.objects.ShelfDistribution;
import zombie.scripting.objects.Trigger;
import zombie.scripting.objects.Waypoint;
import zombie.scripting.objects.Zone;
import zombie.ui.PZConsole;
import zombie.ui.TutorialManager;

// Referenced classes of package zombie.scripting:
//            IScriptObjectStore

public class ScriptManager
    implements IScriptObjectStore
{

    public ScriptManager()
    {
        TriggerMap = new THashMap();
        CustomTriggerMap = new THashMap();
        CustomTriggerLastRan = new THashMap();
        HookMap = new THashMap();
        ModuleMap = new THashMap();
        PlayingScripts = new Stack();
        CurrentLoadingModule = null;
        ModuleAliases = new THashMap();
        skipping = false;
        MapMap = new THashMap();
        toStop = new ArrayList();
        toStopInstance = new ArrayList();
        CachedModules = new HashMap();
        recipesTempList = new Stack();
        zoneTempList = new Stack();
        conTempList = new Stack();
        floorTempList = new Stack();
        shelfTempList = new Stack();
    }

    public void AddOneTime(String event, String script)
    {
        event = event.toLowerCase();
        Stack stack = null;
        if(HookMap.containsKey(event))
        {
            stack = (Stack)HookMap.get(event);
        } else
        {
            stack = new Stack();
            HookMap.put(event, stack);
        }
        stack.add(script);
    }

    public void FireHook(String event)
    {
        if(HookMap.containsKey(event))
        {
            String hook;
            for(Iterator i$ = ((Stack)HookMap.get(event)).iterator(); i$.hasNext(); PlayScript(hook))
                hook = (String)i$.next();

            ((Stack)HookMap.get(event)).clear();
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

    public void StopScript(String stop)
    {
        toStop.add(stop);
    }

    public void PlayInstanceScript(String idname, String script, String a, IsoGameCharacter A)
    {
        for(int n = 0; n < PlayingScripts.size(); n++)
            if(idname != null && idname.equals(((zombie.scripting.objects.Script.ScriptInstance)PlayingScripts.get(n)).ID))
                return;

        zombie.scripting.objects.Script.ScriptInstance inst = new zombie.scripting.objects.Script.ScriptInstance();
        inst.ID = idname;
        Script scr = getScript(script);
        inst.theScript = scr;
        inst.CharacterAliases.put(a, A);
        inst.CharacterAliasesR.put(A, a);
        A.getActiveInInstances().add(inst);
        instance.PlayingScripts.add(inst);
        inst.begin();
    }

    public zombie.scripting.objects.Script.ScriptInstance PlayInstanceScript(String idname, String script, THashMap Aliases)
    {
        for(int n = 0; n < PlayingScripts.size(); n++)
            if(idname != null && idname.equals(((zombie.scripting.objects.Script.ScriptInstance)PlayingScripts.get(n)).ID))
                return null;

        zombie.scripting.objects.Script.ScriptInstance inst = new zombie.scripting.objects.Script.ScriptInstance();
        inst.ID = idname;
        Script scr = getScript(script);
        inst.theScript = scr;
        IsoGameCharacter A;
        for(Iterator it = Aliases.entrySet().iterator(); it != null && it.hasNext(); A.getActiveInInstances().add(inst))
        {
            java.util.Map.Entry entry = (java.util.Map.Entry)it.next();
            String a = (String)entry.getKey();
            A = (IsoGameCharacter)entry.getValue();
            inst.CharacterAliases.put(a, A);
            inst.CharacterAliasesR.put(A, a);
        }

        instance.PlayingScripts.add(inst);
        inst.begin();
        return inst;
    }

    public void PlayInstanceScript(String idname, String script, String a, IsoGameCharacter A, String b, IsoGameCharacter B)
    {
        zombie.scripting.objects.Script.ScriptInstance inst = new zombie.scripting.objects.Script.ScriptInstance();
        inst.ID = idname;
        Script scr = getScript(script);
        int n;
        if(scr == null)
            n = 0;
        inst.theScript = scr;
        inst.CharacterAliases.put(a, A);
        inst.CharacterAliasesR.put(A, a);
        inst.CharacterAliases.put(b, B);
        inst.CharacterAliasesR.put(B, b);
        for(n = 0; n < PlayingScripts.size(); n++)
            if(idname != null && idname.equals(((zombie.scripting.objects.Script.ScriptInstance)PlayingScripts.get(n)).ID))
                inst.CharactersAlreadyInScript = true;

        A.getActiveInInstances().add(inst);
        B.getActiveInInstances().add(inst);
        instance.PlayingScripts.add(inst);
        inst.begin();
    }

    public void PlayInstanceScript(String idname, String script, String a, IsoGameCharacter A, String b, IsoGameCharacter B, String c, 
            IsoGameCharacter C)
    {
        for(int n = 0; n < PlayingScripts.size(); n++)
            if(idname != null && idname.equals(((zombie.scripting.objects.Script.ScriptInstance)PlayingScripts.get(n)).ID))
                return;

        zombie.scripting.objects.Script.ScriptInstance inst = new zombie.scripting.objects.Script.ScriptInstance();
        inst.ID = idname;
        Script scr = getScript(script);
        inst.theScript = scr;
        inst.CharacterAliases.put(a, A);
        inst.CharacterAliasesR.put(A, a);
        inst.CharacterAliases.put(b, B);
        inst.CharacterAliasesR.put(B, b);
        inst.CharacterAliases.put(c, C);
        inst.CharacterAliasesR.put(C, c);
        A.getActiveInInstances().add(inst);
        B.getActiveInInstances().add(inst);
        C.getActiveInInstances().add(inst);
        instance.PlayingScripts.add(inst);
        inst.begin();
    }

    public void PlayScript(String script)
    {
        Script scr = getScript(script);
        if(scr != null)
        {
            for(int n = 0; n < PlayingScripts.size(); n++)
                if(((zombie.scripting.objects.Script.ScriptInstance)PlayingScripts.get(n)).theScript == scr && !((zombie.scripting.objects.Script.ScriptInstance)PlayingScripts.get(n)).theScript.Instancable)
                {
                    PlayingScripts.remove(n);
                    n--;
                }

            scr.module.PlayScript(scr.name);
        }
    }

    public zombie.scripting.objects.Script.ScriptInstance PlayScript(String script, zombie.scripting.objects.Script.ScriptInstance aliases)
    {
        Script scr = getScript(script);
        if(scr != null)
        {
            for(int n = 0; n < PlayingScripts.size(); n++)
                if(((zombie.scripting.objects.Script.ScriptInstance)PlayingScripts.get(n)).theScript == scr && !((zombie.scripting.objects.Script.ScriptInstance)PlayingScripts.get(n)).theScript.Instancable)
                {
                    PlayingScripts.remove(n);
                    n--;
                }

            return scr.module.PlayScript(scr.name, aliases);
        } else
        {
            return null;
        }
    }

    public void update()
    {
        if(!PZConsole.instance.isVisible() && Keyboard.isKeyDown(1) && TutorialManager.instance.StealControl)
            skipping = true;
        for(int n = 0; n < toStopInstance.size(); n++)
        {
            for(int m = 0; m < PlayingScripts.size(); m++)
            {
                if(PlayingScripts.get(m) != toStopInstance.get(n))
                    continue;
                zombie.scripting.objects.Script.ScriptInstance s = (zombie.scripting.objects.Script.ScriptInstance)PlayingScripts.get(m);
                IsoGameCharacter c;
                for(Iterator i = s.CharacterAliases.values().iterator(); i != null && i.hasNext(); c.getActiveInInstances().remove(s))
                    c = (IsoGameCharacter)i.next();

                PlayingScripts.remove(m);
                m--;
            }

        }

        toStopInstance.clear();
        for(int n = 0; n < toStop.size(); n++)
        {
            for(int m = 0; m < PlayingScripts.size(); m++)
            {
                if(((zombie.scripting.objects.Script.ScriptInstance)PlayingScripts.get(m)).theScript.name.equals(toStop.get(n)))
                {
                    zombie.scripting.objects.Script.ScriptInstance s = (zombie.scripting.objects.Script.ScriptInstance)PlayingScripts.get(m);
                    IsoGameCharacter c;
                    for(Iterator i = s.CharacterAliases.values().iterator(); i != null && i.hasNext(); c.getActiveInInstances().remove(s))
                        c = (IsoGameCharacter)i.next();

                    m--;
                }
                PlayingScripts.remove(m);
            }

        }

        toStop.clear();
        for(Iterator it = CustomTriggerMap.values().iterator(); it != null && it.hasNext();)
        {
            Stack next = (Stack)it.next();
            int n = 0;
            while(n < next.size()) 
            {
                if(!((Trigger)next.get(n)).Locked && ((Trigger)next.get(n)).module.ValidMapCheck(IsoWorld.instance.CurrentCell.getFilename()))
                    ((Trigger)next.get(n)).Process();
                n++;
            }
        }

        Set keys = CustomTriggerLastRan.keySet();
        String key;
        Integer next;
        for(Iterator i$ = keys.iterator(); i$.hasNext(); CustomTriggerLastRan.put(key, next))
        {
            key = (String)i$.next();
            next = (Integer)CustomTriggerLastRan.get(key);
            Integer integer = next;
            Integer integer1 = next = Integer.valueOf(next.intValue() + 1);
            Integer _tmp = integer;
        }

        for(int n = 0; n < PlayingScripts.size(); n++)
        {
            if(!((zombie.scripting.objects.Script.ScriptInstance)PlayingScripts.get(n)).theScript.module.ValidMapCheck(IsoWorld.instance.CurrentCell.getFilename()))
                continue;
            ((zombie.scripting.objects.Script.ScriptInstance)PlayingScripts.get(n)).update();
            if(!((zombie.scripting.objects.Script.ScriptInstance)PlayingScripts.get(n)).finished())
                continue;
            zombie.scripting.objects.Script.ScriptInstance inst = (zombie.scripting.objects.Script.ScriptInstance)PlayingScripts.get(n);
            PlayingScripts.remove(n);
            IsoGameCharacter chr;
            for(Iterator it2 = inst.CharacterAliases.values().iterator(); it2 != null && it2.hasNext(); chr.getActiveInInstances().remove(inst))
                chr = (IsoGameCharacter)it2.next();

            n--;
        }

    }

    public void LoadFile(String filename, boolean bLoadJar)
        throws FileNotFoundException
    {
        if(filename.contains(".tmx"))
        {
            IsoWorld.mapPath = filename.substring(0, filename.lastIndexOf("/"));
            IsoWorld.mapUseJar = bLoadJar;
            return;
        }
        if(!filename.contains(".txt"))
            return;
        InputStreamReader isr = IndieFileLoader.getStreamReader(filename, !bLoadJar);
        BufferedReader br = new BufferedReader(isr);
        String inputLine = null;
        String totalFile = "";
        int depth = 0;
        do
            try
            {
                if((inputLine = br.readLine()) == null)
                    break;
                if(inputLine != null)
                    totalFile = (new StringBuilder()).append(totalFile).append(inputLine).toString();
            }
            catch(Exception ex)
            {
                return;
            }
        while(true);
        int a;
        int b;
        for(; totalFile.contains("*/"); totalFile = (new StringBuilder()).append(totalFile.substring(0, a)).append("\n").append(totalFile.substring(b + 2)).toString())
        {
            a = totalFile.indexOf("/*");
            b = totalFile.indexOf("*/");
        }

        ParseScript(totalFile);
    }

    public void LoadFilePP(String filename, boolean bLoadJar)
        throws FileNotFoundException, UnsupportedEncodingException
    {
        if(filename.contains(".tmx"))
        {
            IsoWorld.mapPath = filename.substring(0, filename.lastIndexOf("/"));
            IsoWorld.mapUseJar = bLoadJar;
            return;
        }
        if(!filename.contains(".txt"))
            return;
        InputStreamReader isr = IndieFileLoader.getStreamReader(filename, !bLoadJar);
        BufferedReader br = new BufferedReader(isr);
        String inputLine = null;
        String totalFile = "";
        int lastdepth = 0;
        do
            try
            {
                if((inputLine = br.readLine()) == null)
                    break;
                if(inputLine != null)
                    totalFile = (new StringBuilder()).append(totalFile).append(inputLine).toString();
            }
            catch(Exception ex)
            {
                return;
            }
        while(true);
        try
        {
            int a;
            int b;
            for(; totalFile.contains("*/"); totalFile = (new StringBuilder()).append(totalFile.substring(0, a)).append("\n").append(totalFile.substring(b + 2)).toString())
            {
                a = totalFile.indexOf("/*");
                b = totalFile.indexOf("*/");
            }

        }
        catch(Exception ex)
        {
            Logger.getLogger(IsoWorld.class.getName()).log(Level.SEVERE, null, ex);
        }
        ParseScriptPP(totalFile);
    }

    private void CreateFromToken(String token)
    {
        token = token.trim();
        if(token.indexOf("module") == 0)
        {
            int firstopen = token.indexOf("{");
            int lastClose = token.lastIndexOf("}");
            String waypoint[] = token.split("[{}]");
            String name = waypoint[0];
            name = name.replace("module", "");
            name = name.trim();
            String actual = token.substring(firstopen + 1, lastClose);
            ScriptModule way = (ScriptModule)ModuleMap.get(name);
            way.Load(name, actual);
        }
    }

    private void CreateFromTokenPP(String token)
    {
        token = token.trim();
        if(token.indexOf("module") == 0)
        {
            String waypoint[] = token.split("[{}]");
            String name = waypoint[0];
            name = name.replace("module", "");
            name = name.trim();
            ScriptModule way = new ScriptModule();
            ModuleMap.put(name, way);
        }
    }

    public void LoadStory(String story)
        throws IOException, URISyntaxException
    {
        try
        {
            Enumeration en = GameWindow.class.getClassLoader().getResources((new StringBuilder()).append("stories/").append(story).append("/").toString());
            URL url = null;
            if(en.hasMoreElements())
            {
                URL metaInf = (URL)en.nextElement();
                File fileMetaInf = new File(metaInf.toURI());
                String filenames[] = fileMetaInf.list();
                String arr$[] = filenames;
                int len$ = arr$.length;
                for(int i$ = 0; i$ < len$; i$++)
                {
                    String f = arr$[i$];
                    LoadFile((new StringBuilder()).append("stories/").append(story).append("/").append(f).toString(), true);
                }

            }
        }
        catch(IOException ex)
        {
            Logger.getLogger(ScriptManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Stack getStoryList()
        throws IOException, URISyntaxException
    {
        Stack StoryList = new Stack();
        File fo = new File("mods/stories/");
        String internalNames[] = fo.list();
        StoryList.addAll(Arrays.asList(internalNames));
        fo = new File("media/stories/");
        internalNames = fo.list();
        StoryList.addAll(Arrays.asList(internalNames));
        return StoryList;
    }

    public void LoadDir(String path, boolean bUseJar)
        throws URISyntaxException
    {
        if(bUseJar)
        {
            File dir = new File((new StringBuilder()).append("media/").append(path).toString());
            String children[] = dir.list();
            for(int i = 0; i < children.length; i++)
            {
                String filename = children[i];
                File fileMetaInf = new File((new StringBuilder()).append(dir.getAbsolutePath()).append(File.separator).append(filename).toString());
                searchFolders(fileMetaInf);
            }

        } else
        {
            File fo = new File((new StringBuilder()).append("mods/").append(path).toString());
            searchFolders(fo);
        }
    }

    public void LoadDirPP(String path, boolean bUseJar)
        throws URISyntaxException
    {
        if(bUseJar)
        {
            File dir = new File((new StringBuilder()).append("media/").append(path).toString());
            String children[] = dir.list();
            for(int i = 0; i < children.length; i++)
            {
                String filename = children[i];
                File fileMetaInf = new File((new StringBuilder()).append(dir.getAbsolutePath()).append(File.separator).append(filename).toString());
                searchFoldersPP(fileMetaInf);
            }

        } else
        {
            File fo = new File((new StringBuilder()).append("mods/").append(path).toString());
            searchFoldersPP(fo);
        }
    }

    public void searchFoldersPP(File fo)
    {
        if(fo.isDirectory())
        {
            String internalNames[] = fo.list();
            for(int i = 0; i < internalNames.length; i++)
                searchFoldersPP(new File((new StringBuilder()).append(fo.getAbsolutePath()).append(File.separator).append(internalNames[i]).toString()));

        } else
        {
            try
            {
                LoadFilePP(fo.getAbsolutePath(), false);
            }
            catch(Exception ex) { }
        }
    }

    public void searchFolders(File fo)
    {
        if(fo.isDirectory())
        {
            String internalNames[] = fo.list();
            for(int i = 0; i < internalNames.length; i++)
                searchFolders(new File((new StringBuilder()).append(fo.getAbsolutePath()).append(File.separator).append(internalNames[i]).toString()));

        } else
        if(fo.getAbsolutePath().toLowerCase().contains(".txt"))
            try
            {
                LoadFile(fo.getAbsolutePath(), false);
            }
            catch(Exception ex) { }
        else
        if(fo.getAbsolutePath().toLowerCase().contains(".lot"))
        {
            String name = fo.getAbsolutePath().substring(fo.getAbsolutePath().lastIndexOf("\\") + 1);
            name = name.substring(0, name.lastIndexOf("."));
            MapMap.put(name, fo.getAbsolutePath());
        }
    }

    public static String getItemName(String name)
    {
        if(!name.contains("."))
            return name;
        else
            return name.split("\\.")[1];
    }

    public void FillInventory(IsoGameCharacter chr, ItemContainer container, String InventoryScript)
    {
        Inventory inv = null;
        String mod = chr.getScriptModule();
        if(InventoryScript.contains("."))
        {
            inv = getInventory(InventoryScript);
            mod = InventoryScript.split("\\.")[0];
        } else
        {
            inv = getInventory((new StringBuilder()).append(chr.getScriptModule()).append(".").append(InventoryScript).toString());
        }
        if(inv == null)
            return;
        for(int n = 0; n < inv.Items.size(); n++)
            if(((zombie.scripting.objects.Inventory.Source)inv.Items.get(n)).type.trim().length() > 0)
            {
                InventoryItem item = InventoryItemFactory.CreateItem((new StringBuilder()).append(mod).append(".").append(((zombie.scripting.objects.Inventory.Source)inv.Items.get(n)).type).toString());
                item.setUses(((zombie.scripting.objects.Inventory.Source)inv.Items.get(n)).count);
                container.AddItem(item);
            }

    }

    public void Trigger(String type)
    {
        type = type.toLowerCase();
        FireHook(type);
        if(TriggerMap.containsKey(type))
        {
            Stack triggers = (Stack)TriggerMap.get(type);
            for(int n = 0; n < triggers.size(); n++)
                if(!((Trigger)triggers.get(n)).module.disabled && !((Trigger)triggers.get(n)).Locked)
                {
                    ((Trigger)triggers.get(n)).TriggerParam = null;
                    ((Trigger)triggers.get(n)).TriggerParam2 = null;
                    ((Trigger)triggers.get(n)).TriggerParam3 = null;
                    ((Trigger)triggers.get(n)).Process();
                }

        }
    }

    public void Trigger(String type, String param)
    {
        type = type.toLowerCase();
        FireHook(type);
        if(TriggerMap.containsKey(type))
        {
            Stack triggers = (Stack)TriggerMap.get(type);
            for(int n = 0; n < triggers.size(); n++)
                if(!((Trigger)triggers.get(n)).module.disabled && !((Trigger)triggers.get(n)).Locked)
                {
                    ((Trigger)triggers.get(n)).TriggerParam = param;
                    ((Trigger)triggers.get(n)).Process();
                }

        }
    }

    public void Trigger(String type, String param, String param2)
    {
        type = type.toLowerCase();
        FireHook(type);
        if(TriggerMap.containsKey(type))
        {
            Stack triggers = (Stack)TriggerMap.get(type);
            for(int n = 0; n < triggers.size(); n++)
                if(!((Trigger)triggers.get(n)).module.disabled && !((Trigger)triggers.get(n)).Locked)
                {
                    ((Trigger)triggers.get(n)).TriggerParam = param;
                    ((Trigger)triggers.get(n)).TriggerParam2 = param2;
                    ((Trigger)triggers.get(n)).Process();
                }

        }
    }

    public void Trigger(String type, String param, String param2, String param3)
    {
        type = type.toLowerCase();
        FireHook(type);
        if(TriggerMap.containsKey(type))
        {
            Stack triggers = (Stack)TriggerMap.get(type);
            for(int n = 0; n < triggers.size(); n++)
                if(!((Trigger)triggers.get(n)).module.disabled && !((Trigger)triggers.get(n)).Locked)
                {
                    ((Trigger)triggers.get(n)).TriggerParam = param;
                    ((Trigger)triggers.get(n)).TriggerParam2 = param2;
                    ((Trigger)triggers.get(n)).TriggerParam2 = param3;
                    ((Trigger)triggers.get(n)).Process();
                }

        }
    }

    public boolean IsScriptPlaying(String check)
    {
        for(int n = 0; n < PlayingScripts.size(); n++)
            if(((zombie.scripting.objects.Script.ScriptInstance)PlayingScripts.get(n)).theScript.name.equals(check))
                return true;

        return false;
    }

    public boolean IsScriptPlaying(zombie.scripting.objects.Script.ScriptInstance check)
    {
        for(int n = 0; n < PlayingScripts.size(); n++)
            if(PlayingScripts.get(n) == check)
                return true;

        return false;
    }

    public void PauseScript(String name)
    {
        for(int n = 0; n < PlayingScripts.size(); n++)
            if(((zombie.scripting.objects.Script.ScriptInstance)PlayingScripts.get(n)).theScript.name.equals(name))
                ((zombie.scripting.objects.Script.ScriptInstance)PlayingScripts.get(n)).Paused = true;

    }

    public void UnPauseScript(String name)
    {
        for(int n = 0; n < PlayingScripts.size(); n++)
            if(((zombie.scripting.objects.Script.ScriptInstance)PlayingScripts.get(n)).theScript.name.equals(name))
                ((zombie.scripting.objects.Script.ScriptInstance)PlayingScripts.get(n)).Paused = false;

    }

    public ScriptModule getModule(String name)
    {
        if(CachedModules.containsKey(name))
            return (ScriptModule)CachedModules.get(name);
        ScriptModule ret = null;
        if(ModuleAliases.containsKey(name))
            name = (String)ModuleAliases.get(name);
        if(CachedModules.containsKey(name))
            return (ScriptModule)CachedModules.get(name);
        if(ModuleMap.containsKey(name))
            if(((ScriptModule)ModuleMap.get(name)).disabled)
                ret = null;
            else
                ret = (ScriptModule)ModuleMap.get(name);
        if(ret != null)
        {
            CachedModules.put(name, ret);
            return ret;
        }
        if(name.indexOf(".") != -1)
            ret = getModule(name.split("\\.")[0]);
        if(ret != null)
        {
            CachedModules.put(name, ret);
            return ret;
        } else
        {
            return null;
        }
    }

    public ScriptModule getModuleNoDisableCheck(String name)
    {
        if(ModuleAliases.containsKey(name))
            name = (String)ModuleAliases.get(name);
        if(ModuleMap.containsKey(name))
            return (ScriptModule)ModuleMap.get(name);
        if(name.indexOf(".") != -1)
            return getModule(name.split("\\.")[0]);
        else
            return null;
    }

    public Inventory getInventory(String name)
    {
        ScriptModule module = getModule(name);
        if(module == null)
            return null;
        else
            return (Inventory)module.InventoryMap.get(getItemName(name));
    }

    public ScriptCharacter getCharacter(String name)
    {
        ScriptModule module = getModule(name);
        if(module == null)
            return null;
        else
            return module.getCharacter(getItemName(name));
    }

    public ScriptCharacter FindCharacter(String name)
    {
        for(Iterator modules = ModuleMap.values().iterator(); modules != null && modules.hasNext();)
        {
            ScriptModule m = (ScriptModule)modules.next();
            if(!m.disabled && m.ValidMapCheck(IsoWorld.instance.playerCell) && m.CharacterMap.containsKey(name))
                return m.getCharacter(name);
        }

        return null;
    }

    public IsoGameCharacter getCharacterActual(String name)
    {
        ScriptModule module = getModule(name);
        if(module == null)
            return null;
        else
            return module.getCharacterActual(getItemName(name));
    }

    public int getFlagIntValue(String name)
    {
        ScriptModule module = getModule(name);
        if(module == null)
            return 0;
        else
            return module.getFlagIntValue(getItemName(name));
    }

    public String getFlagValue(String name)
    {
        ScriptModule module = getModule(name);
        if(module == null)
            return "";
        else
            return module.getFlagValue(getItemName(name));
    }

    public Waypoint getWaypoint(String name)
    {
        ScriptModule module = getModule(name);
        if(module == null)
            return null;
        else
            return module.getWaypoint(getItemName(name));
    }

    public ScriptContainer getScriptContainer(String name)
    {
        ScriptModule module = getModule(name);
        if(module == null)
            return null;
        else
            return module.getScriptContainer(getItemName(name));
    }

    public Room getRoom(String name)
    {
        ScriptModule module = getModule(name);
        if(module == null)
            return null;
        else
            return module.getRoom(getItemName(name));
    }

    public LanguageDefinition getLanguageDef(String name)
    {
        ScriptModule module = getModule(name);
        if(module == null)
            return null;
        else
            return module.getLanguageDef(getItemName(name));
    }

    public String getLanguage(String id)
    {
        if(!id.contains("@"))
        {
            return id;
        } else
        {
            String split[] = id.split("-");
            LanguageDefinition def = getLanguageDef(split[0]);
            return def.get(Integer.parseInt(split[1]));
        }
    }

    public ScriptTalker getTalker(String name)
    {
        ScriptModule module = getModule(name);
        if(module == null)
            return null;
        else
            return module.getTalker(getItemName(name));
    }

    public ScriptActivatable getActivatable(String name)
    {
        ScriptModule module = getModule(name);
        if(module == null)
            return null;
        else
            return module.getActivatable(getItemName(name));
    }

    public ScriptFlag getFlag(String name)
    {
        ScriptModule module = getModule(name);
        if(module == null)
            return null;
        else
            return module.getFlag(getItemName(name));
    }

    public Zone getZone(String name)
    {
        ScriptModule module = getModule(name);
        if(module == null)
            return null;
        else
            return module.getZone(getItemName(name));
    }

    public QuestTaskCondition getQuestCondition(String name)
    {
        ScriptModule module = getModule(name);
        if(module == null)
            return null;
        else
            return module.getQuestCondition(getItemName(name));
    }

    public Item getItem(String name)
    {
        ScriptModule module = getModule(name);
        if(module == null)
            return null;
        else
            return module.getItem(getItemName(name));
    }

    public Item FindItem(String name)
    {
        Item item;
label0:
        {
            ScriptModule module = getModule(name);
            if(module == null)
                return null;
            item = module.getItem(getItemName(name));
            if(item != null)
                break label0;
            Iterator modules = ModuleMap.values().iterator();
            do
            {
                ScriptModule m;
                do
                {
                    if(modules == null || !modules.hasNext())
                        break label0;
                    m = (ScriptModule)modules.next();
                } while(m.disabled || !m.ValidMapCheck(IsoWorld.instance.playerCell));
                item = module.getItem(getItemName(name));
            } while(item == null);
            return item;
        }
        return item;
    }

    public Recipe getRecipe(String name)
    {
        ScriptModule module = getModule(name);
        if(module == null)
            return null;
        else
            return module.getRecipe(getItemName(name));
    }

    public void CheckExitPoints()
    {
        for(Iterator modules = ModuleMap.values().iterator(); modules != null && modules.hasNext();)
        {
            ScriptModule m = (ScriptModule)modules.next();
            if(!m.disabled && m.ValidMapCheck(IsoWorld.instance.playerCell) && m.CheckExitPoints())
                return;
        }

    }

    public Script getScript(String name)
    {
        ScriptModule module = getModule(name);
        if(module == null)
            return null;
        else
            return module.getScript(getItemName(name));
    }

    public Stack getAllRecipes()
    {
        Iterator modules = ModuleMap.values().iterator();
        recipesTempList.clear();
        do
        {
            if(modules == null || !modules.hasNext())
                break;
            ScriptModule m = (ScriptModule)modules.next();
            if(!m.disabled && m.ValidMapCheck(IsoWorld.instance.playerCell))
            {
                Iterator recipes = m.RecipeMap.values().iterator();
                while(recipes != null && recipes.hasNext()) 
                {
                    Recipe r = (Recipe)recipes.next();
                    recipesTempList.add(r);
                }
            }
        } while(true);
        return recipesTempList;
    }

    public Stack getAllZones()
    {
        Iterator modules = ModuleMap.values().iterator();
        zoneTempList.clear();
        do
        {
            if(modules == null || !modules.hasNext())
                break;
            ScriptModule m = (ScriptModule)modules.next();
            if(!m.disabled && m.ValidMapCheck(IsoWorld.instance.playerCell))
            {
                Iterator recipes = m.ZoneList.iterator();
                while(recipes != null && recipes.hasNext()) 
                {
                    Zone r = (Zone)recipes.next();
                    zoneTempList.add(r);
                }
            }
        } while(true);
        return zoneTempList;
    }

    public Stack getAllContainerDistributions()
    {
        Iterator modules = ModuleMap.values().iterator();
        conTempList.clear();
        do
        {
            if(modules == null || !modules.hasNext())
                break;
            ScriptModule m = (ScriptModule)modules.next();
            if(!m.disabled && m.ValidMapCheck(IsoWorld.instance.playerCell))
            {
                Iterator recipes = m.ContainerDistributions.iterator();
                while(recipes != null && recipes.hasNext()) 
                {
                    ContainerDistribution r = (ContainerDistribution)recipes.next();
                    conTempList.add(r);
                }
            }
        } while(true);
        return conTempList;
    }

    public Stack getAllShelfDistributions()
    {
        Iterator modules = ModuleMap.values().iterator();
        shelfTempList.clear();
        do
        {
            if(modules == null || !modules.hasNext())
                break;
            ScriptModule m = (ScriptModule)modules.next();
            if(!m.disabled && m.ValidMapCheck(IsoWorld.instance.playerCell))
            {
                Iterator recipes = m.ShelfDistributions.iterator();
                while(recipes != null && recipes.hasNext()) 
                {
                    ShelfDistribution r = (ShelfDistribution)recipes.next();
                    shelfTempList.add(r);
                }
            }
        } while(true);
        return shelfTempList;
    }

    public Stack getAllFloorDistributions()
    {
        Iterator modules = ModuleMap.values().iterator();
        floorTempList.clear();
        do
        {
            if(modules == null || !modules.hasNext())
                break;
            ScriptModule m = (ScriptModule)modules.next();
            if(!m.disabled && m.ValidMapCheck(IsoWorld.instance.playerCell))
            {
                Iterator recipes = m.FloorDistributions.iterator();
                while(recipes != null && recipes.hasNext()) 
                {
                    FloorDistribution r = (FloorDistribution)recipes.next();
                    floorTempList.add(r);
                }
            }
        } while(true);
        return floorTempList;
    }

    public Stack getZones(String Zone)
    {
        Iterator modules = ModuleMap.values().iterator();
        zoneTempList.clear();
        while(modules != null && modules.hasNext()) 
        {
            ScriptModule m = (ScriptModule)modules.next();
            if(!m.disabled && m.ValidMapCheck(IsoWorld.instance.playerCell))
            {
                Iterator recipes = m.ZoneList.iterator();
                while(recipes != null && recipes.hasNext()) 
                {
                    Zone r = (Zone)recipes.next();
                    if(r.name.equals(Zone))
                        zoneTempList.add(r);
                }
            }
        }
        return zoneTempList;
    }

    public void AddZone(String module, String name, Zone zone)
    {
        ScriptModule mod = null;
        if(ModuleMap.containsKey(module))
        {
            mod = getModule(module);
        } else
        {
            mod = new ScriptModule();
            mod.name = module;
            ModuleMap.put(module, mod);
        }
        mod.ZoneMap.put(name, zone);
        mod.ZoneList.add(zone);
    }

    public void AddRoom(String module, String name, Room room)
    {
        ScriptModule mod = null;
        if(ModuleMap.containsKey(module))
        {
            mod = getModule(module);
        } else
        {
            mod = new ScriptModule();
            mod.name = module;
            ModuleMap.put(module, mod);
        }
        mod.RoomMap.put(name, room);
        mod.RoomList.add(room);
    }

    public void Reset()
    {
        ModuleMap.clear();
        ModuleAliases.clear();
        TriggerMap.clear();
        HookMap.clear();
        CustomTriggerMap.clear();
        CustomTriggerLastRan.clear();
        PlayingScripts.clear();
        try
        {
            LoadDirPP("scripts/", true);
            LoadDir("scripts/", true);
            Stack stories = instance.getStoryList();
            LoadDirPP("scripts/", false);
            LoadDir("scripts/", false);
        }
        catch(Exception ex) { }
    }

    public String getRandomMap()
    {
        int n = Rand.Next(MapMap.keySet().size());
        Iterator it = MapMap.keySet().iterator();
        String str = "";
        for(int c = -1; it != null && it.hasNext() && c != n; c++)
            str = (String)it.next();

        return str;
    }

    public Stack getAllRecipesFor(String result)
    {
        Stack rec = getAllRecipes();
        Stack res = new Stack();
        for(int n = 0; n < rec.size(); n++)
        {
            String t = ((Recipe)rec.get(n)).Result.type;
            if(t.contains("."))
                t = t.substring(t.indexOf(".") + 1);
            if(t.equals(result))
                res.add(rec.get(n));
        }

        return res;
    }

    public void StopScript(zombie.scripting.objects.Script.ScriptInstance currentinstance)
    {
        toStopInstance.add(currentinstance);
    }

    public static ScriptManager instance = new ScriptManager();
    public THashMap TriggerMap;
    public THashMap CustomTriggerMap;
    public THashMap CustomTriggerLastRan;
    public THashMap HookMap;
    public THashMap ModuleMap;
    public Stack PlayingScripts;
    public ScriptModule CurrentLoadingModule;
    public THashMap ModuleAliases;
    public boolean skipping;
    public THashMap MapMap;
    ArrayList toStop;
    ArrayList toStopInstance;
    HashMap CachedModules;
    Stack recipesTempList;
    Stack zoneTempList;
    Stack conTempList;
    Stack floorTempList;
    Stack shelfTempList;

}
