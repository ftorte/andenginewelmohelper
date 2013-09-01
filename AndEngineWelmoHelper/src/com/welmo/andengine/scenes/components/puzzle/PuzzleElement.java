package com.welmo.andengine.scenes.components.puzzle;

import org.andengine.entity.IEntity;
import org.andengine.entity.primitive.Line;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.TiledSprite;
import org.andengine.entity.sprite.vbo.HighPerformanceTiledSpriteVertexBufferObject;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.DrawType;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import android.util.Log;

public class PuzzleElement extends TiledSprite{
	
	// --------------------------------------------------------------------
	// constants
	// --------------------------------------------------------------------
	public static final String TAG			="PuzzleElement";
	//--------------------------------------------------------------
	public enum PuzzleElementVertes{TL,RT,BL, BT};		// N = Top, L=Left, R=Right, B=Bottom
	//public enum PuzzleElementNeighbors {NT,NB,NL,NR};  	// Nx = Neighbor T=TOP, B=Bottom, L=Left R=Right

	//--------------------------------------------------------------
	// Constants
	//--------------------------------------------------------------
	public static final int NUMB_NEIGHBORS 					=4;
	public static final int NOT_VAID_NEIGHBORS 				=1000000;
	public static final int NOT_VAID_ID 					=-1;
	public static final int NEIGHTBOR_TOP					=0;
	public static final int NEIGHTBOR_BOTTOM				=1;
	public static final int NEIGHTBOR_LEFT					=2;
	public static final int NEIGHTBOR_RIGHT					=3;
	public static final int NEIGHTBOR_NB					=4;
	public static final int DEFAUL_STICK_SENSITIVITY		=10;
	public static final int	X								=0;
	public static final int Y								=1;
	//--------------------------------------------------------------------------------
	// Members variables
	//--------------------------------------------------------------------------------
	
	protected int							mID						= NOT_VAID_ID;
	protected PuzzleElement[] 				neighbors 				= null;
	protected int							stickSensitivity		= 0;
	protected boolean						isMemeberOfContainer	= false;
	protected PuzzleElementContainer		mContainer				= null;
	protected float[]						mLastTouchPosXY			= new float[2];	
	protected boolean						isOnMove				= false;

	//limit control
	private boolean							activePositionLimit;
	private float[]							mMinPositionXY			= {0,0};
	private float[] 						mMaxPositionXY			= {0,0};
	private PuzzleSprites					mThePuzzle				= null;
	
	//implement borders
	private Line							TopBorder 				= null;
	private Line							BottomBorder 			= null;
	private Line							LeftBorder 				= null;
	private Line							RightBorder 			= null;
	private boolean							hasBorder				= false;
	protected float[]						mActiveZoneXY			= new float[2];		//will Contain the active zone XY
	
	//--------------------------------------------------------------------------------
	// Constructor
	//--------------------------------------------------------------------------------
	
	public PuzzleElement(float pWidth, float pHeight,boolean activeBrd,PuzzleSprites pThePuzzle,
			ITiledTextureRegion pTiledTextureRegion,
			VertexBufferObjectManager pVertexBufferObject) {
		
		super(0, 0, pWidth, pHeight, pTiledTextureRegion,new HighPerformanceTiledSpriteVertexBufferObject(pVertexBufferObject, 
						TiledSprite.TILEDSPRITE_SIZE * (pTiledTextureRegion).getTileCount(), 
						DrawType.STATIC,
						true, Sprite.VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT));
				
		//crete vector of neghbors
		neighbors = new PuzzleElement[NUMB_NEIGHBORS];
		for(int i=0; i< NUMB_NEIGHBORS; i++)
			neighbors[i] = null;
		stickSensitivity=DEFAUL_STICK_SENSITIVITY;
		
		//setup borders
		hasBorder=activeBrd;		
		if(hasBorder)
			createBorder();
		
		mThePuzzle = pThePuzzle;
				
	}
	public void resetBorder(boolean borderYN){
		if(hasBorder && borderYN){
			TopBorder.setPosition(0, 0, mWidth, 0);
			BottomBorder.setPosition(0, mHeight, mWidth, mHeight);
			LeftBorder.setPosition(0, 0, 0, mHeight);
			RightBorder.setPosition(mWidth, 0, mWidth, mHeight);
			return;
		}
		if(!hasBorder && borderYN){
			hasBorder=true;
			createBorder();
			return;
		}
		if(hasBorder && !borderYN){
			hasBorder=false;
			this.detachChild(TopBorder);
			this.detachChild(BottomBorder);
			this.detachChild(LeftBorder);
			this.detachChild(RightBorder);
		}
	}

