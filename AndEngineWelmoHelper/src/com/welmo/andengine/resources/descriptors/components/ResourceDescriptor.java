package com.welmo.andengine.resources.descriptors.components;

import org.xml.sax.Attributes;

import android.content.Context;

import com.welmo.andengine.scenes.descriptors.ScnTags;
import com.welmo.andengine.utility.ScreenDimensionHelper;

public class ResourceDescriptor {
	// ===========================================================
	// Constants
	// ===========================================================

	public static int IVALID_TYPE			=-1;
	public static int TYPE_TEXTURE 			= 1;
	public static int TYPE_TEXTUREREGION 	= 2;
	public static int TYPE_FONT 			= 3;
	public static int TYPE_SOUND			= 4;
	public static int TYPE_MUSIC			= 5;
	public static int IVALID_ID				=-1;
	
	// ===========================================================
	// Member Variables
	// ===========================================================

	public int ID;
	public String Name;
	public int[] Parameters = null;
	
	// ===========================================================
	// Constructor
	// ===========================================================

	public ResourceDescriptor(int id, int type, String name) {
		this.ID = id;
		Name = String.copyValueOf(name.toCharArray());
		Parameters= new int[10];
		for(int i=0; i < 10; i++)
			Parameters[i]=0;
	}
	//@SuppressWarnings("static-access")
	public ResourceDescriptor() {
		this.ID = IVALID_ID;
		Name="";
		Parameters= new int[10];
		for(int i=0; i < 10; i++)
			Parameters[i]=0;
	}


	public void readXMLDescription(Attributes attributes, Context ctx) {
		//variable containing attributes value
		this.ID=0;
		
		ScreenDimensionHelper dimHelper = ScreenDimensionHelper.getInstance(ctx);
		
		String value = null;
		
		if((value=attributes.getValue(ResTags.R_A_NAME)) != null)
			this.Name = new String(value);

		if((value=attributes.getValue(ResTags.R_A_HEIGHT)) != null)
			this.Parameters[ResTags.R_A_HEIGHT_IDX]=Integer.parseInt(value);
		
		if((value=attributes.getValue(ResTags.R_A_WIDTH)) != null)
			this.Parameters[ResTags.R_A_WIDTH_IDX]=Integer.parseInt(value);
		
		if((value=attributes.getValue(ResTags.R_A_POSITION_X)) != null)
			this.Parameters[ResTags.R_A_POSITION_X_IDX]=dimHelper.parsPosition(ScreenDimensionHelper.X,value);
		
		if((value=attributes.getValue(ResTags.R_A_POSITION_Y)) != null)
			this.Parameters[ResTags.R_A_POSITION_Y_IDX]=dimHelper.parsPosition(ScreenDimensionHelper.Y,value);

	}
	
	@Override
	public String toString() {
		return "ResourceDescriptor" +  "]";
	}

}