package com.welmo.andengine.scenes;

import java.util.HashMap;



import org.andengine.engine.Engine;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.CardinalSplineMoveModifier.CardinalSplineMoveModifierConfig;
import org.andengine.entity.modifier.PathModifier.Path;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.scene.background.IBackground;
import org.andengine.entity.scene.background.SpriteBackground;
import org.andengine.entity.shape.IAreaShape;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.util.HorizontalAlign;

import com.welmo.andengine.managers.ResourcesManager;
import com.welmo.andengine.managers.SceneDescriptorsManager;
import com.welmo.andengine.managers.SceneManager;
import com.welmo.andengine.scenes.components.CardinalSplineMoveAndRotateModifier;
import com.welmo.andengine.scenes.components.ClickableSprite;
import com.welmo.andengine.scenes.components.CompoundSprite;
import com.welmo.andengine.scenes.components.IActionOnSceneListener;
import com.welmo.andengine.scenes.components.PositionHelper;
import com.welmo.andengine.scenes.components.Stick;
import com.welmo.andengine.scenes.components.TextComponent;
import com.welmo.andengine.scenes.descriptors.components.BackGroundObjectDescriptor;
import com.welmo.andengine.scenes.descriptors.components.BasicDescriptor;
import com.welmo.andengine.scenes.descriptors.components.BasicObjectDescriptor;
import com.welmo.andengine.scenes.descriptors.components.SceneDescriptor;
import com.welmo.andengine.scenes.descriptors.components.SpriteObjectDescriptor;
import com.welmo.andengine.scenes.descriptors.components.TextObjectDescriptor;
import com.welmo.andengine.scenes.descriptors.events.SceneActions;


import android.content.Context;


public class ManageableScene extends Scene implements IManageableScene, IActionOnSceneListener{
	//--------------------------------------------------------
	// Variables
	//--------------------------------------------------------
	private static final String 				TAG  = "ManageableScene";
	
	protected Engine 							mEngine;
	protected Context 							mContext;
	protected ResourcesManager					pRM;
	protected SceneManager						pSM;
	protected HashMap<Integer, IAreaShape> 		mapOfObjects;
	protected SceneDescriptor 					pSCDescriptor;
	
	// ===========================================================
	// Constructor
	// ===========================================================
	public ManageableScene(){
		//Initialize pointer to resource manager and object map
		pRM = ResourcesManager.getInstance();
		mapOfObjects = new HashMap<Integer, IAreaShape>();
	}
	
	// ===========================================================
	// Load the scene
	// ===========================================================
	public void loadScene(SceneDescriptor sceneDescriptor) {
		pSCDescriptor = sceneDescriptor;
		for(BasicDescriptor scObjDsc:pSCDescriptor.pChild){
			loadComponent(scObjDsc, this);
		}
		//Enable audio option
		mEngine.getEngineOptions().getAudioOptions().setNeedsMusic(true);
		mEngine.getEngineOptions().getAudioOptions().setNeedsSound(true);
	}
	
	// ===========================================================
	// Load components
	// ===========================================================
	private void loadComponent(BasicDescriptor scObjDsc, IEntity pEntityFather){
		IEntity newEntity = null;
		if(scObjDsc instanceof BackGroundObjectDescriptor){
			this.setBackground(createBackground((BackGroundObjectDescriptor)scObjDsc));
			return;
		}
		if(scObjDsc instanceof SpriteObjectDescriptor){
			SpriteObjectDescriptor pSprtDsc = (SpriteObjectDescriptor)scObjDsc;
			switch(pSprtDsc.getType()){	
			case  STATIC:
				newEntity = createSprite(pSprtDsc);
				break;
			case CLICKABLE: /* Create the clickable sprite elements */
				newEntity = createClickableSprite(pSprtDsc);
				break;
			case COMPOUND_SPRITE:
				newEntity = createCompoundSprite(pSprtDsc);
				break;
			case ANIMATED: /* Create the animated sprite elements */
				newEntity = createAnimatedSprite(pSprtDsc);
				break;
			default:
				break;
			}
			if(newEntity != null)pEntityFather.attachChild(newEntity);
		}
		if(scObjDsc instanceof TextObjectDescriptor){
			newEntity = createText((TextObjectDescriptor)scObjDsc,pEntityFather);
		}
		//handle children
		if(newEntity != null)
			for(BasicDescriptor theChild:scObjDsc.pChild)
				loadComponent(theChild, newEntity);
		this.sortChildren();
		
		return;
	}
	// ===========================================================
	// Create components
	// ===========================================================

