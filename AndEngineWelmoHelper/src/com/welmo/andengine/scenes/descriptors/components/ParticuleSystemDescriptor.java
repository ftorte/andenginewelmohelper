package com.welmo.andengine.scenes.descriptors.components;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.andengine.entity.particle.initializer.AlphaParticleInitializer;
import org.andengine.entity.particle.initializer.ColorParticleInitializer;
import org.andengine.entity.particle.initializer.RotationParticleInitializer;
import org.andengine.entity.particle.initializer.VelocityParticleInitializer;
import org.andengine.entity.particle.modifier.AlphaParticleModifier;
import org.andengine.entity.particle.modifier.ColorParticleModifier;
import org.andengine.entity.particle.modifier.ExpireParticleInitializer;
import org.andengine.entity.particle.modifier.ScaleParticleModifier;
import org.andengine.entity.sprite.Sprite;
import org.xml.sax.Attributes;

import com.welmo.andengine.scenes.descriptors.BasicDescriptor;
import com.welmo.andengine.scenes.descriptors.ScnTags;
import com.welmo.andengine.scenes.descriptors.components.ParticuleSystemDescriptor.ParticleInitializerDescriptor;
import com.welmo.andengine.scenes.descriptors.components.ParticuleSystemDescriptor.ParticleModifiersDescriptor;

public class ParticuleSystemDescriptor extends BasicDescriptor{

	
	// ===========================================================
	// Inner Classes
	// ===========================================================	
	public static class ParticleInitializerDescriptor{
		
		public static enum InitializerType{NULL,COLOR_FIXED, COLOR_RANGE, ALPHA, VELOCITY, ROTATION, ACCELERATION, EXPIRE};	
			
		public InitializerType type = InitializerType.NULL;
		
	
		public float pMinRed		=0.0f;
		public float pMinGreen		=0.0f;
		public float pMinBlue		=0.0f;
		public float pMaxRed		=0.0f;
		public float pMaxGreen		=0.0f;
		public float pMaxBlue		=0.0f;
		public float pAlfa			=0.0f;
		public float pMinVelX		=0.0f;
		public float pMaxVelX		=0.0f;
		public float pMinVelY		=0.0f;
		public float pMaxVelY		=0.0f;
		public float pMinRotation	=0.0f;
		public float pMaxRotation	=0.0f;
		public float pLifeTime		=0.0f;
		public float pAccX			=1.0f;
		public float pAccY			=1.0f;
		
		
		public ParticleInitializerDescriptor(){};
		
		public void readXMLDescription(Attributes attributes) {
			String value;
			//TYPE
			if((value = attributes.getValue(ScnTags.S_A_TYPE))!=null) 				this.type = InitializerType.valueOf(value);

			switch(this.type){
			case COLOR_FIXED: 
				if((value = attributes.getValue(ScnTags.S_A_RED))!=null) 			this.pMinRed 	= Float.parseFloat(value);
				if((value = attributes.getValue(ScnTags.S_A_GREEN))!=null) 			this.pMinGreen 	= Float.parseFloat(value);
				if((value = attributes.getValue(ScnTags.S_A_BLUE))!=null) 			this.pMinBlue 	= Float.parseFloat(value);
				break;
			case COLOR_RANGE: 
				if((value = attributes.getValue(ScnTags.S_A_MIN_RED))!=null) 			this.pMinRed 	= Float.parseFloat(value);
				if((value = attributes.getValue(ScnTags.S_A_MIN_GREEN))!=null) 			this.pMinGreen 	= Float.parseFloat(value);
				if((value = attributes.getValue(ScnTags.S_A_MIN_BLUE))!=null) 			this.pMinBlue 	= Float.parseFloat(value);
				if((value = attributes.getValue(ScnTags.S_A_MAX_RED))!=null) 			this.pMaxRed 	= Float.parseFloat(value);
				if((value = attributes.getValue(ScnTags.S_A_MAX_GREEN))!=null) 			this.pMaxGreen 	= Float.parseFloat(value);
				if((value = attributes.getValue(ScnTags.S_A_MAX_BLUE))!=null) 			this.pMaxBlue 	= Float.parseFloat(value);
				break;
			case ALPHA: 
				if((value = attributes.getValue(ScnTags.S_A_ALPHA))!=null) 			this.pAlfa 		= Float.parseFloat(value);
				break;
			case VELOCITY: 
				if((value = attributes.getValue(ScnTags.S_A_MIN_VELX))!=null) 		this.pMinVelX	= Float.parseFloat(value);	
				if((value = attributes.getValue(ScnTags.S_A_MAX_VELX))!=null) 		this.pMaxVelX	= Float.parseFloat(value);	
				if((value = attributes.getValue(ScnTags.S_A_MIN_VELY))!=null) 		this.pMinVelY	= Float.parseFloat(value);	
				if((value = attributes.getValue(ScnTags.S_A_MAX_VELY))!=null) 		this.pMaxVelY	= Float.parseFloat(value);	
				break;
			case ROTATION: 
				if((value = attributes.getValue(ScnTags.S_A_MIN_ROTATION))!=null) 	this.pMinRotation=Float.parseFloat(value);	
				if((value = attributes.getValue(ScnTags.S_A_MAX_ROTATION))!=null) 	this.pMaxRotation=Float.parseFloat(value);	
				break;
			case EXPIRE: 
				if((value = attributes.getValue(ScnTags.S_A_LIFE_TIME))!=null) 		this.pLifeTime	= Float.parseFloat(value);	
				break;
			case ACCELERATION:
				if((value = attributes.getValue(ScnTags.S_A_ACCEL_X))!=null) 		this.pAccX		= Float.parseFloat(value);	
				if((value = attributes.getValue(ScnTags.S_A_ACCEL_Y))!=null) 		this.pAccY		= Float.parseFloat(value);	
				break;
			default:
				break;
			}
		}
	};
	public static class ParticleModifiersDescriptor{

