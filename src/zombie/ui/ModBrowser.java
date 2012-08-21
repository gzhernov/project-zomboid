// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ModBrowser.java

package zombie.ui;

import zombie.core.Color;
import zombie.gameStates.ModBrowserState;

// Referenced classes of package zombie.ui:
//            NewWindow, ListBox

public class ModBrowser extends NewWindow
{

    public ModBrowser(ModBrowserState state, int x, int y, int width, int height)
    {
        super(x, y, width, height, false);
        scaleRatioX = 1.0F;
        scaleRatioY = 1.0F;
        ResizeToFitY = false;
        scaleRatioX = (float)width / 640F;
        scaleRatioY = (float)height / 480F;
        Categories = new ListBox("categories", state);
        Mods = new ListBox("mods", state);
        Installed = new ListBox("installed", state);
        Categories.x = 30F;
        Categories.y = 50F;
        Categories.width = (int)(100F * scaleRatioX);
        Categories.height = (int)(200F * scaleRatioY);
        Mods.x = Categories.x + (float)Categories.width + 60F;
        Mods.y = 50F;
        Mods.width = (int)(100F * scaleRatioX);
        Mods.height = (int)(300F * scaleRatioY);
        Installed.x = Mods.x + (float)Mods.width + 60F;
        Installed.y = 50F;
        Installed.width = (int)(100F * scaleRatioX);
        Installed.height = (int)(300F * scaleRatioY);
        AddChild(Categories);
        AddChild(Mods);
        AddChild(Installed);
        Refresh();
    }

    public void Refresh()
    {
        Categories.AddItem(null, Color.yellow, Color.lightGray, Color.black);
    }

    public void update()
    {
        super.update();
    }

    public void render()
    {
        super.render();
    }

    ListBox Categories;
    ListBox Mods;
    ListBox Installed;
    float scaleRatioX;
    float scaleRatioY;
}
