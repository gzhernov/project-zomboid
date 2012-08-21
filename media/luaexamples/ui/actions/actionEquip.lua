require 'ui/uiHelpers'
require 'ui/actionUIElement'
require 'ui/actions/equipActionOptions'

------------------------------------------------------------------------------------------

EquipAction = {};

EquipAction.perform = function(elementTable)
	if elementTable.itemPrimary ~= nil then
		local item = getPlayer():getInventory():FindAndReturn(elementTable.itemPrimary:getFullType());
		if item ~= nil then
			getPlayer():setPrimaryHandItem(item);
		end
	end
	if elementTable.itemSecondary ~= nil then
		local item = getPlayer():getInventory():FindAndReturn(elementTable.itemSecondary:getFullType());
		if item ~= nil then
			getPlayer():setSecondaryHandItem(item);
		end
	end
end

EquipAction.isFinished = function(elementTable)
	return true;
end

EquipAction.render = function(elementTable, element)

	if elementTable.itemSecondary ~= nil or elementTable.itemPrimary ~= nil then
		element:DrawTexture(elementTable.usingIcon, 0, 0, 1.0);
	else
		element:DrawTexture(elementTable.texture, 0, 0, 1.0);
	end
	
	if elementTable.itemSecondary ~= nil then
		textureSec = elementTable.itemSecondary:getTexture();
		element:DrawTexture(textureSec, 32-24, 32-24, 24, 24, 1.0);
	end
	if elementTable.itemPrimary ~= nil then
		texturePrim = elementTable.itemPrimary:getTexture();
		element:DrawTexture(texturePrim, -5, -5, 32, 32, 1.0);
	end
end

EquipAction.load = function(self, input)
	if input:readBoolean() then
		self.itemPrimary = InventoryItemFactory.CreateItem(input:readUTF());
	end
	if input:readBoolean() then
		self.itemSecondary = InventoryItemFactory.CreateItem(input:readUTF());
	end
end

EquipAction.save = function(self, output)

	if self.itemPrimary ~= nil then
		output:writeBoolean(true);
		output:writeUTF(self.itemPrimary:getFullType());
	else	
		output:writeBoolean(false);
	end
	if self.itemSecondary ~= nil then
		output:writeBoolean(true);
		output:writeUTF(self.itemSecondary:getFullType());
	else	
		output:writeBoolean(false);
	end
end

EquipAction.create = function(doAddToUI)
	local action = UIActions.createNewAction("Equip", "Equip items", "media/ui/actions/Hotkeys_Equip.png", EquipAction.save, EquipAction.load, EquipAction.perform, EquipAction.isFinished, EquipAction.render, EquipActionOptions.ui, doAddToUI);
	-- set additional data.
	action.usingIcon = getTexture("media/ui/actions/Hotkeys_BlankEquip.png");
	action.itemPrimary = nil;
	action.itemSecondary = nil;
	return action;
end

------------------------------------------------------------------------------------------
