package com.welmo.andengine.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.Semaphore;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.andengine.audio.music.Music;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.SmoothCamera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.particle.SpriteParticleSystem;
import org.andengine.entity.particle.emitter.BaseParticleEmitter;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.util.FPSLogger;
import org.andengine.input.touch.TouchEvent;
import org.andengine.input.touch.controller.MultiTouch;
import org.andengine.input.touch.detector.PinchZoomDetector;
import org.andengine.input.touch.detector.ScrollDetector;
import org.andengine.input.touch.detector.SurfaceScrollDetector;
import org.andengine.input.touch.detector.PinchZoomDetector.IPinchZoomDetectorListener;
import org.andengine.input.touch.detector.ScrollDetector.IScrollDetectorListener;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.progress.IProgressListener;
import org.json.JSONException;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.widget.Toast;

import com.welmo.andengine.managers.ParticuleSystemManager;
import com.welmo.andengine.managers.ResourcesManager;
import com.welmo.andengine.managers.SceneManager;
import com.welmo.andengine.managers.SharedPreferenceManager;
import com.welmo.andengine.resources.descriptors.ParserXMLResourcesDescriptor;
import com.welmo.andengine.scenes.IConfigurableScene;
import com.welmo.andengine.scenes.IManageableScene;
import com.welmo.andengine.scenes.ManageableScene;
import com.welmo.andengine.scenes.components.interfaces.IActivitySceneListener;
import com.welmo.andengine.scenes.descriptors.ParserXMLSceneDescriptor;
import com.welmo.andengine.scenes.descriptors.events.ComponentEventHandlerDescriptor.Events;
import com.welmo.andengine.scenes.operations.IOperationHandler;
import com.welmo.andengine.scenes.operations.Operation;
import com.welmo.andengine.utility.AsyncResourcesScenesLoader;
import com.welmo.andengine.utility.IAsyncCallBack;
import com.welmo.andengine.utility.inappbilling.IabHelper;
import com.welmo.andengine.utility.inappbilling.IabResult;
import com.welmo.andengine.utility.inappbilling.Inventory;
import com.welmo.andengine.utility.inappbilling.Purchase;
import com.welmo.andengine.utility.inappbilling.PurchasingManager;
import com.welmo.andengine.utility.inappbilling.Security;
import com.welmo.andengine.utility.inappbilling.PurchasingManager.IAPurchasing;


public class SimpleWelmoActivity extends SimpleBaseGameActivity implements IActivitySceneListener, IOnSceneTouchListener, IScrollDetectorListener, IPinchZoomDetectorListener ,IOperationHandler , IAPurchasing{
	// ===========================================================
	// Constants
	// ===========================================================
	private static final String 			TAG = "SimpleWelmoActivity";
	private final boolean 					mDebug = false;		

	//default values
	final String 							FONTHBASEPATH 	= "font/";
	final String 							TEXTUREBASEPATH = "gfx/";
	
	final String							HAS_MUSIC		= "HasMusic";
	final String							MUSIC_ON		= "MusicON";
	final String							HAS_SOUND		= "HasSouns";
	final String							SOUND_ON		= "SoundON";
	final String							GAME_LEVEL		= "gameLevel";
	
	
	final String							GAME_MUSIC		="Startup";
	
		
	private enum RESOURCETYPE{
		TEXTURES, SOUNDS, MUSICS,TILEDTEXTURES,
	}
	// ===========================================================
	// ===========================================================
	// Fields
	// ===========================================================	
	BitmapTextureAtlas 						pTextureAtals;
	ITextureRegion							pTextureRegion;
	int 									nCameraWidth	= 800;
	int										nCameraHeight 	= 480;
	
	Display 								display			= null;
	
	// ===========================================================
	// Fields
	// ===========================================================
	protected SceneManager 					mSceneManager;
	protected ResourcesManager 				mResourceManager;
	protected SmoothCamera 					mSmoothCamera;
	protected HUDisplay						mHUD;
	
	private SurfaceScrollDetector 			mScrollDetector;
	private PinchZoomDetector 				mPinchZoomDetector;
	private IScrollDetectorListener   		mScrollListener;
	private IPinchZoomDetectorListener   	mPinchZoomlListener;
	
	protected IProgressListener 			progressDialog=null;
	protected int							progressDone=0;
	protected boolean						bIsOnBackgroundLoading=false;
	
