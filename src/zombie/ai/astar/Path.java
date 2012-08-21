// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 13.08.2012 13:54:45
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   Path.java

package zombie.ai.astar;

import java.util.ArrayList;
import java.util.Stack;

public class Path
{
    public static class Step
    {

        public boolean equals(Object other)
        {
            if(other instanceof Step)
            {
                Step o = (Step)other;
                return o.x == x && o.y == y && o.z == z;
            } else
            {
                return false;
            }
        }

        public int getX()
        {
            return x;
        }

        public int getY()
        {
            return y;
        }

        public int getZ()
        {
            return z;
        }

        public int hashCode()
        {
            return x * y * z;
        }

        public int x;
        public int y;
        public int z;

        public Step(int x, int y, int z)
        {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public Step()
        {
        }
    }


    public Path()
    {
        steps = new ArrayList();
        cost = 0.0F;
    }

    public float costPerStep()
    {
        if(steps.isEmpty())
            return cost;
        else
            return cost / (float)steps.size();
    }

    public void appendStep(int x, int y, int z)
    {
        Step step = null;
        step = new Step();
        step.x = x;
        step.y = y;
        step.z = z;
        steps.add(step);
    }

    public boolean contains(int x, int y, int z)
    {
        containsStep.x = x;
        containsStep.y = y;
        containsStep.z = z;
        return steps.contains(containsStep);
    }

    public int getLength()
    {
        return steps.size();
    }

    public Step getStep(int index)
    {
        return (Step)steps.get(index);
    }

    public int getX(int index)
    {
        return getStep(index).x;
    }

    public int getY(int index)
    {
        return getStep(index).y;
    }

    public int getZ(int index)
    {
        return getStep(index).z;
    }

    public static Step createStep()
    {
        if(stepstore.isEmpty())
        {
            for(int n = 0; n < 200; n++)
            {
                Step step = new Step();
                stepstore.push(step);
            }

        }
        return (Step)stepstore.push(containsStep);
    }

    public void prependStep(int x, int y, int z)
    {
        Step step = null;
        step = new Step();
        step.x = x;
        step.y = y;
        step.z = z;
        steps.add(0, step);
    }

    private ArrayList steps;
    public float cost;
    public static Stack stepstore = new Stack();
    static Step containsStep = new Step();

}