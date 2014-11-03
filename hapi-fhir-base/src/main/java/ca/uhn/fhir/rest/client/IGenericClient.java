package ca.uhn.fhir.rest.client;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 University Health Network
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

import java.util.List;
import java.util.Map;

import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.base.resource.BaseConformance;
import ca.uhn.fhir.model.primitive.DateTimeDt;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.UriDt;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.api.IRestfulClient;
import ca.uhn.fhir.rest.gclient.ICreate;
import ca.uhn.fhir.rest.gclient.IDelete;
import ca.uhn.fhir.rest.gclient.IGetPage;
import ca.uhn.fhir.rest.gclient.IGetTags;
import ca.uhn.fhir.rest.gclient.ITransaction;
import ca.uhn.fhir.rest.gclient.IUntypedQuery;
import ca.uhn.fhir.rest.gclient.IUpdate;

public interface IGenericClient {
	
	/**
	 * Retrieves and returns the server conformance statement
	 */
	BaseConformance conformance();

	/**
	 * Fluent method for the "create" operation, which creates a new resource instance on the server
	 */
	ICreate create();

	/**
	 * Implementation of the "type create" method.
	 * 
	 * @param theResource
	 *            The resource to create
	 * @return An outcome
	 * @deprecated Use {@link #create() fluent method instead}. This method will be removed.
	 * 
	 */
	MethodOutcome create(IResource theResource);

	/**
	 * Fluent method for the "delete" operation, which performs a logical delete on a server resource
	 */
	IDelete delete();

	/**
	 * Implementation of the "delete instance" method.
	 * 
	 * @param theType
	 *            The type of resource to delete
	 * @param theId
	 *            the ID of the resource to delete
	 * @return An outcome
	 * @deprecated Use {@link #delete()) instead
	 */
	MethodOutcome delete(Class<? extends IResource> theType, IdDt theId);

	/**
	 * Implementation of the "delete instance" method.
	 * 
	 * @param theType
	 *            The type of resource to delete
	 * @param theId
	 *            the ID of the resource to delete
	 * @return An outcome
	 * @deprecated Use {@link #delete()) instead
	 */
	MethodOutcome delete(Class<? extends IResource> theType, String theId);

	/**
	 * Fluent method for the "get tags" operation
	 */
	IGetTags getTags();

	/**
	 * Implementation of the "history instance" method.
	 * 
	 * @param theType
	 *            The type of resource to return the history for, or <code>null<code> to search for history across all resources
	 * @param theId
	 *            The ID of the resource to return the history for, or <code>null</code> to search for all resource instances. Note that if this param is not null, <code>theType</code> must also not
	 *            be null
	 * @param theSince
	 *            If not null, request that the server only return resources updated since this time
	 * @param theLimit
	 *            If not null, request that the server return no more than this number of resources. Note that the server may return less even if more are available, but should not return more
	 *            according to the FHIR specification.
	 * @return A bundle containing returned resources
	 */
	<T extends IResource> Bundle history(Class<T> theType, IdDt theIdDt, DateTimeDt theSince, Integer theLimit);

	/**
	 * Implementation of the "history instance" method.
	 * 
	 * @param theType
	 *            The type of resource to return the history for, or <code>null<code> to search for history across all resources
	 * @param theId
	 *            The ID of the resource to return the history for, or <code>null</code> to search for all resource instances. Note that if this param is not null, <code>theType</code> must also not
	 *            be null
	 * @param theSince
	 *            If not null, request that the server only return resources updated since this time
	 * @param theLimit
	 *            If not null, request that the server return no more than this number of resources. Note that the server may return less even if more are available, but should not return more
	 *            according to the FHIR specification.
	 * @return A bundle containing returned resources
	 */
	<T extends IResource> Bundle history(Class<T> theType, String theIdDt, DateTimeDt theSince, Integer theLimit);

	/**
	 * Loads the previous/next bundle of resources from a paged set, using the link specified in the "link type=next" tag within the atom bundle.
	 * 
	 * @see Bundle#getLinkNext()
	 */
	IGetPage loadPage();

	/**
	 * Implementation of the "instance read" method. This method will only ever do a "read" for the latest version of a given resource instance, even if the ID passed in contains a version. If you
	 * wish to request a specific version of a resource (the "vread" operation), use {@link #vread(Class, IdDt)} instead.
	 * <p>
	 * Note that if an absolute resource ID is passed in (i.e. a URL containing a protocol and host as well as the resource type and ID) the server base for the client will be ignored, and the URL
	 * passed in will be queried.
	 * </p>
	 * 
	 * @param theType
	 *            The type of resource to load
	 * @param theId
	 *            The ID to load, including the resource ID and the resource version ID. Valid values include "Patient/123/_history/222", or "http://example.com/fhir/Patient/123/_history/222"
	 * @return The resource
	 */
	<T extends IResource> T read(Class<T> theType, IdDt theId);

	/**
	 * Implementation of the "instance read" method.
	 * 
	 * @param theType
	 *            The type of resource to load
	 * @param theId
	 *            The ID to load
	 * @return The resource
	 */
	<T extends IResource> T read(Class<T> theType, String theId);

