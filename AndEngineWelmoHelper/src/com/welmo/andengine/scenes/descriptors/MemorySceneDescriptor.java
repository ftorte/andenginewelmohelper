package com.welmo.andengine.scenes.descriptors;

import com.welmo.andengine.scenes.descriptors.components.GameLevel;
import com.welmo.andengine.scenes.descriptors.components.SceneDescriptor;

public class MemorySceneDescriptor extends SceneDescriptor{
	
	//  ========================================================
	//  Value of difficulties (are row index in memoryeStructure}
	//  =========================================================		
	//public static final int 				EASY 		= 0;
	//public static final int 				MEDIUM 		= 1;
	//public static final int 				DIFFICULT 	= 2;
	//public static final int 				HARD 		= 3;
	//Value of parameter (are column index in memoryeStructure}
	public static final int 				DIFFICULTY 	= 0;
	public static final int 				HEIGHT 		= 1;
	public static final int 				WIDTH 		= 2;
	public static final int 				NB_SYMBOLS 	= 3;
	public static final int					GEOMETRY_DSC_NBCOL = 4;
	
	protected GameLevel						nMaxLevelAllowed;
	protected int							nTopBottomBorder;
	protected int							nLeftRightBorder;
	protected int							nVIntraBorder;
	protected int							nHIntraBorder;
	protected int							nStdCardHeight;
	protected int							nStdCardWidth;
	protected String 						resouceName;
	protected int  							nMaxNbOfSymbols;
	protected int[][]						memoryStructure = {{0,3,5,7},{1,2,4,4},{2,5,8,18},{3,6,10,30}};
	protected int[][]						memoryMapOfCardsTiles;
	
	public int[][] getMemoryMapOfCardsTiles() {
		return memoryMapOfCardsTiles;
	}
	public void setMemoryMapOfCardsTiles(int[][] memoryMapOfCardsTiles) {
		this.memoryMapOfCardsTiles = memoryMapOfCardsTiles;
	}
	public void setMemoryStructure(int[][] memoryStructure) {
		this.memoryStructure = memoryStructure;
	}
	public int getMaxNbOfSymbols() {
		return nMaxNbOfSymbols;
	}
	public void setMaxNbOfSymbols(int MaxNbOfSymbols) {
		this.nMaxNbOfSymbols = MaxNbOfSymbols;
	}
	public String getResouceName() {
		return resouceName;
	}
	public void setResouceName(String resouceName) {
		this.resouceName = resouceName;
	}

	//  ========================================================
	//  Getter & Setters
	//  =========================================================	
	public GameLevel getMaxLevelAllowed() {
		return nMaxLevelAllowed;
	}
	public int getTopBottomBorder() {
		return nTopBottomBorder;
	}
	public int getLeftRightBorder() {
		return nLeftRightBorder;
	}
	public int getVIntraBorder() {
		return nVIntraBorder;
	}
	public int getHIntraBorder() {
		return nHIntraBorder;
	}
	public int getStdCardHeight() {
		return nStdCardHeight;
	}
	public int getStdCardWidth() {
		return nStdCardWidth;
	}
	public  int[][] getMemoryStructure() {
		return memoryStructure;
	}
	
	
	public void setMaxLevelAllowed(GameLevel nMaxLevelAllowed) {
		this.nMaxLevelAllowed = nMaxLevelAllowed;
	}
	public void setTopBottomBorder(int nTopBottomBorder) {
		this.nTopBottomBorder = nTopBottomBorder;
	}
	public void setLeftRightBorder(int nLeftRightBorder) {
		this.nLeftRightBorder = nLeftRightBorder;
	}
	public void setVIntraBorder(int nVIntraBorder) {
		this.nVIntraBorder = nVIntraBorder;
	}
	public void setHIntraBorder(int nHIntraBorder) {
		this.nHIntraBorder = nHIntraBorder;
	}
	public void setStdCardHeight(int nStdCardHeight) {
		this.nStdCardHeight = nStdCardHeight;
	}
	public void setStdCardWidth(int nStdCardWidth) {
		this.nStdCardWidth = nStdCardWidth;
	}
	
	public MemorySceneDescriptor(){
		//Default Values
		nMaxLevelAllowed 	= GameLevel.HARD;
		nTopBottomBorder 	= 40;
		nLeftRightBorder 	= 40;
		nVIntraBorder 		= 10;
		nHIntraBorder 		= 10;
		nStdCardHeight 		= 175;
		nStdCardWidth 		= 125;
		resouceName			= new String();
		nMaxNbOfSymbols 	= 30;
	}
}
