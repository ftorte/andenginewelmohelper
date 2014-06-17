package com.welmo.andengine.scenes.descriptors.components;

import java.util.StringTokenizer;

import org.andengine.entity.IEntity;
import org.xml.sax.Attributes;

import com.welmo.andengine.scenes.descriptors.ScnTags;

import android.util.Log;

public class PuzzleObjectDescriptor extends BasicObjectDescriptor {
	
	protected int 			nbColumns			= 0;
	protected int			nbRows				= 0;
	public String 			tiledTextureName	="";
	public String 			textureResourceName	="";
	protected float[]		mPiecesBox			= {0,0,0,0};
	protected float[]		mPuzzleZone			= {0,0,0,0};
	protected boolean		hasActiveBorder		= false;		//if true the pieces and container have borders
	protected boolean		hasActiveZone		= false;		//if true the puzzle zone is active and pieces are stick to the zone
	protected boolean		hasWhiteBG			= false;		//if true the puzzle zone is active and pieces are stick to the zone
	protected String		mHelperImage		= "";			//if different from "" the puzzle zone have as background the final figures in color on withe background with low alpah
	protected float			mHelperImageAlpha	= 0.1f;	
	public static int 		INVALIDDIM = -1;
	
	public boolean hasActiveBorder() {return hasActiveBorder;}
	public boolean 	hasWhiteBackground() {return hasWhiteBG;}
	public void 	hasWhiteBackground(boolean value) {hasWhiteBG = value;}
	public void 	setHasActiveBorder(boolean hasActiveBorder) {this.hasActiveBorder = hasActiveBorder;}
	public boolean 	hasActiveZone() {return hasActiveZone;}
	public void 	setHasActiveZone(boolean hasActiveZone) {this.hasActiveZone = hasActiveZone;}
	public String 	getHelperImage() {return mHelperImage;}
	public void 	setHelperImage(String helperImage) {mHelperImage = helperImage;}
	public float 	getHelperImageAlpha() {return mHelperImageAlpha;}
	public void 	setHelperImageAlpha(float mHelperZoneAlpha) {this.mHelperImageAlpha = mHelperZoneAlpha;}
	
	
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
	
	
	public String getTiledTextureName() {
		return tiledTextureName;
	}
	public void getTiledTextureResourceName(String testureName) {
		this.tiledTextureName = testureName;
	}
	public String getTiledTextureResourceName() {
		return textureResourceName;
	}
	public void setTextureFileName(String resourceName) {
		this.textureResourceName = resourceName;
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
	
	
	@Override
	public void readXMLDescription(Attributes attr) {
		Log.i(TAG,"\t\t readPuzzleDescription");
	
		super.readXMLDescription(attr);
	
		// Read the puzzle 
		this.ID=Integer.parseInt(attr.getValue(ScnTags.S_A_ID));

		this.tiledTextureName = new String(attr.getValue(ScnTags.S_A_RESOURCE_NAME));
		
		// Read specific puzzle parameters Nb cols & Nb Rows
		if(attr.getValue(ScnTags.S_A_NBCOLS)!= null)
			this.setNbColumns(Integer.parseInt(attr.getValue(ScnTags.S_A_NBCOLS)));
		else
			this.setNbColumns(1);
		if(attr.getValue(ScnTags.S_A_NBROWS)!= null)
			this.setNbRows(Integer.parseInt(attr.getValue(ScnTags.S_A_NBROWS)));
		else
			this.setNbRows(1);
		
		// Read specific puzzle geometries
		float[] geometry = new float[4];
		// Read specific puzzle zone
		if(attr.getValue(ScnTags.S_A_PUZZLE_ZONE)!= null){
			int i=0;
			StringTokenizer st = new StringTokenizer(attr.getValue(ScnTags.S_A_PUZZLE_ZONE),";");
		    while (st.hasMoreTokens()) {
		    	geometry[i++]=Float.parseFloat(st.nextToken());
		     }
		    this.setPuzzleZoneGeometry(geometry);
		}
		// Read specific puzzle box
		if(attr.getValue(ScnTags.S_A_PUZZLE_BOX)!= null){
			int i=0;
			StringTokenizer st = new StringTokenizer(attr.getValue(ScnTags.S_A_PUZZLE_BOX),";");
		    while (st.hasMoreTokens()) {
		    	geometry[i++]=Float.parseFloat(st.nextToken());
		     }
		    this.setPieceBoxGeometry(geometry);
		}
		// Read if active border
		if(attr.getValue(ScnTags.S_A_ACTIVE_BORDER)!= null)
			this.setHasActiveBorder(Boolean.parseBoolean(attr.getValue(ScnTags.S_A_ACTIVE_BORDER)));
		else
			this.setHasActiveBorder(false);

		// Read if active zone
		if(attr.getValue(ScnTags.S_A_ACTIVE_ZONE)!= null)
			this.setHasActiveZone(Boolean.parseBoolean(attr.getValue(ScnTags.S_A_ACTIVE_ZONE)));
		else
			this.setHasActiveZone(false);
		// Read if helper image on
		if(attr.getValue(ScnTags.S_A_HELPER_IMAGE)!= null)
			this.setHelperImage(attr.getValue(ScnTags.S_A_HELPER_IMAGE));
		
		// Read if white background
		if(attr.getValue(ScnTags.S_A_HAS_WHITE_BACKGROUND)!= null)
			this.hasWhiteBackground(Boolean.parseBoolean(attr.getValue(ScnTags.S_A_HAS_WHITE_BACKGROUND)));
		
		// Read if helper image on
		if(attr.getValue(ScnTags.S_A_HELPER_IMG_ALPHA)!= null)
			this.setHelperImageAlpha(Float.parseFloat(attr.getValue(ScnTags.S_A_HELPER_IMG_ALPHA)));
		else
			this.setHelperImageAlpha(1.0f);
		
	}
}
