package com.welmo.andengine.scenes.descriptors.components;

import java.util.ArrayList;
import java.util.LinkedList;

public abstract class BasicObjectDescriptor extends BasicDescriptor{
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
	protected int 			ID;
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
		ID=0;
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
	public int getID(){
		return ID;
	}
}
