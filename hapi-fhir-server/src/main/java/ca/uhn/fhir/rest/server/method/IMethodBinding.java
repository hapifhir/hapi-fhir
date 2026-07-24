package ca.uhn.fhir.rest.server.method;

import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.server.IRestfulServer;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;

import java.io.IOException;

public interface IMethodBinding {
	MethodMatchEnum incomingServerRequestMatchesMethod(RequestDetails theRequest);

	/**
	 *  Which FHIR operation does this binding support?
	 *  Takes the requestDetails so the same binding inspect the caller and support multiple actions.
	 *  E.g. read and vread.
	 *
	 * @return the operation of this binding for this request.
	 */
	default RestOperationTypeEnum getRestOperationType(RequestDetails theRequestDetails) {
		return getRestOperationType();
	}

	/**
	 * A simpler version of the above for implementors that are not polymorphic, or for servers that are
	 * not standard FHIR REST (e.g. JAXRs, or GraphQL).
	 * @return the operation of this handler
	 */
	RestOperationTypeEnum getRestOperationType();

	Object invokeServer(IRestfulServer<?> theServer, RequestDetails theRequest)
			throws BaseServerResponseException, IOException;

	/**
	 * Get to describe and identify this binding.
	 * Used to detect and warn of duplicate bindings.
	 */
	String getBindingKey();

	/**
	 * The "target" of this binding.
	 * If this binding extends BaseMethodBinding, then the provider will be the annotated provider object.
	 * Other bindings should return the "target" of this binding, as applicable.
	 * @return the object handling this binding.
	 */
	Object getProvider();

	/**
	 * The resource name for type and instance methods, or null for server methods.
	 * @return the target resource name or null for server methods.
	 */
	String getResourceName();

	// fixme move this stuff to a conformance-builder sibling

	boolean isSupportsConditional();
	/** for conformance */
	boolean isSupportsConditionalMultiple();

	/**
	 * Can this binding handle this request?
	 * @return if this binding can handle this request.
	 */
	default void close() {
		// default empty close()
	}
}
