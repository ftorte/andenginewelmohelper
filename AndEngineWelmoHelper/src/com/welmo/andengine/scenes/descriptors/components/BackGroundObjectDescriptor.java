package com.welmo.andengine.scenes.descriptors.components;

public class BackGroundObjectDescriptor extends BasicComponentDescriptor {
		// enumerators to manage object types & object events
		public enum BackGroundTypes {
		    NO_TYPE, COLOR, SPRITE
		}
		
		public BackGroundTypes type;
		public String color;
		public SpriteObjectDescriptor sprite;
		
		public BackGroundObjectDescriptor(){
			type = BackGroundTypes.NO_TYPE;
			color = "";
			sprite = null;
		}
}
