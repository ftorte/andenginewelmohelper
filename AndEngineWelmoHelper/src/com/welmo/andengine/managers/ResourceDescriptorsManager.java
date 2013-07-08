package com.welmo.andengine.managers;



import java.util.HashMap;

import android.util.Log;

import com.welmo.andengine.resources.descriptors.components.BuildableTextureDescriptor;
import com.welmo.andengine.resources.descriptors.components.ColorDescriptor;
import com.welmo.andengine.resources.descriptors.components.FontDescriptor;
import com.welmo.andengine.resources.descriptors.components.MusicDescriptor;
import com.welmo.andengine.resources.descriptors.components.SoundDescriptor;
import com.welmo.andengine.resources.descriptors.components.TextureDescriptor;
import com.welmo.andengine.resources.descriptors.components.TextureRegionDescriptor;
import com.welmo.andengine.resources.descriptors.components.TiledTextureRegionDescriptor;
import com.welmo.andengine.scenes.descriptors.components.MultiViewSceneDescriptor;
import com.welmo.andengine.scenes.descriptors.components.ParserXMLSceneDescriptor;
import com.welmo.andengine.scenes.descriptors.components.SceneDescriptor;


public class ResourceDescriptorsManager {
		
	private static final String TAG ="ResourceDescriptorsManager";
	//--------------------------------------------------------
	// Variables
	//--------------------------------------------------------
	// Resources
	protected HashMap<String,TextureDescriptor> 			hmTextureDscMap;
	protected HashMap<String,TextureRegionDescriptor> 		hmTextureRegionDscMap;
	protected HashMap<String,ColorDescriptor> 				hmColorDscMap;
	protected HashMap<String,FontDescriptor> 				hmFontDscMap;
	protected HashMap<String,BuildableTextureDescriptor>	hmBuildableTextureDscMap;
	protected HashMap<String,TiledTextureRegionDescriptor>  hmTiledTextureRegionDscMap;
	protected HashMap<String,MusicDescriptor>  				hmMusicDscMap;
	protected HashMap<String,SoundDescriptor>  				hmSoundDscMap;
	
	
	// singleton Instance
	private static ResourceDescriptorsManager 	mInstance=null;
	
	//--------------------------------------------------------
		// Constructors
		//--------------------------------------------------------
	private ResourceDescriptorsManager(){
		hmTextureDscMap = new HashMap<String,TextureDescriptor>();
		hmTextureRegionDscMap = new HashMap<String,TextureRegionDescriptor>();
		hmColorDscMap = new HashMap<String,ColorDescriptor>();
		hmFontDscMap = new HashMap<String,FontDescriptor>();
		hmBuildableTextureDscMap = new HashMap<String,BuildableTextureDescriptor>();
		hmTiledTextureRegionDscMap = new HashMap<String,TiledTextureRegionDescriptor>();
		hmMusicDscMap = new HashMap<String,MusicDescriptor>();
		hmSoundDscMap = new HashMap<String,SoundDescriptor>();
	}
	@method
	public synchronized  static ResourceDescriptorsManager getInstance(){
		if(mInstance == null)
			mInstance = new  ResourceDescriptorsManager();
		return mInstance;
	}
	
	//--------------------------------------------------------
	// Methods to add/get descriptors
	//--------------------------------------------------------
	//--------------------------------------------------------
	// TEXTURE
	//--------------------------------------------------------
	@method
	//Add a texture description to the texture descriptions list
	public synchronized void addTexture(String name, TextureDescriptor texture){
		if (hmTextureDscMap == null)
			throw new NullPointerException("ResurceDescriptorsManager not initialized correctly"); 
		hmTextureDscMap.put(name,texture);
	}
	@method
	public TextureDescriptor getTextureDescriptor(String name){
		if (hmTextureRegionDscMap == null)
			throw new NullPointerException("ResurceDescriptorsManager not initialized correctly"); 
		return hmTextureDscMap.get(name);
	}

