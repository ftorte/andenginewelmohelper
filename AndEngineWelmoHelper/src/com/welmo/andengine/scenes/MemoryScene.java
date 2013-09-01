package com.welmo.andengine.scenes;

import java.util.ArrayList;
import java.util.HashMap;
import org.andengine.entity.IEntity;
import org.andengine.entity.shape.IAreaShape;
import org.andengine.input.touch.TouchEvent;

import com.welmo.andengine.managers.ResourcesManager;
import com.welmo.andengine.scenes.components.CardSprite;
import com.welmo.andengine.scenes.components.ComponentDefaultEventHandler;
import com.welmo.andengine.scenes.components.CardSprite.CardSide;
import com.welmo.andengine.scenes.components.IClickable;
import com.welmo.andengine.scenes.descriptors.MemorySceneDescriptor;
import com.welmo.andengine.scenes.descriptors.SceneDescriptor;
import com.welmo.andengine.scenes.descriptors.components.BasicDescriptor;
import com.welmo.andengine.scenes.descriptors.components.GameLevel;
import com.welmo.andengine.scenes.descriptors.components.SpriteObjectDescriptor;
import com.welmo.andengine.scenes.descriptors.events.ComponentEventHandlerDescriptor;
import com.welmo.andengine.scenes.descriptors.events.ComponentEventHandlerDescriptor.Events;
import com.welmo.andengine.scenes.descriptors.events.SceneActions.ActionType;
import com.welmo.andengine.utility.ICallBack;
import com.welmo.andengine.utility.SoundSequence;
import com.welmo.andengine.utility.SoundTaskPoolSequence;

import android.util.Log;

public class MemoryScene extends ManageableScene {
		
	// ===========================================================
	// Constants
	// ===========================================================
	//Log & Debug
	private static final String 			TAG = "MemoryScene";
		
	// ===========================================================
	// Variables
	// ===========================================================
	// Game configuration
	// -----------------------------------------------------------
	// Structure of memory grid (default)
	protected int[][]						memoryStructure = {{0,3,5,7},{1,3,6,8},{2,5,8,18},{3,6,10,30}};
	// -----------------------------------------------------------
	// Card Deck characteristic
	protected GameLevel						nMaxLevelAllowed 	= GameLevel.EASY;
	protected int							nMaxNbOfSymbols 	= 30;
	protected int							nTopBottomBorder 	= 40;
	protected int							nLeftRightBorder 	= 40;
	protected int							nVIntraBorder 		= 10;
	protected int							nHIntraBorder 		= 10;
	protected int							nStdCardHeight 		= 175;
	protected int							nStdCardWidth 		= 125;
	
	// ===========================================================
	// Dinamic variables
	// Manage Cards
	protected int							nNbOfCardsLastLine;
	protected ArrayList<Integer>			memoryDeck 			= null;		//contains the current deck of cards
	protected ArrayList<Integer>			alSymbolIDList		= null;		//contains all available symbols to buld the deck
	protected HashMap<Integer,IEntity>		allCards;					//Map of Cards 
	
	//Table geometry in pixel
	protected int							nNbOfCards;	
	protected GameLevel						nMemoryLevel 		= GameLevel.EASY;
	protected int							nDeltaBorderX		= 0;
	protected int							nDeltaBorderY		= 0;
	//Card Dimension
	protected int							nCardWidth			= 0;
	protected int							nCardHeight			= 0;
	protected float							fFlipTime			= MemorySceneDescriptor.DEFAULT_FLIP_TIME;
	protected float							fWaitBackFlip		= MemorySceneDescriptor.DEFAULT_FLIP_TIME;
	
	
	//GameStatus
	protected int 							nStatus					= STATUS_NOSELCTION;
	public static final int 				STATUS_NOSELCTION		= 1;
	public static final int 				STATUS_ONE_CARD_SELECTED= 2;
	public static final int 				STATUS_TWO_CARD_SELECTED= 3;
	
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
		allCards		= new HashMap<Integer,IEntity>();
		alSymbolIDList 	= new ArrayList<Integer>();
		memoryDeck 		= new ArrayList<Integer>();
		nNbOfCards	= 0;
		nMaxNbOfSymbols = 0;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================
	// ===========================================================
	// private & protected member function
	// ===========================================================
	//
	public void configureMemory(MemorySceneDescriptor pdsc){
		nMaxLevelAllowed 	= pdsc.getMaxLevelAllowed();
		nTopBottomBorder 	= pdsc.getTopBottomBorder();
		nLeftRightBorder 	= pdsc.getLeftRightBorder();
		nVIntraBorder 		= pdsc.getVIntraBorder();
		nHIntraBorder 		= pdsc.getHIntraBorder();
		nStdCardHeight 		= pdsc.getStdCardHeight();
		nStdCardWidth 		= pdsc.getStdCardWidth();
		nMaxNbOfSymbols     = pdsc.getMaxNbOfSymbols();
		memoryStructure 	= pdsc.getMemoryStructure();
		fFlipTime			= pdsc.getFlipTime();
		fWaitBackFlip		= pdsc.getWaitBackFlip();
	}
	
