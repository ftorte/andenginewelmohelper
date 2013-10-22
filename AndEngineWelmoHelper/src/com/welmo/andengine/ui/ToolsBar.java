package com.welmo.andengine.ui;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class ToolsBar extends Rectangle {

	// -----------------------------------------------------------------------------------------
	// Constants
	// -----------------------------------------------------------------------------------------
	private final static String 					TAG 					= "ToolsBar";
		
	public static int								NB_OF_PRIMARY_COLORS 	= 8;
	public static int								NB_OF_SECONDARY_COLORS 	= 8;
	public static int								ACTIVE_COLORS 			= NB_OF_PRIMARY_COLORS;
	protected Rectangle[][]							pColorPalletSemector	= null;
	
	
	public static int								START 					= 0;
	public static int								INITIALIZED 			= 1;
	public static int								NBOFBUTTON	 			= 8;
	public static int								SCENEHEIGHT	 			= 800;
	public static int								BUTTOEXTDIM				= SCENEHEIGHT/NBOFBUTTON;

	protected int 									nStatus					= START;
	protected VertexBufferObjectManager				pVBO 					= null;
	protected Scene									pTheScene				= null;
	
	
	public ToolsBar(VertexBufferObjectManager pRectangleVertexBufferObject,Scene theScene){
		super(0,0,SCENEHEIGHT,BUTTOEXTDIM, pRectangleVertexBufferObject);
		pTheScene = theScene;
		pVBO = pRectangleVertexBufferObject;
		//init();
	}
	
}
