package com.welmo.andengine.resources.descriptors;

import org.xml.sax.Attributes;

import android.content.Context;



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
	@Override
	public void readXMLDescription(Attributes attributes, Context ctx) {
		super.readXMLDescription(attributes, ctx);
	}
}
