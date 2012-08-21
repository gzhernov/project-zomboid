// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   LoginState.java

package zombie.gameStates;

import zombie.GameWindow;

// Referenced classes of package zombie.gameStates:
//            GameState, GameStateMachine

public class LoginState extends GameState
{

    public LoginState()
    {
    }

    public void enter()
    {
    }

    public GameStateMachine.StateAction update()
    {
        if(LoggedIn && version.equals(GameWindow.version))
            return GameStateMachine.StateAction.Continue;
        else
            return GameStateMachine.StateAction.Remain;
    }

    public static boolean LoggedIn = false;
    public static String version = "0.2.0q";

}
