// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   MovementBlender.java

package zombie.ui;

import zombie.iso.Vector2;

// Referenced classes of package zombie.ui:
//            UIElement

public class MovementBlender extends UIElement
{

    public MovementBlender(UIElement uiElement)
    {
        sx = 0.0F;
        sy = 0.0F;
        Time = 0.0F;
        TimeMax = 0.0F;
        tx = 0.0F;
        ty = 0.0F;
        lamount = -100F;
        x = uiElement.x;
        y = uiElement.y;
        sx = x;
        sy = y;
        tx = x;
        ty = y;
        uiElement.x = 0.0F;
        uiElement.y = 0.0F;
        width = uiElement.width;
        height = uiElement.height;
        AddChild(uiElement);
    }

    public void MoveTo(float x, float y, float timeSec)
    {
        if(tx == x && ty == y)
        {
            return;
        } else
        {
            TimeMax = timeSec * 30F;
            Time = 0.0F;
            sx = getX();
            sy = getY();
            tx = x;
            ty = y;
            return;
        }
    }

    public boolean Running()
    {
        float amount = Time / TimeMax;
        return (double)amount <= 1.0D;
    }

    public void update()
    {
        super.update();
        Time++;
        float amount = Time / TimeMax;
        if(amount > 1.0F)
            amount = 1.0F;
        if(amount == 1.0F && lamount == 1.0F)
        {
            return;
        } else
        {
            lamount = amount;
            Vector2 vector = new Vector2();
            Vector2 value1 = new Vector2();
            Vector2 value2 = new Vector2();
            value1.x = sx;
            value1.y = sy;
            value2.x = tx;
            value2.y = ty;
            amount = amount <= 1.0F ? amount >= 0.0F ? amount : 0.0F : 1.0F;
            amount = amount * amount * (3F - 2.0F * amount);
            vector.x = value1.x + (value2.x - value1.x) * amount;
            vector.y = value1.y + (value2.y - value1.y) * amount;
            setX(vector.x);
            setY(vector.y);
            return;
        }
    }

    public float sx;
    public float sy;
    public float Time;
    public float TimeMax;
    public float tx;
    public float ty;
    float lamount;
}
