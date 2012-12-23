package com.welmo.andengine.scenes.components;

//import org.andengine.engine.Engine;

import java.util.HashMap;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.entity.shape.IAreaShape;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.TiledSprite;
import org.andengine.entity.sprite.vbo.HighPerformanceTiledSpriteVertexBufferObject;
import org.andengine.entity.sprite.vbo.ITiledSpriteVertexBufferObject;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.shader.PositionColorTextureCoordinatesShaderProgram;
import org.andengine.opengl.shader.ShaderProgram;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.util.GLState;
import org.andengine.opengl.vbo.DrawType;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.welmo.andengine.managers.EventDescriptionsManager;
import com.welmo.andengine.managers.ResourcesManager;
import com.welmo.andengine.scenes.components.CardSprite.CardSide;
import com.welmo.andengine.scenes.descriptors.components.SpriteObjectDescriptor;
import com.welmo.andengine.scenes.descriptors.events.ComponentEventHandlerDescriptor;
import com.welmo.andengine.scenes.descriptors.events.SceneActions;
import com.welmo.andengine.scenes.descriptors.events.ComponentEventHandlerDescriptor.Events;

//import com.welmo.andengine.managers.ResourcesManager;
//import com.welmo.andengine.scenes.descriptors.components.SpriteObjectDescriptor;

import android.opengl.GLES20;
import android.util.Log;


public class CardSprite extends TiledSprite implements IClickableSprite, IActionOnSceneListener{

	// ===========================================================
	// Constants
	// ===========================================================
	//Log & Debug
	private static final String TAG = "CardSprite";

	public enum CardSide {
		A, B, 
	}

	// ===========================================================
	// Fields
	// ===========================================================
	protected HashMap<Events,IComponentEventHandler> 	hmEventHandlers = null;
	protected int 										nID 			= -1;	
	private IActionOnSceneListener   					mActionListener	= null;
	private int 										nSideATileNb	= 0;
	private int 										nSideBTileNb	= 0;
	private String										sSoundNAme		= "";
	

