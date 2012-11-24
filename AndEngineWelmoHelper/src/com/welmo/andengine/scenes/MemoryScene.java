package com.welmo.andengine.scenes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.andengine.engine.Engine.EngineLock;
import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;
import org.andengine.entity.shape.IAreaShape;
import org.andengine.input.touch.TouchEvent;
import org.andengine.ui.activity.BaseGameActivity;

import com.welmo.andengine.managers.ResourcesManager;
import com.welmo.andengine.scenes.components.CardSprite;
import com.welmo.andengine.scenes.components.ComponentDefaultEventHandler;
import com.welmo.andengine.scenes.components.IComponentEventHandler;
import com.welmo.andengine.scenes.components.CardSprite.CardSide;
import com.welmo.andengine.scenes.descriptors.components.BasicDescriptor;
import com.welmo.andengine.scenes.descriptors.components.GameLevel;
import com.welmo.andengine.scenes.descriptors.components.SceneDescriptor;
import com.welmo.andengine.scenes.descriptors.components.SpriteObjectDescriptor;
import com.welmo.andengine.scenes.descriptors.events.ComponentEventHandlerDescriptor;

import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;

public class MemoryScene extends ManageableScene {
		
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
	public static final int 				HEIGHT 		= 1;
	public static final int 				WIDTH 		= 2;
	public static final int 				NB_SYMBOLS 	= 3;
	//Memory limits
	protected static final  int				MAX_NB_OF_SYMBOLS = 30;
	
	
	// ===========================================================
	// Variables
	// ===========================================================
	// Game configuration
	// -----------------------------------------------------------
	// Structure of memory grid (default)
	protected static final int[][]			memoryStructure = {{0,3,5,7},
		{1,3,6,8},{2,5,8,18},{3,6,10,30}};
	// -----------------------------------------------------------
	// Card Deck characteristic
	protected int							nNbOfCards;	
	protected int							nMemoryLevel = MEDIUM;
	protected int							nMaxLevelAllowed = EASY;
	protected int							nTopBottomBorder 	= 40;
	protected int							nLeftRightBorder 	= 40;
	protected int							nVIntraBorder 		= 10;
	protected int							nHIntraBorder 		= 10;
	protected int							nStdCardHeight 		= 175;
	protected int							nStdCardWidth 		= 125;
	
	// ===========================================================
	// Dinamic variables
	protected int							nNbOfCardsLastLine;
	protected ArrayList<Integer>			memoryDeck = null;
	protected ArrayList<Integer>			alSymbolIDList = null;
	//Table geometry in pixel
	protected int							nDeltaBorderX		= 0;
	protected int							nDeltaBorderY		= 0;
	//Card Dimension
	protected int							nCardWidth			= 0;
	protected int							nCardHeight			= 0;
	
	//Card ArrayList
	protected ArrayList<IEntity>			allCards;
	
	//GameStatus
	protected int 							nStatus					= STAUS_NOSELCTION;
	public static final int 				STAUS_NOSELCTION		= 1;
	public static final int 				STAUS_ONE_CARD_SELECTED	= 2;
	public static final int 				STAUS_TWO_CARD_SELECTED	= 3;
	protected boolean 						disableEvent			= false;
	
	protected int							nFistCard			= -1; // if no card is selected
	protected int							nSecondCard			= -1;
	
	// ===========================================================
	// Fields
	// ===========================================================

