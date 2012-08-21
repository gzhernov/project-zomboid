-- helper lua file for doing timed actions...
require 'timedactionshelper'



--v0.9 28 March 2012
reloadAction = nil;
reloadStarted = false;
reloadWeapon = nil;
rackingStarted = false;
rackWeapon = nil;

ticks = 0;
lastTimeRWasPressed = 0;
spaceIsPressed = false;
lastTimeSpaceWasPressed = 0;
rIsPressed = false;
EMPTY = 0;
SPENT = 1;
LIVE = 2;
testVariable = true;
print('Running Stormys Reloading');

-- This function serves to intialise
-- all the properties of a weapon. Is
-- called when attempts to reload/fire/
-- or rack the weapon in order to guarantee
-- the fields are available and we don't get
-- nil pointers.
function setUpGun(weapon)
	for i,v in ipairs(weaponsList) do
		if(v.name == weapon:getName()) then
		-- this is essentially done eveytime
		-- we check a weapon, should have additional
		-- check to prevent it repeatedly setting
		-- up the same fields
			local itemdata = weapon:getModData();
			v.defaultAmmo = weapon:getAmmoType();
			v.defaultSwingSound = weapon:getSwingSound();
			weapon:setAmmoType(nil);
			itemdata.ammo = v.ammo;
			itemdata.clipName = v.clipName;
			itemdata.clipIcon = v.clipIcon;
			itemdata.reloadStyle = v.reloadStyle;
			weapon:setSwingSound(v.shootSound);
			itemdata.shotStyle = v.shotStyle;
			itemdata.ejectSound = v.ejectSound;
			itemdata.clickSound = v.clickSound;
			itemdata.insertRoundSound = v.insertSound;
			itemdata.rackSound = v.rackSound;
			itemdata.maxAmmo = v.maxAmmo;
			itemdata.reloadTime = v.reloadTime;
			itemdata.rackTime = v.rackTime;
			if(itemdata.currentAmmo == nil) then
				--it is the first time we have called
				--this method with this item
				--set up the following fields
				itemdata.currentAmmo = 0;
				itemdata.roundChambered = 0;
				itemdata.containsClip = v.containsClip;
				-- For revolvers only
				if(itemdata.reloadStyle == 'revolver') then
					itemdata.currentAmmo = 1;
					itemdata.chamberOne = v.chamberOne;
					itemdata.chamberTwo = v.chamberTwo;
					itemdata.chamberThree = v.chamberThree;
					itemdata.chamberFour = v.chamberFour;
					itemdata.chamberFive = v.chamberFive;
					itemdata.chamberSix = v.chamberSix;
				end
			end
			return true;
		end
	end
	return false;
end


-- This function serves to intialise
-- all the properties of a clip. Is
-- called when attempts to reload or rack
-- the weapon in order to guarantee
-- the fields are available and we don't get
-- nil pointers.
function setUpClip(clip)
	for i,v in ipairs(clipList) do
		-- In order to avoid stacking the names
		-- of clips are being changed.
		-- If the clip has never been set up
		-- we can use the name to check whether
		-- the item in question has a match in clipList
		-- If it has been set up already, the name
		-- will have changed and we will have to use the
		-- Type
		if(v.name == clip:getName() or v.clipType == clip:getType()) then
			local itemdata = clip:getModData();
			itemdata.ammo = v.ammo;
			itemdata.clipName = v.clipName;
			itemdata.clipType = v.clipType;
			itemdata.reloadStyle = v.reloadStyle;
			itemdata.shotStyle = v.shotStyle;
			itemdata.ejectSound = v.ejectSound;
			itemdata.clickSound = v.clickSound;
			itemdata.insertRoundSound = v.insertSound;
			itemdata.rackSound = v.rackSound;
			itemdata.maxAmmo = v.maxAmmo;
			itemdata.reloadTime = v.reloadTime;
			itemdata.rackTime = v.rackTime;
			if(itemdata.currentAmmo == nil) then
				--it is the first time we have called
				--this method with this item
				--set up the following two fields
				itemdata.currentAmmo = 0;
				itemdata.roundChambered = 0;
				itemdata.containsClip = v.containsClip;
			end
			clip:setName(v.name..' Rounds Left: '..getCurrentCapacity(clip));
			return true;
		end
	end
	return false;
end

--******************************************************
-- Some convenience methods
--******************************************************

