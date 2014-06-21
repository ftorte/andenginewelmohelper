package com.welmo.andengine.scenes.descriptors.components;

import org.xml.sax.Attributes;

import com.welmo.andengine.scenes.descriptors.ScnTags;

public class ColoringSpriteDescriptor extends BasicComponentDescriptor{
	protected String		sImagefileName;

	@Override
	public void readXMLDescription(Attributes attributes) {
		// TODO Auto-generated method stub
		super.readXMLDescription(attributes);
		String value = null;
		//read the filename of the image
		if((value = attributes.getValue(ScnTags.S_A_FILE_NAME))!=null) 
			this.sImagefileName=new String(value);				
	}

	public String getImageFilename() {
		return sImagefileName;
	}
}