	// ===========================================================
	// Create component BackGround
	// ===========================================================
	private IBackground createBackground(BackGroundObjectDescriptor pBkgDsc){
		switch(pBkgDsc.type){
		case COLOR:
			return new Background(pRM.getColor(pBkgDsc.color));
		case SPRITE:
			SpriteObjectDescriptor pSDsc = (SpriteObjectDescriptor)pBkgDsc.pChild.getFirst();
			Sprite spriteBKG = new Sprite(0, 0, pSDsc.getIDimension().getWidth(), pSDsc.getIDimension().getHeight(), 
					pRM.getTextureRegion(pSDsc.getTextureName()), 
					this.mEngine.getVertexBufferObjectManager());
			return new SpriteBackground(spriteBKG);
		default:
			return null;
		}
	}
	// ===========================================================
	// Create component Static Sprite
	// ===========================================================
	private IEntity createSprite(SpriteObjectDescriptor spDsc){
		final Sprite newSprite = new Sprite(spDsc.getIPosition().getX(), spDsc.getIPosition().getY(), 
				spDsc.getIDimension().getWidth(), spDsc.getIDimension().getHeight(), 
				pRM.getTextureRegion(spDsc.getTextureName()), 
				this.mEngine.getVertexBufferObjectManager());
		newSprite.setZIndex(spDsc.getIPosition().getZorder());
		mapOfObjects.put(spDsc.getID(), newSprite); 
		return newSprite;
	}
	private IEntity createCompoundSprite(SpriteObjectDescriptor spDsc){
		CompoundSprite newCompound = new CompoundSprite(0, 0, 0,0, this.mEngine.getVertexBufferObjectManager());
		newCompound.setID(spDsc.getID());
		newCompound.setActionOnSceneListener(this);
		newCompound.setPDescriptor(spDsc);
		this.registerTouchArea(newCompound);
		mapOfObjects.put(spDsc.getID(), newCompound); 
		return newCompound;
	}
	
