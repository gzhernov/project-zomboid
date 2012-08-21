require 'ui/actions/actionGrab'
require 'ui/actions/actionEquip'

ActionFactory = {};
ActionFactory.createFromName = function(name)

	if name == "TakeInventory" then
		return TakeInventory.create(false);
	end
	if name == "Equip" then
		return EquipAction.create(false);
	end
	
	return nil;

end