	public float getDistanceFromNeighbor(int theNeighbor){
		float dinstance=NOT_VAID_NEIGHBORS;
		float[] neighborXY=new float[2];
		float[] thisObjectXY=new float[2];
		float[] tmp;
		if(neighbors[theNeighbor]!= null){
			if(neighbors[theNeighbor].isMemeberOfContainer)
				tmp = neighbors[theNeighbor].mContainer.convertLocalToSceneCoordinates(neighbors[theNeighbor].getX(), neighbors[theNeighbor].getY());
			else
				tmp = neighbors[theNeighbor].convertLocalToSceneCoordinates(0,0);
			//to avoid the problem of convert to local which return the same vector
			neighborXY[0]=tmp[0];
			neighborXY[1]=tmp[1];
			
			if(this.isMemeberOfContainer)
				thisObjectXY = this.mContainer.convertLocalToSceneCoordinates(this.getX(), this.getY());
			else
				thisObjectXY = this.convertLocalToSceneCoordinates(0,0);
			
			switch(theNeighbor){
			case NEIGHTBOR_TOP:
				dinstance=distance(neighborXY[X],thisObjectXY[X],neighborXY[Y] +neighbors[NEIGHTBOR_TOP].getHeight(),thisObjectXY[Y]);
				break;
			case NEIGHTBOR_BOTTOM:
				dinstance=distance(neighborXY[X],thisObjectXY[X],neighborXY[Y],thisObjectXY[Y]+ this.getHeight() );
				break;
			case NEIGHTBOR_LEFT:
				dinstance=distance(neighborXY[X] + neighbors[NEIGHTBOR_LEFT].getWidth(), thisObjectXY[X],neighborXY[Y],thisObjectXY[Y]);
				break;
			case NEIGHTBOR_RIGHT:
				dinstance=distance(neighborXY[X],thisObjectXY[X] + this.getWidth(),neighborXY[Y],thisObjectXY[Y]);
				break;
			}
		}
		return dinstance;
	}
	public void setXYLimit(float minX, float minY,float maxX, float maxY) {
		activePositionLimit=true;
		mMinPositionXY[X]=minX;
		mMinPositionXY[Y]=minX;
		mMaxPositionXY[X]=maxX;
		mMaxPositionXY[Y]=maxY;
	}
	public void resetMovementLimit() {
		activePositionLimit=false;
	}
	public int getNearest(){
		for(int i=0; i< NEIGHTBOR_NB; i++){
			if(getDistanceFromNeighbor(i) < stickSensitivity)
				return i;
		}
		return NOT_VAID_NEIGHBORS;
	}
	public PuzzleElement getNeighbor(int neigborType){
		return neighbors[neigborType];
	}
	private float distance(float pX1, float pX2, float pY1, float pY2){
		return (float) Math.sqrt((pX1-pX2)*(pX1-pX2) + (pY1-pY2)*(pY1-pY2));
	}
	
