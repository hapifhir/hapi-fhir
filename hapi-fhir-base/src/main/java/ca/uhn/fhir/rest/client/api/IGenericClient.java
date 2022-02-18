package ca.uhn.fhir.rest.client.api;

/*-
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseResource;

import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.UriDt;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.exceptions.FhirClientConnectionException;
import ca.uhn.fhir.rest.client.exceptions.FhirClientInappropriateForServerException;
import ca.uhn.fhir.rest.gclient.*;

public interface IGenericClient extends IRestfulClient {

	/**
	 * Fetch the capability statement for the server
	 */
	IFetchConformanceUntyped capabilities();

	/**
	 * Fluent method for the "create" operation, which creates a new resource instance on the server
	 */
	ICreate create();

	/**
	 * Fluent method for the "delete" operation, which performs a logical delete on a server resource
	 */
	IDelete delete();

	/**
	 * Retrieves the server's conformance statement
	 * 
	 * @deprecated As of HAPI 3.0.0 this method has been deprecated, as the operation is now called "capabilities". Use {@link #capabilities()} instead
	 */
	IFetchConformanceUntyped fetchConformance();

	/**
	 * Force the client to fetch the server's conformance statement and validate that it is appropriate for this client.
	 * 
	 * @throws FhirClientConnectionException
	 *            if the conformance statement cannot be read, or if the client
	 * @throws FhirClientInappropriateForServerException
	 *            If the conformance statement indicates that the server is inappropriate for this client (e.g. it implements the wrong version of FHIR)
	 */
	void forceConformanceCheck() throws FhirClientConnectionException;

	/**
	 * Implementation of the "history" method
	 */
	IHistory history();

	/**
	 * Loads the previous/next bundle of resources from a paged set, using the link specified in the "link type=next" tag within the atom bundle.
	 */
	IGetPage loadPage();

	/**
	 * Fluent method for the "meta" operations, which can be used to get, add and remove tags and other
	 * Meta elements from a resource or across the server.
	 * 
	 * @since 1.1
	 */
	IMeta meta();

	/**
	 * Implementation of the FHIR "extended operations" action
	 */
	IOperation operation();

	/**
	 * Fluent method for the "patch" operation, which performs a logical patch on a server resource
	 */
	IPatch patch();

	/**
	 * Fluent method for "read" and "vread" methods.
	 */
	IRead read();

	/**
	 * Implementation of the "instance read" method.
	 * 
	 * @param theType
	 *           The type of resource to load
	 * @param theId
	 *           The ID to load
	 * @return The resource
	 * 
	 * @deprecated Use {@link #read() read() fluent method} instead (deprecated in HAPI FHIR 3.0.0)
	 */
	@Deprecated
	<T extends IBaseResource> T read(Class<T> theType, String theId);

	/**
	 * Perform the "read" operation (retrieve the latest version of a resource instance by ID) using an absolute URL.
	 * 
	 * @param theType
	 *           The resource type that is being retrieved
	 * @param theUrl
	 *           The absolute URL, e.g. "http://example.com/fhir/Patient/123"
	 * @return The returned resource from the server
	 * @deprecated Use {@link #read() read() fluent method} instead (deprecated in HAPI FHIR 3.0.0)
	 */
	@Deprecated
	<T extends IBaseResource> T read(Class<T> theType, UriDt theUrl);

	/**
	 * Perform the "read" operation (retrieve the latest version of a resource instance by ID) using an absolute URL.
	 * 
	 * @param theUrl
	 *           The absolute URL, e.g. "http://example.com/fhir/Patient/123"
	 * @return The returned resource from the server
	 * @deprecated Use {@link #read() read() fluent method} instead (deprecated in HAPI FHIR 3.0.0)
	 */
	@Deprecated
	IBaseResource read(UriDt theUrl);

	/**
	 * Register a new interceptor for this client. An interceptor can be used to add additional logging, or add security headers, or pre-process responses, etc.
	 */
	@Override
	void registerInterceptor(Object theInterceptor);

	/**
	 * Search for resources matching a given set of criteria. Searching is a very powerful
	 * feature in FHIR with many features for specifying exactly what should be seaerched for
	 * and how it should be returned. See the <a href="http://www.hl7.org/fhir/search.html">specification on search</a>
	 * for more information.
	 */
	<T extends IBaseBundle> IUntypedQuery<T> search();

	/**
	 * If set to <code>true</code>, the client will log all requests and all responses. This is probably not a good production setting since it will result in a lot of extra logging, but it can be
	 * useful for troubleshooting.
	 * 
	 * @param theLogRequestAndResponse
	 *           Should requests and responses be logged
	 * @deprecated Use LoggingInterceptor as a client interceptor registered to your
	 *             client instead, as this provides much more fine-grained control over what is logged. This
	 *             method will be removed at some point (deprecated in HAPI 1.6 - 2016-06-16)
	 */
	@Deprecated
	void setLogRequestAndResponse(boolean theLogRequestAndResponse);

	/**
	 * Send a transaction (collection of resources) to the server to be executed as a single unit
	 */
	ITransaction transaction();

	/**
	 * Remove an intercaptor that was previously registered using {@link IRestfulClient#registerInterceptor(Object)}
	 */
	@Override
	void unregisterInterceptor(Object theInterceptor);

	/**
	 * Fluent method for the "update" operation, which updates a resource instance on the server
	 */
	IUpdate update();

	/**
	 * Implementation of the "instance update" method.
	 * 
	 * @param theId
	 *           The ID to update
	 * @param theResource
	 *           The new resource body
	 * @return An outcome containing the results and possibly the new version ID
	 * @deprecated Use {@link #update() update() fluent method} instead (deprecated in HAPI FHIR 3.0.0)
	 */
	@Deprecated
	MethodOutcome update(IdDt theId, IBaseResource theResource);

	/**
	 * Implementation of the "instance update" method.
	 * 
	 * @param theId
	 *           The ID to update
	 * @param theResource
	 *           The new resource body
	 * @return An outcome containing the results and possibly the new version ID
	 * @deprecated Use {@link #update() update() fluent method} instead (deprecated in HAPI FHIR 3.0.0)
	 */
	@Deprecated
	MethodOutcome update(String theId, IBaseResource theResource);

	/**
	 * Validate a resource
	 */
	IValidate validate();

	/**
	 * Implementation of the "type validate" method.
	 * 
	 * @param theResource
	 *           The resource to validate
	 * @return An outcome containing any validation issues
	 * @deprecated Use {@link #validate() validate() fluent method} instead (deprecated in HAPI FHIR 3.0.0)
	 */
	@Deprecated
	MethodOutcome validate(IBaseResource theResource);

	/**
	 * Implementation of the "instance vread" method. Note that this method expects <code>theId</code> to contain a resource ID as well as a version ID, and will fail if it does not.
	 * <p>
	 * Note that if an absolute resource ID is passed in (i.e. a URL containing a protocol and host as well as the resource type and ID) the server base for the client will be ignored, and the URL
	 * passed in will be queried.
	 * </p>
	 * 
	 * @param theType
	 *           The type of resource to load
	 * @param theId
	 *           The ID to load, including the resource ID and the resource version ID. Valid values include "Patient/123/_history/222", or "http://example.com/fhir/Patient/123/_history/222"
	 * @return The resource
	 * @deprecated Use {@link #read() read() fluent method} instead (deprecated in HAPI FHIR 3.0.0)
	 */
	@Deprecated
	<T extends IBaseResource> T vread(Class<T> theType, IdDt theId);

	/**
	 * Implementation of the "instance vread" method.
	 * 
	 * @param theType
	 *           The type of resource to load
	 * @param theId
	 *           The ID to load
	 * @param theVersionId
	 *           The version ID
	 * @return The resource
	 * @deprecated Use {@link #read() read() fluent method} instead (deprecated in HAPI FHIR 3.0.0)
	 */
	@Deprecated
	<T extends IBaseResource> T vread(Class<T> theType, String theId, String theVersionId);

}
