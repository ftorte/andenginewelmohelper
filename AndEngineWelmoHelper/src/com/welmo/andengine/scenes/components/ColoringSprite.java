package com.welmo.andengine.scenes.components;


import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import android.util.Log;

import com.welmo.andengine.managers.ResourcesManager.DecoratedTextures;
import com.welmo.andengine.scenes.components.interfaces.IComponent;
import com.welmo.andengine.scenes.descriptors.BasicDescriptor;
import com.welmo.andengine.scenes.operations.IOperationHandler;
import com.welmo.andengine.scenes.operations.Operation;

public class ColoringSprite  extends Sprite implements IComponent, IOperationHandler{
	
	// --------------------------------------------------------------------------------
	// Constant
	// --------------------------------------------------------------------------------
	final static String 				TAG 					= "ColoringSprite";
	final static int					LONG_CLICK_MASK			= 0X03;
	final static int					NANOSEC_PER_CENTSEC		= 10000000;
	final static long					MIN_CLICK_TIME_IN_CENTSEC=5;
	final static long					MAX_CLICK_TIME_IN_CENTSEC=20;
	
	// --------------------------------------------------------------------------------
	// Memeber Variables
	// --------------------------------------------------------------------------------
	private DecoratedTextures 			theDecoratedTexture 	= null;
	private int 						ID						= 0;
	private int							colorFill				= 0xFFFFFFFF;
	private int							colorBorder				= 0xFF000000;
	private PixelsStack 				theStack 				= null;
	@SuppressWarnings("unused")
	private int							nLongClick				= 0x0;
	protected long						lTimeStartInCentSec		= 0l;
	protected long						lMinClickTime			= MIN_CLICK_TIME_IN_CENTSEC;
	protected long						lMaxClickTime			= MAX_CLICK_TIME_IN_CENTSEC;
	protected Operation					msgClickMessage			= null;
	
	
	// --------------------------------------------------------------
	// Inner class to manage stack of pixels
	// --------------------------------------------------------------
	class PixelsStack {
		protected int[] stack;
		protected int	stacksize;
		protected int 	width;
		protected int 	height;
		protected int	stackPointer 	= 	0;
		
		PixelsStack(int width, int height){
			stack = new int[width*height];
			stacksize = width*height;
			this.width=width;
			this.height=height;
		}
		public void emptyStack() { 
			stackPointer = 0;
		}
		public boolean push(int ID){ 
			if(stackPointer < stacksize - 1){ 
				stackPointer++; 
				stack[stackPointer] = ID; 
				return true; 
			}     
			else 
				return false;  
		}
		public int pop() { 
			if(stackPointer > 0){ 
				return stack[stackPointer--]; 
			}     
			else
				return -1;  
		}    
	}    
	
	// --------------------------------------------------------------------------------
	// Constructors
	// --------------------------------------------------------------------------------	
	public ColoringSprite(float pX, float pY, DecoratedTextures pDecoratedTextureRegion,
			VertexBufferObjectManager pVertexBufferObjectManager) {
		super(pX, pY, pDecoratedTextureRegion.getTexture(), pVertexBufferObjectManager);
		theDecoratedTexture = pDecoratedTextureRegion;
		theStack = new PixelsStack(theDecoratedTexture.getWidth(), theDecoratedTexture.getHeight());
		nLongClick = 0x0;
		//create the on click message
		msgClickMessage = new Operation (IOperationHandler.OperationTypes.COLORING_CKIK,0);
		
		//transfor the spirte in pure black/white image 
		//transforToBW();
	}
	
