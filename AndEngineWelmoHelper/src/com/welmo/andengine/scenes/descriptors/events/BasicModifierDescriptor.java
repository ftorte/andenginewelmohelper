package com.welmo.andengine.scenes.descriptors.events;

import java.util.LinkedList;

import org.xml.sax.Attributes;

import com.welmo.andengine.scenes.descriptors.ScnTags;
import com.welmo.andengine.scenes.descriptors.events.ComponentModifierDescriptor.ModifierType;

public abstract class BasicModifierDescriptor {
	public interface IModifier{
		public ModifierType getType();
		public float getScaleFactor();
		public float getScaleBegin();
		public float getScaleEnd();
		public float getMoveFactor();
		public int getStickFactor();
		public String getSoundName();
		public float getDuration();	
		
		public void setType(ModifierType m);
		public void setScaleFactor(float sf);
		public void setScaleBegin(float sf);
		public void setScaleEnd(float sf);
		public void setMoveFactor(float mf);
		public void setStickWidth(int sw);
		public void setSoundName(String sound);
		public void setDuration(float duration);	
	}
	public interface IModifierList{
		public LinkedList<ComponentModifierDescriptor> getModifiers();
		public ExecutionOrder getExecOrder();
		public void setExecOrder(ExecutionOrder mt);
	}
	
	// ========================================================
	// Private Members	
	// ========================================================
	protected boolean 	isAList=false;
	protected int 		ID=-1;

	// ========================================================
	// Constructor	
	// ========================================================
	BasicModifierDescriptor(){
		isAList 		= false;
	}
	// ========================================================
	// Constructor	
	// ========================================================
	boolean isASet(){
		return isAList;
	}
	public IModifier getIModifier(){
		return null;
	}
	public IModifierList getIModifierList(){
		return null;
	}
	public int getID() {
		return ID;
	}
	public void setID(int iD) {
		if(iD<0)
			throw new IllegalArgumentException("Negative value for ID is not accepted");
		ID = iD;
	}
	abstract public void readXMLDescription(Attributes attributes);
}