function getCurrentCapacity(weapon)
	itemdata = weapon:getModData();
	return itemdata.currentAmmo
end

function setCurrentCapacity(weapon, value)
	itemdata = weapon:getModData();
	itemdata.currentAmmo = value;
end

function getMaxCapacity(weapon)
	itemdata = weapon:getModData();
	return itemdata.maxAmmo
end

function isRoundChambered(weapon)
	itemdata = weapon:getModData();
	return itemdata.roundChambered;
end

function setRoundChambered(weapon, state)
	itemdata = weapon:getModData();
	itemdata.roundChambered = state;
end

function getReloadTime(weapon)
	itemdata = weapon:getModData();
	return itemdata.reloadTime;
end

function getRackTime(weapon)
	itemdata = weapon:getModData();
	
	return itemdata.rackTime;
end

function getClickSound(weapon)
	itemdata = weapon:getModData();
	return itemdata.clickSound;
end

function getInsertRoundSound(weapon)
	itemdata = weapon:getModData();
	return itemdata.insertRoundSound;
end

function getRackSound(weapon)
	itemdata = weapon:getModData();
	return itemdata.rackSound;
end

function getEjectSound(weapon)
	itemdata = weapon:getModData();
	return itemdata.ejectSound;
end

function getAmmoType(weapon)
	itemdata = weapon:getModData();
	return itemdata.ammo;
end

function getShotStyle(weapon)
	itemdata = weapon:getModData();
	return itemdata.shotStyle;
end

function containsClip(weapon)
	itemdata = weapon:getModData();
	return itemdata.containsClip;
end

function setContainsClip(weapon, value)
	itemdata = weapon:getModData();
	itemdata.containsClip = value;
end

function getReloadStyle(weapon)
	itemdata = weapon:getModData();
	return itemdata.reloadStyle;
end

function getClipName(weapon)
	itemdata = weapon:getModData();
	return itemdata.clipName;
end

function getClipType(clip)
	itemdata = clip:getModData();
	return itemdata.clipType;
end

function getCylinderChamber(weapon, chamber)
	itemdata = weapon:getModData();
	-- switch statement would be better
	if(chamber == 1) then
		return itemdata.chamberOne;
	elseif(chamber == 2) then
		return itemdata.chamberTwo;
	elseif(chamber == 3) then
		return itemdata.chamberThree;
	elseif(chamber == 4) then
		return itemdata.chamberFour;
	elseif(chamber == 5) then
		return itemdata.chamberFive;
	elseif(chamber == 6) then
		return itemdata.chamberSix;
	end
end

function setCylinderChamber(weapon, chamber, Round)
	itemdata = weapon:getModData();
	-- switch statement would be better
	if(chamber == 1) then
		itemdata.chamberOne = Round;
	elseif(chamber == 2) then
		itemdata.chamberTwo = Round;
	elseif(chamber == 3) then
		itemdata.chamberThree = Round;
	elseif(chamber == 4) then
		itemdata.chamberFour = Round;
	elseif(chamber == 5) then
		itemdata.chamberFive = Round;
	elseif(chamber == 6) then
		itemdata.chamberSix = Round;
	end
end

function rotateCylinder(weapon)
	if(getReloadStyle(weapon) ~= 'revolver') then
		return;
	else
		setCurrentCapacity(weapon, getCurrentCapacity(weapon) + 1);
		if(getCurrentCapacity(weapon) > getMaxCapacity(weapon)) then
			setCurrentCapacity(weapon, 1);
		end
		if(getCylinderChamber(weapon, getCurrentCapacity(weapon)) == LIVE) then
			setRoundChambered(weapon, 1);
		else
			setRoundChambered(weapon, 0);
		end
	end
end

function spinCylinder(weapon)
	if(getReloadStyle(weapon) ~= 'revolver') then
		return;
	else
		setCurrentCapacity(weapon, ZombRand(1, 6));
		if(getCylinderChamber(weapon, getCurrentCapacity(weapon)) == LIVE) then
			setRoundChambered(weapon, 1);
		else
			setRoundChambered(weapon, 0);
		end
	end
end

function getIcon(weapon)
	itemdata = weapon:getModData();
	return itemdata.clipIcon;
end

--*****************************************************************
-- END OF CONVENIENCE METHODS
--*****************************************************************

