// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   IsoJukebox.java

package zombie.iso.objects;

import zombie.SoundManager;
import zombie.WorldSoundManager;
import zombie.characters.IsoPlayer;
import zombie.core.Core;
import zombie.core.Rand;
import zombie.gameStates.IngameState;
import zombie.iso.*;
import zombie.iso.sprite.IsoSprite;
import zombie.openal.Audio;

public class IsoJukebox extends IsoObject
{

    public IsoJukebox(IsoCell cell, IsoGridSquare sq, IsoSprite spr)
    {
        super(cell, sq, spr);
        JukeboxTrack = null;
        IsPlaying = false;
        MusicRadius = 30F;
        Activated = false;
        WorldSoundPulseRate = 150;
        WorldSoundPulseDelay = 0;
    }

    public String getObjectName()
    {
        return "Jukebox";
    }

    public IsoJukebox(IsoCell cell)
    {
        super(cell);
        JukeboxTrack = null;
        IsPlaying = false;
        MusicRadius = 30F;
        Activated = false;
        WorldSoundPulseRate = 150;
        WorldSoundPulseDelay = 0;
    }

    public IsoJukebox(IsoCell cell, IsoGridSquare sq, String gid)
    {
        super(cell, sq, gid);
        JukeboxTrack = null;
        IsPlaying = false;
        MusicRadius = 30F;
        Activated = false;
        WorldSoundPulseRate = 150;
        WorldSoundPulseDelay = 0;
        JukeboxTrack = null;
        IsPlaying = false;
        Activated = false;
        WorldSoundPulseDelay = 0;
    }

    public void SetPlaying(boolean ShouldPlay)
    {
        if(IsPlaying == ShouldPlay)
            return;
        IsPlaying = ShouldPlay;
        if(IsPlaying && JukeboxTrack == null)
        {
            String Trackname = null;
            switch(Rand.Next(4))
            {
            case 0: // '\0'
                Trackname = "paws1";
                break;

            case 1: // '\001'
                Trackname = "paws2";
                break;

            case 2: // '\002'
                Trackname = "paws3";
                break;

            case 3: // '\003'
                Trackname = "paws4";
                break;
            }
            JukeboxTrack = SoundManager.instance.PlaySound(Trackname, false, 0.0F);
        }
    }

    public boolean onMouseLeftClick(int x, int y)
    {
        if(IsoPlayer.getInstance() == null)
            return false;
        if(IsoPlayer.getInstance().getCurrentSquare() == null)
            return false;
        float DistRatioFromSource = 0.0F;
        int Dist = Math.abs(square.getX() - IsoPlayer.getInstance().getCurrentSquare().getX()) + Math.abs((square.getY() - IsoPlayer.getInstance().getCurrentSquare().getY()) + Math.abs(square.getZ() - IsoPlayer.getInstance().getCurrentSquare().getZ()));
        if(Dist < 4)
            if(!Activated)
            {
                if(Core.NumJukeBoxesActive < Core.MaxJukeBoxesActive)
                {
                    WorldSoundPulseDelay = 0;
                    Activated = true;
                    SetPlaying(true);
                    Core.NumJukeBoxesActive++;
                }
            } else
            {
                WorldSoundPulseDelay = 0;
                SetPlaying(false);
                Activated = false;
                if(JukeboxTrack != null)
                {
                    SoundManager.instance.StopSound(JukeboxTrack);
                    JukeboxTrack.stop();
                    JukeboxTrack = null;
                }
                Core.NumJukeBoxesActive--;
            }
        return true;
    }

    public void update()
    {
        if(IsoPlayer.getInstance() == null)
            return;
        if(IsoPlayer.getInstance().getCurrentSquare() == null)
            return;
        if(Activated)
        {
            float DistRatioFromSource = 0.0F;
            int Dist = Math.abs(square.getX() - IsoPlayer.getInstance().getCurrentSquare().getX()) + Math.abs((square.getY() - IsoPlayer.getInstance().getCurrentSquare().getY()) + Math.abs(square.getZ() - IsoPlayer.getInstance().getCurrentSquare().getZ()));
            if((float)Dist < MusicRadius)
            {
                SetPlaying(true);
                DistRatioFromSource = (MusicRadius - (float)Dist) / MusicRadius;
            }
            if(JukeboxTrack != null)
            {
                float DistRatioFromSourceMod = DistRatioFromSource + 0.2F;
                if(DistRatioFromSourceMod > 1.0F)
                    DistRatioFromSourceMod = 1.0F;
                SoundManager.instance.BlendVolume(JukeboxTrack, DistRatioFromSource);
                SoundManager.instance.BlendVolume(IngameState.instance.musicTrack, (1.0F - DistRatioFromSourceMod) * 0.5F);
                if(WorldSoundPulseDelay > 0)
                    WorldSoundPulseDelay--;
                if(WorldSoundPulseDelay == 0)
                {
                    WorldSoundManager.instance.addSound(IsoPlayer.getInstance(), square.getX(), square.getY(), square.getZ(), 70, 70, true);
                    WorldSoundPulseDelay = WorldSoundPulseRate;
                }
                if(!JukeboxTrack.isPlaying())
                {
                    WorldSoundPulseDelay = 0;
                    SetPlaying(false);
                    Activated = false;
                    if(JukeboxTrack != null)
                    {
                        SoundManager.instance.StopSound(JukeboxTrack);
                        JukeboxTrack.stop();
                        JukeboxTrack = null;
                    }
                    Core.NumJukeBoxesActive--;
                }
            }
        }
    }

    private Audio JukeboxTrack;
    private boolean IsPlaying;
    private float MusicRadius;
    private boolean Activated;
    private int WorldSoundPulseRate;
    private int WorldSoundPulseDelay;
}
