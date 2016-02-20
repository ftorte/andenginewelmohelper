package com.welmo.andengine.scenes.components.puzzle;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.andengine.audio.sound.Sound;
import org.andengine.engine.Engine;
import org.andengine.entity.IEntity;
import org.andengine.entity.particle.SpriteParticleSystem;
import org.andengine.entity.particle.emitter.BaseParticleEmitter;
import org.andengine.entity.primitive.Line;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.shape.IAreaShape;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.TiledSprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.texture.region.TextureRegion;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.HandlerThread;
import android.util.Log;
import android.util.Pair;

import com.welmo.andengine.managers.ParticuleSystemManager;
import com.welmo.andengine.managers.ResourcesManager;
import com.welmo.andengine.managers.SharedPreferenceManager;
import com.welmo.andengine.scenes.IManageableScene;
import com.welmo.andengine.scenes.components.CardSprite.CardSide;
import com.welmo.andengine.scenes.components.interfaces.IActionSceneListener;
import com.welmo.andengine.scenes.components.interfaces.IComponent;
import com.welmo.andengine.scenes.components.interfaces.IComponentEventHandler;
import com.welmo.andengine.scenes.components.interfaces.IComponentLifeCycle;
import com.welmo.andengine.scenes.components.interfaces.IComponentLifeCycleListener;
import com.welmo.andengine.scenes.components.interfaces.IPersistent;
import com.welmo.andengine.scenes.descriptors.BasicDescriptor;
import com.welmo.andengine.scenes.descriptors.MemorySceneDescriptor;
import com.welmo.andengine.scenes.descriptors.ScnTags;
import com.welmo.andengine.scenes.descriptors.components.GameLevel;
import com.welmo.andengine.scenes.descriptors.components.PuzzleObjectDescriptor;
import com.welmo.andengine.scenes.descriptors.events.ComponentEventHandlerDescriptor;
import com.welmo.andengine.scenes.descriptors.events.ComponentEventHandlerDescriptor.Events;
import com.welmo.andengine.scenes.descriptors.events.SceneActions;
import com.welmo.andengine.scenes.descriptors.events.SceneActions.ActionType;
import com.welmo.andengine.scenes.operations.IOperationHandler;

/**
 * @author SESA10148
 * This class implement a rectangle containing puzzle elements. Each puzzle element is constitute by a squared sprite
 * 
 *
 */
public class PuzzleSprites extends Rectangle implements IComponent, IComponentLifeCycle, IComponentEventHandler, IActionSceneListener,IPersistent{
	// --------------------------------------------------------------------
	// constants
	// --------------------------------------------------------------------
	public static final int X				= 0;
	public static final int Y				= 1;
	
	public static final int PX0				= 0;
	public static final int PY0				= 1;
	public static final int WIDTH			= 2;
	public static final int HEIGHT			= 3;
	public static final int DEFAULT_PX0		= 4;
	public static final int DEFAULT_PY0		= 5;
	
	
	public static final int STATUS_START	= 1;
	public static final int STATUS_ONGOING	= 2;
	public static final int STATUS_END		= 3;
	public static final int STATUS_PAUSED   = 4;
	
	
	public static final int DEFAULT_ZINDEX 					= 20;
	public static final int STACK_ZINDEX					= 20;
	public static final int ACTIVE_ZONE_ZINDEX				= 10;
	public static final int SELECTED_ZINDEX 				= 100;
	public static final int SELECTED_NONE					= -1;
	public static final float ACTIVE_ZONE_TRANSPARANCE		= 0.8f;

	
	
	// --------------------------------------------------------------------
	protected	int 							nID					= 0;
	
	protected	int 							nbRows				= 0;
	protected	int 							nbCols				= 0;
	protected	int 							nbPieces			= 0;
	protected	float 							mPieceWidth			= 0;
	protected	float 							mPieceHeight 		= 0;
	
	public		long							fStartTimeIn_ms		= 0; 
	
	
	protected	float 							mMaxPositionX		= 0;
	protected	float 							mMaxPositionY		= 0;
	
	protected	float[]							mPuzzle				= {0,0,0,0};
	protected	float[]							mPiecesBox			= {0,0,0,0};
	protected	float[]							mPuzzleZone			= {0,0,0,0,0,0};
	
