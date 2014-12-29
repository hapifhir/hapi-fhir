package ca.uhn.fhir.rest.gclient;

import org.hl7.fhir.instance.model.IBaseResource;

public interface IReadExecutable<T extends IBaseResource> extends IClientExecutable<IReadExecutable<T>, T>{

	/**
	 * Send an "If-None-Match" header containing <code>theVersion</code>, which requests
	 * that the server return an "HTTP 301 Not Modified" if the newest version of the resource
	 * on the server has the same version as the version ID specified by <code>theVersion</code>.
	 * In this case, the client operation will perform the linked operation.
	 * <p>
	 * See the <a href="http://jamesagnew.github.io/hapi-fhir/doc_rest_etag.html">ETag Documentation</a>
	 * for more information.
	 * </p>
	 * @param theVersion The version ID (e.g. "123")
	 */
	IReadIfNoneMatch<T> ifVersionMatches(String theVersion);

}
