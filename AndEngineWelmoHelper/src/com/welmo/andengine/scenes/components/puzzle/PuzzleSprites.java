package com.welmo.andengine.scenes.components.puzzle;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.andengine.engine.Engine;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.TiledSprite;
import org.andengine.entity.sprite.vbo.HighPerformanceTiledSpriteVertexBufferObject;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.DrawType;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import com.welmo.andengine.managers.ResourcesManager;
import com.welmo.andengine.scenes.components.IComponentLifeCycle;
import com.welmo.andengine.scenes.components.IComponentLifeCycleListener;
import com.welmo.andengine.scenes.descriptors.components.PuzzleObjectDescriptor;

/**
 * @author SESA10148
 * This class implement a rectangle containing puzzle elements. Each puzzle element is constitute by a squared sprite
 * 
 *
 */
public class PuzzleSprites extends Rectangle implements IComponentLifeCycle{
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
	private 	String							mHelperImage		= "";			//if <> "" the puzzle zone have as background the final figures in color on withe background with low alpah
	private 	float							mHelperImageAlpha	= 0.4f;		    //if true the puzzle zone have as background the final figures in color on withe background with low alpah
	
	
	protected	PuzzleElement[] 				pieces				= null;
	protected	List<PuzzleElementContainer> 	mContainersList 	= null;
	protected	List<PuzzleElement> 			mPiecesList 		= null;
	
	protected	Engine							mTheEngine 			= null;
	protected   PuzzleObjectDescriptor			mDescriptor			= null;
	
	//listeners
	protected 	IComponentLifeCycleListener		mLifeCycleListener	= null;
	protected int								mStatus				= STATUS_START;
	// --------------------------------------------------------------------
	// Constructors
	// --------------------------------------------------------------------

	public PuzzleSprites(PuzzleObjectDescriptor pDescriptor , Engine pEngine) {
		super(pDescriptor.getIPosition().getX(), pDescriptor.getIPosition().getY(), 
				pDescriptor.getIDimension().getWidth(), pDescriptor.getIDimension().getHeight(), pEngine.getVertexBufferObjectManager());
					
		//read Parameter
		mDescriptor				= pDescriptor;
		mTheEngine				= pEngine;
		
		//read puzzle geometry
		mPuzzle[PX0]			= mDescriptor.getIPosition().getX();
		mPuzzle[PY0]			= mDescriptor.getIPosition().getY();
		mPuzzle[WIDTH]			= mDescriptor.getIDimension().getWidth();
		mPuzzle[HEIGHT]			= mDescriptor.getIDimension().getHeight();
		
		//read piece box geometry
		mPiecesBox[PX0]			= mDescriptor.getPieceBoxGeometry()[PX0];
		mPiecesBox[PY0]			= mDescriptor.getPieceBoxGeometry()[PY0];
		mPiecesBox[WIDTH]		= mDescriptor.getPieceBoxGeometry()[WIDTH];
		mPiecesBox[HEIGHT]		= mDescriptor.getPieceBoxGeometry()[HEIGHT];
		
		//read puzzle zone geometry
		mPuzzleZone[PX0]		= mDescriptor.getPuzzleZoneGeometry()[PX0];
		mPuzzleZone[PY0]		= mDescriptor.getPuzzleZoneGeometry()[PY0];
		mPuzzleZone[WIDTH]		= mDescriptor.getPuzzleZoneGeometry()[WIDTH];
		mPuzzleZone[HEIGHT]		= mDescriptor.getPuzzleZoneGeometry()[HEIGHT];
		
		hasActiveBorder			= mDescriptor.hasActiveBorder();
		hasActiveZone			= mDescriptor.hasActiveZone();
		
		mHelperImage			= mDescriptor.getHelperImage();
		
		mMaxPositionX 			= mPuzzle[WIDTH];
		mMaxPositionY 			= mPuzzle[HEIGHT];
		
		//get geometry 
		nbRows 					= mDescriptor.getNbRows();
		nbCols 					= mDescriptor.getNbRows();
		nbPieces 				= nbRows*nbCols;
				
		//Init Array
		mContainersList 		= new ArrayList<PuzzleElementContainer>();
		mPiecesList 			= new ArrayList<PuzzleElement>();
		
		mStatus					= STATUS_START;
	}

