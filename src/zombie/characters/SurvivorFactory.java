// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   SurvivorFactory.java

package zombie.characters;

import gnu.trove.map.hash.THashMap;
import zombie.core.Collections.NulledArrayList;
import zombie.core.Rand;
import zombie.inventory.types.Clothing;
import zombie.iso.IsoCell;

// Referenced classes of package zombie.characters:
//            SurvivorDesc, IsoSurvivor, IsoGameCharacter

public class SurvivorFactory
{

    public SurvivorFactory()
    {
    }

    public static SurvivorDesc[] CreateFamily(int nCount)
    {
        SurvivorDesc survivors[] = new SurvivorDesc[nCount];
        for(int n = 0; n < nCount; n++)
        {
            survivors[n] = CreateSurvivor();
            if(n > 0)
                survivors[n].surname = survivors[0].surname;
        }

        return survivors;
    }

    public static SurvivorDesc CreateSurvivor()
    {
        SurvivorDesc survivor = new SurvivorDesc();
        IsoGameCharacter.SurvivorMap.put(Integer.valueOf(survivor.ID), survivor);
        survivor.forename = MaleForenames[Rand.Next(MaleForenames.length)];
        survivor.surname = Surnames[Rand.Next(Surnames.length)];
        switch(Rand.Next(2))
        {
        case 0: // '\0'
            survivor.head = "Base_Head1";
            break;

        case 1: // '\001'
            survivor.head = "Base_Head2";
            break;
        }
        Integer skin = Integer.valueOf(Rand.Next(4));
        survivor.skinpal = (new StringBuilder()).append("Skin_0").append(skin.toString()).toString();
        switch(Rand.Next(2))
        {
        case 0: // '\0'
            survivor.top = "Shirt";
            break;

        case 1: // '\001'
            survivor.top = "Vest";
            break;
        }
        survivor.toppal = (String)((NulledArrayList)Clothing.getClothingPaletteMap().get(survivor.top)).get(topCur);
        survivor.bottomspal = (String)((NulledArrayList)Clothing.getClothingPaletteMap().get(survivor.bottoms)).get(botCur);
        topCur++;
        botCur++;
        if(topCur >= ((NulledArrayList)Clothing.getClothingPaletteMap().get(survivor.top)).size())
            topCur = 0;
        if(botCur >= ((NulledArrayList)Clothing.getClothingPaletteMap().get(survivor.bottoms)).size())
            botCur = 0;
        return survivor;
    }

    public static SurvivorDesc[] CreateSurvivorGroup(int nCount)
    {
        SurvivorDesc survivors[] = new SurvivorDesc[nCount];
        for(int n = 0; n < nCount; n++)
            survivors[n] = CreateSurvivor();

        return survivors;
    }

    public static IsoSurvivor InstansiateInCell(SurvivorDesc desc, IsoCell cell, int x, int y, int z)
    {
        desc.Instance = new IsoSurvivor(desc, cell, x, y, z);
        return (IsoSurvivor)desc.Instance;
    }

    public static String MaleForenames[] = {
        "James", "John", "Robert", "Bobby", "Michael", "William", "David", "Richard", "Charles", "Joseph", 
        "Thomas", "Chris", "Christopher", "Daniel", "Paul", "Mark", "Donald", "George", "Kenneth", "Steven", 
        "Stephen", "Edward", "Brian", "Ronald", "Anthony", "Kevin", "Jason", "Matthew", "Gary", "Timothy", 
        "Jose", "Larry", "Jeffrey", "Frank", "Scott", "Eric", "Andrew", "Andy", "Raymond", "Gregory", 
        "Greg", "Joshua", "Josh", "Jerry", "Dennis", "Walter", "Patrick", "Peter", "Harold", "Douglas", 
        "Henry", "Carl", "Arthur", "Ryan", "Roger", "Joe", "Juan", "Albert", "Jonathan", "Justin", 
        "Terry", "Gerald", "Keith", "Samuel", "Sam", "Willie", "Ralph", "Lawrence", "Nicholas", "Nick", 
        "Roy", "Benjamin", "Ben", "Bruce", "Brandon", "Adam", "Harry", "Fred", "Wayne", "Billy", 
        "Steve", "Louis", "Jeremy", "Aaron", "Randy", "Howard", "Eugene", "Carlos", "Russell", "Bobby", 
        "Victor", "Martin", "Ernest", "Phillip", "Todd", "Jesse", "Craig", "Alan", "Shawn", "Clarence", 
        "Sean", "Philip", "Johnny", "Earl", "Jimmy", "Antonio", "Danny", "Bryan", "Tony", "Luis", 
        "Mike", "Stanley", "Leonard", "Nathan", "Dale"
    };
    public static String Surnames[] = {
        "Simpson", "Hodgetts", "Cowen", "Porter", "Smith", "Johnson", "Williams", "Jones", "Brown", "Davis", 
        "Miller", "Wilson", "Moore", "Taylor", "Anderson", "Thomas", "Jackson", "White", "Harris", "Martin", 
        "Thompson", "Garcia", "Martinez", "Robinson", "Clark", "Rodriguez", "Lewis", "Lee", "Walker", "Hall", 
        "Allen", "Young", "Hernandez", "King", "Wright", "Lopez", "Hill", "Scott", "Green", "Adams", 
        "Baker", "Gonzalez", "Nelson", "Carter", "Mitchell", "Perez", "Roberts", "Turner", "Phillips", "Campbell", 
        "Parker", "Evans", "Edwards", "Collins", "Stewart", "Sanchez", "Morris", "Rogers", "Reed", "Cook", 
        "Morgan", "Bell", "Murphy", "Bailey", "Cooper", "Richardson", "Cox", "Howard", "Ward", "Torres", 
        "Peterson", "Gray", "Ramirez", "James", "Watson", "Brooks", "Kelly", "Sanders", "Price", "Bennet", 
        "Wood"
    };
    static int topCur = 0;
    static int botCur = 0;

}
