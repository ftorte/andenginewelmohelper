package com.welmo.andengine.scenes.descriptors.components;

import java.util.List;

import org.andengine.engine.Engine;
import org.xml.sax.Attributes;

import android.util.Log;

import com.welmo.andengine.scenes.components.ProgressBar;
import com.welmo.andengine.scenes.components.buttons.ButtonClick;
import com.welmo.andengine.scenes.components.buttons.ButtonOnOff;
import com.welmo.andengine.scenes.components.buttons.ButtonOnOffwithTimer;
import com.welmo.andengine.scenes.components.buttons.ButtonBasic.Types;
import com.welmo.andengine.scenes.components.interfaces.IComponent;
import com.welmo.andengine.scenes.descriptors.ScnTags;

public class ProgressBarDescriptor extends BasicComponentDescriptor {

	//	*****************************************************************************************************
	//		Constants
	//  *****************************************************************************************************
	private final static String 		TAG 						= "ProgressBar";
	
	private final static int 			INCREMENT					= 1;
	
	//	*****************************************************************************************************
	//		Variables
	//  *****************************************************************************************************
	protected boolean						bIsPersistent				= false;	
	protected String						sGlobaVariable				= null;
	
	public enum Type {CONTINUE, DISCRETE};
	
	protected Type							type						= Type.CONTINUE;
	//Resources
	protected String 						sIncrementButton			= null;					//Sprite for the button that when clicked increment the progress
	protected String 						sDecrementButton			= null;					//Sprite for the button that when clicked increment the progress
	protected String 						sBarBackground				= null;
	protected String 						sBarProgressNotch			= null;					//The notch that is addedd at each increment 
	protected String						sMapDisplayValues			= null;
	
	
	//Values
	protected int							nMinValue					= 0;
	protected int 							nMaxValue					= 100;
	protected int							nInternalHeight				= 0;
	protected int							nDisplayID					= 0;
		
	
	@SuppressWarnings("unused")
	private void copy(ProgressBarDescriptor prt){
			sIncrementButton 		= new String(prt.sIncrementButton);
			sDecrementButton 		= new String(prt.sDecrementButton);
			sBarBackground 			= new String(prt.sBarProgressNotch);
			sBarProgressNotch 		= new String(prt.sBarProgressNotch);
			sGlobaVariable			= new String(prt.sGlobaVariable);
			type					= prt.type;
			nMinValue				= prt.nMinValue;
			nMaxValue				= prt.nMaxValue;
			bIsPersistent			= prt.bIsPersistent;
			nInternalHeight			= prt.nInternalHeight;
			nDisplayID				= prt.nDisplayID;
			
	}	
	
	public ProgressBarDescriptor(){
	}
	
	/***************************************************************
	 * getters & setters
	 ***************************************************************/
	public String getGlobalVariable() {
		// TODO Auto-generated method stub
		return sGlobaVariable;
	}
	public Boolean getPersistence() {
		// TODO Auto-generated method stub
		return bIsPersistent;
	}
	public void setPersistence(boolean value){
		// TODO Auto-generated method stub
		bIsPersistent = value;
	}
	public int getMinValue(){
		return this.nMinValue;
	}
	public void setMinValue(int value){
		this.nMinValue=value;
	}
	public int getMaxValue(){
		return this.nMaxValue;
	}
	public void setManValue(int value){
		this.nMaxValue=value;
	}
	public int getInternaHeight(){
		return this.nInternalHeight;
	}
	public void setInternaHeight(int value){
		this.nInternalHeight=value;
	}
	public Type getType() {
		// TODO Auto-generated method stub
		return this.type;
	}
	public String getIncrementButton(){
		return sIncrementButton;
	}
	public String getsDecrementButton(){
		return sDecrementButton;
	}
	public String getsBarBackgroun(){
		return sBarBackground;
	}
	public String getsBarProgressNotch(){
		return sBarProgressNotch;
	}
	public int getDisplayID(){
		return nDisplayID;
	}
	public void setDisplayID(int ID){
		nDisplayID=ID;
	}
	public String getMapDisplayValues(){
		return sMapDisplayValues;
	}
	public void setMapDisplayValues(String map){
		sMapDisplayValues=new String(map);
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
		
		if((value = attr.getValue(ScnTags.S_A_PERSISTENCE))!=null)
			this.bIsPersistent = Boolean.parseBoolean(value);
		if((value = attr.getValue(ScnTags.S_A_PERSISTENCE_VARNAME))!=null)
			sGlobaVariable = new String(value);
		if((value = attr.getValue(ScnTags.S_A_TYPE))!=null)
			type = Type.valueOf(value);
		if((value = attr.getValue(ScnTags.S_A_INCREMENT_BUTTON))!=null)
			sIncrementButton = new String(value);
		if((value = attr.getValue(ScnTags.S_A_DECREMENT_RUTTON))!=null)
			sDecrementButton = new String(value);
		if((value = attr.getValue(ScnTags.S_A_BAR_BACKGROUND))!=null)
			sBarBackground = new String(value);
		if((value = attr.getValue(ScnTags.S_A_BAR_PROGRESS_NOTCH))!=null)
			sBarProgressNotch = new String(value);
		if((value = attr.getValue(ScnTags.S_A_MIN_VAL))!=null)
			nMinValue = Integer.parseInt(value);
		if((value = attr.getValue(ScnTags.S_A_MAX_VAL))!=null)
			nMaxValue = Integer.parseInt(value);
		if((value = attr.getValue(ScnTags.S_A_INTERNALl_HEIGHT))!=null)
			nInternalHeight = Integer.parseInt(value);
		if((value = attr.getValue(ScnTags.S_A_DISPLAYID))!=null)
			nDisplayID = Integer.parseInt(value);
		if((value = attr.getValue(ScnTags.S_A_MAPDISPLAYVALUES))!=null)
			sMapDisplayValues = new String(value);
	}
	@Override
	public IComponent CreateComponentInstance(Engine theEngine) {
			return 	new ProgressBar(this,theEngine.getVertexBufferObjectManager());
	}
}