-- Resets the debouncer we're using to detect reloads/
-- weapon racks. Is done here rather than in the timed action's
-- stop action to avoid the same button starting the timed action
-- from cancelling the timed action.
function checkButtonPressed(totalTicks)
	ticks = totalTicks;
	if(rIsPressed == true) then
		if(isGun(getPlayer():getPrimaryHandItem()) == true or isClip(getPlayer():getPrimaryHandItem()) == true) then
			if(lastTimeRWasPressed + (getReloadTime(getPlayer():getPrimaryHandItem())+(getPlayer():getMoodles():getMoodleLevel(MoodleType.Panic)*30))  <= totalTicks) then
				rIsPressed = false;
			end
		end
	end
	if(spaceIsPressed == true) then
		if(isGun(getPlayer():getPrimaryHandItem()) == true or isClip(getPlayer():getPrimaryHandItem()) == true) then
			local timeToWait = math.max(getRackTime(getPlayer():getPrimaryHandItem()), 30);
		
			if(lastTimeSpaceWasPressed + timeToWait <= totalTicks) then
				spaceIsPressed = false;
			end
		end
	end
end
Events.OnTick.Add(checkButtonPressed);

-- Simple check to make sure the gun
-- has a designated reload style (see eof)
function isGun(weapon)
	for i,v in ipairs(weaponsList) do
		if(v.name == weapon:getName()) then
			return true;
		end
	end
	return false;
end

-- Simple check to make sure the clip
-- has a designated reload style (see eof)
function isClip(clip)
	for i,v in ipairs(clipList) do
		if(v.name == clip:getName() or v.clipType == clip:getType()) then
			return true;
		end
	end
	return false;
end

-- Checks whether the DoAttack() method may
-- begin (i.e whether a weapon has a round loaded)
-- Addional check so that gun properties are reset
-- for NPC characters until AI can be made compatible
-- (never managed to test this)
function checkLoaded(character, chargeDelta)
	local weapon = character:getPrimaryHandItem();
	if(character ~= getPlayer()
		and isGun(weapon) == true) then
		if (weapon:getModData().defaultAmmo ~= nil) then
			weapon:setAmmoType(weapon:getModData().defaultAmmo);
			weapon:setSwingSound(weapon:getModData().defaultSwingSound);
		end
	else
		if(setUpGun(weapon) == true) then
			if(isRoundChambered(weapon) == 1) then
				character:DoAttack(chargeDelta);
			elseif(getClickSound(weapon) ~= nil) then
				getSoundManager():PlayWorldSound(getClickSound(weapon), character:getSquare(), 0, 4, 1.0, false);
				if(getReloadStyle(weapon) == 'revolver') then
					rotateCylinder(weapon);
				end
			end
		else
			character:DoAttack(chargeDelta);
		end
	end
end
Hook.Attack.Add(checkLoaded);

-- Determines whether after firing
-- the weapon can be fired again
-- for the revolver, this involves
-- checking the next chamber. For
-- auto weapons, checks whether there
-- is ammo left
function fireShot(wielder, weapon)
	if(getReloadStyle(weapon) == 'revolver') then
		setCylinderChamber(weapon, getCurrentCapacity(weapon), EMPTY);
		rotateCylinder(weapon);
	elseif(isRoundChambered(weapon) == 1) then
		setRoundChambered(weapon, 0);
		if(getShotStyle(weapon) == 'auto') then
			if(getCurrentCapacity(weapon) > 0) then
				setCurrentCapacity(weapon, getCurrentCapacity(weapon) - 1);
				setRoundChambered(weapon, 1);
			end
		end
	end
end
Events.OnWeaponSwingHitPoint.Add(fireShot);

-- Used for testing. Can be deleted
giveOnce = false;
function giveShotgun(character)
end
Events.OnPlayerUpdate.Add(giveShotgun);
-- End

reloadAction = {};

