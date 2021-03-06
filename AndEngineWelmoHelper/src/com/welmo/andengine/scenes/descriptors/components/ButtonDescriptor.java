package com.welmo.andengine.scenes.descriptors.components;

import java.util.List;

import org.andengine.engine.Engine;
import org.xml.sax.Attributes;

import com.welmo.andengine.scenes.components.buttons.ButtonBasic;
import com.welmo.andengine.scenes.components.buttons.ButtonBasic.Types;
import com.welmo.andengine.scenes.components.buttons.ButtonClick;
import com.welmo.andengine.scenes.components.buttons.ButtonOnOff;
import com.welmo.andengine.scenes.components.buttons.ButtonOnOffwithTimer;
import com.welmo.andengine.scenes.components.buttons.ButtonPulse;
import com.welmo.andengine.scenes.components.buttons.ButtonSceneLauncher;
import com.welmo.andengine.scenes.components.interfaces.IComponent;
import com.welmo.andengine.scenes.components.puzzle.PuzzleSprites;
import com.welmo.andengine.scenes.descriptors.BasicDescriptor;
import com.welmo.andengine.scenes.descriptors.ScnTags;

import android.util.Log;

public class ButtonDescriptor extends BasicComponentDescriptor{
	
	//	*****************************************************************************************************
	//		Constants
	//  *****************************************************************************************************
	private final static String 		TAG 						= "ButtonDescriptor";
	
	public boolean						bSpriteBased				= true;
	public boolean						bIsPersistent				= false;
	
	public String						sGlobaVariable				= null;
	
	
	//if sprite based here is where textures names are stored
	public String 						sBackGroundTextureName;
	public String 						sButtonTextureON;
	public String 						sButtonTextureOFF;
	
	//if rectangle based her are the colors
	// color
	public static int					BUTTONBARBACKGROUND  		= 0XA6A6A6;
	public static int					SELCTEDBUTTONBACKGROUND 	= 0X505050;	
	public int 							nDefaultColorBackGround 	= BUTTONBARBACKGROUND;
	public int 							nSelectedColotBackGround 	= SELCTEDBUTTONBACKGROUND;
	//geometry
	public int							nExternaDimension			= 0;
	public int							nInternalDimension			= 0;
	
	
	
	//messages
	public List<String>				    lEventMessages				= null;

	public int 							nDisplayID					= 0;
	
	@SuppressWarnings("unused")
	private void copy(ButtonDescriptor prt){
		bSpriteBased					= prt.bSpriteBased;
		if(bSpriteBased){
			sBackGroundTextureName 		= new String(prt.sBackGroundTextureName);
			sButtonTextureON 			= new String(prt.sButtonTextureON);
			sButtonTextureOFF 			= new String(prt.sButtonTextureOFF);
		}
		else{
			nDefaultColorBackGround 	= prt.nDefaultColorBackGround;
			nSelectedColotBackGround 	= prt.nSelectedColotBackGround;
		}
		nExternaDimension			= prt.nExternaDimension;
		nInternalDimension			= prt.nInternalDimension;
		
		lEventMessages				= prt.lEventMessages;
	}	
	public ButtonDescriptor(){
	}
	/***************************************************************
	 * Overrided functions 
	 ***************************************************************/
	@Override
	public void readXMLDescription(Attributes attr){
		/*
		<button ID=200; type="ON-OFF" action="" sprite-ON="" sprite-OFF=""/>
		*/
		Log.i(TAG,"\t\t readXMLDescription");	
		//call XML parser for class parent parameters
		super.readXMLDescription(attr);
		String value;
		
		// read background/ON/OFF textures
		if((value = attr.getValue(ScnTags.S_A_BG_TEXTURE))!=null)
			sBackGroundTextureName = new String(value);

		if((value = attr.getValue(ScnTags.S_A_ON_TEXTURE))!=null)
			sButtonTextureON = new String(value);

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
			nDisplayID = Integer.parseInt(value);
		
	}
	@Override
	public IComponent CreateComponentInstance(Engine theEngine) {
		super.CreateComponentInstance(theEngine);
		switch(Types.valueOf(this.sSubType)){
		case BASIC:
			break;
		case CLICK:
			return 	new ButtonClick(this,theEngine.getVertexBufferObjectManager());
		case ON_OFF:
			return 	new ButtonOnOff(this,theEngine.getVertexBufferObjectManager());
		case ON_OFF_WITH_TIMER:
			return 	new ButtonOnOffwithTimer(this,theEngine.getVertexBufferObjectManager());
		default:
			break;
		}
		return null;
	}
	public Boolean getPersistence() {
		// TODO Auto-generated method stub
		return bIsPersistent;
	}
	public void setPersistence(boolean value){
		// TODO Auto-generated method stub
		bIsPersistent = value;
	}
}