		public ModifierType	type = ModifierType.NULL;
		
		public float pFromTime		=0.0f;
		public float pToTime		=0.0f;
		public float pFromScale		=0.0f;
		public float pToScale		=0.0f;
		public float pFromRed		=0.0f;
		public float pToRed			=0.0f;
		public float pFromGreen		=0.0f;
		public float pToGreen		=0.0f;
		public float pFromBlue		=0.0f;
		public float pToBlue		=0.0f;
		public float pFromAlfa		=0.0f;
		public float pToAlfa		=0.0f;
		
		public ParticleModifiersDescriptor(){};
		
		
		public void readXMLDescription(Attributes attributes) {
			String value;
			//TYPE
			if((value = attributes.getValue(ScnTags.S_A_TYPE))!=null) 				this.type = ModifierType.valueOf(value);

			if((value = attributes.getValue(ScnTags.S_A_FROM_TYME))!=null) 			this.pFromTime 	= Float.parseFloat(value);
			if((value = attributes.getValue(ScnTags.S_A_TO_TYME))!=null) 			this.pToTime 	= Float.parseFloat(value);

			switch(this.type){
			case SCALE: 
				if((value = attributes.getValue(ScnTags.S_A_FROM_SCALE))!=null) 	this.pFromScale = Float.parseFloat(value);
				if((value = attributes.getValue(ScnTags.S_A_TO_SCALE))!=null) 		this.pToScale 	= Float.parseFloat(value);
				break;
			case COLOR: 
				//Color
				if((value = attributes.getValue(ScnTags.S_A_FROM_RED))!=null) 		this.pFromRed 	= Float.parseFloat(value);
				if((value = attributes.getValue(ScnTags.S_A_TO_RED))!=null) 		this.pToRed 	= Float.parseFloat(value);
				if((value = attributes.getValue(ScnTags.S_A_FROM_GREEN))!=null) 	this.pFromGreen = Float.parseFloat(value);
				if((value = attributes.getValue(ScnTags.S_A_TO_GREEN))!=null) 		this.pToGreen 	= Float.parseFloat(value);
				if((value = attributes.getValue(ScnTags.S_A_FROM_BLUE))!=null) 		this.pFromBlue 	= Float.parseFloat(value);
				if((value = attributes.getValue(ScnTags.S_A_TO_BLUE))!=null) 		this.pToBlue 	= Float.parseFloat(value);	
				break;
			case ALPHA: 

				if((value = attributes.getValue(ScnTags.S_A_FROM_ALPHA))!=null) 	this.pFromAlfa	= Float.parseFloat(value);	
				if((value = attributes.getValue(ScnTags.S_A_TO_ALPHA))!=null) 		this.pToAlfa	= Float.parseFloat(value);	
				break;
			default:
				break;
			}
		}
	};

	public static enum EmitterType{CIRCULAR, CIRCULAR_OUTLINE, POINT, RECTANGULAR, RECTANGULAR_OUTLINE};
	public static enum ModifierType{NULL, SCALE, COLOR, ALPHA};
	
	// ===========================================================
	// Variables
	// ===========================================================
	protected EmitterType	theEmitterType = EmitterType.CIRCULAR;  

	protected float 		fCenterX 	= 0;
	protected float 		fCenterY 	= 0;
	protected float 		fRadiusX 	= 0;
	protected float 		fRadiusY	= 0;
	protected float 		fWidth 		= 0;
	protected float 		fHeight 	= 0;
	


	protected float 		fRateMin = 0;
	protected float 		fRateMax = 0;
	protected int 			nParticelsMax = 0;
	protected String 		sTextureName = "";
	
