// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 13.08.2012 13:54:46
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   LuaState.java

package zombie.ai.states;

import zombie.Lua.LuaEventManager;
import zombie.SoundManager;
import zombie.ai.State;
import zombie.characters.IsoGameCharacter;

public class LuaState extends State
{

    public LuaState()
    {
    }

    public static LuaState instance()
    {
        return _instance;
    }

    public void execute(IsoGameCharacter owner)
    {
        LuaEventManager.TriggerEvent("OnAIStateExecute", owner);
    }

    public void enter(IsoGameCharacter owner)
    {
        LuaEventManager.TriggerEvent("OnAIStateEnter", owner);
    }

    public void exit(IsoGameCharacter owner)
    {
        LuaEventManager.TriggerEvent("OnAIStateExit", owner);
    }

    void calculate()
    {
        SoundManager.instance.update3();
    }

    static LuaState _instance = new LuaState();

}