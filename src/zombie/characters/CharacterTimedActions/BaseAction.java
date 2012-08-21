// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   BaseAction.java

package zombie.characters.CharacterTimedActions;

import zombie.Lua.LuaEventManager;
import zombie.SoundManager;
import zombie.ai.StateMachine;
import zombie.ai.states.SwipeState;
import zombie.ai.states.SwipeStatePlayer;
import zombie.characters.IsoGameCharacter;
import zombie.characters.IsoPlayer;
import zombie.openal.Audio;
import zombie.ui.ActionProgressBar;
import zombie.ui.UIManager;

public class BaseAction
{

    public BaseAction(IsoGameCharacter chr)
    {
        SoundEffect = null;
        CurrentTime = 0;
        MaxTime = 60;
        UseProgressBar = true;
        StopOnWalk = true;
        StopOnRun = true;
        this.chr = chr;
    }

    public void PlayLoopedSoundTillComplete(String name, int radius, float maxGain)
    {
        SoundEffect = SoundManager.instance.PlayWorldSound(name, true, chr.getCurrentSquare(), 0.2F, radius, maxGain, true);
    }

    public void update()
    {
        if(chr.getStateMachine().getCurrent() == SwipeState.instance())
            chr.getStateMachine().changeState(chr.getDefaultState());
        if(chr.getStateMachine().getCurrent() == SwipeStatePlayer.instance())
            chr.getStateMachine().changeState(chr.getDefaultState());
        CurrentTime++;
        if(UseProgressBar && chr == IsoPlayer.getInstance())
        {
            UIManager.getProgressBar().setValue((float)CurrentTime / (float)MaxTime);
            if(CurrentTime > MaxTime)
                UIManager.getProgressBar().setValue(1.0F);
        }
    }

    public void start()
    {
    }

    public void stop()
    {
        if(SoundEffect != null)
        {
            SoundManager.instance.StopSound(SoundEffect);
            SoundEffect = null;
        }
        LuaEventManager.TriggerEvent("OnPlayerCancelTimedAction", this);
    }

    public boolean valid()
    {
        return true;
    }

    public boolean finished()
    {
        return CurrentTime >= MaxTime;
    }

    public void perform()
    {
    }

    public Audio SoundEffect;
    public int CurrentTime;
    public int MaxTime;
    public boolean UseProgressBar;
    public IsoGameCharacter chr;
    public boolean StopOnWalk;
    public boolean StopOnRun;
}
