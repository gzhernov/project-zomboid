// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   IsoSprite.java

package zombie.iso.sprite;

import gnu.trove.map.hash.THashMap;
import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import zombie.*;
import zombie.core.Collections.NulledArrayList;
import zombie.core.properties.PropertyContainer;
import zombie.core.textures.*;
import zombie.iso.*;
import zombie.iso.SpriteDetails.IsoObjectType;
import zombie.ui.SpeedControls;
import zombie.ui.UIManager;

// Referenced classes of package zombie.iso.sprite:
//            IsoSpriteInstance, IsoAnim, IsoDirectionFrame, IsoSpriteManager

public class IsoSprite
{

    public IsoSpriteInstance newInstance()
    {
        return new IsoSpriteInstance(this);
    }

    public PropertyContainer getProperties()
    {
        return Properties;
    }

    public void setProperties(PropertyContainer Properties)
    {
        this.Properties = Properties;
    }

    public String getParentObjectName()
    {
        return parentObjectName;
    }

    public IsoSprite()
    {
        Animate = true;
        CurrentAnim = null;
        DeleteWhenFinished = false;
        sprOffX = 0;
        Loop = true;
        lsx = -1F;
        lsy = -1F;
        soffX = 0;
        soffY = 0;
        Properties = new PropertyContainer();
        TintMod = new ColorInfo(1.0F, 1.0F, 1.0F, 1.0F);
        AnimMap = new THashMap(2);
        AnimStack = new NulledArrayList(1);
        lx = -1F;
        ly = -1F;
        lz = -1F;
        Angle = 0.0F;
        parentObjectName = null;
        type = IsoObjectType.MAX;
        if(IsoWorld.instance.CurrentCell != null)
            parentManager = IsoWorld.instance.CurrentCell.SpriteManager;
        def = new IsoSpriteInstance(this);
    }

    IsoSprite(IsoSpriteManager manager, Texture tex)
    {
        Animate = true;
        CurrentAnim = null;
        DeleteWhenFinished = false;
        sprOffX = 0;
        Loop = true;
        lsx = -1F;
        lsy = -1F;
        soffX = 0;
        soffY = 0;
        Properties = new PropertyContainer();
        TintMod = new ColorInfo(1.0F, 1.0F, 1.0F, 1.0F);
        AnimMap = new THashMap(2);
        AnimStack = new NulledArrayList(1);
        lx = -1F;
        ly = -1F;
        lz = -1F;
        Angle = 0.0F;
        parentObjectName = null;
        type = IsoObjectType.MAX;
        parentManager = manager;
        def = new IsoSpriteInstance(this);
        name = tex.getName();
        CurrentAnim = new IsoAnim();
        CurrentAnim.Frames.add(new IsoDirectionFrame(tex));
        AnimMap.put("default", CurrentAnim);
        CurrentAnim.ID = AnimStack.size();
        AnimStack.add(CurrentAnim);
        ID = manager.ID++;
        manager.IntMap.put(Integer.valueOf(ID), this);
    }

    IsoSprite(IsoSpriteManager manager, int spriteID)
    {
        Animate = true;
        CurrentAnim = null;
        DeleteWhenFinished = false;
        sprOffX = 0;
        Loop = true;
        lsx = -1F;
        lsy = -1F;
        soffX = 0;
        soffY = 0;
        Properties = new PropertyContainer();
        TintMod = new ColorInfo(1.0F, 1.0F, 1.0F, 1.0F);
        AnimMap = new THashMap(2);
        AnimStack = new NulledArrayList(1);
        lx = -1F;
        ly = -1F;
        lz = -1F;
        Angle = 0.0F;
        parentObjectName = null;
        type = IsoObjectType.MAX;
        parentManager = manager;
        manager.SpriteMap.put(Integer.valueOf(spriteID), this);
        def = new IsoSpriteInstance(this);
    }

    public void save(DataOutputStream output)
        throws IOException
    {
        GameWindow.WriteString(output, name);
    }

    public void load(DataInputStream input)
        throws IOException
    {
        name = GameWindow.ReadString(input);
        LoadFramesNoDirPageSimple(name);
    }

