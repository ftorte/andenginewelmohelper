package com.welmo.andengine.managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.welmo.andengine.scenes.descriptors.events.EventHandlerDescriptor;
import com.welmo.andengine.scenes.descriptors.events.SceneActionsSet;
import com.welmo.andengine.scenes.descriptors.events.ComponentEventHandlerDescriptor;

public class EventHandlersManager {
	
	//Manage list of Modifier per eachi objec per each event
	private HashMap<EventHandlerDescriptor.Events, HashMap<Object,List<ComponentEventHandlerDescriptor>>> modifiers;
	private HashMap<EventHandlerDescriptor.Events, HashMap<Object,List<SceneActionsSet>>> actions;
	
	public EventHandlersManager(){
		modifiers = new HashMap<EventHandlerDescriptor.Events, HashMap<Object,List<ComponentEventHandlerDescriptor>>>();
		actions = new HashMap<EventHandlerDescriptor.Events, HashMap<Object,List<SceneActionsSet>>>();
	}

	public List<SceneActionsSet> getActionList(EventHandlerDescriptor.Events evt, Object o){
		HashMap<Object,List<SceneActionsSet>> pObjectEvent = actions.get(evt);
		if(pObjectEvent == null)
			return null;
		return pObjectEvent.get(o);
	}
	public List<ComponentEventHandlerDescriptor> getModifierList(EventHandlerDescriptor.Events evt, Object o){
		HashMap<Object,List<ComponentEventHandlerDescriptor>> pObjectEvent = modifiers.get(evt);
		if(pObjectEvent == null)
			return null;
		return pObjectEvent.get(o);
	}
	
	public void addAction(EventHandlerDescriptor.Events evt, Object obj, SceneActionsSet act){
		//check if event key exist 
		HashMap<Object,List<SceneActionsSet>> pObjectEvent = actions.get(evt);
		if(pObjectEvent == null)
			actions.put(evt, (pObjectEvent = new HashMap<Object,List<SceneActionsSet>>()));
		
		//check if object key exist
		List<SceneActionsSet> pActions = pObjectEvent.get(obj);
		if(pActions == null)
			pObjectEvent.put(obj, pActions = new ArrayList<SceneActionsSet>());
	
		pActions.add(act);
	}
	
	public void addModifier(EventHandlerDescriptor.Events evt, Object obj, ComponentEventHandlerDescriptor act){
		//check if event key exist 
		HashMap<Object,List<ComponentEventHandlerDescriptor>> pObjectEvent = modifiers.get(evt);
		if(pObjectEvent == null)
			modifiers.put(evt, (pObjectEvent = new HashMap<Object,List<ComponentEventHandlerDescriptor>>()));
		
		//check if object key exist
		List<ComponentEventHandlerDescriptor> pActions = pObjectEvent.get(obj);
		if(pActions == null)
			pObjectEvent.put(obj, pActions = new ArrayList<ComponentEventHandlerDescriptor>());
	
		pActions.add(act);
	}
}
