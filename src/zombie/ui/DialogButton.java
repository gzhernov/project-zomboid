// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   DialogButton.java

package zombie.ui;

import se.krka.kahlua.integration.LuaCaller;
import se.krka.kahlua.vm.KahluaTable;
import zombie.Lua.LuaManager;
import zombie.core.Color;
import zombie.core.textures.Texture;
import zombie.core.textures.TexturePackPage;

// Referenced classes of package zombie.ui:
//            UIElement, UIEventHandler, TextManager, UIFont

public class DialogButton extends UIElement
{

    public DialogButton(UIElement messages, int x, int y, String text, String name)
    {
        clicked = false;
        mouseOver = false;
        MessageTarget2 = null;
        this.x = x;
        this.y = y;
        origX = x;
        MessageTarget = messages;
        upLeft = TexturePackPage.getTexture("ButtonL_Up");
        upMid = TexturePackPage.getTexture("ButtonM_Up");
        upRight = TexturePackPage.getTexture("ButtonR_Up");
        downLeft = TexturePackPage.getTexture("ButtonL_Down");
        downMid = TexturePackPage.getTexture("ButtonM_Down");
        downRight = TexturePackPage.getTexture("ButtonR_Down");
        this.name = name;
        this.text = text;
        width = TextManager.instance.MeasureStringX(UIFont.Small, text);
        width += 8;
        if(width < 40)
            width = 40;
        height = downMid.getHeight();
        this.x -= width / 2;
    }

    public DialogButton(UIEventHandler messages, int x, int y, String text, String name)
    {
        clicked = false;
        mouseOver = false;
        MessageTarget2 = null;
        this.x = x;
        this.y = y;
        origX = x;
        MessageTarget2 = messages;
        upLeft = TexturePackPage.getTexture("ButtonL_Up");
        upMid = TexturePackPage.getTexture("ButtonM_Up");
        upRight = TexturePackPage.getTexture("ButtonR_Up");
        downLeft = TexturePackPage.getTexture("ButtonL_Down");
        downMid = TexturePackPage.getTexture("ButtonM_Down");
        downRight = TexturePackPage.getTexture("ButtonR_Down");
        this.name = name;
        this.text = text;
        width = TextManager.instance.MeasureStringX(UIFont.Small, text);
        width += 8;
        if(width < 40)
            width = 40;
        height = downMid.getHeight();
        this.x -= width / 2;
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
        mouseOver = true;
        Object o[];
        if(getTable() != null && getTable().rawget("onMouseMove") != null)
            o = LuaManager.caller.pcall(LuaManager.thread, getTable().rawget("onMouseMove"), new Object[] {
                table, Integer.valueOf(dx), Integer.valueOf(dy)
            });
        return true;
    }

    public void onMouseMoveOutside(int dx, int dy)
    {
        clicked = false;
        Object o[];
        if(getTable() != null && getTable().rawget("onMouseMoveOutside") != null)
            o = LuaManager.caller.pcall(LuaManager.thread, getTable().rawget("onMouseMoveOutside"), new Object[] {
                table, Integer.valueOf(dx), Integer.valueOf(dy)
            });
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
            if(MessageTarget != null)
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
            DrawTexture(downLeft, 0, 0, 1.0F);
            DrawTextureScaledCol(downMid, downLeft.getWidth(), 0, getWidth() - downLeft.getWidth() * 2, downLeft.getHeight(), new Color(255, 255, 255, 255));
            DrawTexture(downRight, getWidth() - downRight.getWidth(), 0, 1.0F);
            DrawTextCentre(text, getWidth() / 2, 1, 1.0F, 1.0F, 1.0F, 1.0F);
        } else
        {
            DrawTexture(upLeft, 0, 0, 1.0F);
            DrawTextureScaledCol(upMid, downLeft.getWidth(), 0, getWidth() - downLeft.getWidth() * 2, downLeft.getHeight(), new Color(255, 255, 255, 255));
            DrawTexture(upRight, getWidth() - downRight.getWidth(), 0, 1.0F);
            DrawTextCentre(text, getWidth() / 2, 0, 1.0F, 1.0F, 1.0F, 1.0F);
        }
        super.render();
    }

    public boolean clicked;
    public UIElement MessageTarget;
    public boolean mouseOver;
    public String name;
    public String text;
    Texture downLeft;
    Texture downMid;
    Texture downRight;
    int origX;
    Texture upLeft;
    Texture upMid;
    Texture upRight;
    private UIEventHandler MessageTarget2;
}
