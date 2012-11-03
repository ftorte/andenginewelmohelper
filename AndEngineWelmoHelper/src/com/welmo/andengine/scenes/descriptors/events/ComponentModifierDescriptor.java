package com.welmo.andengine.scenes.descriptors.events;

import java.util.LinkedList;

import com.welmo.andengine.scenes.descriptors.components.BasicDescriptor;
import com.welmo.andengine.scenes.descriptors.events.ComponentEventHandlerDescriptor.ModifiersListType;


public class ComponentModifierDescriptor extends EventHandlerDescriptor{
	public enum ModifierType {
		NONE, MOVE, SCALE, CHANGE_COLOR,SOUND;
	}	
	public interface IModifier{
		public ModifierType getType();
		public float getScaleFactor();
		public float getScaleBegin();
		public float getScaleEnd();
		public float getMoveFactor();
		public int getStickFactor();
		public String getSoundName();

		public void setType(ModifierType m);
		public void setScaleFactor(float sf);
		public void setScaleBegin(float sf);
		public void setScaleEnd(float sf);
		public void setMoveFactor(float mf);
		public void setStickWidth(int sw);
		public void setSoundName(String sound);
		
	}
	public interface IModifierList{
		public LinkedList<ComponentModifierDescriptor> getModifiers();
		public ModifiersListType getModiferListType();
		
		public void setModifierListType(ModifiersListType mt);
	}
	
	// ========================================================
	// Private Members	
	// ========================================================
	private boolean 						isAList;
	//member accessible if is modifier
	private ModifierType 					type;
	private float 							fScaleFactor;
	private float 							fMoveFactor; 
	private float 							fScaleBegin;
	private float 							fScaleEnd;	
	private int 							stick_with;
	private String							soundName;
	
	//member accessible if is modifier list
	private LinkedList<ComponentModifierDescriptor> 	llModifierSetList;
	private ModifiersListType 				eSetType;
	
	// ========================================================
	// Constructor	
	// ========================================================
	public ComponentModifierDescriptor() {
		super();
		eSetType 		= ModifiersListType.SEQUENCE;
		isAList 		= false;
		type 			= ModifierType.NONE;
		fScaleFactor 	= 1;
		fScaleBegin		= 1;
		fScaleEnd		= 1;
		fMoveFactor 	= 1; 
		stick_with		= 0;
		llModifierSetList = null;
	};

	// ========================================================
	// Methods Get & Set	
	// ========================================================
	public boolean isASet() { return isAList;}
	public void setIsASet(boolean isASet) {
		this.isAList = isASet;
		if(isAList){
			if(llModifierSetList == null)
				llModifierSetList = new LinkedList<ComponentModifierDescriptor>();
			else
				llModifierSetList.clear();
		}
		if(!isAList)
			if(llModifierSetList != null)
						llModifierSetList.clear();
	}
	
	public IModifier getIModifier(){
		if ( !isAList){
			return  new IModifier()  {  
				@Override
				public ModifierType getType() {
					return type;
				}
				@Override
				public float getScaleFactor() {
					return fScaleFactor;
				}
				@Override
				public float getMoveFactor() {
					return fMoveFactor;
				}
				@Override
				public int getStickFactor() {
					return stick_with;
				}
				@Override
				public void setType(ModifierType t) {
					type = t;
				}
				@Override
				public void setScaleFactor(float sf) {
					fScaleFactor = sf;
				}
				@Override
				public void setMoveFactor(float mf) {
					fMoveFactor = mf;			
				}
				@Override
				public void setStickWidth(int sw) {
					stick_with = sw;				
				}
				@Override
				public float getScaleBegin() {
					return fScaleBegin;
				}
				@Override
				public float getScaleEnd() {
					return fScaleEnd;
				}
				@Override
				public void setScaleBegin(float sf) {
					fScaleBegin = sf;
				}
				@Override
				public void setScaleEnd(float sf) {
					fScaleEnd = sf;
				}
				@Override
				public String getSoundName() {
					return soundName;
				}
				@Override
				public void setSoundName(String sound) {
					soundName = new String(sound);
				}
			};
		}
		else{ 
			throw new NullPointerException("Invalid Call of getIModifier for a modifier of type list");
		}
	}
	public IModifierList getIModifierList(){
		if ( !isAList){
			return  new IModifierList()  {
				@Override
				public LinkedList<ComponentModifierDescriptor> getModifiers() {
					return llModifierSetList;
				}
				@Override
				public ModifiersListType getModiferListType() {
					return eSetType;
				}
				@Override
				public void setModifierListType(ModifiersListType mt) {
					eSetType = mt;
				} };
		}
		else{ 
			throw new NullPointerException("Invalid Call of getIModifierList for a modifier which is not a list");
		}
	}
}
