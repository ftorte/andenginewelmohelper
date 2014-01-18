package com.welmo.andengine.scenes.components;


import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import android.util.Log;
import com.welmo.andengine.managers.ResourcesManager.DecoratedTextures;
import com.welmo.andengine.scenes.descriptors.components.BasicDescriptor;

public class ColoringSprite  extends Sprite implements IBasicComponent{
	
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
	private PixelsStack 				theStack 				= null;
	private int							nLongClick				= 0x0;
	protected long						lTimeStartInCentSec		= 0l;
	protected long						lMinClickTime			= MIN_CLICK_TIME_IN_CENTSEC;
	protected long						lMaxClickTime			= MAX_CLICK_TIME_IN_CENTSEC;
	
	
	
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
		theStack = new PixelsStack(1024, 1024);
		nLongClick = 0x0;
	}
	
	//--------------------------------------------------------------------------
	// Load a new Image
	// @imageName 	theNewfilename with the path from the resource to load the image example "gfx/monster01.png"
	//
	public void loadImage(String imageName) {
		theDecoratedTexture.loadImage(imageName);
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
				Log.i(TAG," Coloring sprite onAreaTouched ACTION_UP time [" + lCurrentTimeInCentSec + "]");
				long deltaTime = lCurrentTimeInCentSec - lTimeStartInCentSec;
				Log.i(TAG," Coloring sprite onAreaTouched ACTION_UP click time [" + deltaTime + "]");
				if((deltaTime >= lMinClickTime) & (deltaTime <= lMaxClickTime )){
					flood((int) pTouchAreaLocalX, (int)pTouchAreaLocalY);
					Log.i(TAG," Coloring sprite onAreaTouched ACTION_UP FLOOD");
				}
				lTimeStartInCentSec = 0;
				break;
			case TouchEvent.ACTION_DOWN:
				lTimeStartInCentSec = System.nanoTime()/NANOSEC_PER_CENTSEC;
				Log.i(TAG," Coloring sprite onAreaTouched ACTION_DOWN time [" + lTimeStartInCentSec + "]");
				break; 
			case TouchEvent.ACTION_MOVE:
				break;
			case TouchEvent.ACTION_OUTSIDE:
			case TouchEvent.INVALID_POINTER_ID:
			case TouchEvent.ACTION_CANCEL:
				lTimeStartInCentSec = 0;
				nLongClick = 0;
				Log.i(TAG," Coloring sprite onAreaTouched ACTION_OTHERS nLongClick= " + nLongClick);
				break;
		}
		return false;
	}
	// --------------------------------------------------------------------------------
	// Color flood algorithm
	// --------------------------------------------------------------------------------		
	public void flood(int X, int Y){
		int[] pixelsCopy = theDecoratedTexture.getPixelsCopy();
		int OldColor = pixelsCopy[convertXYtoID(X,Y)];
		floodFillScanlineStack(pixelsCopy,X, Y, colorFill, OldColor);
		theDecoratedTexture.setPixelsFromCopy(pixelsCopy);
		theDecoratedTexture.reloadBitmap();
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
	        
	        while((y1 >= 0) && (screenBuffer[convertXYtoID(x,y1)] == oldColor)) 
	        	y1--;
	        
	        y1++;
	        
	        spanLeft = spanRight = false;
	        
	        while(y1 <= theStack.height && screenBuffer[convertXYtoID(x,y1)] == oldColor )
	        {
	        	screenBuffer[convertXYtoID(x,y1)] = newColor;
	        	
	        	if(!spanLeft && x > 0 && screenBuffer[convertXYtoID(x-1,y1)] == oldColor) 
	        	{
	        		if(!theStack.push(convertXYtoID(x-1,y1))) return;
	        		spanLeft = true;
	        	}
	        	else if(spanLeft && x > 0 && screenBuffer[convertXYtoID(x-1,y1)] != oldColor)
	        	{
	        		spanLeft = false;
	        	}
	        	if(!spanRight && x < (theStack.width - 1) && screenBuffer[convertXYtoID(x+1,y1)] == oldColor) 
	        	{
	        		if(!theStack.push(convertXYtoID(x+1,y1))) return;
	        		spanRight = true;
	        	}
	        	else if(spanRight && x < (theStack.width - 1) && screenBuffer[convertXYtoID(x+1,y1)] != oldColor)
	        	{
	        		spanRight = false;
	        	} 
	        	y1++;
	        }
	    }
	}

	@Override
	public void build(BasicDescriptor pDsc) {
		// TODO Auto-generated method stub
		
	}
}
