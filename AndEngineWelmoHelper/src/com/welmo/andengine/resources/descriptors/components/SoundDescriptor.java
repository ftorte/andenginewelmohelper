package com.welmo.andengine.resources.descriptors.components;

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
}
