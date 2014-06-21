package com.welmo.andengine.scenes.descriptors.interfaces;

import org.andengine.engine.Engine;
import org.xml.sax.Attributes;

import com.welmo.andengine.scenes.components.interfaces.IComponent;

/*
 * The IDescriptor Interface implement the default methods any descriprot must implement
 * readXMLDescription => Methods to read the attribute of a descriptor from and XML file
 */

public interface IDescriptor {
	public void readXMLDescription(Attributes attributes);
}
