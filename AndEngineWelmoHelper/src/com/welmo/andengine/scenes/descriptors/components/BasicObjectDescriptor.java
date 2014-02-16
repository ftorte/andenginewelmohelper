package com.welmo.andengine.scenes.descriptors.components;


import org.xml.sax.Attributes;

import android.util.Log;

import com.welmo.andengine.scenes.descriptors.BasicDescriptor;

public abstract class BasicObjectDescriptor extends BasicDescriptor{
	
	public static String 				TAG ="BasicObjectDescriptor";
	
	public enum Alignment {
	    NO_ALIGNEMENT, CENTER, LEFTH, TOP, BOTTOM, RIGHT
	}
	/* Inner Interfaces to:
	 * Manage Object Dimension => IDimension
	 * Manage Object Position => IPosition
	 * Manage Object Orientation => IOrientation
	 */
	public interface IDimension{
		void setWidth(int w);
		void setHeight(int h);
		int getWidth();
		int getHeight();
	}
	public interface IPosition{
		void setX(int w);
		void setY(int h);
		void setZorder(int Z);
		void setHorizontalAlignment(Alignment hA);
		void setVerticalAlignment(Alignment vA);
	
		int getX();
		int getY();
		int getZorder();
		Alignment getHorizzontalAlignment();
		Alignment getVerticalAlignment();
	}
	public interface IOrientation{
		void setOrientation(float angle);
		void setRotationCenterX(float rX);
		void setRotationCenterY(float rY);
		