    IsoSprite(IsoSpriteManager manager, int id, Texture tex)
    {
        Animate = true;
        CurrentAnim = null;
        DeleteWhenFinished = false;
        sprOffX = 0;
        Loop = true;
        lsx = -1F;
        lsy = -1F;
        soffX = 0;
        soffY = 0;
        Properties = new PropertyContainer();
        TintMod = new ColorInfo(1.0F, 1.0F, 1.0F, 1.0F);
        AnimMap = new THashMap(2);
        AnimStack = new NulledArrayList(1);
        lx = -1F;
        ly = -1F;
        lz = -1F;
        Angle = 0.0F;
        parentObjectName = null;
        type = IsoObjectType.MAX;
        parentManager = manager;
        CurrentAnim = new IsoAnim();
        CurrentAnim.Frames.add(new IsoDirectionFrame(tex));
        AnimMap.put("default", CurrentAnim);
        CurrentAnim.ID = AnimStack.size();
        AnimStack.add(CurrentAnim);
        manager.SpriteMap.put(Integer.valueOf(id), this);
    }

    public IsoSprite(IsoSpriteManager manager)
    {
        Animate = true;
        CurrentAnim = null;
        DeleteWhenFinished = false;
        sprOffX = 0;
        Loop = true;
        lsx = -1F;
        lsy = -1F;
        soffX = 0;
        soffY = 0;
        Properties = new PropertyContainer();
        TintMod = new ColorInfo(1.0F, 1.0F, 1.0F, 1.0F);
        AnimMap = new THashMap(2);
        AnimStack = new NulledArrayList(1);
        lx = -1F;
        ly = -1F;
        lz = -1F;
        Angle = 0.0F;
        parentObjectName = null;
        type = IsoObjectType.MAX;
        parentManager = manager;
        CurrentAnim = new IsoAnim();
        AnimMap.put("default", CurrentAnim);
        CurrentAnim.ID = AnimStack.size();
        AnimStack.add(CurrentAnim);
    }

    public static IsoSprite CreateSprite(IsoSpriteManager manager)
    {
        IsoSprite spr = new IsoSprite(manager, spriteID++);
        return spr;
    }

    public static IsoSprite getSprite(IsoSpriteManager manager, int id)
    {
        if(id > maxgid)
            maxgid = id;
        if(manager.IntMap.containsKey(Integer.valueOf(id)))
            return (IsoSprite)manager.IntMap.get(Integer.valueOf(id));
        else
            return null;
    }

    public static IsoSprite getSprite(IsoSpriteManager manager, IsoSprite spr, int offset)
    {
        if(spr.name.contains("_"))
        {
            int id = Integer.parseInt(spr.name.split("_")[1].trim());
            id += offset;
            return (IsoSprite)manager.NamedMap.get((new StringBuilder()).append(spr.name.split("_")[0]).append("_").append(id).toString());
        } else
        {
            return null;
        }
    }

    public static IsoSprite getSprite(IsoSpriteManager manager, String name, int offset)
    {
        IsoSprite spr = (IsoSprite)manager.NamedMap.get(name);
        if(spr.name.contains("_"))
        {
            int id = Integer.parseInt(spr.name.split("_")[1].trim());
            id += offset;
            return (IsoSprite)manager.NamedMap.get((new StringBuilder()).append(spr.name.split("_")[0]).append("_").append(id).toString());
        } else
        {
            return null;
        }
    }

    public static IsoSprite getSprite(IsoSpriteManager manager, Texture tex)
    {
        if(manager.IntMap.containsKey(Integer.valueOf(tex.getID())))
            return (IsoSprite)manager.IntMap.get(Integer.valueOf(tex.getID()));
        else
            return new IsoSprite(manager, tex);
    }

    public static IsoSprite getSprite(IsoSpriteManager manager, int gid, Texture tex)
    {
        if(manager.IntMap.containsKey(Integer.valueOf(gid)))
            return (IsoSprite)manager.IntMap.get(Integer.valueOf(gid));
        if(gid > maxgid)
            maxgid = gid;
        return new IsoSprite(manager, gid, tex);
    }

    public static IsoSprite getSpriteCopy(IsoSpriteManager manager, int id)
    {
        if(manager.IntMap.containsKey(Integer.valueOf(id)))
        {
            maxgid++;
            IsoSprite spr = new IsoSprite(manager, spriteID++);
            IsoSprite ot = (IsoSprite)manager.IntMap.get(Integer.valueOf(id));
            spr.AnimMap = ot.AnimMap;
            spr.CurrentAnim = ot.CurrentAnim;
            return spr;
        } else
        {
            return null;
        }
    }

