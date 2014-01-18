package com.welmo.andengine.managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.welmo.andengine.scenes.descriptors.events.ComponentEventHandlerDescriptor;
import com.welmo.andengine.scenes.descriptors.events.SceneActions;

public class EventDescriptionsManager {
	
	private static EventDescriptionsManager pInstance = null;
	
	//Manage list of Modifier per eachi objec per each event
	private HashMap<ComponentEventHandlerDescriptor.Events, HashMap<Object,List<ComponentEventHandlerDescriptor>>> modifiers;
	private HashMap<ComponentEventHandlerDescriptor.Events, HashMap<Object,List<SceneActions>>> actions;
	
	private EventDescriptionsManager(){
		modifiers = new HashMap<ComponentEventHandlerDescriptor.Events, HashMap<Object,List<ComponentEventHandlerDescriptor>>>();
		actions = new HashMap<ComponentEventHandlerDescriptor.Events, HashMap<Object,List<SceneActions>>>();
		
	}
	
	public List<SceneActions> getActionList(ComponentEventHandlerDescriptor.Events evt, Object o){
		HashMap<Object,List<SceneActions>> pObjectEvent = actions.get(evt);
		if(pObjectEvent == null)
			return null;
		return pObjectEvent.get(o);
	}
	public List<ComponentEventHandlerDescriptor> getModifierList(ComponentEventHandlerDescriptor.Events evt, Object o){
		HashMap<Object,List<ComponentEventHandlerDescriptor>> pObjectEvent = modifiers.get(evt);
		if(pObjectEvent == null)
			return null;
		return pObjectEvent.get(o);
	}
	
	public void addAction(ComponentEventHandlerDescriptor.Events evt, Object obj, SceneActions act){
		//check if event key exist 
		HashMap<Object,List<SceneActions>> pObjectEvent = actions.get(evt);
		if(pObjectEvent == null)
			actions.put(evt, (pObjectEvent = new HashMap<Object,List<SceneActions>>()));
		
		//check if object key exist
		List<SceneActions> pActions = pObjectEvent.get(obj);
		if(pActions == null)
			pObjectEvent.put(obj, pActions = new ArrayList<SceneActions>());
	
		pActions.add(act);
	}
	
	public void addModifier(ComponentEventHandlerDescriptor.Events evt, Object obj, ComponentEventHandlerDescriptor act){
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
	public static EventDescriptionsManager getInstance(){
		if(pInstance == null){
			return pInstance =  new EventDescriptionsManager();
		}
		return pInstance;
	}
	
}

