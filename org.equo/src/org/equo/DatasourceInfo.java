package org.equo;

import java.util.HashMap;

public class DatasourceInfo {
	
	private String name;
	
	private HashMap<String, String> properties;
	
	public DatasourceInfo(String name, HashMap<String, String> properties) {
		this.name = name;
		this.properties = properties;
	}
	
	public String getName() {
		return name;
	}

	public HashMap<String, String> getProperties() {
		return properties;
	}
	
	public String getProperty(String name) {
		return getProperty(false, name, null);
	}
	
	public String getProperty(String name, String defaults) {
		return getProperty(false, name, defaults);
	}
	
	public String getRequiredProperty(String name) throws IllegalArgumentException {
		return getProperty(true, name, null);
	}
	
	private String getProperty(boolean required, String name, String defaults) {
		String value = properties.get(name);
		if (required && value == null) {
			throw new IllegalArgumentException("Missing required data source property '" + name + "'");
		}
		if (value == null) return defaults;
		return value;
	}
}
