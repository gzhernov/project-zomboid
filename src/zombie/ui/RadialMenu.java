// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   RadialMenu.java

package zombie.ui;

import java.util.Stack;
import java.util.Vector;
import zombie.characters.*;
import zombie.iso.IsoObject;
import zombie.iso.Vector2;

// Referenced classes of package zombie.ui:
//            UIElement, HUDButton

public class RadialMenu extends UIElement
{
    public static class MenuOption
    {

        public void Clicked(RadialMenu menu)
        {
            if(SubMenu != null)
            {
                menu.CurrentMenu = SubMenu;
                menu.getControls().clear();
                menu.BuildMenuFromCurrentMenu();
            } else
            {
                menu.ClickedRadialOption(Command);
            }
        }

        public String HoverText;
        public String Icon;
        public String Background;
        public String Command;
        public Menu SubMenu;

        public MenuOption(String Command, String HoverText, String Background, String Icon)
        {
            SubMenu = null;
            this.Command = Command;
            this.HoverText = HoverText;
            this.Background = Background;
            this.Icon = Icon;
        }

        public MenuOption(String Command, String HoverText, String Background, String Icon, Menu Sub)
        {
            SubMenu = null;
            this.Command = Command;
            this.HoverText = HoverText;
            this.Background = Background;
            this.Icon = Icon;
            SubMenu = Sub;
        }
    }

    public static class Menu
    {

        public Stack Options;
        public Menu parent;

        public Menu(Menu parent)
        {
            Options = new Stack();
            this.parent = null;
            this.parent = parent;
        }
    }


    public void ButtonClicked(String name)
    {
        for(int n = 0; n < CurrentMenu.Options.size(); n++)
            if(((MenuOption)CurrentMenu.Options.get(n)).Command.equals(name))
            {
                ((MenuOption)CurrentMenu.Options.get(n)).Clicked(this);
                return;
            }

        if("Back".equals(name))
        {
            CurrentMenu = CurrentMenu.parent;
            getControls().clear();
            BuildMenuFromCurrentMenu();
            return;
        } else
        {
            return;
        }
    }

    private void ClickedRadialOption(String Command)
    {
        if("TeamUp".equals(Command) && (RadialTarget instanceof IsoSurvivor))
            ((IsoSurvivor)RadialTarget).MeetFirstTime(IsoPlayer.getInstance(), true, false);
        if("Follow".equals(Command) && (RadialTarget instanceof IsoSurvivor))
            ((IsoSurvivor)RadialTarget).FollowMe(IsoPlayer.getInstance());
        if("Stay".equals(Command) && (RadialTarget instanceof IsoSurvivor))
            ((IsoSurvivor)RadialTarget).StayHere(IsoPlayer.getInstance());
        if("Guard".equals(Command) && (RadialTarget instanceof IsoSurvivor))
            ((IsoSurvivor)RadialTarget).Guard(IsoPlayer.getInstance());
        close();
    }

    private void BuildMenuFromCurrentMenu()
    {
        getControls().clear();
        int num = CurrentMenu.Options.size();
        Vector2 positions[] = new Vector2[num];
        if(num == 1)
            positions[0] = new Vector2(getWidth() / 2, 16F);
        else
        if(num == 2)
        {
            positions[0] = new Vector2(getWidth() / 2, 16F);
            positions[1] = new Vector2(getWidth() / 2, 16F);
            positions[0].x -= 16F;
            positions[1].x += 16F;
        } else
        if(num == 3)
        {
            positions[0] = new Vector2(getWidth() / 2, 32F);
            positions[1] = new Vector2(getWidth() / 2, 16F);
            positions[2] = new Vector2(getWidth() / 2, 32F);
            positions[0].x -= 32F;
            positions[2].x += 32F;
        }
        for(int n = 0; n < num; n++)
        {
            HUDButton hud = new HUDButton(((MenuOption)CurrentMenu.Options.get(n)).Command, (int)positions[n].x - 12, (int)positions[n].y - 12, ((MenuOption)CurrentMenu.Options.get(n)).Background, ((MenuOption)CurrentMenu.Options.get(n)).Background, ((MenuOption)CurrentMenu.Options.get(n)).Icon, this);
            AddChild(hud);
        }

        if(CurrentMenu != MainMenu)
        {
            HUDButton hud = new HUDButton("Back", getWidth() / 2 - 12, getHeight() - 32, "Radial_Button_Red", "Radial_Button_Red", "Radial_Button_Back_White", this);
            AddChild(hud);
        }
    }

    void close()
    {
        setVisible(false);
        CurrentMenu = null;
        MainMenu = null;
        OptionButtons.clear();
    }

    public RadialMenu()
    {
        RadialTarget = null;
        OptionButtons = new Stack();
        BackButton = null;
        visible = false;
    }

    public void render()
    {
        super.render();
        if(CurrentMenu == null)
            return;
        for(int n = 0; n < getControls().size(); n++)
        {
            UIElement el = (UIElement)getControls().get(n);
            if(!(el instanceof HUDButton))
                continue;
            HUDButton b = (HUDButton)el;
            if(!b.mouseOver)
                continue;
            if(n >= CurrentMenu.Options.size())
                DrawTextCentre("Back", getWidth() / 2, getHeight() / 2, 1.0F, 1.0F, 1.0F, 1.0F);
            else
                DrawTextCentre(((MenuOption)CurrentMenu.Options.get(n)).HoverText, getWidth() / 2, getHeight() / 2, 1.0F, 1.0F, 1.0F, 1.0F);
        }

    }

    public void CreateFromObject(IsoObject obj, int mousex, int mousey)
    {
        setX(mousex - 100);
        setY(mousey - 50);
        setWidth(200);
        setHeight(100);
        setVisible(true);
        RadialTarget = obj;
        MainMenu = CurrentMenu = new Menu(null);
        if(obj instanceof IsoSurvivor)
        {
            boolean bDoGroupOptions = false;
            IsoSurvivor survivor = (IsoSurvivor)obj;
            if(IsoPlayer.getInstance().getDescriptor().InGroupWith(survivor))
                bDoGroupOptions = true;
            MainMenu.Options.add(new MenuOption("TalkTo", "Talk To", "Radial_Button_Blue", "media/ui/Radial_Button_Talk_White.png"));
            if(bDoGroupOptions)
            {
                MenuOption orders = new MenuOption("Orders", "Orders", "Radial_Button_Blue", "media/ui/Radial_Button_Orders_White.png");
                orders.SubMenu = new Menu(MainMenu);
                orders.SubMenu.Options.add(new MenuOption("Follow", "Follow", "Radial_Button_Blue", "media/ui/Radial_Button_Follow_Blue.png"));
                orders.SubMenu.Options.add(new MenuOption("Guard", "Guard", "Radial_Button_Blue", "media/ui/Radial_Button_Guard_Blue.png"));
                orders.SubMenu.Options.add(new MenuOption("Stay", "Stay", "Radial_Button_Blue", "media/ui/Radial_Button_Stay_Blue.png"));
                MainMenu.Options.add(orders);
            } else
            {
                MainMenu.Options.add(new MenuOption("TeamUp", "Team Up", "Radial_Button_Blue", "media/ui/Radial_Button_Follow_Blue.png"));
            }
        }
        BuildMenuFromCurrentMenu();
    }

    public IsoObject RadialTarget;
    public Menu MainMenu;
    public Menu CurrentMenu;
    public Stack OptionButtons;
    public HUDButton BackButton;


}
