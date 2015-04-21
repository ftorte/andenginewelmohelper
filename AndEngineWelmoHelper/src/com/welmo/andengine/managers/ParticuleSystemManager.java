package com.welmo.andengine.managers;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.andengine.entity.particle.SpriteParticleSystem;
import org.andengine.entity.particle.emitter.CircleOutlineParticleEmitter;
import org.andengine.entity.particle.emitter.IParticleEmitter;
import org.andengine.entity.particle.initializer.AlphaParticleInitializer;
import org.andengine.entity.particle.initializer.BlendFunctionParticleInitializer;
import org.andengine.entity.particle.initializer.ColorParticleInitializer;
import org.andengine.entity.particle.initializer.RotationParticleInitializer;
import org.andengine.entity.particle.initializer.VelocityParticleInitializer;
import org.andengine.entity.particle.modifier.AlphaParticleModifier;
import org.andengine.entity.particle.modifier.ColorParticleModifier;
import org.andengine.entity.particle.modifier.ExpireParticleInitializer;
import org.andengine.entity.particle.modifier.ScaleParticleModifier;
import org.andengine.entity.sprite.Sprite;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

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
import com.welmo.andengine.scenes.descriptors.ParserXMLSceneDescriptor;
import com.welmo.andengine.scenes.descriptors.ScnTags;
import com.welmo.andengine.scenes.descriptors.components.ParticuleSystemDescriptor;
import com.welmo.andengine.scenes.descriptors.components.ParticuleSystemDescriptor.ParticleInitializerDescriptor;
import com.welmo.andengine.scenes.descriptors.components.ParticuleSystemDescriptor.ParticleModifiersDescriptor;
import com.welmo.andengine.utility.ScreenDimensionHelper;

import android.content.Context;
import android.graphics.Typeface;
import android.opengl.GLES20;
import android.util.Log;



public class ParticuleSystemManager {
	
	private static final String 							TAG ="ParticuleSystemManager";
	//--------------------------------------------------------
	// Variables
	//--------------------------------------------------------
	// Resources
	protected HashMap<String,SpriteParticleSystem> 			hmParticuleSystem 		= null;;
	Map<String, ParticuleSystemDescriptor>					mapParticuleSystemMap 	= null;

	
	//--------------------------------------------------------
	// Inner Classes 
	//--------------------------------------------------------
	public class ParserXMLParticuleSystemDescriptor extends DefaultHandler {
		ParticuleSystemManager 			pParticuleSystemMgr = null;
		ParticuleSystemDescriptor 		pDescriptor = null;
		@Override
		public void processingInstruction(String target, String data) throws SAXException {
			super.processingInstruction(target, data);
		}
		public ParserXMLParticuleSystemDescriptor(ParticuleSystemManager theManager) {
			super();
			pParticuleSystemMgr = theManager;
		}
		@Override
		public void startDocument() throws SAXException {
			super.startDocument();
		}
		@Override
		public void startElement(String uri, String localName, String name,	Attributes attributes) throws SAXException {
			if (localName.equalsIgnoreCase(ScnTags.S_PARTICULESYSTEM)){
				pDescriptor = new ParticuleSystemDescriptor();
				pDescriptor.readXMLDescription(attributes);
				pParticuleSystemMgr.addDescrptor(pDescriptor);
				return;			
			}
		}
		@Override
		public void endElement(String uri, String localName, String name) throws SAXException {		
			if (localName.equalsIgnoreCase(ScnTags.S_PARTICULESYSTEM)){
				pDescriptor  = null;
			}
		}
		public void characters(char[] ch,int start, int length)	throws SAXException{
		}
	}
	
