// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   LoadGamePopupState.java

package zombie.gameStates;

import java.io.File;
import zombie.GameWindow;
import zombie.core.Core;
import zombie.iso.IsoWorld;
import zombie.ui.UIEventHandler;
import zombie.ui.UIManager;

// Referenced classes of package zombie.gameStates:
//            GameState, CharacterCreationState, GameStateMachine

public class LoadGamePopupState extends GameState
    implements UIEventHandler
{

    public LoadGamePopupState()
    {
    }

    public void enter()
    {
        bLoadGame = false;
        bLoadCharacter = false;
        bDone = false;
        if(CheckWorldExists())
            UIManager.DoModal("load", "Do you want to load the saved world?", true, this);
        else
            bDone = true;
    }

    public GameStateMachine.StateAction update()
    {
        if(bDone)
            return GameStateMachine.StateAction.Continue;
        else
            return GameStateMachine.StateAction.Remain;
    }

    public void render()
    {
        Core.getInstance().StartFrame();
        Core.getInstance().EndFrame();
        Core.getInstance().StartFrameUI();
        UIManager.render();
        Core.getInstance().EndFrameUI();
    }

    public void DoubleClick(String s, int i, int j)
    {
    }

    public void ModalClick(String name, String chosen)
    {
        if("char".equals(name))
        {
            if(chosen.equals("Yes"))
                bLoadCharacter = true;
            else
                bLoadCharacter = false;
            bDone = true;
        }
        if("load".equals(name))
            if(chosen.equals("Yes"))
            {
                bLoadGame = true;
                File file = new File((new StringBuilder()).append(GameWindow.getGameModeCacheDir()).append(File.separator).append("map_p.bin").toString());
                if(file.exists())
                {
                    UIManager.DoModal("char", "Do you want to continue as your previous character?", true, this);
                } else
                {
                    bDone = true;
                    bLoadCharacter = false;
                }
            } else
            {
                bLoadGame = false;
                bDone = true;
            }
    }

    public void Selected(String s, int i, int j)
    {
    }

    public GameState redirectState()
    {
        if(!bLoadGame)
        {
            File file = new File((new StringBuilder()).append(GameWindow.getGameModeCacheDir()).append(File.separator).toString());
            removeDirectory(file);
        }
        if((!bLoadGame || !bLoadCharacter) && Core.GameMode.equals("Sandbox"))
        {
            CharacterCreationState st = new CharacterCreationState();
            GameWindow.CharacterCreationStateHandle = st;
            return st;
        } else
        {
            return null;
        }
    }

    public static boolean removeDirectory(File directory)
    {
        if(directory == null)
            return false;
        if(!directory.exists())
            return true;
        if(!directory.isDirectory())
            return false;
        String list[] = directory.list();
        if(list != null)
        {
            for(int i = 0; i < list.length; i++)
            {
                File entry = new File(directory, list[i]);
                if(entry.isDirectory())
                {
                    if(!removeDirectory(entry))
                        return false;
                    continue;
                }
                if(!entry.delete())
                    return false;
            }

        }
        return true;
    }

    private boolean CheckWorldExists()
    {
        String oplayerCell = IsoWorld.instance.playerCell;
        oplayerCell = "5_5";
        File file = new File((new StringBuilder()).append(GameWindow.getGameModeCacheDir()).append(File.separator).append("map_t.bin").toString());
        return file.exists();
    }

    public static boolean bLoadGame = false;
    public static boolean bLoadCharacter = false;
    public static boolean bDone = false;

}
