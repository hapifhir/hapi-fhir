package ca.uhn.fhir.rest.gclient;

import org.hl7.fhir.instance.model.api.IBaseResource;

public interface IOperationUntypedWithInput<T> extends IClientExecutable<IOperationUntypedWithInput<T>, T> {

	/**
	 * The client should invoke this method using an HTTP GET instead of an HTTP POST. Note that
	 * according the the FHIR specification, all methods must support using the POST method, but
	 * only certain methods may support the HTTP GET method, so it is generally not necessary
	 * to use this feature. 
	 * <p>
	 * If you have a specific reason for needing to use a GET however, this method will enable it.
	 * </p>
	 */
	IOperationUntypedWithInput<T> useHttpGet();

	/**
	 * If this operation returns a single resource body as its return type instead of a <code>Parameters</code>
	 * resource, use this method to specify that resource type. This is useful for certain
	 * operations (e.g. <code>Patient/NNN/$everything</code>) which return a bundle instead of
	 * a Parameters resource.
	 */
	<R extends IBaseResource> IOperationUntypedWithInput<R> returnResourceType(Class<R> theReturnType);

}