	// ===========================================================
	// Create component Text
	// ===========================================================
	private IEntity createText(TextObjectDescriptor spTxtDsc, IEntity pEntityFather){
		IEntity newEntity = null;
		switch(spTxtDsc.getType()){	
		case SIMPLE: //TODO add to text description Text Option with default value
			if(pEntityFather instanceof IAreaShape)
				newEntity = new TextComponent(spTxtDsc, pRM, mEngine, (IAreaShape)pEntityFather);
			else
				newEntity = new TextComponent(spTxtDsc, pRM, mEngine, null);
			pEntityFather.attachChild(newEntity);
			break;
		default:
			break;
		}
		return newEntity;
	}
	// ===========================================================
	// Create component Clickable Sprite
	// ===========================================================
	private IEntity createClickableSprite(SpriteObjectDescriptor spDsc){
			
		ClickableSprite newClickableSprite = new ClickableSprite (spDsc,pRM,mEngine);
		
		this.registerTouchArea(newClickableSprite);
		mapOfObjects.put(spDsc.getID(), newClickableSprite); 
		
		
		newClickableSprite.setActionOnSceneListener(this);
		newClickableSprite.setEventsHandler(spDsc.pEventHandlerList);
		return newClickableSprite;
	}
	// ===========================================================
	// Create component Animated Text
	// ===========================================================
	private IEntity createAnimatedSprite(SpriteObjectDescriptor spDsc){
		final AnimatedSprite animatedObject = new AnimatedSprite(100,100, 
				pRM.getTiledTexture(spDsc.getTextureName()), 
				this.mEngine.getVertexBufferObjectManager());
		
		final Path path = new Path(5).to(10, 10).to(10, 480 - 74).to(800 - 58, 480 - 74).to(800 - 58, 10).to(10, 10);

		/* Add the proper animation when a waypoint of the path is passed. */
		CardinalSplineMoveModifierConfig splineModifierConfig= new CardinalSplineMoveModifierConfig(8,0.4f);
		
		splineModifierConfig.setControlPoint(0, 233f, 88f);
		splineModifierConfig.setControlPoint(1, 182f, 238f);
		splineModifierConfig.setControlPoint(2, 68f,  194f);
		splineModifierConfig.setControlPoint(3, 109f, 376f);
		splineModifierConfig.setControlPoint(4, 290f, 357f);
		splineModifierConfig.setControlPoint(5, 346f, 141f);
		splineModifierConfig.setControlPoint(6, 259f, 154f);
		splineModifierConfig.setControlPoint(7, 233f, 88f);
		
		CardinalSplineMoveAndRotateModifier splineModifier = new CardinalSplineMoveAndRotateModifier(10,splineModifierConfig);
		
		splineModifier.pEngine = this.mEngine;
		splineModifier.pScene = this;
		
		
		animatedObject.registerEntityModifier(new LoopEntityModifier(splineModifier));
		
		animatedObject.animate(50);
		return animatedObject;
	}
	public void init(Engine theEngine, Context ctx) {
		mEngine = theEngine;
		mContext = ctx;
	}
	public void resetScene(){
		
	}	
	// ===========================================================
	// Dummy Class implementing IClickLeastener of ClicableSprite object
	// ===========================================================
	//Default Implementation of IActionOnSceneListener interface
	@Override
	public boolean onActionChangeScene(String nextScene) {
		ManageableScene psc = (ManageableScene) pSM.getScene(nextScene);
		if(psc != null){
			mEngine.setScene(pSM.getScene(nextScene));
			return true;
		}
		else
			return false;
	}
	public void setSceneManager(SceneManager sceneManager) {
		this.pSM = sceneManager;
	}
	@Override
	public void onStick(IAreaShape currentShapeToStick,
			SceneActions stickActionDescription) {
		IAreaShape shapeToStickWith = mapOfObjects.get(stickActionDescription.stick_with);
		// TO DO calculation distance must be from border and not from center & value must be a parameter
		if (shapeToStickWith != null){
			if(Stick.isStickOn(currentShapeToStick, shapeToStickWith, 150)){
				switch(stickActionDescription.stickMode){
				case STICK_LEFTH: Stick.lefth(currentShapeToStick, shapeToStickWith);break;
				case STICK_RIGHT: Stick.right(currentShapeToStick, shapeToStickWith);break;
				case STICK_UP: Stick.up(currentShapeToStick, shapeToStickWith);break;
				case STICK_BOTTOM: Stick.bottom(currentShapeToStick, shapeToStickWith);break;
				case STICK_UP_LEFTH: Stick.upLefth(currentShapeToStick, shapeToStickWith);break;
				case STICK_UP_RIGHT: Stick.upRight(currentShapeToStick, shapeToStickWith);break;
				case STICK_BOTTOM_LEFTH: Stick.bottomLefth(currentShapeToStick, shapeToStickWith);break;
				case STICK_BOTTOM_RIGHT: Stick.bottomRight(currentShapeToStick, shapeToStickWith);break;
				}
			}
		}
	}
	@Override
	public String getFatherScene() {
		return this.pSCDescriptor.getSceneFather();
	}
}
