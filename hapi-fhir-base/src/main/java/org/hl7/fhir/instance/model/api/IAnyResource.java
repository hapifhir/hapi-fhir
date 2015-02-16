package org.hl7.fhir.instance.model.api;

import org.hl7.fhir.instance.model.IBaseResource;

public interface IAnyResource extends IBaseResource {

	String getId();

	IAnyResource setId(String theId);

	IIdType getIdElement();

	IMetaType getMeta();
	
}
