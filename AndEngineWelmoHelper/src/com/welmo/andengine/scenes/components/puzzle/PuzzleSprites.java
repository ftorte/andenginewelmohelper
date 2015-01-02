package com.welmo.andengine.scenes.components.puzzle;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.andengine.engine.Engine;
import org.andengine.entity.IEntity;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.shape.IAreaShape;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.TiledSprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;

import android.content.SharedPreferences;

import com.welmo.andengine.managers.ResourcesManager;
import com.welmo.andengine.managers.SharedPreferenceManager;
import com.welmo.andengine.scenes.IManageableScene;
import com.welmo.andengine.scenes.components.CardSprite.CardSide;
import com.welmo.andengine.scenes.components.interfaces.IActionSceneListener;
import com.welmo.andengine.scenes.components.interfaces.IActivitySceneListener;
import com.welmo.andengine.scenes.components.interfaces.IComponent;
import com.welmo.andengine.scenes.components.interfaces.IComponentEventHandler;
import com.welmo.andengine.scenes.components.interfaces.IComponentLifeCycle;
import com.welmo.andengine.scenes.components.interfaces.IComponentLifeCycleListener;
import com.welmo.andengine.scenes.components.interfaces.IPersistent;
import com.welmo.andengine.scenes.descriptors.BasicDescriptor;
import com.welmo.andengine.scenes.descriptors.components.PuzzleObjectDescriptor;
import com.welmo.andengine.scenes.descriptors.events.ComponentEventHandlerDescriptor;
import com.welmo.andengine.scenes.descriptors.events.SceneActions;
import com.welmo.andengine.scenes.descriptors.events.SceneActions.ActionType;
import com.welmo.andengine.scenes.operations.IOperationHandler;

/**
 * @author SESA10148
 * This class implement a rectangle containing puzzle elements. Each puzzle element is constitute by a squared sprite
 * 
 *
 */
public class PuzzleSprites extends Rectangle implements IComponent, IComponentLifeCycle, IComponentEventHandler, IActionSceneListener{
	// --------------------------------------------------------------------
	// constants
	// --------------------------------------------------------------------
	public static final int X				= 0;
	public static final int Y				= 1;
	
	public static final int PX0				= 0;
	public static final int PY0				= 1;
	public static final int WIDTH			= 2;
	public static final int HEIGHT			= 3;
	
	
	public static final int STATUS_START	= 1;
	public static final int STATUS_ONGOING	= 2;
	public static final int STATUS_END		= 3;
	public static final int STATUS_PAUSED   = 4;
	
	public static final int DEFAULT_ZINDEX 	= 100;
	public static final int STACK_ZINDEX	= 10;
	
	
	// --------------------------------------------------------------------
	protected	int 							nID					= 0;
	
	protected	int 							nbRows				= 0;
	protected	int 							nbCols				= 0;
	protected	int 							nbPieces			= 0;
	protected	float 							mPieceWidth			= 0;
	protected	float 							mPieceHeight 		= 0;
	
	protected	float 							mMaxPositionX		= 0;
	protected	float 							mMaxPositionY		= 0;
	
	protected	float[]							mPuzzle				= {0,0,0,0};
	protected	float[]							mPiecesBox			= {0,0,0,0};
	protected	float[]							mPuzzleZone			= {0,0,0,0};
	
	private 	boolean							hasActiveBorder		= false;		//if true the pieces and container have borders
	private 	boolean							hasActiveZone		= false;		//if true the puzzle zone is active and pieces are stick to the zone
	private 	boolean							hasWhiteBackground	= false;		//if true the puzzle pieces have a white background
	private 	String							mHelperImage		= "";			//if <> "" the puzzle zone have as background the final figures in color on withe background with low alpah
	private 	String 							mTiledTextureName	= "";	
	private 	String 							mTiledTextureResource = "";	
	

	private 	float							mHelperImageAlpha	= 0.4f;		    //if true the puzzle zone have as background the final figures in color on withe background with low alpah
	
	protected	int 							mZOrder				=0;
	
	protected	PuzzleElement[] 				mPieces				= null;
	protected	List<PuzzleElementContainer> 	mContainersList 	= null;
	protected	List<PuzzleElement> 			mPiecesList 		= null;
	
	protected	Engine							mTheEngine 			= null;
	
	
	//listeners
	protected 	IComponentLifeCycleListener		mLifeCycleListener	= null;
	protected 	int								mStatus				= STATUS_START;
	protected 	IActionSceneListener			mActSceneListener 	= null;
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
		mPuzzleZone[PX0]		= pDescriptor.getPuzzleZoneGeometry()[PX0];
		mPuzzleZone[PY0]		= pDescriptor.getPuzzleZoneGeometry()[PY0];
		mPuzzleZone[WIDTH]		= pDescriptor.getPuzzleZoneGeometry()[WIDTH];
		mPuzzleZone[HEIGHT]		= pDescriptor.getPuzzleZoneGeometry()[HEIGHT];
		
