// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   TextureNameAlreadyInUseException.java

package zombie.core.textures;


public class TextureNameAlreadyInUseException extends RuntimeException
{

    public TextureNameAlreadyInUseException(String name)
    {
        super((new StringBuilder()).append("Texture Name ").append(name).append(" is already in use").toString());
    }
}
