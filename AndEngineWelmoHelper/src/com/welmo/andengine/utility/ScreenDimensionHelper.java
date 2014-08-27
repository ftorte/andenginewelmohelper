package com.welmo.andengine.utility;

import android.content.Context;
import android.view.Display;
import android.view.WindowManager;

public class ScreenDimensionHelper {
	
	private static ScreenDimensionHelper mInstance=null;
	private Display display = null;
	private int width =0;
	private int height = 0;
	
	public static int X = 0;
	public static int Y = 1;
	public static int W = 0;
	public static int H = 1;
	int orientation; 
	
	@SuppressWarnings("deprecation")
	private ScreenDimensionHelper(Context ctx){
		
		WindowManager wm = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
		display = wm.getDefaultDisplay();
		width = display.getWidth();
		height = display.getHeight();
	}
	@method
	public static ScreenDimensionHelper getInstance(Context ctx){
		if(mInstance == null)
			mInstance = new  ScreenDimensionHelper(ctx);
		return mInstance;
	}
	public int getWidth() {
		return width;
	}
	public int getHeight() {
		return height;
	}
	public int getPositionFromPctRatio(int typeAxis, int pctRatio){
		//if check ration is between 0% to 100%
		if(pctRatio > 100 || pctRatio < 0)
			throw new IllegalArgumentException("PositionHelper:getPxFromPctRatio encountered position purcentage ration above limits (0, 1)");
		
		//if requested value is position X
		if(typeAxis == ScreenDimensionHelper.X){
				return width*pctRatio/100;
		}
		//if requested value is position Y
		if(typeAxis == ScreenDimensionHelper.Y){
			return height*pctRatio/100;
		}
		//Default return 0	
		return 0;
	}
	
	public int parsPosition(int type,String position){
		if(position.charAt(position.length()-1)=='%')
			return this.getPositionFromPctRatio(type,Integer.parseInt(position.substring(0, position.length()-1)));
		else
			return Integer.parseInt(position);
	}
	public int parsLenght(int type,String position){
			return parsPosition(type,position);
	}
}
