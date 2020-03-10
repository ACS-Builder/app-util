package com.kasafal.classloader;

import org.apache.log4j.xml.DOMConfigurator;

public class ApplicationClassLoader extends ClassLoader {
	static {
			DOMConfigurator.configure(System.getProperty("logConfigFile"));	
	}

	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		return Class.forName(name);
	}
}
