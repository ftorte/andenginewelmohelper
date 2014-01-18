package com.welmo.andengine.scenes.components.buttons;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.welmo.andengine.scenes.descriptors.components.BasicDescriptor;
import com.welmo.andengine.scenes.descriptors.components.ButtonDescriptor;
import com.welmo.andengine.scenes.descriptors.components.ToolsBarDescriptor;
import com.welmo.andengine.scenes.messages.ISceneMessageHandler;
import com.welmo.andengine.scenes.messages.Message;
import com.welmo.andengine.utility.ColorHelper;

/******************************************************************************************************************
 * ToolsBar class is a rectangle containing a set of button. 
 ******************************************************************************************************************/
public class ToolsBar extends Rectangle implements ISceneMessageHandler{

	// -----------------------------------------------------------------------------------------
	// Constants
	// -----------------------------------------------------------------------------------------
	private final static String 					TAG 					= "ToolsBar";
	
	public static int								START 					= 0;
	public static int								INITIALIZED 			= 1;
	public static int								NBOFBUTTON	 			= 8;
	
	protected int 									nStatus					= START;
	protected VertexBufferObjectManager				pVBO 					= null;
	protected Scene									pTheScene				= null;
	protected ISceneMessageHandler					pMsgHandler				= null;
	
	//buttons list
	List<ButtonBasic> 								nListOfButtons;
	int 											nSelectedButton = 0;
	ToolsBarDescriptor								pToolBarDecsriptor = null;

	public ToolsBar(VertexBufferObjectManager pRectangleVertexBufferObject,Scene theScene, ToolsBarDescriptor pDescriptor){
		super(0,0,pDescriptor.getWidth(),pDescriptor.getHeight(), pRectangleVertexBufferObject);
		
		pTheScene = theScene;
		pVBO = pRectangleVertexBufferObject;
		
		nListOfButtons = new ArrayList<ButtonBasic>();
		
		pToolBarDecsriptor = pDescriptor;
		
		//Check that the linked scene can accept messages
		if(! (theScene instanceof ISceneMessageHandler))
			throw new NullPointerException("the message handler is not right class type");
		
		pMsgHandler = (ISceneMessageHandler)theScene;
		
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
			ButtonDescriptor pBtnDsc = (ButtonDescriptor) entry.getValue();
			
			ButtonOnOff theButton = new ButtonOnOff(pBtnDsc,this,pVBO);
			theButton.build(pBtnDsc);
			
			theButton.nID = ButtonIndex++;
			
			nListOfButtons.add(theButton);
			
			this.attachChild(theButton);
			
			theButton.setPosition(lastXPosition,0);
			
			lastXPosition = lastXPosition + pBtnDsc.nExternaDimension;
			
			pTheScene.registerTouchArea(theButton);
		}
	}
	// ----------------------------------------------------------------------------
	// Implement Interface ISceneMessageHandler
	// ----------------------------------------------------------------------------
	@Override
	public void SendMessage(Message msg) {
		if(this.pMsgHandler != null)
			pMsgHandler.SendMessage(msg);
	}
}