	private 	boolean							hasActiveBorder		= false;		//if true the pieces and container have borders
	private 	boolean							hasActiveZone		= false;		//if true the puzzle zone is active and pieces are stick to the zone
	private 	boolean							hasActiveZoneBorders= false;		//if true there will be a border around each puzzle expected place (this is valid only if hasActiveZone = true
	private 	boolean							hasWhiteBackground	= false;		//if true the puzzle pieces have a white background
	private 	boolean							hasHelperImg		= false;		//if true the puzzle has the helper image as background of puzzle zone
	private 	String							mHelperImage		= "";			//if <> "" the puzzle zone have as background the final figures in color on withe background with low alpah
	private 	String							mHelperTextureRegion= "";
	private 	String 							mTiledTextureName	= "";	
	private 	String 							mTiledTextureResource = "";	
	private 	ITiledTextureRegion 			pTiledTexture		= null;
	private     Sprite 							sHelperImage		= null;	
	private     Rectangle 						sActiveZone			= null;	
	
	//Has multiple levels
	protected boolean 							hasDynamicGameLevel	=false;
	protected List<Pair<Integer,Integer>> 		lGameLevelMap		= null;
	protected int 								nGameLevel			= 0;
	
	private 	float							mHelperImageAlpha	= 0.4f;		    //if true the puzzle zone have as background the final figures in color on withe background with low alpah
	
	protected	int 							mZOrder				=0;
	
	protected	PuzzleElement[] 				mPieces				= null;
	protected	List<PuzzleElementContainer> 	mContainersList 	= null;
	protected	List<PuzzleElement> 			mPiecesList 		= null;
	
	//fireworks effect
	protected 	boolean							hasFireworks		= false;
	protected 	SpriteParticleSystem			theFireworks		= null;
	protected 	HandlerThread					pHandeler			= null;
	protected 	long 							nFireworkDuration	= 1000;
	
	protected	Engine							mTheEngine 			= null;
	
	
	//listeners
	protected 	IComponentLifeCycleListener		mLifeCycleListener	= null;
	protected 	int								mStatus				= STATUS_START;
	protected 	IActionSceneListener			mActSceneListener 	= null;
	protected   Sound 							sndTouch			= null;
	protected   Sound 							sndEndLeveSuccess	= null;
	protected   Sound 							sndEndLeveFail		= null;
	
	//handle slected Pieces & 
	protected 	int								nSelectedPiece		= SELECTED_NONE;
	protected 	int								nSelectedContainer	= SELECTED_NONE;
	
	//persistence
	protected SharedPreferenceManager			pSPM				= null;
	protected String							persistentValue		= "GameLevel";

	// --------------------------------------------------------------------
	// Constructors
	// --------------------------------------------------------------------

