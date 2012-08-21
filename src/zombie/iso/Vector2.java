// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Vector2.java

package zombie.iso;

import java.awt.Dimension;
import java.awt.Point;

public class Vector2
    implements Cloneable
{

    public Vector2()
    {
        x = 0.0F;
        y = 0.0F;
    }

    public Vector2(Vector2 other)
    {
        x = other.x;
        y = other.y;
    }

    public Vector2(float x, float y)
    {
        this.x = x;
        this.y = y;
    }

    public void rotate(float rad)
    {
        double rx = (double)x * Math.cos(rad) - (double)y * Math.sin(rad);
        double ry = (double)x * Math.sin(rad) + (double)y * Math.cos(rad);
        x = (float)rx;
        y = (float)ry;
    }

    public static Vector2 fromAwtPoint(Point p)
    {
        return new Vector2(p.x, p.y);
    }

    public static Vector2 fromLengthDirection(float length, float direction)
    {
        Vector2 v = new Vector2();
        v.setLengthAndDirection(direction, length);
        return v;
    }

    public Vector2 add(Vector2 other)
    {
        return new Vector2(x + other.x, y + other.y);
    }

    public Vector2 addToThis(Vector2 other)
    {
        x += other.x;
        y += other.y;
        return this;
    }

    public Vector2 aimAt(Vector2 other)
    {
        setLengthAndDirection(angleTo(other), getLength());
        return this;
    }

    public float angleTo(Vector2 other)
    {
        return (float)Math.atan2(other.y - y, other.x - x);
    }

    public Vector2 clone()
    {
        return new Vector2(this);
    }

    public float distanceTo(Vector2 other)
    {
        return (float)Math.sqrt(Math.pow(other.x - x, 2D) + Math.pow(other.y - y, 2D));
    }

    public float dot(Vector2 other)
    {
        return x * other.x + y * other.y;
    }

    public static float dot(float x, float y, float tx, float ty)
    {
        return x * tx + y * ty;
    }

    public boolean equals(Object other)
    {
        if(other instanceof Vector2)
        {
            Vector2 v = (Vector2)other;
            return v.x == x && v.y == y;
        } else
        {
            return false;
        }
    }

    public float getDirection()
    {
        return (float)Math.atan2(x, y);
    }

    public float getLength()
    {
        return (float)Math.sqrt(x * x + y * y);
    }

    public void normalize()
    {
        float length = getLength();
        if(length == 0.0F)
        {
            x = 0.0F;
            y = 0.0F;
        } else
        {
            x = x / length;
            y = y / length;
        }
        length = getLength();
    }

    public Vector2 set(Vector2 other)
    {
        x = other.x;
        y = other.y;
        return this;
    }

    public Vector2 set(float x, float y)
    {
        this.x = x;
        this.y = y;
        return this;
    }

    public Vector2 setDirection(float direction)
    {
        setLengthAndDirection(direction, getLength());
        return this;
    }

    public Vector2 setLength(float length)
    {
        normalize();
        x *= length;
        y *= length;
        return this;
    }

    public Vector2 setLengthAndDirection(float direction, float length)
    {
        x = (float)(Math.cos(direction) * (double)length);
        y = (float)(Math.sin(direction) * (double)length);
        return this;
    }

    public Dimension toAwtDimension()
    {
        return new Dimension((int)x, (int)y);
    }

    public Point toAwtPoint()
    {
        return new Point((int)x, (int)y);
    }

    public String toString()
    {
        return String.format("Vector2 (X: %f, Y: %f) (L: %f, D:%f)", new Object[] {
            Float.valueOf(x), Float.valueOf(y), Float.valueOf(getLength()), Float.valueOf(getDirection())
        });
    }

    public float getX()
    {
        return x;
    }

    public void setX(float x)
    {
        this.x = x;
    }

    public float getY()
    {
        return y;
    }

    public void setY(float y)
    {
        this.y = y;
    }



    public float x;
    public float y;
}
