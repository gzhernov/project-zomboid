// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Vector3.java

package zombie.iso;

import java.awt.Dimension;
import java.awt.Point;

// Referenced classes of package zombie.iso:
//            Vector2

public class Vector3
    implements Cloneable
{

    public Vector3()
    {
        x = 0.0F;
        y = 0.0F;
        z = 0.0F;
    }

    public Vector3(Vector3 other)
    {
        x = other.x;
        y = other.y;
        z = other.z;
    }

    public Vector3(float x, float y, float z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
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

    public Vector3 addToThis(Vector2 other)
    {
        x += other.x;
        y += other.y;
        return this;
    }

    public Vector3 aimAt(Vector2 other)
    {
        setLengthAndDirection(angleTo(other), getLength());
        return this;
    }

    public float angleTo(Vector2 other)
    {
        return (float)Math.atan2(other.y - y, other.x - x);
    }

    public Vector3 clone()
    {
        return new Vector3(this);
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
        return (float)Math.sqrt(x * x + y * y + z * z);
    }

    public void normalize()
    {
        float length = getLength();
        if(length == 0.0F)
        {
            x = 0.0F;
            y = 0.0F;
            z = 0.0F;
        } else
        {
            x = x / length;
            y = y / length;
            z = z / length;
        }
        length = getLength();
    }

    public Vector3 set(Vector3 other)
    {
        x = other.x;
        y = other.y;
        z = other.z;
        return this;
    }

    public Vector3 set(float x, float y, float z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }

    public Vector3 setDirection(float direction)
    {
        setLengthAndDirection(direction, getLength());
        return this;
    }

    public Vector3 setLength(float length)
    {
        normalize();
        x *= length;
        y *= length;
        z *= length;
        return this;
    }

    public Vector3 setLengthAndDirection(float direction, float length)
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



    public float x;
    public float y;
    public float z;
}