	/*
	private int mCurrentTileIndex;
	private final ITiledSpriteVertexBufferObject mTiledSpriteVertexBufferObject;
	 */
	// ===========================================================
	// Constructors
	// ===========================================================
	public CardSprite(SpriteObjectDescriptor pSPRDscf, ResourcesManager pRM, Engine theEngine){
		this((float)pSPRDscf.getIPosition().getX(), (float)pSPRDscf.getIPosition().getY(), (float)pSPRDscf.getIDimension().getWidth(),  (float)pSPRDscf.getIDimension().getHeight(), 
				pRM.getTiledTextureRegion(pSPRDscf.getTextureName()), 
				new HighPerformanceTiledSpriteVertexBufferObject(theEngine.getVertexBufferObjectManager(), 
						CardSprite.TILEDSPRITE_SIZE * (pRM.getTiledTextureRegion(pSPRDscf.getTextureName())).getTileCount(), 
						DrawType.STATIC,
						true, Sprite.VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT)); 
		this.setCurrentTileIndex(0);
		init();
		configure(pSPRDscf);
	}
	protected CardSprite(final float pX, final float pY, final float pWidth, final float pHeight, final ITiledTextureRegion pTiledTextureRegion, final ITiledSpriteVertexBufferObject pTiledSpriteVertexBufferObject) {
		this(pX, pY, pWidth, pHeight, pTiledTextureRegion, pTiledSpriteVertexBufferObject, PositionColorTextureCoordinatesShaderProgram.getInstance());
	}
	protected CardSprite(final float pX, final float pY, final float pWidth, final float pHeight, final ITiledTextureRegion pTiledTextureRegion, final ITiledSpriteVertexBufferObject pTiledSpriteVertexBufferObject, final ShaderProgram pShaderProgram) {
		super(pX, pY, pWidth, pHeight, pTiledTextureRegion, pTiledSpriteVertexBufferObject, pShaderProgram);	
		//this.mTiledSpriteVertexBufferObject = pTiledSpriteVertexBufferObject;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// private member function
	// ===========================================================	
	protected void init(){
		hmEventHandlers			= new HashMap<Events,IComponentEventHandler>();
	}
	public void configure(SpriteObjectDescriptor spDsc){
		ResourcesManager pRM = ResourcesManager.getInstance();
		setID(spDsc.getID());

		//set position			
		setX(spDsc.getIPosition().getX());
		setY(spDsc.getIPosition().getY());

		//set color	
		String theColor = spDsc.getICharacteristis().getColor();
		Log.i(TAG,"Get Color: " + theColor);
		if(!theColor.equals(""))
			this.setColor(pRM.getColor(theColor));
		
		setSidesTiles(spDsc.getSidesA(), spDsc.getSidesB());
		this.setSoundName(spDsc.getSoundName());
	}
	public void setSideA(){
		setCurrentTileIndex(nSideATileNb);
		this.setRotation(0);
	}
	public void setSideB(){
		setCurrentTileIndex(nSideBTileNb);
		this.setRotation(180);
	}
	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	public int getID() {
		return nID;
	}
	public void setID(int ID) {
		this.nID = ID;
	}
	public void setSidesTiles(int sideA, int sideB) {
		if((sideA >= this.getTileCount()) || (sideB >= this.getTileCount()) || (sideA < 0) || (sideB < 0)){
			nSideATileNb = nSideBTileNb = 0;
		}
		else{
			nSideATileNb=sideA;
			nSideBTileNb=sideB;
		}
	}
	// ===========================================================
	// Interfaces & Superclass
	// ===========================================================	
	// ===========================================================		
	// ====== IClickableSprite ==== 	
	public void addEventsHandler(Events theEvent, IComponentEventHandler oCmpDefEventHandler){
		Log.i(TAG,"\t addEventsHandler " + theEvent);
		hmEventHandlers.put(theEvent, oCmpDefEventHandler);
	}
	public void setActionOnSceneListener(IActionOnSceneListener actionLeastner) {
		Log.i(TAG,"\t setActionOnSceneListener ");
		this.mActionListener=actionLeastner;
	}
	public IActionOnSceneListener getActionOnSceneListener(){
		return mActionListener;
	}
	// ===========================================================		
	// ====== IActionOnSceneListener ==== 	
	@Override
	public boolean onActionChangeScene(String nextSceneName) {
		// TODO Auto-generated method stub
		return this.mActionListener.onActionChangeScene(nextSceneName);
	}
	@Override
	public void onStick(IAreaShape currentShapeToStick,
			SceneActions stickActionDescription) {
		this.mActionListener.onStick(currentShapeToStick, stickActionDescription);

	}
	@Override
	public void onFlipCard(int CardID) {
		this.mActionListener.onFlipCard(CardID);	
	}	
	@Override
	public void lockTouch() {
		this.mActionListener.lockTouch();
	}
	@Override
	public void unLockTouch() {
		this.mActionListener.unLockTouch();
	}
	// ===========================================================		
	// ====== SuperClass methods ==== 	
	@Override
	public boolean onAreaTouched(TouchEvent pSceneTouchEvent,float pTouchAreaLocalX, float pTouchAreaLocalY) {
		
		boolean managed = false;
		switch (pSceneTouchEvent.getAction()) {
		case TouchEvent.ACTION_DOWN:
			break;
		case TouchEvent.ACTION_MOVE:
			break;
		case TouchEvent.ACTION_UP:
			Log.i(TAG,"\t onAreaTouched TouchEvent.ACTION_UP");
			if(hmEventHandlers != null){
				Log.i(TAG,"\t launch event handler trough object control" + this.nSideATileNb);
				IComponentEventHandler handlerEvent = hmEventHandlers.get(ComponentEventHandlerDescriptor.Events.ON_CLICK);
				if(handlerEvent != null){
					Log.i(TAG,"\t launch event handler trough object found");
					handlerEvent.handleEvent(this);
				}
			}
			break;
		}
		return managed;
	}
	@Override
	protected void applyRotation(final GLState pGLState) {
		final float rotation = this.mRotation;

		if (rotation>=0 && rotation <=90){
			//Log.i(TAG,"\t applyRotation" + rotation);
			if ( this.getCurrentTileIndex() != nSideATileNb) setCurrentTileIndex(nSideATileNb);
		}
		if (rotation>=90 && rotation <=180){
			//Log.i(TAG,"\t applyRotation" + rotation);
			if ( this.getCurrentTileIndex() != nSideBTileNb) setCurrentTileIndex(nSideBTileNb);
		}
		
		if(rotation != 0) {
			final float rotationCenterX = this.mRotationCenterX * this.mScaleX;
			final float rotationCenterY = this.mRotationCenterY * this.mScaleY;
			//Log.i(TAG,"\t applyRotation rotation and translate XY " + rotation + " "+ rotationCenterX +" " + rotationCenterY);
			pGLState.translateModelViewGLMatrixf(rotationCenterX, rotationCenterY, 0);
			/* Note we are applying rotation around the y-axis and not the z-axis anymore! */
			pGLState.rotateModelViewGLMatrixf(rotation, 0, 1, 0);
			pGLState.translateModelViewGLMatrixf(-rotationCenterX, -rotationCenterY, 0);
		}
	}
	@Override
	protected void applyTranslation(final GLState pGLState) {
		pGLState.translateModelViewGLMatrixf(this.mX, this.mY, 0);
	}
	// ===========================================================
	// Methods
	// ===========================================================
	public void onFlip() {
		IComponentEventHandler handlerEvent = hmEventHandlers.get(ComponentEventHandlerDescriptor.Events.ON_CLICK);
		if(handlerEvent != null){
			Log.i(TAG,"\t launch event handler trough object found");
			handlerEvent.handleEvent(this);
		}
	}
	public String getSoundName() {
		return sSoundNAme;
	}
	public void setSoundName(String sSound) {
		this.sSoundNAme = sSound;
	}
}

