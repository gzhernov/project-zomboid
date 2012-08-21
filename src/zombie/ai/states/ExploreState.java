// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 13.08.2012 13:54:46
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   ExploreState.java

package zombie.ai.states;

import zombie.SoundManager;
import zombie.ai.State;
import zombie.characters.IsoGameCharacter;
import zombie.core.Rand;

public class ExploreState extends State
{

    public ExploreState()
    {
    }

    public static ExploreState instance()
    {
        return _instance;
    }

    public void execute(IsoGameCharacter owner)
    {
        owner.PathTo(Rand.Next(100), Rand.Next(100), 0, false);
    }

    void calculate()
    {
        SoundManager.instance.update4();
    }

    static ExploreState _instance = new ExploreState();

}