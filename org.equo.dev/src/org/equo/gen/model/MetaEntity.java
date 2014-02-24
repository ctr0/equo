package org.equo.gen.model;

import java.util.ArrayList;
import java.util.List;

import org.equo.gen.model.MetaModuleResolver.MetaPeerResolver;

public class MetaEntity implements IMetaField {

	private String entity;
	private String model;
	private boolean pk;
	
	private ArrayList<MetaForeignSegment> segments;
	
	public MetaEntity(String entity, String model, Boolean pk) {
		this.entity = entity;
		this.model = model;
		if (pk != null) this.pk = pk;
		this.segments = new ArrayList<MetaForeignSegment>();
	}

	public void addSegment(MetaForeignSegment segment) {
		segments.add(segment);
	}

	public List<MetaForeignSegment> getSegments() {
		return segments;
	}

	public String getEntity() {
		return entity;
	}

	public String getModel() {
		return model;
	}

	public boolean isPrimaryKey() {
		return pk;
	}
	
	/* (non-Javadoc)
	 * @see org.ctro.jsql.dev.IMetaField#accept(org.ctro.jsql.dev.MetaModuleResolver.MetaPeerResolver)
	 */
	@Override
	public void accept(MetaPeerResolver resolver) throws MetadataException {
		resolver.resolveField(this);
	}
	
	public MetaForeignSegment getSegmentByForeignField(String name) {
		for (MetaForeignSegment segment : segments) {
			if (segment.getForeignField().equals(name)) {
				return segment;
			}
		}
		return null;
	}
}
