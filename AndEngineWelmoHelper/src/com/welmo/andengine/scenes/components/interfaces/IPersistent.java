package com.welmo.andengine.scenes.components.interfaces;

import android.content.SharedPreferences;

public interface IPersistent {
	public void doLoad(SharedPreferences sp, String url_root);
	public void doSave(SharedPreferences sp, String url_root);
}
