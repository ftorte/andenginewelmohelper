package com.welmo.andengine.utility;

import org.andengine.audio.exception.AudioException;
import org.andengine.audio.sound.exception.SoundReleasedException;

import com.welmo.andengine.managers.ResourcesManager.SoundContainer;
import com.welmo.andengine.managers.ResourcesManager.SoundType;

import android.util.Log;


public class SoundTaskPoolSequence extends Thread
{
	private static String TAG="SoundTaskPoolSequence";
	
	protected SoundContainer[] 	params;
	protected ICallBack 		mCallBack;
	
	public SoundTaskPoolSequence(){
		params=null;
	}
	public void setup(SoundContainer...sounds){
		params = sounds;
	}
	public void setup(ICallBack CallBack,SoundContainer...sounds){
		params = sounds;
		if (CallBack instanceof ICallBack)
			mCallBack = CallBack;
	}
	@Override
	public void run(){
		try {     
			if (mCallBack instanceof ICallBack)
				mCallBack.onPreExecution();
			
			if(params != null){
				for(int index=0; index < params.length; index++){
					//TODO add configurable management of volume
					//TODO add exception for null sound
					if(params[index] != null){
						Log.i(TAG,"Play Sound: " + index);
						if(params[index].getType() == SoundType.SOUND){
							if (params[index].getTheSound().isReleased())
								return;
							params[index].getTheSound().setVolume(1000);
							params[index].getTheSound().play();
							Thread.sleep(params[index].getDuration());
						}
						else if(params[index].getType() == SoundType.PAUSE){
							Thread.sleep(params[index].getDuration());
						}
					}
				}
			}
			
			if (mCallBack instanceof ICallBack)
				mCallBack.onPostExecution();
			
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (AudioException e){
			e.printStackTrace();
		}catch (SoundReleasedException e){
			e.printStackTrace();
		}
	}
}