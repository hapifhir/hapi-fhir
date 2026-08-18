/*-
 * #%L
 * HAPI FHIR - Server Framework
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package ca.uhn.fhir.rest.server.method;

import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.server.IRestfulServer;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;

import java.io.IOException;

/**
 * Abstract interface for RestfulServer handlers.
 * This interface should not be considered stable.
 * Implementors should expect changes until this interface is marked stable.
 */
public interface IMethodBinding {
	/**
	 * Can this binding handle this request?
	 * Note: at this stage of processing, RequestDetails.getRestOperationType() is not available.
	 *
	 * @return if this binding can handle this request.
	 */
	MethodMatchEnum incomingServerRequestMatchesMethod(RequestDetails theRequest);

	/**
	 *  Which FHIR operation does this binding support?
	 *  Takes the requestDetails so the same binding inspect the caller and support multiple actions.
	 *  E.g. read and vread.
	 *  This is used by RestfulServer to populate RequestDetails.getRestOperationType().
	 *
	 * @return the operation of this binding for this request.
	 */
	default RestOperationTypeEnum getRestOperationType(RequestDetails theRequestDetails) {
		return getRestOperationType();
	}

	/**
	 * A simpler version of the above for implementors that are not polymorphic, for servers that are
	 * not standard FHIR REST (e.g. JAXRs, or GraphQL), and for building conformance documents.
	 * @return the operation of this handler
	 */
	RestOperationTypeEnum getRestOperationType();

	/**
	 * Actually do the work.
	 * Implementors are expected to write the results to the output if required.
	 * See {@link BaseResourceReturningMethodBinding#callHooksAndWriteResponse}
	 * @param theServer
	 * @param theRequest
	 * @return
	 * @throws BaseServerResponseException
	 * @throws IOException
	 */
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

	// TODO post-release: separate invocation from conformance by moving this to a conformance-builder.  Maybe a visitor
	// pattern. There are several places in the conformance builder that reference specific classes like
	// OperationMethodBinding or SearchMethodBinding

	/**
	 * For conformance.
	 */
	boolean isSupportsConditional();

	/**
	 * For conformance.
	 */
	boolean isSupportsConditionalMultiple();

	/**
	 * Release any resources bound by this handler.
	 */
	default void close() {
		// default empty close()
	}
}
