package com.welmo.andengine.scenes.components.interfaces;

public interface IActivitySceneListener {
		public void unZoom();
		public void setIActivitySceneListener(IActivitySceneListener pListener);
		public boolean onFatherScene();
		public boolean onChangeScene(String nextSceneName); // Sprite call this interface to inform scene parent that has been clicked
		public boolean onChangeChildScene(String nextScene);
}
