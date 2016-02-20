package com.welmo.andengine.scenes.descriptors.components;

import java.util.List;

import org.andengine.engine.Engine;
import org.xml.sax.Attributes;

import android.util.Log;

import com.welmo.andengine.scenes.components.buttons.ButtonClick;
import com.welmo.andengine.scenes.components.buttons.ButtonOnOff;
import com.welmo.andengine.scenes.components.buttons.ButtonOnOffwithTimer;
import com.welmo.andengine.scenes.components.buttons.ButtonBasic.Types;
import com.welmo.andengine.scenes.components.buttons.GroupButtonOnOff;
import com.welmo.andengine.scenes.components.interfaces.IComponent;
import com.welmo.andengine.scenes.descriptors.ScnTags;

public class GroupButtonsDescriptor extends BasicComponentDescriptor{
	//	*****************************************************************************************************
	//		Constants
	//  *****************************************************************************************************
	private final static String 		TAG 						= "GroupButtonsDescriptor";
	
	public enum Types{ONE_CHOICE, MULTY_CHOICES}

	private boolean						bIsPersistent				= false;
		
	
	// if rectangle based her are the colors
	// color
	//private static int					BUTTONBARBACKGROUND  		= 0XA6A6A6;
	//private static int					SELCTEDBUTTONBACKGROUND 	= 0X505050;	
	//private int 							nDefaultColorBackGround 	= BUTTONBARBACKGROUND;
	//private int 							nSelectedColotBackGround 	= SELCTEDBUTTONBACKGROUND;
	
	//messages
	//private List<String>				    lEventMessages				= null;

	protected Types						nGroupType 	               	= Types.ONE_CHOICE;	
	protected List<String>				lValues						= null;
	
	public GroupButtonsDescriptor(){
	}
	
	/***************************************************************
	 * Getters & Setters 
	 ***************************************************************/
	//persistence
	public Boolean getPersistence() {return bIsPersistent;}
	public void setPersistence(boolean value){bIsPersistent = value;}
	//type
	public Types getType() {return nGroupType;}	
	public void setType(Types newType) { nGroupType = newType;}	

	
	/***************************************************************
	 * Overrided functions 
	 ***************************************************************/
	@Override
	public void readXMLDescription(Attributes attr){
		
		Log.i(TAG,"\t\t readXMLDescription");	
		//call XML parser for class parent parameters
		super.readXMLDescription(attr);
		String value;
	
		//ready group type
		if((value = attr.getValue(ScnTags.S_A_ON_TEXTURE))!=null)
			setType(Types.valueOf(value));
		/*
		// read background/ON/OFF textures
		if((value = attr.getValue(ScnTags.S_A_TYPE))!=null)
			sBackGroundTextureName = new String(value);

		if((value = attr.getValue(ScnTags.S_A_ON_TEXTURE))!=null)
			setType(Types.valueOf(value));
		
		if((value = attr.getValue(ScnTags.S_A_OFF_TEXTURE))!=null)
			sButtonTextureOFF = new String(value);
		
		// read dimensions 
		if((value = attr.getValue(ScnTags.S_A_EXT_DIM))!=null)
			nExternaDimension = Integer.parseInt(value);
		
		if((value = attr.getValue(ScnTags.S_A_INT_DIM))!=null)
			nInternalDimension = Integer.parseInt(value);
		
		if((value = attr.getValue(ScnTags.S_A_PERSISTENCE))!=null)
			this.bIsPersistent = Boolean.parseBoolean(value);
		
		if((value = attr.getValue(ScnTags.S_A_PERSISTENCE_VARNAME))!=null)
			sGlobaVariable = new String(value);
		
		if((value = attr.getValue(ScnTags.S_A_DISPLAYID))!=null)
			nDisplayID = Integer.parseInt(value);*/
		
	}
	@Override
	public IComponent CreateComponentInstance(Engine theEngine) {
		return 	new GroupButtonOnOff(this,theEngine.getVertexBufferObjectManager());
	}
}