	public void setNeighbors(int neighbor, PuzzleElement theNeighbor) {
		this.neighbors[neighbor] = theNeighbor;
	}
	public boolean isMemeberOfContainer() {
		return isMemeberOfContainer;
	}
	public static float[] getMinXYRegionOfTowElements(PuzzleElement elementA, PuzzleElement elementB){
		float[] result=new float[2];
		result[0]=Math.min(elementA.mX, elementB.mX);
		result[1]=Math.min(elementA.mY, elementB.mY);
		return result;
	}  
	public static float[] getMaxXYRegionOfTowElements(PuzzleElement elementA, PuzzleElement elementB){
		float[] result=new float[2];
		result[0]=Math.max(elementA.mX+elementA.mWidth, elementB.mX+elementB.mWidth);
		result[1]=Math.max(elementA.mY+elementA.mHeight, elementB.mY+elementB.mHeight);
		return result;
	} 
	public void addToContainer(PuzzleElementContainer newRectangle){
		//attache the pieces to the container
		if(hasParent())
			getParent().detachChild(this);
		
		newRectangle.attachChild(this);
		isMemeberOfContainer=true;
		mContainer=newRectangle;
		mThePuzzle.deleteFromList(this);
	}
	public void changeContainer(PuzzleElementContainer newRectangle){
		//attache the pieces to the container
		assert(this.mContainer!=null && hasParent());
		
		float[] positionXY = this.convertLocalToSceneCoordinates(0,0);
		
		getParent().detachChild(this);
		
		this.setPosition(positionXY[X], positionXY[Y]);
		
		newRectangle.attachChild(this);
		isMemeberOfContainer=true;
		mContainer=newRectangle;
	}
	public void detachAllNeigborInContainer(PuzzleElementContainer container){
		for (int i=0; i < this.NUMB_NEIGHBORS; i++){
			if(neighbors[i] != null && neighbors[i].mContainer == container)
					detachNeighbor(i);
		}
	}
	private void detachNeighbor(int neigborType){
		//get pointer to the neighbor and detach from it
		PuzzleElement theNeighbor=neighbors[neigborType];
		neighbors[neigborType]=null;
		
		//detach the element to the neigbor pointer
		switch(neigborType){
		case PuzzleElement.NEIGHTBOR_TOP:
			theNeighbor.neighbors[PuzzleElement.NEIGHTBOR_BOTTOM]=null;
			break;
		case PuzzleElement.NEIGHTBOR_BOTTOM:
			theNeighbor.neighbors[PuzzleElement.NEIGHTBOR_TOP]=null;
			break;
		case PuzzleElement.NEIGHTBOR_LEFT:
			theNeighbor.neighbors[PuzzleElement.NEIGHTBOR_RIGHT]=null;
			break;
		case PuzzleElement.NEIGHTBOR_RIGHT:
			theNeighbor.neighbors[PuzzleElement.NEIGHTBOR_LEFT]=null;
			break;
		}
	}
	@Override
	public void setPosition(IEntity pOtherEntity) {
		float pX = pOtherEntity.getX();
		float pY = pOtherEntity.getY();
		this.setPosition(pX, pY);
	}
	@Override
	public void setPosition(float pX, float pY) {
		Log.i(TAG,"setPosition Start: X=" + pX + " Y="+ pY);
		if(activePositionLimit){
			if(pX< mMinPositionXY[X]){
				pX =0;
			}
			else{
				if((pX + this.getWidth())> mMaxPositionXY[X]){
					pX =mMaxPositionXY[X]- this.getWidth();
				}
			}
			if(pY< mMinPositionXY[Y]){
				pY =mMinPositionXY[Y];
			}
			else{
				if((pY +this.getHeight())> mMaxPositionXY[Y]){
					pY =mMaxPositionXY[Y]-this.getHeight();
				}
			}
		}
		Log.i(TAG,"SetPosition End: X=" + pX + " Y="+ pY);
		super.setPosition(pX, pY);
	}
	// ===========================================================
	// Private member functions
	// ===========================================================	
	private void createBorder(){
		TopBorder = new Line(0, 0, mWidth, 0, this.getVertexBufferObjectManager());
		TopBorder.setColor(0.94f, 0.94f, 0.94f);
		TopBorder.setLineWidth(2);
		attachChild(TopBorder);

		BottomBorder = new Line(0, mHeight, mWidth, mHeight, this.getVertexBufferObjectManager());
		BottomBorder.setLineWidth(2);
		BottomBorder.setColor(0.94f, 0.94f, 0.94f);
		attachChild(BottomBorder);

		LeftBorder = new Line(0, 0, 0, mHeight, this.getVertexBufferObjectManager());
		LeftBorder.setLineWidth(3);
		LeftBorder.setColor(0.94f, 0.94f, 0.94f);
		attachChild(LeftBorder);

		RightBorder = new Line(mWidth, 0, mWidth, mHeight, this.getVertexBufferObjectManager());
		RightBorder.setLineWidth(3);
		RightBorder.setColor(0.94f, 0.94f, 0.94f);
		attachChild(RightBorder);
	}
	
