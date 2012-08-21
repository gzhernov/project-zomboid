// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   LuaTimedAction.java

package zombie.characters.CharacterTimedActions;

import se.krka.kahlua.vm.KahluaTable;
import zombie.Lua.LuaManager;
import zombie.characters.IsoGameCharacter;

// Referenced classes of package zombie.characters.CharacterTimedActions:
//            BaseAction

public class LuaTimedAction extends BaseAction
{

    public LuaTimedAction(KahluaTable table, IsoGameCharacter chr)
    {
        super(chr);
        this.table = table;
        Object maxTime = table.rawget("maxTime");
        MaxTime = ((Integer)LuaManager.converterManager.fromLuaToJava(maxTime, Integer.class)).intValue();
        Object stopOnWalk = table.rawget("stopOnWalk");
        Object stopOnRun = table.rawget("stopOnRun");
        if(stopOnWalk != null)
            StopOnWalk = ((Boolean)LuaManager.converterManager.fromLuaToJava(stopOnWalk,  Boolean.class)).booleanValue();
        if(stopOnRun != null)
            StopOnRun = ((Boolean)LuaManager.converterManager.fromLuaToJava(stopOnRun,  Boolean.class)).booleanValue();
    }

    public boolean valid()
    {
        Object o[] = LuaManager.caller.pcall(LuaManager.thread, table.rawget("isValidFunc"), new Object[] {
            table.rawget("character"), table.rawget("param1"), table.rawget("param2"), table.rawget("param3"), table.rawget("param4"), table.rawget("param5")
        });
        return o.length > 0 && ((Boolean)(Boolean)o[0]).booleanValue();
    }

    public void start()
    {
        LuaManager.caller.pcall(LuaManager.thread, table.rawget("startFunc"), new Object[] {
            table.rawget("character"), table.rawget("param1"), table.rawget("param2"), table.rawget("param3"), table.rawget("param4"), table.rawget("param5")
        });
    }

    public void stop()
    {
        super.stop();
        LuaManager.caller.pcall(LuaManager.thread, table.rawget("onStopFunc"), new Object[] {
            table.rawget("character"), table.rawget("param1"), table.rawget("param2"), table.rawget("param3"), table.rawget("param4"), table.rawget("param5")
        });
    }

    public void perform()
    {
        LuaManager.caller.pcall(LuaManager.thread, table.rawget("performFunc"), new Object[] {
            table.rawget("character"), table.rawget("param1"), table.rawget("param2"), table.rawget("param3"), table.rawget("param4"), table.rawget("param5")
        });
    }

    KahluaTable table;
}
