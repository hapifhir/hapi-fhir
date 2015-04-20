package ca.uhn.fhir.rest.gclient;

import org.hl7.fhir.instance.model.api.IBaseConformance;

public interface IFetchConformanceUntyped {

	/**
	 * Retrieve the conformance statement using the given model type
	 */
	<T extends IBaseConformance> IFetchConformanceTyped<T> ofType(Class<T> theType);

}
