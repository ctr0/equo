package org.equo.gen.model;



public interface MetaModuleResolver {
	
	public MetaPeerResolver resolvePeer(MetaPeer peer) throws MetadataException;
	
	public interface MetaPeerResolver {
		
		public void resolveField(MetaField field) throws MetadataException;
		
		public void resolveField(MetaEntity foreign) throws MetadataException;
		
		public void resolveEnd() throws MetadataException;
	}
	
	public void resolveEnd() throws MetadataException;

}