-- Performs various checks to make sure that
-- reloading is valid
function canReload()
	local weapon = getPlayer():getPrimaryHandItem();
	if(weapon ~= nil) then
		if(isGun(weapon) == true) then
			local itemdata = weapon:getModData();
			-- sets the itemModData values if they've never been set...
			if itemdata.maxAmmo == nil then
				setUpGun(weapon);
			end
			if(getReloadStyle(weapon) == 'clip') then
				-- check whether the weapon contains a clip
				-- or whether there is a clip to load
				if(containsClip(weapon) == 1
				or getPlayer():getInventory():FindAndReturn(getAmmoType(weapon)) ~= nil) then
					return true;
				end
			elseif(getReloadStyle(weapon) == 'revolver') then
				local chamberIsFull = true;
				-- loop through the chambers and check whether
				-- any are empty
				for t = 1, getMaxCapacity(weapon) do
					if(getCylinderChamber(weapon, t) == EMPTY) then
						chamberIsFull = false;
						t = t+1;
					end
					if(chamberIsFull == false) then
						return true;
					end
				end
			-- if we get here then the reload style is shotgun
			-- we should just check there is ammo available
			elseif(getCurrentCapacity(weapon) < getMaxCapacity(weapon)
			and getPlayer():getInventory():FindAndReturn(getAmmoType(weapon)) ~= nil) then
				return true;
			end
		end
		if(isClip(weapon) == true) then
			local itemdata = weapon:getModData();
			-- sets the original values if they've never been set...
			if(itemdata.maxAmmo == nil) then
				setUpClip(weapon);
			end
			-- check for ammo
			if (getCurrentCapacity(weapon) < getMaxCapacity(weapon)
			and getPlayer():getInventory():FindAndReturn(getAmmoType(weapon)) ~= nil) then
				return true;
			end
		end
	end
	return false;
end

--**************************************************
--
--				Shotgun Based Reloading
--				  Timed Action methods
--
--**************************************************

function shotgunReloadIsValid(char, square)
	local weapon = char:getPrimaryHandItem();
	if(weapon ~= nil) then
		if((isGun(weapon) == true or isClip(weapon) == true)and getCurrentCapacity(weapon) < getMaxCapacity(weapon)
		   and char:getInventory():FindAndReturn(getAmmoType(weapon)) ~= nil) then
				return true;
		end
	end
	if (isKeyDown(Keyboard.KEY_R)==false) then
		reloadStarted = false;
	end
	return false;
end

function shotgunReloadStart(char, square)
	reloadStarted = true;
	local weapon = reloadWeapon;
end

function shotgunReloadPerform(char, square)
	local weapon = reloadWeapon;
	getSoundManager():PlayWorldSound(getInsertRoundSound(weapon), char:getSquare(), 0, 10, 1.0, false);
	setCurrentCapacity(weapon, getCurrentCapacity(weapon) + 1);
	setUpClip(weapon);
	-- remove the necessary ammo
	getPlayer():getInventory():RemoveOneOf(getAmmoType(weapon));
	reloadStarted = false;
	rIsPressed = false;
	if (isKeyDown(Keyboard.KEY_R)==false) then
			char:setPrimaryHandItem(whyClipInHand);
			whyClipInHand = nil;
			reloadWeapon = getPlayer():getPrimaryHandItem();
			weapon = reloadWeapon;
			local moodles = getPlayer():getMoodles();
			local panicLevel = moodles:getMoodleLevel(MoodleType.Panic);
			local weapon = getPlayer():getPrimaryHandItem();
			local valid = shotgunReloadIsValid;
			local start = shotgunReloadStart;
			local perform = shotgunReloadPerform;
			if(getReloadStyle(weapon) == 'revolver') then
				valid = revolverReloadIsValid;
				start = revolverReloadStart;
				perform = revolverReloadPerform;
			elseif(getReloadStyle(weapon) == 'clip') then
				valid = clipReloadIsValid;
				start = clipReloadStart;
				perform = clipReloadPerform;
			end
			reloadAction = TimedActions.Create("Reload", valid, start, perform, stopReload);
			-- remember to factor in marksman time adjustment here
			TimedActions.Start(reloadAction, getPlayer(), (getReloadTime(reloadWeapon)*getPlayer():getReloadingMod())+(panicLevel*30), false, true, getPlayer():getSquare());
			reloadStarted = true;
		
	end

end


--**************************************************
--
--				Clip Based Reloading
--				Timed Action methods
--
--**************************************************

function clipReloadIsValid(char, square)
	local weapon = char:getPrimaryHandItem();
	if(weapon ~= nil) then
		if(isGun(weapon) == true
		and (containsClip(weapon) == 1
		or char:getInventory():FindAndReturn(getAmmoType(weapon)) ~= nil)) then
			return true;
		end
	end
	if (isKeyDown(Keyboard.KEY_R)==false) then
		reloadStarted = false;
		
	end
	return false;
end