	protected String 		sName = "";
	
	
	List<ParticleInitializerDescriptor>	lstInitializers;
	List<ParticleModifiersDescriptor>	lstModifiers;
	
	
	// ===========================================================
	// Getters and Setters
	// ===========================================================
	public EmitterType getEmitterType() {
		return theEmitterType;
	}
	public void setEmitterType(EmitterType theType) {
		this.theEmitterType = theType;
	}
	public float getCenterX() {
		return fCenterX;
	}
	public void setCenterX(float fCenterX) {
		this.fCenterX = fCenterX;
	}
	public float getCenterY() {
		return fCenterY;
	}
	public void setCenterY(float fCenterY) {
		this.fCenterY = fCenterY;
	}
	public float getRadiusX() {
		return fRadiusX;
	}
	public void setRadiusX(float fRadiusX) {
		this.fRadiusX = fRadiusX;
	}
	public float getRadiusY() {
		return fRadiusY;
	}
	public void setRadiusY(float fRadiusY) {
		this.fRadiusY = fRadiusY;
	}
	
	public float getWidth() {
		return fRadiusY;
	}
	public void setWidth(float fWidth) {
		this.fWidth = fWidth;
	}
	
	public float getHeight() {
		return fHeight;
	}
	public void setHeight(float fHeight) {
		this.fHeight = fHeight;
	}
	
	
	
	public float getRateMin() {
		return fRateMin;
	}
	public void setRateMin(float fRateMin) {
		this.fRateMin = fRateMin;
	}
	public float getRateMax() {
		return fRateMax;
	}
	public void setRateMax(float fRateMax) {
		this.fRateMax = fRateMax;
	}
	public int getParticelsMax() {
		return nParticelsMax;
	}
	public void setParticelsMax(int nParticelsMax) {
		this.nParticelsMax = nParticelsMax;
	}
	public String getTextureRegionName() {
		return sTextureName;
	}
	public void setTextureRegionName(String sTextureName) {
		this.sTextureName = sTextureName;
	}
	public String getName() {
		return sName;
	}
	public void setName(String sName) {
		this.sName = sName;
	}
	
	
	public List<ParticleInitializerDescriptor>	 getInitializers(){
		return lstInitializers;
	}
	
	public List<ParticleModifiersDescriptor>	 getModifiers(){
		return lstModifiers;
	}
	
	// ===========================================================
	// Constructor
	// ===========================================================
	public ParticuleSystemDescriptor() {
		lstInitializers = new ArrayList<ParticleInitializerDescriptor>();
		lstModifiers= new ArrayList<ParticleModifiersDescriptor>();
		
	}
	// ===========================================================
	// Pubblic Functions
	// ===========================================================
	@Override
	public void readXMLDescription(Attributes attributes) {
		super.readXMLDescription(attributes);
		//variable containing attributes value
		String value;
		
		if((value = attributes.getValue(ScnTags.S_A_TYPE))!=null) 			this.theEmitterType = EmitterType.valueOf(value);
		
		if((value = attributes.getValue(ScnTags.S_A_CENTER_X))!=null) 		this.setCenterX(Float.parseFloat(value));
		if((value = attributes.getValue(ScnTags.S_A_CENTER_Y))!=null) 		this.setCenterY(Float.parseFloat(value));
		if((value = attributes.getValue(ScnTags.S_A_RADIUS_X))!=null) 		this.setRadiusX(Float.parseFloat(value));
		if((value = attributes.getValue(ScnTags.S_A_RADIUS_Y))!=null) 		this.setRadiusY(Float.parseFloat(value));
		if((value = attributes.getValue(ScnTags.S_A_WIDTH))!=null) 			this.setWidth(Float.parseFloat(value));
		if((value = attributes.getValue(ScnTags.S_A_HEIGHT))!=null) 		this.setHeight(Float.parseFloat(value));
		
		
		if((value = attributes.getValue(ScnTags.S_A_RATEMIN))!=null) 			this.setRateMin(Float.parseFloat(value));
		if((value = attributes.getValue(ScnTags.S_A_RATEMAX))!=null) 			this.setRateMax(Float.parseFloat(value));
		if((value = attributes.getValue(ScnTags.S_A_PARTICELMAX))!=null) 		this.setParticelsMax(Integer.parseInt(value));	
		if((value = attributes.getValue(ScnTags.S_A_RESOURCE_NAME))!=null) 		this.setTextureRegionName(new String(value));
	
		if((value = attributes.getValue(ScnTags.S_A_NAME))!=null) 				this.setName(new String(value));
		
	}
	public void addInitalizerDescriptor(ParticleInitializerDescriptor pDescriptorInitiliers) {
		lstInitializers.add(pDescriptorInitiliers);
	}
	public void addModifierDescriptor(ParticleModifiersDescriptor pDescriptorModifier) {
		lstModifiers.add(pDescriptorModifier);
	}
}
