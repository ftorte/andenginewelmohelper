package com.welmo.andengine.managers;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import org.andengine.audio.music.Music;
import org.andengine.audio.music.MusicFactory;
import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.engine.Engine;
import org.andengine.extension.svg.opengl.texture.atlas.bitmap.SVGBitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.font.IFont;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.EmptyBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.bitmap.source.decorator.BaseBitmapTextureAtlasSourceDecorator;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder.TextureAtlasBuilderException;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.util.color.Color;
import org.andengine.util.debug.Debug;
import org.andengine.util.modifier.IModifier.DeepCopyNotSupportedException;

import com.welmo.andengine.resources.descriptors.BuildableTextureDescriptor;
import com.welmo.andengine.resources.descriptors.ColorDescriptor;
import com.welmo.andengine.resources.descriptors.DynamicTiledTextureRegionDescriptor;
import com.welmo.andengine.resources.descriptors.FontDescriptor;
import com.welmo.andengine.resources.descriptors.MusicDescriptor;
import com.welmo.andengine.resources.descriptors.ResTags;
import com.welmo.andengine.resources.descriptors.SoundDescriptor;
import com.welmo.andengine.resources.descriptors.TextureDescriptor;
import com.welmo.andengine.resources.descriptors.TextureRegionDescriptor;
import com.welmo.andengine.resources.descriptors.TiledTextureRegionDescriptor;
import com.welmo.andengine.utility.method;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;

public class ResourcesManager {
	// ===========================================================
	// Constants
	// ===========================================================
	final static String TAG 		= "ResourcesManager";
	
	final String FONTHBASEPATH 		= "font/";
	final String TEXTUREBASEPATH 	= "gfx/";
	final String MUSICBASEPATH		= "musics/";
	final String SOUNDBASEPATH		= "sounds/";
	
	public enum SoundType { 
		SOUND, PAUSE,
	}
	// ===========================================================
	// Variables
	// ===========================================================
	Context mCtx;
	Engine 	mEngine;
	
	boolean initialized 			= false;

	private HashMap<String, Font> 							mapFonts;
	private HashMap<String, ITextureRegion> 				mapTextureRegions;
	private HashMap<String, BitmapTextureAtlas> 			mapBitmapTexturesAtlas;
	private HashMap<String, Color> 							mapColors;
	private HashMap<String, BuildableBitmapTextureAtlas> 	mapBuildablBitmapTexturesAtlas;
	private HashMap<String, ITiledTextureRegion>  			mapTiledTextureRegions;
	private HashMap<String, DynamicTiledTextureRegion>  	mapConfigTiledTextureRegions;
	private HashMap<String, DynamicTextureRegion>  			mapDynamicTextureRegions;
	private HashMap<String, Music> 							mapMusics;
	private HashMap<String, SoundContainer>  				mapSound;
	private HashMap<String, DecoratedTextures>				mapDecoratedTextures;
	
	
	// singleton Instance
	private static ResourcesManager 	mInstance=null;
	
	// -----------------------------------------------------------------
	//[FT] Temporary varaibles to manage the decorated textures
	// [FT] Bitmap balloon = null;
	Bitmap baloonworking = null;
	
	//private int[] pixelsCopy	=	new int[1024*750];
	//private int[] stack			= 	new int[1024*750];
	//[FT] Temporary varaibles to manage the decorated textures
	// -----------------------------------------------------------------
	// Inner classess
	// -----------------------------------------------------------------
	// Inner class SoundExtended
	public class SoundContainer{
	
		private Sound theSound  = null;
		private int duration	= 0;
		private SoundType type	= SoundType.SOUND;
		
		public SoundContainer() {
		}
		public Sound getTheSound() {
			return theSound;
		}

		protected void createFromAsset(String asset, int duration) {
			try {
				this.theSound = SoundFactory.createSoundFromAsset(mEngine.getSoundManager(), mCtx, asset);
				this.duration = duration;
			} 
			catch (IllegalArgumentException e) {    } 
			catch (IllegalStateException e) { } 
			catch (IOException e) { } 
		}
		public int getDuration() {
			return duration;
		}
		public void setDuration(int duration) {
			this.duration = duration;
		}
		public SoundType getType() {
			return type;
		}
		public void setType(SoundType type) {
			this.type = type;
		}
	}
	
	// Inner class SoundExtended
	public class DynamicTiledTextureRegion{

		protected String 	assetFileName = new String();
		protected int		nNbCol = 0;
		protected int		nNbRow = 0; 
		
		private BitmapTextureAtlas mBitmapTextureAtlas;
		private TiledTextureRegion mTiledTexture;
		
		public void setTextureAtlas(BitmapTextureAtlas theTextureAtlas){mBitmapTextureAtlas = theTextureAtlas;}
		protected BitmapTextureAtlas getTextureAtlas(){return mBitmapTextureAtlas;}
		
