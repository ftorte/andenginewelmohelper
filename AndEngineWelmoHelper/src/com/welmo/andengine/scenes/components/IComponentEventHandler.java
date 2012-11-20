package com.welmo.andengine.scenes.components;

import org.andengine.entity.IEntity;

import com.welmo.andengine.scenes.descriptors.events.ComponentEventHandlerDescriptor;

public interface IComponentEventHandler {
		public void setUpEventsHandler(ComponentEventHandlerDescriptor entry);
		public void handleEvent(IEntity pItem);
		public IComponentEventHandler cloneEvent(ComponentEventHandlerDescriptor entry);
		public int getID();
}
