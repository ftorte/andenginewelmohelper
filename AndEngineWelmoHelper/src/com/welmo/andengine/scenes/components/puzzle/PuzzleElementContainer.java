package com.welmo.andengine.scenes.components.puzzle;

import java.util.ArrayList;
import java.util.List;

import org.andengine.entity.IEntity;
import org.andengine.entity.primitive.Line;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.primitive.vbo.IRectangleVertexBufferObject;
import org.andengine.entity.shape.IAreaShape;
import org.andengine.input.touch.TouchEvent;

import android.util.Log;

public class PuzzleElementContainer extends Rectangle{

	// ===================================================================================
	// Constants
	// ===================================================================================
	final static int 									NB_OF_BORDER 	= 4;
	final static int									X				= 0;
	final static int									Y				= 1;
	
	// ===================================================================================
	// Member variables
	// ===================================================================================
	//Log & Debug
	private static final String 						TAG 			= "PuzzleElementContainer";
	private Line										TopBorder 		= null;
	private Line										BottomBorder 	= null;
	private Line										LeftBorder 		= null;
	private Line										RightBorder 	= null;
	private boolean										hasBorder		= false;
	//private int											nbOfComponents	= 0;
	
	private boolean										activePositionLimit;
	private float[]										mMinPositionXY	= {0,0};
	private float[] 									mMaxPositionXY	= {0,0};
	private float[]										mLastTouchPosXY	= {0,0};
	private boolean 									isOnMove		= false;
	protected List<PuzzleElement> 						mPuzzleElements 	= null;
	
	//protected DefaultIClickableImplementation 			mIClicakableImpmementation 	= null;
	private class NeighborDescriptor{
		public PuzzleElement thePiece;
		public PuzzleElement theNeighbor;
		public int theNeighborType = PuzzleElement.NOT_VAID_NEIGHBORS;
		
	}
	// ===================================================================================
	// Constructors
	// ===================================================================================