	//===========================================================
	// Constructors
	// ===========================================================
	public MemoryScene(){
		super();
		Log.i(TAG,"Default Constructor");
		//create the symbol list
		allCards		= new ArrayList<IEntity>();
		alSymbolIDList 	= new ArrayList<Integer>();
		memoryDeck 		= new ArrayList<Integer>();
		nNbOfCards	= 0;
		for(int i = 0; i < MAX_NB_OF_SYMBOLS; i++)
		{
			alSymbolIDList.add(i);
		}
		InitCardDeck();
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================
	// ===========================================================
	// private & protected member function
	// ===========================================================
	//
	/**
	 * InitCardDeck: 	create a memory deck containing 2 equal card per available symbol. 
	 * 					Shuffle the memory deck
	 * 
	 */
	protected void InitCardDeck(){
		Log.i(TAG,"InitCardDeck");
		//shuffle the symbols to be used
		java.util.Collections.shuffle(alSymbolIDList);
		memoryDeck.clear();
		//select only the first N symbols depending on memory level & add it to the card deck
		//symbols are added two times
		for (int i = 0; i < memoryStructure[nMemoryLevel][NB_SYMBOLS];  i++){
			memoryDeck.add(alSymbolIDList.get(i));
			memoryDeck.add(alSymbolIDList.get(i)+ MAX_NB_OF_SYMBOLS);
		}
		nNbOfCards = 2*memoryStructure[nMemoryLevel][NB_SYMBOLS];
		nNbOfCardsLastLine = nNbOfCards - ((memoryStructure[nMemoryLevel][HEIGHT]-1)
				*memoryStructure[nMemoryLevel][WIDTH]);
		
		//shuffle the memory deck
		java.util.Collections.shuffle(memoryDeck);
			
	}
	protected void RestartGame(){
		Log.i(TAG,"RestartGame");
		InitCardDeck();
		
		//set all cards as not visible and unregister area touch
		for (int i = 0; i < 2*MAX_NB_OF_SYMBOLS; i++){
			((CardSprite)allCards.get(i)).setVisible(false);
			this.unregisterTouchArea((IAreaShape)allCards.get(i));
			//((CardSprite)allCards.get(i+MAX_NB_OF_SYMBOLS)).setVisible(false);
			//this.unregisterTouchArea((IAreaShape)allCards.get(i+MAX_NB_OF_SYMBOLS));
		}
		
		//  set cards in current deck visible
		//  register touch area & ensure orientation & side are 180° side B
		for (int i=0; i < memoryDeck.size(); i++){
			CardSprite tmpCard = (CardSprite)allCards.get(memoryDeck.get(i));
			tmpCard.setVisible(true);
			tmpCard.setRotation(180);
			tmpCard.setSideB();
			this.registerTouchArea((IAreaShape)allCards.get(memoryDeck.get(i)));
		}
		
		int nRow = memoryStructure[nMemoryLevel][HEIGHT];
		int nCol = memoryStructure[nMemoryLevel][WIDTH];
		
		int px = 0;
		int py = nTopBottomBorder + nDeltaBorderY/2;
		
		Log.i(TAG,"py = " + py);
		
		//reset positions for all lines - 1
		for (int i=0; i < nRow-1; i++){
			px = nLeftRightBorder + nDeltaBorderX/2;
			for (int j=0; j < nCol; j++){
				((CardSprite)allCards.get(memoryDeck.get(i*nCol+j))).setX(px);
				((CardSprite)allCards.get(memoryDeck.get(i*nCol+j))).setY(py);
				px += nCardWidth + nVIntraBorder + nDeltaBorderX;
			}
			py += nCardHeight + nHIntraBorder + nDeltaBorderY;
		}
		//Manage last line
		// Calvulate Nb og card in last line
		px = nLeftRightBorder + nDeltaBorderX/2 +(nCardWidth + nVIntraBorder + nDeltaBorderX)/2* 
				(nCol - this.nNbOfCardsLastLine);
		
		Log.i(TAG,"Nb of cards last line " + nNbOfCardsLastLine);
		
		for (int j=0; j < nNbOfCardsLastLine;  j++){
			((CardSprite)allCards.get(memoryDeck.get((nRow-1)*nCol+j))).setX(px);
			((CardSprite)allCards.get(memoryDeck.get((nRow-1)*nCol+j))).setY(py);
			px += nCardWidth + nVIntraBorder + nDeltaBorderX;
		}
	}
	public void ResetGeometry(){
		//get camera dimension
		float camera_height = this.mEngine.getCamera().getHeight();
		float camera_width = this.mEngine.getCamera().getWidth();
		
		//calculate maximal width & height allowed by the geometry for a card
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
		
		//calculate new card dimensions
		nCardHeight = (int)(nStdCardHeight*ratio);
		nCardWidth = (int)(nStdCardWidth*ratio);
		
		Log.i(TAG,"Cards Height & width = " + nCardHeight + " " + nCardWidth);
		
		//adjust border
		nDeltaBorderX = (int)((camera_width - nLeftRightBorder*2  - nCardWidth *memoryStructure[nMemoryLevel][WIDTH] 
				- (nVIntraBorder*(memoryStructure[nMemoryLevel][WIDTH] -1)))/(memoryStructure[nMemoryLevel][WIDTH]));
		
		nDeltaBorderY = (int)((camera_height - nTopBottomBorder*2 - nCardHeight*memoryStructure[nMemoryLevel][HEIGHT] 
				- (nHIntraBorder*(memoryStructure[nMemoryLevel][HEIGHT] -1)))/(memoryStructure[nMemoryLevel][HEIGHT]));
		
		Log.i(TAG,"Border Delta X & DealtY = " + nDeltaBorderX + " " + nDeltaBorderY);
		
	}		
	// ===========================================================
	// Interfaces & Superclass
	// ===========================================================	
	@Override
	public void loadScene(SceneDescriptor sceneDescriptor) {
		
		nMemoryLevel = GameDifficultConvertInt(sceneDescriptor.getGameLevel());
		pSCDescriptor = sceneDescriptor;

		//clear scene
		this.clearTouchAreas();
		//Detach child cards
		for (int i=this.getChildCount() - 1;  i >= 0; i--){
			IEntity tmpEntity = this.getChildByIndex(i);
			if(tmpEntity != null)
				this.detachChild(tmpEntity);
		}
		
		this.hmEventHandlers.clear();
		allCards.clear();
		
		//reset geometry
		ResetGeometry();
		
		for (ComponentEventHandlerDescriptor ehDsc:pSCDescriptor.pGlobalEventHandlerList){
			ComponentDefaultEventHandler newEventHandler= new ComponentDefaultEventHandler();
			newEventHandler.setUpEventsHandler(ehDsc);
			this.hmEventHandlers.put(ehDsc.getID(), newEventHandler);
		}

		for(BasicDescriptor scObjDsc:pSCDescriptor.pChild){
			
			//reset dimension to fit memory layout
			if(scObjDsc instanceof SpriteObjectDescriptor){
				((SpriteObjectDescriptor) scObjDsc).getIDimension().setHeight(nCardHeight);
				((SpriteObjectDescriptor) scObjDsc).getIDimension().setWidth(nCardWidth);
			}
			IEntity newEntity = loadComponent(scObjDsc, this);
			if(newEntity instanceof CardSprite)
				allCards.add(newEntity);
		}
		
		//Init simbot of cards
		for (int i = 0; i < MAX_NB_OF_SYMBOLS; i++){
			((CardSprite)allCards.get(i)).setSidesTiles(i, MAX_NB_OF_SYMBOLS);
			((CardSprite)allCards.get(i)).setSideB();
			((CardSprite)allCards.get(i)).setID(i);
			((CardSprite)allCards.get(i+MAX_NB_OF_SYMBOLS)).setSidesTiles(i, MAX_NB_OF_SYMBOLS);
			((CardSprite)allCards.get(i+MAX_NB_OF_SYMBOLS)).setSideB();
			((CardSprite)allCards.get(i+MAX_NB_OF_SYMBOLS)).setID(i+MAX_NB_OF_SYMBOLS);
		}

		RestartGame();
		
		//Enable audio option
		mEngine.getEngineOptions().getAudioOptions().setNeedsMusic(true);
		mEngine.getEngineOptions().getAudioOptions().setNeedsSound(true);
	}
	@Override
	public void resetScene(){
		Log.i(TAG,"resetScene");
		RestartGame();
	}
	// ===========================================================
	// Interface IOnActionSceneLeastener
	@Override
	public void onFlipCard(int CardID, CardSide CardSide){
		switch(nStatus){
		case STAUS_NOSELCTION:
			Log.i(TAG,"Current status = STAUS_NOSELCTION selected card" + CardID);
			nStatus = STAUS_ONE_CARD_SELECTED;
			nFistCard = CardID;
			break;
		case STAUS_ONE_CARD_SELECTED:
			Log.i(TAG,"Current status = STAUS_ONE_CARD_SELECTED selected card" + CardID);
			if(CardID != nFistCard){
				nStatus = STAUS_NOSELCTION;
				nSecondCard = CardID;
				CheckSelection();
			}
			break;
		case STAUS_TWO_CARD_SELECTED:
			break;
		
		}
	}

	private void CheckSelection(){
		this.disableEvent = true;
		this.mActivity.runOnUiThread(new Runnable(){ 
			public void run(){
				try {
					Thread.sleep(3000);
					if (Math.abs(nFistCard - nSecondCard) == MAX_NB_OF_SYMBOLS)
						hideCards(Math.min(nFistCard,nSecondCard));
					else{
						((CardSprite)allCards.get(nFistCard)).setSideB();
						((CardSprite)allCards.get(nFistCard)).setRotation(180);
						((CardSprite)allCards.get(nSecondCard)).setSideB();
						((CardSprite)allCards.get(nSecondCard)).setRotation(180);
					}
				} 
				catch (InterruptedException e) {
					e.printStackTrace();
				}
				disableEvent = false; 
				nStatus = STAUS_NOSELCTION;
			}});
	}
	protected void hideCards(int cardID) {
		((CardSprite)allCards.get(cardID)).setVisible(false);
		this.unregisterTouchArea((IAreaShape)allCards.get(cardID));
		((CardSprite)allCards.get(cardID + MAX_NB_OF_SYMBOLS)).setVisible(false);
		this.unregisterTouchArea((IAreaShape)allCards.get(cardID +MAX_NB_OF_SYMBOLS));
	}

	@Override
	public boolean onSceneTouchEvent(TouchEvent pSceneTouchEvent) {
		// TODO Auto-generated method stub
		if(this.disableEvent){
			return false;
		}
		else
			return super.onSceneTouchEvent(pSceneTouchEvent);
		
	}
	// ===========================================================		
	// ===========================================================
	// Public Methods
	// ===========================================================
	public void changeDifficulty(GameLevel newLevel){
		switch(newLevel){
		case EASY: nMemoryLevel = EASY; break;
		case MEDIUM: nMemoryLevel = MEDIUM; break;
		case DIFFICULT: nMemoryLevel = DIFFICULT; break;
		case HARD: nMemoryLevel = HARD; break;
		default: nMemoryLevel = EASY; break;
		}
	}
	
	/**
	 * Convert value of game level to correct value and limot it to max difficulty allowed
	 * @param newLevel = the new level we want set up
	 * @return the value in int limited to max level allowed
	 */
	private int GameDifficultConvertInt(GameLevel newLevel){
		int newdifficulty = EASY;
		switch(newLevel){
		case EASY: newdifficulty = EASY; break;
		case MEDIUM:newdifficulty = MEDIUM; break;
		case DIFFICULT:newdifficulty = DIFFICULT; break;
		case HARD:newdifficulty = HARD; break;
		default:return EASY; 
		}
		if (newdifficulty > nMaxLevelAllowed)
			newdifficulty = nMaxLevelAllowed;
		return newdifficulty;
	}
	public void ReLoadScene(){
		loadScene(this.pSCDescriptor);
	}
	public void ReLoadGame(GameLevel newLevel){
		this.pSCDescriptor.setGameLevel(newLevel);
		loadScene(this.pSCDescriptor);
	}
}
