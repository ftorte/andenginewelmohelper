package com.welmo.andengine.scenes.descriptors.components;


public class SpriteObjectDescriptor extends BasicObjectDescriptor{
	// enumerators to manage object types & object events
	public enum SpritesTypes {
	    NO_TYPE, STATIC, CLICKABLE, COMPOUND_SPRITE, ANIMATED, COLORING_SPRITE
	}
	
	public SpritesTypes 		type;
	public String 			textureName;
	public int 				nSideATile =0;
	public int 				nSideBTile =0;
	protected String 			soundName = "";
	
	public String getSoundName() {
		return soundName;
	}
	public void setSoundName(String soundName) {
		this.soundName = soundName;
	}
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
	public void setSidesTiles(int sideA, int sideB){
		nSideATile = sideA;
		nSideBTile = sideB;
	}
	public int getSidesA(){
		return nSideATile;
	}
	public int getSidesB(){
		return nSideBTile;
	}
	
	//@Override
	public void copyFrom(SpriteObjectDescriptor copyfrom){
		
		super.copyFrom(copyfrom);
		
		type = copyfrom.type;
		textureName = copyfrom.textureName;
		nSideATile = copyfrom.nSideATile;
		nSideBTile = copyfrom.nSideBTile;
	}
}
