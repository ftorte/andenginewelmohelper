package com.welmo.andengine.resources.descriptors.components;

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
	}
	//@SuppressWarnings("static-access")
	public ResourceDescriptor() {
		this.ID = IVALID_ID;
		Name="";
		Parameters= new int[10];
	}


	@Override
	public String toString() {
		return "ResourceDescriptor" +  "]";
	}

}