package org.equo.gen.model;

import org.equo.gen.model.MetaModuleResolver.MetaPeerResolver;

public interface IMetaField {

	void accept(MetaPeerResolver resolver) throws MetadataException;
	
}
