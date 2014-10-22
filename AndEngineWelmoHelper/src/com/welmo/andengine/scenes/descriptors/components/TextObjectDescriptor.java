package com.welmo.andengine.scenes.descriptors.components;

import org.xml.sax.Attributes;

import com.welmo.andengine.scenes.descriptors.ScnTags;

import android.util.Log;

public class TextObjectDescriptor extends BasicComponentDescriptor{
	// enumerators to manage object types & object events
	public enum TextTypes {
		NO_TYPE, SIMPLE
	}
	public TextTypes type = TextTypes.NO_TYPE;;
	public String message;
	public String FontName;
	
	public TextTypes getType() {
		return type;
	}
	public void setType(TextTypes type) {
		this.type = type;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getFontName() {
		return FontName;
	}
	public void setFontName(String fontName) {
		FontName = fontName;
	}
	public TextObjectDescriptor() {
		super();
		this.message=new String("");
		this.FontName=new String("");
	}
	
	@Override
	public void readXMLDescription(Attributes attributes) {
		Log.i(TAG,"\t\t readXMLDescription");
		
		super.readXMLDescription(attributes);
		
		//set touchable = false
		this.isTouchable = false;
				
		this.ID=Integer.parseInt(attributes.getValue(ScnTags.S_A_ID ));
		this.FontName = attributes.getValue(ScnTags.S_A_RESOURCE_NAME);
		this.message = new String (attributes.getValue(ScnTags.S_A_MESSAGE));	
		this.type=TextObjectDescriptor.TextTypes.valueOf(attributes.getValue(ScnTags.S_A_TYPE));
	}
}
