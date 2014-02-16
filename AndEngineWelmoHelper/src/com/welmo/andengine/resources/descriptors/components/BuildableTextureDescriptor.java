package com.welmo.andengine.resources.descriptors.components;

import java.util.ArrayList;

public class BuildableTextureDescriptor extends ResourceDescriptor {

	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Member Variables
	// ===========================================================
	public boolean 									valid=false;
	public ArrayList<TiledTextureRegionDescriptor>	Regions=null;

	// ===========================================================
	// Constructor
	// ===========================================================


	public BuildableTextureDescriptor() {
		Regions=new ArrayList<TiledTextureRegionDescriptor>();
	}


	@Override
	public String toString() {
		return "ResourceDescriptor" +  "]";
	}

}

