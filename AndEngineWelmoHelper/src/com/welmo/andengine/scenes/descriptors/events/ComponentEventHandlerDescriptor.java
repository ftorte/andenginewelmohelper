package com.welmo.andengine.scenes.descriptors.events;

import java.util.LinkedList;

import com.welmo.andengine.scenes.descriptors.components.BasicDescriptor;

public class ComponentEventHandlerDescriptor extends BasicModifierDescriptor{
	public static enum Events {
		NO_EVENT, ON_MOVE, ON_CLICK
	}
	public Events 								event;
	public ComponentModifierListDescriptor		modifierSet=null;
	public LinkedList<SceneActions>				preModAction=null;
	public LinkedList<SceneActions>				postModAction=null;
	public LinkedList<SceneActions>				onModAction=null;
	public int cloneID;
	
	public int getCloneID() {
		return cloneID;
	}

	public void setCloneID(int cloneID) {
		this.cloneID = cloneID;
	}

	// ========================================================
	// Constructor	
	// ========================================================
	public ComponentEventHandlerDescriptor() {
		super();
		ID = -1;
		//enExecOrder = ExecutionOrder.SERIAL;
		event = Events.NO_EVENT;
		cloneID = -1;
		postModAction = new LinkedList<SceneActions>();
		preModAction = new LinkedList<SceneActions>();
		onModAction = new LinkedList<SceneActions>();
	}
}