		hasActiveBorder			= pDescriptor.hasActiveBorder();
		hasActiveZone			= pDescriptor.hasActiveZone();
		hasWhiteBackground		= pDescriptor.hasWhiteBackground();
		
		
		mHelperImage			= new String(pDescriptor.getHelperImage());
		mTiledTextureName		= new String(pDescriptor.getTiledTextureName());
		mTiledTextureResource	= new String(pDescriptor.getTiledTextureResourceName());
		
		
		mMaxPositionX 			= mPuzzle[WIDTH];
		mMaxPositionY 			= mPuzzle[HEIGHT];
		mZOrder					= pDescriptor.getIPosition().getZorder();
		
		//get geometry 
		nbRows 					= pDescriptor.getNbRows();
		nbCols 					= pDescriptor.getNbRows();
		nbPieces 				= nbRows*nbCols;
				
		//Init Array
		mContainersList 		= new ArrayList<PuzzleElementContainer>();
		mPiecesList 			= new ArrayList<PuzzleElement>();
		
		mStatus					= STATUS_START;
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
	// ------------------------------------------------------------------------------------
	// public memebers
	// ------------------------------------------------------------------------------------
	public void clearPuzzle(){
	
		if(mStatus==STATUS_START)
			return;

		//detach all childres
		this.detachChildren();

		//change parent to all pieces to be attached to the Puzzle Sprite
		mPiecesList.clear();							//clear Piece list			
		mContainersList.clear();
	
		mStatus=STATUS_START;
		return;
	}
	public void createPuzzle(){
		
		ResourcesManager pRM = ResourcesManager.getInstance();
		
		//Set additional parameters
		setZIndex(mZOrder);
		
		setAlpha(0);

		//get the texture
		ITiledTextureRegion theTiledTexture = pRM.getDinamicTiledTextureRegion(mTiledTextureName, 
				mTiledTextureResource, nbCols, nbRows);

		//Calculate to zoom factor 
		float zoomRatio = 1;
		if(mPuzzleZone[WIDTH] != 0 && mPuzzleZone[HEIGHT] != 0 ){
			mPieceWidth = mPuzzleZone[WIDTH]/nbCols;
			mPieceHeight = mPuzzleZone[HEIGHT]/nbRows;
			zoomRatio = Math.min(mPieceWidth/theTiledTexture.getWidth(),mPieceHeight/theTiledTexture.getHeight()) ;
		}
		mPieceWidth  = theTiledTexture.getWidth()*zoomRatio;
		mPieceHeight = theTiledTexture.getHeight()*zoomRatio;
		
		//Create vectors of tiled sprite pointers
		mPieces = new PuzzleElement[nbPieces];
		
		float[] pXY = new float[2];
		
		//create the pieces		
		for (int i=0; i < nbPieces; i++){
				mPieces[i] = new PuzzleElement(mPieceWidth, mPieceHeight, hasActiveBorder, this, hasWhiteBackground, theTiledTexture, mTheEngine.getVertexBufferObjectManager());
				//Attach the puzzle element to the entity
				mPieces[i].setCurrentTileIndex(i);
				mPieces[i].setID(i);
				mPieces[i].setXYLimit(0,0,mMaxPositionX,mMaxPositionY);
				if(hasActiveZone){
					pXY[X] = mPuzzleZone[X] + mPieceWidth * (int)(i% nbCols);
					pXY[Y] = mPuzzleZone[Y] + mPieceHeight * (int)(i/nbCols);
					mPieces[i].setActiveZoneXY(pXY);
					mPieces[i].setZIndex(DEFAULT_ZINDEX);
				}
				mPiecesList.add(mPieces[i]);
				attachChild(mPieces[i]);
		}
		
		setUpStartingPosition();

		setUpNeighbor();
		
		if(mLifeCycleListener != null)
			mLifeCycleListener.onStart();
		
		setUpHelperImage();
		
		mStatus					= STATUS_ONGOING;
	}
	
	public void setUpHelperImage(){
		if(!mHelperImage.isEmpty()){
			ResourcesManager pRM = ResourcesManager.getInstance();
			// FT ITextureRegion theImage=pRM.getTextureRegion(mDescriptor.getHelperImage());
			ITextureRegion theImage=pRM.getTextureRegion(mHelperImage);
			
			Sprite helperImage = new Sprite(mPuzzleZone[PX0],mPuzzleZone[PY0],
					mPieceWidth * nbCols,mPieceHeight*nbRows,
					theImage,mTheEngine.getVertexBufferObjectManager());
			
			this.attachChild(helperImage);
			helperImage.setAlpha(mHelperImageAlpha);
			helperImage.setZIndex(STACK_ZINDEX);
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
	}
	public void destroyContainer(PuzzleElementContainer theContainer){
		detachChild(theContainer);
		mContainersList.remove(theContainer);
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
			if(mPiecesList.get(i).contains(sceneCoord[0],sceneCoord[1]))
				return mPiecesList.get(i).onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
		}
		//dispatch touch to container
		for(int i=0; i<this.mContainersList.size(); i++){
			if(mContainersList.get(i).contains(sceneCoord[0],sceneCoord[1]))
				return mContainersList.get(i).onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
		}
		return false;
	}

	public void deleteFromList(PuzzleElement thePuzzleElement) {
		this.mPiecesList.remove(thePuzzleElement);
		//if there are no pieces anumore the puzzle is finished
		if(mPiecesList.size()==0){
			//Fire Result to scene
			mActSceneListener.onResult(1);
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
}
