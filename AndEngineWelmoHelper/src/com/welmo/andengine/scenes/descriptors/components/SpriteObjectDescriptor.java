package com.welmo.andengine.scenes.descriptors.components;


public class SpriteObjectDescriptor extends BasicObjectDescriptor{
	// enumerators to manage object types & object events
	public enum SpritesTypes {
	    NO_TYPE, STATIC, CLICKABLE, COMPOUND_SPRITE, ANIMATED
	}
	
	protected SpritesTypes type;
	protected String textureName;
	
	public SpritesTypes getType() {
		return type;
	}
	public void setType(SpritesTypes type) {
		this.type = type;
	}
	public String getTextureName() {
		return textureName;
	}
	public void setTextureName(String textureName) {
		this.textureName = textureName;
	}
	public SpriteObjectDescriptor() {
		super();
		this.type=SpritesTypes.NO_TYPE;
		this.textureName=new String("");
	}
}
