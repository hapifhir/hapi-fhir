package ca.uhn.fhir.rest.gclient;

import org.hl7.fhir.instance.model.api.IBaseMetaType;
import org.hl7.fhir.instance.model.api.IIdType;

public interface IMetaGetUnsourced<T extends IBaseMetaType> {

	IClientExecutable<IClientExecutable<?,?>, T> fromServer();
	
	IClientExecutable<IClientExecutable<?,?>, T> fromType(String theResourceName);

	/**
	 * Get the meta from a resource instance by ID. 
	 * 
	 * @param theId The ID. Must contain both a resource type and an ID part
	 */
	IClientExecutable<IClientExecutable<?,?>, T> fromResource(IIdType theId);


}
