// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   IsoDeadBody.java

package zombie.iso.objects;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import zombie.GameWindow;
import zombie.IndieGL;
import zombie.characters.*;
import zombie.core.Collections.NulledArrayList;
import zombie.core.Color;
import zombie.core.textures.ColorInfo;
import zombie.core.textures.Texture;
import zombie.inventory.InventoryItem;
import zombie.inventory.ItemContainer;
import zombie.iso.*;
import zombie.iso.sprite.*;
import zombie.scripting.ScriptManager;
import zombie.ui.*;

public class IsoDeadBody extends IsoMovingObject
    implements Talker
{

    public String getObjectName()
    {
        return "DeadBody";
    }

    public IsoDeadBody(IsoGameCharacter died)
    {
        super(died.getCell(), false);
        wasZombie = false;
        bUseParts = false;
        SpeakTime = 0;
        updateCount = 15;
        Speaking = false;
        sayLine = "";
        int fgdsf;
        if(died instanceof IsoSurvivor)
            fgdsf = 0;
        if(died == IsoPlayer.instance)
        {
            File file = new File((new StringBuilder()).append(GameWindow.getGameModeCacheDir()).append(File.separator).append("map_p.bin").toString());
            if(file.exists())
                file.delete();
        }
        died.getCurrentSquare().getStaticMovingObjects().add(this);
        if(died instanceof IsoSurvivor)
        {
            IsoWorld.instance.TotalSurvivorNights += ((IsoSurvivor)(IsoSurvivor)died).nightsSurvived;
            IsoWorld.instance.TotalSurvivorsDead++;
            if(IsoWorld.instance.SurvivorSurvivalRecord < ((IsoSurvivor)(IsoSurvivor)died).nightsSurvived)
                IsoWorld.instance.SurvivorSurvivalRecord = ((IsoSurvivor)(IsoSurvivor)died).nightsSurvived;
        }
        wasZombie = died instanceof IsoZombie;
        if(!died.getScriptName().equals("none"))
            ScriptManager.instance.Trigger("OnCharacterDeath", died.getScriptName());
        dir = died.dir;
        Collidable = false;
        ClothingItem_Head = died.getClothingItem_Head();
        ClothingItem_Torso = died.getClothingItem_Torso();
        ClothingItem_Hands = died.getClothingItem_Hands();
        ClothingItem_Legs = died.getClothingItem_Legs();
        ClothingItem_Feet = died.getClothingItem_Feet();
        bUseParts = died.isbUseParts();
        torsoSprite = died.getTorsoSprite();
        legsSprite = died.getLegsSprite();
        headSprite = died.getHeadSprite();
        shoeSprite = died.getShoeSprite();
        topSprite = died.getTopSprite();
        bottomsSprite = died.getBottomsSprite();
        IsoGridSquare sq = died.getCurrentSquare();
        square = sq;
        sprite = died.sprite;
        def = new IsoSpriteInstance(sprite);
        died.sprite = null;
        def.Frame = died.def.Frame;
        def.Flip = died.def.Flip;
        def.Frame = sprite.CurrentAnim.Frames.size() - 1;
        sprite.Animate = false;
        dir = died.dir;
        x = died.getX();
        y = died.getY();
        z = died.getZ();
        nx = x;
        ny = y;
        offsetX = died.offsetX;
        offsetY = died.offsetY;
        solid = false;
        getCell().getRemoveList().add(died);
        shootable = false;
        if(!died.getInventory().Items.isEmpty())
        {
            OutlineOnMouseover = true;
            container = died.getInventory();
        }
        if(died instanceof IsoZombie)
            getCell().getZombieList().remove((IsoZombie)died);
        if(died instanceof IsoSurvivor)
            getCell().getSurvivorList().remove((IsoSurvivor)died);
        died.getCurrentSquare().getMovingObjects().remove(died);
        if(died.getLastSquare() != null)
            died.getLastSquare().getMovingObjects().remove(died);
        sayLine = died.getSayLine();
        SpeakColor = died.getSpeakColour();
        SpeakTime = died.getSpeakTime();
        Speaking = died.isSpeaking();
        died = null;
    }

    public IsoDeadBody(IsoCell cell)
    {
        super(cell);
        wasZombie = false;
        bUseParts = false;
        SpeakTime = 0;
        updateCount = 15;
        Speaking = false;
        sayLine = "";
        SpeakColor = Color.white;
        solid = false;
        shootable = false;
    }

    public void load(ByteBuffer input)
        throws IOException
    {
        super.load(input);
        wasZombie = input.get() == 1;
        legsSprite = IsoWorld.instance.spriteManager.AddSprite(GameWindow.ReadString(input));
        if(input.get() == 1)
            torsoSprite = IsoWorld.instance.spriteManager.AddSprite(GameWindow.ReadString(input));
        if(input.get() == 1)
            headSprite = IsoWorld.instance.spriteManager.AddSprite(GameWindow.ReadString(input));
        if(input.get() == 1)
            bottomsSprite = IsoWorld.instance.spriteManager.AddSprite(GameWindow.ReadString(input));
        if(input.get() == 1)
            shoeSprite = IsoWorld.instance.spriteManager.AddSprite(GameWindow.ReadString(input));
        if(input.get() == 1)
            topSprite = IsoWorld.instance.spriteManager.AddSprite(GameWindow.ReadString(input));
        sprite = legsSprite;
        if(input.get() == 1)
        {
            container = new ItemContainer();
            container.load(input);
            OutlineOnMouseover = true;
        }
    }

    public void save(ByteBuffer output)
        throws IOException
    {
        super.save(output);
        output.put(((byte)(wasZombie ? 1 : 0)));
        legsSprite.name = ((IsoDirectionFrame)legsSprite.CurrentAnim.Frames.get(legsSprite.CurrentAnim.Frames.size() - 1)).getTexture(dir).getName();
        GameWindow.WriteString(output, legsSprite.name);
        if(torsoSprite != null && ((IsoDirectionFrame)torsoSprite.CurrentAnim.Frames.get(legsSprite.CurrentAnim.Frames.size() - 1)).getTexture(dir) != null)
        {
            torsoSprite.name = ((IsoDirectionFrame)torsoSprite.CurrentAnim.Frames.get(legsSprite.CurrentAnim.Frames.size() - 1)).getTexture(dir).getName();
            output.put((byte)1);
            GameWindow.WriteString(output, torsoSprite.name);
        } else
        {
            output.put((byte)0);
        }
        if(headSprite != null && ((IsoDirectionFrame)headSprite.CurrentAnim.Frames.get(legsSprite.CurrentAnim.Frames.size() - 1)).getTexture(dir) != null)
        {
            headSprite.name = ((IsoDirectionFrame)headSprite.CurrentAnim.Frames.get(legsSprite.CurrentAnim.Frames.size() - 1)).getTexture(dir).getName();
            output.put((byte)1);
            GameWindow.WriteString(output, headSprite.name);
        } else
        {
            output.put((byte)0);
        }
        if(bottomsSprite != null && ((IsoDirectionFrame)bottomsSprite.CurrentAnim.Frames.get(legsSprite.CurrentAnim.Frames.size() - 1)).getTexture(dir) != null)
        {
            bottomsSprite.name = ((IsoDirectionFrame)bottomsSprite.CurrentAnim.Frames.get(legsSprite.CurrentAnim.Frames.size() - 1)).getTexture(dir).getName();
            output.put((byte)1);
            GameWindow.WriteString(output, bottomsSprite.name);
        } else
        {
            output.put((byte)0);
        }
        if(shoeSprite != null && ((IsoDirectionFrame)shoeSprite.CurrentAnim.Frames.get(legsSprite.CurrentAnim.Frames.size() - 1)).getTexture(dir) != null)
        {
            shoeSprite.name = ((IsoDirectionFrame)shoeSprite.CurrentAnim.Frames.get(legsSprite.CurrentAnim.Frames.size() - 1)).getTexture(dir).getName();
            output.put((byte)1);
            GameWindow.WriteString(output, shoeSprite.name);
        } else
        {
            output.put((byte)0);
        }
        if(topSprite != null && ((IsoDirectionFrame)topSprite.CurrentAnim.Frames.get(legsSprite.CurrentAnim.Frames.size() - 1)).getTexture(dir) != null)
        {
            topSprite.name = ((IsoDirectionFrame)topSprite.CurrentAnim.Frames.get(legsSprite.CurrentAnim.Frames.size() - 1)).getTexture(dir).getName();
            output.put((byte)1);
            GameWindow.WriteString(output, topSprite.name);
        } else
        {
            output.put((byte)0);
        }
        if(container != null)
        {
            output.put((byte)1);
            container.save(output);
        } else
        {
            output.put((byte)0);
        }
    }

    public void renderlast()
    {
        if(Speaking)
        {
            float sx = sprite.lsx;
            float sy = sprite.lsy;
            sx = (int)sx;
            sy = (int)sy;
            sx -= offsetX;
            sy -= offsetY;
            sx -= (int)IsoCamera.getOffX();
            sy -= (int)IsoCamera.getOffY();
            sx += 8F;
            sy += 32F;
            if(sayLine != null)
            {
                IndieGL.End();
                TextManager.instance.DrawStringCentre(UIFont.Medium, (int)sx, (int)sy, sayLine, SpeakColor.r, SpeakColor.g, SpeakColor.b, SpeakColor.a);
            }
        }
    }

    public void render(float x, float y, float z, ColorInfo col, boolean bDoChild)
    {
        if(def == null)
            def = new IsoSpriteInstance(sprite);
        def.Frame = sprite.CurrentAnim.Frames.size() - 1;
        if(!bUseParts)
        {
            sprite.render(def, this, x, y, z, dir, offsetX, offsetY, col);
        } else
        {
            legsSprite.render(def, this, x, y, z, dir, offsetX, offsetY, col);
            if(torsoSprite != null)
                torsoSprite.render(def, this, x, y, z, dir, offsetX, offsetY, col);
            if(headSprite != null)
                headSprite.render(def, this, x, y, z, dir, offsetX, offsetY, col);
            if(bottomsSprite != null)
                bottomsSprite.render(def, this, x, y, z, dir, offsetX, offsetY, col);
            if(shoeSprite != null)
                shoeSprite.render(def, this, x, y, z, dir, offsetX, offsetY, col);
            if(topSprite != null)
                topSprite.render(def, this, x, y, z, dir, offsetX, offsetY, col);
        }
    }

    public void update()
    {
        if(def == null)
            def = new IsoSpriteInstance(sprite);
        def.Frame = sprite.CurrentAnim.Frames.size() - 2;
        if(wasZombie)
            return;
        SpeakTime--;
        if(SpeakTime <= 0)
            Speaking = false;
        if(container == null)
            return;
        updateCount--;
        if(updateCount < 0)
            updateCount = 15;
        else
            return;
        if(ClothingItem_Torso != null && !container.contains(ClothingItem_Torso.getType()))
        {
            topSprite = null;
            ClothingItem_Torso = null;
        }
        if(ClothingItem_Legs != null && !container.contains(ClothingItem_Legs.getType()))
        {
            bottomsSprite = null;
            ClothingItem_Legs = null;
        }
        if(ClothingItem_Feet != null && !container.contains(ClothingItem_Feet.getType()))
        {
            shoeSprite = null;
            ClothingItem_Feet = null;
        }
    }

    public boolean IsSpeaking()
    {
        return Speaking;
    }

    public void Say(String line)
    {
        SpeakTime = line.length() * 4;
        if(SpeakTime < 60)
            SpeakTime = 60;
        sayLine = line;
        if(TutorialManager.instance.ProfanityFilter)
        {
            sayLine = sayLine.replace("Fuck", "****");
            sayLine = sayLine.replace("fuck", "****");
            sayLine = sayLine.replace("Shit", "****");
            sayLine = sayLine.replace("shit", "****");
            sayLine = sayLine.replace("FUCK", "****");
            sayLine = sayLine.replace("SHIT", "****");
        }
        Speaking = true;
    }

    public String getTalkerType()
    {
        return "Talker";
    }

    public InventoryItem ClothingItem_Head;
    public InventoryItem ClothingItem_Torso;
    public InventoryItem ClothingItem_Hands;
    public InventoryItem ClothingItem_Legs;
    public InventoryItem ClothingItem_Feet;
    IsoSprite torsoSprite;
    IsoSprite legsSprite;
    IsoSprite headSprite;
    IsoSprite shoeSprite;
    IsoSprite topSprite;
    IsoSprite bottomsSprite;
    boolean wasZombie;
    public boolean bUseParts;
    private final Color SpeakColor;
    private int SpeakTime;
    int updateCount;
    public boolean Speaking;
    public String sayLine;
}
