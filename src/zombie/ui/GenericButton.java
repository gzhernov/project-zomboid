// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   GenericButton.java

package zombie.ui;

import se.krka.kahlua.integration.LuaCaller;
import se.krka.kahlua.vm.KahluaTable;
import zombie.Lua.LuaManager;
import zombie.core.textures.Texture;

// Referenced classes of package zombie.ui:
//            UIElement, UIEventHandler

public class GenericButton extends UIElement
{

    public GenericButton(UIElement messages, int x, int y, int width, int height, String name, String text, 
            Texture UpTex, Texture DownTex)
    {
        clicked = false;
        mouseOver = false;
        UpTexture = null;
        DownTexture = null;
        MessageTarget2 = null;
        this.x = x;
        this.y = y;
        MessageTarget = messages;
        this.name = name;
        this.text = text;
        this.width = width;
        this.height = height;
        UpTexture = UpTex;
        DownTexture = DownTex;
    }

    public GenericButton(UIEventHandler messages, int x, int y, int width, int height, String name, String text, 
            Texture UpTex, Texture DownTex)
    {
        clicked = false;
        mouseOver = false;
        UpTexture = null;
        DownTexture = null;
        MessageTarget2 = null;
        this.x = x;
        this.y = y;
        MessageTarget2 = messages;
        this.name = name;
        this.text = text;
        this.width = width;
        this.height = height;
        UpTexture = UpTex;
        DownTexture = DownTex;
    }

    public boolean onMouseDown(int x, int y)
    {
        if(!isVisible())
            return false;
        Object o[];
        if(getTable() != null && getTable().rawget("onMouseDown") != null)
            o = LuaManager.caller.pcall(LuaManager.thread, getTable().rawget("onMouseDown"), new Object[] {
                table, Integer.valueOf(x), Integer.valueOf(y)
            });
        clicked = true;
        return true;
    }

    public boolean onMouseMove(int dx, int dy)
    {
        Object o[];
        if(getTable() != null && getTable().rawget("onMouseMove") != null)
            o = LuaManager.caller.pcall(LuaManager.thread, getTable().rawget("onMouseMove"), new Object[] {
                table, Integer.valueOf(dx), Integer.valueOf(dy)
            });
        mouseOver = true;
        return true;
    }

    public void onMouseMoveOutside(int dx, int dy)
    {
        Object o[];
        if(getTable() != null && getTable().rawget("onMouseMoveOutside") != null)
            o = LuaManager.caller.pcall(LuaManager.thread, getTable().rawget("onMouseMoveOutside"), new Object[] {
                table, Integer.valueOf(dx), Integer.valueOf(dy)
            });
        clicked = false;
        mouseOver = false;
    }

    public boolean onMouseUp(int x, int y)
    {
        Object o[];
        if(getTable() != null && getTable().rawget("onMouseUp") != null)
            o = LuaManager.caller.pcall(LuaManager.thread, getTable().rawget("onMouseUp"), new Object[] {
                table, Integer.valueOf(x), Integer.valueOf(y)
            });
        if(clicked)
            if(MessageTarget2 != null)
                MessageTarget2.Selected(name, 0, 0);
            else
                MessageTarget.ButtonClicked(name);
        clicked = false;
        return true;
    }

    public void render()
    {
        if(!isVisible())
            return;
        int dy = 0;
        if(clicked)
        {
            DrawTextureScaled(DownTexture, 0, 0, getWidth(), getHeight(), 1.0F);
            DrawTextCentre(text, getWidth() / 2, 1, 1.0F, 1.0F, 1.0F, 1.0F);
        } else
        {
            DrawTextureScaled(UpTexture, 0, 0, getWidth(), getHeight(), 1.0F);
            DrawTextCentre(text, getWidth() / 2, 1, 1.0F, 1.0F, 1.0F, 1.0F);
        }
        super.render();
    }

    public boolean clicked;
    public UIElement MessageTarget;
    public boolean mouseOver;
    public String name;
    public String text;
    Texture UpTexture;
    Texture DownTexture;
    private UIEventHandler MessageTarget2;
}
