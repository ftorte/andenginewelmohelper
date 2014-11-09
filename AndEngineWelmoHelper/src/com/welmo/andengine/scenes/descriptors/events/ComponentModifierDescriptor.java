package com.welmo.andengine.scenes.descriptors.events;

import org.xml.sax.Attributes;

import android.util.Log;

import com.welmo.andengine.scenes.descriptors.ScnTags;


public class ComponentModifierDescriptor extends BasicModifierDescriptor{
	public enum ModifierType {
		NONE, MOVE, SCALE, CHANGE_COLOR,SOUND,FOLLOW;
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
	private float							mDuration;
		
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
		mDuration		= 1;
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
				@Override
				public float getDuration() {
					// TODO Auto-generated method stub
					return mDuration;
				}
				@Override
				public void setDuration(float pDuration) {
					mDuration = pDuration;
				}
			};
		}
		else{ 
			throw new NullPointerException("Invalid Call of getIModifier for a modifier of type list");
		}
	}
	
	public void readXMLDescription(Attributes attributes){ 

		this.getIModifier().setType(ComponentModifierDescriptor.ModifierType.valueOf(attributes.getValue(ScnTags.S_A_TYPE)));
		switch(this.getIModifier().getType()){
		case SCALE:
			this.getIModifier().setScaleBegin(Float.parseFloat(attributes.getValue(ScnTags.S_A_SCALE_BEGIN)));
			this.getIModifier().setScaleEnd(Float.parseFloat(attributes.getValue(ScnTags.S_A_SCALE_END)));
			if(attributes.getValue(ScnTags.S_A_DURATION) != null)
				this.getIModifier().setDuration(Float.parseFloat(attributes.getValue(ScnTags.S_A_DURATION)));
			break;
		case SOUND:
			this.getIModifier().setSoundName(attributes.getValue(ScnTags.S_A_NAME));
			break;
		case MOVE:
			if(attributes.getValue(ScnTags.S_A_DURATION) != null)
				this.getIModifier().setDuration(Float.parseFloat(attributes.getValue(ScnTags.S_A_DURATION)));
			break;
		}
	}
}
