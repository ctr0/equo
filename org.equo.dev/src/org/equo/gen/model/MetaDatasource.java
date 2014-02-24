package org.equo.gen.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class MetaDatasource {
	
	private String name;
	
	private Map<String, String> props = new HashMap<String, String>();
	
	private Map<String, MetaPeer> peers = new LinkedHashMap<String, MetaPeer>();

	public MetaDatasource(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setProperty(String name, String value) {
		props.put(name, value);
	}

	public void addPeer(MetaPeer peer) throws MetadataException {
		MetaPeer p = peers.put(peer.getName(), peer);
		if (p != null) {
			peers.put(p.getName(), p);
			throw new MetadataException("Duplicated peer name");
		}
	}
	
	public Collection<MetaPeer> getPeers() {
		return peers.values();
	}

}