function clipReloadStart(char, square)
	reloadStarted = true;
	local weapon = reloadWeapon;
	if(containsClip(weapon) == 1) then
		getSoundManager():PlayWorldSound(getEjectSound(weapon), char:getSquare(), 0, 10, 1.0, false);
	else
		getSoundManager():PlayWorldSound(getInsertRoundSound(weapon), char:getSquare(), 0, 10, 1.0, false);
	end
end

whyClipInHand = nil;

function clipReloadPerform(char, square)
	local weapon = reloadWeapon;
	whyClipInHand = weapon;	
	if(containsClip(weapon) == 1) then
		-- we are ejecting the current clip
		-- can be replaced by factory method
		local clip = InventoryItem.new('Base',getClipName(weapon),getAmmoType(weapon),getIcon(weapon));
		setUpClip(clip);
		setCurrentCapacity(clip, getCurrentCapacity(weapon));
		setCurrentCapacity(weapon, 0);
		clip:setName(getClipName(weapon)..' Rounds Left: '..getCurrentCapacity(clip));
		char:getInventory():AddItem(clip);
		reloadStarted = false;
		setContainsClip(weapon, 0);
	
		char:setPrimaryHandItem(clip);
	else
		-- we are insterting a new clip
		local clip = nil;
		local mostAmmo = -1;
		iterator = char:getInventory():getItems():iterator();
		while(iterator:hasNext()) do
			local currentItem = iterator:next();
			if(setUpClip(currentItem) == true) then
				if(getClipType(currentItem) == getAmmoType(weapon)) then
					if(getCurrentCapacity(currentItem) > mostAmmo) then
						clip = currentItem;
						mostAmmo = getCurrentCapacity(clip);
					end
				end
			end
		end
		if (clip ~= nil) then
			setCurrentCapacity(weapon, getCurrentCapacity(clip));
			char:getInventory():Remove(clip);
			reloadStarted = false;
			setContainsClip(weapon, 1);
			char:getXp():AddXP(Perks.Reloading, 1);
			if(canRack() == true
				and rackingStarted == false) then
				TimedActions.Start(rackingAction, getPlayer(), getRackTime(getPlayer():getPrimaryHandItem()) * getPlayer():getReloadingMod(), false, true, getPlayer():getSquare(), true);
				rackingStarted = true;
				spaceIsPressed = true
				lastTimeSpaceWasPressed = ticks;
			end
		end
	end
end


--**************************************************
--
--				Revolver Based Reloading
--				  Timed Action methods
--
--**************************************************

function revolverReloadIsValid(char, square)
	local weapon = char:getPrimaryHandItem();
	if(weapon ~= nil) then
		if((isGun(weapon) == true or isClip(weapon) == true)
			and char:getInventory():FindAndReturn(getAmmoType(weapon)) ~= nil) then
				return true;
		end
	end
	if (isKeyDown(Keyboard.KEY_R)==false) then
		reloadStarted = false;
	end
	return false;
end

function revolverReloadStart(char, square)
	reloadStarted = true;
end

function revolverReloadPerform(char, square)
	local weapon = char:getPrimaryHandItem();
	getSoundManager():PlayWorldSound(getInsertRoundSound(weapon), char:getSquare(), 0, 10, 1.0, false);
	local roundHasBeenInserted = false;
	while (roundHasBeenInserted == false) do
		if(getCylinderChamber(weapon, getCurrentCapacity(weapon)) == EMPTY) then
			setCylinderChamber(weapon, getCurrentCapacity(weapon), LIVE);
			roundHasBeenInserted = true;
		end
		rotateCylinder(weapon);
	end
	-- remove the necessary ammo
	getPlayer():getInventory():RemoveOneOf(getAmmoType(weapon));
		reloadStarted = false;	
		if (isKeyDown(Keyboard.KEY_R)==false) then
			char:setPrimaryHandItem(whyClipInHand);
			whyClipInHand = nil;
			reloadWeapon = getPlayer():getPrimaryHandItem();
			weapon = reloadWeapon;
			local moodles = getPlayer():getMoodles();
			local panicLevel = moodles:getMoodleLevel(MoodleType.Panic);
			local weapon = getPlayer():getPrimaryHandItem();
			local valid = shotgunReloadIsValid;
			local start = shotgunReloadStart;
			local perform = shotgunReloadPerform;
			if(getReloadStyle(weapon) == 'revolver') then
				valid = revolverReloadIsValid;
				start = revolverReloadStart;
				perform = revolverReloadPerform;
			elseif(getReloadStyle(weapon) == 'clip') then
				valid = clipReloadIsValid;
				start = clipReloadStart;
				perform = clipReloadPerform;
			end
			reloadAction = TimedActions.Create("Reload", valid, start, perform, stopReload);
			-- remember to factor in marksman time adjustment here
			TimedActions.Start(reloadAction, getPlayer(), (getReloadTime(reloadWeapon)*getPlayer():getReloadingMod())+(panicLevel*30), false, true, getPlayer():getSquare());
			reloadStarted = true;

	end
	

