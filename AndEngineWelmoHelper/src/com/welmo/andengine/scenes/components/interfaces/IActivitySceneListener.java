package com.welmo.andengine.scenes.components.interfaces;


public interface IActivitySceneListener {
		public boolean onChangeScene(String nextSceneName); // Sprite call this interface to inform scene parent that has been clicked
		public void unZoom();
		public void setIActivitySceneListener(IActivitySceneListener pListener);
		public boolean onFatherScene();
}