	//--------------------------------------------------------
	// TEXTUREREGION
	//--------------------------------------------------------
	@method
	//Add the description of a texture region to the texture region descriptions list
	public synchronized void addTextureRegion(String name, TextureRegionDescriptor textureRegion){
		if (hmTextureRegionDscMap == null)
			throw new NullPointerException("ResurceDescriptorsManager not initialized correctly"); 
		hmTextureRegionDscMap.put(name,textureRegion);
	}
	@method
	public TextureRegionDescriptor getTextureRegion(String name){
		if (hmTextureRegionDscMap == null)
			throw new NullPointerException("ResurceDescriptorsManager not initialized correctly"); 
		return hmTextureRegionDscMap.get(name);
	}
	//--------------------------------------------------------
	// COLOR
	//--------------------------------------------------------
	@method
	//Add the description of a scene descriptions list
	public synchronized void addColor(String name, ColorDescriptor color){
		if (hmColorDscMap == null)
			throw new NullPointerException("ResurceDescriptorsManager not initialized correctly"); 
		hmColorDscMap.put(name,color);
	}
	@method
	//Add the description of a scene descriptions list
	public ColorDescriptor getColor(String name){
		if (hmColorDscMap == null)
			throw new NullPointerException("ResurceDescriptorsManager not initialized correctly"); 
		return hmColorDscMap.get(name);
	}
	//--------------------------------------------------------
	// FONT
	//--------------------------------------------------------
	@method
	//Add the description of a scene descriptions list
	public synchronized void addFont(String name, FontDescriptor font){
		if (hmFontDscMap == null)
			throw new NullPointerException("ResurceDescriptorsManager not initialized correctly"); 
		hmFontDscMap.put(name,font);
	}
	@method
	//Add the description of a scene descriptions list
	public FontDescriptor getFont(String name){
		if (hmFontDscMap == null)
			throw new NullPointerException("ResurceDescriptorsManager not initialized correctly"); 
		return hmFontDscMap.get(name);
	}
	
	//--------------------------------------------------------
	// BUILDABLETEXTURE
	//--------------------------------------------------------
	@method
	//Add a texture description to the texture descriptions list
	public synchronized void addBuildableTexture(String name, BuildableTextureDescriptor texture){
		if (hmBuildableTextureDscMap == null)
			throw new NullPointerException("ResurceDescriptorsManager not initialized correctly"); 
		hmBuildableTextureDscMap.put(name,texture);
	}
	@method
	public BuildableTextureDescriptor getBuildableTexture(String name){
		if (hmTextureRegionDscMap == null)
			throw new NullPointerException("ResurceDescriptorsManager not initialized correctly"); 
		return hmBuildableTextureDscMap.get(name);
	}

	//--------------------------------------------------------
	// TILEDTEXTUREREGION
	//--------------------------------------------------------
	@method
	//Add the description of a texture region to the texture region descriptions list
	public synchronized void addTiledTextureRegion(String name, TiledTextureRegionDescriptor textureRegion){
		if (hmTiledTextureRegionDscMap == null)
			throw new NullPointerException("ResurceDescriptorsManager not initialized correctly"); 
		hmTiledTextureRegionDscMap.put(name,textureRegion);
	}
	@method
	public TiledTextureRegionDescriptor getTiledTextureRegion(String name){
		if (hmTiledTextureRegionDscMap == null)
			throw new NullPointerException("ResurceDescriptorsManager not initialized correctly"); 
		return hmTiledTextureRegionDscMap.get(name);
	}
	//--------------------------------------------------------
	// MUSIC
	//--------------------------------------------------------
	//Add the description of a texture region to the texture region descriptions list
	public synchronized void addMusicDescriptor(String name, MusicDescriptor musicDsc){
		if (this.hmMusicDscMap == null)
			throw new NullPointerException("ResurceDescriptorsManager not initialized correctly"); 
		hmMusicDscMap.put(name,musicDsc);
	}
	@method
	public MusicDescriptor getMusicDescriptor(String name){
		if (hmMusicDscMap == null)
			throw new NullPointerException("ResurceDescriptorsManager not initialized correctly"); 
		return hmMusicDscMap.get(name);
	}
	//--------------------------------------------------------
	// SOUND
	//--------------------------------------------------------
	public synchronized void addSoundDescriptor(String name, SoundDescriptor soundDsc){
		if (this.hmSoundDscMap == null)
			throw new NullPointerException("ResurceDescriptorsManager not initialized correctly"); 
		hmSoundDscMap.put(name,soundDsc);
	}
	@method
	public SoundDescriptor getSoundDescriptor(String name){
		if (hmSoundDscMap == null)
			throw new NullPointerException("ResurceDescriptorsManager not initialized correctly"); 
		Log.d(TAG,"getSoundDescriptor : hasmaps size = " + hmSoundDscMap.size());
		return hmSoundDscMap.get(name);
	}
}