	/**
	 * Perform the "read" operation (retrieve the latest version of a resource instance by ID) using an absolute URL.
	 * 
	 * @param theType
	 *            The resource type that is being retrieved
	 * @param theUrl
	 *            The absolute URL, e.g. "http://example.com/fhir/Patient/123"
	 * @return The returned resource from the server
	 */
	<T extends IResource> T read(Class<T> theType, UriDt theUrl);

	/**
	 * Perform the "read" operation (retrieve the latest version of a resource instance by ID) using an absolute URL.
	 * 
	 * @param theUrl
	 *            The absolute URL, e.g. "http://example.com/fhir/Patient/123"
	 * @return The returned resource from the server
	 */
	IResource read(UriDt theUrl);

	/**
	 * Register a new interceptor for this client. An interceptor can be used to add additional logging, or add security headers, or pre-process responses, etc.
	 */
	void registerInterceptor(IClientInterceptor theInterceptor);

	IUntypedQuery search();

	/**
	 * Implementation of the "instance search" method.
	 * 
	 * @param theType
	 *            The type of resource to load
	 * @param theParams
	 * @return
	 */
	<T extends IResource> Bundle search(Class<T> theType, Map<String, List<IQueryParameterType>> theParams);

	/**
	 * Perform the "search" operation using an absolute URL.
	 * 
	 * @param theType
	 *            The primary resource type that is being retrieved
	 * @param theUrl
	 *            The absolute URL, e.g. "http://example.com/fhir/Patient/123"
	 * @return The returned bundle from the server
	 */
	<T extends IResource> Bundle search(Class<T> theType, UriDt theUrl);

	Bundle search(UriDt theUrl);

	/**
	 * If set to <code>true</code>, the client will log all requests and all responses. This is probably not a good production setting since it will result in a lot of extra logging, but it can be
	 * useful for troubleshooting.
	 * 
	 * @param theLogRequestAndResponse
	 *            Should requests and responses be logged
	 */
	void setLogRequestAndResponse(boolean theLogRequestAndResponse);

	/**
	 * Send a transaction (collection of resources) to the server to be executed as a single unit
	 */
	ITransaction transaction();

	/**
	 * Implementation of the "transaction" method.
	 * 
	 * @param theResources
	 *            The resources to create/update in a single transaction
	 * @return A list of resource stubs (<b>these will not be fully populated</b>) containing IDs and other {@link IResource#getResourceMetadata() metadata}
	 * @deprecated Use {@link #transaction()}
	 * 
	 */
	List<IResource> transaction(List<IResource> theResources);

	/**
	 * Remove an intercaptor that was previously registered using {@link IRestfulClient#registerInterceptor(IClientInterceptor)}
	 */
	void unregisterInterceptor(IClientInterceptor theInterceptor);

	/**
	 * Fluent method for the "update" operation, which performs a logical delete on a server resource
	 */
	IUpdate update();

	/**
	 * Implementation of the "instance update" method.
	 * 
	 * @param theId
	 *            The ID to update
	 * @param theResource
	 *            The new resource body
	 * @return An outcome containing the results and possibly the new version ID
	 */
	MethodOutcome update(IdDt theIdDt, IResource theResource);

	/**
	 * Implementation of the "instance update" method.
	 * 
	 * @param theId
	 *            The ID to update
	 * @param theResource
	 *            The new resource body
	 * @return An outcome containing the results and possibly the new version ID
	 */
	MethodOutcome update(String theIdDt, IResource theResource);

	/**
	 * Implementation of the "type validate" method.
	 * 
	 * @param theId
	 *            The ID to validate
	 * @param theResource
	 *            The resource to validate
	 * @return An outcome containing any validation issues
	 */
	MethodOutcome validate(IResource theResource);

	/**
	 * Implementation of the "instance vread" method. Note that this method expects <code>theId</code> to contain a resource ID as well as a version ID, and will fail if it does not.
	 * <p>
	 * Note that if an absolute resource ID is passed in (i.e. a URL containing a protocol and host as well as the resource type and ID) the server base for the client will be ignored, and the URL
	 * passed in will be queried.
	 * </p>
	 * 
	 * @param theType
	 *            The type of resource to load
	 * @param theId
	 *            The ID to load, including the resource ID and the resource version ID. Valid values include "Patient/123/_history/222", or "http://example.com/fhir/Patient/123/_history/222"
	 * @return The resource
	 */
	<T extends IResource> T vread(Class<T> theType, IdDt theId);

	/**
	 * Implementation of the "instance vread" method.
	 * 
	 * @param theType
	 *            The type of resource to load
	 * @param theId
	 *            The ID to load
	 * @param theVersionId
	 *            The version ID
	 * @return The resource
	 * @deprecated Deprecated in 0.7 - IdDt can contain an ID and a version, so this class doesn't make a lot of sense
	 */
	<T extends IResource> T vread(Class<T> theType, IdDt theId, IdDt theVersionId);

	/**
	 * Implementation of the "instance vread" method.
	 * 
	 * @param theType
	 *            The type of resource to load
	 * @param theId
	 *            The ID to load
	 * @param theVersionId
	 *            The version ID
	 * @return The resource
	 */
	<T extends IResource> T vread(Class<T> theType, String theId, String theVersionId);

}
