package com.welmo.andengine.scenes.descriptors.components;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.xml.sax.Attributes;

import com.welmo.andengine.scenes.descriptors.BasicDescriptor;
import com.welmo.andengine.scenes.descriptors.ScnTags;

import android.util.Log;

public class HUDDescriptor extends BasicObjectDescriptor {

	// -----------------------------------------------------------------------------------------------------------------------------
	// Constants
	private final static String 						TAG 			= "HUDisplay";
	//Member variables
	private boolean										hasColorPicker 		= false;
	private boolean										hasScrollDetector	= false;
	private boolean										hasPinchAndZoom		= false;
	
	
	public boolean hasColorPicker() {
		return hasColorPicker;
	}
	public boolean hasScrollDetector() {
		return hasScrollDetector;
	}
	public boolean hasPinchAndZoom() {
		return hasPinchAndZoom;
	}
	/***************************************************************
	 * Constructor 
	 ***************************************************************/
	public HUDDescriptor() {
		super();
	}
	/***************************************************************
	 * Overrided functions 
	 ***************************************************************/
	@Override
	public void readXMLDescription(Attributes attr){
		Log.i(TAG,"\t\t readSpriteDescription");
		
		//call XML parser for class parent parameters
		super.readXMLDescription(attr);
	
		String value;
		// read if has colorPiker
		if((value = attr.getValue(ScnTags.S_A_HASCOLORPICKER))!=null) hasColorPicker=Boolean.parseBoolean(value);
		// read if has has Scroll Detector
		if((value = attr.getValue(ScnTags.S_A_HASSCROLLDETECTOR))!=null) hasScrollDetector=Boolean.parseBoolean(value);
		// read if has has Pinch AndZoom
		if((value = attr.getValue(ScnTags.S_A_HASPINCHANDZOOM))!=null) hasPinchAndZoom=Boolean.parseBoolean(value);
	}
	/***************************************************************
	 * Setters & Getters 
	 ***************************************************************/
	public List<ToolsBarDescriptor> getToolBarDescriptors() {
		List<ToolsBarDescriptor> pToolsBars = new ArrayList<ToolsBarDescriptor>();
		Iterator<Entry<Integer, BasicDescriptor>> pChildsIterator =  pChild.entrySet().iterator();
		while(pChildsIterator.hasNext()){
			Entry<Integer, BasicDescriptor> element = pChildsIterator.next();
			if(element.getValue() instanceof ToolsBarDescriptor)
				pToolsBars.add((ToolsBarDescriptor)element.getValue());
		}
		return pToolsBars;
	}
	public void setHasColorPicker(boolean hasColorPicker) {
		this.hasColorPicker = hasColorPicker;
	}
	
}
