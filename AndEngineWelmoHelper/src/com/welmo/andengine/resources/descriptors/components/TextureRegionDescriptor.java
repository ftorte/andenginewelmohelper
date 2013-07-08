package com.welmo.andengine.resources.descriptors.components;



public class TextureRegionDescriptor extends ResourceDescriptor{
	public enum TEXTURETYPE {SVG, PNG};
	public String textureName;
	public String filename;
	public TEXTURETYPE type;
	
	TextureRegionDescriptor(){
		filename = "";
		textureName = "";
		type = TEXTURETYPE.SVG;
	}
}
