package com.welmo.andengine.scenes.descriptors.components;

import java.util.HashMap;
import java.util.LinkedList;

import com.welmo.andengine.scenes.descriptors.events.ComponentEventHandlerDescriptor;
import com.welmo.andengine.scenes.descriptors.events.ComponentEventHandlerDescriptor;

public class BasicDescriptor {
	public LinkedList<BasicDescriptor> pChild;
	public HashMap<ComponentEventHandlerDescriptor.Events,ComponentEventHandlerDescriptor> pEventHandlerList;
	protected String className="";
	// ***************************************************
	// Constructor
	// ***************************************************
	protected BasicDescriptor(){
		pChild = new LinkedList<BasicDescriptor>();
		pEventHandlerList = new HashMap<ComponentEventHandlerDescriptor.Events,ComponentEventHandlerDescriptor>();
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public void copyFrom(BasicDescriptor copyfrom) {
		className 	= new String(copyfrom.className);
	}
}
