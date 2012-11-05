package com.welmo.andengine.scenes.descriptors.events;

import java.util.LinkedList;

import com.welmo.andengine.scenes.descriptors.components.BasicDescriptor;

public class ComponentEventHandlerDescriptor extends BasicModifierDescriptor{
	public static enum Events {
		NO_EVENT, ON_MOVE, ON_CLICK
	}
	public Events 								event;
	//public ExecutionOrder 						enExecOrder; 
	
	public ComponentModifierListDescriptor		modifierSet=null;
	public SceneActions							preModAction=null;
	public SceneActions							postModAction=null;
	public SceneActions							onModAction=null;
	
	public int ID;
	// ========================================================
	// Constructor	
	// ========================================================
	public ComponentEventHandlerDescriptor() {
		super();
		ID = -1;
		//enExecOrder = ExecutionOrder.SERIAL;
		event = Events.NO_EVENT;
	}
}
