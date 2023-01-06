package ca.uhn.fhir.rest.gclient;

import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

public interface IPatchWithBody extends IPatchExecutable {

	/**
	 * Build a conditional URL using fluent constants on resource types
	 *
	 * @param theResourceType
	 *           The resource type to patch (e.g. "Patient.class")
	 */
	IPatchWithQuery conditional(Class<? extends IBaseResource> theResourceType);

	/**
	 * Build a conditional URL using fluent constants on resource types
	 * 
	 * @param theResourceType
	 *           The resource type to patch (e.g. "Patient")
	 */
	IPatchWithQuery conditional(String theResourceType);

	/**
	 * Specifies that the update should be performed as a conditional create
	 * against a given search URL.
	 *
	 * @param theSearchUrl
	 *           The search URL to use. The format of this URL should be of the form <code>[ResourceType]?[Parameters]</code>,
	 *           for example: <code>Patient?name=Smith&amp;identifier=13.2.4.11.4%7C847366</code>
	 */
	IPatchExecutable conditionalByUrl(String theSearchUrl);

	/**
	 * The resource ID to patch (must include both a resource type and an ID, e.g. <code>Patient/123</code>)
	 */
	IPatchExecutable withId(IIdType theId);

	/**
	 * The resource ID to patch (must include both a resource type and an ID, e.g. <code>Patient/123</code>)
	 */
	IPatchExecutable withId(String theId);

}
