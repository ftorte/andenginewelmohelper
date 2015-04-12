package com.welmo.andengine.scenes.components.interfaces;

import java.util.ArrayList;

public interface IActivitySceneListener {
		public void unZoom();
		public void setIActivitySceneListener(IActivitySceneListener pListener);
		public boolean onFatherScene();
		public boolean onChangeScene(String nextSceneName); // Sprite call this interface to inform scene parent that has been clicked
		public boolean onChangeChildScene(String nextScene);
		public boolean onLaunchChildScene(String nextScene, ArrayList<String> parameters);
		public void onCloseChildScene();
		public void onReloadScene();
		public void onGoToMenu();
		public void onGoToNextLevel();
		public boolean checkLicence(String sLicence);
}