	public PuzzleSprites(PuzzleObjectDescriptor pDescriptor , Engine pEngine) {
		super(pDescriptor.getIPosition().getX(), pDescriptor.getIPosition().getY(), 
				pDescriptor.getIDimension().getWidth(), pDescriptor.getIDimension().getHeight(), pEngine.getVertexBufferObjectManager());
					
		//read Parameters
		mTheEngine				= pEngine;
		
		//read puzzle geometry
		mPuzzle[PX0]			= pDescriptor.getIPosition().getX();
		mPuzzle[PY0]			= pDescriptor.getIPosition().getY();
		mPuzzle[WIDTH]			= pDescriptor.getIDimension().getWidth();
		mPuzzle[HEIGHT]			= pDescriptor.getIDimension().getHeight();
		
		//read piece box geometry
		mPiecesBox[PX0]			= pDescriptor.getPieceBoxGeometry()[PX0];
		mPiecesBox[PY0]			= pDescriptor.getPieceBoxGeometry()[PY0];
		mPiecesBox[WIDTH]		= pDescriptor.getPieceBoxGeometry()[WIDTH];
		mPiecesBox[HEIGHT]		= pDescriptor.getPieceBoxGeometry()[HEIGHT];
		
		//read puzzle zone geometry
		mPuzzleZone[DEFAULT_PX0]= pDescriptor.getPuzzleZoneGeometry()[PX0];
		mPuzzleZone[DEFAULT_PY0]= pDescriptor.getPuzzleZoneGeometry()[PY0];
		mPuzzleZone[WIDTH]		= pDescriptor.getPuzzleZoneGeometry()[WIDTH];
		mPuzzleZone[HEIGHT]		= pDescriptor.getPuzzleZoneGeometry()[HEIGHT];
		
		hasActiveBorder			= pDescriptor.hasActiveBorder();
		hasActiveZoneBorders	= pDescriptor.hasActiveZoneBorders();
		hasActiveZone			= pDescriptor.hasActiveZone();
		hasWhiteBackground		= pDescriptor.hasWhiteBackground();
		hasHelperImg			= pDescriptor.hasHelperImage();
		
		mHelperImage			= new String(pDescriptor.getHelperImage());
		mHelperTextureRegion 	= new String(pDescriptor.getHelperTextureRegion());
		
		mTiledTextureName		= new String(pDescriptor.getTiledTextureName());
		mTiledTextureResource	= new String(pDescriptor.getTiledTextureResourceName());
		
		
		mMaxPositionX 			= mPuzzle[WIDTH];
		mMaxPositionY 			= mPuzzle[HEIGHT];
		mZOrder					= pDescriptor.getIPosition().getZorder();
		
		//get geometry 
		setGeometry(pDescriptor.getNbRows(),pDescriptor.getNbColumns());
		
		if(hasDynamicGameLevel= pDescriptor.hasDynamicGameLevel()){
			lGameLevelMap = new ArrayList<Pair<Integer,Integer>>();
			readGameLevelMap(pDescriptor.getGameLevelMap());
		}
				
		//Init Array
		mContainersList 		= new ArrayList<PuzzleElementContainer>();
		mPiecesList 			= new ArrayList<PuzzleElement>();
		
		//fireworks
		if(pDescriptor.hasFireworsk()){
			hasFireworks			= true;
			nFireworkDuration		= pDescriptor.getFireworkDuration();
			this.theFireworks		= ParticuleSystemManager.getInstance().getParticuleSystem(pDescriptor.getFireworksName());	
			if(this.theFireworks == null){
				hasFireworks = false;
			}	
		}
		
		//Looper.prepare();
		pHandeler 				= new HandlerThread("handle ParticleSystem"); 
		mStatus					= STATUS_START;
		
		//effects
		ResourcesManager rMgr = ResourcesManager.getInstance();
		sndTouch			= rMgr.getSound("puzzlepieces_touch").getTheSound();;
		sndEndLeveSuccess	= rMgr.getSound("level_win").getTheSound();;
		sndEndLeveFail 		= rMgr.getSound("puzzlepieces_touch").getTheSound();;
	}
	

