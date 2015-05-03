package com.welmo.andengine.managers;

import java.util.HashMap;

import org.andengine.opengl.font.Font;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferenceManager {

	public enum STDPreferences {GLOBAL_VARIABLES}
	// ===========================================================
	// Fields
	// ===========================================================
	protected HashMap<String, SharedPreferences>		spPreferenceMap = null;
	protected static SharedPreferenceManager 			pInstance		= null;
	protected Context									ctxTheContext	= null;
	
	
	// ===========================================================
	// private functions
	// ===========================================================
	private SharedPreferenceManager(Context ctx){
		spPreferenceMap = new HashMap<String, SharedPreferences>(); 
		ctxTheContext = ctx;
	}
	
	// ===========================================================
	// public functions
	// ===========================================================
	public synchronized static SharedPreferenceManager getInstance(Context ctx){
		
		if(pInstance== null)
			pInstance = new SharedPreferenceManager(ctx);
		
		return pInstance;
	}
	
	public SharedPreferences getSharedPreferences(String strSourcePreferences){
		SharedPreferences sp = null;
		if((sp = spPreferenceMap.get(strSourcePreferences)) == null){
			sp = ctxTheContext.getSharedPreferences(strSourcePreferences, Context.MODE_PRIVATE);
			if(sp != null)
				spPreferenceMap.put(strSourcePreferences, sp);
		}
		return sp;
	}
}