	// --------------------------------------------------------------------
	// members' getters & setters
	// --------------------------------------------------------------------
	public int   getNbRows() {
		return nbRows;
	}
	public void  setNbRows(int nbRows) {
		this.nbRows = nbRows;
	}
	public int   getNbCols() {
		return nbCols;
	}
	public void  setNbCols(int nbCols) {
		this.nbCols = nbCols;
	}
	public int   getNbPieces() {
		return nbPieces;
	}
	public void  setNbPieces(int nbPieces) {
		this.nbPieces = nbPieces;
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
		return pieces;
	}
	public void setPieces(PuzzleElement[] pieces) {
		this.pieces = pieces;
	}
	public void setLifeCycleListener(IComponentLifeCycleListener mLifeCycleLeastener) {
		this.mLifeCycleListener = mLifeCycleLeastener;
	}
	// ------------------------------------------------------------------------------------
	// public memebers
	// ------------------------------------------------------------------------------------
	public void createPuzzle(){
		
		ResourcesManager pRM = ResourcesManager.getInstance();
		
		//Set additional parameters
		setZIndex(mDescriptor.getIPosition().getZorder());
		setAlpha(0);

		//get the texture
		ITiledTextureRegion theTiledTexture = pRM.getTiledTextureRegion(mDescriptor.getTextureName());

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
		pieces = new PuzzleElement[nbPieces];
		
		float[] pXY = new float[2];
		
		//create the pieces		
		for (int i=0; i < nbPieces; i++){
				pieces[i] = new PuzzleElement(mPieceWidth, mPieceHeight, hasActiveBorder, this, theTiledTexture, mTheEngine.getVertexBufferObjectManager());
				//Attach the puzzle element to the entity
				pieces[i].setCurrentTileIndex(i);
				pieces[i].setID(i);
				pieces[i].setXYLimit(0,0,mMaxPositionX,mMaxPositionY);
				if(hasActiveZone){
					pXY[X] = mPuzzleZone[X] + mPieceWidth * (int)(i% nbCols);
					pXY[Y] = mPuzzleZone[Y] + mPieceHeight * (int)(i/nbCols);
					pieces[i].setActiveZoneXY(pXY);
					pieces[i].setZIndex(DEFAULT_ZINDEX);
				}
				mPiecesList.add(pieces[i]);
				attachChild(pieces[i]);
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
			ITextureRegion theImage=pRM.getTextureRegion(mDescriptor.getHelperImage());
			
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
					pieces[pieceNb].setNeighbors(PuzzleElement.NEIGHTBOR_TOP, pieces[pieceNb - nbCols]);
				//set BOTTOM
				if(i<(nbRows-1))
					pieces[pieceNb].setNeighbors(PuzzleElement.NEIGHTBOR_BOTTOM, pieces[pieceNb + nbCols]);
				//set LEFT
				if(j!= 0)
					pieces[pieceNb].setNeighbors(PuzzleElement.NEIGHTBOR_LEFT,pieces[pieceNb-1]);
				//set RIGHT
				if(j<(nbCols-1))
					pieces[pieceNb].setNeighbors(PuzzleElement.NEIGHTBOR_RIGHT,pieces[pieceNb+1]);
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
				pieces[i].setPosition(mPiecesBox[PX0] + (deltaXY[PX0] * rnd1), 
									  mPiecesBox[PY0] + (deltaXY[PY0] * rnd2));
			}
		}
		else{
			float[] pXY ={0,0};
			int pieceNb=0;
			for (int i=0; i < nbRows; i++){
				for (int j=0; j < nbCols; j++){
					pieces[pieceNb].setPosition(pXY[PX0], pXY[PY0]);
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
				pieces[i].detachSelf();					//detach from current parent
				pieces[i].resetBorder(hasActiveBorder); //reset the border
				this.attachChild(pieces[i]);			//attach to P�zzleSprite
				mPiecesList.add(pieces[i]);				//add to mPiecesList;
				pieces[i].isMemeberOfContainer=false;	//clear container
				pieces[i].mContainer=null;				//clear container
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
}