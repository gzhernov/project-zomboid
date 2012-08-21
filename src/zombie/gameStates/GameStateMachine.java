// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   GameStateMachine.java

package zombie.gameStates;

import java.util.ArrayList;

// Referenced classes of package zombie.gameStates:
//            GameState

public class GameStateMachine
{
	/*    */   public static enum StateAction
	/*    */   {
	/* 21 */     Continue, 
	/* 22 */     Remain;
	/*    */   }


    public GameStateMachine()
    {
        firstrun = true;
        Loop = true;
        StateIndex = 0;
        LoopToState = 0;
        States = new ArrayList();
        current = null;
    }

    public void render()
    {
        if(current != null)
            current.render();
    }

    public void update()
    {
        if(firstrun)
        {
            if(current == null)
                current = (GameState)States.get(StateIndex);
            current.enter();
            firstrun = false;
        }
        if(current == null)
            if(Loop)
            {
                StateIndex = LoopToState;
                current = (GameState)States.get(StateIndex);
                if(StateIndex < States.size())
                    current.enter();
            } else
            {
                return;
            }
        if(current != null && current.update() == StateAction.Continue)
        {
            current.exit();
            GameState next = current.redirectState();
            if(next == null)
            {
                StateIndex++;
                if(StateIndex < States.size())
                {
                    current = (GameState)States.get(StateIndex);
                    current.enter();
                } else
                {
                    current = null;
                }
            } else
            {
                next.enter();
                current = next;
            }
        }
    }

    public boolean firstrun;
    public boolean Loop;
    public int StateIndex;
    public int LoopToState;
    public ArrayList States;
    GameState current;
}
