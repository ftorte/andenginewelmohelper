package com.welmo.andengine.resources.descriptors.components;

import org.xml.sax.Attributes;

import android.content.Context;

import com.welmo.andengine.utility.ScreenDimensionHelper;

public class SoundDescriptor extends ResourceDescriptor{
	public String filename;
	public int duration=0;
	SoundDescriptor(){
		duration=0;
		filename = new String("");
	}
	public int getDuration() {
		return duration;
	}
	public void setDuration(int duration) {
		this.duration = duration;
	}
	@Override
	public void readXMLDescription(Attributes attributes, Context ctx) {
		super.readXMLDescription(attributes, ctx);
	}
}