	public PuzzleElementContainer(float pX, float pY, float pWidth, float pHeight,
			boolean activeBrd, IRectangleVertexBufferObject mRectangleVertexBufferObject, IEntity parent) {
		super(pX, pY, pWidth, pHeight, mRectangleVertexBufferObject);
		init();
		//set rectangle as transparent
		this.setAlpha(0);
		//setup borders
		hasBorder=activeBrd;		
		if(hasBorder)
			createBorder();
		mPuzzleElements = new ArrayList<PuzzleElement>();
	}
	// ===================================================================================
	// protected for init and configuration
	// ===================================================================================
	protected void init(){
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
	
	// ===================================================================================
	// protected for init and configuration
	// ===================================================================================
	@Override
	public void attachChild(IEntity pEntity) throws IllegalStateException {
		if(pEntity instanceof PuzzleElement ){
			attachPuzzleElement((PuzzleElement)pEntity);
		}
		super.attachChild(pEntity);
	}
	@Override
	public boolean detachChild(IEntity pEntity) throws IllegalStateException {
		if(pEntity instanceof PuzzleElement ){
			detachPuzzleElement((PuzzleElement)pEntity);
		}
		return super.detachChild(pEntity);
	}
	public void attachPuzzleElement(PuzzleElement pNewPuzzleElement) throws IllegalStateException {

		
		if(mPuzzleElements.isEmpty()){
			this.setPosition(pNewPuzzleElement.getX(),pNewPuzzleElement.getY());
			this.setSize(pNewPuzzleElement.getWidth(),pNewPuzzleElement.getHeight());
			pNewPuzzleElement.setPosition(0, 0);
			mPuzzleElements.add(pNewPuzzleElement);
		}
		else{
			//Calculate coordinate current rectangle in parent coordinate system
			float[] currContainerMinXY = this.convertLocalToSceneCoordinates(0, 0).clone(); 
			
			float curRecXmin = currContainerMinXY[X];
			float curRecXmax = curRecXmin + this.getWidth();
			float curRecYmin = currContainerMinXY[Y];
			float curRecYmax = curRecYmin + this.getHeight();

			//Calculate coordinate new object in current container parent's coord system
			float[] pOriginalXY = pNewPuzzleElement.convertLocalToSceneCoordinates(0, 0).clone();
			float[] pConvertedXY = this.convertSceneToLocalCoordinates(pOriginalXY).clone();

			float newObjXmin = pOriginalXY[X];
			float newObjXmax = newObjXmin + pNewPuzzleElement.getWidth();
			float newObjYmin = pOriginalXY[Y];
			float newObjYmax = newObjYmin + pNewPuzzleElement.getHeight();

			//1st expand container zone
			this.setWidth(Math.max(curRecXmax, newObjXmax)-Math.min(curRecXmin,newObjXmin));
			this.setHeight(Math.max(curRecYmax, newObjYmax)-Math.min(curRecYmin,newObjYmin));

			//2nd calculate compound delta PX and delta PY
			float deltaPX = Math.min(curRecXmin,newObjXmin)-curRecXmin;
			float deltaPY = Math.min(curRecYmin,newObjYmin)-curRecYmin;

			//3th move all current child by -delatX/Y
			for (IEntity tmpEntity:mChildren)
				tmpEntity.setPosition(tmpEntity.getX()-deltaPX, tmpEntity.getY()-deltaPY);

			//4th move container by delatX/Y
			this.setPosition(this.getX()+deltaPX, this.getY()+deltaPY);

			//5th add new entity to relative position in the Compound space
			pConvertedXY = this.convertSceneToLocalCoordinates(pOriginalXY).clone();
			pNewPuzzleElement.setPosition(pConvertedXY[X], pConvertedXY[Y]);
			mPuzzleElements.add(pNewPuzzleElement);
		}
		resetBorder(true);
		return;
	}
	public boolean detachPuzzleElement(PuzzleElement pElementToDetach){

		if(mPuzzleElements.isEmpty())
			return false;

		mPuzzleElements.remove(pElementToDetach);
		
		return true;
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
	public int getNbOfPuzzleElements() {
		return mPuzzleElements.size();
	}
	public void moveDeltaXY(float deltaX, float deltaY){
		Log.i(TAG,"Move Delat XY:"+ " DX="+deltaX + " DY="+deltaX);
		this.setPosition(this.getX()+ deltaX, this.getY()+ deltaY);
	}
	@Override
	public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
		switch (pSceneTouchEvent.getAction()){
		case TouchEvent.ACTION_DOWN:
			this.mLastTouchPosXY[X]=pTouchAreaLocalX;
			this.mLastTouchPosXY[Y]=pTouchAreaLocalY;
			this.isOnMove=true;
			return true;
		case TouchEvent.ACTION_MOVE:
			float[] deltaXY=new float[2];
			deltaXY[X]=pTouchAreaLocalX - mLastTouchPosXY[X];
			deltaXY[Y]=pTouchAreaLocalY - mLastTouchPosXY[Y];
			this.mLastTouchPosXY[X]=pTouchAreaLocalX;
			this.mLastTouchPosXY[Y]=pTouchAreaLocalY;
			if(!isOnMove){
				isOnMove=true;
				return true;
			}
			moveDeltaXY(deltaXY[X], deltaXY[Y]);
			
			NeighborDescriptor nearPuzzleElement = this.getNearestPuzzleElement();

			if(nearPuzzleElement != null){
				attachePieceToNearest(nearPuzzleElement);
				if(nearPuzzleElement.theNeighbor.isMemeberOfContainer)
					mergeWithContainer(nearPuzzleElement.theNeighbor.mContainer);
				else
					addPuzzleElement(nearPuzzleElement.theNeighbor);
			}
			break;
		case TouchEvent.ACTION_OUTSIDE:
		case TouchEvent.ACTION_UP:
		case TouchEvent.ACTION_CANCEL:
			this.isOnMove=false;
			return true;
		default:
			return false;
		}
		return true;
		
	}
	
   public void attachePieceToNearest(NeighborDescriptor theNeeighborDsc){
		
		//get X/Y position in Scene ref system of Top/Left corner of neighbor to attache to
		float[] newXY = theNeeighborDsc.theNeighbor.convertLocalToSceneCoordinates(0,0);
		
		float newX=newXY[X];
		float newY=newXY[Y];
		
		//Calculate the new position of the piece depending on the type of neighbor to attach to
		switch(theNeeighborDsc.theNeighborType){
		case PuzzleElement.NEIGHTBOR_TOP:
			newY=newY + theNeeighborDsc.theNeighbor.getHeight();
			break;
		case PuzzleElement.NEIGHTBOR_BOTTOM:
			newY=newY - theNeeighborDsc.theNeighbor.getHeight();
			break;
		case PuzzleElement.NEIGHTBOR_LEFT:
			newX=newX + theNeeighborDsc.theNeighbor.getWidth();
			break;
		case PuzzleElement.NEIGHTBOR_RIGHT:
			newX=newX - theNeeighborDsc.theNeighbor.getWidth();
			break;
		}
		//get the coordinate to attach to in element local(local is the parent of element) reference system
		newXY=this.convertSceneToLocalCoordinates(newX,newY);
		newX=newXY[X];
		newY=newXY[Y];
		
		this.setPosition(getX() + newX - theNeeighborDsc.thePiece.getX(), 
				this.getY()+ newY - theNeeighborDsc.thePiece.getY());
	}

	
	public NeighborDescriptor getNearestPuzzleElement(){
		NeighborDescriptor thePuzzleElement = new NeighborDescriptor();
		int theNeighborType =PuzzleElement.NOT_VAID_NEIGHBORS;
		
		for(int childIndex=this.mPuzzleElements.size()-1; childIndex>=0; childIndex--){
			//get Element from contain
			thePuzzleElement.thePiece = mPuzzleElements.get(childIndex);
			//check if found a neighbor
			if((theNeighborType = thePuzzleElement.thePiece.getNearest()) != PuzzleElement.NOT_VAID_NEIGHBORS){
				thePuzzleElement.theNeighborType = theNeighborType;
				thePuzzleElement.theNeighbor= thePuzzleElement.thePiece.getNeighbor(theNeighborType);
				return thePuzzleElement;
			}
		}
		return null;
	}
	
	public void mergeWithContainer(PuzzleElementContainer otherContainer){
		//get all elements from the container and add to it
		for(int nbChild=otherContainer.getNbOfPuzzleElements()-1; nbChild>=0; nbChild--){
			//get Element from contain
			PuzzleElement theElement = (PuzzleElement) otherContainer.mPuzzleElements.get(nbChild);
			theElement.changeContainer(this);
			theElement.detachAllNeigborInContainer(this);
		}
		((PuzzleSprites)this.getParent()).destroyContainer(otherContainer);
	}
	
	public void addPuzzleElement(PuzzleElement thePuzzleElement){
		//update element 
		thePuzzleElement.addToContainer(this);
		thePuzzleElement.detachAllNeigborInContainer(this);
		thePuzzleElement.resetBorder(false);
		//detach element from PuzzleSpriteList
		((PuzzleSprites) this.getParent()).deleteFromList(thePuzzleElement);
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
}
