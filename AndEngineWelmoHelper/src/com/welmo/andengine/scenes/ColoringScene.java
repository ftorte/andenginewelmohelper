package com.welmo.andengine.scenes;

import java.util.Map;

import org.andengine.engine.camera.SmoothCamera;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.input.touch.TouchEvent;

import com.welmo.andengine.scenes.descriptors.components.BasicObjectDescriptor;

public class ColoringScene extends ManageableScene implements IConfigurableScene{
			
	@Override
	public void configure(Map<String,String> parameterList) {
		
		this.bImplementPinchAndZoom = true;
		
		this.resetScene();
		int[] imgDimWH = new int[2];
		
		if(parameterList.containsKey("image_dimension_X") && parameterList.containsKey("image_dimension_Y")){
			imgDimWH[0] = Integer.parseInt(parameterList.get("image_dimension_X"));
			imgDimWH[1] = Integer.parseInt(parameterList.get("image_dimension_Y"));
			BasicObjectDescriptor objDsc = (BasicObjectDescriptor) this.pSCDescriptor.pChild.get(200);
			objDsc.getIDimension().setWidth(imgDimWH[0]);
			objDsc.getIDimension().setHeight(imgDimWH[0]);
		}
		this.loadScene(this.pSCDescriptor);
		this.resetScene();
			
	}
}
