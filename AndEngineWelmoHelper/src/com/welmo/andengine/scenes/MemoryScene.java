package com.welmo.andengine.scenes;

import java.util.ArrayList;
import java.util.List;

import org.andengine.entity.IEntity;
import org.andengine.entity.shape.IAreaShape;

import com.welmo.andengine.scenes.components.CardSprite;
import com.welmo.andengine.scenes.components.ComponentDefaultEventHandler;
import com.welmo.andengine.scenes.descriptors.components.BasicDescriptor;
import com.welmo.andengine.scenes.descriptors.components.SceneDescriptor;
import com.welmo.andengine.scenes.descriptors.components.SpriteObjectDescriptor;
import com.welmo.andengine.scenes.descriptors.events.ComponentEventHandlerDescriptor;

import android.util.Log;

public class MemoryScene extends ManageableScene {

	enum DifficultyLevel{
		EASY,MEDIUM,DIFFICULT,HARD
	}			
	// ===========================================================
	// Constants
	// ===========================================================
	//Log & Debug
	private static final String 			TAG = "MemoryScene";
	//Value of difficulties (are row index in memoryeStructure}
	public static final int 				EASY 		= 0;
	public static final int 				MEDIUM 		= 1;
	public static final int 				DIFFICULT 	= 2;
	public static final int 				HARD 		= 3;
	//Value of parameter (are column index in memoryeStructure}
	public static final int 				DIFFICULTY 	= 0;
	public static final int 				WIDTH 		= 2;
	public static final int 				HEIGHT 		= 1;
	public static final int 				NB_SYMBOLS 	= 3;
	//Structure of memory grid
	protected static final int[][]			memoryStructure = {{0,3,4,6},
		{1,4,8,16},{2,5,8,20},{3,6,10,30}};
	protected int							nMemoryLevel = MEDIUM;
	protected int							nMaxLevelAllowed = 3;
	protected static final  int				MAX_NB_OF_SYMBOLS = 30;
	protected ArrayList<Integer>			memoryDeck = null;
	protected ArrayList<Integer>			alSymbolList = null;
	//symbol geometry in pixel
	protected int							nSymbolWith 		= 10;
	protected int							nSymbolHeight 		= 10;
	protected int							nTopBottomBorder 	= 40;
	protected int							nLeftRightBorder 	= 20;
	protected int							nVIntraBorder 		= 10;
	protected int							nHIntraBorder 		= 10;
	protected int							nStdCardHeight 		= 175;
	protected int							nStdCardWidth 		= 125;
	protected int							nCardWidth			= 0;
	protected int							nCardHeight			= 0;
	
	//Card ArrayList
	protected ArrayList<IEntity>			allCards;
	
	// ===========================================================
	// Fields
	// ===========================================================

