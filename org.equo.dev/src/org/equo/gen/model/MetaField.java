package org.equo.gen.model;

import org.equo.gen.Parser.Location;
import org.equo.gen.model.MetaModuleResolver.MetaPeerResolver;


public class MetaField implements IMetaField {
	
	private String name;
	private String domain;
	private String model;
	private boolean pk;
	private Location location;
	
	public MetaField(String name, String domain, String model, Boolean pk) {
		this.name = name == null ? domain : name;
		this.domain = domain;
		this.model = model;
		if (pk != null) this.pk = pk;
	}
	
	MetaField(MetaField field) {
		this.name = field.name;
		this.domain = field.domain;
		this.model = field.model;
		this.pk = field.pk;
	}

	public String getId() {
		if (name != null) return name;
		return domain;
	}
	
	public String getName() {
		return name;
	}

	public String getDomain() {
		return domain;
	}

	public String getModel() {
		return model;
	}

	public boolean isPrimaryKey() {
		return pk;
	}
	
	public void setPrimaryKey(boolean pk) {
		this.pk = pk;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setModel(String model) {
		this.model = model;
	}
	
	public void setLocation(Location location) {
		this.location = location;
	}

	@Override
	public void accept(MetaPeerResolver resolver) throws MetadataException {
		resolver.resolveField(this);
	}

	@Override
	public int hashCode() {
		return getId().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj instanceof MetaField) {
			return getId().equals(((MetaField) obj).getId());
		}
		return false;
	}

	@Override
	public String toString() {
		return getId();
	}

}
