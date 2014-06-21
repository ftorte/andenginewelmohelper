package com.welmo.andengine.scenes.components.interfaces;

import org.andengine.input.touch.TouchEvent;

import com.welmo.andengine.scenes.descriptors.events.ComponentEventHandlerDescriptor.Events;
import com.welmo.andengine.scenes.descriptors.events.SceneActions.ActionType;

public interface IComponentClickable extends IComponent{
	void addEventsHandler(Events theEvent, IComponentEventHandler oCmpDefEventHandler);
	//public void setActionOnSceneListener(IActionOnSceneListener actionLeastner);
	public IActionOnSceneListener getActionOnSceneListener();
	public boolean onTouched(TouchEvent pSceneTouchEvent,float pTouchAreaLocalX, float pTouchAreaLocalY);
	public void onFireEventAction(Events event, ActionType type);
}