	//--------------------------------------------------------------------------
	// Load a new Image
	// @imageName 	theNewfilename with the path from the resource to load the image example "gfx/monster01.png"
	//
	public void loadImage(String imageName) {
		theDecoratedTexture.loadImage(imageName);
		//transforToBW();
	}
	public void transforToBW(){
		//get copuy of the image
		int[] pixelsCopy = theDecoratedTexture.getPixelsCopy();
		
		int greyLevel;
		
		for(int index=0; index < (theDecoratedTexture.MAX_HEIGHT * theDecoratedTexture.MAX_WIDTH); index++){
			
			greyLevel = (((pixelsCopy[index] & 0x0FF0000)>>16) + ((pixelsCopy[index] & 0x000FF00)>>8) + (pixelsCopy[index] & 0x00000FF));
			if(greyLevel > 250)
				pixelsCopy[index] = 0xFFFFFFFF;
			else
				pixelsCopy[index] = 0xFF000000;				
		}
				
		theDecoratedTexture.setPixelsFromCopy(pixelsCopy);
		theDecoratedTexture.reloadBitmap();
	}
	
	//--------------------------------------------------------------------------
	// Getter & Setter
	//--------------------------------------------------------------------------	
	public int getColorFill() {
		return colorFill;
	}

	public void setColorFill(int colorFill) {
		this.colorFill = colorFill;
	}
	
	// --------------------------------------------------------------------------------
	// Implement interface IBasicComponent
	// --------------------------------------------------------------------------------
	@Override
	public int getID() {// TODO Auto-generated method stub
		return this.ID;
	}


