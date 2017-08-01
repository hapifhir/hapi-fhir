package ca.uhn.fhir.rest.gclient;

import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.PreferReturnEnum;

public interface ICreateTyped extends IClientExecutable<ICreateTyped, MethodOutcome> {
	
	/**
	 * @since HAPI 0.9 / FHIR DSTU 2
	 */
	ICreateWithQuery conditional();

	/**
	 * Specifies that the create should be performed as a conditional create
	 * against a given search URL.
	 *
	 * @param theSearchUrl The search URL to use. The format of this URL should be of the form <code>[ResourceType]?[Parameters]</code>,
	 *                     for example: <code>Patient?name=Smith&amp;identifier=13.2.4.11.4%7C847366</code>
	 * @since HAPI 0.9 / FHIR DSTU 2
	 */
	ICreateTyped conditionalByUrl(String theSearchUrl);

	/**
	 * Add a <code>Prefer</code> header to the request, which requests that the server include 
	 * or suppress the resource body as a part of the result. If a resource is returned by the server
	 * it will be parsed an accessible to the client via {@link MethodOutcome#getResource()}
	 * 
	 * @since HAPI 1.1
	 */
	ICreateTyped prefer(PreferReturnEnum theReturn);

}
