// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 13.08.2012 13:54:45
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   LOSThread.java

package zombie;

import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import zombie.characters.IsoGameCharacter;
import zombie.characters.IsoZombie;
import zombie.core.Collections.NulledArrayList;
import zombie.iso.IsoCell;
import zombie.iso.IsoMovingObject;
import zombie.iso.IsoWorld;

// Referenced classes of package zombie:
//            GameTime

public class LOSThread
{
    public class LOSJob
    {

        private void Execute()
        {
            SeenList.clear();
            for(int n = 0; n < IsoWorld.instance.CurrentCell.getObjectList().size(); n++)
            {
                IsoMovingObject obj = (IsoMovingObject)IsoWorld.instance.CurrentCell.getObjectList().get(n);
                if(obj == POVCharacter || !(obj instanceof IsoGameCharacter) || (obj instanceof IsoZombie) && ((IsoZombie)obj).Ghost)
                    continue;
                float dist = obj.DistTo(POVCharacter);
                if(dist <= GameTime.getInstance().getViewDist() && POVCharacter.CanSee(obj))
                    SeenList.add(obj);
            }

            POVCharacter.Seen(SeenList);
            SeenList.clear();
        }

        public IsoGameCharacter POVCharacter;
       


        public LOSJob()
        {
           
        }
    }


    public LOSThread()
    {
        finished = false;
        running = false;
        SeenList = new Stack();
        Jobs = new Stack();
    }

    public void Start()
    {
        while(running) 
            finished = true;
        finished = false;
        losThread = new Thread(new Runnable() {

            public void run()
            {
                try
                {
                    LOSThread.instance.run();
                }
                catch(InterruptedException ex)
                {
                    Logger.getLogger(LOSThread.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

          
        }
);
        losThread.setName("LOSThread");
        losThread.start();
    }

    public void AddJob(IsoGameCharacter pov)
    {
        synchronized(Jobs)
        {
            LOSJob job = new LOSJob();
            job.POVCharacter = pov;
            Jobs.add(job);
        }
    }

    private void run()
        throws InterruptedException
    {
        int doBatch = 50;
        running = true;
        while(!finished) 
        {
            while(!Jobs.isEmpty() && doBatch > 0) 
                synchronized(Jobs)
                {
                    LOSJob job = (LOSJob)Jobs.remove(0);
                    job.Execute();
                    doBatch--;
                }
            doBatch = 50;
            Thread.sleep(5L);
        }
        running = false;
    }

    public static LOSThread instance = new LOSThread();
    public Thread losThread;
    public boolean finished;
    public boolean running;
    public Stack SeenList;
    public Stack Jobs;


}