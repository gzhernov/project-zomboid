// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   CreateQuest.java

package zombie.scripting.commands.quest;

import zombie.Quests.QuestCreator;
import zombie.scripting.commands.BaseCommand;
import zombie.scripting.objects.ScriptModule;

public class CreateQuest extends BaseCommand
{

    public CreateQuest()
    {
    }

    public void init(String object, String params[])
    {
        if(object == null || !object.equals("Quest"))
            return;
        name = params[0].trim().replace("\"", "");
        description = params[1].replace("\"", "");
        description = module.getLanguage(description);
        if(description.indexOf("\"") == 0)
        {
            description = description.substring(1);
            description = description.substring(0, description.length() - 1);
        }
    }

    public void begin()
    {
        QuestCreator.CreateQuest(name, description);
    }

    public boolean IsFinished()
    {
        return true;
    }

    public boolean getValue()
    {
        return false;
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
}
