// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   GameState.java

package zombie.gameStates;


// Referenced classes of package zombie.gameStates:
//            GameStateMachine

public class GameState
{

    public GameState()
    {
    }

    public void enter()
    {
    }

    public void exit()
    {
    }

    public void render()
    {
    }

    public GameState redirectState()
    {
        return null;
    }

    public GameStateMachine.StateAction update()
    {
        return GameStateMachine.StateAction.Continue;
    }
}