end



--**************************************************
--
--				Reloading Action Control
--
--**************************************************


function stopReload()
	reloadStarted = false;
	reloadAction.javaAction = nil;	
end


function startReload()
	if(isKeyDown(Keyboard.KEY_R) == true and rIsPressed == false and reloadStarted == false) then
		if(canReload() == true
			and reloadStarted == false) then
			-- prep the reload information
			-- may want to add marksman modifier in here
			reloadWeapon = getPlayer():getPrimaryHandItem();
			local moodles = getPlayer():getMoodles();
			local panicLevel = moodles:getMoodleLevel(MoodleType.Panic);
			local weapon = getPlayer():getPrimaryHandItem();
			local valid = shotgunReloadIsValid;
			local start = shotgunReloadStart;
			local perform = shotgunReloadPerform;
			if(getReloadStyle(weapon) == 'revolver') then
				valid = revolverReloadIsValid;
				start = revolverReloadStart;
				perform = revolverReloadPerform;
			elseif(getReloadStyle(weapon) == 'clip') then
				valid = clipReloadIsValid;
				start = clipReloadStart;
				perform = clipReloadPerform;
			end
			reloadAction = TimedActions.Create("Reload", valid, start, perform, stopReload);
			-- remember to factor in marksman time adjustment here
			TimedActions.Start(reloadAction, getPlayer(), (getReloadTime(reloadWeapon)*getPlayer():getReloadingMod())+(panicLevel*30), false, true, getPlayer():getSquare());
			reloadStarted = true;
			rIsPressed = true
			lastTimeRWasPressed = ticks;
		end
	end
end

Events.OnPlayerUpdate.Add(startReload);






--**************************************************
--
--				Racking Action Control
--
--**************************************************

function stopRacking()
	rackingStarted = false;
	rackingAction.javaAction = nil;
end

function canRack()
	local weapon = getPlayer():getPrimaryHandItem();
	if(weapon ~= nil) then
		if(setUpGun(weapon) == true and reloadStarted == false) then
			return true;
		elseif(setUpClip(weapon) == true and reloadStarted == false and getCurrentCapacity(weapon) > 0) then
			return true;
		end
	end
	return false;
end

function rackingIsValid(char, square)
	local weapon = char:getPrimaryHandItem();
	if(weapon ~= nil) then
		if((isGun(weapon) == true or isClip(weapon) == true)and reloadStarted == false) then
			return true;
		end
	end
	rackingStarted = false;
	return false;
end

function rackingStart(char, square)
	rackingStarted = true;
	rackWeapon = char:getPrimaryHandItem();
	getSoundManager():PlayWorldSound(getRackSound(rackWeapon), char:getSquare(), 0, 5, 1.0, false);
end


function rackingPerform(char, square)
	local weapon = rackWeapon;
	-- load a round into the chamber
	--need factory method here
	if(isClip(weapon) == true and getCurrentCapacity(weapon) > 0) then
		getPlayer():getInventory():AddItem(InventoryItem.new('Base','Bullets',getAmmoType(weapon),'text'),1);
		setCurrentCapacity(weapon, getCurrentCapacity(weapon) - 1);
		setUpClip(weapon);
	elseif(getReloadStyle(weapon) == 'revolver') then
		--spin the cylinder
		spinCylinder(weapon);
	elseif(getCurrentCapacity(weapon) > 0) then
		setCurrentCapacity(weapon, getCurrentCapacity(weapon) - 1);
		setRoundChambered(weapon, 1);
	end
	rackingStarted = false;
end

rackingAction = {};
rackingAction = TimedActions.Create("Rack", rackingIsValid, rackingStart, rackingPerform,stopRacking);


