package com.welmo.andengine.scenes.components;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.andengine.audio.sound.Sound;
import org.andengine.engine.Engine;
import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.entity.modifier.ParallelEntityModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.shape.IAreaShape;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import android.util.Log;

import com.welmo.andengine.managers.EventDescriptionsManager;
import com.welmo.andengine.managers.ResourcesManager;
import com.welmo.andengine.scenes.descriptors.components.SpriteObjectDescriptor;
import com.welmo.andengine.scenes.descriptors.components.TextObjectDescriptor;
import com.welmo.andengine.scenes.descriptors.events.ComponentModifierDescriptor;
import com.welmo.andengine.scenes.descriptors.events.ComponentEventHandlerDescriptor;
import com.welmo.andengine.scenes.descriptors.events.ComponentEventHandlerDescriptor.Events;
import com.welmo.andengine.scenes.descriptors.events.ExecutionOrder;
import com.welmo.andengine.scenes.descriptors.events.SceneActions;
import com.welmo.andengine.utility.MLOG;


public class ClickableSprite extends Sprite {
	// ===========================================================
	// Constants
	// ===========================================================
	//Log & Debug
	private static final String TAG = "ClickableSprite";
	// ===========================================================
	// Fields
	// ===========================================================
	private IActionOnSceneListener mActionListener			=null;
	private int nID											=-1;
	private EventDescriptionsManager pEDMgr					=null;
	private Object					 pDescriptor			=null;
	//Fields to manage modifiers & actions
	HashMap<Events,IEntityModifier>	 pEntityModifiers		=null;							
	
