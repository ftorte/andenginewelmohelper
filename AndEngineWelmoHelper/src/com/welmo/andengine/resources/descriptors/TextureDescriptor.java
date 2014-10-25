package com.welmo.andengine.resources.descriptors;

import java.util.ArrayList;

import org.xml.sax.Attributes;

import android.content.Context;



public class TextureDescriptor extends ResourceDescriptor {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Member Variables
	// ===========================================================
	public boolean 								valid		= false;
	public ArrayList<TextureRegionDescriptor>	Regions		= null;
	public boolean 								autpacking 	= false;
	
	// ===========================================================
	// Constructor
	// ===========================================================

	
	public TextureDescriptor() {
		Regions=new ArrayList<TextureRegionDescriptor>();
	}


	@Override
	public String toString() {
		return "ResourceDescriptor" +  "]";
	}
	@Override
	public void readXMLDescription(Attributes attributes, Context ctx) {
		super.readXMLDescription(attributes, ctx);
	}
	

}
