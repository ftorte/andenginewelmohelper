package com.welmo.andengine.scenes;

import java.util.ArrayList;

import org.andengine.entity.IEntity;

import com.welmo.andengine.scenes.components.TextComponent;
import com.welmo.andengine.scenes.descriptors.ConfiguredSceneDescriptor;

public class InAppPurchasing extends ManageableCameraScene implements IConfigurableScene{

	@Override
	public void configure(ConfiguredSceneDescriptor descriptor) {
		return;
	}

	@Override
	public String getNameOfInstantiatedScene() {
		//Not applicable for this modal scene
		return null;
	}

	@Override
	public void setParameter(ArrayList<String> parameterList) {
		
	
	}
}
