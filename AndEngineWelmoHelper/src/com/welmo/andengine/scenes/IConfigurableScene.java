package com.welmo.andengine.scenes;


import com.welmo.andengine.scenes.descriptors.ConfiguredSceneDescriptor;
// *******************************************************************************************
// A scene that implement i configurable must implement a configure method that get a list of 
// parameter and change the scene in accordance with the parameter passed. Each parameter is a 
// string that the scene know how to parse and manage
// *******************************************************************************************
public interface IConfigurableScene {
	public void configure(ConfiguredSceneDescriptor descriptor);
	public String getNameOfInstantiatedScene();
}
