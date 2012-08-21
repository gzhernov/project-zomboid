require 'ui/uiHelpers'

EquipActionOptions = {};
EquipActionOptions.ui =
{		
	render = function(elementData, element) 
		element:DrawTextRight("Primary slot:", 110, 7, 1,1,1,1);
		element:DrawTextRight("Secondary slot:", 110, 42, 1,1,1,1);
	end,
	itemAData = {},
	itemBData = {},
	onPlaceItemPrimary = function(elementData, item)
		print("Setting primary option to "..item:getFullType());
		elementData.parentData.action.itemPrimary = InventoryItemFactory.CreateItem(item:getFullType());
	end,
	onPlaceItemSecondary = function(elementData, item)
		print("Setting secondary option to "..item:getFullType());
		elementData.parentData.action.itemSecondary = InventoryItemFactory.CreateItem(item:getFullType());	
	end,
	
	doCreation = function(self)

		self.itemA = VirtualItemSlot.new("EquipActionPrimary", 130, 0, "ItemBackground_Blue", getPlayer());
		self.itemB = VirtualItemSlot.new("EquipActionSecondary", 130, 35, "ItemBackground_Blue", getPlayer());
		self.itemAData.parentData = self;
		self.itemBData.parentData = self;		
		self.itemAData.onPlaceItem = self.onPlaceItemPrimary;
		self.itemBData.onPlaceItem = self.onPlaceItemSecondary;
		self.itemA:setTable(self.itemAData);
		self.itemB:setTable(self.itemBData);
		self.javaObject:AddChild(self.itemA);
		self.javaObject:AddChild(self.itemB);
	end,
	x = 5,
	y = 32,
	width = 200,
	height = 130;
};	
