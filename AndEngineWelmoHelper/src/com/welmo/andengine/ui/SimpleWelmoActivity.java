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
import org.andengine.util.progress.IProgressListener;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import com.welmo.andengine.managers.ResourcesManager;
import com.welmo.andengine.managers.SceneManager;
import com.welmo.andengine.resources.descriptors.components.ParserXMLResourcesDescriptor;
import com.welmo.andengine.scenes.IManageableScene;
import com.welmo.andengine.scenes.MemoryScene;
import com.welmo.andengine.scenes.ProgressDialogScene;
import com.welmo.andengine.scenes.components.CardSprite;
import com.welmo.andengine.scenes.descriptors.components.GameLevel;
import com.welmo.andengine.scenes.descriptors.components.ParserXMLSceneDescriptor;
import com.welmo.andengine.utility.AsyncResourcesScenesLoader;
import com.welmo.andengine.utility.IAsyncCallBack;


public class SimpleWelmoActivity extends SimpleBaseGameActivity{
	// ===========================================================
	// Constants
	// ===========================================================
	private static final String TAG = "SimpleWelmoActivity";

	//default values
	//private static final int 		CAMERA_WIDTH 	= 800;
	//private static final int 		CAMERA_HEIGHT 	= 480;
	final String 					FONTHBASEPATH 	= "font/";
	final String 					TEXTUREBASEPATH = "gfx/";
	
	private enum RESOURCETYPE{
		TEXTURES, SOUNDS, MUSICS,TILEDTEXTURES,
	}
	// ===========================================================
	// ===========================================================
	// Fields
	// ===========================================================	
	BitmapTextureAtlas 	pTextureAtals;
	ITextureRegion		pTextureRegion;
	int 				nCameraWidth	= 800;
	int					nCameraHeight 	= 480;
	
	// ===========================================================
	// Fields
	// ===========================================================
	protected SceneManager 					mSceneManager;
	protected ResourcesManager 				mResourceManager;
	protected Camera 						mCamera;
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
	
	//field for the main scene displayed after the thank scene
	protected String						mMainSceneName="";
	protected String[]						mStartResourceDscFile=null;
	protected String[]						mStartSceneDscFile=null;
	
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
		this.mFirstSceneName 		= new String(firstScene);
		
	}
	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================
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
		
		this.mCamera = new Camera(0, 0, nCameraWidth, nCameraHeight);
		EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, 
				new RatioResolutionPolicy(nCameraWidth, nCameraHeight), mCamera);

		//Enable Z depth to +- 1000px
		mCamera.setZClippingPlanes(-1000, 1000);
		//Enable audio option for both music and sound effects
		engineOptions.getAudioOptions().setNeedsMusic(true);
		engineOptions.getAudioOptions().setNeedsSound(true);
		return engineOptions;

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
		mSceneManager = new SceneManager(this);
		mSceneManager.init(this.getEngine(), this);
		
		//get the first scene
		if((mFirstScene = mSceneManager.getScene(mFirstSceneName)) == null){
			gameToast("Not Start Up Scene Created");
			this.mFirstScene = new Scene();
		}
		
		//get the thank scene
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
					if(mFirstSceneDuration > 0) 
						Thread.sleep(mFirstSceneDuration);

					if(mThanksScene !=  null){
						mEngine.setScene(mThanksScene);
						Thread.sleep(mThanksSceneDuration);
					}
					if(mLoadingScene != null){
						mEngine.setScene(mLoadingScene);
						if(mLoadingScene instanceof IProgressListener)
							progressDialog = (IProgressListener)mLoadingScene;

					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
		
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
            	Log.i(TAG,"Activate 1st Scene");
            	mEngine.setScene((Scene)mSceneManager.getScene(mMainSceneName));
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
	protected final void loadScenes(String[] Scenes,int pctWorkLoad) {
		for (String sceneName:Scenes ){
			mSceneManager.BuildScenes(sceneName); 
		}
		Log.i(TAG,"Scenes Loaded");
	}
	
	
	private final void loadResource(RESOURCETYPE type, String[] resources, int pctWorkLoad){
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