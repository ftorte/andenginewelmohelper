package com.welmo.andengine.scenes.descriptors.components;

public class TextObjectDescriptor extends BasicObjectDescriptor{
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
	

}