		float getOrientation();
		float getRotationCenterX();
		float getRotationCenterY();
	}
	public interface ICharacteristics{
		void setColor(String colorName);
		String getColor();
	}
	//----------------------------------------------------------------------//
	//Protected members
	protected int 			pX, pY;
	protected int			pZOrder;
	protected int 			width, height;
	protected float 		rX;
	protected float 		rY;
	protected float 		orientation;
	protected String 		colorName;
	protected Alignment		horizzontalAlignment; 
	protected Alignment		verticalAlignment;
	//----------------------------------------------------------------------//
	// Public constructor
	//----------------------------------------------------------------------//
	public BasicObjectDescriptor(){
		super();
		//ID=0;
		pX=0;
		pY=0;
		pZOrder=0;
		width=0;
		height=0;
		orientation=0;
		rX = 0;
		rY = 0;
		horizzontalAlignment=Alignment.CENTER; 
		verticalAlignment=Alignment.CENTER;;
		colorName = new String("");
	}
	//----------------------------------------------------------------------//
	// Public methods
	//----------------------------------------------------------------------//
	public IDimension getIDimension(){
		return  new IDimension()  {  
			public void setWidth(int w){
				width=w;
			};
			public void setHeight(int h){
				height=h;
			};
			public int getWidth(){
				return width;
			}
			public int getHeight(){
				return height;
			}
		};
	}
	public IPosition getIPosition(){
		return  new IPosition()  {  
			public void setX(int x){
				pX=x;
			};
			public void setY(int y){
				pY=y;
			};
			public void setZorder(int z){
				pZOrder=z;
			}
			public int getX(){
				return pX;
			}
			public int getY(){
				return pY;
			}
			public int getZorder(){
				return pZOrder;
			}
			public Alignment getHorizzontalAlignment() {
				return horizzontalAlignment;
			}
			public void setHorizontalAlignment(Alignment alignement) {
				horizzontalAlignment = alignement;
			}
			public Alignment getVerticalAlignment() {
				return verticalAlignment;
			}
			public void setVerticalAlignment(Alignment alignement) {
				verticalAlignment = alignement;
			}
		};
	}
	public IOrientation getIOriantation(){
		return  new IOrientation()  {  
			public void setOrientation(float angle){
				orientation=angle;
			};
			public float getOrientation(){
				return orientation;
			}
			@Override
			public void setRotationCenterX(float theRX) {
				rX=theRX;
			}
			@Override
			public void setRotationCenterY(float theRY) {
				rY=theRY;
			}
			@Override
			public float getRotationCenterX() {
				return rX;
			}
			@Override
			public float getRotationCenterY() {
				return rY;
			}
		};
	}
	public ICharacteristics getICharacteristis(){
		return  new ICharacteristics()  {  
			public void setColor(String theColor) {
				colorName = new String (theColor);
			}
			public String getColor() {
				return colorName;
			}
		};
	}
	public void copyFrom(BasicObjectDescriptor copyfrom) {
		super.copyFrom(copyfrom);
		//ID 			= copyfrom.ID;
		pX 			= copyfrom.pX;
		pY 			= copyfrom.pY;
		pZOrder 	= copyfrom.pZOrder;
		width		= copyfrom.width;
		height		= copyfrom.height;
		rX			= copyfrom.rX;
		rY			= copyfrom.rY;
		orientation	= copyfrom.orientation;
		colorName	= copyfrom.colorName;
		horizzontalAlignment 	= copyfrom.horizzontalAlignment; 
		verticalAlignment		= copyfrom.verticalAlignment;
	}
	public void readXMLDescription(Attributes attributes) {
		super.readXMLDescription(attributes);
		parseAttributesPosition(this.getIPosition(), attributes);
		parseAttributesDimensions(this.getIDimension(),attributes);
		parseAttributesOrientation(this.getIOriantation(),attributes);
		parseAttributesCharacteristics(this.getICharacteristis(),attributes);
	}
	//-------------------------------------------------------------------------------------
	// Specfic private functions to read the attributes
	//-------------------------------------------------------------------------------------
	private void parseAttributesPosition(IPosition pPosition,Attributes attributes){
		Log.i(TAG,"\t\t\t parseAttributesPosition");
		if((attributes.getValue(ScnTags.S_A_POSITION_X) != null) && (attributes.getValue(ScnTags.S_A_POSITION_Y) != null)){
			//pPosition.setX(dimHelper.parsPosition(ScreenDimensionHelper.X,attributes.getValue(ScnTags.S_A_POSITION_X)));
			//pPosition.setY(dimHelper.parsPosition(ScreenDimensionHelper.Y,attributes.getValue(ScnTags.S_A_POSITION_Y)));
			pPosition.setX(Integer.parseInt(attributes.getValue(ScnTags.S_A_POSITION_X)));
			pPosition.setY(Integer.parseInt(attributes.getValue(ScnTags.S_A_POSITION_Y)));
		}
		else{
			pPosition.setX(0);
			pPosition.setY(0);
		}
		//read Z Order
		if(attributes.getValue(ScnTags.S_A_Z_ORDER) != null){ 
			pPosition.setZorder(Integer.parseInt(attributes.getValue(ScnTags.S_A_Z_ORDER)));
		}
		else{
			pPosition.setZorder(0);
		}
		//read H Alignment
		if(attributes.getValue(ScnTags.S_A_H_ALIGNEMENT) != null){ 
			pPosition.setHorizontalAlignment(Alignment.valueOf(attributes.getValue(ScnTags.S_A_H_ALIGNEMENT)));
		}
		else{
			pPosition.setHorizontalAlignment(Alignment.NO_ALIGNEMENT);
		}
		//read V Alignment
		if(attributes.getValue(ScnTags.S_A_H_ALIGNEMENT) != null){ 
			pPosition.setVerticalAlignment(Alignment.valueOf(attributes.getValue(ScnTags.S_A_V_ALIGNEMENT)));
		}
		else{
			pPosition.setVerticalAlignment(Alignment.NO_ALIGNEMENT);
		}
	}
	private void parseAttributesDimensions(IDimension pDimensions,Attributes attributes){
		Log.i(TAG,"\t\t\t parseAttributesDimensions");
		if((attributes.getValue(ScnTags.S_A_WIDTH) != null) && (attributes.getValue(ScnTags.S_A_WIDTH) != null)){
			//pDimensions.setWidth(dimHelper.parsLenght(ScreenDimensionHelper.W, attributes.getValue(ScnTags.S_A_WIDTH)));
			//pDimensions.setHeight(dimHelper.parsLenght(ScreenDimensionHelper.H, attributes.getValue(ScnTags.S_A_HEIGHT)));
			pDimensions.setWidth(Integer.parseInt(attributes.getValue(ScnTags.S_A_WIDTH)));
			pDimensions.setHeight(Integer.parseInt(attributes.getValue(ScnTags.S_A_HEIGHT)));
		}
		else{
			pDimensions.setWidth(100);
			pDimensions.setHeight(100);
		}
	}
	private void parseAttributesOrientation(IOrientation pDimensions,Attributes attributes){
		Log.i(TAG,"\t\t\t parseAttributesOrientation");

		if(attributes.getValue(ScnTags.S_A_ORIENTATION) != null)
			pDimensions.setOrientation(Float.parseFloat(attributes.getValue(ScnTags.S_A_ORIENTATION)));
		else
			pDimensions.setOrientation(0f);

		if(attributes.getValue(ScnTags.S_A_ROTAION_CENTER_X) != null)
			pDimensions.setRotationCenterX(Float.parseFloat(attributes.getValue(ScnTags.S_A_ROTAION_CENTER_X)));
		else
			pDimensions.setRotationCenterX(0f);

		if(attributes.getValue(ScnTags.S_A_ROTAION_CENTER_Y) != null)
			pDimensions.setRotationCenterY(Float.parseFloat(attributes.getValue(ScnTags.S_A_ROTAION_CENTER_Y)));
		else
			pDimensions.setRotationCenterY(0f);

	}
	private void parseAttributesCharacteristics(ICharacteristics pCharacteristics,Attributes attributes){
		Log.i(TAG,"\t\t\t parseAttributesCharacteristics");
		if(attributes.getValue(ScnTags.S_A_COLOR) != null){
			Log.i(TAG,"parseAttributesCharacteristics Color = " + attributes.getValue(ScnTags.S_A_COLOR));
			pCharacteristics.setColor(attributes.getValue(ScnTags.S_A_COLOR));
			Log.i(TAG,"parseAttributesCharacteristics Color Set-up= " + pCharacteristics.getColor());
		}
		else
			pCharacteristics.setColor("");
	}
}
