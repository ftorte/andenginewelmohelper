package com.welmo.andengine.scenes.components;

import org.andengine.entity.IEntity;
import org.andengine.input.touch.TouchEvent;

import com.welmo.andengine.scenes.descriptors.events.ComponentEventHandlerDescriptor;
import com.welmo.andengine.scenes.descriptors.events.SceneActions.ActionType;

public interface IComponentEventHandler {
		public void setUpEventsHandler(ComponentEventHandlerDescriptor entry);
		public void handleEvent(IEntity pItem, TouchEvent pSceneTouchEvent, TouchEvent lastTouchEvent);
		public IComponentEventHandler cloneEvent(ComponentEventHandlerDescriptor entry);
		public int getID();
		public void onFireAction(ActionType type, IEntity pItem);
}