	private void readGameLevelMap(String gameLevelMap) {
		//Parse JSON strings
		JSONObject jObject;
		try {
			jObject = new JSONObject(gameLevelMap);
			JSONArray gamelevels = jObject.getJSONArray(ScnTags.S_A_GAME_LEVEL_MAP);
			//parse the array
			for (int i=0; i<gamelevels.length(); i++){
				JSONArray currLine = (JSONArray)gamelevels.get(i);
				lGameLevelMap.add(new Pair<Integer,Integer>(currLine.getInt(1),currLine.getInt(2)));
			} 
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	// --------------------------------------------------------------------
	// members' getters & setters
	// --------------------------------------------------------------------
	public String getmTiledTextureResource() {
		return mTiledTextureResource;
	}
	public void setmTiledTextureResource(String mTiledTextureResource) {
		this.mTiledTextureResource = mTiledTextureResource;
	}
	public String getmHelperImage() {
		return mHelperImage;
	}
	public void setmHelperImage(String mHelperImage) {
		this.mHelperImage = new String(mHelperImage);
	}
	public int   getNbRows() {
		return nbRows;
	}
	public void  setNbRows(int nbRows) {
		this.nbRows = nbRows;
		nbPieces = this.nbRows*this.nbCols;
	}
	public int   getNbCols() {
		return nbCols;
	}
	public void  setNbCols(int nbCols) {
		this.nbCols = nbCols;
		nbPieces = this.nbRows*this.nbCols;
	}
	public int   getNbPieces() {
		return nbPieces;
	}
	public float getPieceWidth() {
		return mPieceWidth;
	}
	public void  setPieceWidth(float pieceWidth) {
		this.mPieceWidth = pieceWidth;
	}
	public float getPieceHeight() {
		return mPieceHeight;
	}
	public void  setPieceHeight(float pieceHeight) {
		this.mPieceHeight = pieceHeight;
	}
	public TiledSprite[] getPieces() {
		return mPieces;
	}
	public void setPieces(PuzzleElement[] pieces) {
		this.mPieces = pieces;
	}
	public void setLifeCycleListener(IComponentLifeCycleListener mLifeCycleLeastener) {
		this.mLifeCycleListener = mLifeCycleLeastener;
	}
	public void setGeometry(int nRows, int nCols){
		nbRows 					= nRows;
		nbCols 					= nCols;
		nbPieces 				= nbRows*nbCols;
	}
	// ------------------------------------------------------------------------------------
	// public memebers
	// ------------------------------------------------------------------------------------
	public void clearPuzzle(){
	
		if(mStatus==STATUS_START)
			return;

		
		//clear selection of pieces and container
		unSelectPiece();
		unSelectContainer();
		
		//detach all childres
		this.detachChildren();

		//change parent to all pieces to be attached to the Puzzle Sprite
		mPiecesList.clear();							//clear Piece list		
	
		mContainersList.clear();
		
		
		//clear Helper Image
		this.detachChild(this.sHelperImage);
		sHelperImage =  null;
		
		//clear Active Zone
		this.detachChildren();
		this.detachChild(this.sActiveZone);
		this.sActiveZone = null;
		
		//reset counter to 0
		fStartTimeIn_ms = 0;
		
		
		//force Zindex short
		this.sortChildren(true);
		
		mStatus=STATUS_START;
		return;
	}
	public void createPuzzle(){
		
		ResourcesManager pRM = ResourcesManager.getInstance();
		
		//Set additional parameters
		setZIndex(mZOrder);
		
		setAlpha(0);

		//if dynamic level active configure geometry in function of the current game level 
		if(this.hasDynamicGameLevel){
			doLoad();
			if(nGameLevel < 0 || nGameLevel >= lGameLevelMap.size())
				nGameLevel = 0;
			Pair<Integer,Integer> gameLevel = lGameLevelMap.get(nGameLevel);
			setGeometry(gameLevel.first,gameLevel.second);
		}
		
		//get the texture
		pTiledTexture = pRM.getDinamicTiledTextureRegion(mTiledTextureName, 
				mTiledTextureResource, nbCols, nbRows);

		//Calculate to zoom factor 
		float zoomRatio = 1;
		if(mPuzzleZone[WIDTH] != 0 && mPuzzleZone[HEIGHT] != 0 ){
			mPieceWidth = mPuzzleZone[WIDTH]/nbCols;
			mPieceHeight = mPuzzleZone[HEIGHT]/nbRows;
			zoomRatio = Math.min(mPieceWidth/pTiledTexture.getWidth(),mPieceHeight/pTiledTexture.getHeight()) ;
		}
		mPieceWidth  = pTiledTexture.getWidth()*zoomRatio;
		mPieceHeight = pTiledTexture.getHeight()*zoomRatio;
		
		//set Origin X and Y for puzzle zone 
		mPuzzleZone[PX0] = (Float) (mPuzzleZone[WIDTH] 	> mPieceWidth  * nbCols ? mPuzzleZone[DEFAULT_PX0] + ((mPuzzleZone[WIDTH] - (mPieceWidth * nbCols) )/2 ): mPuzzleZone[DEFAULT_PX0]);
		mPuzzleZone[PY0] = (Float) (mPuzzleZone[HEIGHT] > mPieceHeight * nbRows ? mPuzzleZone[DEFAULT_PY0] + ((mPuzzleZone[HEIGHT] - (mPieceHeight * nbRows) )/2 ): mPuzzleZone[DEFAULT_PY0]);
		
		//Create vectors of tiled sprite pointers
		mPieces = new PuzzleElement[nbPieces];
		
		float[] pXY = new float[2];
		
		//create the pieces		
		for (int i=0; i < nbPieces; i++){
				mPieces[i] = new PuzzleElement(mPieceWidth, mPieceHeight, hasActiveBorder, this, hasWhiteBackground, pTiledTexture, mTheEngine.getVertexBufferObjectManager());
				//Attach the puzzle element to the entity
				mPieces[i].setCurrentTileIndex(i);
				mPieces[i].setID(i);
				mPieces[i].setXYLimit(0,0,mMaxPositionX,mMaxPositionY);
				if(hasActiveZone){
					pXY[X] = mPuzzleZone[X] + mPieceWidth * (int)(i% nbCols);
					pXY[Y] = mPuzzleZone[Y] + mPieceHeight * (int)(i/nbCols);
					mPieces[i].setActiveZoneXY(pXY);
					mPieces[i].setZIndex(DEFAULT_ZINDEX+i);
				}
				mPiecesList.add(mPieces[i]);
				attachChild(mPieces[i]);
		}
		
		setUpStartingPosition();

		setUpNeighbor();
		
		if(mLifeCycleListener != null)
			mLifeCycleListener.onStart();
		
		fStartTimeIn_ms = System.currentTimeMillis();
		
		setUpHelperImage();

		setUpActiveZone();

		
		//Activate foreworks
		if(hasFireworks && 	this.theFireworks != null && !theFireworks.hasParent()){
				this.attachChild(theFireworks);
				this.theFireworks.setParticlesSpawnEnabled(false);
		}		
		mStatus	= STATUS_ONGOING;
	}
	public void setUpHelperImage(){
		if(hasHelperImg && !mHelperImage.isEmpty()){
			ResourcesManager pRM = ResourcesManager.getInstance();
			
			//ITextureRegion theImage=pRM.getTextureRegion(mHelperImage);							//changed with dynamic load
			ITextureRegion theImage = pRM.loadDynamicTextureRegion(mHelperTextureRegion,mHelperImage);
			
			sHelperImage = new Sprite(mPuzzleZone[PX0],mPuzzleZone[PY0], mPieceWidth * nbCols,mPieceHeight*nbRows,theImage,mTheEngine.getVertexBufferObjectManager());
			
			this.attachChild(sHelperImage);
			sHelperImage.setAlpha(mHelperImageAlpha);
			sHelperImage.setZIndex(STACK_ZINDEX);
		}
	}
	public void setUpActiveZone(){
		
		if(this.hasActiveZone){
			sActiveZone = new Rectangle(mPuzzleZone[PX0] ,mPuzzleZone[PY0] ,mPieceWidth * nbCols,mPieceHeight*nbRows,mTheEngine.getVertexBufferObjectManager());
			sActiveZone.setAlpha(ACTIVE_ZONE_TRANSPARANCE);
			sHelperImage.setZIndex(ACTIVE_ZONE_ZINDEX);
			this.attachChild(sActiveZone);
		}
		Line  theLine = null;
		if(this.hasActiveZone && this.hasActiveZoneBorders){
			for(int i=0; i <= this.nbCols; i++){
				theLine = new Line(i*mPieceWidth,0,i*mPieceWidth,mPieceHeight*nbRows, mTheEngine.getVertexBufferObjectManager());
				theLine.setColor(1, 0, 0);
				theLine.setZIndex(ACTIVE_ZONE_ZINDEX);
				sActiveZone.attachChild(theLine);
			}
			for(int i=0; i <= this.nbRows; i++){
				theLine = new Line(0,i*mPieceHeight,nbCols*mPieceWidth, i*mPieceHeight,mTheEngine.getVertexBufferObjectManager());
				theLine.setColor(1, 0, 0);
				theLine.setZIndex(ACTIVE_ZONE_ZINDEX);
				sActiveZone.attachChild(theLine);
			}
		}
	}
	public void setUpNeighbor(){
		//setup neighbors
		int pieceNb=0;
		for (int i=0; i < nbRows; i++){
			for (int j=0; j < nbCols; j++){
				//set TOP
				if(i!= 0)
					mPieces[pieceNb].setNeighbors(PuzzleElement.NEIGHTBOR_TOP, mPieces[pieceNb - nbCols]);
				//set BOTTOM
				if(i<(nbRows-1))
					mPieces[pieceNb].setNeighbors(PuzzleElement.NEIGHTBOR_BOTTOM, mPieces[pieceNb + nbCols]);
				//set LEFT
				if(j!= 0)
					mPieces[pieceNb].setNeighbors(PuzzleElement.NEIGHTBOR_LEFT,mPieces[pieceNb-1]);
				//set RIGHT
				if(j<(nbCols-1))
					mPieces[pieceNb].setNeighbors(PuzzleElement.NEIGHTBOR_RIGHT,mPieces[pieceNb+1]);
				pieceNb++;
			}
		}
	}
	public void setUpStartingPosition(){

		if((mPiecesBox[WIDTH] !=0) && (mPiecesBox[HEIGHT]!=0)){
			float[] deltaXY = {mPiecesBox[WIDTH] - mPieceWidth,mPiecesBox[HEIGHT] - mPieceHeight};
			
			Random randomGenerator = new Random();
			
			for (int i=0; i < this.nbPieces; i++){
				float rnd1 = randomGenerator.nextFloat();
				float rnd2 = randomGenerator.nextFloat();
				mPieces[i].setPosition(mPiecesBox[PX0] + (deltaXY[PX0] * rnd1), 
									  mPiecesBox[PY0] + (deltaXY[PY0] * rnd2));
			}
		}
		else{
			float[] pXY ={0,0};
			int pieceNb=0;
			for (int i=0; i < nbRows; i++){
				for (int j=0; j < nbCols; j++){
					mPieces[pieceNb].setPosition(pXY[PX0], pXY[PY0]);
					pXY[PX0] = pXY[PX0]+ mPieceWidth +30;
					pieceNb+=1;
				}
				pXY[PX0]=0;
				pXY[PY0] = pXY[PY0]+ mPieceHeight +30;
			}
		}

	}	
	public void createNewContainer(int nearestType, PuzzleElement theElement, PuzzleElement theNeighbor){

		PuzzleElementContainer newContainer = new PuzzleElementContainer(0,0,0,0,true,this.mRectangleVertexBufferObject,this);
		this.attachChild(newContainer);
		mContainersList.add(newContainer);

		newContainer.setXYLimit(0,0,mTheEngine.getCamera().getWidth(),mTheEngine.getCamera().getHeight());
		//attache the pieces to the container
		theElement.addToContainer(newContainer);
		theNeighbor.addToContainer(newContainer);
		theElement.resetBorder(false);
		theNeighbor.resetBorder(false);
		mPiecesList.remove(theElement);
		mPiecesList.remove(theNeighbor);
		
		//since pieces are merged ensure that noone is selected;
		unSelectPiece();
	}
	public void unSelectPiece(){
		if(nSelectedPiece != SELECTED_NONE){
			//chack that current index is still in the limit of the array and if yes set Z order to default
			if(nSelectedPiece <  this.mPiecesList.size())
				this.mPiecesList.get(nSelectedPiece).setZIndex(DEFAULT_ZINDEX+nSelectedPiece);
			nSelectedPiece = SELECTED_NONE;
			this.sortChildren(true);
		}
	}
	public void unSelectContainer(){
		if(nSelectedContainer != SELECTED_NONE){
			//chack that current index is still in the limit of the array and if yes set Z order to default
			if(nSelectedContainer <  this.mContainersList.size())
				this.mContainersList.get(nSelectedContainer).setZIndex(DEFAULT_ZINDEX+nSelectedContainer);
			nSelectedContainer = SELECTED_NONE;
			this.sortChildren(true);
		}
	}
	
	public void destroyContainer(PuzzleElementContainer theContainer){
		detachChild(theContainer);
		mContainersList.remove(theContainer);
		//TO DO ensure current container is not selected. Current version not optimal since de-select current selected container which is not always the destroyed containers
		unSelectContainer();
	}
	public void launchFireworks(float parameter[]){
		if( this.hasFireworks){
			((BaseParticleEmitter)(theFireworks.getParticleEmitter())).setCenter(parameter[0],parameter[1]);
			theFireworks.setParticlesSpawnEnabled(true);
			/*pHandeler.postDelayed(new Runnable(){
				public void run(){
					theFireworks.setParticlesSpawnEnabled(false);
				}},this.nFireworkDuration);*/
		}
	}
	
	public void playSoundTouch(){
		//if released try to re-load the sound
		if(sndTouch != null && sndTouch.isReleased()) sndTouch	= ResourcesManager.getInstance().getSound("puzzlepieces_touch").getTheSound();;
		//if the sound is still null exit
		if(sndTouch == null) return;
		sndTouch.play();
	}
	public void playSoundEndLeveSuccess(){
		//if released try to re-load the sound
		if(sndEndLeveSuccess != null && sndEndLeveSuccess.isReleased()) sndEndLeveSuccess	= ResourcesManager.getInstance().getSound("level_win").getTheSound();;
		//if the sound is still null exit
		if(sndEndLeveSuccess == null) return;
		sndEndLeveSuccess.play();
	}
	public void playSoundEndLeveFail(){
		//if released try to re-load the sound
		if(sndEndLeveFail != null && sndEndLeveFail.isReleased()) sndEndLeveFail	= ResourcesManager.getInstance().getSound("puzzlepieces_touch").getTheSound();;		
		//if the sound is still null exit
		if(sndEndLeveFail == null)return;
		sndEndLeveFail.play();
	}
	// ------------------------------------------------------------------------------------
	// Override members
	// ------------------------------------------------------------------------------------
	@Override
 	public boolean onAreaTouched(TouchEvent pSceneTouchEvent,float pTouchAreaLocalX, float pTouchAreaLocalY) {
		float[] sceneCoord = new float[2];
		sceneCoord = this.convertLocalToSceneCoordinates(pTouchAreaLocalX, pTouchAreaLocalY);
		//dispatch touch to pieces
		for(int i=0; i<mPiecesList.size(); i++){
			PuzzleElement pElement = mPiecesList.get(i);
			if(pElement.contains(sceneCoord[0],sceneCoord[1])){
				//TO DO change the ZINDEX to selected and deselect previous pieces if one TO DO
				if (nSelectedPiece != i){ 
					pElement.setZIndex(SELECTED_ZINDEX+nSelectedPiece);
					unSelectPiece();
					nSelectedPiece = i;
					this.sortChildren(true);
				}
				return mPiecesList.get(i).onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
			}
		}
		//dispatch touch to container
		for(int i=0; i<this.mContainersList.size(); i++){
			PuzzleElementContainer pElement = mContainersList.get(i);
			if(mContainersList.get(i).contains(sceneCoord[0],sceneCoord[1])){
				if (nSelectedContainer != i){ 
					pElement.setZIndex(SELECTED_ZINDEX+nSelectedContainer);
					unSelectContainer();
					nSelectedContainer = i;
					this.sortChildren(true);
				}
				return mContainersList.get(i).onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
			}
		}
		return false;
	}

	public void deleteFromList(PuzzleElement thePuzzleElement) {
		//flag current selected piece as not selected before to delete it
		unSelectPiece();
		
		this.mPiecesList.remove(thePuzzleElement);
		//if there are no pieces anymore so the puzzle is finished
		if(mPiecesList.size()==0){
			//play effecct succcees
			playSoundEndLeveSuccess();
			
			//Fire Result to scene
			long endTimeIn_ms = System.currentTimeMillis();
			long executedTimeIn_ms = endTimeIn_ms - fStartTimeIn_ms;
			
			
			long seconds 	= (executedTimeIn_ms / 1000) % 60;
		    long minutes 	= (executedTimeIn_ms / 60000) % 60;
		    long hours 		= executedTimeIn_ms / 3600000;
		    
		    StringBuilder b = new StringBuilder();
		    b.append(hours == 0 ? "00" : hours < 10 ? String.valueOf("0" + hours) : String.valueOf(hours));
		    b.append(":");
		    b.append(minutes == 0 ? "00" : minutes < 10 ? String.valueOf("0" + minutes) : String.valueOf(minutes));
		    b.append(":");
		    b.append(seconds == 0 ? "00" : seconds < 10 ? String.valueOf("0" + seconds) : String.valueOf(seconds));
		    
			// Score TODO configurable
			if(executedTimeIn_ms > 120000 )	//180000 ms = 3 mintes
				mActSceneListener.onResult(0, 50,  b.toString());
			else
				if(executedTimeIn_ms > 60000 )	//140000 ms = 2.x mintes
					mActSceneListener.onResult(1, 100,  b.toString());
				else
					if(executedTimeIn_ms > 30000 )	//140000 ms = 2.x mintes
						mActSceneListener.onResult(2, 200,  b.toString());
					else
						mActSceneListener.onResult(3, 400,  b.toString());
		}
	}
	public boolean isPieceListEmpty() {
		return this.mPiecesList.isEmpty();
	}

	// ------------------------------------------------------------------------------------
	// Interfaces
	// ------------------------------------------------------------------------------------
	// implements IComponentLifeCycle
	@Override
	public void start() {
		createPuzzle();
	}
	@Override
	public void end() {
		// TODO Auto-generated method stub
		mStatus=STATUS_END;
	}
	@Override
	public void pause() {
		// TODO Auto-generated method stub
		mStatus=STATUS_PAUSED;
	}
	@Override
	public void reset() {
		if(mStatus==STATUS_START){
			createPuzzle();
			return;
		}
		
		//change parent to all pieces to be attached to the Puzzle Sprite
		mPiecesList.clear();							//clear Piece list			
		
		for (int i=0; i < nbPieces; i++){
				mPieces[i].detachSelf();					//detach from current parent
				mPieces[i].resetBorder(hasActiveBorder); //reset the border
				this.attachChild(mPieces[i]);			//attach to PüzzleSprite
				mPiecesList.add(mPieces[i]);				//add to mPiecesList;
				mPieces[i].isMemeberOfContainer=false;	//clear container
				mPieces[i].mContainer=null;				//clear container
		}
		//Setup startingPosition of all pieces
		setUpStartingPosition();
		//setup neighbor
		setUpNeighbor();
		//clear containers list and delete container
		for (PuzzleElementContainer theContainer:mContainersList){
			this.detachChild(theContainer);
		}
		mContainersList.clear();
		//ensure no containers are selcted
		unSelectContainer();
		
		//Puzzle has been resettled so reset the time-stamp
		fStartTimeIn_ms = System.currentTimeMillis();
		return;
	}
	public boolean hasActiveZone() {
		return hasActiveZone;
	}
	public void setHasActiveZone(boolean hasActiveZone) {
		this.hasActiveZone = hasActiveZone;
	}

	// **************************************************************************************
	// implements IComponent
	// **************************************************************************************
	
	@Override
	public int getID() {
		// TODO Auto-generated method stub
		return this.nID;
	}
	@Override
	public void setID(int ID) {
		this.nID = ID;
	}
	@Override
	public IEntity getTheComponentParent() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void setTheComponentParent(IEntity parent) {
		// TODO Auto-generated method stub
	}
	@Override
	public void configure(BasicDescriptor pDsc) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void setActionSceneListner(IActionSceneListener scenelistener) {
		mActSceneListener = scenelistener;// TODO Auto-generated method stub	
	}
	@Override
	public String getPersistenceURL() {
		String sPersistenceURL = null;

		IEntity pFather = this.getParent();
		if(pFather instanceof IManageableScene){
			sPersistenceURL = new String(((IManageableScene)pFather).getSceneName());
		}
		else{
			if(pFather instanceof IComponent){
				sPersistenceURL = new String(((IComponent)pFather).getPersistenceURL());
			}
			else
				throw new NullPointerException("Not Correct Hierarchy compnent is not attached to another component or a scene");
		}
		sPersistenceURL = sPersistenceURL.concat(new String("/" + this.getID()));
		return sPersistenceURL;
	}
	/* **************************************************************************************
	// Implement Interface IComponentEventHandler
	// 	public void setUpEventsHandler(ComponentEventHandlerDescriptor entry);
	// 	public void handleEvent(IEntity pItem, TouchEvent pSceneTouchEvent, TouchEvent lastTouchEvent);
	// 	public IComponentEventHandler cloneEvent(ComponentEventHandlerDescriptor entry);
	// 	public int getID(); => Implemented by IComponent Interface
	// 	public void onFireAction(ActionType type, IEntity pItem);
	**************************************************************************************** */
	@Override
	public void setUpEventsHandler(ComponentEventHandlerDescriptor entry) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void handleEvent(IEntity pItem, TouchEvent pSceneTouchEvent,
			TouchEvent lastTouchEvent) {
		// TODO Auto-generated method stub
	}
	@Override
	public IComponentEventHandler cloneEvent(
			ComponentEventHandlerDescriptor entry) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void onFireAction(ActionType type, IEntity pItem) {
		// TODO Auto-generated method stub
	}

	
	// ===========================================================		
	// ====== IActionOnSceneListener ==== 	
	@Override
	public void onStick(IAreaShape currentShapeToStick,
			SceneActions stickActionDescription) {
		mActSceneListener.onStick(currentShapeToStick, stickActionDescription);
	}
	@Override
	public void onFlipCard(int CardID, CardSide currentSide) {
		mActSceneListener.onFlipCard(CardID,currentSide);	
	}	
	@Override
	public void lockTouch() {
		mActSceneListener.lockTouch();
	}
	@Override
	public void unLockTouch() {
		mActSceneListener.unLockTouch();
	}
	public void setIActionOnSceneListener(IActionSceneListener pListener){
		mActSceneListener = pListener;
	}
	@Override
	public void onResult(int result) {
		// TODO Auto-generated method stub
	}
	@Override
	public void setOperationsHandler(IOperationHandler messageHandler) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onResult(int i, int j, String string) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void handleEvent(IEntity pItem, Events theEvent) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public boolean checkLicence(String sLicence) {
		// TODO Auto-generated method stub
		return false;
	}
	// ******************************************************************
	// Implement Interface IPersist
	// ******************************************************************
	@Override
	public void doLoad() {	
		if(pSPM == null)
			throw new NullPointerException("In doSave the Shared Preferences Manager is null");
		
		SharedPreferences sp = pSPM.getSharedPreferences(SharedPreferenceManager.STDPreferences.GLOBAL_VARIABLES.name());
		this.nGameLevel=sp.getInt(persistentValue, 0);
	}
	@Override
	public void doSave() {
		
	}		
	@Override
	public void doLoad(SharedPreferenceManager sp) {
		pSPM = sp;
		doLoad();
	}

	@Override
	public void doSave(SharedPreferenceManager sp) {
		pSPM = sp;
		doSave();
	}
	@Override
	public boolean isPersitent(){
		return true;
	}
	@Override
	public void setSharedPreferenceManager(SharedPreferenceManager sp) {
		this.pSPM = sp;
	}

}