	//field for the first scene displayed at the beginning
	protected Scene							mFirstScene=null;
	protected String						mFirstSceneName="";
	protected int							mFirstSceneDuration=0;
	
	//field for the thanks scene displayed after the first scene
	protected Scene							mThanksScene=null;
	protected String						mThanksSceneName="";
	protected int							mThanksSceneDuration=0;
	
	protected Scene							mLoadingScene=null;
	protected String						mLoadingSceneName="";
	protected Semaphore 					executeStartupSene;
	protected Semaphore 					executeStartup;
	
	
	//field for the main scene displayed after the thank scene
	protected String						mMainSceneName="";
	protected String[]						mStartResourceDscFile=null;
	protected String[]						mStartSceneDscFile=null;
	protected String[]						mParticuleSystemsDscFile=null;
	
	static protected Music					mMusic;
	
	//Preferences
	protected SharedPreferences				mPreferences;
	protected SharedPreferences.Editor		mPreferencesEditor;
	
	
	
	//field for the main scene displayed after the thank scene
	
	//field for the in app purchasing
	protected PurchasingManager				mPurchMgr 	= null;
	protected Inventory						mInventory 	= null;
    static final int 						RC_REQUEST 	= 10001;// (arbitrary) request code for the purchase flow
	
	protected String 						mTheBase64EncodedPublicKey = null;
	
	//field for particul system management
	protected ParticuleSystemManager pPartSystemMgr = ParticuleSystemManager.getInstance();
	
	
	// ===========================================================
	// Configures resources/scene file and lists
	// ===========================================================
	public void setStartResourceDscFiles(String strFileResouce){
		mStartResourceDscFile 		= new String[1];
		mStartResourceDscFile[0] 	= new String(strFileResouce);
	}
	public void setStartSceneDscFile(String strFileScenes){
		mStartSceneDscFile 		= new String[1];
		mStartSceneDscFile[0] 	= new String(strFileScenes);
	}
	public void setMainSceneName(String strMainScene){
		mMainSceneName = new String(strMainScene);
	}
	public void setFirstSceneName(String firstScene){
		setFirstSceneName(firstScene,0);
	}
	//
	public void setFirstSceneName(String firstScene,int duration){
		this.mFirstSceneName 		= new String(firstScene);
		mFirstSceneDuration = 0;
		if(duration> 0) 
			mFirstSceneDuration=duration;
	}
	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================
	public Camera getCamera() {
		return this.mSmoothCamera;
	}
	public int getCameraWidth() {
		return nCameraWidth;
	}
	public void setCameraWidth(int nCameraWidth) {
		this.nCameraWidth = nCameraWidth;
	}
	public int getCameraHeight() {
		return nCameraHeight;
	}
	public void setCameraHeight(int nCameraHeight) {
		this.nCameraHeight = nCameraHeight;
	}
	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================


	@Override
	public EngineOptions onCreateEngineOptions() {
		
		//this.mCamera = new Camera(0, 0, nCameraWidth, nCameraHeight);
		this.mSmoothCamera = new SmoothCamera(0, 0, nCameraWidth, nCameraHeight, 1000, 1000, 1.0f);

		
		EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, 
				new RatioResolutionPolicy(nCameraWidth, nCameraHeight), mSmoothCamera);

		//enable multitouch
		if(mDebug){
			if(MultiTouch.isSupported(this)) {
				if(MultiTouch.isSupportedDistinct(this)) {
					gameToast("MultiTouch detected --> Both controls will work properly!");
				} else {
					gameToast("MultiTouch detected, but your device has problems distinguishing between fingers.\n\nControls are placed at different vertical locations.");
				}
			} else {
				gameToast("Sorry your device does NOT support MultiTouch!\n\n(Falling back to SingleTouch.)\n\nControls are placed at different vertical locations.");
			}
		}

		//Enable Z depth to +- 1000px
		mSmoothCamera.setZClippingPlanes(-1000, 1000);
		//Enable audio option for both music and sound effects
		engineOptions.getAudioOptions().setNeedsMusic(true);
		engineOptions.getAudioOptions().setNeedsSound(true);
		
		//get display metric
		display = getWindowManager().getDefaultDisplay(); 
		DisplayMetrics dm = new DisplayMetrics();
		
		display.getMetrics(dm);
		
		Runtime rt = Runtime.getRuntime();
		long maxMemory = rt.maxMemory();
		
		//create smart list to manage pinch&zoom and scroll
		//mBKPITouchAreas = new SmartList<ITouchArea>();
		
