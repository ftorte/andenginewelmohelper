package com.welmo.andengine.resources.descriptors.components;



import org.xml.sax.Attributes;

import android.content.Context;
import android.graphics.Typeface;

public class FontDescriptor extends ResourceDescriptor {
	// ===========================================================
	// Constants
	// ===========================================================
	public Typeface TypeFace;
	public boolean AntiAlias;
	public String color;
	public String filename;
	public int texture_sizeX;
	public int texture_sizeY;
	@Override
	public void readXMLDescription(Attributes attributes, Context ctx) {
		super.readXMLDescription(attributes, ctx);
	} 
	
	// ===========================================================
	// Member Variables
	// ===========================================================
}
