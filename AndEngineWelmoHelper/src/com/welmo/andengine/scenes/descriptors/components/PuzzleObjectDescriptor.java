package com.welmo.andengine.scenes.descriptors.components;

public class PuzzleObjectDescriptor extends BasicObjectDescriptor {
	protected int 			nbColumns			= 0;
	protected int			nbRows				= 0;
	public String 		textureName			="";
	protected float[]		mPiecesBox			= {0,0,0,0};
	protected float[]		mPuzzleZone			= {0,0,0,0};
	protected boolean		hasActiveBorder		= false;		//if true the pieces and container have borders
	protected boolean		hasActiveZone		= false;		//if true the puzzle zone is active and pieces are stick to the zone
	protected String		mHelperImage		= "";			//if different from "" the puzzle zone have as background the final figures in color on withe background with low alpah
	protected float			mHelperImageAlpha	= 0.1f;	

	public boolean hasActiveBorder() {
		return hasActiveBorder;
	}
	public void setHasActiveBorder(boolean hasActiveBorder) {
		this.hasActiveBorder = hasActiveBorder;
	}
	public boolean hasActiveZone() {
		return hasActiveZone;
	}
	public void setHasActiveZone(boolean hasActiveZone) {
		this.hasActiveZone = hasActiveZone;
	}
	public String getHelperImage() {
		return mHelperImage;
	}
	public void setHelperImage(String helperImage) {
		mHelperImage = helperImage;
	}
	public float getHelperImageAlpha() {
		return mHelperImageAlpha;
	}
	public void setHelperImageAlpha(float mHelperZoneAlpha) {
		this.mHelperImageAlpha = mHelperZoneAlpha;
	}
	
	public static int 	INVALIDDIM = -1;
	
	public float[] getPieceBoxGeometry() {
		return mPiecesBox;
	}
	public void setPieceBoxGeometry(float[] geometry) {
		mPiecesBox[0]=geometry[0];
		mPiecesBox[1]=geometry[1];
		mPiecesBox[2]=geometry[2];
		mPiecesBox[3]=geometry[3];
	}
	public float[] getPuzzleZoneGeometry() {
		return mPuzzleZone;
	}
	public void setPuzzleZoneGeometry(float[] geometry) {
		mPuzzleZone[0]=geometry[0];
		mPuzzleZone[1]=geometry[1];
		mPuzzleZone[2]=geometry[2];
		mPuzzleZone[3]=geometry[3];
	}
	
	public String getTextureName() {
		return textureName;
	}
	public void setTextureName(String textureName) {
		this.textureName = textureName;
	}
	public int getNbColumns() {
		return nbColumns;
	}
	public void setNbColumns(int nbColumns) {
		this.nbColumns = nbColumns;
	}
	public int getNbRows() {
		return nbRows;
	}
	public void setNbRows(int nbRows) {
		this.nbRows = nbRows;
	}
	
}
