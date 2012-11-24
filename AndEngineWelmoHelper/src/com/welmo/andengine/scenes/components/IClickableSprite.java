package com.welmo.andengine.scenes.component;

import com.welmo.andengine.scenes.descriptors.events.ComponentEventHandlerDescriptor.Events;

public interface IClickableSprite {
	void addEventsHandler(Events theEvent, IComponentEventHandler oCmpDefEventHandler);
	public void setActionOnSceneListener(IActionOnSceneListener actionLeastner);
	public IActionOnSceneListener getActionOnSceneListener();
}
