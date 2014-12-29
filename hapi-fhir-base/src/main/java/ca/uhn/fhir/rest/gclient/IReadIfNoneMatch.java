package ca.uhn.fhir.rest.gclient;

import org.hl7.fhir.instance.model.IBaseResource;

import ca.uhn.fhir.rest.server.exceptions.NotModifiedException;

public interface IReadIfNoneMatch<T extends IBaseResource> {

	/**
	 * If the server responds with an <code>HTTP 301 Not Modified</code>,
	 * return the given instance.
	 */
	IReadExecutable<T> returnResource(T theInstance);
	
	/**
	 * If the server responds with an <code>HTTP 301 Not Modified</code>,
	 * return <code>null</code>.
	 */
	IReadExecutable<T> returnNull();
	
	/**
	 * If the server responds with an <code>HTTP 301 Not Modified</code>,
	 * throw a {@link NotModifiedException}.
	 */
	IReadExecutable<T> throwNotModifiedException();
	
}
