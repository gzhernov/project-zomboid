-- helper lua file for doing timed actions...
require 'timedactionshelper'

-- declare a few variables to use.
mineList = {}
buildMineAction = {}
mineCount = 0;

-- function that repeatedly gets called and says if timed action is still valid (i.e. we still have an apple to make the mine with)
function buildMineIsValid(char, square)
	return char:getInventory():contains("Apple");
end

-- What to do when timed action is started. In this case loop the hammering sound.
function buildMineStart(char, square)
	buildMineAction:playLoopedSoundTillComplete("hammernail", 10, 0.4); -- name, radius, volume
end

-- function that is called once the timed action reaches complete. In this case, place the mine.
function buildMinePerform(char, square)
	-- Create a new iso object called 'Mine' and add it to the tile.
	-- Using the black floor tile again.
	local mine =  IsoObject.new(square, "TileFloorInt_7", "Mine");
	-- Set a countdown so the player can get off the tile in time.
	local data = mine:getModData();
	data.countdownTillArm = 120;
	data.exploded = false;
	square:AddTileObject(mine);
	-- Store the mines so we can update their countdown times and check for detonation...
	mineList[mineCount + 1] = mine;
	mineCount = mineCount + 1;
	local inventoryItem = char:getInventory():FindAndReturn("Apple");
	inventoryItem:Use(); -- use up the Apple.
end

-- Create the timed action, give it a name, and point it at the valid, start and perform functions.
buildMineAction = TimedActions.Create("BuildMine", buildMineIsValid, buildMineStart, buildMinePerform);

-- Explode a mine when a character steps on it.
function ExplodeMine(mine, square)
	
	local radius = 3;
	local numFires = 2;

	local cell = getWorld():getCell();

	local objects = cell:getLuaObjectList();
	for k,v in ipairs(objects) do
		if square:DistTo(v) < radius and v:isCharacter() and v:isDead() == false then
			v:Kill(nil);
		end		
	end
	
	getSoundManager():PlayWorldSound("explode", square, 0, 20, 1.0, false);
	
	for i = 0, numFires do
			local sq = cell:getGridSquare(ZombRand(square:getX()-radius, square:getX()+radius), 
												ZombRand(square:getY()-radius, square:getY()+radius), 
													square:getZ() );
													
			if sq ~= nil then				
				sq:StartFire();			
			end
		end
	
end

-- Update all mines in world and explode any with characters stood on their square.
function UpdateMine(mine)

	local square = mine:getSquare();
	
	local data = mine:getModData();
	
	if data.exploded then
		return;
	end
	-- If still arming then continue arming...
	if data.countdownTillArm > 0 then		
		data.countdownTillArm = data.countdownTillArm - 1;
	else
		local objects = square:getLuaMovingObjectList();
		
		if objects ~= nil then
			if not table.isempty(objects) then
				data.exploded = true;
				ExplodeMine(mine, square);				
				square:RemoveTileObject(mine);
			end
		end
		
	end
	
	return false;
	
end

-- Called once per frame to update mines.
function Tick(totalTicks)
	
	for k,o in ipairs(mineList) do
	
		UpdateMine(o);
		
	end
	
end

-- Do tile building. isRender is true when we should draw the ghost image of what is to be built.
-- isRender = false when we should actually start / do the building. inventoryItem is the item on the cursor 
-- that should define what you're building.
function DoTileBuilding(isRender, x, y, z, square, inventoryItem)
	
	if inventoryItem:getType() == "Apple" then
		DoMineBuilding(isRender, x, y, z, square, inventoryItem);
	end
	
end


-- Do code for building mines. If isRender is true, draw the mineSprite. Else start the timed action.
mineSprite = nil;
function DoMineBuilding(isRender, x, y, z, square, inventoryItem)

	--	If the tile is not free (not counting characters) then it's not valid, so return.
	if not square:isFree(false) then
		return;
	end
	
	-- If we're just rendering it in position, draw the ghost image of it.
	if isRender then
		-- mine sprite has not been created, so create it...
		if mineSprite == nil then			
			print("Creating mine tile sprite");
			mineSprite = IsoSprite.new();
			-- load the sprite - in this case just using a black floor tile
			-- We could easily draw a new tile for this.
			mineSprite:LoadFramesNoDirPageSimple("TileFloorInt_7");
		end
		
		-- Draw the ghost tile.
		mineSprite:RenderGhostTile(x, y, z);
	else
		print("Placing mine.");
		-- Get the player and start the timed action for building a mine on it. 90 = 3 seconds (non frame comp)
		local player = getPlayer();
		TimedActions.Start(buildMineAction, player, 90, square);

	end	
end

Events.OnTick.Add(Tick);

Events.OnDoTileBuilding.Add(DoTileBuilding);