		Log.v("onCreate", "maxMemory:" + Long.toString(maxMemory));
		
		if(mDebug)gameToast("maxMemory:" + Long.toString(maxMemory));
		
		//create the semaphore to block launch of main if startup scene is not shown
		executeStartupSene 	= new Semaphore(1, true);
		executeStartup		= new Semaphore(1, true);

		//Check if first installation and configure default option
		mPreferences = getSharedPreferences(SharedPreferenceManager.STDPreferences.GLOBAL_VARIABLES.name(),MODE_PRIVATE);
		mPreferencesEditor = mPreferences.edit();
		
		//if is new installation load preferences from the XML file
		if(mPreferences.getBoolean("newInstallation", true)){
			setUpDefaultPreferences(mPreferencesEditor);
			mPreferencesEditor.putBoolean("newInstallation", false);
			
		}
		
		//Manage music & sound option
		if(mPreferences.getBoolean(HAS_MUSIC,false)){
			engineOptions.getAudioOptions().setNeedsMusic(true);
		}
		else
			engineOptions.getAudioOptions().setNeedsMusic(false);
		
		if(mPreferences.getBoolean(HAS_SOUND,false)){
			engineOptions.getAudioOptions().setNeedsSound(true);
		}
		else
			engineOptions.getAudioOptions().setNeedsSound(false);
		
		
		//Mange in ap billing
		//get instance of purchasing manger
		if (mTheBase64EncodedPublicKey != null){
			mPurchMgr = new PurchasingManager(this,this);
			mPurchMgr.connectService(this, this.mTheBase64EncodedPublicKey);
		}
			
		//manage particule system call particule class
		pPartSystemMgr = ParticuleSystemManager.getInstance();
		
