// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   AddGotoLocationTask.java

package zombie.scripting.commands.quest;

import zombie.Quests.QuestCreator;
import zombie.scripting.commands.BaseCommand;
import zombie.scripting.objects.ScriptModule;
import zombie.scripting.objects.Waypoint;

public class AddGotoLocationTask extends BaseCommand
{

    public AddGotoLocationTask()
    {
    }

    public void init(String object, String params[])
    {
        if(object == null || !object.equals("Quest"))
            return;
        name = params[0].trim().replace("\"", "");
        description = params[1].trim().replace("\"", "");
        description = module.getLanguage(description);
        if(description.indexOf("\"") == 0)
        {
            description = description.substring(1);
            description = description.substring(0, description.length() - 1);
        }
        location = params[2].trim().replace("\"", "");
    }

    public void begin()
    {
        Waypoint way = module.getWaypoint(location);
        if(way == null)
        {
            return;
        } else
        {
            QuestCreator.AddQuestTask_GotoLocation(name, description, way.x, way.y, way.z);
            return;
        }
    }

    public boolean IsFinished()
    {
        return true;
    }

    public void update()
    {
    }

    public boolean DoesInstantly()
    {
        return true;
    }

    String name;
    String description;
    String location;
}
