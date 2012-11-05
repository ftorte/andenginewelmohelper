package com.welmo.andengine.scenes.descriptors.events;

import java.util.LinkedList;

public class ComponentModifierDescriptor extends BasicModifierDescriptor{
	public enum ModifierType {
		NONE, MOVE, SCALE, CHANGE_COLOR,SOUND;
	}	
	
	// ========================================================
	// Private Members	
	// ========================================================
	//private boolean 						isAList;
	//member accessible if is modifier
	private ModifierType					mModifierType;
	private float 							fScaleFactor;
	private float 							fMoveFactor; 
	private float 							fScaleBegin;
	private float 							fScaleEnd;	
	private int 							stick_with;
	private String							soundName;
		
	// ========================================================
	// Constructor	
	// ========================================================
	public ComponentModifierDescriptor() {
		super();
		mModifierType   = ModifierType.SCALE;
		fScaleFactor 	= 1;
		fScaleBegin		= 1;
		fScaleEnd		= 1;
		fMoveFactor 	= 1; 
		stick_with		= 0;
	};

	// ========================================================
	// Methods Get & Set	
	// ========================================================
	@Override
	public IModifier getIModifier(){
		if ( !isAList){
			return  new IModifier()  {  
				@Override
				public ModifierType getType() {
					return mModifierType;
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
					mModifierType = t;
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
}
