package org.equo.gen.model;

import java.util.ArrayList;
import java.util.List;

import org.equo.gen.Parser.Location;
import org.equo.gen.model.MetaModuleResolver.MetaPeerResolver;


public class MetaPeer {
	
	private String name;
	private String model;
	private String entity;
	
	private List<IMetaField> childs = new ArrayList<IMetaField>();
	private List<MetaField> fields = new ArrayList<MetaField>();
	
	private List<Detail> details = new ArrayList<Detail>();
	private List<Lookup> lookups = new ArrayList<Lookup>();
	
	private Location location;
	private boolean resolved;

	public MetaPeer(String name, String model, String entity) {
		this.name = name;
		this.model = model;
		this.entity = entity;
	}

	public String getName() {
		return name;
	}

	public String getEntity() {
		return entity;
	}

	public String getModel() {
		return model;
	}

	public void addDeclaredField(IMetaField field) {
		childs.add(field);
	}
	
	public MetaField getDeclaredField(String name) {
		for (IMetaField field : childs) {
			if (field instanceof MetaField) {
				MetaField f = (MetaField) field;
				if (name.equals(f.getId())) {
					return f;
				}
			}
		}
		return null;
	}
	
	public void addField(MetaField field) {
		fields.add(field);
	}
	
	public List<MetaField> getFields() {
		return fields;
	}
	
	public MetaField getField(String name) {
		for (MetaField field : fields) {
			if (name.equals(field.getId())) {
				return field;
			}
		}
		return null;
	}
	
	public List<MetaField> getPrimaryKeyFields() {
		ArrayList<MetaField> pkFields = new ArrayList<MetaField>();
		for (MetaField field : fields) {
			if (field.isPrimaryKey()) {
				pkFields.add(field);
			}
		}
		return pkFields;
	}
	
	public void addEntity(MetaEntity entity2) {
		// TODO Auto-generated method stub
		
	}
	
	public void addLookup(Lookup lookup) {
		lookups.add(lookup);
	}

	public List<Lookup> getLookups() {
		return lookups;
	}
	
	public void addDetail(Detail detail) {
		details.add(detail);
	}
	
	public List<Detail> getDetails() {
		return details;
	}
	
	public void setLocation(Location location) {
		this.location = location;
	}

	public boolean isResolved() {
		return resolved;
	}

	public void setResolved(boolean resolved) {
		this.resolved = resolved;
	}

	public void accept(MetaPeerResolver resolver) throws MetadataException {
		for (IMetaField child : childs) {
			child.accept(resolver);
		}
		resolver.resolveEnd();
	}
	
	@Override
	public String toString() {
		return name;
	}

}