		//exist
		return engineOptions;
	}
	protected void setUpDefaultPreferences(SharedPreferences.Editor mPrefEdt){
		mPrefEdt.putBoolean(HAS_MUSIC, true);
		mPrefEdt.putBoolean(MUSIC_ON, true);
		mPrefEdt.putBoolean(HAS_SOUND, true);
		mPrefEdt.putBoolean(SOUND_ON, true);
		mPrefEdt.putInt(GAME_LEVEL, 0);
		mPrefEdt.commit();
	}

	@Override
	public void onCreateResources() {	
		//Initialize resource manger
		mResourceManager = ResourcesManager.getInstance();
		mResourceManager.init(this, this.mEngine);

		//read descriptors for the resources necessaries for the first scene, the thanks scene  
		readResourceDescriptions(mStartResourceDscFile);
		//read descriptors for the first scene and the thanks scene  
		readScenesDescriptions(mStartSceneDscFile);		
		//read particule Systems
		readParticuleSystemDescriptions(mParticuleSystemsDscFile);
		
	}

	public void gameToast(final String msg) {
		this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(SimpleWelmoActivity.this, msg, Toast.LENGTH_SHORT).show();
			}
		});
	}
		    
	@Override
	public Scene onCreateScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());
		
		//initialize scene manager
		//mSceneManager = new SceneManager(this,);
		//mSceneManager.init(this.getEngine(), this);
		mSceneManager = new SceneManager(this,this.getEngine(), this);
		
		//creat pinch & zoom detectors
		this.mScrollDetector = new SurfaceScrollDetector(this);
		this.mPinchZoomDetector = new PinchZoomDetector(this);
		
		
		//get the first scene
		if((mFirstScene = mSceneManager.getScene(mFirstSceneName)) == null){
			gameToast("Not Start Up Scene Created");
			this.mFirstScene = new Scene();
		}
		
		
		if(this.mPreferences.getBoolean(MUSIC_ON, false)){
			Music msc = mResourceManager.getMusic(GAME_MUSIC);
			msc.setVolume(10000);
			msc.setLooping(true);
			msc.play();
		}
		
		//get the thanks scene
		if(mThanksSceneName.length() > 0)
			if((mThanksScene = mSceneManager.getScene(mThanksSceneName))== null){
				gameToast("Wrong ThankScene Configuration");
			}

		if(mLoadingSceneName.length() > 0){
			this.mLoadingScene = mSceneManager.getScene(mLoadingSceneName);
			if(mLoadingScene instanceof IProgressListener)
				progressDialog = (IProgressListener)mLoadingScene;
		}
		
		lauchResourceSceneBackGroundLoading();
		
		new Thread(new Runnable() {
			public void run(){
				try {
					//acquire a semaphore to block other tasks
					executeStartupSene.acquire();
					executeStartup.acquire();
					
					if(mFirstSceneDuration > 0) 
						Thread.sleep(mFirstSceneDuration);
					
					if(mThanksScene !=  null){
						mEngine.setScene(mThanksScene);
						if(mLoadingScene instanceof ManageableScene)
							 ((ManageableScene)mLoadingScene).onFireEvent(Events.ON_SCENE_LAUNCH);
						Thread.sleep(mThanksSceneDuration);
					}
					if(mLoadingScene != null){
						mEngine.setScene(mLoadingScene);
						if(mLoadingScene instanceof IProgressListener)
							progressDialog = (IProgressListener)mLoadingScene;
						if(mLoadingScene instanceof ManageableScene)
							 ((ManageableScene)mLoadingScene).onFireEvent(Events.ON_SCENE_LAUNCH);

					}
					//release the semaphore to free execution of other tasks
					executeStartupSene.release();
					
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
		
		//Create the HUD
		mHUD = new HUDisplay(this.mEngine, nCameraWidth,nCameraHeight);
		mScrollListener = mHUD;
		mPinchZoomlListener = mHUD;
		
		return mFirstScene;
	}
	private void lauchResourceSceneBackGroundLoading(){
		IAsyncCallBack callback = new IAsyncCallBack() {
            @Override
            public void workToDo() {
            	bIsOnBackgroundLoading = true;
            	Log.i(TAG,"Load Resources Descriptors");
            	onLoadResourcesDescriptionsInBackGround(); 
            	Log.i(TAG,"Load Scene Descriptors");
            	onLoadScenesDescriptionsInBackGround();
        		Log.i(TAG,"Load Resources");
        		onLoadResourcesInBackGround();
        		Log.i(TAG,"Load Scenes");
        		onLoadScenesInBackGound();
        		bIsOnBackgroundLoading = false;
            }
            @Override
            public void onComplete() {
            	try {
            		executeStartupSene.acquire();
            		Log.i(TAG,"Activate 1st Scene");
            		// FT mEngine.setScene((Scene)mSceneManager.getScene(mMainSceneName));
            		onChangeScene(mMainSceneName);
            		executeStartupSene.release();
            		executeStartup.release();
            	}
            	catch (InterruptedException e) {
            		e.printStackTrace();
            	}
            }
        };
        AsyncResourcesScenesLoader loadingTask = new AsyncResourcesScenesLoader();
        loadingTask.setupTaskToLoadResource(callback);
        loadingTask.start();
	}

	// ===========================================================
	// Methods must be overrides in child classes
	// ===========================================================
	protected void onLoadResourcesDescriptionsInBackGround() 
	{ 
		return;
	} 
	protected void onLoadScenesDescriptionsInBackGround() 
	{ 
		return;
	} 
	protected void onLoadScenesInBackGound(){
		return;
	}
	protected void onLoadResourcesInBackGround() {
		mResourceManager.EngineLoadResources(this.mEngine);
		Log.i(TAG,"Resources Loaded");
	}
	// ===========================================================
	// Final Methods 
	// ===========================================================
	protected final void readResourceDescriptions(String[] filesNames){
		try { 
			SAXParserFactory spf = SAXParserFactory.newInstance(); 
			SAXParser sp = spf.newSAXParser(); 
			XMLReader xr = sp.getXMLReader(); 
			
			//parse resources descriptions
			ParserXMLResourcesDescriptor resourceDescriptionHandler = new ParserXMLResourcesDescriptor(this); 
			xr.setContentHandler(resourceDescriptionHandler); 
			if(filesNames != null){
				for (String filename:filesNames ){
					Log.i(TAG,"Read Resource Description => " + filename);
					xr.parse(new InputSource(this.getAssets().open(filename))); 
				}
			}
			
		} catch(ParserConfigurationException pce) { 
			Log.e("SAX XML", "sax parse error", pce); 
		} catch(SAXException se) { 
			Log.e("SAX XML", "sax error", se); 
		} catch(IOException ioe) { 
			Log.e("SAX XML", "sax parse io error", ioe); 
		} 
	}
	protected final void readScenesDescriptions(String[] filesNames){
		try { 
			SAXParserFactory spf = SAXParserFactory.newInstance(); 
			SAXParser sp = spf.newSAXParser(); 
			XMLReader xr = sp.getXMLReader(); 
			//parse resources descriptions
			ParserXMLSceneDescriptor resourceDescriptionHandler = new ParserXMLSceneDescriptor(this); 
			xr.setContentHandler(resourceDescriptionHandler); 
			if(filesNames != null){
				for (String filename:filesNames ){
					Log.i(TAG,"Read Scene Description => " + filename);
					xr.parse(new InputSource(this.getAssets().open(filename))); 
				}
			}
		} catch(ParserConfigurationException pce) { 
			Log.e("SAX XML", "sax parse error", pce); 
		} catch(SAXException se) { 
			Log.e("SAX XML", "sax error", se); 
		} catch(IOException ioe) { 
			Log.e("SAX XML", "sax parse io error", ioe); 
		} 
	}
	protected final void readParticuleSystemDescriptions(String[] filesNames){
			if(pPartSystemMgr == null)
				pPartSystemMgr = ParticuleSystemManager.getInstance();
			else
					pPartSystemMgr.readParticuleSystemDescriptions(this , filesNames);
	}
	/*protected final void loadScenes(String[] Scenes,int pctWorkLoad) {
		for (String sceneName:Scenes ){
			mSceneManager.getScene(sceneName); 
		}
		Log.i(TAG,"Scenes Loaded");
	}*/
	protected final void loadResource(RESOURCETYPE type, String[] resources, int pctWorkLoad){
		//if list is null make the progress = to pctWorkLoad
		if(resources.length <= 0){
			if(pctWorkLoad>0)
				progressDone += pctWorkLoad;
			
			if(progressDialog != null)
				progressDialog.onProgressChanged(pctWorkLoad);
			
			return;
		}
		for (String resourceName:resources ){
			switch(type){
			case TEXTURES: 
				mResourceManager.getTextureRegion(resourceName); 
				break;
			case SOUNDS:
				mResourceManager.getSound(resourceName); 
				break;
			case MUSICS:
				mResourceManager.getMusic(resourceName);
				break;
			case TILEDTEXTURES:
				mResourceManager.getTiledTextureRegion(resourceName); 
				break;
			default:
				break;
			}
			progressDone += pctWorkLoad;
			if(progressDialog != null)
				progressDialog.onProgressChanged(progressDone);
		}
	}
	protected final void loadTextures(String[] textures, int pctWorkLoad) { 
		loadResource(RESOURCETYPE.TEXTURES, textures, pctWorkLoad);
	}
	protected final void loadTiledTextures(String[] tiledTextures, int pctWorkLoad) {
		loadResource(RESOURCETYPE.TILEDTEXTURES, tiledTextures, pctWorkLoad);
	}
	protected final void loadSounds(String[] sounds,int pctWorkLoad) {
		loadResource(RESOURCETYPE.SOUNDS, sounds, pctWorkLoad);
	}
	protected final void loadMusics(String[] musics,int pctWorkLoad) {
		loadResource(RESOURCETYPE.MUSICS, musics, pctWorkLoad);
	}
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
	
	@Override
	public void onBackPressed() {
		Scene currentScene = this.mEngine.getScene();
		try {
			executeStartup.acquire();
			if(currentScene.hasChildScene()){
				currentScene.clearChildScene();
			}
			else{
				if(currentScene instanceof IManageableScene){
					String fatherSceneName = ((IManageableScene)currentScene).getFatherScene();
					if(fatherSceneName.length() == 0)
						super.onBackPressed();
					else{
						this.mEngine.setScene((Scene)mSceneManager.getScene(fatherSceneName));
					}
				}
			}
			executeStartup.release();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	public synchronized final void onResumeGame(){
		if(!bIsOnBackgroundLoading){	
			super.onResumeGame();
			ResourcesManager mgr = ResourcesManager.getInstance();
			mgr.ResumeGame();
			onCustomResumeGame();
		}
		else
			super.onResumeGame();
	}
	public synchronized void onCustomResumeGame(){
		
	}
	@Override
	public synchronized void onPauseGame() {
		// TODO Auto-generated method stub
		super.onPauseGame();
		ResourcesManager mgr = ResourcesManager.getInstance();
		mgr.PauseGame();
	}
	//-----------------------------------------------------------------------------------------------------------------------------------------
	// Scroll detector listener
	//-----------------------------------------------------------------------------------------------------------------------------------------
	@Override
	public void onScrollStarted(final ScrollDetector pScollDetector, final int pPointerID, final float pDistanceX, final float pDistanceY) {
		mScrollListener.onScrollStarted(pScollDetector,pPointerID,pDistanceX,pDistanceY);
	}
	@Override
	public void onScroll(final ScrollDetector pScollDetector, final int pPointerID, final float pDistanceX, final float pDistanceY) {
		mScrollListener.onScroll(pScollDetector,pPointerID,pDistanceX,pDistanceY);
	}
	@Override
	public void onScrollFinished(final ScrollDetector pScollDetector, final int pPointerID, final float pDistanceX, final float pDistanceY) {
		mScrollListener.onScrollFinished(pScollDetector,pPointerID,pDistanceX,pDistanceY);
	}
	//-----------------------------------------------------------------------------------------------------------------------------------------
	// Pinch and zoom detector
	//-----------------------------------------------------------------------------------------------------------------------------------------	
	@Override
	public void onPinchZoomStarted(final PinchZoomDetector pPinchZoomDetector, final TouchEvent pTouchEvent) {
		if(mPinchZoomlListener!= null)
			mPinchZoomlListener.onPinchZoomStarted(pPinchZoomDetector,pTouchEvent);		
	}
	@Override
	public void onPinchZoom(final PinchZoomDetector pPinchZoomDetector, final TouchEvent pTouchEvent, final float pZoomFactor) {
		if(mPinchZoomlListener!= null)
			mPinchZoomlListener.onPinchZoom(pPinchZoomDetector,pTouchEvent,pZoomFactor);		
	}
	@Override
	public void onPinchZoomFinished(final PinchZoomDetector pPinchZoomDetector, final TouchEvent pTouchEvent, final float pZoomFactor) {
		if(mPinchZoomlListener!= null)
			mPinchZoomlListener.onPinchZoomFinished(pPinchZoomDetector,pTouchEvent,pZoomFactor);		
	}
	// ------------------------------------------------------------------------------
	// Override BaseGameActivity Methods
	// ------------------------------------------------------------------------------
	@Override
	public boolean onSceneTouchEvent(final Scene pScene, final TouchEvent pSceneTouchEvent) {
		Log.i(TAG,"onSceneTouchEvent");
		this.mPinchZoomDetector.onTouchEvent(pSceneTouchEvent);

		if(this.mPinchZoomDetector.isZooming()) {
			Log.i(TAG,"onSceneTouchEvent isZooming");
			this.mScrollDetector.setEnabled(false);
		} else {
			if(pSceneTouchEvent.isActionDown()) {
				Log.i(TAG,"onSceneTouchEvent isScroling");
				this.mScrollDetector.setEnabled(true);
			}
			this.mScrollDetector.onTouchEvent(pSceneTouchEvent);
		}
		return true;
	}
	// ------------------------------------------------------------------------------
	// Implement IActivitySceneListener detector
	// ------------------------------------------------------------------------------
	@Override
	public boolean onChangeScene(String nextScene) {
		ManageableScene psc = (ManageableScene) mSceneManager.getScene(nextScene);
		if(psc != null){
			//Cancel any zoom factor
			unZoom();
			// Cancel any scroll movements (position the camera center to the origin)
			this.mSmoothCamera.setCenterDirect(nCameraWidth/2, nCameraHeight/2);
			this.mSmoothCamera.setZoomFactorDirect(1);
			//reset the scene to initial state
			psc.resetScene();
			// configure the HUD if any
			if(psc.hasHUD()){
				mHUD.config(psc.getHUDDsc(),(psc instanceof IOperationHandler ? (IOperationHandler)psc : null),this.mResourceManager);
	        	mHUD.setVisible(true);
	        	mSmoothCamera.setHUD(mHUD);
	        }
	        else{	
	        	mHUD.setVisible(false);
	        	mSmoothCamera.setHUD(null);
	        }
			//Activate the pinch & zoom and the scroll if any
			if(psc.hasPinchAndZoomActive()){
				psc.setOnSceneTouchListener(this);
				psc.setTouchAreaBindingOnActionDownEnabled(true);
			}
			else{
				psc.setOnSceneTouchListener(null);
				psc.setTouchAreaBindingOnActionDownEnabled(false);
			}
			if(this.mPreferences.getBoolean(MUSIC_ON, false)){
				
				Music msc = mResourceManager.getMusic(GAME_MUSIC);
				msc.setVolume(10000);
				msc.setLooping(true);
				msc.play();
			}
			//set father message handler for messages that the scene don't handle
			psc.setFatherSceneMessageHandler(this);
			//FTO to complete psc.fireEvent(SCENELOADED);
			//add scene to the engine to be displayed
			psc.sortChildren(true);
			

			//Load the scene in the engine
			mEngine.setScene(psc);
			
			// fire events to be exectures when the scene is launched;
			psc.onFireEvent(Events.ON_SCENE_LAUNCH);
			return true;
		}
		else
			return false;
	}
	
	@Override
	public boolean onChangeChildScene(String nextScene) {
		Scene currentScene = this.mEngine.getScene();
		
		ManageableScene psc = (ManageableScene) mSceneManager.getScene(nextScene);
		if(psc != null){
			//set father message handler for messages that the scene don't handle
			psc.setFatherSceneMessageHandler(this);
			
			psc.sortChildren(true);
			currentScene.setChildScene(psc,false, true, true);
			psc.onFireEvent(Events.ON_SCENE_LAUNCH);
			return true;
		}
		else
			return false;
	}
	@Override
	public boolean onLaunchChildScene(String nextScene, ArrayList<String> parameters){
		ManageableScene psc = (ManageableScene) mSceneManager.getScene(nextScene);
		
		if(psc == null) return false;
		
		if(!(psc instanceof IConfigurableScene))return false;
		
		((IConfigurableScene)psc).setParameter(parameters);
		
		psc.sortChildren(true);
		
		this.mEngine.getScene().setChildScene(psc,false, true, true);
		psc.onFireEvent(Events.ON_SCENE_LAUNCH);
		
		return false;
		
	}
	@Override
	public void setIActivitySceneListener(IActivitySceneListener pListener) {
		// TODO Auto-generated method stub
	}
	@Override
	public void unZoom() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void doOperation(Operation msg) {
		switch(msg.type){
			case RESET_SCROLL_ZOOM:
				Log.i(TAG,"RESET_SCROLL");
				// Cancel any scroll movements (position the camera center to the origin)
				this.mSmoothCamera.setCenterDirect(nCameraWidth/2, nCameraHeight/2);
				this.mSmoothCamera.setZoomFactorDirect(1);
				break;
			case SET_MUSIC_ONOFF:
				boolean bMusicON = msg.getParameterBoolean(0).booleanValue();
				Editor edt = this.mPreferences.edit();
				edt.putBoolean(MUSIC_ON, bMusicON);
				edt.commit();
				Music msc = mResourceManager.getMusic(GAME_MUSIC);
				if(bMusicON){
					if(!msc.isPlaying()){
						msc.setVolume(10000);
						msc.setLooping(true);
						msc.play();
					}
				}
				else{
					if(msc.isPlaying())
						msc.pause();
				}
				break;
			case RESET_PERSITENCE:
				this.mPreferences.edit().clear();
				this.mPreferences.edit().commit();
				break;
			case FIRE_PARTICULE:
				//TODO
				/*String particuleEffectName = msg.getParameterString(0);
				float particuleEffectDuration = msg.getParametersNumber(1);
				//check that a particule system manager exist
				if(pPartSystemMgr == null)
					throw new NullPointerException("ParticuleSystem Not Initialized");
				
				final SpriteParticleSystem particuleSystem = pPartSystemMgr.getParticuleSystem(particuleEffectName);
				
				if(particuleSystem == null)
					throw new NullPointerException("Called a Particule system that doesen't exists [" + particuleEffectName + "]" );
				
				BaseParticleEmitter theEmitter = ((BaseParticleEmitter)particuleSystem.getParticleEmitter());
				theEmitter.setCenter(600,400);
			
				particuleSystem.setParticlesSpawnEnabled(true);	//enable the particule systems
				
				this.mEngine.getScene().attachChild(particuleSystem);*/
				
			default:
				break;
		}
	}
	@Override
	public void undoOperation(Operation msg) {
		IOperationHandler hdOperation = msg.getHander();// TODO Auto-generated method stub
		hdOperation.undoOperation(msg);
	}
	@Override
	public boolean onFatherScene() {
		Scene currentScene = this.mEngine.getScene();
		if(currentScene instanceof IManageableScene){
			String fatherSceneName = ((IManageableScene)currentScene).getFatherScene();
			if(fatherSceneName.length() > 0){
				this.onChangeScene(fatherSceneName);
				return true;
			}
		}
		return false;
	}
	@Override
	public void onCloseChildScene() {
		Scene currentScene = this.mEngine.getScene();
		try {
			executeStartup.acquire();
			if(currentScene.hasChildScene()){
				currentScene.clearChildScene();
			}
			executeStartup.release();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	public void onReloadScene() {
		Scene currentScene = this.mEngine.getScene();
		if(currentScene != null)
			currentScene.reset();
	}
	@Override
	public void onGoToMenu() {	
	}
	@Override
	public void onGoToNextLevel() {
	}
	@Override
	public void onConsumeFinished(Purchase purchase, IabResult result) {
		// TODO Auto-generated method stub
		Log.d(TAG, "Consumabe finished Multi finished.");
		//TO DO
	}
	@Override
	public void onConsumeMultiFinished(List<Purchase> purchases,
			List<IabResult> results) {
		// TODO Auto-generated method stub
		Log.d(TAG, "Consumable Multi finished.");
		//TO DO
		
	}
	@Override
	public void onIabPurchaseFinished(IabResult result, Purchase info) {
		Log.d(TAG, "Purchased finished.");
		mPreferencesEditor.putBoolean(info.getSku(), true);
		mPreferencesEditor.commit();
		onReloadScene();
	}
	@Override
	public void onIabSetupFinished(IabResult result) {
		Log.d(TAG, "Setup finished.");

		if (!result.isSuccess()) {
			// Oh noes, there was a problem.
			// TODO complain("Problem setting up in-app billing: " + result);
			return;
		}
		//Log.d(TAG, "Setup successful. Querying inventory.");
		mPurchMgr.queryInventoryAsync(true,this);
	}
	
	public void addDefaultProductOwned(Inventory inv){
	}
	
	@Override
	public void onQueryInventoryFinished(IabResult result, Inventory inv) {
		Log.d(TAG, "Query inventory finished.");
		Log.d(TAG, "Query inventory was successful.");

		mInventory = inv;
		
		//add default product available in the game by defaults
		this.addDefaultProductOwned(mInventory);
		
		//clear Catalog
		this.mPurchMgr.clearStoredInventory(mPreferencesEditor);
		
		//store inventory in shared preference
		List<String> allOwnedSkus = mInventory.getAllOwnedSkus();
		ListIterator<String> it = allOwnedSkus.listIterator();
		
		
		boolean hasproduct = false;
		
		if(it.hasNext()) hasproduct = true;
		
		while(it.hasNext()){
			String sku = it.next();
			mPreferencesEditor.putBoolean(sku, true);
		}
		if(hasproduct) mPreferencesEditor.commit();
		
		//reload scene to ensure status are updated following purchased component 
		onReloadScene();
		
		Log.d(TAG, "Initial inventory query finished; enabling main UI.");
	}
	

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		mPurchMgr.handleActivityResult(requestCode, resultCode, data);
	}

	void complain(String message) {
        Log.e(TAG, "**** TrivialDrive Error: " + message);
        alert("Error: " + message);
    }

    void alert(String message) {
        AlertDialog.Builder bld = new AlertDialog.Builder(this);
        bld.setMessage(message);
        bld.setNeutralButton("OK", null);
        Log.d(TAG, "Showing alert dialog: " + message);
        bld.create().show();
    }
    void logDebug(String msg) {
        if (mDebug) Log.d(TAG, msg);
    }
    void logError(String msg) {
    	if (mDebug) Log.e(TAG, "In-app billing error: " + msg);
    }

    void logWarn(String msg) {
    	if (mDebug)  Log.w(TAG, "In-app billing warning: " + msg);
    }
    int getResponseCodeFromIntent(Intent i) {
        Object o = i.getExtras().get(IabHelper.RESPONSE_CODE);
        if (o == null) {
            logError("Intent with no response code, assuming OK (known issue)");
            return IabHelper.BILLING_RESPONSE_RESULT_OK;
        }
        else if (o instanceof Integer) return ((Integer)o).intValue();
        else if (o instanceof Long) return (int)((Long)o).longValue();
        else {
            logError("Unexpected type for intent response code.");
            logError(o.getClass().getName());
            throw new RuntimeException("Unexpected type for intent response code: " + o.getClass().getName());
        }
    }
	@Override
	public boolean checkLicence(String sLicence) {
		return mPreferences.getBoolean(sLicence, false);
	}
	@Override
	public void onInAppPurchasing(String sProductID) {
		String payload = "";
		mPurchMgr.launchPurchaseFlow(this, sProductID, IabHelper.ITEM_TYPE_INAPP, 1001, this, payload);		
	}
}