function startRacking()
	if(isKeyDown(Keyboard.KEY_SPACE) == true and isKeyDown(Keyboard.KEY_LCONTROL) and spaceIsPressed == false) then
		if(canRack() == true
			and rackingStarted == false) then
			rackWeapon = char:getPrimaryHandItem();
			TimedActions.Start(rackingAction, getPlayer(), getRackTime(getPlayer():getPrimaryHandItem())* getPlayer():getReloadingMod(), false, true, getPlayer():getSquare(), true);
			rackingStarted = true;
			spaceIsPressed = true
			lastTimeSpaceWasPressed = ticks;
		end
	end
end

Events.OnPlayerUpdate.Add(startRacking);


--*******************************************************
--
--				Additional Item Properties
--		(examples of reload type have been provided)

--	Make sure items added are added to the corresponding
--					list at the bottom
--
--*******************************************************

-- name = 'String', 					-- The actual display name of the item. Should match the one in items.txt
-- shotStyle = 'single', 				-- should be either the string 'single' (requires racking after each shot) or 'auto'
-- reloadStyle = 'round',				-- 'round' if rounds are insterted, 'clip' if magazines are inserted
-- ammo = 'ShotgunShells', 				-- the itemname of the ammo that the weapon uses
-- clipName = nil, 						-- If using a clip, the display name of the item
-- clipIcon = nil,	 					-- temporary (but necessary) field. Icons for clips must be specified and put in the media\inventory folder
										-- counter intuitively the icon should also be kept in the weapon that uses the clip not the clip itself
-- clipType = nil,						-- The item type in the module of the clip
-- shootSound = 'stormyShotgunFire', 	-- The sound to play when a shot fires
-- clickSound = 'stormyShotgunClick', 	-- The sound to play when an empty chamber is fired
-- ejectSound = nil, 					-- The sound to play when a magazine is ejected
-- insertSound = 'stormyShotgunInsertRound', -- The sound to play when a round/magazine is inserted
-- rackSound = 'stormyShotgunPump', 	-- The sound to play when racking
-- containsClip = 0, 					-- Whether the weapon contains a clip. 0 = false 1 = true
-- maxAmmo = 8, 						-- The maximum ammo the gun can hold. For magazine based weapons this should equal the clip size
-- reloadTime = 30, 					-- Time it takes to insert a round/insert a magazine/eject a magazine
-- rackTime = 10, 						-- The time it takes to rack the weapon
-- reloadValid = shotgunReloadIsValid, 	-- Function that determines the timed action is valid.
										-- shotgunReloadIsValid for single shot weapons.
										-- clipReloadIsValid for magazine,
										-- revolverReloadIsValid for revolvers
-- reloadStart = shotgunReloadStart, 	-- Function that starts when the timed action is valid
										-- shotgunReloadStart for single shot weapons.
										-- clipReloadStart for magazine,
										-- revolverReloadStart for revolvers
-- reloadPerform = shotgunReloadPerform}; -- Function performed when the time action finishes
										-- shotgunReloadPerform for single shot weapons.
										-- clipReloadPerform for magazine,
										-- revolverReloadPerform for revolvers
-- chamberOne = 0,						-- For revolvers, 0 = EMPTY, 1 = LIVE round
-- chamberTwo = 0,						--
-- chamberThree = 0,					--
-- chamberFour = 0,						--
-- chamberFive = 0,						--
-- chamberSix = 0						--





pistol = {	name = 'Pistol',
			shotStyle = 'auto',
			reloadStyle = 'clip',
			ammo = 'BerettaClip',
			clipName = '9mm Magazine',
			clipIcon = 'BerettaClip',
			shootSound = 'stormy9mmFire',
			clickSound = 'stormy9mmClick',
			ejectSound = 'stormy9mmClipEject',
			insertSound = 'stormy9mmClipLoad',
			rackSound = 'stormy9mmRack',
			containsClip = 1,
			maxAmmo = 15,
			reloadTime = 10,
			rackTime = 10};

pistolClip = {	name = '9mm Magazine',
			clipType = 'BerettaClip',
			shotStyle = 'single',
			reloadStyle = 'round',
			ammo = 'Bullets9mm',
			clipName = 'none',
			shootSound = 'none',
			clickSound = 'none',
			ejectSound = 'none',
			insertSound = 'stormyRevolverInsertRound',
			rackSound = 'stormyRevolverInsertRound',
			containsClip = 0,
			maxAmmo = 15,
			reloadTime = 30,
			rackTime = 10};

-- If you've created an item above you must add
-- it to the relevant list here
weaponsList = {pistol};
clipList = {pistolClip};