	//===========================================================
	// Constructors
	// ===========================================================
	public MemoryScene(){
		Log.i(TAG,"Default Constructor");
		//create the symbol list
		allCards		= new ArrayList<IEntity>();
		alSymbolList 	= new ArrayList<Integer>();
		memoryDeck 		= new ArrayList<Integer>();
		for(int i = 0; i < MAX_NB_OF_SYMBOLS; i++)
		{
			alSymbolList.add(i);
		}
		InitCardDeck();
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================
	// ===========================================================
	// private & protected member function
	// ===========================================================
	protected void InitCardDeck(){
		Log.i(TAG,"InitCardDeck");
		//shuffle the symbols to be used
		java.util.Collections.shuffle(alSymbolList);
		memoryDeck.clear();
		//select only the first N symbols depending on the NB used by the level & add it to the card deck
		//symbols are added two times
		for (int i = 0; i < memoryStructure[nMemoryLevel][NB_SYMBOLS];  i++){
			memoryDeck.add(alSymbolList.get(i));
			memoryDeck.add(alSymbolList.get(i)+ MAX_NB_OF_SYMBOLS);
		}
		//shuffle the memory deck
		java.util.Collections.shuffle(memoryDeck);
		java.util.Collections.shuffle(memoryDeck);
		java.util.Collections.shuffle(memoryDeck);
		String strDebug = new String();
		for (int i=0; i < memoryDeck.size(); i++)
			strDebug =  strDebug +", " + memoryDeck.get(i);
		Log.i(TAG,"InitCardDeck = " + strDebug);
	}
	protected void RestartGame(){
		InitCardDeck();
		//set all cards as not visible
		for (int i = 0; i < MAX_NB_OF_SYMBOLS; i++){
			((CardSprite)allCards.get(i)).setVisible(false);
			((CardSprite)allCards.get(i+30)).setVisible(false);
		}

		//set cards in current shuffle visible
		for (int i=0; i < memoryDeck.size(); i++)
			((CardSprite)allCards.get(memoryDeck.get(i))).setVisible(true);
		
		int nRow = memoryStructure[nMemoryLevel][HEIGHT];
		int nCol = memoryStructure[nMemoryLevel][WIDTH];
		
		int px = 0;
		int py = nTopBottomBorder;
		
		for (int i=0; i < nRow; i++){
			px = nLeftRightBorder;
			for (int j=0; j < nCol; j++){
				((CardSprite)allCards.get(memoryDeck.get(i*nCol+j))).setX(px);
				((CardSprite)allCards.get(memoryDeck.get(i*nCol+j))).setY(py);
				px += nCardWidth + nVIntraBorder;
			}
			py += nCardHeight + nHIntraBorder;
		}
	}
	public void resetGeometry(){
		//get camera dimension
		float camera_height = this.mEngine.getCamera().getHeight();
		float camera_width = this.mEngine.getCamera().getWidth();
		
		//calculate maximal width & height allowed by the geometry
		float cardMaxWidth = ((camera_width - 2*nLeftRightBorder
				- nVIntraBorder*(memoryStructure[nMemoryLevel][WIDTH]-1) )/memoryStructure[nMemoryLevel][WIDTH]);
		float cardMaxHeight = ((camera_height - 2*nTopBottomBorder 
				- nHIntraBorder*(memoryStructure[nMemoryLevel][HEIGHT]-1))/memoryStructure[nMemoryLevel][HEIGHT]);

		Log.i(TAG,"Geometry cardMaxWidth, cardMaxHeight = " + cardMaxWidth + " " + cardMaxHeight );
		
		//Calculate minimal reduction ration
		float scaleH = cardMaxHeight/nStdCardHeight;
		float scaleW = cardMaxWidth/nStdCardWidth;
		float ratio = Math.min(scaleW,scaleH);
		
		Log.i(TAG,"Geometry scale W, scale H, ration = " + scaleH + " " + scaleW + " " + ratio);
		
		nCardHeight = (int)(nStdCardHeight*ratio);
		nCardWidth = (int)(nStdCardWidth*ratio);
		
		Log.i(TAG,"Cards Height & width = " + nCardHeight + " " + nCardWidth);
		
	}
	
			
	// ===========================================================
	// Interfaces & Superclass
	// ===========================================================	
	@Override
	public void loadScene(SceneDescriptor sceneDescriptor) {
		//super.loadScene(sceneDescriptor);
		
		//nMemoryLevel = sceneDescriptor.getMemodyLevel();
		
		pSCDescriptor = sceneDescriptor;

		//reset geometry
		resetGeometry();
		
		for (ComponentEventHandlerDescriptor ehDsc:pSCDescriptor.pGlobalEventHandlerList){
			ComponentDefaultEventHandler newEventHandler= new ComponentDefaultEventHandler();
			newEventHandler.setUpEventsHandler(ehDsc);
			this.hmEventHandlers.put(ehDsc.getID(), newEventHandler);
		}

		//Debug
		//int count=0;
	
		for(BasicDescriptor scObjDsc:pSCDescriptor.pChild){
			
			//reset dimension to fit memory layout
			if(scObjDsc instanceof SpriteObjectDescriptor){
				((SpriteObjectDescriptor) scObjDsc).getIDimension().setHeight(nCardHeight);
				((SpriteObjectDescriptor) scObjDsc).getIDimension().setWidth(nCardWidth);
			}
		
			IEntity newEntity = loadComponent(scObjDsc, this);
			if(newEntity instanceof CardSprite){
				allCards.add(newEntity);
				//((CardSprite)newEntity).setScaleCenter(0, 0);
				//this.unregisterTouchArea((IAreaShape)newEntity);
				//count++;
			}
		}
		//Log.i(TAG,"Nb of Cards = " + count);
		
		//Init simbot of cards
		for (int i = 0; i < MAX_NB_OF_SYMBOLS; i++){
			((CardSprite)allCards.get(i)).setSidesTiles(i, 30);
			((CardSprite)allCards.get(i+30)).setSidesTiles(i, 30);
		}

		RestartGame();
		
		//Enable audio option
		mEngine.getEngineOptions().getAudioOptions().setNeedsMusic(true);
		mEngine.getEngineOptions().getAudioOptions().setNeedsSound(true);
	}
	// ===========================================================		
	// ===========================================================
	// Public Methods
	// ===========================================================
	public void changeDifficulty(MemoryScene.DifficultyLevel newLevel){
		switch(newLevel){
		case EASY: 
			nMemoryLevel = EASY; break;
		case MEDIUM:
			nMemoryLevel = MEDIUM;
			break;
		case DIFFICULT:
			nMemoryLevel = DIFFICULT;
			break;
		case HARD:
			nMemoryLevel = HARD;
			break;
		default:
			nMemoryLevel = EASY;
			break;
		}
		resetGeometry();
		//FT reLoadSceneGame();
		RestartGame();
	}
}
