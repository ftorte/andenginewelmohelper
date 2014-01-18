package com.welmo.andengine.utility;

public class AsyncResourcesScenesLoader extends Thread{


	// ===========================================================
	// Fields
	// ===========================================================

	IAsyncCallBack _params;

	// ===========================================================
	// Inherited Methods
	// ===========================================================

	public void setupTaskToLoadResource(IAsyncCallBack params){
		this._params = params;
	}
	@Override
	public void run(){
		_params.workToDo();
		_params.onComplete();
	}
}