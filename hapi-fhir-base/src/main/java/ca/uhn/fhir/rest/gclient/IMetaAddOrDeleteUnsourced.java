package ca.uhn.fhir.rest.gclient;

import org.hl7.fhir.instance.model.api.IIdType;

public interface IMetaAddOrDeleteUnsourced {

	IMetaAddOrDeleteSourced onResource(IIdType theId);
	
}
