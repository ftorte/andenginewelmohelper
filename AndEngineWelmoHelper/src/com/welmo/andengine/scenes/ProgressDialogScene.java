package com.welmo.andengine.scenes;

import org.andengine.entity.IEntity;
import org.andengine.util.progress.IProgressListener;

import android.util.Log;



public class ProgressDialogScene extends ManageableScene implements IProgressListener{

	final static String TAG = "ProgressDialogScene";
	int progressPurcentage	= 0;
	
	@Override
	public void onProgressChanged(int pProgress) {
		progressPurcentage = pProgress;
		
		IEntity thesprite = this.getChildByIndex(0);
		float newX = thesprite.getX()+10;
		float newY = thesprite.getY()+10;
		
		thesprite.setX(newX);
		thesprite.setY(newY);
		Log.i(TAG,"new position X=" + newX + "new position Y=" + newX );
	}
}
