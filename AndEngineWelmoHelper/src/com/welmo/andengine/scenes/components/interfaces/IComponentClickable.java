package com.welmo.andengine.scenes.components.interfaces;

import java.util.ArrayList;

import org.andengine.input.touch.TouchEvent;

import com.welmo.andengine.scenes.descriptors.events.ComponentEventHandlerDescriptor.Events;
import com.welmo.andengine.scenes.descriptors.events.SceneActions.ActionType;

public interface IComponentClickable extends IComponent{
	void addEventsHandler(Events theEvent, IComponentEventHandler oCmpDefEventHandler);
	ArrayList<IComponentEventHandler>  getEventsHandler(Events theEvent);
	//public void setActionOnSceneListener(IActionOnSceneListener actionLeastner);
	public IActionSceneListener getActionOnSceneListener();
	public boolean onTouched(TouchEvent pSceneTouchEvent,float pTouchAreaLocalX, float pTouchAreaLocalY);
	public void onFireEventAction(Events event, ActionType type);
	public boolean onFireEvent(Events event);
}
