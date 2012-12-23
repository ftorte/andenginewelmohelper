package com.welmo.andengine.scenes.descriptors.components;

public enum GameLevel {
	EASY,MEDIUM,DIFFICULT,HARD;
	static public GameLevel fromOrdinal(int ordinal) {
		switch(ordinal){
		case 0: return EASY;
		case 1: return MEDIUM;
		case 2: return DIFFICULT;
		case 3: return HARD;
		default: return EASY; 
		}
	}

}