    public static IsoSprite getSpriteForceCreate(IsoSpriteManager manager, int id)
    {
        if(id > maxgid)
            maxgid = id;
        if(manager.SpriteMap.containsKey(Integer.valueOf(id)))
        {
            return (IsoSprite)manager.SpriteMap.get(Integer.valueOf(id));
        } else
        {
            IsoSprite spr = new IsoSprite(manager);
            manager.SpriteMap.put(Integer.valueOf(id), spr);
            return (IsoSprite)manager.SpriteMap.get(Integer.valueOf(id));
        }
    }

    public void Dispose()
    {
        IsoAnim anim;
        for(Iterator i$ = AnimMap.values().iterator(); i$.hasNext(); anim.Dispose())
            anim = (IsoAnim)i$.next();

        AnimMap.clear();
        AnimStack.clear();
        CurrentAnim = null;
    }

    public boolean isMaskClicked(IsoDirections dir, int x, int y)
    {
        Texture image;
        image = ((IsoDirectionFrame)CurrentAnim.Frames.get((int)def.Frame)).directions[dir.index()];
        if(image == null)
            return false;
        Mask mask;
        mask = image.getMask();
        if(mask == null)
            return true;
        x = (int)((float)x - image.offsetX);
        y = (int)((float)y - image.offsetY);
        if(x < 0 || y < 0)
            return false;
        if(mask.mask.length <= x)
            return false;
        if(mask.mask[0].length <= y)
            return false;
        try
        {
            return mask.mask[x][y];
        }
        catch(Exception ex)
        {
            Logger.getLogger(GameApplet.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }

    public boolean isMaskClicked(IsoDirections dir, int x, int y, boolean flip)
    {
        if(def == null)
            def = new IsoSpriteInstance(this);
        if(def.Frame >= (float)CurrentAnim.Frames.size())
            return false;
        Texture image;
        image = ((IsoDirectionFrame)CurrentAnim.Frames.get((int)def.Frame)).directions[dir.index()];
        if(image == null)
            return false;
        Mask mask;
        mask = image.getMask();
        if(mask == null)
            return true;
        if(flip)
        {
            x = (int)((float)x - ((float)(image.getWidthOrig() - image.getWidth()) - image.offsetX));
            y = (int)((float)y - image.offsetY);
            x = image.getWidth() - x;
        } else
        {
            x = (int)((float)x - image.offsetX);
            y = (int)((float)y - image.offsetY);
            x = image.getWidth() - x;
        }
        if(x < 0 || y < 0)
            return false;
        if(mask.mask.length <= x)
            return false;
        if(mask.mask[0].length <= y)
            return false;
        try
        {
            return mask.mask[x][y];
        }
        catch(Exception ex)
        {
            Logger.getLogger(GameApplet.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }

    public float getMaskClickedY(IsoDirections dir, int x, int y, boolean flip)
    {
        Texture image;
        image = ((IsoDirectionFrame)CurrentAnim.Frames.get((int)def.Frame)).directions[dir.index()];
        if(image == null)
            return 10000F;
        Mask mask = image.getMask();
        if(mask == null)
            return 10000F;
        try
        {
            if(flip)
            {
                x = (int)((float)x - ((float)(image.getWidthOrig() - image.getWidth()) - image.offsetX));
                y = (int)((float)y - image.offsetY);
                x = image.getWidth() - x;
            } else
            {
                x = (int)((float)x - image.offsetX);
                y = (int)((float)y - image.offsetY);
                x = image.getWidth() - x;
            }
            return (float)y;
        }
        catch(Exception ex)
        {
            Logger.getLogger(GameApplet.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 10000F;
    }

    public void LoadFrameExplicit(String ObjectName)
    {
        CurrentAnim = new IsoAnim();
        AnimMap.put("Default", CurrentAnim);
        CurrentAnim.ID = AnimStack.size();
        AnimStack.add(CurrentAnim);
        CurrentAnim.LoadFrameExplicit(ObjectName);
    }

    public void LoadFrames(String ObjectName, String AnimName, int nFrames)
    {
        CurrentAnim = new IsoAnim();
        AnimMap.put(AnimName, CurrentAnim);
        CurrentAnim.ID = AnimStack.size();
        AnimStack.add(CurrentAnim);
        CurrentAnim.LoadFrames(ObjectName, AnimName, nFrames);
    }

    public void DupeFrame()
    {
        CurrentAnim.DupeFrame();
    }

    public void LoadExtraFrame(String ObjectName, String AnimName, int i)
    {
        CurrentAnim.LoadExtraFrame(ObjectName, AnimName, i);
    }

    public void LoadFramesBits(String ObjectName, String Variant, String AnimName, int nFrames)
    {
        CurrentAnim = new IsoAnim();
        AnimMap.put(AnimName, CurrentAnim);
        CurrentAnim.ID = AnimStack.size();
        AnimStack.add(CurrentAnim);
        CurrentAnim.LoadFramesBits(ObjectName, Variant, AnimName, nFrames);
    }

    public void LoadFramesUseOtherFrame(String ObjectName, String Variant, String AnimName, String OtherAnimName, int nOtherFrameFrame, String pal)
    {
        CurrentAnim = new IsoAnim();
        AnimMap.put(AnimName, CurrentAnim);
        CurrentAnim.ID = AnimStack.size();
        AnimStack.add(CurrentAnim);
        CurrentAnim.LoadFramesUseOtherFrame(ObjectName, Variant, AnimName, OtherAnimName, nOtherFrameFrame, pal);
    }

    public void AddFramesUseOtherFrame(String ObjectName, String Variant, String AnimName, String OtherAnimName, int nOtherFrameFrame, String pal)
    {
        CurrentAnim.LoadFramesUseOtherFrame(ObjectName, Variant, AnimName, OtherAnimName, nOtherFrameFrame, pal);
    }

    public void LoadFramesBits(String ObjectName, String Variant, String AnimName, int nFrames, String pal)
    {
        CurrentAnim = new IsoAnim();
        AnimMap.put(AnimName, CurrentAnim);
        CurrentAnim.ID = AnimStack.size();
        AnimStack.add(CurrentAnim);
        CurrentAnim.LoadFramesBits(ObjectName, Variant, AnimName, nFrames, pal);
    }

    public void LoadFramesBits(String ObjectName, String AnimName, int nFrames)
    {
        CurrentAnim = new IsoAnim();
        AnimMap.put(AnimName, CurrentAnim);
        CurrentAnim.ID = AnimStack.size();
        AnimStack.add(CurrentAnim);
        CurrentAnim.LoadFramesBits(ObjectName, AnimName, nFrames);
    }

    public void LoadFramesBitRepeatFrame(String ObjectName, String Variant, String AnimName, int FrameToAdd, String pal)
    {
        CurrentAnim = (IsoAnim)AnimMap.get(AnimName);
    }

    public void LoadFramesNoDir(String ObjectName, String AnimName, int nFrames)
    {
        CurrentAnim = new IsoAnim();
        AnimMap.put(AnimName, CurrentAnim);
        CurrentAnim.ID = AnimStack.size();
        AnimStack.add(CurrentAnim);
        CurrentAnim.LoadFramesNoDir(ObjectName, AnimName, nFrames);
    }

    public void LoadFramesNoDirPage(String ObjectName, String AnimName, int nFrames)
    {
        CurrentAnim = new IsoAnim();
        AnimMap.put(AnimName, CurrentAnim);
        CurrentAnim.ID = AnimStack.size();
        AnimStack.add(CurrentAnim);
        CurrentAnim.LoadFramesNoDirPage(ObjectName, AnimName, nFrames);
    }

    public void LoadFramesNoDirPageSimple(String ObjectName)
    {
        CurrentAnim = new IsoAnim();
        AnimMap.put("default", CurrentAnim);
        CurrentAnim.ID = AnimStack.size();
        AnimStack.add(CurrentAnim);
        CurrentAnim.LoadFramesNoDirPage(ObjectName);
    }

    public void LoadFramesPageSimple(String NObjectName, String SObjectName, String EObjectName, String WObjectName)
    {
        CurrentAnim = new IsoAnim();
        AnimMap.put("default", CurrentAnim);
        CurrentAnim.ID = AnimStack.size();
        AnimStack.add(CurrentAnim);
        CurrentAnim.LoadFramesPageSimple(NObjectName, SObjectName, EObjectName, WObjectName);
    }

    public void LoadFramesNoDirPalette(String ObjectName, String AnimName, int nFrames, String Palette)
    {
        CurrentAnim = new IsoAnim();
        AnimMap.put(AnimName, CurrentAnim);
        CurrentAnim.ID = AnimStack.size();
        AnimStack.add(CurrentAnim);
        CurrentAnim.LoadFramesNoDirPalette(ObjectName, AnimName, nFrames, Palette);
    }

    public void LoadFramesPalette(String ObjectName, String AnimName, int nFrames, zombie.core.textures.PaletteManager.PaletteInfo info)
    {
        CurrentAnim = new IsoAnim();
        AnimMap.put(AnimName, CurrentAnim);
        CurrentAnim.ID = AnimStack.size();
        AnimStack.add(CurrentAnim);
        CurrentAnim.LoadFramesPalette(ObjectName, AnimName, nFrames, info);
    }

    public void LoadFramesPalette(String ObjectName, String AnimName, int nFrames, String Palette)
    {
        CurrentAnim = new IsoAnim();
        AnimMap.put(AnimName, CurrentAnim);
        CurrentAnim.ID = AnimStack.size();
        AnimStack.add(CurrentAnim);
        CurrentAnim.LoadFramesPalette(ObjectName, AnimName, nFrames, Palette);
    }

    public void LoadFramesPcx(String ObjectName, String AnimName, int nFrames)
    {
        CurrentAnim = new IsoAnim();
        AnimMap.put(AnimName, CurrentAnim);
        CurrentAnim.ID = AnimStack.size();
        AnimStack.add(CurrentAnim);
        CurrentAnim.LoadFramesPcx(ObjectName, AnimName, nFrames);
    }

    public void PlayAnimNoReset(String name)
    {
        if(AnimMap.containsKey(name) && (CurrentAnim == null || !CurrentAnim.name.equals(name)))
            CurrentAnim = (IsoAnim)AnimMap.get(name);
    }

    public void PlayAnim(IsoAnim anim)
    {
        if(CurrentAnim == null || CurrentAnim != anim)
            CurrentAnim = anim;
    }

    public void PlayAnim(String name)
    {
        if((CurrentAnim == null || !CurrentAnim.name.equals(name)) && AnimMap.containsKey(name))
            CurrentAnim = (IsoAnim)AnimMap.get(name);
    }

    public void PlayAnimUnlooped(String name)
    {
        if(AnimMap.containsKey(name))
        {
            if(CurrentAnim == null || !CurrentAnim.name.equals(name))
                CurrentAnim = (IsoAnim)AnimMap.get(name);
            CurrentAnim.looped = false;
        }
    }

    public void ChangeTintMod(ColorInfo NewTintMod)
    {
        TintMod.r = NewTintMod.r;
        TintMod.g = NewTintMod.g;
        TintMod.b = NewTintMod.b;
        TintMod.a = NewTintMod.a;
    }

    public void RenderGhostTile(int x, int y, int z)
    {
        IndieGL.glBlendFunc(770, 1);
        render(null, null, x, y, z, IsoDirections.N, 0.0F, -144F, IsoGridSquare.getDefColorInfo());
    }

    public void render(IsoObject obj, float x, float y, float z, IsoDirections dir, float offsetX, float offsetY, 
            ColorInfo info2)
    {
        if(def == null)
            def = new IsoSpriteInstance(this);
        render(def, obj, x, y, z, dir, offsetX, offsetY, info2);
    }

    public float getScreenY(float x, float y, float z, float offsetX, float offsetY)
    {
        float sy = 0.0F;
        if(lx != x || ly != y || lz != z)
        {
            sy = IsoUtils.YToScreen(x + def.offX, y + def.offY, z + def.offZ, 0);
            lsy = sy;
        } else
        {
            sy = lsy;
        }
        return sy;
    }

    public float getScreenX(float x, float y, float z, float offsetX, float offsetY)
    {
        float sx = 0.0F;
        if(lx != x || ly != y || lz != z)
        {
            sx = IsoUtils.XToScreen(x + def.offX, y + def.offY, z + def.offZ, 0);
            lsx = sx;
        } else
        {
            sx = lsx;
        }
        return sx;
    }

    public void render(IsoSpriteInstance inst, IsoObject obj, float x, float y, float z, IsoDirections dir, float offsetX, 
            float offsetY, ColorInfo info2)
    {
        if(CurrentAnim == null || CurrentAnim.Frames.isEmpty())
            return;
        info.r = info2.r;
        info.g = info2.g;
        info.b = info2.b;
        info.a = info2.a;
        float frame;
        float sx;
        float sy;
        frame = inst.Frame;
        if(inst.Frame >= (float)CurrentAnim.Frames.size())
            frame = CurrentAnim.Frames.size() - 1;
        if(inst.Frame < 0.0F)
        {
            inst.Frame = 0.0F;
            frame = 0.0F;
        }
        if(inst != null)
            inst.renderprep(obj);
        sx = 0.0F;
        sy = 0.0F;
        if(lx != x || ly != y || lz != z)
        {
            sx = IsoUtils.XToScreen(x + inst.offX, y + inst.offY, z + inst.offZ, 0);
            sy = IsoUtils.YToScreen(x + inst.offX, y + inst.offY, z + inst.offZ, 0);
            lsx = sx;
            lsy = sy;
        } else
        {
            sx = lsx;
            sy = lsy;
        }
        lx = x;
        ly = y;
        lz = z;
        if(CurrentAnim == null)
            return;
        try
        {
            sx = (int)sx;
            sy = (int)sy;
            sx -= offsetX;
            sy -= offsetY;
            sx -= (int)IsoCamera.getOffX();
            sy -= (int)IsoCamera.getOffY();
            sy += 128F;
            sx += soffX;
            sy += soffY;
            sx = (int)sx;
            sy = (int)sy;
            if((obj instanceof IsoMovingObject) && CurrentAnim != null && ((IsoDirectionFrame)CurrentAnim.Frames.get((int)frame)).getTexture(dir) != null)
            {
                sx -= ((IsoDirectionFrame)CurrentAnim.Frames.get((int)frame)).getTexture(dir).getWidthOrig() / 2;
                sy -= ((IsoDirectionFrame)CurrentAnim.Frames.get((int)frame)).getTexture(dir).getHeightOrig();
            }
            float lr = info.r;
            float lg = info.g;
            float lb = info.b;
            if(inst != null)
            {
                if(inst.tintr != 1.0F || inst.tintg != 1.0F || inst.tintb != 1.0F)
                {
                    info.r *= inst.tintr;
                    info.g *= inst.tintg;
                    info.b *= inst.tintb;
                }
                info.a = inst.alpha;
            }
            if(TintMod.r != 1.0F || TintMod.g != 1.0F || TintMod.b != 1.0F)
            {
                info.r *= TintMod.r;
                info.g *= TintMod.g;
                info.b *= TintMod.b;
            }
            int n;
            if((int)frame < CurrentAnim.Frames.size())
                ((IsoDirectionFrame)CurrentAnim.Frames.get((int)frame)).render((int)sx, (int)sy, dir, info, inst.Flip, Angle);
            else
                n = 0;
            info.r = lr;
            info.g = lg;
            info.b = lb;
        }
        catch(Exception ex)
        {
            IndieGL.End();
            Logger.getLogger(GameApplet.class.getName()).log(Level.SEVERE, null, ex);
        }
        return;
    }

    public void renderObjectPicker(IsoObject obj, float x, float y, float z, IsoDirections dir, float offsetX, float offsetY, 
            ColorInfo info)
    {
        if(def == null)
            def = new IsoSpriteInstance(this);
        if(CurrentAnim == null || CurrentAnim.Frames.isEmpty())
            return;
        if(CurrentAnim.Frames.size() == 0)
            return;
        if(def.Frame >= (float)CurrentAnim.Frames.size())
            def.Frame = 0.0F;
        if(((IsoDirectionFrame)CurrentAnim.Frames.get((int)def.Frame)).getTexture(dir) == null)
            return;
        float sx = 0.0F;
        float sy = 0.0F;
        if(lx != x || ly != y || lz != z)
        {
            sx = IsoUtils.XToScreen(x + def.offX, y, z + def.offZ, 0);
            sy = IsoUtils.YToScreen(x + def.offX, y, z + def.offZ, 0);
            lsx = sx;
            lsy = sy;
        } else
        {
            sx = lsx;
            sy = lsy;
        }
        lx = x;
        ly = y;
        lz = z;
        if(CurrentAnim == null)
            return;
        sx = (int)sx;
        sy = (int)sy;
        sx -= offsetX;
        sy -= offsetY;
        sx -= (int)IsoCamera.getOffX();
        sy -= (int)IsoCamera.getOffY();
        sy += 128F;
        if(obj instanceof IsoMovingObject)
        {
            sx -= ((IsoDirectionFrame)CurrentAnim.Frames.get((int)def.Frame)).getTexture(dir).getWidthOrig() / 2;
            sy -= ((IsoDirectionFrame)CurrentAnim.Frames.get((int)def.Frame)).getTexture(dir).getHeightOrig();
        }
        if(def.Frame < (float)CurrentAnim.Frames.size())
        {
            Texture tex = ((IsoDirectionFrame)CurrentAnim.Frames.get((int)def.Frame)).getTexture(dir);
            if(obj != null)
            {
                boolean flip = dir == IsoDirections.W || dir == IsoDirections.SW || dir == IsoDirections.S;
                if(def.Flip)
                    flip = !flip;
                IsoObjectPicker.Instance.Add((int)sx, (int)sy, tex.getWidthOrig(), tex.getHeightOrig(), obj.square, obj, flip);
            }
        }
    }

    public void update()
    {
        update(def);
    }

    public void update(IsoSpriteInstance def)
    {
        if(def == null)
            def = new IsoSpriteInstance(this);
        if(CurrentAnim == null)
            return;
        if(Animate && !def.Finished)
        {
            if(UIManager.getSpeedControls().getCurrentGameSpeed() > 0)
                def.Frame += def.AnimFrameIncrease * (GameTime.instance.getMultipliedSecondsSinceLastUpdate() * 60F);
            int n;
            if(CurrentAnim.name.equals("ZombieDeath") && def.Frame >= (float)CurrentAnim.Frames.size())
                n = 0;
            if((int)def.Frame >= CurrentAnim.Frames.size() && Loop && def.Looped)
                def.Frame = 0.0F;
            if((int)def.Frame >= CurrentAnim.Frames.size() && (!Loop || !def.Looped))
            {
                def.Finished = true;
                def.Frame = CurrentAnim.FinishUnloopedOnFrame;
                if(DeleteWhenFinished)
                {
                    Dispose();
                    Animate = false;
                }
            }
        }
    }

    public void CacheAnims(String key)
    {
        Stack animList = new Stack();
        for(int n = 0; n < AnimStack.size(); n++)
        {
            IsoAnim anim = (IsoAnim)AnimStack.get(n);
            String total = (new StringBuilder()).append(key).append(anim.name).toString();
            animList.add(total);
            if(!IsoAnim.GlobalAnimMap.containsKey(total))
                IsoAnim.GlobalAnimMap.put(total, anim);
        }

        AnimNameSet.put(key, ((Object) (animList.toArray())));
    }

    public static boolean HasCache(String string)
    {
        return AnimNameSet.containsKey(string);
    }

    public void LoadCache(String string)
    {
        Object arr[] = (Object[])AnimNameSet.get(string);
        for(int n = 0; n < arr.length; n++)
        {
            String s = (String)arr[n];
            IsoAnim a = (IsoAnim)IsoAnim.GlobalAnimMap.get(s);
            AnimMap.put(a.name, a);
            AnimStack.add(a);
            CurrentAnim = a;
        }

    }

    public void setName(String string)
    {
        name = string;
    }

    public void setParentObjectName(String val)
    {
        parentObjectName = val;
    }

    public IsoObjectType getType()
    {
        return type;
    }

    public void setType(IsoObjectType ntype)
    {
        type = ntype;
    }

    public void AddProperties(IsoSprite sprite)
    {
        getProperties().AddProperties(sprite.getProperties());
    }

    public static int maxCount = 0;
    public static int spriteID = 0xf4240;
    public static int maxgid = 0;
    public static float alphaStep = 0.05F;
    public boolean Animate;
    public IsoAnim CurrentAnim;
    public boolean DeleteWhenFinished;
    public short sprOffX;
    public boolean Loop;
    public float lsx;
    public float lsy;
    public short soffX;
    public short soffY;
    public PropertyContainer Properties;
    public ColorInfo TintMod;
    THashMap AnimMap;
    public NulledArrayList AnimStack;
    float lx;
    float ly;
    float lz;
    IsoSpriteManager parentManager;
    public float Angle;
    public String name;
    private String parentObjectName;
    public boolean Scissor;
    public int ID;
    public IsoSpriteInstance def;
    static ColorInfo info = new ColorInfo();
    static THashMap AnimNameSet = new THashMap();
    IsoObjectType type;

}
