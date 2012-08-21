// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   TextureNotFoundException.java

package zombie.core.textures;


public class TextureNotFoundException extends RuntimeException
{

    public TextureNotFoundException(String name)
    {
        super((new StringBuilder()).append("Image ").append(name).append(" not found! ").toString());
    }
}