		public void setTiledTexture(TiledTextureRegion theTiledTexture){mTiledTexture = theTiledTexture;}
		protected TiledTextureRegion getTiledTexture(){return mTiledTexture;}
		
		public void createFromAsset(String newAssetName, int nbCol, int nbRow, Context ctx) {
			mTiledTexture = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mBitmapTextureAtlas, ctx, newAssetName, 0,0,nbCol, nbRow);
			mBitmapTextureAtlas.load();
			this.nNbCol = nbCol;
        	this.nNbRow = nbRow;
		}
		
		public void updateFromAsset(String newAssetName, int nbCol, int nbRow, Context ctx) {
            //if( newAssetName.compareToIgnoreCase(assetFileName) != 0){
            mBitmapTextureAtlas.clearTextureAtlasSources();
            mTiledTexture = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlas, ctx, newAssetName, 0,0,nbCol, nbRow);
            mBitmapTextureAtlas.load();
            assetFileName = new String(newAssetName);
            this.nNbCol = nbCol;
            this.nNbRow = nbRow;
            //}
		}
		
		public ITiledTextureRegion getITiledTextureRegion(){return  mTiledTexture;}
		
		public boolean isSameAs(String resourceName, int nCol, int nRow) {
			if((resourceName.compareToIgnoreCase(assetFileName) == 0) && (this.nNbCol == nCol) && (this.nNbRow == nRow))
				return true;
			return false;
		}
	}	
	
	// Inner class SoundExtended
	public class DynamicTextureRegion{

		protected String 	assetFileName = new String();	
		private BitmapTextureAtlas mBitmapTextureAtlas;
		private TextureRegion mTexture;

		
		public DynamicTextureRegion(){
			
		}
		
		public void loadFromAsset(String newAssetName, Context ctx) {
			mBitmapTextureAtlas.clearTextureAtlasSources();
			mTexture = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mBitmapTextureAtlas, ctx, newAssetName, 0,0);
			mBitmapTextureAtlas.load();
			assetFileName = new String(newAssetName);
		}

		public ITextureRegion getITextureRegion(){return  (ITextureRegion)mTexture;}

		public boolean isSameAs(String resourceName) {
			if(resourceName.compareToIgnoreCase(assetFileName) == 0) return true;
			return false;
		}

		public void setBitmapTextureAtlas(BitmapTextureAtlas bitmapTextureAtlas) {
			mBitmapTextureAtlas = bitmapTextureAtlas;
		}
		public BitmapTextureAtlas getBitmapTextureAtlas() {
			return mBitmapTextureAtlas;
		}

	}	

	// Inner class PixelImageContainter
	public class DecoratedTextures{
		
		Bitmap 								theBitmap 					= null;
		Bitmap 								theBitmapOrg 				= null;
		
		ITextureRegion  					mDecoratedTextureRegion		= null;
		private int[] 						pixelsCopy					= null;
		private int							width 						= 0;
		private int							height 						= 0;
		private BitmapTextureAtlas 			theTextureAtlas				= null;
		private IBitmapTextureAtlasSource 	decoratedTextureAtlasSource = null;
		private Context						theCtx						= null;
		public final int					MAX_WIDTH					= 2048;
		public final int					MAX_HEIGHT					= 2048;
		private String						strName						= "";
		private int							mBackGroundColor			=0xFFFFFF;
		
		
		public DecoratedTextures(Context ctx , String imageSource, BitmapTextureAtlas bitmapTextureAtlas) {
		
			this.pixelsCopy 		= new int[MAX_WIDTH*MAX_HEIGHT];
			this.theTextureAtlas 	= bitmapTextureAtlas;
			this.theCtx 			= ctx;
			this.strName			= imageSource;
			try {
				//create a bitmap usint ghe image in the file that will contains the image
				theBitmapOrg 	= BitmapFactory.decodeStream(theCtx.getAssets().open(imageSource));
				this.width 		= theBitmapOrg.getWidth();
				this.height		= theBitmapOrg.getHeight();
				theBitmap 		= theBitmapOrg.copy(Bitmap.Config.ARGB_8888, true);
				theBitmap.getPixels(pixelsCopy, 0, width, 0, 0, width, height);
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//Decorate the texture atlas with the image
			decoratedTextureAtlasSource = new BaseBitmapTextureAtlasSourceDecorator(new EmptyBitmapTextureAtlasSource(1024, 750)) {
				@Override
				protected void onDecorateBitmap(Canvas pCanvas) throws Exception {
					pCanvas.drawBitmap(theBitmap, 0, 0, this.mPaint);
				}
				
				@Override
				public BaseBitmapTextureAtlasSourceDecorator deepCopy() {
					throw new DeepCopyNotSupportedException();
				}
			};

			mDecoratedTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromSource(bitmapTextureAtlas, decoratedTextureAtlasSource, 0, 0);			
		}
		public int getWidth(){return width;};
		public int getHeight(){return height;};
		public ITextureRegion getTexture(){return this.mDecoratedTextureRegion;};
		
		public void loadImage(String newImage){
			
			if (strName.equalsIgnoreCase(newImage))
				return;
			
			this.strName = newImage;
			
			try {
				theBitmapOrg 	= BitmapFactory.decodeStream(theCtx.getAssets().open(newImage));
				height 			= theBitmapOrg.getHeight();
				width 			= theBitmapOrg.getWidth();
				theBitmap 		= theBitmapOrg.copy(Bitmap.Config.ARGB_8888, true);
				theBitmap.getPixels(pixelsCopy, 0, width, 0, 0, width, height);
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			theTextureAtlas.clearTextureAtlasSources();
			mDecoratedTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromSource(theTextureAtlas, decoratedTextureAtlasSource, 0, 0);
		}
		public String getName(){
			return strName;
		}
		public int[] getPixelsCopy(){
			return this.pixelsCopy;
		}
		public void setPixelsFromCopy(int[] pixelsCopy){
			theBitmap.setPixels(pixelsCopy, 0, width, 0, 0, width, height);
		}
		public void reloadBitmap(){
			theTextureAtlas.clearTextureAtlasSources();
			mDecoratedTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromSource(theTextureAtlas, decoratedTextureAtlasSource, 0, 0);
		}
		
	}
	// ===========================================================
	// Constructors
	// ===========================================================
	private ResourcesManager(){
		mapFonts 						= new HashMap<String, Font>();
		mapTextureRegions 				= new HashMap<String, ITextureRegion>();
		mapBitmapTexturesAtlas 			= new HashMap<String, BitmapTextureAtlas>();
		mapColors 						= new HashMap<String, Color>();
		mapBuildablBitmapTexturesAtlas 	= new HashMap<String, BuildableBitmapTextureAtlas>();
		mapTiledTextureRegions 			= new HashMap<String, ITiledTextureRegion>();
		mapMusics 						= new HashMap<String, Music>();
		mapSound 						= new HashMap<String, SoundContainer>();
		mapDecoratedTextures 			= new HashMap<String, DecoratedTextures>();
		mapConfigTiledTextureRegions	= new HashMap<String, DynamicTiledTextureRegion>();
		mapDynamicTextureRegions		= new HashMap<String, DynamicTextureRegion>();
		initialized 					= false;
	}
	@method
	public synchronized static ResourcesManager getInstance(){
		if(mInstance == null)
			mInstance = new  ResourcesManager();
		return mInstance;
	}
	public synchronized void init(Context ctx, Engine eng){
		//if init called for already initilized manager but with different context and engine throw an exception
		if(initialized & (mCtx != ctx || mEngine!= eng)){ //if intiliazation called on onother conxtx
			mapFonts.clear();
			mapTextureRegions.clear();
			mapBitmapTexturesAtlas.clear();
			mapColors.clear();
			mapBuildablBitmapTexturesAtlas.clear();
			mapTiledTextureRegions.clear();
			mapDecoratedTextures.clear();
			initialized = false;
		}
		if(!initialized){
			// init resource manager with font & texture base path
			FontFactory.setAssetBasePath(FONTHBASEPATH);
			SVGBitmapTextureAtlasTextureRegionFactory.setAssetBasePath(TEXTUREBASEPATH);
			BitmapTextureAtlasTextureRegionFactory.setAssetBasePath(TEXTUREBASEPATH);
			MusicFactory.setAssetBasePath(MUSICBASEPATH);
			SoundFactory.setAssetBasePath(SOUNDBASEPATH);

			//mCtx 	= ctx;
			mCtx 	= ctx.getApplicationContext();
			mEngine = eng;
			initialized = true;
		}	
	}
	
	// ===========================================================
	// GETTERS & SETTERS
	// ===========================================================
	
	public Engine getEngine() { return mEngine;}
	public void setEngine(Engine mEngine) {this.mEngine = mEngine;}

	// ===========================================================
	// PAUSE & RESUME
	// ===========================================================
	public synchronized void PauseGame(){
		Log.i(TAG, "PauseGame In");
		//release all sound
		for (SoundContainer value : mapSound.values()) {
			if(!value.theSound.isReleased())
				value.theSound.pause();
				value.theSound.release();
				Log.i(TAG, "PauseGame In " + value.theSound.getSoundID());
		}
		
		//releas all musics
		for (Music value : this.mapMusics.values()) {
			if(!value.isReleased())
				value.release();
				Log.i(TAG, "PauseGame In ");
		}
		
		mapSound.clear();
		mapMusics.clear();
		
		//Try to fix bug the dynamic texture after a pause becomes black
		mapConfigTiledTextureRegions.clear();
		
		Log.i(TAG, "PauseGame Out");
	}
	public synchronized void ResumeGame(){

	}
	public synchronized void EngineLoadResources(Engine theEndgine){
		TextureManager textureManager = theEndgine.getTextureManager();
		for(String key:mapBitmapTexturesAtlas.keySet())
			textureManager.loadTexture(mapBitmapTexturesAtlas.get(key));
	}
	// =============================================================================
	// FONT
	// =============================================================================
	public synchronized IFont loadFont(String fontName){
		ResourceDescriptorsManager pResDscMng = ResourceDescriptorsManager.getInstance();
		FontDescriptor pFontDsc = pResDscMng.getFont(fontName);
		
		//create font
		Font newFont=null;
		if(pFontDsc.filename.contentEquals("")){
			//Create font from typeFace
			newFont = FontFactory.create(mEngine.getFontManager(),
					mEngine.getTextureManager(), pFontDsc.texture_sizeX,pFontDsc.texture_sizeY, pFontDsc.TypeFace, 
					pFontDsc.Parameters[ResTags.R_A_FONT_SIZE_IDX]);
		}
		else{
			final ITexture fontTexture = new BitmapTextureAtlas(mEngine.getTextureManager(), pFontDsc.texture_sizeX,pFontDsc.texture_sizeY, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
			newFont = FontFactory.createFromAsset(mEngine.getFontManager(), fontTexture,this.mCtx.getAssets(), 
					pFontDsc.filename, pFontDsc.Parameters[ResTags.R_A_FONT_SIZE_IDX], pFontDsc.AntiAlias,
					android.graphics.Color.WHITE);
			newFont.load();

		}
		
		//add font to font manger
		this.mapFonts.put(pFontDsc.Name,newFont);
		//return the new font just created
		return newFont;
	}
	public IFont getFont(String fontName){
		IFont theFont = mapFonts.get(fontName);
		//if the texture region is not already loaded in the resource manager load it
		if(theFont==null) 
			theFont = loadFont(fontName);
		//return the found or loaded texture region
		return theFont;
	}
	// =============================================================================
	// TEXTURE REGION
	// =============================================================================
	public void packTexture(BitmapTextureAtlas theTextureAtlas, TextureDescriptor pTextRegDsc){
	
		class Rect{
			public int x,y,w,h=0;
			public Rect (int px, int py,int pw,int ph){
				this.x = px;
				this.y = py;
				this.w = pw;
				this.h = ph;
			}
		}
		
		class Node{
			Node 					child[] 		= new Node[2];
			Rect 					rctangle		= null;
			TextureRegionDescriptor tesctureDSC		= null;
			boolean 				leaf 			= true;
			
			public void createLeaf(){
				this.child[0] = new Node();
				this.child[1] = new Node();
				leaf = false;
			}

			public Node insert(TextureRegionDescriptor texture){

				//read some parameter
				int textureW 	= texture.Parameters[ResTags.R_A_WIDTH_IDX];
				int textureH	= texture.Parameters[ResTags.R_A_HEIGHT_IDX];
				int x 			= this.rctangle.x;
				int y 			= this.rctangle.y;
				int w 			= this.rctangle.w;
				int h 			= this.rctangle.h;

				//if we're not a leaf then try inserting into first child
				if(!leaf){
					Node newNode = child[0].insert(texture);
					if(newNode != null) return newNode;

					//no room, insert into second
					return child[1].insert(texture);
				}

				else{
					//if there's already a lightmap here, return)
					if( tesctureDSC != null) return null;

					//if we're too small, return
					if((textureW > w) || (textureH >h))
						return null;

					//if we're just right, accept)
					if((textureW == w) && (textureH == h)){
						this.tesctureDSC = texture;
						texture.Parameters[ResTags.R_A_POSITION_X_IDX] = x;
						texture.Parameters[ResTags.R_A_POSITION_Y_IDX] = y;
						return this;
					}


					//otherwise, gotta split this node and create some kids)
					createLeaf();

					//decide which way to split
					float dw = w - textureW;
					float dh = h - textureH;

					if(dw > dh){
						//split vertical
						child[0].rctangle = new Rect(x, y,textureW, h);
						child[1].rctangle = new Rect(x+textureW, y, w - textureW , h);
					}
					else{
						//split horizontal
						child[0].rctangle = new Rect(x, y, w, textureH);
						child[1].rctangle = new Rect(x, y+textureH,w, h-textureH);
					}
					//insert into first child we created)
					return this.child[0].insert( texture);
				}
			}
		};
		        			        		
		//Sorting Texture from biggest to lowest
		Collections.sort(pTextRegDsc.Regions, new Comparator<TextureRegionDescriptor>() {
		        @Override
		        public int compare(TextureRegionDescriptor  texture1, TextureRegionDescriptor  texture2)
		        {

		        	int sur1 = texture1.Parameters[ResTags.R_A_WIDTH_IDX]*texture1.Parameters[ResTags.R_A_HEIGHT_IDX];
		        	int sur2 = texture2.Parameters[ResTags.R_A_WIDTH_IDX]*texture2.Parameters[ResTags.R_A_HEIGHT_IDX];
		        	return (sur1>sur2 ? -1 : (sur1==sur2 ? 0 : 1));
		        }
		    });
		
		//create the rood node containg the structure
		Node rootNode = new Node();
		rootNode.rctangle = new Rect(0,0,pTextRegDsc.Parameters[ResTags.R_A_WIDTH_IDX],pTextRegDsc.Parameters[ResTags.R_A_HEIGHT_IDX]);
		

		
		//calculate texture dimension for PNG	
		//iterate to all textureregion to create the textures
		for (TextureRegionDescriptor pTRDsc:pTextRegDsc.Regions){	
			switch(pTRDsc.type){
				case SVG://don nothing
					break;
				case PNG://
					BitmapFactory.Options options =new BitmapFactory.Options();
					options.inJustDecodeBounds = true;
					//Returns null, sizes are in the options variable
					String filename = new String("gfx/" + pTRDsc.filename);
					//BitmapFactory.decodeFile("/gfx/sprite_final/monster.png", options);
					try {
						BitmapFactory.decodeStream(mCtx.getAssets().open(filename), null, options);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					pTRDsc.Parameters[ResTags.R_A_WIDTH_IDX] = options.outWidth;
					pTRDsc.Parameters[ResTags.R_A_HEIGHT_IDX] = options.outHeight;
					break;
				default:
					break;
			}
		}

		//Crete node tree
		for (TextureRegionDescriptor pTRDsc:pTextRegDsc.Regions){
			rootNode.insert(pTRDsc);
		}

				
		//iterate to all textureregion to create the textures
		for (TextureRegionDescriptor pTRDsc:pTextRegDsc.Regions){	
			//check texture region is new if not generate an exception
			if(mapBitmapTexturesAtlas.get(pTRDsc.Name)!=null) 
				throw new IllegalArgumentException("In LoadTexture: Tentative to create a texture region that already exists ");
			switch(pTRDsc.type){
				case SVG://Create texture region if SVG
					mapTextureRegions.put(pTRDsc.Name, SVGBitmapTextureAtlasTextureRegionFactory.createFromAsset(theTextureAtlas, 
							this.mCtx, pTRDsc.filename, pTRDsc.Parameters[ResTags.R_A_WIDTH_IDX], pTRDsc.Parameters[ResTags.R_A_HEIGHT_IDX], 
							pTRDsc.Parameters[ResTags.R_A_POSITION_X_IDX], pTRDsc.Parameters[ResTags.R_A_POSITION_Y_IDX]));
					break;
				case PNG://
					mapTextureRegions.put(pTRDsc.Name, BitmapTextureAtlasTextureRegionFactory.createFromAsset(theTextureAtlas, 
							this.mCtx, pTRDsc.filename,pTRDsc.Parameters[ResTags.R_A_POSITION_X_IDX], pTRDsc.Parameters[ResTags.R_A_POSITION_Y_IDX]));
					break;
				default:
					break;
			}
		}
	}
	public synchronized ITexture loadTexture(String textureName){
		ResourceDescriptorsManager pResDscMng = ResourceDescriptorsManager.getInstance();
		TextureDescriptor pTextDsc = pResDscMng.getTextureDescriptor(textureName);

		//check if texture already exists
		if(mapBitmapTexturesAtlas.get(pTextDsc.Name)!=null)
			throw new IllegalArgumentException("In LoadTexture: Tentative to load a texture already loaded");

		//Create the texture
		BitmapTextureAtlas pTextureAtlas = new BitmapTextureAtlas(mEngine.getTextureManager(),pTextDsc.Parameters[ResTags.R_A_WIDTH_IDX], pTextDsc.Parameters[ResTags.R_A_HEIGHT_IDX], TextureOptions.BILINEAR_PREMULTIPLYALPHA);		
		mapBitmapTexturesAtlas.put(pTextDsc.Name,pTextureAtlas);

		pTextureAtlas.clearTextureAtlasSources();
		
		if(pTextDsc.autpacking){
			this.packTexture(pTextureAtlas, pTextDsc);
		}
		else{
			//iterate to all textureregion define in the texture
			for (TextureRegionDescriptor pTRDsc:pTextDsc.Regions){	

				if(mapBitmapTexturesAtlas.get(pTRDsc.Name)!=null) // check that texture region is new
					throw new IllegalArgumentException("In LoadTexture: Tentative to create a texture region that already exists ");

				switch(pTRDsc.type){
					case SVG://Create texture region if SVG
						mapTextureRegions.put(pTRDsc.Name, SVGBitmapTextureAtlasTextureRegionFactory.createFromAsset(pTextureAtlas, 
								this.mCtx, pTRDsc.filename, pTRDsc.Parameters[ResTags.R_A_WIDTH_IDX], pTRDsc.Parameters[ResTags.R_A_HEIGHT_IDX], 
								pTRDsc.Parameters[ResTags.R_A_POSITION_X_IDX], pTRDsc.Parameters[ResTags.R_A_POSITION_Y_IDX]));
						break;
					case PNG://
						mapTextureRegions.put(pTRDsc.Name, BitmapTextureAtlasTextureRegionFactory.createFromAsset(pTextureAtlas, 
								this.mCtx, pTRDsc.filename,pTRDsc.Parameters[ResTags.R_A_POSITION_X_IDX], pTRDsc.Parameters[ResTags.R_A_POSITION_Y_IDX]));
						break;
					default:
						break;
				}
			}
		}
		mEngine.getTextureManager().loadTexture(pTextureAtlas);		
		return pTextureAtlas;
	}
	public synchronized ITextureRegion loadTextureRegion(String textureRegionName){
		ResourceDescriptorsManager pResDscMng = ResourceDescriptorsManager.getInstance();
		TextureRegionDescriptor pTextRegDsc = pResDscMng.getTextureRegion(textureRegionName);
		if(pTextRegDsc == null)
			throw new IllegalArgumentException("In LoadTextureRegion: there is no description for the requested texture = " + textureRegionName);
		
		//To load a texture region the manager load the texture and all child regions
		loadTexture(pTextRegDsc.textureName);
		
		//return the texture region that has just been loaded
		return mapTextureRegions.get(textureRegionName);
	}
	public ITextureRegion getTextureRegion(String textureRegionName){
		ITextureRegion theTexture = mapTextureRegions.get(textureRegionName);
		//if the texture region is not already loaded in the resource manager load it
		if(theTexture==null) 
			theTexture = loadTextureRegion(textureRegionName);
		//return the found or loaded texture region
		return theTexture;
	}
	public ITextureRegion loadDynamicTextureRegion(String textureRegionName, String textureSourceFileName){
		DynamicTextureRegion theTexture = this.mapDynamicTextureRegions.get(textureRegionName);
		
		if(theTexture==null){ //if null create the dynamic texture
			TextureRegionDescriptor pTextRegDsc = ResourceDescriptorsManager.getInstance().getTextureRegion(textureRegionName);
			TextureDescriptor pTextDsc = ResourceDescriptorsManager.getInstance().getTextureDescriptor(pTextRegDsc.textureName);
		
			if(pTextDsc == null)
				return null;
			
			theTexture = new DynamicTextureRegion();
				
			//Create the texture
			theTexture.setBitmapTextureAtlas(new BitmapTextureAtlas(mEngine.getTextureManager(),pTextDsc.Parameters[ResTags.R_A_WIDTH_IDX], pTextDsc.Parameters[ResTags.R_A_HEIGHT_IDX], TextureOptions.BILINEAR_PREMULTIPLYALPHA ));
			
			//update the map
			this.mapDynamicTextureRegions.put(pTextDsc.Name,theTexture);
			
			//load the texture atlas in the engin
			mEngine.getTextureManager().loadTexture(theTexture.getBitmapTextureAtlas());	
			
		}
		
		theTexture.loadFromAsset(textureSourceFileName, this.mCtx);

		return theTexture.getITextureRegion();
	}
	public DecoratedTextures getDecoratedTextureRegion(String DecoratorName, String ImageName){
		
		BitmapTextureAtlas mBitmapTextureAtlas;
		
		if((mBitmapTextureAtlas = mapBitmapTexturesAtlas.get(DecoratorName))==null){
			//Creation Bitmap Texture Atlas will contain the texture region
			mBitmapTextureAtlas = new BitmapTextureAtlas(this.mEngine.getTextureManager(), 1024, 750, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
			mapBitmapTexturesAtlas.put(ImageName,mBitmapTextureAtlas);
		
			//Create the texture region container
			DecoratedTextures imgContainer = new DecoratedTextures(this.mCtx,ImageName,mBitmapTextureAtlas);
		
			this.mapDecoratedTextures.put(ImageName, imgContainer);
			//load the texture region into the engine
			mBitmapTextureAtlas.load();
			return imgContainer;	
		}
		else
		{
			DecoratedTextures imgContainer = mapDecoratedTextures.get(DecoratorName);
			if(! imgContainer.getName().equalsIgnoreCase(ImageName))
				imgContainer.loadImage(ImageName);
			return imgContainer;
		}
	}
	
	// =============================================================================
	// COLOR
	// =============================================================================
	public synchronized Color loadColor(String colorName){
		Log.i(TAG,"loadColor:" + colorName);
		ResourceDescriptorsManager pResDscMng = ResourceDescriptorsManager.getInstance();
		ColorDescriptor pColorDsc = pResDscMng.getColor(colorName);

		//create color
		Color newColor = null;
		newColor = new Color((float)(pColorDsc.Parameters[ResTags.R_A_RED_IDX]/255.0), 
				(float)(pColorDsc.Parameters[ResTags.R_A_GREEN_IDX]/255.0),
				(float)( pColorDsc.Parameters[ResTags.R_A_BLUE_IDX]/255.0));	

		//add font to font manger
		this.mapColors.put(colorName,newColor);

		//return the new font just created
		return newColor;
	}
	public Color getColor(String colorName){
		Color theColor = mapColors.get(colorName);
		//if the texture region is not already loaded in the resource manager load it
		if(theColor==null) 
			theColor = loadColor(colorName);
		//return the found or loaded texture region
		return theColor;

	}
	// =============================================================================
	// TILED TEXTURE REGION
	// =============================================================================
	public synchronized ITexture loadBuildableTexture(String textureName){
		ResourceDescriptorsManager pResDscMng = ResourceDescriptorsManager.getInstance();
		BuildableTextureDescriptor pTextRegDsc = pResDscMng.getBuildableTexture(textureName);

		//check if texture already exists
		if(mapBuildablBitmapTexturesAtlas.get(pTextRegDsc.Name)!=null)
			throw new IllegalArgumentException("In LoadTexture: Tentative to load a texture already loaded");

		//Create the texture
		BuildableBitmapTextureAtlas pBuildableTextureAtlas = new BuildableBitmapTextureAtlas(mEngine.getTextureManager(), pTextRegDsc.Parameters[ResTags.R_A_WIDTH_IDX], pTextRegDsc.Parameters[ResTags.R_A_HEIGHT_IDX], TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		//FT pBuildableTextureAtlas.clearTextureAtlasSources();
		mapBuildablBitmapTexturesAtlas.put(pTextRegDsc.Name,pBuildableTextureAtlas);

		//iterate to all tiled textures regions define in the texture
		for (TiledTextureRegionDescriptor pTRDsc:pTextRegDsc.Regions){	
			
			if(mapBitmapTexturesAtlas.get(pTRDsc.Name)!=null) // check that texture region is new
				throw new IllegalArgumentException("In LoadTexture: Tentative to create a texture region that already exists ");

			//Create texture region
			mapTiledTextureRegions.put(pTRDsc.Name, 
					BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(pBuildableTextureAtlas, 
							this.mCtx, pTRDsc.filename, pTRDsc.column, pTRDsc.row));
		}
		try {
			pBuildableTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 0, 1));
			pBuildableTextureAtlas.load();
		} catch (TextureAtlasBuilderException e) {
			Debug.e(e);
		}
		
		return pBuildableTextureAtlas;
	}
	public synchronized ITiledTextureRegion loadTiledTextureRegion(String tiledTextureRegionName){
		ResourceDescriptorsManager pResDscMng = ResourceDescriptorsManager.getInstance();
		TiledTextureRegionDescriptor pTiledTextRegDsc = pResDscMng.getTiledTextureRegion(tiledTextureRegionName);
		if(pTiledTextRegDsc == null)
		 	throw new IllegalArgumentException("In LoadTiledTextureRegion: there is no description for the requested texture = " + tiledTextureRegionName);
		 
		//To load a texture region the manager load the texture and all child regions
		loadBuildableTexture(pTiledTextRegDsc.textureName);
		
		//return the texture region that has just been loaded
		return mapTiledTextureRegions.get(tiledTextureRegionName);
	}
	public synchronized DynamicTiledTextureRegion loadDynamicTiledTextureRegion(String tiledTextureRegionName){
		ResourceDescriptorsManager pResDscMng = ResourceDescriptorsManager.getInstance();
		DynamicTiledTextureRegionDescriptor pTRDsc = pResDscMng.getDynamicTiledTextureRegion(tiledTextureRegionName);
		
		if(pTRDsc == null)
		 	throw new IllegalArgumentException("In loadDynamicTiledTextureRegion: there is no description for the requested texture = " + tiledTextureRegionName);
		
		DynamicTiledTextureRegion newDinamicTextureRegion = new DynamicTiledTextureRegion();	
		
		//Create the texture
		TextureDescriptor pTextgDsc = pResDscMng.getTextureDescriptor(pTRDsc.textureName);
		
		newDinamicTextureRegion.setTextureAtlas( new BitmapTextureAtlas(mEngine.getTextureManager(),
				pTextgDsc.Parameters[ResTags.R_A_WIDTH_IDX], pTextgDsc.Parameters[ResTags.R_A_HEIGHT_IDX], 
				TextureOptions.BILINEAR_PREMULTIPLYALPHA));
		
		//create the all the dynamic texture
		newDinamicTextureRegion.createFromAsset(pTRDsc.filename, pTRDsc.column, pTRDsc.row, this.mCtx);

		//Save in map 
		this.mapConfigTiledTextureRegions.put(pTRDsc.Name, newDinamicTextureRegion);
		
		return newDinamicTextureRegion;
	}	
	public synchronized ITiledTextureRegion getTiledTextureRegion(String tiledTextureRegionName){
		ITiledTextureRegion theTiledTextureRegion = this.mapTiledTextureRegions.get(tiledTextureRegionName);
		//if the texture region is not already loaded in the resource manager load it
		if(theTiledTextureRegion==null) 
			theTiledTextureRegion = loadTiledTextureRegion(tiledTextureRegionName);
		//return the found or loaded texture region
		return theTiledTextureRegion;
	}
	public synchronized ITiledTextureRegion getDinamicTiledTextureRegion(String tiledTextureRegionName,String resourceName,int nbCol, int nbRow){
		DynamicTiledTextureRegion theDinamicTiledTextureRegion = this.mapConfigTiledTextureRegions.get(tiledTextureRegionName);
		
		//if the texture region is not already loaded in the resource manager load it
		if(theDinamicTiledTextureRegion==null) 
			theDinamicTiledTextureRegion = loadDynamicTiledTextureRegion(tiledTextureRegionName);
		
		//check that the resourceName is the requested with the exact nb of tiles(col & row) and if no reload it
		if(!theDinamicTiledTextureRegion.isSameAs(resourceName, nbCol, nbRow)) 
			theDinamicTiledTextureRegion.updateFromAsset(resourceName, nbCol, nbRow, this.mCtx); // => this action generate a crash
			//theDinamicTiledTextureRegion = loadDynamicTiledTextureRegion(tiledTextureRegionName);
	
		//return the found or loaded texture region
		return theDinamicTiledTextureRegion.getITiledTextureRegion();
	}
	// =============================================================================
	// MUSIC
	// =============================================================================
	public Music loadMusic(String musicName){
		ResourceDescriptorsManager pResDscMng = ResourceDescriptorsManager.getInstance();
		MusicDescriptor pMusicDsc = pResDscMng.getMusicDescriptor(musicName);
		if(pMusicDsc == null)
			throw new IllegalArgumentException("In LoadMusic: there is no description for the requested music = " + musicName);

		//To load a texture region the manager load the texture and all child regions
		Music newMusic = null;
		try {
			newMusic = MusicFactory.createMusicFromAsset(mEngine.getMusicManager(), mCtx, 
					pMusicDsc.filename);
			this.mapMusics.put(musicName, newMusic);
			//return the texture region that has just been loaded
			return newMusic;
		} catch (IllegalStateException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	public Music getMusic(String musicName){
		Music theMusic = this.mapMusics.get(musicName);
		//if the texture region is not already loaded in the resource manager load it
		if(theMusic==null) 
			theMusic = loadMusic(musicName);
		//return the found or loaded texture region
		return theMusic;
	}
	// =============================================================================
	// SOUND
	// =============================================================================
	public synchronized SoundContainer loadSound(String soundName){
		Log.d(TAG,"loadSound " + soundName);
		ResourceDescriptorsManager pResDscMng = ResourceDescriptorsManager.getInstance();
		SoundDescriptor pSoundDsc = pResDscMng.getSoundDescriptor(soundName);
		if(pSoundDsc == null)
			throw new IllegalArgumentException("In LoadSound: there is no sound description for the requested sound = " + soundName);

		//To load a texture region the manager load the texture and all child regions
		SoundContainer newSound = new SoundContainer();
		newSound.createFromAsset(pSoundDsc.filename, pSoundDsc.duration);
		this.mapSound.put(soundName, newSound);
		return newSound;
	}
	public SoundContainer getSound(String soundName){
		SoundContainer theSound = this.mapSound.get(soundName);
		//if the texture region is not already loaded in the resource manager load it
		if(theSound==null) 
			theSound = loadSound(soundName);
		//return the found or loaded texture region
		return theSound;
	}
	public String getStringResourceByName(String aString) {
	    String packageName = this.mCtx.getPackageName();
	    int resId = mCtx.getResources().getIdentifier(aString, "string", packageName);
	    if (resId == 0) {
	        return aString;
	    } else {
	        return mCtx.getString(resId);
	    }
	}
}
