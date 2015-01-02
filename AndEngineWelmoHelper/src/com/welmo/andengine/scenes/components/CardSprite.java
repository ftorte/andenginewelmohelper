package com.welmo.andengine.scenes.components;

//import org.andengine.engine.Engine;


import org.andengine.engine.Engine;
import org.andengine.entity.shape.IAreaShape;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.TiledSprite;
import org.andengine.entity.sprite.vbo.HighPerformanceTiledSpriteVertexBufferObject;
import org.andengine.entity.sprite.vbo.ITiledSpriteVertexBufferObject;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.shader.PositionColorTextureCoordinatesShaderProgram;
import org.andengine.opengl.shader.ShaderProgram;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.util.GLState;
import org.andengine.opengl.vbo.DrawType;

import com.welmo.andengine.managers.ResourcesManager;
import com.welmo.andengine.scenes.components.interfaces.IComponentClickableDfltImp;
import com.welmo.andengine.scenes.components.interfaces.IActionSceneListener;
import com.welmo.andengine.scenes.components.interfaces.IComponentClickable;
import com.welmo.andengine.scenes.components.interfaces.IComponent;
import com.welmo.andengine.scenes.components.interfaces.IComponentEventHandler;
import com.welmo.andengine.scenes.descriptors.BasicDescriptor;
import com.welmo.andengine.scenes.descriptors.components.SpriteObjectDescriptor;
import com.welmo.andengine.scenes.descriptors.events.SceneActions;
import com.welmo.andengine.scenes.descriptors.events.ComponentEventHandlerDescriptor.Events;
import com.welmo.andengine.scenes.descriptors.events.SceneActions.ActionType;
import com.welmo.andengine.scenes.operations.IOperationHandler;

//import com.welmo.andengine.managers.ResourcesManager;
//import com.welmo.andengine.scenes.descriptors.components.SpriteObjectDescriptor;





import android.util.Log;


public class CardSprite extends TiledSprite implements IComponentClickable, IActionSceneListener, IComponent{

	// =========================================================================================
	// Constants
	// =========================================================================================
	//Log & Debug
	private static final String TAG = "CardSprite";

	public enum CardSide {
		A, B, 
	}
	// =========================================================================================
	// Fields
	// =========================================================================================
	protected int 										nSideATileNb	= 0;
	protected int 										nSideBTileNb	= 0;
	protected String									sSoundNAme		= "";
	protected CardSide									currentSide 	= CardSide.A;
	protected IComponentClickable 						mIClicakableImpmementation = null;
	protected IActionSceneListener 						mIActionSceneListnerImpmementation = null;
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
		mIClicakableImpmementation =  new IComponentClickableDfltImp();
		mIClicakableImpmementation.setParent(this);
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
		
		//Init side A & B and set card as side A
		setSidesTiles(spDsc.getSidesA(), spDsc.getSidesB());
		setSideA();
		
		this.setSoundName(spDsc.getSoundName());
	}
	public void setSideA(){
		setCurrentTileIndex(nSideATileNb);
		currentSide = CardSide.A;
		this.setRotation(0);
	}
	public void setSideB(){
		setCurrentTileIndex(nSideBTileNb);
		currentSide = CardSide.B;
		this.setRotation(180);
	}
	public CardSide getCurrentSidet(){
		return currentSide;
	}
	// ===========================================================
	// Methods 
	// ===========================================================
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
	// ====== IActionOnSceneListener ==== 	
	// ===========================================================		
	// ====== SuperClass methods ==== 	
	@Override
	public boolean onAreaTouched(TouchEvent pSceneTouchEvent,float pTouchAreaLocalX, float pTouchAreaLocalY) {
		return this.onTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
	}
	@Override
	protected void applyRotation(final GLState pGLState) {
		final float rotation = this.mRotation;

		if (rotation>=0 && rotation <=90){
			//Log.i(TAG,"\t applyRotation" + rotation);
			if ( this.getCurrentTileIndex() != nSideATileNb)
				setCurrentTileIndex(nSideATileNb);
			if (currentSide != CardSide.A)
				currentSide = CardSide.A;
		}
		if (rotation>=90 && rotation <=180){
			//Log.i(TAG,"\t applyRotation" + rotation);
			if ( this.getCurrentTileIndex() != nSideBTileNb)
				setCurrentTileIndex(nSideBTileNb);
			if (currentSide != CardSide.B)
				currentSide = CardSide.B;

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
	public String getSoundName() {
		return sSoundNAme;
	}
	public void setSoundName(String sSound) {
		this.sSoundNAme = sSound;
	}
	// ===========================================================
	// Interfaces & Superclass
	// ===========================================================	
	// ====== IClickableSprite ==== 	
	public void addEventsHandler(Events theEvent, IComponentEventHandler oCmpDefEventHandler){
		mIClicakableImpmementation.addEventsHandler(theEvent, oCmpDefEventHandler);
	}
	public IActionSceneListener getActionOnSceneListener(){
		return mIClicakableImpmementation.getActionOnSceneListener();
	}
	public int getID() {
		return mIClicakableImpmementation.getID();
	}
	public void setID(int ID) {
		mIClicakableImpmementation.setID(ID);
	}
	public boolean onTouched(TouchEvent pSceneTouchEvent,
			float pTouchAreaLocalX, float pTouchAreaLocalY) {
		return mIClicakableImpmementation.onTouched(pSceneTouchEvent,pTouchAreaLocalX,pTouchAreaLocalY);
	}
	@Override
	public void onFireEventAction(Events event, ActionType type) {
		mIClicakableImpmementation.onFireEventAction(event, type);
	}
	@Override
	public IComponentEventHandler getEventsHandler(Events theEvent) {
		mIClicakableImpmementation.getEventsHandler(theEvent);
		return null;
	}
	@Override
	public String getPersistenceURL() {
		return mIClicakableImpmementation.getPersistenceURL();
	}
	// ===========================================================		
	// ====== IActionOnSceneListener ==== 	
	@Override
	public void onStick(IAreaShape currentShapeToStick,
			SceneActions stickActionDescription) {
		mIActionSceneListnerImpmementation.onStick(currentShapeToStick, stickActionDescription);
	}
	@Override
	public void onFlipCard(int CardID, CardSide currentSide) {
		mIActionSceneListnerImpmementation.onFlipCard(CardID,currentSide);	
	}	
	@Override
	public void lockTouch() {
		mIActionSceneListnerImpmementation.lockTouch();
	}
	@Override
	public void unLockTouch() {
		mIActionSceneListnerImpmementation.unLockTouch();
	}
	public void setIActionOnSceneListener(IActionSceneListener pListener){
		mIActionSceneListnerImpmementation = pListener;
	}
	@Override
	public void configure(BasicDescriptor pDsc) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void setActionSceneListner(IActionSceneListener scenelistener) {
		// TODO Auto-generated method stub
		
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

