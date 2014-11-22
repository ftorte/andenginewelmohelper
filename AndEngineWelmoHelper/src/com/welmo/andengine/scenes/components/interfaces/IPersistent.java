package com.welmo.andengine.scenes.components.interfaces;

import com.welmo.andengine.managers.SharedPreferenceManager;

import android.content.SharedPreferences;

public interface IPersistent {
	public void doLoad(SharedPreferences sp, String url_root);
	public void doSave(SharedPreferences sp, String url_root);
	public void doLoad();
	public void doSave();
	public void doLoad(SharedPreferenceManager sp);
	public void doSave(SharedPreferenceManager sp);
	public void setSharedPreferenceManager(SharedPreferenceManager sp);
}
