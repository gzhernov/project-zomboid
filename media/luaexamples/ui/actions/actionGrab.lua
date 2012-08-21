require 'ui/uiHelpers'
require 'ui/actionUIElement'
require 'ui/actions/takeInventoryActionOptions'

------------------------------------------------------------------------------------------

------------------------------------------------------------------------------------------

TakeInventory = {};

TakeInventory.perform = function(elementTable)
	if elementTable.itemPrimary ~= nil then
		local item = getPlayer():getInventory():FindAndReturn(elementTable.itemPrimary:getFullType());
		if item ~= nil then
			UIManager.setDragInventory(item);
		end
	end
end

TakeInventory.isFinished = function(elementTable)
	return true;
end

TakeInventory.render = function(elementTable, element)
	if elementTable.itemPrimary ~= nil then
		element:DrawTexture(elementTable.usingIcon, 0, 0, 1.0);
	else
		element:DrawTexture(elementTable.texture, 0, 0, 1.0);
	end

	if elementTable.itemPrimary ~= nil then
		textureSec = elementTable.itemPrimary:getTexture();
		element:DrawTexture(textureSec, 0, 0, 32, 32, 1.0);
	end
end


TakeInventory.load = function(self, input)
	if input:readBoolean() then
		self.itemPrimary = InventoryItemFactory.CreateItem(input:readUTF());
	end

end

TakeInventory.save = function(self, output)

	if self.itemPrimary ~= nil then
		output:writeBoolean(true);
		output:writeUTF(self.itemPrimary:getFullType());
	else	
		output:writeBoolean(false);
	end

end

TakeInventory.create = function(doAddToUI)
	local action = UIActions.createNewAction("TakeInventory", "Grab Inventory", "media/ui/actions/Hotkeys_TakeInv.png", TakeInventory.save, TakeInventory.load, TakeInventory.perform, TakeInventory.isFinished, TakeInventory.render, TakeInventoryActionOptions.ui, doAddToUI);
	-- set additional data.
	action.usingIcon = getTexture("media/ui/actions/Hotkeys_BlankTakeInv.png");
	
	action.itemPrimary = nil;
	return action;
end