	@Override
	public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
		switch (pSceneTouchEvent.getAction()){
		case TouchEvent.ACTION_DOWN:
			mLastTouchPosXY[X]=pTouchAreaLocalX;
			mLastTouchPosXY[Y]=pTouchAreaLocalY;
			isOnMove=true;
			break;
		case TouchEvent.ACTION_MOVE:
			float[] deltaXY=new float[2];
			deltaXY[X]=pTouchAreaLocalX - mLastTouchPosXY[X];
			deltaXY[Y]=pTouchAreaLocalY - mLastTouchPosXY[Y];
			mLastTouchPosXY[X]=pTouchAreaLocalX;
			mLastTouchPosXY[Y]=pTouchAreaLocalY;
			if(!isOnMove){
				isOnMove=true;
				return true;
			}
			//move to new position
			this.setPosition(this.getX() + deltaXY[X], this.getY()+ deltaXY[Y]);
			//check if is near to another puzzle element and stick to it
			if (mThePuzzle.hasActiveZone())
				stickToActiveZone();
			else
				stickToNeighbors();     
			break;
			
		case TouchEvent.ACTION_OUTSIDE:
		case TouchEvent.ACTION_UP:
		case TouchEvent.ACTION_CANCEL:
			
			this.isOnMove=false;
			break;
		default:
			break;
		}
		return true;
	}
	
	public void stickToActiveZone(){
			if(distance(this.getX(),mActiveZoneXY[X], getY(),mActiveZoneXY[Y]) < stickSensitivity){
				setPosition(mActiveZoneXY[X], mActiveZoneXY[Y]);
				mThePuzzle.deleteFromList(this);
				resetBorder(false);
				mThePuzzle.setZIndex(PuzzleSprites.STACK_ZINDEX+10);
			}
	}
	
	public void stickToNeighbors(){
		//see if is near to a neighbor
		int nearestSprite = this.getNearest();

		if(nearestSprite != this.NOT_VAID_NEIGHBORS){
			attachePieceToNearest(nearestSprite);
			if(!neighbors[nearestSprite].isMemeberOfContainer){
				//create new container
				mThePuzzle.createNewContainer(nearestSprite, this, this.neighbors[nearestSprite]);
				detachNeighbors(nearestSprite, this, this.neighbors[nearestSprite]);
				return;
			}
			if(neighbors[nearestSprite].isMemeberOfContainer){
				//merge neighbor container
				this.addToContainer(neighbors[nearestSprite].mContainer);
				this.resetBorder(false);
				this.detachAllNeigborInContainer(neighbors[nearestSprite].mContainer);
				return;
			}
		}
	}
	public void attachePieceToNearest(int nNearest){
		
		//get X/Y position in Scene ref system of Top/Left corner of neighbor to attache to
		float[] newXY = neighbors[nNearest].convertLocalToSceneCoordinates(0,0);
		
		float newX=newXY[X];
		float newY=newXY[Y];
		
		//Calculate the new position of the piece depending on the type of neighbor to attach to
		switch(nNearest){
		case PuzzleElement.NEIGHTBOR_TOP:
			newY=newY + neighbors[nNearest].getHeight();
			break;
		case PuzzleElement.NEIGHTBOR_BOTTOM:
			newY=newY - neighbors[nNearest].getHeight();;
			break;
		case PuzzleElement.NEIGHTBOR_LEFT:
			newX=newX + neighbors[nNearest].getWidth();
			break;
		case PuzzleElement.NEIGHTBOR_RIGHT:
			newX=newX - neighbors[nNearest].getWidth();
			break;
		}
		//get the coordinate to attach to in element local(local is the parent of element) reference system
		newXY=getParent().convertSceneToLocalCoordinates(newX,newY);
		newX=newXY[X];
		newY=newXY[Y];
		
		//if(isMemeberOfContainer){
			//if the element to attach belong to a container; calculate the delta move and move the container not the piece only
			//float currXY[] = theElement.mContainer.convertSceneToLocalCoordinates(theElement.getX(),theElement.getY());//get current position
		//	mContainer.setPosition(mContainer.getX() + newX - getX(), mContainer.getY()+ newY - getY());
		//}
		//else{
			//if the element to attach doesn't belong to a container move it to the expected position
			setPosition(newX, newY);
		//}
	}
	private void detachNeighbors(int nearestType, PuzzleElement theElement, PuzzleElement theNeighbor){
		theElement.neighbors[nearestType]=null;
		switch(nearestType){
		case PuzzleElement.NEIGHTBOR_TOP:
			theNeighbor.neighbors[PuzzleElement.NEIGHTBOR_BOTTOM]=null;
			break;
		case PuzzleElement.NEIGHTBOR_BOTTOM:
			theNeighbor.neighbors[PuzzleElement.NEIGHTBOR_TOP]=null;
			break;
		case PuzzleElement.NEIGHTBOR_LEFT:
			theNeighbor.neighbors[PuzzleElement.NEIGHTBOR_RIGHT]=null;
			break;
		case PuzzleElement.NEIGHTBOR_RIGHT:
			theNeighbor.neighbors[PuzzleElement.NEIGHTBOR_LEFT]=null;
			break;
		}
	}
	public int getID() {
		return mID;
	}
	public void setID(int pID) {
		mID = pID;
	}
	public float[] getActiveZoneXY() {
		return mActiveZoneXY;
	}
	public void setActiveZoneXY(float[] mActiveZoneXY) {
		this.mActiveZoneXY[X] = mActiveZoneXY[X];
		this.mActiveZoneXY[Y] = mActiveZoneXY[Y];
	}
}
