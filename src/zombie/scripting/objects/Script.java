// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Script.java

package zombie.scripting.objects;

import gnu.trove.map.hash.THashMap;
import java.util.*;
import javax.swing.JOptionPane;
import zombie.characters.IsoGameCharacter;
import zombie.scripting.ScriptManager;
import zombie.scripting.ScriptParsingUtils;
import zombie.scripting.commands.*;

// Referenced classes of package zombie.scripting.objects:
//            BaseScriptObject

public class Script extends BaseScriptObject
{
    public static class ScriptInstance
    {

        public void update()
        {
            theScript.update(this);
        }

        public boolean HasAlias(String str)
        {
            return CharacterAliases.containsKey(str);
        }

        public IsoGameCharacter getAlias(String str)
        {
            return (IsoGameCharacter)CharacterAliases.get(str);
        }

        public boolean finished()
        {
            return theScript.finished(this);
        }

        public void begin()
        {
            theScript.begin(this);
        }

        public boolean AllowBehaviours(IsoGameCharacter gameCharacter)
        {
            return theScript.AllowCharacterBehaviour((String)CharacterAliasesR.get(gameCharacter), this);
        }

        public void CopyAliases(ScriptInstance from)
        {
            if(from == this)
                return;
            parent = from;
            Iterator it = from.CharacterAliases.entrySet().iterator();
            CharacterAliases.clear();
            CharacterAliasesR.clear();
            java.util.Map.Entry e;
            for(; it != null && it.hasNext(); CharacterAliasesR.put(e.getValue(), e.getKey()))
            {
                e = (java.util.Map.Entry)it.next();
                CharacterAliases.put(e.getKey(), e.getValue());
            }

        }

        public ScriptInstance parent;
        public Script theScript;
        public int CommandIndex;
        public boolean Paused;
        public THashMap CharacterAliases;
        public THashMap CharacterAliasesR;
        public String ID;
        public boolean CharactersAlreadyInScript;

        public ScriptInstance()
        {
            parent = null;
            CommandIndex = 0;
            Paused = false;
            CharacterAliases = new THashMap();
            CharacterAliasesR = new THashMap();
            ID = "";
            CharactersAlreadyInScript = false;
        }
    }


    public Script()
    {
        Instancable = false;
        CommandList = new ArrayList();
        LastConditional = null;
    }

    public void Load(String name, String strArray[])
    {
        this.name = name;
        for(int n = 0; n < strArray.length; n++)
            ParseCommand(strArray[n].trim());

    }

    public void begin(ScriptInstance instance)
    {
        instance.CommandIndex = 0;
        if(instance.CommandIndex >= CommandList.size())
            return;
        BaseCommand com = (BaseCommand)CommandList.get(instance.CommandIndex);
        com.currentinstance = instance;
        com.begin();
        for(; com.DoesInstantly(); com.begin())
        {
            com.currentinstance = instance;
            com.update();
            com.Finish();
            instance.CommandIndex++;
            if(instance.CommandIndex >= CommandList.size())
                return;
            com = (BaseCommand)CommandList.get(instance.CommandIndex);
            com.currentinstance = instance;
        }

    }

    public boolean finished(ScriptInstance instance)
    {
        return instance.CommandIndex >= CommandList.size();
    }

    public void reset(ScriptInstance instance)
    {
        instance.CommandIndex = 0;
        instance.Paused = false;
    }

    public void update(ScriptInstance instance)
    {
        if(instance.CommandIndex >= CommandList.size())
            return;
        if(instance.Paused)
            return;
        BaseCommand com = (BaseCommand)CommandList.get(instance.CommandIndex);
        com.currentinstance = instance;
        if(ScriptManager.instance.skipping)
            com.updateskip();
        else
            com.update();
        if(ScriptManager.instance.skipping || com.IsFinished())
        {
            com.Finish();
            instance.CommandIndex++;
            if(instance.CommandIndex >= CommandList.size())
                return;
            BaseCommand com2 = (BaseCommand)CommandList.get(instance.CommandIndex);
            com2.currentinstance = instance;
            com2.begin();
            for(; com2.DoesInstantly(); com2.begin())
            {
                com2.update();
                com2.Finish();
                instance.CommandIndex++;
                if(instance.CommandIndex >= CommandList.size())
                    return;
                com2 = (BaseCommand)CommandList.get(instance.CommandIndex);
                com2.currentinstance = instance;
            }

        }
    }

    protected void ParseCommand(String command)
    {
        if(command.trim().length() == 0)
            return;
        BaseCommand com = ReturnCommand(command);
        if(com != null)
        {
            com.script = this;
            CommandList.add(com);
        }
    }

