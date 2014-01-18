package com.welmo.andengine.utility;

public class ColorHelper {
	public static float Red(int color){
		int red = ((color >> 16) & 0x0000FF);
		return (float)red/256;
	}
	public static  float Green(int color){
		int green = ((color >> 8) & 0x0000FF);
		return (float)green/256;
	}
	public static  float Blue(int color){
		int blue = (color & 0x0000FF);
		return (float)blue/256;
	}
}
