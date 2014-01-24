package com.welmo.andengine.scenes.descriptors.components;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.Attributes;

import com.welmo.andengine.scenes.descriptors.BasicDescriptor;

import android.util.Log;

public class ToolsBarDescriptor extends BasicDescriptor{
	
	//	*****************************************************************************************************
	//		Constants
	//  *****************************************************************************************************
	private final static String 		TAG 					= "ToolsBarDescriptor";
	
	// Geometires
	public static int					DEFAULT_SCENEWIDTH	 	= 1280;
	public static int					DEFAULT_SCENEHEIGHT	 	= 800;
	public static int					NB_OF_TOOLS_PER_BAR 	= 8;
	public static int					DEFAUTL_BUTTOEXTDIM		= DEFAULT_SCENEHEIGHT/NB_OF_TOOLS_PER_BAR;
	public static int					BUTTOINTDIM				= (int)((float)DEFAUTL_BUTTOEXTDIM*0.90f);
	public static int					INBUTTONPXY				= (int)((BUTTOINTDIM -BUTTOINTDIM)/2);
	// Colors
	public static int					TOOLBARBACKGROUND  		= 0XA6A6A6;
	public static int					SELCTEDTOOLBACKGROUND  	= 0X505050;
	//	*****************************************************************************************************
	//		Members
	//  *****************************************************************************************************
	protected Positioning 				ePosition				= Positioning.TOP;
	protected int 						nHeight					= DEFAUTL_BUTTOEXTDIM;
	protected int 						nWidth					= DEFAULT_SCENEHEIGHT;
	protected int						nNbOfButtons			= NB_OF_TOOLS_PER_BAR;
	protected int						nBackgroungColor		= TOOLBARBACKGROUND;
	protected int						nX						= 0;
	protected int						nY						= 0;
		
	/***************************************************************
	 * Constructor
	 ***************************************************************/
	public ToolsBarDescriptor(){
		//lButtonDesc = new ArrayList<ButtonDescriptor>();
		ePosition 	= Positioning.TOP;
	}
	/***************************************************************
	 * Getters & Setters 
	 ***************************************************************/
	/* Get/Set Position */
	public Positioning 	getPosition() {return ePosition;}
	public void setPosition(Positioning position) {this.ePosition = position;}
	/* Get/Set height */
	public int 	getHeight() {return nHeight;}
	public void setHeight(int height) {this.nHeight = height;}
	/* Get/Set widht */
	public int 	getWidth() {return nWidth;}
	public void setWidht(int widht) {this.nWidth = widht;}
	/* Get/Set Number of Buttons */
	public int 	getNbOfButtons() {return nNbOfButtons;}
	public void setNbOfButtons(int nbOfButtons) {this.nNbOfButtons = nbOfButtons;}
	/* Get/Set Background Colors */
	public int getBackgroungColor() {return nBackgroungColor;}
	public void setBackgroungColor(int nBackgroungColor) {this.nBackgroungColor = nBackgroungColor;}
	/* Get/Set X */
	public int 	getX() {return nX;}
	public void setX(int X) {this.nX = X;}
	/* Get/Set Y */
	public int 	getY() {return nY;}
	public void setY(int Y) {this.nY = Y;}

	/***************************************************************
	 * Overrided functions 
	 ***************************************************************/
	@Override
	public void readXMLDescription(Attributes attr){
		Log.i(TAG,"\t\t readXMLDescription");	
		//call XML parser for class parent parameters
		super.readXMLDescription(attr);
		String value;
		// read position
		if((value = attr.getValue(ScnTags.S_A_TOLLBARPOSITION))!=null) 
			ePosition=Positioning.valueOf(value);
		
		if((value = attr.getValue(ScnTags.S_A_HEIGHT))!=null) 
			setHeight(Integer.parseInt(value));
		
		if((value = attr.getValue(ScnTags.S_A_WIDTH))!=null) 
			setWidht(Integer.parseInt(value));
		
		if((value = attr.getValue(ScnTags.S_A_POSITION_X))!=null) 
			setX(Integer.parseInt(value));
		
		if((value = attr.getValue(ScnTags.S_A_POSITION_Y))!=null) 
			setY(Integer.parseInt(value));
		
	}
}
