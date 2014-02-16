package com.welmo.andengine.scenes.components;

import org.andengine.engine.Engine;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.CardinalSplineMoveModifier;
import org.andengine.entity.scene.Scene;


public class CardinalSplineMoveAndRotateModifier extends CardinalSplineMoveModifier{

	public CardinalSplineMoveAndRotateModifier(float pDuration,
			CardinalSplineMoveModifierConfig pCardinalSplineMoveModifierConfig) {
		super(pDuration, pCardinalSplineMoveModifierConfig);
		// TODO Auto-generated constructor stub
	}
	protected float lastX;
	protected float lastY;
	protected int nbLine=0;
	
	// temporary solution to be replaced with an event
	public Scene pScene;
	public Engine pEngine;
	
	@Override
	protected void onManagedUpdate(float pSecondsElapsed, IEntity pEntity) {
		// TODO Auto-generated method stub
		// Read current position which will be previous position once spline calculates
	
		lastX = pEntity.getX(); 
		lastY = pEntity.getY();
		
		//calculate new position
		super.onManagedUpdate(pSecondsElapsed, pEntity);
	
		// draw line from previous position to new position
		//final Line line = new Line(lastX, lastY, pEntity.getX(), pEntity.getY(), 2, this.pEngine.getVertexBufferObjectManager());
		//pScene.attachChild(line);
		//nbLine++;
		//Log.v("CardinalSplineMoveAndRotateModifier","NBLINE=" + nbLine);
		
		float deltaX = (pEntity.getX() - lastX);
		float deltaY = (pEntity.getY() - lastY);
		
		float angle = (float)Math.toDegrees(Math.atan(deltaY/deltaX));
		if (deltaX<0){
			if (deltaY <0){
				angle =  180 + angle;
			}
			else{
				angle =  180 + angle;	
			}
		}		
		pEntity.setRotation(angle);
	
	}

}
