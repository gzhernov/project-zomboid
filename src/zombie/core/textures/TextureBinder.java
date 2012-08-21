// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   TextureBinder.java

package zombie.core.textures;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

public class TextureBinder
{

    public TextureBinder()
    {
        maxTextureUnits = 0;
        textureUnitIDStart = 33984;
        textureIndex = 0;
        activeTextureIndex = 0;
        maxTextureUnits = 1;
        textureUnitIDs = new int[maxTextureUnits];
        for(int n = 0; n < maxTextureUnits; n++)
            textureUnitIDs[n] = -1;

    }

    public void bind(int textureID)
    {
        for(int n = 0; n < maxTextureUnits; n++)
            if(textureUnitIDs[n] == textureID)
            {
                int textureUnit = n + textureUnitIDStart;
                GL13.glActiveTexture(textureUnit);
                activeTextureIndex = textureUnit;
                return;
            }

        textureUnitIDs[textureIndex] = textureID;
        GL13.glActiveTexture(textureUnitIDStart + textureIndex);
        GL11.glBindTexture(3553, textureID);
        activeTextureIndex = textureUnitIDStart + textureIndex;
        textureIndex++;
        if(textureIndex >= maxTextureUnits)
            textureIndex = 0;
    }

    public static TextureBinder instance = new TextureBinder();
    public int maxTextureUnits;
    public int textureUnitIDs[];
    public int textureUnitIDStart;
    public int textureIndex;
    public int activeTextureIndex;

}
