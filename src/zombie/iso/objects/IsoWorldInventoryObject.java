// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   IsoWorldInventoryObject.java

package zombie.iso.objects;

import java.io.*;
import java.nio.ByteBuffer;
import zombie.FrameLoader;
import zombie.GameWindow;
import zombie.characters.IsoPlayer;
import zombie.core.Collections.NulledArrayList;
import zombie.core.textures.ColorInfo;
import zombie.core.textures.Texture;
import zombie.inventory.*;
import zombie.iso.*;
import zombie.iso.sprite.*;
import zombie.ui.ObjectTooltip;

public class IsoWorldInventoryObject extends IsoObject
{

    public IsoWorldInventoryObject(InventoryItem item, IsoGridSquare sq, float xoff, float yoff, float zoff)
    {
        OutlineOnMouseover = true;
        this.xoff = xoff;
        this.yoff = yoff;
        this.zoff = zoff;
        this.item = item;
        sprite = IsoSprite.CreateSprite(sq.getCell().SpriteManager);
        String str = item.getWorldTexture();
        try
        {
            Texture tex = Texture.getSharedTexture(str);
            if(tex == null)
                str = "media/inventory/world/WItem_Sack.png";
        }
        catch(Exception ex) { }
        sprite.LoadFrameExplicit(str);
        square = sq;
        offsetY -= 100F;
        offsetX -= 30F;
        if(!FrameLoader.bServer)
        {
            offsetX += ((IsoDirectionFrame)sprite.CurrentAnim.Frames.get(0)).getTexture(dir).getWidthOrig() / 2;
            offsetY += ((IsoDirectionFrame)sprite.CurrentAnim.Frames.get(0)).getTexture(dir).getHeightOrig();
        }
    }

    public void load(DataInputStream input)
        throws IOException
    {
        xoff = input.readFloat();
        yoff = input.readFloat();
        zoff = input.readFloat();
        offsetX = input.readFloat();
        offsetY = input.readFloat();
        sprite = IsoSprite.CreateSprite(IsoWorld.instance.spriteManager);
        String objType = GameWindow.ReadString(input);
        item = InventoryItemFactory.CreateItem(objType);
        String str = item.getWorldTexture();
        try
        {
            Texture tex = Texture.getSharedTexture(str);
            if(tex == null)
                str = "media/inventory/world/WItem_Sack.png";
        }
        catch(Exception ex) { }
        sprite.LoadFrameExplicit(str);
    }

    public void load(ByteBuffer input)
        throws IOException
    {
        xoff = input.getFloat();
        yoff = input.getFloat();
        zoff = input.getFloat();
        offsetX = input.getFloat();
        offsetY = input.getFloat();
        sprite = IsoSprite.CreateSprite(IsoWorld.instance.spriteManager);
        String objType = GameWindow.ReadString(input);
        item = InventoryItemFactory.CreateItem(objType);
        String str = item.getWorldTexture();
        try
        {
            Texture tex = Texture.getSharedTexture(str);
            if(tex == null)
                str = "media/inventory/world/WItem_Sack.png";
        }
        catch(Exception ex) { }
        sprite.LoadFrameExplicit(str);
    }

    public boolean Serialize()
    {
        return true;
    }

    public void save(DataOutputStream output)
        throws IOException
    {
        output.writeBoolean(Serialize());
        if(!Serialize())
        {
            return;
        } else
        {
            output.writeInt(getObjectName().hashCode());
            output.writeFloat(xoff);
            output.writeFloat(yoff);
            output.writeFloat(zoff);
            output.writeFloat(offsetX);
            output.writeFloat(offsetY);
            GameWindow.WriteString(output, (new StringBuilder()).append(item.getModule()).append(".").append(item.getType()).toString());
            return;
        }
    }

    public void save(ByteBuffer output)
        throws IOException
    {
        output.put(((byte)(Serialize() ? 1 : 0)));
        if(!Serialize())
        {
            return;
        } else
        {
            output.putInt(getObjectName().hashCode());
            output.putFloat(xoff);
            output.putFloat(yoff);
            output.putFloat(zoff);
            output.putFloat(offsetX);
            output.putFloat(offsetY);
            GameWindow.WriteString(output, (new StringBuilder()).append(item.getModule()).append(".").append(item.getType()).toString());
            return;
        }
    }

    public String getObjectName()
    {
        return "WorldInventoryItem";
    }

    public IsoWorldInventoryObject(IsoCell cell)
    {
        super(cell);
    }

    public void DoTooltip(ObjectTooltip tooltipUI)
    {
        item.DoTooltip(tooltipUI);
    }

    public boolean HasTooltip()
    {
        return false;
    }

    public boolean onMouseLeftClick(int x, int y)
    {
        if(IsoUtils.DistanceTo(square.getX(), square.getY(), IsoPlayer.getInstance().getX(), IsoPlayer.getInstance().getY()) < 2.0F && (float)square.getZ() == IsoPlayer.getInstance().getZ())
        {
            IsoPlayer.getInstance().getInventory().AddItem(item);
            DirtySlice();
            square.getObjects().remove(this);
            return true;
        } else
        {
            return false;
        }
    }

    public void render(float x, float y, float z, ColorInfo col, boolean bDoChild)
    {
        sprite.render(this, x + xoff, y + yoff, z + zoff, dir, offsetX, offsetY, col);
    }

    public void renderObjectPicker(float x, float y, float z, ColorInfo lightInfo)
    {
        if(sprite == null)
        {
            return;
        } else
        {
            sprite.renderObjectPicker(this, x + xoff, y + yoff, z + zoff, dir, offsetX, offsetY, lightInfo);
            return;
        }
    }

    public InventoryItem item;
    public float xoff;
    public float yoff;
    public float zoff;
}
