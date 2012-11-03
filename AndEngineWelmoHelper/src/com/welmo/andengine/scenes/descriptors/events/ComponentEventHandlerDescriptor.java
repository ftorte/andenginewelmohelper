package com.welmo.andengine.scenes.descriptors.events;

import java.util.LinkedList;

import com.welmo.andengine.scenes.descriptors.components.BasicDescriptor;


//inner class to manage modifiers and actions
public class ComponentEventHandlerDescriptor extends EventHandlerDescriptor{
	public enum ModifiersListType {
		PARALLEL, SEQUENCE;
	}	
	public Events 							event = Events.NO_EVENT;
	public ModifiersListType 				modifierListType =  ModifiersListType.SEQUENCE; 
	public LinkedList<ComponentModifierDescriptor> 	ModifiersList;
	public int ID;
	// ========================================================
	// Constructor	
	// ========================================================
	public ComponentEventHandlerDescriptor() {
		super();
		ID = -1;
		ModifiersList = new  LinkedList<ComponentModifierDescriptor>();
	}
}