	protected void ResetSymbolList(){
		alSymbolIDList.clear();
		for(int i = 0; i < nMaxNbOfSymbols; i++)
		{
			alSymbolIDList.add(i);
		}
	}
	/**
	 * InitCardDeck: 	create a memory deck containing 2 equal card per available symbol. 
	 * 					Shuffle the memory deck
	 * 
	 */
	protected void InitCardDeck(){
		Log.i(TAG,"InitCardDeck");
		
		//shuffle the symbols to be used
		java.util.Collections.shuffle(alSymbolIDList);
		
		//select only the first N symbols depending on game level & add it to the card deck
		//symbols are added two times to take into account that there are two cards
		memoryDeck.clear();
		for (int i = 0; i < memoryStructure[nMemoryLevel.ordinal()][MemorySceneDescriptor.NB_SYMBOLS];  i++){
			memoryDeck.add(alSymbolIDList.get(i));					//Create element
			memoryDeck.add(alSymbolIDList.get(i)+ nMaxNbOfSymbols); //Create the element copy
		}
		
		//shuffle the memory deck
		java.util.Collections.shuffle(memoryDeck);
		
		//reset the nb of cards
		nNbOfCards = 2*memoryStructure[nMemoryLevel.ordinal()][MemorySceneDescriptor.NB_SYMBOLS];
		nNbOfCardsLastLine = nNbOfCards - ((memoryStructure[nMemoryLevel.ordinal()][MemorySceneDescriptor.HEIGHT]-1)*memoryStructure[nMemoryLevel.ordinal()][MemorySceneDescriptor.WIDTH]);
		
	}
	/**
	 * Reset the came using the same Game level and same symbol list
	 */
	protected void RestartGame(){
		Log.i(TAG,"RestartGame");
		
		//reset game status
		nStatus	= STATUS_NOSELCTION;
		nLockTouch = 0;
		
		//set all cards as not visible and unregister all area touch
		for (int i = 0; i < 2*nMaxNbOfSymbols; i++){
			((CardSprite)allCards.get(i)).setVisible(false);
			this.unregisterTouchArea((IAreaShape)allCards.get(i));
		}
		
		//shuffle card and create a new deck of cards
		InitCardDeck();
		
		//  set cards in current deck visible
		//  register touch area & ensure orientation & side are 180° side B
		for (int i=0; i < memoryDeck.size(); i++){
			CardSprite tmpCard = (CardSprite)allCards.get(memoryDeck.get(i));
			tmpCard.setVisible(true);
			tmpCard.setSideB();
			this.registerTouchArea(tmpCard);
		}
		
		//Set position of cards in the screen
		int nRow = memoryStructure[nMemoryLevel.ordinal()][MemorySceneDescriptor.HEIGHT];
		int nCol = memoryStructure[nMemoryLevel.ordinal()][MemorySceneDescriptor.WIDTH];
		
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
				- nVIntraBorder*(memoryStructure[nMemoryLevel.ordinal()][MemorySceneDescriptor.WIDTH]-1) )/memoryStructure[nMemoryLevel.ordinal()][MemorySceneDescriptor.WIDTH]);
		float cardMaxHeight = ((camera_height - 2*nTopBottomBorder 
				- nHIntraBorder*(memoryStructure[nMemoryLevel.ordinal()][MemorySceneDescriptor.HEIGHT]-1))/memoryStructure[nMemoryLevel.ordinal()][MemorySceneDescriptor.HEIGHT]);

		Log.i(TAG,"Geometry cardMaxWidth, cardMaxHeight = " + cardMaxWidth + " " + cardMaxHeight );
		
		//Calculate minimal reduction ratio
		float scaleH = cardMaxHeight/nStdCardHeight;
		float scaleW = cardMaxWidth/nStdCardWidth;
		float ratio = Math.min(scaleW,scaleH);
		
		Log.i(TAG,"Geometry scale W, scale H, ration = " + scaleH + " " + scaleW + " " + ratio);
		
		//calculate new card dimensions
		nCardHeight = (int)(nStdCardHeight*ratio);
		nCardWidth = (int)(nStdCardWidth*ratio);
		
		Log.i(TAG,"Cards Height & width = " + nCardHeight + " " + nCardWidth);
		
		//adjust border
		nDeltaBorderX = (int)((camera_width - nLeftRightBorder*2  - nCardWidth *memoryStructure[nMemoryLevel.ordinal()][MemorySceneDescriptor.WIDTH] 
				- (nVIntraBorder*(memoryStructure[nMemoryLevel.ordinal()][MemorySceneDescriptor.WIDTH] -1)))/(memoryStructure[nMemoryLevel.ordinal()][MemorySceneDescriptor.WIDTH]));
		
		nDeltaBorderY = (int)((camera_height - nTopBottomBorder*2 - nCardHeight*memoryStructure[nMemoryLevel.ordinal()][MemorySceneDescriptor.HEIGHT] 
				- (nHIntraBorder*(memoryStructure[nMemoryLevel.ordinal()][MemorySceneDescriptor.HEIGHT] -1)))/(memoryStructure[nMemoryLevel.ordinal()][MemorySceneDescriptor.HEIGHT]));
		
		Log.i(TAG,"Border Delta X & DealtY = " + nDeltaBorderX + " " + nDeltaBorderY);
		
	}	
	
	void InitializeEventHandler(){

		//initialize Scene Event Handlers
		for (ComponentEventHandlerDescriptor ehDsc:pSCDescriptor.pGlobalEventHandlerList){
			ComponentDefaultEventHandler newEventHandler= new ComponentDefaultEventHandler();
			newEventHandler.setUpEventsHandler(ehDsc);
			this.hmEventHandlers.put(ehDsc.getID(), newEventHandler);
		}
	}

	void InitializeCards(){

			for(BasicDescriptor scObjDsc:pSCDescriptor.pChild.values()){
				//reset dimension to fit memory layout
				if(scObjDsc instanceof SpriteObjectDescriptor){
					((SpriteObjectDescriptor) scObjDsc).getIDimension().setHeight(nCardHeight);
					((SpriteObjectDescriptor) scObjDsc).getIDimension().setWidth(nCardWidth);
				}
				IEntity newEntity = loadComponent(scObjDsc, this);
				if(newEntity instanceof CardSprite){
					allCards.put(((CardSprite) newEntity).getID(),newEntity);
					((CardSprite) newEntity).setSidesTiles(((SpriteObjectDescriptor) scObjDsc).getSidesA(),((SpriteObjectDescriptor) scObjDsc).getSidesB());	
				
				}
			}
			//TODO put code to check the if the allCads has less the 2*Number of max symbol exception
	}
			
	// ===========================================================
	// Interfaces & Superclass
	// ===========================================================
	@Override
	public void loadScene(SceneDescriptor sceneDescriptor) {
		
		configureMemory((MemorySceneDescriptor)sceneDescriptor);
		nMemoryLevel = sceneDescriptor.getGameLevel();
		pSCDescriptor = sceneDescriptor;
				
		//Clear the scene
		ClearScene();
		
		//Reset symbol list
		ResetSymbolList();
		
		//Init Card Deck
		InitCardDeck();
				
		//reset geometry
		ResetGeometry();
				
		//Create Event Handlers
		InitializeEventHandler();
		
		//Create All card sprites
		InitializeCards();
		
		//Restart the Game
		RestartGame();
	
		
		nStatus = this.STATUS_NOSELCTION;
		loadScenePhrases(pSCDescriptor);
	}
	@Override
	public void resetScene(){
		Log.i(TAG,"resetScene");
		super.resetScene();
		RestartGame();
		nStatus = this.STATUS_NOSELCTION;
	}
	public void resetScene(GameLevel newLevel){
		Log.i(TAG,"resetScene with new game level = " + newLevel);
		
		super.resetScene();
		this.pSCDescriptor.setGameLevel(newLevel); 		//change game level
		this.nMemoryLevel = pSCDescriptor.getGameLevel();
	
		//loadScene(pSCDescriptor);	

		//Clear the scene
		ClearScene();

		//Init Card Deck
		InitCardDeck();

		//reset geometry
		ResetGeometry();

		//Create Event Handlers
		InitializeEventHandler();

		//Create All card sprites
		InitializeCards();

		//Restart the Game
		RestartGame();
	}

	// ===========================================================
	// Interface IOnActionSceneLeastener
	@Override
	public void onFlipCard(int CardID,CardSide CurrentSide){
		switch(nStatus){
		case STATUS_NOSELCTION:
			if(CurrentSide == CardSide.A){
				Log.i(TAG,"00:CS = NO-S => Card: " + CardID + "Side A");
				nFistCard = CardID;												//register first card
				this.unregisterTouchArea((IAreaShape)allCards.get(CardID));		//make its touch disabled
				nStatus = STATUS_ONE_CARD_SELECTED;
				Log.i(TAG,"01:NS = ONE-S");
			}
			else
				Log.i(TAG,"02:CS = NO-S => Card: " + CardID + "Side B => do nothing");
			break;
		case STATUS_ONE_CARD_SELECTED:
			if(CardID != nFistCard){ 
				if(CurrentSide == CardSide.A){
					Log.i(TAG,"03:CS = ONE-S => Card: " + CardID + "Side A");
					nSecondCard = CardID;										//register first card
					this.unregisterTouchArea((IAreaShape)allCards.get(CardID)); //make its touch disabled
					CheckSelection();		
				}
				else
					Log.i(TAG,"04:CS = ONE-S => Card: " + CardID + "Side B => do nothing");
				//else do nothing since no new cards has been selected this should never happens
			}
			else{
				if(CurrentSide == CardSide.B){
					Log.i(TAG,"05:CS = ONE-S => Card: " + CardID + "Side B");
					nStatus = STATUS_NOSELCTION;
					Log.i(TAG,"06:NS = NO-S");
				//else do nothing since it means I re-selected the first card this should not happen
				}
				else
					Log.i(TAG,"05:CS = ONE-S => Card: " + CardID + "Side A => do nothing");	
			}
			break;
		case STATUS_TWO_CARD_SELECTED:
			break;
		}
	}
	@Override
	protected void ClearScene(){
		//clear scene
		super.ClearScene();
		//Clear all cards
		allCards.clear();
	}
	private void CheckSelection(){
		lockTouch();
		
		new Thread(new Runnable() {
			public void run() {
				try {
					Thread.sleep((long)fWaitBackFlip);
				} catch (InterruptedException e) {
					unLockTouch();
					e.printStackTrace();
				}
				if (Math.abs(nFistCard - nSecondCard) == nMaxNbOfSymbols)
					hideCards(Math.min(nFistCard,nSecondCard));
				else{
					((IClickable)allCards.get(nFistCard)).onFireEventAction(Events.ON_CLICK, ActionType.FLIP);
					registerTouchArea((IAreaShape)allCards.get(nFistCard));										//activate touch 
					((IClickable)allCards.get(nSecondCard)).onFireEventAction(Events.ON_CLICK, ActionType.FLIP);
					registerTouchArea((IAreaShape)allCards.get(nSecondCard));									//activate touch 
					Log.i(TAG,"10:CS = TWO-S => Card1: " + nFistCard + "Card2:  " + nSecondCard);
					unLockTouch();
					nStatus = STATUS_NOSELCTION;
					Log.i(TAG,"11:NS = NO-S");
				}
			}
		}).start();
	}
	protected void hideCards(int cardID) {
		((CardSprite)allCards.get(cardID)).setVisible(false);
		this.unregisterTouchArea((IAreaShape)allCards.get(cardID));
		((CardSprite)allCards.get(cardID + nMaxNbOfSymbols)).setVisible(false);
		this.unregisterTouchArea((IAreaShape)allCards.get(cardID +nMaxNbOfSymbols));
		
		//Build Sound sequence
		ResourcesManager rMgr = ResourcesManager.getInstance();
		SoundSequence sequence = this.mapOfPhrases.get("twocardmatch");
		if(sequence != null){
			sequence.getSequence()[sequence.getParameter(0)] = rMgr.getSound(((CardSprite)allCards.get(cardID)).getSoundName());
			sequence.getSequence()[sequence.getParameter(1)]  =rMgr.getSound(((CardSprite)allCards.get(cardID + nMaxNbOfSymbols)).getSoundName());

			SoundTaskPoolSequence playsequence = new SoundTaskPoolSequence();
			playsequence.setup(new ICallBack(){

				@Override
				public void onPreExecution(){
				}

				@Override
				public void onPostExecution(){
					unLockTouch();
					nStatus = STATUS_NOSELCTION;
				}},sequence.getSequence());
			playsequence.start();   
		}
	}
	// ===========================================================		
	// ===========================================================
	// Public Methods
	// ===========================================================
	public void changeDifficulty(GameLevel newLevel){
		switch(newLevel){
		case EASY: 
			nMemoryLevel = GameLevel.EASY; 
			break;
		case MEDIUM:
			nMemoryLevel = GameLevel.MEDIUM; 
			break;
		case DIFFICULT: 
			nMemoryLevel = GameLevel.DIFFICULT; 
			break;
		case HARD: 
			nMemoryLevel = GameLevel.HARD; 
			break;
		default: 
			nMemoryLevel = GameLevel.EASY; 
			break;
		}
	}
}
