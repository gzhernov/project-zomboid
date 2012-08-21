// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   LuaUIWindow.java

package zombie.ui;

import se.krka.kahlua.integration.LuaCaller;
import se.krka.kahlua.vm.KahluaTable;
import zombie.Lua.LuaManager;

// Referenced classes of package zombie.ui:
//            NewWindow

public class LuaUIWindow extends NewWindow
{

    public LuaUIWindow(int x, int y, int wid, int hei, boolean hasClose, KahluaTable table)
    {
        super(x, y, wid, hei, hasClose);
        ResizeToFitY = false;
        this.table = table;
    }

    public void ButtonClicked(String name)
    {
        super.ButtonClicked(name);
        Object o[];
        if(getTable().rawget("onButtonClicked") != null)
            o = LuaManager.caller.pcall(LuaManager.thread, getTable().rawget("onButtonClicked"), new Object[] {
                table, name
            });
    }
}
