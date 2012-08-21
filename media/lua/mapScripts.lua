function AddTrees(cell)
	
	print("Adding trees.");
	for x=0, cell:getWidth()-1 do
		for y=0, cell:getHeight()-1 do
			local square = cell:getGridSquare(x, y, 0);
			n = cell:getGridSquare(x, y-1, 0);
			s = cell:getGridSquare(x, y+1, 0);
			e = cell:getGridSquare(x+1, y, 0);
			w = cell:getGridSquare(x-1, y, 0);
			if square:isCommonGrass() and not square:isZone("NoTreeSpawn") then
			
				local rand = ZombRand(2, 7);
				
				if(rand>3) then
					local vegitation =  IsoObject.new(square, "TileTrees_"..rand, false);
					
					square:AddTileObject(vegitation);			
				
				end
				
				if(ZombRand(5)==0) then
				
					local rand = ZombRand(6);
					
					if(rand==0 or rand==1 or rand==2 or rand==3) then
						rand = -1;
					end
					if(rand==4) then
						rand = ZombRand(4);
					end
					if(rand==5) then
						rand = 16;
					end
					
					if(rand >= 0) then
						local vegitation =  IsoTree.new(square, "TileTrees_"..rand);
						
						square:AddTileObject(vegitation);
						square:RecalcAllWithNeighbours(true);
					end
				end
				
			end
		end
	end

end

function getMainCellLot(x, y)
	
	if x == 50 and y == 50 then
		return "suburbs1";
	end
	if x == 50 and y == 49 then
		return "roadNS";
	end
	if x == 50 and y == 48 then
		return "roadNS_03";
	end
	if x == 50 and y == 47 then
		return "roadNS_02";
	end
	if x == 50 and y == 46 then
		return "roadNS";
	end	
	if x == 49 and y == 48 then
		return "Lot_Farmland";
	end
	if x == 50 and y < 50 then
		return "roadNS";
	end
	
	return "Lot_Rural_00";
end

function getStartZombiesByGrid(x, y)
	if x == 50 and y == 50 then
		return 2750;
	end	
	
	if x==50 and y < 50 then
		return 5;
	end

	if x==49 and y == 48 then
		return 6;
	end
	
	return 2;	
end

function cellSurvivorRemoteness(x, y)
	if x == 50 and y == 50 then
		return 5000;
	end	
	
	if x==49 and y == 48 then
		return 10000;
	end
	
	return 30000;	
end
function cellSurvivorSpawns(x, y)
	if x == 50 and y == 50 then
		return 2;
	end	
	
	if x==49 and y == 48 then
		return 1;
	end
	
	return 0;	
end


function getStartIndoorZombiesByGrid(x, y)

	-- the town
	if x == 50 and y == 50 then
		return 60;
	end	
	
	-- the farm
	if x == 49 and y == 48 then
		return 3;
	end	
	
	return 2;	
end

function getZombieAttractionFactorByGrid(x, y)
	if x == 50 and y == 50 then
		return 1.0;
	end	
	
	
	-- the farm
	if x == 49 and y == 48 then
		return 0.2;
	end	
	if x == 50 and y < 50 then
		return 0.2;
	end	
	
	return 0.1;	
end

function mapLoaded(cell, name, x, y)
	print 'Map Loaded';
	
	local grabnewseed = ZombRand(12345);
	if name ~= 'suburbs1' then
		print 'Doing random vegitation';	
		AddTrees(cell);
	end
	
	if x==50 and y==49 then
		cell:PlaceLot("media/lots/Lot_Rural_Copse_00.lot", 50, 100, 0, true);
		cell:PlaceLot("media/lots/Lot_Rural_TreeCluster_00.lot", 70, 120, 0, true);
		cell:PlaceLot("media/lots/Lot_Rural_TreeCluster_00.lot", 80, 200, 0, true);
		cell:PlaceLot("media/lots/Lot_Rural_Farmhouse_00.lot", 80, 70, 0, true);
		cell:PlaceLot("media/lots/Lot_Rural_FieldCrop_00.lot", 77, 44, 0, true);		
							
	elseif x==50 and y==48 then
		cell:PlaceLot("media/lots/Lot_Rural_Copse_00.lot", 70, 200, 0, true);
		cell:PlaceLot("media/lots/Lot_Rural_TreeCluster_00.lot", 70, 150, 0, true);
		cell:PlaceLot("media/lots/Lot_Rural_TreeCluster_00.lot", 220, 100, 0, true);
		cell:PlaceLot("media/lots/Lot_Rural_Farmhouse_00.lot", 230, 160, 0, true);
	elseif x==49 and y==48 then

	elseif x~=50 then
		if ZombRand(5) == 0 then
			cell:PlaceLot("media/lots/Lot_Rural_Farmhouse_00.lot", 50 + ZombRand(200), 50 + ZombRand(200), 0, true);
		end
		
		for i=0,5 do
			if ZombRand(5)==0 then
					cell:PlaceLot("media/lots/Lot_Rural_Copse_00.lot", ZombRand(300), ZombRand(300), 0, true);
			end
			if ZombRand(5)==0 then
					cell:PlaceLot("media/lots/Lot_Rural_TreeCluster_00.lot", ZombRand(300), ZombRand(300), 0, true);
			end			
		end
	end
	

end

Events.OnPostMapLoad.Add(mapLoaded);