package ca.uhn.fhir.rest.gclient;

import org.hl7.fhir.instance.model.api.IIdType;

public interface IUpdateTyped extends IUpdateExecutable {

	IUpdateExecutable withId(IIdType theId);

	IUpdateExecutable withId(String theId);

	/**
	 * Specifies that the update should be performed as a conditional create
	 * against a given search URL.
	 *
	 * @param theSearchUrl The search URL to use. The format of this URL should be of the form <code>[ResourceType]?[Parameters]</code>,
	 *                     for example: <code>Patient?name=Smith&amp;identifier=13.2.4.11.4%7C847366</code>
	 * @since HAPI 0.9 / FHIR DSTU 2
	 */
	IUpdateTyped conditionalByUrl(String theSearchUrl);

	/**
	 * @since HAPI 0.9 / FHIR DSTU 2
	 */
	IUpdateWithQuery conditional();

}
