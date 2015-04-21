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

public class ParticuleSystemDescriptor extends BasicDescriptor{

	
	// ===========================================================
	// Inner Classes
	// ===========================================================	
	public class ParticleInitializerDescriptor{
		public InitializerType type;
		public float pRed;
		public float pGreen;
		public float pBlue;
		public float pAlfa;
		public float pMinVelX;
		public float pMaxVelX;
		public float pMinVelY;
		public float pMaxVelY;
		public float pMinRotation;
		public float pMaxRotation;
		public float pLifeTime;
	};
	public class ParticleModifiersDescriptor{
		public ModifierType	type;
		
		public float pFromTime;
		public float pToTime;
		public float pFromScale;
		public float pToScale;
		public float pFromRed;
		public float pToRed;
		public float pFromGreen;
		public float pToGreen;
		public float pFromBlue;
		public float pToBlue;
		public float pFromAlfa;
		public float pToAlfa;		
	};

	public static enum EmitterType{CIRCULAR};
	public static enum InitializerType{COLOR, ALPHA, VELOCITY, ROTATION, EXPIRE};	
	public static enum ModifierType{SCALE, COLOR, ALPHA};
	
	// ===========================================================
	// Variables
	// ===========================================================
	protected EmitterType	theEmitterType = EmitterType.CIRCULAR;  

	protected float 		fCenterX = 0;
	protected float 		fCenterY = 0;
	protected float 		fRadiusX = 0;
	protected float 		fRadiusY = 0;


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
		if((value = attributes.getValue(ScnTags.S_A_POSITION_X))!=null) 		this.setCenterX(Float.parseFloat(value));
		if((value = attributes.getValue(ScnTags.S_A_POSITION_Y))!=null) 		this.setCenterY(Float.parseFloat(value));
		if((value = attributes.getValue(ScnTags.S_A_RADIUS_X))!=null) 			this.setRadiusX(Float.parseFloat(value));
		if((value = attributes.getValue(ScnTags.S_A_RADIUS_Y))!=null) 			this.setRadiusY(Float.parseFloat(value));
		
		
		if((value = attributes.getValue(ScnTags.S_A_RATEMIN))!=null) 			this.setRateMin(Float.parseFloat(value));
		if((value = attributes.getValue(ScnTags.S_A_RATEMAX))!=null) 			this.setRateMax(Float.parseFloat(value));
		if((value = attributes.getValue(ScnTags.S_A_PARTICELMAX))!=null) 		this.setParticelsMax(Integer.parseInt(value));
		
		if((value = attributes.getValue(ScnTags.S_A_RESOURCE_NAME))!=null) 		this.setTextureRegionName(new String(value));
		
		if((value = attributes.getValue(ScnTags.S_A_NAME))!=null) 				this.setName(new String(value));
		
	}
}