	@Override
	public void setID(int ID) {
		this.ID=ID;
	}
	// --------------------------------------------------------------------------------
	// Override interface ITouchArea
	// --------------------------------------------------------------------------------	
	@Override
	public boolean onAreaTouched(TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
		Log.i(TAG," Coloring sprite onAreaTouched");
		int action = pSceneTouchEvent.getAction();
		
		switch(action){
			case TouchEvent.ACTION_UP:
				long lCurrentTimeInCentSec = System.nanoTime()/NANOSEC_PER_CENTSEC;
				long deltaTime = lCurrentTimeInCentSec - lTimeStartInCentSec;
				if((deltaTime >= lMinClickTime) & (deltaTime <= lMaxClickTime )){
					int oldColor = theDecoratedTexture.getPixelsCopy()[convertXYtoID((int)pTouchAreaLocalX,(int)pTouchAreaLocalY)];
					msgClickMessage.setParameter((int) pTouchAreaLocalX, (int)pTouchAreaLocalY,colorFill,oldColor);
					msgClickMessage.pushHander(this);
					this.doOperation(msgClickMessage);
					//Dispatch to parent to store in the undo queue
					if(getParent() instanceof IOperationHandler){
						((IOperationHandler)getParent()).doOperation(msgClickMessage);
					}
				}
				lTimeStartInCentSec = 0;
				break;
			case TouchEvent.ACTION_DOWN:
				lTimeStartInCentSec = System.nanoTime()/NANOSEC_PER_CENTSEC;
				break; 
			case TouchEvent.ACTION_MOVE:
				break;
			case TouchEvent.ACTION_OUTSIDE:
			case TouchEvent.INVALID_POINTER_ID:
			case TouchEvent.ACTION_CANCEL:
				lTimeStartInCentSec = 0;
				nLongClick = 0;
				break;
		}
		return false;
	}
	// --------------------------------------------------------------------------------
	// Color flood algorithm
	// --------------------------------------------------------------------------------		
	public int flood(final int X, final int Y, int newColor){
		int[] pixelsCopy = theDecoratedTexture.getPixelsCopy();
		int OldColor = pixelsCopy[convertXYtoID(X,Y)];
		floodFillScanlineStack(pixelsCopy,X, Y, newColor, OldColor);
		theDecoratedTexture.setPixelsFromCopy(pixelsCopy);
		theDecoratedTexture.reloadBitmap();
		return OldColor;
	}
	public int convertXYtoID(int X, int Y){
		return (Y << 10) | X;
	}
	//The scanline floodfill algorithm using our own stack routines, faster
	public void floodFillScanlineStack(int[] screenBuffer, int x, int y, int newColor, int oldColor)
	{
	    if(oldColor == newColor) return;
	    
	    
	    theStack.emptyStack();
	    
	    int y1; 
	    boolean spanLeft, spanRight;
	    
	    if(!theStack.push(convertXYtoID(x,y))) return;
	    
	    int theID;
	   
	    while((theID = theStack.pop())!= -1 )
	    {    
	        y=	theID >> 10;
	        x=	theID & 0x03FF;
	        
	    	y1 = y;
	       
	    	//colorBorder
	        //while((y1 >= 0) && (screenBuffer[convertXYtoID(x,y1)] == oldColor)) 
	        while((y1 >= 0) && (screenBuffer[convertXYtoID(x,y1)] != colorBorder))         
	        	y1--;
	        
	        y1++;
	        
	        spanLeft = spanRight = false;
	        
	        //while(y1 <= theStack.height && screenBuffer[convertXYtoID(x,y1)] == oldColor )
	        while(y1 <= theStack.height && screenBuffer[convertXYtoID(x,y1)] != colorBorder )
	        {
	        	screenBuffer[convertXYtoID(x,y1)] = newColor;
	        	
	        	//if(!spanLeft && x > 0 && screenBuffer[convertXYtoID(x-1,y1)] == oldColor) 
	        	if(!spanLeft && x > 0 && (screenBuffer[convertXYtoID(x-1,y1)] != colorBorder) && (screenBuffer[convertXYtoID(x-1,y1)] != newColor)) 
	        	{
	        		if(!theStack.push(convertXYtoID(x-1,y1))) return;
	        		spanLeft = true;
	        	}
	        	//else if(spanLeft && x > 0 && screenBuffer[convertXYtoID(x-1,y1)] != oldColor)
	        	else if(spanLeft && x > 0 && (screenBuffer[convertXYtoID(x-1,y1)] == colorBorder))
	        	{
	        		spanLeft = false;
	        	}
	        	//if(!spanRight && x < (theStack.width - 1) && screenBuffer[convertXYtoID(x+1,y1)] == oldColor) 
	        	if(!spanRight && x < (theStack.width - 1) && (screenBuffer[convertXYtoID(x+1,y1)] != colorBorder) && (screenBuffer[convertXYtoID(x+1,y1)] != newColor)) 
	        	{
	        		if(!theStack.push(convertXYtoID(x+1,y1))) return;
	        		spanRight = true;
	        	}
	        	//else if(spanRight && x < (theStack.width - 1) && screenBuffer[convertXYtoID(x+1,y1)] != oldColor)	
	        	else if(spanRight && x < (theStack.width - 1) && (screenBuffer[convertXYtoID(x+1,y1)]  == colorBorder))
	        	{
	        		spanRight = false;
	        	} 
	        	y1++;
	        }
	        Log.i(TAG,"Point = " + x + " " + y1  );
	    }
	}

	@Override
	public void build(BasicDescriptor pDsc) {
		// TODO Auto-generated method stub
		
	}
	
	/* *******************************************************************************************
	 * Implement interface IOperationHandler
	 * public void doOperation(Operation msg);
	   public void undoOperation(Operation msg);
	 */
	@Override
	public void doOperation(Operation msg) {
		switch(msg.type){
			case COLORING_CKIK:
				Log.i(TAG,"COLORING_CKIK");
				// Cancel any scroll movements (position the camera center to the origin)
				flood(msg.getParameter(0),msg.getParameter(1),msg.getParameter(2));
				break;
			default:
				break;
		}
	}

	@Override
	public void undoOperation(Operation ope) {
		IOperationHandler handler = ope.getHander();
		if(handler == this){
			switch(ope.type){
				case COLORING_CKIK:
					Log.i(TAG,"BACK COLORING_CKIK");
					// Cancel any scroll movements (position the camera center to the origin)
					flood(ope.getParameter(0),ope.getParameter(1),ope.getParameter(3));
				break;
			default:
				break;
		}
		}
		else
			handler.undoOperation(ope);
			
	}
}
