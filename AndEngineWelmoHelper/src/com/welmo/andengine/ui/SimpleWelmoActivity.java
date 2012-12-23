package com.welmo.andengine.ui;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.AlphaModifier;
import org.andengine.entity.modifier.DelayModifier;
import org.andengine.entity.modifier.ParallelEntityModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.TextMenuItem;
import org.andengine.entity.scene.menu.item.decorator.ColorMenuItemDecorator;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.util.FPSLogger;
import org.andengine.extension.svg.opengl.texture.atlas.bitmap.SVGBitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.color.Color;
import org.andengine.util.modifier.IModifier;
import org.andengine.util.modifier.IModifier.IModifierListener;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import android.util.Log;
import android.view.KeyEvent;

import com.welmo.andengine.managers.ResourcesManager;
import com.welmo.andengine.managers.SceneManager;
import com.welmo.andengine.resources.descriptors.components.ParserXMLResourcesDescriptor;
import com.welmo.andengine.scenes.IManageableScene;
import com.welmo.andengine.scenes.MemoryScene;
import com.welmo.andengine.scenes.descriptors.components.GameLevel;
import com.welmo.andengine.scenes.descriptors.components.ParserXMLSceneDescriptor;
import com.welmo.andengine.utility.AsyncResourcesScenesLoader;
import com.welmo.andengine.utility.IAsyncCallBack;


public class SimpleWelmoActivity extends SimpleBaseGameActivity{
	// ===========================================================
	// Constants
	// ===========================================================
	private static final String TAG = "SplashScreen";

	private static final int 		CAMERA_WIDTH = 800;
	private static final int 		CAMERA_HEIGHT = 480;
	// ===========================================================
	// ===========================================================
	// Fields
	// ===========================================================	
	BitmapTextureAtlas 	pTextureAtals;
	ITextureRegion		pTextureRegion;
	
	// ===========================================================
	// Fields
	// ===========================================================
	protected SceneManager 					mSceneManager;
	protected ResourcesManager 				mResourceManager;
	protected Camera 						mCamera;
	protected boolean						bIsOnBackgroundLoading=false;
	
	protected String						mFirstSceneName="";
	protected String						mThanksSceneName="";
	protected String						mLoadingSceneName="";
	protected String						mMainSceneName="";
	protected String[]						mStartResourceDscFile=null;
	protected String[]						mStartSceneDscFile=null;
	

	final String FONTHBASEPATH = "font/";
	final String TEXTUREBASEPATH = "gfx/";
	
	// ===========================================================
	// Configurables resources/scene file and lists
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================


	@Override
	public EngineOptions onCreateEngineOptions() {
		
		this.mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), mCamera);

		//Enable audio option
		mCamera.setZClippingPlanes(-1000, 1000);
		engineOptions.getAudioOptions().setNeedsMusic(true);
		engineOptions.getAudioOptions().setNeedsSound(true);
		return engineOptions;

	}

	@Override
	public void onCreateResources() {	
		//Initialize resource manger
		mResourceManager = ResourcesManager.getInstance();
		mResourceManager.init(this, this.mEngine);

		readResourceDescriptions(mStartResourceDscFile);
		readScenesDescriptions(mStartSceneDscFile);		
	}

	@Override
	public Scene onCreateScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());
		
		//initialize scene manager
		mSceneManager = new SceneManager(this);
		mSceneManager.init(this.getEngine(), this);
		
		Scene sceneStart = null;
		Scene thanksScene = null;
		Scene loadingSecene = null;
		
		//Get default scenes 
		if(mFirstSceneName.length() > 0){
			sceneStart = mSceneManager.getScene(mFirstSceneName);
		}
		else{
			sceneStart = new Scene();
		}
		if(mThanksSceneName.length() > 0){
			thanksScene = mSceneManager.getScene(mThanksSceneName);
		}
		if(mLoadingSceneName.length() > 0){
			loadingSecene = mSceneManager.getScene(mLoadingSceneName);
		}
		
		
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
            	Log.i(TAG,"Activate 1st Scene");
            	mEngine.setScene((Scene)mSceneManager.getScene(mMainSceneName));
            }
        };
        AsyncResourcesScenesLoader loadingTask = new AsyncResourcesScenesLoader();
        loadingTask.setupTaskToLoadResource(callback);
        loadingTask.start();
        
		Log.i(TAG,"Load Splash Screen");
        
		return sceneStart;
		
	}


	// ===========================================================
	// Methods to overrides in child class
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
	protected final void loadScenes(String[] Scenes) {
		for (String sceneName:Scenes ){
			mSceneManager.BuildScenes(sceneName); 
		}
		Log.i(TAG,"Scenes Loaded");
	}
	
	protected final void loadTextures(String[] textures) {
		for (String textureRegionName:textures ){
			mResourceManager.getTextureRegion(textureRegionName); 
		}
	}
	protected final void loadTiledTextures(String[] tiledTextures) {
		for (String tiledTextureRegionName:tiledTextures ){
			mResourceManager.getTiledTextureRegion(tiledTextureRegionName); 
		}
	}
	protected final void loadSounds(String[] sounds) {
		for (String soundName:sounds ){
			mResourceManager.getSound(soundName); 
		}
	}
	protected final void loadMusics(String[] musics) {
		for (String musicName:musics ){
			mResourceManager.getMusic(musicName); 
		}
	}
	
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
	
	@Override
	public void onBackPressed() {
		Scene currentScene = this.mEngine.getScene();
		if(currentScene instanceof IManageableScene){
			String fatherSceneName = ((IManageableScene)currentScene).getFatherScene();
			if(fatherSceneName.length() == 0)
				super.onBackPressed();
			else
				this.mEngine.setScene((Scene)mSceneManager.getScene(fatherSceneName));
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
	
}