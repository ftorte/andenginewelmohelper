package com.welmo.andengine.scenes.descriptors.components;

import java.util.ArrayList;

import com.welmo.andengine.resources.descriptors.ResourceDescriptor;
import com.welmo.andengine.scenes.descriptors.SceneDescriptor;

public class MultiViewSceneDescriptor extends ResourceDescriptor{
		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Member Variables
		// ===========================================================
		public ArrayList<SceneDescriptor>	scScenes=null;

		// ===========================================================
		// Constructor
		// ===========================================================


		public MultiViewSceneDescriptor() {
			scScenes=new ArrayList<SceneDescriptor>();
		}


		@Override
		public String toString() {
			return "ResourceDescriptor" +  "]";
		}
}
