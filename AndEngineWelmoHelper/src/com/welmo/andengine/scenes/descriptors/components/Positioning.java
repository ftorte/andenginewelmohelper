package com.welmo.andengine.scenes.descriptors.components;

public enum Positioning {
	TOP, LEFT, BOTTOM, RIGHT;
	static public Positioning fromOrdinal(int ordinal) {
		switch(ordinal){
		case 0: return TOP;
		case 1: return LEFT;
		case 2: return BOTTOM;
		case 3: return RIGHT;
		default: return TOP; 
		}
	}

}