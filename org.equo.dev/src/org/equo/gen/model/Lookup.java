package org.equo.gen.model;

import java.util.ArrayList;
import java.util.List;

public class Lookup {
	
	public MetaPeer foreignPeer;
	
	public List<MetaField> localFields = new ArrayList<MetaField>();
	
	Lookup(MetaPeer peer) {
		this.foreignPeer = peer;
	}

	public MetaPeer getForeignPeer() {
		return foreignPeer;
	}

	public void setForeignPeer(MetaPeer foreignPeer) {
		this.foreignPeer = foreignPeer;
	}

	public List<MetaField> getLocalFields() {
		return localFields;
	}

	public void addLocalField(MetaField foreignField) {
		this.localFields.add(foreignField);
	}

}
