package com.welmo.andengine.resources.descriptors.components;

import org.xml.sax.Attributes;

import android.content.Context;

public class TiledTextureRegionDescriptor extends ResourceDescriptor{
	public String textureName;
	public String filename;
	public int column;
	public int row;
	@Override
	public void readXMLDescription(Attributes attributes, Context ctx) {
		super.readXMLDescription(attributes, ctx);
	}
}
