require 'ui/uiHelpers'

ActionOptions = {};
ActionOptions.window =
{			
	render = function(elementData, element) 
		if elementData.action ~= nil then
			element:DrawTextCentre("Options for '"..elementData.action.title.."' action", element:getWidth() / 2, 5, 1,1,1,1);
		end
	end,
	
	x = 100,
	y = 100,
	width = 200,
	height = 100,		
	hasClose = true;
	
	clone = function(self)
		
		local window = 
		{
			render = self.render,
			x = self.x,
			y = self.y,
			width = self.width,
			height = self.height,		
			hasClose = self.hasClose;
		}
		
		return window;
		
	end
};	
