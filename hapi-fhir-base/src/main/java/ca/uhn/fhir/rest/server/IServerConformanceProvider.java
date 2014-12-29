package ca.uhn.fhir.rest.server;

import javax.servlet.http.HttpServletRequest;

import org.hl7.fhir.instance.model.IBaseResource;

public interface IServerConformanceProvider<T extends IBaseResource> {

	/**
	 * Actually create and return the conformance statement
	 * 
	 * See the class documentation for an important note if you are extending this class
	 */
	public abstract T getServerConformance(HttpServletRequest theRequest);

}