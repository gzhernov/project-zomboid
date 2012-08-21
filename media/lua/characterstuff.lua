function DoTraits()
	

    TraitFactory.addTrait("Axeman", "Axe Man", 0, "o   Double speed breaking through doors with the axe.<br>o   Faster axe swing.<br>", true);
    TraitFactory.addTrait("Handy", "Handy", 0, "o   Faster barricading.", true);
    TraitFactory.addTrait("ThickSkinned", "Thick Skinned", 0, "o   Less chance of scratches or bites breaking the skin.", true);
    TraitFactory.addTrait("Patient", "Patient", 4, "o   Less like to get angry.", false);
    TraitFactory.addTrait("ShortTemper", "Short Tempered", -4, "o   Quick to anger.", false);
    TraitFactory.addTrait("Brooding", "Brooding", -2, "o   Recovers slower from bad moods.", false);
    TraitFactory.addTrait("Brave", "Brave", 3, "o   Less prone to becoming panicked.", false);
    TraitFactory.addTrait("Cowardly", "Cowardly", -2, "o   Especially prone to becoming panicked.", false);
    TraitFactory.addTrait("Clumsy", "Clumsy", -1, "o   Makes more noise when moving.", false);
    TraitFactory.addTrait("Graceful", "Graceful", 4, "o   Makes less noise when moving.", false);
    TraitFactory.addTrait("Hypercondriac", "Hypochondriac", -4, "o   May develop infection symptoms without having been infected.", false);
    TraitFactory.addTrait("ShortSighted", "Short Sighted", -2, "o   Smaller view distance<br>o   Slower visibility fade", false);
    TraitFactory.addTrait("HardOfHearing", "Hard of Hearing", -2, "o   Smaller perception radius<br>o   Smaller hearing range", false);
    TraitFactory.addTrait("KeenHearing", "Keen Hearing", 6, "o   Larger perception radius", false);
    TraitFactory.addTrait("EagleEyed", "Eagle Eyed", 6, "o   Faster visibility fade<br>o   Higher visibility arc", false);
    TraitFactory.addTrait("HeartyAppitite", "Hearty Appetite", -2, "o   Needs to eat more regularly", false);
    TraitFactory.addTrait("LightEater", "Light Eater", 3, "o   Needs to eat less regularly", false);
    TraitFactory.addTrait("Athletic", "Athletic", 6, "o   Faster running speed<br>o   Can run for longer without tiring", false);
    TraitFactory.addTrait("Overweight", "Overweight", -3, "o   Slower running speed<br>o   Tire from running more easily", false);
    TraitFactory.addTrait("Strong", "Strong", 6, "o   Extra knockback from melee weapons<br>o   Increased carrying weight", false);
    TraitFactory.addTrait("Weak", "Weak", -4, "o   Less knockback from melee weapons<br>o   Decreased carrying weight", false);
    TraitFactory.addTrait("Resilient", "Resilient", 3, "o   Less prone to disease<br>o   Slower rate of zombification", false);
    TraitFactory.addTrait("ProneToIllness", "Prone to Illness", -4, "o   More prone to disease<br>o   Faster rate of zombification", false);
    TraitFactory.addTrait("LightDrinker", "Light Drinker", -2, "o   gets drunk quickly", false);
    TraitFactory.addTrait("HeavyDrinker", "Hardened Drinker", 3, "o   Doesn't get drunk easily", false);
    TraitFactory.addTrait("Agoraphobic", "Agoraphobic", -4, "o   gets panicked when outdoors", false);
    TraitFactory.addTrait("Claustophobic", "Claustrophobic", -4, "o   gets panicked when indoors", false);
	TraitFactory.addTrait("Marksman", "Marksman", 0, "o   Improved gun accuracy.<br>o   Quicker reload.<br>", true);
	TraitFactory.addTrait("NightOwl", "NightOwl", 0, "o   Requires little sleep.<br>o   Stays extra alert even when sleeping.<br>", true);
	TraitFactory.addTrait("GiftOfTheGab", "Gift Of The Gab", 0, "o   Extra high charisma.<br>o   Better chance of currying favour from NPCs.<br>", true);
	TraitFactory.addTrait("Outdoorsman", "Outdoorsman", 0, "o   Not effected by harsh weather conditions.<br>o   Extra orienteering ability.<br>", true);

	TraitFactory.setMutualExclusive("ShortTemper", "Patient");
    TraitFactory.setMutualExclusive("Weak", "Strong");
    TraitFactory.setMutualExclusive("Athletic", "Overweight");
    TraitFactory.setMutualExclusive("Resilient", "ProneToIllness");
    TraitFactory.setMutualExclusive("LightDrinker", "HeavyDrinker");
    TraitFactory.setMutualExclusive("HardOfHearing", "KeenHearing");
    TraitFactory.setMutualExclusive("HeartyAppitite", "LightEater");
    TraitFactory.setMutualExclusive("Clumsy", "Graceful");
    TraitFactory.setMutualExclusive("Brave", "Cowardly");
    TraitFactory.setMutualExclusive("ShortSighted", "EagleEyed");

end

function DoProfessions()

	local fireofficer = ProfessionFactory.addProfession("fireofficer", "Fire Officer", "Prof_FireFighter");
	fireofficer:addFreeTrait("Axeman");
	
	local policeofficer = ProfessionFactory.addProfession("policeofficer", "Police Officer", "Prof_Cop");
	policeofficer:addFreeTrait("Marksman");

	local parkranger = ProfessionFactory.addProfession("parkranger", "Park Ranger", "Prof_ParkRanger");
	parkranger:addFreeTrait("Outdoorsman");
	
	local constructionworker = ProfessionFactory.addProfession("constructionworker", "Construction Worker", "Prof_ConstructionWorker");
	constructionworker:addFreeTrait("ThickSkinned");
	constructionworker:addFreeTrait("Handy");
	
	local securityguard = ProfessionFactory.addProfession("securityguard", "Security Guard", "Prof_SecurityGuard");
	securityguard:addFreeTrait("NightOwl");
	

end

Events.OnGameBoot.Add(DoTraits);
Events.OnGameBoot.Add(DoProfessions);