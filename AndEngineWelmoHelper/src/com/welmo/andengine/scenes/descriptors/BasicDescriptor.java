package com.welmo.andengine.scenes.descriptors;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.xml.sax.Attributes;

import com.welmo.andengine.scenes.descriptors.components.ScnTags;
import com.welmo.andengine.scenes.descriptors.events.ComponentEventHandlerDescriptor;
import com.welmo.andengine.scenes.descriptors.events.ComponentEventHandlerDescriptor;

public class BasicDescriptor {
	public HashMap<ComponentEventHandlerDescriptor.Events,ComponentEventHandlerDescriptor> pEventHandlerList;
	protected String 						className=""; 		// specific class name to implement the described component
	protected int 							ID=0;				// and ID to identify the component
	protected String 						sOnClickMessage="";	// messsage
	protected String 						sSubType="";		// specific SubTyep
	public Map<Integer,BasicDescriptor> 	pChild;				// attached object child
	
	// ***************************************************
	// Constructor
	// ***************************************************
	protected BasicDescriptor(){
		pChild 				= new HashMap<Integer,BasicDescriptor>();
		pEventHandlerList 	= new HashMap<ComponentEventHandlerDescriptor.Events,ComponentEventHandlerDescriptor>();
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
		//TODO missing copy for children and event handlers list
	}
	public String getOnClickMessage() {
		return sOnClickMessage;
	}
	public void setOnClickMessage(String msg) {
		sOnClickMessage = new String (msg);
	}
	public void readXMLDescription(Attributes attributes) {
		//variable containing attributes value
		String value;
		if((value = attributes.getValue(ScnTags.S_A_ID))!=null) 				this.setID(Integer.parseInt(value));
		if((value = attributes.getValue(ScnTags.S_A_CLASSNAME))!=null) 			this.setClassName(value);
		if((value = attributes.getValue(ScnTags.S_A_ON_CLIK_MESSAGE))!=null)	this.sOnClickMessage=new String(attributes.getValue(ScnTags.S_A_ON_CLIK_MESSAGE));				
		if((value = attributes.getValue(ScnTags.S_A_TYPE))!=null) 				this.sSubType=new String(attributes.getValue(ScnTags.S_A_TYPE));				
	}
	public String getSubType() {
		return sSubType;
	}
	public void setSubType(String type) {
		sSubType = new String(type);
	}
}
