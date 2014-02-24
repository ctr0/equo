package org.equo.gen.model;

import java.util.ArrayList;
import java.util.List;

public class Detail {
	
	public MetaPeer foreignPeer;
	
	public List<MetaField> foreignFields = new ArrayList<MetaField>();

	public Detail(MetaPeer foreignPeer) {
		this.foreignPeer = foreignPeer;
	}

	public MetaPeer getForeignPeer() {
		return foreignPeer;
	}

	public List<MetaField> getForeignFields() {
		return foreignFields;
	}

	public void addForeignField(MetaField foreignField) {
		this.foreignFields.add(foreignField);
	}

}