    protected BaseCommand ReturnCommand(String command)
    {
        if(command.indexOf("callwait") == 0)
        {
            command = command.replace("callwait", "");
            command = (new StringBuilder()).append(command.trim()).append(".CallWait()").toString();
        }
        if(command.indexOf("call") == 0)
        {
            command = command.replace("call", "");
            command = (new StringBuilder()).append(command.trim()).append(".Call()").toString();
        }
        if(command.indexOf("else") == 0)
        {
            int open = command.indexOf("{");
            int close = command.lastIndexOf("}");
            String internal = command.substring(open + 1, close);
            internal = internal;
            LastConditional.AddElse(internal);
            LastConditional = null;
            return null;
        }
        if(command.indexOf("if") == 0)
        {
            int open = command.indexOf("{");
            int close = command.lastIndexOf("}");
            int openc = command.indexOf("(");
            int closec = command.indexOf(")");
            String internal = command.substring(open + 1, close);
            String condition = command.substring(openc + 1, open).trim();
            condition = condition.substring(0, condition.length() - 1);
            internal = internal;
            condition = condition;
            LastConditional = new ConditionalCommand(condition, internal, this);
            return LastConditional;
        }
        String object = null;
        String actual = null;
        if(command.indexOf(".") != -1 && command.indexOf(".") < command.indexOf("("))
        {
            String arr[] = new String[2];
            int lindexof = command.indexOf(".");
            int indexofb = command.indexOf("(");
            int itouse = command.indexOf(".");
            for(; lindexof < indexofb && lindexof != -1; lindexof = command.indexOf(".", lindexof + 1))
                itouse = lindexof;

            arr[0] = command.substring(0, itouse);
            arr[1] = command.substring(itouse + 1);
            object = arr[0];
            actual = arr[1];
        } else
        {
            actual = command;
        }
        if(actual.trim().length() > 0)
            return DoActual(actual, object);
        else
            return null;
    }

    protected BaseCommand DoActual(String actual, String object)
    {
        if(actual.contains("Wait"))
            actual = actual;
        String command = null;
        Exception exception;
        try
        {
            command = new String(actual.substring(0, actual.indexOf("(")));
        }
        catch(Exception ex)
        {
            exception = ex;
        }
        actual = actual.replace(command, "");
        actual = actual.trim().substring(1);
        actual = actual.trim().substring(0, actual.trim().lastIndexOf(")"));
        String params[] = ScriptParsingUtils.SplitExceptInbetween(actual, ",", "\"");
        for(int n = 0; n < params.length; n++)
            params[n] = new String(params[n].trim());

        boolean bInverse = false;
        if(command.indexOf("!") == 0)
        {
            command = command.replace("!", "");
            bInverse = true;
        }
        BaseCommand com = CommandFactory.CreateCommand(command);
        if(com == null)
            JOptionPane.showMessageDialog(null, (new StringBuilder()).append("Command: ").append(command).append(" not found").toString(), "Error", 0);
        com.module = ScriptManager.instance.CurrentLoadingModule;
        try
        {
            if(bInverse)
                com.init("!", params);
            else
            if(object != null)
                com.init(new String(object), params);
            else
                com.init(null, params);
        }
        catch(Exception ex)
        {
            String paramstext = ": [";
            if(params.length > 0)
            {
                for(int n = 0; n < params.length; n++)
                    paramstext = (new StringBuilder()).append(paramstext).append(params[n]).append(", ").toString();

                paramstext = (new StringBuilder()).append(paramstext.substring(0, paramstext.length() - 2)).append("]").toString();
            } else
            {
                paramstext = ".";
            }
            JOptionPane.showMessageDialog(null, (new StringBuilder()).append("Command: ").append(command).append(" parameters incorrect").append(paramstext).toString(), "Error", 0);
        }
        return com;
    }

    public String[] DoScriptParsing(String name, String script)
    {
        Stack StringTokenList = new Stack();
        boolean done = false;
        int nextindexOfOpen = 0;
        int nextindexOfClosed = 0;
        int nextindexOfSemi = 0;
        int depth = 0;
        depth = 0;
        nextindexOfOpen = 0;
        nextindexOfClosed = 0;
        nextindexOfSemi = 0;
        if(script.indexOf("}", nextindexOfOpen) == -1)
        {
            done = true;
            Load(name, script.split(";"));
            return script.split(";");
        }
        do
        {
            nextindexOfOpen = script.indexOf("{", nextindexOfOpen + 1);
            nextindexOfClosed = script.indexOf("}", nextindexOfClosed + 1);
            nextindexOfSemi = script.indexOf(";", nextindexOfSemi + 1);
            if((nextindexOfSemi < nextindexOfOpen || nextindexOfOpen == -1 && nextindexOfSemi != -1) && depth == 0)
            {
                StringTokenList.add(script.substring(0, nextindexOfSemi));
                script = script.substring(nextindexOfSemi + 1);
                nextindexOfOpen = 0;
                nextindexOfClosed = 0;
                nextindexOfSemi = 0;
            } else
            if(nextindexOfClosed < nextindexOfOpen && nextindexOfClosed != -1 || nextindexOfOpen == -1)
            {
                nextindexOfOpen = nextindexOfClosed;
                if(--depth == 0)
                {
                    StringTokenList.add(script.substring(0, nextindexOfOpen + 1));
                    script = script.substring(nextindexOfOpen + 1);
                    nextindexOfOpen = 0;
                    nextindexOfClosed = 0;
                    nextindexOfSemi = 0;
                }
            } else
            if(nextindexOfOpen != -1)
            {
                nextindexOfClosed = nextindexOfOpen;
                depth++;
            }
        } while(script.trim().length() > 0);
        String strArr[] = new String[StringTokenList.size()];
        for(int n = 0; n < StringTokenList.size(); n++)
            strArr[n] = (String)StringTokenList.get(n);

        Load(name, strArr);
        return strArr;
    }

    public boolean AllowCharacterBehaviour(String scriptCharacter, ScriptInstance instance)
    {
        if(instance.CommandIndex >= CommandList.size())
            return true;
        if(instance.Paused)
            return true;
        else
            return ((BaseCommand)CommandList.get(instance.CommandIndex)).AllowCharacterBehaviour(scriptCharacter);
    }

    public boolean Instancable;
    public String name;
    public ArrayList CommandList;
    ConditionalCommand LastConditional;
}
