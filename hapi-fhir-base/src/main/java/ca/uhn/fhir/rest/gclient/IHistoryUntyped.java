package ca.uhn.fhir.rest.gclient;

import org.hl7.fhir.instance.model.api.IBaseBundle;

import ca.uhn.fhir.model.api.Bundle;

public interface IHistoryUntyped {

	/**
	 * Request that the method return a DSTU1 style bundle object. This method should only
	 * be called if you are accessing a DSTU1 server.
	 */
	IHistoryTyped<Bundle> andReturnDstu1Bundle();
	
	/**
	 * Request that the method return a Bundle resource (such as <code>ca.uhn.fhir.model.dstu2.resource.Bundle</code>).
	 * Use this method if you are accessing a DSTU2+ server.
	 */
	<T extends IBaseBundle> IHistoryTyped<T> andReturnBundle(Class<T> theType);
	
}
