package com.welmo.andengine.scenes.descriptors.events;

import java.util.LinkedList;

import com.welmo.andengine.scenes.descriptors.components.BasicDescriptor;

public class EventHandlerDescriptor extends BasicDescriptor{
	public static enum Events {
		NO_EVENT, ON_MOVE, ON_CLICK
	}
	public enum SetType {
		PARALLEL, SEQUENCE;
	}
	public Events 								event;
	public SetType 								type; 
	public LinkedList<ActivitySetDescriptor> 	ActivityList;
	public int ID;
	// ========================================================
	// Constructor	
	// ========================================================
	public EventHandlerDescriptor() {
		super();
		ID = -1;
		type = SetType.SEQUENCE;
		event = Events.NO_EVENT;
		ActivityList = new  LinkedList<ActivitySetDescriptor>();
	}
}
