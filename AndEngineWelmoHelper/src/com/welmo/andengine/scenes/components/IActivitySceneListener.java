package com.welmo.andengine.scenes.components;


public interface IActivitySceneListener {
		public boolean onChangeScene(String nextSceneName); // Sprite call this interface to inform scene parentthat has been clicked
		public void unZoom();
		public void setIActivitySceneListener(IActivitySceneListener pListener);
}