	// singleton Instance
	private static ParticuleSystemManager 					mInstance=null;
	//--------------------------------------------------------
	// Constructors
	//--------------------------------------------------------
	private ParticuleSystemManager(){
		hmParticuleSystem =  new  HashMap<String,SpriteParticleSystem>();
		mapParticuleSystemMap = new HashMap<String, ParticuleSystemDescriptor>();
	}
	public synchronized  static ParticuleSystemManager getInstance(){
		if(mInstance == null)
			mInstance = new  ParticuleSystemManager();
		return mInstance;
	}
	public SpriteParticleSystem addParticuleSystem(String particuleName, ParticuleSystemDescriptor pPSD){
		if(hmParticuleSystem.containsKey(particuleName))
			return hmParticuleSystem.get(particuleName);
		
		ResourcesManager pRM = ResourcesManager.getInstance();
		
		IParticleEmitter pIPEmiter = null;
		switch(pPSD.getEmitterType()){
		case CIRCULAR:
			pIPEmiter = new CircleOutlineParticleEmitter(pPSD.getCenterX(), pPSD.getCenterY(), pPSD.getRadiusX(),pPSD.getRadiusY());
			break;
		default:
			break;
			
		}

		SpriteParticleSystem particleSystem = new SpriteParticleSystem(pIPEmiter, pPSD.getRateMin(), pPSD.getRateMax(),pPSD.getParticelsMax(), 
				pRM.getTextureRegion(pPSD.getTextureRegionName()),pRM.getEngine().getVertexBufferObjectManager());


		//Default Initialized
		particleSystem.addParticleInitializer(new BlendFunctionParticleInitializer<Sprite>(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE));
		
		//Readt Initializers Descriptors
		Iterator<ParticleInitializerDescriptor> itPartInitilizer=pPSD.getInitializers().iterator();
		
		while (itPartInitilizer.hasNext()){
			ParticleInitializerDescriptor pInitializer = itPartInitilizer.next();
			switch (pInitializer.type){
			case COLOR:
				particleSystem.addParticleInitializer(new ColorParticleInitializer<Sprite>(pInitializer.pRed, pInitializer.pGreen, pInitializer.pBlue));
				break;
			case ALPHA:
				particleSystem.addParticleInitializer(new AlphaParticleInitializer<Sprite>(pInitializer.pAlfa));
				break;
			case VELOCITY:
				particleSystem.addParticleInitializer(new VelocityParticleInitializer<Sprite>(pInitializer.pMinVelX, pInitializer.pMaxVelX, pInitializer.pMinVelY, pInitializer.pMaxVelY));
				break;
			case ROTATION:
				particleSystem.addParticleInitializer(new RotationParticleInitializer<Sprite>(pInitializer.pMinRotation, pInitializer.pMaxRotation));
				break;
			case EXPIRE:
				particleSystem.addParticleInitializer(new ExpireParticleInitializer<Sprite>(pInitializer.pLifeTime));
				break;

			}
		}
		
		//Readt Initializers Modifiers
		Iterator<ParticleModifiersDescriptor> itPartModifiers=pPSD.getModifiers().iterator();

		while (itPartInitilizer.hasNext()){
			ParticleModifiersDescriptor pModifier = itPartModifiers.next();
			switch (pModifier.type){
			case SCALE:
				particleSystem.addParticleModifier(new ScaleParticleModifier<Sprite>(pModifier.pFromTime, pModifier.pToTime, pModifier.pFromScale, pModifier.pToScale));				break;
			case COLOR:
				particleSystem.addParticleModifier(new ColorParticleModifier<Sprite>(pModifier.pFromTime, pModifier.pToTime, pModifier.pFromRed, pModifier.pToRed, pModifier.pFromGreen, pModifier.pToGreen, pModifier.pFromBlue, pModifier.pToBlue));
				break;
			case ALPHA:
				particleSystem.addParticleModifier(new AlphaParticleModifier<Sprite>(pModifier.pFromTime, pModifier.pToTime, pModifier.pFromAlfa, pModifier.pToAlfa));
				break;
			}
		}	
		//by default the partucule system is disabled and is addedd to the availables PS map
		particleSystem.setParticlesSpawnEnabled(false);
		hmParticuleSystem.put(particuleName, particleSystem);
		
		return particleSystem;
	}

	public SpriteParticleSystem getParticuleSytem(String particuleSystemName){
		if(hmParticuleSystem.containsKey(particuleSystemName))
			return hmParticuleSystem.get(particuleSystemName);
		return null;
	}
	
	public void readParticuleSystemDescriptions(Context theCtx, String[] filesNames){
		try { 
			SAXParserFactory spf = SAXParserFactory.newInstance(); 
			SAXParser sp = spf.newSAXParser(); 
			XMLReader xr = sp.getXMLReader(); 
			//parse resources descriptions
			ParserXMLParticuleSystemDescriptor particuleSystelDescriptionHandler = new ParserXMLParticuleSystemDescriptor(this); 
			xr.setContentHandler(particuleSystelDescriptionHandler); 
			if(filesNames != null){
				for (String filename:filesNames ){
					Log.i(TAG,"Read Scene Description => " + filename);
					xr.parse(new InputSource(theCtx.getAssets().open(filename))); 
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
	public void addDescrptor(ParticuleSystemDescriptor pDescriptor){
		mapParticuleSystemMap.put(pDescriptor.getName(), pDescriptor);
	}
}
