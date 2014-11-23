package com.welmo.andengine.scenes.components.buttons;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.welmo.andengine.scenes.descriptors.BasicDescriptor;
import com.welmo.andengine.scenes.descriptors.components.ButtonDescriptor;
import com.welmo.andengine.scenes.descriptors.components.ToolsBarDescriptor;
import com.welmo.andengine.scenes.operations.IOperationHandler;
import com.welmo.andengine.scenes.operations.Operation;
import com.welmo.andengine.utility.ColorHelper;

/******************************************************************************************************************
 * ToolsBar class is a rectangle containing a set of button. 
 ******************************************************************************************************************/
public class ToolsBar extends Rectangle implements IOperationHandler{

	// -----------------------------------------------------------------------------------------
	// Constants
	// -----------------------------------------------------------------------------------------
	@SuppressWarnings("unused")
	private final static String 					TAG 					= "ToolsBar";
	
	public static int								START 					= 0;
	public static int								INITIALIZED 			= 1;
	public static int								NBOFBUTTON	 			= 8;
	
	protected int 									nStatus					= START;
	protected VertexBufferObjectManager				pVBO 					= null;
	protected Scene									pTheScene				= null;
	protected IOperationHandler					pMsgHandler				= null;
	
	//buttons list
	List<ButtonBasic> 								nListOfButtons			= new ArrayList<ButtonBasic>();;
	int 											nSelectedButton = 0;
	ToolsBarDescriptor								pToolBarDecsriptor = null;

	public ToolsBar(VertexBufferObjectManager pRectangleVertexBufferObject,Scene theScene, ToolsBarDescriptor pDescriptor){
		super(0,0,pDescriptor.getWidth(),pDescriptor.getHeight(), pRectangleVertexBufferObject);
		
		pTheScene 			= theScene;
		pVBO 				= pRectangleVertexBufferObject;
		pToolBarDecsriptor 	= pDescriptor;
		
		//Check that the linked scene can accept messages
		if(! (theScene instanceof IOperationHandler))
			throw new NullPointerException("the message handler is not right class type");
		
		pMsgHandler = (IOperationHandler)theScene;
		
		init();
	}
	public void init(){
		nStatus			= START;

		//set default background of the ColorToolBar
		int color = pToolBarDecsriptor.getBackgroungColor();
		this.setColor(ColorHelper.Red(color),ColorHelper.Green(color),ColorHelper.Blue(color));

		int ButtonIndex = 0;
		int lastXPosition = 0;
		
		Iterator<Entry<Integer,BasicDescriptor>> btnDscIterator = pToolBarDecsriptor.pChild.entrySet().iterator();
		//TODO make internal/external dimension configurable
		
		while(btnDscIterator.hasNext()) {
			Entry<Integer,BasicDescriptor> entry = btnDscIterator.next();
			if(!(entry.getValue() instanceof ButtonDescriptor))
				throw new NullPointerException ("ToolBar accept only button descriptors");
			
			ButtonDescriptor pBtnDsc = (ButtonDescriptor) entry.getValue();
			ButtonBasic newButton = null;
			switch (ButtonBasic.Types.valueOf(pBtnDsc.getSubType())){
				case BASIC:
					break;
				case CLICK:
					newButton = new ButtonClick(pBtnDsc,pVBO);
					break;
				case ON_OFF:
					newButton = new ButtonOnOff(pBtnDsc,pVBO);
					break;
				case ON_OFF_WITH_TIMER:
					newButton = new ButtonOnOffwithTimer(pBtnDsc,pVBO);
					break;
				case PULSE:
					newButton = new ButtonPulse(pBtnDsc,pVBO);
					break;	
			}
			newButton.configure(pBtnDsc);
			newButton.setOperationsHandler(this);
			newButton.nID = ButtonIndex++;
			nListOfButtons.add(newButton);
			this.attachChild(newButton);
			newButton.setPosition(lastXPosition,0);
			lastXPosition = lastXPosition + pBtnDsc.nExternaDimension;
			pTheScene.registerTouchArea(newButton);
		}
	}
	// ----------------------------------------------------------------------------
	// Implement Interface IOperationHandler
	// ----------------------------------------------------------------------------
	@Override
	public void doOperation(Operation msg) {
		if(this.pMsgHandler != null){
			pMsgHandler.doOperation(msg);
		}
	}
	@Override
	public void undoOperation(Operation msg) {
		IOperationHandler hdOperation = msg.getHander();// TODO Auto-generated method stub
		hdOperation.undoOperation(msg);
	}
}
