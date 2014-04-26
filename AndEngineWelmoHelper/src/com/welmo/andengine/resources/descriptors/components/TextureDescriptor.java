package com.welmo.andengine.resources.descriptors.components;

import java.util.ArrayList;



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

}
