package com.welmo.andengine.scenes.descriptors.components;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.xml.sax.Attributes;

import com.welmo.andengine.scenes.descriptors.events.ComponentEventHandlerDescriptor;
import com.welmo.andengine.scenes.descriptors.events.ComponentEventHandlerDescriptor;

public class BasicDescriptor {
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
		this.className = new String(className);
	}
	public void copyFrom(BasicDescriptor copyfrom) {
		ID			= copyfrom.ID;
		className 	= new String(copyfrom.className);
	}
	public void readXMLDescription(Attributes attributes) {
		//variable containing attributes value
		String value;
		
		if((value = attributes.getValue(ScnTags.S_A_ID))!=null) this.setID(Integer.parseInt(value));
		if((value = attributes.getValue(ScnTags.S_A_CLASSNAME))!=null) this.setClassName(value);
				
	}
}
