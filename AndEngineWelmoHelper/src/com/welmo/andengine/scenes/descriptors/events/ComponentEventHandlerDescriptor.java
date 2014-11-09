package com.welmo.andengine.scenes.descriptors.events;

import java.util.LinkedList;

import org.xml.sax.Attributes;

import com.welmo.andengine.scenes.descriptors.ScnTags;

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
	
	
	public void readXMLDescription(Attributes attributes){ 

		if(attributes.getValue(ScnTags.S_A_ID) != null)
			this.setID(Integer.parseInt(attributes.getValue(ScnTags.S_A_ID)));
		
		if(attributes.getValue(ScnTags.S_A_CLONEID) != null)
			this.setCloneID(Integer.parseInt(attributes.getValue(ScnTags.S_A_CLONEID)));
		
		this.event=ComponentEventHandlerDescriptor.Events.valueOf(attributes.getValue(ScnTags.S_A_EVENT));
		
	}
	
}
