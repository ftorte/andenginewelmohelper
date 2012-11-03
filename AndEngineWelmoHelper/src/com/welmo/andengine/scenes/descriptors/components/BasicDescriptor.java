package com.welmo.andengine.scenes.descriptors.components;

import java.util.HashMap;
import java.util.LinkedList;

import com.welmo.andengine.scenes.descriptors.events.ComponentEventHandlerDescriptor;
import com.welmo.andengine.scenes.descriptors.events.EventHandlerDescriptor;

public class BasicDescriptor {
	public LinkedList<BasicDescriptor> pChild;
	public HashMap<EventHandlerDescriptor.Events,ComponentEventHandlerDescriptor> pEventHandlerList;
	// ***************************************************
	// Constructor
	// ***************************************************
	protected BasicDescriptor(){
		pChild = new LinkedList<BasicDescriptor>();
		pEventHandlerList = new HashMap<EventHandlerDescriptor.Events,ComponentEventHandlerDescriptor>();
	}
	
}
