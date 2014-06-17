package ca.uhn.fhir.rest.client;

/*
 * #%L
 * HAPI FHIR Library
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
import ca.uhn.fhir.model.dstu.resource.Conformance;
import ca.uhn.fhir.model.primitive.DateTimeDt;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.gclient.IGetTags;
import ca.uhn.fhir.rest.gclient.IUntypedQuery;

public interface IGenericClient {

	/**
	 * Retrieves and returns the server conformance statement
	 */
	Conformance conformance();

	/**
	 * Implementation of the "type create" method.
	 * 
	 * @param theResource
	 *            The resource to create
	 * @return An outcome
	 */
	MethodOutcome create(IResource theResource);

	/**
	 * Implementation of the "transaction" method.
	 * 
	 * @param theResources
	 *            The resources to create/update in a single transaction
	 * @return A list of resource stubs (<b>these will not be fully populated</b>) containing IDs and other
	 *         {@link IResource#getResourceMetadata() metadata}
	 */
	List<IResource> transaction(List<IResource> theResources);

	/**
	 * Implementation of the "delete instance" method.
	 * 
	 * @param theType
	 *            The type of resource to delete
	 * @param theId
	 *            the ID of the resource to delete
	 * @return An outcome
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
	 */
	MethodOutcome delete(Class<? extends IResource> theType, String theId);

	/**
	 * Implementation of the "history instance" method.
	 * 
	 * @param theType
	 *            The type of resource to return the history for, or
	 *            <code>null<code> to search for history across all resources
	 * @param theId
	 *            The ID of the resource to return the history for, or <code>null</code> to search for all resource
	 *            instances. Note that if this param is not null, <code>theType</code> must also not be null
	 * @param theSince
	 *            If not null, request that the server only return resources updated since this time
	 * @param theLimit
	 *            If not null, request that the server return no more than this number of resources. Note that the
	 *            server may return less even if more are available, but should not return more according to the FHIR
	 *            specification.
	 * @return A bundle containing returned resources
	 */
	<T extends IResource> Bundle history(Class<T> theType, IdDt theIdDt, DateTimeDt theSince, Integer theLimit);

	/**
	 * Implementation of the "history instance" method.
	 * 
	 * @param theType
	 *            The type of resource to return the history for, or
	 *            <code>null<code> to search for history across all resources
	 * @param theId
	 *            The ID of the resource to return the history for, or <code>null</code> to search for all resource
	 *            instances. Note that if this param is not null, <code>theType</code> must also not be null
	 * @param theSince
	 *            If not null, request that the server only return resources updated since this time
	 * @param theLimit
	 *            If not null, request that the server return no more than this number of resources. Note that the
	 *            server may return less even if more are available, but should not return more according to the FHIR
	 *            specification.
	 * @return A bundle containing returned resources
	 */
	<T extends IResource> Bundle history(Class<T> theType, String theIdDt, DateTimeDt theSince, Integer theLimit);

	/**
	 * Implementation of the "instance read" method.
	 * 
	 * @param theType
	 *            The type of resource to load
	 * @param theId
	 *            The ID to load
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

	/**
	 * If set to <code>true</code>, the client will log all requests and all responses. This is probably not a good
	 * production setting since it will result in a lot of extra logging, but it can be useful for troubleshooting.
	 * 
	 * @param theLogRequestAndResponse
	 *            Should requests and responses be logged
	 */
	void setLogRequestAndResponse(boolean theLogRequestAndResponse);

	/**
	 * Fluent method for the "get tags" operation
	 */
	IGetTags getTags();

}
