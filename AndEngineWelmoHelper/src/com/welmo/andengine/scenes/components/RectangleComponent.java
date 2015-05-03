package com.welmo.andengine.scenes.components;

import org.andengine.engine.Engine;
import org.andengine.entity.IEntity;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.shape.IAreaShape;

import com.welmo.andengine.managers.ResourcesManager;
import com.welmo.andengine.scenes.components.interfaces.IActionSceneListener;
import com.welmo.andengine.scenes.components.interfaces.IComponent;
import com.welmo.andengine.scenes.descriptors.BasicDescriptor;
import com.welmo.andengine.scenes.descriptors.components.SpriteObjectDescriptor;
import com.welmo.andengine.scenes.operations.IOperationHandler;

public class RectangleComponent extends Rectangle implements IComponent{

	

	// ========================================================================
	// Constants
	// ========================================================================
	//Log & Debug
	private static final String 		TAG = "ClickableSprite";
	// ========================================================================
	// Fields implementation 
	// ========================================================================
	private int 						nID = 0;
	// ========================================================================
	// Constructors
	// ========================================================================
	public RectangleComponent(SpriteObjectDescriptor pSPRDscf, Engine theEngine){
		super(pSPRDscf.getIPosition().getX(), pSPRDscf.getIPosition().getY(), 
				pSPRDscf.getIDimension().getWidth(), pSPRDscf.getIDimension().getHeight(), 
				theEngine.getVertexBufferObjectManager());
		
		configure(pSPRDscf); 
	}
	
	
	// ===========================================================
	// public member function
	// ===========================================================	
	// ===========================================================
	// Interfaces & Superclass
	// ===========================================================	

	@Override
	public int getID() {
		return nID;
	}

	@Override
	public void setID(int ID) {
		nID = ID;
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
	public void configure(BasicDescriptor spDsc){
		if(!(spDsc instanceof SpriteObjectDescriptor))
			throw new NullPointerException("invalid descripto in Static Sprite creation");
		
		
		SpriteObjectDescriptor dsc = (SpriteObjectDescriptor)spDsc;
		
		setID(spDsc.getID());
		
		/* Setup Rotation*/
		setRotationCenter(dsc.getIOriantation().getRotationCenterX(), dsc.getIOriantation().getRotationCenterX());
		setRotation(dsc.getIOriantation().getOrientation());
		
		//set position			
		setX(dsc.getIPosition().getX());
		setY(dsc.getIPosition().getY());
		
		//set Z_Order
		this.setZIndex(dsc.getIPosition().getZorder());	
		
		//set alignement
		if(this.getParent() instanceof IAreaShape){
			PositionHelper.align(dsc.getIPosition(), this, (IAreaShape)this.getParent() );
		}
		
		//set color	
		String theColor = dsc.getICharacteristis().getColor();
		if(!theColor.equals(""))
			this.setColor(ResourcesManager.getInstance().getColor(theColor));
		else
			this.setAlpha(0);
	}
	@Override
	public void setActionSceneListner(IActionSceneListener scenelistener) {
		// TODO Auto-generated method stub	
	}

	@Override
	public void setOperationsHandler(IOperationHandler messageHandler) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getPersistenceURL() {
		// TODO Auto-generated method stub
		return null;
	}
}