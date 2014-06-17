package com.welmo.andengine.resources.descriptors.components;

import org.xml.sax.Attributes;

import android.content.Context;

public class MusicDescriptor extends ResourceDescriptor{
	public String filename;
	@Override
	public void readXMLDescription(Attributes attributes, Context ctx) {
		super.readXMLDescription(attributes, ctx);
	}
}
