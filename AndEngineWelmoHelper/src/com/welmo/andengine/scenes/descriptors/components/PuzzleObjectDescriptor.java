package com.welmo.andengine.scenes.descriptors.components;

import java.util.StringTokenizer;

import org.andengine.engine.Engine;
import org.andengine.entity.IEntity;
import org.xml.sax.Attributes;

import com.welmo.andengine.scenes.components.interfaces.IComponent;
import com.welmo.andengine.scenes.components.puzzle.PuzzleSprites;
import com.welmo.andengine.scenes.descriptors.ScnTags;

import android.util.Log;

public class PuzzleObjectDescriptor extends BasicComponentDescriptor {
	
	protected int 			nbColumns			= 0;
	protected int			nbRows				= 0;
	public String 			tiledTextureName	= "";
	public String 			textureResourceName	= "";
	protected float[]		mPiecesBox			= {0,0,0,0};
	protected float[]		mPuzzleZone			= {0,0,0,0};
	protected boolean		hasActiveBorder		= false;		//if true the pieces and container have borders
	protected boolean		hasActiveZone		= false;		//if true the puzzle zone is active and pieces are stick to the zone
	protected boolean		hasActiveZoneBorders= false;		//if true the zone has border to trace the contur of each piece
	protected boolean		hasWhiteBG			= false;		//if true the puzzle zone is active and pieces are stick to the zone
	protected boolean		hasHelperImg		= false;
	protected String		mHelperImage		= "";			//if different from "" the puzzle zone have as background the final figures in color on withe background with low alpah
	protected String		mHelperTextureRegion= "";
	protected float			mHelperImageAlpha	= 0.1f;
	protected boolean 		hasFireworks		= false;
	protected String 		fireworksName		= "";	
	protected long			fireworksDuration	= 1000;			//Default value for firework is 1s
	public static int 		INVALIDDIM 			= -1;
	
	//Has multiple levels
	protected String 		strStingGameLevelsMap = null;
	protected boolean		hasDnamicGameLevel	= false;

	
	public boolean  hasActiveBorder() {return hasActiveBorder;}
	public boolean  hasDynamicGameLevel() {return hasDnamicGameLevel;}
	public void  hasDynamicGameLevel(boolean val) {hasDnamicGameLevel = val;}
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
	public String getGameLevelMap() {
		return this.strStingGameLevelsMap;
	}
	
	
	@Override
	public void readXMLDescription(Attributes attr) {
		Log.i(TAG,"\t\t readPuzzleDescription");
	
		super.readXMLDescription(attr);
	
		String value;
		// Read the puzzle 
		this.ID=Integer.parseInt(attr.getValue(ScnTags.S_A_ID));

		this.tiledTextureName = new String(attr.getValue(ScnTags.S_A_RESOURCE_NAME));
		
		// Read specific puzzle parameters Nb cols & Nb Rows
		if((value = attr.getValue(ScnTags.S_A_NBCOLS))!= null)
			this.setNbColumns(Integer.parseInt(value));
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
		
		// Read if active zone
		if((value = attr.getValue(ScnTags.S_A_ACTIVE_ZONE_BORDERS))!= null)
			this.hasActiveZoneBorders(Boolean.parseBoolean(value));
		else
			this.hasActiveZoneBorders(false);
		
		// Read if active zone
		if((value = attr.getValue(ScnTags.S_A_GAME_LEVEL_MAP))!= null){
			this.hasDynamicGameLevel(true);
			this.strStingGameLevelsMap = new String(value);
		}
	
		
		// Read if has helper image			==> repalced by the helper imlage texture region
		//if((value = attr.getValue(ScnTags.S_A_HAS_HELPER_IMG))!= null)
		//	this.hasHelperImage(Boolean.parseBoolean(value));
			
		// Read if helper image on	
		if(attr.getValue(ScnTags.S_A_HELPER_IMAGE)!= null)
			this.setHelperImage(attr.getValue(ScnTags.S_A_HELPER_IMAGE));
	
		// Read if helper image on
		if((value = attr.getValue(ScnTags.S_A_HELPER_TEXTURE_REGION))!= null){
				this.hasHelperImage(true);
				this.setHelperTextureRegion(value);
		} else{
			this.hasHelperImage(false);
		}
		
		
		// Read if white background
		if(attr.getValue(ScnTags.S_A_HAS_WHITE_BACKGROUND)!= null)
			this.hasWhiteBackground(Boolean.parseBoolean(attr.getValue(ScnTags.S_A_HAS_WHITE_BACKGROUND)));
		
		// Read if helper image on
		if(attr.getValue(ScnTags.S_A_HELPER_IMG_ALPHA)!= null)
			this.setHelperImageAlpha(Float.parseFloat(attr.getValue(ScnTags.S_A_HELPER_IMG_ALPHA)));
		else
			this.setHelperImageAlpha(1.0f);
		// Read if firework to used while pieces are linked
		if((value=attr.getValue(ScnTags.S_A_FIREWORKS))!= null){
			this.hasFireworks=true;
			this.fireworksName = new String(value);
		}
		else
			this.hasFireworks=false;
		
		
		
		// Read if firework to used while pieces are linked
		if((value=attr.getValue(ScnTags.S_A_FIREWORKS_DURATION))!= null) this.fireworksDuration=Long.parseLong(value);
	}
	public void hasHelperImage(boolean hasHelperImg) {
		this.hasHelperImg = hasHelperImg;
	}
	public void hasActiveZoneBorders(boolean value){
		this.hasActiveZoneBorders = value;
	}
	public boolean hasActiveZoneBorders(){
		return this.hasActiveZoneBorders;
	}
	public boolean hasHelperImage() {
		return hasHelperImg;
	}
	@Override
	public IComponent CreateComponentInstance(Engine mEngine) {
		// TODO Auto-generated method stub
		PuzzleSprites puzzle= new PuzzleSprites(this, mEngine);
		return puzzle;
	}
	public boolean hasFireworsk() {
		return this.hasFireworks;
	}
	public String getFireworksName() {
		return this.fireworksName;
	}
	public long getFireworkDuration() {
		return this.fireworksDuration;// TODO Auto-generated method stub
	}
	public String getHelperTextureRegion() {
		return mHelperTextureRegion;
	}
	public void setHelperTextureRegion(String name) {
		mHelperTextureRegion = new String (name);
	}
	
}
