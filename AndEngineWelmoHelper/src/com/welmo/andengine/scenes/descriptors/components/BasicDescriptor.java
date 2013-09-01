package com.welmo.andengine.scenes.descriptors.components;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import com.welmo.andengine.scenes.descriptors.events.ComponentEventHandlerDescriptor;
import com.welmo.andengine.scenes.descriptors.events.ComponentEventHandlerDescriptor;

public class BasicDescriptor {
	//FT public LinkedList<BasicDescriptor> pChild;
	public Map<Integer,BasicDescriptor> pChild;
	public HashMap<ComponentEventHandlerDescriptor.Events,ComponentEventHandlerDescriptor> pEventHandlerList;
	protected String className="";
	protected int ID=0;
	
	// ***************************************************
	// Constructor
	// ***************************************************
	protected BasicDescriptor(){
		//FT pChild = new LinkedList<BasicDescriptor>();
		pChild = new HashMap<Integer,BasicDescriptor>();
		pEventHandlerList = new HashMap<ComponentEventHandlerDescriptor.Events,ComponentEventHandlerDescriptor>();
	}
	public int getID() {
		return ID;
	}
	public void setID(int iD) {
		ID = iD;
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
