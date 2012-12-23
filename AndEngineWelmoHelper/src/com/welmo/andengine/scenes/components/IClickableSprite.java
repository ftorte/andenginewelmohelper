package com.welmo.andengine.scenes.components;

import com.welmo.andengine.scenes.descriptors.events.ComponentEventHandlerDescriptor.Events;

public interface IClickableSprite {
	void addEventsHandler(Events theEvent, IComponentEventHandler oCmpDefEventHandler);
	public void setActionOnSceneListener(IActionOnSceneListener actionLeastner);
	public IActionOnSceneListener getActionOnSceneListener();
	public int getID();
	public void setID(int id);
	
}
