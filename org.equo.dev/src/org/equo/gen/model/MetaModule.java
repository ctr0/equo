package org.equo.gen.model;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import org.equo.gen.model.MetaModuleResolver.MetaPeerResolver;

public class MetaModule {
	
	private String id;

	private String name;
	
	private String pkg;
	
	private Map<String, MetaDomain> domains = new LinkedHashMap<String, MetaDomain>();
	
	private Map<String, MetaDatasource> datasources = new LinkedHashMap<String, MetaDatasource>();
	
	public MetaModule() {}

	public MetaModule(String id, String name, String pkg) {
		this.id = id;
		this.name = name;
		this.pkg = pkg;
	}

	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getPackage() {
		return pkg;
	}

	public void setPackage(String pkg) {
		this.pkg = pkg;
	}
	
	public Collection<MetaDatasource> getDatasources() {
		return datasources.values();
	}
	
	public MetaDatasource getDatasource(String name) {
		return datasources.get(name);
	}

	public void addDatasource(MetaDatasource datasource) {
		datasources.put(datasource.getName(), datasource);
	}

	public void addDomain(MetaDomain domain) {
		domains.put(domain.getName(), domain);
	}

	public Collection<MetaDomain> getDomains() {
		return domains.values();
	}
	
	public MetaDomain getDomain(String name) {
		return domains.get(name);
	}

	public void accept(MetaModuleResolver resolver) throws MetadataException {
		for (MetaDatasource ds : datasources.values()) {
			for (MetaPeer peer : ds.getPeers()) {
				MetaPeerResolver r = resolver.resolvePeer(peer);
				if (r != null) {
					peer.accept(r);
				}
			}
		}
		resolver.resolveEnd();
	}
	
}