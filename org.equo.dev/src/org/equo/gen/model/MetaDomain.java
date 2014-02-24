package org.equo.gen.model;

import java.util.ArrayList;
import java.util.List;

import org.equo.gen.Parser.Location;

public class MetaDomain {

	private String name;
	private String type;
	private Integer length = -1;
	private Integer scale = -1;
	private Boolean nullable = true;
	private String model;
	
	private ArrayList<PossibleValue> possibleValues = new ArrayList<MetaDomain.PossibleValue>();
	private Location location;

	public MetaDomain(String name, String type, 
			Integer length, Integer scale, Boolean nullable,
			String model) {
		
		this.name = name;
		this.model = model;
		this.type = type;
		if (length != null) this.length = length;
		if (scale != null) this.scale = scale;
		if (nullable != null) this.nullable = nullable;
	}

	public static class PossibleValue {
		
		private String value;
		private String name;
		
		public PossibleValue(String value, String name) {
			this.value = value;
			this.name = name;
		}

		public String getValue() {
			return value;
		}

		public String getName() {
			return name;
		}
	}

	public void addPossibleValue(String value, String name) {
		possibleValues.add(new PossibleValue(value, name));
	}

	public List<PossibleValue> getPossibleValues() {
		return possibleValues;
	}

	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}

	public int getLength() {
		return length;
	}

	public int getScale() {
		return scale;
	}

	public boolean isNullable() {
		return nullable;
	}

	public String getModel() {
		return model;
	}
	
	public void setLocation(Location location) {
		this.location = location;
	} 
	
	@Override
	public int hashCode() {
		return getName().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj instanceof MetaDomain) {
			return getName().equals(((MetaDomain) obj).getName());
		}
		return false;
	}
}