	// ===========================================================
	// Constructors
	// ===========================================================
	public ClickableSprite(SpriteObjectDescriptor pSPRDscf, ResourcesManager pRM, Engine theEngine){
		super(pSPRDscf.getIPosition().getX(), pSPRDscf.getIPosition().getY(), 
				pSPRDscf.getIDimension().getWidth(), pSPRDscf.getIDimension().getHeight(), 
				pRM.getTextureRegion(pSPRDscf.getTextureName()), 
				theEngine.getVertexBufferObjectManager());
		init();
		configure(pSPRDscf); 
	}
	public ClickableSprite(float pX, float pY, float pWidth, float pHeight,
			ITextureRegion pTextureRegion,
			VertexBufferObjectManager pVertexBufferObjectManager) {
		super(pX, pY, pWidth, pHeight, pTextureRegion, pVertexBufferObjectManager);
		init(); 		
	}
	// ===========================================================
	// private member function
	// ===========================================================	
	private void init(){
		pEDMgr = EventDescriptionsManager.getInstance();
		pEntityModifiers = new HashMap<Events,IEntityModifier>();	
	}
	// ===========================================================
	// public member function
	// ===========================================================	
	public void configure(SpriteObjectDescriptor spDsc){
		ResourcesManager pRM = ResourcesManager.getInstance();
		setID(spDsc.getID());
		setPDescriptor(spDsc);
		/* Setup Rotation*/
		setRotationCenter(spDsc.getIOriantation().getRotationCenterX(), spDsc.getIOriantation().getRotationCenterX());
		setRotation(spDsc.getIOriantation().getOrientation());
		//set position			
		setX(spDsc.getIPosition().getX());
		setY(spDsc.getIPosition().getY());
		//set Z_Order
		this.setZIndex(spDsc.getIPosition().getZorder());	
		//set color	
		String theColor = spDsc.getICharacteristis().getColor();
		Log.i(TAG,"Get Color: " + theColor);
		if(!theColor.equals(""))
			this.setColor(pRM.getColor(theColor));
	}
	@Override
	public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
			float pTouchAreaLocalX, float pTouchAreaLocalY) {
		
		boolean managed = false;
		List<SceneActions> pActionList = null;
		List<ComponentEventHandlerDescriptor> pModifierList = null;
		
		switch (pSceneTouchEvent.getAction()) {
		case TouchEvent.ACTION_DOWN:
			if (MLOG.LOG)Log.i(TAG,"onAreaTouched ACTION_DOWN = " + nID);
			break;
		case TouchEvent.ACTION_MOVE:
			if (MLOG.LOG)Log.i(TAG,"onAreaTouched ACTION_MOVE = " + nID);
			pModifierList = pEDMgr.getModifierList(ComponentEventHandlerDescriptor.Events.ON_MOVE,this.getPDescriptor());
			if (pModifierList != null){
				/*
				for (SceneComponentModifier mod: pModifierList) {
					switch(mod.type){
					case MOVE: 
						this.setPosition(pSceneTouchEvent.getX() - this.getWidth() / 2, pSceneTouchEvent.getY() - this.getHeight() / 2);	
						managed = true;
						break;
					case SCALE:
						break;
					default:
						break;
					}
				}
				*/
			}
			if(mActionListener != null){
				pActionList = pEDMgr.getActionList(ComponentEventHandlerDescriptor.Events.ON_MOVE,this.getPDescriptor());
				if (pActionList != null){
					for (SceneActions act: pActionList) {
						switch(act.type){
						case STICK:
							mActionListener.onStick(this, act);
							managed = true;
							break;
						default:
							break;
						}
					}
				}
			}
			break;
		case TouchEvent.ACTION_UP:
			if (MLOG.LOG)Log.i(TAG,"onAreaTouched ACTION_UP= " + nID);
			// [FT] mClickListener.onClick(this.nID);
			if(mActionListener != null){
				pActionList = pEDMgr.getActionList(ComponentEventHandlerDescriptor.Events.ON_CLICK,this.getPDescriptor());
				if (pActionList != null){
					for (SceneActions act: pActionList) {
						switch(act.type){
						case CHANGE_SCENE:
							mActionListener.onActionChangeScene(act.NextScene);
							managed = true;
							break;
						default:
							break;
						}
					}
				}
			}
			if(pEntityModifiers != null){
				IEntityModifier mEntityModifier = pEntityModifiers.get(ComponentEventHandlerDescriptor.Events.ON_CLICK);
				if(mEntityModifier != null){
					mEntityModifier.reset();
					this.registerEntityModifier(mEntityModifier);
				}
				ResourcesManager rMgr = ResourcesManager.getInstance();
				Sound snd = rMgr.getSound("Animal1");
				snd.play();
			}
			break;
		}
		return managed;
	}
	public int getID() {
		return nID;
	}
	public void setID(int ID) {
		this.nID = ID;
	}
	public static interface  IClickLeastener{
		public void onClick(int ObjectID); // Sprite call this interface to inform parent that has been clicked
	}
	public void setActionOnSceneListener(IActionOnSceneListener actionLeastner) {
		this.mActionListener=actionLeastner;
	}
	public Object getPDescriptor() {
		return pDescriptor;
	}
	public void setPDescriptor(Object pDescriptor) {
		this.pDescriptor = pDescriptor;
	}
	public IEntityModifier setEventsHandler(HashMap<ComponentEventHandlerDescriptor.Events,ComponentEventHandlerDescriptor> pEventHandlerDescList){
		Set<Entry<ComponentEventHandlerDescriptor.Events,ComponentEventHandlerDescriptor>> eventHandlersSet;
		eventHandlersSet = pEventHandlerDescList.entrySet();
		for (Entry<ComponentEventHandlerDescriptor.Events,ComponentEventHandlerDescriptor> entry : eventHandlersSet){
			ComponentEventHandlerDescriptor eventHandler = entry.getValue();
			Events theEvent = entry.getKey();
			//switch(eventHandler.enExecOrder){
			//case SERIAL:
				IEntityModifier modifers[];
				int index = 0;
				modifers = new IEntityModifier[eventHandler.modifierSet.getIModifierList().getModifiers().size()];
				for(ComponentModifierDescriptor m: eventHandler.modifierSet.getIModifierList().getModifiers()){
					IEntityModifier modifier;
					switch(m.getIModifier().getType()){
						case SCALE:
							modifier = new ScaleModifier(1,m.getIModifier().getScaleBegin(),m.getIModifier().getScaleEnd());
							modifers[index++] = modifier;
							break;
						case SOUND:
							modifers[index++] = null;
						default:
							break;
					}
				}
				IEntityModifier modifierset; 
				if(eventHandler.modifierSet.getIModifierList().getExecOrder() == ExecutionOrder.SERIAL){
					modifierset = new SequenceEntityModifier(modifers);
				}
				else
					modifierset = new ParallelEntityModifier(modifers);
			
				this.pEntityModifiers.put(theEvent, modifierset);
				return modifierset;
		}
		return null;
	}
}