-- Return true if recipe is valid, false otherwise
function TorchBatteryRemoval_TestIsValid ( a, b, c, d )

	return a:getUsedDelta() > 0; -- Only allow the battery removal if the flashlight has any battery left in it.
	
end

-- When creating item in result box of crafting panel.
function TorchBatteryRemoval_OnCreate(a, b, c, d, result)

	result:setUsedDelta(a:getUsedDelta());  -- Set the battery's power to that of the flashlight

end

-- When clicking to perform crafting and pick up new item
function TorchBatteryRemoval_OnGrab (a, b, c, d, result)

	a:setUsedDelta(0); -- Clear the flashlight's power.

end

-- Return true if recipe is valid, false otherwise
function TorchBatteryInsert_TestIsValid ( a, b, c, d )

	return a:getUsedDelta() == 0; -- Only allow the battery inserting if the flashlight has no battery left in it.
	
end

-- When creating item in result box of crafting panel.
function TorchBatteryInsert_OnCreate(a, b, c, d, result)

	result:setUsedDelta(b:getUsedDelta());  -- Set the flashlight's power to that of the battery fed into it

end

-- When clicking to perform crafting and pick up new item
function TorchBatteryInsert_OnGrab (a, b, c, d, result)

end

function TastySoup_TestIsValid (a,b,c,d)
	return true;
end

function TastySoup_OnCreate (a, b, c, d, result)

end

function TastySoup_OnGrab (a, b, c, d, result)
	player = getPlayer();
	cookinglevel = player:getPerkLevel(Perks.Cooking);
	
	if cookinglevel == 0 then
		result:setName("Watery Soup");
		result:setHungChange(result:getHungChange()*0.7);
	end
	if cookinglevel == 1 then
		result:setName("Okay Soup");
	end
	if cookinglevel == 2 then
		result:setName("Nice Soup");
		result:setHungChange(result:getHungChange()*1.1);
	end
	if cookinglevel == 3 then
		result:setName("Tasty Soup");
		result:setHungChange(result:getHungChange()*1.3);
	end
	if cookinglevel == 4 then
		result:setName("Delicious Soup");
		result:setHungChange(result:getHungChange()*1.5);
	end
	if cookinglevel == 5 then
		result:setName("Gourmet Soup");
		result:setHungChange(result:getHungChange()*2);
	end
end