package com.welmo.andengine.scenes.components;

public interface IComponentLifeCycleListener {
		public void onStart();
		public void onEnd();
		public void onPause();
		public void onReset();
		public void onProgress();
}
