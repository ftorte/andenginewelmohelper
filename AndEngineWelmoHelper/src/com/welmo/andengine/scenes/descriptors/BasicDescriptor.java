package com.welmo.andengine.scenes.descriptors;

import java.util.HashMap;
import java.util.Map;

import org.xml.sax.Attributes;

import com.welmo.andengine.scenes.descriptors.events.ComponentEventHandlerDescriptor;

public class BasicDescriptor {
	public HashMap<ComponentEventHandlerDescriptor.Events,ComponentEventHandlerDescriptor> pEventHandlerList;
	protected String 						className=""; 		// specific class name to implement the described component
	protected int 							ID=0;				// and ID to identify the component
	protected String 						sOnClickMessage="";	// messsage
	protected String 						sSubType="";		// specific SubTyep
	public Map<Integer,BasicDescriptor> 	pChild;				// attached object child
	protected boolean						isTemplate = false;
	protected Integer						isInstanceOfID = 0;
	
	// ***************************************************
	// Constructor
	// ***************************************************
	protected BasicDescriptor(){
		pChild 				= new HashMap<Integer,BasicDescriptor>();
		pEventHandlerList 	= new HashMap<ComponentEventHandlerDescriptor.Events,ComponentEventHandlerDescriptor>();
	}
	public boolean isTemplate() {
		return this.isTemplate;
	}
	public void isTemplate(boolean value) {
		this.isTemplate = value;
	}
	public Integer isIstanceOfID() {
		return this.isInstanceOfID;
	}
	public void isIstanceOfID(Integer value) {
		this.isInstanceOfID = value;
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
		className			= new String(copyfrom.className);
		ID					= copyfrom.ID;
		sOnClickMessage 	= copyfrom.sOnClickMessage;
		sSubType			= copyfrom.sSubType;		
		isTemplate 			= copyfrom.isTemplate;
		//handle hash maps
		pEventHandlerList.clear();
		pEventHandlerList.putAll(copyfrom.pEventHandlerList);
		pChild.clear();				
		pChild.putAll(copyfrom.pChild);
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
		if((value = attributes.getValue(ScnTags.S_IS_TEMPLATE))!=null) 			this.isTemplate = Boolean.parseBoolean(attributes.getValue(ScnTags.S_IS_TEMPLATE));
		
	}
	public void instantiateXMLDescription(BasicDescriptor instantiateFromObject, Attributes attributes) {
		//copy for template
		this.instantiateFrom(instantiateFromObject);
		//read parameter to change all customized values
		this.readXMLDescription(attributes);
		
	}
	protected void instantiateFrom(BasicDescriptor instantiateFromObject){
		this.copyFrom(instantiateFromObject);
		isTemplate 		= false;
	}
	public String getSubType() {
		return sSubType;
	}
	public void setSubType(String type) {
		sSubType = new String(type);
	}
}
